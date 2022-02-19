/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.RubrosPorTipoLiq;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ExoneracionesMB implements Serializable {
    
    @Inject
    private UserSession session;
    @Inject
    private ManagerService services;
    @Inject
    private ServletSession ss;
    private LazyModel<FnExoneracionLiquidacion> exoneraciones;
    private FnExoneracionClase exoneracion;
//    private List<CatCategoriasPredio> categoriasList, tempList;
    private FnExoneracionLiquidacion exoConsulta;
    private String mensajeExoneracion;
    private Boolean usuarioAutorizado;
    private List<RubrosPorTipoLiq> detList1, detList2;
    private FinaRenLiquidacion original, posterior;
    private List<FinaRenDetLiquidacion> detLiq;
    private Map<String, Object> parametros;
    private FnExoneracionTipo tipoExoneracion;
    
    @PostConstruct
    public void initView() {
        try {
            if (session.esLogueado()) {
//                CatCategoriasPredio temp;
                exoneraciones = new LazyModel<>(FnExoneracionLiquidacion.class);
                exoneraciones.getSorteds().put("id", "desc");
                exoneraciones.addFilter("estado", true);
//                exoneraciones.addFilter("liquidacionPosterior.estadoLiquidacion:notEqual", new FinaRenEstadoLiquidacion(4L));
                exoneracion = new FnExoneracionClase();
//                categoriasList = new ArrayList();
//                List<CatCategoriasPredio> tempList = (List<CatCategoriasPredio>) services.findAll(QuerysFinanciero.getCatCategoriasPredio, new String[]{"estado"}, new Object[]{true});

//                for (CatCategoriasPredio t : tempList) {
//                    temp = (CatCategoriasPredio) EntityBeanCopy.clone(services.find(CatCategoriasPredio.class, t.getId()));
//                    categoriasList.add(temp);
//                }
                usuarioAutorizado = session.getEsDirector();
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void anularExoneracion(FnExoneracionLiquidacion exo) {
        try {
            exoConsulta = exo;
            exoConsulta.setEstado(Boolean.FALSE);
            exoConsulta.getLiquidacionOriginal().setEstaExonerado(Boolean.FALSE);
            exoConsulta.getLiquidacionOriginal().setValorExoneracion(BigDecimal.ZERO);
            exoConsulta.getLiquidacionOriginal().setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            exoConsulta.getLiquidacionPosterior().setEstadoLiquidacion(new FinaRenEstadoLiquidacion(5L));
            services.save(exoConsulta.getLiquidacionOriginal());
            services.save(exoConsulta.getLiquidacionPosterior());
            services.save(exoConsulta);
            JsfUtil.addInformationMessage("Info", "Exoneracion anulada satisfactoriamente");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addInformationMessage("Info", "Hubo un error al anular la exoneracion");
        }
    }
    
    public void verDetalles(FnExoneracionLiquidacion exo) {
        exoConsulta = exo;
        mensajeExoneracion = "\t-Tiene una exoneración de: " + exo.getExoneracion().getExoneracionTipo().getDescripcion().toUpperCase()
                + "\nNúmero de resolución: " + exo.getExoneracion().getNumResolucionSac();
        detList1 = new ArrayList<>();
        detList2 = new ArrayList<>();
        FinaRenRubrosLiquidacion rubro;
        original = this.exoConsulta.getLiquidacionOriginal();
        parametros = new HashMap<>();
        parametros.put("liquidacion", original);
        detLiq = services.findAllQuery("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC", parametros);
        for (FinaRenDetLiquidacion temp : detLiq) {
            rubro = (FinaRenRubrosLiquidacion) services.find(FinaRenRubrosLiquidacion.class, temp.getRubro().getId());
            detList1.add(new RubrosPorTipoLiq(rubro, temp.getValor()));
        }
        posterior = this.exoConsulta.getLiquidacionPosterior();
        posterior.setTotalPago(posterior.getTotalPago().setScale(2, RoundingMode.HALF_UP));
        posterior.setUsuarioIngreso(session.getNameUser());
        services.save(posterior);
        parametros = new HashMap<>();
        parametros.put("liquidacion", posterior);
        detLiq = services.findAllQuery("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC", parametros);
        for (FinaRenDetLiquidacion temp2 : detLiq) {
            rubro = (FinaRenRubrosLiquidacion) services.find(FinaRenRubrosLiquidacion.class, temp2.getRubro().getId());
            detList2.add(new RubrosPorTipoLiq(rubro, temp2.getValor()));
        }
    }
    
    public void imprimirExoClase3y6(FnExoneracionLiquidacion exo) {
        FnSolicitudExoneracion exoneracion = exo.getExoneracion();
        List<FnExoneracionLiquidacion> exoneraciones = null;
        List parametros;
        FinaRenLiquidacion liqPost;
        BigDecimal posterior = exo.getLiquidacionPosterior().getTotalPago(), original = exo.getLiquidacionOriginal().getTotalPago();
        try {
            ss.instanciarParametros();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.addParametro("LOGO", path + SisVars.sisLogo);
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/recaudaciones/").concat("/"));
            ss.addParametro("FECHA", exo.getFechaIngreso());
            ss.addParametro("IMP_ORIG", original.setScale(2, BigDecimal.ROUND_HALF_UP));
            ss.addParametro("DIFERENCIA", original.subtract(posterior).setScale(2, BigDecimal.ROUND_HALF_UP));
            ss.addParametro("IMP_NEW", posterior.setScale(2, BigDecimal.ROUND_HALF_UP));
            ss.addParametro("ID_SOLICITUD", exo.getId());
            ss.setNombreSubCarpeta("recaudaciones");
            ss.setNombreReporte("formulario_exoneracion_master");
            JsfUtil.redirectNewTab(SisVars.urlbase + "Documento");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void nuevaExoneracion() {
        this.exoneracion = new FnExoneracionClase();
        JsfUtil.executeJS("PF('dlgExon').show()");
    }
    
    public void nuevoTipoExon() {
        tipoExoneracion = new FnExoneracionTipo();
        JsfUtil.executeJS("PF('dlgTipExon').show()");
    }
    
    public void editarExoneracion() {
        if (exoneracion != null) {
            if (exoneracion.getDescripcion() == null || exoneracion.getDescripcion().trim().length() <= 0) {
                JsfUtil.addErrorMessage(MessagesRentas.advert, "No ha ingresado el la descripción de la exoneración");
                return;
            }
            if (services.guadModExoneracion(exoneracion) != null) {
                JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.guardadoCorrecto);
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.advert, MessagesRentas.ErrorGuardar);
            }
        }
    }
    
    public void guardarExoneracion() {
        if (exoneracion != null) {
            if (exoneracion.getDescripcion() == null || exoneracion.getDescripcion().trim().length() <= 0) {
                JsfUtil.addErrorMessage(MessagesRentas.advert, "No ha ingresado el la descripción de la exoneración");
                return;
            }
            exoneracion.setUsuarioCreacion(session.getNameUser());
            exoneracion.setFechaIngreso(new Date());
            exoneracion.setDescripcion((exoneracion.getDescripcion().toUpperCase()));
            exoneracion = services.guadModExoneracion(exoneracion);
            if (exoneracion != null) {
                JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.guardadoCorrecto);
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.advert, MessagesRentas.ErrorGuardar);
            }
        }
        exoneraciones = new LazyModel<>(FnExoneracionLiquidacion.class, "id", "DESC");
        JsfUtil.executeJS("PF('dlgExon').hide()");
    }
    
    public void agregarTipo() {
        
        if (tipoExoneracion.getDescripcion() == null || tipoExoneracion.getDescripcion().trim().length() <= 0) {
            JsfUtil.addErrorMessage(MessagesRentas.advert, "No ha ingresado el la descripción del tipo exoneración");
            return;
        }
        tipoExoneracion.setDescripcion((tipoExoneracion.getDescripcion().toUpperCase()));
        tipoExoneracion.setReglamento((tipoExoneracion.getReglamento().toUpperCase()));
        if (tipoExoneracion.getId() == null) {
            tipoExoneracion.setEstado(Boolean.TRUE);
            tipoExoneracion.setFechaIngreso(new Date());
            tipoExoneracion.setUsuarioCreacion(session.getNameUser());
            if (exoneracion.getFnExoneracionTipoList() == null) {
                exoneracion.setFnExoneracionTipoList(new ArrayList<FnExoneracionTipo>());
            }
            exoneracion.getFnExoneracionTipoList().add(tipoExoneracion);
        } else {
            
        }
        JsfUtil.executeJS("PF('dlgTipExon').hide()");
    }
    
    public void continuar() {
        JsfUtil.redirectFaces("/vistaprocesos/dashBoard.xhtml");
    }
    
    public UserSession getSession() {
        return session;
    }
    
    public void setSession(UserSession session) {
        this.session = session;
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
    
    public LazyModel<FnExoneracionLiquidacion> getExoneraciones() {
        return exoneraciones;
    }
    
    public void setExoneraciones(LazyModel<FnExoneracionLiquidacion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }
    
    public FnExoneracionClase getExoneracion() {
        return exoneracion;
    }
    
    public void setExoneracion(FnExoneracionClase exoneracion) {
        this.exoneracion = exoneracion;
    }
    
    public FnExoneracionLiquidacion getExoConsulta() {
        return exoConsulta;
    }
    
    public void setExoConsulta(FnExoneracionLiquidacion exoConsulta) {
        this.exoConsulta = exoConsulta;
    }
    
    public String getMensajeExoneracion() {
        return mensajeExoneracion;
    }
    
    public void setMensajeExoneracion(String mensajeExoneracion) {
        this.mensajeExoneracion = mensajeExoneracion;
    }
    
    public Boolean getUsuarioAutorizado() {
        return usuarioAutorizado;
    }
    
    public void setUsuarioAutorizado(Boolean usuarioAutorizado) {
        this.usuarioAutorizado = usuarioAutorizado;
    }
    
    public List<RubrosPorTipoLiq> getDetList1() {
        return detList1;
    }
    
    public void setDetList1(List<RubrosPorTipoLiq> detList1) {
        this.detList1 = detList1;
    }
    
    public List<RubrosPorTipoLiq> getDetList2() {
        return detList2;
    }
    
    public void setDetList2(List<RubrosPorTipoLiq> detList2) {
        this.detList2 = detList2;
    }
    
    public FinaRenLiquidacion getOriginal() {
        return original;
    }
    
    public void setOriginal(FinaRenLiquidacion original) {
        this.original = original;
    }
    
    public FinaRenLiquidacion getPosterior() {
        return posterior;
    }
    
    public void setPosterior(FinaRenLiquidacion posterior) {
        this.posterior = posterior;
    }
    
    public List<FinaRenDetLiquidacion> getDetLiq() {
        return detLiq;
    }
    
    public void setDetLiq(List<FinaRenDetLiquidacion> detLiq) {
        this.detLiq = detLiq;
    }
    
    public Map<String, Object> getParametros() {
        return parametros;
    }
    
    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }
    
    public FnExoneracionTipo getTipoExoneracion() {
        return tipoExoneracion;
    }
    
    public void setTipoExoneracion(FnExoneracionTipo tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }
    
}
