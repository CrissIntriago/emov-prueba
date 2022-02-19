/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.google.gson.Gson;
import com.origami.sigef.ats.modelAts.Air;
import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.DetalleCompras;
import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.ats.modelAts.FormasPagoModel;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.VentaEstablecimiento;
import com.origami.sigef.ats.modelAts.Ventas;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaPago;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.TalonResumenModel;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Named(value = "contATSView")
@ViewScoped
public class ContATSController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private RenFacturaService renFacturaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ValoresService valoresService;

    private List<String> meses;
    private List<Short> listaPeriodo;
    private List<RenFactura> liquidacionesCompras;
    private List<TalonResumenModel> talonResumenModels;

    private OpcionBusqueda opcionBusqueda;
    private Ventas ventas;
    private Compras compras;
    private IVA ats;
    private VentaEstablecimiento ventaEstablecimiento;
    private Gson gson;

    private Integer mesSeleccionado;

    private String ruta;

    private BigDecimal totalVentas;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        ruta = valoresService.findByCodigo("RUTA_ATS");
        closeform();
    }

    private void closeform() {
        meses = Utils.getMeses();
        mesSeleccionado = Utils.getMes(new Date());
        compras = new Compras();
        compras.setDetalleCompras(new ArrayList<>());
        ventas = new Ventas();
        ventaEstablecimiento = new VentaEstablecimiento();
        ventas.setDetalleVentas(new ArrayList<>());
        ats = new IVA();
        totalVentas = BigDecimal.ZERO;
        talonResumenModels = new ArrayList<>();
        gson = new Gson();
    }

    public Integer mes(String mes) {
        return Utils.convertirLetraAMes(mes);
    }

    public void generarATS() {
        String msj = validacion();
        if (!msj.equals("")) {
            JsfUtil.addErrorMessage("AVISO!!!", msj);
            return;
        }
        liquidacionesCompras = renFacturaService.findCompras(opcionBusqueda, mesSeleccionado);
        ventas.setDetalleVentas(renFacturaService.findVentas(opcionBusqueda, mesSeleccionado));
        if (!liquidacionesCompras.isEmpty() || !ventas.getDetalleVentas().isEmpty()) {
            generacionATS();
            createVentas();
            createVentasEstablecimiento();
            if (compras.getDetalleCompras() != null && !compras.getDetalleCompras().isEmpty()) {
                ats.setCompras(compras);
                unificarDetalle(ats.getCompras());
            } else {
                createATSCabecera();
            }
            ruta = ruta + "AT-" + String.format("%02d", mesSeleccionado) + opcionBusqueda.getAnio() + ".xml";
            if (DocumentosUtil.crearArchivo(ats, ruta)) {
                descargarAts(ruta, opcionBusqueda.getAnio().intValue(), mesSeleccionado);
            } else {
                JsfUtil.addErrorMessage("", "ATS no generado");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "No existen datos de ventas o compra");
        }
        closeform();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void generacionATS() {
        if (!liquidacionesCompras.isEmpty()) {
            for (RenFactura l : liquidacionesCompras) {
                if (l.getDetalleCompras() != null && !l.getDetalleCompras().isEmpty()) {
                    l.setAts(gson.fromJson(l.getDetalleCompras(), IVA.class));
                    createCompras(l.getAts(), l);
                }
            }
        }
    }

    public void createCompras(IVA atsFromJson, RenFactura l) {
        if (atsFromJson != null) {
            createATSCabecera(atsFromJson);
            if (atsFromJson.getCompras().getDetalleCompras() != null) {
                for (DetalleCompras comprasDetalle : atsFromJson.getCompras().getDetalleCompras()) {
                    comprasDetalle.setAutRetencion1(l.getNumeroAutorizacion());
                    comprasDetalle.setFechaEmiRet1(formatoFecha(comprasDetalle.getFechaEmision()));
                    comprasDetalle.setFechaEmision(formatoFecha(comprasDetalle.getFechaEmision()));
                    comprasDetalle.setFechaRegistro(formatoFecha(comprasDetalle.getFechaEmision()));
                    compras.getDetalleCompras().add(comprasDetalle);
                }
            }
        }
    }

    private String formatoFecha(String fechaJso) {
        if (!fechaJso.contains("/")) {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = null;
            try {
                fecha = formatoDelTexto.parse(fechaJso);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return Utils.dateFormatPattern("dd/MM/yyyy", fecha);
        } else {
            return fechaJso;
        }
    }

    private void createATSCabecera(IVA atsFromJson) {
        ats.setTipoIDInformante(atsFromJson.getTipoIDInformante());
        ats.setIdInformante(atsFromJson.getIdInformante());
        ats.setRazonSocial(atsFromJson.getRazonSocial());
        ats.setAnio(atsFromJson.getAnio());
        ats.setMes(atsFromJson.getMes());
        ats.setNumEstabRuc(atsFromJson.getNumEstabRuc());
        ats.setCodigoOperativo(atsFromJson.getCodigoOperativo());
    }

    public void createVentas() {
        double suma = 0.00;
        if (!ventas.getDetalleVentas().isEmpty()) {
            for (DetalleVentas v : ventas.getDetalleVentas()) {
                //se suma los valores que sea de facturas fisicas
                if (v.getTipoEmision().equals("F")) {
                    double baseImp = 0.00;
                    double baseImponible = 0.00;
                    if (v.getBaseImpGrav() != null) {
                        baseImp = v.getBaseImpGrav().doubleValue();
                    }
                    if (v.getBaseImponible() != null) {
                        baseImponible = v.getBaseImponible().doubleValue();
                    }
                    suma = suma + baseImp + baseImponible;
                }
                v.setFormasDePago(createPagosVentas(v.getIdCliente()));
            }
            tipoIdentificacionCliente();
            ats.setVentas(ventas);
            totalVentas = new BigDecimal(suma);
            ats.setTotalVentas(totalVentas.setScale(2, RoundingMode.HALF_UP));
        }
    }

    public FormasPagoModel createPagosVentas(String identificacion) {
        List<RenFacturaPago> pagosVentas = renFacturaService.findLiquidacionCliente(identificacion, mesSeleccionado, opcionBusqueda);
        FormasPagoModel pagosModel = new FormasPagoModel();
        pagosModel.setFormaPago(new ArrayList<>());
        if (!pagosVentas.isEmpty()) {
            for (RenFacturaPago p : pagosVentas) {
                pagosModel.getFormaPago().add(p.getFormaPago().getCodigo());
            }
        }
        return pagosModel;
    }

    public void tipoIdentificacionCliente() {
        for (DetalleVentas v : ventas.getDetalleVentas()) {
            switch (v.getTpIdCliente()) {
                case "04": //RUC
                    v.setIdCliente(v.getIdCliente() + "001");
                    break;
                case "07": // OTROS
                    v.setIdCliente("999999999");
                    break;
            }
        }
    }

    public void createVentasEstablecimiento() {
        ventaEstablecimiento.setVentaEst(renFacturaService.findVentasEstablecimiento(opcionBusqueda.getAnio().intValue(), mesSeleccionado));
        ats.setVentasEstablecimiento(ventaEstablecimiento);
    }

    public void unificarDetalle(Compras compras) {
        for (DetalleCompras dt : compras.getDetalleCompras()) {
            if (dt.getAir() != null) {
                if (dt.getAir().getDetalleAir() != null) {
                    if (!dt.getAir().getDetalleAir().isEmpty()) {
                        List<String> codRetAir = new ArrayList<>();
                        List<Air.DetalleAir> temp = new ArrayList<>();
                        for (Air.DetalleAir item : dt.getAir().getDetalleAir()) {
                            if (!codRetAir.contains(item.getCodRetAir())) {
                                codRetAir.add(item.getCodRetAir());
                            }
                        }
                        for (String item : codRetAir) {
                            Air.DetalleAir aux = new Air.DetalleAir();
                            aux.setCodRetAir(item);
                            double sum_1 = 0;
                            double sum_2 = 0;
                            for (Air.DetalleAir da : dt.getAir().getDetalleAir()) {
                                if (da.getCodRetAir().equals(item)) {
                                    aux.setPorcentajeAir(da.getPorcentajeAir());
                                    sum_1 = Utils.redondearDosDecimales(sum_1 + Utils.redondearDosDecimales(da.getBaseImpAir().doubleValue()));
                                    sum_2 = Utils.redondearDosDecimales(sum_2 + Utils.redondearDosDecimales(da.getValRetAir().doubleValue()));
                                }
                            }
                            aux.setBaseImpAir(new BigDecimal(sum_1).setScale(2, RoundingMode.HALF_UP));
                            aux.setValRetAir(new BigDecimal(sum_2).setScale(2, RoundingMode.HALF_UP));
                            temp.add(aux);
                        }
                        dt.getAir().setDetalleAir(temp);
                    }
                }
            }
        }
    }

    private void createATSCabecera() {
        ats.setTipoIDInformante("R");
        ats.setIdInformante(userSession.getUsuario().getEmpresaId().getRuc());
        ats.setRazonSocial(Utils.quitarTildes(userSession.getUsuario().getEmpresaId().getNombreEntidad()));
        ats.setAnio(opcionBusqueda.getAnio().intValue());
        ats.setMes(String.format("%02d", mesSeleccionado));
        ats.setNumEstabRuc("001");
        ats.setCodigoOperativo("IVA");
        ats.setTotalVentas(ats.getTotalVentas() != null ? ats.getTotalVentas() : BigDecimal.ZERO.setScale(2));
    }

    public void descargarAts(String ruta, Integer anio, Integer mes) {
        if (!ruta.isEmpty()) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            servletSession.setNombreDocumento(ruta);
            servletSession.addParametro("download", true);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            this.opcionBusqueda.setAnio(anio.shortValue());
            this.mesSeleccionado = mes;
            generarTalonResumen();
        }
    }

    public void generarTalonResumen() {
        String msj = validacion();
        if (!msj.equals("")) {
            JsfUtil.addErrorMessage("AVISO!!!", msj);
            return;
        }
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        servletSession.addParametro("anio", opcionBusqueda.getAnio().intValue());
        servletSession.addParametro("periodo", String.format("%02d", mesSeleccionado) + "-" + opcionBusqueda.getAnio().intValue());
        if (isCargaEnCero()) {
            servletSession.setNombreSubCarpeta("_tributacion");
            servletSession.setNombreReporte("talonResumenRetencionEnCero");
        } else {
            servletSession.addParametro("mes", mesSeleccionado);
            talonResumenCompras();
            servletSession.addParametro("SUBREPORT_DIR_TRIBUTACION", JsfUtil.getRealPath("reportes/_tributacion/"));
            servletSession.setNombreSubCarpeta("_tributacion");
            servletSession.setNombreReporte("talonResumenRetencion");
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        closeform();
    }

    private Boolean isCargaEnCero() {
        liquidacionesCompras = renFacturaService.findCompras(opcionBusqueda, mesSeleccionado);
        ventas.setDetalleVentas(renFacturaService.findVentas(opcionBusqueda, mesSeleccionado));
        if (liquidacionesCompras.isEmpty() && ventas.getDetalleVentas().isEmpty()) {
            return true;
        }
        return false;
    }

    private void talonResumenCompras() {
//        talonResumenModels = liquidacionService.findAllComprasByLiquidacion(anio, mes);
        talonResumenModels = renFacturaService.findAllComprasByLiquidacion(opcionBusqueda.getAnio().intValue(), mesSeleccionado);
        TalonResumenModel tFactura = new TalonResumenModel();
        TalonResumenModel tNotaDebito = new TalonResumenModel();
        int cont = 0;
        Integer contRegistrosFactura = 0;
        Integer contRegistrosNotas = 0;
        if (!talonResumenModels.isEmpty()) {
            List<TalonResumenModel> newTalon = new ArrayList<>();
            for (TalonResumenModel m : talonResumenModels) {
                cont++;
                if (cont == 1) {
                    switch (m.getCodCompra()) {
                        case "01":
                            initTalonResumenCompra(tFactura, m);
                            break;
                        case "05":
                            initTalonResumenCompra(tNotaDebito, m);
                            break;
                    }
                }
                switch (m.getCodCompra()) {
                    case "01":
                        tFactura.setNumRegistros(contRegistrosFactura++);
                        initTalonResumenCompraSum(tFactura, m);
                        break;
                    case "05":
                        tNotaDebito.setNumRegistros(contRegistrosNotas++);
                        initTalonResumenCompraSum(tNotaDebito, m);
                        break;
                }
            }
            if (tNotaDebito.getCodCompra() != null) {
                newTalon.add(tNotaDebito);
            }
            if (tFactura.getCodCompra() != null) {
                newTalon.add(tFactura);
            }
            servletSession.addParametro("talonResumenModel", newTalon);
        }
    }

    private void initTalonResumenCompra(TalonResumenModel t, TalonResumenModel m) {
        t.setCodCompra(m.getCodCompra());
        t.setLiquidacionId(m.getLiquidacionId());
        t.setFacturaId(m.getFacturaId());
        t.setTransaccion(m.getTransaccion());
    }

    private void initTalonResumenCompraSum(TalonResumenModel t, TalonResumenModel m) {
        t.setBi_tarifa0(t.getBi_tarifa0().add(m.getBi_tarifa0()));
        t.setBi_tarifa_diferente0(t.getBi_tarifa_diferente0().add(m.getBi_tarifa_diferente0()));
        t.setBi_no_objetivoIva(t.getBi_no_objetivoIva().add(m.getBi_no_objetivoIva()));
        t.setValor_iva(t.getValor_iva().add(m.getValor_iva()));
    }

    private String validacion() {
        String result = "";
        if (opcionBusqueda.getAnio() == null) {
            result = "Debe seleccionar un periodo";
        }
        if (mesSeleccionado == null || mesSeleccionado.equals("")) {
            result = "Debe seleccionar un mes";
        }
        return result;
    }

    public CatalogoItemService getCatalogoItemService() {
        return catalogoItemService;
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Integer getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(Integer mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

}
