/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.itextpdf.text.Font;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ConciliacionBancaria;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.DetalleConciliacionBancaria;
import com.origami.sigef.common.lazy.LazyConciliacionModel;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.Name_Header_xlsx;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.ConciliacionBancariaService;
import com.origami.sigef.contabilidad.service.DetalleConciliacionBancariaService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;
import com.origami.sigef.talentohumano.services.CuentaBancariaService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author jesus
 */
@Named(value = "conciliacionBancariaView")
@ViewScoped
public class ConciliacionBancariaController implements Serializable {

    @Inject
    private CuentaBancariaService cuentaBancariaService;
    @Inject
    private ConciliacionBancariaService conciliacionBancariaService;
    @Inject
    private DetalleConciliacionBancariaService detalleConciliacionBancariaService;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ValoresService valoresService;

    private List<CuentaBancaria> cuentasBancarias;
    // private CuentaBancaria cuentaBancaria;
    private List<Integer> anios;
    private Integer anioMax;
    private List<String> meses;
    private Integer anio;
    private Integer mes;
    private ConciliacionBancaria conciliacionBancaria;
    private List<DetalleConciliacionBancaria> debitosLibroBanco;
    private List<DetalleConciliacionBancaria> creditosLibroBanco;
    private List<DetalleConciliacionBancaria> estadoCuenta;
    private List<DetalleConciliacionBancaria> creditoDebitosNoEfectivizados;
    private List<DetalleConciliacionBancaria> creditoDebitosNoEfecDebitos;
    private List<DetalleConciliacionBancaria> creditoDebitosNoEfecCreditos;
    private List<Name_Header_xlsx> parametorsXlsx;
    private LazyDataModel<DetalleConciliacionBancaria> lazyDebidosLibroBanco;
    private LazyDataModel<DetalleConciliacionBancaria> lazyCreditosLibroBanco;
    private Boolean disabledByEstado;
    private DetalleConciliacionBancaria detalleConciliacionBancariaEstC;
    private Boolean edit;
    private Boolean editableCellEdit;

    // private String PAGOEFEC = "PAGO EFEC";
    private String PAGONOEFEC = "PAGO NO EFEC";
    // private String DEBITOEFEC = "DEBITO EF";
    private String DEBITONOEFEC = "DEBITO NO EFEC";
    // private String ANULADO = "ANULADO";
    //
    // private String DEPOSITOEFEC = "DEPOSITO EF";
    // private String CREDITOEFEC = "CREDITO EF";
    private String CREDITONOEFEC = "CREDITO NO EFEC";
    private String DEPOSITONOEFEC = "DEPOSITO NO EFEC";
    private List<ContCuentaEntidad> listContCuentaEntidad;
    private ContCuentaEntidad cuentaBancaria;

    @PostConstruct
    public void init() {
        loadAniosMeses();
        loadModel();
        cuentasBancarias = cuentaBancariaService.getListaCuentaBancariaByAnio(anio.shortValue());
        listContCuentaEntidad = cuentaBancariaService.getListaCuentasEntidad();
    }

    public void loadModel() {
        edit = Boolean.FALSE;
        disabledByEstado = Boolean.FALSE;
        editableCellEdit = Boolean.TRUE;
        cuentaBancaria = new ContCuentaEntidad();
        conciliacionBancaria = new ConciliacionBancaria();
        creditoDebitosNoEfectivizados = new ArrayList<>();
        debitosLibroBanco = new ArrayList<>();
        creditosLibroBanco = new ArrayList<>();
        creditoDebitosNoEfecDebitos = new ArrayList<>();
        creditoDebitosNoEfecCreditos = new ArrayList<>();
        estadoCuenta = new ArrayList<>();
        lazyDebidosLibroBanco = null;
        lazyCreditosLibroBanco = null;
        detalleConciliacionBancariaEstC = new DetalleConciliacionBancaria();
    }

    private void loadAniosMeses() {
        parametorsXlsx = new ArrayList<>();
        anios = new ArrayList<>();
        anioMax = (Utils.getAnio(new Date()) - 1);
        for (int i = anioMax; i <= (Utils.getAnio(new Date())); i++) {
            anios.add(i);
        }
        Collections.sort(anios, Collections.reverseOrder());
        meses = Utils.getMeses();
        anio = anios.get(0);
        mes = Utils.getMes(new Date());
    }

    public Integer mes(String mes) {
        return Utils.convertirLetraAMes(mes);
    }

    public void loadCuentaBancariaAnio() {
        cuentasBancarias = cuentaBancariaService.getListaCuentaBancariaByAnio(anio.shortValue());
        JsfUtil.update("formMaster");
    }

