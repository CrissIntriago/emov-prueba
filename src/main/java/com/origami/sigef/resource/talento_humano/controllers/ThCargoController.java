/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import com.origami.sigef.resource.talento_humano.services.ThCargoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thCargoView")
@ViewScoped
public class ThCargoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ThCargoService thCargoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaPresupuestoService;

    private LazyModel<ThCargo> thCargoLazyModel;
    private LazyModel<ThRubro> thRubroLazyModel;
    private LazyModel<UnidadAdministrativa> thUnidadAdministrativaLazyModel;
    private LazyModel<ThEscalaSalarial> thEscalaSalarialLazyModel;
    private LazyModel<ThRegimenLaboral> thRegimenLaboralLazyModel;

    private List<ThCargoRubros> thCargoRubrosInsert;
    private List<ThCargoRubros> thCargoRubrosDelete;
    private List<ThRubro> thRubrosList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> listTipoCargo;
    private List<CatalogoItem> tipoCargoList;

    private ThCargo thCargo;
    private OpcionBusqueda opcionBusqueda;

    private Boolean view, variosRegistros, codigo, completarTarea, boolTramite, presupuesto;

    private int cantidadRegistros;

    private short newPeriodo;
    private short oldPeriodo;

    private BigDecimal cupoDistributivo, cupoAsignadoDistributivo, cupoDisponibleDistributivo;
    private BigDecimal cupoDistributivoAnexo, cupoAsignadoDistributivoAnexo, cupoDisponibleDistributivoAnexo;

    @PostConstruct
    public void init() {
        cleanForm();
        boolTramite = Boolean.FALSE;
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                updateDistributivos();
                boolTramite = Boolean.TRUE;
                opcionBusqueda.setAnio(cupoPresupuestoService.getAnio(tramite));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        listaPeriodo = catalogoItemService.getPeriodo();
        listTipoCargo = catalogoItemService.findByCatalogo("tipoCargo");
        tipoCargoList = catalogoItemService.findByCatalogo("th_tipo_contrato");
        //ThCargo
        thCargoLazyModel = new LazyModel<>(ThCargo.class);
        thCargoLazyModel.getSorteds().put("codigo", "ASC");
        thCargoLazyModel.getFilterss().put("estado", true);
        //ThRubros
        thRubroLazyModel = new LazyModel<>(ThRubro.class);
        thRubroLazyModel.getSorteds().put("nombre", "ASC");
        thRubroLazyModel.getFilterss().put("estado", true);
        thRubroLazyModel.getFilterss().put("activo", true);
        thRubroLazyModel.getFilterss().put("ingreso", true);
        //ThUnidadAdministrativa
        thUnidadAdministrativaLazyModel = new LazyModel<>(UnidadAdministrativa.class);
        thUnidadAdministrativaLazyModel.getSorteds().put("id", "ASC");
        thUnidadAdministrativaLazyModel.getFilterss().put("estado", true);
        //ThEscalaSalarial
        thEscalaSalarialLazyModel = new LazyModel<>(ThEscalaSalarial.class);
        thEscalaSalarialLazyModel.getSorteds().put("id", "ASC");
        thEscalaSalarialLazyModel.getFilterss().put("estado", true);
        //ThRegimenLaboral
        thRegimenLaboralLazyModel = new LazyModel<>(ThRegimenLaboral.class);
        thRegimenLaboralLazyModel.getFilterss().put("estado", true);
        thRegimenLaboralLazyModel.getSorteds().put("nombre", "ASC");
        thRegimenLaboralLazyModel.setDistinct(false);
    }

    public void form(ThCargo thCargo, Boolean view) {
        cleanForm();
        this.view = view;
        if (thCargo != null) {
            this.thCargo = thCargo;
            rubrosXanio(false);
            consultarAprobacion(opcionBusqueda.getAnio());
        }
        JsfUtil.executeJS("PF('thCargoDlg').show()");
        PrimeFaces.current().ajax().update("thCargoForm");
    }

    public void save() {
        Boolean edit = thCargo.getId() != null;
        if (thCargo.getNombreCargo() == null || thCargo.getNombreCargo().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el nombre del cargo");
            return;
        }
        if (thCargo.getIdUnidad() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar la unidad administrativa");
            return;
        }
        if (thCargo.getIdRegimen() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar el regimén laboral");
            return;
        }
        if (thCargo.getIdGrupo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar la escala salarial");
            return;
        }
        if (thCargo.getIdCatalogoItem() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un tipo de cargo");
            return;
        }
        if (edit) {
            thCargo.setUsuarioModifica(userSession.getNameUser());
            thCargo.setFechaModificacion(new Date());
            thCargoService.edit(thCargo);
            //Insertar nuevos rubros
            for (ThCargoRubros item : thCargoRubrosInsert) {
                if (item.getId() != null) {
                    thCargoRubrosService.edit(item);
                } else {
                    item.setIdCargo(thCargo);
                    thCargoRubrosService.create(item);
                }
            }
            //Eliminar rubros seleccionados
            for (ThCargoRubros item : thCargoRubrosDelete) {
                item.setEstado(false);
                thCargoRubrosService.edit(item);
            }
        } else {
            thCargo.setUsuarioCreacion(userSession.getNameUser());
            thCargo.setFechaCreacion(new Date());
            if (variosRegistros) {//varios registros
                for (int i = 1; i <= cantidadRegistros; i++) {
                    thCargo.setId(null);
                    saveThCargo(thCargo);
                }
            } else {//un solo registro
                saveThCargo(thCargo);
            }
        }
        if (boolTramite) {
            updateDistributivos();
            PrimeFaces.current().ajax().update("gridDistributivo");
        }
        JsfUtil.executeJS("PF('thCargoDlg').hide()");
        PrimeFaces.current().ajax().update("thCargoForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    private void saveThCargo(ThCargo thCargo) {
        generarCodigo(thCargo);
        thCargo = thCargoService.create(thCargo);
        for (ThCargoRubros thCargoRubros : thCargoRubrosInsert) {
            thCargoRubros.setIdCargo(thCargo);
            thCargoRubros.setId(null);
            thCargoRubrosService.create(thCargoRubros);
        }
    }

    private void generarCodigo(ThCargo thCargo) {
        thCargo.setCodigo(thCargoService.getCode());
    }

    public void closeForm() {
        thCargo = new ThCargo();
        cleanForm();
        JsfUtil.executeJS("PF('thCargoDlg').hide()");
    }

    private void cleanForm() {
        opcionBusqueda = new OpcionBusqueda();
        cantidadRegistros = 0;
        variosRegistros = false;
        thCargo = new ThCargo();
        thCargoRubrosInsert = new ArrayList<>();
        thCargoRubrosDelete = new ArrayList<>();
    }

    public void openDlg(String dlg, String form) {
        if (dlg.equals("rubrosDlg")) {
            if (consultarAprobacion(opcionBusqueda.getAnio())) {
                JsfUtil.addWarningMessage("AVISO!!!", "No puede agregar un rubro en este periodo debido a que ya esta aprobado el presupuesto del periodo seleccionado");
                return;
            }
        }
        thRubrosList = new ArrayList<>();
        codigo = true;
        oldPeriodo = Utils.getAnio(new Date()).shortValue();
        newPeriodo = opcionBusqueda.getAnioSiguiente();
        JsfUtil.executeJS("PF('" + dlg + "').show()");
        PrimeFaces.current().ajax().update(form);
    }

    public void closeDlgUnidad(UnidadAdministrativa unidadAdministrativa) {
        thCargo.setIdUnidad(unidadAdministrativa);
        JsfUtil.executeJS("PF('unidadDlg').hide()");
        PrimeFaces.current().ajax().update("gridUnidad");
    }

    public void closeDlgRegimen(ThRegimenLaboral thRegimenLaboral) {
        thCargo.setIdRegimen(thRegimenLaboral);
        JsfUtil.executeJS("PF('regimenDlg').hide()");
        PrimeFaces.current().ajax().update("gridRegimen");
    }

    public void closeDlgEscala(ThEscalaSalarial thEscalaSalarial) {
        thCargo.setIdGrupo(thEscalaSalarial);
        if (!consultarAprobacion(opcionBusqueda.getAnio())) {
            if (!thCargoRubrosInsert.isEmpty()) {
                for (ThCargoRubros thCargoRubros : thCargoRubrosInsert) {
                    actualizaProyecccion(thCargoRubros);
                }
            }
        }
        JsfUtil.executeJS("PF('escalaDlg').hide()");
        PrimeFaces.current().ajax().update("fielsetEscala");
        PrimeFaces.current().ajax().update("thCargosRubroTable");
    }

    public void closeDlgRubro(ThRubro thRubro) {
        if (thCargo.getIdGrupo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una escala salarial antes de agregar los rubros");
            return;
        }
        boolean accion = true;
        ThCargoRubros cargoRubro = new ThCargoRubros(thRubro, opcionBusqueda.getAnio());
        if (thRubro.getIngreso()) {
            actualizaProyecccion(cargoRubro);
        } else {
            cargoRubro.setMonto(BigDecimal.ZERO);
        }
        if (!thCargoRubrosInsert.isEmpty()) {
            for (ThCargoRubros th : thCargoRubrosInsert) {
                if (th.getIdRubro().equals(thRubro)) {
                    accion = false;
                }
            }
        }
        if (accion) {
            thCargoRubrosInsert.add(cargoRubro);
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha agregado correctamente");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Ya se encuentra seleccionado");
        }
        PrimeFaces.current().ajax().update("thCargosRubroTable");
    }

    public void actualizaProyecccion(ThCargoRubros thCargoRubros) {
        if (thCargoRubros.getProyeccion() > 12) {
            JsfUtil.addWarningMessage("Aviso!!!", "El máximo valor de la proyección es 12");
            thCargoRubros.setProyeccion(12);
        }
        ThRubro thRubro = thCargoRubros.getIdRubro();
        if (!thRubro.getTipoValor() && !thRubro.getPorcentaje()) {
            if (thCargoRubros.getIdRubro().getClasificacion() != null) {
                System.out.println("CODIGO: " + thCargoRubros.getIdRubro().getClasificacion().getCodigo());
                if (thCargoRubros.getIdRubro().getClasificacion().getCodigo().equals("DT")) {
                    thCargoRubros.setMonto(new BigDecimal(thCargo.getIdGrupo().getRemuneracionDolares().doubleValue()));
                } else {
                    thCargoRubros.setMonto(new BigDecimal(thCargo.getIdGrupo().getRemuneracionDolares().doubleValue() * thCargoRubros.getProyeccion()));
                }
            } else {
                thCargoRubros.setMonto(new BigDecimal(thCargo.getIdGrupo().getRemuneracionDolares().doubleValue() * thCargoRubros.getProyeccion()));
            }
        } else if (thRubro.getTipoValor() && !thRubro.getPorcentaje()) {
            thCargoRubros.setMonto(new BigDecimal(thCargoRubros.getProyeccion() * (thRubro.getValor().doubleValue() / 12)));
        } else if (thRubro.getTipoValor() && thRubro.getPorcentaje()) {
            thCargoRubros.setMonto(new BigDecimal((thCargo.getIdGrupo().getRemuneracionDolares().doubleValue() * (thRubro.getValor().doubleValue() / 100)) * thCargoRubros.getProyeccion()));
        }
    }

    public void processRubros() {
        if (!thRubrosList.isEmpty()) {
            for (ThRubro rubro : thRubrosList) {
                ThCargoRubros thCargoRubros = new ThCargoRubros();
                thCargoRubros.setIdRubro(rubro);
                thCargoRubrosInsert.add(thCargoRubros);
            }
            JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado correctamente los rubros ");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar uno o mas rubros");
        }
    }

    public void delete(ThCargo thCargo) {
        thCargo.setEstado(false);
        thCargo.setActivo(false);
        thCargoService.edit(thCargo);
        PrimeFaces.current().ajax().update("thCargoTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public void inactivar(ThCargo thCargo) {
        thCargo.setActivo(false);
        thCargoService.edit(thCargo);
        PrimeFaces.current().ajax().update("thCargoTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha inactivado correctamente");
    }

    public void rubrosXanio(Boolean accion) {
        consultarAprobacion(opcionBusqueda.getAnio());
        if (this.thCargo.getId() != null) {
            if (accion) {
                thCargoRubrosInsert = new ArrayList<>();
            }
            List<ThCargoRubros> aux = thCargoRubrosService.findByCargoXanio(this.thCargo, opcionBusqueda.getAnio());
            for (ThCargoRubros item : aux) {
                thCargoRubrosInsert.add(item);
            }
            if (accion) {
                PrimeFaces.current().ajax().update("thCargosRubroTable");
            }
        }
    }

    public void cargoXanioCopy() {
        if (newPeriodo == oldPeriodo) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede generar una copia del mismo año");
            return;
        }
        if (oldPeriodo > newPeriodo) {
            JsfUtil.addWarningMessage("AVISO!!!", "El periodo siguiente no debe ser menor al periodo actual");
            return;
        }
        List<ThCargo> thCargosList = thCargoService.findCargoParametro(codigo);
        if (!thCargosList.isEmpty()) {
            for (ThCargo thCargo : thCargosList) {
                rubroXanioCopy(thCargo);
            }
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha generado la copia desde el período " + newPeriodo + " al período " + oldPeriodo);
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No existe datos para generar un copia");
        }
        JsfUtil.executeJS("PF('parametrosDlg').hide()");
    }

    private void rubroXanioCopy(ThCargo thCargo) {
        List<ThCargoRubros> list = thCargoRubrosService.findByCargoXanio(thCargo, newPeriodo);
        if (list.isEmpty()) {
            List<ThCargoRubros> rubrosList = thCargoRubrosService.findByCargoXanio(thCargo, oldPeriodo);
            for (ThCargoRubros thCargoRubros : rubrosList) {
                thCargoRubros.setId(null);
                thCargoRubros.setPeriodo(newPeriodo);
                thCargoRubros.setIdCargo(thCargo);
                thCargoRubrosService.create(thCargoRubros);
            }
        }
    }

    public void deteleCargoRubro(ThCargoRubros thCargoRubros, int index) {
        if (thCargoRubros.getId() != null) {
            thCargoRubrosDelete.add(thCargoRubros);
            thCargoRubrosInsert.remove(thCargoRubros);
        } else {
            thCargoRubrosInsert.remove(index);
        }
        PrimeFaces.current().ajax().update("thCargosRubroTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se eliminado correctamente");
    }

    public void updateDistributivos() {
        cupoDistributivo = cupoPresupuestoService.getCupoOtros("D", BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsignadoDistributivo = thCargoRubrosService.cupoAsignadoDistributivo(true, true, opcionBusqueda.getAnio());
        cupoDisponibleDistributivo = cupoDistributivo.subtract(cupoAsignadoDistributivo);
        cupoDistributivoAnexo = cupoPresupuestoService.getCupoOtros("DA", BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsignadoDistributivoAnexo = thCargoRubrosService.cupoAsignadoDistributivo(true, false, opcionBusqueda.getAnio());
        cupoDisponibleDistributivoAnexo = cupoDistributivoAnexo.subtract(cupoAsignadoDistributivoAnexo);
        if (cupoDisponibleDistributivo.doubleValue() == 0 && cupoDisponibleDistributivoAnexo.doubleValue() == 0) {
            completarTarea = Boolean.FALSE;
        } else {
            completarTarea = Boolean.TRUE;
        }
    }

    public void openDlg() {
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.titularTH)); //TALENTO HUMANO 14
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    private Boolean consultarAprobacion(Short periodo) {
        return catalogoProformaPresupuestoService.presupuestoEgresoAprobado(periodo);
    }

    public LazyModel<ThCargo> getThCargoLazyModel() {
        return thCargoLazyModel;
    }

    public void setThCargoLazyModel(LazyModel<ThCargo> thCargoLazyModel) {
        this.thCargoLazyModel = thCargoLazyModel;
    }

    public LazyModel<ThRubro> getThRubroLazyModel() {
        return thRubroLazyModel;
    }

    public void setThRubroLazyModel(LazyModel<ThRubro> thRubroLazyModel) {
        this.thRubroLazyModel = thRubroLazyModel;
    }

    public LazyModel<UnidadAdministrativa> getThUnidadAdministrativaLazyModel() {
        return thUnidadAdministrativaLazyModel;
    }

    public void setThUnidadAdministrativaLazyModel(LazyModel<UnidadAdministrativa> thUnidadAdministrativaLazyModel) {
        this.thUnidadAdministrativaLazyModel = thUnidadAdministrativaLazyModel;
    }

    public LazyModel<ThEscalaSalarial> getThEscalaSalarialLazyModel() {
        return thEscalaSalarialLazyModel;
    }

    public void setThEscalaSalarialLazyModel(LazyModel<ThEscalaSalarial> thEscalaSalarialLazyModel) {
        this.thEscalaSalarialLazyModel = thEscalaSalarialLazyModel;
    }

    public LazyModel<ThRegimenLaboral> getThRegimenLaboralLazyModel() {
        return thRegimenLaboralLazyModel;
    }

    public void setThRegimenLaboralLazyModel(LazyModel<ThRegimenLaboral> thRegimenLaboralLazyModel) {
        this.thRegimenLaboralLazyModel = thRegimenLaboralLazyModel;
    }

    public List<ThCargoRubros> getThCargoRubrosInsert() {
        return thCargoRubrosInsert;
    }

    public void setThCargoRubrosInsert(List<ThCargoRubros> thCargoRubrosInsert) {
        this.thCargoRubrosInsert = thCargoRubrosInsert;
    }

    public ThCargo getThCargo() {
        return thCargo;
    }

    public void setThCargo(ThCargo thCargo) {
        this.thCargo = thCargo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<ThRubro> getThRubrosList() {
        return thRubrosList;
    }

    public void setThRubrosList(List<ThRubro> thRubrosList) {
        this.thRubrosList = thRubrosList;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Boolean getVariosRegistros() {
        return variosRegistros;
    }

    public void setVariosRegistros(Boolean variosRegistros) {
        this.variosRegistros = variosRegistros;
    }

    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    public void setCantidadRegistros(int cantidadRegistros) {
        this.cantidadRegistros = cantidadRegistros;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<CatalogoItem> getListTipoCargo() {
        return listTipoCargo;
    }

    public void setListTipoCargo(List<CatalogoItem> listTipoCargo) {
        this.listTipoCargo = listTipoCargo;
    }

    public short getNewPeriodo() {
        return newPeriodo;
    }

    public void setNewPeriodo(short newPeriodo) {
        this.newPeriodo = newPeriodo;
    }

    public short getOldPeriodo() {
        return oldPeriodo;
    }

    public void setOldPeriodo(short oldPeriodo) {
        this.oldPeriodo = oldPeriodo;
    }

    public Boolean getCodigo() {
        return codigo;
    }

    public void setCodigo(Boolean codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getCupoDistributivo() {
        return cupoDistributivo;
    }

    public void setCupoDistributivo(BigDecimal cupoDistributivo) {
        this.cupoDistributivo = cupoDistributivo;
    }

    public BigDecimal getCupoAsignadoDistributivo() {
        return cupoAsignadoDistributivo;
    }

    public void setCupoAsignadoDistributivo(BigDecimal cupoAsignadoDistributivo) {
        this.cupoAsignadoDistributivo = cupoAsignadoDistributivo;
    }

    public BigDecimal getCupoDisponibleDistributivo() {
        return cupoDisponibleDistributivo;
    }

    public void setCupoDisponibleDistributivo(BigDecimal cupoDisponibleDistributivo) {
        this.cupoDisponibleDistributivo = cupoDisponibleDistributivo;
    }

    public BigDecimal getCupoDistributivoAnexo() {
        return cupoDistributivoAnexo;
    }

    public void setCupoDistributivoAnexo(BigDecimal cupoDistributivoAnexo) {
        this.cupoDistributivoAnexo = cupoDistributivoAnexo;
    }

    public BigDecimal getCupoAsignadoDistributivoAnexo() {
        return cupoAsignadoDistributivoAnexo;
    }

    public void setCupoAsignadoDistributivoAnexo(BigDecimal cupoAsignadoDistributivoAnexo) {
        this.cupoAsignadoDistributivoAnexo = cupoAsignadoDistributivoAnexo;
    }

    public BigDecimal getCupoDisponibleDistributivoAnexo() {
        return cupoDisponibleDistributivoAnexo;
    }

    public void setCupoDisponibleDistributivoAnexo(BigDecimal cupoDisponibleDistributivoAnexo) {
        this.cupoDisponibleDistributivoAnexo = cupoDisponibleDistributivoAnexo;
    }

    public Boolean getCompletarTarea() {
        return completarTarea;
    }

    public void setCompletarTarea(Boolean completarTarea) {
        this.completarTarea = completarTarea;
    }

    public List<CatalogoItem> getTipoCargoList() {
        return tipoCargoList;
    }

    public void setTipoCargoList(List<CatalogoItem> tipoCargoList) {
        this.tipoCargoList = tipoCargoList;
    }

}
