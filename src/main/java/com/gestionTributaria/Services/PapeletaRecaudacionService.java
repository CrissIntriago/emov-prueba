/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.PapeletaRecaudacion;
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
public class PapeletaRecaudacionService extends AbstractService<PapeletaRecaudacion> {
    
    private static final Logger LOG = Logger.getLogger(PapeletaRecaudacionService.class.getName());
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    
    public PapeletaRecaudacionService() {
        super(PapeletaRecaudacion.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<PapeletaRecaudacion> findPapeletaRecaudacionByfecha(Date fechaRegistro){
        try {
            return (List<PapeletaRecaudacion>) em.createNativeQuery("SELECT * FROM asgard.papeleta_recaudacion p where to_char(p.fecha_registro,'yyyy-MM-dd') = ?1 order by p.id",PapeletaRecaudacion.class)
                    .setParameter(1, sdf2.format(fechaRegistro)).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public List<PapeletaRecaudacion> getPapeletarecaudacion(Date fechaRegistro){
        try {
            if(Utils.isNotEmpty(this.findPapeletaRecaudacionByfecha(fechaRegistro))){
                return this.findPapeletaRecaudacionByfecha(fechaRegistro);
            }else{
                return (List<PapeletaRecaudacion>) em.createNativeQuery("select * from reportes.rec_ingresos_efectivo(?1);", "papeletaRecaudacionMaping")
                        .setParameter(1, sdf2.format(fechaRegistro)).getResultList(); 
            }            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
