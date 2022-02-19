/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThConfigHorasExtra;
import com.origami.sigef.resource.talento_humano.entities.ThDiasLaborados;
import com.origami.sigef.resource.talento_humano.entities.ThHorasExtras;
import com.origami.sigef.resource.talento_humano.entities.ThHorasExtrasDetalle;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThConfigHorasExtraService;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
import com.origami.sigef.resource.talento_humano.services.ThHorasExtrasDetalleService;
import com.origami.sigef.resource.talento_humano.services.ThHorasExtrasService;
import com.origami.sigef.resource.talento_humano.services.ThTipoRolService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
@Named(value = "thHorasExtrasView")
@ViewScoped
public class ThHorasExtrasController implements Serializable {

    @Inject
    private ThHorasExtrasService thHorasExtrasService;
    @Inject
    private ThHorasExtrasDetalleService thHorasExtrasDetalleService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThTipoRolService thTipoRolService;
    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThConfigHorasExtraService thConfigHorasExtraService;
    @Inject
    private ServletSession servletSession;

    private ThHorasExtras thHorasExtras;
    private ThTipoRol thTipoRolSeleccionado;
    private ThConfig thConfig;
    private OpcionBusqueda opcionBusqueda;

    private LazyModel<ThHorasExtras> thHorasExtrasLazy;

    private List<ThTipoRol> thTipoRolList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> listCatalogo;
    private List<ThHorasExtrasDetalle> thHorasExtrasDetalles, thHorasExtrasDetallesDelete;

    private Boolean view;

