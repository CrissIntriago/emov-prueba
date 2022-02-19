/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
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
 * @author ORIGAMIEC
 */
@Named(value = "generalReformaView")
@ViewScoped
public class AprobacionReformaController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaSuplementoIngresoService reformaService;
    @Inject
    private CuposService cupoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private ProductoService productoService;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PlanProgramaticoService planProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPrespuestoService;
    @Inject
    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;

    private LazyModel<ReformaIngresoSuplemento> lazyyReforma;
    private List<String> listaReforma;
    private ReformaIngresoSuplemento reforma;
    private ReformaIngresoSuplemento reformaObjeto;
    private LazyModel<DetalleReformaIngresoSuplemento> lazyreformaIngreso;
    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private List<ThCargoRubros> copiaDiReforma;
    private List<ThCargoRubros> lazyPartidasDistributivoAnexo;
    private LazyModel<ProformaPresupuestoPlanificado> lazyPartidasDirectas;
    private LazyModel<Producto> lazyAsignacionProducto;
    private List<UnidadAdministrativa> unidades;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramaticos;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;
    private List<CatalogoItem> listafuenteFinanciamiento;
    private List<ProformaPresupuestoPlanificado> profromaReformaList;
    private List<ProformaPresupuestoPlanificado> showCodigoRepetidosList;
    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    private String observaciones;
    //NUEVO
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;
    private List<PresPlanProgramatico> listEstructura;
    private List<PresCatalogoPresupuestario> listItem;
    private List<PresFuenteFinanciamiento> listFuenteNew;
    private LazyModel<ThCargo> thCargoLazyModel;
    private List<ThCargoRubros> thCargoLazyModelRubros;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reformaObjeto = new ReformaIngresoSuplemento();
                reforma = new ReformaIngresoSuplemento();
                lazyyReforma = new LazyModel(ReformaIngresoSuplemento.class);
                lazyyReforma.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyyReforma.getFilterss().put("tipo:equal", true);
                listaReforma = new ArrayList<>();
                listaReforma.add("PAPP");
                listaReforma.add("DISTRIBUTIVO");
                listaReforma.add("DISTRIBUTIVO ANEXO");
                listaReforma.add("PARTIDAS DIRECTAS");
