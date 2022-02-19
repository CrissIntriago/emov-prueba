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
import com.origami.sigef.common.entities.BeneficiosDecimoTercero;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.BeneficioDecimoTerceroService;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.TipoRolBeneficiosService;
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
 * @author ORIGAMI1
 */
@Named(value = "beneficioDecimoTercerView")
@ViewScoped
public class BeneficioDecimoTercerBeans implements Serializable {

    private BeneficiosDecimoTercero beneficioDecimoTercero;
    @Inject
    private BeneficioDecimoTerceroService beneficioTerceroService;
    private LazyModel<BeneficiosDecimoTercero> lazyBeneficioTercer;
    @Inject
    private TipoRolBeneficiosService tipoRolBenefService;
    private List<TipoRolBeneficios> listTipoRolBeneficios;
    private TipoRolBeneficios tipoRolBeneficios;

    @Inject
    private AcumulacionFondoReservaService acumulacionFondosService;

    @Inject
    private CatalogoItemService catalogoItemService;
    private CatalogoItem decimoTercerItem;

    @Inject
    private DiasLaboradoService diaService;

    @Inject
    private DistributivoEscalaService disEscalaService;
    private DistributivoEscala disEscala;
    @Inject
    private ClienteService clienteService;

    @Inject
    private ValoresRolesService valorRolService;
    private ValoresRoles valorR;

    boolean desabilitar;
    boolean metodo;
    boolean boton;

    @Inject
    private ServletSession ss;

    private BigDecimal totalCobrar;
    private BigDecimal totalAjuste;
    private BigDecimal totalGanado;
    private BigDecimal totalDecimo;
    private BigDecimal totaldescuento;

