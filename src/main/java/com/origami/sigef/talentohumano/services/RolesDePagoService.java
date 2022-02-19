/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.junit.Test;

/**
 *
 * @author Luis Pozo G
 */
@Stateless
@javax.enterprise.context.Dependent
public class RolesDePagoService extends AbstractService<RolesDePago> {
    
    @Inject
    private UserSession userSession;
    @Inject
    private PartidaDistributivoService partidaService;
    @Inject
    private ValoresRolesService valorRolesService;
    @Inject
    private CuentaContableService cuentaService;
    
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public RolesDePagoService() {
        super(RolesDePago.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }
    
    public List<RolesDePago> findRolesXPeriodo(short periodo) {
        try {
            Query query = getEntityManager().createQuery("SELECT DISTINCT r from RolesDePago r WHERE r.estado = true and r.periodo=?1 ")
                    .setParameter(1, periodo);
            List<RolesDePago> listser = (List<RolesDePago>) query.getResultList();
            return listser;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public RolesDePago rolPagoXservidor(Short anio, String ci) {
        try {
            Query query = em.createQuery("SELECT DISTINCT r FROM RolesDePago r INNER JOIN r.servidor s WHERE r.servidor = s AND s.estado = TRUE AND r.estado = TRUE AND r.periodo = ?1 AND s.persona.identificacion = ?2")
                    .setParameter(1, anio).setParameter(2, ci);
            RolesDePago result = (RolesDePago) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<RolesDePago> rubrosAsignado(String cod, RolesDePago asignacion, TipoRol rol) {
        try {
            Query query = em.createNativeQuery("select * from talento_humano.roles_de_pago rp\n"
                    + "left join talento_humano.valores_roles vr ON vr.rol_pago = rp.id\n"
                    + "INNER JOIN conf.parametros_talento_humano ph ON ph.id = vr.valor_parametrizable\n"
                    + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                    + "where rp.estado = true and vr.estado = true AND ci.codigo = ?1\n"
                    + "AND rp.id = ?2 AND rp.periodo = ?3", RolesDePago.class)
                    .setParameter(1, cod).setParameter(2, asignacion.getId()).setParameter(3, rol.getAnio());
            List<RolesDePago> result = (List<RolesDePago>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<RolesDePago> getServidorInDiaLaboradoxTipoRol(TipoRol t) {
        try {
            List<RolesDePago> result;
            Query query = em.createQuery("SELECT r FROM RolesDePago r WHERE r.periodo=?2 AND r.servidor in (select d.servidor FROM DiasLaborado d WHERE d.estado = TRUE AND d.tipoRol = ?1) ORDER BY r.servidor.persona.apellido")
                    .setParameter(1, t).setParameter(2, t.getAnio());
            return result = query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
        
    }
    
    @Transactional
    @Test
    public void batchsave(List<Servidor> auxListSer, Short anio, RolesDePago auxRol) {
        int i = 0, size = 25;
        for (Servidor s : auxListSer) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            RolesDePago rolesDePago = new RolesDePago();
            rolesDePago.setServidor(s);
            rolesDePago.setEstado(Boolean.TRUE);
            rolesDePago.setFechaCreacion(new Date());
            rolesDePago.setPeriodo(anio);
            rolesDePago.setFechaModificacion(new Date());
            rolesDePago.setUsuarioCreacion(userSession.getName());
            rolesDePago.setUsuarioModificacion(userSession.getName());
            em.persist(rolesDePago);
            setValoresRolesPartida(rolesDePago, anio);
            i++;
        }
    }
    
    @Transactional
    @Test
    public void setValoresRolesPartida(RolesDePago rol, Short anio) {
        RolesDePago auxRol = rol;
        Servidor servidorDialog = auxRol.getServidor();
        List<PartidasDistributivo> partidaDistributivo = partidaService.showPartidaxDistributivoAndPeriodo(servidorDialog.getDistributivo(), anio);
        if (partidaDistributivo != null) {
            guardarValores(partidaDistributivo, anio, auxRol);
        }
        
        List<PartidasDistributivo> lisNewPartidas = partidaService.showAllPartidaXDistributiv(servidorDialog.getDistributivo(), anio);
        List<ValoresRoles> listValoresIngresos = valorRolesService.FindValoresIngresoRolesXRol(auxRol);
        List<ValoresRoles> listValoresEliminar = new ArrayList<>();
        for (ValoresRoles vr : listValoresIngresos) {
            boolean comprobar = false;
            for (PartidasDistributivo pd : lisNewPartidas) {
                if (vr.getValorParametrizable().getTipo().getCodigo().equals(pd.getDistributivo().getValoresParametrizados().getTipo().getCodigo())) {
                    comprobar = true;
                    break;
                }
            }
            if (comprobar == false) {
                listValoresEliminar.add(vr);
            }
        }
        if (!listValoresEliminar.isEmpty()) {
            for (ValoresRoles ValorEliminar : listValoresEliminar) {
                ValorEliminar.setEstado(Boolean.FALSE);
                valorRolesService.edit(ValorEliminar);
            }
        }
        List<ValoresRoles> listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        if (!listaValoresRoles.isEmpty()) {
            addctacontable(listaValoresRoles);
            
        }
    }
    
    @Transactional
    @Test
    public void guardarValores(List<PartidasDistributivo> partidaDistributivo, Short anio, RolesDePago auxRol) {
        int i = 0, size = 25;
        for (PartidasDistributivo item : partidaDistributivo) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            ValoresRoles valoresRol = new ValoresRoles();
            valoresRol.setPeriodo(anio);
            valoresRol.setEstado(Boolean.TRUE);
            valoresRol.setValorParametrizable(item.getDistributivo().getValoresParametrizados());
            valoresRol.setEstructuraApR(item.getEstructuraAp());
            valoresRol.setFuenteApR(item.getFuenteAp());
            valoresRol.setFuenteDirectaR(item.getFuenteDirecta());
            valoresRol.setItemApR(item.getItemAp());
            valoresRol.setPartidaAp(item.getPartidaAp());
            valoresRol.setRolPago(auxRol);
            valoresRol.setCuentaContable(getCuenta(valoresRol.getItemApR()));
            valoresRol = valorRolesService.create(valoresRol);
            em.persist(valoresRol);
            i++;
        }
        
    }
    
    public CuentaContable getCuenta(CatalogoPresupuesto c) {
        CuentaContable cuenta;
        cuenta = cuentaService.getCuentaXItem(c);
        return cuenta;
    }
    
    @Transactional
    @Test
    public void addctacontable(List<ValoresRoles> listaValoresRoles) {
        for (ValoresRoles item : listaValoresRoles) {
            if (item.getCuentaContable() == null && item.getItemApR() != null) {
                item.setCuentaContable(getCuenta(item.getItemApR()));
                valorRolesService.edit(item);
            }
        }
    }
        
}
