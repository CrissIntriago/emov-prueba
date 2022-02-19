/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ProcesoServidor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.ProcesoServidorService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import com.origami.sigef.talentohumano.services.detalleRegistroService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI1
 */
@Named(value = "movimientoBienesBajaView")
@ViewScoped
public class MovimientoBienesBajaBeans extends BpmnBaseRoot implements Serializable {

    private LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel;

    /*Objetos*/
    private List<ActivoFijoServidor> bienesItemList;
    private ActivoFijoCustodio activoFijoCustodio;

    /*Inject Services*/
    @Inject
    private ActivoFijoServidorService activoFijoServidorService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;
    @Inject
    private ProcesoServidorService procesoSerService;
    @Inject
    private detalleBancoServices bancoService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private detalleRegistroService detalleRegistroService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private UsuarioService usuarioService;

    private OpcionBusqueda opcionBusqueda;

    private String observaciones;

    @Inject
    private ClienteService clienteService;
    /*Variables*/
    private String DescripcionDevolucion;
    private Servidor servidorTramite;

    @PostConstruct
    public void initialize() {
        if (!JsfUtil.isAjaxRequest()) {
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
        }
        this.opcionBusqueda = new OpcionBusqueda();
        Long tramiteNumero = tramite.getNumTramite();
        ProcesoServidor servidor = procesoSerService.findProcesoServidorByNTramite(new BigInteger("" + tramiteNumero));
        servidorTramite = servidor.getServidorP();
        this.activoFijoCustodio = new ActivoFijoCustodio();
        this.activoFijoCustodioLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.activoFijoCustodioLazyModel.getSorteds().put("id", "ASC");
        this.activoFijoCustodioLazyModel.getFilterss().put("estado", true);
        this.activoFijoCustodioLazyModel.getFilterss().put("actaGuardalmacen", false);
        this.activoFijoCustodioLazyModel.getFilterss().put("servidor", servidor.getServidorP());
        this.bienesItemList = new ArrayList<>();
//        System.out.println("tiene usuario--->" + usuarioService.findByFuncionario(servidorTramite));
    }

    public void form(ActivoFijoCustodio activoFijoCustodio) {
        if (activoFijoCustodio != null) {
            this.activoFijoCustodio = activoFijoCustodio;
            restablecerFormulario();
            this.bienesItemList = activoFijoServidorService.getBienesAsignados(activoFijoCustodio);
        } else {
            this.activoFijoCustodio = new ActivoFijoCustodio();
            this.activoFijoCustodio.setDescripcion("De conformidad con los artículos 41, 44 del Reglamento administración y control de bienes del sector publico vigente a la fecha....");
        }
        PrimeFaces.current().executeScript("PF('descripcionDevolucionDLG').show()");
        PrimeFaces.current().ajax().update(":descripcionDevolucionPanel");
    }

    public void darDeBajaCustodio() {
        if (DescripcionDevolucion == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe ingresar la descripción/motivo de la devolución de los bienes");
            return;
        }
        short cero = 0;
        activoFijoCustodio.setCantidadBienes(cero);
        activoFijoCustodio.setEstado(Boolean.FALSE);
        activoFijoCustodio.setUsuarioModificacion(userSession.getNameUser());
        activoFijoCustodio.setFechaModificacion(new Date());
        activoFijoCustodioService.edit(activoFijoCustodio);
        List<ActivoFijoServidor> ListadoDeBaja = activoFijoServidorService.getBienesAsignados(activoFijoCustodio);
        /*DAR DE BAJA A LOS SERVICIOS QUE ESTABAN ASIGNADOS A ESTE CUSTODIO*/
        for (ActivoFijoServidor bienes : ListadoDeBaja) {
            bienes.setEstado(Boolean.FALSE);
            bienes.setAsignado(Boolean.FALSE);
            bienes.setFechaDevolucion(new Date());
            bienes.setObservacionFinal("SERVIDOR DADO DE BAJA");
            activoFijoServidorService.edit(bienes);
        }
        List<Long> aux = actaGuardalmacen(ListadoDeBaja);
        PrimeFaces.current().executeScript("PF('descripcionDevolucionDLG').hide()");
        imprimirDevolucion(activoFijoCustodio, aux);
        restablecerFormulario();
        JsfUtil.addSuccessMessage("ASIGNACIÓN DE CUSTODIO", "Se ha dado de baja correctamente");
    }

