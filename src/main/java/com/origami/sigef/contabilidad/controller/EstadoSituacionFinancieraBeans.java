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
import com.origami.sigef.common.entities.BalanceComprobacion;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.EstadoSituacionFinanciera;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.AsientosContablesService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.EstadoSituacionFinancieraService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "estadoSituacionFinancieraView")
@ViewScoped
public class EstadoSituacionFinancieraBeans implements Serializable {

    private OpcionBusqueda busqueda;
    private List<Short> listaPeriodo;
    @Inject
    private ServletSession ss;
    @Inject
    private BalanceComprobacionService balanceService;
    private BalanceComprobacion balanceComprobación;
    @Inject
    private UserSession usser;
    @Inject
    private CuentaContableService cuentaService;
    @Inject
    private AsientosContablesService asientosService;
    @Inject
    private EstadoSituacionFinancieraService estadoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private PlanCuentasService planCuentasService;
    @Inject
    private ContCuentasService contCuentasService;

    private EstadoSituacionFinanciera estadoFinanciero;

    private Date inicio;
    private Date fin;
    private List<BalanceComprobacion> listBalance = new ArrayList<>();
    private List<EstadoSituacionFinanciera> listEstadoSituacionFinanciero;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime();
        fin = new Date();
        balanceComprobación = new BalanceComprobacion();
    }

    public void generarPdf(String isExcel) {
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
        List<String> lisDetalleCT;
        System.out.println("sizeNivel: "+sizeNivel());
        lisDetalleCT = balanceService.getAllCuentasContablesPlus(sizeNivel(), inicio, fin, busqueda.getAnio());
        if (!lisDetalleCT.isEmpty()) {
            listBalance = new ArrayList<>();
            for (String item : lisDetalleCT) {
                SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioAcum(item, inicio, fin, busqueda.getAnio());
                SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoAcum(item, inicio, fin, busqueda.getAnio(), false);
//                CuentaContable c = cuentaService.findCuentaContableByCodigoAndPerido(item, busqueda.getAnio());
                ContCuentas c = contCuentasService.findContCuentasByCodigo(item);
                balanceComprobación.setCuentaContable(c);
                balanceComprobación.setSaldoInicialDebe(inicial.getSaldoDebe());
                balanceComprobación.setSaldoInicialHaber(inicial.getSaldoHaber());
                balanceComprobación.setFlujoDebe(flujo != null ? flujo.getSaldoDebe() : BigDecimal.ZERO);
                balanceComprobación.setFlujoHaber(flujo != null ? flujo.getSaldoHaber() : BigDecimal.ZERO);
                balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
//                balanceService.create(balanceComprobación);
//                System.out.println("Codigo:" + c.getCodigo() + " || inicialDebe:" + balanceComprobación.getSaldoInicialDebe() + " || inicialHaber:" + balanceComprobación.getSaldoInicialHaber() + " || flujoDebe:" + balanceComprobación.getFlujoDebe() + " || flujohaber:" + balanceComprobación.getFlujoHaber()
//                        + " || AcumDebe:" + balanceComprobación.getAcumuladoDebe() + " || AcumHaber:" + balanceComprobación.getAcumuladoHaber() + " || TotalDebe:" + balanceComprobación.getTotalDebe() + " || TotalHaber:" + balanceComprobación.getTotalHaber());
                listBalance.add(balanceComprobación);
                balanceComprobación = new BalanceComprobacion();

            }
        }
        List<AsientosContables> listEstadoF;
        long codigo = 1;
        listEstadoF = asientosService.getAsientosContablesByTipo(codigo);

        estadoService.deleteAll();
        listEstadoSituacionFinanciero = new ArrayList<>();
        if (!listEstadoF.isEmpty()) {
            for (AsientosContables i : listEstadoF) {
                Map<String, BigDecimal> result = calcularAnios(i);
                estadoFinanciero = new EstadoSituacionFinanciera();
                estadoFinanciero.setAsientoContable(i);
                estadoFinanciero.setValorAnioActual(result.get("anioActual"));
                estadoFinanciero.setValorAnioAnterior(result.get("anioAnterior"));
//                estadoService.create(estadoFinanciero);
                listEstadoSituacionFinanciero.add(estadoFinanciero);
            }
        }
        /**
         * Suma los valores de cuentas hijas en cuentas padre ejemplo si hay 141
         * y 14199, los valores de 14199 se suman en 141
         */
//        System.out.println("listEstadoSituacionfinanciero:"+listEstadoSituacionFinanciero.toString());
        if (!listEstadoSituacionFinanciero.isEmpty()) {
            for (EstadoSituacionFinanciera i : listEstadoSituacionFinanciero) {
//                System.out.println("asiento contable:"+i.getAsientoContable().getTitulo());
//                System.out.println("Valor Año Actual:"+i.getValorAnioActual());
//                System.out.println("Valor Año Anterior:"+i.getValorAnioAnterior());
//                System.out.println("-------- ");
                BigDecimal anioActual = BigDecimal.ZERO;
                BigDecimal anioAnterior = BigDecimal.ZERO;
                
                List<EstadoSituacionFinanciera> listEstado = listEstadoSituacionFinanciero.stream().
                    filter(data -> data.getAsientoContable().getCodigo().
                    startsWith(i.getAsientoContable().getCodigo())).collect(Collectors.toList());
                
                if (listEstado.size() > 1) {
                    for (EstadoSituacionFinanciera e : listEstado) {
                        anioActual = anioActual.add(e.getValorAnioActual().abs());
                        anioAnterior = anioAnterior.add(e.getValorAnioAnterior().abs());
                    }
                    i.setValorAnioActual(anioActual);
                    i.setValorAnioAnterior(anioAnterior);
                }
                estadoService.create(i);
            }
        }

        List<EstadoSituacionFinanciera> listEstadoFinal;
        listEstadoFinal = estadoService.findAll();
        Map<String, BigDecimal> sumas = getSumatorias(listEstadoFinal);
        //<editor-fold defaultstate="collapsed" desc="parametros">
        //anioActual
        ss.addParametro("activoC", sumas.get("activoC"));
        ss.addParametro("activoNoC", sumas.get("activoNoC"));
        ss.addParametro("inversiones", sumas.get("inversiones"));
        ss.addParametro("otrosActivosF", sumas.get("otrosActivosF"));
        ss.addParametro("inversionesBienesL", sumas.get("inversionesBienesL"));
        ss.addParametro("inversionProyectosP", sumas.get("inversionProyectosP"));
        ss.addParametro("pasivoC", sumas.get("pasivoC"));
        ss.addParametro("endeudamiento", sumas.get("endeudamiento"));
        ss.addParametro("financiero", sumas.get("financiero"));
        ss.addParametro("provisiones", sumas.get("provisiones"));
        ss.addParametro("patrimonioA", sumas.get("patrimonioA"));
        ss.addParametro("cuentasO", sumas.get("cuentasO"));
        //anioAnterior
        ss.addParametro("activoCA", sumas.get("activoCA"));
        ss.addParametro("activoNoCA", sumas.get("activoNoCA"));
        ss.addParametro("inversionesA", sumas.get("inversionesA"));
        ss.addParametro("otrosActivosFA", sumas.get("otrosActivosFA"));
        ss.addParametro("inversionesBienesLA", sumas.get("inversionesBienesLA"));
        ss.addParametro("inversionProyectosPA", sumas.get("inversionProyectosPA"));
        ss.addParametro("pasivoCA", sumas.get("pasivoCA"));
        ss.addParametro("endeudamientoA", sumas.get("endeudamientoA"));
        ss.addParametro("financieroA", sumas.get("financieroA"));
        ss.addParametro("provisionesA", sumas.get("provisionesA"));
        ss.addParametro("patrimonioAA", sumas.get("patrimonioAA"));
        ss.addParametro("cuentasOA", sumas.get("cuentasOA"));
        ss.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(false);
        }
        ss.setNombreReporte("estadoSituacionFinanciera");
        ss.setNombreSubCarpeta("AsientosContables");

