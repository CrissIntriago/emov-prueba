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
import com.origami.sigef.common.entities.EstadoResultado;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.AsientosContablesService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.EstadoResultadoService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.BalanceComprobacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Named(value = "estadoResultadoBeansView")
@ViewScoped
public class EstadoResultadoBeans implements Serializable {

    private OpcionBusqueda busqueda;
    private Date inicio;
    private Date fin;
    private EstadoResultado estadoResultado;
    private Integer anterior;
    private List<Short> listaPeriodo;
    
    @Inject
    private UserSession usser;
    @Inject
    private ServletSession ss;
    @Inject
    private BalanceComprobacionService balanceService;
    @Inject
    private AsientosContablesService asientosService;
    @Inject
    private EstadoResultadoService estadoRService;
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
//        System.out.println(anterior);
        List<EstadoResultado> listEstadoResultados;
        listEstadoResultados = estadoRService.findAll();
        if (!listEstadoResultados.isEmpty()) {
            for (EstadoResultado items : listEstadoResultados) {
                estadoRService.remove(items);
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
        List<AsientosContables> listAsientosContables;
        long codigo = 2;
        listAsientosContables = asientosService.getAsientosContablesByTipo(codigo);
        if (!listAsientosContables.isEmpty()) {
            for (AsientosContables item : listAsientosContables) {
                Map<String, BigDecimal> valores = getAniosTotales(item);
                estadoResultado = new EstadoResultado();
                estadoResultado.setAsientoContable(item);
                estadoResultado.setValorAnioActual(valores.get("anioActual"));
                estadoResultado.setValorAnioAnterior(valores.get("anioAnterior"));
                estadoRService.create(estadoResultado);
            }
        }

        Distributivo jefeFinanciero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
        Distributivo gerente = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.maximaAutoridad));

        ss.addParametro("anio", busqueda.getAnio());
        ss.addParametro("diaDesde", Utils.getDia(inicio));
        ss.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(inicio)));
        ss.addParametro("diaHasta", Utils.getDia(fin));
        ss.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fin)));
        ss.addParametro("usuario", usser.getNameUser());

