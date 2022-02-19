/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.lazy.PlanLocalSistemaLazy;
import com.origami.sigef.contabilidad.model.PlanLocalFormat;
import com.origami.sigef.contabilidad.service.PlanLocalSistemaService;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "planSistemaView")
@ViewScoped
public class PlanLocalSistemaController implements Serializable {

    /**
     *
     */
    @Inject
    private UserSession userSession;

    private static final long serialVersionUID = 1L;
    @Inject
    private PlanLocalSistemaService planSistemaService;
    @Inject
    private MasterCatalogoService masterCatalogoService;

    private OpcionBusqueda opcionBusqueda;
    private PlanLocalSistemaLazy lazy;
    private PlanLocalSistema planLocalSistema;
    private PlanLocalSistema planSeleccionado;
    private List<MasterCatalogo> periodos;

    @PostConstruct
    public void init() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.lazy = new PlanLocalSistemaLazy(opcionBusqueda);
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.planLocalSistema = new PlanLocalSistema();
    }

    public void importarExcel(FileUploadEvent event) {
        try {
            InputStream input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            XSSFSheet sheet = worbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            int next = 0;
            
            String codSistema = "";
            String codPolitica = "";
            String codObjetivo = "";
            
            List<String> sistemasxls = new ArrayList<>();
            List<String> politicasxls = new ArrayList<>();
            List<String> objetivosxls = new ArrayList<>();
            
            //List<PlanLocalFormat> listModelo = new ArrayList<>();
            //PlanLocalFormat objModelo = new PlanLocalFormat();
            
            
            List<PlanLocalPolitica> listaPoliticas = new ArrayList<>();
            List<PlanLocalObjetivo> listaObjetivos = new ArrayList<>();
            
            List<PlanLocalSistema> listaSistemas = new ArrayList<>();
            PlanLocalSistema sistema = new PlanLocalSistema();
            PlanLocalObjetivo objetivo = new PlanLocalObjetivo();
            
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (next > 0) {
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue() != null) {
                        DataFormatter formatter = new DataFormatter();
                        //codPolitica = row.getCell(5).getStringCellValue();
                        codPolitica = formatter.formatCellValue(row.getCell(4));
                        codObjetivo = formatter.formatCellValue(row.getCell(2));
                        codSistema = formatter.formatCellValue(row.getCell(0));
                        
                        System.out.println("codPolitica ->" + codPolitica);
                        System.out.println("codObjetivo ->" + codObjetivo);
                        System.out.println("codSistema ->" + codSistema);
                        
                        if (politicasxls.contains(codPolitica)) {
                            PlanLocalPolitica politica = new PlanLocalPolitica();
                            politica.setCodigo(codPolitica);
                            politica.setDescripcion(row.getCell(5).getStringCellValue());
                            politica.setPeriodo(Utils.getAnio(new Date()).shortValue());
                            politica.setEstado(true);
                            politica.setFechaVigencia(new Date());
                            politica.setFechaCaducidad(new Date());
                            listaPoliticas.add(politica);
                        } else {
                            politicasxls.add(codPolitica);
                            PlanLocalPolitica politica = new PlanLocalPolitica();
                            politica.setCodigo(codPolitica);
                            politica.setDescripcion(row.getCell(5).getStringCellValue());
                            politica.setPeriodo(Utils.getAnio(new Date()).shortValue());
                            politica.setEstado(true);
                            politica.setFechaVigencia(new Date());
                            politica.setFechaCaducidad(new Date());
                            listaPoliticas.add(politica);
                        }
                        
                        if (objetivosxls.contains(codObjetivo)) {
                            objetivo.setCodigo(codObjetivo);
                            objetivo.setDescripcion(row.getCell(3).getStringCellValue());
                            objetivo.setPeriodo(Utils.getAnio(new Date()).shortValue());
                            objetivo.setEstado(true);
                            objetivo.setFechaVigencia(new Date());
                            objetivo.setFechaCaducidad(new Date());
                        } else {
                            objetivosxls.add(codObjetivo);
                        }
                        
                        if (sistemasxls.contains(codSistema)) {
                            sistema.setCodigo(codSistema);
                            sistema.setDescripcion(row.getCell(1).getStringCellValue());
                            sistema.setPeriodo(Utils.getAnio(new Date()).shortValue());
                            sistema.setEstado(true);
                            sistema.setFechaVigencia(new Date());
                            sistema.setFechaCaducidad(new Date());
                        } else {
                            sistemasxls.add(codSistema);
                        }
                        
                        //planSistemaService.create(listaSistemas.get(next-1));
                       //listModelo.add(new PlanLocalFormat(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue(),row.getCell(3).getStringCellValue(),row.getCell(4).getStringCellValue(),row.getCell(5).getStringCellValue()));

                    }
                }
                next += 1;
            }
            objetivo.setPlanLocalPoliticaList(listaPoliticas);
            listaObjetivos.add(objetivo);
            sistema.setPlanLocalObjetivoList(listaObjetivos);
            listaSistemas.add(sistema);
            
            for (PlanLocalSistema pl: listaSistemas) {
                //planSistemaService.create(l);
                System.out.println(pl.getDescripcion());
            }
            
            JsfUtil.addSuccessMessage("INFO!!!", "Se han subido los datos correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void form(PlanLocalSistema p) {
        try {
            if (p != null) {
                this.planLocalSistema = p;
            } else {
                this.planLocalSistema = new PlanLocalSistema();
                planLocalSistema.setPeriodo(opcionBusqueda.getAnio());
                planLocalSistema.setFechaCreacion(fechaVigente());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public void guardar() {

        boolean edit = planLocalSistema.getId() != null;
        try {
            PlanLocalSistema existeCuenta = planSistemaService.existeCuenta(planLocalSistema);
            if (planLocalSistema.getId() == null && existeCuenta != null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Plan Local Sistema", planLocalSistema.getCodigo() + " se ecuentra registrada en el sistema.");
                return;
            }
            if (!edit) {
                planLocalSistema.setUsuarioCreacion(userSession.getNameUser());
                planLocalSistema.setFechaCreacion(new Date());
                planLocalSistema = planSistemaService.create(planLocalSistema);
            } else {
                planLocalSistema.setUsuarioModifica(userSession.getNameUser());
                planLocalSistema.setFechaModificacion(new Date());
                planSistemaService.edit(planLocalSistema);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('planDialog').hide()");
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Plan De Nacional", planLocalSistema.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");

    }

    public Boolean editable() {
        boolean edit = planLocalSistema.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public void eliminar(PlanLocalSistema plan) {
        List<PlanLocalObjetivo> sistema = planSistemaService.getObjetivosByEje(plan);
        if (sistema != null) {
            if (!sistema.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Nacional", "Sistema " + plan.getCodigo() + " tiene información asociada.");
                return;
            }
        }
        plan.setEstado(Boolean.FALSE);
        plan.setFechaCaducidad(new Date());
        planSistemaService.edit(plan);
        JsfUtil.addSuccessMessage("Plan De Nacional", "Sistema " + plan.getCodigo() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("planes");
    }

    public Date fechaVigente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Short anio = opcionBusqueda.getAnio();
        String dia = "01/01/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<PlanLocalSistema> getLazy() {
        return lazy;
    }

    public void setLazy(PlanLocalSistemaLazy lazy) {
        this.lazy = lazy;
    }

    public PlanLocalSistema getPlanLocalSistema() {
        return planLocalSistema;
    }

    public void setPlanLocalSistema(PlanLocalSistema planLocalSistema) {
        this.planLocalSistema = planLocalSistema;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public PlanLocalSistema getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(PlanLocalSistema planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

}
