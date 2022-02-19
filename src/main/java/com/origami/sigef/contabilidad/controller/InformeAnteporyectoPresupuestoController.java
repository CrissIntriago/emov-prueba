/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
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

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "informeAnteproyectoView")
@ViewScoped
public class InformeAnteporyectoPresupuestoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaService;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;

    private String observaciones;
    private Short periodoCosnulatdo;
    private CatalogoProformaPresupuesto catalogoProformaEgresos;
    private CatalogoProformaPresupuesto catalogoProformaIngresos;
    private MasterCatalogo mc;
    private List<MasterCatalogo> periodosEgresos;
    private List<MasterCatalogo> periodosIngresos;
    private CatalogoProformaPresupuesto catalogoProAdicionalEgresos;
    private CatalogoProformaPresupuesto catalogoProAdicionalIngresos;
    private CatalogoPresupuesto catItem;
    private CatalogoPresupuesto nivelN;
    private CatalogoPresupuesto fuenteF;
    private CatalogoPresupuesto codigoC;
    private String adicionalString;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodoCosnulatdo = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                catalogoProformaIngresos = new CatalogoProformaPresupuesto();
                catalogoProformaEgresos = new CatalogoProformaPresupuesto();
                catalogoProAdicionalEgresos = new CatalogoProformaPresupuesto();
                catalogoProAdicionalIngresos = new CatalogoProformaPresupuesto();
                mc = new MasterCatalogo();
                mc.setAnio(Utils.getAnio(new Date()).shortValue());

                periodosIngresos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "CP", periodoCosnulatdo});
                periodosEgresos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "CP", periodoCosnulatdo});
                catItem = new CatalogoPresupuesto();
                nivelN = new CatalogoPresupuesto();
                fuenteF = new CatalogoPresupuesto();
                codigoC = new CatalogoPresupuesto();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarDescripcion() {
        CatalogoProformaPresupuesto data = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProformaEgresos.getPeriodoCatalogo().getAnio(), false);

        if (data != null) {
            catalogoProformaEgresos.setDescripcion(data.getDescripcion());
            catalogoProAdicionalEgresos.setId(data.getId());
        } else {
            catalogoProAdicionalEgresos = new CatalogoProformaPresupuesto();
            catalogoProformaEgresos.setDescripcion("");
        }
    }

    public void actualizarDescripcionIngresos() {

        CatalogoProformaPresupuesto item = catalogoProformaService.getCatalogoProformaPresupuesto2(catalogoProformaIngresos.getPeriodoCatalogo().getAnio(), true);

        if (item != null) {
            catalogoProformaIngresos.setDescripcion(item.getDescripcion());
            catalogoProAdicionalIngresos.setId(item.getId());
        } else {
            catalogoProAdicionalIngresos = new CatalogoProformaPresupuesto();
            catalogoProformaIngresos.setDescripcion("");
        }
    }

    public void imprimirRep(boolean excel) {
        if (catalogoProformaEgresos.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        }
        ss.addParametro("anio", catalogoProformaEgresos.getPeriodoCatalogo().getAnio());
        if ((catalogoProAdicionalEgresos.getId() != null)) {
            ss.addParametro("idPro", catalogoProAdicionalEgresos.getId());
            if (excel) {
                ss.setContentType("xlsx");
                ss.setNombreReporte("proformaPresupuestoEgresoExcel");
            } else {
                ss.setNombreReporte("proformaPresupuestoEgresoPdf");
            }
        } else {
            if (excel) {
                ss.setContentType("xlsx");
                ss.setNombreReporte("proformaPresupuestoEgresoNoCatalogoExcel");
            } else {
                ss.setNombreReporte("proformaPresupuestoEgresoNoCatalogoPdf");
            }
        }
        ss.setNombreSubCarpeta("presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public void imprimirResumen(boolean excel) {
        if (catalogoProformaEgresos.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        }
        ss.addParametro("periodo", catalogoProformaEgresos.getPeriodoCatalogo().getAnio());
        if ((catalogoProAdicionalEgresos.getId() != null)) {
            ss.addParametro("id", catalogoProAdicionalEgresos.getId());
            if (excel) {
                ss.setContentType("xlsx");
                ss.setNombreReporte("resumenProformaEgre");
            } else {
                ss.setNombreReporte("resumenProformaEgre");
            }
        }

        ss.setNombreSubCarpeta("presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public void imprimirRepIngreso(boolean excel) {
        
         if (catalogoProformaIngresos.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Aviso", "Escoja un año para proceder");
            return;
        }

//        proformaPresupuestoActualizar = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
//        if (proformaPresupuestoActualizar == null) {
//            JsfUtil.addWarningMessage("Aviso", "Para este informe necesita registrar la proforma de ingreso (año a convenir)");
//            return;
//        }

        if (catalogoProformaIngresos.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        }
//else if (catalogoProformaIngresos.getRowCount() == 0) {
//            JsfUtil.addWarningMessage("Información", "No existe información que imprimir");
//            return;
//        } else {
            if ((codigoC.getCodigo() != null)) {
                ss.addParametro("cod", codigoC.getCodigo() + "%");
            }
            if ((catItem.getClasificacion() != null)) {
                ss.addParametro("cl", catItem.getClasificacion().getTexto());
            }
            if ((nivelN.getNivel() != null)) {
                ss.addParametro("nii", nivelN.getNivel().getOrden());
            }
            if ((fuenteF.getFuente() != null)) {
                ss.addParametro("fuente", fuenteF.getFuente().getNombre());
            }
            if ((adicionalString != null)) {
//                ss.addParametro("tipo", LOG);
            }
            ss.addParametro("anio", catalogoProformaIngresos.getPeriodoCatalogo().getAnio());
            ss.addParametro("NOMBRE_REPORTE", "PROFORMA INGRESOS");
            //ss.addParametro("ENTIDAD", u.getUsuario().getEmpresaId());
            //ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            if ((catalogoProAdicionalIngresos.getId() != null)) {
                ss.addParametro("idPro", catalogoProAdicionalIngresos.getId());
//                if (excel) {
//                    ss.setContentType("xlsx");
//                    ss.setNombreReporte("proformaPresupuestoIngresoExcel");
//                } else {
//                    ss.setNombreReporte("proformaPresupuestoIngresoPdfNuevo");
//                }
                if (excel) {
                    ss.setContentType("xlsx");
                }
                ss.setNombreReporte("proformaPresupuestoIngresoPdfNuevo");
            } else {
                if (excel) {
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoExcel");
                } else {
                    ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoPdf");
                }
            }

            ss.setNombreSubCarpeta("presupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        
        
//        if (catalogoProformaIngresos.getPeriodoCatalogo() == null) {
//            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
//            return;
//        }
//        if ((codigoC.getCodigo() != null)) {
//            ss.addParametro("cod", codigoC.getCodigo() + "%");
//        }
//        if ((catItem.getClasificacion() != null)) {
//            ss.addParametro("cl", catItem.getClasificacion().getTexto());
//        }
//        if ((nivelN.getNivel() != null)) {
//            ss.addParametro("nii", nivelN.getNivel().getOrden());
//        }
//        if ((fuenteF.getFuente() != null)) {
//            ss.addParametro("fuente", fuenteF.getFuente().getNombre());
//        }
//        if ((adicionalString != null)) {
////                ss.addParametro("tipo", LOG);
//        }
//        if ((catalogoProAdicionalIngresos.getId() != null)) {
//            ss.addParametro("idPro", catalogoProAdicionalIngresos.getId());
//            if (excel) {
//                ss.setContentType("xlsx");
//                ss.setNombreReporte("proformaPresupuestoIngresoExcel");
//            } else {
//                ss.setNombreReporte("proformaPresupuestoIngresoPdfNuevo");
//            }
//        } else {
//            if (excel) {
//                ss.setContentType("xlsx");
//                ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoExcel");
//            } else {
//                ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoPdf");
//            }
//        }
//        ss.setNombreSubCarpeta("presupuesto");
//        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public void imprimirResumenIngresos(boolean excel) {
        if (catalogoProformaIngresos.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        }
        ss.addParametro("anio", catalogoProformaIngresos.getPeriodoCatalogo().getAnio());
        ss.addParametro("id", catalogoProAdicionalIngresos.getId());
        if (excel) {
            ss.setContentType("xlsx");
            ss.setNombreReporte("resumenProformaIngresos");
        } else {
            ss.setNombreReporte("resumenProformaIngresos");
        }
        ss.addParametro("NOMBRE_REPORTE", "PROFORMA INGRESOS");
        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
        ss.setNombreSubCarpeta("presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Short getPeriodoCosnulatdo() {
        return periodoCosnulatdo;
    }

    public void setPeriodoCosnulatdo(Short periodoCosnulatdo) {
        this.periodoCosnulatdo = periodoCosnulatdo;
    }

    public CatalogoProformaPresupuesto getCatalogoProformaEgresos() {
        return catalogoProformaEgresos;
    }

    public void setCatalogoProformaEgresos(CatalogoProformaPresupuesto catalogoProformaEgresos) {
        this.catalogoProformaEgresos = catalogoProformaEgresos;
    }

    public CatalogoProformaPresupuesto getCatalogoProformaIngresos() {
        return catalogoProformaIngresos;
    }

    public void setCatalogoProformaIngresos(CatalogoProformaPresupuesto catalogoProformaIngresos) {
        this.catalogoProformaIngresos = catalogoProformaIngresos;
    }

    public MasterCatalogo getMc() {
        return mc;
    }

    public void setMc(MasterCatalogo mc) {
        this.mc = mc;
    }

    public List<MasterCatalogo> getPeriodosEgresos() {
        return periodosEgresos;
    }

    public void setPeriodosEgresos(List<MasterCatalogo> periodosEgresos) {
        this.periodosEgresos = periodosEgresos;
    }

    public List<MasterCatalogo> getPeriodosIngresos() {
        return periodosIngresos;
    }

    public void setPeriodosIngresos(List<MasterCatalogo> periodosIngresos) {
        this.periodosIngresos = periodosIngresos;
    }

//</editor-fold>
    public CatalogoProformaPresupuesto getCatalogoProAdicionalEgresos() {
        return catalogoProAdicionalEgresos;
    }

    public void setCatalogoProAdicionalEgresos(CatalogoProformaPresupuesto catalogoProAdicionalEgresos) {
        this.catalogoProAdicionalEgresos = catalogoProAdicionalEgresos;
    }

    public CatalogoProformaPresupuesto getCatalogoProAdicionalIngresos() {
        return catalogoProAdicionalIngresos;
    }

    public void setCatalogoProAdicionalIngresos(CatalogoProformaPresupuesto catalogoProAdicionalIngresos) {
        this.catalogoProAdicionalIngresos = catalogoProAdicionalIngresos;
    }

    public void abriDlogo() {

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
            //clienteService.getUnidadUserCodigo("CPR","2")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));//presupuesto 2
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
}
