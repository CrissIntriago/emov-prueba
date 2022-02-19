/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaDTO;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProformaPresupuestoPlanificadoService extends AbstractService<ProformaPresupuestoPlanificado> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private ManagerService service;

    public ProformaPresupuestoPlanificadoService() {
        super(ProformaPresupuestoPlanificado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProformaDTO> totalProductoProforma(Short periodo) {

        String sqlString = "select p.codigo_presupuestario as partida, sum(p.monto) as total, cp.descripcion as item, pm.descripcion as estructura, fue.descripcion as fuente \n"
                + " from producto p inner join catalogo_presupuesto cp on p.item_presupuestario = cp.id\n"
                + "inner join plan_programatico pm on p.estructura_programatica = pm.id inner join fuente_financiamiento fue on fue.id = p.fuente"
                + " WHERE p.periodo=?1 GROUP BY p.codigo_presupuestario,cp.descripcion,pm.descripcion,fue.descripcion";
        List<ProformaDTO> result = em.createNativeQuery(sqlString, ProformaDTO.class).setParameter(1, periodo).getResultList();

        return result;
    }

    public PlanProgramatico devulveObjeto(Short periodo, Long id) {
        PlanProgramatico plan = (PlanProgramatico) em.createQuery("SELECT p FROM PlanProgramatico p WHERE p.id=:id")
                .setParameter("id", id)
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

    public List<ProformaPDTO> muestrameEgresos(Short periodo) {

        Query query = em.createNativeQuery("select p.codigo_presupuestario As partida, sum(p.monto) as total,\n"
                + "                p.estructura_new AS estructuraProgramatica, p.item_new AS itemPresupuestario,\n"
                + "                p.fuente_new as fuente\n"
                + "                from producto p WHERE p.periodo=?1 and p.codigo_reforma is null and \n"
                + "                p.codigo_reforma_traspaso is null and p.codigo_presupuestario IS NOT NULL\n"
                + "                group by p.codigo_presupuestario, p.estructura_new,p.item_new,p.fuente_new;")
                .setParameter(1, periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                BigInteger e, i, f;
                e = (BigInteger) data[2];
                i = (BigInteger) data[3];
                f = (BigInteger) data[4];
                ProformaPDTO ppdto = new ProformaPDTO();               
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setEstructuraNew(service.find(PresPlanProgramatico.class, e.longValue()));
                ppdto.setItemNew(service.find(PresCatalogoPresupuestario.class, i.longValue()));
                ppdto.setFuenteNew(service.find(PresFuenteFinanciamiento.class, f.longValue()));
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public Boolean consultarDatos(Short periodo, String codigo) {

//        String hql = "SELECT p.codigoPresupuestario,SUM(p.monto) FROM Producto p INNER JOIN p.estructuraProgramatica estructura ";
//        List<Object[]> result = (List<Object[]>) em.createQuery("SELECT p.codigoPresupuestario AS partida,SUM(p.monto) AS total,p.estructuraProgramatica AS estructuraProgramatica ,p.itemPresupuestario AS itemPresupuestario,p.fuente AS fuente FROM Producto p  WHERE p.periodo= :periodo GROUP BY p.codigoPresupuestario,p.estructuraProgramatica,p.itemPresupuestario,p.fuente")
//                .setParameter("periodo",periodo)
//                .getResultList();
        Query query = em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.estado= true AND p.periodo=:periodo AND p.partidaPresupuestaria=:codigo AND p.codigoReforma IS NULL")
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            return true;
        } else {
            return false;
        }
    }

    public BigDecimal totalPresupuesto(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.valor) FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND p.codigoReforma  IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo)
                .getSingleResult();

        if (val == null) {
            val = BigDecimal.ZERO;
        }

        return val;
    }

    public void eliminandoRegistroLogica(Short periodo, String codigo) {

        Query query = em.createQuery("delete from ProformaPresupuestoPlanificado p WHERE p.periodo = :periodo and p.codigo = :codigo and p.codigoReforma IS NULL and p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo);

        query.executeUpdate();
    }

    public List<ProformaPresupuestoPlanificado> getProformaPresupuestoPlanificado(short periodo) {
        String sql = "SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo = :periodo AND p.estado = TRUE and p.codigoReforma IS NULL";
        Query query = em.createQuery(sql)
                .setParameter("periodo", periodo);
        List<ProformaPresupuestoPlanificado> result = query.getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> ListaDePartidaBloquear(Short periodo) {
        String sqlString = "select *from proforma_presupuesto_planificado pa where pa.periodo=?1\n"
                + "			and pa.codigo_reforma is null and pa.partida_presupuestaria not in (select pr.codigo_presupuestario  "
                + "from producto pr where pr.periodo=?2 and pr.codigo_reforma is null)";
        List<ProformaPresupuestoPlanificado> result = em.createNativeQuery(sqlString, ProformaPresupuestoPlanificado.class).setParameter(1, periodo).setParameter(2, periodo).getResultList();

        return result;
    }

    public List<CatalogoProformaPresupuesto> desactivarBoton(Short periodo, boolean tipo, boolean aprobado) {

        Query query = em.createQuery("SELECT p FROM CatalogoProformaPresupuesto p WHERE p.periodo= :periodo AND p.tipoFlujo= :tipo AND p.aprobado= :aprobado ")
                .setParameter("periodo", periodo)
                .setParameter("tipo", tipo)
                .setParameter("aprobado", aprobado);
        List<CatalogoProformaPresupuesto> result = query.getResultList();

        return result;
    }

    //metodo que se usa para bloquear acciones en el periodo enviado distributivo General y Anexo
    public boolean BloquearSiEsAprobado(Short periodo, boolean tipo, boolean aprobado) {
        Query query = em.createQuery("SELECT p FROM CatalogoProformaPresupuesto p WHERE p.periodo= :periodo AND p.tipoFlujo= :tipo AND p.aprobado= :aprobado AND p.estado = TRUE ")
                .setParameter("periodo", periodo)
                .setParameter("tipo", tipo)
                .setParameter("aprobado", aprobado);
        List<CatalogoProformaPresupuesto> li = query.getResultList();
        boolean verificar = true;
        if (li.size() > 0) {
            verificar = true;
        } else {
            verificar = false;
        }
        return verificar;
    }

    // retorna un true si la partida se encuentra en esta proforma y pertenece a ese codigo
    public boolean ExistPartidaInProforma(short periodo, String codigo, String partida) {
        Query query = em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo = ?1 AND p.estado = TRUE AND p.codigo = ?2 AND p.partidaPresupuestaria = ?3 AND p.codigoReforma IS NULL")
                .setParameter(1, periodo)
                .setParameter(2, codigo)
                .setParameter(3, partida);
        ProformaPresupuestoPlanificado li = (ProformaPresupuestoPlanificado) query.getSingleResult();
        boolean result = false;
        if (li != null) {
            result = true;

        } else {
            result = false;
        }
        return result;
    }

    public List<ProformaPDTO> mostrarProfromaPresupuesto(Short periodo) {
        Query query = em.createQuery("SELECT p.partidaPresupuestaria,SUM(p.valor),p.estructuraProgramatica, p.itemPresupuestario,p.fuente FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo GROUP BY p.partidaPresupuestaria,p.estructuraProgramatica, p.itemPresupuestario,p.fuente")
                .setParameter("periodo", periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setEstructuraProgramatica((PlanProgramatico) data[2]);
                ppdto.setItemPresupuestario((CatalogoPresupuesto) data[3]);
                ppdto.setFuente((FuenteFinanciamiento) data[4]);
                ppdto.setFuenteDirecta(ppdto.getFuente().getTipoFuente());
                list.add(ppdto);
            }
            return list;
        }

        return null;

    }

    public List<ProformaPDTO> mostrarDistribuivo(Short periodo) {
        String sql = "select pg.codigo||cap.codigo||ci.orden as partida,\n"
                + "sum(es.remuneracion_dolares) as total ,dis.estructura_programatica,\n"
                + "dis.item_presupuestario_egresos,dis.item_presupuestario_egresos\n"
                + "FROM talento_humano.distributivo dis inner join\n"
                + "talento_humano.escala_salarial es on\n"
                + "es.id = dis.escala_salarial inner join public.plan_programatico pg\n"
                + "on pg.id=dis.estructura_programatica \n"
                + "inner join public.catalogo_presupuesto cap \n"
                + "on cap.id=dis.item_presupuestario_egresos inner join \n"
                + "public.fuente_financiamiento fu on fu.id=dis.fuente_financiamiento \n"
                + "inner join catalogo_item ci on  ci.id = fu.tipo_fuente\n"
                + "where dis.anio=?1 GROUP BY partida,dis.estructura_programatica,\n"
                + "dis.item_presupuestario_egresos,dis.item_presupuestario_egresos; \n"
                + "";
        List<Object[]> result = em.createNativeQuery(sql, Object.class).setParameter(1, periodo).getResultList();

        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setEstructuraProgramatica((PlanProgramatico) data[2]);
                ppdto.setItemPresupuestario((CatalogoPresupuesto) data[3]);
                ppdto.setFuente((FuenteFinanciamiento) data[4]);
                ppdto.setFuenteDirecta(ppdto.getFuente().getTipoFuente());
                list.add(ppdto);

            }
            return list;
        }

        return null;
    }

    public List<ProformaPresupuestoPlanificado> verListaAnual(Short periodo) {
        Query query = em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo and p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("periodo", periodo);
        List<ProformaPresupuestoPlanificado> result = query.getResultList();

        return result;

    }

    public List<PlanProgramatico> filtroEstructura(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM ProformaPresupuestoPlanificado p INNER JOIN p.estructuraProgramatica e WHERE p.periodo= :periodo ")
                .setParameter("periodo", periodo);
        List<PlanProgramatico> result = query.getResultList();
        return result;
    }

    public List<CatalogoPresupuesto> filtroItem(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM ProformaPresupuestoPlanificado p INNER JOIN p.itemPresupuestario e WHERE p.periodo= :periodo ")
                .setParameter("periodo", periodo);
        List<CatalogoPresupuesto> result = query.getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> getPratidasDirectasReformas(Short periodo, String tipo) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.estado= true AND p.periodo= :periodo AND p.codigo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo).getResultList();

        return result;
    }

    public List<ProformaPresupuestoPlanificado> getListaVerificacion(BigInteger b) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();

        return result;
    }

    public BigDecimal totalSuplemento(BigInteger r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento ) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma ").setParameter("reforma", r).getSingleResult();
        return result;
    }

    public BigDecimal totalSuplementoActual(ProformaPresupuestoPlanificado r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT p.reformaSuplemento FROM ProformaPresupuestoPlanificado p WHERE p.id= :id").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> getProformaPresupuestoPartidaDirecta(short periodo, String codigo) {

        return (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo = :periodo AND p.estado = TRUE and p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.codigo = :codigo ")
                .setParameter("periodo", periodo).setParameter("codigo", codigo).getResultList();

    }

    public List<ProformaPresupuestoPlanificado> getPdRefroma(BigInteger b) {

        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM  ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ProformaPresupuestoPlanificado getActual(ProformaPresupuestoPlanificado p) {
        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.id= :id")
                .setParameter("id", p.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getMontoReservado(String partida, Short periodo) {
        BigDecimal lista = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE e.codigo IN ('LIQUI','APRO') AND d.presupuesto.partida=:partida AND s.periodo=:periodo")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return lista;
    }
}
