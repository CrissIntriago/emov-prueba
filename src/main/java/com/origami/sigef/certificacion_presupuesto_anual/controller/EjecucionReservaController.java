/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ContratoReservaService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author alexa
 */
@Named(value = "ejecucionReservaView")
@ViewScoped
public class EjecucionReservaController implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ReservaCompromisoService solicitudService;
    @Inject
    private DetalleReservaCompromisoService detalleSolicitudService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ContratoReservaService contratoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private MasterCatalogoService periodoService;

    private SolicitudReservaCompromiso solicitudReservaCompromiso;
    private DetalleSolicitudCompromiso detalleSolicitudReserva;
    private LazyModel<SolicitudReservaCompromiso> lazy;
    private List<DetalleSolicitudCompromiso> listaSolicitudes;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;
    private String fileName;
    private Cliente beneficiario;
    private boolean panelProveedor, panelServidor;
    private LazyModel<Proveedor> proveedorLazy;
    private LazyModel<Servidor> servidorLazy;
    private Boolean tipoContrato;
    private Boolean tablaContrato;
    private List<Adquisiciones> listaAqusionesSeleccionadas;
    private List<ContratosReservaEjecuion> listaAqusicionesGuardar;
    private List<CatalogoItem> tiposAdquisicion;
    private ContratosReservaEjecuion contratos;
    private List<ContratosReservaEjecuion> listaDatos;
    private Boolean renderComponentes;
    private Long idEntity;
    private List<CatalogoItem> estadoFiltros;
    private List<UnidadAdministrativa> unidadFiltros;

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalPresupuestado;

    private OpcionBusqueda opcionBusqueda;
    private List<Short> listaPeriodo;

    @PostConstruct
    public void inicializar() {
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.beneficiario = new Cliente();
        this.detalleSolicitudReserva = new DetalleSolicitudCompromiso();
        opcionBusqueda = new OpcionBusqueda();
        this.listaPeriodo = solicitudService.listaAniosAprobados(Boolean.TRUE);
        if (listaPeriodo != null) {
            int indice = listaPeriodo.size();
            if (!listaPeriodo.isEmpty() && indice == 1) {
                opcionBusqueda.setAnio(listaPeriodo.get(0));
            } else {
                opcionBusqueda.setAnio(listaPeriodo.get(indice - 1));
            }
        }
        actualizarListadoReservas();
        this.listaSolicitudes = new ArrayList<>();
        this.solicitudRequisitoList = new ArrayList<>();
        this.procedimientoRequisitoList = new ArrayList<>();
        this.procedimientoSeleccionado = new Procedimiento();
        panelProveedor = true;
        panelServidor = false;
        tipoContrato = Boolean.TRUE;
        tablaContrato = Boolean.FALSE;
        renderComponentes = Boolean.TRUE;
        this.listaAqusionesSeleccionadas = new ArrayList<>();
        this.listaAqusicionesGuardar = new ArrayList<>();
        this.tiposAdquisicion = adquisicionesService.getTipos("tipo_adquisicion");
        this.contratos = new ContratosReservaEjecuion();
        this.listaDatos = new ArrayList<>();
        estadoFiltros = new ArrayList<>();
        estadoFiltros = catalogoService.getItemsByCatalogo("estado_solicitud");
        unidadFiltros = new ArrayList<>();
        unidadFiltros = solicitudService.getListaUnidadesReservas();
    }

    public void actualizarListadoReservas() {
        if (opcionBusqueda.getAnio() != null) {
            this.lazy = new LazyModel(SolicitudReservaCompromiso.class);
            this.lazy.getFilterss().put("estado.codigo", Arrays.asList("APRO"));
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            lazy.getFilterss().put("antiguo", false);
        } else {
            lazy = null;
        }
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();
        return a;
    }

    private void cargarDatosRegistrados(SolicitudReservaCompromiso reservaCompromiso) {

        idEntity = 0L;
        tipoContrato = Boolean.TRUE;
        tablaContrato = Boolean.FALSE;
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = reservaCompromiso;
        this.procedimientoSeleccionado = reservaCompromiso.getProcedimiento();
        this.listaDatos = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        listaAqusicionesGuardar = new ArrayList<>();
        this.listaSolicitudes = new ArrayList<>();
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(reservaCompromiso);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((reserva) -> {
                solicitudRequisitoList.add(reserva);
            });
        }
        this.listaSolicitudes = solicitudService.getListaDetlleSolciitud(reservaCompromiso);

        listaDatos = contratoService.getListaContratos(solicitudReservaCompromiso);

        if (listaDatos.size() > 0) {
            for (ContratosReservaEjecuion listaDato : listaDatos) {
                listaAqusicionesGuardar.add(listaDato);

            }

            if (listaAqusicionesGuardar.size() > 0) {
                tipoContrato = Boolean.FALSE;
                tablaContrato = Boolean.TRUE;
            }
        }

    }

    public void visualizarSolicitudes(SolicitudReservaCompromiso reservaCompromiso) {
        cargarDatosRegistrados(reservaCompromiso);
        PrimeFaces.current().executeScript("PF('DlgoVisualizacionAprobacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionAprobacion");
    }

    public void aprobarSolicitud(boolean aprobar, SolicitudReservaCompromiso s) {

        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        int result = 0;
        int result2 = 0;

        this.solicitudReservaCompromiso = s;
        this.solicitudReservaCompromiso.setComentario("");

        if (aprobar) {
            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("APRO", "estado_solicitud"));
            solicitudService.edit(solicitudReservaCompromiso);

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " aprobada con éxito");

            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);

            if (!listP.isEmpty()) {
                for (Producto pr : listP) {

                    valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                    result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                    if (result > 0) {

                    }
                }
            }

            List<Presupuesto> lis = solicitudService.listaPresupuestoPDI(periodo);
            if (!lis.isEmpty()) {
                for (Presupuesto li : lis) {
                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                    solicitudService.updatePDIReservas(valor2, periodo, li);

                }
            }

        } else {
            PrimeFaces.current().executeScript("PF('DlgoObservacion').show()");
            PrimeFaces.current().ajax().update(":formObservacion");
        }

    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    setFileName("/resources/demo/media/" + pr.getUrl());
                    break;
                } else {
                    setFileName("");
                }
            }
            if (getFileName().equals("")) {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void observacion() {
        int resultado = 0;
        int result = 0;

        if (this.solicitudReservaCompromiso.getComentario().length() > 0) {

            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("O", "estado_solicitud"));
            solicitudService.edit(solicitudReservaCompromiso);
            PrimeFaces.current().executeScript("PF('DlgoObservacion').hide()");
            PrimeFaces.current().ajax().update(":formObservacion");

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + "se ha notificada con extio");

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No deje los campos vacios");
        }

    }

    public void actualizarSolicitudDeReservaAprobada(SolicitudReservaCompromiso reservaCompromiso) {
        boolean verificando = solicitudRequisitoService.getVerificacionContabilizado(reservaCompromiso);

        if (verificando) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "ESTA RESERVA ESTA CONTABILIDAZA");
            return;
        }
        cargarDatosRegistrados(reservaCompromiso);
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(reservaCompromiso.getProcedimiento());

        calcularTotales(true);
        PrimeFaces.current().executeScript("PF('DlgoActualizacionReserva').show()");
        PrimeFaces.current().ajax().update(":formActualizacionReserva");
    }

    public void saveActualizacionSolicitud() {
        boolean verificando = solicitudRequisitoService.getVerificacionContabilizado(solicitudReservaCompromiso);
        if (verificando || solicitudReservaCompromiso.getContabilizado()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "ESTA RESERVA ESTA CONTABILIDAZA");
            return;
        }
        Boolean verificar = true;
        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        BigDecimal valor3 = BigDecimal.ZERO;
        int result = 0;
        int result2 = 0;
        int result3 = 0;
        
        // NUEVO
        if ((listaAqusicionesGuardar.isEmpty() || listaAqusicionesGuardar.size() == 0 || null == listaAqusicionesGuardar) && solicitudReservaCompromiso.getBeneficiario() == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe ingresar por los menos un beneficiario o un contrato".toUpperCase());
            return;
        }
        
        if (solicitudReservaCompromiso.getBeneficiario() == null) {
            if (listaAqusicionesGuardar.size() == 1) {
                for (ContratosReservaEjecuion data : listaAqusicionesGuardar) {
                    solicitudReservaCompromiso.setBeneficiario(data.getContrato().getProveedor().getCliente());
                    solicitudReservaCompromiso.setTipoBeneficiario(true);
                }
            } else if (listaAqusicionesGuardar.size() > 1) {
                solicitudReservaCompromiso.setBeneficiario(null);
            } else if (listaAqusicionesGuardar.isEmpty()) {
                if (!tipoContrato) {
                    JsfUtil.addWarningMessage("AVISO", "Debe ingresar por los menos un beneficiario o un contrato".toUpperCase());
                    return;
                }
            }
        } else {
            if (listaAqusicionesGuardar.size() == 1) {
                for (ContratosReservaEjecuion data : listaAqusicionesGuardar) {
                    solicitudReservaCompromiso.setBeneficiario(data.getContrato().getProveedor().getCliente());
                    solicitudReservaCompromiso.setTipoBeneficiario(true);
                }
            } else if (listaAqusicionesGuardar.size() > 1) {
                solicitudReservaCompromiso.setBeneficiario(null);

            }
        }
        solicitudReservaCompromiso.setUsuarioModificacion(userSession.getNameUser());
        solicitudReservaCompromiso.setFechaModificacion(new Date());
        //solicitudReservaCompromiso.setFechaActualizacion(new Date());
        solicitudReservaCompromiso.setComprometido(Boolean.TRUE);
        solicitudService.edit(solicitudReservaCompromiso);
        for (DetalleSolicitudCompromiso data : listaSolicitudes) {
            detalleSolicitudService.edit(data);
        }
        if (!tipoContrato) {
            contratoService.geteliminarContratos(solicitudReservaCompromiso);
            for (ContratosReservaEjecuion li : listaAqusicionesGuardar) {
                contratos = new ContratosReservaEjecuion();
                contratos.setContrato(li.getContrato());
                contratos.setReserva(solicitudReservaCompromiso);
                contratos = contratoService.create(contratos);
            }
        }
        List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(solicitudReservaCompromiso.getPeriodo());
        for (Producto pr : listP) {
            valor = solicitudService.sumaTotalCompromisoProducto(solicitudReservaCompromiso.getPeriodo(), pr.getId());
            result = solicitudService.updateComprometidoReservaProducto(valor, solicitudReservaCompromiso.getPeriodo(), pr.getId());
        }
        List<ThCargoRubros> lis = solicitudService.listaReservasSinRepetir(solicitudReservaCompromiso.getPeriodo());
        for (ThCargoRubros li : lis) {
            valor2 = solicitudService.sumaTotalComprometidoPresupuesto(solicitudReservaCompromiso.getPeriodo(), li.getId());
            result2 = solicitudService.actualizarComprometidoReservaPresupuesto(valor2, solicitudReservaCompromiso.getPeriodo(), li.getId());
        }

        List<ProformaPresupuestoPlanificado> tmpPd = solicitudService.listaReservasPdSinRepetir(solicitudReservaCompromiso.getPeriodo());

        if (tmpPd != null && !tmpPd.isEmpty()) {
            for (ProformaPresupuestoPlanificado li : tmpPd) {
                valor3 = solicitudService.sumaTotalComprometidoPresupuestoPdi(solicitudReservaCompromiso.getPeriodo(), li.getId());
                result3 = solicitudService.actualizarComprometidoReservaPresupuestoPd(valor3, solicitudReservaCompromiso.getPeriodo(), li.getId());

                if (result3 > 0) {

                }
            }
        }

        listaAqusicionesGuardar.clear();
        PrimeFaces.current().executeScript("PF('DlgoActualizacionReserva').hide()");
        JsfUtil.addSuccessMessage("INFORMACIÓN", "La solicitud de reserva de compromiso ha sido actualizada correctamente");
    }

    public void vizualizarSolicitudActualizada(SolicitudReservaCompromiso reservaCompromiso) {
        cargarDatosRegistrados(reservaCompromiso);
        calcularTotales(false);
        PrimeFaces.current().executeScript("PF('DlgoVisualizarActualizacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionActualizacion");
    }

    public void CargarDatosBeneficiario() {
        if (solicitudReservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (solicitudReservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                proveedorLazy = new LazyModel<>(Proveedor.class);
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new LazyModel<>(Servidor.class);
            }
        } else {
            PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        }
    }

    public void AñadirBeneficiarioServidor(Servidor servidor) {
        solicitudReservaCompromiso.setBeneficiario(servidor.getPersona());
        solicitudReservaCompromiso.setTipoBeneficiario(Boolean.FALSE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + solicitudReservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void insertarComprometido(DetalleSolicitudCompromiso detalle) {
        this.detalleSolicitudReserva = detalle;
        if (detalleSolicitudReserva.getMontoComprometido().doubleValue() > detalleSolicitudReserva.getMontoSolicitado().doubleValue()) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "El monto comprometido es mayor al monto solicitado");
            detalleSolicitudReserva.setMontoComprometido(BigDecimal.ZERO);
        }
        calcularTotales(true);
    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        solicitudReservaCompromiso.setBeneficiario(provedor.getCliente());
        solicitudReservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + solicitudReservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void ocultarTablacontratos() {
        if (tipoContrato) {
            tablaContrato = false;
            this.listaAqusicionesGuardar.clear();

        } else {
            tablaContrato = true;
            List<Adquisiciones> listaConsulta = adquisicionesService.getListaAquisionesValidadas(true);
            listaAqusionesSeleccionadas.clear();
            if (listaConsulta.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No hay datos");
                return;
            }

            for (Adquisiciones adquisiciones : listaConsulta) {
                listaAqusionesSeleccionadas.add(adquisiciones);
            }

            PrimeFaces.current().ajax().update(":formContratosAquisiciones");
            PrimeFaces.current().executeScript("PF('DlogoContratos').show()");

        }

    }

    public void seleccionAdquisicion(Adquisiciones a) {
        List<ContratosReservaEjecuion> verificarList = adquisicionesService.getListaAdquisionesNoOcupados(true, a);
        if (verificarList.size() > 0) {
            JsfUtil.addWarningMessage("Advertencia", "El Contrato ya es utilizado por otra Reserva");
            return;
        }

        BigDecimal valorSuma = BigDecimal.ZERO;
        contratos = new ContratosReservaEjecuion();
        contratos.setContrato(a);
        contratos.setReserva(null);

        contratos.setId(maximoLista() + 1);

        listaAqusicionesGuardar.add(contratos);
        listaAqusionesSeleccionadas.remove(a);

        if (listaAqusicionesGuardar.size() > 0) {
            for (ContratosReservaEjecuion data : listaAqusicionesGuardar) {
                valorSuma = valorSuma.add(data.getContrato().getValorContratado());
            }

            if (listaSolicitudes.size() > 0) {
                for (DetalleSolicitudCompromiso item : listaSolicitudes) {
                    if (valorSuma.doubleValue() > item.getMontoSolicitado().doubleValue()) {
                        item.setMontoComprometido(BigDecimal.ZERO);
                    } else {
                        item.setMontoComprometido(valorSuma);
                    }
                }

            }
        }

        if (listaAqusicionesGuardar.size() == 1) {
            if (listaAqusicionesGuardar.get(0).getContrato() != null) {
                solicitudReservaCompromiso.setBeneficiario(a.getProveedor().getCliente() == null ? new Cliente() : a.getProveedor().getCliente());
                solicitudReservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
            }
        } else if (listaAqusicionesGuardar.size() > 1) {
            solicitudReservaCompromiso.setBeneficiario(null);
            solicitudReservaCompromiso.setTipoBeneficiario(null);
        }

        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Exitos", "Registro Correctamente Registrado");
    }

    public Long maximoLista() {
        Long a = 0L;
        if (listaAqusicionesGuardar.size() > 0) {
            a = listaAqusicionesGuardar.stream().mapToLong(i -> i.getId()).max().getAsLong();

        }

        return a;

    }

    public void eliminarContrato(ContratosReservaEjecuion a) {
        BigDecimal valorSuma = BigDecimal.ZERO;
        if (a.getId() != null) {
            contratoService.remove(a);
        }
        listaAqusicionesGuardar.remove(a);
        if (listaAqusicionesGuardar.size() > 0) {
            for (ContratosReservaEjecuion data : listaAqusicionesGuardar) {
                valorSuma = valorSuma.add(data.getContrato().getValorContratado());
            }
            if (listaSolicitudes.size() > 0) {
                for (DetalleSolicitudCompromiso item : listaSolicitudes) {
                    if (valorSuma.doubleValue() > item.getMontoSolicitado().doubleValue()) {
                        item.setMontoComprometido(BigDecimal.ZERO);
                    } else {
                        item.setMontoComprometido(valorSuma);
                    }
                }
            }
        }
        calcularTotales(true);
        if (listaAqusicionesGuardar.size() == 1) {
            if (listaAqusicionesGuardar.get(0).getContrato() != null) {
                solicitudReservaCompromiso.setBeneficiario(listaAqusicionesGuardar.get(0).getContrato().getProveedor().getCliente() == null ? new Cliente() : listaAqusicionesGuardar.get(0).getContrato().getProveedor().getCliente());
                solicitudReservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
            }
        } else if (listaAqusicionesGuardar.size() > 1 || Utils.isEmpty(listaAqusicionesGuardar)) {
            solicitudReservaCompromiso.setBeneficiario(null);
            solicitudReservaCompromiso.setTipoBeneficiario(null);
        }
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Exitos", "Eliminado con éxito");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:dataaprobacion");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:dataaprobacion");
        }
    }

    private void calcularTotales(Boolean accion) {
        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalPresupuestado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaSolicitudes) {
            if (accion) {
                if (detalle.getActividadProducto() != null) {
                    totalPresupuestado = totalPresupuestado.add(detalle.getActividadProducto().getMontoReformada());
                } else if (detalle.getRefDistributivo() != null) {
                    totalPresupuestado = totalPresupuestado.add(detalle.getRefDistributivo().getReformaCodificado());
                } else if (detalle.getPartidasDirecta() != null) {
                    totalPresupuestado = totalPresupuestado.add(detalle.getPartidasDirecta().getReformaCodificado());
                }
            }
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public BigDecimal getTotalPresupuestado() {
        return totalPresupuestado;
    }

    public void setTotalPresupuestado(BigDecimal totalPresupuestado) {
        this.totalPresupuestado = totalPresupuestado;
    }

    public List<CatalogoItem> getEstadoFiltros() {
        return estadoFiltros;
    }

    public void setEstadoFiltros(List<CatalogoItem> estadoFiltros) {
        this.estadoFiltros = estadoFiltros;
    }

    public List<UnidadAdministrativa> getUnidadFiltros() {
        return unidadFiltros;
    }

    public void setUnidadFiltros(List<UnidadAdministrativa> unidadFiltros) {
        this.unidadFiltros = unidadFiltros;
    }

    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public Boolean getRenderComponentes() {
        return renderComponentes;
    }

    public void setRenderComponentes(Boolean renderComponentes) {
        this.renderComponentes = renderComponentes;
    }

    public Boolean getTablaContrato() {
        return tablaContrato;
    }

    public void setTablaContrato(Boolean tablaContrato) {
        this.tablaContrato = tablaContrato;
    }

    public List<CatalogoItem> getTiposAdquisicion() {
        return tiposAdquisicion;
    }

    public void setTiposAdquisicion(List<CatalogoItem> tiposAdquisicion) {
        this.tiposAdquisicion = tiposAdquisicion;
    }

    public List<Adquisiciones> getListaAqusionesSeleccionadas() {
        return listaAqusionesSeleccionadas;
    }

    public void setListaAqusionesSeleccionadas(List<Adquisiciones> listaAqusionesSeleccionadas) {
        this.listaAqusionesSeleccionadas = listaAqusionesSeleccionadas;
    }

    public List<ContratosReservaEjecuion> getListaAqusicionesGuardar() {
        return listaAqusicionesGuardar;
    }

    public void setListaAqusicionesGuardar(List<ContratosReservaEjecuion> listaAqusicionesGuardar) {
        this.listaAqusicionesGuardar = listaAqusicionesGuardar;
    }

    public Boolean getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Boolean tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public SolicitudReservaCompromiso getSolicitudReservaCompromiso() {
        return solicitudReservaCompromiso;
    }

    public void setSolicitudReservaCompromiso(SolicitudReservaCompromiso solicitudReservaCompromiso) {
        this.solicitudReservaCompromiso = solicitudReservaCompromiso;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitudReserva() {
        return detalleSolicitudReserva;
    }

    public void setDetalleSolicitudReserva(DetalleSolicitudCompromiso detalleSolicitudReserva) {
        this.detalleSolicitudReserva = detalleSolicitudReserva;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudReservaCompromiso> lazy) {
        this.lazy = lazy;
    }

    public List<DetalleSolicitudCompromiso> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<DetalleSolicitudCompromiso> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
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

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
//</editor-fold>
}
