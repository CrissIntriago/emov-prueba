/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AsientosContables;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FlujoEfectivo;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.AsientosContablesService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FlujoEfectivoService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.BalanceComprobacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "flujoEfectivoView")
@ViewScoped
public class FlujoEfectivoBeans implements Serializable {

    private OpcionBusqueda busqueda;
    private List<Short> listaPeriodo;
    private Date inicio;
    private Date fin;
    private Integer anterior;
    private FlujoEfectivo flujoEfectivo;
    
    @Inject
    private UserSession usser;
    @Inject
    private ServletSession ss;
    @Inject
    private AsientosContablesService asientosService;
    @Inject
    private FlujoEfectivoService flujoService;
    @Inject
    private BalanceComprobacionService balanceService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime();
        fin = new Date();
    }

    public void generarPdf(String isExcel) {
        anterior = (busqueda.getAnio().intValue() - 1);
        List<FlujoEfectivo> listFlujo;
        listFlujo = flujoService.findAll();
        if (!listFlujo.isEmpty()) {
            for (FlujoEfectivo i : listFlujo) {
                flujoService.remove(i);
            }
        }
        if (inicio == null || fin == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar el Rango de fecha.");
            return;
        }
        if (inicio.compareTo(fin) > 0) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar las fechas correctamente.");
            return;
        }
        List<AsientosContables> listEstadoF;
        long codigo = 3;
        listEstadoF = asientosService.getDatosTipoAsientoFSinGrupo(codigo);
        if (!listEstadoF.isEmpty()) {
            for (AsientosContables item : listEstadoF) {
                Map<String, BigDecimal> valores = getAniosTotales(item);
                flujoEfectivo = new FlujoEfectivo();
                flujoEfectivo.setAsientoContable(item);
                flujoEfectivo.setAnioActual(valores.get("anioActual"));
                flujoEfectivo.setAnioAnterior(valores.get("anioAnterior"));
                flujoService.create(flujoEfectivo);
            }
        }
        List<AsientosContables> listEstadoFGrupo;
        listEstadoFGrupo = asientosService.getDatosTipoAsientoFUnGrupo(codigo, "VARIACIONES NO PRESUPUESTARIAS");
        if (!listEstadoFGrupo.isEmpty()) {
            for (AsientosContables item : listEstadoFGrupo) {
                flujoEfectivo = new FlujoEfectivo();
                flujoEfectivo.setAsientoContable(item);
                flujoEfectivo.setAnioActual(getVariacionesNoPresupuestadas(item, busqueda.getAnio(), 0));
                flujoEfectivo.setAnioAnterior(getVariacionesNoPresupuestadas(item, anterior.shortValue(), 1));
                flujoService.create(flujoEfectivo);
            }
        }
        List<FlujoEfectivo> listFlujoMostrar;
        listFlujoMostrar = flujoService.findAll();
        Map<String, BigDecimal> sumas = getSumatorias(listFlujoMostrar);

        //<editor-fold defaultstate="collapsed" desc="parametros">
        ss.addParametro("anio", busqueda.getAnio());
        ss.addParametro("diaDesde", Utils.getDia(inicio));
        ss.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(inicio)));
        ss.addParametro("diaHasta", Utils.getDia(fin));
        ss.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fin)));
        ss.addParametro("usuario", usser.getNameUser());