    private List<Long> actaGuardalmacen(List<ActivoFijoServidor> bienesList) {
        /*Volvemos a colocar los bienes al guardalmacén activo*/
        ActivoFijoCustodio nuevaActaGuardalmacen = new ActivoFijoCustodio();
        nuevaActaGuardalmacen.setServidor(clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen));
        nuevaActaGuardalmacen.setDescripcion("ESTOS BIENES SON ASIGNADOS AL SERVIDOR CON CARGO DE GUARDALMACEN");
        nuevaActaGuardalmacen.setCantidadBienes((short) bienesList.size());
        nuevaActaGuardalmacen.setFechaCreacion(new Date());
        nuevaActaGuardalmacen.setUsuarioCreacion(userSession.getNameUser());
        nuevaActaGuardalmacen.setEstado(Boolean.TRUE);
        nuevaActaGuardalmacen.setFechaEntrega(new Date());
        ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.TRUE);
        if (ultimaActa != null) {
            nuevaActaGuardalmacen.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
        } else {
            nuevaActaGuardalmacen.setNumeroActa(BigInteger.valueOf(1));
        }
        nuevaActaGuardalmacen.setActaGuardalmacen(Boolean.TRUE);
        nuevaActaGuardalmacen = activoFijoCustodioService.create(nuevaActaGuardalmacen);
        List<Long> aux = new ArrayList<>();
        for (ActivoFijoServidor bienes : bienesList) {
            aux.add(bienes.getId());
            ActivoFijoServidor bienDevuelto = new ActivoFijoServidor();
            bienDevuelto.setActivoFijoCustodio(nuevaActaGuardalmacen);
            bienDevuelto.setBienesItem(bienes.getBienesItem());
            bienDevuelto.setAsignado(Boolean.TRUE);
            bienDevuelto.setEstado(Boolean.FALSE);
            bienDevuelto.setFechaAsignacion(new Date());
            bienDevuelto.setObservacionInicial("");
            bienDevuelto.setObservacionFinal("");
            bienDevuelto.setEstadoBien(bienes.getBienesItem().getEstadoBien().getTexto());
            bienDevuelto = activoFijoServidorService.create(bienDevuelto);
            System.out.println("BIENES " + bienDevuelto);
        }
        return aux;
    }

    public void imprimirDevolucion(ActivoFijoCustodio activoAsignado, List<Long> listaDevolucion) {
        Servidor guardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        servletSession.addParametro("id_custodio", activoAsignado.getId());
        servletSession.addParametro("descripcion_devolucion", getDescripcionDevolucion());
        servletSession.addParametro("nombre_guardalmacen", guardalmacen.getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_guardalmacen", guardalmacen.getPersona().getIdentificacion());
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.addParametro("bienes", listaDevolucion);
        if (guardalmacen.getDistributivo() != null) {
            if (guardalmacen.getDistributivo().getCargo() != null) {
                servletSession.addParametro("cargo_guardalmacen", guardalmacen.getDistributivo().getCargo().getNombreCargo());
            } else {
                servletSession.addParametro("cargo_guardalmacen", "");
            }
        } else {
            servletSession.addParametro("cargo_guardalmacen", "");
        }
        servletSession.setNombreReporte("DevolucionDeBienes");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void restablecerFormulario() {
        bienesItemList = new ArrayList<>();
        DescripcionDevolucion = null;
    }

    public void disableUser() {
        List<DetalleBanco> listBancos = bancoService.findListBancoByServidor(servidorTramite);
        if (!listBancos.isEmpty()) {
            for (DetalleBanco item : listBancos) {
                item.setEstado(Boolean.FALSE);
                bancoService.edit(item);
            }
        }
        Servidor servidorSalida = new Servidor();
        servidorSalida = servidorTramite;
        Distributivo disSalida;
        disSalida = distributivoService.findByDistributivo(servidorTramite);
        disSalida.setServidorPublico(null);
        distributivoService.edit(disSalida);
        DetalleRegistro detalleRegistro = detalleRegistroService.getDetalleRegistroByServidor(servidorTramite);
        detalleRegistro.setEstado(false);
        detalleRegistroService.edit(detalleRegistro);
//        usuario.getFuncionario().setEstado(Boolean.FALSE);
        servidorSalida.setEstado(false);
        servidorService.edit(servidorSalida);
//        PrimeFaces.current().ajax().update("frmMain");
        JsfUtil.addInformationMessage("Info", "Servidos dado de Baja Correctamente");
    }

    public void completar() {
        try {
            observacion.setObservacion(observaciones);
//            getParamts().put("usuarioSistemas", clienteService.getrolsUser(RolUsuario.responsableSistema));
//            getParamts().put("idServidor", session.getUserId());
            if (usuarioService.findByFuncionario(servidorTramite)) {
                getParamts().put("aprobado", 1);
                getParamts().put("usuarioSistemas", clienteService.getrolsUser(RolUsuario.responsableSistema));
            } else {
                getParamts().put("aprobado", 0);
                disableUser();
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void ingObservacion() {
//        System.out.println(activoFijoCustodioLazyModel.getRowCount());
        if (activoFijoCustodioLazyModel.getRowCount() >= 1) {
            JsfUtil.addErrorMessage("ELIMINACIÓN DE BIENES", "Debe dar de baja a Todos los Bienes de este Servidor");
            return;
        }
        observacion.setObservacion(observaciones);
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        observacion.setIdTramite(tramite);
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<ActivoFijoCustodio> getActivoFijoCustodioLazyModel() {
        return activoFijoCustodioLazyModel;
    }

    public void setActivoFijoCustodioLazyModel(LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel) {
        this.activoFijoCustodioLazyModel = activoFijoCustodioLazyModel;
    }

    public Servidor getServidorTramite() {
        return servidorTramite;
    }

    public void setServidorTramite(Servidor servidorTramite) {
        this.servidorTramite = servidorTramite;
    }

    public String getDescripcionDevolucion() {
        return DescripcionDevolucion;
    }

    public void setDescripcionDevolucion(String DescripcionDevolucion) {
        this.DescripcionDevolucion = DescripcionDevolucion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>
}
