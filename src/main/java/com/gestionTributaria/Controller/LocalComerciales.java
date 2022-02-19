/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.RenActivosLocalComercial;
import com.gestionTributaria.Entities.RenBalancePatente;
import com.gestionTributaria.Entities.RenDesvalorizacion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
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
import javassist.bytecode.analysis.Util;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */

@Named
@ViewScoped
public class LocalComerciales implements Serializable {

    @Inject
    private ManagerService services;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;

    private FinaRenPatente patente;
    private LazyModel<FinaRenPatente> patentes;
    private RenBalancePatente balance;
    private FinaRenActividadComercial actividadPrincipal;
    private List<FinaRenActividadComercial> actividades;
    private Cliente persona;
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

    private short tipoExoneracion = 0;
    private BigDecimal tasaPermiso;
    private int mesesInteres = 0;
    // Fin Variables formula
    private Map<String, Object> param;

    @PostConstruct
    public void initView() {
        iniciarDatos();
    }

    public void iniciarDatos() {
        persona = new Cliente();
        Integer numero = 14;
        param = new HashMap<>();
        param.put("codigoTituloReporte", numero.longValue());
        param.put("estado", true);

        tipoLiquidacion = (FinaRenTipoLiquidacion) services.findByParameter(FinaRenTipoLiquidacion.class, param);
        tipoLiquidacionPermiso = (FinaRenTipoLiquidacion) services.find(FinaRenTipoLiquidacion.class, 21L);
        tiposLiquidacions = services.gettiposLiquidacionByCodTitRep(2);
        patentes = new LazyModel(FinaRenPatente.class);
        /* descomentar
        initLiquidacion();
        consultarRubros();
         */
        localesLazy = new LazyModel(FinaRenLocalComercial.class);
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
                    /* descomentar
                    seleccionarPatente(renPropietarioPatente);
                     */
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
//
//    public void openDlgActividades(FinaRenLocalComercial l) {
//
//        if (l.getRenActividadComercialCollection().isEmpty()) {
//            JsfUti.messageError(null, "Info", "El local seleccionado no tiene actividad registrada");
//            return;
//        }
//        seleccionar(l);
//        actividades = (List<RenActividadComercial>) l.getRenActividadComercialCollection();
//        actividadPrincipal = null;
//        JsfUtil.addInformationMessage("Info", "Local comercial seleccionado correctamente");
//        JsfUtil.executeJS("PF('dlgActividades').show();");
//    }
//
//    public void seleccionarPatente(RenPatente patente) {
//        try {
//            this.patente = patente;
//            this.cedulaRuc = patente.getPropietario().getCiRuc();
//            JsfUti.messageInfo(null, "Info", "Patente seleccionada correctamente");
//            JsfUti.executeJS("PF('dlgPatentes').hide();");
//        } catch (Exception e) {
//            LOG.log(Level.OFF, "Seleccionar Local", e);
//        }
//    }
//
//    public void seleccionarActividad(RenActividadComercial act) {
//        try {
//            this.actividadPrincipal = act;
//            JsfUti.messageInfo(null, "Info", "Actividad seleccionada correctamente");
//            JsfUti.executeJS("PF('dlgActividades').hide();");
//        } catch (Exception e) {
//            LOG.log(Level.OFF, "Seleccionar Local", e);
//        }
//    }
//
//    public void seleccionar(RenLocalComercial local) {
//        System.out.println("el tipo de local desde el metodo: " + local.getTipoLocal());
//        try {
//            if (this.anioSolicitudPermiso != null) {
//                this.liquidacion.setAnio(this.anioSolicitudPermiso - 1);
//                this.liquidacion.setPatente(this.patente);
//
//                this.liquidacion.setTipoLiquidacion(this.tipoLiquidacion);
//                Integer existeLiquidacion = services.existeLiquidacionPatente(this.liquidacion);
//                System.out.println("la cosa fea es :" + existeLiquidacion);
//                if (existeLiquidacion != null) {
//                    if (existeLiquidacion.equals(2)) {
//                        JsfUti.messageError(null, "Liquidación", "La patente seleccionada no ha pagado el impuesto del año " + (this.anioSolicitudPermiso - 1) + " para poder emitir el permiso del año " + this.anioSolicitudPermiso);
//                        return;
//                    }
//                } else {
//                    JsfUti.messageError(null, "Liquidación", "La patente seleccionada no ha pagado el impuesto del año " + (this.anioSolicitudPermiso - 1) + " para poder emitir el permiso del año " + this.anioSolicitudPermiso);
//                    return;
//                }
//            } else {
//                JsfUti.messageError(null, "Info", " Debe seleccionar el año de solucitid del permiso.");
//                return;
//            }
//            this.localSel = local;
//            this.liquidacion.setTipoLiquidacion(this.tipoLiquidacionPermiso);
//            this.liquidacion.setLocalComercial(local);
//            //this.generarValorLiquidacion();0
//
//        } catch (Exception e) {
//            LOG.log(Level.OFF, "Seleccionar Local", e);
//        }
//    }
//
//    public void consultarRubros() {
//        try {
//            rubrosLiquidacion = new ArrayList<>();
//            detalle = new ArrayList();
//            if (tipoLiquidacionPermiso != null) {
//                rubrosLiquidacion = services.getRubrosPorLiquidacion(tipoLiquidacionPermiso.getId());
//                initLiquidacion();
//                detalle.add(new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "VALOR DE PATENTE"));
//                for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
//                    detalle.add(new FinaRenDetLiquidacion(temp.getValor(), temp.getId(), temp.getDescripcion()));
//                }
//
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Cargar Rubros", e);
//        }
//        JsfUti.update("frmAlcPlus");
//    }
//
//    public void selectLocal(RenLocalComercial local) {
//        try {
//            if (localesEnte == null) {
//                localesEnte = new ArrayList<>();
//            }
//            if (!localesEnte.contains(local)) {
//                localesEnte.add(local);
//            } else {
//                JsfUti.messageInfo(null, "Info", "No se puede agregar dos veces el mismo local comercial");
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
//    }
//
//    public void generarValorLiquidacion() {
//        try {
//            RenFactorPorMetro factor1;
//            RenFactorPorCapital factor2;
//            BigDecimal valorLiq;
//
//            if (balance.getCapital() != null && balance.getCapital().compareTo(BigDecimal.ZERO) > 0) {
//                if (localSel != null) {
//                    factor2 = (RenFactorPorCapital) servs.find(QuerysFinanciero.getRangoPatente, new String[]{"capital"}, new Object[]{balance.getCapital()});
//                    if (factor2.isAplica_porcentaje()) {
//                        valor = balance.getCapital().multiply(factor2.getValor()).divide(new BigDecimal("100"));
//                    } else {
//                        valor = factor2.getValor();
//                    }
//
//                    switch (tipoExoneracion) {
//                        case 1: {
//                            valor = BigDecimal.ZERO;
//                            break;
//                        }
//                        case 2: {
//                            valor = valor.divide(new BigDecimal("3"));
//                            break;
//                        }
//                        case 3: {
//                            valor = valor.divide(new BigDecimal("2"));
//                            break;
//                        }
//                    }
//
//                    if (valor.compareTo(new BigDecimal("25000")) > 0) {
//                        valor = new BigDecimal("25000");
//                    }
//
//                    detalle.get(0).setValor(valor);
//
//                    detalle.get(1).setValor(tasaPermiso);
//                    liquidacion.setTotalPago(tasaPermiso);
//                }
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
//    }
//
//    public void buscarEnte() {
//        if (cedulaRuc == null || cedulaRuc.trim().length() == 0) {
//            JsfUti.messageError(null, "Advertencia", "Debe Ingresar el Numero de Cedula o RUC");
//            return;
//        }
//        mostrarRequisitos = false;
//        propietario = (CatEnte) servs.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{cedulaRuc});
//        if (propietario != null) {
//            mostrarRequisitos = true;
//            localesEnte = propietario.getLocalesComercialesCollection();
//            esPersonaProp = propietario.getEsPersona();
//        } else {
//            mostrarRequisitos = false;
//            propietario = null;
//            tipoLiquidacion = null;
//            persona = new CatEnte();
//            persona.setEsPersona(cedulaRuc.length() == 10);
//            persona.setCiRuc(cedulaRuc);
//            JsfUti.messageInfo(null, Messages.enteNoExiste, "");
//            JsfUti.update("formSolicitante");
//            JsfUti.executeJS("PF('dlgSolInf').show();");
//        }
//
//    }
//
//    public void guardarSolicitante() {
//        Boolean flag;
//        if (persona.getId() == null) {
//            flag = guardarCliente();
//        } else {
//            flag = editarCliente();
//        }
//        if (flag) {
//            propietario = persona;
//            JsfUti.executeJS("PF('dlgSolInf').hide();");
//            JsfUti.messageInfo(null, "Info", "Se creó el usuario correctamente.");
//        } else {
//            JsfUti.messageError(null, "Error", "No se pudo guardar los datos correctamente. Modifique los datos e intente de nuevo.");
//        }
//    }
//
//    public void cancelarGuardado() {
//        emailNew = "";
//        tlfnNew = "";
//        persona = new CatEnte();
//    }
//
//    public void initLiquidacion() {
//        liquidacion = new FinaRenLiquidacion();
//        liquidacion.setTotalPago(new BigDecimal(0));
//        liquidacion.setFechaContratoAnt(new Date());
//        liquidacion.setCostoAdq(BigDecimal.ZERO);
//        liquidacion.setCuantia(BigDecimal.ZERO);
//        liquidacion.setAnio(Utils.getAnio(new Date()));
//        localSel = null;
//    }
//
//    public void buscarLocal() {
//        Map<String, Object> options = new HashMap<>();
//        options.put("resizable", false);
//        options.put("draggable", false);
//        options.put("modal", true);
//        options.put("width", "95%");
//        options.put("height", "85%");
//        options.put("closable", true);
//        options.put("contentWidth", "100%");
//        JsfUti.openDialog("/resources/dialog/localesComerciales", options, null);
//    }
//
//    public void seleccionarLocal(SelectEvent event) {
//        try {
//            selectLocal((RenLocalComercial) event.getObject());
//            seleccionar((RenLocalComercial) event.getObject());
//            esPersonaProp = propietario.getEsPersona();
//            if (tipoLiquidacion.getCodigoTituloReporte() == 98) {
////                if (!this.localSel.getTurismo()) {
////                    JsfUti.messageError(null, "Advertencia", "Local comercial seleccionado no tiene categoria de Turismo");
////                    return;
////                }
//            }
//            JsfUti.update("frmAlcPlus");
//        } catch (Exception e) {
//            LOG.log(Level.OFF, "Seleccionar Local", e);
//        }
//    }
//
//    public void procesar() {
//        try {
//            if (localSel == null) {
//                JsfUti.messageError(null, MessagesRentas.error, MessagesRentas.faltaIngresar.concat("local comercial"));
//                return;
//            }
//            if (localSel.getId() == null) {
//                JsfUti.messageError(null, MessagesRentas.error, MessagesRentas.faltaIngresar.concat("local comercial"));
//                return;
//            }
//
//            Object numLiquidacion = null;
//            liquidacion.setComprador(patente.getPropietario());
//            liquidacion.setFechaIngreso(new Date());
//            liquidacion.setUsuarioIngreso(session.getName_user());
//            if (liquidacion.getEstadoLiquidacion() == null) {
//                liquidacion.setEstadoLiquidacion(services.getEstadoLiquidacionByDesc(2L));
//            }
//
//            liquidacion.setTipoLiquidacion(tipoLiquidacionPermiso);
//            liquidacion.setLocalComercial(localSel);
//            liquidacion.setPatente(patente);
//
//            detalle.remove(0);
//            liquidacion.setSaldo(liquidacion.getTotalPago());
//            liquidacion.setValidada(Boolean.FALSE);
//            balance.setPatente(patente);
//            balance.setLocalComercial(localSel);
//            balance.setAnioBalance(anioSolicitudPermiso - 1);
//            liquidacion.setAnio(anioSolicitudPermiso - 1);
//            liquidacion = services.guardarLiquidacionPatente(liquidacion, detalle, tipoLiquidacionPermiso, balance);
//            if (liquidacion != null) {
//                JsfUtil.executeJS("PF('obs').hide()");
//                JsfUtil.executeJS("PF('dlgIdLiquidacion').show()");
//                JsfUtil.update("numLiq:dlgIdLiquidacion");
//                imprimir();
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//        }
//    }

    /*MODIFICADO POR JC*/
    private void imprimir() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.instanciarParametros();
//        ss.setTieneDatasource(Boolean.TRUE);
        ss.addParametro("LOGO", path.concat(SisVars.logoReportes));
        ss.addParametro("LIQUIDACION", liquidacion.getId());
        ss.addParametro("ID_LOCAL_COMERCIAL", localSel.getId());
        ss.addParametro("NUM_TRAMITE", liquidacion.getNumReporte());
        ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
        ss.setNombreReporte("sComprobantePermisoFuncionamiento");
        ss.addParametro("FirmaRentas", path + SisVars.sisFirmaRentas);
        ss.addParametro("FirmaTesorero", path + SisVars.sisFirmaTesorero);

        JsfUtil.redirectNewTab("/Documento");

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
        if (null == existeLiquidacion) {
            JsfUtil.addErrorMessage("Liquidación", "Liquidación de " + tipoLiquidacion.getNombreTitulo() + "para el local seleccionado, ya fue pagado.");
        } else {
            switch (existeLiquidacion) {
                case 3:
                    JsfUtil.executeJS("PF('obs').show()");
                    break;
                case 2:
                    JsfUtil.addErrorMessage("Liquidación", "Ya existe una Liquidación activa de " + tipoLiquidacion.getNombreTitulo() + "para el local seleccionado");
                    break;
                default:
                    JsfUtil.addErrorMessage("Liquidación", "Liquidación de " + tipoLiquidacion.getNombreTitulo() + "para el local seleccionado, ya fue pagado.");
                    break;
            }
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
            this.cedulaRuc = propietario.getIdentificacionCompleta();
            mostrarRequisitos = true;
            localesEnte = propietario.getLocalesComercialesCollection();
            esPersonaProp = "PER_NA".equals(propietario.getTipoProv().getCodigo());
        }

    }

}