//        Cliente jefeFinanciero = clienteService.getClienteEspecificos(RolUsuario.financiero);
//        Cliente gerente = clienteService.getClienteEspecificos(RolUsuario.maximaAutoridad);
        Distributivo jefeFinanciero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
        Distributivo gerente = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.maximaAutoridad));
        ss.addParametro("usuarioFinanciero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getNombreCompleto() : "");
        ss.addParametro("cargoFinanciero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.financiero).getTexto());
        ss.addParametro("ciFinanciero", jefeFinanciero != null ? jefeFinanciero.getServidorPublico().getPersona().getIdentificacion() : "");

        ss.addParametro("cargoGerente", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.maximaAutoridad).getTexto());
        ss.addParametro("usuarioGerente", gerente != null ? gerente.getServidorPublico().getPersona().getNombreCompleto() : "");
        ss.addParametro("ciGerente", gerente != null ? gerente.getServidorPublico().getPersona().getIdentificacion() : "");
        //</editor-fold>
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        elegirPeriodo();
    }

    public Map<String, BigDecimal> getSumatorias(List<EstadoSituacionFinanciera> lisFinan) {
        Map<String, BigDecimal> result = new HashMap<>();
        //anioActual
        BigDecimal activoC = BigDecimal.ZERO;
        BigDecimal activoNoC = BigDecimal.ZERO;
        BigDecimal inversiones = BigDecimal.ZERO;
        BigDecimal otrosActivosF = BigDecimal.ZERO;
        BigDecimal inversionesBienesL = BigDecimal.ZERO;
        BigDecimal inversionProyectosP = BigDecimal.ZERO;
        BigDecimal pasivoC = BigDecimal.ZERO;
        BigDecimal endeudamiento = BigDecimal.ZERO;
        BigDecimal financiero = BigDecimal.ZERO;
        BigDecimal provisiones = BigDecimal.ZERO;
        BigDecimal patrimonioA = BigDecimal.ZERO;
        BigDecimal cuentasO = BigDecimal.ZERO;
        //anioAnterior
        BigDecimal activoCA = BigDecimal.ZERO;
        BigDecimal activoNoCA = BigDecimal.ZERO;
        BigDecimal inversionesA = BigDecimal.ZERO;
        BigDecimal otrosActivosFA = BigDecimal.ZERO;
        BigDecimal inversionesBienesLA = BigDecimal.ZERO;
        BigDecimal inversionProyectosPA = BigDecimal.ZERO;
        BigDecimal pasivoCA = BigDecimal.ZERO;
        BigDecimal endeudamientoA = BigDecimal.ZERO;
        BigDecimal financieroA = BigDecimal.ZERO;
        BigDecimal provisionesA = BigDecimal.ZERO;
        BigDecimal patrimonioAA = BigDecimal.ZERO;
        BigDecimal cuentasOA = BigDecimal.ZERO;
        for (EstadoSituacionFinanciera item : lisFinan) {
            if (item.getAsientoContable().getSubgrupo() != null) {
                switch (item.getAsientoContable().getSubgrupo()) {
                    case "INVERSIONES":
                        inversiones = inversiones.add(item.getValorAnioActual());
                        inversionesA = inversionesA.add(item.getValorAnioAnterior());
                        break;
                    case "OTROS ACTIVOS FINANCIEROS":
                        otrosActivosF = otrosActivosF.add(item.getValorAnioActual());
                        otrosActivosFA = otrosActivosFA.add(item.getValorAnioAnterior());
                        break;
                    case "INVERSIONES EN BIENES DE LARGA DURACIÓN":
                        inversionesBienesL = inversionesBienesL.add(item.getValorAnioActual());
                        inversionesBienesLA = inversionesBienesLA.add(item.getValorAnioAnterior());
                        break;
                    case "INVERSIONES EN PROYECTOS Y PROGRAMAS":
                        inversionProyectosP = inversionProyectosP.add(item.getValorAnioActual());
                        inversionProyectosPA = inversionProyectosPA.add(item.getValorAnioAnterior());
                        break;
                    case "ENDEUDAMIENTO":
                        endeudamiento = endeudamiento.add(item.getValorAnioActual());
                        endeudamientoA = endeudamientoA.add(item.getValorAnioAnterior());
                        break;
                    case "FINANCIEROS":
                        financiero = financiero.add(item.getValorAnioActual());
                        financieroA = financieroA.add(item.getValorAnioAnterior());
                        break;
                    case "PROVISIONES":
                        provisiones = provisiones.add(item.getValorAnioActual());
                        provisionesA = provisionesA.add(item.getValorAnioAnterior());
                        break;
                    case "PATRIMONIO ACUMULADO":
                        patrimonioA = patrimonioA.add(item.getValorAnioActual());
                        patrimonioAA = patrimonioAA.add(item.getValorAnioAnterior());
                        break;
                    case "CUENTAS DE ORDEN":
                        cuentasO = cuentasO.add(item.getValorAnioActual());
                        cuentasOA = cuentasOA.add(item.getValorAnioAnterior());
                        break;
                }
            }
            if ("ACTIVO".equals(item.getAsientoContable().getGrupo()) && "CORRIENTE".equals(item.getAsientoContable().getSubtitulo())) {
                activoC = activoC.add(item.getValorAnioActual());
                activoCA = activoCA.add(item.getValorAnioAnterior());
            }
            if ("ACTIVO".equals(item.getAsientoContable().getGrupo()) && "NO CORRIENTE".equals(item.getAsientoContable().getSubtitulo())) {
                activoNoC = activoNoC.add(item.getValorAnioActual());
                activoNoCA = activoNoCA.add(item.getValorAnioAnterior());
            }
            if ("PASIVO".equals(item.getAsientoContable().getGrupo()) && "CORRIENTE".equals(item.getAsientoContable().getSubtitulo())) {
                pasivoC = pasivoC.add(item.getValorAnioActual());
                pasivoCA = pasivoCA.add(item.getValorAnioAnterior());
            }
        }
        ss.addParametro("anio", busqueda.getAnio());
        ss.addParametro("diaDesde", Utils.getDia(inicio));
        ss.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(inicio)));
        ss.addParametro("diaHasta", Utils.getDia(fin));
        ss.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fin)));
        ss.addParametro("usuario", usser.getNameUser());
        //anioActual
        result.put("inversiones", inversiones);
        result.put("otrosActivosF", otrosActivosF);
        result.put("inversionesBienesL", inversionesBienesL);
        result.put("inversionProyectosP", inversionProyectosP);
        result.put("endeudamiento", endeudamiento);
        result.put("financiero", financiero);
        result.put("provisiones", provisiones);
        result.put("patrimonioA", patrimonioA);
        result.put("cuentasO", cuentasO);
        result.put("activoC", activoC);
        result.put("activoNoC", activoNoC);
        result.put("pasivoC", pasivoC);
        //anioAnterior
        result.put("inversionesA", inversionesA);
        result.put("otrosActivosFA", otrosActivosFA);
        result.put("inversionesBienesLA", inversionesBienesLA);
        result.put("inversionProyectosPA", inversionProyectosPA);
        result.put("endeudamientoA", endeudamientoA);
        result.put("financieroA", financieroA);
        result.put("provisionesA", provisionesA);
        result.put("patrimonioAA", patrimonioAA);
        result.put("cuentasOA", cuentasOA);
        result.put("activoCA", activoCA);
        result.put("activoNoCA", activoNoCA);
        result.put("pasivoCA", pasivoCA);

        return result;
    }

    public Map<String, BigDecimal> calcularAnios(AsientosContables asientoContable) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal anioActual = BigDecimal.ZERO;
        BigDecimal anioAnterior = BigDecimal.ZERO;
        BigDecimal totalDebe = BigDecimal.ZERO;
        BigDecimal totalHaber = BigDecimal.ZERO;
        if ("61803".equals(asientoContable.getCodigo())) {
            List<BalanceComprobacion> listBalanceCodigo2X3 = listBalance.stream().filter(data -> data.getCuentaContable().getCodigo().startsWith("62") || data.getCuentaContable().getCodigo().startsWith("63")).collect(Collectors.toList());
//            listBalance = balanceService.getBalanceComprobacionXCodigoX2AND3();
            if (!listBalanceCodigo2X3.isEmpty()) {
                for (BalanceComprobacion item : listBalanceCodigo2X3) {
//                    if ("62".equals(item.getCuentaContable().getCodigo().substring(0, 2))) {
//                        totalDebe = totalDebe.add(item.getTotalDebe());
//                        totalHaber = totalHaber.add(item.getTotalHaber());
//                    }
//                    if ("63".equals(item.getCuentaContable().getCodigo().substring(0, 2))) {
                    totalDebe = totalDebe.add(item.getTotalDebe());
                    totalHaber = totalHaber.add(item.getTotalHaber());

//                    }
                }
                anioActual = totalDebe.subtract(totalHaber);
//                System.out.println("IF");
//                System.out.println("asiento contable:"+asientoContable.getTitulo());
//                System.out.println("Valor Año Actual:"+anioActual);
//                //System.out.println("Valor Año Anterior:"+anioAnterior);
//                System.out.println("-------- ");
            }
        } else {
            List<BalanceComprobacion> listBalanceCodigo = listBalance.stream().filter(data -> data.getCuentaContable().getCodigo()
                    .startsWith(asientoContable.getCodigo())).collect(Collectors.toList());
//            listBalance = balanceService.getBalanceComprobacionXCodigo(i.getCodigo());
            if (!listBalanceCodigo.isEmpty()) {
                for (BalanceComprobacion item : listBalanceCodigo) {
                    anioActual = anioActual.add(AgrupacionAnioActual(item));
                    anioAnterior = anioAnterior.add(AgrupacionAnioAnterior(item));
//                    System.out.println("ELSE");
//                    System.out.println("asiento contable:"+asientoContable.getTitulo());
//                    System.out.println("Valor Año Actual:"+anioActual);
//                    System.out.println("Valor Año Anterior:"+anioAnterior);
//                    System.out.println("-------- ");
                    
                }
            }

        }
        BigDecimal sumatoriaAnioActual = BigDecimal.ZERO;
        if(anioAnterior != null || anioAnterior != BigDecimal.ZERO){
            System.out.println("entro if ");
            sumatoriaAnioActual = anioActual.add(anioAnterior);
        }else{
            System.out.println("else");
            sumatoriaAnioActual = anioActual;
        }
        System.out.println("sumatoriaAnioActual:"+sumatoriaAnioActual);
        
        result.put("anioActual", sumatoriaAnioActual);
        result.put("anioAnterior", anioAnterior);
        return result;
    }

    public BigDecimal AgrupacionAnioActual(BalanceComprobacion item) {
        BigDecimal result = BigDecimal.ZERO;
        result = item.getFlujoDebe().subtract(item.getFlujoHaber());
        return result;
    }

    public BigDecimal AgrupacionAnioAnterior(BalanceComprobacion item) {
        BigDecimal result = BigDecimal.ZERO;
        result = item.getSaldoInicialDebe().subtract(item.getSaldoInicialHaber());
        return result;
    }

    public BigDecimal totalDebe(BalanceComprobacion b) {
        BigDecimal result = BigDecimal.ZERO;
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalHaber(BalanceComprobacion b) {
        BigDecimal result = BigDecimal.ZERO;
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            return result.abs();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void elegirPeriodo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        inicio = calendar.getTime();
        fin = new Date();
    }

    public Integer sizeNivel() {
        List<PlanCuentas> listNiveles = planCuentasService.getNivelesList(CONFIG.PLAN_CUENTA_CONTABLE, false);
        if (listNiveles != null) {
            Integer result = listNiveles.stream().mapToInt(x -> x.getNumDigito()).sum();
            return result;
        } else {
            return 12;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
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

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
//</editor-fold>

}
