/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
 * @author Enrique Poveda
 */
@Named(value = "dgPresupuestoView")
@ViewScoped
public class ContContabilidadPresupuestoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private UserSession userSession;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PresupuestoService presService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;

    private ContDiarioGeneral contDiarioGeneral;
    private OpcionBusqueda opcionBusqueda;
    private DefaultStreamedContent downloadFormato;

    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete;
    private List<Short> listaPeriodo;
    private List<ContDiarioGeneralDetalle> cuentasNoExistentesList;

    private LazyModel<Presupuesto> presupuestoLazyModel;

    private BigDecimal totalSaldoDisponible, totalComprometido, totalDevengado, totalEjecutado;

    private Boolean view;

    private String vistaOrigen;

    @PostConstruct
    public void initialize() {
        formInicializar();
        loadDataRedirect();
        userSession.setIdDiario(null);
        listaPeriodo = catalogoItemService.getPeriodo();
        calcularTotales();
    }

    public void formInicializar() {
        opcionBusqueda = new OpcionBusqueda();
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
        view = true;
        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_presupuesto"));
    }

    public void loadDataRedirect() {
        if (userSession.getIdDiario() != null) {
            contDiarioGeneral = ContRegistroContable.findById(userSession.getIdDiario());
            List<ContDiarioGeneralDetalle> tempList = this.ContRegistroContable.findByIdDiario(contDiarioGeneral);
            contDiarioGeneralDetallesList = new ArrayList<>();
            for (ContDiarioGeneralDetalle temp : tempList) {
                contDiarioGeneralDetallesList.add(temp);
            }
            view = Utils.clone(userSession.getViewDiario());
            userSession.setIdDiario(null);
        }
    }

    public void prepDownload() throws Exception {
        File file = new File(SisVars.RUTA_FORMATS_REGISTROS_CONTABLES + "/formato_presupuesto.xlsx");
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

    public void setearVistaOrigen(String origen) {
        vistaOrigen = origen;
    }

    public static Object nvl(Object expr1, Object expr2) {
        return (null != expr1) ? expr1 : expr2;
    }

    public void cargarFormato(FileUploadEvent event) {
        try {
            cuentasNoExistentesList = new ArrayList<>();
            InputStream input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = null;
            if (vistaOrigen.equals("PRESUPUESTO") || vistaOrigen == "PRESUPUESTO") {
                sheet = worbook.getSheetAt(2);
            }
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            int next = 0;
            String tipo = "", cuenta_partida = "";
            Double debe_comprome = 0.00, haber_devengado = 0.00, ejecutado = 0.00, comprometidoRegistrado = 0.00;
            BigDecimal saldoTemp = BigDecimal.ZERO;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().trim() != null) {
                        tipo = dataFormatter.formatCellValue(row.getCell(0)).trim();
                        cuenta_partida = dataFormatter.formatCellValue(row.getCell(1)).trim();
                        debe_comprome = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(2)).replace(",", "."), 0.00).toString());
                        haber_devengado = Double.valueOf(nvl(dataFormatter.formatCellValue(row.getCell(3)).replace(",", "."), 0.00).toString());
                        if (tipo != null && cuenta_partida != null && debe_comprome != null && haber_devengado != null) {
                            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                            Presupuesto presupuesto = presService.findPresupuestoByCodigoAndPerido(cuenta_partida, contDiarioGeneral.getPeriodo());//contCuentasService.findCtaContableByCodigo(cuenta_partida);
                            if (presupuesto == null) {
                                ContDiarioGeneralDetalle detalleNotExist = new ContDiarioGeneralDetalle();
                                detalleNotExist.setObservacion(tipo);
                                detalleNotExist.setPartidaPresupuestaria(cuenta_partida);
                                if (tipo.toLowerCase().equalsIgnoreCase("p")) {
                                    detalleNotExist.setDebe(BigDecimal.ZERO);
                                    detalleNotExist.setHaber(BigDecimal.ZERO);
                                    detalleNotExist.setComprometido(BigDecimal.valueOf(debe_comprome));
                                    detalleNotExist.setDevengado(BigDecimal.valueOf(haber_devengado));
                                    ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)).replace(",", "."));
                                    detalleNotExist.setEjecutado(BigDecimal.valueOf(ejecutado));
                                }
                                cuentasNoExistentesList.add(detalleNotExist);
                            } else {
                                if (!contDiarioGeneralDetallesList.isEmpty()) {
                                    for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                                        if (item.getPartidaPresupuestaria().equals(presupuesto.getPartida())) {
                                            comprometidoRegistrado = comprometidoRegistrado + item.getComprometido().doubleValue();
                                        }
                                    }
                                }
                                saldoTemp = obtSaldoDisponible(presupuesto, comprometidoRegistrado, contDiarioGeneral.getPeriodo());
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
                                if (tipo.toLowerCase().equalsIgnoreCase("p") && row.getCell(4) != null) {
                                    ejecutado = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)).replace(",", "."));
                                    detalle.setDebe(BigDecimal.ZERO);
                                    detalle.setHaber(BigDecimal.ZERO);
                                    detalle.setComprometido(BigDecimal.valueOf(debe_comprome));
                                    detalle.setDevengado(BigDecimal.valueOf(haber_devengado));
                                    detalle.setEjecutado(BigDecimal.valueOf(ejecutado));
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
            contDiarioGeneralDetallesDelete = new ArrayList<>();
            PrimeFaces.current().ajax().update("dgClase");
            PrimeFaces.current().ajax().update("dgTipo");
            PrimeFaces.current().executeScript("PF('subirdocu').hide()");
            if (cuentasNoExistentesList.size() > 0) {
                PrimeFaces.current().executeScript("PF('cuentasnoexistentes').show()");
                PrimeFaces.current().ajax().update("tablactanoexist");
                limpiarCarga();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addWarningMessage("INFO!!!", "Cargue un archivo con el mismo formato y tipo de la plantilla");
        }
    }

    public BigDecimal obtSaldoDisponible(Presupuesto pres, Double compro, Short peri) {
        BigDecimal resultado = BigDecimal.ZERO;
        resultado = pres.getCodificado().subtract(BigDecimal.valueOf(compro)).subtract(BigDecimal.valueOf(ContRegistroContable.saldoDisponible(pres.getPartida(), peri))).subtract(obtieneTotalReservado(pres.getPartida()));
        if (resultado == BigDecimal.ZERO || resultado == null) {
            return BigDecimal.ZERO;
        }
        return resultado;
    }

    public BigDecimal obtieneTotalReservado(String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalReservado(partida);
        return resultado;
    }

    public void limpiarCarga() {
        contDiarioGeneralDetallesList.clear();
        calcularTotales();
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void openDlgPresupuestos() {
        this.presupuestoLazyModel = new LazyModel<>(Presupuesto.class);
        this.presupuestoLazyModel.addSorted("tipo", "DESC");
        this.presupuestoLazyModel.addSorted("partida", "ASC");
        this.presupuestoLazyModel.getFilterss().put("periodo", contDiarioGeneral.getPeriodo());
        PrimeFaces.current().executeScript("PF('presupuestoDlg').show()");
        PrimeFaces.current().ajax().update("presupuestoForm");
    }

    public void aniadirPresupuesto(Presupuesto presupuesto) {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        double comprometidoRegistrado = 0;
        if (!contDiarioGeneralDetallesList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                if (item.getPartidaPresupuestaria().equals(presupuesto.getPartida())) {
                    comprometidoRegistrado = comprometidoRegistrado + item.getComprometido().doubleValue();
                }
            }
        }
        detalle.setSaldoDisponible(new BigDecimal(presupuesto.getCodificado().doubleValue() - comprometidoRegistrado - ContRegistroContable.saldoDisponible(presupuesto.getPartida(), contDiarioGeneral.getPeriodo())));
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
        contDiarioGeneralDetallesList.add(detalle);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('presupuestoDlg').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void montosPresupuestario(ContDiarioGeneralDetalle detalle, int valor) {
        switch (valor) {
            case 1:
                if (Utils.redondearDosDecimales(detalle.getComprometido().doubleValue()) > Utils.redondearDosDecimales(detalle.getSaldoDisponible().doubleValue())) {
                    JsfUtil.addWarningMessage("AVISO!!!", "El valor ingresado en el comprometido es mayor al saldo disponible");
                    detalle.setComprometido(BigDecimal.ZERO);
                }
                break;
            case 2:
                if (detalle.getIdPresPlanProgramatico() != null) {
                    if (Utils.redondearDosDecimales(detalle.getDevengado().doubleValue()) > Utils.redondearDosDecimales(detalle.getComprometido().doubleValue())) {
                        JsfUtil.addWarningMessage("AVISO!!!", "El valor ingresado en el devengado es mayor al valor comprometido");
                        detalle.setDevengado(BigDecimal.ZERO);
                    }
                }
                break;
            case 3:
                JsfUtil.addSuccessMessage("INFO!!", "Se registro exitosamente el valor ejecutado sin valor comprometido ni devengado");
//                if (Utils.redondearDosDecimales(detalle.getEjecutado().doubleValue()) > Utils.redondearDosDecimales(detalle.getDevengado().doubleValue())) {
//                    JsfUtil.addWarningMessage("AVISO!!!", "El valor ingresado en el ejecutado es mayor al valor devengado");
//                    detalle.setEjecutado(BigDecimal.ZERO);
//                }
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

    public void calcularTotales() {
        totalSaldoDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        if (!contDiarioGeneralDetallesList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                totalSaldoDisponible = totalSaldoDisponible.add(item.getSaldoDisponible()).setScale(2, RoundingMode.HALF_UP);
                totalComprometido = totalComprometido.add(item.getComprometido()).setScale(2, RoundingMode.HALF_UP);
                totalDevengado = totalDevengado.add(item.getDevengado()).setScale(2, RoundingMode.HALF_UP);
                totalEjecutado = totalEjecutado.add(item.getEjecutado()).setScale(2, RoundingMode.HALF_UP);
            }
        }
        contDiarioGeneral.setCuadrado(Boolean.TRUE);
    }

    public void saveDiario() {
        contDiarioGeneral.setDebe(BigDecimal.ZERO);
        contDiarioGeneral.setHaber(BigDecimal.ZERO);
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_PRESUPUESTO);
        String mensaje = ContRegistroContable.validaciones(contDiarioGeneral, contDiarioGeneralDetallesList);
        if (mensaje.equals("")) {
            if (contDiarioGeneral.getId() != null) {
                ContRegistroContable.edit(contDiarioGeneral, contDiarioGeneralDetallesList, contDiarioGeneralDetallesDelete);
            } else {
                contDiarioGeneral = ContRegistroContable.create(contDiarioGeneral, contDiarioGeneralDetallesList, null, true);
            }
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

    public BigDecimal getTotalSaldoDisponible() {
        return totalSaldoDisponible;
    }

    public void setTotalSaldoDisponible(BigDecimal totalSaldoDisponible) {
        this.totalSaldoDisponible = totalSaldoDisponible;
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

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<ContDiarioGeneralDetalle> getCuentasNoExistentesList() {
        return cuentasNoExistentesList;
    }

    public void setCuentasNoExistentesList(List<ContDiarioGeneralDetalle> cuentasNoExistentesList) {
        this.cuentasNoExistentesList = cuentasNoExistentesList;
    }

    public String getVistaOrigen() {
        return vistaOrigen;
    }

    public void setVistaOrigen(String vistaOrigen) {
        this.vistaOrigen = vistaOrigen;
    }

    public LazyModel<Presupuesto> getPresupuestoLazyModel() {
        return presupuestoLazyModel;
    }

    public void setPresupuestoLazyModel(LazyModel<Presupuesto> presupuestoLazyModel) {
        this.presupuestoLazyModel = presupuestoLazyModel;
    }

}
