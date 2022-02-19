/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BeneficiarioSolicitudReservaService extends AbstractService<BeneficiarioSolicitudReserva> {
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public BeneficiarioSolicitudReservaService() {
        super(BeneficiarioSolicitudReserva.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioReservasComprometidas(SolicitudReservaCompromiso reservaCompromiso) {
        List<BeneficiarioSolicitudReserva> resultado = (List<BeneficiarioSolicitudReserva>) em.createQuery("SELECT b FROM BeneficiarioSolicitudReserva b WHERE b.reservaCompromiso=:reservaCompromiso")
                .setParameter("reservaCompromiso", reservaCompromiso)
                .getResultList();
        return resultado;
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioRolGeneral(TipoRol tipoRol, Short periodo) {
        String sql = "SELECT ser.persona,lr.neto_recibir\n"
                + "FROM talento_humano.rol_rubro rr\n"
                + "INNER JOIN talento_humano.valores_roles vr\n"
                + "ON rr.valor_asignacion = vr.id\n"
                + "INNER JOIN talento_humano.liquidacion_rol lr\n"
                + "ON rr.liquidacion_rol = lr.id\n"
                + "INNER JOIN talento_humano.tipo_rol tr\n"
                + "ON lr.tipo_rol = tr.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rdp\n"
                + "ON lr.rol_pago = rdp.id\n"
                + "INNER JOIN talento_humano.servidor ser\n"
                + "ON rdp.servidor = ser.id\n"
                + "WHERE rr.estado=TRUE AND lr.estado=TRUE\n"
                + "AND tr.id=?1 AND vr.partida_ap IS NOT NULL AND vr.periodo=?2\n"
                + "GROUP BY 1,2\n"
                + "ORDER BY 1 ASC";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId()).setParameter(2, periodo);
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioFondoReserva(TipoRol tipoRol) {
        String sql = "SELECT \n"
                + "s.persona, fr.valor_fondos as fondos\n"
                + "FROM talento_humano.acumulacion_fondo_reserva afr\n"
                + "INNER JOIN talento_humano.fondos_reserva fr ON fr.acumulacion_fondos = afr.id\n"
                + "INNER JOIN talento_humano.servidor s ON afr.servidor = s.id\n"
                + "INNER JOIN cliente cli ON s.persona = cli.id\n"
                + "WHERE fr.tipo_rol =?1 AND afr.estado=TRUE AND afr.acumula=false AND fr.estado=true\n"
                + "ORDER BY cli.apellido;";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId());
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioBeneficiosSindicales(TipoRolBeneficios tipoRolBeneficios) {
        String sql = "SELECT \n"
                + "s.persona,\n"
                + "SUM(bs.valor_beneficio)\n"
                + "FROM talento_humano.beneficios_sindicales bs\n"
                + "INNER JOIN talento_humano.servidor s\n"
                + "ON bs.servidor = s.id\n"
                + "WHERE bs.tipo_rol_beneficios=?1 AND bs.estado=TRUE\n"
                + "GROUP BY 1\n"
                + "ORDER BY 1";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId());
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioDecimoTercero(TipoRolBeneficios tipoRolBeneficios, Short periodo) {
        String sql = "SELECT s.persona,\n"
                + "sum(bdt.decimo_tercer_ganado + CASE  WHEN bdt.ajuste is null THEN 0 ELSE bdt.ajuste END) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_tercero bdt\n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr\n"
                + "ON bdt.acumulacion_fondos_reserva = afr.id\n"
                + "INNER JOIN talento_humano.servidor s\n"
                + "ON afr.servidor = s.id\n"
                + "WHERE bdt.estado=TRUE AND bdt.tipo_rol_beneficio=?1\n"
                + "GROUP BY 1\n"
                + "ORDER BY 1";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId());
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiaripDecimoCuarto(TipoRolBeneficios tipoRolBeneficios, Short periodo) {
        String sql = "SELECT s.persona,\n"
                + "sum(bdc.cobrar) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_cuarto bdc\n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr\n"
                + "ON bdc.acumulacion_fondos = afr.id\n"
                + "INNER JOIN talento_humano.servidor s\n"
                + "ON afr.servidor = s.id\n"
                + "WHERE bdc.estado=TRUE AND bdc.tipo_rol_beneficio=?1 AND bdc.activo = true \n"
                + "GROUP BY 1\n"
                + "ORDER BY 1";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId());
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    private List<BeneficiarioSolicitudReserva> listadoBeneficiario(List<Object[]> result) {
        if (result != null) {
            List<BeneficiarioSolicitudReserva> list = new ArrayList<>();
            for (Object[] object : result) {
                BeneficiarioSolicitudReserva data = new BeneficiarioSolicitudReserva();
                data.setTipoBeneficiario(Boolean.FALSE);
                data.setBeneficiario(getClienteById((BigInteger) object[0]));
                data.setValor((BigDecimal) object[1]);
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }
    
    public Cliente getClienteById(BigInteger cliente) {
        Cliente result = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.id=:cliente")
                .setParameter("cliente", cliente.longValue())
                .getSingleResult();
        return result;
    }
    
    public void eliminarBeneficiarios(SolicitudReservaCompromiso r) {
        Query query = em.createNativeQuery("delete from certificacion_presupuestaria_anual.beneficiario_solicitud_reserva rc where rc.reserva_compromiso=?1");
        query.setParameter(1, r.getId());
        int a = query.executeUpdate();
        System.out.println(a);
    }
    
    public List<BeneficiarioSolicitudReserva> getListaBeneficiarioSolicitudReservas(SolicitudReservaCompromiso r) {
        List<BeneficiarioSolicitudReserva> lista = (List<BeneficiarioSolicitudReserva>) em.createQuery("SELECT b FROM BeneficiarioSolicitudReserva b WHERE b.reservaCompromiso= :reserva")
                .setParameter("reserva", r).getResultList();
        return lista;
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioRolHorasExtras(TipoRol tipoRol, Short periodo) {
        String sql = "SELECT ser.persona,rhe.neto_recibir\n"
                + "FROM talento_humano.rol_horas_extras_suplementarias rhe\n"
                + "INNER JOIN talento_humano.roles_de_pago rdp ON rhe.servidor_partida = rdp.id\n"
                + "INNER JOIN talento_humano.servidor ser ON rdp.servidor=ser.id\n"
                + "WHERE rhe.estado = TRUE AND rhe.tipo_rol=?1 AND rdp.periodo=?2";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId()).setParameter(2, periodo);
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
    public List<BeneficiarioSolicitudReserva> getBeneficiarioProvisionales(TipoRol tipoRol, Short periodo, String codigo) {
        String sql = "SELECT ser.persona, fr.valor_fondos FROM talento_humano.fondos_reserva fr\n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr on fr.acumulacion_fondos = afr.id \n"
                + "INNER JOIN public.catalogo_item ci ON afr.tipo_acumulacion = ci.id\n"
                + "INNER JOIN talento_humano.servidor ser ON afr.servidor = ser.id\n"
                + "WHERE fr.tipo_rol = ?1 AND ci.codigo= ?2 AND fr.estado = true AND afr.estado=true AND afr.acumula=TRUE;";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId()).setParameter(2, codigo);
        List<Object[]> result = query.getResultList();
        return listadoBeneficiario(result);
    }
    
  
    
}
