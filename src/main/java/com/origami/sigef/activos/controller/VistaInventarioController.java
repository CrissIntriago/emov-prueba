/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.administracionCompra.controller.Numero_Letras;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
 * @author Erwin
 */
@Named(value = "vistaInventarioView")
@ViewScoped
public class VistaInventarioController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    //Luis Suarez
    private LazyModel<Inventario> lazyVista;
    private List<InventarioItems> listaDetalleItems;
    private ArrayList<DetalleItem> listDetalle;
    private List<CatalogoMovimiento> motivoMovimiento;
    private Inventario inventario;
    private String cond;

    @Inject
    private ProcesoIngresoService procesoIngresoService;
    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;
    @Inject
    private InventarioItemsService inventarioItemsService;
    @Inject
    private ServletSession ss;
    private Numero_Letras numeroLetra;
    private BigDecimal valorTotal = BigDecimal.ZERO, valorUnitarioTotal = BigDecimal.ZERO;
    private Integer cantidadTotal = 0;

    @PostConstruct
    public void initView() {
        lazyVista = new LazyModel(Inventario.class);
        lazyVista.getFilterss().put("estadoAdicional:equal", "COMPLETO");
        listaDetalleItems = new ArrayList<>();
        inventario = new Inventario();
        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoEgresoInventario("INGINV", "SALINV");
        numeroLetra = new Numero_Letras();
    }

    public void visualizar(Inventario inv) {
        inventario = inv;
        listaDetalleItems = new ArrayList<>();
        listaDetalleItems = procesoIngresoService.verItems(inventario);
        getTotalListItems(listaDetalleItems);
        PrimeFaces.current().ajax().update(":formDetalle");
        PrimeFaces.current().executeScript("PF('dlgDetalle').show()");
    }

    public List<InventarioItems> getItemByInventario(Inventario inv) {
        if (inventario == null) {
            inventario = inv;
        }
        List<InventarioItems> listItemsConsultar = inventarioItemsService.getItemByInventarioItems(inventario);
        getTotalListItems(listItemsConsultar);
        return inventarioItemsService.getItemByInventarioItems(inventario);
    }

    public void getTotalListItems(List<InventarioItems> listItemsConsultar) {
        valorTotal = BigDecimal.ZERO;
        valorUnitarioTotal = BigDecimal.ZERO;
        cantidadTotal = 0;
        if (Utils.isNotEmpty(listItemsConsultar)) {
            cantidadTotal = listItemsConsultar.stream().mapToInt(x -> x.getCantidad() != null ? x.getCantidad() : 0).sum();
            valorTotal = listItemsConsultar.stream().filter(x -> x.getTotal() != null).map(x -> x.getTotal()).reduce(BigDecimal.ZERO, (b1, b2) -> b1.add(b2));
            valorUnitarioTotal = listItemsConsultar.stream().filter(x -> x.getPrecio() != null).map(x -> x.getPrecio()).reduce(BigDecimal.ZERO, (b1, b2) -> b1.add(b2));
        }
    }

    public void buscar(Inventario i) {
        listaDetalleItems = new ArrayList<>();
        listaDetalleItems = procesoIngresoService.verItems(i);
        PrimeFaces.current().executeScript("PF('dlgovistaInventario').show()");
        PrimeFaces.current().ajax().update(":formDlogoVistaInventario");

    }

    public void imprimirInventario(Inventario inv) {
        Servidor serv = inventarioItemsService.getServidorRol(RolUsuario.guardaAlmacen);
        if (serv != null) {
            ss.addParametro("guardalmacen", serv.getPersona() != null ? serv.getPersona().getNombreCompleto() : "");
        } else {
            ss.addParametro("guardalmacen", "");
        }
        if (inv.getTipoMovimiento().equals("INGRESO")) {
            ss.addParametro("ing_Id", inv.getId());
            ss.setNombreReporte("ingresoInventario");
            ss.setNombreSubCarpeta("activos");
            java.math.BigDecimal d = inventarioItemsService.getValorAcumuladoIngreso(inv);
            String num = numeroLetra.convertir(d.setScale(2, java.math.RoundingMode.HALF_UP) + "", true);
            ss.addParametro("acumulado_lt", num);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            ss.addParametro("egr_id", inv.getId());
            ss.setNombreReporte("egresoInventario");
            ss.setNombreSubCarpeta("activos");
            java.math.BigDecimal d = inventarioItemsService.getValorAcumulado(inv);
            String num = numeroLetra.convertir(d.setScale(2, java.math.RoundingMode.HALF_UP) + "", true);
            ss.addParametro("acumulado_lt", num);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<Inventario> getLazyVista() {
        return lazyVista;
    }

    public void setLazyVista(LazyModel<Inventario> lazyVista) {
        this.lazyVista = lazyVista;
    }

    public List<InventarioItems> getListaDetalleItems() {
        return listaDetalleItems;
    }

    public void setListaDetalleItems(List<InventarioItems> listaDetalleItems) {
        this.listaDetalleItems = listaDetalleItems;
    }

    public ArrayList<DetalleItem> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(ArrayList<DetalleItem> listDetalle) {
        this.listDetalle = listDetalle;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public List<CatalogoMovimiento> getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(List<CatalogoMovimiento> motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorUnitarioTotal() {
        return valorUnitarioTotal;
    }

    public void setValorUnitarioTotal(BigDecimal valorUnitarioTotal) {
        this.valorUnitarioTotal = valorUnitarioTotal;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
//</editor-fold>

}
