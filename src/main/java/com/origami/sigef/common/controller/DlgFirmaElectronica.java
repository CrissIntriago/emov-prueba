/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.ws.IrisService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.ImageAreaSelectEvent;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named
@ViewScoped
public class DlgFirmaElectronica implements Serializable {

    @Inject
    private IrisService irisService;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;

    private FirmaElectronica firmaElectronica;
    private String clave;
    // //SIRVE PARA CONDICIONAR SI YA LA CLAVE INGRESADA ESTA VALIDADA PARA QUE SE PROCEDA A MOSTRAR LAS URL DE LAS IMAGENES DEL PDF
    private Boolean imagenPDF;
    private List<Imagenes> imagenesPdfs;
    private Boolean existenImagenes;

    @PostConstruct
    protected void initView() {
        imagenPDF = Boolean.valueOf(ss.getParametros().get("imagenPDF").toString());
        if (imagenPDF == null) {
            imagenPDF = Boolean.FALSE;
        }
        firmaElectronica = (FirmaElectronica) ss.getParametros().get("firmaElectronica");
        firmaElectronica.setUsuario(new Usuarios(us.getUserId()));
        clave = "";
        imagenesPdfs = new ArrayList<>();
        existenImagenes = Boolean.FALSE;

    }

    /**
     * En teoria la firma esta validada en el sistema y si deberia dejar firmar
     * =V SI RETORNA NULL TONCES NO SE FIRMO
     */
    public void validarFirmarDocumento() {
        if (validarFirma()) {
            firmaElectronica.setClave(clave);
            FirmaElectronica archivoGenerado = firmarElectronicamente(firmaElectronica);
//            FirmaElectronica archivoGenerado = appServices.firmarElectronicamente(firmaElectronica);
            firmaElectronica.setClave(Utils.encriptaEnMD5(clave));
            if (archivoGenerado != null) {
                JsfUtil.addInformationMessage("Información", "Archivo Firmado Correctamente");
            } else {
                JsfUtil.addInformationMessage("Información", "No se pudo firmar el documento");
            }
            PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
        }
    }

    /**
     * *
     * ESTE METODO SIRVE PARA VALIDAR LA FIRMA ELECTRONICA Y VER SI PROCEDE A
     * QUE SE GENEREN LAS URLS (IMAGENES) DEL PDF SUBIDO Y ASI PODER OBTENER LA
     * POSICION DONDE SERÁ FIRMADO EL PDF
     */
    public void validarFirmaImagenPDF() {
        if (validarFirma()) {
            imagenesPdfs = obtenerImagenesDesdePDF(firmaElectronica.getArchivoFirmar());
            if (Utils.isNotEmpty(imagenesPdfs)) {
                existenImagenes = Boolean.TRUE;
            }
        }
    }

    /**
     * Obtiene la posicion de la imagen en dond se necesita que la firma sea
     * puesta
     *
     * @param e
     */
    public void selectEndListener(ImageAreaSelectEvent e) {
        if (e.getWidth() == 150 && e.getHeight() == 50) {
            String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
            firmaElectronica.setClave(clave);
            firmaElectronica.setNumeroPagina(Integer.valueOf(pagina) + 1);
            firmaElectronica.setPosicionX1("" + (e.getX1()));
            firmaElectronica.setPosicionX2("" + (e.getX2()));
            firmaElectronica.setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
            firmaElectronica.setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
            firmaElectronica.setMotivo("Firmado Electrónicamente por FirmaEC");
            firmaElectronica.setUrlQr("");
            // firmaElectronica.setWidth(e.getImgWidth() + "");
            // firmaElectronica.setHeight(e.getImgHeight() + "");

            FirmaElectronica archivoGenerado = firmarElectronicamente(firmaElectronica);
            firmaElectronica.setClave(Utils.encriptaEnMD5(clave));//SE LA ENCRIPTA NUEVAMENTE
            PrimeFaces.current().dialog().closeDynamic(archivoGenerado);//devuelve el nombre del archivo debe anexarle la ruta para que está sea visualizada
        } else {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica");
        }
    }

    private Boolean validarFirma() {

        if (!clave.isEmpty()) {
            if (us.getFirmaElectronica() != null) {
                if (new Date().before(us.getFirmaElectronica().getFechaExpiracion())) {
                    if (Utils.encriptaEnMD5(clave).equals(us.getFirmaElectronica().getClave())) {
                        return Boolean.TRUE;
                    } else {
                        JsfUtil.addErrorMessage("", "Clave incorrecta");
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

    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica) {
        return (FirmaElectronica) irisService.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/generar", FirmaElectronica.class);
    }

    public List<Imagenes> obtenerImagenesDesdePDF(String ruta) {
        String urlFile = Utils.getFilterRuta(ruta);
        return (List<Imagenes>) irisService.methodListGET(SisVars.appQrService + "resource/pdfImagenes/" + urlFile, Imagenes[].class);
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Sertters">

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getImagenPDF() {
        return imagenPDF;
    }

    public void setImagenPDF(Boolean imagenPDF) {
        this.imagenPDF = imagenPDF;
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
