/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.ws.IrisService;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "firmarDocumentsView")
@ViewScoped
public class FirmarDocumentsController implements Serializable {

    @Inject
    private UserSession us;
    @Inject
    private IrisService irisService;
    @Inject
    private ServletSession ss;

    private UploadedFile uploadedFile;
    private boolean showOptionFirmar;
    private String clave;
    private Boolean existenImagenes;
    private List<Imagenes> imagenesPdfs;
    private FirmaElectronica firmaElectronica;

    @PostConstruct
    public void init() {
        if (us.getFirmaElectronica() != null) {
            showOptionFirmar = false;
            firmaElectronica = us.getFirmaElectronica();
            existenImagenes = Boolean.FALSE;
            imagenesPdfs = new ArrayList<>();
            firmaElectronica.setMotivo("Firmado Electrónicamente por FirmaEC");
            firmaElectronica.setUrlQr("");
        } else {
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            JsfUtil.addErrorMessage("ERROR", "AGREGUE FIRMA ELECTRONICA AL SISTEMA PARA PODER UTILIZAR ESTÁ OPCIÓN");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            uploadedFile = event.getFile();
            existenImagenes = Boolean.TRUE;
            File file = FilesUtil.copyFileServer1(event, SisVars.RUTA_FILES_TEMP);
            System.out.println("file " + file.getAbsolutePath());
            firmaElectronica.setArchivoFirmar(file.getAbsolutePath());
            JsfUtil.addSuccessMessage("INFORMACIÓN", "Valide su firma para firmar el Documento");

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo", "");
        }
    }

    public void validarFirmaImagenPDF() {
        if (validarFirma()) {
            imagenesPdfs = obtenerImagenesDesdePDF(firmaElectronica.getArchivoFirmar());
            if (Utils.isNotEmpty(imagenesPdfs)) {
            }
        }
    }

    public void validarFirmarDocumento() {
//        if (validarFirma()) {
        firmaElectronica.setClave(clave);
        FirmaElectronica archivoGenerado = generarFirmaElectronica(firmaElectronica, SisVars.wsFirmaEC);
        firmaElectronica.setClave(Utils.encriptaEnMD5(clave));
        firmaElectronica.setUrlQr("");
        firmaElectronica.setMotivo("Firmado Electrónicamente por FirmaEC");
        if (archivoGenerado != null) {
            JsfUtil.addInformationMessage("", "Elemento guardado con éxito.");
        } else {
            JsfUtil.addInformationMessage("", "No se pudo firmar el documento");
        }
//        PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
//        }
    }

    private Boolean validarFirma() {
        if (!clave.isEmpty()) {
            if (us.getFirmaElectronica() != null) {
                if (new Date().before(us.getFirmaElectronica().getFechaExpiracion())) {
                    if (Utils.encriptaEnMD5(clave).equals(us.getFirmaElectronica().getClave())) {
                        return Boolean.TRUE;
                    } else {
                        JsfUtil.addErrorMessage("", "Clave incorrecta.");
                        return Boolean.FALSE;
                    }
                } else {
                    JsfUtil.addErrorMessage("", "Su firma electrónica esta caducada");
                    return Boolean.FALSE;
                }
            } else {
                JsfUtil.addErrorMessage("", "Suba su firma electrónica, primero");
                return Boolean.FALSE;
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
            return Boolean.FALSE;
        }
    }

    public Void selectEndListener(ImageAreaSelectEvent e) {
        if (e.getWidth() == 150 && e.getHeight() == 50) {
            String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
            firmaElectronica.setClave(clave);
            firmaElectronica.setNumeroPagina(Integer.valueOf(pagina) + 1);
            firmaElectronica.setPosicionX1("" + (e.getX1()));
            firmaElectronica.setPosicionX2("" + (e.getX2()));
            firmaElectronica.setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
            firmaElectronica.setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
            FirmaElectronica archivoGenerado = firmarElectronicamente(firmaElectronica);
            firmaElectronica.setClave(Utils.encriptaEnMD5(clave));//SE LA ENCRIPTA NUEVAMENTE
            //Eliminar Archivos
            if (archivoGenerado != null) {
                ss.addParametro("download", true);
                ss.setNombreDocumento(archivoGenerado.getArchivoFirmado());
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
            }
            existenImagenes = false;
            imagenesPdfs = new ArrayList<>();
            clave = "";
            PrimeFaces.current().ajax().update("mainForm");
        } else {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica");
        }
        return null;
    }

    public FirmaElectronica generarFirmaElectronica(FirmaElectronica firmaElectrica, String tipo) {
        FirmaElectronica archivoGenerado = new FirmaElectronica();
        if (!firmaElectrica.getClave().isEmpty()) {
            if (Utils.encriptaEnMD5(firmaElectrica.getClave()).equals(us.getFirmaElectronica().getClave())) {
                firmaElectrica.setIsuser(us.getUsuario().getNameUsuario());
                firmaElectrica.setClave(firmaElectrica.getClave());
                //LLAMANDO DIRECTAMENTE AL METODO GENERAR FIRMA AL WS
                archivoGenerado = (FirmaElectronica) irisService.methodPOST(firmaElectrica, tipo + "firmaElectronica/generar", FirmaElectronica.class);
                firmaElectrica.setClave(Utils.encriptaEnMD5(firmaElectrica.getClave()));
                if (archivoGenerado != null) {
                    JsfUtil.addInformationMessage("", "Elemento guardado con éxito.");
                } else {
                    JsfUtil.addSuccessMessage("", "No se pudo firmar el documento");
                }

            } else {
                JsfUtil.addErrorMessage("", "Clave incorrecta");
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
        }

        return archivoGenerado;
    }

    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica) {
        firmaElectronica.setUsuario(null);
        return (FirmaElectronica) irisService.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/generar", FirmaElectronica.class);
    }

    public List<Imagenes> obtenerImagenesDesdePDF(String ruta) {
        String urlFile = Utils.getFilterRuta(ruta);
        return (List<Imagenes>) irisService.methodListGET(SisVars.appQrService + "resource/pdfImagenes/" + urlFile, Imagenes[].class);
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public boolean isShowOptionFirmar() {
        return showOptionFirmar;
    }

    public void setShowOptionFirmar(boolean showOptionFirmar) {
        this.showOptionFirmar = showOptionFirmar;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Imagenes> getImagenesPdfs() {
        return imagenesPdfs;
    }

    public void setImagenesPdfs(List<Imagenes> imagenesPdfs) {
        this.imagenesPdfs = imagenesPdfs;
    }

    public Boolean getExistenImagenes() {
        return existenImagenes;
    }

    public void setExistenImagenes(Boolean existenImagenes) {
        this.existenImagenes = existenImagenes;
    }

//</editor-fold>
}
