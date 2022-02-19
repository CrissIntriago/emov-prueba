/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.RetencionesImpuestoRenta;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TablaImpuestoRenta;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.junit.Test;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RetencionImpuestoRentaService extends AbstractService<RetencionesImpuestoRenta> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private UserSession userSession;
    @Inject
    private ValoresDistributivoService valoresservice;
    @Inject
    private TablaImpuestoService tablaservice;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private GastoPersonalService gastoService;

    public RetencionImpuestoRentaService() {
        super(RetencionesImpuestoRenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<RetencionesImpuestoRenta> listaretencion(TipoRol rol) {
        if (rol == null) {
            return null;
        }
        if (rol.getId() == null) {
            return null;
        }
        List<RetencionesImpuestoRenta> result = (List<RetencionesImpuestoRenta>) em.createQuery("SELECT p FROM RetencionesImpuestoRenta p WHERE p.estado=TRUE AND p.tipoRol = ?1 AND p.periodo = ?2")
                .setParameter(1, rol).setParameter(2, rol.getAnio())
                .getResultList();
        return result;
    }

    public RetencionesImpuestoRenta getRetencionMensual(TipoRol rol, Servidor serv) {
        try {
            Query query = em.createQuery("SELECT r FROM RetencionesImpuestoRenta r where r.tipoRol = ?1 AND r.gastoPersonal.servidorPublico = ?2 AND r.estado = TRUE")
                    .setParameter(1, rol).setParameter(2, serv);
            RetencionesImpuestoRenta result = (RetencionesImpuestoRenta) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<RetencionesImpuestoRenta> retencionMensualRubro(Servidor serv, String cod, TipoRol rol) {
        try {
            Query query = em.createNativeQuery("SELECT * FROM talento_humano.retenciones_impuesto_renta rt\n"
                    + "INNER JOIN talento_humano.gasto_personal gp ON gp.id = rt.gasto_personal\n"
                    + "INNER JOIN conf.parametros_talento_humano ph on ph.id = rt.valor_parametrizado\n"
                    + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                    + "where rt.estado = true and gp.servidor_publico = ?1 AND ci.codigo = ?2 and rt.cuota_mensual > 0\n"
                    + "and rt.tipo_rol = ?3", RetencionesImpuestoRenta.class)
                    .setParameter(1, serv.getId()).setParameter(2, cod).setParameter(3, rol.getId());
            List<RetencionesImpuestoRenta> result = (List<RetencionesImpuestoRenta>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public RetencionesImpuestoRenta getGenerarImpRenta(Servidor serv, TipoRol rolSeleccionado) {
        GastoPersonal g = gastoService.existeRegistro(serv, rolSeleccionado.getAnio());
        if (g != null) {
            RetencionesImpuestoRenta retencion = new RetencionesImpuestoRenta();
            retencion.setGastoPersonal(new GastoPersonal());
            retencion.setValorParametrizado(new ParametrosTalentoHumano());
            retencion.setTipoRol(new TipoRol());
            retencion.setGastoPersonal(g);
            retencion.setPeriodo(rolSeleccionado.getAnio());
            retencion.setValorParametrizado(parametroService.valorParametroTipo("IMP_RENTA"));
            retencion.setFechaCreacion(new Date());
            retencion.setUsuarioCreacion(userSession.getNameUser());
            retencion.setAporteindividual(getAportePAT(g));
            retencion.setIngresosBrutos(getIngresoBru(g, retencion));
            retencion.setIngresosNetos(getingresoNe(g, retencion));
            retencion.setTipoRol(rolSeleccionado);
            BigDecimal exceso = getExceso(retencion);
            BigDecimal valorFraccion = getFraccion(exceso, retencion, rolSeleccionado.getAnio());
            retencion.setImpuestoRentaAnual(getimpuesto(valorFraccion, retencion, rolSeleccionado.getAnio()));
            retencion.setCuotaMensual(getCuotaMensual(retencion));
            if (diasService.diaLaborado(rolSeleccionado, g.getServidorPublico()) != null) {
                if (TalentoHumano.validarFechaInicio(g.getFechaEntrega(), rolSeleccionado)) {
                    if (g.getServidorPublico().getFechaSalida() == null) {
                        if (TalentoHumano.validarFechaInicio(g.getServidorPublico().getFechaIngreso(), rolSeleccionado)) {
                            retencion = this.create(retencion);
                        }
                    }
                    if (g.getServidorPublico().getFechaIngreso() != null && g.getServidorPublico().getFechaSalida() != null) {
                        if (TalentoHumano.validarFechaInicio(g.getServidorPublico().getFechaIngreso(), rolSeleccionado) && TalentoHumano.validarFechaFin(g.getServidorPublico().getFechaSalida(), rolSeleccionado)) {
                            retencion = this.create(retencion);
                        }
                    }
                }
                em.persist(retencion);
            }
            return retencion;
        }
        return null;
    }

    @Transactional
    @Test
    public void eliminarList(List<RetencionesImpuestoRenta> delete) {
        if (!delete.isEmpty()) {
            for (RetencionesImpuestoRenta f : delete) {
                f.setEstado(Boolean.FALSE);
                this.edit(f);
            }
        }
    }

    @Transactional
    @Test
    public void addList(List<GastoPersonal> lista, TipoRol rolSeleccionado) {
        int i = 0, BATCH_SIZE = 100;
        if (!lista.isEmpty()) {
            for (GastoPersonal g : lista) {
                if (i > 0 && i % BATCH_SIZE == 0) {
                    em.flush();
                    em.clear();
                }
                RetencionesImpuestoRenta retencion = new RetencionesImpuestoRenta();
                retencion.setGastoPersonal(new GastoPersonal());
                retencion.setValorParametrizado(new ParametrosTalentoHumano());
                retencion.setTipoRol(new TipoRol());
                retencion.setGastoPersonal(g);
                retencion.setPeriodo(rolSeleccionado.getAnio());
                retencion.setValorParametrizado(parametroService.valorParametroTipo("IMP_RENTA"));
                retencion.setFechaCreacion(new Date());
                retencion.setUsuarioCreacion(userSession.getNameUser());
                retencion.setAporteindividual(getAportePAT(g));
                retencion.setIngresosBrutos(getIngresoBru(g, retencion));
                retencion.setIngresosNetos(getingresoNe(g, retencion));
                retencion.setTipoRol(rolSeleccionado);
                BigDecimal exceso = getExceso(retencion);
                BigDecimal valorFraccion = getFraccion(exceso, retencion, rolSeleccionado.getAnio());
                retencion.setImpuestoRentaAnual(getimpuesto(valorFraccion, retencion, rolSeleccionado.getAnio()));
                retencion.setCuotaMensual(getCuotaMensual(retencion));
                if (diasService.diaLaborado(rolSeleccionado, g.getServidorPublico()) != null) {
                    if (TalentoHumano.validarFechaInicio(g.getFechaEntrega(), rolSeleccionado)) {
                        if (g.getServidorPublico().getFechaSalida() == null) {
                            if (TalentoHumano.validarFechaInicio(g.getServidorPublico().getFechaIngreso(), rolSeleccionado)) {
                                retencion = this.create(retencion);
                            }
                        }
                        if (g.getServidorPublico().getFechaIngreso() != null && g.getServidorPublico().getFechaSalida() != null) {
                            if (TalentoHumano.validarFechaInicio(g.getServidorPublico().getFechaIngreso(), rolSeleccionado) && TalentoHumano.validarFechaFin(g.getServidorPublico().getFechaSalida(), rolSeleccionado)) {
                                retencion = this.create(retencion);
                            }
                        }
                    }
                    em.persist(retencion);
                    i++;
                }
            }
        }
    }

    @Transactional
    @Test
    public void actualizarValores(TipoRol rolSeleccionado) {
        List<RetencionesImpuestoRenta> listaRetenciones = this.listaretencion(rolSeleccionado);
        if (!listaRetenciones.isEmpty()) {
            for (RetencionesImpuestoRenta r : listaRetenciones) {
                BigDecimal exceso = BigDecimal.ZERO;
                BigDecimal valorFraccion = BigDecimal.ZERO;
                r.setAporteindividual(getAportePAT(r.getGastoPersonal()));
                r.setIngresosBrutos(getIngresoBru(r.getGastoPersonal(), r));
                r.setIngresosNetos(getingresoNe(r.getGastoPersonal(), r));
                exceso = getExceso(r);
                valorFraccion = getFraccion(exceso, r, rolSeleccionado.getAnio());
                r.setImpuestoRentaAnual(getimpuesto(valorFraccion, r, rolSeleccionado.getAnio()));
                r.setCuotaMensual(getCuotaMensual(r));
                this.edit(r);
            }
        }

    }

    public BigDecimal getAportePAT(GastoPersonal g) {
        BigDecimal porcentajeLosep = valoresservice.valorParametrizado("APOR_IND_LOEP");
        double aux = (g.getTotalIngreso().doubleValue() * porcentajeLosep.doubleValue()) / 100;
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getIngresoBru(GastoPersonal g, RetencionesImpuestoRenta retencion) {
        double aux = g.getTotalIngreso().doubleValue() - retencion.getAporteindividual().doubleValue();
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getingresoNe(GastoPersonal g, RetencionesImpuestoRenta retencion) {
        double aux = (retencion.getIngresosBrutos().doubleValue() - g.getTotalGasto().doubleValue());
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);

    }

    public BigDecimal getExceso(RetencionesImpuestoRenta retencion) {
        TablaImpuestoRenta tabla = tablaservice.getorcentaje(retencion.getTipoRol().getAnio(), retencion.getIngresosNetos().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        double aux = retencion.getIngresosNetos().doubleValue() - tabla.getFraccionBasica().doubleValue();
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getFraccion(BigDecimal exceso, RetencionesImpuestoRenta retencion, Short periodo) {
        TablaImpuestoRenta tabla = tablaservice.getorcentaje(periodo, retencion.getIngresosNetos());
        double aux = (exceso.doubleValue() * tabla.getPorcentajeFraccionExcedente()) / 100;
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getimpuesto(BigDecimal valorFraccion, RetencionesImpuestoRenta retencion, Short periodo) {
        TablaImpuestoRenta tabla = tablaservice.getorcentaje(periodo, retencion.getIngresosNetos());
        double aux = tabla.getImpuestoFraccionBasica().doubleValue() + valorFraccion.doubleValue();
        return new BigDecimal(aux).setScale(2, BigDecimal.ROUND_HALF_EVEN);

    }

    public BigDecimal getCuotaMensual(RetencionesImpuestoRenta retencion) {
        int mes, aux;
        mes = Utils.getMes(retencion.getGastoPersonal().getFechaEntrega()) - 1;
        aux = 12 - mes;
        double resul = retencion.getImpuestoRentaAnual().doubleValue() / aux;
        return new BigDecimal(resul).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
