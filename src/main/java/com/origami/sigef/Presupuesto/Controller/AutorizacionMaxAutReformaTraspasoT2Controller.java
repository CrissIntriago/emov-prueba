/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
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
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
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
 * @author Sandra Arroba
 */
@Named(value = "reformaT2AutorizacionMaxAutView")
@ViewScoped
public class AutorizacionMaxAutReformaTraspasoT2Controller extends BpmnBaseRoot implements Serializable {

    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;
    private Distributivo dataView;

    //PAPP
    private PlanLocalProgramaProyecto planLocal;
    private PlanAnualPoliticaPublica planPolitica;
    private PlanAnualProgramaProyecto planAnual;
    private ActividadOperativa actividad;
    private Producto producto;

    //PARTIDAS DISTRIBUTIVO
    private PartidasDistributivo partidaDistributivo;
    private PartidasDistributivoAnexo partidasDistributivoAnexo;
    private ProformaPresupuestoPlanificado partidasDirectas;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;

    private List<Distributivo> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaVista;

    private List<ProformaPresupuestoPlanificado> proformaReformaList;
    private List<ProformaPresupuestoPlanificado> showCodigoRepetidosList;

    private List<PlanLocalProgramaProyecto> listaPlanLocalOriginal;
    private List<PlanLocalProgramaProyecto> listaPlanLocalNuevo;

    private List<PlanAnualPoliticaPublica> listaPlanPoliticaOriginal;
    private List<PlanAnualPoliticaPublica> listaPlanPoliticaNuevo;

    private List<PlanAnualProgramaProyecto> listaPlanAnualOriginal;
    private List<PlanAnualProgramaProyecto> listaPlanAnualNuevo;

    private List<ActividadOperativa> listaActividadOriginal;
    private List<ActividadOperativa> listaActividadNuevo;

    private List<Producto> listaProdutoOriginal;
    private List<Producto> listaProductoNuevo;

    private List<PartidasDistributivo> listaPartidasDistributivoNuevo;
    private List<PartidasDistributivoAnexo> listaPartidasDistributivoAnexoNuevo;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private List<ProformaPresupuestoPlanificado> profromaReformaList;

    private String observaciones;
    private Cliente clienteNotifacacion;
    private BigDecimal rmu;

    private boolean vistaPartidaDisGeneral;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;
    private boolean btnAprobar, btnRechazar;

    @Inject
    private ReformaTraspasoService reformaTraspasoService;

    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;

    @Inject
    private UserSession userSession;

    @Inject
    private ClienteService clienteService;

    @Inject
    private CatalogoService catalogoService;

    @Inject
    private PlanProgramaticoService planProgramaticoService;

    @Inject
    private ProductoService productoService;

    @Inject
    private ActividadOperativaService actividadOperativaService;

    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;

    @Inject
    private KatalinaService katalinaService;

    @Inject
    private ReservaCompromisoService solicitudService;

    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    @Inject
    private PartidaDistributivoService partidasDistributivoService;
    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;

    @Inject
    private ValoresDistributivoService valoresService;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reformaTraspaso = new ReformaTraspaso();
                detalleReformaTraspaso = new DetalleReformaTraspaso();
                busqueda = new OpcionBusqueda();
                dataView = new Distributivo();

                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");

                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                listaPlanProgramatico = new ArrayList<>();
                clienteNotifacacion = new Cliente();

                listaProducto = new ArrayList<>();
                listaPartidaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                listaPartidaDirecta = new ArrayList<>();

                listaPartidasDistributivoNuevo = new ArrayList<>();
                listaPartidasDistributivoAnexoNuevo = new ArrayList<>();

