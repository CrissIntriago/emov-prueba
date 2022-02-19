/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.BeneficiosDecimoCuarto;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.JsfUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.junit.Test;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BeneficioDecimoCuartoService extends AbstractService<BeneficiosDecimoCuarto> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private DiasLaboradoService diaService;
    @Inject
    private ValoresRolesService valorRolService;
    @Inject
    private RolRubroService rolRubroService;

    public BeneficioDecimoCuartoService() {
        super(BeneficiosDecimoCuarto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BeneficiosDecimoCuarto> getBeneficiosCuartoXtipo(TipoRolBeneficios tipo) {
        try {
            Query query = em.createQuery("SELECT b FROM BeneficiosDecimoCuarto b where b.estado = true AND b.tipoRolBeneficio = ?1")
                    .setParameter(1, tipo);
            List<BeneficiosDecimoCuarto> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    @Test
    public void guardar(List<AcumulacionFondoReserva> listAcumulacion, TipoRolBeneficios tipoRolBeneficios) {
        int i = 0;
        int BATCH_SIZE = 25;
        for (AcumulacionFondoReserva item : listAcumulacion) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
            BeneficiosDecimoCuarto beneficioCuarto = new BeneficiosDecimoCuarto();
            beneficioCuarto.setTipoRolBeneficio(tipoRolBeneficios);
            beneficioCuarto.setAcumulacionFondos(item);
            beneficioCuarto.setEstado(Boolean.TRUE);
            beneficioCuarto.setMeses(diaService.diasLaboradoAnioServidor(tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta(), item.getServidor()).shortValue());
            beneficioCuarto.setBaseImponible(mBaseImponible(beneficioCuarto));
            beneficioCuarto.setCobradoRol(rolRubroService.getValorRubroTipoVeneficio(beneficioCuarto) != null ? rolRubroService.getValorRubroTipoVeneficio(beneficioCuarto) : BigDecimal.ZERO);
            beneficioCuarto.setCobrar(mACobrar(beneficioCuarto));
            beneficioCuarto.setValorRol(valorRolService.FindValoresRolesXServidorXperiodo(item.getServidor(), item.getPeriodo()));
            if (beneficioCuarto.getAcumulacionFondos() != null) {
                if (beneficioCuarto.getAcumulacionFondos().getServidor() != null) {
                    beneficioCuarto.setActivo(beneficioCuarto.getAcumulacionFondos().getServidor().getEstado());
                }
            }
            beneficioCuarto = this.create(beneficioCuarto);
            em.persist(beneficioCuarto);
            i++;
        }
    }

    @Transactional
    @Test
    public void update(List<BeneficiosDecimoCuarto> listBeneficios, TipoRolBeneficios tipoRolBeneficios) {
        for (BeneficiosDecimoCuarto item : listBeneficios) {
            item.setMeses(diaService.diasLaboradoAnioServidor(tipoRolBeneficios.getPeriodoDesde(), tipoRolBeneficios.getPeriodoHasta(), item.getAcumulacionFondos().getServidor()).shortValue());
            item.setBaseImponible(mBaseImponible(item));
            item.setCobrar(mACobrar(item));
            item.setValorRol(valorRolService.FindValoresRolesXServidorXperiodo(item.getAcumulacionFondos().getServidor(), item.getAcumulacionFondos().getPeriodo()));
            this.edit(item);
        }
    }

    public BigDecimal mBaseImponible(BeneficiosDecimoCuarto ben) {
        BigDecimal resul = BigDecimal.ZERO;
        for (FondosReserva fr : ben.getAcumulacionFondos().getListFondosReserva()) {
            resul = resul.add(fr.getValorFondos());
        }
        return resul.setScale(2, BigDecimal.ROUND_HALF_EVEN);
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

}
