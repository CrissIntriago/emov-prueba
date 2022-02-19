/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.models.RubrosPorTipoLiq;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "titulosRentasGenerado")
@ViewScoped
public class TitulosRentasGeneradoMB extends ReportGenealModel implements Serializable {
    
    @Inject
    private ManagerService services;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private InteresesService interesesService;
    
    private List<FinaRenLiquidacion> liquidacionseleccionadas;
    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;
    private FinaRenLiquidacion original, posterior, liquidacion;
    private List<FnExoneracionLiquidacion> exoneraciones;
    private List<RubrosPorTipoLiq> detList1, detList2;
    private FnExoneracionLiquidacion exoneracion;
    private List<FinaRenDetLiquidacion> detLiq;
    private String observacion;
    // private GeDocumentos documento;
    //protected List<GeDocumentos> geDocumentosList;
    protected LazyModel<Cliente> solicitantes;
    private Cliente comprador, vendedor;
    
    @PostConstruct
    public void initView() {
        //solicitantes = new LazyModel(Cliente.class);
        liquidacionesLazy = new LazyModel(FinaRenLiquidacion.class);
        liquidacionesLazy.getSorteds().put("fechaIngreso", "DESC");
        
    }
    
    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        
        if (l.getEstadoLiquidacion() != null && l.getEstadoLiquidacion().getId().equals(2L)) {
            
            Map<String, BigDecimal> interesMap = new HashMap<>();
            interesMap = services.valoresInteres(l, new Date());
            interes = interesMap.get("interesCalculado");
            if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
                l.setDescuento(interesMap.get("descuento"));
                l.setRecargo(interesMap.get("recargo"));
            }
            
        }
        
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }
        
        l.setInteres(interes);
        l.setPagoFinal(l.getTotalPago().add(l.getRecargo()).add(l.getInteres()));
        
        return l.getInteres();
    }
    
    public void generarComprobante(FinaRenLiquidacion liquidacion) {
        
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.setNombreSubCarpeta("Emision");
//       ss.addParametro("LOGO", path + SisVars.logoReportes);
//       ss.addParametro("SUBREPORT_DIR", JsfUti.getRealPath("/reportes/").concat("/Emision/"));
        ss.setNombreReporte("emisionPredioUrbanoAnulado");
        // ss.setTieneDatasource(Boolean.TRUE);
        //ss.setTieneDatasource(Boolean.TRUE);
        ss.addParametro("ID_LIQUIDACION", liquidacion.getId());
        JsfUtil.redirectNewTab("/Documento");
        
    }
    
    public void calculosAdicionales() {
        if (this.liquidacion != null) {
            if (this.liquidacion.getTipoLiquidacion().getId() == 13L) {
                try {
                    this.liquidacion = services.realizarDescuentoRecargaInteresPredial(this.liquidacion, null);
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
            //PARA LOSS PERMISOS AMBIENTALES
            if (this.liquidacion.getTipoLiquidacion().getTransaccionPadre() == 16L) {
                try {
                    Calendar fechaInteres = Calendar.getInstance();
                    fechaInteres.set(this.liquidacion.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
                    this.liquidacion.setInteres(services.generarInteres(this.liquidacion.getSaldo(), fechaInteres.getTime(), null));
                    if (this.liquidacion.getInteres() == null) {
                        this.liquidacion.setInteres(BigDecimal.ZERO);
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
            List<RenParametrosInteresMulta> parametrosInteresMultas = services.getListParametrosInteresMulta(liquidacion);
            if (parametrosInteresMultas != null && !parametrosInteresMultas.isEmpty()) {//VERIFICAR SI EMITE MULTA-INTERES
                for (RenParametrosInteresMulta interesMulta : parametrosInteresMultas) {
                    if (interesMulta.getTipo().equalsIgnoreCase("I")) {
                        try {
                            Calendar fecha = Calendar.getInstance();
                            fecha.set(Calendar.DAY_OF_MONTH, interesMulta.getDia());
                            fecha.set(Calendar.MONTH, interesMulta.getMes() - 1);
                            fecha.set(Calendar.YEAR, liquidacion.getAnio() + 1);
                            liquidacion.setInteres(services.generarInteres(liquidacion.getSaldo(), fecha.getTime(), null));
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }
                    }
//                    if (interesMulta.getTipo().equalsIgnoreCase("M")) {
//                        liquidacion.setRecargo(recaudacion.generarMultas(liquidacion, interesMulta));
//                    }
                }
            }
            this.liquidacion.calcularPago();
        }
    }
    
    public void handleFileDocumentBySave(FileUploadEvent event) {
//        try {
////            Long documentoId = fserv.uploadFile(FilesUtil.copyFileServer1(event), event.getFile().getFileName(), event.getFile().getContentType());
////            documentoBean.setFechaCreacion(new Date());
////            documentoBean.setNombre(event.getFile().getFileName());
////            documentoBean.setContentType(event.getFile().getContentType());
////            documentoBean.setDocumentoId(documentoId);
////            documentoBean.setIdentificacion("Baja de Titulos Prediales");
////            for (RenLiquidacion r : liquidacionseleccionadas) {
////                documentoBean.setLiquidacion(r.getId());
////                this.setDocumento(documentoBean.saveDocumentoLiquidacionAnulada());
////                geDocumentosList.add(this.getDocumento());
////            }
//
//            JsfUtil.addInformationMessage( "Nota1", "Archivo cargado Satisfactoriamente");
//        } catch (IOException e) {
//            System.err.println(e);
//        }
    }
    
    public void descargarDocumento(FinaRenLiquidacion r) {
//        GeDocumentos doc = (GeDocumentos) manager.find(Querys.getGeDocumentosIdLiquidacion, new String[]{"liquidacion"}, new Object[]{r.getId()});
//        if (doc != null) {
//            try {
//                String url = doc.getFileOid().toString();
//                if (url != null && url.trim().length() > 0) {
//                    JsfUti.redirectNewTab(SisVars.urlbase + "DescargarDocsRepositorio?idDoc=" + url + "&type=pdf");
//                } else {
//                    JsfUti.messageWarning(null, "No Existen Documentos para el Predio Seleccionado", "");
//                }
//            } catch (Exception e) {
//                Logger.getLogger(EspaciosPublicos.class.getName()).log(Level.SEVERE, null, e);
//            }
//        } else {
//            JsfUti.messageError(null, "Nota1", "No existen Archivoss Asociados");
//        }

    }

//    public void inactivarDocumento(GeDocumentos documentos) {
//        try {
////            documentos = catas.saveInactivarDocumentos(documentos);
////            if (!documentos.getEstado()) {
////                JsfUti.messageInfo(null, "Exito!", "Datos Registrados Correctamente");
////                geDocumentosList.remove(documentos);
////            } else {
////                JsfUti.messageError(null, "", "Error al Eliminar los Archivos");
////            }
//        } catch (Exception e) {
//            Logger.getLogger(EspaciosPublicos.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
    public void actualizarComprador() {
        if (liquidacion == null) {
            JsfUtil.addErrorMessage("", "Error debe selecionar una liquidacion");
        } else {
            if (comprador == null) {
                JsfUtil.addErrorMessage("", "Error debe selecionar un comprador");
            } else {
                liquidacion.setComprador(comprador);
                services.update(liquidacion);
                for (FinaRenPago p : liquidacion.getRenPagoCollection()) {
                    p.setContribuyente(comprador);
                }
                JsfUtil.executeJS("PF('dlgNombCont').hide();");
                JsfUtil.update("mainForm");
            }
        }
        
    }
    
    public void actualizarLiquidacion() {
        services.update(liquidacion);
        JsfUtil.executeJS("PF('dlgObservacion').hide();");
        JsfUtil.update("mainForm");
    }
    
    public void actualizarVendedor() {
        if (liquidacion == null) {
            JsfUtil.addErrorMessage("", "Error debe selecionar una liquidacion");
        } else {
            if (vendedor == null) {
                JsfUtil.addErrorMessage("", "Error debe selecionar un comprador");
            } else {
                liquidacion.setVendedor(vendedor);
                services.update(liquidacion);
                JsfUtil.executeJS("PF('dlgNombVendedor').hide();");
                JsfUtil.update("mainForm");
            }
        }
        
    }
    
    public ManagerService getServices() {
        return services;
    }
    
    public void setServices(ManagerService services) {
        this.services = services;
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
    
    public List<FinaRenLiquidacion> getLiquidacionseleccionadas() {
        return liquidacionseleccionadas;
    }
    
    public void setLiquidacionseleccionadas(List<FinaRenLiquidacion> liquidacionseleccionadas) {
        this.liquidacionseleccionadas = liquidacionseleccionadas;
    }
    
    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }
    
    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
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
    
    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }
    
    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
    
    public List<FnExoneracionLiquidacion> getExoneraciones() {
        return exoneraciones;
    }
    
    public void setExoneraciones(List<FnExoneracionLiquidacion> exoneraciones) {
        this.exoneraciones = exoneraciones;
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
    
    public FnExoneracionLiquidacion getExoneracion() {
        return exoneracion;
    }
    
    public void setExoneracion(FnExoneracionLiquidacion exoneracion) {
        this.exoneracion = exoneracion;
    }
    
    public List<FinaRenDetLiquidacion> getDetLiq() {
        return detLiq;
    }
    
    public void setDetLiq(List<FinaRenDetLiquidacion> detLiq) {
        this.detLiq = detLiq;
    }
    
    public String getObservacion() {
        return observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }
    
    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }
    
    public Cliente getComprador() {
        return comprador;
    }
    
    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }
    
    public Cliente getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }
    
}
