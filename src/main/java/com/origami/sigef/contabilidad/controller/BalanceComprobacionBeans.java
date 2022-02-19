/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.BalanceComprobacion;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.BalanceComprobacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "balanceComprobacionView")
@ViewScoped
public class BalanceComprobacionBeans implements Serializable {

    private OpcionBusqueda busqueda;
    private List<Short> listaPeriodo;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;
    @Inject
    private BalanceComprobacionService balanceService;
    private BalanceComprobacion balanceComprobación;
    @Inject
    private UserSession usser;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;

    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private PlanCuentasService planCuentasService;
    private List<PlanCuentas> listNiveles;
    private PlanCuentas nivel;
    private Date inicio;
    private Date fin;
    private boolean catalogo;
    private List<String> codigosNoFound;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        listNiveles = planCuentasService.getNivelesList(CONFIG.PLAN_CUENTA_CONTABLE, false);
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime(); // new Date("01/01/" + busqueda.getAnio());
        fin = new Date();
        balanceComprobación = new BalanceComprobacion();
        catalogo = false;
    }

    public void generarPdf(String isExcel) {
        codigosNoFound = new ArrayList<>();
        balanceService.deleteAll();
        if (inicio == null || fin == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar el Rango de fecha.");
            return;
        }
        if (inicio.compareTo(fin) > 0) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar las fechas correctamente.");
            return;
        }
        Calendar cInicio = Calendar.getInstance();
        Calendar cFin = Calendar.getInstance();
        cInicio.setTime(inicio);
        cFin.setTime(fin);
        if ((cInicio.get(Calendar.YEAR)) != busqueda.getAnio().intValue() || cFin.get(Calendar.YEAR) != busqueda.getAnio().intValue()) {
            JsfUtil.addWarningMessage("Advertencia", " Verifique que el rango de Fechas este dentro del año correspondiente.");
            return;
        }
