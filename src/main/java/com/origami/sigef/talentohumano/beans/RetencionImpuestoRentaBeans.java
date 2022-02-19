/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.RetencionesImpuestoRenta;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.services.GastoPersonalService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.RetencionImpuestoRentaService;
import com.origami.sigef.talentohumano.services.TablaImpuestoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Origami
 */
@Named(value = "RetencionImpuestoRentaBeans")
@ViewScoped
public class RetencionImpuestoRentaBeans implements Serializable {

    private LazyModel<RetencionesImpuestoRenta> lazy;
    private short periodo;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private TipoRol rolSeleccionado;
    private List<ValoresDistributivo> valores;
    private RetencionesImpuestoRenta retencion;
    private BigDecimal porcentajeLosep;

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private RetencionImpuestoRentaService retencionService;
    @Inject
    private GastoPersonalService gastoservices;
    @Inject
    private ValoresDistributivoService valoresservice;
    @Inject
    private TablaImpuestoService tablaservice;
    @Inject
    private ParametrizableService parametroService;

    private BigDecimal exceso;
    private BigDecimal valorFraccion;
//    private CatalogoItem rolGeneral;
    private ParametrosTalentoHumano valoPrametro;

    @PostConstruct
    public void inicializate() {
        retencion = new RetencionesImpuestoRenta();
        retencion.setTipoRol(new TipoRol());
        retencion.setGastoPersonal(new GastoPersonal());
        retencion.setValorParametrizado(new ParametrosTalentoHumano());
        rolSeleccionado = new TipoRol();
        periodo = Utils.getAnio(new Date()).shortValue();
        valoPrametro = parametroService.valorParametroTipo("IMP_RENTA");
        rolesMensuales = tipoRolService.listaRolesXanio(periodo);
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        porcentajeLosep = valoresservice.valorParametrizado("APOR_IND_LOSEP");
    }

    public void buscar() {
        if (retencion.getTipoRol() == null) {
            lazy = null;
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        rolSeleccionado = retencion.getTipoRol();
        List<GastoPersonal> lista = gastoservices.listGastoPersonal(rolSeleccionado.getAnio());
        List<RetencionesImpuestoRenta> listaRetenciones = retencionService.listaretencion(rolSeleccionado);
        try {
            if (listaRetenciones.isEmpty()) {
                retencionService.addList(lista, rolSeleccionado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lazy = new LazyModel<>(RetencionesImpuestoRenta.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("periodo", periodo);
        lazy.getFilterss().put("tipoRol", retencion.getTipoRol());
        lazy.getSorteds().put("gastoPersonal.servidorPublico.persona.apellido", "ASC");
        lazy.setDistinct(false);
        if (rolSeleccionado.getEstadoAprobacion().equals(registrado)) {
            retencionService.actualizarValores(rolSeleccionado);
        } else {
        }
    }

    public void actualizarDatos() {
        List<RetencionesImpuestoRenta> listaRetenciones = retencionService.listaretencion(rolSeleccionado);
        if (listaRetenciones == null) {
            JsfUtil.addErrorMessage("", "No hay datos para guardar");
            return;
        }
        List<GastoPersonal> listaGastosPersonles = gastoservices.listGastoPersonal(rolSeleccionado.getAnio());
        if (listaGastosPersonles == null) {
            JsfUtil.addErrorMessage("", "No hay datos para guardar");
            return;
        }
        List<RetencionesImpuestoRenta> delete = new ArrayList<>();
        List<GastoPersonal> agregar = new ArrayList<>();
        if (listaRetenciones.size() > listaGastosPersonles.size()) {
            for (RetencionesImpuestoRenta f : listaRetenciones) {
                int cont = 0;
                for (GastoPersonal a : listaGastosPersonles) {
                    cont++;
                    if (f.getGastoPersonal().equals(a)) {
                        cont--;
                    }
                }
                if (cont == listaGastosPersonles.size()) {
                    delete.add(f);
                }
            }
            retencionService.eliminarList(delete);
        }
        if (listaRetenciones.size() < listaGastosPersonles.size()) {
            for (GastoPersonal a : listaGastosPersonles) {
                int cont = 0;
                for (RetencionesImpuestoRenta f : listaRetenciones) {
                    cont++;
                    if (a.equals(f.getGastoPersonal())) {
                        cont--;
                    }
                }
                if (cont == listaRetenciones.size()) {
                    agregar.add(a);
                }
            }
            retencionService.addList(agregar, rolSeleccionado);
        }
        retencionService.actualizarValores(rolSeleccionado);
    }

    public void eliminar(RetencionesImpuestoRenta retencion) {
        JsfUtil.addSuccessMessage("Servidor Público", retencion.getGastoPersonal().getServidorPublico().getPersona().getNombreCompleto() + " Eliminada con Exito");
        retencion.setEstado(Boolean.FALSE);
        retencionService.edit(retencion);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public void eliminarRegistros() {
        if (rolSeleccionado != null) {
            return;
        }
        List<RetencionesImpuestoRenta> listaRetenciones = retencionService.listaretencion(rolSeleccionado);
        retencionService.eliminarList(listaRetenciones);
        JsfUtil.addSuccessMessage("Retencin Impuesto a la Renta ", " Registros Eliminados Correctamente");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public LazyModel<RetencionesImpuestoRenta> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RetencionesImpuestoRenta> lazy) {
        this.lazy = lazy;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public CatalogoItem getRegistrado() {
        return registrado;
    }

    public void setRegistrado(CatalogoItem registrado) {
        this.registrado = registrado;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public List<ValoresDistributivo> getValores() {
        return valores;
    }

    public void setValores(List<ValoresDistributivo> valores) {
        this.valores = valores;
    }

    public RetencionesImpuestoRenta getRetencion() {
        return retencion;
    }

    public void setRetencion(RetencionesImpuestoRenta retencion) {
        this.retencion = retencion;
    }

    public BigDecimal getPorcentajeLosep() {
        return porcentajeLosep;
    }

    public void setPorcentajeLosep(BigDecimal porcentajeLosep) {
        this.porcentajeLosep = porcentajeLosep;
    }

}
