/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.EstructuraProgramaPresupuestario;
import com.origami.sigef.Presupuesto.Lazy.EstructuraProgramaPresupuestarioLazy;
import com.origami.sigef.Presupuesto.Service.EstructuraProgramaPresupuestarioService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "estructuraProgramaPresupuestarioView")
@ViewScoped
public class EstructuraProgramaPresupuestarioController implements Serializable {

    /*Inject Services*/
    @Inject
    private EstructuraProgramaPresupuestarioService estructuraProgramaPresupuestarioService;
    @Inject
    private NivelService nivelService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession userSession;

    /*Objects*/
    private OpcionBusqueda opcionBusqueda;
    private EstructuraProgramaPresupuestario estructuraProgramaPresupuestario;
    private EstructuraProgramaPresupuestario estructuraProgramaPresupuestarioSeleccionado;

    /*Variables*/
    private int cantColumnas;
    private String columnClass;

    /*Listas*/
    private List<Nivel> niveles;
    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> clasificaciones;

    /*Lazy*/
    private EstructuraProgramaPresupuestarioLazy estructuraProgramaPresupuestarioLazy;

    /*Contructor Inicializador*/
    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.estructuraProgramaPresupuestario = new EstructuraProgramaPresupuestario();
        this.estructuraProgramaPresupuestarioLazy = new EstructuraProgramaPresupuestarioLazy(opcionBusqueda);

