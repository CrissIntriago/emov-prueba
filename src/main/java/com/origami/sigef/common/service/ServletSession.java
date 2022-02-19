/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@SessionScoped
public class ServletSession implements Serializable {

    private List<Map<String, Object>> reportes;
    private Map<String, Object> parametros = null;
    private String urlAyuda;
    private String nombreReporte;
    private String nombreDocumento;
    private String nombreSubCarpeta;
    private String contentType = "pdf";
    private Boolean encuadernacion = Boolean.FALSE;
    private List dataSource;
    private Boolean agregarReporte = Boolean.FALSE;
    private Boolean fondoBlanco = Boolean.FALSE;
    private Boolean onePagePerSheet = Boolean.FALSE;
    private Boolean isIgnorePaginator = Boolean.FALSE;
    private Boolean imprimir = Boolean.FALSE;
    private Boolean borrarDatos = Boolean.TRUE;

    private Integer margen;
    //FIRMAR DE CERTICADOS
    private Long idCertficado;
    private Boolean firmarDoc = Boolean.FALSE;
    private Object tempData = null;
    private Boolean saveFile = Boolean.FALSE;
    private String urlServerFile;

    public void instanciarParametros() {
        parametros = new HashMap<>();
    }

    public void addParametro(String nombre, Object value) {
        if (parametros == null) {
            instanciarParametros();
        }
        parametros.put(nombre, value);
    }

    public Boolean estaVacio() {
        if (parametros != null) {
            return parametros.isEmpty();
        } else {
            return true;
        }
    }

    public boolean existeParametro(String parametro) {
        if (parametros == null) {
            return false;
        }
        return parametros.containsKey(parametro);
    }

    /**
     * En el <code>Map</code> debe esta incluido un parametro con
     * <code>nombreReporte</code> donde se va tomar el nombre del reporte que se
     * va agregar al final del primero.
     * <p>
     * Si el reporte seencuentra en la misma carpeta tomara en el nombre de la
     * variable <code>nombreSubCarpeta</code> para el caso que se encuentre en
     * otra carpeta se debe incluir otro parametro <code>nombreSubCarpeta</code>
     *
     * @param map Object
     */
    public void addParametrosReportes(Map map) {
        if (reportes == null) {
            reportes = new ArrayList<>();
        }
        reportes.add(map);
    }

    /**
     * Obtiene el valor de parametros.
     *
     * @param parametro
     * @return
     */
    public Object getValor(String parametro) {
        return parametros.get(parametro);
    }

    public void borrarDatos() {
        if (borrarDatos) {
            parametros = null;
            parametros = new HashMap<>();
            nombreReporte = null;
            nombreDocumento = null;
            nombreSubCarpeta = null;
            contentType = "pdf";
            encuadernacion = Boolean.FALSE;
            agregarReporte = Boolean.FALSE;
            reportes = null;
            idCertficado = null;
            firmarDoc = Boolean.FALSE;
            tempData = null;
            dataSource = null;
            this.isIgnorePaginator = Boolean.FALSE;
        }
    }

    public void borrarParametros() {
        parametros = null;
        reportes = null;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getNombreDocumento() {
        if (nombreDocumento == null) {
            nombreDocumento = nombreReporte + new Date().getTime();
        }
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getNombreSubCarpeta() {
        return nombreSubCarpeta;
    }

    public void setNombreSubCarpeta(String nombreSubCarpeta) {
        this.nombreSubCarpeta = nombreSubCarpeta;
    }

    public Boolean getEncuadernacion() {
        return encuadernacion;
    }

    public void setEncuadernacion(Boolean encuadernacion) {
        this.encuadernacion = encuadernacion;
    }

    public List getDataSource() {
        return dataSource;
    }

    public void setDataSource(List dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getAgregarReporte() {
        return agregarReporte;
    }

    public void setAgregarReporte(Boolean agregarReporte) {
        this.agregarReporte = agregarReporte;
    }

    public Boolean getFondoBlanco() {
        return fondoBlanco;
    }

    public void setFondoBlanco(Boolean fondoBlanco) {
        this.fondoBlanco = fondoBlanco;
    }

    public Boolean getOnePagePerSheet() {
        return onePagePerSheet;
    }

    public void setOnePagePerSheet(Boolean onePagePerSheet) {
        this.onePagePerSheet = onePagePerSheet;
    }

    /**
     * Tipo de documento que muestra el servlet pdf, xlsx, docx, png, jpg
     *
     * @return pdf, xlsx, docx, png, jpg
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Permite pdf, xlsx, docx, png, jpg
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getMargen() {
        return margen;
    }

    public void setMargen(Integer margen) {
        this.margen = margen;
    }

    public Boolean getFirmarDoc() {
        return firmarDoc;
    }

    public void setFirmarDoc(Boolean firmarDoc) {
        this.firmarDoc = firmarDoc;
    }

    public Long getIdCertficado() {
        return idCertficado;
    }

    public void setIdCertficado(Long idCertficado) {
        this.idCertficado = idCertficado;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public Object getTempData() {
        return tempData;
    }

    public List<Map<String, Object>> getReportes() {
        return reportes;
    }

    public void setReportes(List<Map<String, Object>> reportes) {
        this.reportes = reportes;
    }

    public void setTempData(Object tempData) {
        try {
            if (tempData instanceof Serializable) {
                this.tempData = Utils.clone(tempData);
            } else {
                this.tempData = tempData;
            }
        } catch (Exception ex) {
            Logger.getLogger(ServletSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean getBorrarDatos() {
        return borrarDatos;
    }

    public void setBorrarDatos(Boolean borrarDatos) {
        this.borrarDatos = borrarDatos;
    }

    public String getUrlAyuda() {
        return urlAyuda;
    }

    public void setUrlAyuda(String urlAyuda) {
        this.urlAyuda = urlAyuda;
    }

    public Boolean getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(Boolean saveFile) {
        this.saveFile = saveFile;
    }

    public String getUrlServerFile() {
        return urlServerFile;
    }

    public void setUrlServerFile(String urlServerFile) {
        this.urlServerFile = urlServerFile;
    }

    public Boolean getIsIgnorePaginator() {
        return isIgnorePaginator;
    }

    public void setIsIgnorePaginator(Boolean isIgnorePaginator) {
        this.isIgnorePaginator = isIgnorePaginator;
    }

}
