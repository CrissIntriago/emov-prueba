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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
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
@Named(value = "recepcionSolicitudView")
@ViewScoped
public class RecepcionSolicitudControlles extends BpmnBaseRoot implements Serializable {

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private UserSession session;
    @Inject
    private ClienteService clienteService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private List<MasterCatalogo> periodos;
    private List<PappProceso> listaPlanPapp;
    private List<ProductoProceso> listProductoPro;
    private OpcionBusqueda busqueda;
    private Distributivo d;
    private List<UploadedFile> files;

    private CatalogoItem catalogoItem;
    private CatalogoItem catalogoItem2;
    private BigInteger numTramite;
    private MasterCatalogo planProgramaProyecto;
    private PappProceso pappProceso;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        busqueda = new OpcionBusqueda();
        catalogoItem = catalogoItemService.getEstadoRol("PRORECACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PROEMIACT");
        pappProceso = new PappProceso();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});;
//        planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
//        d = clienteService.getuusuarioLogeado(session.getNameUser());
//        if (d != null) {
//            listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem2, numTramite);
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
            System.out.println("anio: " + busqueda.getAnio());
            planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
            d = clienteService.getuusuarioLogeado(session.getNameUser());
//            if (d != null) {
            listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem2, numTramite, busqueda.getAnio());
//            }
//        } else {
//            listaPlanPapp = new ArrayList<>();
        }
    }

    public BigDecimal getMontoDisponible(ProductoProceso da) {
        if (da.getProducto() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal valor = pappService.getValorAproabdo(da.getProducto());
        return da.getProducto().getMontoReformada().subtract(valor);
    }

    public void finalizarTarea(PappProceso plan) {
        pappProceso = plan;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            pappProceso.setEstadoProceso(catalogoItem);
            pappService.edit(pappProceso);
            getParamts().put("usuario_3", clienteService.getrolsUser(RolUsuario.analistaPlanificacion));
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
            pappProceso = new PappProceso();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public String usuarioSolicita(PappProceso plan) {
        Usuarios user = clienteService.getUsuarioServidor(plan.getUsuarioSolicita());
        if (user != null && user.getFuncionario() != null && user.getFuncionario().getPersona() != null) {
            return user.getFuncionario().getPersona().getNombreCompleto();
        }
        return "";

    }

    public void openDialogAmortizacion(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem2, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<PappProceso> getListaPlanPapp() {
        return listaPlanPapp;
    }

    public void setListaPlanPapp(List<PappProceso> listaPlanPapp) {
        this.listaPlanPapp = listaPlanPapp;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<ProductoProceso> getListProductoPro() {
        return listProductoPro;
    }

    public void setListProductoPro(List<ProductoProceso> listProductoPro) {
        this.listProductoPro = listProductoPro;
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

}