//                copiaDiReforma = new ArrayList<>();
                profromaReformaList = new ArrayList<>();
                showCodigoRepetidosList = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public BigDecimal getSuplemento(ReformaIngresoSuplemento r) {
        return reformaService.getTotalSuplemento(r);
    }

    public BigDecimal getReduccion(ReformaIngresoSuplemento r) {

        return reformaService.getTotalReduccion(r);
    }

    public void visulauzarReformaIngreso(ReformaIngresoSuplemento r) {
        lazyreformaIngreso = new LazyModel(DetalleReformaIngresoSuplemento.class);
        lazyreformaIngreso.getFilterss().put("reforma:equal", r);
        PrimeFaces.current().executeScript("PF('dlgoVistaReformaIngreso').show()");
        PrimeFaces.current().ajax().update(":formVistaIngreso");
    }

    public void abrirDlogoAprobacion(ReformaIngresoSuplemento r) {
        reformaObjeto = new ReformaIngresoSuplemento();
        reformaObjeto = r;
        reformaObjeto.setFechaAprobacion(new Date());
        PrimeFaces.current().executeScript("PF('dlgoAprbacionReforma').show()");
        PrimeFaces.current().ajax().update(":formAprobacionReforma");
    }

    public void aprobarReforma() {

        if (reformaObjeto.getCodigo() == null || reformaObjeto.getCodigo() == "") {
            JsfUtil.addErrorMessage("Error", "No pueden estar los campos vacios");
            return;
        }

        List<Producto> productosPartidas = reformaService.showProductoNuevo(BigInteger.valueOf(reformaObjeto.getId()));
        boolean verificarPartidasPapp = true;
        if (!productosPartidas.isEmpty()) {
            for (Producto itemData : productosPartidas) {
                if (itemData.getCodigoPresupuestario() == null || itemData.getCodigoPresupuestario() == "") {
                    verificarPartidasPapp = false;
                    break;
                }
            }

        }
        if (!verificarPartidasPapp) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "HAY PRODUCTO QUE NO TIENE UNA PARTIDA");
            return;

        }

        List<ThCargoRubros> partidasDistrC = reformaService.partidasDis(reformaObjeto);

        List<PartidasDistributivoAnexo> partidasDistrAnexoC = reformaService.partidasDisAnexo(reformaObjeto);

        List<ProformaPresupuestoPlanificado> partidasDirectasC = reformaService.partidasDirect(reformaObjeto);

        if (!agruparProformaReforma(reformaObjeto)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "HAY CODIGOS REPETIDOS POR FAVOR REVISAR");
            PrimeFaces.current().executeScript("PF('dlogoCodigoRepetidos').show()");
            PrimeFaces.current().ajax().update("dlogoPartidasRepetidas");

            return;
        }

        if (reformaService.getReformaEquilibrada(reformaObjeto)) {
            CatalogoItem estado = reformaService.getlistaEstado("APRO", "estado_solicitud");
            CatalogoItem estadoPartidas = reformaService.getlistaEstado("ARD", "estado_distributivo");

            reformaObjeto.setFechaModificacion(new Date());
            reformaObjeto.setUsuarioModificacion(userSession.getNameUser());
            reformaObjeto.setEstado(estado);
            reformaService.edit(reformaObjeto);
            reformaService.actualizarIngresoEgreso(reformaObjeto, estadoPartidas);
            PrimeFaces.current().executeScript("PF('dlgoAprbacionReforma').hide()");
            PrimeFaces.current().ajax().update(":formAprobacionReforma");
            JsfUtil.addInformationMessage("Información", "Se ha aprobado correctamente la reforma");

        } else {
            JsfUtil.addErrorMessage("Error", "No se puede aporbar porque los Montos de Ingreso no coinciden con los montos de egresos");

        }
    }

    public Boolean agruparProformaReforma(ReformaIngresoSuplemento r) {
        Boolean verificar = true;
        BigInteger integer = BigInteger.valueOf(r.getId());
        profromaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaService.gePappGroupReforma(r.getPeriodo(), r);
        List<ProformaPDTO> listaagrupacionDistr = reformaService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidaAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaService.showPartidaDirectasReforma(r.getPeriodo(), r);

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
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                profromaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
            }

        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="AGRUPAR DISTRIBUTIVO">
        if (!listaagrupacionDistr.isEmpty()) {

            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO i : listaagrupacionDistr) {

                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
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
                proformaPresupuesto.setReformaCodificado(i.getTotal().add(i.getReformaSuplemento()).subtract(i.getReformaReduccion()));
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

                profromaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }
        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
        if (!listaagrupacionDistrAnexo.isEmpty()) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
                    if (p.getPartidaAp() == null ? data.getPartidaPresupuestaria() == null : p.getPartidaAp().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
                proformaPresupuesto.setValor(p.getDistributivoAnexo().getMonto());
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
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PDA");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                profromaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }

        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
        if (!listaPartidasdirectas.isEmpty()) {

            for (ProformaPresupuestoPlanificado k : listaPartidasdirectas) {

                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
                    if (k.getPartidaPresupuestaria() == data.getPartidaPresupuestaria()) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                profromaReformaList.add(k);

            }

        }
//</editor-fold>

        //VERIFICACION DE CODIGOS REPETIDOS
        if (!showCodigoRepetidosList.isEmpty()) {

            verificar = false;
        }

        return verificar;
    }

    public void rechazarReforma() {
        CatalogoItem estado = reformaService.getlistaEstado("RECHA", "estado_solicitud");
        CatalogoItem estadoPartidas = reformaService.getlistaEstado("RERD", "estado_distributivo");
        reformaObjeto.setFechaModificacion(new Date());
        reformaObjeto.setUsuarioModificacion(userSession.getNameUser());
        reformaObjeto.setEstado(estado);
        reformaService.edit(reformaObjeto);
        reformaService.actualizarIngresoEgreso(reformaObjeto, estadoPartidas);
        JsfUtil.addInformationMessage("Información", "Se ha rechazado correctamente la reforma");

    }

    public void canclear() {
        PrimeFaces.current().executeScript("PF('dlgoAprbacionReforma').hide()");
        PrimeFaces.current().ajax().update(":formAprobacionReforma");
    }

    public BigDecimal getValorReformas(ReformaIngresoSuplemento r, String data) {
        BigDecimal result = BigDecimal.ZERO;
        BigInteger b = BigInteger.valueOf(r.getId());
        if (r.getTipo()) {

            switch (data) {
                case "PAPP":
                    result = reformaService.getSuplementoPapp(b);
                    break;

                case "DISTRIBUTIVO":
                    result = reformaService.getSuplementoDistributivoNuevo(b);
                    break;

                case "DISTRIBUTIVO ANEXO":
                    result = reformaService.getSuplementoDistributivoAnexoNuevo(b);
                    break;

                case "PARTIDAS DIRECTAS":
                    result = reformaService.getSuplementoPartidasDirectas(b);
                    break;

            }
        } else {
            switch (data) {
                case "PAPP":
                    result = reformaService.getReduccionPapp(b);
                    break;

                case "DISTRIBUTIVO":
                    result = reformaService.getReduccionDistributivoNuevo(b);
                    break;

                case "DISTRIBUTIVO ANEXO":
                    result = reformaService.getReduccionDistributivoAnexoNuevo(b);
                    break;

                case "PARTIDAS DIRECTAS":
                    result = reformaService.getReduccionPartidasDirectas(b);
                    break;

            }

        }

        return result;
    }

    public BigDecimal gettoalIngreso(ReformaIngresoSuplemento r) {
        return reformaService.totalIngresoReforma(r);
    }

    public BigDecimal getTotalEgreso(ReformaIngresoSuplemento r) {
        return reformaService.totalEgresoReforma(r);
    }

    public void abrirDldoPappAsigancion(ReformaIngresoSuplemento r, String data) {

        switch (data) {
            case "PAPP":
                // listafuenteFinanciamiento = catalogoService.MostarTodoCatalogo("tipo_fuente_financiamiento");
                unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
                //  listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
                //  listaPlanProgramaticos = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), r.getPeriodo());
                //   listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), r.getPeriodo());
                lazyAsignacionProducto = new LazyModel(Producto.class);
                lazyAsignacionProducto.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(r.getId()));
                lazyAsignacionProducto.getFilterss().put("codigoPresupuestario:equal", null);
                lazyAsignacionProducto.setDistinct(false);
                listItem = new ArrayList<>();
                listItem = catalogoServiceNew.findTipoPresupuesto(false);
                listEstructura = new ArrayList<>();
                listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
                listFuenteNew = new ArrayList<>();
                listFuenteNew = fuenteServiceNew.getFuenteFinanciamiento();
                PrimeFaces.current().executeScript("PF('dlgoAsignacionPartidasPapp').show()");
                PrimeFaces.current().ajax().update("formAsignacionPartidasPapp");
                break;

        }
    }

    public void editarCell(Producto p) {
        try {

            if (p.getItemNew() != null && p.getEstructruaNew() != null && p.getFuenteNew() != null) {

                p.setCodigoPresupuestario(p.getEstructruaNew().getCodigo() + p.getItemNew().getCodigo() + p.getFuenteNew().getCodFuente());
            } else {
                p.setCodigoPresupuestario(null);
            }

            productoService.edit(p);

            JsfUtil.addInformationMessage("Plan Programa Proyecto", "Se ha Asigando partida correctamente");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void visualizarTipos(ReformaIngresoSuplemento r, String data) {

        reforma = new ReformaIngresoSuplemento();
        reforma = r;
        switch (data) {
            case "PAPP":
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", BigInteger.valueOf(r.getId()));
                PrimeFaces.current().executeScript("PF('dlgoVistaReformaPapp').show()");
                PrimeFaces.current().ajax().update("formVistaPapp");
                break;

            case "DISTRIBUTIVO":

                thCargoLazyModel = new LazyModel<>(ThCargo.class);
                thCargoLazyModel.getFilterss().put("estado", true);
                thCargoLazyModel.getSorteds().put("id", "ASC");
                thCargoLazyModel.setDistinct(false);
                PrimeFaces.current().executeScript("PF('dlgoVistaReformaDistributivo').show()");
                PrimeFaces.current().ajax().update("formVistaDistributivo");
                break;

            case "DISTRIBUTIVO ANEXO":
               
                thCargoLazyModelRubros = new ArrayList<>();
                thCargoLazyModelRubros = valoresService.getDistributivoAnexoReforma(r.getPeriodo(), BigInteger.valueOf(r.getId()));
//                thCargoLazyModelRubros.getFilterss().put("codigoReforma", BigInteger.valueOf(r.getId()));
//                thCargoLazyModelRubros.getFilterss().put("idRubro.origen", false);

                PrimeFaces.current().executeScript("PF('dlgoVistaReformaDistributivoAnexo').show()");
                JsfUtil.update("formVistaAnexo");
                break;

            case "PARTIDAS DIRECTAS":
                lazyPartidasDirectas = new LazyModel(ProformaPresupuestoPlanificado.class);
                lazyPartidasDirectas.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(r.getId()));
                PrimeFaces.current().executeScript("PF('dlgoVistaReformaPartidasDirectas').show()");
                PrimeFaces.current().ajax().update("formVistaPartidasDirectas");
                break;

        }
    }

    public List<PartidasDistributivo> getPartidasDistributivo(Distributivo d) {
        return valoresService.listaPresupuestoPartidasReforma(d, BigInteger.valueOf(reforma.getId()));
    }

    public List<ProformaPresupuestoPlanificado> getShowCodigoRepetidosList() {
        return showCodigoRepetidosList;
    }

    public void setShowCodigoRepetidosList(List<ProformaPresupuestoPlanificado> showCodigoRepetidosList) {
        this.showCodigoRepetidosList = showCodigoRepetidosList;
    }

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reforma = new ReformaIngresoSuplemento();
        reforma = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void loadingRubros(ThCargo t) {

        copiaDiReforma = new ArrayList<>();
        copiaDiReforma = thCargoRubrosService.listaDistributivos(BigInteger.valueOf(reforma.getId()), t);

    }

    public void completarTarea(Boolean aprobado) {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")

            if (!aprobado) {
                CatalogoItem estado = new CatalogoItem();
                estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RR");
                suplementoIngresoService.actualizarEstadoPapp(estado, BigInteger.valueOf(reforma.getId()));
                getParamts().put("usuarioplan", clienteService.getrolsUser(RolUsuario.planificacion));
                getParamts().put("aprobadoRevision", 0);
            } else {
                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
                getParamts().put("aprobadoRevision", 1);
            }

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reforma = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }
//<editor-fold defaultstate="collapsed" desc="getter and setter">

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<PlanProgramatico> getListaPlanProgramaticos() {
        return listaPlanProgramaticos;
    }

    public void setListaPlanProgramaticos(List<PlanProgramatico> listaPlanProgramaticos) {
        this.listaPlanProgramaticos = listaPlanProgramaticos;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }

    public List<CatalogoItem> getListafuenteFinanciamiento() {
        return listafuenteFinanciamiento;
    }

    public void setListafuenteFinanciamiento(List<CatalogoItem> listafuenteFinanciamiento) {
        this.listafuenteFinanciamiento = listafuenteFinanciamiento;
    }

    public LazyModel<DetalleReformaIngresoSuplemento> getLazyreformaIngreso() {
        return lazyreformaIngreso;
    }

    public void setLazyreformaIngreso(LazyModel<DetalleReformaIngresoSuplemento> lazyreformaIngreso) {
        this.lazyreformaIngreso = lazyreformaIngreso;
    }

    public LazyModel<VistaGeneralPlanAnual> getVistaGeneralPlanAnualLazy() {
        return vistaGeneralPlanAnualLazy;
    }

    public void setVistaGeneralPlanAnualLazy(LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy) {
        this.vistaGeneralPlanAnualLazy = vistaGeneralPlanAnualLazy;
    }

    public List<ThCargoRubros> getCopiaDiReforma() {
        return copiaDiReforma;
    }

    public void setCopiaDiReforma(List<ThCargoRubros> copiaDiReforma) {
        this.copiaDiReforma = copiaDiReforma;
    }

    public List<ThCargoRubros> getLazyPartidasDistributivoAnexo() {
        return lazyPartidasDistributivoAnexo;
    }

    public void setLazyPartidasDistributivoAnexo(List<ThCargoRubros> lazyPartidasDistributivoAnexo) {
        this.lazyPartidasDistributivoAnexo = lazyPartidasDistributivoAnexo;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getLazyPartidasDirectas() {
        return lazyPartidasDirectas;
    }

    public void setLazyPartidasDirectas(LazyModel<ProformaPresupuestoPlanificado> lazyPartidasDirectas) {
        this.lazyPartidasDirectas = lazyPartidasDirectas;
    }

    public LazyModel<Producto> getLazyAsignacionProducto() {
        return lazyAsignacionProducto;
    }

    public void setLazyAsignacionProducto(LazyModel<Producto> lazyAsignacionProducto) {
        this.lazyAsignacionProducto = lazyAsignacionProducto;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyyReforma() {
        return lazyyReforma;
    }

    public void setLazyyReforma(LazyModel<ReformaIngresoSuplemento> lazyyReforma) {
        this.lazyyReforma = lazyyReforma;
    }

    public List<String> getListaReforma() {
        return listaReforma;
    }

    public void setListaReforma(List<String> listaReforma) {
        this.listaReforma = listaReforma;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    public ReformaIngresoSuplemento getReformaObjeto() {
        return reformaObjeto;
    }

    public void setReformaObjeto(ReformaIngresoSuplemento reformaObjeto) {
        this.reformaObjeto = reformaObjeto;
    }
//</editor-fold>

    public List<PresPlanProgramatico> getListEstructura() {
        return listEstructura;
    }

    public void setListEstructura(List<PresPlanProgramatico> listEstructura) {
        this.listEstructura = listEstructura;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

    public List<PresFuenteFinanciamiento> getListFuenteNew() {
        return listFuenteNew;
    }

    public void setListFuenteNew(List<PresFuenteFinanciamiento> listFuenteNew) {
        this.listFuenteNew = listFuenteNew;
    }

    public LazyModel<ThCargo> getThCargoLazyModel() {
        return thCargoLazyModel;
    }

    public void setThCargoLazyModel(LazyModel<ThCargo> thCargoLazyModel) {
        this.thCargoLazyModel = thCargoLazyModel;
    }

    public List<ThCargoRubros> getThCargoLazyModelRubros() {
        return thCargoLazyModelRubros;
    }

    public void setThCargoLazyModelRubros(List<ThCargoRubros> thCargoLazyModelRubros) {
        this.thCargoLazyModelRubros = thCargoLazyModelRubros;
    }

}