//<editor-fold defaultstate="collapsed" desc="metodos de catalogo">
        if (catalogo == true) {
            ////detalleTransacción
            List<ContCuentas> lisDetalleCT;
            lisDetalleCT = balanceService.getCuentasIfOrNotGobierno(inicio, fin, busqueda.getAnio(), true);
            if (!lisDetalleCT.isEmpty()) {
                for (ContCuentas item : lisDetalleCT) {
                    System.out.println("--------------------------------------------");
                    System.out.println("Item: " + item.getCodigo());
                    SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioNoAcum(item.getCodigo(), inicio, fin, busqueda.getAnio());
                    SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoNoAcum(item.getCodigo(), inicio, fin, busqueda.getAnio(), false);
                    System.out.println("inicial >> inicial.getSaldoDebe(): " + inicial.getSaldoDebe() + " inicial.getSaldoHaber():" + inicial.getSaldoHaber());
                    System.out.println("flujo >> flujo.getSaldoDebe(): " + flujo.getSaldoDebe() + " flujo.getSaldoHaber():" + flujo.getSaldoHaber());
                    balanceComprobación.setCuentaContable(item);
                    balanceComprobación.setSaldoInicialDebe(inicial.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setSaldoInicialHaber(inicial.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setFlujoDebe(flujo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setFlujoHaber(flujo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                    balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                    System.out.println("setAcumuladoDebe: " + balanceComprobación.getAcumuladoDebe() + " setAcumuladoHaber: " + balanceComprobación.getAcumuladoHaber());
                    System.out.println("------");
                    balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                    balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
                    balanceService.create(balanceComprobación);
                    balanceComprobación = new BalanceComprobacion();
                }
            }
            //agregando cuentas no gobierno y buscando el catalogo
            List<ContCuentas> listDetalleCTnoCatalogo;
            List<ContCuentas> listDetalleCTA = new ArrayList<>();
            listDetalleCTnoCatalogo = balanceService.getCuentasIfOrNotGobierno(inicio, fin, busqueda.getAnio(), false);
            if (!listDetalleCTnoCatalogo.isEmpty()) {
                for (ContCuentas item : listDetalleCTnoCatalogo) {
                    ContCuentas recorrer;
                    recorrer = getCuentaGobierno(item);
                    if (recorrer != null) {
                        boolean con = false;
                        boolean var = false;
                        for (ContCuentas i : lisDetalleCT) {
                            if (recorrer.getId() != null && recorrer.getCodigo().equals(i.getCodigo())) {
                                con = true;
                            }
                        }
                        for (int i = 0; i < listDetalleCTA.size(); i++) {
                            System.out.println("Cta Gobierno: "+recorrer.getCodigo());
                            if (recorrer.getId() != null && recorrer.getCodigo().equals(listDetalleCTA.get(i).getCodigo())) {
                                var = true;
                            }
                        }
                        if (con == false && var == false) {
                            listDetalleCTA.add(recorrer);
                        }
                    } else {
                        codigosNoFound.add(item.getCodigo());
                    }
                }
            }

            if (!listDetalleCTA.isEmpty()) {
                for (ContCuentas item : listDetalleCTA) {
                    SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioAcum(item.getCodigo(), inicio, fin, busqueda.getAnio());
                    SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoAcum(item.getCodigo(), inicio, fin, busqueda.getAnio(), false);
                    balanceComprobación.setCuentaContable(item);
                    System.out.println("item: " + item.getCodigo() + " id " + item.getId());
                    System.out.println("inicial " + inicial.toString());
                    System.out.println("flujo " + flujo.toString());
                    balanceComprobación.setSaldoInicialDebe(totalDebeInicial(inicial).setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setSaldoInicialHaber(totalHaberInicial(inicial).setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setFlujoDebe(flujo != null ? flujo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    balanceComprobación.setFlujoHaber(flujo != null ? flujo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                    balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                    System.out.println("getAcumuladoDebe: " + balanceComprobación.getAcumuladoDebe() + " getAcumuladoHaber: " + balanceComprobación.getAcumuladoHaber());
                    System.out.println("getAcumuladoDebe: " + balanceComprobación.getAcumuladoDebe().setScale(2, RoundingMode.HALF_UP) + " getAcumuladoHaber: " + balanceComprobación.getAcumuladoHaber().setScale(2, RoundingMode.HALF_UP));
                    System.out.println("------");
                    balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                    balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
                    balanceService.create(balanceComprobación);
                    balanceComprobación = new BalanceComprobacion();
                }
            }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="nivel">
        } else {

            if (nivel == null) {
                JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar el nivel para el Balance.");
                return;
            }
            List<String> listCuentas;
            listCuentas = balanceService.getAllCuentasContablesPlus(sizeNivel(nivel), inicio, fin, busqueda.getAnio());
            if (!listCuentas.isEmpty()) {
                for (String item : listCuentas) {
                    System.out.println("item: " + item);
                    SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioAcum(item, inicio, fin, busqueda.getAnio());
                    SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoAcum(item, inicio, fin, busqueda.getAnio(), false);
//                    CuentaContable c = cuentaService.findCuentaContableByCodigoAndPerido(item, busqueda.getAnio());
                    ContCuentas c = contCuentasService.findContCuentasByCodigo(item);
                    balanceComprobación.setCuentaContable(c);
                    balanceComprobación.setSaldoInicialDebe(inicial.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setSaldoInicialHaber(inicial.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
                    balanceComprobación.setFlujoDebe(flujo != null ? flujo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    balanceComprobación.setFlujoHaber(flujo != null ? flujo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                    balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                    balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                    balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
                    balanceService.create(balanceComprobación);
                    balanceComprobación = new BalanceComprobacion();
                }
            }
//</editor-fold>
        }
        //<editor-fold defaultstate="collapsed" desc="Parametros para el reporte">
//        Cliente jefeFinanciero = clienteService.getClienteEspecificos(RolUsuario.financiero);
//        Cliente contador = clienteService.getClienteEspecificos(RolUsuario.contador);
//        Distributivo jefeFinanciero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
//        Distributivo contador = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.contador));
        ss.addParametro("anio", busqueda.getAnio());
        ss.addParametro("diaDesde", Utils.getDia(inicio));
        ss.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(inicio)));
        ss.addParametro("diaHasta", Utils.getDia(fin));
        ss.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fin)));
        ss.addParametro("usuario", usser.getNameUser());

//        ss.addParametro("jefeFinanciero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.financiero).getTexto());
//        ss.addParametro("ci_financiero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getIdentificacion() : "");
//        ss.addParametro("nombre_financiero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getNombreCompleto() : "");
//
//        ss.addParametro("contador", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.contador).getTexto());
//        ss.addParametro("ci_contador", contador != null ? contador.getServidorPublico().getPersona().getIdentificacion() : "");
//        ss.addParametro("nombre_contador", contador != null ? contador.getServidorPublico().getPersona().getNombreCompleto() : "");
        ss.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(false);
            ss.setIsIgnorePaginator(true);
        }
        ss.setNombreReporte("balanceComprobacion");
        ss.setNombreSubCarpeta("AsientosContables");
//</editor-fold>
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        elegirPeriodo();
        if (!codigosNoFound.isEmpty()) {
            for (String p : codigosNoFound) {
                JsfUtil.addWarningMessage("Advertencia", " La cuenta " + p + " no está configurada correctamente.");
            }
        }

    }

    public ContCuentas getCuentaGobierno(ContCuentas c) {
        ContCuentas resultCuenta = null;
        if (c.getGobierno() == true) {
            return c;
        } else {
            Integer i = c.getCodigo().length();
            for (int j = 1; j <= i; j++) {
                ContCuentas cuenta = contCuentasService.findContCuentasByCodigo(c.getCodigo().substring(0, i - j));
                if (cuenta != null) {
                    if (cuenta.getGobierno() == true) {
                        resultCuenta = cuenta;
                        break;
                    }
                }
            }
            if (resultCuenta == null) {
                System.out.println("cuenta hija " + c.getCodigo());
            }
            return resultCuenta;
        }
    }

    public BigDecimal totalDebeInicial(SaldoDebeHaberDTO saldo) {
        BigDecimal result;
        saldo.setSaldoDebe(saldo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
        saldo.setSaldoHaber(saldo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("D Debe Ini: " + saldo.getSaldoDebe() + " Haber Ini: " + saldo.getSaldoHaber());
        result = saldo.getSaldoDebe().subtract(saldo.getSaldoHaber());
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalHaberInicial(SaldoDebeHaberDTO saldo) {
        BigDecimal result;
        saldo.setSaldoDebe(saldo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
        saldo.setSaldoHaber(saldo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("H Debe Ini: " + saldo.getSaldoDebe() + " Haber Ini: " + saldo.getSaldoHaber());
        result = saldo.getSaldoDebe().subtract(saldo.getSaldoHaber());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            return result.abs();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalDebe(BalanceComprobacion b) {
        BigDecimal result;
        b.setAcumuladoDebe(b.getAcumuladoDebe().setScale(2, RoundingMode.HALF_UP));
        b.setAcumuladoHaber(b.getAcumuladoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("D b.getAcumuladoDebe(): " + b.getAcumuladoDebe());
        System.out.println("D b.getAcumuladoHaber(): " + b.getAcumuladoHaber());
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        System.out.println("D Result: " + result);
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalHaber(BalanceComprobacion b) {
        BigDecimal result;
         b.setAcumuladoDebe(b.getAcumuladoDebe().setScale(2, RoundingMode.HALF_UP));
        b.setAcumuladoHaber(b.getAcumuladoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("H b.getAcumuladoDebe(): " + b.getAcumuladoDebe());
        System.out.println("H b.getAcumuladoHaber(): " + b.getAcumuladoHaber());
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        System.out.println("H Result: " + result);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            return result.abs();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void elegirPeriodo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime(); // new Date("01/01/" + busqueda.getAnio());
        fin = new Date();
        nivel = null;
        catalogo = false;
    }

    public Integer sizeNivel(PlanCuentas cuentas) {
        Integer result;
        result = listNiveles.stream().filter(x -> x.getNivel() <= cuentas.getNivel()).mapToInt(x -> x.getNumDigito()).sum();
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<PlanCuentas> getListNiveles() {
        return listNiveles;
    }

    public void setListNiveles(List<PlanCuentas> listNiveles) {
        this.listNiveles = listNiveles;
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

    public boolean isCatalogo() {
        return catalogo;
    }

    public void setCatalogo(boolean catalogo) {
        this.catalogo = catalogo;
    }

    public PlanCuentas getNivel() {
        return nivel;
    }

    public void setNivel(PlanCuentas nivel) {
        this.nivel = nivel;
    }
//</editor-fold>

}
