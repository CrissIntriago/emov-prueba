/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@Stateless
@javax.enterprise.context.Dependent
public class AnticipoRemuneracionService extends AbstractService<AnticipoRemuneracion> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AnticipoRemuneracionService() {
        super(AnticipoRemuneracion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public BigDecimal sueldoServidor(short periodo, Cliente cliente) {
        BigDecimal sueldo;
        Query query = getEntityManager().createNativeQuery("SELECT de.remuneracion_dolares from talento_humano.servidor s "
                + "INNER JOIN 	public.cliente c ON c.id = s.persona "
                + "INNER JOIN talento_humano.distributivo d ON s.distributivo = d.id "
                + "INNER JOIN talento_humano.distributivo_escala de ON de.distributivo = d.id "
                + "where de.anio = ?1 AND c.identificacion =?2 AND s.estado = true")
                .setParameter(1, periodo)
                .setParameter(2, cliente.getIdentificacion());
        sueldo = (BigDecimal) query.getSingleResult();
        return sueldo;
    }

    public Distributivo getDistributivo(short anio, Servidor servidor) {
        try {
            Query query = getEntityManager().createQuery("SELECT de.distributivo FROM DistributivoEscala de INNER JOIN de.distributivo d WHERE de.distributivo = d AND de.anio = ?1 and d.servidorPublico = ?2")
                    .setParameter(1, anio)
                    .setParameter(2, servidor);
            Distributivo dist = (Distributivo) query.getSingleResult();
            return dist;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CatalogoItem getEstadoAnticipo(String codigo, short orden) {
        Query query = getEntityManager().createQuery("SELECT i FROM CatalogoItem i WHERE i.codigo = ?1 AND i.orden = ?2")
                .setParameter(1, codigo)
                .setParameter(2, orden);
        CatalogoItem catalogo = (CatalogoItem) query.getSingleResult();
        return catalogo;
    }

    public List<AnticipoRemuneracion> getLista(short anio) {
        List<AnticipoRemuneracion> result = (List<AnticipoRemuneracion>) getEntityManager().createQuery("SELECT a FROM AnticipoRemuneracion a WHERE a.estado = true AND a.periodo = ?1")
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public List<AnticipoRemuneracion> anticipoRubro(Servidor ser, String cod, TipoRol rol) {
        List<AnticipoRemuneracion> result = (List<AnticipoRemuneracion>) em.createNativeQuery("select a.* from talento_humano.anticipo_remuneracion a\n"
                + "INNER JOIN conf.parametros_talento_humano ph on ph.id = a.valor_parametrizado\n"
                + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                + "where a.estado = TRUE AND a.servidor = ?1 and ci.codigo = ?2\n"
                + "and a.id in (\n"
                + "SELECT ca.anticipo_remuneracion from talento_humano.cuota_anticipo ca\n"
                + "where ca.mes = ?3 and ca.estado_cuota = false AND a.estado = true)", AnticipoRemuneracion.class)
                .setParameter(1, ser.getId()).setParameter(2, cod).setParameter(3, rol.getMes().getDescripcion())
                .getResultList();
        return result;
    }

    public AnticipoRemuneracion getServidorAnticipoRMU(Servidor serv, CatalogoItem estadoDeuda, TipoRol rolMes) {
        try {
            Query query = em.createQuery("SELECT a FROM AnticipoRemuneracion a WHERE a.periodo=?4  AND a.estado = true AND a.servidor = ?1 AND a.estadoAnticipo = ?2 AND a.id IN (SELECT c.anticipoRemuneracion FROM CuotaAnticipo c WHERE c.mes = ?3 AND c.estadoCuota = FALSE )")
                    .setParameter(1, serv).setParameter(2, estadoDeuda).setParameter(3, rolMes.getMes().getDescripcion()).setParameter(4, rolMes.getAnio());
            AnticipoRemuneracion result = (AnticipoRemuneracion) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public AnticipoRemuneracion findAnticipoByNTramite(BigInteger tramite) {
        try {
            AnticipoRemuneracion resultado;
            Query query = em.createQuery(" SELECT a FROM AnticipoRemuneracion a WHERE a.numTramite = ?1 AND a.estado = true AND a.periodo= ?2")
                    .setParameter(1, tramite)
                    .setParameter(2, Utils.getAnio(new Date()).shortValue());
            return resultado = (AnticipoRemuneracion) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public int getUpdateAnticipo(ComprobantePago comprobante) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.anticipo_remuneracion SET comprobante=FALSE WHERE comprobante_pago=?1")
                .setParameter(1, comprobante.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }
}
