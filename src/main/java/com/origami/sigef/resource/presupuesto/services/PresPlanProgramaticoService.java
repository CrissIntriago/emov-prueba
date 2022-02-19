/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.services;

import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.models.QUERY;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class PresPlanProgramaticoService extends AbstractService<PresPlanProgramatico> {

    private static final Logger LOG = Logger.getLogger(PresPlanProgramaticoService.class.getName());

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PresPlanProgramaticoService() {
        super(PresPlanProgramatico.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getNextCode(PlanCuentas parametro, PresPlanProgramatico parametro_2) {
        if (parametro_2 != null) {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_CC_PADRE_PROGRAMATICO)
                    .setParameter(1, parametro)
                    .setParameter(2, parametro_2)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro);
        } else {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_CC_PROGRAMATICO)
                    .setParameter(1, parametro)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro);
        }
    }

    private String determinarCodigo(String codigo, PlanCuentas parametro) {
        System.out.println("CODIGO: " + codigo);
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
                int aux = Integer.parseInt(codigo) + 1;
                return String.valueOf(aux);
            }
        } else {
            return "1";
        }
    }

    public boolean findExiste(String codigo) {
        List<PresPlanProgramatico> result = (List<PresPlanProgramatico>) em.createQuery(QUERY.EXITS_PROGRAMATICO)
                .setParameter(1, codigo)
                .getResultList();
        return !result.isEmpty();
    }

    public Boolean poseeHijos(PresPlanProgramatico pp) {
        Boolean exit = Boolean.FALSE;
        try {
            List<PresPlanProgramatico> result = (List<PresPlanProgramatico>) em.createQuery("SELECT p FROM PresPlanProgramatico p where p.activo = TRUE AND p.estado = TRUE AND p.padre = ?1 ")
                    .setParameter(1, pp).getResultList();
            if (result.isEmpty()) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return Boolean.FALSE;
        }
    }

    public List<PresPlanProgramatico> getEstructuraProgramatica(String codigo) {
        List<PresPlanProgramatico> result = (List<PresPlanProgramatico>) em.createQuery(QUERY.ALL_ESTRUCTURA_PROGRAMATICA)
                .setParameter(1, codigo)
                .getResultList();
        return result;
    }
    
    public List<PresPlanProgramatico> getEstructurasSubProg () {
            List<PresPlanProgramatico> result = (List<PresPlanProgramatico>) em.createQuery("SELECT p FROM PresPlanProgramatico p WHERE p.activo = TRUE AND p.estado = TRUE AND p.confId.nivel = 3")
                .getResultList();
        return result;
    }
    
    public List<PresPlanProgramatico> getEstructuraProgramaticaAll() {
        List<PresPlanProgramatico> result = (List<PresPlanProgramatico>) em.createQuery(QUERY.ALL_ESTRUCTURA_PROGRAMATICA_ESTADO)
                .getResultList();
        return result;
    }

}
