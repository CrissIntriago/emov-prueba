/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContCuentasService extends AbstractService<ContCuentas> {

    private static final Logger LOG = Logger.getLogger(ContCuentasService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContCuentasService() {
        super(ContCuentas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getNextCode(PlanCuentas parametro, ContCuentas parametro_2) {
        if (parametro_2 != null) {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_CC_PADRE)
                    .setParameter(1, parametro)
                    .setParameter(2, parametro_2)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro, parametro_2);
        } else {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_CC)
                    .setParameter(1, parametro)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro, parametro_2);
        }
    }

    private String determinarCodigo(String codigo, PlanCuentas parametro, ContCuentas parametro_2) {
        if (codigo != null) {
            if (parametro.getSeparador()) {//Si el codigo tiene separadores
                String code = "";
                if (codigo.contains(parametro.getCaracter())) {
                    String separador = parametro.getCaracter();
                    if (separador.equals(".")) {
                        separador = "\\" + separador;
                    }
                    String[] split = codigo.split(separador);
                    int pos = (split.length - 1);
                    String ultimo = split[pos];
                    int aux = Integer.parseInt(ultimo) + 1;
                    String concatenar;
                    if (parametro.getNumDigito() > 1) {
                        concatenar = Utils.completarCadenaConCeros(String.valueOf(aux), parametro.getNumDigito());
                    } else {
                        concatenar = String.valueOf(aux);
                    }
                    split[pos] = concatenar;
                    for (int x = 0; x < split.length; x++) {
                        code = code.concat(split[x]).concat(parametro.getCaracter());
                    }
                }
                return code;
            } else {//Si el codigo no tiene separadores
                String codePadre = "";
                if (parametro_2 != null) {
                    codePadre = parametro_2.getCodigo();
                }
                String codeNew = codigo.substring(codePadre.length(), codigo.length());
                if (parametro_2 != null) {
                    int aux1 = Integer.parseInt(codeNew) + 1;
                    return codePadre.concat(Utils.completarCadenaConCeros(String.valueOf(aux1), parametro.getNumDigito()));
                } else {
                    int aux2 = Integer.parseInt(codigo) + 1;
                    return codePadre.concat(Utils.completarCadenaConCeros(String.valueOf(aux2), parametro.getNumDigito()));
                }
            }
        } else {
            return "1";
        }
    }

    public List<ContCuentas> findMovimientos(boolean activo, boolean movimiento) {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.FIND_CUENTA_ACTIVO_MOVIMIENTO)
                .setParameter(1, activo)
                .setParameter(2, movimiento)
                .getResultList();
        return result;
    }

    public boolean findExiste(String codigo) {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.EXITS_CUENTA_CONTABLE)
                .setParameter(1, codigo)
                .getResultList();
        return !result.isEmpty();
    }

    public List<ContCuentas> filterCuentas(String busqueda) {
        try {
            List<ContCuentas> result = (List<ContCuentas>) em.createQuery("SELECT c FROM ContCuentas c where c.activo = TRUE AND c.movimiento = TRUE AND c.ctaPagarCobrar = FALSE AND UPPER(CONCAT(c.codigo,' ',c.descripcion)) LIKE UPPER(?1)")
                    .setParameter(1, busqueda).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<ContCuentas> filterCuentasCtaCobro(String busqueda) {
        try {
            List<ContCuentas> result = (List<ContCuentas>) em.createQuery("SELECT c FROM ContCuentas c where c.activo = TRUE AND c.movimiento = TRUE AND c.ctaPagarCobrar = TRUE AND UPPER(CONCAT(c.codigo,' ',c.descripcion)) LIKE UPPER(?1)")
                    .setParameter(1, busqueda).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean findHijos(ContCuentas contCuentas) {
        List<ContCuentas> result = (List<ContCuentas>) em.createQuery(QUERY.FIND_HIJOS_CUENTA)
                .setParameter(1, contCuentas)
                .getResultList();
        return !result.isEmpty();
    }

    public List<ContCuentas> findCtaContableByTipo(ContCuentas cta) {
        try {
            List<ContCuentas> result = (List<ContCuentas>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.padre = ?1 ")
                    .setParameter(1, cta).getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ContCuentas> findCtaContableByCodigo(String cuenta) {
        try {
            List<ContCuentas> result = (List<ContCuentas>) em.createQuery("SELECT c FROM ContCuentas c WHERE c.codigo = ?1 ")
                    .setParameter(1, cuenta).getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas findContCuentasByCodigo(String cuenta) {
        try {
            ContCuentas result = (ContCuentas) em.createQuery("SELECT c FROM ContCuentas c WHERE c.codigo = ?1 ")
                    .setParameter(1, cuenta).getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas findContCuentasByCodigoAndEstadoIsTrue(String cuenta) {
        try {
            ContCuentas result = (ContCuentas) em.createQuery("SELECT c FROM ContCuentas c WHERE c.codigo = ?1 and c.estado = TRUE ")
                    .setParameter(1, cuenta).getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas findCodigo(String codCuentaInsert) {
        try {
            ContCuentas result = (ContCuentas) em.createQuery("SELECT ct FROM ContCuentas ct WHERE ct.codigo = ?1 AND ct.activo = true AND ct.estado= true")
                    .setParameter(1, codCuentaInsert)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ContCuentas> findAllByEstadoIsTRUE() {
        try {
            List<ContCuentas> result = (List<ContCuentas>) em.createQuery("SELECT c FROM ContCuentas c WHERE c.estado= true ")
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ContCuentas> findAllAgrupacionAndGobierno(Boolean gobierno) {
        try {
            return em.createQuery("SELECT c FROM ContCuentas c WHERE c.gobierno = ?1 AND c.estado = true ORDER BY c.codigo ASC")
                    .setParameter(1, gobierno)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ContCuentas> getHijosByPadre(ContCuentas padre) {
        try {
            return em.createQuery("SELECT c FROM ContCuentas c WHERE c.padre.id = ?1 AND c.estado = TRUE").setParameter(1, padre.getId()).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
