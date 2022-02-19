/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.DetalleRecaudacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleRecaudacionService extends AbstractService<DetalleRecaudacion> {

    private static final Logger LOG = Logger.getLogger(DetalleRecaudacionService.class.getName());
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    public DetalleRecaudacionService() {
        super(DetalleRecaudacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleRecaudacion> findDetalleRecaudacion(Date fechaRegistro) {
        try {
            return (List<DetalleRecaudacion>) em.createNativeQuery("SELECT * FROM asgard.detalle_recaudacion d where to_char(d.fecha_registro,'yyyy-MM-dd') = ?1 order by d.id", DetalleRecaudacion.class)
                    .setParameter(1, sdf2.format(fechaRegistro)).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "findDetalleRecaudacion ERROR ", e);
            return null;
        }
    }

    public List<DetalleRecaudacion> getdetalleRecaudacion(Date fechaRegistro) {
        try {
            if (Utils.isNotEmpty(this.findDetalleRecaudacion(fechaRegistro))) {
                return this.findDetalleRecaudacion(fechaRegistro);
            } else {
                return (List<DetalleRecaudacion>) em.createNativeQuery("select * from reportes.rec_report_recaudador(?1) rp where rp.valor <> 0.00;", "detalleRecaudacionMaping")
                        .setParameter(1, sdf2.format(fechaRegistro)).getResultList();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

}