    private String identificacion;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        opcionBusqueda = new OpcionBusqueda();
        actualizarTipoRol();
        listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
        cleanForm();
    }

    private void cleanForm() {
        thHorasExtras = new ThHorasExtras();
        view = Boolean.TRUE;
    }

    public void updateLazy() {
        if (thTipoRolSeleccionado != null) {
            findThConfig(false);
            thHorasExtrasLazy = new LazyModel<>(ThHorasExtras.class);
            thHorasExtrasLazy.getSorteds().put("idCargoServidor.idServidor.persona.apellido", "ASC");
            thHorasExtrasLazy.getFilterss().put("estado", true);
            thHorasExtrasLazy.getFilterss().put("idTipoRol", thTipoRolSeleccionado);
            thHorasExtrasLazy.setDistinct(false);
        } else {
            thHorasExtrasLazy = null;
            thConfig = null;
        }
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thConfigService.findCode(CONFIG.CONFIG_TH_HORAS_EXTRAS);
        if (thConfig == null) {
            mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
        } else {
            if (accion) {
                if (thConfig.getCodConfig() == null) {
                    mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado";
                }
            }
        }
        return mensaje;
    }

    public void actualizarTipoRol() {
        if (opcionBusqueda.getAnio() == null) {
            thTipoRolList = null;
        } else {
            thTipoRolList = thTipoRolService.getRolAnio(opcionBusqueda.getAnio());
        }
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

    public void save() {
        thConfigService.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public void openForm(ThHorasExtras thHorasExtras, Boolean view) {
        String msj = findThConfig(true);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        this.view = view;
        this.thHorasExtras = thHorasExtras;
        thHorasExtrasDetalles = new ArrayList<>();
        thHorasExtrasDetallesDelete = new ArrayList<>();
        if (thHorasExtras != null) {
            thHorasExtrasDetalles = thHorasExtrasDetalleService.findByNamedQuery("ThHorasExtrasDetalle.findByIdHorasExtras", thHorasExtras);
            if (thHorasExtrasDetalles.isEmpty()) {
                getListDetalle();
            }
        } else {
            this.thHorasExtras = new ThHorasExtras();
        }
        JsfUtil.executeJS("PF('thHorasDetalleDlg').show()");
        PrimeFaces.current().ajax().update("thHorasDetalleForm");
    }

    public void getListDetalle() {
        List<ThConfigHorasExtra> temp = thConfigHorasExtraService.findByNamedQuery("ThConfigHorasExtra.findByIdTipo", thHorasExtras.getIdCargoServidor().getIdCargo().getIdCatalogoItem());
        if (temp != null && !temp.isEmpty()) {
            for (ThConfigHorasExtra conf : temp) {
                ThHorasExtrasDetalle obj = new ThHorasExtrasDetalle(conf.getPorcentaje(), conf.getIndice(), conf.getIdTipo().getTexto(), this.thHorasExtras, conf);
                thHorasExtrasDetalles.add(obj);
            }
        }
    }

    public void calcularMonto(ThHorasExtrasDetalle detalle) {
        if (detalle.getHoras() > detalle.getIdThConfigHoras().getMaxHoras()) {
            detalle.setHoras(0);
            detalle.setValor(BigDecimal.ZERO);
            JsfUtil.addWarningMessage("AVISO!!!", "Las horas ingresadas son mayor a las horas configuradas");
            return;
        }
        Integer valor = thInterfaces.getValorStatico();
        detalle.setValor(new BigDecimal((detalle.getIdHorasExtras().getRmu().doubleValue() / valor) * detalle.getIndice().doubleValue() * detalle.getHoras()));
        calcularTotal();
    }

    public void calcularTotal() {
        if (!thHorasExtrasDetalles.isEmpty()) {
            for (ThHorasExtrasDetalle item : thHorasExtrasDetalles) {
                thHorasExtras.setValor(thHorasExtras.getValor().add(item.getValor()).setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            thHorasExtras.setValor(BigDecimal.ZERO);
        }
    }

    public void closeForm() {
        if (thHorasExtras.getIdCargoServidor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un servidor");
            return;
        }
        thHorasExtras.setIdRubro(thInterfaces.getIdThRubro(thConfig.getCodConfig()));
        if (thHorasExtras.getId() != null) {
            thHorasExtras.setUsuarioModificacion(thInterfaces.getUser());
            thHorasExtras.setFechaModificacion(new Date());
            thHorasExtrasService.edit(thHorasExtras);
        } else {
            if (thInterfaces.getvalidarServidorHorasExtras(thHorasExtras.getIdCargoServidor(), thTipoRolSeleccionado)) {
                JsfUtil.addWarningMessage("AVISO!!!", "Ya existe un registro del servidor seleccionado");
                return;
            } else {
                thHorasExtras.setIdTipoRol(thTipoRolSeleccionado);
                thHorasExtras.setUsuarioCreacion(thInterfaces.getUser());
                thHorasExtras.setFechaCreacion(new Date());
                thHorasExtras = thHorasExtrasService.create(thHorasExtras);
            }
        }
        //guardar y editar
        for (ThHorasExtrasDetalle item : thHorasExtrasDetalles) {
            if (item.getId() != null) {
                thHorasExtrasDetalleService.edit(item);
            } else {
                item.setIdHorasExtras(thHorasExtras);
                thHorasExtrasDetalleService.create(item);
            }
        }
        //eliminar
        if (!thHorasExtrasDetallesDelete.isEmpty()) {
            for (ThHorasExtrasDetalle item : thHorasExtrasDetallesDelete) {
                item.setEstado(false);
                thHorasExtrasDetalleService.edit(item);
            }
        }
        identificacion = null;
        JsfUtil.executeJS("PF('thHorasDetalleDlg').hide()");
        JsfUtil.addSuccessMessage("INFO", "Se ha registrado correctamente");
        PrimeFaces.current().ajax().update("thHorasExtrasTable");
    }

    public void deleteDetalle(ThHorasExtrasDetalle detalle, int index) {
        if (detalle.getId() != null) {
            thHorasExtrasDetallesDelete.add(detalle);
            thHorasExtrasDetalles.remove(detalle);
        } else {
            thHorasExtrasDetalles.remove(index);
        }
        calcularTotal();
        JsfUtil.addSuccessMessage("INFO", "Se ha eliminado correctamente");
        PrimeFaces.current().ajax().update("thConfigHorasExtraTable");
    }

    public void openCloseRepoteDlg(Boolean accion, ThHorasExtras thHorasExtras) {
        this.thHorasExtras = thHorasExtras;
        if (accion) {
            JsfUtil.executeJS("PF('reporteDlg').show()");
        } else {
            JsfUtil.executeJS("PF('reporteDlg').hide()");
        }
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public void printReport(String tipoDocumento, int codigo) {
        switch (codigo) {
            case 1:
                servletSession.setNombreReporte("horas_extras");
                servletSession.addParametro("ci_servidor", "");
                break;
            case 2:
                servletSession.setNombreReporte("horas_extras_general");
                break;
            case 3:
                servletSession.setNombreReporte("horas_extras");
                servletSession.addParametro("ci_servidor", thHorasExtras.getIdCargoServidor().getIdServidor().getPersona().getIdentificacion());
                break;
        }
        servletSession.addParametro("idTipoRol", thTipoRolSeleccionado.getId());
        servletSession.addParametro("nombre_rol", thTipoRolSeleccionado.getDescripcion());
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void delete(ThHorasExtras thHorasExtras) {
        thHorasExtras.setEstado(Boolean.FALSE);
        thHorasExtras.setUsuarioModificacion(thInterfaces.getUser());
        thHorasExtras.setFechaModificacion(new Date());
        thHorasExtrasService.edit(thHorasExtras);
        JsfUtil.addSuccessMessage("INFO", "Se ha eliminado corractamente");
    }

    public void findServidor() {
        Servidor servidor = thInterfaces.findByServidor(identificacion);
        if (servidor != null) {
            thHorasExtras.setIdCargoServidor(thInterfaces.setServidorCargo(servidor));
            if (thHorasExtras.getIdCargoServidor() != null) {
                ThDiasLaborados dias = thInterfaces.getdiasLaborados(thHorasExtras.getIdCargoServidor(), thTipoRolSeleccionado);
                if (dias != null) {
                    thHorasExtras.setRmu(thHorasExtras.getIdCargoServidor().getIdCargo().getIdGrupo().getRemuneracionDolares());
                    thHorasExtras.setDias(dias.getDiasLaborados());
                    int horas = thHorasExtras.getDias() * 8;
                    thHorasExtras.setHoras((short) horas);
                    getListDetalle();
                    JsfUtil.addSuccessMessage("INFO!!!", "Datos del servidor cargado");
                } else {
                    JsfUtil.addWarningMessage("AVISO!!!", "El servidor no se ecuentra en la nomina del tipo de rol seleccionado");
                }
            } else {
                JsfUtil.addWarningMessage("AVISO!!!", "El servidor no tiene ningun cargo asignado");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encuentra ningun servidor público con el número de identificación ingresado");
        }
    }

    public ThHorasExtras getThHorasExtras() {
        return thHorasExtras;
    }

    public void setThHorasExtras(ThHorasExtras thHorasExtras) {
        this.thHorasExtras = thHorasExtras;
    }

    public ThTipoRol getThTipoRolSeleccionado() {
        return thTipoRolSeleccionado;
    }

    public void setThTipoRolSeleccionado(ThTipoRol thTipoRolSeleccionado) {
        this.thTipoRolSeleccionado = thTipoRolSeleccionado;
    }

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ThHorasExtras> getThHorasExtrasLazy() {
        return thHorasExtrasLazy;
    }

    public void setThHorasExtrasLazy(LazyModel<ThHorasExtras> thHorasExtrasLazy) {
        this.thHorasExtrasLazy = thHorasExtrasLazy;
    }

    public List<ThTipoRol> getThTipoRolList() {
        return thTipoRolList;
    }

    public void setThTipoRolList(List<ThTipoRol> thTipoRolList) {
        this.thTipoRolList = thTipoRolList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<ThHorasExtrasDetalle> getThHorasExtrasDetalles() {
        return thHorasExtrasDetalles;
    }

    public void setThHorasExtrasDetalles(List<ThHorasExtrasDetalle> thHorasExtrasDetalles) {
        this.thHorasExtrasDetalles = thHorasExtrasDetalles;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

}
