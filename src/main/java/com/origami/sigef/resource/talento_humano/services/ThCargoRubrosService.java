/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.models.ThRubroAsignacionModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThCargoRubrosService extends AbstractService<ThCargoRubros> {

    @Inject
    private ThRubroService thRubroService;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PresPlanProgramaticoService presPlanProgramaticoService;
    @Inject
    private PresCatalogoPresupuestarioService presCatalogoPresupuestarioService;
    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanciamientoService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThCargoRubrosService() {
        super(ThCargoRubros.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ThCargoRubros> findByCargoXanio(ThCargo thCargo, short oldPeriodo) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery(QUERY.FIND_CARGO_RUBRO_ANIO)
                .setParameter(1, thCargo)
                .setParameter(2, oldPeriodo)
                .getResultList();
        return result;
    }

    public List<ThCargoRubros> findByCargoXanioIngresos(ThCargo thCargo, Short periodo) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery(QUERY.FIND_CARGO_RUBRO_ANIO_INGRESOS)
                .setParameter(1, thCargo)
                .setParameter(2, periodo)
                .getResultList();
        return result;
    }

    public List<ThCargoRubros> copiaReforma(Boolean estado, Short periodo) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t where t.estado=:estado AND t.periodo=:periodo AND t.codigoReforma is null and t.codigoReformaTraspaso is null")
                .setParameter("estado", estado).setParameter("periodo", periodo).getResultList();
    }

    public List<ThCargoRubros> copiaVerificacion(BigInteger verificacion) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t  WHERE t.codigoReforma=:verificacion ")
                .setParameter("verificacion", verificacion).getResultList();
    }

    public List<ThCargoRubros> copiaVerificacion2(BigInteger verificacion) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t  WHERE t.codigoReformaTraspaso=:verificacion ")
                .setParameter("verificacion", verificacion).getResultList();
    }

    public List<ThCargoRubros> distributivoCompleto(Short periodo) {
        return (List<ThCargoRubros>) em.createNativeQuery("select th.* from talento_humano.th_cargo_rubros th INNER JOIN talento_humano.th_cargo cg on th.id_cargo = cg.id\n"
                + "inner join talento_humano.th_rubro tr ON tr.id = th.id_rubro\n"
                + "where th.periodo = ?1 and th.estado = true and th.codigo_reforma is null \n"
                + "and th.codigo_reforma_traspaso is null AND cg.activo = TRUE AND cg.estado = TRUE and tr.origen = true\n"
                + "UNION ALL\n"
                + "select th.* from talento_humano.th_cargo_rubros th inner join talento_humano.th_rubro tr ON tr.id = th.id_rubro\n"
                + "where th.periodo = ?1 and th.estado = true and th.codigo_reforma is null \n"
                + "and th.codigo_reforma_traspaso is null and tr.origen = false", ThCargoRubros.class).
                setParameter(1, periodo).getResultList();
    }

    public List<ThCargoRubros> listaDistributivos(BigInteger reforma, ThCargo th) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t WHERE t.codigoReforma=:codigo and t.idCargo=:cargo and t.idRubro.origen=TRUE AND t.idRubro.ingreso=TRUE  ")
                .setParameter("codigo", reforma).setParameter("cargo", th).getResultList();
    }

    public List<ThCargoRubros> listaDistributivosQnexo(BigInteger reforma) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t WHERE t.codigoReforma=:codigo and t.idRubro.origen=FALSE AND t.idRubro.ingreso=TRUE ")
                .setParameter("codigo", reforma).getResultList();
    }

    public List<ThCargoRubros> listaDistributivoReformando(ThCargo th, BigInteger reforma) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t WHERE t.idRubro.ingreso=TRUE AND t.idCargo=:cargo  AND t.codigoReforma=:reforma AND t.idRubro.origen=TRUE")
                .setParameter("cargo", th).setParameter("reforma", reforma).getResultList();
    }

    public List<ThCargoRubros> listaDistributivoAnexoReformando(BigInteger reforma) {
        return (List<ThCargoRubros>) em.createQuery("SELECT t FROM ThCargoRubros t WHERE t.idRubro.ingreso=TRUE AND t.idRubro.origen=TRUE AND t.codigoReforma=:reforma")
                .setParameter("reforma", reforma).getResultList();
    }

    public BigDecimal cupoAsignadoDistributivo(boolean tipoRubro, boolean tipoDistributivo, Short periodo) {
        BigDecimal result = (BigDecimal) em.createQuery(QUERY.SUM_RUBROS_CARGO)
                .setParameter(1, tipoRubro)
                .setParameter(2, tipoDistributivo)
                .setParameter(3, periodo)
                .getSingleResult();
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean countPartidasAsignadas(Short anio) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery(QUERY.LIST_RUBROS_CARGO)
                .setParameter(1, anio).getResultList();
        return !result.isEmpty();
    }

    public List<ThRubroAsignacionModel> findRubrosXParametros(Short anio, Boolean cargos, List<String> cargosSeleccionado, Boolean tiporubro, Boolean acciones, Boolean asociacion) {
        String cargosList = cargosSeleccionado.toString().replace("[", "").replace("]", "");
        List<ThRubroAsignacionModel> result = (List<ThRubroAsignacionModel>) em.createNativeQuery(QUERY.MODEL_TH_RUBRO_ASIGNACION, "ThRubroAsignacionModelMapping")
                .setParameter(1, anio)
                .setParameter(2, cargos)
                .setParameter(3, cargosList)
                .setParameter(4, tiporubro)
                .setParameter(5, acciones)
                .setParameter(6, asociacion)
                .getResultList();
        return result;
    }

    public List<ThCargoRubros> findRubrosXParametros(Short anio, String thCargoSeleccionado, UnidadAdministrativa unidadFind, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion) {
        boolean parametro_1 = false;
        String valor_1 = "";
        boolean parametro_2 = false;
        String valor_2 = "";
        boolean parametro_3 = false;
        String valor_3 = "";
        if (unidadFind != null) {
            parametro_1 = true;
            valor_1 = unidadFind.getId().toString();
        }
        if (filtroContrato != null) {
            parametro_2 = true;
            valor_2 = filtroContrato.getId().toString();
        }
        if (filtroClasificacion != null) {
            parametro_3 = true;
            valor_3 = filtroClasificacion.getId().toString();
        }
        List<ThCargoRubros> result = new ArrayList<>();
        List<ThRubroAsignacionModel> temp = (List<ThRubroAsignacionModel>) em.createNativeQuery("SELECT \n"
                + "tcr.id_rubro as idrubro, tcr.id_estructura as idestructura, tcr.id_presupuesto as idpresupuesto, \n"
                + "tcr.id_fuente as idfuente, tcr.partida_presupuestaria as partida, tcr.id_cuenta as idcuenta\n"
                + "FROM talento_humano.th_cargo_rubros tcr\n"
                + "INNER JOIN talento_humano.th_cargo tc ON tcr.id_cargo = tc.id\n"
                + "WHERE tc.estado = true AND tc.activo = true\n"
                + "AND tcr.periodo = ?1 AND tc.nombre_cargo = ?2\n"
                + "AND (CASE WHEN true = ?3 THEN tc.id_unidad = CAST(CONCAT(?4,NULL) AS bigint) ELSE true END)\n"
                + "AND (CASE WHEN true = ?5 THEN tc.id_contrato = CAST(CONCAT(?6,NULL) AS bigint) ELSE true END)\n"
                + "AND (CASE WHEN true = ?7 THEN tc.id_catalogo_item = CAST(CONCAT(?8,NULL) AS bigint) ELSE true END)\n"
                + "GROUP BY 1,2,3,4,5,6", "ThFiltroRubro")
                .setParameter(1, anio)
                .setParameter(2, thCargoSeleccionado)
                .setParameter(3, parametro_1)
                .setParameter(4, valor_1)
                .setParameter(5, parametro_2)
                .setParameter(6, valor_2)
                .setParameter(7, parametro_3)
                .setParameter(8, valor_3)
                .getResultList();
        for (ThRubroAsignacionModel item : temp) {
            ThCargoRubros add = new ThCargoRubros();
            if (item.getIdrubro() != null) {
                add.setIdRubro(thRubroService.find(item.getIdrubro()));
            }
            if (item.getIdcuenta() != null) {
                add.setIdCuenta(contCuentasService.find(item.getIdcuenta()));
            }
            if (item.getIdestructura() != null) {
                add.setIdEstructura(presPlanProgramaticoService.find(item.getIdestructura()));
            }
            if (item.getIdpresupuesto() != null) {
                add.setIdPresupuesto(presCatalogoPresupuestarioService.find(item.getIdpresupuesto()));
            }
            if (item.getIdfuente() != null) {
                add.setIdFuente(presFuenteFinanciamientoService.find(item.getIdfuente()));
            }
            if (item.getPartida() != null) {
                add.setPartidaPresupuestaria(item.getPartida());
            }
            result.add(add);
        }
        return result;
    }

    public void getUpdateRubroPartida(String thCargoSeleccionado, ThCargoRubros item, Short anio, UnidadAdministrativa unidadFind, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion) {
        System.out.println("getUpdateRubroPartida");
        boolean parametro_1 = false;
        String valor_1 = "";
        boolean parametro_2 = false;
        String valor_2 = "";
        boolean parametro_3 = false;
        String valor_3 = "";
        if (unidadFind != null) {
            parametro_1 = true;
            valor_1 = unidadFind.getId().toString();
        }
        if (filtroContrato != null) {
            parametro_2 = true;
            valor_2 = filtroContrato.getId().toString();
        }
        if (filtroClasificacion != null) {
            parametro_3 = true;
            valor_3 = filtroClasificacion.getId().toString();
        }
        int valor = em.createNativeQuery(QUERY.UPDATE_RUBRO_PARTIDA)
                .setParameter(1, thCargoSeleccionado)
                .setParameter(2, item.getIdRubro().getId())
                .setParameter(3, item.getIdEstructura().getId())
                .setParameter(4, item.getIdPresupuesto().getId())
                .setParameter(5, item.getIdFuente().getId())
                .setParameter(6, item.getPartidaPresupuestaria())
                .setParameter(7, anio)
                .setParameter(8, parametro_1)
                .setParameter(9, valor_1)
                .setParameter(10, parametro_2)
                .setParameter(11, valor_2)
                .setParameter(12, parametro_3)
                .setParameter(13, valor_3)
                .executeUpdate();
    }

    public void getUpdateRubroCuenta(String thCargoSeleccionado, ThCargoRubros item, Short anio, UnidadAdministrativa unidadFind, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion) {
        System.out.println("getUpdateRubroCuenta");
        boolean parametro_1 = false;
        String valor_1 = "";
        boolean parametro_2 = false;
        String valor_2 = "";
        boolean parametro_3 = false;
        String valor_3 = "";
        if (unidadFind != null) {
            parametro_1 = true;
            valor_1 = unidadFind.getId().toString();
        }
        if (filtroContrato != null) {
            parametro_2 = true;
            valor_2 = filtroContrato.getId().toString();
        }
        if (filtroClasificacion != null) {
            parametro_3 = true;
            valor_3 = filtroClasificacion.getId().toString();
        }
        int valor = em.createNativeQuery(QUERY.UPDATE_RUBRO_CUENTA)
                .setParameter(1, thCargoSeleccionado)
                .setParameter(2, item.getIdRubro().getId())
                .setParameter(3, item.getIdCuenta().getId())
                .setParameter(4, anio)
                .setParameter(5, parametro_1)
                .setParameter(6, valor_1)
                .setParameter(7, parametro_2)
                .setParameter(8, valor_2)
                .setParameter(9, parametro_3)
                .setParameter(10, valor_3)
                .executeUpdate();
    }

}
