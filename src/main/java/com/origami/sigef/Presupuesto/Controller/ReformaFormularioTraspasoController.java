/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Model.FormularioEmisionDatos;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reformaFormularioTraspasoView")
@ViewScoped
public class ReformaFormularioTraspasoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ActividadOperativaService actividadOperativaService;
    @Inject
    private ProductoService productoService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private ReservaCompromisoService solicitudService;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;
    private ReformaTraspaso reformaTraspaso;
    private OpcionBusqueda busqueda;
    private FormularioEmisionDatos formulario;
    private List<Producto> listaProducto;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    private String observaciones, referencia, motivacion, autorizado;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private Cliente clienteNotifacacion, clientePlanificacion;
    private List<ProformaPresupuestoPlanificado> showCodigoRepetidosList;
    private List<ProformaPresupuestoPlanificado> proformaReformaList;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<ActividadOperativa> listaActividadNuevo;

    private ActividadOperativa actividad;
    private Producto producto;
    private Boolean renderTabPAPP, renderedPartidaDirecta, renderBtnImpPDI;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listaProducto = new ArrayList<>();
                listaPartidaDirecta = new ArrayList<>();
                reformaTraspaso = new ReformaTraspaso();
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                busqueda = new OpcionBusqueda();
                if (!listaPartidaDirecta.isEmpty()) {
                    renderBtnImpPDI = true;
                } else {
                    renderBtnImpPDI = false;
                }
                listaPartidaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                formulario = new FormularioEmisionDatos();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarCorreo(String email, String asunto, String userStart, ReformaTraspaso r) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE LA REFORMA CON NO." + r.getCodigo() + " HA SIDO APROBADA"
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + clienteNotifacacion.getNombreCompleto());
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId()));
        for (Producto item : listaProducto) {
            if (item.getEstado()) {
                totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
            }
        }
        listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        if (!listaPartidaDirecta.isEmpty()) {
            for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
            }
        }
        if (incremento) {
            return totalTraspasoIncrementoR;
        } else {
            return totalTraspasoReduccionR;
        }
    }

    public Boolean renderBtnImpPDI(ReformaTraspaso r) {
        listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(r.getPeriodo(), BigInteger.valueOf(r.getId()));
        if (!listaPartidaDirecta.isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean renderBtnImpPAPP(ReformaTraspaso r) {
        listaProducto = obtProdUniRespRefor(r.getUnidadRequiriente().getId(), BigInteger.valueOf(r.getId()));
        if (!listaProducto.isEmpty()) {
            return true;
        }
        return false;
    }

    public void generateReport(ReformaTraspaso r) {
        reformaTraspaso = r;
        String autorizado1 = "", autorizado2 = "", alcaldeCanton = "", alcalde = "";
        autorizado1 = valoresService.findByCodigo("txt_autorizado1_reformaT1");
        autorizado2 = valoresService.findByCodigo("txt_autorizado2_reformaT1");
        autorizado = autorizado1 + "\n" + autorizado2;
        motivacion = reformaTraspasoService.obtieneConceptoTramite(reformaTraspaso);
        alcaldeCanton = valoresService.findByCodigo("txt_alcalde");
        alcalde = reformaTraspasoService.getClienteAlcalde();
        ss.addParametro("id_reforma", r.getId());
        ss.addParametro("codigo_reforma", r.getCodigo());
        ss.addParametro("periodo", r.getPeriodo());
        ss.addParametro("fecha_traspaso_reforma", r.getFechaAprobacion());
        ss.addParametro("unidad_solicitante", r.getUnidadRequiriente().getNombre());
        ss.addParametro("rolAlcalde", alcaldeCanton);
        ss.addParametro("elabPresupuesto", r.getUsuarioCreacion());
        ss.addParametro("revFinan", "");
        ss.addParametro("nombreAlcalde", alcalde);
        PrimeFaces.current().executeScript("PF('dlgFormularioReforma').show()");
        JsfUtil.update("frmFormularioReforma");
    }

    public void imprimirFormularioReforma() {
        JSONObject valor = new JSONObject();
        valor.put("AUTORIZADO", autorizado);
        valor.put("REFERENCIA", referencia);
        valor.put("MOTIVACION", motivacion);
        reformaTraspaso.setValorFormularioPdi(valor.toString());
        reformaTraspasoService.edit(reformaTraspaso);
        ss.addParametro("autorizado1", autorizado);
        ss.addParametro("referencia", referencia);
        ss.addParametro("motivacion", motivacion);
        ss.setNombreReporte("reformaTraspasoT1PDI");
        ss.setNombreSubCarpeta("reformasPresupuesto");
        PrimeFaces.current().executeScript("PF('dlgFormularioReforma').hide()");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void setearValorInforme() {
        String valor = valoresService.findByCodigo("HTML_INFORME_PERTINENCIA");
        formulario.setParrafoUno(valor);
        reformaTraspaso.setValorInforme(valor);
    }

    public void realizarFormulario(ReformaTraspaso r, Boolean prueba) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        formulario = new FormularioEmisionDatos();
        if (r.getTipo()) {
            formulario.setNum("No." + r.getNumeracion() + "-TRASPASO TIPO 1-" + r.getPeriodo());
        } else {
            formulario.setNum("No." + r.getNumeracion() + "-TRASPASO TIPO 2-" + r.getPeriodo());
        }

        String nombre1 = clienteService.getrolsUser(RolUsuario.presupuesto);
        if (nombre1 == null) {
            formulario.setNombrePresupuesto("");
            formulario.setCargoPresupuesto("");
        } else {
            Distributivo dist1 = clienteService.getuusuarioLogeado(nombre1);
            if (dist1 != null) {
                formulario.setNombrePresupuesto(dist1.getServidorPublico().getPersona().getNombreCompleto());
                formulario.setCargoPresupuesto(dist1.getCargo().getNombreCargo());
            } else {
                formulario.setNombrePresupuesto("");
                formulario.setCargoPresupuesto("");
            }
        }

        String nombre2 = clienteService.getrolsUser(RolUsuario.financiero);

        if (nombre2 == null) {
            formulario.setNombreFinanciero("");
            formulario.setCargoFinanciero("");
        } else {
            Distributivo dist2 = clienteService.getuusuarioLogeado(nombre2);
            if (dist2 != null) {
                formulario.setNombreFinanciero(dist2.getServidorPublico().getPersona().getNombreCompleto());
                formulario.setCargoFinanciero(dist2.getCargo().getNombreCargo());
            } else {
                formulario.setNombreFinanciero("");
                formulario.setCargoFinanciero("");
            }

        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        formulario.setFecha(simpleDateFormat.format(reformaTraspaso.getFechaTraspasoReforma()));
        if (prueba) {
            PrimeFaces.current().executeScript("PF('dlgFormulario').show()");
            PrimeFaces.current().ajax().update(":frmFormulario");
        } else {
            if (reformaTraspaso.getValorInforme() == null) {
                setearValorInforme();
            }
            PrimeFaces.current().executeScript("PF('dlgFormularioPer').show()");
            //PrimeFaces.current().ajax().update(":frmFormularioPer");
        }

    }

    public void imprimirInformePertinencia(Boolean prueba) {
        if (prueba) {
            ss.addParametro("num", formulario.getNum());
            ss.addParametro("fecha", formulario.getFecha());
            ss.addParametro("parrafouno", formulario.getParrafoUno());
            ss.addParametro("parrafodos", formulario.getParrafoDos());
            ss.addParametro("parrafotres", formulario.getParrafoTres());
            ss.addParametro("parrafocuatro", formulario.getParrafoCuatro());
            ss.addParametro("parrafocinco", formulario.getParrafoCinco());
            ss.addParametro("nombrePresupuesto", formulario.getNombrePresupuesto());
            ss.addParametro("cargoPresupuesto", formulario.getCargoPresupuesto());
            ss.addParametro("tipoPresupuesto", "Presupuesto");
            ss.addParametro("nombreFinanciero", formulario.getNombreFinanciero());
            ss.addParametro("cargoFinanciero", formulario.getCargoFinanciero());
            ss.addParametro("tipoFinanciero", "Financiero");
            ss.setNombreReporte("refromaGeneralPertinencia");
        } else {
            if (reformaTraspaso.getValorInforme() != null) {
                ss.addParametro("parrafouno", reformaTraspaso.getValorInforme());
                reformaTraspasoService.edit(reformaTraspaso);
            }
            ss.addParametro("num", formulario.getNum());
            ss.addParametro("fecha", formulario.getFecha());
//            ss.addParametro("parrafouno", formulario.getParrafoUno());
            ss.addParametro("nombrePresupuesto", formulario.getNombrePresupuesto());
            ss.addParametro("cargoPresupuesto", formulario.getCargoPresupuesto());
            ss.addParametro("nombreFinanciero", formulario.getNombreFinanciero());
            ss.addParametro("cargoFinanciero", formulario.getCargoFinanciero());
            ss.setNombreReporte("reformaGeneralPertinenciaTraspaso");
        }

        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }

    public void imprimirAnexos(int a) {
        switch (a) {
            case 1:
                ss.addParametro("id", reformaTraspaso.getId());
                ss.setNombreReporte("reformaTraspasoT1AnexoUno");
                break;
            default:
                ss.addParametro("id", reformaTraspaso.getId());
                ss.setNombreReporte("reformaTraspasoT1PAPP");
                break;
        }
        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        renderTabPAPP = !listaProducto.isEmpty();
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
    }

    public void dlogoObservaciones(ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        if (busqueda.getAnio() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Eliga un Período");
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
            String usuario_planificacion = clienteService.getrolsUser(RolUsuario.directorPlanificacion);
            clienteNotifacacion = solicitudService.devuelveClienteNotitfacion(BigInteger.valueOf(tramite.getNumTramite()));
            if (!listaProducto.isEmpty()) {
                if (!usuario_planificacion.equals(null) && !usuario_planificacion.equals("") && !usuario_planificacion.equals(" ")) {
                    clientePlanificacion = clienteService.obtClienteByUsuario(usuario_planificacion);
                    enviarCorreo(clientePlanificacion.getEmail(), "REFORMA TRASPASO TIPO 1", clientePlanificacion.getNombreCompleto(), reformaTraspaso);
                }
            }
            if (saveTramite() == null) {
                return;
            }
            enviarCorreo(clienteNotifacacion.getEmail(), "REFORMA TRASPASO TIPO 1", clienteNotifacacion.getNombreCompleto(), reformaTraspaso);
            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void agruparActualizarProformaReforma(ReformaTraspaso r) {
        proformaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaTraspasoService.getPappGroupReforma(r.getPeriodo(), r);
        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (!listaAgrupacionPapp.isEmpty()) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO ob : listaAgrupacionPapp) {

                proformaPresupuesto.setValor(ob.getTotal());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setPartidaPresupuestaria(ob.getPartida());
                proformaPresupuesto.setItemPresupuestario(ob.getItemPresupuestario());
                proformaPresupuesto.setEstructuraProgramatica(ob.getEstructuraProgramatica());
                proformaPresupuesto.setFuente(ob.getFuente());
                proformaPresupuesto.setFuenteDirecta(ob.getFuenteDirecta());
                proformaPresupuesto.setCodigo("PAPP");
                proformaPresupuesto.setReformaSuplemento(ob.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(ob.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(ob.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(ob.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getTraspasoIncremento().add(proformaPresupuesto.getReformaSuplemento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
            }

        }
//</editor-fold>
        //VERIFICACION DE CODIGOS REPETIDOS
        if (!showCodigoRepetidosList.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "HAY PARTIDAS QUE SE REPITEN POR FAVOR CORRIJA");
            return;
        }
        //<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO PROFORMA">
        List<ProformaPresupuestoPlanificado> proformaVieja = reformaTraspasoService.proformaVieja(r.getPeriodo());
        int count = 0;
        if (!proformaVieja.isEmpty() && !proformaReformaList.isEmpty()) {
            for (ProformaPresupuestoPlanificado x : proformaVieja) {
                for (ProformaPresupuestoPlanificado y : proformaReformaList) {
                    ProformaPresupuestoPlanificado editProforma = new ProformaPresupuestoPlanificado();
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {

                        editProforma = reformaTraspasoService.editProformavieja(x);
                        editProforma.setReformaSuplemento(y.getReformaSuplemento().add(x.getReformaSuplemento()));
                        editProforma.setReformaReduccion(y.getReformaReduccion().add(x.getReformaReduccion()));
                        editProforma.setTraspasoIncremento(y.getTraspasoIncremento().add(x.getTraspasoIncremento()));
                        editProforma.setTraspasoReduccion(y.getTraspasoReduccion().add(x.getTraspasoReduccion()));
                        editProforma.setReformaCodificado(editProforma.getValor().add(editProforma.getTraspasoIncremento()).subtract(editProforma.getTraspasoReduccion()));
                        editProforma.setFechaModificacion(new Date());
                        editProforma.setUsuarioModificacion(userSession.getNameUser());
                        proformaService.edit(editProforma);
                        count = count + 1;
                    }
                }
            }
        }
        if (!proformaVieja.isEmpty() && !proformaReformaList.isEmpty()) {
            count = 0;
            boolean verificador = true;
            ProformaPresupuestoPlanificado editPro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado x : proformaReformaList) {
                editPro = new ProformaPresupuestoPlanificado();
                verificador = true;
                for (ProformaPresupuestoPlanificado y : proformaVieja) {
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {
                        verificador = false;
                    }
                }
                if (verificador) {
                    editPro = Utils.clone(x);
                    editPro.setId(null);
                    editPro.setReformaCodificado(editPro.getValor().add(editPro.getReformaSuplemento()).subtract(editPro.getReformaReduccion()));
                    editPro.setFechaModificacion(new Date());
                    editPro.setUsuarioModificacion(userSession.getNameUser());
                    editPro.setCodigoReforma(null);
                    editPro.setCodigoReformaTraspaso(null);
                    editPro.setValor(BigDecimal.ZERO);
                    editPro = proformaService.create(editPro);
                    count = count + 1;
                }
            }
        }
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Acualizado la proforma");
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }
    
    
    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }

    public LazyModel<ReformaTraspaso> getLazyReformaTraspaso() {
        return lazyReformaTraspaso;
    }

    public void setLazyReformaTraspaso(LazyModel<ReformaTraspaso> lazyReformaTraspaso) {
        this.lazyReformaTraspaso = lazyReformaTraspaso;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<PartidasDistributivo> getListaPartidaDistributivo() {
        return listaPartidaDistributivo;
    }

    public void setListaPartidaDistributivo(List<PartidasDistributivo> listaPartidaDistributivo) {
        this.listaPartidaDistributivo = listaPartidaDistributivo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidaDistributivoAnexo() {
        return listaPartidaDistributivoAnexo;
    }

    public void setListaPartidaDistributivoAnexo(List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo) {
        this.listaPartidaDistributivoAnexo = listaPartidaDistributivoAnexo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidaDirecta() {
        return listaPartidaDirecta;
    }

    public void setListaPartidaDirecta(List<ProformaPresupuestoPlanificado> listaPartidaDirecta) {
        this.listaPartidaDirecta = listaPartidaDirecta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public FormularioEmisionDatos getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioEmisionDatos formulario) {
        this.formulario = formulario;
    }

    public double getTotalTraspasoReduccion() {
        return totalTraspasoReduccion;
    }

    public void setTotalTraspasoReduccion(double totalTraspasoReduccion) {
        this.totalTraspasoReduccion = totalTraspasoReduccion;
    }

    public double getTotalTraspasoIncremento() {
        return totalTraspasoIncremento;
    }

    public void setTotalTraspasoIncremento(double totalTraspasoIncremento) {
        this.totalTraspasoIncremento = totalTraspasoIncremento;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public Boolean getRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(Boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public Boolean getRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(Boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

    public Boolean getRenderBtnImpPDI() {
        return renderBtnImpPDI;
    }

    public void setRenderBtnImpPDI(Boolean renderBtnImpPDI) {
        this.renderBtnImpPDI = renderBtnImpPDI;
    }

//</editor-fold>
}