    public void loadConciliacionBancaria() {
        if (anio != null && mes != null && cuentaBancaria != null && cuentaBancaria.getId() != null) {
            creditoDebitosNoEfectivizados = new ArrayList<>();
            creditoDebitosNoEfecDebitos = new ArrayList<>();
            creditoDebitosNoEfecCreditos = new ArrayList<>();
            estadoCuenta = new ArrayList<>();
            creditosLibroBanco = new ArrayList<>();
            debitosLibroBanco = new ArrayList<>();
            String fecha = anio + "-" + String.format("%02d", mes);
            String fechaDefault = anio + "-01"; // ENERO
            conciliacionBancaria = conciliacionBancariaService.findConciliacionBancariaByMesAndAnio(mes, anio,
                    cuentaBancaria.getIdCuentaMovimiento().getId());
            if (conciliacionBancaria != null) {
                edit = Boolean.TRUE;
                disabledByEstado = "CONCILIADO".equals(conciliacionBancaria.getEstadoConciliacion());
                if ("CONCILIADO".equals(conciliacionBancaria.getEstadoConciliacion())) {
                    editableCellEdit = Boolean.FALSE;
                } else {
                    editableCellEdit = Boolean.TRUE;
                }
                creditoDebitosNoEfectivizados = new ArrayList<>();
                // separar en dos listas creditos y debitos de comprobante de pago y diario
                // general.
                if (!conciliacionBancaria.getDetallesConciliacionesBancarias().isEmpty()) {
                    debitosLibroBanco = new ArrayList<>();
                    creditosLibroBanco = new ArrayList<>();
                    estadoCuenta = new ArrayList<>();
                    for (DetalleConciliacionBancaria d : conciliacionBancaria.getDetallesConciliacionesBancarias()) {
                        d.setTipoAnterior(d.getConciliacion());
                        switch (d.getTipo()) {
                            case "DLB": // DEBITO LIBRO BANCO
                                debitosLibroBanco.add(d);
                                break;
                            case "CLB": // CREDITO LIBRO BANCO
                                creditosLibroBanco.add(d);
                                break;
                            case "ESTC":
                                estadoCuenta.add(d);
                                break;
                            case "NO_EFECTIVO_ANTERIOR":// SE OBTIENE LOS PAGOS NOS EFECTIVZDOS DE MESES ANTERIORES EN
                                                        // EL CASO DE EXISTIR DE LA MISMA TABLA
                                creditoDebitosNoEfectivizados.add(d);
                                break;
                        }
                    }
                }
            } else {
                // se trae los registros de diario general y comprobante de pago debito
                editableCellEdit = Boolean.TRUE;
                disabledByEstado = Boolean.FALSE;
                edit = Boolean.FALSE;
                conciliacionBancaria = new ConciliacionBancaria();
                // System.out.println("fecha:"+fecha);
                // System.out.println("id movimiento:"+cuentaBancaria.getIdCuentaMovimiento().getId());
                debitosLibroBanco = detalleConciliacionBancariaService.findAllComprobantePagoforDetalleConciliacionDebito(
                                cuentaBancaria.getIdCuentaMovimiento().getId(), fecha);
                
                List<DetalleConciliacionBancaria> dGdebitos = detalleConciliacionBancariaService
                        .findAllDiarioGeneralforDetalleConciliacionDebito(cuentaBancaria.getIdCuentaMovimiento().getId(),
                                fecha);
                unirLibroBanco(debitosLibroBanco, dGdebitos);
                // se trae los registros de diario general y comprobante de pago credito
                creditosLibroBanco = detalleConciliacionBancariaService
                        .findAllDiarioGeneralforDetalleConciliacionCredito(cuentaBancaria.getIdCuentaMovimiento().getId(),
                                fecha);
                conciliacionBancaria.setLibroBancoDebito(
                        detalleConciliacionBancariaService.finValueDebitoByComprobantePagoAndDiarioGeneral(
                                cuentaBancaria.getIdCuentaMovimiento().getId(), fecha, anio.shortValue(), fechaDefault));
                conciliacionBancaria.setLibroBancoCredito(
                        detalleConciliacionBancariaService.finValueCreditoByComprobantePagoAndDiarioGeneral(
                                cuentaBancaria.getIdCuentaMovimiento().getId(), fecha, anio.shortValue(), fechaDefault));
                // calcularSaldoInicial(fecha, fechaDefault);
                calcularSaldoInicial(fecha);
            }
            // SI NO EXISTE PAGOS LOS BUSCA EN REGISTROS ANTERIORES.
            cargarDatosNoEfectivos(fecha);
            initLazyModel(debitosLibroBanco, creditosLibroBanco);
        } else {
            loadModel();
        }
        JsfUtil.update("formMaster");
    }

    private void cargarDatosNoEfectivos(String fecha) {
        if (creditoDebitosNoEfectivizados.isEmpty()) {
            creditoDebitosNoEfectivizados = detalleConciliacionBancariaService
                    .findAllDetalleConciliacionByNoEfectivo(fecha, cuentaBancaria.getIdCuentaMovimiento().getId(),
                            PAGONOEFEC, CREDITONOEFEC, DEBITONOEFEC, DEPOSITONOEFEC);
            if (!creditoDebitosNoEfectivizados.isEmpty()) {
                for (DetalleConciliacionBancaria d : creditoDebitosNoEfectivizados) {
                    // d.setId(Utils.idTemp());
                    d.setTipoReferencia("NO_EFECTIVO_ANTERIOR");
                    d.setTipoAnterior(d.getConciliacion());
                    getCalcularConciliacion(d);
                    switch (d.getTipo()) {
                        case "DLB": // DEBITO LIBRO BANCO NO EFECTIVOS
                            creditoDebitosNoEfecDebitos.add(d);
                            break;
                        case "CLB": // CREDITO LIBRO BANCO NO EFECTVOS
                            creditoDebitosNoEfecCreditos.add(d);
                            break;
                    }
                }
            }
        } else {
            for (DetalleConciliacionBancaria d : creditoDebitosNoEfecCreditos) {
                d.setTipoAnterior(d.getConciliacion());
                switch (d.getTipo()) {
                    case "DLB": // DEBITO LIBRO BANCO NO EFECTIVOS
                        creditoDebitosNoEfecDebitos.add(d);
                        break;
                    case "CLB": // CREDITO LIBRO BANCO NO EFECTVOS
                        creditoDebitosNoEfecCreditos.add(d);
                        break;
                }
            }
        }
    }

