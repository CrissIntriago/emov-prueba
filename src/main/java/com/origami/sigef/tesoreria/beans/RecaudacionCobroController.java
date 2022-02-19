/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.RecaudacionCobro;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.DetalleCorteOrdenCobro;
import com.origami.sigef.tesoreria.modelTarifario.ModelExcel;
import com.origami.sigef.tesoreria.service.CorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.DetalleCorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.RecaudacionCobroService;
import com.origami.sigef.tesoreria.service.RecaudacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "recaudacionCobroView")
@ViewScoped
public class RecaudacionCobroController implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionCobroController.class.getName());

    @Inject
    private CorteOrdenCobroService corteService;
    @Inject
    private DetalleCorteOrdenCobroService detalleCorteService;
    @Inject
    private RecaudacionService recaudacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private RecaudacionCobroService recaudacionConbroService;

    private List<DetalleCorteOrdenCobro> listDetalleCorte;
    private List<Recaudacion> recaudaciones;
    private List<CorteOrdenCobro> cortes;
    private List<ModelExcel> listaModel;

    private ModelExcel datoExcel;
    private Recaudacion recaudacion;
    private CorteOrdenCobro corte;
    private RecaudacionCobro recaudacionCobro;
    private DetalleCorteOrdenCobro detalleCorte;
    private UploadedFile file;
    private Workbook book;

    private BigDecimal saldo;
    private BigDecimal saldoAfectado;
    private Integer numeroReg;
    private Integer numHoja;
    private Integer numCell;
    private Integer numFila;
    private Boolean renderedCodigoRec;

    @PostConstruct
    public void init() {
        saldo = BigDecimal.ZERO;
        saldoAfectado = BigDecimal.ZERO;
        numeroReg = 0;
        renderedCodigoRec = Boolean.FALSE;
        recaudacion = new Recaudacion();
        corte = new CorteOrdenCobro();
        recaudacionCobro = new RecaudacionCobro();
        recaudacionCobro.setCorte(new CorteOrdenCobro());
        recaudacionCobro.setRecaudacion(new Recaudacion());
        detalleCorte = new DetalleCorteOrdenCobro();
        listDetalleCorte = new ArrayList<>();
        listaModel = new ArrayList<>();
        datoExcel = new ModelExcel();
//        lazyDetalleCorte = new LazyModel<>(DetalleCorteOrdenCobro.class);
//        lazyDetalleCorte.getFilterss().put("corteOrdenCobro", corte);
        recaudaciones = recaudacionService.recaudacionList();
        cortes = corteService.getListaOrdenCobros();
        if (userSession.getVarTemp() instanceof Recaudacion) {
            recaudacion = (Recaudacion) userSession.getVarTemp();
            userSession.setVarTemp(null);
            actulizarRecaudacion();
            renderedCodigoRec = Boolean.TRUE;
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void guardar() {
        try {
            boolean edit = recaudacionCobro.getId() != null;
            if (listDetalleCorte.isEmpty()) {
                JsfUtil.addWarningMessage("Advertencia", "No existen datos a Registrar");
                return;
            }
            if (!edit) {
                detalleVerificado();
                recaudacion.setSaldoAfectar(saldoAfectado);
                recaudacion.setFechaMidificacion(new Date());
                recaudacion.setUsuarioModifica(userSession.getNameUser());
                recaudacionService.edit(recaudacion);
                recaudacionCobro.setCorte(corte);
                recaudacionCobro.setRecaudacion(recaudacion);
                recaudacionCobro.setNumAfectado(numeroReg.shortValue());
                recaudacionCobro.setSaldoAfectar(saldoAfectado);
                //valor que fueron saldados de la lista de ordenes de Cobro
                recaudacionCobro.setValor(saldo);
                recaudacionCobro = recaudacionConbroService.create(recaudacionCobro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsfUtil.addSuccessMessage("Registro", "Datos Registrados con Éxito.");
        cortes = corteService.getListaOrdenCobros();
        JsfUtil.redirectFaces("/tesoreria/cobros/_Registro-Cobro");
        cancelar();
    }

    public void detalleVerificado() {
        if (!listDetalleCorte.isEmpty()) {
            listDetalleCorte.stream().filter((d) -> (d.getVerificado())).forEachOrdered((d) -> {
                detalleCorteService.edit(d);
            });
        }
    }

    public void buscarDetalleCorte() {
        if (recaudacion.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Registro de Recaudación");
            corte = new CorteOrdenCobro();
            return;
        }
        resetValue();
        System.out.println("corte --> " + corte + " recaudación -- > " + recaudacion.getId_banco());
        listDetalleCorte = detalleCorteService.listaDetalleOrdenCobro(corte, recaudacion.getId_banco());
//        lazyDetalleCorte = new LazyModel<>(DetalleCorteOrdenCobro.class);
//        lazyDetalleCorte.getFilterss().put("corteOrdenCobro", corte);
//        lazyDetalleCorte.getSorteds().put("id_banco", "ASC");
//        lazyDetalleCorte.getFilterss().put("id_banco", recaudacion.getId_banco());
    }

    public void actualizarValores(Boolean var, DetalleCorteOrdenCobro dtCorte) {
        if (!var) {
            numeroReg -= 1;
            saldo = saldo.subtract(dtCorte.getTotal());
            saldoAfectado = saldoAfectado.add(dtCorte.getTotal());
        } else {
            numeroReg += 1;
            saldo = saldo.add(dtCorte.getTotal());
            saldoAfectado = saldoAfectado.subtract(dtCorte.getTotal());
        }
    }

    public void verificarCorte(DetalleCorteOrdenCobro dtCorte) {
        if (!Objects.equals(dtCorte.getId_banco(), recaudacion.getId_banco())) {
            JsfUtil.addWarningMessage("Información", "Entidad Financiera no correspondida");
            return;
        }
        if (dtCorte.getVerificado()) {
            dtCorte.setVerificado(Boolean.FALSE);
            dtCorte.setCobroAjuste(null);
        } else {
            if (saldoAfectado.doubleValue() < dtCorte.getTotal().doubleValue()) {
                JsfUtil.addWarningMessage("Erro", "El SALDO por AFECTAR es menor que el valor a saldar: " + dtCorte.getTotal());
                return;
            }
            dtCorte.setVerificado(Boolean.TRUE);
            dtCorte.setCobroAjuste(recaudacion);
        }

//        detalleCorteService.edit(dtCorte);
        JsfUtil.addSuccessMessage("Registro", "Dato fue actualizado con éxito.");
        actualizarValores(dtCorte.getVerificado(), dtCorte);
    }

    public void actulizarRecaudacion() {
        corte = new CorteOrdenCobro();
        saldoAfectado = BigDecimal.ZERO;
        resetValue();
        saldoAfectado = recaudacion.getSaldoAfectar();
    }

    public void dialogoArchivo() {
        if (corte.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un cobro con su detalle");
            return;
        }
        if (recaudacion.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Registro de Recaudación");
            return;
        }
        if (!listaModel.isEmpty()) {
            listaModel = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('dlgExcel').show()");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            if (file != null) {
                book = WorkbookFactory.create(file.getInputstream());
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("ERROR", file.getFileName() + " no pudo ser subido correctamente. " + e);
            FacesContext.getCurrentInstance().addMessage(null, message);
            LOG.log(Level.SEVERE, null, e);
        }
        JsfUtil.addSuccessMessage("Archivo", "Archivo cargado con éxito");
    }

    public void cargarExcel() {
        try {
            System.out.println("entro a cargar excel");
            if (book != null) {
                Sheet hoja = book.getSheetAt(numHoja - 1);
                Row fila = hoja.getRow(numFila - 1);
                int rows = hoja.getLastRowNum();
                System.out.println("fila " + fila.getRowNum());
                System.out.println("rows " + rows);
                System.out.println("hoja " + hoja);
                for (int i = fila.getRowNum(); i < rows; i++) {
                    Row row = hoja.getRow(i);
                    if (row != null) {
                        System.out.println("r " + row);
                        datoExcel = new ModelExcel();
                        Cell fecha = row.getCell(numCell - 1);
                        Cell numPlaca = row.getCell(numCell);
                        Cell numPapeleta = row.getCell(numCell + 1);
                        Cell valorItem = row.getCell(numCell + 2);
                        if (fecha != null) {
                            switch (fecha.getCellTypeEnum()) {
                                case NUMERIC:
                                    datoExcel.setFecha(TalentoHumano.formatearDate(fecha.getDateCellValue(), "dd/MM/yyyy"));
                                    System.out.println("fecha-->" + datoExcel.getFecha());
                                    break;
                                case STRING:
                                    datoExcel.setFecha(TalentoHumano.ParseFecha(fecha.getStringCellValue().replaceAll("\\s", ""), "dd/MM/yyyy"));
                                    System.out.println("fecha2-->" + datoExcel.getFecha());
                                    break;
                            }
//                            datoExcel.setFecha(TalentoHumano.ParseFecha(fecha.getStringCellValue().replaceAll("\\s", ""), "dd/MM/yyyy"));
                        }
                        if (numPlaca != null) {
                            switch (numPlaca.getCellTypeEnum()) {
                                case NUMERIC:
                                    datoExcel.setPlaca(numPlaca.getNumericCellValue() + "");
                                    System.out.println("placa-->" + datoExcel.getPlaca());
                                    break;
                                case STRING:
                                    datoExcel.setPlaca(numPlaca.getStringCellValue().replaceAll("\\s", ""));
                                    System.out.println("placa-->" + datoExcel.getPlaca());
                                    break;
                            }
//                            datoExcel.setPlaca(numPlaca.getStringCellValue().replaceAll("\\s", ""));//replace elimina los espacios, tabulaciones y retornos
                        }
                        if (numPapeleta != null) {
                            switch (numPapeleta.getCellTypeEnum()) {
                                case NUMERIC:
                                    datoExcel.setNumPapeleta((int) numPapeleta.getNumericCellValue() + "");
                                    System.out.println("papeleta-->" + datoExcel.getNumPapeleta());
                                    break;
                                case STRING:
                                    datoExcel.setNumPapeleta(numPapeleta.getStringCellValue());
                                    System.out.println("papeleta-->" + datoExcel.getNumPapeleta());
                                    break;
                            }
                        }
                        if (valorItem != null) {
                            datoExcel.setValor(new BigDecimal(valorItem.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            System.out.println("valor-->" + datoExcel.getValor());
                        }
                        listaModel.add(datoExcel);
                    }
                }
                if (!listaModel.isEmpty()) {
                    compararLista();
                }
            }
            JsfUtil.addSuccessMessage("Información", "Datos Cargados con Éxito.");
            PrimeFaces.current().executeScript("PF('dlgExcel').hide()");
            PrimeFaces.current().ajax().update("dialogForm");
            PrimeFaces.current().ajax().update("formMain");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("ERROR", file.getFileName() + " no pudo ser Cargar correctamente. " + e);
            FacesContext.getCurrentInstance().addMessage(null, message);
            LOG.log(Level.SEVERE, null, e);
        }
        numHoja = null;
        numCell = null;
        numFila = null;
    }

    public void compararLista() throws ParseException {
        try {
            if (!listDetalleCorte.isEmpty() && !listaModel.isEmpty()) {
                for (ModelExcel cell : listaModel) {
                    for (DetalleCorteOrdenCobro d : listDetalleCorte) {
                        String placa = "";
                        if (d.getPlaca().equals("-")) {
                            placa = null;
                        } else {
                            placa = d.getPlaca();
                        }
                        if (cell.getValor() != null) {
                            if (Objects.equals(d.getId_banco(), recaudacion.getId_banco())) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                Date currentdate = simpleDateFormat.parse(d.getFechaEmision());
                                if (cell.getPlaca() != null) {
                                    String numPape = cell.getNumPapeleta().replaceAll("'", "");
                                    Integer papeleta = Integer.parseInt(numPape);
                                    System.out.println("cell no null" + cell.getPlaca());
                                    System.out.println("placa " + cell.getPlaca() + " placa " + placa);
                                    System.out.println("placa " + cell.getPlaca() + " identificac " + d.getIdentificacion());
                                    System.out.println("num " + papeleta + " num " + d.getNumeroPapeleta());
                                    System.out.println("fecha " + Utils.dateFormatPattern("dd/MM/yyyy", cell.getFecha()) + " fecha " + Utils.dateFormatPattern("dd/MM/yyyy", currentdate));
                                    System.out.println("valor " + cell.getValor() + " valor " + d.getTotal());
                                    if (cell.getPlaca().equals(placa)
                                            && papeleta.equals(Integer.parseInt(d.getNumeroPapeleta()))
                                            && cell.getValor().equals(d.getTotal())
                                            && Utils.dateFormatPattern("dd/MM/yyyy", currentdate).equals(Utils.dateFormatPattern("dd/MM/yyyy", cell.getFecha()))) {
                                        d.setVerificado(Boolean.TRUE);
                                        d.setCobroAjuste(recaudacion);
                                        cell.setEstado(Boolean.TRUE);
                                        actualizarValores(d.getVerificado(), d);
                                    } else if (cell.getPlaca().equals(d.getIdentificacion())
                                            && papeleta.equals(Integer.parseInt(d.getNumeroPapeleta()))
                                            && cell.getValor().equals(d.getTotal())
                                            && Utils.dateFormatPattern("dd/MM/yyyy", currentdate).equals(Utils.dateFormatPattern("dd/MM/yyyy", cell.getFecha()))) {
                                        d.setVerificado(Boolean.TRUE);
                                        d.setCobroAjuste(recaudacion);
                                        cell.setEstado(Boolean.TRUE);
                                        actualizarValores(d.getVerificado(), d);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("ERROR", " no pudo ser Cargar correctamente los datos." + e);
            FacesContext.getCurrentInstance().addMessage(null, message);
            LOG.log(Level.SEVERE, null, e);
        }
    }

//    public BigDecimal converString(String valor) {
//        
//    }
    public void cancelar() {
        recaudacionCobro = new RecaudacionCobro();
        recaudacion = new Recaudacion();
        corte = new CorteOrdenCobro();
        saldoAfectado = BigDecimal.ZERO;
        recaudacionCobro = new RecaudacionCobro();
        recaudacionCobro.setCorte(new CorteOrdenCobro());
        recaudacionCobro.setRecaudacion(new Recaudacion());
        resetValue();
    }

    public void resetValue() {
        listDetalleCorte = new ArrayList<>();
        saldo = BigDecimal.ZERO;
        numeroReg = 0;
    }

    public String fechaRegistro(Date fecha) {
        if (fecha != null) {
            return Utils.dateFormatPattern("dd/MM/yyyy", fecha);
        }
        return "";
    }

    public List<DetalleCorteOrdenCobro> getListDetalleCorte() {
        return listDetalleCorte;
    }

    public void setListDetalleCorte(List<DetalleCorteOrdenCobro> listDetalleCorte) {
        this.listDetalleCorte = listDetalleCorte;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public int getNumeroReg() {
        return numeroReg;
    }

    public void setNumeroReg(int numeroReg) {
        this.numeroReg = numeroReg;
    }

    public CorteOrdenCobro getCorte() {
        return corte;
    }

    public void setCorte(CorteOrdenCobro corte) {
        this.corte = corte;
    }

    public Recaudacion getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(Recaudacion recaudacion) {
        this.recaudacion = recaudacion;
    }

    public List<Recaudacion> getRecaudaciones() {
        return recaudaciones;
    }

    public void setRecaudaciones(List<Recaudacion> recaudaciones) {
        this.recaudaciones = recaudaciones;
    }

    public List<CorteOrdenCobro> getCortes() {
        return cortes;
    }

    public void setCortes(List<CorteOrdenCobro> cortes) {
        this.cortes = cortes;
    }

    public DetalleCorteOrdenCobro getDetalleCorte() {
        return detalleCorte;
    }

    public void setDetalleCorte(DetalleCorteOrdenCobro detalleCorte) {
        this.detalleCorte = detalleCorte;
    }

    public BigDecimal getSaldoAfectado() {
        return saldoAfectado;
    }

    public void setSaldoAfectado(BigDecimal saldoAfectado) {
        this.saldoAfectado = saldoAfectado;
    }

    public Integer getNumHoja() {
        return numHoja;
    }

    public void setNumHoja(Integer numHoja) {
        this.numHoja = numHoja;
    }

    public Integer getNumCell() {
        return numCell;
    }

    public void setNumCell(Integer numCell) {
        this.numCell = numCell;
    }

    public Integer getNumFila() {
        return numFila;
    }

    public void setNumFila(Integer numFila) {
        this.numFila = numFila;
    }

    public Boolean getRenderedCodigoRec() {
        return renderedCodigoRec;
    }

    public void setRenderedCodigoRec(Boolean renderedCodigoRec) {
        this.renderedCodigoRec = renderedCodigoRec;
    }

}
