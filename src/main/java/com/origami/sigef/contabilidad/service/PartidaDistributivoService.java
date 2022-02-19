/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PartidaDistributivoService extends AbstractService<PartidasDistributivo> {

    @Inject
    private ManagerService service;

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PartidaDistributivoService() {
        super(PartidasDistributivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public PlanProgramatico devulveObjeto(Short periodo, Long id) {
        PlanProgramatico plan = (PlanProgramatico) em.createQuery("SELECT p FROM PlanProgramatico p WHERE p.id=:id AND p.periodo=:periodo")
                .setParameter("id", id)
                .setParameter("periodo", periodo)
                .getResultStream().findFirst().orElse(null);
        return plan;
    }

    public CatalogoPresupuesto devulveObjetoItem(Long id) {
        CatalogoPresupuesto item = (CatalogoPresupuesto) em.createQuery("SELECT p FROM CatalogoPresupuesto p WHERE p.id=:id")
                .setParameter("id", id)
                .getSingleResult();
        return item;
    }

    public FuenteFinanciamiento devulveObjetoFuente(Short periodo, Long id) {
        FuenteFinanciamiento fuente = (FuenteFinanciamiento) em.createQuery("SELECT ae FROM FuenteFinanciamiento ae WHERE ae.id= :id AND ae.periodo= :periodo")
                .setParameter("id", id)
                .setParameter("periodo", periodo)
                .getSingleResult();
        return fuente;
    }

    public CatalogoItem devulveObjetoFuenteDirecta(Long id) {
        CatalogoItem fuenteDirecta = (CatalogoItem) em.createQuery("SELECT ae FROM CatalogoItem ae WHERE ae.id= :id")
                .setParameter("id", id)
                .getSingleResult();
        return fuenteDirecta;
    }

    public List<ProformaPDTO> showCodigosAgrupados(Short periodo) {

        Query nativeQuery = em.createNativeQuery("(select thr.partida_presupuestaria,sum(thr.monto) as total, \n"
                + "thr.id_estructura,thr.id_presupuesto, thr.id_fuente, 'PD' as tipo \n"
                + "from talento_humano.th_cargo_rubros thr inner join talento_humano.th_rubro r \n"
                + "ON r.id = thr.id_rubro inner join talento_humano.th_cargo thc ON thc.id = thr.id_cargo\n"
                + "where thr.periodo=?1 AND thr.partida_presupuestaria is not null and thr.estado = true\n"
                + "and thr.codigo_reforma is null and thr.codigo_reforma_traspaso is null and r.origen = true\n"
                + "and thc.estado = true and thc.activo = true\n"
                + "GROUP BY thr.partida_presupuestaria, thr.id_estructura,thr.id_presupuesto,thr.id_fuente,tipo)\n"
                + "UNION ALL\n"
                + "(select thr.partida_presupuestaria,sum(thr.monto) as total, \n"
                + "thr.id_estructura,thr.id_presupuesto, thr.id_fuente, 'PDA' as tipo \n"
                + "from talento_humano.th_cargo_rubros thr inner join talento_humano.th_rubro r \n"
                + "ON r.id = thr.id_rubro where thr.periodo=?1 AND thr.partida_presupuestaria is not null \n"
                + "and thr.codigo_reforma is null and thr.codigo_reforma_traspaso is null and r.origen = false\n"
                + "GROUP BY thr.partida_presupuestaria, thr.id_estructura,thr.id_presupuesto,thr.id_fuente,tipo);");
        nativeQuery.setParameter(1, periodo);
        List<Object[]> result = nativeQuery.getResultList();

        if (Utils.isNotEmpty(result)) {

            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                BigInteger a, b, c;
                a = (BigInteger) data[2];
                b = (BigInteger) data[3];
                c = (BigInteger) data[4];
                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setEstructuraNew(service.find(PresPlanProgramatico.class, a.longValue()));
                ppdto.setItemNew(service.find(PresCatalogoPresupuestario.class, b.longValue()));
                ppdto.setFuenteNew(service.find(PresFuenteFinanciamiento.class, c.longValue()));
                ppdto.setCodigo((String) data[5]);
                list.add(ppdto);

            }
            return list;
        }
        return null;
    }

    public List<Cliente> filtroServidor(Short periodo) {

        List<Cliente> listaClientes = (List<Cliente>) em.createQuery("SELECT per FROM PartidasDistributivo p INNER JOIN p.distributivo pd INNER JOIN pd.servidorPublico sp INNER JOIN sp.persona per WHERE p.periodo= :periodo ")
                .setParameter("periodo", periodo)
                .getResultList();
        return listaClientes;

    }

    public List<UnidadAdministrativa> filtroUnidades(Short periodo) {
        List<UnidadAdministrativa> listaUnidades = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT(u) FROM PartidasDistributivo p INNER JOIN p.distributivo d INNER JOIN d.unidadAdministrativa u WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getResultList();
        return listaUnidades;
    }

    public List<Cargo> filtroCargos(Short periodo) {
        List<Cargo> listaCargos = (List<Cargo>) em.createQuery("SELECT DISTINCT (c) FROM PartidasDistributivo p INNER JOIN p.distributivo d INNER JOIN d.cargo c WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getResultList();
        return listaCargos;

    }

    public Distributivo getDistributivo(Long id) {

        Distributivo dis = (Distributivo) em.createQuery("SELECT p FROM Distributivo p where p.id=:id").setParameter("id", id).getSingleResult();
        return dis;
    }

    public List<Long> casteraDistributivo(Short periodo) throws ParseException {

        String fecha = periodo.toString();
        Date date = new SimpleDateFormat("yyyy").parse(fecha);

        List<Long> listaDistributivos
                = (List<Long>) em.createNativeQuery("Select d.id from talento_humano.distributivo d  \n"
                        + "	where d.id not in (SELECT f.distributivo from partidas_distributivo f where f.periodo=?1)\n"
                        + "					   AND EXTRACT(year from d.vigencia_desde)<=?2")
                        .setParameter(1, periodo)
                        .setParameter(2, periodo)
                        .getResultList();
        return listaDistributivos;

    }

    public List<Long> consultaDistributivosFechas(Short periodo) {
        String sql = "select pd.id from talento_humano.distributivo d\n"
                + "inner join partidas_distributivo pd on \n"
                + "pd.distributivo=d.id\n"
                + "where  EXTRACT(year from d.vigencia_desde)>pd.periodo\n"
                + "and pd.periodo=?1";

        List<Long> disListFechasCambaidas = (List<Long>) em.createNativeQuery(sql).setParameter(1, periodo).getResultList();
        return disListFechasCambaidas;

    }

    public void eliminarPartidasDistribuivosCambiados(Long id) {
        String hql = "delete from PartidasDistributivo d where d.id=?1  ";
        Query query = em.createQuery(hql).setParameter(1, id);

        int rowCount = query.executeUpdate();

    }

    //Luis Pozo G traer todos los distributivos Anexos por periodo y su Partida Ap is not null
    public List<PartidasDistributivoAnexo> showCPartidasAnexo(Short periodo) {

        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo = ?1 AND p.partidaAp IS NOT NULL").setParameter(1, periodo);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    //trae todos los valores de partidas distributivo que no estan en valores rubros
    public List<PartidasDistributivo> showPartidaxDistributivoAndPeriodo(Distributivo d, Short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT p FROM PartidasDistributivo p "
                    + "JOIN p.distributivo v JOIN v.valoresParametrizados param "
                    + "WHERE v.distributivo=?1 AND  p.partidaAp IS NOT NULL  AND v.anio=?2 AND v.estado = TRUE "
                    + "AND p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL"
                    + " AND param.id NOT IN ( SELECT va.valorParametrizable FROM ValoresRoles va JOIN va.rolPago r "
                    + "JOIN r.servidor s WHERE va.estado = TRUE and s.distributivo = ?1 AND s.estado = true and va.periodo = ?2)")
                    .setParameter(1, d).setParameter(2, anio);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PartidasDistributivo> showAllPartidaXDistributiv(Distributivo d, Short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT p FROM PartidasDistributivo p JOIN p.distributivo v JOIN v.valoresParametrizados param WHERE v.distributivo=?1 AND  p.partidaAp IS NOT NULL  AND v.anio=?2 AND v.estado = TRUE AND p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL")
                    .setParameter(1, d).setParameter(2, anio);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    //trae el distributivo de las partidas de un año
    public List<Distributivo> getAllPartidaDistributivo(Short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT d FROM PartidasDistributivo p JOIN p.distributivo v JOIN v.distributivo d WHERE p.partidaAp IS NOT NULL  AND v.anio=?1 AND v.estado = TRUE AND p.estado = TRUE AND p.periodo = ?1")
                    .setParameter(1, anio);
            List<Distributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    //me da el distributivo dee un añi y una unidad administrativa en especifico
    public List<Distributivo> getPartidaDistributivoXperiodoUnidad(Short anio, long id) {
        try {
            Query query = em.createQuery("SELECT DISTINCT d FROM PartidasDistributivo p JOIN p.distributivo v JOIN v.distributivo d JOIN d.unidadAdministrativa u WHERE p.partidaAp IS NOT NULL  AND v.anio=?1 AND v.estado = TRUE AND u.id = ?2 ")
                    .setParameter(1, anio).setParameter(2, id);
            List<Distributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PartidasDistributivo> getServidoresXRubroPartida(Short anio, String codigo) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivo pd JOIN pd.distributivo vd JOIN vd.distributivo d JOIN vd.valoresParametrizados p JOIN p.tipo c WHERE pd.periodo = ?1 AND pd.estado = TRUE AND vd.estado = TRUE AND c.codigo = ?2 AND d.servidorPublico IS NOT NULL AND pd.partidaAp IS NOT NULL")
                    .setParameter(1, anio).setParameter(2, codigo);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PartidasDistributivo> getListPartidaDistributivoOriginal(Short anio) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivo pd JOIN pd.distributivo vd JOIN vd.distributivo d JOIN vd.valoresParametrizados p JOIN p.tipo c JOIN d.cargo ca WHERE pd.periodo = ?1 AND pd.estado = TRUE AND vd.estado = TRUE AND pd.codigoReforma IS NULL ORDER BY ca.id")
                    .setParameter(1, anio);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PartidasDistributivo> getPartidasXValorRubro(ValoresDistributivo valor) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivo pd where pd.distributivo = ?1 and pd.estado = true and pd.codigoReformaTraspaso IS NULL AND pd.codigoReforma IS NULL ")
                    .setParameter(1, valor);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    // trae todo los datos de partidaDistributivo x vALORDistirbutivo
    public List<PartidasDistributivo> getPartidasAllXValorRubro(ValoresDistributivo valor, Short anio) {
        try {
            if (valor == null) {
                return null;
            }
            if (valor.getId() == null) {
                return null;
            }
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivo pd where pd.distributivo = ?1 and pd.estado = true  and pd.periodo = ?2 ")
                    .setParameter(1, valor)
                    .setParameter(2, anio);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ThCargoRubros> getDistributivoOriginal(Short periodo) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.periodo =:periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.estado=true")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<PartidasDistributivoAnexo> getDistributivoAnexoOriginal(Short periodo) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo=:periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.estado=true")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public BigDecimal reservadoGlobal(String partida, Short periodo, int solicitaado, BigInteger num) {
        BigDecimal result = BigDecimal.ZERO;

        switch (solicitaado) {
            case 1:
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE d.presupuesto.partida=:partida AND d.periodo=:periodo AND e.codigo in ('LIQUI','APRO')")
                        .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(BigDecimal.ZERO);
                break;
            case 2:
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoComprometido),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE d.presupuesto.partida=:partida AND d.periodo=:periodo AND e.codigo in ('LIQUI','APRO')")
                        .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(BigDecimal.ZERO);
                break;
            default:
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM PartidasDistributivo p WHERE p.partidaAp=:partida AND p.codigoReforma=:reforma ")
                        .setParameter("partida", partida).setParameter("reforma", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
                break;
        }
        return result;
    }

    public List<String> partidasGlobales(BigInteger num) {
        List<String> lista = (List<String>) em.createQuery("SELECT DISTINCT(p.partidaAp) FROM PartidasDistributivo p WHERE p.codigoReforma=:reforma AND p.partidaAp IS NOT NULL")
                .setParameter("reforma", num).getResultList();
        return lista;

    }

    public BigDecimal presupuestoPDReformado(String partida, Short periodo, int solicitaado, BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaCodificado),0) FROM PartidasDistributivo p WHERE p.partidaAp=:partida AND p.codigoReforma=:reforma")
                .setParameter("partida", partida).setParameter("reforma", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        return result;
    }

    public BigDecimal presupuestoPDReduccion(String partida, Short periodo, int solicitaado, BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reserva),0) FROM PartidasDistributivo p WHERE p.partidaAp=:partida AND p.codigoReforma=:reforma")
                .setParameter("partida", partida).setParameter("reforma", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        return result;
    }

    public BigDecimal montoPresupuestado(String partida, Short periodo) {
        BigDecimal data = (BigDecimal) em.createQuery("SELECT COALESCE(p.codificado,0) FROM Presupuesto p WHERE p.partida=:partida AND p.periodo=:periodo")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return data;
    }
}
