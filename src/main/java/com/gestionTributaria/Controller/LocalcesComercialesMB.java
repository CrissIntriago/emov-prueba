/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.*;
import com.gestionTributaria.Entities.*;

import com.gestionTributaria.Commons.GroovyUtil;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Commons.SisVars;

import com.gestionTributaria.Services.ManagerService;
import com.itextpdf.awt.geom.misc.Messages;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "localesComerciales")
@ViewScoped
public class LocalcesComercialesMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(LocalcesComercialesMB.class.getName());

    @Inject
    private ManagerService services;

    @Inject
    private ServletSession ss;
    @Inject
    private CatalogoService catalogoService;

    @Inject
    private UserSession session;

    protected String emailNew = "";
    protected String tlfnNew = "";
    private Cliente persona;
    private FinaRenPatente patente;
    private LazyModel<FinaRenPatente> patentes;
    private RenBalancePatente balance;
    private FinaRenActividadComercial actividadPrincipal;
    private List<FinaRenActividadComercial> actividades;

    private Integer anioSolicitudPermiso;

    private Integer tipo = 1;
    private Integer tipoCons = 2;
    private Integer maxAnio;
    private Integer maxAnioAct;
    private Integer minAnio = 1990;
    private Boolean esPersonaProp = true;
    private Boolean esLocal = false, variosRotulos = false;
    private BigDecimal valor;

    private Cliente propietario;
    private FinaRenLiquidacion liquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacionPermiso;
    private List<FinaRenTipoLiquidacion> tiposLiquidacions;
    private List<FinaRenRubrosLiquidacion> rubrosLiquidacion;
    private RenDesvalorizacion desvalorizacion;

    private Boolean mostrarRequisitos;
    private String cedulaRuc;
    private List<FinaRenLocalComercial> localesEnte;
    private LazyModel<FinaRenLocalComercial> localesLazy;
    private FinaRenLocalComercial localSel;
    private List<FinaRenDetLiquidacion> detalle;
    // Variables para locales comerciales
    private RenActivosLocalComercial local;
    private FinaRenPatente renPropietarioPatente;
