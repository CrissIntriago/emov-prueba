/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.certificadoPAPP;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ProductoProcesoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "analisisProcesoView")
@ViewScoped
public class AnalisisProcesoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private ProductoProcesoService productoProcesoService;

    private OpcionBusqueda busqueda;
    private Distributivo d;
    private List<UploadedFile> files;
    private CatalogoItem catalogoItem;
    private CatalogoItem catalogoItem2;
    private CatalogoItem catalogoItem3;
    private List<MasterCatalogo> periodos;
    private CatalogoItem catalogoItem4;
    private String observaciones;
    private BigInteger numTramite;
    private MasterCatalogo planProgramaProyecto;
    private PappProceso pappProceso;
    private List<PappProceso> listaPlanPapp;
    private List<ProductoProceso> listProductoPro;
    private static final Logger LOG = Logger.getLogger(PlanAnualProgramaProyectoService.class.getName());

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        busqueda = new OpcionBusqueda();
        catalogoItem = catalogoItemService.getEstadoRol("PROANAACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PRORECHACT");
        catalogoItem3 = catalogoItemService.getEstadoRol("PRORECACT");
        catalogoItem4 = catalogoItemService.getEstadoRol("PROEMIACT");
        pappProceso = new PappProceso();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});;
//        planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
//        d = clienteService.getuusuarioLogeado(userSession.getNameUser());
//        if (d != null) {
//            listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem3, numTramite);
//        }
        if (periodos != null) {
            int indice = periodos.size();
            if (!periodos.isEmpty() && indice == 1) {
                busqueda.setAnio(periodos.get(0).getAnio());
            } else {
                busqueda.setAnio(periodos.get(indice - 1).getAnio());
            }
        }
        cargarXanio();
    }

    public void cargarXanio() {
        if (busqueda.getAnio() != null) {
            planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
            d = clienteService.getuusuarioLogeado(userSession.getNameUser());
//            if (d != null) {
            listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem3, numTramite, busqueda.getAnio());
//            }
//        } else {
//            listaPlanPapp = new ArrayList<>();
        }
    }

    public void continuarTarea(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem4, numTramite, plan.getActividadOperativa());
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void editarValorSolicitado(ProductoProceso da) {
        if (da.getSolicitado().setScale(2, RoundingMode.HALF_UP).compareTo(getMontoDisponible(da).setScale(2, RoundingMode.HALF_UP)) == 1) {
            da.setSolicitado(productoProcesoService.valorProductoProceso(da));
            productoProcesoService.edit(da);
            JsfUtil.addWarningMessage("", "El monto a solicitar no puede ser mayor al monot disponible");

        } else {
            JsfUtil.addInformationMessage("", "Transacción Correcta");
            productoProcesoService.edit(da);
        }
    }

    public BigDecimal getMontoDisponible(ProductoProceso da) {
        if (da.getProducto() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal valor = pappService.getValorAproabdo(da.getProducto());
        return da.getProducto().getMontoReformada().subtract(valor);
    }

    public void completarTarea(int estado) {
        try {
            if (estado == 0) {
                rechazarProductos();
                pappService.remove(pappProceso);
               // this.pappProceso.setEstadoProceso(catalogoItem2);
                getParamts().put("usuario_1", pappProceso.getUsuarioSolicita());
            } else {
                this.pappProceso.setEstadoProceso(catalogoItem);
                if (listProductoPro != null && !listProductoPro.isEmpty()) {
                    for (ProductoProceso item : listProductoPro) {
                        if(item.getSolicitado() == null){
                            JsfUtil.addWarningMessage("", "Debe ingresar solicitado");
                            return;
                        }
                        if (item.getSolicitado().setScale(2, RoundingMode.HALF_UP).compareTo(getMontoDisponible(item).setScale(2, RoundingMode.HALF_UP)) == 1) {
                            JsfUtil.addWarningMessage("", "No se puede aprobar esta tarea porque no hay monto disponible suficiente para cubrir lo solicitado");
                            return;
                        }
                        item.setEstado(new CatalogoItem(416L));
                        productoProcesoService.edit(item);
                    }
                }
                pappService.edit(this.pappProceso);
                getParamts().put("usuario_4", clienteService.getrolsUser(RolUsuario.analistaPlanificacion));
            }
            observacion.setObservacion(observaciones);
            getParamts().put("aprobado", estado);
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
            this.pappProceso = new PappProceso();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void openDialogAmortizacion(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem4, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void rechazarProductos() {
        if (!listProductoPro.isEmpty()) {
            for (ProductoProceso p : listProductoPro) {
                productoProcesoService.remove(p);
            }
        }
    }

    public String usuarioSolicita(PappProceso plan) {
        Usuarios user = clienteService.getUsuarioServidor(plan.getUsuarioSolicita());
        if (user != null && user.getFuncionario() != null && user.getFuncionario().getPersona() != null) {
            return user.getFuncionario().getPersona().getNombreCompleto();
        }
        return "";

    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public MasterCatalogo getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(MasterCatalogo planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

    public PappProceso getPappProceso() {
        return pappProceso;
    }

    public void setPappProceso(PappProceso pappProceso) {
        this.pappProceso = pappProceso;
    }

    public List<PappProceso> getListaPlanPapp() {
        return listaPlanPapp;
    }

    public void setListaPlanPapp(List<PappProceso> listaPlanPapp) {
        this.listaPlanPapp = listaPlanPapp;
    }

    public List<ProductoProceso> getListProductoPro() {
        return listProductoPro;
    }

    public void setListProductoPro(List<ProductoProceso> listProductoPro) {
        this.listProductoPro = listProductoPro;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

}