    public void guardar() {
        if (validarGuardar()) {
            if (edit) {
                if (conciliacionBancaria.getSaldoConciliadoEstC().equals(conciliacionBancaria.getSaldoConciliadoLB())) {
                    conciliacionBancaria.setEstadoConciliacion("CONCILIADO");
                }
                conciliacionBancaria.setFechaModifica(new Date());
                conciliacionBancaria.setUsuarioModifica(this.session.getNameUser());
                conciliacionBancariaService.edit(conciliacionBancaria);
                editDetalleConciliacion(debitosLibroBanco);
                editDetalleConciliacion(creditosLibroBanco);
                editDetalleConciliacion(estadoCuenta);
                editDetalleConciliacion(creditoDebitosNoEfecDebitos);
                editDetalleConciliacion(creditoDebitosNoEfecCreditos);
            } else {
                conciliacionBancaria.setCuentaContableBanco(cuentaBancaria.getId());
                conciliacionBancaria.setNombreCuentaContableBanco(cuentaBancaria.getIdCuentaMovimiento().getCodigo() + "-"
                        + cuentaBancaria.getIdCuentaMovimiento().getDescripcion());
                conciliacionBancaria.setNumeroNombreCuentaBanco(
                        cuentaBancaria.getNumeroCuenta() + "-" + cuentaBancaria.getNombre());
                conciliacionBancaria.setAnio(anio);
                conciliacionBancaria.setMes(mes);
                conciliacionBancaria.setPeriodo(anio + "-" + String.format("%02d", mes));
                conciliacionBancaria.setEstado(Boolean.TRUE);
                conciliacionBancaria.setFechaCreacion(new Date());
                conciliacionBancaria.setCuentaContable(cuentaBancaria.getIdCuentaMovimiento().getId());
                conciliacionBancaria.setUsuarioCreacion(this.session.getNameUser());
                if (conciliacionBancaria.getSaldoConciliadoEstC().equals(conciliacionBancaria.getSaldoConciliadoLB())) {
                    conciliacionBancaria.setEstadoConciliacion("CONCILIADO");
                } else {
                    conciliacionBancaria.setEstadoConciliacion("REGISTRADO");
                }
                conciliacionBancariaService.create(conciliacionBancaria);
                createDetalleConciliacion(debitosLibroBanco);
                createDetalleConciliacion(creditosLibroBanco);
                createDetalleConciliacion(estadoCuenta);
                createDetalleConciliacion(creditoDebitosNoEfecDebitos);
                createDetalleConciliacion(creditoDebitosNoEfecCreditos);
            }
        }
        JsfUtil.addSuccessMessage("", "Conciliación " + (edit ? " Edtida" : " Guardado") + " con éxito");
        JsfUtil.update("formMaster");
        loadModel();
    }

    public void createDetalleConciliacion(List<DetalleConciliacionBancaria> object) {
        if (object != null && !object.isEmpty()) {
            for (DetalleConciliacionBancaria d : object) {
                if (d.getConciliacionBancaria() != null) {
                    detalleConciliacionBancariaService.edit(d);
                } else {
                    d.setId(null);
                    d.setConciliacionBancaria(conciliacionBancaria);
                    detalleConciliacionBancariaService.create(d);
                }
            }
        }
    }

    public void editDetalleConciliacion(List<DetalleConciliacionBancaria> object) {
        if (object != null && !object.isEmpty()) {
            for (DetalleConciliacionBancaria d : object) {
                if (detalleConciliacionBancariaService.find(d.getId()) != null) {
                    detalleConciliacionBancariaService.edit(d);
                } else {
                    d.setId(null);
                    d.setConciliacionBancaria(conciliacionBancaria);
                    detalleConciliacionBancariaService.create(d);
                }
            }
        }
    }

    public void unirLibroBanco(List<DetalleConciliacionBancaria> cPdebitos,
            List<DetalleConciliacionBancaria> dGdebitos) {
        if (cPdebitos != null && dGdebitos != null) {
            List<DetalleConciliacionBancaria> auxConciliacion = cPdebitos;
            for (DetalleConciliacionBancaria cb : dGdebitos) {
                auxConciliacion.add(cb);
            }
            debitosLibroBanco = auxConciliacion;
        } else if (cPdebitos == null && dGdebitos != null) {
            debitosLibroBanco = dGdebitos;
        }

    }

