package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.models.ListadoInventarioModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "listadoInventarioController")
@ViewScoped
public class ListadoInventarioController implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private GrupoNivelesService nivelesService;
    @Inject
    private DetalleItemService itemService;
    @Inject
    private InventarioItemsService inventarioItemsService;
    private String cantidadExistente = "can-todos";//si es can-todos es con 0
    private String showCuentaContable = "tipo-existencia";//si es tipo-existencia muestra items
    private Date fechaHasta;

    private DetalleItem itemSelect;
    private ContCuentas cuentaSelect;
    private GrupoNiveles areaSelect, grupoSelect, SubgrupoSelect;
    private List<GrupoNiveles> listAreas, listGrupos, listSubgrupos;
    private List<ContCuentas> listContCuentas;
    private List<DetalleItem> listItems;
    private List<ListadoInventarioModel> listItemsInventario;

    @PostConstruct
    public void initView() {
        listAreas = nivelesService.findByPadres();
        clean();
    }

    public void clean() {
        listGrupos = null;
        listSubgrupos = null;
        areaSelect = null;
        grupoSelect = null;
        SubgrupoSelect = null;
        itemSelect = null;
        cantidadExistente = "can-todos";
        showCuentaContable = "tipo-existencia";
        fechaHasta = new Date();
        listItemsInventario = null;
    }

    public void loadCuentasContables() {
        if (showCuentaContable.equals("tipo-contable")) {
            listContCuentas = itemService.getCuentasContablesOfDetalleItems();
            listItemsInventario = null;
        } else if (showCuentaContable.equals("tipo-existencia")) {
            clean();
        }
    }

    public void loadGrupos() {
        grupoSelect = null;
        SubgrupoSelect = null;
        itemSelect = null;
        if (areaSelect != null) {
//            System.out.println("id area " + areaSelect.getId());
            listGrupos = nivelesService.findGrupoByPadreEscogido(areaSelect);
        }
    }

    public void loadSubgrupos() {
        SubgrupoSelect = null;
        itemSelect = null;
        if (grupoSelect != null) {
//            System.out.println("id grupo " + grupoSelect.getId());
            listSubgrupos = nivelesService.findGrupoByPadreEscogido(grupoSelect);
        }
    }

    public void loadItems() {
        itemSelect = null;
        if (SubgrupoSelect != null) {
//            System.out.println("id SubgrupoSelect " + SubgrupoSelect.getId());
            listItems = itemService.getDetalleItemByGrupoNivel(SubgrupoSelect);
        }
    }

    public void generar() {
//        System.out.println("fecha hasta " + fechaHasta);
//        System.out.println("Datos: " + areaSelect.getId() + " grupoSelect: " + grupoSelect.getId() + " SubgrupoSelect: "
//                + SubgrupoSelect.getId() + " itemSelect: " + itemSelect.getId());
        if (showCuentaContable.equals("tipo-existencia")) {
            listItemsInventario = itemService.getItemsModel(areaSelect != null ? areaSelect.getId() : null, grupoSelect != null ? grupoSelect.getId() : null,
                    SubgrupoSelect != null ? SubgrupoSelect.getId() : null, itemSelect != null ? itemSelect.getId() : null);
        } else if (showCuentaContable.equals("tipo-contable")) {
            listItemsInventario = itemService.getItemsContablesModel(cuentaSelect != null ? cuentaSelect.getId() : null);
        }
        for (ListadoInventarioModel l : listItemsInventario) {
            System.out.println("l.id: " + l.getId());
            calculateStockByPeriodo(l);
        }
        if (cantidadExistente.equals("can-existencia")) {
            listItemsInventario = listItemsInventario.stream().filter(x -> x.getCantidad() > 0).collect(Collectors.toList());
        }
        JsfUtil.addSuccessMessage("Información", "Items Cargados Correctamente.");
    }

    public void generarReporte(Boolean tipo) {
        if (!tipo) {
            ss.setContentType("xlsx");
            ss.setOnePagePerSheet(true);
        }
        ss.addParametro("hasta", fechaHasta);
        ss.setDataSource(listItemsInventario);
        ss.setNombreReporte(showCuentaContable.equals("tipo-existencia") ? "listadoItems" : "listadoItemsCuentaContable");
        ss.setNombreSubCarpeta("inventario");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void calculateStockByPeriodo(ListadoInventarioModel l) {
        List<InventarioItems> lisInventarioMovimiento = inventarioItemsService.getItemsInventarioByFechaHastaMovimientos(l.getId(), fechaHasta);
        System.out.println("movimientos " + lisInventarioMovimiento != null ? lisInventarioMovimiento.size() : "no movimientos");
        Integer cant = 0;
        Integer cantidadExistente = 0;
        BigDecimal precio = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal precioExistente = BigDecimal.ZERO;
        BigDecimal totalExistente = BigDecimal.ZERO;
        BigDecimal precioAcumulado = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        System.out.println("Codigo >>> "+l.getCodigoAgrupado());
        for (InventarioItems i : lisInventarioMovimiento) {
            if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                cant = i.getCantidad();
                if (i.getIva() != null) {
                    if (i.getIva().compareTo(BigDecimal.ZERO) == 1) {
                        iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_UP);
                        precio = iva;
                    } else {
                        precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                    }
                } else {
                    precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                }
                total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_UP);

                cantidadExistente = cant + cantidadExistente;
                totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_UP);
//                precioExistente = BigDecimal.valueOf(totalExistente.doubleValue() / cantidadExistente).setScale(4, RoundingMode.HALF_UP);
                if (cantidadExistente > 0) {
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                }else{
                    precioExistente = BigDecimal.ZERO;
                }
