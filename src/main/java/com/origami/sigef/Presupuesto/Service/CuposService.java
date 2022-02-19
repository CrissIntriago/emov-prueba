/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMIEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CuposService extends AbstractService<Cupos> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuposService() {
        super(Cupos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Cupos> getVerificacionCupos(ReformaIngresoSuplemento r) {
        List<Cupos> result = (List<Cupos>) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r WHERE r.id= :id AND c.unidadAdministrativa  IS NOT NULL").setParameter("id", r.getId()).getResultList();
        return result;
    }

    public BigDecimal getSumaVerificacion(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(c.montoCupo) FROM Cupos c INNER JOIN c.reforma r WHERE  r.id= :id").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public Cupos getCodigoCupo(ReformaIngresoSuplemento r, String codigo) {
        Cupos result = (Cupos) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r WHERE  r.id= :id AND c.otros= :codigo").setParameter("id", r.getId()).setParameter("codigo", codigo).getResultStream().findFirst().orElse(null);
        return result;
    }

    public Cupos getUnidadesCupoTotal(ReformaIngresoSuplemento r, UnidadAdministrativa u) {
        Cupos result = (Cupos) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r INNER JOIN c.unidadAdministrativa u WHERE r.id= :id AND u.id= :unidad")
                .setParameter("id", r.getId()).setParameter("unidad", u.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getValorActualSuplemento(Producto p) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT p.suplementoEgreso FROM Producto p where p.id= :id").setParameter("id", p.getId()).getSingleResult();

        return result;
    }

    public BigDecimal getValorActualReduccion(Producto p) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT p.reduccionEgreso FROM Producto p where  p.id= :id").setParameter("id", p.getId()).getSingleResult();

        return result;
    }

    public BigDecimal verficiarCupoSuplemento(BigInteger r, UnidadAdministrativa u) {
//        BigDecimal sumaTotalPadres = BigDecimal.ZERO;
//        UnidadAdministrativa unidad = new UnidadAdministrativa();
//        List<UnidadAdministrativa> lista = new ArrayList<>();

//        unidad = u;
//        List<UnidadAdministrativa> unidadList = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE").getResultList();
//        for (UnidadAdministrativa data : unidadList) {
//
//            if (data.equals(unidad)) {
//                if (unidad.getPadre() != null) {
//                    UnidadAdministrativa unidadLista = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE AND u.id= :id ")
//                            .setParameter("id", data.getPadre().getId()).getSingleResult();
//                    if (unidad != null) {
//                        lista.add(unidadLista);
//                        unidad = unidadLista;
//                    }
//                }
//
//            }
//
//        }
//
//        lista.add(u);
//
//        for (UnidadAdministrativa data : lista) {
        BigDecimal resultado = (BigDecimal) em.createQuery("SELECT SUM(p.suplementoEgreso) FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u WHERE p.codigoReforma= :reforma AND a.codigoReforma= :reformaActividad AND u.id= :unidad ")
                .setParameter("reforma", r).setParameter("reformaActividad", r).setParameter("unidad", u.getId()).getSingleResult();

//            if (resultado != null) {
//                sumaTotalPadres = sumaTotalPadres.add(resultado);
//            }
//        }
        return resultado;
//        return sumaTotalPadres;
    }

    public int actualizarValorActividad(ActividadOperativa a) {

        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.montoReformada) FROM Producto p INNER JOIN p.actividadOperativa a WHERE a.id= :id ").setParameter("id", a.getId()).getSingleResult();

        Query query = em.createQuery("UPDATE ActividadOperativa p SET p.monotReformado= :monto WHERE p.id= :id")
                .setParameter("monto", valor)
                .setParameter("id", a.getId());

        int result = query.executeUpdate();
        return result;
    }

    public int actualizarValorActividadReduccion(ActividadOperativa a) {

        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.montoReformada) FROM Producto p INNER JOIN p.actividadOperativa a WHERE a.id= :id ").setParameter("id", a.getId()).getSingleResult();

        Query query = em.createQuery("UPDATE ActividadOperativa p SET p.monotReformado= :monto WHERE p.id= :id")
                .setParameter("monto", valor)
                .setParameter("id", a.getId());

        int result = query.executeUpdate();
        return result;
    }

    public BigDecimal recaudadoSuplemento(BigInteger r, UnidadAdministrativa u) {

        BigDecimal resultado = (BigDecimal) em.createQuery("SELECT SUM(p.suplementoEgreso) FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u WHERE  p.codigoReforma= :reforma AND a.codigoReforma= :reformaActividad AND u.id= :unidad ")
                .setParameter("reforma", r).setParameter("reformaActividad", r).setParameter("unidad", u.getId()).getSingleResult();
        return resultado;
    }

    public Producto productoVerificando(Producto p) {

        Producto resultado = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id= :id ")
                .setParameter("id", p.getId()).getSingleResult();
        return resultado;
    }

    public BigDecimal getCuposPadres(ReformaIngresoSuplemento r, UnidadAdministrativa u) {
        BigDecimal sumaTotalPadres = BigDecimal.ZERO;
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        List<UnidadAdministrativa> lista = new ArrayList<>();
        List<Cupos> listacupos = new ArrayList<>();
        unidad = u;
        List<UnidadAdministrativa> unidadList = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE").getResultList();
        for (UnidadAdministrativa data : unidadList) {

            if (data.equals(unidad)) {
                if (unidad.getPadre() != null) {
                    UnidadAdministrativa unidadLista = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE AND u.id= :id ")
                            .setParameter("id", data.getPadre().getId()).getSingleResult();
                    if (unidad != null) {
                        lista.add(unidadLista);
                        unidad = unidadLista;
                    }
                }
            }
        }
        lista.add(u);
        for (UnidadAdministrativa item : lista) {

            Cupos c = new Cupos();
            c = (Cupos) em.createQuery("SELECT c FROM Cupos c WHERE c.unidadAdministrativa= :unidad AND c.reforma= :reforma").setParameter("unidad", item).setParameter("reforma", r).getSingleResult();
            sumaTotalPadres = sumaTotalPadres.add(c.getMontoCupo());
        }
        return sumaTotalPadres;
    }

    public BigDecimal cupoUnidadValor(UnidadAdministrativa u, ReformaIngresoSuplemento r) {

        BigDecimal result = BigDecimal.ZERO;
        Cupos c = (Cupos) em.createQuery("SELECT c FROM Cupos c WHERE c.unidadAdministrativa= :unidad AND c.reforma= :reforma").setParameter("unidad", u).setParameter("reforma", r).getSingleResult();

        if (c != null) {
            result = c.getMontoCupo();
        }
        return result;
    }

    public BigDecimal cuposPapp(ReformaIngresoSuplemento r) {
        List<Cupos> cu = (List<Cupos>) em.createQuery("SELECT c FROM Cupos c WHERE c.reforma=:reforma AND c.unidadAdministrativa IS NOT NULL").setParameter("reforma", r).getResultList();
        BigDecimal result = BigDecimal.ZERO;
        if (cu.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(c.montoCupo) FROM Cupos c WHERE c.reforma=:reforma AND c.unidadAdministrativa IS NOT NULL").setParameter("reforma", r).getSingleResult();
        }
        return result;
    }

    public BigDecimal getCupoActual(Cupos c) {

        BigDecimal valor = BigDecimal.ZERO;
        Cupos result = (Cupos) em.createQuery("SELECT c FROM Cupos c WHERE  c.id= :id ").setParameter("id", c.getId()).getResultStream().findFirst().orElse(null);

        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getMontoCupo();
        }
        return valor;
    }

    public List<Cupos> getVerificadorCupos(ReformaIngresoSuplemento r) {
        List<Cupos> result = (List<Cupos>) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r WHERE r.id= :id AND c.unidadAdministrativa IS NOT NULL").setParameter("id", r.getId()).getResultList();
        return result;
    }

    public List<Cupos> getCuposDetalle(ReformaIngresoSuplemento r) {
        List<Cupos> result = (List<Cupos>) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r WHERE r.id= :id  ORDER BY r.id asc").setParameter("id", r.getId()).getResultList();
        return result;
    }

    public List<UnidadAdministrativa> getCuposDetalleUnidades(ReformaIngresoSuplemento r) {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT c.unidadAdministrativa FROM Cupos c INNER JOIN c.reforma r WHERE r.id= :id AND c.unidadAdministrativa IS NOT NULL AND c.responsable IS NOT NULL AND c.usuario IS NOT NULL ").setParameter("id", r.getId()).getResultList();
        return result;
    }

    public ReformaIngresoSuplemento getReformaTramite(BigInteger num) {
        ReformaIngresoSuplemento result = (ReformaIngresoSuplemento) em.createQuery("SELECT r FROM ReformaIngresoSuplemento r where r.numTramite= :num").setParameter("num", num).getSingleResult();
        return result;
    }

    public List<Cupos> getRevisionAsignacion(ReformaIngresoSuplemento r) {
        List<Cupos> lista = em.createNativeQuery("select cupo.* from presupuesto.cupos cupo\n"
                + "                where cupo.reforma=?1 and cupo.usuario=\n"
                + "                (select a.usuario from \n"
                + "                (select c.usuario,count(c.*) as conteo \n"
                + "					from presupuesto.cupos c\n"
                + "                where c.responsable is not null and c.usuario is not null\n"
                + "                 and c.reforma=?2\n"
                + "                group by c.usuario having count(c.*)>1)  as a)", Cupos.class).setParameter(1, r).setParameter(2, r).getResultList();

        return lista;
    }

}