        this.clasificaciones = catalogoService.getItemsByCatalogo("tipo_plan");
        this.niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"plan_programatico", "NIVELES"});
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});

        cantColumnas = 4;
        columnClass = "ui-grid-col-3";
    }

    /*Buscar Estrucutura Programa Presupuestario*/
    public void buscar() {
        estructuraProgramaPresupuestarioLazy = new EstructuraProgramaPresupuestarioLazy(opcionBusqueda);
    }

    /*Cancelar*/
    public void cancelar() {
        opcionBusqueda = new OpcionBusqueda();
        estructuraProgramaPresupuestarioLazy = new EstructuraProgramaPresupuestarioLazy(opcionBusqueda);
    }

    /*Fecha Vigente de la Estructura Programa Presupuestario que se creara*/
    private Date fechaVigente() {
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

    /*Actualiza los campos del formulario de registro (Grid)*/
    private void actualizarInfoGridFom() {

        switch (estructuraProgramaPresupuestario.getNivel().getOrden()) {
            case 1:
                cantColumnas = 2;
                columnClass = "ui-grid-col-2";
                break;
            case 2:
            case 3:
                cantColumnas = 2;
                columnClass = "ui-grid-col-2";
                break;
        }

    }

    /*Generar código de la estructura programa presupuestario*/
    private String generarCodigo(EstructuraProgramaPresupuestario estructuraPrograma) {

        if (estructuraPrograma.getPadre() != null) {
            return estructuraPrograma.getPadre().getCodigo() + getSubCodigo(estructuraPrograma);
        }

        return getSubCodigo(estructuraPrograma);
    }

    /*Funciones adicionales*/
    public boolean renderElementForm(EstructuraProgramaPresupuestario estructuraPrograma, int position) {

        if (estructuraPrograma.getNivel() != null) {
            return position <= estructuraPrograma.getNivel().getOrden();
        }
        return false;
    }

    public boolean readOnlyElementForm(EstructuraProgramaPresupuestario estructuraPrograma, int position) {

        return position != estructuraPrograma.getNivel().getOrden();

    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("mostrarColumnas");
        PrimeFaces.current().ajax().update("estructuraProgramasTable");
        PrimeFaces.current().ajax().update("formEstructuraProgramatica");
    }

    public Boolean editable() {
        boolean edit = estructuraProgramaPresupuestario.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    /*Generar código de la estructura programa presupuestario*/
    private String getSubCodigo(EstructuraProgramaPresupuestario estructuraPrograma) {

        String format = "%0" + estructuraPrograma.getNivel().getLongitud() + "d";
        switch (estructuraPrograma.getNivel().getOrden()) {

            case 1: {
                return String.format(format, estructuraPrograma.getFuncion());
            }
            case 2: {
                return String.format(format, estructuraPrograma.getPrograma());
            }
            case 3: {
                return String.format(format, estructuraPrograma.getSubprograma());
            }
        }
        return "";
    }

    /*Formulario de registro padre*/
    public void formPadre() {

        Boolean existe = estructuraProgramaPresupuestarioService.consultarExitenciaMasterCatalogo(opcionBusqueda.getAnio());
        if (existe) {
            try {
                estructuraProgramaPresupuestario = new EstructuraProgramaPresupuestario();
                estructuraProgramaPresupuestario.setFechaVigente(fechaVigente());
                estructuraProgramaPresupuestario.setClasificacion((CatalogoItem) catalogoService.getTipoItem("FUNC"));
                estructuraProgramaPresupuestario.setNivel(nivelService.getFirstNivel("plan_programatico", "NIVELES"));
                estructuraProgramaPresupuestario.setPeriodo(opcionBusqueda.getAnio());
                estructuraProgramaPresupuestario.setFuncion(estructuraProgramaPresupuestarioService.getMaxValueForChild(estructuraProgramaPresupuestario, true));

                actualizarInfoGridFom();
                PrimeFaces.current().executeScript("PF('registrarEstructuraDialog').show()");
                PrimeFaces.current().ajax().update(":formEstructuraProgramatica");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            JsfUtil.addWarningMessage("AVISO", "No esta creado un Catalogo de Presupuesto para el período " + opcionBusqueda.getAnio());
        }

    }

    /*Formulario de registro Hijo*/
    public void formHijo(EstructuraProgramaPresupuestario estructuraPrograma, boolean edit) {
        if (edit) {
            estructuraProgramaPresupuestario = estructuraPrograma;

        } else {
            estructuraProgramaPresupuestario = new EstructuraProgramaPresupuestario();
            estructuraProgramaPresupuestario.setPadre(estructuraPrograma);
            estructuraProgramaPresupuestario.setNivel(nivelService.getProximoNivel(estructuraPrograma.getNivel()));
            estructuraProgramaPresupuestario.setPeriodo(estructuraPrograma.getPeriodo());
            estructuraProgramaPresupuestario.setFechaVigente(fechaVigente());

            switch (estructuraPrograma.getNivel().getOrden()) {
                case 1:
                    estructuraProgramaPresupuestario.setFuncion(estructuraPrograma.getFuncion());
                    estructuraProgramaPresupuestario.setClasificacion((CatalogoItem) catalogoService.getTipoItem("PROG"));
                    estructuraProgramaPresupuestario.setPrograma(estructuraProgramaPresupuestarioService.getMaxValueForChild(estructuraPrograma, false));
                    break;
                case 2:
                    estructuraProgramaPresupuestario.setFuncion(estructuraPrograma.getFuncion());
                    estructuraProgramaPresupuestario.setPrograma(estructuraPrograma.getPrograma());
                    estructuraProgramaPresupuestario.setClasificacion((CatalogoItem) catalogoService.getTipoItem("SUBPROG"));
                    estructuraProgramaPresupuestario.setSubprograma(estructuraProgramaPresupuestarioService.getMaxValueForChild(estructuraPrograma, false));
            }
        }
        actualizarInfoGridFom();
        PrimeFaces.current().executeScript("PF('registrarEstructuraDialog').show()");
        PrimeFaces.current().ajax().update(":formEstructuraProgramatica");
    }

    /*Eliminar*/
    public void delete(EstructuraProgramaPresupuestario estructuraPrograma) {
        List<EstructuraProgramaPresupuestario> hijos = estructuraProgramaPresupuestarioService.getHijosByPadre(estructuraPrograma);

        if (hijos != null) {
            if (!hijos.isEmpty()) {
                JsfUtil.addErrorMessage("Estructura Programa Presupuestario", estructuraPrograma.getCodigo() + " tiene cuentas de movimientos asociadas.");
                return;
            }
        }
        JsfUtil.addSuccessMessage("Estructura Programa Presupuestario", estructuraPrograma.getCodigo() + " eliminada con éxito");
        estructuraPrograma.setEstado(Boolean.FALSE);
        estructuraProgramaPresupuestarioService.edit(estructuraPrograma);
        PrimeFaces.current().ajax().update("estructuraProgramasTable");
    }

    /*Guardar*/
    public void save() {
        boolean edit = estructuraProgramaPresupuestario.getId() != null;
        try {
            estructuraProgramaPresupuestario.setCodigo(generarCodigo(estructuraProgramaPresupuestario));
            EstructuraProgramaPresupuestario existeplan = estructuraProgramaPresupuestarioService.existeCuenta(estructuraProgramaPresupuestario);
            if (estructuraProgramaPresupuestario.getId() == null && existeplan != null) {
                JsfUtil.addWarningMessage("Estructura Programa Presupuestario", estructuraProgramaPresupuestario.getCodigo() + " se encuentra registrado en el sistema.");
                return;
            }
            if (estructuraProgramaPresupuestario.getId() != null && existeplan != null) {
                if (!Objects.equals(estructuraProgramaPresupuestario.getId(), existeplan.getId())) {
                    JsfUtil.addWarningMessage("Estructura Programa Presupuestario", estructuraProgramaPresupuestario.getCodigo() + " se encuentra registrado en el sistema.");
                    return;
                }
            }
            if (estructuraProgramaPresupuestario.getId() == null) {
                estructuraProgramaPresupuestario.setUsuarioCreacion(userSession.getNameUser());
                estructuraProgramaPresupuestario.setFechaCreacion(new Date());
                estructuraProgramaPresupuestarioSeleccionado = estructuraProgramaPresupuestarioService.create(estructuraProgramaPresupuestario);
            } else {
                estructuraProgramaPresupuestario.setUsuarioModifica(userSession.getNameUser());
                estructuraProgramaPresupuestario.setFechaModificacion(new Date());
                estructuraProgramaPresupuestarioService.edit(estructuraProgramaPresupuestario);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('registrarEstructuraDialog').hide()");
        PrimeFaces.current().ajax().update(":estructuraProgramasTable");
        JsfUtil.addSuccessMessage("Estructura Programa Presupuestario", estructuraProgramaPresupuestario.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
    }

    //<editor-fold defaultstate="collapsed" desc="Get & Set">
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public EstructuraProgramaPresupuestario getEstructuraProgramaPresupuestario() {
        return estructuraProgramaPresupuestario;
    }

    public void setEstructuraProgramaPresupuestario(EstructuraProgramaPresupuestario estructuraProgramaPresupuestario) {
        this.estructuraProgramaPresupuestario = estructuraProgramaPresupuestario;
    }

    public EstructuraProgramaPresupuestario getEstructuraProgramaPresupuestarioSeleccionado() {
        return estructuraProgramaPresupuestarioSeleccionado;
    }

    public void setEstructuraProgramaPresupuestarioSeleccionado(EstructuraProgramaPresupuestario estructuraProgramaPresupuestarioSeleccionado) {
        this.estructuraProgramaPresupuestarioSeleccionado = estructuraProgramaPresupuestarioSeleccionado;
    }

    public int getCantColumnas() {
        return cantColumnas;
    }

    public void setCantColumnas(int cantColumnas) {
        this.cantColumnas = cantColumnas;
    }

    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public EstructuraProgramaPresupuestarioLazy getEstructuraProgramaPresupuestarioLazy() {
        return estructuraProgramaPresupuestarioLazy;
    }

    public void setEstructuraProgramaPresupuestarioLazy(EstructuraProgramaPresupuestarioLazy estructuraProgramaPresupuestarioLazy) {
        this.estructuraProgramaPresupuestarioLazy = estructuraProgramaPresupuestarioLazy;
    }
//</editor-fold>

}
