/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CupoPresupuesto;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMIEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CupoPresupuestoService extends AbstractService<CupoPresupuesto> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CupoPresupuestoService() {
        super(CupoPresupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CupoPresupuesto> getVerificacion(BigInteger num) {
        List<CupoPresupuesto> result = (List<CupoPresupuesto>) em.createQuery("SELECT cu FROM CupoPresupuesto cu WHERE cu.numTramite= :num ")
                .setParameter("num", num).getResultList();
        return result;
    }

    public BigDecimal getValorActual(CupoPresupuesto c) {
        BigDecimal valor = BigDecimal.ZERO;
        CupoPresupuesto result = (CupoPresupuesto) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE  c.id= :id ").setParameter("id", c.getId()).getResultStream().findFirst().orElse(null);

        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getMontoCupo();
        }
        return valor;
    }

    public BigDecimal getValorCupoIngreso(Short periodo) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(c.presupuestoInicial) FROM ProformaIngreso c INNER JOIN c.item.confId n where c.periodo= :periodo AND n.nivel=2")
                .setParameter("periodo", periodo).getSingleResult();
        return result;
    }

    public BigDecimal getValorCupoAsinado(BigInteger num, Short periodo) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(c.montoCupo) FROM CupoPresupuesto c where c.numTramite= :num AND c.periodo= :periodo")
                .setParameter("num", num).setParameter("periodo", periodo).getSingleResult();
        return result;
    }

    public BigDecimal getCuposPadres(Short periodo, UnidadAdministrativa u, BigInteger num) {
        BigDecimal sumaTotalPadres = BigDecimal.ZERO;
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        List<UnidadAdministrativa> lista = new ArrayList<>();
        List<CupoPresupuesto> listacupos = new ArrayList<>();
        unidad = u;
        List<UnidadAdministrativa> unidadList = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE").getResultList();

        if (verificarCupo(periodo)) {
            sumaTotalPadres = BigDecimal.ZERO;

        } else {
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

                CupoPresupuesto c = new CupoPresupuesto();
                c = (CupoPresupuesto) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.unidadAdministrativa.id= :unidad AND c.numTramite= :num")
                        .setParameter("unidad", item.getId()).setParameter("num", num).getSingleResult();

                sumaTotalPadres = sumaTotalPadres.add(c.getMontoCupo());

            }

        }

        return sumaTotalPadres;
    }

    public BigDecimal verficiarCupoSuplemento(Short periodo, UnidadAdministrativa u) {
        BigDecimal sumaTotalPadres = BigDecimal.ZERO;
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        List<UnidadAdministrativa> lista = new ArrayList<>();
        List<CupoPresupuesto> listacupos = new ArrayList<>();
        unidad = u;
//               
//        List<UnidadAdministrativa> unidadList = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE").getResultList();
//        
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
        BigDecimal resultado = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(a.monto),0) FROM ActividadOperativa a INNER JOIN a.unidadResponsable u WHERE a.periodo= :periodo AND u.id= :unidad  AND a.codigoReforma is null and a.codigoReformaTraspaso is null ")
                .setParameter("periodo", periodo).setParameter("unidad", u.getId()).getSingleResult();

