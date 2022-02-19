/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.CupoPresupuesto;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * @author ORIGAMIEC
 */
@Named(value = "cupoPresupuestoView")
@ViewScoped
public class CupoPresupuestoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private UserSession user;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    private CupoPresupuesto cupo;
    private UnidadAdministrativa unidad;
    private String tittle;
    private short periodo;
    private List<MasterCatalogo> masterList;
    private List<UnidadAdministrativa> listaUnidades;

    private LazyModel<CupoPresupuesto> lazyUnidades;
    private BigDecimal totalCupo, cupoAsignado;
    private LazyModel<CupoPresupuesto> lazyOtros;

    private String observaciones;
    private List<Usuarios> usuarios;
    private ArrayList<String> usuariosScoma;
    private List<UnidadAdministrativa> unidadesConCupo;
    private boolean bloqueo;
    private List<String> listaRoles;
    private boolean btnCompletarTarea;
    private List<CupoPresupuesto> verificacionUser;

  

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.cupo = new CupoPresupuesto();
                this.unidad = new UnidadAdministrativa();
                this.tittle = "Cupos Presupuesto";
                this.masterList = new ArrayList<>();
                this.masterList = periodoService.findAllCatalogoByAnioAndTipo("CP");
                this.listaUnidades = new ArrayList<>();
                Calendar c = new GregorianCalendar();
                String anio = Integer.toString(c.get(Calendar.YEAR));
                periodo = Short.valueOf(anio);

                lazyUnidades = new LazyModel(CupoPresupuesto.class);
                lazyUnidades.getFilterss().put("otros:equal", null);
                lazyUnidades.getFilterss().put("periodo:equal", periodo);
                lazyUnidades.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyUnidades.getSorteds().put("id", "ASC");

                lazyOtros = new LazyModel(CupoPresupuesto.class);
                lazyOtros.getFilterss().put("unidadAdministrativa:equal", null);
                lazyOtros.getFilterss().put("periodo:equal", periodo);
                lazyOtros.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyOtros.getSorteds().put("id", "ASC");
                updateValoresCupo();
                this.usuarios = new ArrayList();
                usuariosScoma = new ArrayList<>();
                unidadesConCupo = new ArrayList();
                desactivarAprobado(periodo);
                listaRoles = new ArrayList<>();
                btnCompletarTarea = true;
                verificacionUser = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

   

    public void desactivarButoon(Short periodo) {
        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, false, true);

        if (li.size() > 0) {
            btnCompletarTarea = true;
        } else {
            btnCompletarTarea = false;
        }
    }

    public void desactivarAprobado(Short periodo) {
        boolean verificar = true;
        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, true, true);

        if (li.size() > 0) {
            verificar = true;
        } else {
            verificar = false;
        }
        setBloqueo(verificar);
    }

    public void updateValoresCupo() {
        totalCupo = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        totalCupo = cupoPresupuestoService.getValorCupoIngreso(periodo);
        cupoAsignado = cupoPresupuestoService.getValorCupoAsinado(BigInteger.valueOf(tramite.getNumTramite()), periodo);
    }

    public void buscar() {
        List<CupoPresupuesto> lista = cupoPresupuestoService.getVerificacion(BigInteger.valueOf(tramite.getNumTramite()));
        if (lista.isEmpty()) {
            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa ud : unidades) {
                cupo = new CupoPresupuesto();
                cupo.setUnidadAdministrativa(ud);
                cupo.setPeriodo(periodo);
                cupo.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
                cupo = cupoPresupuestoService.create(cupo);
            }
            this.cupo = new CupoPresupuesto();
            this.cupo.setOtros("D");
            cupo.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            this.cupo.setPeriodo(periodo);
            cupo = cupoPresupuestoService.create(cupo);
            this.cupo = new CupoPresupuesto();
            cupo.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            this.cupo.setOtros("DA");
            this.cupo.setPeriodo(periodo);
            cupo = cupoPresupuestoService.create(cupo);

            this.cupo = new CupoPresupuesto();
            cupo.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            this.cupo.setOtros("PD");
            this.cupo.setPeriodo(periodo);
            cupo = cupoPresupuestoService.create(cupo);

            this.cupo = new CupoPresupuesto();

        }
        desactivarAprobado(periodo);
        lazyUnidades = new LazyModel(CupoPresupuesto.class);
        lazyUnidades.getFilterss().put("otros:equal", null);
        lazyUnidades.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
        lazyUnidades.getSorteds().put("id", "ASC");
        lazyOtros = new LazyModel(CupoPresupuesto.class);
        lazyOtros.getFilterss().put("unidadAdministrativa:equal", null);
        lazyOtros.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
        lazyOtros.getSorteds().put("id", "ASC");
        updateValoresCupo();
    }

    public List<String> filtrarLista(CupoPresupuesto c) {
        return cupoPresupuestoService.getListaRoles(c.getUnidadAdministrativa());
    }

    public void asingarCupo(CupoPresupuesto p) {
        cupo = new CupoPresupuesto();
        cupo = p;
        BigDecimal cupoGlobalAsignado = cupoPresupuestoService.getValorCupoAsinado(BigInteger.valueOf(tramite.getNumTramite()), periodo);
        BigDecimal valorActual = cupoPresupuestoService.getValorActual(p);
        if (cupoGlobalAsignado.add(cupo.getMontoCupo()).subtract(valorActual).doubleValue() > totalCupo.doubleValue()) {
            cupo.setMontoCupo(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Infromacion", "No tienes suficiente fondo para brindar esa cantidad ");
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Infromacion", "El cupo se asigno correctamente");
        }
        cupoPresupuestoService.edit(cupo);
        updateValoresCupo();
    }

    public void abriDlogo() {
        List<CupoPresupuesto> listaVerificadora = cupoPresupuestoService.getVerificadorCupos(BigInteger.valueOf(tramite.getNumTramite()));
        boolean bandera = false;
        if (listaVerificadora.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE ENVIAR LA TAREA SI NO HAY CUPOS");
            return;
        }
        for (CupoPresupuesto cupos : listaVerificadora) {
            if (cupos.getMontoCupo().doubleValue() == 0) {
                bandera = true;
            } else {
                bandera = false;
                break;
            }
        }
        if (bandera) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ASIGNE CUPOS Y RESPONSABLES");
            return;
        }
        for (CupoPresupuesto cupo : listaVerificadora) {
            if (cupo.getMontoCupo().doubleValue() > 0) {
                if (cupo.getUserNameResponsable() == null) {
                    bandera = true;
                    break;
                }
            }
        }
        if (bandera) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "HAY CUPOS EN UNIDADES PERO NO HA ASIGNADO UN RESPOSABLES ...!");
            return;
        }
        bandera = false;
        for (CupoPresupuesto cupos : listaVerificadora) {
            if (cupos.getResponsable() == null) {
                bandera = true;
            } else {
                bandera = false;
                break;
            }
        }
        if (bandera) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO HAY NIGUN RESPONSABLE");
            return;
        }
        verificacionUser = new ArrayList<>();
        List<CupoPresupuesto> getlista = cupoPresupuestoService.getRevisionAsignacion(BigInteger.valueOf(tramite.getNumTramite()));
        verificacionUser = getlista;
        if (!getlista.isEmpty()) {
            PrimeFaces.current().executeScript("PF('dlgresponsables').show()");
            PrimeFaces.current().ajax().update(":frmDlgresponsables");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "Verifique la asignaciòn a responsabeles ya que hay usuario que se le repsonsabiliza en varias unidades");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public void traermeUnidadeasCupos() {
        List<CupoPresupuesto> entidadCupos = new ArrayList<>();
        usuariosScoma = new ArrayList<>();
        entidadCupos = cupoPresupuestoService.getEntidadConCupos(BigInteger.valueOf(tramite.getNumTramite()));
        for (CupoPresupuesto item : entidadCupos) {
            if (!isNullOrEmpty(item.getUserNameResponsable())) {
                usuariosScoma.add(item.getUserNameResponsable());
            }
        }
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            traermeUnidadeasCupos();
            getParamts().put("usuario", this.session.getNameUser());
            this.getParamts().put("usuarios", usuariosScoma);

            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void editarResponsable(CupoPresupuesto c) {
        if (c.getResponsable() == null) {
            c.setResponsable(null);
            c.setUserNameResponsable(null);
        } else {
            c.setUserNameResponsable(cupoPresupuestoService.getRolesUsuariosNameUser(c.getUnidadAdministrativa(), c.getResponsable()));
            if (c.getUserNameResponsable() == null) {
                c.setResponsable(null);
                JsfUtil.addErrorMessage("ERROR!!!", "No tiene un usuario relacionado");
                return;
            }
            if (c.getUserNameResponsable().toUpperCase().equals("ADMIN")) {
                c.setUserNameResponsable(null);
                c.setResponsable(null);
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede seleccionar este tipo de responsable");
            }
        }
        cupoPresupuestoService.edit(c);
        PrimeFaces.current().ajax().update("messages");
        if (c.getResponsable() != null) {
            JsfUtil.addInformationMessage("Información", "Editado con éxito");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public List<CupoPresupuesto> getVerificacionUser() {
        return verificacionUser;
    }

    public void setVerificacionUser(List<CupoPresupuesto> verificacionUser) {
        this.verificacionUser = verificacionUser;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<MasterCatalogo> getMasterList() {
        return masterList;
    }

    public void setMasterList(List<MasterCatalogo> masterList) {
        this.masterList = masterList;
    }

    public List<UnidadAdministrativa> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadAdministrativa> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public LazyModel<CupoPresupuesto> getLazyUnidades() {
        return lazyUnidades;
    }

    public void setLazyUnidades(LazyModel<CupoPresupuesto> lazyUnidades) {
        this.lazyUnidades = lazyUnidades;
    }

    public LazyModel<CupoPresupuesto> getLazyOtros() {
        return lazyOtros;
    }

    public void setLazyOtros(LazyModel<CupoPresupuesto> lazyOtros) {
        this.lazyOtros = lazyOtros;
    }

    public CupoPresupuesto getCupo() {
        return cupo;
    }

    public void setCupo(CupoPresupuesto cupo) {
        this.cupo = cupo;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public List<String> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<String> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public boolean isBtnCompletarTarea() {
        return btnCompletarTarea;
    }

    public void setBtnCompletarTarea(boolean btnCompletarTarea) {
        this.btnCompletarTarea = btnCompletarTarea;
    }

//</editor-fold>
}
