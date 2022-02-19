/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "itemTarifarioView")
@ViewScoped
public class ItemTarifarioController implements Serializable {

    private static final Logger LOG = Logger.getLogger(ItemTarifarioController.class.getName());
    private static final long serialVersionUID = 1L;

    @Inject
    private PresCatalogoPresupuestarioService catalogoPresupuestoService;
    @Inject
    private ManagerService manager;
    @Inject
    private ContCuentaPartidaService cuentaPartidaService;

    private LazyModel<FinaRenRubrosLiquidacion> lazy;
    private List<FinaRenRubrosLiquidacion> itemList;
    private List<FinaRenRubrosLiquidacion> terceros;
    private List<FinaRenRubrosLiquidacion> itemsAdd;
    private FinaRenRubrosLiquidacion itemTarifario;
    private FinaRenRubrosLiquidacion itemTercero;
    private FinaRenRubrosLiquidacion itemTarifarioSelect;
    private Boolean edit;
    private List<PresCatalogoPresupuestario> tipos;
    private PresCatalogoPresupuestario tipo;
    private PresCatalogoPresupuestario tipoTercero;
    private PresCatalogoPresupuestario tipoCart;
    private List<ContCuentas> cuentascontable;
    private List<ContCuentas> cuentaContableList;
    private List<ContCuentas> contraCuentas;
    private List<PresCatalogoPresupuestario> partidas;
    private List<FinaRenRubrosLiquidacion> presupuestoList;
    private LazyModel<ContCuentas> cuentaLazy;
    private ContCuentas cuentaContableSeleccionado;
    private int codigoAccion;
    private BigDecimal porcentaje;
    private Boolean ctaOrdenPropio;
    private Boolean ctaOrdentercero;
    private Boolean requerido;

    @PostConstruct
    public void init() {
        edit = Boolean.FALSE;
        ctaOrdenPropio = Boolean.FALSE;
        ctaOrdentercero = Boolean.FALSE;
        itemTarifario = new FinaRenRubrosLiquidacion();
        itemTercero = new FinaRenRubrosLiquidacion();
        tipos = catalogoPresupuestoService.getCatalogoPresupuestoByNivel(2);
        lazy = new LazyModel<>(FinaRenRubrosLiquidacion.class);
//        lazy.addFilter("estado", true);
        lazy.addSorted("descripcion", "asc");
        itemsAdd = new ArrayList<>();
        porcentaje = new BigDecimal(100);
        tipo = new PresCatalogoPresupuestario();
        tipoTercero = new PresCatalogoPresupuestario();
        this.cargarList();
        requerido = Boolean.TRUE;
    }

    public void cargarList(){
        itemList = catalogoPresupuestoService.getFinaRubrosConfCta(Boolean.TRUE);
        terceros = catalogoPresupuestoService.getFinaRubrosConfCta(Boolean.FALSE);
    }
    
    public void guardar() {
        if (Utils.isEmpty(itemsAdd)) {
            JsfUtil.addWarningMessage("Error", "No se seleccionaron Rubros.");
            return;
        }
        for (FinaRenRubrosLiquidacion rb : itemsAdd) {
            System.out.println("item>"+itemTarifario.toString());
            rb.setContCc(itemTarifario.getContCc());
            rb.setContCp(itemTarifario.getContCp());
            rb.setPartida(itemTarifario.getPartida());
            rb.setPartidacart(itemTarifario.getPartidacart());
            rb.setCtaDebeCart(itemTarifario.getCtaDebeCart());
            rb.setCtaHaberCart(itemTarifario.getCtaHaberCart());
            rb.setCtaOrdenDeudor(itemTarifario.getCtaOrdenDeudor());
            rb.setCtaOrdenAcreedor(itemTarifario.getCtaOrdenAcreedor());
            manager.save(rb);
        }
        itemTarifario = new FinaRenRubrosLiquidacion();
        itemsAdd = new ArrayList<>();
        this.cargarList();
        JsfUtil.addInformationMessage("Datos " + (edit ? "Editados" : "Registrados") + " Correctamente", "");
//        cancelar();
//        PrimeFaces.current().ajax().update("datos");
    }
    public void guardarterceros() {
        if (Utils.isEmpty(itemsAdd)) {
            JsfUtil.addWarningMessage("Error", "No se seleccionaron Rubros.");
            return;
        }
        for (FinaRenRubrosLiquidacion rb : itemsAdd) {
            System.out.println("item>"+itemTarifario.toString());
            rb.setContCc(itemTercero.getContCc());
            rb.setContCp(itemTercero.getContCp());
            rb.setCtaDebeCart(itemTercero.getCtaDebeCart());
            rb.setCtaHaberCart(itemTercero.getCtaHaberCart());
            rb.setPartidacart(itemTercero.getPartidacart());
            rb.setPorcentajeServicio(itemTercero.getPorcentajeServicio());
            rb.setPorcentaje(itemTercero.getPorcentaje());
            rb.setCtaOrdenDeudor(itemTercero.getCtaOrdenDeudor());
            rb.setCtaOrdenAcreedor(itemTercero.getCtaOrdenAcreedor());
            manager.save(rb);
        }
        itemTercero = new FinaRenRubrosLiquidacion();
        itemsAdd = new ArrayList<>();
        this.cargarList();
        JsfUtil.addInformationMessage("Datos " + (edit ? "Editados" : "Registrados") + " Correctamente", "");
//        cancelar();
//        PrimeFaces.current().ajax().update("datos");
    }
    
