/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.BeneficiosDecimoCuarto;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.BeneficioDecimoCuartoService;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.TipoRolBeneficiosService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "beneficioDecimoCuartoView")
@ViewScoped
public class BeneficioDecimoCuartoBeans implements Serializable {

    @Inject
    private TipoRolBeneficiosService tipoRolBenefService;
    private List<TipoRolBeneficios> listTipoRolBeneficios;
    private TipoRolBeneficios tipoRolBeneficios;

    @Inject
    private BeneficioDecimoCuartoService beneficioCuartoService;
    private BeneficiosDecimoCuarto beneficioCuarto;

    private LazyModel<BeneficiosDecimoCuarto> lazyBeneficioCuarto;
    private LazyModel<BeneficiosDecimoCuarto> lazyBeneficioCuartoInactivos;

    @Inject
    private CatalogoItemService catalogoItemService;
    private CatalogoItem decimoCuartoItem;

    @Inject
    private AcumulacionFondoReservaService acumulacionFondosService;

    @Inject
    private DiasLaboradoService diaService;

    @Inject
    private ValoresDistributivoService valoresService;

    @Inject
    private ValoresRolesService valorRolService;
    private ValoresRoles valorR;

    boolean desabilitar;
    boolean metodo;
    boolean boton;

    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;

    private BigDecimal totalCobrar;
    private BigDecimal totalAjuste;
    private BigDecimal totalBase;
    private BigDecimal totaldescuento;

