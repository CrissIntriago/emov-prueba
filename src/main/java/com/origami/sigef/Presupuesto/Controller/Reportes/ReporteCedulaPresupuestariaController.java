/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Model.CedulaPresupuestariaEgreso;
import com.origami.sigef.Presupuesto.Model.CedulaPresupuestariaEgresoConsolidada;
import com.origami.sigef.Presupuesto.Model.ListaItemCedulaPresupuestariaIngreso;
import com.origami.sigef.Presupuesto.Model.ReporteCedulaPresupuestariaEgreso;
import com.origami.sigef.Presupuesto.Model.ReporteCedulaPresupuestariaEgresoConsolidada;
import com.origami.sigef.Presupuesto.Model.ReporteCedulaPresupuestariaIngreso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reporteCedulaPreView")
@ViewScoped
public class ReporteCedulaPresupuestariaController implements Serializable {

    private boolean cedulaPresupuestaria;
    private boolean tipoReporte;
    private boolean partidasMovimientos;
    private Date fechadesde;
    private Date fechahasta;

    private Presupuesto presupuesto;
    private OpcionBusqueda busqueda;
    private BigDecimal totalInicial = BigDecimal.ZERO;
    private BigDecimal totalReforma = BigDecimal.ZERO;
    private BigDecimal totalCodificado = BigDecimal.ZERO;
    private BigDecimal totalReservado = BigDecimal.ZERO;
    private BigDecimal totalComprometido = BigDecimal.ZERO;
    private BigDecimal totalDevengado = BigDecimal.ZERO;
    private BigDecimal totalRecaudado = BigDecimal.ZERO;
    private BigDecimal totalSaldoPorDevengar = BigDecimal.ZERO;
    private BigDecimal totalSaldoPorComprometer = BigDecimal.ZERO;
    private BigDecimal totalSaldoPorPagar = BigDecimal.ZERO;
    private BigDecimal totalPagado = BigDecimal.ZERO;

    private List<MasterCatalogo> periodos;

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ServletSession servlet;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private ManagerService service;
    private Map<String, Object> param;
    private List<ListaItemCedulaPresupuestariaIngreso> lista;
    private List<CedulaPresupuestariaEgreso> listReporteEgresoEstructura;
    private List<CedulaPresupuestariaEgreso> listReporteTmp;
    private List<CedulaPresupuestariaEgreso> listReporteTmpClean;

    private List<CedulaPresupuestariaEgresoConsolidada> listaCedulaEgresoConsolidado;

    @PostConstruct
    public void initView() {
        presupuesto = new Presupuesto();
        busqueda = new OpcionBusqueda();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        tipoReporte = true;
        cedulaPresupuestaria = true;
        partidasMovimientos = false;
        asignarRangoPeriodos();
    }

