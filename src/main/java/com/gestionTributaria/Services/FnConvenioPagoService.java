package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnConvenioPago;

import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.TituloCredito;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.KatalinaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class FnConvenioPagoService extends AbstractService<FnConvenioPago> {

    private static final Logger LOG = Logger.getLogger(FnConvenioPagoService.class.getName());

    private KatalinaService kata;

    private FinaRenLiquidacionService finas;

    private CoaJuicioPredioServices coas;

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnConvenioPagoService() {
        super(FnConvenioPago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FnConvenioPago findbyConvenioPago(FnConvenioPago convenio) {
        List<FnConvenioPago> convenioPago = new ArrayList<>();
        try {
            System.out.println("COVENIO: " + convenio);
            convenioPago = (List<FnConvenioPago>) em.createQuery("select a from FnConvenioPago a where a.id=?1 and a.estado =3").setParameter(1, convenio.getId()).getResultList();
            if (convenioPago.isEmpty()) {
                return null;
            }
            System.out.println("CONVENIO RECUPERADO: " + convenioPago.get(0).getDescripcion());
        } catch (Exception ex) {
            Logger.getLogger(FnConvenioPago.class.getName()).log(Level.SEVERE, "Error al buscar el estado del convenio", ex);
        }
        return convenioPago.get(0);
    }

    /*
     @Schedule(month = "12", hour = "20", dayOfMonth = "31", year = "*", persistent = false)
    public void getHistorialActividadLocales() {
        em.createNativeQuery("call  asgard.update_actividad_locales()").executeUpdate();
    }
     */
    @Schedule(month = "*", hour = "7", dayOfMonth = "*", year = "*", persistent = false)
    public void conveniosCoactivaJu() {
        List<FnConvenioPago> result = new ArrayList<>();
        List<Correo> correos = new ArrayList<>();
        result = (List<FnConvenioPago>) em.createQuery("select a from FnConvenioPago a where a.estado =2");
        for (FnConvenioPago f : result) {
            List<FnConvenioPagoDetalle> detalles = (List<FnConvenioPagoDetalle>) em.createQuery("select a from FnConvenioPagoDetalle a where a.convenio=?1").setParameter(1, f.getId()).getResultList();

            for (FnConvenioPagoDetalle fn : detalles) {
                if (fn.getLiquidacion().getEstadoLiquidacion().getId() == 1) {
                } 
                else {
                    //LocalDate myDate = LocalDate.parse(fn.getFechaMaximaPago());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaA = new Date();
                    Date fechaInicial = dateFormat.parse(fechaA);
                    Date fechaFinal;
                    fechaFinal = dateFormat.parse(fn.getFechaMaximaPago());

                    int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);

                    TituloCredito tc = new TituloCredito();
                    CoaJuicioPredio juicio = null;
                    juicio = (CoaJuicioPredio) em.createQuery("select a from CoaJuicioPredio a where a.liquidacion=?1").setParameter(1, fn.getLiquidacion().getId()).getResultList();
                    tc = (TituloCredito) em.createQuery("select a from TituloCredito a where a.idLiquidacion=?1").setParameter(1, fn.getLiquidacion().getId()).getResultList();
                    if (dias < 0 && tc == null) {
                        Correo co = new Correo();
                        co.setAsunto("");
                        co.setDestinatario("");
                        co.setMensaje("");
                        kata.enviarCorreo(co);
                    }
                    if (tc != null && juicio != null) {
                        Correo co = new Correo();
                        co.setAsunto("");
                        co.setDestinatario("");
                        co.setMensaje("");
                        kata.enviarCorreo(co);
                       
                    }

                }
            }
        }
    }
}