    @PostConstruct
    public void init() {
        decimoCuartoItem = catalogoItemService.getEstadoRol("ACU-DECIMO-4to");
        tipoRolBeneficios = new TipoRolBeneficios();
        listTipoRolBeneficios = new ArrayList<>();
        listTipoRolBeneficios = tipoRolBenefService.findByTipoRolByTipo("ROL_TIPO_DEC_CUARTO");
        beneficioCuarto = new BeneficiosDecimoCuarto();
        desabilitar = true;
        metodo = false;
        boton = true;
        totalCobrar = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalAjuste = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalBase = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totaldescuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public void buscar() {

        if (tipoRolBeneficios == null) {
            JsfUtil.addWarningMessage("Información", " Ingrese un Período de Rol Beneficios.");
            lazyBeneficioCuarto = null;
            desabilitar = true;
            boton = true;
            return;
        } else {
            desabilitar = false;
        }

        List<AcumulacionFondoReserva> acumulacionFondosList;
        acumulacionFondosList = new ArrayList<>();
        acumulacionFondosList = acumulacionFondosService.AcumulacionFondosReservInNotBeneficioCuarto(true, decimoCuartoItem, tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta());
//        System.out.println(acumulacionFondosList);
        if (!acumulacionFondosList.isEmpty() || acumulacionFondosList != null) {
            beneficioCuartoService.guardar(acumulacionFondosList, tipoRolBeneficios);
        }
        List<BeneficiosDecimoCuarto> listBeneficiosC;
        listBeneficiosC = beneficioCuartoService.getBeneficiosCuartoXtipo(tipoRolBeneficios);
        if (!listBeneficiosC.isEmpty()) {
            if (tipoRolBeneficios.getEstadoAprobacionBen().getCodigo().equals("registrado-rol")) {
                beneficioCuartoService.update(listBeneficiosC, tipoRolBeneficios);
            }
        }

        lazyBeneficioCuarto = new LazyModel<>(BeneficiosDecimoCuarto.class);
        lazyBeneficioCuarto.getFilterss().put("estado", true);
        lazyBeneficioCuarto.getFilterss().put("tipoRolBeneficio", tipoRolBeneficios);
        lazyBeneficioCuarto.getSorteds().put("acumulacionFondos.servidor.persona.apellido", "ASC");
        lazyBeneficioCuarto.setDistinct(Boolean.FALSE);
        lazyBeneficioCuartoInactivos = new LazyModel<>(BeneficiosDecimoCuarto.class);
        lazyBeneficioCuartoInactivos.getFilterss().put("estado", true);
        lazyBeneficioCuartoInactivos.getFilterss().put("tipoRolBeneficio", tipoRolBeneficios);
        lazyBeneficioCuartoInactivos.getFilterss().put("activo", false);
        lazyBeneficioCuartoInactivos.getSorteds().put("acumulacionFondos.servidor.persona.apellido", "ASC");
        lazyBeneficioCuartoInactivos.setDistinct(Boolean.FALSE);
        actualizarValores();

        JsfUtil.addSuccessMessage("Información", "Datos del Período de Rol Beneficios Seleccionado Cargados.");
    }

    public void actualizarValores() {
        totalCobrar = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalAjuste = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalBase = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totaldescuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        List<BeneficiosDecimoCuarto> listCuarto;
        listCuarto = beneficioCuartoService.getBeneficiosCuartoXtipo(tipoRolBeneficios);
        if (!listCuarto.isEmpty()) {
            for (BeneficiosDecimoCuarto item : listCuarto) {
                if (item.getActivo()) {
                    if (item.getCobrar() != null) {
                        totalCobrar = totalCobrar.add(item.getCobrar().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                    if (item.getAjuste() != null) {
                        totalAjuste = totalAjuste.add(item.getAjuste().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                    if (item.getBaseImponible() != null) {
                        totalBase = totalBase.add(item.getBaseImponible().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                    if (item.getDescuento() != null) {
                        totaldescuento = totaldescuento.add(item.getDescuento().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                }
            }
        }

    }

    public BigDecimal mACobrar(BeneficiosDecimoCuarto bene) {
//        System.out.println("mCobrar");
        BigDecimal resultado = BigDecimal.ZERO;
        boolean verificar = false;

        if (bene.getBaseImponible() != null) {
            resultado = bene.getBaseImponible();
            if (bene.getAjuste() != null) {
                resultado = resultado.add(bene.getAjuste());
            }
            if (bene.getDescuento() != null) {
                if (bene.getDescuento().doubleValue() > resultado.doubleValue()) {
                    bene.setDescuento(null);
                    verificar = true;
                } else {
                    resultado = resultado.subtract(bene.getDescuento());
                }

            }
            if (verificar == true) {
                JsfUtil.addSuccessMessage("Información", "El valor de Descuento no puede ser mayor al valor de la base imponible + el ajuste .");
            } else {
                JsfUtil.addSuccessMessage("Información", "Valor a Cobrar de " + bene.getAcumulacionFondos().getServidor().getPersona().getNombreCompleto() + " Actualizado Con éxito.");
            }
            return resultado;
        }
        if (bene.getAjuste() != null) {
            resultado = bene.getAjuste();
            if (bene.getBaseImponible() != null) {
                resultado = resultado.add(bene.getBaseImponible());
            }
            if (bene.getDescuento() != null) {
                if (bene.getDescuento().doubleValue() > resultado.doubleValue()) {
                    bene.setDescuento(null);
                    verificar = true;
                } else {
                    resultado = resultado.subtract(bene.getDescuento());
                }
            }
            if (verificar == true) {
                JsfUtil.addSuccessMessage("Información", "El valor de Descuento no puede ser mayor al valor de la base imponible + el ajuste .");
            } else {
                JsfUtil.addSuccessMessage("Información", "Valor a Cobrar de " + bene.getAcumulacionFondos().getServidor().getPersona().getNombreCompleto() + " Actualizado Con éxito.");
            }
            return resultado;
        } else {
            return null;
        }
    }

    public void onCellEdit(BeneficiosDecimoCuarto beneficio) {
        beneficio.setCobrar(mACobrar(beneficio));
        beneficioCuartoService.edit(beneficio);
        actualizarValores();
        PrimeFaces.current().ajax().update("dtDecimoCuarto");
    }

    public void generarReporte(boolean c, int tipo) {
        if (tipoRolBeneficios.getId() == null) {
            return;
        }
        ss.addParametro("id", tipoRolBeneficios.getId());
        if (c) {
            ss.setContentType("xlsx");
        }
        if (tipo == 0) {
            ss.setNombreReporte("BeneficioDecimoCuarto");
        } else {
            Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
            ss.addParametro("nombre_resp", distributivo.getServidorPublico().getPersona().getNombreCompleto());
            ss.addParametro("cargo_resp", catalogoItemService.getEstadoRol(RolUsuario.titularTH).getTexto());
            ss.setNombreReporte("rolBeneficioDecimoCuarto");
        }
        ss.setNombreSubCarpeta("RolBeneficios");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }

    public void permitirEditar(boolean m) {
        if (m) {
            metodo = false;
            boton = true;

        } else {
            metodo = true;
            boton = false;

        }
        PrimeFaces.current().ajax().update("formMain");

    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public List<TipoRolBeneficios> getListTipoRolBeneficios() {
        return listTipoRolBeneficios;
    }

    public void setListTipoRolBeneficios(List<TipoRolBeneficios> listTipoRolBeneficios) {
        this.listTipoRolBeneficios = listTipoRolBeneficios;
    }

    public TipoRolBeneficios getTipoRolBeneficios() {
        return tipoRolBeneficios;
    }

    public void setTipoRolBeneficios(TipoRolBeneficios tipoRolBeneficios) {
        this.tipoRolBeneficios = tipoRolBeneficios;
    }

    public LazyModel<BeneficiosDecimoCuarto> getLazyBeneficioCuarto() {
        return lazyBeneficioCuarto;
    }

    public void setLazyBeneficioCuarto(LazyModel<BeneficiosDecimoCuarto> lazyBeneficioCuarto) {
        this.lazyBeneficioCuarto = lazyBeneficioCuarto;
    }

    public boolean isDesabilitar() {
        return desabilitar;
    }

    public void setDesabilitar(boolean desabilitar) {
        this.desabilitar = desabilitar;
    }

    public boolean isMetodo() {
        return metodo;
    }

    public void setMetodo(boolean metodo) {
        this.metodo = metodo;
    }

    public boolean isBoton() {
        return boton;
    }

    public void setBoton(boolean boton) {
        this.boton = boton;
    }
//</editor-fold>

    public BigDecimal getTotalCobrar() {
        return totalCobrar;
    }

    public void setTotalCobrar(BigDecimal totalCobrar) {
        this.totalCobrar = totalCobrar;
    }

    public BigDecimal getTotalAjuste() {
        return totalAjuste;
    }

    public void setTotalAjuste(BigDecimal totalAjuste) {
        this.totalAjuste = totalAjuste;
    }

    public BigDecimal getTotalBase() {
        return totalBase;
    }

    public void setTotalBase(BigDecimal totalBase) {
        this.totalBase = totalBase;
    }

    public BigDecimal getTotaldescuento() {
        return totaldescuento;
    }

    public void setTotaldescuento(BigDecimal totaldescuento) {
        this.totaldescuento = totaldescuento;
    }

    public LazyModel<BeneficiosDecimoCuarto> getLazyBeneficioCuartoInactivos() {
        return lazyBeneficioCuartoInactivos;
    }

    public void setLazyBeneficioCuartoInactivos(LazyModel<BeneficiosDecimoCuarto> lazyBeneficioCuartoInactivos) {
        this.lazyBeneficioCuartoInactivos = lazyBeneficioCuartoInactivos;
    }

}
