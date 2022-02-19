/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import com.origami.sigef.resource.contabilidad.models.MovimientoCuentasModel;
import com.origami.sigef.resource.contabilidad.services.ContContabilidadModuloService;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
@Named(value = "contMovimientoCuentasView")
@ViewScoped
public class ContMovimientoCuentasController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;
    @Inject
    private ContContabilidadModuloService contContabilidadModuloService;

    private OpcionBusqueda opcionBusqueda;
    private ContCuentas selectContCuentas;
    private ContCuentas inicialContCuentas;
    private ContCuentas finalContCuentas;

    private Date fechaInicio, fechaFin;

    private Integer codigo, codAux;

    private String codCuenta, nomCuenta, codCuenta1, codCuenta2;

    private LazyModel<ContCuentas> contCuentasLazy;

    private List<Short> listaPeriodo;

    private Boolean tipoSearch;

    @PostConstruct
    public void init() {
        cleanForm();
        listaPeriodo = catalogoItemService.getPeriodo();
    }

    public void cleanForm() {
        opcionBusqueda = new OpcionBusqueda();
        actualizarFechas();
        codigo = 1;
        tipoSearch = false;
        codCuenta = null;
        codCuenta1 = null;
        codCuenta2 = null;
        nomCuenta = null;
        selectContCuentas = new ContCuentas();
        inicialContCuentas = new ContCuentas();
        finalContCuentas = new ContCuentas();
    }

    public void actualizarFechas() {

        String fecha = "01-01-" + opcionBusqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Integer dia = new Date().getDate();
        Integer mes = new Date().getMonth() + 1;
        try {
            fechaInicio = dateFormat.parse(fecha);
            String fecha_hasta = dia.toString() + "-" + mes.toString() + "-" + opcionBusqueda.getAnio();
            fechaFin = dateFormat.parse(fecha_hasta);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

    }

    public void searchContCuenta(Integer codigo) {
        codAux = codigo;
        switch (codigo) {
            case 1:
                selectContCuentas = validarRegistro(codCuenta);
                if (selectContCuentas == null) {
                    openDlgCuenta(codAux);
                }
                break;
            case 3:
                this.codigo = 3;
                inicialContCuentas = validarRegistro(codCuenta1);
                if (inicialContCuentas == null) {
                    openDlgCuenta(codAux);
                }
                break;
            case 4:
                this.codigo = 3;
                finalContCuentas = validarRegistro(codCuenta2);
                if (finalContCuentas == null) {
                    openDlgCuenta(codAux);
                }
                break;
        }

    }

    private ContCuentas validarRegistro(String codigo) {
        if (codigo != null) {
            return contCuentasService.findCodigo(codigo);
        } else {
            return null;
        }
    }

    public void openDlgCuenta(Integer codigo) {
        codAux = codigo;
        updadateLazyCuenta();
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void selectBuscarCuenta() {
        if (tipoSearch) {
            codigo = 1;
        } else {
            codigo = 2;
        }
        selectContCuentas = new ContCuentas();
        PrimeFaces.current().ajax().update("loadCuenta");
        System.out.println("select cont cuentas:"+selectContCuentas.toString());
    }

    public void updadateLazyCuenta() {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        System.out.println("actualiza lazy cuenta");
        switch(this.codigo){
            case 1:
                System.out.println("codigo 1:"+this.codigo);
//                contCuentasLazy.getFilterss().put("codigo:startsWith", codCuenta);
                contCuentasLazy.getFilterss().put("movimiento", true);
            break;
            
            case 2:
                contCuentasLazy.getFilterss().put("movimiento", false);

//                if (nomCuenta != null && !nomCuenta.equals("")) {
//                    contCuentasLazy.getFilterss().put("movimiento", true);
//                    contCuentasLazy.getFilterss().put("descripcion:or:descripcion", nomCuenta);
//                }
            break;
        }
    }

    public void closeDlgCuenta(ContCuentas contCuentas) {
        switch (codAux) {
            case 1:
            case 2:
                selectContCuentas = contCuentas;
                break;
            case 3:
                codigo = 3;
                inicialContCuentas = contCuentas;
                break;
            case 4:
                codigo = 3;
                finalContCuentas = contCuentas;
                break;
        }
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
    }

    public void printReport(String tipoArchivo) {
        System.out.println("codigo:"+codigo);
        ContSaldoInicial contSaldoInicial = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(selectContCuentas, opcionBusqueda.getAnio());
        List<MovimientoCuentasModel> movimientos = new ArrayList<>();

        BigDecimal saldoInicial;
        BigDecimal saldoInicialSum = BigDecimal.ZERO;
        BigDecimal saldoInicialFechaCorte = BigDecimal.ZERO;
        if (contSaldoInicial != null) {
            saldoInicial = (contSaldoInicial.getSaldoDebe() == null ? BigDecimal.ZERO
                    : contSaldoInicial.getSaldoDebe()).subtract(contSaldoInicial.getSaldoHaber() == null ? BigDecimal.ZERO
                    : contSaldoInicial.getSaldoHaber());
        } else {
            saldoInicial = BigDecimal.ZERO;
        }
     
        if (codigo == 1 || codigo == 2) {
            if (selectContCuentas == null || selectContCuentas.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una cuenta contable");
                return;
            }
            saldoInicialFechaCorte = calcularSaldoInicialPorFechaCorte();
            saldoInicialSum = saldoInicial.add(saldoInicialFechaCorte);
            servletSession.addParametro("saldo_inicial", saldoInicialSum);
            servletSession.addParametro("cod_cuenta", selectContCuentas.getCodigo());
            servletSession.addParametro("cod_cuenta_inicial", "");
            servletSession.addParametro("cod_cuenta_final", "");
        } else {
            if (inicialContCuentas == null || inicialContCuentas.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una cuenta contable inicial");
                return;
            }
            if (finalContCuentas == null || finalContCuentas.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una cuenta contable final");
                return;
            }
//            saldoInicialFechaCorte = calcularSaldoInicialPorFechaCorte();
//            saldoInicialSum = saldoInicial.add(saldoInicialFechaCorte);
//            System.out.println("saldo inicial:"+saldoInicialSum);
            servletSession.addParametro("cod_cuenta", "");
            servletSession.addParametro("saldo_inicial", saldoInicialSum);
            servletSession.addParametro("cod_cuenta_inicial", inicialContCuentas.getCodigo());
            servletSession.addParametro("cod_cuenta_final", finalContCuentas.getCodigo());
            System.out.println("cod_cuenta_inicial: " + inicialContCuentas.getCodigo());
            System.out.println("cod_cuenta_final: " + finalContCuentas.getCodigo());
        }
        servletSession.addParametro("saldo_inicial", saldoInicialSum);
        servletSession.addParametro("codigo", codigo);
        servletSession.addParametro("fecha_inicio", fechaInicio);
        servletSession.addParametro("fecha_inicio_string", Utils.convertirFechaLetra(fechaInicio));
        servletSession.addParametro("fecha_fin", fechaFin);
        servletSession.addParametro("fecha_fin_string", Utils.convertirFechaLetra(fechaFin));
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_contabilidad");
        servletSession.setNombreReporte("movimiento_cuentas");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        cleanForm();
    }
    
    
    public BigDecimal calcularSaldoInicialPorFechaCorte(){
        List<MovimientoCuentasModel> movimientos = new ArrayList<>();

        BigDecimal saldoInicialFechaCorte = BigDecimal.ZERO;
        
        Calendar cal = Calendar.getInstance();
        cal.set(opcionBusqueda.getAnio(), 0, 1);
        Date fecha_inicio_anio = cal.getTime();
        if (fechaInicio.getDate() == 1 && fechaInicio.getMonth() + 1 == 1) {
            movimientos = new ArrayList<>();
        }else{
            movimientos = contContabilidadModuloService.movimientosCuentasByPeriodoAndCodigo(codigo, selectContCuentas.getCodigo(),"","", fecha_inicio_anio, fechaInicio, opcionBusqueda.getAnio());
            // System.out.println("movimientos tamanio:"+movimientos.size());
            // System.out.println("movimientos:"+movimientos.toString());
        }

        if (!movimientos.isEmpty()) {
            saldoInicialFechaCorte = (movimientos.get(0).getSum_debe() == null ? BigDecimal.ZERO
                    : movimientos.get(0).getSum_debe()).subtract(movimientos.get(0).getSum_haber() == null ? BigDecimal.ZERO
                    : movimientos.get(0).getSum_haber());
        } else {
            saldoInicialFechaCorte = BigDecimal.ZERO;
        }
        
        return saldoInicialFechaCorte;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ContCuentas getSelectContCuentas() {
        return selectContCuentas;
    }

    public void setSelectContCuentas(ContCuentas selectContCuentas) {
        this.selectContCuentas = selectContCuentas;
    }

    public String getCodCuenta() {
        return codCuenta;
    }

    public void setCodCuenta(String codCuenta) {
        this.codCuenta = codCuenta;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public String getNomCuenta() {
        return nomCuenta;
    }

    public void setNomCuenta(String nomCuenta) {
        this.nomCuenta = nomCuenta;
    }

    public Boolean getTipoSearch() {
        return tipoSearch;
    }

    public void setTipoSearch(Boolean tipoSearch) {
        this.tipoSearch = tipoSearch;
    }

    public ContCuentas getInicialContCuentas() {
        return inicialContCuentas;
    }

    public void setInicialContCuentas(ContCuentas inicialContCuentas) {
        this.inicialContCuentas = inicialContCuentas;
    }

    public ContCuentas getFinalContCuentas() {
        return finalContCuentas;
    }

    public void setFinalContCuentas(ContCuentas finalContCuentas) {
        this.finalContCuentas = finalContCuentas;
    }

    public Integer getCodAux() {
        return codAux;
    }

    public void setCodAux(Integer codAux) {
        this.codAux = codAux;
    }

    public String getCodCuenta1() {
        return codCuenta1;
    }

    public void setCodCuenta1(String codCuenta1) {
        this.codCuenta1 = codCuenta1;
    }

    public String getCodCuenta2() {
        return codCuenta2;
    }

    public void setCodCuenta2(String codCuenta2) {
        this.codCuenta2 = codCuenta2;
    }

}