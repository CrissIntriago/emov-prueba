/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UbicacionInstitucion;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UbicacionInstitucionService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "unidadAdministrativaView")
@ViewScoped
public class UnidadAdministrativaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private UnidadAdministrativaService unidadAdmiinistrativaService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UbicacionInstitucionService institucionService;
    private TreeNode root;

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    private UnidadAdministrativa unidadAdministrativa;
    private List<CatalogoItem> listaCatalogoItem;
    private List<UbicacionInstitucion> listUbicacionInst;
    private LazyModel<UnidadAdministrativa> lazy;
    private List<UnidadAdministrativa> listaRelacion, padrelista, padresReasignar;
    private boolean compoenente;
    private UnidadAdministrativa unidadSeleccionada;

    @PostConstruct
    public void inicializar() {
        this.unidadAdministrativa = new UnidadAdministrativa();
        this.lazy = new LazyModel(UnidadAdministrativa.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("hasPadre", 0);
        lazy.getSorteds().put("nombre", "ASC");
        lazy.getSorteds().put("tipoUnidad.orden", "ASC");
        lazy.setDistinct(false);
        this.compoenente = Boolean.TRUE;
        this.padrelista = unidadAdmiinistrativaService.getUnidadesEstado(true);
        this.listaCatalogoItem = unidadAdmiinistrativaService.getTiposUnidades();
        listUbicacionInst = institucionService.getAllUbicacionInstitucionByEstadoTrue();

    }

    public void formulario(UnidadAdministrativa p) throws ParseException {
        /**
         * * LAS UNIDADES SERÁN VALIDADAS POR SU ORDEN, DEBEN ESTAR ORDENADAS
         * EN CAT_ITEM **
         */
        listUbicacionInst = institucionService.getAllUbicacionInstitucionByEstadoTrue();

        if (p != null) {
            this.unidadAdministrativa = p;
            if (p.getTipoUnidad() != null) {
                this.listaCatalogoItem = catalogoItemService.findCatalogoItemsEQOrden("tipo_unidad", p.getTipoUnidad().getOrden());
            } else {
                this.listaCatalogoItem = catalogoItemService.findCatalogoItems("tipo_unidad");
            }
        } else {
            this.listaCatalogoItem = catalogoItemService.findCatalogoItems("tipo_unidad");
            this.unidadAdministrativa = new UnidadAdministrativa();
            unidadAdministrativa.setFechaVigencia(fechaVigente());
            unidadAdministrativa.setFechaCaducidad(fechaCaducidad());
            unidadAdministrativa.setTipoUnidad(unidadAdmiinistrativaService.getCatalogoTipo("tipo_unidad", "ENT"));
        }
        PrimeFaces.current().executeScript("PF('formDlg').show();");
        PrimeFaces.current().ajax().update("form");
    }

    /**
     * retorna la priemra fecha del anio actual
     *
     * @return
     */
    public Date fechaVigente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Calendar fecha1 = new GregorianCalendar();
        int anio = fecha1.get(Calendar.YEAR);

        String dia = "01/01/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    /**
     * retorna la ultima fecha del anio actual
     *
     * @return
     */
    public Date fechaCaducidad() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Calendar fecha1 = new GregorianCalendar();
        int anio = fecha1.get(Calendar.YEAR);

        String dia = "31/12/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    /**
     * este permite ingresar la unidad que le sigue definido por la clsificacion
     * donde el mismo seteara la unidad administrativa
     *
     * @param c
     * @param edit
     */
    public void form(UnidadAdministrativa c, boolean edit) {
        listUbicacionInst = institucionService.getAllUbicacionInstitucionByEstadoTrue();
        if (edit) {
            unidadAdministrativa = c;
        } else {
            unidadAdministrativa = new UnidadAdministrativa();
            this.listaCatalogoItem = catalogoItemService.findCatalogoItemsOrden("tipo_unidad", c.getTipoUnidad().getOrden());
            unidadAdministrativa.setPadre(c);
            unidadAdministrativa.setFechaVigencia(fechaVigente());
            unidadAdministrativa.setFechaCaducidad(fechaCaducidad());
        }
        PrimeFaces.current().executeScript("PF('formDlg').show();");
        PrimeFaces.current().ajax().update("form");
    }

    public void reasignarUnidad(UnidadAdministrativa c) {
        unidadAdministrativa = c;
        padresReasignar = unidadAdmiinistrativaService.findUnidadesAdministrativasPadres(c);
        PrimeFaces.current().executeScript("PF('formDlgPadres').show();");
        PrimeFaces.current().ajax().update("formDlgPadres");
    }

    /**
     * inserta o actualizar en la base de datos los datos del obejto unidad
     * administrativa
     *
     * @throws ParseException
     */
    public void guardarUnidadAdministrativa() throws ParseException {

        boolean edit = unidadAdministrativa.getId() != null;
        if (unidadAdministrativa.getTipoUnidad() == null) {
            JsfUtil.addErrorMessage("Advertencia", "Asignar el tipo de la Unidad Administrativa.");
            return;
        }
        if (unidadAdministrativa.getUbicacionInstitucion() == null) {
            JsfUtil.addErrorMessage("Advertencia", "Asignar la ubicación de la Unidad Administrativa.");
            return;
        }
        if (!edit) {
            unidadAdministrativa.setEstado(Boolean.TRUE);
            unidadAdministrativa.setEstadoActivo(Boolean.TRUE);
            unidadAdministrativa = unidadAdmiinistrativaService.create(unidadAdministrativa);

        } else {
            if (unidadAdministrativa.getFechaCaducidad() != null) {
                if (unidadAdministrativa.getFechaCaducidad().before(Utils.devolverFecha(new Date(), "yyyy-MM-dd"))) {
                    unidadAdministrativa.setEstadoActivo(Boolean.FALSE);
                } else if (!unidadAdministrativa.getEstadoActivo()) {
                    unidadAdministrativa.setEstadoActivo(Boolean.TRUE);
                }
            }
            if (unidadAdministrativa.getTipoUnidad() != null) {
                List<UnidadAdministrativa> hijos = unidadAdmiinistrativaService.findHijoMayor(unidadAdministrativa);
                if (!hijos.isEmpty()) {
                    JsfUtil.addErrorMessage("Advertencia", "No puede cambiar el tipo de unidad de esta unidad administrativa a una menor que la que posee un hijo");
                    return;
                }
            }
            unidadAdmiinistrativaService.edit(unidadAdministrativa);
        }

        PrimeFaces.current().executeScript("PF('formDlg').hide()");
        PrimeFaces.current().executeScript("PF('formDlgPadres').hide()");
//        PrimeFaces.current().ajax().update("formu,messages");
        JsfUtil.addSuccessMessage("Unidad Administrativa", unidadAdministrativa.getNombre() + (edit ? " editada" : " registrada") + " con éxito.");
        actualizarFomrularios();
    }

    /**
     * verifica si tienes hijos para despues usar para hacer un borrado logico
     * en el caso que no tenga hijos
     *
     * @param c
     */
    public void getConfirmHijos(UnidadAdministrativa c) {
        List<UnidadAdministrativa> hijos = unidadAdmiinistrativaService.findByNamedQuery("UnidadAdministrativa.findByHijos", new Object[]{c.getId()});
        if (hijos != null) {
            if (!hijos.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Unidad Administrativa", c.getNombre() + " tiene unidades asociadas.");
                return;
            }
            JsfUtil.addSuccessMessage("Unidad administrativa", c.getNombre() + " eliminada con éxito");
            c.setEstado(Boolean.FALSE);
            unidadAdmiinistrativaService.edit(c);
            PrimeFaces.current().ajax().update("formu:cuentas, messages");
        }

    }

    public void cargandoNodos(UnidadAdministrativa dept) {
        List<UnidadAdministrativa> unidadesHijas = unidadAdmiinistrativaService.getPadreUnidad(dept);
        root = new DefaultTreeNode(dept, null);
        for (UnidadAdministrativa item : unidadesHijas) {
            TreeNode hijo = new DefaultTreeNode(item, root);
            fillSons(item, hijo);
        }

    }

    public void fillSons(UnidadAdministrativa departamento, TreeNode node) {
        List<UnidadAdministrativa> list = unidadAdmiinistrativaService.getPadreUnidad(departamento);
        if (!list.isEmpty()) {
            for (UnidadAdministrativa item : list) {
                TreeNode hijo = new DefaultTreeNode(item, node);
                fillSons(item, hijo);
            }
        }
    }
//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">

    public void actualizarFomrularios() {
        unidadAdministrativa = null;
    }

    public void eliminar(UnidadAdministrativa u) {
        getConfirmHijos(u);
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public LazyModel<UnidadAdministrativa> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<UnidadAdministrativa> lazy) {
        this.lazy = lazy;
    }

    public List<CatalogoItem> getListaCatalogoItem() {
        return listaCatalogoItem;
    }

    public void setListaCatalogoItem(List<CatalogoItem> listaCatalogoItem) {
        this.listaCatalogoItem = listaCatalogoItem;
    }

    public List<UnidadAdministrativa> getListaRelacion() {
        return listaRelacion;
    }

    public void setListaRelacion(List<UnidadAdministrativa> listaRelacion) {
        this.listaRelacion = listaRelacion;
    }

    public boolean isCompoenente() {
        return compoenente;
    }

    public void setCompoenente(boolean compoenente) {
        this.compoenente = compoenente;
    }

    public UnidadAdministrativa getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(UnidadAdministrativa unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public List<UnidadAdministrativa> getPadrelista() {
        return padrelista;
    }

    public void setPadrelista(List<UnidadAdministrativa> padrelista) {
        this.padrelista = padrelista;
    }
//</editor-fold>

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<UnidadAdministrativa> getPadresReasignar() {
        return padresReasignar;
    }

    public void setPadresReasignar(List<UnidadAdministrativa> padresReasignar) {
        this.padresReasignar = padresReasignar;
    }

    public List<UbicacionInstitucion> getListUbicacionInst() {
        return listUbicacionInst;
    }

    public void setListUbicacionInst(List<UbicacionInstitucion> listUbicacionInst) {
        this.listUbicacionInst = listUbicacionInst;
    }

}
