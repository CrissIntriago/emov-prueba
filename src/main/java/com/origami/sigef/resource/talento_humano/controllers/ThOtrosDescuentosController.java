/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThOtrosDescuentos;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThOtrosDescuentosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thOtrosDescuentosView")
@ViewScoped
public class ThOtrosDescuentosController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThOtrosDescuentosService thOtrosDescuentosService;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThOtrosDescuentos thOtrosDescuentos;
    private ThConfig thConfig;

    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;
    private List<ThOtrosDescuentos> thOtrosDescuentosList;
    private List<ThOtrosDescuentos> thOtrosDescuentosDeleteList;
    private List<ThOtrosDescuentos> thOtrosDescuentosDetalleDeleteList;
    private List<CatalogoItem> listCatalogo;
    private List<ThRubro> thRubroList;

    private LazyModel<ThOtrosDescuentos> thOtrosDescuentosLazy;

    private Boolean panelBoolean, edit;

    private String identificacion;

    @PostConstruct
    public void init() {
        clean();
        listCatalogo = thInterfaces.listaRubrosEgreso();
        findThConfig(true);
        thRubroList = thInterfaces.rubros(thConfig.getCodConfig());
    }

    public void clean() {
        panelBoolean = Boolean.TRUE;
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        thOtrosDescuentos = new ThOtrosDescuentos();
        updateTipoRolList();
    }

    public void updateTipoRolList() {
        if (opcionBusqueda.getAnio() != null) {
            tipoRolList = thInterfaces.tipoRol(opcionBusqueda.getAnio());
        } else {
            tipoRolList = new ArrayList<>();
        }
    }

    public void updateLazyModel() {
        if (thTipoRol != null) {
            thOtrosDescuentosLazy = new LazyModel<>(ThOtrosDescuentos.class);
            thOtrosDescuentosLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thOtrosDescuentosLazy.getFilterss().put("estado", true);
            thOtrosDescuentosLazy.getFilterss().put("idTipoRol", thTipoRol);
            thOtrosDescuentosLazy.setDistinct(false);
        } else {
            thOtrosDescuentosLazy = null;
        }
    }

    public void determinarAccion() {
        if (thTipoRol != null) {
            if (thTipoRol.getAprobado()) {
                updateLazyModel();
                panelBoolean = Boolean.FALSE;
            } else {
                panelBoolean = Boolean.TRUE;
                List<ThOtrosDescuentos> temp = thOtrosDescuentosService.findByNamedQuery("ThOtrosDescuentos.findByTipoRol", thTipoRol);
                if (!temp.isEmpty()) {
                    thOtrosDescuentosList = Utils.clone(temp);
                } else {
                    thOtrosDescuentosList = new ArrayList<>();
                }
                thOtrosDescuentosDeleteList = new ArrayList<>();
                thOtrosDescuentosDetalleDeleteList = new ArrayList<>();
            }
        } else {
            panelBoolean = Boolean.TRUE;
            thOtrosDescuentosList = new ArrayList<>();
            thOtrosDescuentosDeleteList = new ArrayList<>();
            thOtrosDescuentosDetalleDeleteList = new ArrayList<>();
        }
    }

    public void form(ThOtrosDescuentos otrosDescuentos) {
        if (thTipoRol == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un tipo de rol");
            return;
        }
        if (otrosDescuentos != null) {
            thOtrosDescuentos = otrosDescuentos;
            if (otrosDescuentos.getId() != null) {
                thOtrosDescuentos.setThOtrosDescuentosList(Utils.clone(thOtrosDescuentosService.findByNamedQuery("ThOtrosDescuentos.findByDetalle", thOtrosDescuentos)));
            }
            edit = true;
        } else {
            thOtrosDescuentos = new ThOtrosDescuentos();
            thOtrosDescuentos.setIdTipoRol(thTipoRol);
            edit = false;
        }
        JsfUtil.executeJS("PF('descuentoDLG').show()");
        PrimeFaces.current().ajax().update("descuentoForm");
    }

    public void formView(ThOtrosDescuentos otrosDescuentos) {
        thOtrosDescuentos = otrosDescuentos;
        thOtrosDescuentos.setThOtrosDescuentosList(Utils.clone(thOtrosDescuentosService.findByNamedQuery("ThOtrosDescuentos.findByDetalle", thOtrosDescuentos)));
        JsfUtil.executeJS("PF('descuentoViewDLG').show()");
        PrimeFaces.current().ajax().update("descuentoFormView");
    }

    public void addDetalle() {
        ThOtrosDescuentos otros = new ThOtrosDescuentos();
        thOtrosDescuentos.getThOtrosDescuentosList().add(otros);
    }

    public void calcularTotal() {
        thOtrosDescuentos.setValor(BigDecimal.ZERO);
        if (!thOtrosDescuentos.getThOtrosDescuentosList().isEmpty()) {
            double valor = 0;
            for (ThOtrosDescuentos item : thOtrosDescuentos.getThOtrosDescuentosList()) {
                valor = valor + item.getValor().doubleValue();
            }
            this.thOtrosDescuentos.setValor(new BigDecimal(valor));
        }
    }

    public void saveDetalle() {
        if (thOtrosDescuentos.getServidor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un servidor");
            return;
        }
        if (thOtrosDescuentos.getDescripcion() == null || thOtrosDescuentos.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripcion");
            return;
        }
        if (thOtrosDescuentos.getIdRubro() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un rubro");
            return;
        }
        if (!thOtrosDescuentos.getThOtrosDescuentosList().isEmpty()) {
            for (ThOtrosDescuentos item : thOtrosDescuentos.getThOtrosDescuentosList()) {
                if (item.getDescripcion() == null || item.getDescripcion().equals("")) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripcion al detalle");
                    return;
                }
                if (item.getValor() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una valor");
                    return;
                }
            }
        }
        if (!edit) {
            thOtrosDescuentosList.add(Utils.clone(thOtrosDescuentos));
        }
        thOtrosDescuentos = null;
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha cargado correctamente el detalle");
        JsfUtil.executeJS("PF('descuentoDLG').hide()");
        PrimeFaces.current().ajax().update("descuentoForm");
    }

    public void findServidor() {
        thOtrosDescuentos.setServidor(thInterfaces.findByServidor(identificacion));
        if (thOtrosDescuentos.getServidor() != null) {
            JsfUtil.addSuccessMessage("INFO!!!", "Datos del servidor cargado");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encuentra ningun servidor público con el número de identificación ingresado");
        }
    }

    public void save() {
        //crear o editar
        if (!thOtrosDescuentosList.isEmpty()) {
            for (ThOtrosDescuentos item : thOtrosDescuentosList) {
                if (item.getId() == null) {
                    List<ThOtrosDescuentos> otrosList = Utils.clone(item.getThOtrosDescuentosList());
                    item = thOtrosDescuentosService.create(item);
                    if (!otrosList.isEmpty()) {
                        for (ThOtrosDescuentos aux : otrosList) {
                            aux.setIdDescuento(item);
                            thOtrosDescuentosService.create(aux);
                        }
                    }
                } else {
                    thOtrosDescuentosService.edit(item);
                    if (!item.getThOtrosDescuentosList().isEmpty()) {
                        for (ThOtrosDescuentos aux : item.getThOtrosDescuentosList()) {
                            if (aux.getId() != null) {
                                thOtrosDescuentosService.edit(aux);
                            } else {
                                aux.setIdDescuento(item);
                                thOtrosDescuentosService.create(aux);
                            }
                        }
                    }
                }
            }
            //eliminamos los registros
            if (!thOtrosDescuentosDeleteList.isEmpty()) {
                for (ThOtrosDescuentos item : thOtrosDescuentosDeleteList) {
                    item.setEstado(Boolean.FALSE);
                    thOtrosDescuentosService.edit(item);
                }
            }
            //eliminamos los registros del detalle
            if (!thOtrosDescuentosDetalleDeleteList.isEmpty()) {
                for (ThOtrosDescuentos item : thOtrosDescuentosDetalleDeleteList) {
                    item.setEstado(Boolean.FALSE);
                    thOtrosDescuentosService.edit(item);
                }
            }
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado correctamente la información");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay datos para guardar");
        }
    }

    public void detele(ThOtrosDescuentos descuento, int index) {
        if (descuento.getId() != null) {
            thOtrosDescuentosDeleteList.add(descuento);
            thOtrosDescuentosList.remove(descuento);
        } else {
            thOtrosDescuentosList.remove(index);
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se elimino correctamente el descuento");
    }

    public void deteleDetalle(ThOtrosDescuentos descuento, int index) {
        if (descuento.getId() != null) {
            thOtrosDescuentosDetalleDeleteList.add(descuento);
            thOtrosDescuentos.getThOtrosDescuentosList().remove(descuento);
        } else {
            thOtrosDescuentos.getThOtrosDescuentosList().remove(index);
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se elimino correctamente el detalle del descuento");
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

    public void saveConfig() {
        thInterfaces.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thInterfaces.findThConfig(CONFIG.CONFIG_TH_DESCUENTO);
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

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public ThTipoRol getThTipoRol() {
        return thTipoRol;
    }

    public void setThTipoRol(ThTipoRol thTipoRol) {
        this.thTipoRol = thTipoRol;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<ThTipoRol> getTipoRolList() {
        return tipoRolList;
    }

    public void setTipoRolList(List<ThTipoRol> tipoRolList) {
        this.tipoRolList = tipoRolList;
    }

    public List<ThOtrosDescuentos> getThOtrosDescuentosList() {
        return thOtrosDescuentosList;
    }

    public void setThOtrosDescuentosList(List<ThOtrosDescuentos> thOtrosDescuentosList) {
        this.thOtrosDescuentosList = thOtrosDescuentosList;
    }

    public LazyModel<ThOtrosDescuentos> getThOtrosDescuentosLazy() {
        return thOtrosDescuentosLazy;
    }

    public void setThOtrosDescuentosLazy(LazyModel<ThOtrosDescuentos> thOtrosDescuentosLazy) {
        this.thOtrosDescuentosLazy = thOtrosDescuentosLazy;
    }

    public Boolean getPanelBoolean() {
        return panelBoolean;
    }

    public void setPanelBoolean(Boolean panelBoolean) {
        this.panelBoolean = panelBoolean;
    }

    public ThOtrosDescuentos getThOtrosDescuentos() {
        return thOtrosDescuentos;
    }

    public void setThOtrosDescuentos(ThOtrosDescuentos thOtrosDescuentos) {
        this.thOtrosDescuentos = thOtrosDescuentos;
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

    public List<ThRubro> getThRubroList() {
        return thRubroList;
    }

    public void setThRubroList(List<ThRubro> thRubroList) {
        this.thRubroList = thRubroList;
    }

}