//        ss.addParametro("usuarioFinanciero", jefeFinanciero.getServidorPublico().getPersona().getNombreCompleto());
//        ss.addParametro("cargoFinanciero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.financiero).getTexto());
//        ss.addParametro("ciFinanciero", jefeFinanciero.getServidorPublico().getPersona().getIdentificacion());

        ss.addParametro("cargoGerente", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.maximaAutoridad).getTexto());
        ss.addParametro("usuarioGerente", gerente.getServidorPublico().getPersona().getNombreCompleto());
        ss.addParametro("ciGerente", gerente.getServidorPublico().getPersona().getIdentificacion());
        ss.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(false);
        }
        ss.setNombreReporte("estadoResultado");
        ss.setNombreSubCarpeta("AsientosContables");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        elegirPeriodo();
    }

    public Map<String, BigDecimal> getAniosTotales(AsientosContables a) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal anioActual = BigDecimal.ZERO;
        BigDecimal anioAnterior = BigDecimal.ZERO;
        Map<String, BigDecimal> enviar;
        List<String> recorrer = new ArrayList<>();
        if (Utils.haveCharacter("/", a.getCodigo())) {
            List<String> codigos = new ArrayList<>();
//            System.out.println(a.getCodigo() + " entro a la consulta por el -");
            if ("62401".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("62401", "62404");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("63801".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("63801", "63804");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                codigos.add("63807");
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("63501".equals(a.getCodigo().substring(0, 5))) {
                codigos.add("63501");
                codigos.add("63504");
                codigos.add("63505");
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("62501".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("62501", "62504");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("63502".equals(a.getCodigo().substring(0, 5))) {
                codigos.add("63502");
                codigos.add("63503");
                codigos.add("63507");
                codigos.add("63508");
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("62407".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("62421", "62427");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                codigos.add("62407");
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("63808".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("63821", "63827");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                codigos.add("63808");
                codigos.add("63837");
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");

            }
            if ("63851".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("63851", "63893");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
            if ("62521".equals(a.getCodigo().substring(0, 5))) {
                recorrer = rango("62521", "62524");
                for (String rec : recorrer) {
                    codigos.add(rec);
                }
                enviar = calculateSinAc(codigos);
                anioActual = enviar.get("anioActual");
                anioAnterior = enviar.get("anioAnterior");
            }
        } else {
            enviar = calculate(a.getCodigo());
            anioActual = enviar.get("anioActual");
            anioAnterior = enviar.get("anioAnterior");
        }

        result.put("anioActual", anioActual);
        result.put("anioAnterior", anioAnterior);

        return result;

    }

    public Map<String, BigDecimal> calculate(String is) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal anioActual = BigDecimal.ZERO;
        BigDecimal debeAnioActual = BigDecimal.ZERO;
        BigDecimal haberAnioActual = BigDecimal.ZERO;
        BigDecimal anioAnterior = BigDecimal.ZERO;
        BigDecimal debeAnioAnterior = BigDecimal.ZERO;
        BigDecimal haberAnioAnterior = BigDecimal.ZERO;
        Calendar i = Calendar.getInstance();
        Calendar f = Calendar.getInstance();
        i.setTime(inicio);
        i.add(Calendar.YEAR, - 1);
        f.setTime(fin);
        f.add(Calendar.YEAR, - 1);

        //anio Actual
        SaldoDebeHaberDTO saldo = balanceService.getSaldosDebeHaberFlujoEfectivo(inicio, fin, busqueda.getAnio(), is);
        debeAnioActual = debeAnioActual.add(saldo.getSaldoDebe());
        haberAnioActual = haberAnioActual.add(saldo.getSaldoHaber());
        anioActual = haberAnioActual.subtract(debeAnioActual);

        //anio anterior
        SaldoDebeHaberDTO saldoAn = balanceService.getSaldosDebeHaberFlujoEfectivo(i.getTime(), f.getTime(), anterior.shortValue(), is);
        debeAnioAnterior = debeAnioAnterior.add(saldoAn.getSaldoDebe());
        haberAnioAnterior = haberAnioAnterior.add(saldoAn.getSaldoHaber());
        anioAnterior = haberAnioAnterior.subtract(debeAnioAnterior);

        result.put("anioActual", anioActual);
        result.put("anioAnterior", anioAnterior);

        return result;
    }

    public Map<String, BigDecimal> calculateSinAc(List<String> listaC) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal anioActual = BigDecimal.ZERO;
        BigDecimal debeAnioActual = BigDecimal.ZERO;
        BigDecimal haberAnioActual = BigDecimal.ZERO;
        BigDecimal anioAnterior = BigDecimal.ZERO;
        BigDecimal debeAnioAnterior = BigDecimal.ZERO;
        BigDecimal haberAnioAnterior = BigDecimal.ZERO;
        Calendar i = Calendar.getInstance();
        Calendar f = Calendar.getInstance();
        i.setTime(inicio);
        i.add(Calendar.YEAR, - 1);
        f.setTime(fin);
        f.add(Calendar.YEAR, - 1);

        for (String is : listaC) {

            SaldoDebeHaberDTO saldo = balanceService.getSaldosDebeHaberFlujoEfectivo(inicio, fin, busqueda.getAnio(), is);
            debeAnioActual = debeAnioActual.add(saldo.getSaldoDebe());
            haberAnioActual = haberAnioActual.add(saldo.getSaldoHaber());
            anioActual = haberAnioActual.subtract(debeAnioActual);
            //anio anterior
            SaldoDebeHaberDTO saldoAn = balanceService.getSaldosDebeHaberFlujoEfectivo(i.getTime(), f.getTime(), anterior.shortValue(), is);
            debeAnioAnterior = debeAnioAnterior.add(saldoAn.getSaldoDebe());
            haberAnioAnterior = haberAnioAnterior.add(saldoAn.getSaldoHaber());
            anioAnterior = haberAnioAnterior.subtract(debeAnioAnterior);
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

    public List<String> rango(String desde, String hasta) {
        List<String> result = new ArrayList<>();
        for (int i = Integer.parseInt(desde); i <= Integer.parseInt(hasta); i++) {
            result.add(String.valueOf(i));
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

//    public List<MasterCatalogo> getPeriodos() {
//        return periodos;
//    }
//
//    public void setPeriodos(List<MasterCatalogo> periodos) {
//        this.periodos = periodos;
//    }
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

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
//</editor-fold>

}