    public void initLazyModel(List<DetalleConciliacionBancaria> debito, List<DetalleConciliacionBancaria> credito) {
        if (debito != null && credito != null) {
            lazyDebidosLibroBanco = new LazyConciliacionModel(debito);
            lazyCreditosLibroBanco = new LazyConciliacionModel(credito);
        } else if (debito == null && credito != null) {
            JsfUtil.addInformationMessage("", "No existen datos de Débito Libro Banco con los parámetros ingresados");
            lazyDebidosLibroBanco = null;
            lazyCreditosLibroBanco = new LazyConciliacionModel(credito);
        } else if (debito != null && credito == null) {
            JsfUtil.addInformationMessage("", "No existen datos de Crédito Libro Banco con los parámetros ingresados");
            lazyDebidosLibroBanco = new LazyConciliacionModel(debito);
            lazyCreditosLibroBanco = null;
        } else {
            JsfUtil.addInformationMessage("", "No existen datos con los parámetros ingresados");
            lazyCreditosLibroBanco = null;
            lazyDebidosLibroBanco = null;
        }
    }

    public void editConciliacionEstadoCuenta(DetalleConciliacionBancaria d) {
        estadoCuenta.remove(d);
        detalleConciliacionBancariaEstC = d;
        if (d.getConciliacion().equals("NC NO CONTABILIZADAS")) {
            conciliacionBancaria.setCreditoNoEfectLB(conciliacionBancaria.getCreditoNoEfectLB().subtract(d.getValor()));
        } else {
            conciliacionBancaria
                    .setDebitosNoRegistLB(conciliacionBancaria.getDebitosNoRegistLB().subtract(d.getValor()));
        }
        JsfUtil.update("tabRegistroESTC:panel-estado-cuenta");
    }

    public void addDetalleConciliacionEstadoCuenta() {
        if (validarEstadoCuenta()) {
            detalleConciliacionBancariaEstC.setTipo("ESTC");
            if (detalleConciliacionBancariaEstC.getId() == null) {
                detalleConciliacionBancariaEstC.setId(Utils.idTemp());
            }
            if (detalleConciliacionBancariaEstC.getConciliacion().equals("NC NO CONTABILIZADAS")) {
                conciliacionBancaria.setCreditoNoEfectLB(
                        conciliacionBancaria.getCreditoNoEfectLB().add(detalleConciliacionBancariaEstC.getValor()));
            } else {
                conciliacionBancaria.setDebitosNoRegistLB(
                        conciliacionBancaria.getDebitosNoRegistLB().add(detalleConciliacionBancariaEstC.getValor()));
            }
            if (estadoCuenta.isEmpty()) {
                estadoCuenta.add(detalleConciliacionBancariaEstC);
            } else {
                List<DetalleConciliacionBancaria> aux;
                aux = estadoCuenta;
                estadoCuenta = new ArrayList<>();
                aux.add(detalleConciliacionBancariaEstC);
                estadoCuenta = aux;
            }
            detalleConciliacionBancariaEstC = new DetalleConciliacionBancaria();
            // conciliacionBancaria.setSaldoConciliadoLB(conciliacionBancaria.getSaldoLibroBanco()
            // .add(conciliacionBancaria.getCreditoNoEfectLB())
            // .subtract(conciliacionBancaria.getDebitosNoRegistLB()));
            JsfUtil.addSuccessMessage("", "Registro de Estado Cuenta Agregado con éxito");
            JsfUtil.update("tabResumenConciliacion:containerValoresLB");
        }
    }

    public Boolean validarEstadoCuenta() {
        if (detalleConciliacionBancariaEstC.getFecha() == null) {
            JsfUtil.addErrorMessage("", "Ingrese una fecha");
            return false;
        }
        if (detalleConciliacionBancariaEstC.getConciliacion() == null) {
            JsfUtil.addErrorMessage("", "Ingrese un concepto");
            return false;
        }
        if (detalleConciliacionBancariaEstC.getValor() == null) {
            JsfUtil.addErrorMessage("", "Ingrese un valor");
            return false;
        }
        return true;
    }

    public Boolean validarGuardar() {
        if (cuentaBancaria == null || cuentaBancaria.getId() == null) {
            JsfUtil.addErrorMessage("", "Necesita Seleccionar una cueneta Contable");
            return false;
        }
        if (anio == null) {
            JsfUtil.addErrorMessage("", "Necesita Selecciona un año");
            return false;
        }
        if (mes == null) {
            JsfUtil.addErrorMessage("", "Necesita Seleccionar un mes");
            return false;
        }
        return true;
    }

    public void eliminarEstadosCuentas(DetalleConciliacionBancaria estadoCuenta) {
        if (estadoCuenta.getConciliacion().equals("NC NO CONTABILIZADAS")) {
            conciliacionBancaria
                    .setCreditoNoEfectLB(conciliacionBancaria.getCreditoNoEfectLB().subtract(estadoCuenta.getValor()));
        } else {
            conciliacionBancaria.setDebitosNoRegistLB(
                    conciliacionBancaria.getDebitosNoRegistLB().subtract(estadoCuenta.getValor()));
        }
        if (detalleConciliacionBancariaService.find(estadoCuenta.getId()) != null) {
            detalleConciliacionBancariaService.remove(estadoCuenta);
        }
        this.estadoCuenta.remove(estadoCuenta);
        JsfUtil.addSuccessMessage("", "Registro eliminado");
        JsfUtil.update("tabResumenConciliacion:containerValoresLB");
    }

