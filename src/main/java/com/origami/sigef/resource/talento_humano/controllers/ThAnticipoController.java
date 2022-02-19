/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.ThAnticipo;
import com.origami.sigef.resource.talento_humano.entities.ThAnticipoDetalle;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThAnticipoDetalleService;
import com.origami.sigef.resource.talento_humano.services.ThAnticipoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Named(value = "thAnticipoView")
@ViewScoped
public class ThAnticipoController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThAnticipoService thAnticipoService;
    @Inject
    private ThAnticipoDetalleService thAnticipoDetalleService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private FileUploadDoc uploadDoc;

    private ThAnticipo thAnticipo;
    private OpcionBusqueda opcionBusqueda;
    private ThConfig thConfig;

    private List<Short> periodos;
    private List<CatalogoItem> meseList;
    private List<ThAnticipoDetalle> thAnticipoDetalleList;
    private List<ThAnticipoDetalle> thAnticipoDetalleDeleteList;
    private List<UploadedFile> files;
    private List<CatalogoItem> listCatalogo;

    private LazyModel<ThAnticipo> thAnticipoLazy;

    private Boolean view;

    private String identificacion;

    @PostConstruct
    public void init() {
        clean();
        findThConfig(true);
        listCatalogo = thInterfaces.listaRubrosEgreso();
        updateLazyModel();
    }

    public void clean() {
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        thAnticipo = new ThAnticipo();
        thAnticipo.setPeriodo(opcionBusqueda.getAnio());
        meseList = thInterfaces.getListMes();
        thAnticipoDetalleList = new ArrayList<>();
        thAnticipoDetalleDeleteList = new ArrayList<>();
        identificacion = "";
    }

    public void updateLazyModel() {
        if (opcionBusqueda.getAnio() != null) {
            thAnticipoLazy = new LazyModel<>(ThAnticipo.class);
            thAnticipoLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thAnticipoLazy.getFilterss().put("estado", true);
            thAnticipoLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            thAnticipoLazy.setDistinct(false);
        } else {
            thAnticipoLazy = null;
        }
    }

    public void form(ThAnticipo thAnticipo, Boolean accion) {
        this.view = accion;
        if (thAnticipo != null) {
            this.thAnticipo = thAnticipo;
            this.thAnticipoDetalleList = Utils.clone(thAnticipoDetalleService.findByNamedQuery("ThAnticipoDetalle.findByAnticipo", this.thAnticipo));
        } else {
            String msj = findThConfig(true);
            if (!msj.equals("")) {
                JsfUtil.addWarningMessage("AVISO!!!", msj);
                return;
            }
            this.thAnticipo = new ThAnticipo();
            if (opcionBusqueda.getAnio() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                return;
            }
            this.thAnticipo.setFechaAnticipo(new Date());
            this.thAnticipo.setPeriodo(opcionBusqueda.getAnio());
            thAnticipoDetalleList = new ArrayList<>();
            thAnticipoDetalleDeleteList = new ArrayList<>();
        }
        JsfUtil.executeJS("PF('thAnticipoDlg').show()");
        PrimeFaces.current().ajax().update("thAnticipoForm");
    }

    public void findServidor() {
        thAnticipo.setServidor(thInterfaces.findByServidor(identificacion));
        if (thAnticipo.getServidor() != null) {
            thAnticipo.setIdCargoAsignado(thInterfaces.setServidorCargo(thAnticipo.getServidor()));
            thAnticipo.setRemuneracion(thAnticipo.getIdCargoAsignado().getIdCargo().getIdGrupo().getRemuneracionDolares());
            updateMontoAnticipo(false);
            JsfUtil.addSuccessMessage("INFO!!!", "Datos del servidor cargado");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encuentra ningun servidor público con el número de identificación ingresado");
        }
    }

    public void updateMontoAnticipo(Boolean accion) {
        thAnticipo.setMontoAnticipo(new BigDecimal(thAnticipo.getRemuneracion().doubleValue() * thAnticipo.getNumRemuneraciones()));
        if (accion) {
            updateDetalle();
        }
    }

    public void updateDetalle() {
        if (thAnticipo.getId() != null) {
            for (ThAnticipoDetalle item : thAnticipoDetalleList) {
                if (item.getId() != null) {
                    thAnticipoDetalleDeleteList.add(item);
                }
            }
        }
        thAnticipoDetalleList = new ArrayList<>();
        if (thAnticipo.getMontoAnticipo().doubleValue() <= 0) {
            thAnticipo.setIdMesSeleccionado(null);
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un servidor público");
            return;
        }
        if (thAnticipo.getNumCuotas() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "El número de cuotas debe ser mayor a cero");
            return;
        }
        if (thAnticipo.getIdMesSeleccionado() != null) {
            if (validarDisponibilidadCuotas()) {
                JsfUtil.addWarningMessage("AVISO!!!", "El número de cuotas ingresadas es mayor al de las cuotas disponibles");
                return;
            }
            double cuotaMes = (thAnticipo.getMontoAnticipo().doubleValue()) / thAnticipo.getNumCuotas();
            int count = 0;
            for (CatalogoItem mes : meseList) {
                if ((thAnticipo.getIdMesSeleccionado().getOrden() <= mes.getOrden()) && count < thAnticipo.getNumCuotas()) {
                    ThAnticipoDetalle detalle = new ThAnticipoDetalle();
                    detalle.setIdAnticipo(thAnticipo);
                    detalle.setIdMes(mes);
                    detalle.setValorCuota(new BigDecimal(cuotaMes));
                    thAnticipoDetalleList.add(detalle);
                    count++;
                }
            }
        }
        PrimeFaces.current().ajax().update("thAnticipoDetalleTable");
    }

    private boolean validarDisponibilidadCuotas() {
        Boolean result = false;
        int count = 0;
        for (CatalogoItem mes : meseList) {
            if ((thAnticipo.getIdMesSeleccionado().getOrden() <= mes.getOrden())) {
                count++;
            }
        }
        if (count < thAnticipo.getNumCuotas()) {
            result = true;
        }
        return result;
    }

    public void save() {
        if (thAnticipo.getDescripcion() == null || thAnticipo.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción");
            return;
        }
        if (thAnticipoDetalleList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay detalle de las cuotas del anticipo");
            return;
        }
        if (Utils.getMes(thAnticipo.getFechaAnticipo()) > thAnticipo.getIdMesSeleccionado().getOrden()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El mes de la fecha seleccionada es mayor al del mes seleccionado del anticipo");
            return;
        }
        if (validarDisponibilidadCuotas()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El número de cuotas ingresadas es mayor al de las cuotas disponibles");
            return;
        }
        Boolean edit = thAnticipo.getId() != null;
        thAnticipo.setIdRubro(thInterfaces.getIdThRubro(thConfig.getCodConfig()));
        if (edit) {
            thAnticipo.setUsuarioModificacion(thInterfaces.getUser());
            thAnticipo.setFechaModificacion(new Date());
            thAnticipoService.edit(thAnticipo);
            createDetalle(thAnticipo);
        } else {
            thAnticipo.setUsuarioCreacion(thInterfaces.getUser());
            thAnticipo.setFechaCreacion(new Date());
            thAnticipo = thAnticipoService.create(thAnticipo);
            createDetalle(thAnticipo);
        }
        //Eliminar detalle
        deleteDetalle();
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        clean();
        JsfUtil.executeJS("PF('thAnticipoDlg').hide()");
        PrimeFaces.current().ajax().update("thAnticipoTable");
    }

    private void createDetalle(ThAnticipo thAnticipo) {
        for (ThAnticipoDetalle item : thAnticipoDetalleList) {
            item.setIdAnticipo(thAnticipo);
            if (item.getId() != null) {
                thAnticipoDetalleService.edit(item);
            } else {
                thAnticipoDetalleService.create(item);
            }
        }
    }

    private void deleteDetalle() {
        if (!thAnticipoDetalleDeleteList.isEmpty()) {
            for (ThAnticipoDetalle detalle : thAnticipoDetalleDeleteList) {
                detalle.setEstado(Boolean.FALSE);
                thAnticipoDetalleService.edit(detalle);
            }
        }
    }

    public void aprobar(ThAnticipo thAnticipo) {
        thAnticipo.setAprobado(Boolean.TRUE);
        update(thAnticipo, "Se ha aprobado el el anticipo del servidor con CI: " + thAnticipo.getServidor().getPersona().getIdentificacion());
    }

    public void update(ThAnticipo thAnticipo, String msj) {
        thAnticipo.setUsuarioModificacion(thInterfaces.getUser());
        thAnticipo.setFechaModificacion(new Date());
        thAnticipoService.edit(thAnticipo);
        JsfUtil.addSuccessMessage("INFO!!!", msj);
        PrimeFaces.current().ajax().update("thAnticipoTable");
    }

    public void delete(ThAnticipo thAnticipo) {
        thAnticipo.setEstado(Boolean.FALSE);
        update(thAnticipo, "Se ha eliminado correctamente la información");
    }

    public void reporte(ThAnticipo thAnticipo, String tipoDocumento) {
        servletSession.addParametro("id_anticipo", thAnticipo.getId());
        servletSession.setNombreReporte("anticipo");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void uploadDoc(ThAnticipo thAnticipo) {
        this.thAnticipo = thAnticipo;
        files = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('uploadDocDlg').show()");
        PrimeFaces.current().ajax().update("uploadDocForm");
    }

    public void subirDocGeneral(FileUploadEvent event) {
        files.add(event.getFile());
        if (!files.isEmpty()) {
            uploadDoc.upload(thAnticipo, files);
        }
        PrimeFaces.current().executeScript("PF('uploadDocDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!", "Documento agregado correctamente");
    }

    public void verDocumentos(ThAnticipo thAnticipo) {
        this.thAnticipo = thAnticipo;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
    }

    public void openDlgConfig() {
        String msj = findThConfig(false);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        JsfUtil.executeJS("PF('thConfigDlg').show()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thInterfaces.findThConfig(CONFIG.CONFIG_TH_ANTICIPO);
        if (thConfig == null) {
            mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
        } else {
            if (accion) {
                if (thConfig.getCodConfig() == null) {
                    mensaje = "No existe la configuración para cargar los rubros";
                }
            }
        }
        return mensaje;
    }

    public void saveConfig() {
        thInterfaces.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public ThAnticipo getThAnticipo() {
        return thAnticipo;
    }

    public void setThAnticipo(ThAnticipo thAnticipo) {
        this.thAnticipo = thAnticipo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getMeseList() {
        return meseList;
    }

    public void setMeseList(List<CatalogoItem> meseList) {
        this.meseList = meseList;
    }

    public LazyModel<ThAnticipo> getThAnticipoLazy() {
        return thAnticipoLazy;
    }

    public void setThAnticipoLazy(LazyModel<ThAnticipo> thAnticipoLazy) {
        this.thAnticipoLazy = thAnticipoLazy;
    }

    public List<ThAnticipoDetalle> getThAnticipoDetalleList() {
        return thAnticipoDetalleList;
    }

    public void setThAnticipoDetalleList(List<ThAnticipoDetalle> thAnticipoDetalleList) {
        this.thAnticipoDetalleList = thAnticipoDetalleList;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

}