    public void eliminarconfig(FinaRenRubrosLiquidacion item){
        System.out.println("item>>"+ item);
        item.setPartida(null);
        item.setPartidacart(null);
        item.setContCc(null);
        item.setContCp(null);
        item.setCtaDebeCart(null);
        item.setCtaHaberCart(null);
        item.setCtaOrdenDeudor(null);
        item.setCtaOrdenAcreedor(null);
        manager.save(item);
        this.cargarList();
    }
    
    public void editar(FinaRenRubrosLiquidacion item) {
        requerido = Boolean.FALSE;
        itemsAdd = new ArrayList<>();
        this.edit = Boolean.TRUE;
        if(item.getRubroDelMunicipio()){
            if (!itemList.contains(item)) {                
            itemList.add(item);
            }
            this.initRubrosPropios();
            this.setRubroPropio(item);
        }else{
            if(!terceros.contains(item)){
            terceros.add(item);
            }
            this.initRubrosTerceros();
            this.setRubroTercero(item);
        }
        itemsAdd.add(item);
    }
    
    public void initRubrosPropios(){
        itemTarifario = new FinaRenRubrosLiquidacion();
        itemTarifario.setPartida(new PresCatalogoPresupuestario());
        itemTarifario.setPartidacart(new PresCatalogoPresupuestario());
        itemTarifario.setContCc(new ContCuentas());
        itemTarifario.setContCp(new ContCuentas());
        itemTarifario.setCtaDebeCart(new ContCuentas());
        itemTarifario.setCtaHaberCart(new ContCuentas());
        itemTarifario.setCtaOrdenDeudor(new ContCuentas());
        itemTarifario.setCtaOrdenAcreedor(new ContCuentas());
    }
    public void setRubroPropio(FinaRenRubrosLiquidacion item){
        itemTarifario.setPartida(item.getPartida());
        itemTarifario.setPartidacart(item.getPartidacart());
        itemTarifario.setContCc(item.getContCc());
        itemTarifario.setContCp(item.getContCp());
        itemTarifario.setCtaDebeCart(item.getCtaDebeCart());
        itemTarifario.setCtaHaberCart(item.getCtaHaberCart());
        itemTarifario.setCtaOrdenDeudor(item.getCtaOrdenDeudor());
        itemTarifario.setCtaOrdenAcreedor(item.getCtaOrdenAcreedor());
    }
    public void setRubroTercero(FinaRenRubrosLiquidacion item){
        itemTercero.setPartida(item.getPartida());
        itemTercero.setPartidacart(item.getPartidacart());
        itemTercero.setContCc(item.getContCc());
        itemTercero.setContCp(item.getContCp());
        itemTercero.setCtaDebeCart(item.getCtaDebeCart());
        itemTercero.setCtaHaberCart(item.getCtaHaberCart());
        itemTercero.setPorcentaje(item.getPorcentaje());
        itemTercero.setPorcentajeServicio(item.getPorcentajeServicio());
        itemTercero.setCtaOrdenDeudor(item.getCtaOrdenDeudor());
        itemTercero.setCtaOrdenAcreedor(item.getCtaOrdenAcreedor());
    }
    public void initRubrosTerceros(){
        itemTercero = new FinaRenRubrosLiquidacion();
        itemTercero.setPartida(new PresCatalogoPresupuestario());
        itemTercero.setPartidacart(new PresCatalogoPresupuestario());
        itemTercero.setContCc(new ContCuentas());
        itemTercero.setContCp(new ContCuentas());
        itemTercero.setCtaDebeCart(new ContCuentas());
        itemTercero.setCtaHaberCart(new ContCuentas());
        itemTercero.setCtaOrdenDeudor(new ContCuentas());
        itemTercero.setCtaOrdenAcreedor(new ContCuentas());
    }
    
