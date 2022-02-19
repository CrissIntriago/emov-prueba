package com.origami.sigef.resource.presupuesto.procesos.controllers;

import com.origami.sigef.certificacion_presupuesto_anual.model.DocumentosModel;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.presupuesto.procesos.services.ClienteServices;
import com.origami.sigef.resource.presupuesto.procesos.services.ReservaCompromisoServices;
import com.origami.sigef.resource.presupuesto.procesos.services.ProcedimientoRequisitoServices;
import com.origami.sigef.resource.presupuesto.procesos.services.CatalogoItemServices;
import com.origami.sigef.resource.presupuesto.procesos.services.SolicitudRequisitoServices;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class reservaCompromisoMB implements Serializable {

    private LazyModel<Servidor> servidorLazy;
    private LazyModel<Proveedor> proveedorLazy;
    private LazyModel<SolicitudReservaCompromiso> solicitudesReservaCompromiso;
    private List<Procedimiento> procedimientoList;
    private List<UnidadAdministrativa> unidadFiltros = new ArrayList<>();
    private List<Producto> listaProductos;
    private List<CatalogoItem> estadoFiltro = new ArrayList<>();
    private List<DetalleSolicitudCompromiso> listaGuardar, listaGlobalDetalleSolicitudCompromisos, removerDetalle, listaGlobalDetalleSolicitudPartidas, listaeditable1, listaeditable2;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private List<DetalleSolicitudCompromiso> visualizacionSolicitud;
    private List<Presupuesto> listaPresupuesto;
    private SolicitudReservaCompromiso reservaCompromiso;
    private UnidadAdministrativa unidadAdministrativa;
    private Procedimiento procedimientoSeleccionado;
    private BigDecimal totalComprometidoPD;
    private DetalleSolicitudCompromiso detalleSolicitud;
    private boolean panel1, panel2, ocultarbutton, mostrarButton;
    private boolean tabla1;
    private boolean tabla2;
    private boolean tabla3;
    private boolean verififcar;
    private DocumentosModel data;
    private String observaciones;
    private BigDecimal totalSolicitado;
    private BigDecimal totalDisponible;
    private BigDecimal totalComprometido;
    private BigDecimal totalDisponiblePD;
    private BigDecimal totalSolicitadoPD;
    private boolean panelProveedor, panelServidor;
    private String fileName;
    private boolean registraNuevoVerificar;
    @Inject
    private ReservaCompromisoServices reservaCompromisoServices;
    @Inject
    CatalogoItemServices catalogoItemServices;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteServices clienteServices;
    @Inject
    private ProcedimientoRequisitoServices procedimientoRequisitoServices;
    @Inject
    private SolicitudRequisitoServices solicitudRequisitoService;
    @Inject
    private ProcedimientoService procedimientoService;
    /*VARIABLES PARA CONTROL PREVIO DE DOCUMENTOS*/
    Date fechaActual=new Date();
    Boolean documentoRecibdo=Boolean.FALSE;
    @PostConstruct
    public void initView() {
        proveedorLazy = null;
        panel1 = true;
        panel2 = false;
        listaProductos = new ArrayList<>();
        procedimientoList = procedimientoService.getProcedimientos("SOL_RC");
        reservaCompromiso = new SolicitudReservaCompromiso();
        solicitudesReservaCompromiso = new LazyModel<>(SolicitudReservaCompromiso.class);
        unidadFiltros = reservaCompromisoServices.getListaUnidadesReservas();
        estadoFiltro = catalogoItemServices.MostarTodoCatalogo("estado_solicitud");
        procedimientoRequisitoList = new ArrayList<>();
    }

    public void consultarMostrarAndOcultarPaneles() {
        panel1 = true;
        panel2 = false;

    }

    public void registarrMostrarAndOcultarPaneles() {
        clearAllFilters();
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        panel1 = false;
        panel2 = true;
        ocultarbutton = false;
        mostrarButton = true;
        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        this.removerDetalle = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        int anio = Utils.getAnio(new Date());
        reservaCompromiso.setFechaSolicitud(new Date());
        reservaCompromiso.setPeriodo((short) anio);
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:dataSolciitudesReserva");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:dataSolciitudesReserva");
        }
    }

    public void accionComponenetes() {
        if (!reservaCompromisoServices.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }
        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }
        if (reservaCompromiso.getPeriodo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");
        } else {
            tabla1 = true;
            tabla2 = false;
            tabla3 = false;
            //cargoUnidadUser
            unidadAdministrativa = clienteServices.getUnidadPrincipalUSer(userSession.getNameUser());
            System.out.println("unidadAdministrativa " + unidadAdministrativa.toString());
            reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
            //Distributivo d = clienteService.getuusuarioLogeado(user.getNameUser());
            List<Producto> listaAnaidir = new ArrayList<>();
            if (unidadAdministrativa != null) {
                listaAnaidir = reservaCompromisoServices.listadoProductoActividades(reservaCompromiso.getPeriodo(), unidadAdministrativa);
            }

            if (!listaAnaidir.isEmpty()) {
                listaProductos = listaAnaidir;
            }
            if (listaProductos.isEmpty()) {

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No existe datos");
                return;
            }
            PrimeFaces.current().executeScript("PF('Dlogo2').show()");
            PrimeFaces.current().ajax().update(":formDlogo2");
        }
    }

    public void enviarSolicitud(SolicitudReservaCompromiso s) {
        reservaCompromiso = s;
//        observacion.setEstado(true);
//        observacion.setFecCre(new Date());             tienes proceso
//        observacion.setTarea(tarea.getName());
//        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void edicionSolicitud(SolicitudReservaCompromiso s) {
        reservaCompromiso = new SolicitudReservaCompromiso();
        ocultarbutton = true;
        mostrarButton = false;
        listaGuardar = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        removerDetalle = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        procedimientoSeleccionado = s.getProcedimiento();

        listaeditable1 = reservaCompromisoServices.getEdicionDetalleProducto(s);
        listaeditable2 = reservaCompromisoServices.getEdicionDetallePDAndD(s);

        procedimientoRequisitoList = procedimientoRequisitoServices.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        listaeditable1.forEach((detalle1) -> {
            this.listaGlobalDetalleSolicitudCompromisos.add(detalle1);
        });

        listaeditable2.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicitudPartidas.add(detalle2);
        });

        panel1 = false;
        panel2 = true;

        listaGlobalDetalleSolicitudCompromisos.forEach((lista1) -> {
            lista1.setMontoDisponible(lista1.getActividadProducto().getMontoReformada().subtract(reservaCompromisoServices.getSumaEdicionProductos(lista1.getActividadProducto())).add(lista1.getMontoSolicitado()));

        });

        listaGlobalDetalleSolicitudPartidas.forEach((lis) -> {

            lis.setMontoDisponible(lis.getPresupuesto().getCodificado().subtract(reservaCompromisoServices.getSumaEdicionPresupuesto(lis.getPresupuesto())).add(lis.getMontoSolicitado()));
        });
        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update("formMain");
    }

    private void calcularTotales() {
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudCompromisos) {
            totalDisponible = totalDisponible.add(detalle.getMontoDisponible());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
        }
    }

    private void calcularTotalesPD() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
            totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
            totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
            totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
        }
    }

    public void eliminarSolicitudCompleta(SolicitudReservaCompromiso s) {
        reservaCompromisoServices.remove(s);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solcitud eliminada");
    }

    public void accionComponentesDistriuvtioPartida() {

        if (!reservaCompromisoServices.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 1) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);

        if (reservaCompromisoServices.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            if (reservaCompromiso.getPeriodo() == null && reservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

            } else {

                List<Presupuesto> listaPr = reservaCompromisoServices.getPresupuestoPartidasDandPD(reservaCompromiso.getPeriodo(), Arrays.asList("PD", "PDI", "PDA"));
                listaPresupuesto.clear();
                listaPresupuesto = new ArrayList<>();
                if (!listaPr.isEmpty()) {
                    listaPresupuesto = listaPr;
                }
                PrimeFaces.current().executeScript("PF('Dlogo3').show()");
                PrimeFaces.current().ajax().update(":formDlogo3");
            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + reservaCompromiso.getPeriodo());
        }

    }

    public void visualizarDetalleSolcitud(SolicitudReservaCompromiso s) {

        reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        this.procedimientoSeleccionado = s.getProcedimiento();

        procedimientoRequisitoList = procedimientoRequisitoServices.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        visualizacionSolicitud = reservaCompromisoServices.getListaDetlleSolciitud(s);
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : visualizacionSolicitud) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
        }
        PrimeFaces.current().executeScript("PF('DlgoVisualizacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacion");

    }

    public void solicitarReservas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {
            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            calcularTotales();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void eliminadoMemoriaDetalleSOlicitud(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudCompromisos.get(index));
        listaGlobalDetalleSolicitudCompromisos.remove(index);
        calcularTotales();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void solicitarReservasPartidas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudPartidas.add(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudPartidas.remove(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud));
            calcularTotalesPD();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void eliminarPArtidasDirectasAndDistributivo(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudPartidas.get(index));
        listaGlobalDetalleSolicitudPartidas.remove(index);
        calcularTotalesPD();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void CargarDatosBeneficiario() {
        if (reservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (reservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                proveedorLazy
                        = new LazyModel(Proveedor.class
                        );
                proveedorLazy.getFilterss().put("estado", Boolean.TRUE);
                proveedorLazy.setDistinct(false);
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new LazyModel(Servidor.class);
                servidorLazy.setDistinct(false);
            }
        } else {
            PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        }
    }

    public void AñadirBeneficiarioServidor(Servidor servidor) {
        reservaCompromiso.setBeneficiario(servidor.getPersona());
        reservaCompromiso.setTipoBeneficiario(Boolean.FALSE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        reservaCompromiso.setBeneficiario(provedor.getCliente());
        reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }
    //<editor-fold defaultstate="collapsed" desc="GET AND SET">

    public LazyModel<SolicitudReservaCompromiso> getSolicitudesReservaCompromiso() {
        return solicitudesReservaCompromiso;
    }

    public void setSolicitudesReservaCompromiso(LazyModel<SolicitudReservaCompromiso> solicitudesReservaCompromiso) {
        this.solicitudesReservaCompromiso = solicitudesReservaCompromiso;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(DetalleSolicitudCompromiso detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public boolean isPanelProveedor() {
        return panelProveedor;
    }

    public void setPanelProveedor(boolean panelProveedor) {
        this.panelProveedor = panelProveedor;
    }

    public boolean isPanelServidor() {
        return panelServidor;
    }

    public void setPanelServidor(boolean panelServidor) {
        this.panelServidor = panelServidor;
    }

    public List<DetalleSolicitudCompromiso> getVisualizacionSolicitud() {
        return visualizacionSolicitud;
    }

    public void setVisualizacionSolicitud(List<DetalleSolicitudCompromiso> visualizacionSolicitud) {
        this.visualizacionSolicitud = visualizacionSolicitud;
    }

    public List<Presupuesto> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<Presupuesto> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public boolean isVerififcar() {
        return verififcar;
    }

    public void setVerififcar(boolean verififcar) {
        this.verififcar = verififcar;
    }

    public List<UnidadAdministrativa> getUnidadFiltros() {
        return unidadFiltros;
    }

    public void setUnidadFiltros(List<UnidadAdministrativa> unidadFiltros) {
        this.unidadFiltros = unidadFiltros;
    }

    public List<CatalogoItem> getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(List<CatalogoItem> estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public ReservaCompromisoServices getReservaCompromisoServices() {
        return reservaCompromisoServices;
    }

    public void setReservaCompromisoServices(ReservaCompromisoServices reservaCompromisoServices) {
        this.reservaCompromisoServices = reservaCompromisoServices;
    }

    public CatalogoItemServices getCatalogoItemServices() {
        return catalogoItemServices;
    }

    public void setCatalogoItemServices(CatalogoItemServices catalogoItemServices) {
        this.catalogoItemServices = catalogoItemServices;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public boolean isOcultarbutton() {
        return ocultarbutton;
    }

    public void setOcultarbutton(boolean ocultarbutton) {
        this.ocultarbutton = ocultarbutton;
    }

    public boolean isMostrarButton() {
        return mostrarButton;
    }

    public void setMostrarButton(boolean mostrarButton) {
        this.mostrarButton = mostrarButton;
    }

    public boolean isTabla1() {
        return tabla1;
    }

    public void setTabla1(boolean tabla1) {
        this.tabla1 = tabla1;
    }

    public boolean isTabla2() {
        return tabla2;
    }

    public void setTabla2(boolean tabla2) {
        this.tabla2 = tabla2;
    }

    public boolean isTabla3() {
        return tabla3;
    }

    public void setTabla3(boolean tabla3) {
        this.tabla3 = tabla3;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public ClienteServices getClienteServices() {
        return clienteServices;
    }

    public void setClienteServices(ClienteServices clienteServices) {
        this.clienteServices = clienteServices;
    }

    public List<DetalleSolicitudCompromiso> getListaGuardar() {
        return listaGuardar;
    }

    public void setListaGuardar(List<DetalleSolicitudCompromiso> listaGuardar) {
        this.listaGuardar = listaGuardar;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudCompromisos() {
        return listaGlobalDetalleSolicitudCompromisos;
    }

    public void setListaGlobalDetalleSolicitudCompromisos(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos) {
        this.listaGlobalDetalleSolicitudCompromisos = listaGlobalDetalleSolicitudCompromisos;
    }

    public List<DetalleSolicitudCompromiso> getRemoverDetalle() {
        return removerDetalle;
    }

    public void setRemoverDetalle(List<DetalleSolicitudCompromiso> removerDetalle) {
        this.removerDetalle = removerDetalle;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudPartidas() {
        return listaGlobalDetalleSolicitudPartidas;
    }

    public void setListaGlobalDetalleSolicitudPartidas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas) {
        this.listaGlobalDetalleSolicitudPartidas = listaGlobalDetalleSolicitudPartidas;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable1() {
        return listaeditable1;
    }

    public void setListaeditable1(List<DetalleSolicitudCompromiso> listaeditable1) {
        this.listaeditable1 = listaeditable1;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable2() {
        return listaeditable2;
    }

    public void setListaeditable2(List<DetalleSolicitudCompromiso> listaeditable2) {
        this.listaeditable2 = listaeditable2;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public List<SolicitudRequisito> getSolicitudRequisitoList() {
        return solicitudRequisitoList;
    }

    public void setSolicitudRequisitoList(List<SolicitudRequisito> solicitudRequisitoList) {
        this.solicitudRequisitoList = solicitudRequisitoList;
    }

    public Procedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(Procedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    public BigDecimal getTotalComprometidoPD() {
        return totalComprometidoPD;
    }

    public void setTotalComprometidoPD(BigDecimal totalComprometidoPD) {
        this.totalComprometidoPD = totalComprometidoPD;
    }

    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalDisponiblePD() {
        return totalDisponiblePD;
    }

    public void setTotalDisponiblePD(BigDecimal totalDisponiblePD) {
        this.totalDisponiblePD = totalDisponiblePD;
    }

    public BigDecimal getTotalSolicitadoPD() {
        return totalSolicitadoPD;
    }

    public void setTotalSolicitadoPD(BigDecimal totalSolicitadoPD) {
        this.totalSolicitadoPD = totalSolicitadoPD;
    }

    public boolean isRegistraNuevoVerificar() {
        return registraNuevoVerificar;
    }

    public void setRegistraNuevoVerificar(boolean registraNuevoVerificar) {
        this.registraNuevoVerificar = registraNuevoVerificar;
    }

    public ProcedimientoRequisitoServices getProcedimientoRequisitoServices() {
        return procedimientoRequisitoServices;
    }

    public void setProcedimientoRequisitoServices(ProcedimientoRequisitoServices procedimientoRequisitoServices) {
        this.procedimientoRequisitoServices = procedimientoRequisitoServices;
    }

    public SolicitudRequisitoServices getSolicitudRequisitoService() {
        return solicitudRequisitoService;
    }

    public void setSolicitudRequisitoService(SolicitudRequisitoServices solicitudRequisitoService) {
        this.solicitudRequisitoService = solicitudRequisitoService;
    }

    public DocumentosModel getData() {
        return data;
    }

    public void setData(DocumentosModel data) {
        this.data = data;
    }

    public List<Procedimiento> getProcedimientoList() {
        return procedimientoList;
    }

    public void setProcedimientoList(List<Procedimiento> procedimientoList) {
        this.procedimientoList = procedimientoList;
    }

    public ProcedimientoService getProcedimientoService() {
        return procedimientoService;
    }

    public void setProcedimientoService(ProcedimientoService procedimientoService) {
        this.procedimientoService = procedimientoService;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    } 
    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }
     public Boolean getDocumentoRecibdo() {
        return documentoRecibdo;
    }

    public void setDocumentoRecibdo(Boolean documentoRecibdo) {
        this.documentoRecibdo = documentoRecibdo;
    }

//</editor-fold>

   
}