    public void asignarRangoPeriodos() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        if (busqueda.getAnio().intValue() == Utils.getAnio(new Date()).intValue()) {
            fechahasta = new Date();
        } else {
            calendar.set(busqueda.getAnio(), 11, 31);
            fechahasta = calendar.getTime();
        }
    }

    public void ingresos() {
        if (cedulaPresupuestaria) {
            tipoReporte = true;
        }
    }

    public void calculoReporteIngreo() {
        totalInicial = BigDecimal.ZERO;
        totalReforma = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalRecaudado = BigDecimal.ZERO;
        totalSaldoPorDevengar = BigDecimal.ZERO;
        lista = new ArrayList<>();
        lista = presupuestoService.listReformaCedulaPrespupuestariaIngreso(fechadesde, fechahasta, busqueda.getAnio());
        List<ListaItemCedulaPresupuestariaIngreso> listaHijos = presupuestoService.listReformaCedulaPrespupuestariaIngreso(fechadesde, fechahasta, busqueda.getAnio());
        listaHijos = listaHijos.stream().filter(x -> x.getMovimiento() == true).collect(Collectors.toList());
        for (ListaItemCedulaPresupuestariaIngreso item : listaHijos) {
            PresCatalogoPresupuestario itemPr = service.find(PresCatalogoPresupuestario.class, item.getId().longValue());
            recalculoDefinitivo(itemPr.getPadre());
        }

        for (ListaItemCedulaPresupuestariaIngreso item : lista) {

            if (item.getPresupuesto_inicial() == null) {
                item.setPresupuesto_inicial(BigDecimal.ZERO);
            }
            if (item.getReforma() == null) {
                item.setReforma(BigDecimal.ZERO);
            }
            if (item.getCodificado() == null) {
                item.setCodificado(BigDecimal.ZERO);
            }
            if (item.getDevengado() == null) {
                item.setDevengado(BigDecimal.ZERO);
            }
            if (item.getRecaudado() == null) {
                item.setRecaudado(BigDecimal.ZERO);
            }
            if (item.getSaldoPorDevengar() == null) {
                item.setSaldoPorDevengar(BigDecimal.ZERO);
            }
            if (item.getMovimiento()) {
                totalInicial = totalInicial.add(item.getPresupuesto_inicial());
                totalReforma = totalReforma.add(item.getReforma());

                totalDevengado = totalDevengado.add(item.getDevengado());
                totalRecaudado = totalRecaudado.add(item.getRecaudado());

            }
        }

        totalCodificado = totalInicial.add(totalReforma);
        totalSaldoPorDevengar = totalCodificado.subtract(totalDevengado);
    }

    public void recalculoDefinitivo(PresCatalogoPresupuestario cp) {
        BigDecimal sumaReforma = BigDecimal.ZERO, sumaDevengado = BigDecimal.ZERO, sumaRecaudado = BigDecimal.ZERO;
        List<ListaItemCedulaPresupuestariaIngreso> hijos = new ArrayList<>();
        String codigoPadre = cp.getCodigo();
        int posicionPadre = 0;
        for (ListaItemCedulaPresupuestariaIngreso item : lista) {
            if (item.getPadre() != null) {
                if (Long.valueOf(item.getPadre().longValue()).equals(cp.getId())) {

                    if (item.getReforma() == null) {
                        item.setReforma(BigDecimal.ZERO);;
                    }
                    if (item.getDevengado() == null) {
                        item.setDevengado(BigDecimal.ZERO);
                    }
                    if (item.getRecaudado() == null) {
                        item.setRecaudado(BigDecimal.ZERO);
                    }
                    sumaReforma = sumaReforma.add(item.getReforma());

                    sumaDevengado = sumaDevengado.add(item.getDevengado());

                    sumaRecaudado = sumaRecaudado.add(item.getRecaudado());

                }
            }
        }
        for (ListaItemCedulaPresupuestariaIngreso item : lista) {
            if (item.getCodigo().equals(codigoPadre)) {
                item.setReforma(sumaReforma);
                item.setCodificado(item.getPresupuesto_inicial().add(item.getReforma()));
                item.setDevengado(sumaDevengado);
                item.setRecaudado(sumaRecaudado);
                item.setSaldoPorDevengar(item.getCodificado().subtract(item.getDevengado()));
                break;
            }

        }
        if (cp.getPadre() != null) {
            recalculoDefinitivo(cp.getPadre());
        }
    }

    public void reporteCedulaPresupuestariaEgreso(Date fechaDesde, Date fechaHasta, Short periodo) {
        listReporteTmp = new ArrayList<>();
        System.out.println("Enmpieza consulta... ");
        listReporteTmp = presupuestoService.listaCedulaPresupuestariaEgresos(fechaDesde, fechaHasta, periodo);
        System.out.println("Termina consulta... ");
        listReporteEgresoEstructura = new ArrayList<>();
        listReporteEgresoEstructura.addAll(listReporteTmp);
        totalInicial = BigDecimal.ZERO;
        totalReforma = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalReservado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalRecaudado = BigDecimal.ZERO;
        totalSaldoPorComprometer = BigDecimal.ZERO;
        totalSaldoPorDevengar = BigDecimal.ZERO;
        totalSaldoPorPagar = BigDecimal.ZERO;

        if (listReporteTmp != null) {
            for (CedulaPresupuestariaEgreso item : listReporteTmp) {
//                System.out.println("Item.getCodigo: "+item.getCodigo()+" Item.getPadre(): "+item.getPadre()+ " item.getCodigo_estructura(): "+item.getCodigo_estructura());
                integrandoPadres(item.getPadre(), item.getCodigo_estructura(), item.getDescripcion_estructura());
            }

            Collections.sort(listReporteEgresoEstructura, new Comparator<CedulaPresupuestariaEgreso>() {
                public int compare(CedulaPresupuestariaEgreso obj1, CedulaPresupuestariaEgreso obj2) {
                    int c;
                    c = obj1.getCodigo_estructura().compareTo(obj2.getCodigo_estructura());
                    if (c == 0) {
                        c = obj1.getCodigo().compareTo(obj2.getCodigo());
                    }
                    return c;
                }
            });

        }

        for (CedulaPresupuestariaEgreso data : listReporteTmp) {
            sumaArbolPadres(data.getPadre(), data.getCodigo_estructura());

        }

        for (CedulaPresupuestariaEgreso data : listReporteTmp) {
            if (data.isMovimiento()) {
                if (data.getTotal_presupuesto_inicial() == null) {
                    data.setTotal_presupuesto_inicial(BigDecimal.ZERO);
                }
                totalInicial = totalInicial.add(data.getTotal_presupuesto_inicial());
                if (data.getReforma() == null) {
                    data.setReforma(BigDecimal.ZERO);
                }
                totalReforma = totalReforma.add(data.getReforma());
                if (data.getCodificado() == null) {
                    data.setCodificado(BigDecimal.ZERO);
                }
                totalCodificado = totalCodificado.add(data.getCodificado());
                if (data.getReservas() == null) {
                    data.setReservas(BigDecimal.ZERO);
                }
                totalReservado = totalReservado.add(data.getReservas());
                if (data.getComprometido() == null) {
                    data.setComprometido(BigDecimal.ZERO);
                }
                totalComprometido = totalComprometido.add(data.getComprometido());
                if (data.getDevengado() == null) {
                    data.setDevengado(BigDecimal.ZERO);
                }
                totalDevengado = totalDevengado.add(data.getDevengado());

                if (data.getPagado() == null) {
                    data.setPagado(BigDecimal.ZERO);
                }
                totalRecaudado = totalRecaudado.add(data.getPagado());

                if (data.getSaldo_xcomprometer() == null) {
                    data.setSaldo_xcomprometer(BigDecimal.ZERO);
                }
                totalSaldoPorComprometer = totalSaldoPorComprometer.add(data.getSaldo_xcomprometer());
                if (data.getSaldo_xdevengar() == null) {
                    data.setSaldo_xdevengar(BigDecimal.ZERO);
                }

                totalSaldoPorDevengar = totalSaldoPorDevengar.add(data.getSaldo_xdevengar());
                if (data.getSaldo_xpagar() == null) {
                    data.setSaldo_xpagar(BigDecimal.ZERO);
                }
                totalSaldoPorPagar = totalSaldoPorPagar.add(data.getSaldo_xpagar());

            }

        }

    }

    public void sumaArbolPadres(BigInteger idPadre, String estructura) {
        PresCatalogoPresupuestario pres = service.find(PresCatalogoPresupuestario.class, idPadre.longValue());
        BigDecimal inicial = BigDecimal.ZERO;
        BigDecimal reforma = BigDecimal.ZERO;
        BigDecimal codificado = BigDecimal.ZERO;
        BigDecimal reservado = BigDecimal.ZERO;
        BigDecimal comprometido = BigDecimal.ZERO;
        BigDecimal devengado = BigDecimal.ZERO;
        BigDecimal pagado = BigDecimal.ZERO;
        BigDecimal saldoComprometido = BigDecimal.ZERO;
        BigDecimal saldoDevengado = BigDecimal.ZERO;
        BigDecimal saldoPagado = BigDecimal.ZERO;

        for (CedulaPresupuestariaEgreso item : listReporteEgresoEstructura) {
            if (item.getPadre() != null) {
                if (Long.valueOf(item.getPadre().longValue()).equals(pres.getId())) {
                    inicial = inicial.add(item.getTotal_presupuesto_inicial());
                    reforma = reforma.add(item.getReforma());
                    codificado = codificado.add(item.getCodificado());
                    reservado = reservado.add(item.getReservas());
                    comprometido = comprometido.add(item.getComprometido());
                    devengado = devengado.add(item.getDevengado());
                    pagado = pagado.add(item.getPagado());
                    saldoComprometido = saldoComprometido.add(item.getSaldo_xcomprometer());
                    saldoDevengado = saldoDevengado.add(item.getSaldo_xdevengar());
                    saldoPagado = saldoPagado.add(item.getSaldo_xpagar());
                }
            }
        }

        for (CedulaPresupuestariaEgreso item : listReporteEgresoEstructura) {
            if (item.getCodigo().equals(pres.getCodigo()) && item.getCodigo_estructura().equals(estructura)) {
                item.setTotal_presupuesto_inicial(inicial);
                item.setReforma(reforma);
                item.setCodificado(codificado);
                item.setReservas(reservado);
                item.setComprometido(comprometido);
                item.setDevengado(devengado);
                item.setPagado(pagado);
                item.setSaldo_xcomprometer(saldoComprometido);
                item.setSaldo_xdevengar(saldoDevengado);
                item.setSaldo_xpagar(saldoPagado);
            }
        }

        if (pres.getPadre() != null) {
            sumaArbolPadres(BigInteger.valueOf(pres.getPadre().getId().longValue()), estructura);
        }

    }

    public void integrandoPadres(BigInteger padre, String codEstructuraTmp, String estructuraTmp) {
        PresCatalogoPresupuestario pres = service.find(PresCatalogoPresupuestario.class, padre.longValue());
        String estructura = estructuraTmp;
        String codigo = codEstructuraTmp;
        boolean bandera = false;
        if (pres != null) {
            BigInteger padreTmp;
            if (pres.getPadre() != null) {
                padreTmp = BigInteger.valueOf(pres.getPadre().getId());
            } else {
                padreTmp = null;
            }
            CedulaPresupuestariaEgreso tmp = new CedulaPresupuestariaEgreso(pres.getCodigo(), pres.getDescripcion(), codigo, estructura, pres.getMovimiento(), padreTmp, pres.getConfId().getNivel());

            for (CedulaPresupuestariaEgreso item : listReporteEgresoEstructura) {
                if (item.getCodigo().equals(tmp.getCodigo()) && item.getCodigo_estructura().equals(codigo)) {
                    bandera = true;
                    break;
                }
            }
            if (!bandera) {
                listReporteEgresoEstructura.add(tmp);
            }

            if (pres.getPadre() != null) {
                integrandoPadres(BigInteger.valueOf(pres.getPadre().getId()), codigo, estructura);
            }

        }
    }

    public void reporteCedulaPresupuestariaEgresoConsolidado(Date fechaDesde, Date fechaHasta, Short periodo) {
        totalInicial = BigDecimal.ZERO;
        totalReforma = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalRecaudado = BigDecimal.ZERO;
        totalPagado = BigDecimal.ZERO;
        totalReservado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSaldoPorDevengar = BigDecimal.ZERO;
        totalSaldoPorComprometer = BigDecimal.ZERO;
        totalSaldoPorPagar = BigDecimal.ZERO;

        listaCedulaEgresoConsolidado = new ArrayList<>();
        listaCedulaEgresoConsolidado = presupuestoService.listaCedulaPresupuestariaEgresosConsolidado(fechaDesde, fechaHasta, periodo);

        List<CedulaPresupuestariaEgresoConsolidada> listaHijos = listaCedulaEgresoConsolidado.stream().filter(x -> x.isMovimiento() == true).collect(Collectors.toList());

        for (CedulaPresupuestariaEgresoConsolidada item : listaHijos) {
            if (item.getPadre() != null) {
                sumaArbolEgresoConsolidado(item.getPadre());
            }
        }

        for (CedulaPresupuestariaEgresoConsolidada item : listaHijos) {
            if (item.getPresupuestInicial() == null) {
                item.setPresupuestInicial(BigDecimal.ZERO);
            }
            totalInicial = totalInicial.add(item.getPresupuestInicial());
            if (item.getReforma() == null) {
                item.setReforma(BigDecimal.ZERO);
            }
            totalReforma = totalReforma.add(item.getReforma());

            if (item.getCodificado() == null) {
                item.setCodificado(BigDecimal.ZERO);
            }
            totalCodificado = totalCodificado.add(item.getCodificado());
            if (item.getDevengado() == null) {
                item.setDevengado(BigDecimal.ZERO);
            }
            totalDevengado = totalDevengado.add(item.getDevengado());

            if (item.getRecaudado() == null) {
                item.setRecaudado(BigDecimal.ZERO);
            }
            totalRecaudado = totalRecaudado.add(item.getRecaudado());

            if (item.getReservas() == null) {
                item.setReservas(BigDecimal.ZERO);
            }
            totalReservado = totalReservado.add(item.getReservas());
            if (item.getComprometido() == null) {
                item.setComprometido(BigDecimal.ZERO);
            }
            totalComprometido = totalComprometido.add(item.getComprometido());
            if (item.getPagado() == null) {
                item.setPagado(BigDecimal.ZERO);
            }
            totalPagado = totalPagado.add(item.getPagado());
            if (item.getSaldoXcomprometer() == null) {
                item.setSaldoXcomprometer(BigDecimal.ZERO);
            }
            totalSaldoPorComprometer = totalSaldoPorComprometer.add(item.getSaldoXcomprometer());
            if (item.getSaldoXdevengar() == null) {
                item.setSaldoXdevengar(BigDecimal.ZERO);
            }
            totalSaldoPorDevengar = totalSaldoPorDevengar.add(item.getSaldoXdevengar());

            if (item.getSaldoXPagar() == null) {
                item.setSaldoXPagar(BigDecimal.ZERO);
            }
            totalSaldoPorPagar = totalSaldoPorPagar.add(item.getSaldoXPagar());
        }

    }

    public void sumaArbolEgresoConsolidado(BigInteger idPadre) {
        BigDecimal presupuestInicial = BigDecimal.ZERO;
        BigDecimal reforma = BigDecimal.ZERO;
        BigDecimal codificado = BigDecimal.ZERO;
        BigDecimal devengado = BigDecimal.ZERO;
        BigDecimal recaudado = BigDecimal.ZERO;
        BigDecimal reservas = BigDecimal.ZERO;
        BigDecimal comprometido = BigDecimal.ZERO;
        BigDecimal pagado = BigDecimal.ZERO;
        BigDecimal saldoXcomprometer = BigDecimal.ZERO;
        BigDecimal saldoXdevengar = BigDecimal.ZERO;
        BigDecimal saldoXPagar = BigDecimal.ZERO;

        PresCatalogoPresupuestario pres = service.find(PresCatalogoPresupuestario.class, idPadre.longValue());

        for (CedulaPresupuestariaEgresoConsolidada item : listaCedulaEgresoConsolidado) {
            if (item.getPadre() != null) {
                if (Long.valueOf(item.getPadre().longValue()).equals(Long.valueOf(idPadre.longValue()))) {
                    if (item.getPresupuestInicial() == null) {
                        item.setPresupuestInicial(BigDecimal.ZERO);
                    }
                    presupuestInicial = presupuestInicial.add(item.getPresupuestInicial());
                    if (item.getReforma() == null) {
                        item.setReforma(BigDecimal.ZERO);
                    }
                    reforma = reforma.add(item.getReforma());

                    if (item.getCodificado() == null) {
                        item.setCodificado(BigDecimal.ZERO);
                    }
                    codificado = codificado.add(item.getCodificado());
                    if (item.getDevengado() == null) {
                        item.setDevengado(BigDecimal.ZERO);
                    }
                    devengado = devengado.add(item.getDevengado());

                    if (item.getRecaudado() == null) {
                        item.setRecaudado(BigDecimal.ZERO);
                    }
                    recaudado = recaudado.add(item.getRecaudado());

                    if (item.getReservas() == null) {
                        item.setReservas(BigDecimal.ZERO);
                    }
                    reservas = reservas.add(item.getReservas());
                    if (item.getComprometido() == null) {
                        item.setComprometido(BigDecimal.ZERO);
                    }
                    comprometido = comprometido.add(item.getComprometido());
                    if (item.getPagado() == null) {
                        item.setPagado(BigDecimal.ZERO);
                    }
                    pagado = pagado.add(item.getPagado());
                    if (item.getSaldoXcomprometer() == null) {
                        item.setSaldoXcomprometer(BigDecimal.ZERO);
                    }
                    saldoXcomprometer = saldoXcomprometer.add(item.getSaldoXcomprometer());
                    if (item.getSaldoXdevengar() == null) {
                        item.setSaldoXdevengar(BigDecimal.ZERO);
                    }
                    saldoXdevengar = saldoXdevengar.add(item.getSaldoXdevengar());

                    if (item.getSaldoXPagar() == null) {
                        item.setSaldoXPagar(BigDecimal.ZERO);
                    }
                    saldoXPagar = saldoXPagar.add(item.getSaldoXPagar());

                }
            }
        }

        for (CedulaPresupuestariaEgresoConsolidada item : listaCedulaEgresoConsolidado) {
            if (item.getCodigo().equals(pres.getCodigo())) {
                item.setPresupuestInicial(presupuestInicial);
                item.setReforma(reforma);
                item.setCodificado(codificado);
                item.setReservas(reservas);
                item.setComprometido(comprometido);
                item.setDevengado(devengado);
                item.setPagado(pagado);
                item.setRecaudado(recaudado);
                item.setSaldoXPagar(saldoXPagar);
                item.setSaldoXcomprometer(saldoXcomprometer);
                item.setSaldoXdevengar(saldoXdevengar);
            }
        }

        if (pres.getPadre() != null) {
            sumaArbolEgresoConsolidado(BigInteger.valueOf(pres.getPadre().getId()));
        }

    }

    public void imprimir(String isExcel) throws InterruptedException {
        servlet.borrarDatos();
        servlet.instanciarParametros();
        servlet.addParametro("fecha_desde", fechadesde);
        servlet.addParametro("fecha_hasta", fechahasta);
        servlet.addParametro("diaDesde", Utils.getDia(fechadesde));
        servlet.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(fechadesde)));
        servlet.addParametro("diaHasta", Utils.getDia(fechahasta));
        servlet.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fechahasta)));
        servlet.addParametro("per", busqueda.getAnio());
        servlet.setContentType(isExcel);
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        servlet.addParametro("ci_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_presupuesto", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servlet.addParametro("ci_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_director", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
            servlet.setIsIgnorePaginator(true);
        }
        servlet.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        if (cedulaPresupuestaria) {

            calculoReporteIngreo();
            List<ReporteCedulaPresupuestariaIngreso> result = new ArrayList<>();
//            if (partidasMovimientos) {
//                lista = lista.stream().filter(x -> x.getMovimiento() == true).collect(Collectors.toList());
//            }
            result.add(new ReporteCedulaPresupuestariaIngreso(totalInicial, totalReforma, totalCodificado, totalDevengado, totalRecaudado, totalSaldoPorDevengar, lista));
            servlet.setDataSource(result);
            servlet.setNombreReporte("ingresoCedulaPresupuestaria");
            servlet.setNombreSubCarpeta("reportesPresupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            limpiar();
        } else {
            if (tipoReporte) {
                reporteCedulaPresupuestariaEgreso(fechadesde, fechahasta, busqueda.getAnio());
                List<ReporteCedulaPresupuestariaEgreso> result = new ArrayList<>();
                result.add(new ReporteCedulaPresupuestariaEgreso(totalInicial, totalReforma, totalCodificado, totalReservado, totalComprometido, totalDevengado, totalRecaudado, totalSaldoPorComprometer, totalSaldoPorDevengar, totalSaldoPorPagar, listReporteEgresoEstructura));
                servlet.setDataSource(result);
                servlet.setNombreReporte("reporteCedulaPresupuestariEgresoDatasoruce");
                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                limpiar();
            } else {
                reporteCedulaPresupuestariaEgresoConsolidado(fechadesde, fechahasta, busqueda.getAnio());
                List<ReporteCedulaPresupuestariaEgresoConsolidada> result = new ArrayList<>();
                result.add(new ReporteCedulaPresupuestariaEgresoConsolidada(totalInicial, totalReforma, totalCodificado, totalDevengado, totalRecaudado, totalReservado, totalComprometido, totalPagado, totalSaldoPorComprometer, totalSaldoPorDevengar, totalSaldoPorPagar, listaCedulaEgresoConsolidado));
                servlet.setDataSource(result);
                servlet.setNombreReporte("cedulaPresupuestariaEgresoConsolidadoDataSource");
                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                limpiar();
            }

        }

    }

    public void limpiar() {
        busqueda = new OpcionBusqueda();
        tipoReporte = true;
        cedulaPresupuestaria = true;
        asignarRangoPeriodos();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public boolean isCedulaPresupuestaria() {
        return cedulaPresupuestaria;
    }

    public void setCedulaPresupuestaria(boolean cedulaPresupuestaria) {
        this.cedulaPresupuestaria = cedulaPresupuestaria;
    }

    public boolean isTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(boolean tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public boolean isPartidasMovimientos() {
        return partidasMovimientos;
    }

    public void setPartidasMovimientos(boolean partidasMovimientos) {
        this.partidasMovimientos = partidasMovimientos;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }
//</editor-fold>

}
