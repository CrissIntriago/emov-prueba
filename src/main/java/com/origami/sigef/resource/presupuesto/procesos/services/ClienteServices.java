/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ClienteServices extends AbstractService<Cliente> {

    private static final Logger LOG = Logger.getLogger(ClienteServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ClienteServices() {
        super(Cliente.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public UnidadAdministrativa getUnidadPrincipalUSer(String data) {
        BigInteger d = getunidadRolUser(data);

        UnidadAdministrativa result = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.id= :id").setParameter("id", d.longValue()).getSingleResult();
        return result;

    }

    public BigInteger getunidadRolUser(String userString) {

        BigInteger user = (BigInteger) em.createNativeQuery("select un.id from auth.usuarios u inner join\n"
                + "                                              auth.usuario_rol ur ON ur.usuario = u.id\n"
                + "                                              inner join auth.rol r ON r.id = ur.rol\n"
                + "                                               inner join public.unidad_administrativa un on\n"
                + "                                              r.unidad_administrativa=un.id\n"
                + "                							   inner join catalogo_item ci\n"
                + "                							   on r.categoria=ci.id\n"
                + "                							   where u.estado = true AND \n"
                + "                                                u.usuario=?1").setParameter(1, userString).getResultStream().findFirst().orElse(null);

        return user;

    }

    public String getrolsUser(String rol) {
        try {
            String user = (String) em.createNativeQuery("select u.usuario from auth.usuarios u inner join\n"
                    + "auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join catalogo_item ci  on r.categoria=ci.id\n"
                    + "where u.estado = true AND u.estado=true and ci.codigo= ?1 AND r.nombre <> 'admin'")
                    .setParameter(1, rol).getResultStream().findFirst().orElse(null);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public List<Cliente> findByServidoresDepartamento(Long unidadAdministrativa) {
        List<Cliente> clientes = new ArrayList<>();
        try {
            clientes = (List<Cliente>) em.createQuery("select a.idServidor.persona from ThServidorCargo a where a.idCargo.idUnidad.id=?1").
                    setParameter(1, unidadAdministrativa).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clientes;
    }
}
