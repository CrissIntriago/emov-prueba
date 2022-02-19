/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import com.origami.sigef.resource.activos.services.BienCatalogoBldService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "catalogoBldView")
@ViewScoped
public class BienCatalogoBldController implements Serializable {

    private static final Logger LOG = Logger.getLogger(BienCatalogoBldController.class.getName());

    private UploadedFile file;
    private BienCatalogoBld catalagoBLD;
    private LazyModel<BienCatalogoBld> lazyCatalogoBLD;

    @Inject
    private BienCatalogoBldService catalogoBLDService;
    @Inject
    private UserSession us;

    @PostConstruct
    public void initView() {
        catalagoBLD = new BienCatalogoBld();
        lazyCatalogoBLD = new LazyModel<>(BienCatalogoBld.class);
        lazyCatalogoBLD.getFilterss().put("estado", true);
        lazyCatalogoBLD.getSorteds().put("idBien", "ASC");

    }

    public void upload(FileUploadEvent event) throws IOException {
        try {
            file = event.getFile();
            if (file != null) {
                XSSFWorkbook wb = null;
                try {
                    InputStream input = file.getInputstream();
                    wb = new XSSFWorkbook(input);
                } catch (IOException iOException) {
                    JsfUtil.addErrorMessage("Error", "Error al subir archivo");
                }
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
                //Elijo la hoja que pasaré por parámetro
                XSSFSheet sheet = wb.getSheetAt(0);
//                byte[] buffer = new byte[1024];
                int rows = sheet.getLastRowNum();
                for (int i = 2; i < rows + 1; ++i) {
                    XSSFRow row = sheet.getRow(i);
                    if (row != null) {
                        XSSFCell itemPresupuestarioCell = row.getCell(0);
                        XSSFCell idBienCell = row.getCell(1);
                        XSSFCell descripcionCell = row.getCell(2);

                        String descripcion = null;
                        if (descripcionCell != null) {
                            Number idBLD = idBienCell.getNumericCellValue();

                            if (catalogoBLDService.getIdBienes(idBLD.toString()) == null) {

                                descripcion = descripcionCell.getStringCellValue();
                                Number itemPres = itemPresupuestarioCell.getNumericCellValue();

                                String itemPresupuestario = itemPres.toString();
                                if (idBLD != null) {
                                    catalagoBLD.setIdBien(idBLD.toString());
                                    catalagoBLD.setDescripcion(descripcion);
                                    catalagoBLD.setItemPresupuestario(itemPresupuestario);

                                    catalogoBLDService.create(catalagoBLD);
                                }
                            }
                        }
                        catalagoBLD = new BienCatalogoBld();
                    }
                }

                FacesMessage message = new FacesMessage("EXITO", file.getFileName() + " fue subido.");
                FacesContext.getCurrentInstance().addMessage(null, message);
//                PrimeFaces.current().ajax().update("dtExistencias");
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("ERROR", file.getFileName() + " no pudo ser subido correctamente. " + e);
            FacesContext.getCurrentInstance().addMessage(null, message);
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDialogo() {
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').show()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public void save() {
        if (validateCatalogoExistente()) {
            return;
        }
        if (catalagoBLD.getId() != null) {
            catalagoBLD.setFechaModificacion(new Date());
            catalagoBLD.setUsuarioModifica(us.getNameUser());
            catalogoBLDService.edit(catalagoBLD);

        } else {
            catalagoBLD.setEstado(Boolean.TRUE);
            catalagoBLD.setUsuarioCreacion(us.getNameUser());
            catalagoBLD.setFechaCreacion(new Date());
            catalogoBLDService.create(catalagoBLD);
        }
        catalagoBLD = new BienCatalogoBld();
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').hide()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public boolean validateCatalogoExistente() {

        if (catalagoBLD.getIdBien() == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar el id existencia.");
            return true;
        } else {
            if (catalagoBLD.getId() == null) {
                if (catalogoBLDService.getIdBienes(catalagoBLD.getIdBien()) != null) {
                    JsfUtil.addWarningMessage("Advertencia", " El id existencia ya existe, debe ser único.");
                    return true;
                }
            }
        }
        if (catalagoBLD.getDescripcion() == null) {
            JsfUtil.addWarningMessage("Advertencia", " Debe Ingresar la descripción.");
            return true;
        }
        return false;
    }

    public void editar(BienCatalogoBld ex) {
        this.catalagoBLD = ex;
        PrimeFaces.current().executeScript("PF('dlgCatalogoExi').show()");
        PrimeFaces.current().ajax().update("formPanel");
    }

    public void eliminar(BienCatalogoBld ex) {
        if (catalogoBLDService.existenRegistros(ex)) {
            JsfUtil.addWarningMessage("ERROR", "No puede ser eliminado porque ya existen registros de bienes Activos");
        } else {
            ex.setEstado(Boolean.FALSE);
            ex.setFechaModificacion(new Date());
            ex.setUsuarioModifica(us.getNameUser());
            JsfUtil.addInformationMessage("Información", "Item eliminado Correctamente.");
//        catalogoBLDService.remove(ex);
            catalogoBLDService.edit(ex);
        }

        JsfUtil.update("formMain");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public BienCatalogoBld getCatalagoBLD() {
        return catalagoBLD;
    }

    public void setCatalagoBLD(BienCatalogoBld catalagoBLD) {
        this.catalagoBLD = catalagoBLD;
    }

    public LazyModel<BienCatalogoBld> getLazyCatalogoBLD() {
        return lazyCatalogoBLD;
    }

    public void setLazyCatalogoBLD(LazyModel<BienCatalogoBld> lazyCatalogoBLD) {
        this.lazyCatalogoBLD = lazyCatalogoBLD;
    }
//</editor-fold>

}
