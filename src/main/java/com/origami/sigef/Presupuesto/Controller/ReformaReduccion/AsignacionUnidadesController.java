/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Luis Suarez
 */
@Named(value = "asignacionUnidadesReudcionView")
@ViewScoped
public class AsignacionUnidadesController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
    @Inject
    private ReformaSuplementoIngresoService reformaService;
    @Inject
    private UnidadAdministrativaService unidadService;

    private LazyModel<ReformaIngresoSuplemento> lazyReforma;
    private ReformaIngresoSuplemento reforma;
    private CupoReduccion cupoReduccion;
    private List<String> usuarios;
    private String observaciones;
    private LazyModel<CupoReduccion> lazyOtrosReforma;

    private LazyModel<CupoReduccion> lazyCupos;
    List<CupoReduccion> verificacionUser;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                lazyReforma = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReforma.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                this.cupoReduccion = new CupoReduccion();
                verificacionUser = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void mostrarCupoReduccion(ReformaIngresoSuplemento r) {
        reforma = new ReformaIngresoSuplemento();
        reforma = r;

        List<CupoReduccion> lista = cupoReduccionService.getListaVerificacion(reforma);

        if (lista.isEmpty()) {

            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa data : unidades) {
                this.cupoReduccion = new CupoReduccion();
                this.cupoReduccion.setReforma(reforma);
                this.cupoReduccion.setUnidadAdministrativa(data);
                this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleUnidad(BigInteger.valueOf(r.getId()), data, reforma.getPeriodo()));
                this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificado(BigInteger.valueOf(r.getId()), data));
                cupoReduccion = cupoReduccionService.create(cupoReduccion);
            }

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion.setReforma(reforma);

            this.cupoReduccion.setOtros("D");
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivo(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivo(BigInteger.valueOf(r.getId())));
            cupoReduccion = cupoReduccionService.create(cupoReduccion);

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion.setReforma(reforma);
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivoAnexo(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivoAnexo(BigInteger.valueOf(r.getId())));
            this.cupoReduccion.setOtros("DA");
            cupoReduccion = cupoReduccionService.create(cupoReduccion);

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion.setReforma(reforma);
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDirectas(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDirectas(BigInteger.valueOf(r.getId())));
            this.cupoReduccion.setOtros("PD");
            cupoReduccion = cupoReduccionService.create(cupoReduccion);

            this.cupoReduccion = new CupoReduccion();
        } else {

            System.out.println("editando");
            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa data : unidades) {
                this.cupoReduccion = new CupoReduccion();
                this.cupoReduccion = cupoReduccionService.getDataUnidad(data, r);
                this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleUnidad(BigInteger.valueOf(r.getId()), data, reforma.getPeriodo()));
                this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificado(BigInteger.valueOf(r.getId()), data));
                cupoReduccionService.edit(cupoReduccion);
            }

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("D", r);
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivo(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivo(BigInteger.valueOf(r.getId())));
            cupoReduccionService.edit(cupoReduccion);

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("DA", r);
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivoAnexo(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivoAnexo(BigInteger.valueOf(r.getId())));
            cupoReduccionService.edit(cupoReduccion);

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("PD", r);
            this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDirectas(BigInteger.valueOf(r.getId()), reforma.getPeriodo()));
            this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDirectas(BigInteger.valueOf(r.getId())));
            cupoReduccionService.edit(cupoReduccion);
            this.cupoReduccion = new CupoReduccion();
        }

        lazyCupos = new LazyModel(CupoReduccion.class);
        lazyCupos.getFilterss().put("reforma:equal", reforma);
        lazyCupos.getFilterss().put("otros:equal", null);
        lazyCupos.getSorteds().put("id", "ASC");
        lazyOtrosReforma = new LazyModel(CupoReduccion.class);
        lazyOtrosReforma.getFilterss().put("reforma:equal", reforma);
        lazyOtrosReforma.getFilterss().put("unidadAdministrativa:equal", null);
        lazyOtrosReforma.getSorteds().put("id", "ASC");
        PrimeFaces.current().executeScript("PF('DloCupos').show()");
        PrimeFaces.current().ajax().update(":formCupos");

    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return reformaService.getTotalSuplemento(r);
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return reformaService.getTotalReduccionReforma(r);
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<String> filtrarLista(CupoReduccion c) {
        return cupoReduccionService.getListaRoles(c.getUnidadAdministrativa());

    }

    public void traermeUnidadeasCupos() {
        List<CupoReduccion> entidadCupos = new ArrayList<>();
        usuarios = new ArrayList<>();
        entidadCupos = cupoReduccionService.getCuposDetalle(reforma);

        for (CupoReduccion item : entidadCupos) {

            if (!isNullOrEmpty(item.getUsuario())) {
                usuarios.add(item.getUsuario());

            }

        }

    }

    public void editarResponsable(CupoReduccion c) {
        if (c.getResponsable() == null) {
            c.setResponsable(null);
            c.setUsuario(null);
        } else {
            c.setUsuario(cupoReduccionService.getRolesUsuariosNameUser(c.getUnidadAdministrativa(), c.getResponsable()));
            if(c.getUsuario()==null){
                c.setResponsable(null);
                JsfUtil.addErrorMessage("ERROR!!!", "No tiene un usuario relacionado");
                return;
            }
            if (c.getUsuario().toUpperCase().equals("ADMIN")) {
                c.setUsuario(null);
                c.setResponsable(null);
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede seleccionar este tipo de responsable");
            }
        }
        cupoReduccionService.edit(c);
        PrimeFaces.current().ajax().update("messages");
        if (c.getResponsable() != null) {
            JsfUtil.addInformationMessage("Información", "Editado con éxito");
        }
//        if (c.getResponsable() == null || c.getResponsable().length() == 0) {
//            c.setUsuario(null);
//            c.setResponsable(null);
//        } else {
//            c.setUsuario(cupoReduccionService.getRolesUsuariosNameUser(c.getUnidadAdministrativa(), c.getResponsable()));
//        }
//        cupoReduccionService.edit(c);
//        PrimeFaces.current().ajax().update("messages");
//        JsfUtil.addInformationMessage("Información", "Editado con éxito");
    }

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reforma = new ReformaIngresoSuplemento();
        reforma = r;
        traermeUnidadeasCupos();
        List<CupoReduccion> verificar = cupoReduccionService.getCuposDetalle(r);

        if (verificar.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Tienes que asignar responsables");
            return;

        }

        boolean verifi = false;
        for (CupoReduccion item : verificar) {
            if (item.getUsuario() != null) {
                verifi = true;
                break;
            }

        }

        if (!verifi) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Tienes que asignar responsables");
            return;
        }

        verificacionUser = new ArrayList<>();
        List<CupoReduccion> getlista = cupoReduccionService.getRevisionAsignacion(r);
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

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            traermeUnidadeasCupos();
            getParamts().put("usuario", this.session.getNameUser());
            this.getParamts().put("usuarios", usuarios);

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            reforma = new ReformaIngresoSuplemento();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Setter an Getter">
    public LazyModel<ReformaIngresoSuplemento> getLazyReforma() {
        return lazyReforma;
    }

    public void setLazyReforma(LazyModel<ReformaIngresoSuplemento> lazyReforma) {
        this.lazyReforma = lazyReforma;
    }

    public List<CupoReduccion> getVerificacionUser() {
        return verificacionUser;
    }

    public void setVerificacionUser(List<CupoReduccion> verificacionUser) {
        this.verificacionUser = verificacionUser;
    }

    public LazyModel<CupoReduccion> getLazyOtrosReforma() {
        return lazyOtrosReforma;
    }

    public void setLazyOtrosReforma(LazyModel<CupoReduccion> lazyOtrosReforma) {
        this.lazyOtrosReforma = lazyOtrosReforma;
    }

    public LazyModel<CupoReduccion> getLazyCupos() {
        return lazyCupos;
    }

    public void setLazyCupos(LazyModel<CupoReduccion> lazyCupos) {
        this.lazyCupos = lazyCupos;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    public CupoReduccion getCupoReduccion() {
        return cupoReduccion;
    }

    public void setCupoReduccion(CupoReduccion cupoReduccion) {
        this.cupoReduccion = cupoReduccion;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>

}