//        Cliente jefeFinanciero = clienteService.getClienteEspecificos(RolUsuario.financiero);
//        Cliente gerente = clienteService.getClienteEspecificos(RolUsuario.maximaAutoridad);
        Distributivo jefeFinanciero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
        Distributivo gerente = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.maximaAutoridad));
        ss.addParametro("usuarioFinanciero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getNombreCompleto() : " ");
        ss.addParametro("cargoFinanciero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.financiero).getTexto());
        ss.addParametro("ciFinanciero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getIdentificacion() : " ");

        ss.addParametro("cargoGerente", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.maximaAutoridad).getTexto());
        ss.addParametro("usuarioGerente", gerente != null ? gerente.getServidorPublico().getPersona().getNombreCompleto() : " ");
        ss.addParametro("ciGerente", gerente != null ? gerente.getServidorPublico().getPersona().getIdentificacion() : " ");

        //anioActual
        ss.addParametro("fuentesOperacionalesAc", sumas.get("fuentesOperacionalesAc"));
        ss.addParametro("usosOperacionalesAc", sumas.get("usosOperacionalesAc"));
        ss.addParametro("fuentesCapitalAc", sumas.get("fuentesCapitalAc"));
        ss.addParametro("usosProduccionAc", sumas.get("usosProduccionAc"));
        ss.addParametro("fuentesFinaAc", sumas.get("fuentesFinaAc"));
        ss.addParametro("usosFinaAc", sumas.get("usosFinaAc"));
        ss.addParametro("fuentesAc", sumas.get("fuentesAc"));
        ss.addParametro("usosAc", sumas.get("usosAc"));
        ss.addParametro("variacionesAc", sumas.get("variacionesAc"));

        //anioAnterior
        ss.addParametro("fuentesOperacionalesAn", sumas.get("fuentesOperacionalesAn"));
        ss.addParametro("usosOperacionalesAn", sumas.get("usosOperacionalesAn"));
        ss.addParametro("fuentesCapitalAn", sumas.get("fuentesCapitalAn"));
        ss.addParametro("usosProduccionAn", sumas.get("usosProduccionAn"));
        ss.addParametro("fuentesFinaAn", sumas.get("fuentesFinaAn"));
        ss.addParametro("usosFinaAn", sumas.get("usosFinaAn"));
        ss.addParametro("fuentesAn", sumas.get("fuentesAn"));
        ss.addParametro("usosAn", sumas.get("usosAn"));
        ss.addParametro("variacionesAn", sumas.get("variacionesAn"));
//</editor-fold>
        ss.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        ss.setNombreReporte("estadoFlujoEfectivo");
        ss.setNombreSubCarpeta("AsientosContables");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        elegirPeriodo();
    }

    public BigDecimal getVariacionesNoPresupuestadas(AsientosContables a, Short anio, int as) {
        BigDecimal result = BigDecimal.ZERO;
        Map<String, BigDecimal> r = new HashMap<>();

        r = anioXanio(a.getCodigo(), anio, as);
//        if ("212".equals(a.getCodigo())) {
//            result = r.get("totalFinal").subtract(r.get("totalInicial"));
//        } else {
        result = r.get("totalInicial").subtract(r.get("totalFinal"));
//        }
        return result;
    }

    //enviar el año
    public Map<String, BigDecimal> anioXanio(String codigo, Short anio, int as) {
        Map<String, BigDecimal> result = new HashMap<>();
        Calendar i = Calendar.getInstance();
        Calendar f = Calendar.getInstance();
        i.setTime(inicio);
        i.add(Calendar.YEAR, -as);
        f.setTime(fin);
        f.add(Calendar.YEAR, -as);
        BigDecimal totalInicial = BigDecimal.ZERO;
        BigDecimal totalFinal = BigDecimal.ZERO;
        SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioAcum(codigo, i.getTime(), f.getTime(), anio);
        SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoAcum(codigo, i.getTime(), f.getTime(), anio, true);
        totalInicial = inicial.getSaldoDebe().subtract(inicial.getSaldoHaber());
        totalFinal = (inicial.getSaldoDebe().add(flujo.getSaldoDebe())).subtract(inicial.getSaldoHaber().add(flujo.getSaldoHaber()));
        result.put("totalInicial", totalInicial);
        result.put("totalFinal", totalFinal);
        return result;
    }

    public Map<String, BigDecimal> getSumatorias(List<FlujoEfectivo> listFlujo) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal fuentesOperacionalesAc = BigDecimal.ZERO;
        BigDecimal usosOperacionalesAc = BigDecimal.ZERO;
        BigDecimal fuentesCapitalAc = BigDecimal.ZERO;
        BigDecimal usosProduccionAc = BigDecimal.ZERO;
        BigDecimal fuentesFinaAc = BigDecimal.ZERO;
        BigDecimal usosFinaAc = BigDecimal.ZERO;
        BigDecimal fuentesAc = BigDecimal.ZERO;
        BigDecimal usosAc = BigDecimal.ZERO;
        BigDecimal variacionesAc = BigDecimal.ZERO;

        //anioAnterior//--<
        BigDecimal fuentesOperacionalesAn = BigDecimal.ZERO;
        BigDecimal usosOperacionalesAn = BigDecimal.ZERO;
        BigDecimal fuentesCapitalAn = BigDecimal.ZERO;
        BigDecimal usosProduccionAn = BigDecimal.ZERO;
        BigDecimal fuentesFinaAn = BigDecimal.ZERO;
        BigDecimal usosFinaAn = BigDecimal.ZERO;
        BigDecimal fuentesAn = BigDecimal.ZERO;
        BigDecimal usosAn = BigDecimal.ZERO;
        BigDecimal variacionesAn = BigDecimal.ZERO;

        for (FlujoEfectivo item : listFlujo) {
            switch (item.getAsientoContable().getGrupo()) {
                case "FUENTES OPERACIONALES":
                    fuentesOperacionalesAc = fuentesOperacionalesAc.add(item.getAnioActual());
                    fuentesOperacionalesAn = fuentesOperacionalesAn.add(item.getAnioAnterior());
                    break;
                case "USOS OPERACIONALES":
                    usosOperacionalesAc = usosOperacionalesAc.add(item.getAnioActual());
                    usosOperacionalesAn = usosOperacionalesAn.add(item.getAnioAnterior());
                    break;
                case "FUENTES DE CAPITAL":
                    fuentesCapitalAc = fuentesCapitalAc.add(item.getAnioActual());
                    fuentesCapitalAn = fuentesCapitalAn.add(item.getAnioAnterior());
                    break;
                case "USOS DE DE PRODUCCIÓN, INVERSIÓN Y CAPITAL":
                    usosProduccionAc = usosProduccionAc.add(item.getAnioActual());
                    usosProduccionAn = usosProduccionAn.add(item.getAnioAnterior());
                    break;
                case "FUENTES DE FINANCIAMIENTO":
                    fuentesFinaAc = fuentesFinaAc.add(item.getAnioActual());
                    fuentesFinaAn = fuentesFinaAn.add(item.getAnioAnterior());
                    break;
                case "USOS DE FINANCIAMIENTO":
                    usosFinaAc = usosFinaAc.add(item.getAnioActual());
                    usosFinaAn = usosFinaAn.add(item.getAnioAnterior());
                    break;
                case "FLUJOS NO PRESUPUESTARIOS - FUENTES":
                    fuentesAc = fuentesAc.add(item.getAnioActual());
                    fuentesAn = fuentesAn.add(item.getAnioAnterior());
                    break;
                case "USOS":
                    usosAc = usosAc.add(item.getAnioActual());
                    usosAn = usosAn.add(item.getAnioAnterior());
                    break;
                case "VARIACIONES NO PRESUPUESTARIAS":
                    variacionesAc = variacionesAc.add(item.getAnioActual());
                    variacionesAn = variacionesAn.add(item.getAnioAnterior());
                    break;
            }
        }

        //Anio Actual
        result.put("fuentesOperacionalesAc", fuentesOperacionalesAc);
        result.put("usosOperacionalesAc", usosOperacionalesAc);
        result.put("fuentesCapitalAc", fuentesCapitalAc);
        result.put("usosProduccionAc", usosProduccionAc);
        result.put("fuentesFinaAc", fuentesFinaAc);
        result.put("usosFinaAc", usosFinaAc);
        result.put("fuentesAc", fuentesAc);
        result.put("usosAc", usosAc);
        result.put("variacionesAc", variacionesAc);
        //Anio Anterior
        result.put("fuentesOperacionalesAn", fuentesOperacionalesAn);
        result.put("usosOperacionalesAn", usosOperacionalesAn);
        result.put("fuentesCapitalAn", fuentesCapitalAn);
        result.put("usosProduccionAn", usosProduccionAn);
        result.put("fuentesFinaAn", fuentesFinaAn);
        result.put("usosFinaAn", usosFinaAn);
        result.put("fuentesAn", fuentesAn);
        result.put("usosAn", usosAn);
        result.put("variacionesAn", variacionesAn);

        return result;
    }

    public Map<String, BigDecimal> getAniosTotales(AsientosContables a) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal anioActual = BigDecimal.ZERO;
        BigDecimal anioAnterior = BigDecimal.ZERO;
        Calendar i = Calendar.getInstance();
        Calendar f = Calendar.getInstance();
        i.setTime(inicio);
        i.add(Calendar.YEAR, - 1);
        f.setTime(fin);
        f.add(Calendar.YEAR, - 1);

        if ("1".equals(a.getCodigo().substring(0, 1))) {
            SaldoDebeHaberDTO saldo = balanceService.getSaldosDebeHaberFlujoEfectivo(inicio, fin, busqueda.getAnio(), a.getCodigo());
            anioActual = anioActual.add(saldo.getSaldoHaber());
            SaldoDebeHaberDTO saldoAnterior = balanceService.getSaldosDebeHaberFlujoEfectivo(i.getTime(), f.getTime(), anterior.shortValue(), a.getCodigo());
            anioAnterior = anioAnterior.add(saldoAnterior.getSaldoHaber());
//            System.out.println("codigo 1 - saldo actual:"+anioActual);
//            System.out.println("codigo 1 - saldo anterior:"+anioAnterior);
        }

        if ("2".equals(a.getCodigo().substring(0, 1))) {
            System.out.println("codigo:"+a.getCodigo());
            SaldoDebeHaberDTO saldo = balanceService.getSaldosDebeHaberFlujoEfectivo(inicio, fin, busqueda.getAnio(), a.getCodigo());
            anioActual = anioActual.add(saldo.getSaldoDebe());
            SaldoDebeHaberDTO saldoAnterior = balanceService.getSaldosDebeHaberFlujoEfectivo(i.getTime(), f.getTime(), anterior.shortValue(), a.getCodigo());
            anioAnterior = anioAnterior.add(saldoAnterior.getSaldoDebe());
//            System.out.println("codigo 2 - saldo actual:"+anioActual);
//            System.out.println("codigo 2 - saldo anterior:"+anioAnterior);
        }
        result.put("anioActual", anioActual);
        result.put("anioAnterior", anioAnterior);

        return result;
    }

    public void elegirPeriodo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime();
        fin = new Date();
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }
//</editor-fold>
}
