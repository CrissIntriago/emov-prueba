/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services.biotime;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.view.biotime.Marcacion;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.model.MarcacionModel;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class MarcacionService extends AbstractService<Marcacion> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_BIOTIME)
    private EntityManager em;

    public MarcacionService() {
        super(Marcacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Marcacion getMarcacionBiometrico(Date fecha, DiasLaborado d, String evento) {
        try {
            String sql = "";
            if (evento.equals("0")) {
                sql = " and m.time_ = (select min(mc.time_) from marcacion mc WHERE mc.punch_state = ?4) \n";
            } else {
                sql = " and m.time_ = (select max(mc.time_) from marcacion mc WHERE mc.punch_state = ?4) \n";
            }
            Query query = em.createNativeQuery("select *\n"
                    + "from marcacion m\n"
                    + "where m.date_ = ?1\n"
                    + "and m.emp_code = ?2\n"
                    + "and m.terminal_alias = ?3\n"
                    + sql
                    + "order by m.id", Marcacion.class)
                    .setParameter(1, fecha)
                    .setParameter(2, d.getServidor().getCodBiometrico())
                    .setParameter(3, d.getServidor().getPuntoMarcacion())
                    .setParameter(4, evento);
            Marcacion result = (Marcacion) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public MarcacionModel marcacionIngresoSalida(Date fecha, DiasLaborado obj, String evento1, String evento2, String sql) {
        try {
            Query query = em.createNativeQuery(sql, "detalleMarcacionLaboralMaping")
                    .setParameter(1, fecha)
                    .setParameter(2, obj.getServidor().getCodBiometrico())
                    .setParameter(3, obj.getServidor().getPuntoMarcacion().toUpperCase())
                    .setParameter(4, evento1)
                    .setParameter(5, evento2);
            MarcacionModel result = (MarcacionModel) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public MarcacionModel marcacionIngresoSalida(Date fecha, HoraExtraSuplementaria obj, String evento1, String evento2, String sql) {
        try {
            Query query = em.createNativeQuery(sql, "detalleMarcacionLaboralMaping")
                    .setParameter(1, fecha)
                    .setParameter(2, obj.getServidor().getCodBiometrico())
                    .setParameter(3, obj.getServidor().getPuntoMarcacion())
                    .setParameter(4, evento1)
                    .setParameter(5, evento2);
            MarcacionModel result = (MarcacionModel) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public MarcacionModel marcacionIngresoSalidaLaborados(Date fecha, Object obj, String evento1, String evento2) {
        String sql = "WITH consulta as (\n"
                + "select m.emp_code, m.date_ as date,m.time_ as hora_ingreso,\n"
                + "(select m.time_ from marcacion m\n"
                + "WHERE\n"
                + "m.date_ = ?1\n"
                + "and m.emp_code = ?2\n"
                + "and m.punch_state = ?5\n"
                + "and m.time_ = (select max(mc.time_) \n"
                + "			   from marcacion mc \n"
                + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                + "and (upper(m.terminal_alias) = ?3 or upper(m.area_alias)= ?3)) as hora_salida,\n"
                + "	\n"
                + "	(select m.time_ from marcacion m\n"
                + "WHERE\n"
                + "m.date_ = ?1\n"
                + "and m.emp_code = ?2\n"
                + "and m.punch_state = '2'\n"
                + "and m.time_ = (select max(mc.time_) \n"
                + "			   from marcacion mc \n"
                + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                + "and (upper(m.terminal_alias) = ?3 or upper(m.area_alias)= ?3)) as ingreso_descanso,\n"
                + "	\n"
                + "	(select m.time_ from marcacion m\n"
                + "WHERE\n"
                + "m.date_ = ?1\n"
                + "and m.emp_code = ?2\n"
                + "and m.punch_state = '3'\n"
                + "and m.time_ = (select max(mc.time_) \n"
                + "			   from marcacion mc \n"
                + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                + "and (upper(m.terminal_alias) = ?3 or upper(m.area_alias)= ?3)) as salida_descanso\n"
                + "	\n"
                + "from marcacion m\n"
                + "WHERE\n"
                + "m.date_ = ?1\n"
                + "and m.emp_code = ?2\n"
                + "and m.punch_state = ?4\n"
                + "and (upper(m.terminal_alias) = ?3 or upper(m.area_alias)= ?3)\n"
                + "and m.time_ = (select min(mc.time_) \n"
                + "			   from marcacion mc \n"
                + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                + "			   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                + "order by m.date_ asc)\n"
                + "select\n"
                + "emp_code, date, hora_ingreso,hora_salida,ingreso_descanso,salida_descanso,\n"
                + "CAST((hora_salida - hora_ingreso) as time without time zone) as total_hora,\n"
                + "CAST((salida_descanso - ingreso_descanso) as time without time zone) as total_hora_decanso,\n"
                + "CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)) as time without time zone) as horas_laboras\n"
                + "from consulta";
        if (obj instanceof DiasLaborado) {
            DiasLaborado object = (DiasLaborado) obj;
            return marcacionIngresoSalida(fecha, object, evento1, evento2, sql);
        }
        if (obj instanceof HoraExtraSuplementaria) {
            HoraExtraSuplementaria object = (HoraExtraSuplementaria) obj;
            return marcacionIngresoSalida(fecha, object, evento1, evento2, sql);
        }
        return null;
    }

    public MarcacionModel marcacionHorasExtras(Date fecha, HoraExtraSuplementaria h, String evento1, String evento2, Boolean var) {
        try {
            String sql = "";
            if (!var) {
//                sql = "CAST(((hora_salida - hora_ingreso)-'" + TalentoHumano.HORAS_DESCANSO + "') as time without time zone) as horas_extras,\n"
//                        + "CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)-'" + TalentoHumano.HORAS_DESCANSO + "') as time without time zone) as horas_extras_desc\n";
                sql = "case when (hora_salida - hora_ingreso)>'" + TalentoHumano.HORAS_DESCANSO + "' then CAST(((hora_salida - hora_ingreso)-'" + TalentoHumano.HORAS_DESCANSO + "') as time without time zone)\n"
                        + "else null\n"
                        + "end as horas_extras,\n"
                        + "case when ((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso))>'" + TalentoHumano.HORAS_DESCANSO + "' then CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)-'" + TalentoHumano.HORAS_DESCANSO + "') as time without time zone)\n"
                        + "else null\n"
                        + "end as horas_extras_desc\n";
            } else {
//                sql = "CAST(((hora_salida - hora_ingreso)-'" + TalentoHumano.HORAS_SIN_DESCANSO + "') as time without time zone) as horas_extras,\n"
//                        + "CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)-'" + TalentoHumano.HORAS_SIN_DESCANSO + "') as time without time zone) as horas_extras_desc\n";
                sql = "case when (hora_salida - hora_ingreso)>'" + TalentoHumano.HORAS_SIN_DESCANSO + "' then CAST(((hora_salida - hora_ingreso)-'" + TalentoHumano.HORAS_SIN_DESCANSO + "') as time without time zone)\n"
                        + "else null\n"
                        + "end as horas_extras,\n"
                        + "case when ((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso))>'" + TalentoHumano.HORAS_SIN_DESCANSO + "' then CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)-'" + TalentoHumano.HORAS_SIN_DESCANSO + "') as time without time zone)\n"
                        + "else null\n"
                        + "end as horas_extras_desc\n";
            }

            Query query = em.createNativeQuery("WITH consulta as (\n"
                    + "select m.emp_code, m.date_ as date,m.time_ as hora_ingreso,\n"
                    + "(select m.time_ from marcacion m\n"
                    + "WHERE\n"
                    + "m.date_ = ?1\n"
                    + "and m.emp_code = ?2\n"
                    + "and m.punch_state = ?5\n"
                    + "and m.time_ = (select max(mc.time_) \n"
                    + "			   from marcacion mc \n"
                    + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                    + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                    + "and m.terminal_alias = ?3) as hora_salida,\n"
                    + "	\n"
                    + "	(select m.time_ from marcacion m\n"
                    + "WHERE\n"
                    + "m.date_ = ?1\n"
                    + "and m.emp_code = ?2\n"
                    + "and m.punch_state = '2'\n"
                    + "and m.time_ = (select max(mc.time_) \n"
                    + "			   from marcacion mc \n"
                    + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                    + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                    + "and m.terminal_alias = ?3) as ingreso_descanso,\n"
                    + "	\n"
                    + "	(select m.time_ from marcacion m\n"
                    + "WHERE\n"
                    + "m.date_ = ?1\n"
                    + "and m.emp_code = ?2\n"
                    + "and m.punch_state = '3'\n"
                    + "and m.time_ = (select max(mc.time_) \n"
                    + "			   from marcacion mc \n"
                    + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                    + " 		   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                    + "and m.terminal_alias = ?3) as salida_descanso\n"
                    + "	\n"
                    + "from marcacion m\n"
                    + "WHERE\n"
                    + "m.date_ = ?1\n"
                    + "and m.emp_code = ?2\n"
                    + "and m.punch_state = ?4\n"
                    + "and m.terminal_alias = ?3\n"
                    + "and m.time_ = (select min(mc.time_) \n"
                    + "			   from marcacion mc \n"
                    + "			   WHERE mc.punch_state = m.punch_state and mc.date_ = m.date_ \n"
                    + "			   AND mc.emp_code = m.emp_code and mc.terminal_alias = m.terminal_alias)\n"
                    + "order by m.date_ asc)\n"
                    + "select\n"
                    + "emp_code, date, hora_ingreso,hora_salida,ingreso_descanso,salida_descanso,\n"
                    + "CAST((hora_salida - hora_ingreso) as time without time zone) as total_hora,\n"
                    + "CAST((salida_descanso - ingreso_descanso) as time without time zone) as total_hora_decanso,\n"
                    + "CAST(((hora_salida - hora_ingreso)-(salida_descanso - ingreso_descanso)) as time without time zone) as horas_laboras,\n"
                    + sql
                    + "from consulta", "detalleMarcacionMaping")
                    .setParameter(1, fecha)
                    .setParameter(2, h.getServidor().getCodBiometrico())
                    .setParameter(3, h.getServidor().getPuntoMarcacion())
                    .setParameter(4, evento1)
                    .setParameter(5, evento2);
            MarcacionModel result = (MarcacionModel) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
