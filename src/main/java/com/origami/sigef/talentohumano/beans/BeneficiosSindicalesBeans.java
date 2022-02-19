/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiosSindicales;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.talentohumano.services.BeneficiosSindicalesService;
import com.origami.sigef.talentohumano.services.TipoRolBeneficiosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "beneficiosSindicalesView")
@ViewScoped
public class BeneficiosSindicalesBeans implements Serializable {

    private LazyModel<BeneficiosSindicales> beneficioLazy;
    @Inject
    private BeneficiosSindicalesService sindicalesService;
    private BeneficiosSindicales beneficioSindical;
    private List<BeneficiosSindicales> listBeneficios;

    @Inject
    private TipoRolBeneficiosService tipoRolBenefService;
    private List<TipoRolBeneficios> listTipoRolBeneficios;
    private TipoRolBeneficios tipoRolBeneficios;

    private List<Servidor> listServidor;

    @Inject
    private PartidaDistributivoService partidaService;
    private List<PartidasDistributivo> listPartida;

    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;

    boolean desabilitar;

    @Inject
    private ServletSession ss;

    private BigDecimal totalValorB;

    @PostConstruct
    public void init() {
        tipoRolBeneficios = new TipoRolBeneficios();
        listTipoRolBeneficios = new ArrayList<>();
        listTipoRolBeneficios = tipoRolBenefService.findByTipoRolByTipo("ROL_TIPO_BEN_SIN");
        listServidor = new ArrayList<>();
        desabilitar = true;
        totalValorB = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);

    }

    public void buscar() {
        if (tipoRolBeneficios == null) {
            JsfUtil.addWarningMessage("Información", " Ingrese un Período de Rol Beneficios.");
            desabilitar = true;
            beneficioLazy = null;
            return;
        } else {
            desabilitar = false;
        }
        beneficioLazy = new LazyModel<>(BeneficiosSindicales.class);
        beneficioLazy.getFilterss().put("estado", true);
        beneficioLazy.getFilterss().put("tipoRolBeneficios", tipoRolBeneficios);
        actualizarValor();
        if (beneficioLazy == null) {
            JsfUtil.addWarningMessage("Información", " No existen Datos en el Período de Rol Beneficio.");
        } else {
            JsfUtil.addSuccessMessage("Información", " Datos del Período de Rol Beneficios Cargados.");
        }
        PrimeFaces.current().ajax().update(":dtSindicales");

    }

    public void actualizarValor() {
        totalValorB = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        List<BeneficiosSindicales> listSin;
        listSin = sindicalesService.findByBeneficiosXTipo(tipoRolBeneficios);
        if (!listSin.isEmpty()) {
            for (BeneficiosSindicales item : listSin) {
                if (item.getValorBeneficio() != null) {
                    totalValorB = totalValorB.add(item.getValorBeneficio().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
            }
        }
    }

    public void openDlg() {
        boolean preguntaSiesAprobado = false;
        System.out.println(tipoRolBeneficios);
        if (tipoRolBeneficios == null) {
            JsfUtil.addWarningMessage("Información", " Ingrese un Período de Rol Beneficios.");
            return;
        }
        listPartida = new ArrayList<>();
        listPartida = partidaService.getServidoresXRubroPartida(tipoRolBeneficios.getPeriodo(), "BS");
        listServidor = new ArrayList<>();
        for (PartidasDistributivo item : listPartida) {
            boolean verificar = false;
            verificar = proformaService.ExistPartidaInProforma(item.getPeriodo(), "PD", item.getPartidaAp());
            if (verificar == true) {
                listServidor.add(item.getDistributivo().getDistributivo().getServidorPublico());
            }
        }
        preguntaSiesAprobado = proformaService.BloquearSiEsAprobado(tipoRolBeneficios.getPeriodo(), false, true);
        if (preguntaSiesAprobado == false) {
            listServidor = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('dlgServidor').show()");
        PrimeFaces.current().ajax().update(":dtServidor");
    }

    public void GuardarRegistro(Servidor ser) {
        boolean validate = false;
        listBeneficios = new ArrayList<>();
        listBeneficios = sindicalesService.findByBeneficiosXTipo(tipoRolBeneficios);
        if (!listBeneficios.isEmpty()) {
            for (BeneficiosSindicales item : listBeneficios) {
                validate = false;
                if (Objects.equals(item.getServidorP(), ser)) {
                    validate = true;
                    break;
                }
            }
        }
        if (validate == true) {
            JsfUtil.addWarningMessage("Advertencia", "Servidor ya se encuentra registrado para este período de Rol.");
            return;
        }

        beneficioSindical = new BeneficiosSindicales();
        beneficioSindical.setEstado(Boolean.TRUE);
        beneficioSindical.setServidorP(ser);
        beneficioSindical.setTipoRolBeneficios(tipoRolBeneficios);
        beneficioSindical = sindicalesService.create(beneficioSindical);
        actualizarValor();
        PrimeFaces.current().executeScript("PF('dlgServidor').hide()");
        PrimeFaces.current().ajax().update(":dtSindicales");
        JsfUtil.addSuccessMessage("Información", " Se agrego con exito el Servidor.");
    }

    public void Eliminar(BeneficiosSindicales ben) {
        ben.setEstado(Boolean.FALSE);
        sindicalesService.edit(ben);
        actualizarValor();
        JsfUtil.addSuccessMessage("Información", " Se Elimino con exito el Servidor.");

    }

    public void onCellEdit(BeneficiosSindicales ben) {
        sindicalesService.edit(ben);
        actualizarValor();
        JsfUtil.addSuccessMessage("Información", " Datos del Servidor " + ben.getServidorP().getPersona().getNombreCompleto() + " Actualizados con éxito");
    }

    public void editRlBeneficio(TipoRolBeneficios rol) {
        if (rol.getId() == null) {
            JsfUtil.addWarningMessage("Información", " Debe seleccionar tipo Rol Beneficio");
            return;
        }
        tipoRolBenefService.edit(rol);
        JsfUtil.addSuccessMessage("Información", " Descripción del tipo Rol Beneficio agredada con éxito");
    }

    public void generarReporte(boolean c) {
        if (tipoRolBeneficios.getId() == null) {
            return;
        }
        ss.addParametro("id", tipoRolBeneficios.getId());
        if (c) {
            ss.setContentType("xlsx");
        }
        ss.setNombreReporte("BeneficiosSindicales");
        ss.setNombreSubCarpeta("RolBeneficios");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public LazyModel<BeneficiosSindicales> getBeneficioLazy() {
        return beneficioLazy;
    }

    public void setBeneficioLazy(LazyModel<BeneficiosSindicales> beneficioLazy) {
        this.beneficioLazy = beneficioLazy;
    }

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

    public List<Servidor> getListServidor() {
        return listServidor;
    }

    public void setListServidor(List<Servidor> listServidor) {
        this.listServidor = listServidor;
    }

    public boolean isDesabilitar() {
        return desabilitar;
    }

    public void setDesabilitar(boolean desabilitar) {
        this.desabilitar = desabilitar;
    }

    public BigDecimal getTotalValorB() {
        return totalValorB;
    }

    public void setTotalValorB(BigDecimal totalValorB) {
        this.totalValorB = totalValorB;
    }

}
