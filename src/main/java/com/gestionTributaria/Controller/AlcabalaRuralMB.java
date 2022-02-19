/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.CatPredioPropieatrioDTO;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AlcabalaRuralMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(AlcabalaRuralMB.class.getName());

    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private ManagerService services;

//    private List<PredioRural> prediosRurales;
    private List<CatPredioModel> prediosLazy;
    private CatPredioModel rural;
    private CatPredioPropieatrioDTO propietario;
    private String claveCatastral;
    private CatPredioPropieatrioDTO vendedor, pro;
    private FinaRenLiquidacion liquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenRubrosLiquidacion> rubrosLiquidacion;
    protected Cliente comprador;
    protected Usuarios usr;
    private Cliente propietarioGeneral;
    private boolean ventaPrimeraVez;
    private Map<String, Object> param;
    private Boolean esPersonaComp = true;

    @PostConstruct
    public void initView() {
        iniciarDatos();
        loadPredios();

    }

    public void iniciarDatos() {
        try {
            propietarioGeneral = new Cliente();
            comprador = new Cliente();
            claveCatastral = "";
            ventaPrimeraVez = false;
            param = new HashMap<>();
            param.put("prefijo", "ALC");
            tipoLiquidacion = (FinaRenTipoLiquidacion) services.find(FinaRenTipoLiquidacion.class, param);
            initLiquidacion();
            consultarRubros();
            vendedor = new CatPredioPropieatrioDTO();
            param = new HashMap<>();
            param.put("usuario", session.getNameUser());
            usr = (Usuarios) services.find(Usuarios.class, param);
        } catch (Exception e) {
            LOG.log(Level.OFF, "Iniciar vista", e);
        }
    }

    public void loadPredios() {
        //consumir todos los predios
        prediosLazy = new ArrayList<>();
        //prediosLazy = new LazyModel<>(CatPredioModel.class);
        //prediosLazy.getFilterss().put("tipoPredio", "R");
    }

    public void consultarRubros() {
        rubrosLiquidacion = new ArrayList<>();
        if (tipoLiquidacion != null) {
            param = new HashMap<>();
            param.put("tipoLiquidacion.id", tipoLiquidacion.getId());
            param.put("estado", true);
            rubrosLiquidacion = services.findAll(FinaRenRubrosLiquidacion.class, param);

            initLiquidacion();

            liquidacion.setValidada(Boolean.FALSE);
            rubrosLiquidacion.stream().map((ru) -> {
                ru.setValorTotal(ru.getValor());
                return ru;
            }).forEachOrdered((ru) -> {
                ru.getValorTotal().setScale(2, RoundingMode.HALF_UP);
                ru.setCobrar(true);
            });
        }
    }

    public void initLiquidacion() {
        liquidacion = new FinaRenLiquidacion();
        liquidacion.setTotalPago(new BigDecimal(0));
        liquidacion.setFechaContratoAnt(new Date());
        liquidacion.setCostoAdq(BigDecimal.ZERO);
        liquidacion.setCuantia(BigDecimal.ZERO);
    }

    public void onRowSelectRural(SelectEvent event) {
        // buscarEnteByIdentificacion();
        liquidacion.setValorComercial(rural.getAvaluoMunicipal());
        liquidacion.setCuantia(rural.getAvaluoMunicipal());

        calcularAlcabala();

    }

    public void onRowUnselectRural(UnselectEvent event) {
        System.out.println("UNSELECT EVENT");
        CatPredioModel rr = (CatPredioModel) event.getObject();
        if (rr.getClaveCat().equals(rural.getClaveCat())) {
            rural = null;
        }
    }

    public void buscarEnteByIdentificacion() {
        if (rural != null) {

            /**
             * consumir web services loss predios de los propieatiros con objeto
             * llamado "rural"
             */
            List<CatPredioPropieatrioDTO> catPredioPropietarioCollection = new ArrayList<>();
            Cliente e = null;
            if (catPredioPropietarioCollection != null) {
                propietario = new CatPredioPropieatrioDTO();
                propietario = catPredioPropietarioCollection.get(0);
                param = new HashMap<>();
                param.put("identificacion", propietario.getCiuCedRuc());
                e = services.find(Cliente.class, param);
                propietarioGeneral = e;
            }

//            CatEnte e = (CatEnte) services.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{rural.get});
            if (e != null) {
                liquidacion.setVendedor(e);
                liquidacion.setIdentificacion(null);
                liquidacion.setNombreCompradorHistoric(null);
                System.out.println("Id ente: " + e.getId());
            } else {
                liquidacion.setVendedor(null);
                liquidacion.setIdentificacion(propietario.getCiuCedRuc());
                liquidacion.setNombreCompradorHistoric(propietario.getCiuCedRuc());
            }
        }
    }

    public void seletComprador(SelectEvent event) {
        this.comprador = (Cliente) event.getObject();
        if (this.comprador == null) {
            this.comprador = new Cliente();
        }
        liquidacion.setComprador(this.comprador);
    }

    public void preProccess() {
        if (liquidacion.getTotalPago().doubleValue() <= 0) {
            JsfUtil.addErrorMessage("Erro", "Ingrese los valores a liquidar.");
            return;
        }
        if (this.comprador.getId() != null) {
            JsfUtil.executeJS("PF('obs').show()");
        } else {
            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el comprador");
            return;
        }
    }

    public void openDlgRural() {
        if (claveCatastral != null) {

            //consumir web services con la clave catastral y el tipo del predio
            rural = null; //(claveCatastral, "R");
        }

        if (rural == null) {
            rural = new CatPredioModel();
            JsfUtil.executeJS("PF('dlgRural').show()");
        }

    }

    public String getNombreVendedor(Cliente ente) {
        if (ente != null) {
            if (ente.getTipoProv().getCodigo() == "PER_NAT") {
                return Utils.isEmpty(ente.getApellido()) + " " + Utils.isEmpty(ente.getNombre());
            } else {
                return Utils.isEmpty(ente.getRazonSocial());
            }
        }
        return "";
    }

    public void procesar() {

        if (this.comprador.getId() == null) {
            JsfUtil.addErrorMessage("Error", "Falta ingresar el nombre del comprador");
            return;
        }
        if (liquidacion.getCuantia() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar la cuantia.");
            return;
        }
        if (liquidacion.getTotalPago().doubleValue() <= 0) {
            JsfUtil.addErrorMessage("Error", "MessagesRentas.total");
            return;
        }

        try {

            liquidacion.setComprador(comprador);

            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.setEstadoLiquidacion(services.getEstadoLiquidacionByDesc(2L));
            liquidacion.setTipoLiquidacion(tipoLiquidacion);
            liquidacion.setCoactiva(false);

            liquidacion.setVendedorRural(propietarioGeneral.getNombreCompleto().replaceAll("\\s{2,}", " ").trim());
            liquidacion.setVendedorRuralIdentificacion(propietarioGeneral.getIdentificacion().replaceAll("\\s{2,}", " ").trim());
            liquidacion.setClaveCatastralRural(rural.getClaveCat().replaceAll("\\s{2,}", " ").trim());
            if (rural.getCalle() != null) {
                liquidacion.setUbicacion(rural.getCalle().replaceAll("\\s{2,}", " ").trim());
            }

            final String prefijo = tipoLiquidacion.getPrefijo();
            liquidacion = services.guardarLiquidacion(liquidacion, rubrosLiquidacion, prefijo, null);

            if (liquidacion != null) {
                if (services.generarNumLiquidacion(liquidacion, prefijo)) {
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

    public void borrarDatos() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.instanciarParametros();
        ss.setNombreDocumento(liquidacion.getIdLiquidacion());
        ss.setNombreSubCarpeta("RentasMontecristi/LiquidacionesRural");
        //    ss.setTieneDatasource(true);
        ss.addParametro("LIQUIDACION", liquidacion.getId());
        ss.addParametro("LOGO", path + SisVars.sisLogo);
        ss.addParametro("QR", liquidacion.getIdLiquidacion());
        /*MARCA DE AGUA*/
        ss.addParametro("Fondo", path + SisVars.sisMarcaAgua);

        /*FIRMAS*/
        ss.addParametro("FirmaRentas", path + SisVars.sisFirmaRentas);
        ss.addParametro("FirmaTesorero", path + SisVars.sisFirmaTesorero);

        ss.setNombreReporte("sEspecieAlcabalasRural");
        initView();

        comprador = null;
        rural = null;
        JsfUtil.redirectNewTab("Documento");
    }

    public void llenarAlcalbala(Boolean continuar) {
        if (continuar) {
            final Boolean p = "PER_NAT".equals(comprador.getTipoProv().getCodigo());
//            initLiquidacion();
            consultarRubros();
            JsfUtil.update("frmAlcPlus");
            setEsPersonaComp(p);
        } else {
            iniciarDatos();
            tipoLiquidacion = null;
            setComprador(null);

        }
        JsfUtil.update("frmAlcPlus");
    }

    //***** Inicio Caculos Alcabalas *****//
    public void calcularAlcabala() {
        try {
            if (liquidacion.getCuantia() != null) {
                if (ventaPrimeraVez) {
                    liquidacion.setValorComercial(liquidacion.getCuantia());
                } else {
                    if (liquidacion.getCuantia().compareTo(rural.getAvaluoMunicipal()) > 0) {
                        liquidacion.setValorComercial(liquidacion.getCuantia());
                    } else {
                        liquidacion.setValorComercial(rural.getAvaluoMunicipal());

                    }
                }
                liquidacion.setTotalPago(calcularTotalAlcabalas());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }

    }

    private BigDecimal calcularTotalAlcabalas() {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal div = BigDecimal.valueOf(100);
        for (FinaRenRubrosLiquidacion rb : rubrosLiquidacion) {
            if (rb.getTipoValor().getId().equals(2L)) {
                if (rb.getId() != 339) {
                    rb.setValorTotal(getValorRubro(rb.getValor(), liquidacion.getValorComercial(), div));
                    if (liquidacion.getPorcientoRebaja().compareTo(div) != 1 && rb.getId() == 338) {
                        BigDecimal val = rb.getValorTotal();
                        rb.setValorTotal(rb.getValorTotal().subtract(rb.getValorTotal().multiply(liquidacion.getPorcientoRebaja()).divide(div)));
                        rb.getValorTotal().setScale(2, RoundingMode.HALF_UP);
                        liquidacion.setEstaExonerado(true);
                    } else {
                        liquidacion.setEstaExonerado(false);
                    }

                } else {
                    rb.setValorTotal(rb.getValor());
                    rb.getValorTotal().setScale(2, RoundingMode.HALF_UP);
                }
            }
            if (rb.getCobrar()) {

                total = total.add(rb.getValorTotal().setScale(2, RoundingMode.HALF_UP));
            }
        }
        return total;
    }

    private BigDecimal getValorRubro(BigDecimal porRub, BigDecimal cuantia, BigDecimal div) {
        return (porRub.divide(div)).multiply(cuantia).setScale(2, RoundingMode.HALF_UP);
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public List<CatPredioModel> getPrediosLazy() {
        return prediosLazy;
    }

    public void setPrediosLazy(List<CatPredioModel> prediosLazy) {
        this.prediosLazy = prediosLazy;
    }

    public CatPredioModel getRural() {
        return rural;
    }

    public void setRural(CatPredioModel rural) {
        this.rural = rural;
    }

    public CatPredioPropieatrioDTO getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropieatrioDTO propietario) {
        this.propietario = propietario;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public CatPredioPropieatrioDTO getVendedor() {
        return vendedor;
    }

    public void setVendedor(CatPredioPropieatrioDTO vendedor) {
        this.vendedor = vendedor;
    }

    public CatPredioPropieatrioDTO getPro() {
        return pro;
    }

    public void setPro(CatPredioPropieatrioDTO pro) {
        this.pro = pro;
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

    public Usuarios getUsr() {
        return usr;
    }

    public void setUsr(Usuarios usr) {
        this.usr = usr;
    }

    public Cliente getPropietarioGeneral() {
        return propietarioGeneral;
    }

    public void setPropietarioGeneral(Cliente propietarioGeneral) {
        this.propietarioGeneral = propietarioGeneral;
    }

    public boolean isVentaPrimeraVez() {
        return ventaPrimeraVez;
    }

    public void setVentaPrimeraVez(boolean ventaPrimeraVez) {
        this.ventaPrimeraVez = ventaPrimeraVez;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Boolean getEsPersonaComp() {
        return esPersonaComp;
    }

    public void setEsPersonaComp(Boolean esPersonaComp) {
        this.esPersonaComp = esPersonaComp;
    }

}
