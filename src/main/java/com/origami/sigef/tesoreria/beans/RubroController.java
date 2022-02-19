/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import com.origami.sigef.tesoreria.service.RubroService;
import com.origami.sigef.tesoreria.service.RubroTipoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author gutya
 */
@Named(value = "rubroView")
@ViewScoped
public class RubroController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private RubroService rubroService;
    @Inject
    private RubroTipoService rubroTipoService;

    @Inject
    private CatalogoService catalogoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ParametrizableService parametrizableService;

    private LazyModel<Rubro> rubros;
    private Rubro rubro;
    private Boolean edit;
    private RubroTipo rubroTipo;
    private List<RubroTipo> rubroTipos;
    private short periodo;
    private OpcionBusqueda opcionBusqueda;
    private List<MasterCatalogo> periodos;

    private List<CatalogoItem> categoriaRubros;

    private final String IVA = "IVA";
    private final String RENTAS = "RENTAS";
    private Boolean disabledCompras;
    private Boolean disabledVent;
    private Boolean renderedCompraVenta;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        renderedCompraVenta = Boolean.FALSE;
        disabledCompras = Boolean.FALSE;
        disabledVent = Boolean.FALSE;
        edit = Boolean.FALSE;
        rubroTipos = rubroTipoService.findByTipoFact();
        rubro = new Rubro();
        rubro.setPorcentaje(BigDecimal.ZERO);
        rubro.setPorcentajeRetencion(BigDecimal.ZERO);
        rubro.setPorcentajeLibre(Boolean.FALSE);
        rubros = new LazyModel<>(Rubro.class);
        rubros.getFilterss().put("estado", true);
        categoriaRubros = catalogoService.getItemsByCatalogo("categorias_rubros");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});

    }

    public void guardar() {
        edit = rubro.getId() != null;
        if (edit) {
            if (rubro.getVigenciaHasta() != null) {
                rubro.setVigente(Boolean.FALSE);
            }
            rubro.setPeriodo(periodo);
            rubro.setDescripcion(rubro.getDescripcion().toUpperCase());
            setValorTipo();
            rubroService.edit(rubro);
            edit = Boolean.FALSE;
        } else {
            rubro.setVigente(Boolean.TRUE);
            rubro.setPeriodo(periodo);
            rubro.setRubroTipo(rubroTipo);
            rubro.setTipoImpuesto(rubroTipo.getDescripcion().substring(0).toUpperCase());
            rubro.setDescripcion(rubro.getDescripcion().toUpperCase());
            rubro.setEstado(Boolean.TRUE);
            if (rubro.getValor() == null) {
                JsfUtil.addInformationMessage("Campo Valor requerido", "");
                return;
            }
            setValorTipo();
            rubro = rubroService.create(rubro);
        }
        JsfUtil.addInformationMessage("Datos " + (edit ? "Editados" : "Registrados") + " Correctamente", "");
        rubro = new Rubro();
        periodo = 0;
        renderedCompraVenta = Boolean.FALSE;
        disabledCompras = Boolean.FALSE;
        disabledCompras = Boolean.FALSE;
        PrimeFaces.current().ajax().update("datos");
    }

    public void setValorTipo() {
        if (rubroTipo != null) {
            if (rubroTipo.getDescripcion().equals(IVA)) {
                rubro.setPorcentajeRetencion(BigDecimal.valueOf(rubro.getValor()));
            } else if (rubroTipo.getDescripcion().equals(RENTAS)) {
                rubro.setPorcentaje(BigDecimal.valueOf(rubro.getValor()));
            }
        }
    }

    public void eliminar(Rubro rt) {
        rt.setEstado(Boolean.FALSE);
        rubroService.edit(rt);
    }

    public void editar(Rubro rt) {
        edit = Boolean.TRUE;
        rubro = rt;
        if (rubro.getPeriodo() != null) {
            periodo = rubro.getPeriodo();
        }
        rubroTipo = rubro.getRubroTipo();
    }

    public void cancelar() {
        edit = Boolean.FALSE;
        rubro = new Rubro();
        periodo = 0;
        rubroTipo = new RubroTipo();
    }

    public void disabledCompraVenta() {
        if (rubro.getPorcentajeLibre()) {
            disabledCompras = Boolean.FALSE;
            disabledVent = Boolean.TRUE;
        } else {
            disabledCompras = Boolean.TRUE;
            disabledVent = Boolean.FALSE;
        }
    }

    public void renderedGridCompraVenta() {
        if (rubroTipo != null && rubroTipo.getDescripcion().equals("IVA")) {
            renderedCompraVenta = Boolean.TRUE;
        } else {
            renderedCompraVenta = Boolean.FALSE;
        }
    }

//<editor-fold defaultstate="collapsed" desc="GETTERS AND SETTERS">
    public Boolean getRenderedCompraVenta() {
        return renderedCompraVenta;
    }

    public void setRenderedCompraVenta(Boolean renderedCompraVenta) {
        this.renderedCompraVenta = renderedCompraVenta;
    }

    public Boolean getDisabledCompras() {
        return disabledCompras;
    }

    public void setDisabledCompras(Boolean disabledCompras) {
        this.disabledCompras = disabledCompras;
    }

    public Boolean getDisabledVent() {
        return disabledVent;
    }

    public void setDisabledVent(Boolean disabledVent) {
        this.disabledVent = disabledVent;
    }

    public LazyModel<Rubro> getRubros() {
        return rubros;
    }

    public void setRubros(LazyModel<Rubro> rubros) {
        this.rubros = rubros;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public List<RubroTipo> getRubroTipos() {
        return rubroTipos;
    }

    public void setRubroTipos(List<RubroTipo> rubroTipos) {
        this.rubroTipos = rubroTipos;
    }

    public List<CatalogoItem> getCategoriaRubros() {
        return categoriaRubros;
    }

    public void setCategoriaRubros(List<CatalogoItem> categoriaRubros) {
        this.categoriaRubros = categoriaRubros;
    }

    public RubroTipo getRubroTipo() {
        return rubroTipo;
    }

    public void setRubroTipo(RubroTipo rubroTipo) {
        this.rubroTipo = rubroTipo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

//</editor-fold>
}
