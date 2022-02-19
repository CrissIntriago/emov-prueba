/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.BeneficiosDecimoCuarto;
import com.origami.sigef.common.entities.BeneficiosDecimoTercero;
import com.origami.sigef.common.entities.BeneficiosSindicales;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.services.BeneficioDecimoCuartoService;
import com.origami.sigef.talentohumano.services.BeneficioDecimoTerceroService;
import com.origami.sigef.talentohumano.services.BeneficiosSindicalesService;
import com.origami.sigef.talentohumano.services.TipoRolBeneficiosService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI1
 */
@Named(value = "tipoRolBeneficiosView")
@ViewScoped
public class TipoRolBeneficiosBeans implements Serializable {

    @Inject
    private TipoRolBeneficiosService tipoRolBenefService;
    private TipoRolBeneficios tipoRolBeneficio;
    private List<TipoRolBeneficios> listTipoRolBeneficios;

    @Inject
    private CatalogoItemService itemService;
    private List<CatalogoItem> listTipoBeneficios;
    private CatalogoItem estadoTipoRol;

    @Inject
    private UserSession userSession;
    private List<MasterCatalogo> periodos;
    @Inject
    private MasterCatalogoService masterService;
    private Short anioAux;

    private LazyModel<TipoRolBeneficios> lazyTipoRolBeneficios;

    private boolean editBlock;
    private boolean readonlyDate;

    @Inject
    private BeneficiosSindicalesService sindicalesService;

    @Inject
    private BeneficioDecimoCuartoService beneficioCuartoService;

    @Inject
    private BeneficioDecimoTerceroService beneficioTercerService;

