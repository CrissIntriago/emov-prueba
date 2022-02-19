/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.RenActivosLocalComercial;
import com.gestionTributaria.Entities.RenDesvalorizacion;
import com.gestionTributaria.Entities.RenDetallePlusvalia;
import com.gestionTributaria.Entities.RenValoresPlusvalia;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ActualizarListado;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "plusvaliaAlcabalas")
@ViewScoped
public class plusvaliaAlcabalasMB extends ReportGenealModel implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(plusvaliaAlcabalasMB.class.getName());
    
    private Event<ActualizarListado> eventUpdate;
    
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private ManagerService manager;
    @Inject
    private RecaudacionInteface recauejb;
    @Inject
    private CatalogoService catalogo;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    
    private Integer tipo = 1;
    private Integer tipoCons = 2;
    private Boolean esMatriz = false;
    private Boolean seccion1 = false;
    private Boolean seccion2 = false;
    private Boolean exonerar = false;
    private Boolean alcabala = false;
    private Map<String, Object> param;
    private CatPredioModel predioModel;
    private CatPredio predio;
    private CatPredioPropietario vendedor, pro;
    private FinaRenLiquidacion liquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenTipoLiquidacion> tiposLiquidacions;
    private List<FinaRenRubrosLiquidacion> rubrosLiquidacion;
    private RenValoresPlusvalia valoresCalc;
    private RenDesvalorizacion desvalorizacion;
    private List<RenDetallePlusvalia> detallePlusvalias;
    protected Map<String, Object> paramtr;
    
    private Cliente comprador = new Cliente();
    private Cliente vendedorPredioOtroCanton = new Cliente();
    private Boolean esPersonaComp = true;
    private Boolean esPersonaVend = true;
    private List<CatPredio> prediosList;
    private List<CatPredio> predios;
    private AplicarExoneracionMB aplicarExoneracion;
    
    private FnSolicitudExoneracion solicitud;
    
    private Boolean esPersona = true;
    private LazyModel<Cliente> entes;
    private Cliente ente;
    private String cedula;
    private String msg;
    private Long descuentoTemp2;
    private boolean porcentajeManual;
    
    public Long getDescuentoTemp2() {
        return descuentoTemp2;
    }
    
    public void setDescuentoTemp2(Long descuentoTemp2) {
        this.descuentoTemp2 = descuentoTemp2;
    }
    private List<CatPredioPropietario> catPredioPropietarioCollection;

    // Variables para locales comerciales
    private RenActivosLocalComercial local;
    // Fin variables locales comerciales

    //Inicio del tipo de predio que se va a liquidar
    /*
        PDC :PREDIOS DEL CANTON

     */
    private Boolean claveOtroCanton = Boolean.FALSE;
    
    private CatPredio predioWs;
    protected Usuarios usr;
    
    private boolean ventaPrimeraVez;
    private boolean compraExedente;
    private int tipoEnte;
    private short tipoAlcabala = 10;
    private BigDecimal totalDescuento = BigDecimal.ZERO;
    private String esUrbano = "1";
    private boolean verImput;
    private BigDecimal valorPorcentajeExoneracion = BigDecimal.ZERO;
    //FIN DE ES ESO :V

    public boolean isVerImput() {
        return verImput;
    }
    
    public void setVerImput(boolean verImput) {
        this.verImput = verImput;
    }
    
    @PostConstruct
    public void initView() {
        iniciarDatos();
    }
    
    public void iniciarDatos() {
        try {
            verImput = false;
            param = new HashMap<>();
            ventaPrimeraVez = false;
            tiposLiquidacions = manager.gettiposLiquidacionByCodTitRep(1);            
            tipoLiquidacion = null;
            predio = new CatPredio();
            predio.setProvincia(SisVars.PROVINCIA);
            predio.setCanton(SisVars.CANTON);
            initLiquidacion();
            predioWs = new CatPredio();
            consultarRubros();
            valoresCalc = new RenValoresPlusvalia();
            vendedor = new CatPredioPropietario();
            predioModel = new CatPredioModel();
            predioModel.setProvincia(SisVars.PROVINCIA);
            predioModel.setCanton(SisVars.CANTON);
            tipoAlcabala = 10;
            porcentajeManual = false;
            valorPorcentajeExoneracion = BigDecimal.ZERO;
            comprador = new Cliente();
            vendedor = new CatPredioPropietario();
            vendedorPredioOtroCanton = new Cliente();
            
            usr = (Usuarios) manager.find("select u from Usuarios u where u.usuario=:user", new String[]{"user"}, new Object[]{session.getNameUser()});
        } catch (Exception e) {
            LOG.log(Level.OFF, "Iniciar vista", e);
        }
    }
    
    public void selectEnte(Cliente c) {
        
        if (tipoEnte == 1) {
            if (c.getTipoProv() != null) {
                esPersonaVend = "PER_NAT".equals(c.getTipoProv().getCodigo()) ? true : false;
            } else {
                esPersonaVend = true;
            }
            vendedorPredioOtroCanton = c;
//            JsfUtil.update("outDatosEnte");
        } else {
            if (c.getTipoProv() != null) {
                esPersonaComp = "PER_NAT".equals(c.getTipoProv().getCodigo()) ? true : false;
            } else {
                esPersonaComp = true;
            }
            comprador = c;
//            JsfUtil.update("outDatosEnte_");
        }
        
    }
    
    public void buscarEnte(int a) {
//        Map<String, Object> options = new HashMap<>();
//        options.put("resizable", false);
//        options.put("draggable", false);
//        options.put("modal", true);
//        options.put("width", "75%");
//        options.put("closable", true);
//        options.put("contentWidth", "100%");
//        PrimeFaces.current().dialog().openDynamic("/resources/dialog/dialogEnte", options, null);

        tipoEnte = a;
        entes = new LazyModel<>(Cliente.class);
        entes.getFilterss().put("validado", true);
        entes.getFilterss().put("estado", true);
    }
    
    public void consultarRubros() {
        rubrosLiquidacion = new ArrayList<>();
        if (tipoLiquidacion != null) {
            rubrosLiquidacion = manager.getRubrosPorLiquidacion(tipoLiquidacion.getId());
            
            if (tipoLiquidacion.getPrefijo() != null) {
                
            }
            initLiquidacion();
            valoresCalc = new RenValoresPlusvalia();
            if (tipoLiquidacion.getPrefijo().equalsIgnoreCase("PLU")) {
                
                obtenerFaccionAnio();
                
                alcabala = false;
            } else if (tipoLiquidacion.getPrefijo().equalsIgnoreCase("ALC")) {
                alcabala = true;
            }
            liquidacion.setValidada(Boolean.TRUE);
            rubrosLiquidacion.stream().map((ru) -> {
                ru.setValorTotal(ru.getValor());
                return ru;
            }).forEachOrdered((ru) -> {
                ru.getValorTotal().setScale(2, RoundingMode.HALF_UP);
                ru.setCobrar(true);
            });
        }
    }
    
    public void agregar() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "cliente-adm");
    }
    
    public void cambioTipoPersona() {
        if (esPersona != null) {
            if (esPersona) {
                entes = new LazyModel<>(Cliente.class);
                entes.getFilterss().put("tipoProv", new CatalogoItem(88L));
                entes.getFilterss().put("validado", true);
                entes.getFilterss().put("estado", true);
            } else {
                entes = new LazyModel<>(Cliente.class);
                entes.getFilterss().put("tipoProv", new CatalogoItem(89L));
                entes.getFilterss().put("validado", true);
                entes.getFilterss().put("estado", true);
            }
        } else {
            entes = new LazyModel<>(Cliente.class);
            entes.getFilterss().put("validado", true);
            entes.getFilterss().put("estado", true);
        }
        
    }
    
    public void initLiquidacion() {
        liquidacion = new FinaRenLiquidacion();
        liquidacion.setTotalPago(new BigDecimal(0));
        liquidacion.setFechaContratoAnt(new Date());
        liquidacion.setCostoAdq(BigDecimal.ZERO);
        liquidacion.setCuantia(BigDecimal.ZERO);
        liquidacion.setValorHipoteca(BigDecimal.ZERO);
        liquidacion.setObservacion(null);
    }
    
    public void seleccionarPredios(SelectEvent event) {
        predios = (List<CatPredio>) event.getObject();
        detallePlusvalias = new ArrayList<>();
        for (CatPredio pred : predios) {
            RenDetallePlusvalia rdp = new RenDetallePlusvalia();
            rdp.setPrediosAsociados(pred);
            detallePlusvalias.add(rdp);
        }
        esPersonaComp = "C".equals(comprador.getTipoIdentificacion().getCodigo());
        setEsPersonaComp(esPersonaComp);
        
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
                    temp = manager.findByParameter(CatPredio.class, paramtr);
                }
                break;
            
            case 3:// Numero de Predio

                if (predio.getNumPredio() != null) {
                    System.out.println("num predio");
                    temp = manager.getPredioNumPredio(predio.getNumPredio().longValue(), esUrbano);
                }
                
                break;
            case 4:
                if (predio.getClaveCat() != null) {
                    System.out.println("clave catastral");
                    temp = manager.getPredioByClaveCat(predio.getClaveCat(), esUrbano);
                }
                
                break;
            case 5:// Clave anterior
                if (predio.getPredialant() != null) {
                    temp = manager.getPredioByClaveCatAnt(predio.getPredialant(), esUrbano);
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
    
    public void consultar() {
        
        if (tipoLiquidacion == null) {
            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el tipo de liquidación a realizar");
            return;
        }
        try {
            
            CatPredio temp = consultar(tipoCons, predio);
            ventaPrimeraVez = false;
            valorPorcentajeExoneracion = BigDecimal.ZERO;
            
            if (temp != null) {
                predio = temp;
                
                if (alcabala) {
                    liquidacion.setValorComercial(predio.getAvaluoMunicipal());
                    liquidacion.setCuantia(predio.getAvaluoMunicipal());
                    liquidacion.setValorHipoteca(BigDecimal.ZERO);
                    liquidacion.setDescuento(BigDecimal.ZERO);
                    calcularAlcabala();
                } else {
                    liquidacion.setCuantia(predio.getAvaluoMunicipal());
                    valorTotal();
                }
                esMatriz = predio.getPropiedadHorizontal();
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    vendedor = Utils.get(predio.getCatPredioPropietarioList(), 0);
                    this.setVendedorPredioOtroCanton(vendedor.getEnte());
                }
                seccion1 = true;
                claveOtroCanton = Boolean.FALSE;
            } else {
                seccion1 = false;
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            // comprador = manager.find(Cliente.class, 1032502L);
            JsfUtil.update("frmAlcPlus");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error", e);
        }
    }
    
    public String getNombreVendedor(Cliente ente) {
        if (ente != null) {
            if ("PER_NAT".equals(ente.getTipoProv().getCodigo())) {
                return Utils.isEmpty(ente.getApellido()) + " " + Utils.isEmpty(ente.getNombre());
            } else {
                return Utils.isEmpty(ente.getRazonSocial());
            }
        }
        return "";
    }
    
    public void valorTotal() {
        if (alcabala) {
            calcularAlcabala();
        } else {
            liquidacion.setTotalPago(totalPagar());
            liquidacion.getTotalPago().setScale(2, RoundingMode.HALF_UP);
        }
    }
    
    public void editRubro(FinaRenRubrosLiquidacion rubro) {
        
        if (rubro.getValorTotal() == null) {
            rubro.setValorTotal(BigDecimal.ZERO);
        }
        BigDecimal total = BigDecimal.ZERO;
        for (FinaRenRubrosLiquidacion rb : rubrosLiquidacion) {
            if (rb.getCobrar()) {
                total = total.add(rb.getValorTotal().setScale(2, RoundingMode.HALF_UP));
            }
        }
        liquidacion.setTotalPago(total);
        
    }
    
    public void procesar() {
//        if (!claveOtroCanton) {

        if (liquidacionService.verificarLiquidacionPredios(predio, Utils.getAnio(new Date()), tipoLiquidacion, 2L)) {
            JsfUtil.addWarningMessage("", "ya existe una emisión de " + tipoLiquidacion.getNombreTransaccion() + " pendiente de pago para el predio " + predio.getClaveCat());
            return;
        }
        
        if (vendedor == null) {
            System.out.println("vendedor nulo");
            JsfUtil.update("messages");
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaIngresar.concat("nombre del Vendedor"
                    + ", debe seleccionar uno de la tabla de propietario"));
            
            return;
        }
        if (vendedor.getEnte() == null) {
            System.out.println("vendedor ente nulo");
            JsfUtil.update("messages");
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaIngresar.concat("nombre del Vendedor"));
            
            return;
        }
        
        if (comprador.getId() == null) {
            System.out.println("comprador nulo");
            JsfUtil.update("messages");
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaIngresar.concat("nombre del comprador"));
            
            return;
        }
        if (liquidacion.getCuantia() == null) {
            System.out.println("liquidacion cuanta nulo");
            JsfUtil.update("messages");
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.cuantia);
            return;
        }
        if (liquidacion.getTotalPago().doubleValue() <= 0) {
            System.out.println("liquidacion total pago menor a cero");
            JsfUtil.update("messages");
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.total);
            return;
        }
        try {
            Object numLiquidacion;
            /*
                SI EL PREDIO AL QUE SE ESTA GENERANDO LAA ALCABALA Y LA PLUSVALIA PERTENECE AL MUNICIPIO
                CASO CONTRARIO SE ELIJE Y SE REGISTRA EL PREDIO DE OTRO CANTON CON UN TIPO DE PREDIO QUE NO ES URBANO
                EL TIPO DE PREDIO SERA 'O' (OTRO)
             */
            liquidacion.setTipoLiquidacion(tipoLiquidacion);
            liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
            liquidacion.setIpUserSession(session.getIpClient());
            liquidacion.setComprador(comprador);
            param = new HashMap<>();
            param.put("identificacion", vendedor.getCiuCedRuc());
            liquidacion.setVendedor(manager.findByParameter(Cliente.class, param));
            //SE DEBE CAMBIAR POR CATPREDIO
            liquidacion.setPredio(predio);
            liquidacion.setClaveCatastral(predio.getClaveCat());
            liquidacion.setRentas(Boolean.TRUE);
            liquidacion.setAvaluoSolar(predio.getAvaluoSolar());
            liquidacion.setAvaluoMunicipal(predio.getAvaluoMunicipal());
            liquidacion.setAvaluoConstruccion(predio.getAvaluoConstruccion());
//            if (ventaPrimeraVez) {
//                if (tipoLiquidacion.getPrefijo().equals("PLU")) {
//                    liquidacion.setPorcientoRebaja(new BigDecimal("0.05"));
//                }
//            } else {
//                if (tipoLiquidacion.getPrefijo().equals("PLU")) {
//                    liquidacion.setPorcientoRebaja(BigDecimal.TEN);
//                }
//            }
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.setEstadoLiquidacion(manager.getEstadoLiquidacionByDesc(2L));
            liquidacion.setTipoLiquidacion(tipoLiquidacion);
            liquidacion.setCoactiva(false);
            
            if (desvalorizacion != null) {
                valoresCalc.setDesvalorizacion((desvalorizacion.getId() == null ? null : desvalorizacion));
            }
            if (alcabala) {
                detallePlusvalias = null;
            }
            valoresCalc.setRenDetallePlusvaliaCollection(detallePlusvalias);
            final String prefijo = tipoLiquidacion.getPrefijo();
            liquidacion.setDescuento(BigDecimal.ZERO);
            liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
            liquidacion = manager.guardarLiquidacion(liquidacion, rubrosLiquidacion, prefijo, valoresCalc);
            guardarExoneracion(liquidacion);

//            paramtr = new HashMap();
//
//            paramtr.put("PROPIETARIO", liquidacion.getComprador());
//          recauejb.aplicarExoneracionAlcabala(liquidacion, solicitud, paramtr);
//            System.out.println("ESTADO---->" + recauejb);
//            if (recauejb != null) {
//                System.out.println("OK");
//            }
            if (liquidacion != null) {
                if (manager.generarNumLiquidacion(liquidacion, prefijo)) {
                    imprimir(liquidacion);
                    JsfUtil.executeJS("PF('dlgIdLiquidacion').show()");
                    JsfUtil.executeJS("PF('obs').hide()");
                    JsfUtil.update("numLiquidacion:dlgDilLiq");
                } else {
                    JsfUtil.addErrorMessage("Error", "Ocurrio un error al intentar guardar.");
                }
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrio un error al intentar guardar.");
                JsfUtil.executeJS("PF('obs').hide()");
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void guardarExoneracion(FinaRenLiquidacion liquidacion) {
        
        if (liquidacion.getTipoLiquidacion().getId().equals(6L) && valorPorcentajeExoneracion != null && valorPorcentajeExoneracion.compareTo(BigDecimal.ZERO) == 1) {
            FnSolicitudExoneracion solicitud = new FnSolicitudExoneracion();
            solicitud = new FnSolicitudExoneracion();
            solicitud.setExoneracionTipo(new FnExoneracionTipo(42L));
            solicitud.setFechaIngreso(new Date());
            solicitud.setUsuarioCreacion(session.getNameUser());
            solicitud.setSolicitante(null);
            solicitud.setPredio(liquidacion.getPredio());
            solicitud.setEstado(new FnEstadoExoneracion(2L));
            solicitud.setPorcentaje(valorPorcentajeExoneracion.longValue());
            solicitud.setValor(BigDecimal.valueOf(descuentoTemp2));
            solicitud = (FnSolicitudExoneracion) manager.updateEntity(solicitud);
            FnExoneracionLiquidacion exoneracionLiquidacion = new FnExoneracionLiquidacion();
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setExoneracion(solicitud);
            exoneracionLiquidacion.setEstado(true);
            exoneracionLiquidacion.setUsuarioIngreso(session.getNameUser());
            exoneracionLiquidacion.setLiquidacionOriginal(liquidacion);
            exoneracionLiquidacion = (FnExoneracionLiquidacion) manager.updateEntity(exoneracionLiquidacion);
            
        }
    }
    
    public CatPredioModel savePredioOtroCanton() {
        CatPredioModel px = null;
        try {
            px = new CatPredioModel();
            px.setProvincia(this.predioModel.getProvincia());
            px.setCanton(this.predioModel.getCanton());
            //px.setParroquia(this.predioModel.getParroquiaShort());
            px.setZona(this.predioModel.getZona());
            px.setSector(this.predioModel.getSector());
            px.setMz(this.predioModel.getMz());
//            px.setSolar(this.predioModel.getLote());
//            px.setLote(this.predioModel.getLote());
            px.setBloque(this.predioModel.getBloque());
            px.setPiso(this.predioModel.getPiso());
            px.setUnidad(this.predioModel.getUnidad());
            px.setUsuarioCreador(usr);
            px.setInstCreacion(new Date());
            px.setEstado("A");
            //PREDIO DE OTRO CANTON
            px.setTipoPredio("O");
            px.setPropiedadHorizontal(false);

//            px.setClaveCat(claveCatastral(predioModel));
//            px = catas.guardarPredio(px);
//            if (px != null && px.getId() != null) {
//                if (px.getNumPredio() != null || predio.getNumPredio().compareTo(BigInteger.ZERO) <= 0) {
//                  px = catas.generarNumPredio(px);
//                }
//            }
//            if (px != null) {
//                pro = new CatPredioPropieatrioDTO();
//                pro.setPredio(px);
//                pro.setEnte(getVendedorPredioOtroCanton());
//                pro.setEsResidente(false);
//                pro.setUsuario(session.getNameUser());
//                pro.setEstado("A");
//                pro.setFecha(new Date());
//                pro = catas.guardarPropietario(pro, session.getNameUser());
//                return px;
//            }
        } catch (NumberFormatException e) {
        }
        return px;
    }
    
    public void validarClaveOtroCanton() {
        predio = new CatPredio();
        if (tipoLiquidacion == null) {
            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el tipo de liquidación a realizar");
            return;
        }
        try {
            if (predioModel.getProvincia() > 0 && predioModel.getCanton() > 0
                    && predioModel.getParroquiaShort() > 0 && predioModel.getZona() > 0 && predioModel.getSector() > 0
                    && predioModel.getMz() > 0 && predioModel.getLote() > 0) {
                seccion1 = false;
                claveOtroCanton = Boolean.TRUE;
            } else {
                JsfUtil.addWarningMessage("Advertencia", "Verifique los datos ingresados");
            }
            
            JsfUtil.update("frmAlcPlus");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error", e);
        }
        
    }
    
    public void borrarDatos() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.instanciarParametros();
        ss.setNombreDocumento(liquidacion.getIdLiquidacion());
        ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
        // ss.setTieneDatasource(true);
        ss.addParametro("LIQUIDACION", liquidacion.getId());
        ss.addParametro("LOGO", path + SisVars.sisLogo);
        ss.addParametro("FirmaRentas", path + SisVars.sisFirmaRentas);
        ss.addParametro("FirmaTesorero", path + SisVars.sisFirmaTesorero);
        
        if (tipoLiquidacion.getPrefijo().equalsIgnoreCase("PLU")) {
            ss.setNombreReporte("sEspeciePlusvalia");
            JsfUtil.executeJS("PF('dlgConf').show()");
        } else {
            ss.setNombreReporte("sEspecieAlcabalas");
            iniciarDatos();
            tipoLiquidacion = null;
            tipoCons = 2;
            setComprador(null);
            predios = null;
            predio = new CatPredio();
            seccion1 = false;
            seccion2 = false;
            exonerar = false;
        }
        this.initView();
        // SE COMENTO LA LINEA QUE GENERA EL DOC PORQUE QUE INDICARON QUE NO ERA NECESARIO LA GENERACION DEL MISMO
//        JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
    }
    
    public void llenarAlcalbala(Boolean continuar) {
        if (continuar) {
            final Boolean p = comprador.getTipoProv().getCodigo() == "PER_NAT" ? true : false;
            tipoLiquidacion = tiposLiquidacions.get(0);
//            initLiquidacion();
            consultarRubros();
            JsfUtil.update("frmAlcPlus");
            alcabala = true;
            ventaPrimeraVez = false;
            setEsPersonaComp(p);
        } else {
            iniciarDatos();
            tipoLiquidacion = null;
            tipoCons = 2;
            setComprador(null);
            predios = null;
            predio = new CatPredio();
            seccion1 = false;
            seccion2 = false;
            exonerar = false;
            
        }
        JsfUtil.update("frmAlcPlus");
    }
    
    public void seleccionarComprador(SelectEvent event) {
        comprador = (Cliente) event.getObject();
        
        if (comprador != null) {
            esPersonaComp = "C".equals(comprador.getTipoIdentificacion().getCodigo());
        }
        
        JsfUtil.update("entes_global");
    }
    
    public void seleccionarVendedor(SelectEvent event) {
        
        vendedorPredioOtroCanton = (Cliente) event.getObject();
        if (vendedorPredioOtroCanton != null) {
            esPersonaVend = "PER_NAT".equals(vendedorPredioOtroCanton.getTipoProv().getCodigo());
            
        }
        JsfUtil.update("entes_global");
    }

    // ****** Inicio Calculos plusvalia ********//
    public void obtenerFaccionAnio() {
        final CatPredioPropietario e = vendedor;
        if (tipoLiquidacion.getPrefijo().equalsIgnoreCase("PLU")) {
            if (liquidacion != null) {
                desvalorizacion = manager.getDesvalorizacionAnio(Utils.getAnio(liquidacion.getFechaContratoAnt()));
                restarDiferenc();
                JsfUtil.update("frmAlcPlus");
            }
        }
        vendedor = e;
    }
    
    public void restarDiferenc() {
        try {
            
            if (liquidacion.getCuantia() == null) {
                liquidacion.setCuantia(BigDecimal.ZERO);
            }
            if (liquidacion.getCostoAdq() == null) {
                liquidacion.setCostoAdq(BigDecimal.ZERO);
            }
            if (desvalorizacion.getValor() == null) {
                desvalorizacion.setValor(BigDecimal.ZERO);
            }
            
            valoresCalc.setDiferenciaNeta(liquidacion.getCuantia().subtract(liquidacion.getCostoAdq()));
            obtenerDifNet2();
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }
    
    public void obtenerDifNet2() {
        
        valoresCalc.setDiferenciaNeta2(obtenerDifNet2(valoresCalc));
        rebajaGen();
        
    }
    
    public void rebajaGen() {
        int porcAnio = anios(liquidacion.getFechaContratoAnt());
        int cantAnios = getDiffYears(liquidacion.getFechaContratoAnt(), new Date());
        if (porcAnio >= 100 || cantAnios >= 20) {
            JsfUtil.addInformationMessage("El valor de la plusvalia es de 0 porque la compra es de hace 20 años", "");
            valoresCalc.setPorcentajeRebaja(100);
            valoresCalc.setRebajaGen(valoresCalc.getDiferenciaNeta2());
            baseDesv();
            for (FinaRenRubrosLiquidacion rb : rubrosLiquidacion) {
                if (rb.getPrioridad().intValue() == 1) {
                    rb.setValorTotal(BigDecimal.ZERO);
                    
                } else {
                    rb.setValorTotal(rb.getValor());
                }
                rb.setCobrar(Boolean.TRUE);
            }
            liquidacion.setTotalPago(new BigDecimal("3.0"));
            
        } else {
            valoresCalc.setRebajaGen(rebajaGeneral(valoresCalc, liquidacion.getFechaContratoAnt()));
            valoresCalc.setPorcentajeRebaja(porcAnio);
            baseDesv();
//            for (int i = 0; i < rubrosLiquidacion.size(); i++) {
//                
//                if (i == 0) {
//                    FinaRenRubrosLiquidacion fin = rubrosLiquidacion.get(0);
//                    rubrosLiquidacion.remove(0); 
//                    fin.setValorTotal((valoresCalc.getUtilidadImponib().multiply(rubrosLiquidacion.get(i).getValor()).divide(new BigDecimal("100"))));
//                    rubrosLiquidacion.add(0, fin);
//                    System.out.println("r.setValortotal " + rubrosLiquidacion.get(i).getValorTotal());
//                    
//                } else {
//                    
//                    rubrosLiquidacion.get(i).setValorTotal(rubrosLiquidacion.get(i).getValor());
//                    
//                }
//                rubrosLiquidacion.get(i).setCobrar(Boolean.TRUE);
//                rubrosLiquidacion.get(i).getValorTotal().setScale(2, RoundingMode.HALF_UP);
//            }
        }
        liquidacion.setTotalPago(totalPagar());
        if (liquidacion.getTotalPago().compareTo(BigDecimal.ZERO) == 0) {
            liquidacion.setTotalPago(new BigDecimal("3"));
        }
        JsfUtil.update("frmAlcPlus");
    }
    
    public void baseDesv() {
        if (desvalorizacion == null) {
            desvalorizacion = new RenDesvalorizacion();
            desvalorizacion.setAnio(Utils.getAnio(new Date()));
            desvalorizacion.setValor(BigDecimal.ZERO);
        }
        valoresCalc.setDesvalorizacion(desvalorizacion);
//        valoresCalc.setBaseDesvalorizacion((BigDecimal) util.getExpression("baseDesvalorizacion", new Object[]{valoresCalc}));
        valoresCalc.setBaseDesvalorizacion(baseDesvalorizacion());
//        valoresCalc.setDesvalorizacionMonet((BigDecimal) util.getExpression("DesvalorizavioMonetario", new Object[]{valoresCalc, desvalorizacion}));
        valoresCalc.setDesvalorizacionMonet(DesvalorizavioMonetario());
//        valoresCalc.setUtilidadImponib((BigDecimal) util.getExpression("utilidadImponible", new Object[]{valoresCalc}));
        valoresCalc.setUtilidadImponib(utilidadImponible());
//        liquidacion.setTotalPago((BigDecimal) util.getExpression("totalPagar", new Object[]{valoresCalc}));
        liquidacion.setTotalPago(totalPagar());
    }
    
    public BigDecimal obtenerDifNet2(RenValoresPlusvalia valoresCalc) {
        try {
            if (valoresCalc.getDiferenciaNeta() == null) {
                valoresCalc.setDiferenciaNeta(BigDecimal.ZERO);
            }
            if (valoresCalc.getMejorasUrb() == null) {
                valoresCalc.setMejorasUrb(BigDecimal.ZERO);
            }
            if (valoresCalc.getMejorasCemConst() == null) {
                valoresCalc.setMejorasCemConst(BigDecimal.ZERO);
            }
            
            return (valoresCalc.getDiferenciaNeta().subtract(valoresCalc.getMejorasUrb())).subtract(valoresCalc.getMejorasCemConst());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Obtener Diferencia Neta 2.", e);
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal rebajaGeneral(RenValoresPlusvalia valoresCalc, Date fechaContratAnt) {
        int porcAnio = anios(fechaContratAnt);
        System.out.println("porcAnio " + porcAnio);
        int cantAnios = getDiffYears(liquidacion.getFechaContratoAnt(), new Date());
//        if (porcAnio >= 100 || cantAnios >= 20) {
//            return valoresCalc.getDiferenciaNeta2();
//        } else {
        BigDecimal reb = BigDecimal.valueOf(porcAnio).divide(BigDecimal.valueOf(100L));
        System.out.println("valoresCalc.getDiferenciaNeta2().multiply(reb) " + valoresCalc.getDiferenciaNeta2().multiply(reb));
        return valoresCalc.getDiferenciaNeta2().multiply(reb);
//        }
    }
    
    public int anios(Date fechaContratAnt) {
        
        CatalogoItem anioTranscurridoPorc = catalogo.getItemByCatalogoAndCodigo("GT_PROCENTAJE_ANIOS_TRANSC", "PORCE");
        
        int anios = Utils.getAnio(new Date()) - Utils.getAnio(fechaContratAnt);
        Date fechaAct = new Date();
        //java.lang.System.out.println("Utils.getMes(fechaContratAnt) >= Utils.getMes(fechaAct) " + (Utils.getMes(fechaContratAnt) >= Utils.getMes(fechaAct)));
        Calendar c = Calendar.getInstance();
        c.setTime(fechaContratAnt);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        int diff = c1.get(Calendar.YEAR) - c.get(Calendar.YEAR);
        if (c.get(Calendar.MONTH) > c1.get(Calendar.MONTH)
                || (c.get(Calendar.MONTH) == c1.get(Calendar.MONTH) && c.get(Calendar.DATE) > c1.get(Calendar.DATE))) {
            anios--;
        }
        return anios * (anioTranscurridoPorc == null ? 0 : anioTranscurridoPorc.getValor().intValue());
    }
    
    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH);
    }
    
    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }
    
    public BigDecimal baseDesvalorizacion() {
        if (valoresCalc.getDiferenciaNeta2() == null
                && valoresCalc.getRebajaGen() == null) {
            return BigDecimal.ZERO;
        }
        return valoresCalc.getDiferenciaNeta2().subtract(valoresCalc.getRebajaGen());
    }
    
    public BigDecimal DesvalorizavioMonetario() {
        BigDecimal temp = BigDecimal.ZERO;
        System.out.println("VALOR DESVALORIZACION MONETARIA--->" + valoresCalc.getDesvalorizacion().getValor());
        System.out.println("VALOR DESVALORIZACION MONETARIA--->" + valoresCalc.getDesvalorizacionMonet());
        temp = ((valoresCalc.getBaseDesvalorizacion().multiply(valoresCalc.getDesvalorizacion().getValor())).divide(BigDecimal.valueOf(100)));
        System.out.println("VALOR TEMPORAL--->" + temp);
        valoresCalc.setDesvalorizacionMonet(temp);
        valoresCalc.setUtilidadImponib(valoresCalc.getBaseDesvalorizacion().subtract(temp));
        System.out.println("VALOR UTILIDAD NUEVO---->" + valoresCalc.getUtilidadImponib());
        if (desvalorizacion.getValor() == null && valoresCalc.getBaseDesvalorizacion() == null) {
            return BigDecimal.ZERO;
        }
        totalPagar();
        liquidacion.setTotalPago(totalPagar());
        System.out.println("TOTAL APAGAS---->" + totalPagar());
        return valoresCalc.getBaseDesvalorizacion().multiply((desvalorizacion.getValor()).divide(BigDecimal.valueOf(100)));
    }
    
    public BigDecimal utilidadImponible() {
        if (valoresCalc.getBaseDesvalorizacion() == null
                && valoresCalc.getDesvalorizacionMonet() == null) {
            return BigDecimal.ZERO;
        }
        
        return valoresCalc.getBaseDesvalorizacion().subtract(valoresCalc.getDesvalorizacionMonet());
    }
    
    public BigDecimal totalPagar() {
        
        BigDecimal total = BigDecimal.ZERO;
        if (valoresCalc.getUtilidadImponib() == null || valoresCalc.getUtilidadImponib().doubleValue() <= 0) {
            return BigDecimal.ZERO;
        }
        if (rubrosLiquidacion.isEmpty()) {
            return BigDecimal.ZERO;
        }
        for (int i = 0; i < rubrosLiquidacion.size(); i++) {
            System.out.println(" i " + rubrosLiquidacion.get(i).getDescripcion() + "\t\t" + rubrosLiquidacion.get(i).getValor());
            if (rubrosLiquidacion.get(i).getPrioridad().intValue() == 1) {
                
                rubrosLiquidacion.get(i).setValorTotal(valoresCalc.getUtilidadImponib().multiply((rubrosLiquidacion.get(i).getValor()).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
                
                System.out.println("valor " + valoresCalc.getUtilidadImponib());
                System.out.println("ru " + rubrosLiquidacion.get(i).getValor());
                System.out.println("tot " + rubrosLiquidacion.get(i).getValorTotal());
                
            } else {
                rubrosLiquidacion.get(i).setValorTotal(rubrosLiquidacion.get(i).getValor());
            }
            
            rubrosLiquidacion.get(i).getValorTotal().setScale(2, RoundingMode.HALF_UP);
            if (rubrosLiquidacion.get(i).getCobrar()) {
                total = total.add(rubrosLiquidacion.get(i).getValorTotal());
            }
            
        }
        JsfUtil.update("frmAlcPlus:dtRubros");
        return total;
    }
    
    public void renderizarInput() {
        verImput = ventaPrimeraVez;
    }

    /*traigo desde groovy*/
    //***** Fin Calculos Plusvalia *****//
    //***** Inicio Caculos Alcabalas *****//
    public void calcularAlcabala() {
        try {
            if (liquidacion.getFechaContratoAnt() == null) {
                JsfUtil.addWarningMessage("", "Seleccione una fecha");
                return;
            }
            System.out.println("liquidacion.getFechaContratoAnt " + liquidacion.getFechaContratoAnt());
            CatalogoItem porc = catalogo.getItemByCatalogoAndCodigo("GT_DESCUENTOS_ALCABALAS", Utils.getAnio(liquidacion.getFechaContratoAnt()).toString());
            
            if (!porcentajeManual) {
                
                if (porc == null) {
                    liquidacion.setDescuento(BigDecimal.ZERO);
                    liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
                } else {
                    
                    System.out.println("porc.getValor() " + porc.getValor());
                    
                    if (porc.getValor() != null) {
                        liquidacion.setPorcientoRebaja(porc.getValor());
                        liquidacion.setDescuento(porc.getValor().divide(BigDecimal.valueOf(100)));
                    } else {
                        
                        liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
                        liquidacion.setDescuento(BigDecimal.ZERO);
                        
                    }
                    
                }
            } else {
                
                if (liquidacion.getPorcientoRebaja() != null && liquidacion.getPorcientoRebaja().compareTo(BigDecimal.ZERO) == 1) {
                    liquidacion.setDescuento(liquidacion.getPorcientoRebaja().divide(new BigDecimal("100")));
                }
            }
            
            System.out.println("liquidacion.getDescuento() " + liquidacion.getPorcientoRebaja() + "\t\t" + liquidacion.getDescuento());
            
            if (liquidacion.getPorcientoRebaja() == null) {
                liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
            }
            
            if (tipoAlcabala == 0 && liquidacion.getCuantia() != null) {
                liquidacion.setValorHipoteca(BigDecimal.ZERO);
                
            }
            
            BigDecimal cal = liquidacion.getCuantia().subtract(liquidacion.getCuantia().multiply(liquidacion.getDescuento()));
            
            BigDecimal res = BigDecimal.ZERO;
            if (liquidacion.getDesceuentoBiess() != null && liquidacion.getDesceuentoBiess().compareTo(BigDecimal.ZERO) == 1) {
                res = cal.subtract(cal.multiply(liquidacion.getDesceuentoBiess().divide(new BigDecimal("100"))));
            } else {
                res = cal;
            }
            
            liquidacion.setValorComercial(res);
            renderizarInput();
            if (liquidacion.getCuantia() != null) {
                //   final BigDecimal valorAlcabla = manager.find(FinaRenRubrosLiquidacion.class, rubrosLiquidacion.get(0).getId()).getValor();
                if (ventaPrimeraVez == true) {
                    ///aqui estoy trabajando                   

                    /////
                    /////
                    System.out.println("valorPorcentajeExoneracion " + valorPorcentajeExoneracion);
                    if (valorPorcentajeExoneracion != null && valorPorcentajeExoneracion.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal valorExonerado = BigDecimal.ZERO;
                        
                        valorExonerado = (liquidacion.getValorComercial().multiply(valorPorcentajeExoneracion)).divide(BigDecimal.valueOf(100));
                        System.out.println("VALOR EXONERADO---->" + valorExonerado);
                        ////////////////                    
                        liquidacion.setExoneracionDescripcion(null);
                        liquidacion.setValorExoneracion(BigDecimal.ZERO);
                        FinaRenRubrosLiquidacion tem = rubrosLiquidacion.get(0);
                        tem.getValor();
                        BigDecimal descuentoTemp = BigDecimal.ZERO;
                        descuentoTemp = (tem.getValor().multiply(valorPorcentajeExoneracion)).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100));
                        setDescuentoTemp2(descuentoTemp.longValue());
                        tem.setValor(tem.getValor().subtract(descuentoTemp));
                        System.out.println("DESCUENTO TEMPORAL--->" + descuentoTemp);
                        
                        rubrosLiquidacion.remove(0);
                        rubrosLiquidacion.add(0, tem);
                    } else if (valorPorcentajeExoneracion.compareTo(BigDecimal.ZERO) == 0) {
                        JsfUtil.addWarningMessage("", "EL PORCENTAJE DEBE SER MAYOR A CERO");
                        
                    } else {
                        
                    }

                    ///////hasta aquí   
                } else {
                    liquidacion.setExoneracionDescripcion(null);
                    liquidacion.setValorExoneracion(BigDecimal.ZERO);
                    FinaRenRubrosLiquidacion tem = rubrosLiquidacion.get(0);
                    BigDecimal va  = manager.find(FinaRenRubrosLiquidacion.class, rubrosLiquidacion.get(0).getId()).getValor().setScale(2, RoundingMode.HALF_UP);
                    tem.setValor((liquidacion.getValorComercial().multiply(va)).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)));
                    
                    System.out.println("tem.setValor--->" + tem.getValor());
//                  
                    System.out.println("tem.getValor()---->" + tem.getValor());
                    rubrosLiquidacion.remove(0);
                    rubrosLiquidacion.add(0, tem);
                    
                }
                
                liquidacion.setTotalPago(calcularTotalAlcabalas());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        
    }
    
    public void recalcularTotalAlcabla() {
        liquidacion.setTotalPago(calcularTotalAlcabalas());

        ///////////////////
        System.out.println("total pagar" + totalPagar());
//        liquidacion.setTotalPago(totalPagar());

    }
    
    public BigDecimal calcularTotalAlcabalas() {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal div = BigDecimal.valueOf(100);
        liquidacion.setValorHipoteca(BigDecimal.ZERO);
        BigDecimal diff = liquidacion.getCuantia().subtract(liquidacion.getValorHipoteca());
        String anio = Utils.getAnio(liquidacion.getFechaContratoAnt()).toString();
        BigDecimal porDesc;

        //CatalogoItem porc = catalogo.getItemByCatalogoAndCodigo("GT_DESCUENTOS_ALCABALAS", Utils.getAnio(liquidacion.getFechaContratoAnt()).toString());
//        if (porc == null) {
//            liquidacion.setDescuento(BigDecimal.ZERO);
//            liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
//            liquidacion.setValorComercial(liquidacion.getCuantia());
//        } else {
//            if (porc.getValor() != null) {
//                liquidacion.setPorcientoRebaja(porc.getValor());
//                liquidacion.setDescuento(porc.getValor().divide(BigDecimal.valueOf(100)));
//                System.out.println("descuento " + liquidacion.getDescuento());
//                liquidacion.setValorComercial(diff.subtract(diff.multiply(liquidacion.getPorcientoRebaja()).divide(div)));
//            } else {
//                liquidacion.setPorcientoRebaja(BigDecimal.ZERO);
//                liquidacion.setDescuento(BigDecimal.ZERO);
//                liquidacion.setValorComercial(liquidacion.getCuantia());
//            }
//        }
        for (FinaRenRubrosLiquidacion rb : rubrosLiquidacion) {
            if (rb.getCodigoRubro() == 2L || rb.getCodigoRubro() == 6L || rb.getCodigoRubro() == 1L) {
                rb.setValorTotal(rb.getValor().setScale(2, RoundingMode.UP));
            } else if (liquidacion.getValorComercial() != null) {
                rb.setValorTotal(liquidacion.getValorComercial().multiply(rb.getValor().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.UP));
                
            }
            
            if (rb.getCobrar()) {
                total = total.add(rb.getValorTotal());
            }
        }
//        calcularAlcabala();
        liquidacion.setTotalPago(total);
        System.out.println("TOTAL ALCABALA---->" + total + " total>>" + liquidacion.getTotalPago());
        return total;
    }
    
    private BigDecimal getValorRubro(BigDecimal porRub, BigDecimal cuantia, BigDecimal div) {
        return (porRub.divide(div)).multiply(cuantia).setScale(2, RoundingMode.HALF_UP);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER ANG GETTER">
    public Integer getTipo() {
        return tipo;
    }
    
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    public boolean isPorcentajeManual() {
        return porcentajeManual;
    }
    
    public void setPorcentajeManual(boolean porcentajeManual) {
        this.porcentajeManual = porcentajeManual;
    }
    
    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }
    
    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }
    
    public String getEsUrbano() {
        return esUrbano;
    }
    
    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }
    
    public Integer getTipoCons() {
        return tipoCons;
    }
    
    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }
    
    public UserSession getSession() {
        return session;
    }
    
    public void setSession(UserSession session) {
        this.session = session;
    }
    
    public RenValoresPlusvalia getValoresCalc() {
        return valoresCalc;
    }
    
    public void setValoresCalc(RenValoresPlusvalia valoresCalc) {
        this.valoresCalc = valoresCalc;
    }
    
    public RenDesvalorizacion getDesvalorizacion() {
        return desvalorizacion;
    }
    
    public void setDesvalorizacion(RenDesvalorizacion desvalorizacion) {
        this.desvalorizacion = desvalorizacion;
    }
    
    public RenActivosLocalComercial getLocal() {
        return local;
    }
    
    public void setLocal(RenActivosLocalComercial local) {
        this.local = local;
    }
    
    public Boolean getEsMatriz() {
        return esMatriz;
    }
    
    public void setEsMatriz(Boolean esMatriz) {
        this.esMatriz = esMatriz;
    }
    
    public ServletSession getSs() {
        return ss;
    }
    
    public void setSs(ServletSession ss) {
        this.ss = ss;
    }
    
    public Boolean getSeccion1() {
        return seccion1;
    }
    
    public void setSeccion1(Boolean seccion1) {
        this.seccion1 = seccion1;
    }
    
    public Boolean getSeccion2() {
        return seccion2;
    }
    
    public void setSeccion2(Boolean seccion2) {
        this.seccion2 = seccion2;
    }
    
    public List<RenDetallePlusvalia> getDetallePlusvalias() {
        return detallePlusvalias;
    }
    
    public void setDetallePlusvalias(List<RenDetallePlusvalia> detallePlusvalias) {
        this.detallePlusvalias = detallePlusvalias;
    }
    
    public Boolean getExonerar() {
        return exonerar;
    }
    
    public void setExonerar(Boolean exonerar) {
        this.exonerar = exonerar;
    }
    
    public Boolean getAlcabala() {
        return alcabala;
    }
    
    public void setAlcabala(Boolean alcabala) {
        this.alcabala = alcabala;
    }
    
    public CatPredioModel getPredioModel() {
        return predioModel;
    }
    
    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }
    
    public Boolean getClaveOtroCanton() {
        return claveOtroCanton;
    }
    
    public void setClaveOtroCanton(Boolean claveOtroCanton) {
        this.claveOtroCanton = claveOtroCanton;
    }
    
    public boolean isVentaPrimeraVez() {
        return ventaPrimeraVez;
    }
    
    public void setVentaPrimeraVez(boolean ventaPrimeraVez) {
        this.ventaPrimeraVez = ventaPrimeraVez;
    }
    
    public int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(b.YEAR) - a.get(a.YEAR);
        if (a.get(a.MONTH) > b.get(b.MONTH)
                || (a.get(a.MONTH) == b.get(b.MONTH) && a.get(a.DATE) > b.get(b.DATE))) {
            diff--;
        }
        return diff;
    }
    
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
    
    public short getTipoAlcabala() {
        return tipoAlcabala;
    }
    
    public void setTipoAlcabala(short tipoAlcabala) {
        this.tipoAlcabala = tipoAlcabala;
    }
    
    public Event<ActualizarListado> getEventUpdate() {
        return eventUpdate;
    }
    
    public void setEventUpdate(Event<ActualizarListado> eventUpdate) {
        this.eventUpdate = eventUpdate;
    }
    
    public ManagerService getManager() {
        return manager;
    }
    
    public void setManager(ManagerService manager) {
        this.manager = manager;
    }
    
    public Map<String, Object> getParam() {
        return param;
    }
    
    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
    
    public CatPredio getPredio() {
        return predio;
    }
    
    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }
    
    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }
    
    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
    
    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }
    
    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }
    
    public List<FinaRenTipoLiquidacion> getTiposLiquidacions() {
        return tiposLiquidacions;
    }
    
    public void setTiposLiquidacions(List<FinaRenTipoLiquidacion> tiposLiquidacions) {
        this.tiposLiquidacions = tiposLiquidacions;
    }
    
    public List<FinaRenRubrosLiquidacion> getRubrosLiquidacion() {
        return rubrosLiquidacion;
    }
    
    public void setRubrosLiquidacion(List<FinaRenRubrosLiquidacion> rubrosLiquidacion) {
        this.rubrosLiquidacion = rubrosLiquidacion;
    }
    
    public Cliente getComprador() {
        return comprador;
    }
    
    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }
    
    public Cliente getVendedorPredioOtroCanton() {
        return vendedorPredioOtroCanton;
    }
    
    public void setVendedorPredioOtroCanton(Cliente vendedorPredioOtroCanton) {
        this.vendedorPredioOtroCanton = vendedorPredioOtroCanton;
    }
    
    public Boolean getEsPersonaComp() {
        return esPersonaComp;
    }
    
    public void setEsPersonaComp(Boolean esPersonaComp) {
        this.esPersonaComp = esPersonaComp;
    }
    
    public Boolean getEsPersonaVend() {
        return esPersonaVend;
    }
    
    public void setEsPersonaVend(Boolean esPersonaVend) {
        this.esPersonaVend = esPersonaVend;
    }
    
    public Usuarios getUsr() {
        return usr;
    }
    
    public void setUsr(Usuarios usr) {
        this.usr = usr;
    }
    
    public boolean isCompraExedente() {
        return compraExedente;
    }
    
    public void setCompraExedente(boolean compraExedente) {
        this.compraExedente = compraExedente;
    }
    
    public Boolean getEsPersona() {
        return esPersona;
    }
    
    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }
    
    public LazyModel<Cliente> getEntes() {
        return entes;
    }
    
    public void setEntes(LazyModel<Cliente> entes) {
        this.entes = entes;
    }
    
    public Cliente getEnte() {
        return ente;
    }
    
    public void setEnte(Cliente ente) {
        this.ente = ente;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public CatalogoService getCatalogo() {
        return catalogo;
    }
    
    public void setCatalogo(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }
    
    public CatPredioPropietario getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(CatPredioPropietario vendedor) {
        this.vendedor = vendedor;
    }
    
    public CatPredioPropietario getPro() {
        return pro;
    }
    
    public void setPro(CatPredioPropietario pro) {
        this.pro = pro;
    }
    
    public List<CatPredio> getPredios() {
        return predios;
    }
    
    public void setPredios(List<CatPredio> predios) {
        this.predios = predios;
    }
    
    public List<CatPredioPropietario> getCatPredioPropietarioCollection() {
        return catPredioPropietarioCollection;
    }
    
    public void setCatPredioPropietarioCollection(List<CatPredioPropietario> catPredioPropietarioCollection) {
        this.catPredioPropietarioCollection = catPredioPropietarioCollection;
    }
    
    public CatPredio getPredioWs() {
        return predioWs;
    }
    
    public void setPredioWs(CatPredio predioWs) {
        this.predioWs = predioWs;
    }
    
    public int getTipoEnte() {
        return tipoEnte;
    }
    
    public void setTipoEnte(int tipoEnte) {
        this.tipoEnte = tipoEnte;
    }
    
    public BigDecimal getValorPorcentajeExoneracion() {
        return valorPorcentajeExoneracion;
    }
    
    public void setValorPorcentajeExoneracion(BigDecimal valorPorcentajeExoneracion) {
        this.valorPorcentajeExoneracion = valorPorcentajeExoneracion;
    }
//</editor-fold>
}