//                System.out.println("INGRESO " + i.getInventario().getCodigo() + " (i.getInventario().getIngresoEgresoRelacionado() == null) >> cantidad " + cantidadExistente + " precio " + precioExistente + " total " + totalExistente);
            } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                cant = i.getCantidad();
                precio = precioExistente.setScale(4, RoundingMode.HALF_UP);
                total = BigDecimal.valueOf(cant).multiply(precioExistente).setScale(4, RoundingMode.HALF_UP);
//                System.out.println("EGRESO " + i.getInventario().getCodigo() + " >> cantidad " + cant + " precio " + precio + " total " + total);
                cantidadExistente = cantidadExistente - cant;
                totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_UP);
//                precioExistente = BigDecimal.valueOf(totalExistente.doubleValue() / cantidadExistente).setScale(4, RoundingMode.HALF_UP);
                if (cantidadExistente > 0) {
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                }else{
                    precioExistente = BigDecimal.ZERO;
                }
//                System.out.println("EGRESO " + i.getInventario().getCodigo() + " (i.getInventario().getIngresoEgresoRelacionado() == null) >> cantidad " + cantidadExistente + " precio " + precioExistente + " total " + totalExistente);
            } else if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                cant = i.getCantidad();
                if (i.getIva() != null) {
                    if (i.getIva().compareTo(BigDecimal.ZERO) == 1) {
                        iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_UP);
                        precio = iva;
                    } else {
                        precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                    }
                } else {
                    precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                }
                total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_UP);
                cantidadExistente = cantidadExistente + cant;
                totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_UP);
                if (cantidadExistente > 0) {
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                }else{
                    precioExistente = BigDecimal.ZERO;
                }
//                System.out.println("INGRESO " + i.getInventario().getCodigo() + " (i.getInventario().getIngresoEgresoRelacionado() != null) >> cantidad " + cantidadExistente + " precio " + precioExistente + " total " + totalExistente);
            } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                InventarioItems inb = new InventarioItems();
                cant = i.getCantidad();
                if (i.getIva() != null) {
                    if (i.getIva().compareTo(BigDecimal.ZERO) == 1) {
                        iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_UP);
                        precio = iva;
                    } else {
                        precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                    }
                } else {
                    precio = i.getPrecio().setScale(4, RoundingMode.HALF_UP);
                }
                total = BigDecimal.valueOf(precio.doubleValue() * cant).setScale(4, RoundingMode.HALF_UP);
                cantidadExistente = cantidadExistente - cant;
                totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_UP);
                if (cantidadExistente > 0) {
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                }else{
                    precioExistente = BigDecimal.ZERO;
                }
//                System.out.println("EGRESO " + i.getInventario().getCodigo() + " (i.getInventario().getIngresoEgresoRelacionado() != null) >> cantidad " + cantidadExistente + " precio " + precioExistente + " total " + totalExistente);
            }
        }
        l.setTotal(totalExistente);
        l.setPrecio(precioExistente);
        l.setCantidad(cantidadExistente);
        System.out.println("valores que salen del metodo cantidad " + l.getCantidad() + " precio " + l.getPrecio() + " total " + l.getTotal());
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCantidadExistente() {
        return cantidadExistente;
    }

    public void setCantidadExistente(String cantidadExistente) {
        this.cantidadExistente = cantidadExistente;
    }

    public String getShowCuentaContable() {
        return showCuentaContable;
    }

    public void setShowCuentaContable(String showCuentaContable) {
        this.showCuentaContable = showCuentaContable;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<GrupoNiveles> getListAreas() {
        return listAreas;
    }

    public void setListAreas(List<GrupoNiveles> listAreas) {
        this.listAreas = listAreas;
    }

    public List<GrupoNiveles> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<GrupoNiveles> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public List<GrupoNiveles> getListSubgrupos() {
        return listSubgrupos;
    }

    public void setListSubgrupos(List<GrupoNiveles> listSubgrupos) {
        this.listSubgrupos = listSubgrupos;
    }

    public GrupoNiveles getAreaSelect() {
        return areaSelect;
    }

    public void setAreaSelect(GrupoNiveles areaSelect) {
        this.areaSelect = areaSelect;
    }

    public GrupoNiveles getGrupoSelect() {
        return grupoSelect;
    }

    public void setGrupoSelect(GrupoNiveles grupoSelect) {
        this.grupoSelect = grupoSelect;
    }

    public GrupoNiveles getSubgrupoSelect() {
        return SubgrupoSelect;
    }

    public void setSubgrupoSelect(GrupoNiveles SubgrupoSelect) {
        this.SubgrupoSelect = SubgrupoSelect;
    }

    public List<ContCuentas> getListContCuentas() {
        return listContCuentas;
    }

    public void setListContCuentas(List<ContCuentas> listContCuentas) {
        this.listContCuentas = listContCuentas;
    }

    public List<DetalleItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<DetalleItem> listItems) {
        this.listItems = listItems;
    }

    public ContCuentas getCuentaSelect() {
        return cuentaSelect;
    }

    public void setCuentaSelect(ContCuentas cuentaSelect) {
        this.cuentaSelect = cuentaSelect;
    }

    public DetalleItem getItemSelect() {
        return itemSelect;
    }

    public void setItemSelect(DetalleItem itemSelect) {
        this.itemSelect = itemSelect;
    }

    public List<ListadoInventarioModel> getListItemsInventario() {
        return listItemsInventario;
    }

    public void setListItemsInventario(List<ListadoInventarioModel> listItemsInventario) {
        this.listItemsInventario = listItemsInventario;
    }
//</editor-fold>

}
