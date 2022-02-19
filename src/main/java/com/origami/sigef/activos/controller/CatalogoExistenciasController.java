/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

//import java.io.ByteArrayOutputStream;
import com.github.pjfanning.xlsx.StreamingReader;
import com.origami.sigef.activos.service.CatalogoExistenciasService;
import com.origami.sigef.common.entities.CatalogoExistencias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "CatalogoExistenciasView")
@ViewScoped
public class CatalogoExistenciasController implements Serializable {

    private static final Logger LOG = Logger.getLogger(CatalogoExistenciasController.class.getName());

    private UploadedFile file;
    private CatalogoExistencias catalagoExistencias;
    private LazyModel<CatalogoExistencias> lazyCatalogoExistencias;

    @Inject
    private CatalogoExistenciasService catalogoExistenciasService;
    @Inject
    private UserSession us;

    @PostConstruct
    public void initView() {
        catalagoExistencias = new CatalogoExistencias();
        lazyCatalogoExistencias = new LazyModel<>(CatalogoExistencias.class);
        lazyCatalogoExistencias.getFilterss().put("estado", true);
        lazyCatalogoExistencias.getSorteds().put("idExistencia", "ASC");
    }

    public void upload(FileUploadEvent event) throws IOException {
        try {
            if (catalagoExistencias.getFila() == null || catalagoExistencias.getFila() <= 0) {
                JsfUtil.addWarningMessage("Error", "Verifique que número de fila a iniciar");
                return;
            }
            file = event.getFile();
            if (file != null) {
                Workbook wb = null;
                try {
                    InputStream input = file.getInputstream();
                    wb = StreamingReader.builder()
                            .rowCacheSize(100) // number of rows to keep in memory (defaults to 10)
                            .bufferSize(4096) // buffer size to use when reading InputStream to file (defaults to 1024)
                            //.sheetIndex(0)   // index of sheet to use (defaults to 0)
                            .open(input);   // InputStream or File for XLSX file (required)
                } catch (IOException iOException) {
                    JsfUtil.addErrorMessage("Error", "Error al subir archivo");
                }
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
                //Elijo la hoja que pasaré por parámetro
                Sheet sheet = wb.getSheetAt(0);
//                byte[] buffer = new byte[1024];
                int rows = sheet.getLastRowNum();
                int rowsCoun = 0;
                for (Row row : sheet) {
//                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Cell idExistenciaCell = row.getCell(0);
                        Cell descripcionCell = row.getCell(1);
                        Cell unidadMedidaCell = row.getCell(2);
                        Cell caducaCell = row.getCell(3);
                        Cell loteCell = row.getCell(4);
                        Cell itemPresupuestarioCell = row.getCell(5);

                        String descripcion = null;
                        if (descripcionCell != null) {
                            String idExistencia = "";
                            switch (idExistenciaCell.getCellTypeEnum()) {
                                case NUMERIC:
                                    idExistencia = String.valueOf(idExistenciaCell.getNumericCellValue());
                                    break;
                                case STRING:
                                    idExistencia = idExistenciaCell.getStringCellValue();
                                    break;
                            }
//                            if (!idExistencia.equals("ID_EXISTENCIA")) {
//                                String ex = idExistencia.replace("E14", "").replace("E15", "");
////                                String ex = ext.replace("E15", "");
//                                if (ex.length() <= 16) {
//                                    int num2 = 16 - ex.length();
//                                    String cero = "";
//                                    for (int x = 0; x < num2; x++) {
//                                        cero = cero + "0";
//                                    }
//                                    ex = ex.replace(".", "") + cero;
//                                    if (ex.length() == 15) {
//                                        idExistencia = "0" + ex.replace(".", "");
//                                    }
//                                } else {
//                                    idExistencia = ex.replace(".", "");
//                                }
                            idExistencia = idExistencia.replace("E14", "").replace("E15", "").replace(".", "");
                            if (idExistencia.length() > 17) {
                                idExistencia = idExistencia.substring(1);
                            }
                            if (idExistencia.length() == 16 && !idExistencia.equals("ID_EXISTENCIA")) {
                                if (catalogoExistenciasService.getIdExistencia(idExistencia) == null) {
                                    descripcion = descripcionCell.getStringCellValue();
                                    String unidadMedida = unidadMedidaCell.getStringCellValue();
                                    String caduca = caducaCell.getStringCellValue();
                                    String lote = loteCell.getStringCellValue();
                                    String itemPresupuestario = itemPresupuestarioCell.getStringCellValue();
                                    catalagoExistencias.setIdExistencia(idExistencia);
                                    catalagoExistencias.setDescripcion(descripcion);
                                    catalagoExistencias.setUnidadMedida(unidadMedida);
                                    catalagoExistencias.setCaduca(caduca);
                                    catalagoExistencias.setLote(lote);
                                    catalagoExistencias.setItemPresupuestario(itemPresupuestario);
                                    catalogoExistenciasService.create(catalagoExistencias);
                                }
                            }
                        }
                    }
                    catalagoExistencias = new CatalogoExistencias();
//                    output.write(buffer, 0, length);
                    rowsCoun++;
                    if ((rowsCoun % 100) == 0) {
                        System.out.println("registros procesados " + rowsCoun);
                    }

                }
                System.out.println("registros procesados " + rowsCoun);
                FacesMessage message = new FacesMessage("EXITO", file.getFileName() + " fue subido.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("formMain");
            }
        } catch (Exception e) {
//            FacesMessage message = new FacesMessage("ERROR", file.getFileName() + " no pudo ser subido correctamente. " + e);
//            FacesContext.getCurrentInstance().addMessage(null, message);
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<CatalogoExistencias> catalogoExistencias() {
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        setFile(event.getFile());
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void openDialogo() {
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').show()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public void editar(CatalogoExistencias ex) {
        this.catalagoExistencias = ex;
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').show()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public void eliminar(CatalogoExistencias ex) {
        if (catalogoExistenciasService.existenRegistros(ex)) {
            JsfUtil.addWarningMessage("ERROR", "No puede ser eliminado porque ya existen registros de Items de Inventario Activos");
        } else {
            ex.setEstado(Boolean.FALSE);
            ex.setFechaModificacion(new Date());
            ex.setUsuarioModifica(us.getNameUser());
//        catalogoExistenciasService.remove(ex);
            catalogoExistenciasService.edit(ex);
        }

        JsfUtil.update("formMain");
    }

    public void save() {
        if (validateCatalogoExistente()) {
            return;
        }
        if (catalagoExistencias.getId() != null) {
            catalagoExistencias.setFechaModificacion(new Date());
            catalagoExistencias.setUsuarioModifica(us.getNameUser());
            catalogoExistenciasService.edit(catalagoExistencias);
        } else {
            catalagoExistencias.setEstado(Boolean.TRUE);
            catalagoExistencias.setUsuarioCreacion(us.getNameUser());
            catalagoExistencias.setFechaCreacion(new Date());
            catalogoExistenciasService.create(catalagoExistencias);
        }
        catalagoExistencias = new CatalogoExistencias();
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').hide()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public boolean validateCatalogoExistente() {

        if (catalagoExistencias.getIdExistencia() == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar el id existencia.");
            return true;
        } else {
            if (catalagoExistencias.getIdExistencia() == null) {
                if (catalogoExistenciasService.getIdExistencia(catalagoExistencias.getIdExistencia()) != null) {
                    JsfUtil.addWarningMessage("Advertencia", " El id existencia ya existe, debe ser único.");
                    return true;
                }
            }
        }
        if (catalagoExistencias.getUnidadMedida() == null || catalagoExistencias.getUnidadMedida().isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar la unidad de medida.");
            return true;
        }
        if (catalagoExistencias.getDescripcion() == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar la descripción.");
            return true;
        }
        return false;
    }

    public void closeDialogo() {
        catalagoExistencias = new CatalogoExistencias();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public CatalogoExistencias getCatalagoExistencias() {
        return catalagoExistencias;
    }

    public void setCatalagoExistencias(CatalogoExistencias catalagoExistencias) {
        this.catalagoExistencias = catalagoExistencias;
    }

    public LazyModel<CatalogoExistencias> getLazyCatalogoExistencias() {
        return lazyCatalogoExistencias;
    }

    public void setLazyCatalogoExistencias(LazyModel<CatalogoExistencias> lazyCatalogoExistencias) {
        this.lazyCatalogoExistencias = lazyCatalogoExistencias;
    }
}