// Fin variables locales comerciales

    // Inicio Variables para caculos de valores
    private GroovyUtil util;

    private short tipoExoneracion = 0;
    private BigDecimal tasaPermiso;
    private int mesesInteres = 0;
    // Fin Variables formula

    private Map<String, Object> parametros;

    @PostConstruct
    public void initView() {
        iniciarDatos();
    }

    public void iniciarDatos() {
        persona = new Cliente();
        Integer numero = 14;
        parametros = new HashMap<>();
        parametros.put("tituloReporte", numero.longValue());
        parametros.put("estado", true);

//        tipoLiquidacion = (FinaRenTipoLiquidacion) servs.find(QuerysFinanciero.getFinaRenTipoLiquidacionByCodigoReporte);
//SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = true AND r.codigoTituloReporte = :tituloReporte ORDER BY r.nombreTransaccion ASC
        tipoLiquidacion = (FinaRenTipoLiquidacion) services.find("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = :estado AND r.codigoTituloReporte = :tituloReporte ORDER BY r.nombreTransaccion ASC",
                new String[]{"estado", "tituloReporte"}, new Object[]{true, numero.longValue()});
        tipoLiquidacionPermiso = (FinaRenTipoLiquidacion) services.find(FinaRenTipoLiquidacion.class, 21L);
        tiposLiquidacions = services.gettiposLiquidacionByCodTitRep(2);
        patentes = new LazyModel(FinaRenPatente.class);
        initLiquidacion();
        consultarRubros();
        localesLazy = new LazyModel(FinaRenLocalComercial.class
        );
        maxAnio = Utils.getAnio(new Date()) - 1;
        maxAnioAct = Utils.getAnio(new Date());
        tasaPermiso = BigDecimal.ZERO;
        balance = new RenBalancePatente();
        this.anioSolicitudPermiso = Utils.getAnio(new Date());
        actividadPrincipal = null;
        actividades = new ArrayList<>();
    }

    public void openDlgPatente() {
        if (cedulaRuc != null) {
            if (!cedulaRuc.equals("")) {
                renPropietarioPatente = services.buscarCiRucPatentePermiso(cedulaRuc);
                if (renPropietarioPatente != null) {
                    seleccionarPatente(renPropietarioPatente);
                } else {
                    JsfUtil.addErrorMessage("Info", "El numero de identificaicon no exsite");
                    JsfUtil.executeJS("PF('dlgPatentes').show();");
                }
            } else {
                JsfUtil.addErrorMessage("Info", "Seleccione un contribuyente valido");
                JsfUtil.executeJS("PF('dlgPatentes').show();");
            }
        }
    }

    public void openDlgActividades(FinaRenLocalComercial l) {

        if (l.getRenActividadComercialCollection().isEmpty()) {
            JsfUtil.addErrorMessage("Info", "El local seleccionado no tiene actividad registrada");
            return;
        }
        seleccionar(l);
        actividades = (List<FinaRenActividadComercial>) l.getRenActividadComercialCollection();
        actividadPrincipal = null;
        JsfUtil.addInformationMessage("Info", "Local comercial seleccionado correctamente");
        JsfUtil.executeJS("PF('dlgActividades').show();");
    }

    public void seleccionarPatente(FinaRenPatente patente) {
        try {
            this.patente = patente;
            if (patente.getPropietario() != null) {
                this.cedulaRuc = patente.getPropietario().getIdentificacion() + patente.getPropietario().getRuc();
            } else {
                this.cedulaRuc = "000000000";
            }
            JsfUtil.addInformationMessage("Info", "Patente seleccionada correctamente");
            JsfUtil.executeJS("PF('dlgPatentes').hide();");
        } catch (Exception e) {
            LOG.log(Level.OFF, "Seleccionar Local", e);
        }
    }

    public void seleccionarActividad(FinaRenActividadComercial act) {
        try {
            this.actividadPrincipal = act;
            JsfUtil.addInformationMessage("Info", "Actividad seleccionada correctamente");
            JsfUtil.executeJS("PF('dlgActividades').hide();");
        } catch (Exception e) {
            LOG.log(Level.OFF, "Seleccionar Local", e);
        }
    }

    public void seleccionar(FinaRenLocalComercial local) {
        System.out.println("el tipo de local desde el metodo: " + local.getTipoLocal());
        try {
            if (this.anioSolicitudPermiso != null) {
                this.liquidacion.setAnio(this.anioSolicitudPermiso - 1);
                this.liquidacion.setPatente(this.patente);

                this.liquidacion.setTipoLiquidacion(this.tipoLiquidacion);
                Integer existeLiquidacion = services.existeLiquidacionPatente(this.liquidacion);
                System.out.println("la cosa fea es :" + existeLiquidacion);
                if (existeLiquidacion != null) {
                    if (existeLiquidacion.equals(2)) {
                        JsfUtil.addErrorMessage("Liquidación", "La patente seleccionada no ha pagado el impuesto del año " + (this.anioSolicitudPermiso - 1) + " para poder emitir el permiso del año " + this.anioSolicitudPermiso);
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("Liquidación", "La patente seleccionada no ha pagado el impuesto del año " + (this.anioSolicitudPermiso - 1) + " para poder emitir el permiso del año " + this.anioSolicitudPermiso);
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Info", " Debe seleccionar el año de solucitid del permiso.");
                return;
            }
            this.localSel = local;
            this.liquidacion.setTipoLiquidacion(this.tipoLiquidacionPermiso);
            this.liquidacion.setLocalComercial(local);
            //this.generarValorLiquidacion();0

        } catch (Exception e) {
            LOG.log(Level.OFF, "Seleccionar Local", e);
        }
    }

    public void consultarRubros() {
        try {
            rubrosLiquidacion = new ArrayList<>();
            detalle = new ArrayList();
            if (tipoLiquidacionPermiso != null) {
                Map<String, Object> par = new HashMap<>();
                par.put("id", tipoLiquidacionPermiso.getId());
                par.put("estado", true);
                rubrosLiquidacion = services.findAllQuery("select r from FinaRenRubrosLiquidacion r where r.tipoLiquidacion.id=:id and r.estado=:estado", par);
                initLiquidacion();
                detalle.add(new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "VALOR DE PATENTE"));
                for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                    detalle.add(new FinaRenDetLiquidacion(temp.getValor(), temp, temp.getDescripcion()));
                }

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cargar Rubros", e);
        }
        JsfUtil.update("frmAlcPlus");
    }

    public List<FinaRenActividadComercial> getActividades() {
        return actividades;
    }

    public void setActividades(List<FinaRenActividadComercial> actividades) {
        this.actividades = actividades;
    }

    public FinaRenActividadComercial getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(FinaRenActividadComercial actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionPermiso() {
        return tipoLiquidacionPermiso;
    }

    public void setTipoLiquidacionPermiso(FinaRenTipoLiquidacion tipoLiquidacionPermiso) {
        this.tipoLiquidacionPermiso = tipoLiquidacionPermiso;
    }

    public Integer getAnioSolicitudPermiso() {
        return anioSolicitudPermiso;
    }

    public void setAnioSolicitudPermiso(Integer anioSolicitudPermiso) {
        this.anioSolicitudPermiso = anioSolicitudPermiso;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public RenBalancePatente getBalance() {
        return balance;
    }

    public void setBalance(RenBalancePatente balance) {
        this.balance = balance;
    }

    public void selectLocal(FinaRenLocalComercial local) {
        try {
            if (localesEnte == null) {
                localesEnte = new ArrayList<>();
            }
            if (!localesEnte.contains(local)) {
                localesEnte.add(local);
            } else {
                JsfUtil.addInformationMessage("Info", "No se puede agregar dos veces el mismo local comercial");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarValorLiquidacion() {
        try {
            RenFactorPorMetro factor1;
            RenFactorPorCapital factor2;
            BigDecimal valorLiq;

            if (balance.getCapital() != null && balance.getCapital().compareTo(BigDecimal.ZERO) > 0) {
                if (localSel != null) {
                    factor2 = (RenFactorPorCapital) services.find("SELECT r FROM RenFactorPorCapital r WHERE :capital > r.desde AND :capital <= r.hasta", new String[]{"capital"}, new Object[]{balance.getCapital()});
                    if (factor2.getAplicaPorcentaje()) {
                        valor = balance.getCapital().multiply(factor2.getValor()).divide(new BigDecimal("100"));
                    } else {
                        valor = factor2.getValor();
                    }

                    switch (tipoExoneracion) {
                        case 1: {
                            valor = BigDecimal.ZERO;
                            break;
                        }
                        case 2: {
                            valor = valor.divide(new BigDecimal("3"));
                            break;
                        }
                        case 3: {
                            valor = valor.divide(new BigDecimal("2"));
                            break;
                        }
                    }

                    if (valor.compareTo(new BigDecimal("25000")) > 0) {
                        valor = new BigDecimal("25000");
                    }

                    detalle.get(0).setValor(valor);

                    detalle.get(1).setValor(tasaPermiso);
                    liquidacion.setTotalPago(tasaPermiso);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarEnte() {
        if (cedulaRuc == null || cedulaRuc.trim().length() == 0) {
            JsfUtil.addErrorMessage("Advertencia", "Debe Ingresar el Numero de Cedula o RUC");
            return;
        }
        mostrarRequisitos = false;
        propietario = (Cliente) services.find("select e from CatEnte e where e.identificacion = :ciRuc", new String[]{"ciRuc"}, new Object[]{cedulaRuc.substring(0, 10)});
        if (propietario != null) {
            mostrarRequisitos = true;
            localesEnte = propietario.getLocalesComercialesCollection();
            esPersonaProp = "PER_NA".equals(propietario.getTipoProv().getCodigo());
        } else {
            mostrarRequisitos = false;
            propietario = null;
            tipoLiquidacion = null;
            persona = new Cliente();
            //cedulaRuc.length() == 10

            if (cedulaRuc.length() == 10) {
                persona.setIdentificacion(cedulaRuc);
                persona.setTipoProv(catalogoService.getItemByCatalogoAndCodigo("personeria_juridica", "PER_NAT"));
            } else if (cedulaRuc.length() > 10) {
                persona.setIdentificacion(cedulaRuc.substring(0, 10));
                persona.setTipoProv(catalogoService.getItemByCatalogoAndCodigo("personeria_juridica", "PER_JUR"));
                persona.setRuc("001");
            }
            JsfUtil.addInformationMessage("Informacion", "La persona no existe");
            JsfUtil.update("formSolicitante");
            JsfUtil.executeJS("PF('dlgSolInf').show();");
        }

    }

    public void guardarSolicitante() {
        Boolean flag;
        if (persona.getId() == null) {
            persona = (Cliente) services.save(persona);
            flag = true;
        } else {
            flag = services.update(persona);
        }
        if (flag) {
            propietario = persona;
            JsfUtil.executeJS("PF('dlgSolInf').hide();");
            JsfUtil.addInformationMessage("Info", "Se creó el usuario correctamente.");
        } else {
            JsfUtil.addErrorMessage("Error", "No se pudo guardar los datos correctamente. Modifique los datos e intente de nuevo.");
        }
    }

    public void cancelarGuardado() {
        emailNew = "";
        tlfnNew = "";
        persona = new Cliente();
    }

    public void initLiquidacion() {
        liquidacion = new FinaRenLiquidacion();
        liquidacion.setTotalPago(new BigDecimal(0));
        liquidacion.setFechaContratoAnt(new Date());
        liquidacion.setCostoAdq(BigDecimal.ZERO);
        liquidacion.setCuantia(BigDecimal.ZERO);
        liquidacion.setAnio(Utils.getAnio(new Date()));
        localSel = null;
    }

    public void buscarLocal() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "95%");
        options.put("height", "85%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic("localesComerciales_", options, null);
    }

    public void seleccionarLocal(SelectEvent event) {
        try {
            selectLocal((FinaRenLocalComercial) event.getObject());
            seleccionar((FinaRenLocalComercial) event.getObject());
            esPersonaProp = "PER_NA".equals(propietario.getTipoProv().getCodigo());
            if (tipoLiquidacion.getCodigoTituloReporte() == 98) {
//                if (!this.localSel.getTurismo()) {
//                    JsfUtil.addErrorMessage("Advertencia", "Local comercial seleccionado no tiene categoria de Turismo");
//                    return;
//                }
            }
            JsfUtil.update("frmAlcPlus");
        } catch (Exception e) {
            LOG.log(Level.OFF, "Seleccionar Local", e);
        }
    }

    public void procesar() {
        try {
            if (localSel == null) {
                JsfUtil.addErrorMessage("Error", MessagesRentas.faltaIngresar.concat("local comercial"));
                return;
            }
            if (localSel.getId() == null) {
                JsfUtil.addErrorMessage("Error", MessagesRentas.faltaIngresar.concat("local comercial"));
                return;
            }

            Object numLiquidacion = null;
            liquidacion.setComprador(patente.getPropietario());
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            if (liquidacion.getEstadoLiquidacion() == null) {

                Map<String, Object> par = new HashMap<>();
                par.put("id", 2L);

                liquidacion.setEstadoLiquidacion(services.findByParameter(FinaRenEstadoLiquidacion.class, par));
            }

            liquidacion.setTipoLiquidacion(tipoLiquidacionPermiso);
            liquidacion.setLocalComercial(localSel);
            liquidacion.setPatente(patente);

            detalle.remove(0);
            liquidacion.setSaldo(liquidacion.getTotalPago());
            liquidacion.setValidada(Boolean.FALSE);
            balance.setPatente(patente);
            balance.setLocalComercial(localSel);
            balance.setAnioBalance(anioSolicitudPermiso - 1);
            liquidacion.setAnio(anioSolicitudPermiso - 1);
            liquidacion = services.guardarLiquidacionPatente(liquidacion, detalle, tipoLiquidacionPermiso, balance);
            if (liquidacion != null) {
                JsfUtil.executeJS("PF('obs').hide()");
                JsfUtil.executeJS("PF('dlgIdLiquidacion').show()");
                JsfUtil.update("numLiq:dlgIdLiquidacion");
                imprimir();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar Liquidacion LC", e);
        }
    }

    /*MODIFICADO POR JC*/
    private void imprimir() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.instanciarParametros();
        //ss.setDataSource();
        ss.addParametro("LOGO", path.concat(SisVars.logoReportes));
        ss.addParametro("LIQUIDACION", liquidacion.getId());
        ss.addParametro("ID_LOCAL_COMERCIAL", localSel.getId());
        ss.addParametro("NUM_TRAMITE", liquidacion.getNumReporte());
        ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
        ss.setNombreReporte("sComprobantePermisoFuncionamiento");
        ss.addParametro("FirmaRentas", path + SisVars.sisFirmaRentas);
        ss.addParametro("FirmaTesorero", path + SisVars.sisFirmaTesorero);

    }

    public void borrar() {
        iniciarDatos();
        localSel = null;
        esPersonaProp = true;
        localesEnte = null;
        propietario = null;
        tipoLiquidacion = null;
        cedulaRuc = null;
        patente = null;
    }

    public void validar() {
        if (actividadPrincipal == null) {
            JsfUtil.addErrorMessage("Actividad", "Debe seleccionar la actividad principal del local.");
            return;
        }
        liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liquidacion.setIpUserSession(session.getIpClient());
        liquidacion.setLocalComercial(localSel);
        liquidacion.setTipoLiquidacion(tipoLiquidacion);
        Integer existeLiquidacion = null;
        if (!variosRotulos) {
            existeLiquidacion = services.existeLiquidacion(liquidacion);
        } else {
            existeLiquidacion = 3;
        }
        if (existeLiquidacion == 3) {
            JsfUtil.executeJS("PF('obs').show()");
        } else if (existeLiquidacion == 2) {
            JsfUtil.addErrorMessage("Liquidación", "Ya existe una Liquidación activa de " + tipoLiquidacion.getNombreTitulo() + "para el local seleccionado");
        } else {
            JsfUtil.addErrorMessage("Liquidación", "Liquidación de " + tipoLiquidacion.getNombreTitulo() + "para el local seleccionado, ya fue pagado.");
        }
    }

    public void seleccionarComprador(SelectEvent event) {
        Cliente repLagTem = (Cliente) Utils.clone(event.getObject());
        persona.setIdRepresentanteLegal(repLagTem.getId());

        persona.setNombreCompleto(repLagTem.getNombreCompleto());
    }

    /*ACTUALIZADO JC 06/04/2018*/
    public void seleccionarContribuyente() {
        if (cedulaRuc == null || cedulaRuc.equals("")) {
            this.cedulaRuc = propietario.getIdentificacion();
            mostrarRequisitos = true;
            localesEnte = propietario.getLocalesComercialesCollection();
            esPersonaProp = "PER_NA".equals(propietario.getTipoProv().getCodigo());
        }

    }

    public void openDlgSolicitante() {
        JsfUtil.executeJS("PF('dlgPatentes').show();");
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public String getEmailNew() {
        return emailNew;
    }

    public void setEmailNew(String emailNew) {
        this.emailNew = emailNew;
    }

    public String getTlfnNew() {
        return tlfnNew;
    }

    public void setTlfnNew(String tlfnNew) {
        this.tlfnNew = tlfnNew;
    }

    public Cliente getPersona() {
        return persona;
    }

    public void setPersona(Cliente persona) {
        this.persona = persona;
    }

    public LazyModel<FinaRenPatente> getPatentes() {
        return patentes;
    }

    public void setPatentes(LazyModel<FinaRenPatente> patentes) {
        this.patentes = patentes;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public Integer getMaxAnio() {
        return maxAnio;
    }

    public void setMaxAnio(Integer maxAnio) {
        this.maxAnio = maxAnio;
    }

    public Integer getMaxAnioAct() {
        return maxAnioAct;
    }

    public void setMaxAnioAct(Integer maxAnioAct) {
        this.maxAnioAct = maxAnioAct;
    }

    public Integer getMinAnio() {
        return minAnio;
    }

    public void setMinAnio(Integer minAnio) {
        this.minAnio = minAnio;
    }

    public Boolean getEsPersonaProp() {
        return esPersonaProp;
    }

    public void setEsPersonaProp(Boolean esPersonaProp) {
        this.esPersonaProp = esPersonaProp;
    }

    public Boolean getEsLocal() {
        return esLocal;
    }

    public void setEsLocal(Boolean esLocal) {
        this.esLocal = esLocal;
    }

    public Boolean getVariosRotulos() {
        return variosRotulos;
    }

    public void setVariosRotulos(Boolean variosRotulos) {
        this.variosRotulos = variosRotulos;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
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

    public RenDesvalorizacion getDesvalorizacion() {
        return desvalorizacion;
    }

    public void setDesvalorizacion(RenDesvalorizacion desvalorizacion) {
        this.desvalorizacion = desvalorizacion;
    }

    public Boolean getMostrarRequisitos() {
        return mostrarRequisitos;
    }

    public void setMostrarRequisitos(Boolean mostrarRequisitos) {
        this.mostrarRequisitos = mostrarRequisitos;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
    }

    public List<FinaRenLocalComercial> getLocalesEnte() {
        return localesEnte;
    }

    public void setLocalesEnte(List<FinaRenLocalComercial> localesEnte) {
        this.localesEnte = localesEnte;
    }

    public LazyModel<FinaRenLocalComercial> getLocalesLazy() {
        return localesLazy;
    }

    public void setLocalesLazy(LazyModel<FinaRenLocalComercial> localesLazy) {
        this.localesLazy = localesLazy;
    }

    public FinaRenLocalComercial getLocalSel() {
        return localSel;
    }

    public void setLocalSel(FinaRenLocalComercial localSel) {
        this.localSel = localSel;
    }

    public List<FinaRenDetLiquidacion> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FinaRenDetLiquidacion> detalle) {
        this.detalle = detalle;
    }

    public RenActivosLocalComercial getLocal() {
        return local;
    }

    public void setLocal(RenActivosLocalComercial local) {
        this.local = local;
    }

    public FinaRenPatente getRenPropietarioPatente() {
        return renPropietarioPatente;
    }

    public void setRenPropietarioPatente(FinaRenPatente renPropietarioPatente) {
        this.renPropietarioPatente = renPropietarioPatente;
    }

    public GroovyUtil getUtil() {
        return util;
    }

    public void setUtil(GroovyUtil util) {
        this.util = util;
    }

    public short getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(short tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public BigDecimal getTasaPermiso() {
        return tasaPermiso;
    }

    public void setTasaPermiso(BigDecimal tasaPermiso) {
        this.tasaPermiso = tasaPermiso;
    }

    public int getMesesInteres() {
        return mesesInteres;
    }

    public void setMesesInteres(int mesesInteres) {
        this.mesesInteres = mesesInteres;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

}
