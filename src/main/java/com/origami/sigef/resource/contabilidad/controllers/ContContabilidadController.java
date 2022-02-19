/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.RegManualesFinanModel;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "dgContabilidadView")
@ViewScoped
public class ContContabilidadController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UserSession userSession;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PresupuestoService presService;

    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneral contDiarioGeneral;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private PartePresupuestariaModel partePresupuestariaModel;

    private LazyModel<ContDiarioGeneral> contDiarioGeneralLazy;
    private LazyModel<ContCuentas> contCuentasLazy;
    private LazyModel<Factura> facturaLazy;

    private List<Short> listaPeriodo;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete;
    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<PartePresupuestariaModel> partePresupuestariaModelList;
    private List<Factura> facturas, facturasDelete;
    private List<ContDiarioGeneralDetalle> cuentasNoExistentesList, cuentasInconsistenciasList, devengadoMayorList;
    private List<RegManualesFinanModel> partidasAgg;

    private BigDecimal totalDebe, totalHaber, totalComprometido, totalDevengado, totalEjecutado, diferencia;
    private Boolean tipoRelacion, tipoDlg, view;

    private DefaultStreamedContent downloadFormato;
    private String vistaOrigen, titulo_dialogo = "", mensajeInconsistencia;

    @PostConstruct
    public void initialize() {
        clear();
        loadLazyModel();
        formInicializar();
        loadDataRedirect();
        updateContCuentas(false, "");
        userSession.setIdDiario(null);
        listaPeriodo = catalogoItemService.getPeriodo();
        calcularTotales();
    }

    public void loadDataRedirect() {
        if (userSession.getIdDiario() != null) {
            contDiarioGeneral = ContRegistroContable.findById(userSession.getIdDiario());
            List<ContDiarioGeneralDetalle> tempList = this.ContRegistroContable.findByIdDiario(contDiarioGeneral);
            contDiarioGeneralDetallesList = new ArrayList<>();
            for (ContDiarioGeneralDetalle temp : tempList) {
                contDiarioGeneralDetallesList.add(temp);
            }
            facturas = ContRegistroContable.getFacturas(contDiarioGeneral);
            if (facturas == null || facturas.isEmpty()) {
                facturas = new ArrayList<>();
            }
            view = Utils.clone(userSession.getViewDiario());
            userSession.setIdDiario(null);
            actualizarTipol();
        }
    }

    public void setearVistaOrigen(String origen) {
        vistaOrigen = origen;
    }

    public void cargarFormato(FileUploadEvent event) {
        try {
            cuentasNoExistentesList = new ArrayList<>();
            cuentasInconsistenciasList = new ArrayList<>();
            InputStream input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = null;
            if (vistaOrigen.equals("APERTURA") || vistaOrigen == "APERTURA") {
                sheet = worbook.getSheetAt(0);
            } else if (vistaOrigen.equals("AJUSTE") || vistaOrigen == "AJUSTE") {
                sheet = worbook.getSheetAt(1);
            }
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            int next = 0;
            String tipo = "", cuenta_partida = "";
            Double debe_comprome = 0.00, haber_devengado = 0.00, pagado = 0.00;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().trim() != null) {
                        tipo = dataFormatter.formatCellValue(row.getCell(0)).trim();
                        cuenta_partida = dataFormatter.formatCellValue(row.getCell(1)).trim();
                        debe_comprome = Double.valueOf(dataFormatter.formatCellValue(row.getCell(2)).replace(",", "."));
                        haber_devengado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(3)).replace(",", "."));
                        if (tipo != null && cuenta_partida != null && debe_comprome != null && haber_devengado != null) {
                            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                            List<ContCuentas> cta = contCuentasService.findCtaContableByCodigo(cuenta_partida);

                            if (debe_comprome != 0 && haber_devengado != 0) {
                                titulo_dialogo = "CUENTAS INCONSISTENTES";
                                ContDiarioGeneralDetalle detalleInconsistencias = new ContDiarioGeneralDetalle();
                                cuenta_partida = dataFormatter.formatCellValue(row.getCell(1)).trim();
                                detalleInconsistencias.setIdContCuentas(new ContCuentas(new Long(0), cuenta_partida, tipo));
                                detalleInconsistencias.setDebe(BigDecimal.valueOf(debe_comprome));
                                detalleInconsistencias.setHaber(BigDecimal.valueOf(haber_devengado));
                                cuentasInconsistenciasList.add(detalleInconsistencias);
                                mensajeInconsistencia = "DEBE Y HABER NO PUEDEN CONTENER VALORES";
                            } else {
                                if (cta != null && cta.size() > 0) {
                                    detalle.setIdContCuentas(cta.get(0));
                                } else {
                                    ContDiarioGeneralDetalle detalleNotExist = new ContDiarioGeneralDetalle();
                                    detalleNotExist.setIdContCuentas(new ContCuentas(new Long(0), tipo, cuenta_partida));
                                    if (tipo.toLowerCase().equalsIgnoreCase("a")) {
                                        detalleNotExist.setDebe(BigDecimal.valueOf(debe_comprome));
                                        detalleNotExist.setHaber(BigDecimal.valueOf(haber_devengado));
                                        detalleNotExist.setComprometido(BigDecimal.ZERO);
                                        detalleNotExist.setDevengado(BigDecimal.ZERO);
                                        detalleNotExist.setEjecutado(BigDecimal.ZERO);
                                    } else if (tipo.toLowerCase().equalsIgnoreCase("p")) {
                                        detalleNotExist.setDebe(BigDecimal.ZERO);
                                        detalleNotExist.setHaber(BigDecimal.ZERO);
                                        detalleNotExist.setComprometido(BigDecimal.valueOf(debe_comprome));
                                        detalleNotExist.setDevengado(BigDecimal.valueOf(haber_devengado));
                                        pagado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)).replace(",", "."));
                                        detalleNotExist.setEjecutado(BigDecimal.valueOf(pagado));
                                    }
                                    cuentasNoExistentesList.add(detalleNotExist);
                                }
                                if (cta != null && cta.size() > 0) {
                                    if (!detalle.getIdContCuentas().getMovimiento()) {
                                        titulo_dialogo = "CUENTAS DE AGRUPACION";
                                        ContDiarioGeneralDetalle detalleInconsistencias = new ContDiarioGeneralDetalle();
                                        cuenta_partida = dataFormatter.formatCellValue(row.getCell(1)).trim();
                                        detalleInconsistencias.setAnio(next);
                                        detalleInconsistencias.setIdContCuentas(new ContCuentas(new Long(0), cuenta_partida, tipo));
                                        detalleInconsistencias.setDebe(BigDecimal.valueOf(debe_comprome));
                                        detalleInconsistencias.setHaber(BigDecimal.valueOf(haber_devengado));
                                        cuentasInconsistenciasList.add(detalleInconsistencias);
                                        mensajeInconsistencia = "LA CUENTA NO POSEE MOVIMIENTOS";
                                    }
                                }
                                detalle.setNumeracion(next);
                                if (tipo.toLowerCase().trim().equalsIgnoreCase("a") || tipo.toLowerCase().trim().equalsIgnoreCase("j")) {
                                    detalle.setDebe(BigDecimal.valueOf(debe_comprome));
                                    detalle.setHaber(BigDecimal.valueOf(haber_devengado));
                                    detalle.setComprometido(BigDecimal.ZERO);
                                    detalle.setDevengado(BigDecimal.ZERO);
                                    detalle.setEjecutado(BigDecimal.ZERO);
                                } else if (tipo.toLowerCase().equalsIgnoreCase("p") && row.getCell(4) != null) {
                                    pagado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)).replace(",", "."));
                                    detalle.setDebe(BigDecimal.ZERO);
                                    detalle.setHaber(BigDecimal.ZERO);
                                    detalle.setComprometido(BigDecimal.valueOf(debe_comprome));
                                    detalle.setDevengado(BigDecimal.valueOf(haber_devengado));
                                    detalle.setEjecutado(BigDecimal.valueOf(pagado));
                                }
                                contDiarioGeneralDetallesList.add(detalle);
                            }
                        }
                    }
                }
                next++;
            }
            calcularTotales();
            PrimeFaces.current().ajax().update("registroContableDetalleTable");
            contDiarioGeneral.setClase(catalogoItemService.findByCatalogoDiario("diario_general_clases", "DIARIO").get(0));
            clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
            if (tipo.toLowerCase().equalsIgnoreCase("a")) {
                contDiarioGeneral.setTipo(catalogoService.getTiposDiarioGeneralApertura("diario_general_tipos", "A").get(0));
            } else if (tipo.toLowerCase().equalsIgnoreCase("j")) {
                contDiarioGeneral.setTipo(catalogoService.getTiposDiarioGeneralApertura("diario_general_tipos", "J").get(0));
            } else if (tipo.toLowerCase().equalsIgnoreCase("p")) {
                contDiarioGeneral.setTipo(catalogoService.getTiposDiarioGeneralApertura("diario_general_tipos", "P").get(0));
            }
            actualizarTipol();
            contDiarioGeneralDetallesDelete = new ArrayList<>();
            PrimeFaces.current().ajax().update("dgClase");
            PrimeFaces.current().ajax().update("dgTipo");
            PrimeFaces.current().ajax().update("btnNuevaLinea");
            PrimeFaces.current().executeScript("PF('subirdocu').hide()");
            if (cuentasNoExistentesList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasnoexistentes').show()");
                PrimeFaces.current().ajax().update("tablactanoexist");
                limpiarCarga();
            }
            if (cuentasInconsistenciasList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasinconsistencias2').show()");
                PrimeFaces.current().ajax().update("tablactaincons2");
                limpiarCarga();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addWarningMessage("INFO!!!", "Cargue un archivo con el mismo formato y tipo de la plantilla");
        }
    }

    public void cargarFormatoCombinado(FileUploadEvent event) {
        try {
            cuentasNoExistentesList = new ArrayList<>();
            cuentasInconsistenciasList = new ArrayList<>();
            devengadoMayorList = new ArrayList<>();
            partidasAgg = new ArrayList<>();
            InputStream input = null;
            input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = null;
            if (vistaOrigen.equals("FINANCIERO") || vistaOrigen == "FINANCIERO") {
                sheet = worbook.getSheetAt(3);
            }
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            Integer next = 0, linea = 0;
            String cuenta = "", partida = "";
            Double debe = 0.00, haber = 0.00, devengado = 0.00, ejecutado = 0.00, comprometidoRegistrado = 0.00;
            BigDecimal saldoTemp = BigDecimal.ZERO;
            List<RegManualesFinanModel> tempPartidasAgg = new ArrayList<>();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                List<ContCuentas> cta;
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    linea = Integer.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(0)).trim(), 0).toString());
                    debe = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(3)).trim(), 0.00).toString().replace(",", "."));
                    haber = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(4)).trim(), 0.00).toString().replace(",", "."));
                    devengado = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(5)).trim(), 0.00).toString().replace(",", "."));
                    ejecutado = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(6)).trim(), 0.00).toString().replace(",", "."));
                    ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                    Presupuesto presupuesto = new Presupuesto();
                    if (linea != 0) {
                        if ((debe != 0 && haber != 0) || (devengado != 0 && ejecutado != 0)) {
                            ContDiarioGeneralDetalle detalleInconsistencias = new ContDiarioGeneralDetalle();
                            cuenta = dataFormatter.formatCellValue(row.getCell(1)).trim();
                            partida = dataFormatter.formatCellValue(row.getCell(2)).trim();
                            detalleInconsistencias.setAnio(linea);
                            mensajeInconsistencia = "DEBE Y HABER NO PUEDEN CONTENER VALORES";
                            addCtaInconsistente(linea, cuenta, partida, debe, haber, 0.00, devengado, ejecutado);
                        } else {
                            cuenta = nvl(dataFormatter.formatCellValue(row.getCell(1)).trim(), "").toString();
                            partida = nvl(dataFormatter.formatCellValue(row.getCell(2)).trim(), "").toString();
                            if (!cuenta.equals("")) {
                                cta = contCuentasService.findCtaContableByCodigo(cuenta);
                                if (cta != null && cta.size() > 0) {
                                    detalle.setIdContCuentas(cta.get(0));
                                    if (!partida.equals("")) {
                                        if (cuenta.startsWith("62")) {
                                            if (devengado > haber) {
                                                mensajeInconsistencia = "DEVENGADO MAYOR AL HABER";
                                                addCtaInconsistente(linea, cuenta, partida, debe, haber, 0.00, devengado, ejecutado);
                                            }
                                        }
                                        presupuesto = presService.findPresupuestoByCodigoAndPerido(partida, contDiarioGeneral.getPeriodo());
                                        if (presupuesto == null) {
                                            ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(6)).replace(",", "."));
                                            addCtasNoExits(linea, cuenta, partida, debe, haber, devengado, ejecutado);
                                        } else {
                                            if (!contDiarioGeneralDetallesList.isEmpty() && contDiarioGeneralDetallesList.size() > 0) {
                                                for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                                                    if (null != item.getPartidaPresupuestaria()) {
                                                        if (item.getPartidaPresupuestaria().equals(presupuesto.getPartida())) {
                                                            comprometidoRegistrado = comprometidoRegistrado + item.getComprometido().doubleValue();
                                                        }
                                                    }
                                                }
                                            }
                                            saldoTemp = BigDecimal.valueOf(Double.valueOf(nvl(obtSaldoDisponible(presupuesto, comprometidoRegistrado, contDiarioGeneral.getPeriodo()), BigDecimal.ZERO).toString()));
                                            detalle.setSaldoDisponible(saldoTemp);
                                            detalle.setPartidaPresupuestaria(presupuesto.getPartida());
                                            if (devengado > 0) {
                                                detalle.setTipoRegistro(catalogoItemService.findCatItemByTexto("DEVENGADO").get(0));
                                            } else if (ejecutado > 0) {
                                                detalle.setTipoRegistro(catalogoItemService.findCatItemByTexto("EJECUTADO").get(0));
                                            }
                                            if (presupuesto.getItemNew() != null) {
                                                detalle.setIdPresCatalogoPresupuestario(presupuesto.getItemNew());
                                            }
                                            if (presupuesto.getEstructruaNew() != null) {
                                                detalle.setIdPresPlanProgramatico(presupuesto.getEstructruaNew());
                                            }
                                            if (presupuesto.getFuenteNew() != null) {
                                                detalle.setIdPresFuenteFinanciamiento(presupuesto.getFuenteNew());
                                            }
                                        }
                                    }

                                    detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
                                    ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(6)).replace(",", "."));
                                    detalle.setDebe(BigDecimal.valueOf(debe));
                                    detalle.setHaber(BigDecimal.valueOf(haber));
                                    detalle.setComprometido(BigDecimal.ZERO);
                                    detalle.setDevengado(BigDecimal.valueOf(devengado));
                                    detalle.setEjecutado(BigDecimal.valueOf(ejecutado));
                                    contDiarioGeneralDetallesList.add(detalle);
                                } else {
                                    addCtasNoExits(linea, cuenta, partida, debe, haber, devengado, ejecutado);
                                }
                            }
                        }
                    }
                }
                next++;
            }
            contDiarioGeneral.setClase(catalogoItemService.findByCatalogoDiario("diario_general_clases", "DIARIO").get(0));
            clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
            contDiarioGeneral.setTipo(catalogoService.getTiposDiarioGeneralApertura("diario_general_tipos", "F").get(0));
            actualizarTipol();
            calcularTotales();
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("dgClase");
            PrimeFaces.current().ajax().update("dgTipo");
            PrimeFaces.current().ajax().update("btnNuevaLinea");
            PrimeFaces.current().executeScript("PF('subirdocu2').hide()");
            if (cuentasNoExistentesList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasnoexistentes2').show()");
                PrimeFaces.current().ajax().update("tablactanoexist2");
                limpiarCarga();
            }
            if (cuentasInconsistenciasList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasinconsistencias').show()");
                PrimeFaces.current().ajax().update("tablactaincons");
                limpiarCarga();
            }
        } catch (IOException ex) {
            Logger.getLogger(ContContabilidadController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addWarningMessage("INFO!!!", "Cargue un archivo con el mismo formato y tipo de la plantilla");
        }
    }

    public void cargarFormatoCombinadoOld(FileUploadEvent event) {
        try {
            cuentasNoExistentesList = new ArrayList<>();
            cuentasInconsistenciasList = new ArrayList<>();
            InputStream input = null;
            input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = null;
            if (vistaOrigen.equals("FINANCIERO") || vistaOrigen == "FINANCIERO") {
                sheet = worbook.getSheetAt(3);
            }
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            Integer next = 0, linea = 0;
            String cuenta = "", partida = "";
            Double debe = 0.00, haber = 0.00, devengado = 0.00, ejecutado = 0.00, comprometidoRegistrado = 0.00;
            BigDecimal saldoTemp = BigDecimal.ZERO;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                List<ContCuentas> cta;
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    linea = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    debe = Double.valueOf(dataFormatter.formatCellValue(row.getCell(3)).replace(",", "."));
                    haber = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)).replace(",", "."));
                    devengado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(5)).replace(",", "."));
                    ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(6)).replace(",", "."));
                    ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                    Presupuesto presupuesto = new Presupuesto();
                    if ((debe != 0 && haber != 0) || (devengado != 0 && ejecutado != 0)) {
                        ContDiarioGeneralDetalle detalleInconsistencias = new ContDiarioGeneralDetalle();
                        cuenta = dataFormatter.formatCellValue(row.getCell(1)).trim();
                        partida = dataFormatter.formatCellValue(row.getCell(2)).trim();
                        detalleInconsistencias.setAnio(linea);//CAMPO LINEA DEL FORMATO
                        detalleInconsistencias.setIdContCuentas(new ContCuentas(new Long(0), cuenta, partida));//SE USA EL MODELO ContCuentas UNICAMENTE PARA MOSTRAR LAS CUENTAS QUE NO EXISTEN
                        detalleInconsistencias.setDebe(BigDecimal.valueOf(debe));
                        detalleInconsistencias.setHaber(BigDecimal.valueOf(haber));
                        detalleInconsistencias.setComprometido(BigDecimal.ZERO);
                        detalleInconsistencias.setDevengado(BigDecimal.valueOf(devengado));
                        detalleInconsistencias.setEjecutado(BigDecimal.valueOf(ejecutado));
                        cuentasInconsistenciasList.add(detalleInconsistencias);
                    } else {

                        cuenta = nvl(dataFormatter.formatCellValue(row.getCell(1)).trim(), "").toString();
                        partida = nvl(dataFormatter.formatCellValue(row.getCell(2)).trim(), "").toString();

                        if (cuenta != "") {
                            cta = contCuentasService.findCtaContableByCodigo(cuenta);
                            if (cta != null && cta.size() > 0) {
                                detalle.setIdContCuentas(cta.get(0));
                            } else {
                                addCtasNoExits(linea, cuenta, partida, debe, haber, devengado, ejecutado);
                            }
                        }
                        if (partida != "") {
                            presupuesto = presService.findPresupuestoByCodigoAndPerido(partida, contDiarioGeneral.getPeriodo());
                            if (presupuesto == null) {
                                ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(6)).replace(",", "."));
                                addCtasNoExits(linea, cuenta, partida, debe, haber, devengado, ejecutado);
                            } else {
                                if (!contDiarioGeneralDetallesList.isEmpty() && contDiarioGeneralDetallesList.size() > 0) {
                                    for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                                        if (null != item.getPartidaPresupuestaria()) {
                                            if (item.getPartidaPresupuestaria().equals(presupuesto.getPartida())) {
                                                comprometidoRegistrado = comprometidoRegistrado + item.getComprometido().doubleValue();
                                            }
                                        }
                                    }
                                }
                                saldoTemp = BigDecimal.valueOf(Double.valueOf(nvl(obtSaldoDisponible(presupuesto, comprometidoRegistrado, contDiarioGeneral.getPeriodo()), BigDecimal.ZERO).toString()));
                                detalle.setSaldoDisponible(saldoTemp);
                                detalle.setPartidaPresupuestaria(presupuesto.getPartida());
                                if (presupuesto.getItemNew() != null) {
                                    detalle.setIdPresCatalogoPresupuestario(presupuesto.getItemNew());
                                }
                                if (presupuesto.getEstructruaNew() != null) {
                                    detalle.setIdPresPlanProgramatico(presupuesto.getEstructruaNew());
                                }
                                if (presupuesto.getFuenteNew() != null) {
                                    detalle.setIdPresFuenteFinanciamiento(presupuesto.getFuenteNew());
                                }
                                detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
                                ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(6)).replace(",", "."));
                                detalle.setDebe(BigDecimal.valueOf(debe));
                                detalle.setHaber(BigDecimal.valueOf(haber));
                                detalle.setComprometido(BigDecimal.ZERO);
                                detalle.setDevengado(BigDecimal.valueOf(devengado));
                                detalle.setEjecutado(BigDecimal.valueOf(ejecutado));
                            }
                        }
                        contDiarioGeneralDetallesList.add(detalle);
                    }
                }
                next++;
            }
            contDiarioGeneral.setClase(catalogoItemService.findByCatalogoDiario("diario_general_clases", "DIARIO").get(0));
            clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
            contDiarioGeneral.setTipo(catalogoService.getTiposDiarioGeneralApertura("diario_general_tipos", "F").get(0));
            calcularTotales();
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("dgClase");
            PrimeFaces.current().ajax().update("dgTipo");
            PrimeFaces.current().ajax().update("btnNuevaLinea");
            PrimeFaces.current().executeScript("PF('subirdocu2').hide()");
            if (cuentasNoExistentesList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasnoexistentes2').show()");
                PrimeFaces.current().ajax().update("tablactanoexist2");
                limpiarCarga();
            }
            if (cuentasInconsistenciasList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasinconsistencias').show()");
                PrimeFaces.current().ajax().update("tablactaincons");
                limpiarCarga();
            }
        } catch (IOException ex) {
            Logger.getLogger(ContContabilidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCtaInconsistente(Integer linea, String cuenta, String partida, Double debe, Double haber, Double comprometido, Double devengado, Double ejecutado) {
        ContDiarioGeneralDetalle detalleInconsistencias = new ContDiarioGeneralDetalle();
        detalleInconsistencias.setAnio(linea);
        detalleInconsistencias.setIdContCuentas(new ContCuentas(new Long(0), cuenta, partida));
        detalleInconsistencias.setDebe(BigDecimal.valueOf(debe));
        detalleInconsistencias.setHaber(BigDecimal.valueOf(haber));
        detalleInconsistencias.setComprometido(BigDecimal.valueOf(comprometido));
        detalleInconsistencias.setDevengado(BigDecimal.valueOf(devengado));
        detalleInconsistencias.setEjecutado(BigDecimal.valueOf(ejecutado));
        cuentasInconsistenciasList.add(detalleInconsistencias);
    }

    public void addCtasNoExits(Object linea, String cuenta, String partida, Double debe, Double haber, Double devengado, Double ejecutado) {
        ContDiarioGeneralDetalle detalleNotExist = new ContDiarioGeneralDetalle();
        if (linea.equals("0")) {
            detalleNotExist.setObservacion(linea.toString());
            detalleNotExist.setPartidaPresupuestaria(partida);
        } else {
            detalleNotExist.setAnio(Integer.valueOf(linea.toString()));//CAMPO LINEA DEL FORMATO
            detalleNotExist.setIdContCuentas(new ContCuentas(new Long(0), cuenta, partida));//SE USA EL MODELO ContCuentas UNICAMENTE PARA MOSTRAR LAS CUENTAS QUE NO EXISTEN
        }
        detalleNotExist.setDebe(BigDecimal.valueOf(debe));
        detalleNotExist.setHaber(BigDecimal.valueOf(haber));
        detalleNotExist.setComprometido(BigDecimal.ZERO);
        detalleNotExist.setDevengado(BigDecimal.valueOf(devengado));
        detalleNotExist.setEjecutado(BigDecimal.valueOf(ejecutado));
        cuentasNoExistentesList.add(detalleNotExist);
    }

    public BigDecimal obtSaldoDisponible(Presupuesto pres, Double compro, Short peri) {
        BigDecimal resultado = BigDecimal.ZERO;
        resultado = pres.getCodificado().subtract(BigDecimal.valueOf(compro)).subtract(BigDecimal.valueOf(ContRegistroContable.saldoDisponible(pres.getPartida(), peri)));
        if (resultado == BigDecimal.ZERO || resultado == null) {
            return BigDecimal.ZERO;
        }
        return resultado;
    }

    public static Object nvl(Object expr1, Object expr2) {
        return (null != expr1) ? expr1 : expr2;
    }

    public void limpiarCarga() {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetallesList.clear();
        calcularTotales();
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void prepDownload() throws Exception {
        File file = new File(SisVars.RUTA_FORMATS_REGISTROS_CONTABLES + "/formato_contabilidad_modulos.xlsx");
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDownloadFormato(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
    }

    public void setDownloadFormato(DefaultStreamedContent download) {
        this.downloadFormato = download;
    }

    public DefaultStreamedContent getDownloadFormato() throws Exception {
        return downloadFormato;
    }

    public void loadLazyModel() {
        if (opcionBusqueda.getAnio() != null) {
            contDiarioGeneralLazy = new LazyModel<>(ContDiarioGeneral.class);
            contDiarioGeneralLazy.getSorteds().put("numRegistro", "ASC");
            contDiarioGeneralLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            contDiarioGeneralLazy.getFilterss().put("revisado", true);
            contDiarioGeneralLazy.getFilterss().put("estado", true);
        } else {
            contDiarioGeneralLazy = null;
        }
    }

    public void form(ContDiarioGeneral contDiarioGeneral, Boolean editView) {
        userSession.setIdDiario(contDiarioGeneral.getId());
        userSession.setViewDiario(editView);
        if (null != contDiarioGeneral.getCodModulo()) {
            switch (contDiarioGeneral.getCodModulo()) {
                case 0:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/certificacion/presupuestaria");
                    break;
                case 1:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/registos/manuales");
                    break;
                case 2:
                    break;
                case 3:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/talento/humano");
                    break;
                case 4:
                case 5:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/bienes");
                    break;
                case 6:
                case 7:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/inventario");
                    break;
                case 8:
                    JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/presupuesto");
                    break;
                default:
                    break;
            }
        }
    }

    public void reporte(ContDiarioGeneral contDiarioGeneral, String tipoDocumento) {
        ContRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
    }

    public void anular(ContDiarioGeneral contDiarioGeneral) {
        ContRegistroContable.ContabilidadAnular(contDiarioGeneral);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha anulado correctamente");
    }

    public void clear() {
        opcionBusqueda = new OpcionBusqueda();
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
    }

    public void formInicializar() {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
        tiposDiarioGeneral = new ArrayList<>();
        view = true;
        facturas = new ArrayList<>();
        facturasDelete = new ArrayList<>();
        calcularTotales();
    }

    public void actualizarTipol() {
        if (contDiarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(contDiarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void btnSearchContCuenta() {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
        contDiarioGeneralDetallesList.add(detalle);
    }

    public void searchContCuenta(ContDiarioGeneralDetalle detalle) {
        contDiarioGeneralDetalle = detalle;
        contDiarioGeneralDetalle.setIdContCuentas(contCuentasService.findCodigo(detalle.getCodCuentaInsert()));
        if (contDiarioGeneralDetalle.getIdContCuentas() != null) {
            if (!contDiarioGeneralDetalle.getIdContCuentas().getMovimiento()) {
                contDiarioGeneralDetalle.setIdContCuentas(null);
                updateContCuentas(true, detalle.getCodCuentaInsert());
                openDlgCuentas(true);
            }
        } else {
            updateContCuentas(true, detalle.getCodCuentaInsert());
            openDlgCuentas(true);
        }
    }

    public void updateContCuentas(Boolean accion, String code) {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        if (accion) {
            contCuentasLazy.getFilterss().put("codigo:startsWith", code);
        }
    }

    public void openDlgCuentas(Boolean accion) {
        tipoDlg = accion;
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void selectContCuenta(ContCuentas contCuentas) {
        try {
            if (tipoDlg) {
                contDiarioGeneralDetalle.setIdContCuentas(contCuentas);
            } else {
                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                detalle.setIdContCuentas(contCuentas);
                detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
                contDiarioGeneralDetallesList.add(detalle);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar cta. contable", e);
        }
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    private void valoresCero(ContDiarioGeneralDetalle detalle) {
        detalle.setComprometido(BigDecimal.ZERO);
        detalle.setDevengado(BigDecimal.ZERO);
        detalle.setEjecutado(BigDecimal.ZERO);
        detalle.setPartidaPresupuestaria(null);
        detalle.setIdPresCatalogoPresupuestario(null);
        detalle.setIdPresPlanProgramatico(null);
        detalle.setIdPresFuenteFinanciamiento(null);
        detalle.setTipoRegistro(null);
        partePresupuestariaModelList = new ArrayList<>();
        partePresupuestariaModel = null;

    }

    public void determinarRelacionPresupuestaria(ContDiarioGeneralDetalle detalle, boolean tipoRegistro) {
        if (contDiarioGeneral.getTipo() == null || contDiarioGeneral.getClase() == null) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            JsfUtil.addErrorMessage("AVISO!!!", "Debe seleccionar el tipo y la clase");
            return;
        }
        contDiarioGeneralDetalle = detalle;
        valoresCero(contDiarioGeneralDetalle);
        if (tipoRegistro) {
            detalle.setHaber(BigDecimal.ZERO);
        } else {
            detalle.setDebe(BigDecimal.ZERO);
        }
        relacion(tipoRegistro);
        calcularTotales();
    }

    private void relacion(Boolean accion) {
        partePresupuestariaModel = null;
        if (contDiarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
            tipoRelacion = accion;
            if (tipoRelacion) {
                String[] arrayDebe = valoresService.findByCodigo(CONFIG.RESTRINGIR_DEBE).split(",");
                for (String text : arrayDebe) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            } else {
                String[] arrayHaber = valoresService.findByCodigo(CONFIG.RESTRINGIR_HABER).split(",");
                for (String text : arrayHaber) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            }
            System.out.println("ENTRO: " + partePresupuestariaModelList);
            partePresupuestariaModelList = ContRegistroContable.relacionPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral, tipoRelacion);
            System.out.println("SALIO: " + partePresupuestariaModelList.size());
            if (!partePresupuestariaModelList.isEmpty()) {
                if (partePresupuestariaModelList.size() > 1) {
                    openDlgRelaciones();
                } else {
                    partePresupuestariaModel = partePresupuestariaModelList.get(0);
                    guardarRelacionesPresupuestarias();
                }
            }
        }
    }

    private void openDlgRelaciones() {
        if (!partePresupuestariaModelList.isEmpty()) {
            if (partePresupuestariaModelList.size() > 1) {
                PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
            } else {
                partePresupuestariaModel = partePresupuestariaModelList.get(0);
                guardarRelacionesPresupuestarias();
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        String msj = ContRegistroContable.guardarRelacionesPresupuestarias(partePresupuestariaModel, contDiarioGeneralDetalle, tipoRelacion);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        calcularTotales();
        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
    }

    public void calcularTotales() {
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        if (!contDiarioGeneralDetallesList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                totalDebe = totalDebe.add(item.getDebe()).setScale(2, RoundingMode.HALF_UP);
                totalHaber = totalHaber.add(item.getHaber()).setScale(2, RoundingMode.HALF_UP);
                totalComprometido = totalComprometido.add(item.getComprometido()).setScale(2, RoundingMode.HALF_UP);
                totalDevengado = totalDevengado.add(item.getDevengado()).setScale(2, RoundingMode.HALF_UP);
                totalEjecutado = totalEjecutado.add(item.getEjecutado()).setScale(2, RoundingMode.HALF_UP);
            }
        }
        diferencia = totalDebe.subtract(totalHaber);
        if (totalDebe.equals(totalHaber)) {
            contDiarioGeneral.setCuadrado(Boolean.TRUE);
        } else {
            contDiarioGeneral.setCuadrado(Boolean.FALSE);
        }
    }

    public void saveDiario() {
        contDiarioGeneral.setDebe(totalDebe);
        contDiarioGeneral.setHaber(totalHaber);
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_MANUALES);
        String mensaje = ContRegistroContable.validaciones(contDiarioGeneral, contDiarioGeneralDetallesList);
        if (mensaje.equals("")) {
            if (contDiarioGeneral.getId() != null) {
                ContRegistroContable.edit(contDiarioGeneral, contDiarioGeneralDetallesList, contDiarioGeneralDetallesDelete);
            } else {
                contDiarioGeneral = ContRegistroContable.create(contDiarioGeneral, contDiarioGeneralDetallesList, null, true);
            }
            ContRegistroContable.saveEditFacturas(facturas, facturasDelete, contDiarioGeneral);
            JsfUtil.executeJS("PF('dlgConfirmacion').show()");
            PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
    }

    public void openConfirmacion(int code, String tipoDocumento) {
        switch (code) {
            case 1:
                JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/manual");
                break;
            case 2:
                System.out.println("CODE: " + contDiarioGeneral.getId());
                ContRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
                break;
            default:
                formInicializar();
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

    public void deleteRegistro(ContDiarioGeneralDetalle detalle, int indice) {
        if (detalle.getId() != null) {
            contDiarioGeneralDetallesDelete.add(detalle);
            contDiarioGeneralDetallesList.remove(detalle);
        } else {
            contDiarioGeneralDetallesList.remove(indice);
        }
        actualizarList();
        calcularTotales();
    }

    public void actualizarList() {
        int aux = 1;
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
            item.setNumeracion(aux);
            aux += 1;
        }
    }

    public void openDlgFactura() {
        facturaLazy = new LazyModel<>(Factura.class);
        facturaLazy.getSorteds().put("numFactura", "ASC");
        facturaLazy.getFilterss().put("estado", true);
        facturaLazy.getFilterss().put("retenida", false);
        PrimeFaces.current().ajax().update("facturaTable");
        JsfUtil.executeJS("PF('dlgFactura').show()");
    }

    public void loadFacturas() {
        if (!facturas.isEmpty()) {
            contDiarioGeneralDetallesList = ContRegistroContable.getaddCuentas(contDiarioGeneralDetallesList, facturas);
        }
        calcularTotales();
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha agregado correctamente");
        PrimeFaces.current().ajax().update("facturaTableView");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
        JsfUtil.executeJS("PF('dlgFactura').hide()");
    }

    public void deleteFactura(Factura factura) {
        facturasDelete.add(factura);
        facturas.remove(factura);
        List<ContDiarioGeneralDetalle> aux = Utils.clone(contDiarioGeneralDetallesList);
        for (ContDiarioGeneralDetalle item : aux) {
            if (item.getFactura()) {
                if (item.getId() != null) {
                    contDiarioGeneralDetallesDelete.add(item);
                }
                contDiarioGeneralDetallesList.remove(item);
            }
        }
        loadFacturas();
    }

    public String getMensajeInconsistencia() {
        return mensajeInconsistencia;
    }

    public void setMensajeInconsistencia(String mensajeInconsistencia) {
        this.mensajeInconsistencia = mensajeInconsistencia;
    }

    public List<ContDiarioGeneralDetalle> getDevengadoMayorList() {
        return devengadoMayorList;
    }

    public void setDevengadoMayorList(List<ContDiarioGeneralDetalle> devengadoMayorList) {
        this.devengadoMayorList = devengadoMayorList;
    }

    public List<RegManualesFinanModel> getPartidasAgg() {
        return partidasAgg;
    }

    public void setPartidasAgg(List<RegManualesFinanModel> partidasAgg) {
        this.partidasAgg = partidasAgg;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ContDiarioGeneral> getContDiarioGeneralLazy() {
        return contDiarioGeneralLazy;
    }

    public void setContDiarioGeneralLazy(LazyModel<ContDiarioGeneral> contDiarioGeneralLazy) {
        this.contDiarioGeneralLazy = contDiarioGeneralLazy;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ContDiarioGeneral getContDiarioGeneral() {
        return contDiarioGeneral;
    }

    public void setContDiarioGeneral(ContDiarioGeneral contDiarioGeneral) {
        this.contDiarioGeneral = contDiarioGeneral;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetallesList() {
        return contDiarioGeneralDetallesList;
    }

    public void setContDiarioGeneralDetallesList(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList) {
        this.contDiarioGeneralDetallesList = contDiarioGeneralDetallesList;
    }

    public List<CatalogoItem> getClasesDiarioGeneral() {
        return clasesDiarioGeneral;
    }

    public void setClasesDiarioGeneral(List<CatalogoItem> clasesDiarioGeneral) {
        this.clasesDiarioGeneral = clasesDiarioGeneral;
    }

    public List<CatalogoItem> getTiposDiarioGeneral() {
        return tiposDiarioGeneral;
    }

    public void setTiposDiarioGeneral(List<CatalogoItem> tiposDiarioGeneral) {
        this.tiposDiarioGeneral = tiposDiarioGeneral;
    }

    public List<PartePresupuestariaModel> getPartePresupuestariaModelList() {
        return partePresupuestariaModelList;
    }

    public void setPartePresupuestariaModelList(List<PartePresupuestariaModel> partePresupuestariaModelList) {
        this.partePresupuestariaModelList = partePresupuestariaModelList;
    }

    public CatalogoItemService getCatalogoItemService() {
        return catalogoItemService;
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }

    public ContRegistroContable getContRegistroContable() {
        return ContRegistroContable;
    }

    public void setContRegistroContable(ContRegistroContable ContRegistroContable) {
        this.ContRegistroContable = ContRegistroContable;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ValoresService getValoresService() {
        return valoresService;
    }

    public void setValoresService(ValoresService valoresService) {
        this.valoresService = valoresService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public ContCuentasService getContCuentasService() {
        return contCuentasService;
    }

    public void setContCuentasService(ContCuentasService contCuentasService) {
        this.contCuentasService = contCuentasService;
    }

    public ContDiarioGeneralDetalle getContDiarioGeneralDetalle() {
        return contDiarioGeneralDetalle;
    }

    public void setContDiarioGeneralDetalle(ContDiarioGeneralDetalle contDiarioGeneralDetalle) {
        this.contDiarioGeneralDetalle = contDiarioGeneralDetalle;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetallesDelete() {
        return contDiarioGeneralDetallesDelete;
    }

    public void setContDiarioGeneralDetallesDelete(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete) {
        this.contDiarioGeneralDetallesDelete = contDiarioGeneralDetallesDelete;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public String getVistaOrigen() {
        return vistaOrigen;
    }

    public void setVistaOrigen(String vistaOrigen) {
        this.vistaOrigen = vistaOrigen;
    }

    public PartePresupuestariaModel getPartePresupuestariaModel() {
        return partePresupuestariaModel;
    }

    public void setPartePresupuestariaModel(PartePresupuestariaModel partePresupuestariaModel) {
        this.partePresupuestariaModel = partePresupuestariaModel;
    }

    public List<ContDiarioGeneralDetalle> getCuentasNoExistentesList() {
        return cuentasNoExistentesList;
    }

    public void setCuentasNoExistentesList(List<ContDiarioGeneralDetalle> cuentasNoExistentesList) {
        this.cuentasNoExistentesList = cuentasNoExistentesList;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public List<ContDiarioGeneralDetalle> getCuentasInconsistenciasList() {
        return cuentasInconsistenciasList;
    }

    public void setCuentasInconsistenciasList(List<ContDiarioGeneralDetalle> cuentasInconsistenciasList) {
        this.cuentasInconsistenciasList = cuentasInconsistenciasList;
    }

    public String getTitulo_dialogo() {
        return titulo_dialogo;
    }

    public void setTitulo_dialogo(String titulo_dialogo) {
        this.titulo_dialogo = titulo_dialogo;
    }

    public LazyModel<Factura> getFacturaLazy() {
        return facturaLazy;
    }

    public void setFacturaLazy(LazyModel<Factura> facturaLazy) {
        this.facturaLazy = facturaLazy;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
}