    public List<PresCatalogoPresupuestario> catalogoCuenta(String busqueda) {
//        System.out.println("// tipo>>" + busqueda);
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");

        if (tipo != null) {
            partidas = cuentaPartidaService.getCatalogoMovimientoByCodCta(tipo, b);
//            partidas = catalogoPresupuestoService.filterCatalogoPresupuesto(b);
        }
        return partidas;
    }
    
    public List<PresCatalogoPresupuestario> catalogoCuentaTercero(String busqueda) {
//        System.out.println("// tipo>>" + busqueda);
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");

        if (tipoTercero != null) {
            partidas = cuentaPartidaService.getCatalogoMovimientoByCodCta(tipoTercero, b);
//            partidas = catalogoPresupuestoService.filterCatalogoPresupuesto(b);
        }
        return partidas;
    }

    public List<PresCatalogoPresupuestario> catalogoCuentaCartera(String busqueda) {
//        System.out.println("// tipo>>" + busqueda);
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");

        if (tipo != null) {
            partidas = cuentaPartidaService.getCatalogoMovimientoByCodCta(tipoCart, b);
//            partidas = catalogoPresupuestoService.filterCatalogoPresupuesto(b);
        }
        return partidas;
    }

    public List<ContCuentas> cuentasDebe(String busqueda) {
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");
        System.out.println("tipo>>" + itemTarifario.getPartida() + " criterio>>" + b);
        return cuentaPartidaService.getCuentasDebe(itemTarifario.getPartida(), b);
    }

    public List<ContCuentas> cuentasDebeCartera(String busqueda) {
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");
        System.out.println("tipo>>" + itemTarifario.getPartida() + " criterio>>" + b);
        return cuentaPartidaService.getCuentasDebe(itemTarifario.getPartidacart(), b);
    }

    public List<ContCuentas> cuentashaber(String busqueda) {
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");
        System.out.println("tipo>>" + itemTarifario.getPartida() + " criterio>>" + b);
        return cuentaPartidaService.getCuentasHaber(itemTarifario.getPartida(), b);
    }

    public List<ContCuentas> cuentashaberCartera(String busqueda) {
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");
        System.out.println("tipo>>" + itemTarifario.getPartida() + " criterio>>" + b);
        return cuentaPartidaService.getCuentasHaber(itemTarifario.getPartidacart(), b);
    }

    public List<FinaRenRubrosLiquidacion> rubrosLiquidacion(String busqueda) {
        if (busqueda == null) {
            return null;
        }
        busqueda = "%" + busqueda + "%";
        String b = busqueda.toUpperCase().replaceAll(" ", "%");
        this.itemList = catalogoPresupuestoService.getFilterrubrosLiquidacion(b);
        return itemList;
    }

    public void openDlgCuentaContable(int codigo) {
        this.codigoAccion = codigo;
        this.cuentaLazy = new LazyModel<>(ContCuentas.class);
        this.cuentaLazy.getSorteds().put("codigo", "ASC");
        this.cuentaLazy.getFilterss().put("estado", true);
        this.cuentaLazy.getFilterss().put("movimiento", true);        
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
    }
    