    @PostConstruct
    public void init() {
        tipoRolBeneficios = new TipoRolBeneficios();
        decimoTercerItem = catalogoItemService.getEstadoRol("ACU-DECIMO-3ro");
        listTipoRolBeneficios = new ArrayList<>();
        listTipoRolBeneficios = tipoRolBenefService.findByTipoRolByTipo("ROL_TIPO_DEC_TERCERO");
        beneficioDecimoTercero = new BeneficiosDecimoTercero();
        desabilitar = true;
        metodo = false;
        boton = true;
        totalCobrar = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalAjuste = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalDecimo = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totaldescuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalGanado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public void buscar() {
        if (tipoRolBeneficios == null) {
            JsfUtil.addWarningMessage("Información", " Ingrese un Período de Rol Beneficios.");
            lazyBeneficioTercer = null;
            desabilitar = true;
            boton = true;
            return;
        } else {
            desabilitar = false;
        }
        List<AcumulacionFondoReserva> acumulacionFondosList;
        acumulacionFondosList = new ArrayList<>();
        acumulacionFondosList = acumulacionFondosService.AcumulacionFondosReservInNotBeneficioTercer(true, decimoTercerItem, tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta());
        if (!acumulacionFondosList.isEmpty() || acumulacionFondosList != null) {
            guardar(acumulacionFondosList);
        }
        List<BeneficiosDecimoTercero> listBeneficios;
        listBeneficios = beneficioTerceroService.getBeneficiosTerceroXtipo(tipoRolBeneficios);
        if (!listBeneficios.isEmpty()) {
            if (tipoRolBeneficios.getEstadoAprobacionBen().getCodigo().equals("registrado - rol")) {
                update(listBeneficios);
            }
        }
        lazyBeneficioTercer = new LazyModel<>(BeneficiosDecimoTercero.class);
        lazyBeneficioTercer.getFilterss().put("estado", true);
        lazyBeneficioTercer.getFilterss().put("tipoRolBeneficio", tipoRolBeneficios);
        lazyBeneficioTercer.getSorteds().put("acumulacionFondoReserva.servidor.persona.apellido", "ASC");
        lazyBeneficioTercer.setDistinct(false);
        mostrarTotales();
        JsfUtil.addSuccessMessage("Información", "Datos del Período de Rol Beneficios Seleccionado Cargados.");

    }

    public void mostrarTotales() {
        totalCobrar = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalAjuste = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalDecimo = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totaldescuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        totalGanado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        List<BeneficiosDecimoTercero> listTercero;
        listTercero = beneficioTerceroService.getBeneficiosTerceroXtipo(tipoRolBeneficios);
        if (!listTercero.isEmpty()) {
            for (BeneficiosDecimoTercero item : listTercero) {
                if (item.getCobrar() != null) {
                    totalCobrar = totalCobrar.add(item.getCobrar().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                if (item.getAjuste() != null) {
                    totalAjuste = totalAjuste.add(item.getAjuste().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                if (item.getDecimoTercerGanado() != null) {
                    totalDecimo = totalDecimo.add(item.getDecimoTercerGanado().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                if (item.getDescuento() != null) {
                    totaldescuento = totaldescuento.add(item.getDescuento().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                if (item.getDecimoTercerGanado() != null) {
                    totalGanado = totalGanado.add(item.getTotalGanado().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
            }
        }
    }

    public void guardar(List<AcumulacionFondoReserva> listAcumulacion) {
        for (AcumulacionFondoReserva item : listAcumulacion) {
            beneficioDecimoTercero.setTipoRolBeneficio(tipoRolBeneficios);
            beneficioDecimoTercero.setAcumulacionFondoReserva(item);
            beneficioDecimoTercero.setEstado(Boolean.TRUE);
            beneficioDecimoTercero.setMeses(mCalcularMeses(item));
            beneficioDecimoTercero.setTotalGanado(mTotalGanado(item));
            beneficioDecimoTercero.setDecimoTercerGanado(mDecimoTercerGanado(beneficioDecimoTercero));
            beneficioDecimoTercero.setCobrar(mACobrar(beneficioDecimoTercero));
            beneficioDecimoTercero.setValorRol(obtenerValorRubro(beneficioDecimoTercero));
            beneficioDecimoTercero = beneficioTerceroService.create(beneficioDecimoTercero);
            beneficioDecimoTercero = new BeneficiosDecimoTercero();
        }
    }

    public void update(List<BeneficiosDecimoTercero> listBeneficios) {
        for (BeneficiosDecimoTercero item : listBeneficios) {
            item.setMeses(mCalcularMeses(item.getAcumulacionFondoReserva()));
            item.setTotalGanado(mTotalGanado(item.getAcumulacionFondoReserva()));
            item.setDecimoTercerGanado(mDecimoTercerGanado(item));
            item.setCobrar(mACobrar(item));
            item.setValorRol(obtenerValorRubro(item));
            beneficioTerceroService.edit(item);
        }
    }

    public ValoresRoles obtenerValorRubro(BeneficiosDecimoTercero b) {
        valorR = new ValoresRoles();
        valorR = valorRolService.FindValoresRolesXServidorXperiodo(b.getAcumulacionFondoReserva().getServidor(), b.getAcumulacionFondoReserva().getPeriodo());
        return valorR;
    }

    public BigDecimal mACobrar(BeneficiosDecimoTercero bene) {
        BigDecimal resultado = BigDecimal.ZERO;
        boolean verificar = false;

        if (bene.getDecimoTercerGanado() != null) {
            resultado = bene.getDecimoTercerGanado();
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
                JsfUtil.addSuccessMessage("Información", "Valor a Cobrar de " + bene.getAcumulacionFondoReserva().getServidor().getPersona().getNombreCompleto() + " Actualizado Con éxito.");
            }
            return resultado;
        }
        if (bene.getAjuste() != null) {
            resultado = bene.getAjuste();
            if (bene.getDecimoTercerGanado() != null) {
                resultado = resultado.add(bene.getDecimoTercerGanado());
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
                JsfUtil.addSuccessMessage("Información", "Valor a Cobrar de " + bene.getAcumulacionFondoReserva().getServidor().getPersona().getNombreCompleto() + " Actualizado Con éxito.");
            }
            return resultado;
        } else {
            return null;
        }
    }

    public BigDecimal mDecimoTercerGanado(BeneficiosDecimoTercero ben) {
        BigDecimal resultado = BigDecimal.ZERO;
        double cash = 0;
        if (ben.getTotalGanado() == null || ben.getMeses() == null || ben.getMeses() <= 0) {
            return null;
        } else {
            cash = ben.getTotalGanado().doubleValue() / 12;
            resultado = BigDecimal.valueOf(cash).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            return resultado;
        }
    }

    public Short mCalcularMeses(AcumulacionFondoReserva acum) {
        int mes = 0;
        List<DiasLaborado> listaDias;
        listaDias = diaService.mesesLaboradoAnioServidor(tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta(), acum.getServidor());
        if (!listaDias.isEmpty()) {
            for (DiasLaborado itemDia : listaDias) {
                mes = mes + 1;
            }
        }
        return (short) (mes);
    }

    public BigDecimal mTotalGanado(AcumulacionFondoReserva ac) {
        BigDecimal result = BigDecimal.ZERO;
        double gananciaDiaria;
        double gananciaMensual;
        disEscala = disEscalaService.getRMU(ac.getServidor().getDistributivo(), ac.getPeriodo());
        List<DiasLaborado> listaDias;
        listaDias = diaService.mesesLaboradoAnioServidor(tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta(), ac.getServidor());
        if (!listaDias.isEmpty()) {
            for (DiasLaborado itemDia : listaDias) {
                gananciaDiaria = 0;
                gananciaMensual = 0;
                gananciaDiaria = disEscala.getRemuneracionDolares().doubleValue() / TalentoHumano.diasCalendarioLaboral;
                gananciaMensual = gananciaDiaria * itemDia.getDias();
                result = result.add(BigDecimal.valueOf(gananciaMensual).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            }
        }
        return result;
    }

    public void onCellEdit(BeneficiosDecimoTercero beneficio) {
        beneficio.setCobrar(mACobrar(beneficio));
        beneficioTerceroService.edit(beneficio);
        mostrarTotales();
        PrimeFaces.current().ajax().update("dtDecimoTercero");
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
            ss.setNombreReporte("BeneficioDecimoTercero");
        } else {
            Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
            ss.addParametro("nombre_resp", distributivo.getServidorPublico().getPersona().getNombreCompleto());
            ss.addParametro("cargo_resp", catalogoItemService.getEstadoRol(RolUsuario.titularTH).getTexto());
            ss.setNombreReporte("rolBeneficioDecimoTercero");
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

    //<editor-fold defaultstate="collapsed" desc="get and set">
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

    public LazyModel<BeneficiosDecimoTercero> getLazyBeneficioTercer() {
        return lazyBeneficioTercer;
    }

    public void setLazyBeneficioTercer(LazyModel<BeneficiosDecimoTercero> lazyBeneficioTercer) {
        this.lazyBeneficioTercer = lazyBeneficioTercer;
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

    public BigDecimal getTotalGanado() {
        return totalGanado;
    }

    public void setTotalGanado(BigDecimal totalGanado) {
        this.totalGanado = totalGanado;
    }

    public BigDecimal getTotalDecimo() {
        return totalDecimo;
    }

    public void setTotalDecimo(BigDecimal totalDecimo) {
        this.totalDecimo = totalDecimo;
    }

    public BigDecimal getTotaldescuento() {
        return totaldescuento;
    }

    public void setTotaldescuento(BigDecimal totaldescuento) {
        this.totaldescuento = totaldescuento;
    }
//</editor-fold>

}
