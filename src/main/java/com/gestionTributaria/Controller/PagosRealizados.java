/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.NombreContribuyenteModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class PagosRealizados implements Serializable {

    private LazyModel<FinaRenPago> pagos;
    private FinaRenPago pago;
    private Usuarios usuario;
    @Inject
    private ManagerService manager;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;

    private Map<String, Object> parametros;

    /*SOLO JC*/
 /*VARIABLES ASIGNADAS PARA LA EDICION DEL CONTRIBIYENTE*/
    private List<NombreContribuyenteModel> modelNombresList;
    private String nombreContribuyente;

    private String observacion;

    private Boolean renderAnular;
//
//    @Inject
//    private UploadDocumento documentoBean;
//    @Inject
//    private OmegaUploader fserv;
    private Long idDoc = null;

    private Date desde;
    private Date hasta;

    @PostConstruct
    public void initView() {
        try {
            pagos = new LazyModel<>(FinaRenPago.class, "fechaPago", "DESC");
            parametros = new HashMap<>();
            parametros.put("usuario", session.getNameUser());
            usuario = (Usuarios) manager.findByParameter(Usuarios.class, parametros);
            if (usuario != null) {
                Rol rol = new Rol();
                //rol.setId(RolesEspeciales.ADMINISTRADOR);
                Rol supCaj = new Rol();
                //supCaj.setId(RolesEspeciales.SUPERVISOR_CAJA);
                Rol asistenteFinanciero = new Rol();
                //asistenteFinanciero.setId(RolesEspeciales.ASISTENTE_FINANCIERO);
                pagos = new LazyModel<>(FinaRenPago.class, "fechaPago", "DESC");
                if (usuario.getUsuariosRolesList().contains(rol) || usuario.getUsuariosRolesList().contains(supCaj) || usuario.getUsuariosRolesList().contains(asistenteFinanciero)) {//JEFA DE CAJA  - ASISTENTE ADMINISTRATIVO
                    pagos = new LazyModel<>(FinaRenPago.class, "fechaPago", "DESC");
                } else {
                    pagos = new LazyModel<>(FinaRenPago.class, "fechaPago", "DESC");
                    pagos.getFilterss().put("cajero", this.usuario);
                    pagos.addSorted("fechaPago", "DESC");
                }
            }
            desde = new Date();
            hasta = new Date();
        } catch (Exception e) {
            Logger.getLogger(PagosRealizados.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void realizarConsulta() {
        try {
            if (this.desde != null && hasta != null) {
                pagos = new LazyModel<>(FinaRenPago.class, "fechaPago", "DESC");
                pagos.getFilterss().put("fechaPago:gte", desde);
                pagos.getFilterss().put("fechaPago:lte", hasta);
            }
        } catch (Exception e) {
            Logger.getLogger(PagosRealizados.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void verPago(FinaRenPago p) {
        this.pago = p;
        this.setNombreContribuyente();
    }

    /*OPTENGO EL NOMBRE DEL CONTRIBUYENTE PARA SU EDISION*/
    public void setNombreContribuyente() {
        String[] nombres;
        Integer cont = 0;
        NombreContribuyenteModel model = new NombreContribuyenteModel();
        modelNombresList = new ArrayList();
        if (pago.getContribuyente() != null) {
            nombreContribuyente = pago.getContribuyente().getNombreCompleto();
        } else {
            nombreContribuyente = pago.getNombreContribuyente();
        }
        if (pago.getLiquidacion().getNombreCompradorHistoric() != null) {
            nombres = pago.getLiquidacion().getNombreCompradorHistoric().split(";");

            for (String temp : nombres) {
                if (cont % 3 == 0) {
                    model = new NombreContribuyenteModel();
                    model.setUsername(temp);
                }
                if (cont % 3 == 1) {
                    model.setFecha(temp);
                }
                if (cont % 3 == 2) {
                    model.setNombre(temp);
                    modelNombresList.add(model);
                }
                cont++;
            }
        }
    }

    /*ME PERMITE LA ACTUALIZACION DEL CONTRIBUYENTE */
    public void actualizarNombreContribuyente() {
        try {
            String nom = pago.getContribuyente() == null ? pago.getNombreContribuyente() == null ? "" : pago.getNombreContribuyente().toUpperCase() : pago.getContribuyente().getNombreCompleto().toUpperCase();
            if (nombreContribuyente != null) {
                pago.setContribuyente(null);
                pago.setNombreContribuyente(nombreContribuyente);
                pago = (FinaRenPago) manager.save(pago);
                if (pago.getLiquidacion().getNombreCompradorHistoric() == null) {
                    pago.getLiquidacion().setNombreCompradorHistoric(session.getNameUser() + ";" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + ";" + nom + ";");
                } else {
                    pago.getLiquidacion().setNombreCompradorHistoric(pago.getLiquidacion().getNombreCompradorHistoric() + session.getNameUser() + ";" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + ";" + nombreContribuyente.toUpperCase() + ";");
                }
                pago.getLiquidacion().setComprador(null);
                pago.getLiquidacion().setNombreComprador(nombreContribuyente);
                manager.save(pago.getLiquidacion());

            } else {
                JsfUtil.addInformationMessage("Info", "No ha ingresado el nombre del contribuyente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reversarPago() {
        //SOLO SE PUEDE REVERSAR EL ULTIMO PAGO
        try {
            if (this.pago != null && this.pago.getEstado()) {
                if (this.pago.equals(manager.ultimoPago(this.pago.getLiquidacion())) && manager.ultimaEspecie(this.pago.getLiquidacion())) {
                    if (this.pago.getObservacion() != null) {
                        this.pago.setObservacion(this.pago.getObservacion() + "Pago Anulado: " + observacion);
                    } else {
                        this.pago.setObservacion("Pago Anulado: " + observacion);
                    }
                    this.pago.setIpUserSession(this.pago.getIpUserSession() + " ANULA: " + session.getIpClient());
                    this.pago.setMacAddresUsuarioIngreso(this.pago.getMacAddresUsuarioIngreso() + "ANULA: " + session.getMACAddressEquipo());
                    this.pago.setUsuarioAnular(session.getNameUser());
                    this.pago = manager.reversarPago(this.pago);
                    if (this.pago != null) {
                        FinaRenLiquidacion liquidacion = manager.find(FinaRenLiquidacion.class, this.pago.getLiquidacion().getId());
                        Boolean sinAbonos = Boolean.TRUE;
                        for (FinaRenPago p : liquidacion.getRenPagoCollection()) {
                            if (p.getEstado()) {
                                sinAbonos = Boolean.FALSE;
                                break;
                            }
                        }
                        //INACTIVACION DE RENLIQUIDACION POR TIPO
                        Long tipoLiquidacion = liquidacion.getId();
                        if (sinAbonos && tipoLiquidacion != 7L && tipoLiquidacion != 13L) {
                            System.out.println("::: INACTIVAR");
                        }
                        JsfUtil.addInformationMessage("Mensaje", "PAGO ANULADO EXITOSAMENTE");
                    } else {
                        JsfUtil.addErrorMessage("PROBLEMAS AL ANULAR EL PAGO", "VUELVA A REALIZAR LA ACCION");
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "SOLO SE PUEDE ANULAR EL ULTIMO PAGO PROCESADO");
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "EL PAGO YA SE ENCUENTRA ANULADO");
            }
        } catch (Exception e) {
            Logger.getLogger(PagosRealizados.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void imprimirComprobante(FinaRenPago p) {
        this.pago = p;
        this.generarComprobante(true);
    }

    /*METODO MODIFICADO PARA QUE SE PUEDA VIZUALIZAR EL PARAMETRO DEL LOGO EN EL REPORTE EL PARAMETRO DE COPIA CETIFICADA*/
    public void generarComprobante(Boolean copia) {
        List<FinaRenPago> pagoss = new ArrayList<>();
        try {
            if (this.pago != null && this.pago.getEstado()) {
                if (this.pago.getLiquidacion().getDepartamentoDetalleTitulos() == null) {
                    ss.borrarParametros();
                    ss.instanciarParametros();
                    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                    ss.addParametro("COPIA", copia);
                    ss.addParametro("LOGO", path + SisVars.sisLogo);
                    ss.addParametro("Tesorero", path + SisVars.sisFirmaTesorero);
                    ss.addParametro("Financiero", path + SisVars.sisFirmaFinanciero);
                    ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
                    ss.addParametro("SUBREPORT_DIR_ABONO", JsfUtil.getRealPath("/reportes/EmisionMontecristi"));
                    ss.addParametro("SUBREPORT_DIR_COACTIVA", JsfUtil.getRealPath("/reportes/EmisionMontecristi"));
                    //  ss.setTieneDatasource(Boolean.TRUE);
                    /*REPORTE DE PREDIOS URBANOS*/
                    ss.setNombreReporte("sEmisionPredioUrbanoGeneral");
                    ss.setNombreReporte(this.pago.getLiquidacion().getTipoLiquidacion().getNombreReporte());
                    if (this.pago.getLiquidacion().getTipoLiquidacion().getId() == 7L) {
                        ss.setNombreReporte("sEmisionPredioRuralGeneral");
                    }
                    /*REPORTE DE COBROS GENERALES*/
                    if (this.pago.getLiquidacion().getTipoLiquidacion().getNombreReporte() == null
                            || this.pago.getLiquidacion().getTipoLiquidacion().getNombreReporte().trim().length() == 0) {
                        ss.setNombreReporte("sComprobanteCobrosGenerales");
                    } else {
                        ss.setNombreReporte(this.pago.getLiquidacion().getTipoLiquidacion().getNombreReporte());
                    }
                    if (this.pago.getLiquidacion().getTipoLiquidacion().getPrefijo().equals("ALC")
                            && this.pago.getLiquidacion().getPredio() == null) {
                        ss.setNombreReporte("sComprobanteAlcabalaRural");
                        ss.setNombreSubCarpeta("RentasMontecristi/LiquidacionesRural/");
                    }

                    if (this.pago.getLiquidacion().getTipoLiquidacion().getPrefijo().equals("APA")) {
                        ss.borrarParametros();
                        ss.instanciarParametros();
                        // ss.setTieneDatasource(Boolean.TRUE);
                        ss.addParametro("COPIA", copia);
                        System.out.println("COPIA " + copia);
                        ss.addParametro("LOGO_MUNICIPIO", path + SisVars.sisLogo);
                        System.out.println("LOGO_MUNICIPIO " + path + SisVars.sisLogo);
                        ss.addParametro("LOGO_AGUA", path + SisVars.sisLogoAgua);
                        System.out.println("LOGO_MUNICIPIO " + path + SisVars.sisLogoAgua);
                        ss.addParametro("FirmaTesorero", path + SisVars.sisFirmaTesorero);
                        System.out.println("FirmaTesorero " + path + SisVars.sisFirmaTesorero);
//                        ss.addParametro("ID_CUENTA", this.pago.getLiquidacion().getCuenta().getId());
//                        System.out.println("ID_CUENTA " + this.pago.getLiquidacion().getCuenta().getId());
//                        ss.setNombreSubCarpeta("EmisioAguaMontecristi/");
//                        ss.setNombreReporte("sEmisionCobroAguaGeneral");
//                        System.out.println("ss" + ss);
                    }

                    pagoss.add(this.pago);
                    //  ss.setTieneDatasource(Boolean.TRUE);
                    ss.addParametro("liquidaciones", pagoss);
                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "Comprobantes");
                } else {
                    generarComprobante(this.pago.getLiquidacion());
                    System.out.println("PRUEBA" + this.pago.getLiquidacion());
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "SOLO SE PUEDE REIMPRIMIR PAGOS PROCESADOS");
            }

        } catch (Exception e) {
            Logger.getLogger(PagosRealizados.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobante(FinaRenLiquidacion liq) {
        List<FinaRenPago> pa = new ArrayList(liq.getRenPagoCollection());
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarParametros();
            ss.instanciarParametros();
            ss.addParametro("COPIA", Boolean.TRUE);
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            ss.addParametro("LOGO", path + SisVars.sisLogo);
            ss.addParametro("ID_LIQUIDACION", liq.getId());
            ss.addParametro("NUM_COMPROBANTE", liq.getNumComprobante());
            //  ss.setTieneDatasource(Boolean.TRUE);
            /*COMPROBANTES GENERALES*/
            ss.setNombreReporte("sComprobanteCobrosGenerales");
            ss.addParametro("liquidaciones", pa);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Comprobantes");
        } catch (Exception e) {
            //LOG.log(Level.OFF, null, e);
        }
    }

    public void handleFileDocumentBySave(FileUploadEvent event) {
        try {
            Date d = new Date();
            File file = new File(SisVars.rutaRepotiorioArchivo + d.getTime() + event.getFile().getFileName());

            InputStream is;
            is = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
//            Long documentoId = fserv.uploadFile(event.getFile().getInputstream(), event.getFile().getFileName(), event.getFile().getContentType());
//            documentoBean.setFechaCreacion(new Date());
//            documentoBean.setNombre(event.getFile().getFileName());
//            documentoBean.setRaiz(pago.getId());
//            documentoBean.setContentType(event.getFile().getContentType());
//            documentoBean.setDocumentoId(documentoId);
//            documentoBean.setIdentificacion("Anulacion Pago");
//            GeDocumentos saveDocumento = documentoBean.saveDocumento();
//            if (saveDocumento != null) {
//                this.idDoc = saveDocumento.getId();
//            }
            is.close();
            out.close();
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getRenderAnular() {
        session.getRoles();
        for (Long rol : session.getRoles()) {
//            if (rol.equals(RolesEspeciales.SUPERVISOR_CAJA)) {
//                renderAnular = true;
//            } else if (rol.equals(RolesEspeciales.ADMINISTRADOR)) {
//                renderAnular = true;
//            } else if (rol.equals(RolesEspeciales.ASISTENTE_FINANCIERO)) {
//                renderAnular = true;
//            }
        }
        return renderAnular;
    }

    public LazyModel<FinaRenPago> getPagos() {
        return pagos;
    }

    public void setPagos(LazyModel<FinaRenPago> pagos) {
        this.pagos = pagos;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public List<NombreContribuyenteModel> getModelNombresList() {
        return modelNombresList;
    }

    public void setModelNombresList(List<NombreContribuyenteModel> modelNombresList) {
        this.modelNombresList = modelNombresList;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Long idDoc) {
        this.idDoc = idDoc;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

}