                profromaReformaList = new ArrayList<>();
                showCodigoRepetidosList = new ArrayList<>();
                btnAprobar = false;
                btnRechazar = false;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }

            for (Producto item : listaProducto) {

                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
        } else {
            busqueda.setAnio(reforma.getPeriodo());
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }

            if (!listaProducto.isEmpty()) {
                for (Producto item : listaProducto) {

                    if (item.getEstado()) {
                        totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                        totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                        totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                        totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                    }
                }
            }
            if (!listaPartidaDistributivo.isEmpty()) {
                for (PartidasDistributivo listaRubro : listaPartidaDistributivo) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaRubro.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaRubro.getTraspasoReduccion().doubleValue();
                }
            }
            if (!listaPartidaDistributivoAnexo.isEmpty()) {
                for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistributivoAnexo) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaAnexo.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaAnexo.getTraspasoReduccion().doubleValue();
                }
            }
            if (!listaPartidaDirecta.isEmpty()) {
                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
                }
            }
        }

        if (incremento) {
            return totalTraspasoIncrementoR;
        } else {
            return totalTraspasoReduccionR;
        }
    }

    public boolean enviar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REGP-REF");

        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " enviada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
        return true;
    }

    public boolean verificarRepetidosProformaReforma(ReformaTraspaso r) {
        Boolean verificar = true;
        BigInteger integer = BigInteger.valueOf(r.getId());
        proformaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaTraspasoService.getPappGroupReforma(r.getPeriodo(), r);
        List<ProformaPDTO> listaagrupacionDistr = reformaTraspasoService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidasDistributivoAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaTraspasoService.showPartidaDirectasReforma(r.getPeriodo(), r);

        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (Utils.isNotEmpty(listaAgrupacionPapp)) {
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
        //<editor-fold defaultstate="collapsed" desc="AGRUPAR DISTRIBUTIVO">
        if (Utils.isNotEmpty(listaagrupacionDistr)) {

            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO i : listaagrupacionDistr) {

                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (i.getPartida() == null ? data.getPartidaPresupuestaria() == null : i.getPartida().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(i.getPartida());
                proformaPresupuesto.setValor(i.getTotal());
                proformaPresupuesto.setReformaSuplemento(i.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(i.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(i.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(i.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                proformaPresupuesto.setEstructuraProgramatica(i.getEstructuraProgramatica());
                proformaPresupuesto.setItemPresupuestario(i.getItemPresupuestario());
                proformaPresupuesto.setFuente(i.getFuente());
                proformaPresupuesto.setFuenteDirecta(i.getFuenteDirecta());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PD");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
        if (Utils.isNotEmpty(listaagrupacionDistrAnexo)) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (p.getPartidaAp() == null ? data.getPartidaPresupuestaria() == null : p.getPartidaAp().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
                proformaPresupuesto.setValor(p.getMonto());
                proformaPresupuesto.setEstructuraProgramatica(p.getEstructuraApA());
                proformaPresupuesto.setItemPresupuestario(p.getItemApA());
                proformaPresupuesto.setFuente(p.getFuenteApA());
                proformaPresupuesto.setFuenteDirecta(p.getFuenteDirectaA());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setTraspasoIncremento(p.getTraspasoIncremento());
                proformaPresupuesto.setTraspasoReduccion(p.getTraspasoReduccion());
                proformaPresupuesto.setReformaSuplemento(p.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(p.getReformaReduccion());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento().add(proformaPresupuesto.getTraspasoIncremento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PDA");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
        if (Utils.isNotEmpty(listaPartidasdirectas)) {

            for (ProformaPresupuestoPlanificado k : listaPartidasdirectas) {

                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (k.getPartidaPresupuestaria() == null ? data.getPartidaPresupuestaria() == null : k.getPartidaPresupuestaria().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaReformaList.add(k);

            }

        }
//</editor-fold>
        //VERIFICACION DE CODIGOS REPETIDOS
        if (!showCodigoRepetidosList.isEmpty()) {
            verificar = false;
        }
        return verificar;
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta(reforma.getPeriodo(), true, BigInteger.valueOf(reforma.getId()));
        if (listaProducto.isEmpty()) {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "APRT", true, BigInteger.valueOf(reforma.getId()));
        }
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        busqueda.setAnio(reformaTraspaso.getPeriodo());

        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();

        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        dataView = d;
        //  setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        if (vista) {
            vistaPartidaDisGeneral = true;
        } else {
            vistaPartidaDisGeneral = false;
        }
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void enviarCorreo(String email, String asunto, String mensaje, String userStart, ReformaTraspaso r) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA QUE La REFORMA CON NO." + r.getCodigo() + " HA SIDO AUTORIZADO POR LA MÁXIMA AUTORIDAD "
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito a la dirección de email: " + email + " relacionada con: " + clienteNotifacacion.getNombreCompleto());

    }

    public void dlogoObservaciones(ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        if (busqueda.getAnio() == null) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Elija un Período");
            return;

        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int aprobar) {
        try {
            ReformaTraspaso aux = this.reformaTraspaso;

            switch (aprobar) {
                case 2:
                    getParamts().put("userPresupuesto", clienteService.getrolsUser(RolUsuario.presupuesto));
                    if (!enviar(reformaTraspaso)) {
                        return;
                    }   break;
                case 1:
                    getParamts().put("userSecGeneral", clienteService.getrolsUser(RolUsuario.secretariaGeneral));
                    break;
                case 0:
                      break;
                default:
                    break;
            }

            getParamts().put("aprobado", aprobar);

            clienteNotifacacion = solicitudService.devuelveClienteNotitfacion(BigInteger.valueOf(tramite.getNumTramite()));
            enviarCorreo(clienteNotifacacion.getEmail(), "REFORMA TIPO 2", "", clienteNotifacacion.getNombreCompleto(), aux);
            observacion.setObservacion(observaciones);
//           clienteService.getUnidadUserCodigo("THU", "")
            if (saveTramite() == null) {
                return;
            }
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

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public DetalleReformaTraspaso getDetalleReformaTraspaso() {
        return detalleReformaTraspaso;
    }

    public void setDetalleReformaTraspaso(DetalleReformaTraspaso detalleReformaTraspaso) {
        this.detalleReformaTraspaso = detalleReformaTraspaso;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
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

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
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

    public List<Distributivo> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<Distributivo> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public Cliente getClienteNotifacacion() {
        return clienteNotifacacion;
    }

    public void setClienteNotifacacion(Cliente clienteNotifacacion) {
        this.clienteNotifacacion = clienteNotifacacion;
    }

    public PlanLocalProgramaProyecto getPlanLocal() {
        return planLocal;
    }

    public void setPlanLocal(PlanLocalProgramaProyecto planLocal) {
        this.planLocal = planLocal;
    }

    public PlanAnualPoliticaPublica getPlanPolitica() {
        return planPolitica;
    }

    public void setPlanPolitica(PlanAnualPoliticaPublica planPolitica) {
        this.planPolitica = planPolitica;
    }

    public PlanAnualProgramaProyecto getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualProgramaProyecto planAnual) {
        this.planAnual = planAnual;
    }

    public PartidasDistributivo getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(PartidasDistributivo partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public ProformaPresupuestoPlanificado getPartidasDirectas() {
        return partidasDirectas;
    }

    public void setPartidasDirectas(ProformaPresupuestoPlanificado partidasDirectas) {
        this.partidasDirectas = partidasDirectas;
    }

    public List<ProformaPresupuestoPlanificado> getProformaReformaList() {
        return proformaReformaList;
    }

    public void setProformaReformaList(List<ProformaPresupuestoPlanificado> proformaReformaList) {
        this.proformaReformaList = proformaReformaList;
    }

    public List<ProformaPresupuestoPlanificado> getShowCodigoRepetidosList() {
        return showCodigoRepetidosList;
    }

    public void setShowCodigoRepetidosList(List<ProformaPresupuestoPlanificado> showCodigoRepetidosList) {
        this.showCodigoRepetidosList = showCodigoRepetidosList;
    }

    public List<PlanLocalProgramaProyecto> getListaPlanLocalNuevo() {
        return listaPlanLocalNuevo;
    }

    public void setListaPlanLocalNuevo(List<PlanLocalProgramaProyecto> listaPlanLocalNuevo) {
        this.listaPlanLocalNuevo = listaPlanLocalNuevo;
    }

    public List<PlanAnualPoliticaPublica> getListaPlanPoliticaOriginal() {
        return listaPlanPoliticaOriginal;
    }

    public void setListaPlanPoliticaOriginal(List<PlanAnualPoliticaPublica> listaPlanPoliticaOriginal) {
        this.listaPlanPoliticaOriginal = listaPlanPoliticaOriginal;
    }

    public List<PlanAnualPoliticaPublica> getListaPlanPoliticaNuevo() {
        return listaPlanPoliticaNuevo;
    }

    public void setListaPlanPoliticaNuevo(List<PlanAnualPoliticaPublica> listaPlanPoliticaNuevo) {
        this.listaPlanPoliticaNuevo = listaPlanPoliticaNuevo;
    }

    public List<PlanAnualProgramaProyecto> getListaPlanAnualOriginal() {
        return listaPlanAnualOriginal;
    }

    public void setListaPlanAnualOriginal(List<PlanAnualProgramaProyecto> listaPlanAnualOriginal) {
        this.listaPlanAnualOriginal = listaPlanAnualOriginal;
    }

    public List<PlanAnualProgramaProyecto> getListaPlanAnualNuevo() {
        return listaPlanAnualNuevo;
    }

    public void setListaPlanAnualNuevo(List<PlanAnualProgramaProyecto> listaPlanAnualNuevo) {
        this.listaPlanAnualNuevo = listaPlanAnualNuevo;
    }

    public List<ActividadOperativa> getListaActividadOriginal() {
        return listaActividadOriginal;
    }

    public void setListaActividadOriginal(List<ActividadOperativa> listaActividadOriginal) {
        this.listaActividadOriginal = listaActividadOriginal;
    }

    public List<ActividadOperativa> getListaActividadNuevo() {
        return listaActividadNuevo;
    }

    public void setListaActividadNuevo(List<ActividadOperativa> listaActividadNuevo) {
        this.listaActividadNuevo = listaActividadNuevo;
    }

    public List<Producto> getListaProdutoOriginal() {
        return listaProdutoOriginal;
    }

    public void setListaProdutoOriginal(List<Producto> listaProdutoOriginal) {
        this.listaProdutoOriginal = listaProdutoOriginal;
    }

    public List<Producto> getListaProductoNuevo() {
        return listaProductoNuevo;
    }

    public void setListaProductoNuevo(List<Producto> listaProductoNuevo) {
        this.listaProductoNuevo = listaProductoNuevo;
    }

    public List<PartidasDistributivo> getListaPartidasDistributivoNuevo() {
        return listaPartidasDistributivoNuevo;
    }

    public void setListaPartidasDistributivoNuevo(List<PartidasDistributivo> listaPartidasDistributivoNuevo) {
        this.listaPartidasDistributivoNuevo = listaPartidasDistributivoNuevo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidasDistributivoAnexoNuevo() {
        return listaPartidasDistributivoAnexoNuevo;
    }

    public void setListaPartidasDistributivoAnexoNuevo(List<PartidasDistributivoAnexo> listaPartidasDistributivoAnexoNuevo) {
        this.listaPartidasDistributivoAnexoNuevo = listaPartidasDistributivoAnexoNuevo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }

    public List<ProformaPresupuestoPlanificado> getProfromaReformaList() {
        return profromaReformaList;
    }

    public void setProfromaReformaList(List<ProformaPresupuestoPlanificado> profromaReformaList) {
        this.profromaReformaList = profromaReformaList;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
    }

    public boolean isRenderedDistributivo() {
        return renderedDistributivo;
    }

    public void setRenderedDistributivo(boolean renderedDistributivo) {
        this.renderedDistributivo = renderedDistributivo;
    }

    public boolean isRenderedDistributivoAnexo() {
        return renderedDistributivoAnexo;
    }

    public void setRenderedDistributivoAnexo(boolean renderedDistributivoAnexo) {
        this.renderedDistributivoAnexo = renderedDistributivoAnexo;
    }

    public boolean isRenderedPAPP() {
        return renderedPAPP;
    }

    public void setRenderedPAPP(boolean renderedPAPP) {
        this.renderedPAPP = renderedPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
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

}