    public void aniadirCuentaContable() {
        switch(codigoAccion){
            case 1:
                itemTercero.setContCc(cuentaContableSeleccionado);
                break;
            case 2:
                itemTercero.setContCp(cuentaContableSeleccionado);
                break;
            case 3:
                itemTercero.setCtaDebeCart(cuentaContableSeleccionado);
            break;
            case 4:
                itemTercero.setCtaHaberCart(cuentaContableSeleccionado);
                break;
            case 5:
                itemTercero.setCtaOrdenDeudor(cuentaContableSeleccionado);
                break;
            case 6:
                itemTercero.setCtaOrdenAcreedor(cuentaContableSeleccionado);
                break;
            case 7:
                itemTarifario.setCtaOrdenDeudor(cuentaContableSeleccionado);
                break;
            case 8:
                itemTarifario.setCtaOrdenAcreedor(cuentaContableSeleccionado);
                break;
            default:
                break;
        }
        cuentaContableSeleccionado = new ContCuentas();
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
    }

//<editor-fold defaultstate="collapsed" desc="getter and setter">
    public LazyModel<FinaRenRubrosLiquidacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FinaRenRubrosLiquidacion> lazy) {
        this.lazy = lazy;
    }

    public Boolean getCtaOrdenPropio() {
        return ctaOrdenPropio;
    }

    public void setCtaOrdenPropio(Boolean ctaOrdenPropio) {
        this.ctaOrdenPropio = ctaOrdenPropio;
    }

    public Boolean getCtaOrdentercero() {
        return ctaOrdentercero;
    }

    public void setCtaOrdentercero(Boolean ctaOrdentercero) {
        this.ctaOrdentercero = ctaOrdentercero;
    }

    public PresCatalogoPresupuestario getTipoTercero() {
        return tipoTercero;
    }

    public void setTipoTercero(PresCatalogoPresupuestario tipoTercero) {
        this.tipoTercero = tipoTercero;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public List<FinaRenRubrosLiquidacion> getItemsAdd() {
        return itemsAdd;
    }

    public void setItemsAdd(List<FinaRenRubrosLiquidacion> itemsAdd) {
        this.itemsAdd = itemsAdd;
    }

    public List<FinaRenRubrosLiquidacion> getItemList() {
        return itemList;
    }

    public void setItemList(List<FinaRenRubrosLiquidacion> itemList) {
        this.itemList = itemList;
    }

    public FinaRenRubrosLiquidacion getItemTarifario() {
        return itemTarifario;
    }

    public void setItemTarifario(FinaRenRubrosLiquidacion itemTarifario) {
        this.itemTarifario = itemTarifario;
    }

    public FinaRenRubrosLiquidacion getItemTarifarioSelect() {
        return itemTarifarioSelect;
    }

    public void setItemTarifarioSelect(FinaRenRubrosLiquidacion itemTarifarioSelect) {
        this.itemTarifarioSelect = itemTarifarioSelect;
    }

    public List<PresCatalogoPresupuestario> getTipos() {
        return tipos;
    }

    public List<ContCuentas> getCuentascontable() {
        return cuentascontable;
    }

    public void setCuentascontable(List<ContCuentas> cuentascontable) {
        this.cuentascontable = cuentascontable;
    }

    public List<ContCuentas> getCuentaContableList() {
        return cuentaContableList;
    }

    public void setCuentaContableList(List<ContCuentas> cuentaContableList) {
        this.cuentaContableList = cuentaContableList;
    }

    public List<ContCuentas> getContraCuentas() {
        return contraCuentas;
    }

    public void setContraCuentas(List<ContCuentas> contraCuentas) {
        this.contraCuentas = contraCuentas;
    }

    public List<PresCatalogoPresupuestario> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<PresCatalogoPresupuestario> partidas) {
        this.partidas = partidas;
    }

    public List<FinaRenRubrosLiquidacion> getPresupuestoList() {
        return presupuestoList;
    }

    public void setPresupuestoList(List<FinaRenRubrosLiquidacion> presupuestoList) {
        this.presupuestoList = presupuestoList;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public PresCatalogoPresupuestario getTipoCart() {
        return tipoCart;
    }

    public void setTipoCart(PresCatalogoPresupuestario tipoCart) {
        this.tipoCart = tipoCart;
    }

    public List<FinaRenRubrosLiquidacion> getTerceros() {
        return terceros;
    }

    public void setTerceros(List<FinaRenRubrosLiquidacion> terceros) {
        this.terceros = terceros;
    }

    public FinaRenRubrosLiquidacion getItemTerceros() {
        return itemTercero;
    }

    public void setItemTerceros(FinaRenRubrosLiquidacion itemTercero) {
        this.itemTercero = itemTercero;
    }

    public LazyModel<ContCuentas> getCuentaLazy() {
        return cuentaLazy;
    }

    public void setCuentaLazy(LazyModel<ContCuentas> cuentaLazy) {
        this.cuentaLazy = cuentaLazy;
    }

    public FinaRenRubrosLiquidacion getItemTercero() {
        return itemTercero;
    }

    public void setItemTercero(FinaRenRubrosLiquidacion itemTercero) {
        this.itemTercero = itemTercero;
    }

    public ContCuentas getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(ContCuentas cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public PresCatalogoPresupuestario getTipo() {
        return tipo;
    }

    public void setTipo(PresCatalogoPresupuestario tipo) {
        this.tipo = tipo;
    }
//</editor-fold>
}
