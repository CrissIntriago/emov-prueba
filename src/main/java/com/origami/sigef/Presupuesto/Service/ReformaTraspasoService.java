/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReformaTraspasoService extends AbstractService<ReformaTraspaso> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReformaTraspasoService() {
        super(ReformaTraspaso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer maxNumeracion(boolean tipo, Short periodo) {
        Integer numeracion = 0;
        Integer num = (Integer) em.createNativeQuery("SELECT MAX(r.numeracion) FROM presupuesto.reforma_traspaso r WHERE r.estado = true and r.tipo = ?1 AND r.periodo = ?2")
                .setParameter(1, tipo)
                .setParameter(2, periodo)
                .getSingleResult();
        if (num == null) {
            numeracion = 1;
        } else {
            numeracion = num + 1;
        }

        return numeracion;
    }

    public List<Producto> getListProducto(Short periodo, Long unidad, String estado, boolean estado2) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND u.id = ?2 AND e.codigo = ?3 AND p.estado= ?4").setParameter(1, periodo).setParameter(2, unidad).setParameter(3, estado).setParameter(4, estado2).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProducto2(Short periodo, Long unidad, boolean estado2) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND u.id = ?2 AND p.estado= ?3").setParameter(1, periodo).setParameter(2, unidad).setParameter(3, estado2).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProducto3(Short periodo, Long unidad, CatalogoItem c, boolean estado) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND u.id = ?2 AND e.id = ?3 AND p.estado= ?4").setParameter(1, periodo).setParameter(2, unidad).setParameter(3, c.getId()).setParameter(4, estado).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReforma(Short periodo, Long unidad, String estado, boolean estado2, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND u.id = ?2 AND e.codigo = ?3 AND p.estado= ?4 AND p.codigoReformaTraspaso= ?5").setParameter(1, periodo).setParameter(2, unidad).setParameter(3, estado).setParameter(4, estado2).setParameter(5, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReforma2(Long unidad, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where u.id = ?1 AND p.codigoReformaTraspaso= ?2 ORDER BY p.codigoPresupuestario DESC").setParameter(1, unidad).setParameter(2, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReformaConsulta(Short periodo, boolean estado, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p where p.periodo = ?1 AND p.estado = ?2 AND p.codigoReformaTraspaso = ?3 AND (p.traspasoIncremento != 0 OR p.traspasoReduccion != 0)").setParameter(1, periodo).setParameter(2, estado).setParameter(3, idR).getResultList();
        System.out.println("Sizr listDetalleReformaTraspaso >> "+listDetalleReformaTraspaso.size());
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReformaConsulta2(Short periodo, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = (List<Producto>) em.createQuery("SELECT p FROM Producto p where p.periodo = :per AND p.estado= true AND p.codigoReformaTraspaso = :refor AND ( p.traspasoIncremento != 0 OR p.traspasoReduccion != 0 )")
                .setParameter("per", periodo).setParameter("refor", idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReformaDT2(Short periodo, String estado, boolean estado2, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND e.codigo = ?3 AND p.estado= ?4 AND p.codigoReformaTraspaso= ?5").setParameter(1, periodo).setParameter(3, estado).setParameter(4, estado2).setParameter(5, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<DetalleReformaTraspaso> getListReformaProducto(Short periodo, Long unidad) {
        List<DetalleReformaTraspaso> listDetalleReformaTraspaso = em.createQuery("SELECT dt FROM DetalleReformaTraspaso dt INNER JOIN dt.producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u "
                + "where p.periodo = ?1 AND u.id = ?2").setParameter(1, periodo).setParameter(2, unidad).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<ReformaTraspaso> getListReformaT1(Short periodo, CatalogoItem reforma, boolean tipo, CatalogoItem registrada, CatalogoItem observada, CatalogoItem observadaP, BigInteger tramite) {
        List<ReformaTraspaso> listReformaTraspaso = em.createQuery("SELECT p FROM ReformaTraspaso p WHERE p.periodo = ?1 AND p.estadoReforma = ?2  AND p.estadoReformaTramite IN ( ?3 ,?4, ?5) AND p.tipo = ?6 AND p.estado = true AND p.numTramite = ?7 order by p.numeracion ASC")
                .setParameter(1, periodo).setParameter(2, reforma).setParameter(3, registrada).setParameter(4, observada).setParameter(5, observadaP).setParameter(6, tipo).setParameter(7, tramite).getResultList();
        return listReformaTraspaso;
    }

    public List<ReformaTraspaso> getListReformaTramite(Short periodo, boolean tipo, BigInteger tramite) {
        List<ReformaTraspaso> listReformaTraspaso = em.createQuery("SELECT p FROM ReformaTraspaso p WHERE p.tipo = ?1 AND p.estado = true AND p.numTramite = ?2 order by p.numeracion ASC")
                .setParameter(1, tipo).setParameter(2, tramite).getResultList();
        return listReformaTraspaso;
    }

    public List<ReformaTraspaso> getListReforma(Short periodo, CatalogoItem reforma, boolean tipo, CatalogoItem registrada) {
        List<ReformaTraspaso> listReformaTraspaso = em.createQuery("SELECT p FROM ReformaTraspaso p WHERE p.periodo = ?1 AND p.estadoReforma = ?2  AND (p.estadoReformaTramite = ?3 OR p.estadoReformaTramite IS NULL) AND p.tipo = ?4 AND p.estado = true order by p.numeracion ASC")
                .setParameter(1, periodo).setParameter(2, reforma).setParameter(3, registrada).setParameter(4, tipo).getResultList();
        return listReformaTraspaso;
    }

    public UnidadAdministrativa getUnidadById(Long id) {
        UnidadAdministrativa unidad = (UnidadAdministrativa) em.createQuery("SELECT p FROM UnidadAdministrativa p WHERE p.id  = ?1 AND p.estado = true").setParameter(1, id).getSingleResult();
        return unidad;
    }

    public Boolean getProductoSinPartida(Producto productos) {
        Producto producto = null;
        try {
            producto = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.codigoPresupuestario IS NULL AND p.estado = true AND p.id = ?1").setParameter(1, productos.getId()).getSingleResult();
        } catch (NoResultException e) {
            producto = null;
        }
        if (producto == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean getProductoSinPartidaBol(Producto productos) {
        Producto producto = null;
        try {
            producto = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.numeroOrdenId IS NULL AND p.estado = true AND p.id = ?1").setParameter(1, productos.getId()).getSingleResult();
        } catch (NoResultException e) {
            producto = null;
        }
        if (producto == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<Producto> getListProductoByReformaT1SinPartida(Short periodo, Long unidad, String estado, boolean estado2, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND u.id = ?2 AND e.codigo = ?3 AND p.estado= ?4 AND p.codigoReformaTraspaso= ?5 AND p.numeroOrdenId IS NULL").setParameter(1, periodo).setParameter(2, unidad).setParameter(3, estado).setParameter(4, estado2).setParameter(5, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReformaT2SinPartida(Short periodo, String estado, boolean estado2, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND e.codigo = ?2 AND p.estado= ?3 AND p.codigoReformaTraspaso= ?4 AND p.numeroOrdenId IS NULL").setParameter(1, periodo).setParameter(2, estado).setParameter(3, estado2).setParameter(4, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<Producto> getListProductoByReformaT2(Short periodo, String estado, boolean estado2, BigInteger idR) {
        List<Producto> listDetalleReformaTraspaso = em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u INNER JOIN p.estadoPapp e "
                + "where p.periodo = ?1 AND e.codigo = ?2 AND p.estado= ?3 AND p.codigoReformaTraspaso= ?4").setParameter(1, periodo).setParameter(2, estado).setParameter(3, estado2).setParameter(4, idR).getResultList();
        return listDetalleReformaTraspaso;
    }

    public List<PlanLocalProgramaProyecto> getReformaPlanLocalProgramaProyecto(Short periodo, CatalogoItem c, boolean estado2, BigInteger reforma) {
        List<PlanLocalProgramaProyecto> result = (List<PlanLocalProgramaProyecto>) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p INNER JOIN p.estadoPapp ep where p.periodo= :periodo AND ep.id= :estado AND p.estado= :estado2 AND p.codigoReformaTraspaso = :reforma")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<PlanAnualPoliticaPublica> getReformaPlanPoliticaPublica(Short periodo, CatalogoItem c, boolean estado2, BigInteger reforma) {
        List<PlanAnualPoliticaPublica> result = (List<PlanAnualPoliticaPublica>) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p INNER JOIN p.estadoPapp ep where p.periodo= :periodo AND ep.id= :estado AND p.estado= :estado2 AND p.codigoReformaTraspaso = :reforma")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<PlanAnualProgramaProyecto> getReformaPlanAnulaProgramaProyecto(Short periodo, CatalogoItem c, boolean estado2, BigInteger reforma) {
        List<PlanAnualProgramaProyecto> result = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p INNER JOIN p.estadoPapp ep where p.periodo= :periodo AND ep.id= :estado AND p.estado= :estado2 AND p.codigoReformaTraspaso = :reforma")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<ActividadOperativa> getReformaActividadOperativa(Short periodo, CatalogoItem c, boolean estado2, Long unidad, BigInteger reforma) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a INNER JOIN a.estadoPapp ep INNER JOIN a.unidadResponsable u where a.periodo= :periodo AND ep.id= :estado AND a.estado= :estado2 AND a.codigoReformaTraspaso = :reforma AND u.id = :unidad")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).setParameter("unidad", unidad).setParameter("reforma", reforma).getResultList();
        return result;
    }

    public ReformaTraspaso getReformaByCodigo(String codigo) {
        ReformaTraspaso result = (ReformaTraspaso) em.createQuery("SELECT r FROM ReformaTraspaso r WHERE r.codigo = :codigo AND r.estado = true").setParameter("codigo", codigo).getSingleResult();
        return result;
    }

    public Producto getProductoById(Long id) {
        Producto pro = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id = :id and p.estado = true").setParameter("id", id).getSingleResult();
        return pro;
    }

    public List<ThCargo> getThCargoFinalReforma(Short periodo, BigInteger r) {
        List<ThCargo> result = (List<ThCargo>) em.createQuery("SELECT DISTINCT(tc) FROM ThCargoRubros p JOIN p.idCargo tc JOIN p.idRubro tr "
                + "WHERE p.periodo = :periodo AND p.estado=true AND tc.estado = true AND p.codigoReformaTraspaso = :reforma ")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<Distributivo> getDistributivoFinalReforma(Short periodo, BigInteger r) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d)  FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d WHERE p.periodo= :periodo AND vd.estado=TRUE AND p.codigoReformaTraspaso = :reforma ")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<Distributivo> getDistributivoFinalReforma2(Short periodo, BigInteger r) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d)  FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d WHERE p.periodo= :periodo AND vd.estado=TRUE AND p.codigoReformaTraspaso = :reforma AND p.monto != p.reformaCodificado AND (p.traspasoIncremento != 0 or p.traspasoReduccion != 0)")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidasReforma(Distributivo d, Short periodo, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id AND pd.periodo=:periodo AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReformaTraspaso = :reforma ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<PartidasDistributivo> verificarlistaPresupuestoSinPartidasReforma(Distributivo d, Short periodo, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id AND pd.periodo=:periodo AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReformaTraspaso = :reforma  AND pd.partidaAp IS NULL  ")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).setParameter("reforma", b).getResultList();
        List<PartidasDistributivo> inactivos = new ArrayList<>();
        if (result.isEmpty()) {
            inactivos = result;
        } else {
            for (PartidasDistributivo item : inactivos) {
                if (item.getDistributivo().getDistributivo().getEstadoActivo()) {
                    inactivos.add(item);
                }
            }
        }
        return inactivos;
    }

    public List<PartidasDistributivo> listaPDistributivoSinPartidasReforma(Short periodo, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE pd.periodo=:periodo AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReformaTraspaso = :reforma  AND pd.partidaAp IS NULL  ")
                .setParameter("periodo", periodo).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ProformaPresupuestoPlanificado> getListaVerificacion(BigInteger b) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.codigoReformaTraspaso = :reforma")
                .setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ProformaPresupuestoPlanificado> getPartidasDirectasReformas(short periodo, String tipo) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.estado= true AND p.periodo= :periodo AND p.codigo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo).getResultList();
        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidas(Distributivo d, Short periodo) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id AND pd.periodo=:periodo AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReforma IS NULL AND pd.codigoReformaTraspaso IS NULL ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).getResultList();

        return result;
    }

    public Producto getProductoByIdReformaCopia(Long id, CatalogoItem estado) {
        Producto pro = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id = :id and p.estado = true AND p.estadoPapp = :estado").setParameter("id", id).setParameter("estado", estado).getSingleResult();
        return pro;
    }

    public ActividadOperativa showActividadByTraspaso(BigInteger reforma, Long id) {
        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT p from ActividadOperativa p WHERE p.codigoReformaTraspaso= :reforma AND p.id = :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;

    }

    public ActividadOperativa showActividadOriginalByTraspaso(Long id) {
        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT p from ActividadOperativa p WHERE p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.id = :id ")
                .setParameter("id", id).getSingleResult();
        return result;

    }

    public ActividadOperativa showActividadCopiaByTraspaso(Long id) {
        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT p from ActividadOperativa p WHERE p.codigoReformaTraspaso IS NOT NULL AND p.codigoReforma IS NULL AND p.id = :id ")
                .setParameter("id", id).getSingleResult();
        return result;

    }

    public PlanAnualProgramaProyecto showPlanAnualProgramaByTraspaso(BigInteger reforma, Long id) {
        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p from PlanAnualProgramaProyecto p WHERE p.codigoReformaTraspaso= :reforma AND p.id = :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;

    }

    public PlanAnualProgramaProyecto showPlanAnualProgramaOriginalByTraspaso(Long id) {
        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p from PlanAnualProgramaProyecto p WHERE p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.id = :id ")
                .setParameter("id", id).getSingleResult();
        return result;

    }

    public List<PartidasDistributivo> listaPresupuestoPartidasTodasReforma(Short periodo, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE pd.periodo=:periodo AND d.estado=TRUE AND pd.codigoReformaTraspaso = :reforma ORDER BY pd.id ASC")
                .setParameter("periodo", periodo).setParameter("reforma", b).getResultList();

        return result;
    }

    public ReformaTraspaso verificarNumTramiteReforma(BigInteger b, Short periodo) {
        try {
            ReformaTraspaso result = (ReformaTraspaso) em.createQuery("SELECT r from ReformaTraspaso r WHERE r.estado = true AND r.numTramite = :tramite")
                    .setParameter("tramite", b)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String verificarEstadoNumTramiteReforma(Short periodo, BigInteger b) {
        String result = (String) em.createQuery("SELECT e.codigo from ReformaTraspaso r INNER JOIN r.estadoReformaTramite e WHERE r.periodo =:periodo AND r.estado = true AND r.numTramite = :tramite")
                .setParameter("periodo", periodo).setParameter("tramite", b).getResultStream().findFirst().orElse(null);

        return result;
    }

    public int actualizarValorActividad(ActividadOperativa a) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.montoReformada) FROM Producto p JOIN p.actividadOperativa a WHERE a.id = :id ")
                .setParameter("id", a.getId()).getSingleResult();
        valor = valor.setScale(2, RoundingMode.HALF_UP);
        Query query = em.createQuery("UPDATE ActividadOperativa a SET a.monotReformado = ?1 WHERE a.id= ?2")
                .setParameter(1, valor)
                .setParameter(2, a.getId());
        int result = query.executeUpdate();
        return result;
    }

    /*Consulta Presupuesto*/
    public Presupuesto valoresPresupuestoPapp(Short periodo, String partida) {
        try {
            Presupuesto result = (Presupuesto) em.createQuery("SELECT p from Presupuesto p Where p.partida = :partida AND p.tipo = false AND p.periodo = :periodo ")
                    .setParameter("periodo", periodo).setParameter("partida", partida).getSingleResult();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /*Consulta Proforma Presupuesto Planificado*/
    public List<ProformaPDTO> getPappGroupReforma(short periodo, ReformaTraspaso r) {

        BigInteger reforma = BigInteger.valueOf(r.getId());
        Query query = em.createNativeQuery("select p.codigo_presupuestario as partida, COALESCE(SUM(p.monto),0) as total,SUM(COALESCE(p.suplemento_egreso,0)) as suplemento,SUM(COALESCE(p.reduccion_egreso,0)) as reduccion, \n"
                + "               SUM(COALESCE(p.traspaso_incremento,0)) as incrementoT, SUM(COALESCE(p.traspaso_reduccion,0)) as reduccionT,\n"
                + "                p.estructura_programatica AS estructuraProgramatica ,p.item_presupuestario AS itemPresupuestario, \n"
                + "                p.fuente_directa AS fuente\n"
                + "from  producto p \n"
                + "where p.codigo_reforma_traspaso=?1\n"
                + "and p.codigo_presupuestario is not null\n"
                + "\n"
                + "group by  p.codigo_presupuestario,p.estructura_programatica,p.item_presupuestario,p.fuente_directa")
                .setParameter(1, reforma);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                PlanProgramatico p = new PlanProgramatico();
                CatalogoPresupuesto ca = new CatalogoPresupuesto();

                FuenteFinanciamiento fu = new FuenteFinanciamiento();
                CatalogoItem ci = new CatalogoItem();

                p = devuelveObjeto(periodo, (BigInteger) data[6]);
                ca = devuelveObjetoItem((BigInteger) data[7]);
                ci = devuelveObjetoFuenteDirecta((BigInteger) data[8]);

                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);
                ppdto.setReduccionTraspaso((BigDecimal) data[5]);
                ppdto.setReformaCodificado(ppdto.getTotal().add(ppdto.getIncrementoTraspaso()).subtract(ppdto.getReduccionTraspaso()));
                ppdto.setEstructuraProgramatica(p);
                ppdto.setItemPresupuestario(ca);
                ppdto.setFuenteDirecta(ci);
                list.add(ppdto);
            }
            return list;
        }

        return null;

    }

    public PlanProgramatico devuelveObjeto(Short periodo, BigInteger id) {
        PlanProgramatico plan = (PlanProgramatico) em.createQuery("SELECT p FROM PlanProgramatico p WHERE p.id=:id AND p.periodo=:periodo")
                .setParameter("id", id.longValue())
                .setParameter("periodo", periodo)
                .getResultStream().findFirst().orElse(null);
        return plan;
    }

    public CatalogoPresupuesto devuelveObjetoItem(BigInteger id) {
        CatalogoPresupuesto item = (CatalogoPresupuesto) em.createQuery("SELECT p FROM CatalogoPresupuesto p WHERE p.id=:id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return item;
    }

    public CatalogoItem devuelveObjetoFuenteDirecta(BigInteger id) {
        CatalogoItem fuenteDirecta = (CatalogoItem) em.createQuery("SELECT ae FROM CatalogoItem ae WHERE ae.id= :id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return fuenteDirecta;
    }

    public List<ProformaPDTO> showCodigosAgrupadosReformas(Short periodo, ReformaTraspaso r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        Query nativeQuery = em.createNativeQuery("select  pa.partida_ap,COALESCE(SUM(pa.monto),0) as total,sum(COALESCE(pa.reforma_suplemento,0)) as suplemento, sum(COALESCE(pa.reforma_reduccion,0)) as reduccion,"
                + "sum(COALESCE(pa.traspaso_incremento,0)) as incrementoT, sum(COALESCE(pa.traspaso_reduccion,0)) as reduccionT,"
                + "pa.estructura_ap,pa.item_ap,pa.fuente_directa\n"
                + "from partidas_distributivo pa inner join talento_humano.valores_distributivo va\n"
                + "on va.id=pa.distributivo\n"
                + "where pa.periodo=?1 AND pa.partida_ap is not null  AND pa.codigo_reforma_traspaso=?2\n"
                + "GROUP BY pa.partida_ap,pa.estructura_ap,pa.item_ap,pa.fuente_directa");
        nativeQuery.setParameter(1, periodo).setParameter(2, reforma);
        List<Object[]> result = nativeQuery.getResultList();

        if (Utils.isNotEmpty(result)) {

            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {

                PlanProgramatico p = new PlanProgramatico();
                CatalogoPresupuesto ca = new CatalogoPresupuesto();

                FuenteFinanciamiento fu = new FuenteFinanciamiento();
                CatalogoItem ci = new CatalogoItem();

                ProformaPDTO ppdto = new ProformaPDTO();
                p = devuelveObjeto(periodo, (BigInteger) data[6]);
                ca = devuelveObjetoItem((BigInteger) data[7]);
                ci = devuelveObjetoFuenteDirecta((BigInteger) data[8]);

                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);
                ppdto.setReduccionTraspaso((BigDecimal) data[5]);
                ppdto.setEstructuraProgramatica(p);
                ppdto.setItemPresupuestario(ca);
                ppdto.setFuenteDirecta(ci);
                list.add(ppdto);

            }
            return list;
        }
        return null;
    }

    public List<ProformaPresupuestoPlanificado> showPartidaDirectasReforma(short periodo, ReformaTraspaso r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND  p.codigoReformaTraspaso= :reforma AND p.codigo='PDI' AND p.partidaPresupuestaria IS NOT NULL")
                .setParameter("periodo", periodo).setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> proformaVieja(short periodo) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo=:periodo AND p.codigoReforma  IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).getResultList();
        return result;

    }

    public ProformaPresupuestoPlanificado editProformavieja(ProformaPresupuestoPlanificado p) {
        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.id= :id")
                .setParameter("id", p.getId()).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PlanLocalProgramaProyecto> showPlanLocalNuevo(BigInteger b) {
        List<PlanLocalProgramaProyecto> result = (List<PlanLocalProgramaProyecto>) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p WHERE p.codigoReformaTraspaso= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanLocalProgramaProyecto getPlanlocal(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanLocalProgramaProyecto result = (PlanLocalProgramaProyecto) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PlanAnualPoliticaPublica> showPlanPoliticalNuevo(BigInteger b) {
        List<PlanAnualPoliticaPublica> result = (List<PlanAnualPoliticaPublica>) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.codigoReformaTraspaso= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanAnualPoliticaPublica getPlanPolitica(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanAnualPoliticaPublica result = (PlanAnualPoliticaPublica) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PlanAnualProgramaProyecto> showPlanAnualNuevo(BigInteger b) {
        List<PlanAnualProgramaProyecto> result = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.codigoReformaTraspaso= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanAnualProgramaProyecto getPlanAnual(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ActividadOperativa> showActividadOperativaNuevo(BigInteger b) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.codigoReformaTraspaso= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ActividadOperativa getActividadOperativa(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public PlanLocalProgramaProyecto getPlanLocalCreado(BigInteger nuevo) {
        PlanLocalProgramaProyecto p = (PlanLocalProgramaProyecto) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p where p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;
    }

    public PlanAnualPoliticaPublica getPlanPoliticaCreado(BigInteger nuevo) {
        PlanAnualPoliticaPublica p = (PlanAnualPoliticaPublica) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;
    }

    public PlanAnualProgramaProyecto getPlanAnualCreado(BigInteger nuevo) {
        PlanAnualProgramaProyecto p = (PlanAnualProgramaProyecto) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;

    }

    public ActividadOperativa getActividadCreado(BigInteger nuevo) {
        ActividadOperativa p = (ActividadOperativa) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.registroNuevoReferencia= :nuevo AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);

        return p;
    }

    /*DISTRIBUTIVO*/
    public List<PartidasDistributivo> showPartidasDistributivoNnuevo(BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.codigoReformaTraspaso= :reforma AND p.partidaAp IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PartidasDistributivo getPartidasDistributivoNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = b.longValue();

        PartidasDistributivo result = (PartidasDistributivo) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PartidasDistributivoAnexo> showPartidasDistributivoAnexoNnuevo(BigInteger b) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReformaTraspaso= :reforma AND p.partidaAp IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PartidasDistributivoAnexo getPartidasDistributivoAnexoNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PartidasDistributivoAnexo result = (PartidasDistributivoAnexo) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ProformaPresupuestoPlanificado> showPartidasDirectasNnuevo(BigInteger b) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReformaTraspaso= :reforma AND p.partidaPresupuestaria IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ProformaPresupuestoPlanificado getPartidasDirectasNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public String getClienteAlcalde() {
        Query query = em.createNativeQuery("SELECT c.nombre||' '||c.apellido AS nombre FROM cliente c\n"
                + "INNER JOIN talento_humano.servidor s ON s.persona = c.id\n"
                + "INNER JOIN auth.usuarios u ON u.funcionario = s.id\n"
                + "INNER JOIN auth.usuario_rol ur ON ur.usuario = u.id\n"
                + "INNER JOIN auth.rol r ON r.id = ur.rol\n"
                + "INNER JOIN catalogo_item ci ON ci.id = r.categoria\n"
                + "WHERE ci.texto = 'MAXIMA AUTORIDAD'");
        String result = query.getSingleResult().toString();
        return result;
    }

    public String obtieneConceptoTramite(ReformaTraspaso r) {
        try {
            Query query = em.createNativeQuery("SELECT h.concepto FROM procesos.historico_tramites h WHERE h.num_tramite = :tramite").setParameter("tramite", new Long(r.getNumTramite().toString()));
            String result = query.getSingleResult().toString();
            return result;
        } catch (Exception e) {
            return "";
        }

    }

    public String obtieneValorInforme(ReformaTraspaso r) {
        Query query = em.createNativeQuery("SELECT r.valor_formulario_pdi FROM presupuesto.reforma_traspaso r WHERE r.id = :tramite").setParameter("tramite", r.getId());
        if (query.getSingleResult() == null) {
            return "";
        }
        String result = query.getSingleResult().toString();
        return result;
    }

    public List<ReformaTraspaso> obtReformasByUsuario(String usuario) {
        try {
            List<ReformaTraspaso> reformas = em.createQuery("SELECT r FROM ReformaTraspaso r WHERE r.usuarioCreacion = :usuario").setParameter("usuario", usuario).getResultList();
            return reformas;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public BigInteger obtMaxNumeracionByUsuario(String usuario) {
        try {
            BigInteger numeracion = (BigInteger) em.createQuery("SELECT MAX(r.numeracion) FROM ReformaTraspaso r WHERE r.usuarioCreacion = :usuario").setParameter("usuario", usuario).getSingleResult();
            return numeracion;
        } catch (Exception e) {
            return BigInteger.ZERO;
        }
    }

    public BigInteger obtieneMaxNumeracionByUsuario(String usuario) {
        try {
            Integer numeracion = (Integer) em.createQuery("SELECT MAX(r.numeracion) FROM ReformaTraspaso r WHERE r.usuarioCreacion = :usuario").setParameter("usuario", usuario).getSingleResult();
            return BigInteger.valueOf(numeracion).add(BigInteger.ONE);
        } catch (Exception e) {
            return BigInteger.ZERO;
        }
    }

    /*Informe de Pertinencia de Reformas*/
    public List<Producto> getProductoModificadas(BigInteger idR) {
        try {
            List<Producto> reformas = em.createNativeQuery("select pr.* from (\n"
                    + "select p.id, p.monto, p.monto_reformada, p.numero_orden_id, (select pp.monto_reformada from producto pp where pp.id = p.numero_orden_id) as monto_original, (select pp.traspaso_incremento from producto pp where pp.id = p.numero_orden_id) as traspaso_incr,\n"
                    + "	(select pp.traspaso_reduccion from producto pp where pp.id = p.numero_orden_id) as traspaso_reduc, (select pp.monto from producto pp where pp.id = p.numero_orden_id) as monot_mo from producto p where p.codigo_reforma_traspaso = ?1 \n"
                    + "	) x inner join producto pr on pr.id = x.id where x.monto != x.monto_original;").setParameter(1, idR).getResultList();
            return reformas;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
