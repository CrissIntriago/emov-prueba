/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Services.CoaJuicioService;
import com.gestionTributaria.Services.FinaRenRubroLiquidacionService;
import com.gestionTributaria.Services.FnExoneracionClaseService;
import com.gestionTributaria.Services.FnTipoExoneracionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.ResolucionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class ResolucionesMB implements Serializable {
    
    @Inject
    private UserSession uSession;
    @Inject
    private ManagerService services;
    @Inject
    private ResolucionesService resolucionesService;
    @Inject
    private CoaJuicioService juiciosServices;
    @Inject
    private FnExoneracionClaseService exoneracionClaseService;
    @Inject
    private FnTipoExoneracionService exoneracionTipoService;
    @Inject
    private CatalogoItemService catalogoServices;
    @Inject
    private FinaRenRubroLiquidacionService finaRenRubroService;
    @Inject
    private MantenimientoResolucion resoService;
    private Documentos documento;
    private List<Documentos> listaDocumentos;
    private FnResolucion resolucion;
    private LazyModel<FnResolucion> listaResoluciones;
    private LazyModel<CoaJuicio> lazyJuicos;
    private Boolean abrirModal = false;
    private List<FnExoneracionClase> exoneracionesClases;
    private List<FnExoneracionTipo> exoneracionesTipo;
    private FnExoneracionClase exoneracionClase;
    private FnExoneracionTipo exoneracionTipo;
    private List<CatPredio> listaPredioSeleccionados;
    private LazyModel<CatPredio> predios;
    private List<CatalogoItem> porcentajes;
    private CatalogoItem porcentaje;
    private FinaRenRubrosLiquidacion rubro;
    private FnResolucionTipo resoTipo;
    
    public FnResolucionTipo getResoTipo() {
        return resoTipo;
    }
    
    public void setResoTipo(FnResolucionTipo resoTipo) {
        this.resoTipo = resoTipo;
    }
    //para buscar todas las solicitudes de exoneracion
    private LazyModel<FnResolucion> solicitudExoneracion;
    private List<FnResolucionTipo> resolucionTipo;
    
    public List<FnResolucionTipo> getResolucionTipo() {
        return resolucionTipo;
    }
    
    public void setResolucionTipo(List<FnResolucionTipo> resolucionTipo) {
        this.resolucionTipo = resolucionTipo;
    }
    
    @PostConstruct
    public void initView() {
        solicitudExoneracion = new LazyModel<>(FnResolucion.class);
        solicitudExoneracion.addFilter("estado", "A");
        porcentajes = new ArrayList<>();
        exoneracionClase = new FnExoneracionClase();
        exoneracionTipo = new FnExoneracionTipo();
        exoneracionesClases = exoneracionClaseService.findAllExoneraciones();
        exoneracionClase.setId(7L);
        findExoneracionTipos();
        exoneracionTipo.setId(45L);
        llenarLitadoPorcentaje();
        resolucion = new FnResolucion();
        listaDocumentos = new ArrayList<>();
        listaResoluciones = new LazyModel<>(FnResolucion.class);
        predios = new LazyModel<>(CatPredio.class);
        documento = new Documentos();
        listaPredioSeleccionados = new ArrayList<>();
        rubro = finaRenRubroService.findByIdRubroLiquidacion(369L);
        resolucionTipo = resoService.getResolucionT();
        resoTipo = new FnResolucionTipo();
    }
    
    public void llenarLitadoPorcentaje() {
        if (exoneracionTipo.getId().equals(45L)) {
            porcentajes = catalogoServices.getTipoIdentificacionCodigo("discapacidad");
        }
        if (exoneracionTipo.getId().equals(24L)) {
            porcentajes = catalogoServices.getTipoIdentificacionCodigo("leyAnciano");
        }
        if (exoneracionTipo.getId().equals(17L)) {
            porcentajes = catalogoServices.getTipoIdentificacionCodigo("empresaPublica");
        }
        
    }
    
    public List<FnExoneracionTipo> findExoneracionTipos() {
        exoneracionesTipo = new ArrayList<>();
        try {
            exoneracionesTipo = exoneracionTipoService.findAllExoneracionesByIdTipo(exoneracionClase.getId());
        } catch (Exception ex) {
            System.out.println("Ocurrio un error al buscar la lista de tipo de exoneración" + ex.getMessage());
        }
        return exoneracionesTipo;
    }

//    public void guardarResolucion() {
//        try {
//            //se guarda la resolucion
//            //A-->ACTIVO
//            //I-->INACTIVO
//            //AP-->APROBADO
//            resolucion.setEstado("A");
//            resolucion = resolucionesService.create(resolucion);
//            JsfUtil.addInformationMessage("SE GUARDÓ CON ÉXTO LA RESOLUCIÓN", "");
//            //guardamos el detalle de la resolucion
//        } catch (Exception ex) {
//            JsfUtil.addInformationMessage("OCURRIO UN ERROR AL TRATAR DE REGISTRAR LA RESOLUCION", "");
//        }
//
//    }
    public void llenarResolucion(FnResolucion solicitudExoneracion) {
        resolucion = solicitudExoneracion;
        System.out.println("resolucion id " + resolucion.getId());
    }
    
    public void handleFileDocumentBySave(FileUploadEvent event) throws ClassNotFoundException {
        try {
            System.out.println("ENTRA AL METODO");
            Boolean bandera = false;
            documento = new Documentos();
            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            documento.setDepartamento(uSession.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(event.getFile().getFileName());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setEstado(Boolean.TRUE);
            documento.setClaseNombre(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName());
            System.out.println("resolucion id identificador " + resolucion.getId());
            documento.setIdentificador(resolucion.getId());
            listaDocumentos.add(documento);
            System.out.println("LA LISTA DOCUMENTOS " + listaDocumentos);
            if (listaDocumentos.size() <= 1) {
                System.out.println("ENRA AL FOR");
                Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
                for (Documentos doc : listaDocumentos) {
                    System.out.println("entra al seugndo for");
                    documento = (Documentos) services.save(doc);
                }
                bandera = true;
            } else {
                documento = new Documentos();
                listaDocumentos.remove(listaDocumentos.size() - 1);
                JsfUtil.addInformationMessage("Solo puede subir un documento", "");
            }
            if (bandera == true) {
                JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
            }
//            JsfUtil.update("mainForm:dtResoluciones");
            listaDocumentos.clear();
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subir el documento");
        }
    }
    
    public void viewDocumento(FnSolicitudExoneracion resolucion) throws ClassNotFoundException {
        try {
            documento = (Documentos) services.documentoGestionTribtaria(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName(), resolucion.getId());
            if (documento != null) {
                JsfUtil.executeJS("PF('dowloadDoc').show()");
                System.out.println("DOCUMENTO-->" + documento.getRutaDocumento());
            } else {
                JsfUtil.addWarningMessage("Debe subir el documento", "");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MOSTRAR EL DOCUMENTOS" + ex.getMessage());
        }
        
    }

//    public void limpiar() {
//        resolucion = new FnResolucion();
//        documento = new Documentos();
//        listaDocumentos.clear();
//
//    }
    public void saveResolucion() {
        try {
            if (resolucion.getId() != null) {
                resolucionesService.edit(resolucion);
                
            } else {
//                resolucion.setEnte(uSession.getUsuario().getEnte());
//                resolucion.setEstado(Boolean.TRUE);
                resolucion.setFechaIngreso(new Date());
                resolucion.setEstado("A");
                resolucion.setUsuarioCreacion(uSession.getUsuario().getNameUsuario());
                resolucionesService.create(resolucion);
                JsfUtil.addInformationMessage("INFO", "La resolucion se creó con éxito");
            }
            resolucion = new FnResolucion();
            JsfUtil.executeJS("PF('newReslucion').hide();");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error", "Ocurrió un error al crear la resolución" + ex.getMessage());
        }
    }
    
    public void inactivarDocumento(Documentos doc, int index) {
        doc = listaDocumentos.get(index);
        if (doc.getId() != null) {
            doc.setEstado(Boolean.FALSE);
            services.update(doc);
        }
        listaDocumentos.remove(index);
        JsfUtil.addInformationMessage("", "El documento se inactivo con exito");
    }

//    public void editar(FnResolucion resolucion) {
//        this.resolucion = resolucion;
//        this.listaDocumentos = ((List<Documentos>) services.documentoGestionTribtariaActivos(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName(), resolucion.getId()));
//        if (this.listaDocumentos == null) {
//            this.listaDocumentos = new ArrayList<>();
//        }
//    }
    public void adjuntarProcesoCoactivo(CoaJuicio juicio) {
//        resolucion.setJuico(juicio);
        JsfUtil.update("mainForm:procesoSeleccionado");
    }

    //<editor-fold defaultstate="collapsed" desc="GET AND SET">
    public Documentos getDocumento() {
        return documento;
    }
    
    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }
    
    public Boolean getAbrirModal() {
        return abrirModal;
    }
    
    public void setAbrirModal(Boolean abrirModal) {
        this.abrirModal = abrirModal;
    }
    
    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }
    
    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }
//
//    public FnResolucion getResolucion() {
//        return resolucion;
//    }
//
//    public void setResolucion(FnResolucion resolucion) {
//        this.resolucion = resolucion;
//    }

    public LazyModel<FnResolucion> getListaResoluciones() {
        return listaResoluciones;
    }
    
    public void setListaResoluciones(LazyModel<FnResolucion> listaResoluciones) {
        this.listaResoluciones = listaResoluciones;
    }
    
    public LazyModel<CoaJuicio> getLazyJuicos() {
        return lazyJuicos;
    }
    
    public void setLazyJuicos(LazyModel<CoaJuicio> lazyJuicos) {
        this.lazyJuicos = lazyJuicos;
    }
    
    public List<FnExoneracionClase> getExoneracionesClases() {
        return exoneracionesClases;
    }
    
    public void setExoneracionesClases(List<FnExoneracionClase> exoneracionesClases) {
        this.exoneracionesClases = exoneracionesClases;
    }
    
    public List<FnExoneracionTipo> getExoneracionesTipo() {
        return exoneracionesTipo;
    }
    
    public void setExoneracionesTipo(List<FnExoneracionTipo> exoneracionesTipo) {
        this.exoneracionesTipo = exoneracionesTipo;
    }
    
    public FnExoneracionClase getExoneracionClase() {
        return exoneracionClase;
    }
    
    public void setExoneracionClase(FnExoneracionClase exoneracionClase) {
        this.exoneracionClase = exoneracionClase;
    }
    
    public FnExoneracionTipo getExoneracionTipo() {
        return exoneracionTipo;
    }
    
    public void setExoneracionTipo(FnExoneracionTipo exoneracionTipo) {
        this.exoneracionTipo = exoneracionTipo;
        
    }
    
    public List<CatPredio> getListaPredioSeleccionados() {
        return listaPredioSeleccionados;
    }
    
    public void setListaPredioSeleccionados(List<CatPredio> listaPredioSeleccionados) {
        this.listaPredioSeleccionados = listaPredioSeleccionados;
    }
    
    public LazyModel<CatPredio> getPredios() {
        return predios;
    }
    
    public void setPredios(LazyModel<CatPredio> predios) {
        this.predios = predios;
    }
    
    public List<CatalogoItem> getPorcentajes() {
        return porcentajes;
    }
    
    public void setPorcentajes(List<CatalogoItem> porcentajes) {
        this.porcentajes = porcentajes;
    }
    
    public CatalogoItem getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(CatalogoItem porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }
    
    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }
    
    public LazyModel<FnResolucion> getSolicitudExoneracion() {
        return solicitudExoneracion;
    }
    
    public void setSolicitudExoneracion(LazyModel<FnResolucion> solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

//</editor-fold>
    public FnResolucion getResolucion() {
        return resolucion;
    }
    
    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
    }
}