//            if (resultado != null) {
//                sumaTotalPadres = sumaTotalPadres.add(resultado);
//            }
//            
//        }
        return resultado;
    }

    public BigDecimal getValorAsignado(ActividadOperativa a) {
        BigDecimal resultado = (BigDecimal) em.createQuery("SELECT a.monto FROM ActividadOperativa a WHERE a.id= :actividad").setParameter("actividad", a.getId()).getSingleResult();
        return resultado;
    }

    public String estadoPappNomral(Short periodo) {
        String result = "";
        Producto estado = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (estado == null) {
            result = "";
        } else {
            result = estado.getEstadoPapp().getTexto();
        }

        return result;
    }

    public BigDecimal getCupoOtros(String codigo, BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT c.montoCupo FROM CupoPresupuesto c WHERE c.otros= :codigo AND c.numTramite= :num")
                .setParameter("codigo", codigo).setParameter("num", num).getSingleResult();
        return result;
    }

    public BigDecimal getCupoAsignadoDistributivo(Short periodo) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(Sum(vp.valorResultante),0) FROM PartidasDistributivo  p INNER JOIN p.distributivo vp WHERE p.periodo=:periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.partidaAp is not null").setParameter("periodo", periodo).getSingleResult();
        return result;
    }

    public BigDecimal getCupoAsignadoDistributivoAll(Short anio) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT Sum(vd.valorResultante) FROM ValoresDistributivo vd WHERE vd.anio=:anio AND vd.estado = TRUE")
                .setParameter("anio", anio)
                .getSingleResult();
        return result;
    }

    public String getestadoDistributivoNomral(Short periodo) {
        String estado = "";
        try {
            PartidasDistributivo result = (PartidasDistributivo) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.periodo=:periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL").setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
            estado = result.getEstadoPartida().getTexto();
            return estado;
        } catch (NoResultException e) {
            estado = "";
            return estado;
        }

    }

    public BigDecimal getValorActualDisributivo(PartidasDistributivo p) {
        BigDecimal valor = BigDecimal.ZERO;
        PartidasDistributivo result = (PartidasDistributivo) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.id= :id")
                .setParameter("id", p.getId()).getSingleResult();
        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getDistributivo().getValorResultante();
        }
        return valor;
    }

    public BigDecimal getValorAsigandoDistributivoA(Short periodo) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT  COALESCE(SUM(p.distributivoAnexo.monto),0) FROM PartidasDistributivoAnexo p WHERE p.codigoReforma IS NULL and p.codigoReformaTraspaso IS NULL AND p.periodo=: periodo AND p.partidaAp IS NOT NULL")
                .setParameter("periodo", periodo).getSingleResult();
        return valor;
    }

    public BigDecimal getValorAsigandoDistributivoAnexo(Short periodo) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(d.monto) FROM DistributivoAnexo d WHERE d.anio=: anio and d.estado = TRUE")
                .setParameter("anio", periodo).getSingleResult();
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        return valor;
    }

    public BigDecimal getValorActualDA(PartidasDistributivoAnexo p) {
        BigDecimal valor = BigDecimal.ZERO;

        PartidasDistributivoAnexo result = (PartidasDistributivoAnexo) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.id= :id")
                .setParameter("id", p.getId()).getSingleResult();

        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getMonto();
        }
        return valor;
    }

    public String getestadoDistributivoAnexoNomral(Short periodo) {
        String estado = "";

        PartidasDistributivoAnexo result = (PartidasDistributivoAnexo) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo= :periodo "
                + "AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL").setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        if (result == null) {
            estado = "";
        } else {
            estado = result.getEstadoPartida().getTexto();
        }

        return estado;
    }

    public BigDecimal getValorActualPD(ProformaPresupuestoPlanificado p) {
        BigDecimal valor = BigDecimal.ZERO;
        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p from ProformaPresupuestoPlanificado p WHERE p.id= :id")
                .setParameter("id", p.getId()).getSingleResult();

        if (result == null) {
            valor = BigDecimal.ZERO;
        } else {
            valor = result.getValor();
        }

        return valor;
    }

    public BigDecimal getcupoAsignadoPD(Short periodo) {

        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(p.valor) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.periodo= :periodo AND p.codigo='PDI'")
                .setParameter("periodo", periodo).getSingleResult();
        return result;
    }

    public boolean verificarCupo(Short periodo) {
        List<CupoPresupuesto> verificacion = (List<CupoPresupuesto>) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.periodo= :periodo").setParameter("periodo", periodo).getResultList();

        return verificacion.isEmpty();
    }

    public List<Usuarios> listaUsuarios() {
        List<Usuarios> usuarios = (List<Usuarios>) em.createQuery("SELECT u FROM Usuarios u WHERE u.funcionario IS NOT NULL ").getResultList();
        return usuarios;
    }

    public List<UnidadAdministrativa> getUnidadesConCupos(BigInteger num) {
        List<UnidadAdministrativa> unidades = (List<UnidadAdministrativa>) em.createQuery("SELECT c.unidadAdministrativa FROM CupoPresupuesto c WHERE c.numTramite= :num AND c.montoCupo >  0 AND c.unidadAdministrativa IS NOT NULL")
                .setParameter("num", num).getResultList();
        return unidades;
    }

    public Short getListaPeriodos(BigInteger num) {
        CupoPresupuesto cupo = (CupoPresupuesto) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.numTramite= :num").setParameter("num", num).getResultStream().findFirst().orElse(null);
        Short result = cupo.getPeriodo();

        return result;
    }

    public List<String> getListaRoles(UnidadAdministrativa u) {
        List<String> result = (List<String>) em.createNativeQuery("select ci.texto from auth.rol r inner join unidad_administrativa u on r.unidad_administrativa=u.id\n"
                + "	inner join catalogo_item ci on r.categoria=ci.id where u.id=?1").setParameter(1, u.getId()).getResultList();
        return result;
    }

    public String getRolesUsuariosNameUser(UnidadAdministrativa data, String data2) {
        String result = (String) em.createNativeQuery("select u.usuario from auth.usuarios u inner join auth.usuario_rol ur \n"
                + "                ON ur.usuario = u.id INNER join auth.rol r ON r.id = ur.rol\n"
                + "               	inner join catalogo_item ci on r.categoria=ci.id inner join \n"
                + "				unidad_administrativa unidad on r.unidad_administrativa=unidad.id\n"
                + "				where unidad.id=?1 and ci.texto=?2 and u.estado = true ").setParameter(1, data.getId()).setParameter(2, data2).getResultStream().findFirst().orElse(null);

        return result;
    }

    public List<CupoPresupuesto> getEntidadConCupos(BigInteger num) {
        List<CupoPresupuesto> unidades = (List<CupoPresupuesto>) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.numTramite= :num AND c.montoCupo >  0 AND c.unidadAdministrativa IS NOT NULL ")
                .setParameter("num", num).getResultList();
        return unidades;
    }

    public CupoPresupuesto obtenendoCupo(BigInteger num, String user) {
        CupoPresupuesto c = (CupoPresupuesto) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.numTramite= :num AND c.userNameResponsable= :user")
                .setParameter("num", num).setParameter("user", user).getResultStream().findFirst().orElse(null);
        return c;
    }

    public List<CupoPresupuesto> getVerificadorCupos(BigInteger num) {
        List<CupoPresupuesto> result = (List<CupoPresupuesto>) em.createQuery("SELECT c FROM CupoPresupuesto c WHERE c.numTramite=:num  AND c.unidadAdministrativa IS NOT NULL")
                .setParameter("num", num).getResultList();
        return result;
    }

    public List<CupoPresupuesto> getRevisionAsignacion(BigInteger num) {
        List<CupoPresupuesto> lista = em.createNativeQuery("select cupo.* from cupo_presupuesto cupo\n"
                + "                               where cupo.num_tramite=?1 and cupo.username=\n"
                + "                                (select a.username from \n"
                + "                               (select c.username,count(c.*) as conteo \n"
                + "                					from cupo_presupuesto c\n"
                + "                                where c.responsable is not null and c.username is not null\n"
                + "                                 and c.num_tramite=?2\n"
                + "                                group by c.username having count(c.*)>1)  as a)", CupoPresupuesto.class).setParameter(1, num).setParameter(2, num).getResultList();

        return lista;
    }

    public Short getAnio(HistoricoTramites tramite) {
        List<Short> nresult = (List<Short>) em.createQuery(QUERY.PERIODO_CUPO)
                .setParameter(1, new BigInteger(tramite.getNumTramite().toString()))
                .getResultList();
        if (nresult != null && !nresult.isEmpty()) {
            nresult.get(0);
        }
        return 0;
    }
}
