package com.origami.sigef.talentohumano.services;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless
public class ValoresDistributivoService extends AbstractService<ValoresDistributivo> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ValoresDistributivoService() {
        super(ValoresDistributivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //trae valores de distributivo de un disatributivo y un periodo
    public List<ValoresDistributivo> findvaloresDistributivo(Distributivo d, OpcionBusqueda opcion) {
        try {
            if (d.getId() == null) {
                return null;
            }
            Query query = em.createQuery("SELECT v from ValoresDistributivo v WHERE v.distributivo=?1 AND v.estado = true AND v.anio = ?2")
                    .setParameter(1, d).setParameter(2, opcion.getAnio());
            List<ValoresDistributivo> valores = (List<ValoresDistributivo>) query.getResultList();
            return valores;
        } catch (NoResultException e) {
            return null;
        }
    }

    //trae valores de distributivo de un disatributivo y un periodo
    public List<ValoresDistributivo> findvaloresDistributivoXperiodo(Distributivo d, short anio) {
        try {
            Query query = em.createQuery("SELECT v from ValoresDistributivo v WHERE v.distributivo=?1 AND v.estado = true AND v.anio = ?2")
                    .setParameter(1, d).setParameter(2, anio);
            List<ValoresDistributivo> valores = (List<ValoresDistributivo>) query.getResultList();
            return valores;
        } catch (NoResultException e) {
            return null;
        }
    }

    //trae todos los valores de distributivo
    public List<ValoresDistributivo> findAllValoresDistributivo(Distributivo d) {
        try {
            Query query = em.createQuery("SELECT v from ValoresDistributivo v WHERE v.distributivo=?1 AND v.estado = true")
                    .setParameter(1, d);
            List<ValoresDistributivo> valores = (List<ValoresDistributivo>) query.getResultList();
            return valores;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param periodo
     * @return retornar una lista de los dstributivo que no estan en la entidad
     * o tabla partidas_distributivo
     */
    public List<BigInteger> castearDistributivo(Short periodo) {

        List<BigInteger> listaDistributivos = (List<BigInteger>) em.createNativeQuery("select v.id\n"
                + "from talento_humano.distributivo d INNER JOIN talento_humano.valores_distributivo v\n"
                + "on v.distributivo=d.id INNER JOIN talento_humano.distributivo_escala de\n"
                + "on de.distributivo=d.id where de.anio=?1 and v.estado=true  and EXTRACT(year from d.vigencia_desde)<=?2 and v.anio=?3\n"
                + "and v.id not in (SELECT pd.distributivo FROM public.partidas_distributivo pd where  pd.periodo=?4 and pd.codigo_reforma is null \n"
                + "								 and pd.codigo_reforma_traspaso is null ) and\n"
                + "COALESCE(CAST(d.vigencia_hasta AS Date),now()) >= CAST(now() AS Date) order by v.id")
                .setParameter(1, periodo).setParameter(2, periodo).setParameter(3, periodo).setParameter(4, periodo).getResultList();

        return listaDistributivos;
    }

    public List<ValoresDistributivo> getObjetoDistributivo(Short periodo) {
        List<ValoresDistributivo> result = new ArrayList<>();
        List<BigInteger> listaid = castearDistributivo(periodo);
        ValoresDistributivo valores = new ValoresDistributivo();

        if (!listaid.isEmpty() || listaid != null) {
            for (BigInteger item : listaid) {

                valores = (ValoresDistributivo) em.createQuery("SELECT v from ValoresDistributivo v where v.id= :id AND v.estado=true").setParameter("id", item.longValue()).getSingleResult();
                result.add(valores);

                valores = new ValoresDistributivo();
            }
        } else {
            result = null;
        }

        return result;
    }

    public List<Distributivo> getDistributivoFinal(Short periodo) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d) FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d "
                + "WHERE p.periodo=:periodo AND vd.estado = TRUE AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo)
                .getResultList();
        return result;
    }

    public List<Distributivo> getDistributivoFinalReforma(Short periodo, BigInteger r) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d)  FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d WHERE p.periodo= :periodo AND vd.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaAp IS NOT NULL")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargo> getDistributivoReforma(Short periodo, BigInteger r) {
        List<ThCargo> result = (List<ThCargo>) em.createQuery("SELECT DISTINCT(vd)  FROM ThCargoRubros p INNER JOIN p.idCargo vd  WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL AND p.idRubro.origen=TRUE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargoRubros> getDistributivoAnexoReforma(Short periodo, BigInteger r) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p  FROM ThCargoRubros p  WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL AND p.idRubro.origen=FALSE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<Distributivo> getDistributivoFinalReformaModificacion(Short periodo, BigInteger r) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d)  FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d WHERE p.periodo= :periodo AND vd.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaAp IS NOT NULL")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargo> getDistributivoReformaModificacion(Short periodo, BigInteger r) {
        List<ThCargo> result = (List<ThCargo>) em.createQuery("SELECT DISTINCT(p.idCargo)  FROM ThCargoRubros p  WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL AND p.idRubro.origen=TRUE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargoRubros> getDistributivoReformaModificacionAnexo(Short periodo, BigInteger r) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p  WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL AND p.idRubro.origen=FALSE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<Distributivo> getDistributivoFinalReformaNulos(Short periodo, BigInteger r) {
        List<Distributivo> result = (List<Distributivo>) em.createQuery("SELECT DISTINCT(d)  FROM PartidasDistributivo p INNER JOIN p.distributivo vd INNER JOIN vd.distributivo d WHERE p.periodo= :periodo AND vd.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaAp IS NULL")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargo> getDistributivoReformaNulos(Short periodo, BigInteger r) {
        List<ThCargo> result = (List<ThCargo>) em.createQuery("SELECT DISTINCT(p.idCargo) FROM ThCargoRubros p WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NULL AND p.idRubro.origen=TRUE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<ThCargoRubros> getDistributivoReformaNulosAnexo(Short periodo, BigInteger r) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.periodo= :periodo AND p.estado=TRUE AND p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NULL AND p.idRubro.origen=FALSE")
                .setParameter("periodo", periodo).setParameter("reforma", r).getResultList();

        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidas(Distributivo d, Short periodo) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id AND pd.periodo=:periodo AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReforma IS NULL AND pd.codigoReformaTraspaso IS NULL ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).getResultList();

        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidasReforma(Distributivo d, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id  AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReforma=:reforma AND pd.partidaAp IS NOT NULL ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ThCargoRubros> listaPartidasDistriReforma(ThCargo d, BigInteger b) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT pd from ThCargoRubros pd WHERE pd.idCargo.id= :id  AND pd.estado=TRUE  AND pd.codigoReforma=:reforma AND pd.partidaPresupuestaria IS NOT NULL AND pd.idRubro.origen=TRUE ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ThCargoRubros> listaPartidasDisReforma(ThCargo d, BigInteger b) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT pd from ThCargoRubros pd WHERE pd.idCargo.id=:cargo  AND pd.estado=TRUE AND pd.codigoReforma=:reforma AND pd.partidaPresupuestaria IS NOT NULL AND pd.idRubro.origen=TRUE ORDER BY pd.id ASC")
                .setParameter("cargo", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidasReformaNulos(Distributivo d, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id  AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReforma=:reforma AND pd.partidaAp IS NULL ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ThCargoRubros> listaPartidasDistReformaNulos(ThCargo d, BigInteger b) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT pd from ThCargoRubros pd  WHERE pd.idCargo.id= :id  AND pd.estado=TRUE  AND pd.codigoReforma=:reforma AND pd.partidaPresupuestaria IS NULL AND pd.idRubro.origen=TRUE ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<PartidasDistributivo> listaPresupuestoPartidasReformaModificar(Distributivo d, BigInteger b) {
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT pd from PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo d WHERE d.id= :id  AND vd.estado=TRUE AND d.estado=TRUE AND pd.codigoReforma=:reforma and pd.partidaAp is not null ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public List<ThCargoRubros> listaPartidasDisReformaModificar(ThCargo d, BigInteger b) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT pd from ThCargoRubros pd  WHERE pd.idCargo.id= :id AND pd.estado=TRUE  AND pd.codigoReforma=:reforma and pd.partidaPresupuestaria is not null AND pd.idRubro.origen=TRUE ORDER BY pd.id ASC")
                .setParameter("id", d.getId()).setParameter("reforma", b).getResultList();

        return result;
    }

    public BigDecimal getRMu(ThCargo d, Short periodo) {

        BigDecimal result = (BigDecimal) em.createQuery("SELECT de.remuneracionDolares from ThCargoRubros de  where de.idCargo.id=:id  AND de.idCargo.id = :periodo")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (result == null) {
            result = BigDecimal.ZERO;
        }

        return result;

    }

    public BigDecimal getRMuProyeccion(Distributivo d, Short periodo) {

        BigDecimal result = (BigDecimal) em.createQuery("SELECT de.remuneracionDolares from DistributivoEscala de INNER JOIN de.distributivo d where d.id= :id AND de.anio= :periodo")
                .setParameter("id", d.getId()).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (result == null) {
            result = BigDecimal.ZERO;
        }

        return result;

    }

    public BigDecimal getCupoDistributivo(ReformaIngresoSuplemento r, String codigo) {

        BigDecimal valor;
        Cupos c = (Cupos) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma re where re.id= :id AND c.otros= :codigo").setParameter("id", r.getId()).setParameter("codigo", codigo).getSingleResult();

        if (c == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = c.getMontoCupo();

        }

        return valor;
    }

    public BigDecimal valorParametrizado(String codigo) {
        try {
            Query query = em.createQuery("SELECT p.valor FROM ParametrosTalentoHumano p INNER JOIN p.tipo t WHERE p.estado = TRUE AND t.codigo = ?1 AND p.tipo = t")
                    .setParameter(1, codigo);
            BigDecimal result = (BigDecimal) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal valorParametrizado(String codigo, TipoRol rol) {
        try {
            BigDecimal valor = BigDecimal.ZERO;
            Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p INNER JOIN p.tipo t WHERE p.estado = TRUE AND t.codigo = ?1")
                    .setParameter(1, codigo);
            List<ParametrosTalentoHumano> result = (List<ParametrosTalentoHumano>) query.getResultList();
            if (!result.isEmpty()) {
                for (ParametrosTalentoHumano p : result) {
                    if (p.getVigenciaHasta() == null) {
                        valor = p.getValor();
                    } else if (TalentoHumano.validarFechaInicio(p.getVigenciaDesde(), rol) && TalentoHumano.validarFechaFin(p.getVigenciaHasta(), rol)) {
                        valor = p.getValor();
                    }
                }
            }
            return valor;
        } catch (NoResultException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getSumaDistributivosSuplemento(BigInteger b,boolean tipo) {

        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM  ThCargoRubros p where p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL AND p.idRubro.origen=:tipo")
                .setParameter("reforma", b).setParameter("tipo", tipo).getSingleResult();

        return valor;
    }

    public BigDecimal getValorAntiguoSuplementoDistribuvtivo(ThCargoRubros p) {
        BigDecimal valor = BigDecimal.ZERO;

        PartidasDistributivo result = (PartidasDistributivo) em.createQuery("SELECT p FROM  ThCargoRubros p where p.id= :id ").setParameter("id", p.getId()).getSingleResult();
        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getReformaSuplemento();
        }

        return valor;
    }

    public PartidasDistributivo verValorActual(PartidasDistributivo p) {
        PartidasDistributivo valor = (PartidasDistributivo) em.createQuery("SELECT p FROM  PartidasDistributivo p where p.id= :id ").setParameter("id", p.getId()).getSingleResult();
        return valor;
    }
    
    
     public ThCargoRubros verValorActualThCargosRubros(ThCargoRubros p) {
        ThCargoRubros valor = (ThCargoRubros) em.createQuery("SELECT p FROM  ThCargoRubros p where p.id= :id ").setParameter("id", p.getId()).getSingleResult();
        return valor;
    }

    public BigDecimal getSumaDistributivosReduccion(BigInteger b) {

        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM  PartidasDistributivo p where p.codigoReforma= :reforma  ").setParameter("reforma", b).getSingleResult();
        return valor;
    }

    public List<PartidasDistributivo> getCopiaReforma(Short periodo, CatalogoItem c) {
//AND p.estadoPartida= :estado  
//setParameter("estado", c)
        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.periodo= :periodo  AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.estado=true").
                setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<PartidasDistributivo> verificarReformaDis(BigInteger r) {

        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.codigoReforma= :reforma").setParameter("reforma", r).getResultList();
        return result;
    }

    public List<PartidasDistributivo> verificarReformaTraspasoDis(BigInteger r) {

        List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.codigoReformaTraspaso= :reforma").setParameter("reforma", r).getResultList();
        return result;
    }

    public BigDecimal verReserva(String partida, short periodo) {

        BigDecimal result = BigDecimal.ZERO;

        Presupuesto objecto = (Presupuesto) em.createQuery("SELECT p FROM Presupuesto p WHERE p.partida= :partida AND p.periodo= :periodo")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (objecto != null) {
            result = objecto.getReserva();
        }

        return result;
    }

    public ValoresDistributivo getListaValoresAporteEgreso(TipoRol rol, String cod1, String cod2) {
        try {
            Query query = em.createQuery("SELECT v FROM ValoresDistributivo v INNER JOIN v.valoresParametrizados vp WHERE v.estado = TRUE AND v.anio = ?1 AND vp.nombre LIKE '%?2%' AND vp.tipo.codigo =?3")
                    .setParameter(1, rol.getAnio()).setParameter(2, cod1).setParameter(3, cod2);
            List<ValoresDistributivo> result = (List<ValoresDistributivo>) query.getResultList();
            return result.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ValoresDistributivo> getAllValoresDis(Distributivo d, Short anio) {
        try {
            if (d == null) {
                return null;
            }
            if (d.getId() == null) {
                return null;
            }
            Query query = em.createQuery("SELECT vd FROM ValoresDistributivo vd WHERE vd.distributivo = ?1 and vd.anio = ?2 and vd.estado = true ")
                    .setParameter(1, d)
                    .setParameter(2, anio);
            List<ValoresDistributivo> resul = (List<ValoresDistributivo>) query.getResultList();
            return resul;
        } catch (NoResultException e) {
            return null;
        }
    }

//    public Long getCount_1(Distributivo distributivo) {
//        Long resultado = (Long) em.createQuery("SELECT  COUNT (pd) FROM PartidasDistributivo pd INNER JOIN pd.distributivo vd INNER JOIN vd.distributivo di WHERE vd.estado=TRUE AND pd.estado=TRUE AND pd.codigoReforma is null  AND pd.codigoReformaTraspaso is null AND di.estado=TRUE AND di.id=:distributivo AND pd.partidaAp is null")
//                .setParameter("distributivo", distributivo.getId())
//                .getSingleResult();
//        return resultado;
//    }
//
    public Boolean getCount(ValoresDistributivo distributivo) {
        Boolean resultBoolean = Boolean.FALSE;
        PartidasDistributivo resultado = (PartidasDistributivo) em.createQuery("SELECT pd FROM PartidasDistributivo pd INNER JOIN pd.distributivo vd WHERE vd.estado=TRUE AND pd.estado=TRUE AND pd.codigoReforma is null  AND pd.codigoReformaTraspaso is null AND vd.id=:distributivo")
                .setParameter("distributivo", distributivo.getId())
                .getResultStream().findFirst().orElse(null);
        if (resultado != null) {
            if (resultado.getPartidaAp() != null) {
                resultBoolean = Boolean.TRUE;
            }
        }
        return resultBoolean;
    }

    public void getDelePartidaValorDistributivo(ValoresDistributivo vDistributivo) {
        Query query = em.createQuery("DELETE FROM PartidasDistributivo pd WHERE pd.distributivo=:vDistributivo AND pd.codigoReforma is null AND pd.codigoReformaTraspaso is null")
                .setParameter("vDistributivo", vDistributivo);
        int result = query.executeUpdate();
    }
}
