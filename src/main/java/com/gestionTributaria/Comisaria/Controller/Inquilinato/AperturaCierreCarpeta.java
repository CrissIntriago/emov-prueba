/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller.Inquilinato;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.gestionTributaria.Comisaria.Entities.Inspecciones;
import com.gestionTributaria.Comisaria.Entities.LiquidacionInquilinatoView;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Comisaria.Service.InquilinatoCarpetaDetalleService;
import com.gestionTributaria.Comisaria.Service.InquilinatoCarpetaService;
import com.gestionTributaria.Comisaria.Service.InspeccionService;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Commons.VerCedulaUtils;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.InquilinatoCarpeta;
import com.gestionTributaria.Entities.InquilinatoCarpetaDetalle;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.util.Lazy;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AperturaCierreCarpeta extends BpmnBaseRoot implements Serializable {

    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession session;
    @Inject
    private InspeccionService inspeccionService;
    @Inject
    private InquilinatoCarpetaService carpetaService;
    @Inject
    private InquilinatoCarpetaDetalleService carpetaDetalleService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private ServletSession viewReport;
    @Inject
    private FileUploadDoc uploadDoc;
    private LazyModel<InquilinatoCarpeta> lazy;
    private boolean editar;
    private ComisariaRegistros comisaria;
    private List<Catalogo> listaSecotrPredio;
    private List<CatalogoItem> listaAlquiler;
    private Catalogo catalogo;
    private Integer tipoCons;
    private CatPredio predio;
    private String esUrbano;
    private Inspecciones inspeccion;
    private LazyModel<Servidor> lazyServidor;
    private int tipoSeleccion = 0;
    private List<CatalogoItem> listaDescuentos;
    private LazyModel<Cliente> lazyCliente;
    private InquilinatoCarpeta carpeta;
    private InquilinatoCarpetaDetalle carpetaDetalle;
    private FinaRenLiquidacion liquidacion;
    private Cliente propietario;
    private Cliente clienteSeleccion;
    private List<FinaRenDetLiquidacion> detalle;
    private List<FinaRenRubrosLiquidacion> rubros;
    private FinaRenRubrosLiquidacion rubro;
    private List<InquilinatoCarpetaDetalle> historialInquilinato;
    private LazyModel<FinaRenLiquidacion> lazyLiquidaciones;

    private LazyModel<InquilinatoCarpetaDetalle> detalleLazy;
    private LazyModel<Cliente> clientes;
    private String identificacion;
    private boolean aprobado;
    private LazyModel<LiquidacionInquilinatoView> lazyLiquidacionInquilinatoView;
    private LiquidacionInquilinatoView liquidacionTemp;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                resetear();
                consultarComisaria();
                //  consultarComisaria();
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onTabChange(TabChangeEvent event) {
        if (event.getTab().getId().equals("aperturaCierre")) {
            consultarComisaria();

        } else if (event.getTab().getId().equals("consultaAvanzada")) {
            lazyLiquidacionInquilinatoView = new LazyModel<>(LiquidacionInquilinatoView.class);
            lazyLiquidacionInquilinatoView.getSorteds().put("fechaIngreso", "DESC");
        }
    }

    public void imprimirTemp(Long id) {
        liquidacion = new FinaRenLiquidacion();
        liquidacion = service.find(FinaRenLiquidacion.class, id);
        this.imprimir(liquidacion);
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void calculosAdicionales() {
        liquidacion = new FinaRenLiquidacion();
        liquidacion = service.find(FinaRenLiquidacion.class, liquidacionTemp.getId());
        interes(liquidacion);
        liquidacion.calcularPago();
    }

    public void inspeccionNew(Inspecciones i) {
        inspeccion = new Inspecciones();
        if (i != null && i.getId() != null) {
            inspeccion = i;
        } else {
            inspeccion = new Inspecciones();
        }
    }

    public void cargarLazyServidor(int tipo) {
        lazyServidor = new LazyModel<>(Servidor.class);
        tipoSeleccion = tipo;
    }

    public void consultarComisaria() {
        lazy = new LazyModel<>(InquilinatoCarpeta.class);
    }

    public void consultar() {

        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                carpeta.setPredio(predio);
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    carpeta.setPropietario(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
                    identificacion = Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte().getIdentificacionCompleta();

                }
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("mainForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carpetaEdit(InquilinatoCarpeta carpe) {
        this.carpeta = new InquilinatoCarpeta();
        editar = false;
        if (carpe != null) {
            this.carpeta = carpe;
            if (this.carpeta.getPropietario() != null) {
                identificacion = this.carpeta.getPropietario().getIdentificacionCompleta();
            }
            editar = true;
        }
    }

    public void opendlg() {

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        JsfUtil.update("frmDlgObser");
    }

    public void completarTarea(boolean activo) {
        try {

            if (activo) {
                getParamts().put("recepcion", tramite.getObservaciones().get(0).getUserCre());
                getParamts().put("aprobado", 1);
            } else {
                getParamts().put("aprobado", 0);
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void editarCarpeta() {
        if (this.carpeta.getId() == null) {
            this.carpeta.setUsuarioCrea(session.getNameUser());
            this.carpeta.setFechaCrea(new Date());
            this.carpeta.setFechaMod(new Date());
            this.carpeta.setUsuarioMod(session.getNameUser());
            carpetaService.create(carpeta);
        } else {
            this.carpeta.setFechaMod(new Date());
            this.carpeta.setUsuarioMod(session.getNameUser());
            carpetaService.edit(carpeta);
        }
        JsfUtil.addInformationMessage("", "Transacción realizada con exito");
    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior

                break;
            case 2: // Codigo Nuevo
                System.out.println("estructura predial");
                if (predio.getSector() > 0 || predio.getMz() > 0 || predio.getProvincia() > 0 || predio.getCanton() > 0
                        || predio.getParroquia() > 0 || predio.getZona() > 0 || predio.getSolar() > 0 || predio.getPiso() >= 0
                        || predio.getUnidad() >= 0 || predio.getBloque() >= 0) {
                    Map<String, Object> paramtr = new HashMap<>();
                    paramtr.put("estado", "A");
                    paramtr.put("sector", predio.getSector());
                    paramtr.put("mz", predio.getMz());
                    paramtr.put("provincia", predio.getProvincia());
                    paramtr.put("canton", predio.getCanton());
                    paramtr.put("parroquia", predio.getParroquia());
                    paramtr.put("zona", predio.getZona());
                    paramtr.put("solar", predio.getSolar());
                    paramtr.put("piso", predio.getPiso());
                    paramtr.put("unidad", predio.getUnidad());
                    paramtr.put("bloque", predio.getBloque());
                    if (esUrbano == "1") {
                        paramtr.put("tipoPredio", "U");
                    } else {
                        paramtr.put("tipoPredio", "R");
                    }
                    temp = service.findByParameter(CatPredio.class, paramtr);
                }
                break;

            case 3:// Numero de Predio

                if (predio.getNumPredio() != null) {
                    System.out.println("num predio");
                    temp = service.getPredioNumPredio(predio.getNumPredio().longValue(), esUrbano);
                }

                break;
            case 4:
                if (predio.getClaveCat() != null) {
                    System.out.println("clave catastral");
                    temp = service.getPredioByClaveCat(predio.getClaveCat(), esUrbano);
                }

                break;
            case 5:// Clave anterior
                if (predio.getPredialant() != null) {
                    temp = service.getPredioByClaveCatAnt(predio.getPredialant(), esUrbano);
                }

                break;

        }
        if (temp != null) {
            JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.predioEncontrado + temp.getClaveCat());
            return temp;
        } else {
            return null;
        }
    }

    public void save() {
//        try {
        if (predio.getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar el predio");
            return;
        }

        if (comisaria.getAnioInquilinato() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Tiene que ingresar el año del inquilinato");
            return;
        }

        if (catalogo == null) {
            JsfUtil.addWarningMessage("Advertencia", "Tiene que elegir el tipo de sector");
            return;
        } else {
            if (catalogo.getId() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que elegir el tipo de sector");
                return;
            }
        }

        if (comisaria.getAlquiler() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Tiene que elegir el detalle del alquiler");
            return;
        }

        InquilinatoCarpeta temp = carpetaService.verificacionCarpeta(predio);
        if (temp == null) {
            carpeta.setPredio(predio);
            carpeta.setPropietario(comisaria.getEnte());
            carpeta.setEstado(Boolean.TRUE);
            carpeta.setFechaMod(new Date());
            carpeta.setFechaCrea(new Date());
            carpeta.setUsuarioCrea(session.getNameUser());
            carpeta.setUsuarioCrea(session.getNameUser());
            carpeta = carpetaService.create(carpeta);

        } else {

            carpeta = temp;
        }

        InquilinatoCarpetaDetalle tp = carpetaService.verificacionInquilinato(predio, comisaria.getAnioInquilinato().intValue());

        if (tp == null) {

            carpetaDetalle = new InquilinatoCarpetaDetalle();
            carpetaDetalle.setAnio(comisaria.getAnioInquilinato());
            carpetaDetalle.setAvaluoMunicipal(predio.getAvaluoMunicipal());
            carpetaDetalle.setEstado(Boolean.TRUE);
            carpetaDetalle.setInquilinatoCarpeta(carpeta);
            carpetaDetalle.setTipo(catalogo);
            carpetaDetalle.setObservacion(comisaria.getAlquiler().getTexto());
            carpetaDetalle.setFechaCrea(new Date());
            carpetaDetalle.setFechaMod(new Date());
            carpetaDetalle.setUsuarioMod(session.getNameUser());
            carpetaDetalle.setUsuarioCrea(session.getNameUser());
            carpetaDetalle = carpetaDetalleService.create(carpetaDetalle);
        }
        JsfUtil.addInformationMessage("", "Transacción exitosa");
        resetear();
//        } catch (Exception e) {
//            System.out.println(e);
//            JsfUtil.addWarningMessage("", "La Transacción fue rechazada");
//        }
    }

    public void resetear() {
        historialInquilinato = new ArrayList<>();
        rubro = new FinaRenRubrosLiquidacion();
        rubros = new ArrayList<>();
        detalle = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        carpetaDetalle = new InquilinatoCarpetaDetalle();
        clienteSeleccion = new Cliente();
        propietario = new Cliente();
        carpeta = new InquilinatoCarpeta();
        comisaria = new ComisariaRegistros();
        listaSecotrPredio = new ArrayList<>();
        listaSecotrPredio = catalogoService.getItemCatalogo(Arrays.asList("GT_sector_predio_residencial", "GT_sector_predio_industrial"));
        listaDescuentos = new ArrayList<>();
        listaDescuentos = catalogoService.MostarTodoCatalogo("GT_descuento_inquilinato");
        catalogo = new Catalogo();
        predio = new CatPredio();
        liquidacionTemp = new LiquidacionInquilinatoView();
        // detalleLazy = new LazyModel<>(InquilinatoCarpetaDetalle.class);
        // detalleLazy.getFilterss().put("inquilinatoCarpeta.estado", true);

    }

    public void visualizacion(ComisariaRegistros c) {
        comisaria = new ComisariaRegistros();
        comisaria = c;
    }

    public void seleccionar(Servidor s) {
        if (tipoSeleccion == 1) {
            inspeccion.setComisario(s);
        } else if (tipoSeleccion == 2) {
            inspeccion.setDelegado(s.getPersona());
        } else if (tipoSeleccion == 3) {
            comisaria.setResponsableServ(s.getPersona());
        }
    }

    public void liquidar(InquilinatoCarpetaDetalle inquil) {
        carpetaDetalle = new InquilinatoCarpetaDetalle();
        carpetaDetalle = inquil;
        BigDecimal total = BigDecimal.ZERO;
        liquidacion = new FinaRenLiquidacion();
        liquidacion.setAnio(inquil.getAnio().intValue());
        liquidacion.setComprador(inquil.getInquilinatoCarpeta().getPropietario());
        liquidacion.setFechaIngreso(new Date());
        liquidacion.setPredio(inquil.getInquilinatoCarpeta().getPredio());
        liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        liquidacion.setFechaCreacionOriginal(new Date());
        liquidacion.setTipoLiquidacion(liquidacionService.getTipoLiquidacionByPrefijo("RIN"));

        List<FinaRenRubrosLiquidacion> result = service.getRubrosByTipoLiquidacionCodRubroASC(liquidacion.getTipoLiquidacion().getId());

        BigDecimal valorSalario = service.getSalarioAnio(liquidacion.getAnio());
        if (valorSalario == null) {
            JsfUtil.addWarningMessage("", "Verifique que el salario para el año " + liquidacion.getAnio() + " este habilitado");
            return;
        }

        if (result.isEmpty()) {
            JsfUtil.addWarningMessage("", "Verifique que los rubros para Inquilinato esten habilitado o comiquese con el departamento de sistema");
            return;
        }
        rubros = new ArrayList<>();
        for (FinaRenRubrosLiquidacion item : result) {
            rubro = new FinaRenRubrosLiquidacion();
            if (item.getCodigoRubro().equals(1L)) {
                String tipo;
                if (inquil.getTipo().getId().equals(216L)) {
                    tipo = "R";
                } else {
                    tipo = "I";
                }
                BigDecimal porc = carpetaService.proceSbu(tipo, inquil.getAvaluoMunicipal());
                if (porc == null || porc.compareTo(BigDecimal.ZERO) == 0) {
                    item.setValor(BigDecimal.ZERO);
                } else {
                    item.setValor(valorSalario.multiply(porc.divide(new BigDecimal("100"))));
                }
            }
            rubro = Utils.clone(item);
            rubro.setCantidad(1);
            rubro.setValor(item.getValor());
            rubro.setDescripcion(item.getDescripcion());
            rubro.setValorCalculo(new BigDecimal(rubro.getCantidad().toString()).multiply(rubro.getValor()));
            rubro.setCobrar(Boolean.TRUE);
            rubros.add(rubro);
        }
        liquidacion.setUsuarioIngreso(session.getNameUser());
        liquidacion.setUsuarioValida(session.getNameUser());

        if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas()) {
            liquidacion.setValidada(Boolean.FALSE);
        } else {
            liquidacion.setValidada(Boolean.TRUE);
        }

        liquidacion.setTotalPago(totalCalculado(rubros));
        liquidacion.setSaldo(liquidacion.getTotalPago());

        JsfUtil.executeJS("PF('dlgLiquidacion').show()");
        JsfUtil.update("formDialogoLiquidacion");

    }

    public void historialLiquidaciones(InquilinatoCarpeta carpe) {
        lazyLiquidaciones = new LazyModel<>(FinaRenLiquidacion.class);
        lazyLiquidaciones.getFilterss().put("predio.id", carpe.getPredio().getId());
        lazyLiquidaciones.getFilterss().put("tipoLiquidacion.id", 62);
        lazyLiquidaciones.getSorteds().put("anio", "DESC");

    }

    public BigDecimal totalCalculado(List<FinaRenRubrosLiquidacion> detalle) {
        BigDecimal total = BigDecimal.ZERO;
        for (FinaRenRubrosLiquidacion item : rubros) {
            if (item.getValorCalculo() != null && item.getValorCalculo().compareTo(BigDecimal.ZERO) == 1) {
                if (item.getCobrar()) {
                    total = total.add(item.getValorCalculo());
                }
            }
        }

        return total;
    }

    public void aceptar() {
        init();
        JsfUtil.executeJS("PF('dlgLiquidacion').hide()");
        JsfUtil.update("formDialogoLiquidacion");
        JsfUtil.update("formLiq");
        JsfUtil.executeJS("PF('dlgNumeroLiquidacion').hide();");

    }

    public void visualizar(FinaRenLiquidacion liqui) {
        if (liqui != null && liqui.getId() != null) {
            this.liquidacion = new FinaRenLiquidacion();
            this.liquidacion = liqui;
            JsfUtil.executeJS("PF('dlgDetalle').show()");
            JsfUtil.update("formDetEmision");
        } else {
            JsfUtil.addWarningMessage("", "No se ha generado ningún tipo de liquidación");
        }
    }

    public void generarLiquidacion() {
        if (session != null && session.getNameUser() != null) {

            detalle = new ArrayList<>();
            for (FinaRenRubrosLiquidacion item : rubros) {
                FinaRenDetLiquidacion data = new FinaRenDetLiquidacion();
                data.setCantidad(item.getCantidad());
                data.setRubro(item);
                data.setEstado(true);
                data.setValor(item.getValorCalculo());
                data.setValorRecaudado(BigDecimal.ZERO);
                data.setValorSinDescuento(BigDecimal.ZERO);
                detalle.add(data);
            }

            liquidacion = liquidacionService.crearLiquidacion(liquidacion, detalle);
            carpetaDetalle.setLiquidacion(liquidacion);
            carpetaDetalleService.edit(carpetaDetalle);
            JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " Generada con exito");
            JsfUtil.update("formLiq");
            JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");

        }
    }

    public BigDecimal interesReporte(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void imprimirDistribuccion(FinaRenLiquidacion liq) {
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("id", liq.getId());
        viewReport.addParametro("descuento", liq.getDescuento());
        viewReport.addParametro("interes", interesReporte(liq));
        viewReport.addParametro("valor_exonerado", liq.getValorExoneracion());
        viewReport.addParametro("total", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.addParametro("recargo", liq.getRecargo());
        viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
        viewReport.addParametro("pagon_final", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.setNombreReporte("distribucionActivoTotales");
        viewReport.setNombreSubCarpeta("GestionTributatia/general");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimir(FinaRenLiquidacion liq) {
        System.out.println("liq " + liq.getId());

        interesReporte(liq);
        liq.setPagoFinal(liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));

        if (liq.getListDistribuciionCantones() != null && liq.getListDistribuciionCantones().size() > 0) {
            imprimirDistribuccion(liq);
        } else {

            viewReport.borrarParametros();
            viewReport.instanciarParametros();
            viewReport.addParametro("id", liq.getId());
            viewReport.addParametro("descuento", liq.getDescuento());
            viewReport.addParametro("interes", liq.getInteres());
            viewReport.addParametro("valor_exonerado", liq.getExoneracionSumValor());
            viewReport.addParametro("total", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.addParametro("recargo", liq.getRecargo());
            viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
            viewReport.addParametro("pagon_final", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.setNombreReporte("general");
            viewReport.setNombreSubCarpeta("GestionTributatia/general");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void calcularPago() {
        liquidacion.setTotalPago(totalCalculado(rubros));
        liquidacion.setSaldo(liquidacion.getTotalPago());
    }

    public void saveInspeccion() {

        if (predio.getId() == null) {
            JsfUtil.addWarningMessage("", "Debe Seleccionar un predio");
            return;
        }

        inspeccion.setNoSolicitud(inspeccionService.noSolicitud());
        inspeccion.setPredio(predio.getId());
        inspeccion = inspeccionService.create(inspeccion);
        JsfUtil.executeJS("PF('dlogoNumSolcitud').show()");
        JsfUtil.update("fmNumSolicitud");

    }

    public void buscarInquilino() {

        try {

            VerCedulaUtils validacion = new VerCedulaUtils();

            if (identificacion != null && validacion.isCIValida(identificacion)) {

                this.propietario = clienteService.buscarCliente(identificacion);

                if (this.propietario == null) {
                    lazyCliente = new LazyModel(Cliente.class);
                    lazyCliente.getFilterss().put("validado", true);
                    lazyCliente.getFilterss().put("estado", true);
                    JsfUtil.update("frmSolicitante");
                    JsfUtil.executeJS("PF('dlgSolicitante').show();");
                } else {
                    carpeta.setPropietario(propietario);
                    JsfUtil.update("mainForm");

                }

            } else {
                lazyCliente = new LazyModel(Cliente.class);
                lazyCliente.getFilterss().put("validado", true);
                lazyCliente.getFilterss().put("estado", true);
                JsfUtil.update("frmSolicitante");
                JsfUtil.executeJS("PF('dlgSolicitante').show();");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));

            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void seleccionar() {
        if (this.clienteSeleccion.getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un solicitante del listado");
        } else {
            carpeta.setPropietario(clienteSeleccion);
            identificacion = clienteSeleccion.getIdentificacionCompleta();
            JsfUtil.addInformationMessage("Mensaje", "Contribuyente seleccionado.");
            JsfUtil.executeJS("PF('dlgSolicitante').hide();");
        }
    }

    public void nuevoCliente() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "cliente-adm");
    }

    public void closeDialogos() {
        JsfUtil.executeJS("PF('dlgoInspeccion').hide()");
        JsfUtil.update("fmInpeccion");
        JsfUtil.executeJS("PF('dlogoNumSolcitud').hide()");
        JsfUtil.update("fmNumSolicitud");
    }

    public void cargarItmes() {
        listaAlquiler = new ArrayList<>();
        listaAlquiler = catalogoService.MostarTodoCatalogo(catalogo.getCodigo());
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ComisariaServices getComisariaServices() {
        return comisariaServices;
    }

    public void setComisariaServices(ComisariaServices comisariaServices) {
        this.comisariaServices = comisariaServices;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<InquilinatoCarpetaDetalle> getHistorialInquilinato() {
        return historialInquilinato;
    }

    public void setHistorialInquilinato(List<InquilinatoCarpetaDetalle> historialInquilinato) {
        this.historialInquilinato = historialInquilinato;
    }

    public int getTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(int tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<FinaRenDetLiquidacion> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FinaRenDetLiquidacion> detalle) {
        this.detalle = detalle;
    }

    public List<FinaRenRubrosLiquidacion> getRubros() {
        return rubros;
    }

    public void setRubros(List<FinaRenRubrosLiquidacion> rubros) {
        this.rubros = rubros;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public InquilinatoCarpetaDetalle getCarpetaDetalle() {
        return carpetaDetalle;
    }

    public void setCarpetaDetalle(InquilinatoCarpetaDetalle carpetaDetalle) {
        this.carpetaDetalle = carpetaDetalle;
    }

    public LazyModel<InquilinatoCarpetaDetalle> getDetalleLazy() {
        return detalleLazy;
    }

    public void setDetalleLazy(LazyModel<InquilinatoCarpetaDetalle> detalleLazy) {
        this.detalleLazy = detalleLazy;
    }

    public List<CatalogoItem> getListaDescuentos() {
        return listaDescuentos;
    }

    public void setListaDescuentos(List<CatalogoItem> listaDescuentos) {
        this.listaDescuentos = listaDescuentos;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public LazyModel<InquilinatoCarpeta> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<InquilinatoCarpeta> lazy) {
        this.lazy = lazy;
    }

    public ComisariaRegistros getComisaria() {
        return comisaria;
    }

    public void setComisaria(ComisariaRegistros comisaria) {
        this.comisaria = comisaria;
    }

    public List<Catalogo> getListaSecotrPredio() {
        return listaSecotrPredio;
    }

    public void setListaSecotrPredio(List<Catalogo> listaSecotrPredio) {
        this.listaSecotrPredio = listaSecotrPredio;
    }

    public List<CatalogoItem> getListaAlquiler() {
        return listaAlquiler;
    }

    public void setListaAlquiler(List<CatalogoItem> listaAlquiler) {
        this.listaAlquiler = listaAlquiler;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }

    public InspeccionService getInspeccionService() {
        return inspeccionService;
    }

    public void setInspeccionService(InspeccionService inspeccionService) {
        this.inspeccionService = inspeccionService;
    }

    public Inspecciones getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Inspecciones inspeccion) {
        this.inspeccion = inspeccion;
    }

    public LazyModel<Servidor> getLazyServidor() {
        return lazyServidor;
    }

    public void setLazyServidor(LazyModel<Servidor> lazyServidor) {
        this.lazyServidor = lazyServidor;
    }

    public Cliente getClienteSeleccion() {
        return clienteSeleccion;
    }

    public void setClienteSeleccion(Cliente clienteSeleccion) {
        this.clienteSeleccion = clienteSeleccion;
    }

    public LazyModel<Cliente> getLazyCliente() {
        return lazyCliente;
    }

    public void setLazyCliente(LazyModel<Cliente> lazyCliente) {
        this.lazyCliente = lazyCliente;
    }

    public InquilinatoCarpeta getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(InquilinatoCarpeta carpeta) {
        this.carpeta = carpeta;
    }

    public FinaRenLiquidacion getFinaRenLiquidacion() {
        return liquidacion;
    }

    public void setFinaRenLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public LazyModel<FinaRenLiquidacion> getLazyLiquidaciones() {
        return lazyLiquidaciones;
    }

    public void setLazyLiquidaciones(LazyModel<FinaRenLiquidacion> lazyLiquidaciones) {
        this.lazyLiquidaciones = lazyLiquidaciones;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public LazyModel<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(LazyModel<Cliente> clientes) {
        this.clientes = clientes;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public LazyModel<LiquidacionInquilinatoView> getLazyLiquidacionInquilinatoView() {
        return lazyLiquidacionInquilinatoView;
    }

    public void setLazyLiquidacionInquilinatoView(LazyModel<LiquidacionInquilinatoView> lazyLiquidacionInquilinatoView) {
        this.lazyLiquidacionInquilinatoView = lazyLiquidacionInquilinatoView;
    }

    public LiquidacionInquilinatoView getLiquidacionTemp() {
        return liquidacionTemp;
    }

    public void setLiquidacionTemp(LiquidacionInquilinatoView liquidacionTemp) {
        this.liquidacionTemp = liquidacionTemp;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

//</editor-fold>
}
