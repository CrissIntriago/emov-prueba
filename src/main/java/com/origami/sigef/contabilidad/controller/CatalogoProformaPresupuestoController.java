/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.lazy.CatalogoPresupuestoLazy;
import com.origami.sigef.contabilidad.lazy.ProformaPresupuestoPlanificadoLazy;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.presupuestoModel;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProgramacionIngresoEgresoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.MoveEvent;

/**
 *
 * @author Origami
 */
@Named(value = "mantenimientoProformaView")
@ViewScoped
public class CatalogoProformaPresupuestoController extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession userSession;

    @Inject
    private CatalogoProformaPresupuestoService mantenimientoProformaService;
    @Inject
    private PresupuestoService presService;
    @Inject
    private ProgramacionIngresoEgresoService pimService;
    @Inject
    private ProductoService productoService;
    @Inject
    private ActividadOperativaService actividadOperativaService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ClienteService clienteService;
    private CatalogoProformaPresupuesto mantenimientoProforma;
    private CatalogoProformaPresupuesto mantenimientoSeleccionado;
    private LazyModel<CatalogoProformaPresupuesto> lazy;
    private CatalogoPresupuestoLazy presupuestoIngresoLazy;
    private CatalogoPresupuesto cuentaSeleccionada;
    private PresupuestoController presupuestoController;
    private List<presupuestoModel> lista;
    private List<presupuestoModel> lista2;
    private Presupuesto presupuesto;
    private ProgramacionIngresoEgreso pim;

    private ProformaPresupuestoPlanificadoLazy dlgCatalogoPresupuestoEgresoLazy;
    private CatalogoPresupuestoLazy dlgCatalogoPresupuestoIngresoLazy;
    private CatalogoProformaPresupuesto catalogoProformaSeleccionado;
    private LazyModel<CatalogoProformaPresupuesto> catalogoProformaPresupuestoLazyModel;
    private Short periodoConsultado;
    private boolean btnAprobar, btnRechazar;
    private String observaciones;
    private CatalogoItem estadoProgramacion;
    //NUEVO
    private LazyModel<ProformaIngreso> proformaIngresosLazy;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodoConsultado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));

                presupuesto = new Presupuesto();
                lista = new ArrayList<>();
                lista2 = new ArrayList<>();
                mantenimientoProforma = new CatalogoProformaPresupuesto();
                lazy = new LazyModel<>(CatalogoProformaPresupuesto.class);
                lazy.getFilterss().put("estado", true);
                lazy.getSorteds().put("periodo", "DESC");
                presupuestoIngresoLazy = new CatalogoPresupuestoLazy(mantenimientoProforma);
                presupuestoController = new PresupuestoController();
                pim = new ProgramacionIngresoEgreso();
                catalogoProformaPresupuestoLazyModel = new LazyModel<>(CatalogoProformaPresupuesto.class);
                catalogoProformaPresupuestoLazyModel.getFilterss().put("periodo:equal", periodoConsultado);
                catalogoProformaPresupuestoLazyModel.getSorteds().put("periodo", "DESC");
                catalogoProformaPresupuestoLazyModel.getFilterss().put("estado", true);
                btnAprobar = false;
                btnRechazar = false;
                estadoProgramacion = new CatalogoItem();
                estadoProgramacion = catalogoService.getItemByCatalogoAndCodigo("estado_pie", "REG");
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void form(CatalogoProformaPresupuesto mantenimientoProf) {
        if (mantenimientoProf != null) {
            mantenimientoProforma = mantenimientoProf;
        } else {
            mantenimientoProforma = new CatalogoProformaPresupuesto();
        }
        PrimeFaces.current().executeScript("PF('dialogMant').show()");
        PrimeFaces.current().ajax().update("formDialog");
    }

    public void formTable(CatalogoProformaPresupuesto mantenimientoProfo) {
        if (mantenimientoProfo != null) {
            mantenimientoSeleccionado = mantenimientoProfo;
            presupuestoIngresoLazy = new CatalogoPresupuestoLazy(mantenimientoProfo);
        }
        PrimeFaces.current().executeScript("PF('dialogTable').show()");
        PrimeFaces.current().ajax().update("formTable");
        PrimeFaces.current().ajax().update("cuentas");

    }

    /*Funcion para validar la proforma que se va aprobar*/
    public void guardarAprobacionDeLaProforma() {
        CatalogoProformaPresupuesto proforma = mantenimientoProformaService.find(mantenimientoProforma.getId());
        if (proforma.getAprobado() == true) {
            JsfUtil.addSuccessMessage("Estado de Aprobación:", "APROBADO " + mantenimientoProforma.getPeriodo());
            return;
        }
        /*Condicion 1:  para determinar si la proforma a aprobar es Ingreso(true) o Egreso(false)*/
        if (mantenimientoProforma.getTipoFlujo() == true) {
            /*Consultar en el catalogoProformaPresupuesto si existe un egreso del mismo periodo*/
            CatalogoProformaPresupuesto resultEgreso = mantenimientoProformaService.getCatalogoProformaPresupuesto(mantenimientoProforma.getPeriodo(), !mantenimientoProforma.getTipoFlujo());
            /*Condicion 2: Verificar si la consulta resultEgreso trae o no alguna información*/
            if (resultEgreso == null) {
                /*Si se cumple la condicion 2 es que no hay una Proforma de Egreso*/
                PrimeFaces.current().executeScript("PF('dialogMant').hide()");
                JsfUtil.addErrorMessage("Estado de Aprobación", "No existe una Proforma de Egreso para la Proforma de Ingreso del período " + mantenimientoProforma.getPeriodo());
            } else {
                /*Condicion 3: Comparar si el presupuesto de la Proforma de Ingreso es igual al de Egreso*/
                if (mantenimientoProforma.getTotal().equals(resultEgreso.getTotal())) {
                    /*Condicion para determinar si selecione el boton de aprobado*/
                    if (mantenimientoProforma.getAprobado()) {
//                        PrimeFaces.current().executeScript("PF('confirmarAprobado').show()");
                        mantenimientoProformaService.edit(mantenimientoProforma);
                        save();
                    } else {
                        JsfUtil.addWarningMessage("Aviso", "Necesita aprobar la proforma antes de guardar");
                    }
                } else {
                    /*No guardara si no se cumple la condicion 3*/
                    PrimeFaces.current().executeScript("PF('dialogMant').hide()");
                    JsfUtil.addErrorMessage("Estado de Aprobación", "El presupuesto de la Proforma de Egreso del " + resultEgreso.getPeriodo() + " es diferente al presupuesto de la Proforma de Ingreso del " + mantenimientoProforma.getPeriodo());
                }
            }
        } else {
            /*Consultar en el catalogoProformaPresupuesto si existe un Ingreso del mismo periodo*/
            CatalogoProformaPresupuesto resultIngreso = mantenimientoProformaService.getCatalogoProformaPresupuesto(mantenimientoProforma.getPeriodo(), !mantenimientoProforma.getTipoFlujo());
            /*Condicion 4: Verificar si la consulta resultEgreso trae o no alguna información*/
            if (resultIngreso == null) {
                /*Si se cumple la condicion 2 es que no hay una Proforma de Ingreso*/
                PrimeFaces.current().executeScript("PF('dialogMant').hide()");
                JsfUtil.addErrorMessage("Estado de Aprobación", "No existe una Proforma de Ingreso para la Proforma de Engreso del período " + mantenimientoProforma.getPeriodo());
            } else {
                /*Condicion 4: Comparar si el presupuesto de la Proforma de Ingreso es igual al de Egreso*/
                if (mantenimientoProforma.getTotal().equals(resultIngreso.getTotal())) {
                    /*Condicion para determinar si selecione el boton de aprobado*/
                    if (mantenimientoProforma.getAprobado()) {
//                        PrimeFaces.current().executeScript("PF('confirmarAprobado').show()");
                        mantenimientoProformaService.edit(mantenimientoProforma);
                        save();
                    } else {
                        JsfUtil.addWarningMessage("Aviso", "Necesita aprobar la proforma antes de guardar");
                    }
                } else {
                    /*No guardara si no se cumple la condicion 3*/
                    PrimeFaces.current().executeScript("PF('dialogMant').hide()");
                    JsfUtil.addErrorMessage("Estado de Aprobación", "El presupuesto de la Proforma de Ingreso del " + resultIngreso.getPeriodo() + " es diferente al presupuesto de la Proforma de Engreso del " + mantenimientoProforma.getPeriodo());
                }
            }
        }
    }

    public void enviarMetodosIngresos(Short opcionBusqueda) {

        lista2 = presService.presupuestoIngresos(opcionBusqueda);

        for (presupuestoModel item : lista2) {

            presupuesto.setPartida(item.getPartida());
            presupuesto.setItemNew(item.getItemPresupuestario());
            presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
            presupuesto.setFuenteNew(item.getFuente());
            presupuesto.setFuenteDirecta(item.getFuenteDirecta());
            presupuesto.setUsuarioCreacion(userSession.getNameUser());
            presupuesto.setFechaCreacion(new Date());
            presupuesto.setUsuarioModificacion(userSession.getNameUser());
            presupuesto.setFechaModificacion(new Date());
            presupuesto.setPeriodo(opcionBusqueda);
            presupuesto.setTipo(item.getTipo());
            presupuesto.setValorIngreso(item.getTotalingreso());
            presupuesto.setValorEgreso(item.getTotalegresos());
            presupuesto.setReformaSuplemetario(BigDecimal.ZERO);
            presupuesto.setReformaReducido(BigDecimal.ZERO);
            presupuesto.setPresupuestoInicial(item.getTotalingreso());
            presupuesto.setCodificado(presupuesto.getPresupuestoInicial());
            this.presupuesto = presService.create(presupuesto);

            this.presupuesto = new Presupuesto();
        }

        List<ProformaIngreso> resultPresupuesto = mantenimientoProformaService.getCatalogoPresupuesto(opcionBusqueda, (short) 4, true);
        if (!resultPresupuesto.isEmpty()) {
            for (ProformaIngreso item : resultPresupuesto) {
                pim.setItemNew(item.getItem());
                pim.setEnero(BigDecimal.ZERO);
                pim.setFebrero(BigDecimal.ZERO);
                pim.setMarzo(BigDecimal.ZERO);
                pim.setAbril(BigDecimal.ZERO);
                pim.setMayo(BigDecimal.ZERO);
                pim.setJunio(BigDecimal.ZERO);
                pim.setJulio(BigDecimal.ZERO);
                pim.setAgosto(BigDecimal.ZERO);
                pim.setSeptiembre(BigDecimal.ZERO);
                pim.setOctubre(BigDecimal.ZERO);
                pim.setNoviembre(BigDecimal.ZERO);
                pim.setDiciembre(BigDecimal.ZERO);
                pim.setPeriodo(opcionBusqueda);
                pim.setTotalAnio(BigDecimal.ZERO);
                pim.setTipoFlujo(Boolean.TRUE);
                pim.setDistribucion(Boolean.FALSE);
                pim.setFechaCreacion(new Date());
                pim.setUsuarioCreacion(userSession.getNameUser());
                pim.setEstadoA(estadoProgramacion);

                pim = pimService.create(pim);

                pim = new ProgramacionIngresoEgreso();
            }
        }
//        setTotalIngresos(presService.totalIngresoAprobados(opcionBusqueda.getAnio()));
//        setTotalEgresos(presService.totalEgresoAprobados(opcionBusqueda.getAnio()));
    }

    public void enviarMetodosEgresos(Short opcionBusqueda) {
//       this.lista=new ArrayList<>();
        this.lista = presService.presupuestoEgresos(opcionBusqueda);
        if (lista != null) {
            for (presupuestoModel item : lista) {
                presupuesto.setPartida(item.getPartida());
                presupuesto.setItemNew(item.getItemPresupuestario());
                presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
                presupuesto.setFuenteNew(item.getFuente());
                presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setUsuarioCreacion(userSession.getNameUser());
                presupuesto.setFechaCreacion(new Date());
                presupuesto.setUsuarioModificacion(userSession.getNameUser());
                presupuesto.setFechaModificacion(new Date());
                presupuesto.setPeriodo(opcionBusqueda);
                presupuesto.setTipo(item.getTipo());
                presupuesto.setValorIngreso(item.getTotalingreso());
                presupuesto.setValorEgreso(item.getTotalegresos());
                presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setReformaSuplemetario(BigDecimal.ZERO);
                presupuesto.setReformaReducido(BigDecimal.ZERO);
                presupuesto.setPresupuestoInicial(item.getTotalegresos());
                presupuesto.setCodificado(presupuesto.getPresupuestoInicial());
                this.presupuesto = presService.create(presupuesto);

                this.presupuesto = new Presupuesto();

            }
        }
        enviarProgramacionIngresoEgreso(opcionBusqueda);
        //   enviarDistributivo(opcionBusqueda);
    }