    private void calcularSaldoInicial(String fecha) {

        SaldoDebeHaberDTO saldo = conciliacionBancariaService.findHaberAndHaberByComprobantePagoAndDiarioGeneralDetalle(
                cuentaBancaria.getIdCuentaMovimiento().getId(), fecha);
        if (saldo == null) {
            saldo = new SaldoDebeHaberDTO();
        }
        conciliacionBancaria.setLibroBancoSaldoInicial(saldo.getSaldoDebe().subtract(saldo.getSaldoHaber()));
        conciliacionBancaria.setSaldoLibroBanco(conciliacionBancaria.getLibroBancoSaldoInicial()
                .add(conciliacionBancaria.getLibroBancoCredito())
                .subtract(conciliacionBancaria.getLibroBancoDebito()));
    }

    public void getResultValorLibroBco() {
        if (conciliacionBancaria.getEstadoCuentaSaldoInicial() != null
                && conciliacionBancaria.getEstadoCuentaCredito() != null
                && conciliacionBancaria.getEstadoCuentaDebito() != null) {
            conciliacionBancaria.setSaldoCuentaMonetaria(conciliacionBancaria.getEstadoCuentaSaldoInicial()
                    .add(conciliacionBancaria.getEstadoCuentaCredito())
                    .subtract(conciliacionBancaria.getEstadoCuentaDebito()));
        }
        JsfUtil.update("valor-monetario");
        JsfUtil.update("tabResumenConciliacion:containerValoresESTC");
    }