    @PostConstruct
    public void init() {
        estadoTipoRol = new CatalogoItem();
        tipoRolBeneficio = new TipoRolBeneficios();
        periodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
        listTipoBeneficios = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "tipo_roles_beneficios");
        editBlock = false;
        readonlyDate = true;
        anioAux = Utils.getAnio(new Date()).shortValue();
        lazyTipoRolBeneficios = new LazyModel<>(TipoRolBeneficios.class);
        lazyTipoRolBeneficios.getFilterss().put("estado", true);
        lazyTipoRolBeneficios.getFilterss().put("periodo", anioAux);

    }

    public void form(TipoRolBeneficios rolBeneficios) {
        if (anioAux == null) {
            JsfUtil.addWarningMessage("Información", "Debe ingresar el año del Período de Rol Beneficios.");
            return;
        }
        if (rolBeneficios != null) {
            tipoRolBeneficio = rolBeneficios;
            setReadonlyDate(Boolean.TRUE);
            setEditBlock(true);

        } else {
            tipoRolBeneficio = new TipoRolBeneficios();
            tipoRolBeneficio.setPeriodo(anioAux);
            setEditBlock(false);

        }
        PrimeFaces.current().executeScript("PF('dlgRolBeneficio').show()");
        PrimeFaces.current().ajax().update(":panelRolBeneficios");
    }

    public void updatePeriodo() {
        GregorianCalendar calendar = new GregorianCalendar();
        GregorianCalendar d = new GregorianCalendar();
        GregorianCalendar h = new GregorianCalendar();
        if ("ROL_TIPO_DEC_CUARTO".equals(tipoRolBeneficio.getTipo().getCodigo())) {
            setReadonlyDate(Boolean.TRUE);
            d.set(anioAux.intValue() - 1, 2, 1);
            tipoRolBeneficio.setPeriodoDesde(d.getTime());
            if (calendar.isLeapYear(anioAux)) {
                h.set(anioAux.intValue(), 1, 29);
                tipoRolBeneficio.setPeriodoHasta(h.getTime());
            } else {
                h.set(anioAux.intValue(), 1, 28);
                tipoRolBeneficio.setPeriodoHasta(h.getTime());
            }
        }
        if ("ROL_TIPO_DEC_TERCERO".equals(tipoRolBeneficio.getTipo().getCodigo())) {
            setReadonlyDate(Boolean.TRUE);
            d.set(anioAux.intValue() - 1, 11, 1);
            h.set(anioAux.intValue(), 10, 30);
            tipoRolBeneficio.setPeriodoDesde(d.getTime());
            tipoRolBeneficio.setPeriodoHasta(h.getTime());
        }
        if ("ROL_TIPO_BEN_SIN".equals(tipoRolBeneficio.getTipo().getCodigo())) {
            tipoRolBeneficio.setPeriodoDesde(null);
            tipoRolBeneficio.setPeriodoHasta(null);
            setReadonlyDate(Boolean.FALSE);
        }
    }

    public void guardar() {
        if ("".equals(tipoRolBeneficio.getDescripcion())) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la descripción del Tipo De Rol Beneficio.");
            return;
        }
        if (tipoRolBeneficio.getTipo() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el Tipo De Rol Beneficio.");
            return;
        }
        if ("ROL_TIPO_BEN_SIN".equals(tipoRolBeneficio.getTipo().getCodigo())) {
            if (tipoRolBeneficio.getPeriodoDesde() == null) {
                JsfUtil.addWarningMessage("Información", " Debe ingresar Fecha Desde del Tipo De Rol Beneficio.");
                return;
            }
            if (tipoRolBeneficio.getPeriodoHasta() == null) {
                JsfUtil.addWarningMessage("Información", " Debe ingresar Fecha Hasta del Tipo De Rol Beneficio.");
                return;
            }
        }
        Boolean validar = false;
        listTipoRolBeneficios = new ArrayList<>();
        listTipoRolBeneficios = tipoRolBenefService.findByNamedQuery("TipoRolBeneficios.findByPeriodo", anioAux);
        for (TipoRolBeneficios item : listTipoRolBeneficios) {
            System.out.println(item.getTipo());
        }
        if (!listTipoBeneficios.isEmpty()) {
            for (TipoRolBeneficios item : listTipoRolBeneficios) {
                if (Objects.equals(tipoRolBeneficio.getTipo(), item.getTipo())) {
                    validar = true;
                    break;
                }
            }
        }

        boolean edit = tipoRolBeneficio.getId() != null;
        if (!edit) {
            if (validar == true) {
                JsfUtil.addWarningMessage("Información", " Este Tipo ya existe para este Periodo.");
                return;
            }
            tipoRolBeneficio.setEstado(Boolean.TRUE);
            tipoRolBeneficio.setFechaCreacion(new Date());
            tipoRolBeneficio.setFechaModificacion(new Date());
            tipoRolBeneficio.setEstadoAprobacionBen(getTipoEstado("registrado-rol"));
            tipoRolBeneficio.setUsuarioCreacion(userSession.getNameUser());
            tipoRolBeneficio.setUsuarioModificacion(userSession.getNameUser());
            tipoRolBeneficio = tipoRolBenefService.create(tipoRolBeneficio);
        } else {
            tipoRolBeneficio.setUsuarioModificacion(userSession.getNameUser());
            tipoRolBeneficio.setFechaModificacion(new Date());
            tipoRolBenefService.edit(tipoRolBeneficio);
        }
        PrimeFaces.current().executeScript("PF('dlgRolBeneficio').hide()");
        JsfUtil.addSuccessMessage("Información", tipoRolBeneficio.getDescripcion() + (edit ? " editada" : " registrada") + " con éxito.");
    }

    public void buscar() {
        if (anioAux == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el Período para realizar una Busqueda.");
            lazyTipoRolBeneficios = null;
            return;
        }
        lazyTipoRolBeneficios = new LazyModel<>(TipoRolBeneficios.class);
        lazyTipoRolBeneficios.getFilterss().put("estado", true);
        lazyTipoRolBeneficios.getFilterss().put("periodo", anioAux);

    }

    public void eliminar(TipoRolBeneficios rolBeneficio) {
        JsfUtil.addSuccessMessage("Información", rolBeneficio.getDescripcion() + " eliminada con éxito");
        eliminarReferencias(rolBeneficio);
        rolBeneficio.setEstado(Boolean.FALSE);
        tipoRolBenefService.edit(rolBeneficio);
        PrimeFaces.current().ajax().update(":formMain");

    }

    public void eliminarReferencias(TipoRolBeneficios rolBeneficio) {
        if ("ROL_TIPO_DEC_TERCERO".equals(rolBeneficio.getTipo().getCodigo())) {
            List<BeneficiosDecimoTercero> listDecimoTercero;
            listDecimoTercero = beneficioTercerService.getBeneficiosTerceroXtipo(rolBeneficio);
            if (!listDecimoTercero.isEmpty()) {
                for (BeneficiosDecimoTercero itemT : listDecimoTercero) {
                    itemT.setEstado(Boolean.FALSE);
                    System.out.println(itemT.getAcumulacionFondoReserva().getServidor().getPersona().getNombreCompleto());
                    beneficioTercerService.edit(itemT);
                }
            }
        }
        if ("ROL_TIPO_DEC_CUARTO".equals(rolBeneficio.getTipo().getCodigo())) {
            List<BeneficiosDecimoCuarto> listDecimoCuarto;
            listDecimoCuarto = beneficioCuartoService.getBeneficiosCuartoXtipo(rolBeneficio);
            if (!listDecimoCuarto.isEmpty()) {
                for (BeneficiosDecimoCuarto itemT : listDecimoCuarto) {
                    itemT.setEstado(Boolean.FALSE);
                    System.out.println(itemT.getAcumulacionFondos().getServidor().getPersona().getNombreCompleto());
                    beneficioCuartoService.edit(itemT);
                }
            }
        }
        if ("ROL_TIPO_BEN_SIN".equals(rolBeneficio.getTipo().getCodigo())) {
            List<BeneficiosSindicales> listSindicales;
            listSindicales = sindicalesService.findByBeneficiosXTipo(rolBeneficio);
            if (!listSindicales.isEmpty()) {
                for (BeneficiosSindicales itemT : listSindicales) {
                    itemT.setEstado(Boolean.FALSE);
                    System.out.println(itemT.getServidorP().getPersona().getNombreCompleto());
                    sindicalesService.edit(itemT);
                }
            }
        }
    }

    public CatalogoItem getTipoEstado(String codigo) {
        estadoTipoRol = new CatalogoItem();
        estadoTipoRol = itemService.getEstadoRol(codigo);
        return estadoTipoRol;
    }

    public void aprobar(TipoRolBeneficios rol) {
        rol.setEstadoAprobacionBen(getTipoEstado("aprobado-rol"));
        rol.setUsuarioModificacion(userSession.getNameUser());
        rol.setFechaModificacion(new Date());
        tipoRolBenefService.edit(rol);
        JsfUtil.addSuccessMessage("Información", rol.getDescripcion() + " aprobado con éxito");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public Short getAnioAux() {
        return anioAux;
    }

    public void setAnioAux(Short anioAux) {
        this.anioAux = anioAux;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public TipoRolBeneficios getTipoRolBeneficio() {
        return tipoRolBeneficio;
    }

    public void setTipoRolBeneficio(TipoRolBeneficios tipoRolBeneficio) {
        this.tipoRolBeneficio = tipoRolBeneficio;
    }

    public List<CatalogoItem> getListTipoBeneficios() {
        return listTipoBeneficios;
    }

    public void setListTipoBeneficios(List<CatalogoItem> listTipoBeneficios) {
        this.listTipoBeneficios = listTipoBeneficios;
    }

    public LazyModel<TipoRolBeneficios> getLazyTipoRolBeneficios() {
        return lazyTipoRolBeneficios;
    }

    public void setLazyTipoRolBeneficios(LazyModel<TipoRolBeneficios> lazyTipoRolBeneficios) {
        this.lazyTipoRolBeneficios = lazyTipoRolBeneficios;
    }

    public boolean isEditBlock() {
        return editBlock;
    }

    public void setEditBlock(boolean editBlock) {
        this.editBlock = editBlock;
    }

    public boolean isReadonlyDate() {
        return readonlyDate;
    }

    public void setReadonlyDate(boolean readonlyDate) {
        this.readonlyDate = readonlyDate;
    }

}