//    public void enviarDistributivo (Short opcionBusqueda){
//        List<PartidasDistributivo> codigo 
//    }

//    public void enviarDistributivo (Short opcioBusqueda){
//       List<PartidasDistributivo> codigo = presService.filtroCodigo(opcioBusqueda);
//    }
    public void enviarProgramacionIngresoEgreso(Short opcionBusqueda) {
        List<ProformaPresupuestoPlanificado> resultProforma = presService.getProformaEgreso(opcionBusqueda);
        if (!resultProforma.isEmpty()) {
            for (ProformaPresupuestoPlanificado egreso : resultProforma) {

                pim.setPeriodo(opcionBusqueda);
                pim.setMontoActividad(egreso.getValor());
                pim.setTipoFlujo(Boolean.FALSE);
                pim.setCodigoItem(egreso.getPartidaPresupuestaria());
                pim.setEnero(BigDecimal.ZERO);
                pim.setFebrero(BigDecimal.ZERO);
                pim.setMarzo(BigDecimal.ZERO);
                pim.setAbril(BigDecimal.ZERO);
                pim.setMayo(BigDecimal.ZERO);
                pim.setJunio(BigDecimal.ZERO);
                pim.setJulio(BigDecimal.ZERO);
                pim.setAgosto(BigDecimal.ZERO);
                pim.setSeptiembre(BigDecimal.ZERO);
                pim.setOctubre(BigDecimal.ZERO);
                pim.setNoviembre(BigDecimal.ZERO);
                pim.setDiciembre(BigDecimal.ZERO);
                pim.setDistribucion(Boolean.FALSE);
                pim.setMontoCuatrimestre1(BigDecimal.ZERO);
                pim.setMontoCuatrimestre2(BigDecimal.ZERO);
                pim.setMontoCuatrimestre3(BigDecimal.ZERO);
                pim.setTotalAnio(BigDecimal.ZERO);
                pim.setTipoCodigo(egreso.getCodigo());
                pim.setActividad(Boolean.FALSE);
                pim.setFechaCreacion(new Date());
                pim.setUsuarioCreacion(userSession.getNameUser());
                pim.setEstadoA(estadoProgramacion);
                pim.setItemNew(egreso.getItemNew());
                pim = pimService.create(pim);
                this.pim = new ProgramacionIngresoEgreso();
            }
        }

        BigDecimal TotalProducto = BigDecimal.ZERO;
        BigDecimal TotalActividades = BigDecimal.ZERO;
        List<ActividadOperativa> actividadList = actividadService.getListActividades(opcionBusqueda, Arrays.asList("AP", "RP", "REP"));
        List<Producto> productoList = productoService.getListProductos(opcionBusqueda, Arrays.asList("AP", "RP", "REP"));
        //List<Producto> productoList = productoService.findByNamedQuery("Producto.findByPeriodoActual", opcionBusqueda);
        //List<ActividadOperativa> actividadList = actividadOperativaService.findByNamedQuery("ActividadOperativa.findByPeriodoActual", opcionBusqueda);
        if (!productoList.isEmpty() && !actividadList.isEmpty()) {

            for (ActividadOperativa ac : actividadList) {
                TotalActividades = TotalActividades.setScale(2, RoundingMode.HALF_UP).add(ac.getMonotReformado().setScale(2, RoundingMode.HALF_UP));
            }
            for (Producto ac : productoList) {
                TotalProducto = TotalProducto.setScale(2, RoundingMode.HALF_UP).add(ac.getMontoReformada().setScale(2, RoundingMode.HALF_UP));
            }

            if (TotalProducto.setScale(2, RoundingMode.HALF_UP).compareTo(TotalActividades.setScale(2, RoundingMode.HALF_UP)) == 0) {
                List<ProformaPDTO> resultProducto = presService.muestrameEgresos(opcionBusqueda);
                if (resultProducto != null) {
                    for (ProformaPDTO pro : resultProducto) {
                        pim.setItemNew(pro.getItemNew());
                        pim.setCodigoItem(pro.getPartida());
                        pim.setTipoFlujo(Boolean.FALSE);
                        pim.setPeriodo(opcionBusqueda);
                        pim.setMontoActividad(pro.getTotal());
                        pim.setEnero(BigDecimal.ZERO);
                        pim.setFebrero(BigDecimal.ZERO);
                        pim.setMarzo(BigDecimal.ZERO);
                        pim.setAbril(BigDecimal.ZERO);
                        pim.setMayo(BigDecimal.ZERO);
                        pim.setJunio(BigDecimal.ZERO);
                        pim.setJulio(BigDecimal.ZERO);
                        pim.setAgosto(BigDecimal.ZERO);
                        pim.setSeptiembre(BigDecimal.ZERO);
                        pim.setOctubre(BigDecimal.ZERO);
                        pim.setNoviembre(BigDecimal.ZERO);
                        pim.setDiciembre(BigDecimal.ZERO);
                        pim.setDistribucion(Boolean.FALSE);
                        pim.setMontoCuatrimestre1(BigDecimal.ZERO);
                        pim.setMontoCuatrimestre2(BigDecimal.ZERO);
                        pim.setMontoCuatrimestre3(BigDecimal.ZERO);
                        pim.setTotalAnio(BigDecimal.ZERO);
                        pim.setTipoCodigo("PAPP");
                        pim.setActividad(Boolean.TRUE);
                        pim.setFechaCreacion(new Date());
                        pim.setUsuarioCreacion(userSession.getNameUser());
                        pim.setEstadoA(estadoProgramacion);
                        pim.setItemNew(pro.getItemNew());
                        pim = pimService.create(pim);
                        this.pim = new ProgramacionIngresoEgreso();
                    }
                }
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede generar un reporte de partida presupuestaria debido a que el total de "
                        + "productos es diferente del total de actividades del período " + opcionBusqueda);
            }
        }

    }

    public void save() {
        if (mantenimientoProforma.getAprobado() == true) {
            if (mantenimientoProforma.getTipoFlujo() == true) {
                enviarMetodosIngresos(mantenimientoProforma.getPeriodo());
                PrimeFaces.current().ajax().update("messages");

            } else {
                enviarMetodosEgresos(mantenimientoProforma.getPeriodo());
                PrimeFaces.current().ajax().update("messages");

            }
        }
        mantenimientoProforma.setFechaModificacion(new Date());
        mantenimientoProforma.setUsuarioModifica(userSession.getNameUser());
        mantenimientoProforma.setFechaAprobacion(new Date());
        mantenimientoProformaService.edit(mantenimientoProforma);
        PrimeFaces.current().executeScript("PF('confirmarAprobado').hide()");
        PrimeFaces.current().executeScript("PF('dialogMant').hide()");
        JsfUtil.addSuccessMessage("Estado de Aprobación", "La Proforma de Ingreso del " + mantenimientoProforma.getPeriodo() + " fue aprobado");
        PrimeFaces.current().ajax().update("catalogoProformaPresupuestoTable");
    }

    public boolean disabledAcept(CatalogoProformaPresupuesto catalogo) {
        if (catalogo != null) {
            return true;
        } else {
            return false;
        }
    }

    public void guardarAprobacion() {
        try {
            boolean edit = mantenimientoProforma.getId() != null;
            if (edit) {
                mantenimientoProforma.setFechaModificacion(new Date());
                mantenimientoProforma.setUsuarioCreacion(userSession.getNameUser());
                mantenimientoProformaService.edit(mantenimientoProforma);
            }

            PrimeFaces.current().executeScript("PF('confirmarAprobado').hide()");
            PrimeFaces.current().ajax().update("catalogoProformaPresupuestoTable");
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Mantenimiento Proforma", "Catalogo " + mantenimientoProforma.getDescripcion() + (edit ? " Aprobado" : " registrado") + " con éxito.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(CatalogoProformaPresupuesto mantenimiento) {
        if (mantenimiento != null) {
            if (mantenimiento.getTipoFlujo()) {
                List<ProformaIngreso> resultIngreso = mantenimientoProformaService.getCatalogoPresupuesto(mantenimiento.getPeriodo(), null, true);
                if (mantenimiento.getEstado() == false) {
                    mantenimientoProformaService.remove(mantenimiento);
                    JsfUtil.addInformationMessage("Información", "Se ha eliminado correctamente");
                    return;
                }

                if (!resultIngreso.isEmpty()) {
                    JsfUtil.addErrorMessage("Catálogo Proforma", "No se puede eliminar " + mantenimiento.getDescripcion() + " contiene registros");
                    return;
                }
                setEstadoEliminar(mantenimiento);
            } else {
                List<ProformaPresupuestoPlanificado> resultEgreso = mantenimientoProformaService.getProformaPresupuestoPlanificado(mantenimiento.getPeriodo());

                if (!mantenimiento.getEstado() == false) {
                    mantenimientoProformaService.remove(mantenimiento);
                    JsfUtil.addInformationMessage("Información", "Se ha eliminado correctamente");
                    return;
                }

                if (!resultEgreso.isEmpty()) {
                    JsfUtil.addErrorMessage("Catálogo Proforma", "No se puede eliminar " + mantenimiento.getDescripcion() + " contiene registros");
                    return;
                }
                setEstadoEliminar(mantenimiento);
            }

        }
    }

    public void setEstadoEliminar(CatalogoProformaPresupuesto mantenimiento) {
        mantenimiento.setEstado(Boolean.FALSE);
        mantenimiento.setFechaModificacion(new Date());
        mantenimiento.setUsuarioModifica("system");
        mantenimientoProformaService.edit(mantenimiento);
        JsfUtil.addSuccessMessage("Catálogo Proforma", "Eliminado con éxito");
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update("catalogoProformaPresupuestoTable");
    }

    public String redireccionar(CatalogoProformaPresupuesto catalogo) {

        if (catalogo.getTipoFlujo()) {
            return "ProformaDeIngreso.xhtml?faces-redirect=true";
        } else {
            return "ProformaDeEgreso.xhtml?faces-redirect=true";
        }
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("catalogoProformaPresupuestoTable");
        PrimeFaces.current().ajax().update("formDialog");
    }

    public void handleClose(CloseEvent event) {
        addMessage(event.getComponent().getId() + " closed", "So you don't like nature?");
    }

    public void handleMove(MoveEvent event) {
        addMessage(event.getComponent().getId() + " moved", "Left: " + event.getLeft() + ", Top: " + event.getTop());
    }

    public void showDlgProformaPresupuesto(CatalogoProformaPresupuesto catalogoProformaPresupuesto) {
        dlgCatalogoPresupuestoIngresoLazy = new CatalogoPresupuestoLazy();
        dlgCatalogoPresupuestoEgresoLazy = new ProformaPresupuestoPlanificadoLazy();
        catalogoProformaSeleccionado = catalogoProformaPresupuesto;
        if (catalogoProformaPresupuesto.getTipoFlujo().equals(true)) {
            /*Muestra una vista del presupuesto de Ingresos*/
            proformaIngresosLazy = new LazyModel<>(ProformaIngreso.class);
            proformaIngresosLazy.getFilterss().put("periodo", catalogoProformaPresupuesto.getPeriodo());
            proformaIngresosLazy.getSorteds().put("item.codigo", "ASC");
            proformaIngresosLazy.setDistinct(false);
            PrimeFaces.current().executeScript("PF('dlgCatalogoPresupuestoIngreso').show()");
            PrimeFaces.current().ajax().update(":formDlgCatalogoPresupuestoIngreso");
            PrimeFaces.current().ajax().update(":catalogoProformaPresupuestoIngresoTable");
        } else if (catalogoProformaPresupuesto.getTipoFlujo().equals(false)) {
            /*Muestra una vista del presupuesto de Egresos*/
            dlgCatalogoPresupuestoEgresoLazy = new ProformaPresupuestoPlanificadoLazy(catalogoProformaPresupuesto);
            PrimeFaces.current().executeScript("PF('dlgCatalogoPresupuestoEgreso').show()");
            PrimeFaces.current().ajax().update(":formDlgCatalogoPresupuestoEgreso");
            PrimeFaces.current().ajax().update(":catalogoProformaPresupuestoEgresoTable");
        }
    }

    public void AprobarRevision(boolean aprobar) {
        List<CatalogoProformaPresupuesto> listaVerificacionAprobacion;
        List<CatalogoProformaPresupuesto> listaVerificacionRechazados;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (aprobar) {
            listaVerificacionAprobacion = mantenimientoProformaService.getListaVerificacion(periodoConsultado, aprobar);

            if (listaVerificacionAprobacion.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("AVISO", "VERIFIQUE QUE ESTEN APROBADOS LAS PROFORMAS");
                return;
            }

            if (!listaVerificacionAprobacion.isEmpty()) {

                if (listaVerificacionAprobacion.size() != 2) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("ERROR", "PARA PODER APROBARLAS DEBE INGRESAR UN NUMERO DE RESOLUCIÓN Y APROBAR AHÍ MISMO TANTO PARA INGRESO COMO EGRESOS");
                    return;
                }
            }

            btnAprobar = true;
            btnRechazar = false;

        } else {
            listaVerificacionRechazados = mantenimientoProformaService.getListaVerificacion(periodoConsultado, aprobar);

            if (listaVerificacionRechazados.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("ERROR", "NO HAY DATOS");
                return;
            }

            if (!listaVerificacionRechazados.isEmpty()) {

                if (listaVerificacionRechazados.size() != 2) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("ERROR", "PARA PODER RECHAZARLAS NO DEBEN ESTAR APROBADAS INGRESO NI EGRESOS");
                    return;
                }
            }

            btnAprobar = false;
            btnRechazar = true;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);

            CatalogoItem estadoAprobadoPapp = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
            CatalogoItem estadoRecahazadoPapp = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REP");
            CatalogoItem estadoAprobadoOtros = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            CatalogoItem estadoRecahazadoOtros = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RED");

            if (aprobar == 1) {

                actividadService.aprobarTodo(periodoConsultado, estadoAprobadoPapp);
                actividadService.aprobarDistributivosAndPD(periodoConsultado, estadoAprobadoOtros);
            } else {

                actividadService.aprobarTodo(periodoConsultado, estadoRecahazadoPapp);
                actividadService.aprobarDistributivosAndPD(periodoConsultado, estadoRecahazadoOtros);
            }

            getParamts().put("aprobado", aprobar);
            //clienteService.getUnidadUserCodigo("CPR","2")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto)); //PRESUPUESTO 2

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public CatalogoProformaPresupuesto getMantenimientoProforma() {
        return mantenimientoProforma;
    }

    public void setMantenimientoProforma(CatalogoProformaPresupuesto mantenimientoProforma) {
        this.mantenimientoProforma = mantenimientoProforma;
    }

    public LazyModel<CatalogoProformaPresupuesto> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<CatalogoProformaPresupuesto> lazy) {
        this.lazy = lazy;
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public LazyModel<CatalogoPresupuesto> getPresupuestoIngresoLazy() {
        return presupuestoIngresoLazy;
    }

    public void setPresupuestoIngresoLazy(CatalogoPresupuestoLazy presupuestoIngresoLazy) {
        this.presupuestoIngresoLazy = presupuestoIngresoLazy;
    }

    public CatalogoPresupuesto getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(CatalogoPresupuesto cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public CatalogoProformaPresupuesto getMantenimientoSeleccionado() {
        return mantenimientoSeleccionado;
    }

    public void setMantenimientoSeleccionado(CatalogoProformaPresupuesto mantenimientoSeleccionado) {
        this.mantenimientoSeleccionado = mantenimientoSeleccionado;
    }

    public PresupuestoController getPresupuestoController() {
        return presupuestoController;
    }

    public void setPresupuestoController(PresupuestoController presupuestoController) {
        this.presupuestoController = presupuestoController;
    }

    public List<presupuestoModel> getLista() {
        return lista;
    }

    public void setLista(List<presupuestoModel> lista) {
        this.lista = lista;
    }

    public List<presupuestoModel> getLista2() {
        return lista2;
    }

    public void setLista2(List<presupuestoModel> lista2) {
        this.lista2 = lista2;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public ProgramacionIngresoEgreso getPim() {
        return pim;
    }

    public void setPim(ProgramacionIngresoEgreso pim) {
        this.pim = pim;
    }

    public ProformaPresupuestoPlanificadoLazy getDlgCatalogoPresupuestoEgresoLazy() {
        return dlgCatalogoPresupuestoEgresoLazy;
    }

    public void setDlgCatalogoPresupuestoEgresoLazy(ProformaPresupuestoPlanificadoLazy dlgCatalogoPresupuestoEgresoLazy) {
        this.dlgCatalogoPresupuestoEgresoLazy = dlgCatalogoPresupuestoEgresoLazy;
    }

    public CatalogoPresupuestoLazy getDlgCatalogoPresupuestoIngresoLazy() {
        return dlgCatalogoPresupuestoIngresoLazy;
    }

    public void setDlgCatalogoPresupuestoIngresoLazy(CatalogoPresupuestoLazy dlgCatalogoPresupuestoIngresoLazy) {
        this.dlgCatalogoPresupuestoIngresoLazy = dlgCatalogoPresupuestoIngresoLazy;
    }

    public CatalogoProformaPresupuesto getCatalogoProformaSeleccionado() {
        return catalogoProformaSeleccionado;
    }

    public void setCatalogoProformaSeleccionado(CatalogoProformaPresupuesto catalogoProformaSeleccionado) {
        this.catalogoProformaSeleccionado = catalogoProformaSeleccionado;
    }

    public LazyModel<CatalogoProformaPresupuesto> getCatalogoProformaPresupuestoLazyModel() {
        return catalogoProformaPresupuestoLazyModel;
    }

    public void setCatalogoProformaPresupuestoLazyModel(LazyModel<CatalogoProformaPresupuesto> catalogoProformaPresupuestoLazyModel) {
        this.catalogoProformaPresupuestoLazyModel = catalogoProformaPresupuestoLazyModel;
    }
//</editor-fold>

    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LazyModel<ProformaIngreso> getProformaIngresosLazy() {
        return proformaIngresosLazy;
    }

    public void setProformaIngresosLazy(LazyModel<ProformaIngreso> proformaIngresosLazy) {
        this.proformaIngresosLazy = proformaIngresosLazy;
    }

}