    public void getCalcularConciliacion(DetalleConciliacionBancaria c) {
        String conciliacion = "";
        if (c.getConciliacion() != null) {
            conciliacion = c.getConciliacion();
        } else if (c.getTipoAnterior() != null) {
            conciliacion = c.getTipoAnterior();
        }
        if (this.conciliacionBancaria.getSaldoLibroBanco().intValue() == 0) {
            JsfUtil.addWarningMessage("Saldo Libro Banco",
                    "Saldo Libro Banco es " + this.conciliacionBancaria.getSaldoLibroBanco().intValue());
            return;
        }
        switch (conciliacion) {
            case "PAGO EFEC":
            case "DEBITO EFEC":
            case "DEPOSITO EFEC":
            case "CREDITO EFEC":
                asignarValoresEfectivizadoAnulado(c);
                c.setTipoAnterior(c.getConciliacion());
                break;
            case "PAGO NO EFEC": // debito
                if (c.getTipoAnterior() != null && c.getTipoAnterior().equals("DEBITO NO EFEC")) {
                    conciliacionBancaria.setDebitoNoEfectEstC(conciliacionBancaria.getDebitoNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                }
                conciliacionBancaria.setPagoNoEfectEstC(
                        conciliacionBancaria.getPagoNoEfectEstC().add(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                c.setTipoAnterior("PAGO NO EFEC");
                break;
            case "DEBITO NO EFEC": // debito
                if (c.getTipoAnterior() != null && c.getTipoAnterior().equals("PAGO NO EFEC")) {
                    conciliacionBancaria.setPagoNoEfectEstC(conciliacionBancaria.getPagoNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                }
                conciliacionBancaria.setDebitoNoEfectEstC(conciliacionBancaria.getDebitoNoEfectEstC().add(c.getValor())
                        .setScale(2, RoundingMode.HALF_UP));
                c.setTipoAnterior("DEBITO NO EFEC");
                break;
            case "CREDITO NO EFEC": // credito
                if (c.getTipoAnterior() != null && c.getTipoAnterior().equals("DEPOSITO NO EFEC")) {
                    conciliacionBancaria.setDepositosNoEfectEstC(conciliacionBancaria.getDepositosNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                }
                conciliacionBancaria.setNotasCredNoEfectEstC(conciliacionBancaria.getNotasCredNoEfectEstC()
                        .add(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                c.setTipoAnterior("CREDITO NO EFEC");
                break;
            case "DEPOSITO NO EFEC": // credito
                if (c.getTipoAnterior() != null && c.getTipoAnterior().equals("CREDITO NO EFEC")) {
                    conciliacionBancaria.setNotasCredNoEfectEstC(conciliacionBancaria.getNotasCredNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                }
                conciliacionBancaria.setDepositosNoEfectEstC(conciliacionBancaria.getDepositosNoEfectEstC()
                        .add(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                c.setTipoAnterior("DEPOSITO NO EFEC");
                break;
            case "ANULADO":
                asignarValoresEfectivizadoAnulado(c);
                c.setTipoReferencia("ANULADO");
                break;
        }
        JsfUtil.update("tabResumenConciliacion:containerValoresESTC");
    }

    public void asignarValoresEfectivizadoAnulado(DetalleConciliacionBancaria c) {
        if (c.getTipoAnterior() != null) {
            switch (c.getTipoAnterior()) {
                case "PAGO NO EFEC": // debito
                    conciliacionBancaria.setPagoNoEfectEstC(conciliacionBancaria.getPagoNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "DEBITO NO EFEC": // debito
                    conciliacionBancaria.setDebitoNoEfectEstC(conciliacionBancaria.getDebitoNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "CREDITO NO EFEC": // credito
                    conciliacionBancaria.setNotasCredNoEfectEstC(conciliacionBancaria.getNotasCredNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "DEPOSITO NO EFEC": // credito
                    conciliacionBancaria.setDepositosNoEfectEstC(conciliacionBancaria.getDepositosNoEfectEstC()
                            .subtract(c.getValor()).setScale(2, RoundingMode.HALF_UP));
                    break;
            }
        }
    }

    public void imprimirReporte(String tipoReporte) {
        if (validarGuardar()) {
            servletSession.borrarDatos();
            servletSession.addParametro("periodo", anio + "-" + String.format("%02d", mes));
            servletSession.addParametro("id_cuenta", cuentaBancaria.getIdCuentaMovimiento().getId());
            servletSession.setNombreSubCarpeta("conciliacionBancaria");
            servletSession.setNombreReporte("conciliacion");
            servletSession.setContentType(tipoReporte);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addErrorMessage("", "Error no se puede imprimir el reporte ");
        }
    }

    public void generarExcel() {
        String[] headers_1 = { "Fecha", "Transacción", "CP", "Beneficiario", "Detalle", "Valor", "SPI", "Estado",
                "Conciliación", "Referencia", "Fecha Referencia" };
        String[] headers_2 = { "Fecha", "Transacción", "Detalle", "Valor", "Conciliación", "Referencia",
                "Fecha Referencia" };
        String[] headers_3 = { "Fecha", "No. Referencia", "Concepto", "Tipo", "Valor" };
        String[] headers_4 = { "", "Saldo Inicial", "Débitos", "Créditos", "Saldo Final" };
        aniadirArreglo(1, "DÉBITOS LIBRO BANCOS", headers_1);
        aniadirArreglo(2, "CRÉDITOS LIBRO BANCOS", headers_2);
        aniadirArreglo(3, "REGISTRO DEL ESTADO DE CUENTA", headers_3);
        aniadirArreglo(4, "REGISTROS NO EFECTIVIZADOS DÉBITOS", headers_1);
        aniadirArreglo(5, "REGISTROS NO EFECTIVIZADOS CRÉDITOS", headers_2);
        aniadirArreglo(6, "RESUMEN CONCILIACION BANCARIA_MAYOR CONTABLE", headers_4);
        aniadirArreglo(7, "RESUMEN CONCILIACION BANCARIA _BANCO", headers_4);
        crearExcel();
    }

    private void aniadirArreglo(int code, String name, String[] arrayHead) {
        Name_Header_xlsx sheet_1 = new Name_Header_xlsx();
        sheet_1.setCodeSheet(code);
        sheet_1.setNameSheet(name);
        sheet_1.setHeardSheet(arrayHead);
        parametorsXlsx.add(sheet_1);
    }

    private void crearExcel() {
        try {
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = null;
            workbook = new SXSSFWorkbook(100);
            int colum = 0;
            for (Name_Header_xlsx object : parametorsXlsx) {
                // Recorremos las columnas del resultSet
                int rowNum = 0;
                // Creamos el libro para almacenar la data
                SXSSFSheet sheet = workbook.createSheet(object.getNameSheet());
                createNewSheet(sheet, rowNum, object.getCodeSheet(), object.getHeardSheet(), workbook);
                sheet.setAutobreaks(true);
                sheet.setFitToPage(true);
                for (int i = 0; i < object.getHeardSheet().length; i++) {
                    sheet.setColumnWidth(i, 7000);
                    // sheet.autoSizeColumn(i);
                    sheet.setAutoFilter(new CellRangeAddress(0, rowNum, 0, i));
                }
            }
            try {
                String ruta = valoresService.findByCodigo("RUTA_ARCHIVO_BALANCE_INICIAL");
                String xlxs = ruta.concat("Conciliacion_Bancaria.xlsx");
                workbook.write(new FileOutputStream(xlxs));
                workbook.close();
                servletSession.setNombreDocumento(xlxs);
                servletSession.setContentType("application/vnd.ms-sheet");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
            } catch (FileNotFoundException e) {
                Logger.getLogger(ConciliacionBancariaController.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException e) {
                Logger.getLogger(ConciliacionBancariaController.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConciliacionBancariaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createNewSheet(SXSSFSheet sheet, int rowNum, int code, String[] headers, SXSSFWorkbook workbook)
            throws SQLException {
        // obtenemos todas la columnas que hay en el resulset
        // Creamos un nuevo ROW para el header
        SXSSFRow header = sheet.createRow(rowNum);
        int colHeader = 1;
        for (String cell : headers) {
            // Creamos una nueva celda para cada una de las celdas
            SXSSFCell cellHeader = header.createCell(colHeader - 1);
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setBold(true);
            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            // agregamos el valor de la celda
            cellHeader.setCellStyle(style);
            cellHeader.setCellValue(cell.toUpperCase());
            // workbook.getSheetAt(0).autoSizeColumn(colHeader);
            colHeader++;
        }
        rowNum++;
        switch (code) {
            case 1:
                celdas(sheet, debitosLibroBanco, 1, workbook);
                break;
            case 2:
                celdas(sheet, creditosLibroBanco, 2, workbook);
                break;
            case 3:
                celdas(sheet, estadoCuenta, 3, workbook);
                break;
            case 4:
                celdas(sheet, creditoDebitosNoEfecDebitos, 1, workbook);
                break;
            case 5:
                celdas(sheet, creditoDebitosNoEfecDebitos, 1, workbook);
                break;
            default:
                aniadirResumen(sheet, workbook, code);
                break;
        }
    }

    private void celdas(SXSSFSheet sheet, List<DetalleConciliacionBancaria> list, int code, SXSSFWorkbook workbook) {
        int rowNum = 1;
        if (list == null || list.isEmpty()) {
            return;
        }
        for (DetalleConciliacionBancaria c : list) {
            SXSSFRow data = sheet.createRow(rowNum);
            int cont = 1;
            cont = ingresarDatosCell_1(c.getFecha() == null ? "" : Utils.dateFormatPattern("dd/MM/yyy", c.getFecha()),
                    data, cont, workbook);
            if (code == 1 || code == 2) {
                cont = ingresarDatosCell_3(c.getNumDiarioGeneral() == null ? 0 : c.getNumDiarioGeneral().intValue(),
                        data, cont, workbook);
                if (code == 1) {
                    cont = ingresarDatosCell_3(
                            c.getNumComprobantePago() == null ? 0 : c.getNumComprobantePago().intValue(), data, cont,
                            workbook);
                    cont = ingresarDatosCell_1(c.getBeneficiario() == null ? "" : c.getBeneficiario(), data, cont,
                            workbook);
                }
                cont = ingresarDatosCell_1(c.getDetalle() == null ? "" : c.getDetalle(), data, cont, workbook);
                cont = ingresarDatosCell_2(c.getValor(), data, cont, workbook);
                if (code == 1) {
                    cont = ingresarDatosCell_3(c.getSpi() == null ? 0 : c.getSpi().intValue(), data, cont, workbook);
                    cont = ingresarDatosCell_1(c.getEstado(), data, cont, workbook);
                }
                cont = ingresarDatosCell_1(c.getConciliacion() == null ? "" : c.getConciliacion(), data, cont,
                        workbook);
                cont = ingresarDatosCell_1(c.getReferencia() == null ? "" : c.getReferencia(), data, cont, workbook);
                cont = ingresarDatosCell_1(c.getFechaReferencia() == null ? ""
                        : Utils.dateFormatPattern("dd/MM/yyy", c.getFechaReferencia()), data, cont, workbook);
            } else {
                cont = ingresarDatosCell_1(c.getReferencia() == null ? "" : c.getReferencia().toString(), data, cont,
                        workbook);
                cont = ingresarDatosCell_1(c.getConciliacion() == null ? "" : c.getConciliacion(), data, cont,
                        workbook);
                cont = ingresarDatosCell_1(c.getTipo() == null ? "" : c.getTipo(), data, cont, workbook);
                cont = ingresarDatosCell_2(c.getValor(), data, cont, workbook);
            }
            rowNum++;
        }
    }

    private int ingresarDatosCell_1(String string, SXSSFRow data, int i, SXSSFWorkbook workbook) {
        int aux = i;
        SXSSFCell cellData = data.createCell(i - 1);
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        cellData.setCellValue(string);
        cellData.setCellStyle(style);
        return aux = aux + 1;
    }

    private int ingresarDatosCell_2(BigDecimal string, SXSSFRow data, int i, SXSSFWorkbook workbook) {
        int aux = i;
        SXSSFCell cellData = data.createCell(i - 1);
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        cellData.setCellValue(string.doubleValue());
        cellData.setCellStyle(style);
        return aux = aux + 1;
    }

    private int ingresarDatosCell_3(int valor, SXSSFRow data, int i, SXSSFWorkbook workbook) {
        int aux = i;
        SXSSFCell cellData = data.createCell(i - 1);
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        cellData.setCellValue(valor);
        cellData.setCellStyle(style);
        return aux = aux + 1;
    }

    private void aniadirResumen(SXSSFSheet sheet, SXSSFWorkbook workbook, int code) {
        SXSSFRow data;
        if (code == 6) {
            data = sheet.createRow(1);
            ingresarDatosCell_1("CONCEPTO", data, 1, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getLibroBancoSaldoInicial(), data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getLibroBancoDebito(), data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getLibroBancoCredito(), data, 4, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoLibroBanco(), data, 5, workbook);

            data = sheet.createRow(2);
            ingresarDatosCell_1("DÉBITO NO CONTABILIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getDebitosNoRegistLB(), data, 3, workbook);
            ingresarDatosCell_1("-", data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(3);
            ingresarDatosCell_1("CRÉDITO NO CONTABILIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_1("-", data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getCreditoNoEfectLB(), data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(4);
            ingresarDatosCell_1("SALDOS IGUALES", data, 1, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getLibroBancoSaldoInicial(), data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoIgualesLibroBancoDebito(), data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoIgualesLibroBanoCredito(), data, 4, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoConciliadoLB(), data, 5, workbook);
        } else {
            data = sheet.createRow(1);
            ingresarDatosCell_1("CONCEPTO", data, 1, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getEstadoCuentaSaldoInicial(), data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getEstadoCuentaDebito(), data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getEstadoCuentaCredito(), data, 4, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoCuentaMonetaria(), data, 5, workbook);

            data = sheet.createRow(2);
            ingresarDatosCell_1("DÉBITO NO EFECTIVIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getDebitoNoEfectEstC(), data, 3, workbook);
            ingresarDatosCell_1("-", data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(3);
            ingresarDatosCell_1("PAGO NO EFECTIVIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getPagoNoEfectEstC(), data, 3, workbook);
            ingresarDatosCell_1("-", data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(4);
            ingresarDatosCell_1("DEPÓSITO NO EFECTIVIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_1("-", data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getDepositosNoEfectEstC(), data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(5);
            ingresarDatosCell_1("CRÉDITO NO EFECTIVIZADO", data, 1, workbook);
            ingresarDatosCell_1("-", data, 2, workbook);
            ingresarDatosCell_1("-", data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getNotasCredNoEfectEstC(), data, 4, workbook);
            ingresarDatosCell_1("-", data, 5, workbook);

            data = sheet.createRow(6);
            ingresarDatosCell_1("SALDOS IGUALES", data, 1, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getEstadoCuentaSaldoInicial(), data, 2, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoIgualesEstadoCuentaDebito(), data, 3, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoIgualesEstadoCuentaCredito(), data, 4, workbook);
            ingresarDatosCell_2(conciliacionBancaria.getSaldoConciliadoEstC(), data, 5, workbook);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<DetalleConciliacionBancaria> getCreditoDebitosNoEfecDebitos() {
        return creditoDebitosNoEfecDebitos;
    }

    public void setCreditoDebitosNoEfecDebitos(List<DetalleConciliacionBancaria> creditoDebitosNoEfecDebitos) {
        this.creditoDebitosNoEfecDebitos = creditoDebitosNoEfecDebitos;
    }

    public List<DetalleConciliacionBancaria> getCreditoDebitosNoEfecCreditos() {
        return creditoDebitosNoEfecCreditos;
    }

    public void setCreditoDebitosNoEfecCreditos(List<DetalleConciliacionBancaria> creditoDebitosNoEfecCreditos) {
        this.creditoDebitosNoEfecCreditos = creditoDebitosNoEfecCreditos;
    }

    public List<DetalleConciliacionBancaria> getCreditoDebitosNoEfectivizados() {
        return creditoDebitosNoEfectivizados;
    }

    public void setCreditoDebitosNoEfectivizados(List<DetalleConciliacionBancaria> creditoDebitosNoEfectivizados) {
        this.creditoDebitosNoEfectivizados = creditoDebitosNoEfectivizados;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getEditableCellEdit() {
        return editableCellEdit;
    }

    public void setEditableCellEdit(Boolean editableCellEdit) {
        this.editableCellEdit = editableCellEdit;
    }

    public List<DetalleConciliacionBancaria> getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(List<DetalleConciliacionBancaria> estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public DetalleConciliacionBancaria getDetalleConciliacionBancariaEstC() {
        return detalleConciliacionBancariaEstC;
    }

    public void setDetalleConciliacionBancariaEstC(DetalleConciliacionBancaria detalleConciliacionBancariaEstC) {
        this.detalleConciliacionBancariaEstC = detalleConciliacionBancariaEstC;
    }

    public Boolean getDisabledByEstado() {
        return disabledByEstado;
    }

    public void setDisabledByEstado(Boolean disabledByEstado) {
        this.disabledByEstado = disabledByEstado;
    }

    public LazyDataModel<DetalleConciliacionBancaria> getLazyDebidosLibroBanco() {
        return lazyDebidosLibroBanco;
    }

    public void setLazyDebidosLibroBanco(LazyDataModel<DetalleConciliacionBancaria> lazyDebidosLibroBanco) {
        this.lazyDebidosLibroBanco = lazyDebidosLibroBanco;
    }

    public LazyDataModel<DetalleConciliacionBancaria> getLazyCreditosLibroBanco() {
        return lazyCreditosLibroBanco;
    }

    public void setLazyCreditosLibroBanco(LazyDataModel<DetalleConciliacionBancaria> lazyCreditosLibroBanco) {
        this.lazyCreditosLibroBanco = lazyCreditosLibroBanco;
    }

    public void setLazyCreditosLibroBanco(LazyModel<DetalleConciliacionBancaria> lazyCreditosLibroBanco) {
        this.lazyCreditosLibroBanco = lazyCreditosLibroBanco;
    }

    public List<DetalleConciliacionBancaria> getDebitosLibroBanco() {
        return debitosLibroBanco;
    }

    public void setDebitosLibroBanco(List<DetalleConciliacionBancaria> debitosLibroBanco) {
        this.debitosLibroBanco = debitosLibroBanco;
    }

    public List<DetalleConciliacionBancaria> getCreditosLibroBanco() {
        return creditosLibroBanco;
    }

    public void setCreditosLibroBanco(List<DetalleConciliacionBancaria> creditosLibroBanco) {
        this.creditosLibroBanco = creditosLibroBanco;
    }

    public ConciliacionBancaria getConciliacionBancaria() {
        return conciliacionBancaria;
    }

    public void setConciliacionBancaria(ConciliacionBancaria conciliacionBancaria) {
        this.conciliacionBancaria = conciliacionBancaria;
    }

    public ContCuentaEntidad getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(ContCuentaEntidad cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }

    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }

    public List<Integer> getAnios() {
        return anios;
    }

    public void setAnios(List<Integer> anios) {
        this.anios = anios;
    }

    public Integer getAnioMax() {
        return anioMax;
    }

    public void setAnioMax(Integer anioMax) {
        this.anioMax = anioMax;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public List<ContCuentaEntidad> getListContCuentaEntidad() {
        return listContCuentaEntidad;
    }

    public void setEdit(List<ContCuentaEntidad> listContCuentaEntidad) {
        this.listContCuentaEntidad = listContCuentaEntidad;
    }

    // </editor-fold>
}
