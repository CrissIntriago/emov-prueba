/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.models;

/**
 *
 * @author Administrator
 */
public class QUERY {

    //Busca los tramites por usuario
    public static final String FIND_TRAMITE_USER = "SELECT t FROM Tramites t WHERE t.periodo=:periodo AND (t.participants like :user OR t.usercre like :user) ORDER BY t.numTramite DESC";

    //Lista los tipos de tramites desplegados
    public static final String LIST_TIPO_TRAMITE = "SELECT tt FROM TipoTramite tt WHERE tt.estado=TRUE ORDER BY tt.descripcion ASC";

    //Verifica si hay un nivel repetido
    public static final String FIND_NIVEL = "SELECT count(pc) FROM PlanCuentas pc WHERE pc.nivel = ?1 AND pc.codigo = ?2 AND pc.estado = true AND pc.programatico = ?3";

    //Retorna el ultimo nivel de la cuenta contable
    public static final String LAST_NIVEL = "SELECT max(pc.nivel) FROM PlanCuentas pc WHERE pc.codigo = ?1 AND pc.estado = true AND pc.programatico = ?2";

    //Retorna la lista de niveles
    public static final String LIST_NIVELES = "SELECT pc FROM  PlanCuentas pc WHERE pc.codigo = ?1 AND pc.estado = true AND pc.programatico = ?2 ORDER BY pc.nivel ASC";

    //Retorna las configuraciones del siguiente nivel
    public static final String OBJ_NIVEL = "SELECT pc FROM PlanCuentas pc WHERE pc.estado= true AND pc.codigo=?1 AND pc.nivel=?2 AND pc.programatico = ?3";

    //Retorna el codigo maximo de la cuenta contable registrada
    public static final String NEXT_CODE_CC = "SELECT MAX(cc.codigo) FROM ContCuentas cc WHERE cc.confId = ?1";

    //Retorna el codigo maximo de la cuenta contable registrada
    public static final String NEXT_CODE_CC_PADRE = "SELECT MAX(cc.codigo) FROM ContCuentas cc WHERE cc.confId = ?1 AND cc.padre=?2";

    //Retorna true si el codigo ya se encuentra registrado
    public static final String FIND_CODE_TH_RL = "SELECT rl FROM ThRegimenLaboral rl WHERE rl.codigo = ?1 AND rl.estado = true";

    //Retorna el listado de los hijos de objeto seleccionado
    public static final String FIND_LIST_TH_RL = "SELECT rl FROM ThRegimenLaboral rl WHERE rl.padre= ?1 AND rl.estado = true";

    //Retorna los catalogo item de acuerdo al codigo del catalogo
    public static final String FIND_CATALOGO = "SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo = ?1 ORDER BY ci.texto ASC";

    //Retorna el catalogo item de acuerdo al codigo y la descripcion
    //public static final String FIND_CATALOGO_DIARIO = "SELECT * FROM catalogo_item ci INNER JOIN catalogo c ON c.id = ci.catalogo WHERE c.codigo = ?1 and TRIM(UPPER(ci.texto)) = ?2";
    public static final String FIND_CATALOGO_DIARIO = "SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo = ?1 and ci.texto = ?2";

    //Retorna la cantidad de cargos de estado true
    public static final String COUNT_CARGO = "SELECT CAST(COALESCE(REPLACE(MAX(thc.codigo), ?1,''),'0') AS bigint) FROM talento_humano.th_cargo thc WHERE thc.estado = TRUE";

    //Retorna el listado de los cargos y rubros
    public static final String FIND_CARGO_RUBRO = "SELECT rb FROM ThCargoRubros rb INNER JOIN rb.idRubro r WHERE rb.estado = TRUE AND rb.idCargo = ?1 ORDER BY r.ingreso DESC";

    //Retorna el listado de cargos totales de estado true
    public static final String FIND_CARGO_PARAMETRO_1 = "SELECT c FROM ThCargo c WHERE c.estado = true";

    //Retorna el listado de cargos totales de estado true y activos
    public static final String FIND_CARGO_PARAMETRO_2 = "SELECT c FROM ThCargo c WHERE c.estado = true AND c.activo = true";

    //Retornar el listado de rubros por cargo y periodo
    public static final String FIND_CARGO_RUBRO_ANIO = "SELECT rb FROM ThCargoRubros rb INNER JOIN rb.idRubro r WHERE rb.estado = true AND rb.idCargo = ?1 AND rb.periodo = ?2 ORDER BY r.ingreso DESC";

    //Retorna el listado de cuentas contables de acuerdo al los parametros de activo y movimiento
    public static final String FIND_CUENTA_ACTIVO_MOVIMIENTO = "SELECT ct FROM ContCuentas ct WHERE ct.activo = ?1 AND ct.movimiento = ?2 ORDER BY ct.codigo ASC";

    //Retorna el listado de los rubros
    public static final String FIND_RUBROS_PARAMETROS = "SELECT DISTINCT(rb.*) FROM talento_humano.th_cargo_rubros tcr\n"
            + "INNER JOIN talento_humano.th_cargo cg ON tcr.id_cargo = cg.id\n"
            + "INNER JOIN talento_humano.th_rubro rb ON tcr.id_rubro = rb.id\n"
            + "WHERE tcr.estado AND cg.estado AND tcr.periodo=?5 AND rb.ingreso = ?6\n"
            + "AND (CASE WHEN true=?1 THEN cg.activo ELSE true END)\n"
            + "AND (CASE WHEN true=?2 THEN cg.nombre_cargo in (?4) ELSE true END) \n"
            + "AND (CASE WHEN true=?3 THEN tcr.id_cuenta is null ELSE true end)";

    //Retorna el listado de los cargos de acuerdo al estado
    public static final String FIND_CARGO_ESTADO = "SELECT DISTINCT(cg.nombre_cargo) FROM talento_humano.th_cargo cg  WHERE cg.estado= true AND (CASE WHEN true=?1 THEN cg.activo ELSE true END) ORDER BY 1 ASC";

    //Actualiza los parametros
    public static final String UPDATE_RUBRO_PARTIDA = "UPDATE\n"
            + "	talento_humano.th_cargo_rubros\n"
            + "SET\n"
            + "	id_estructura = ?3,\n"
            + "	id_presupuesto = ?4,\n"
            + "	id_fuente = ?5,\n"
            + "	partida_presupuestaria = ?6\n"
            + "FROM talento_humano.th_cargo\n"
            + "WHERE id_rubro = ?2\n"
            + "AND th_cargo_rubros.id_cargo = th_cargo.id\n"
            + "AND th_cargo.estado = true AND th_cargo.activo = true\n"
            + "AND th_cargo.nombre_cargo = ?1 AND th_cargo_rubros.estado = true\n"
            + "AND th_cargo_rubros.periodo = ?7\n"
            + "AND (CASE WHEN true = ?8 THEN th_cargo.id_unidad = CAST(CONCAT(?9,NULL) AS bigint) ELSE true END)\n"
            + "AND (CASE WHEN true = ?10 THEN th_cargo.id_contrato = CAST(CONCAT(?11,NULL) AS bigint) ELSE true END)\n"
            + "AND (CASE WHEN true = ?12 THEN th_cargo.id_catalogo_item = CAST(CONCAT(?13,NULL) AS bigint) ELSE true END)";

    //Actualiza los rubros con la configuracion de partida presupuestaria
    public static final String UPDATE_RUBRO_CUENTA = "UPDATE\n"
            + "	talento_humano.th_cargo_rubros\n"
            + "SET\n"
            + "	id_cuenta = ?3\n"
            + "FROM talento_humano.th_cargo\n"
            + "WHERE id_rubro = ?2\n"
            + "AND th_cargo_rubros.id_cargo = th_cargo.id\n"
            + "AND th_cargo.estado = true AND th_cargo.activo = true\n"
            + "AND th_cargo.nombre_cargo = ?1 AND th_cargo_rubros.estado = true\n"
            + "AND th_cargo_rubros.periodo = ?4\n"
            + "AND (CASE WHEN true = ?5 THEN th_cargo.id_unidad = CAST(CONCAT(?6,NULL) AS bigint) ELSE true END)\n"
            + "AND (CASE WHEN true = ?7 THEN th_cargo.id_contrato = CAST(CONCAT(?8,NULL) AS bigint) ELSE true END)\n"
            + "AND (CASE WHEN true = ?9 THEN th_cargo.id_catalogo_item = CAST(CONCAT(?10,NULL) AS bigint) ELSE true END)";

    //Retorna las relaciones que tiene la cuenta contable enviada como parametro
    public static final String FIND_CUENTA_PARTIDA = "SELECT ccp FROM ContCuentaPartida ccp WHERE ccp.idCuenta=?1";

    //Retorna el listado de presupusto por tipo de presupuesto
    public static final String FIND_PRESUPUESTO_TIPO = "SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.ingreso=?1 AND cp.movimiento=true ORDER BY cp.codigo ASC";

    //Retorna el codigo de la partida de la cuenta por cobrar
    public static final String FIND_CUENTA_PARTIDA_COBRAR_PAGAR = "SELECT pt.codigo FROM ContCuentaPartida ccp INNER JOIN ccp.idPartida1 pt WHERE ccp.idCuenta=?1";

    //Retorna las partidas de acuerdo a los niveles
    public static final String FIND_NIVEL_PRESUPUESTO = "SELECT cp FROM PresCatalogoPresupuestario cp INNER JOIN cp.confId n WHERE n.nivel = ?1 ORDER BY cp.codigo ASC";

    //Retorna el listado del catalogo presupuesto de acuerdo al codigo concatenado
    public static final String FIND_CODIGO_LIKE_PRESUPUESTO = "SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.codigo like ?1 AND cp.movimiento=true ORDER BY cp.codigo ASC";

    //Elimina las relaciones con presupuesto dependiendo de la cuenta contable
    public static final String DELETE_CUENTA_PARTIDA = "DELETE FROM ContCuentaPartida ccp WHERE ccp.idCuenta=?1";

    //Retorna el listado de las cuentas contables que tenga como relación partidas que inicien deacuerdo al codigo del tipo del cargo
    public static final String FIND_CUENTAS_CODE_PARTIDA = "SELECT cc FROM ContCuentaPartida ccp INNER JOIN ccp.idCuenta cc LEFT JOIN ccp.idPartida1 cp1 LEFT JOIN ccp.idPartida2 cp2 WHERE cc.activo=true AND (cp1.codigo LIKE ?1 OR cp2.codigo LIKE ?1) AND cc.movimiento=true GROUP BY cc.id ORDER BY cc.codigo ASC";

    //Retorna el listado de las partidas presupuestaria que inicien con el codigo
    public static final String FIND_CARGO_RUBRO_ANIO_INGRESOS = "SELECT tcr FROM ThCargoRubros tcr INNER JOIN tcr.idRubro rb WHERE tcr.estado=true AND tcr.periodo=?2 AND rb.ingreso = true AND tcr.idCargo=?1";

    //Retorna el listado de las cuentas que tengan ese codigo
    public static final String EXITS_CUENTA_CONTABLE = "SELECT cc FROM ContCuentas cc WHERE cc.codigo =?1";

    //Retorna el listado del item programatico que tengan ese codigo 
    public static final String EXITS_PROGRAMATICO = "SELECT ppp FROM PresPlanProgramatico ppp WHERE ppp.codigo= ?1";

    //Retorna el codigo maximo del item programatico registrada
    public static final String NEXT_CODE_CC_PROGRAMATICO = "SELECT MAX(ppp.codigo) FROM PresPlanProgramatico ppp WHERE ppp.confId = ?1";

    //Retorna el codigo maximo del item programatico registrada
    public static final String NEXT_CODE_CC_PADRE_PROGRAMATICO = "SELECT MAX(ppp.codigo) FROM PresPlanProgramatico ppp WHERE ppp.confId = ?1 AND ppp.padre=?2";

    //Retorna el listado del catalogo presupuestario que tengan ese codigo 
    public static final String EXITS_ITEM_PRESUPUESTARIO = "SELECT ppp FROM PresCatalogoPresupuestario ppp WHERE ppp.codigo= ?1";

    //Retorna el codigo maximo del catalogo presupuestario registrada
    public static final String NEXT_CODE_PRESUPUESTARIO = "SELECT MAX(ppp.codigo) FROM PresCatalogoPresupuestario ppp WHERE ppp.confId = ?1";

    //Retorna el codigo maximo del catalogo presupuestario registrada
    public static final String NEXT_CODE_PRESUPUESTARIO_PADRE = "SELECT MAX(ppp.codigo) FROM PresCatalogoPresupuestario ppp WHERE ppp.confId = ?1 AND ppp.padre=?2";

    //Retorna el listado de servidores que tenga el numero de cedula
    public static final String FIND_SERVIDOR_IDENTIFICACION = "SELECT s FROM Servidor s INNER JOIN s.persona p WHERE p.estado= true AND p.identificacion = ?1 AND s.estado=true";

    //Retorna el listado de los hijos de la cuenta contable
    public static final String FIND_HIJOS_CUENTA = "SELECT cc FROM ContCuentas cc WHERE cc.padre = ?1 AND cc.estado=true";

    //Retorna el listado de las estructuras probramatica pero solo los del parametro que se envia
    public static String ALL_ESTRUCTURA_PROGRAMATICA = "SELECT ep FROM PresPlanProgramatico ep INNER JOIN ep.confId co INNER JOIN co.idTipo_estructura ie WHERE ep.activo = true  AND ie.codigo=?1 ORDER BY ep.codigo ASC";

    //RETORNA EL LISTADO DE LA FUENTE DE FINANCIMAIENTO EN ESTADO ACTIVO
    public static String ALL_FUENTE_FINANCIAMIENTO = "SELECT ff FROM PresFuenteFinanciamiento ff WHERE ff.estado=true ORDER BY ff.codFuente ASC";

    //Retorna el listado del catalogo presupuestario de tipo movimiento y activo
    public static String ALL_CATALOGO_PRESUPUESTO = "SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.activo=true AND cp.movimiento = true ORDER BY cp.codigo ASC";

    //Retorna el listado del catalogo presupuestario de tipo movimiento y activo y codigo
    public static String ALL_CATALOGO_PRESUPUESTO_CODE = "SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.activo=true AND cp.movimiento = true AND cp.codigo LIKE ?1 ORDER BY cp.codigo ASC";

    //PRESUPUESTO
    //Querys para consultar las secuencias de los id de PLANES, PROGRAMAS Y PROYECTOS
    public static String FIND_ID_PLANIFICACION = "SELECT valor FROM presupuesto.secuencias_papp WHERE codigo = ?1 and estado=true";

    public static String UPDATE_ID_PLANIFICACION = "UPDATE presupuesto.secuencias_papp SET valor = ?1 WHERE codigo = ?2";

    //Query para btener el maximo codigo de objetivo local
    public static String FIND_MAX_CODE_OBJETIVO_LOCAL = "SELECT MAX(pl.codigo) FROM public.plan_local_politica pl WHERE pl.objetivo = ?1";

    //Query para obtener los rubros sin reforma
    //public static String FIND_RUBROS_SIN_REFORMA = "SELECT * FROM talento_humano.th_cargo_rubros WHERE periodo = ?1 AND codigo_reforma = NULL";
    //Retorna el listado de los hijos de la cuenta contable
    public static final String FIND_HIJOS_PRESUPUESTO = "SELECT cc FROM PresCatalogoPresupuestario cc WHERE cc.padre = ?1 AND cc.estado=true";

    //Regresa la asignacion del servidor - cargo 
    public static final String FIND_SERVIDOR_CARGO = "SELECT sc FROM ThServidorCargo sc where sc.idServidor=?1 and sc.activo=TRUE";

    //Regresa el historico del cargo asignado del servidor
    public static final String FIND_SERVIDOR_CARGO_ACTIVO = "SELECT thscr FROM ThServidorCargoRepository thscr WHERE thscr.idServidor=?1 AND thscr.fechaFin is null";

    //Regresa la suma de los cargo - rubros
    public static final String SUM_RUBROS_CARGO = "SELECT SUM(thr.monto) FROM ThCargoRubros thr INNER JOIN thr.idRubro rb WHERE rb.ingreso=?1 AND rb.origen=?2 AND thr.periodo = ?3 AND thr.estado=true AND rb.estado=true AND rb.activo=true";

    //Regresa el periodo del cupo asignado
    public static final String PERIODO_CUPO = "SELECT DISTINCT(cp.periodo) FROM CupoPresupuesto cp WHERE cp.numTramite = ?1";

    //Regresa la suma de los cargo - rubros
    public static final String LIST_RUBROS_CARGO = "SELECT thr FROM ThCargoRubros thr INNER JOIN thr.idRubro rb WHERE rb.ingreso=true AND thr.periodo = ?1 AND thr.estado=true AND rb.estado=true AND rb.activo=true AND thr.partidaPresupuestaria is null";

    //Regresa el modelo de datos para la partida presupuestaria
    public static String MODEL_PRESUPUESTO_DEBE = "SELECT * FROM contabilidad.fs_relacion_contable(?1,?2,?3)";

    //Regresa el modelo de datos para la partida presupuestaria
    public static String MODEL_PRESUPUESTO_HABER = "SELECT * FROM contabilidad.fs_relacion_contable_1(?1,?2,?3)";

    //Regresa el modelo de datos para obtener los movimientos de cuentas contables por código de cuenta y periodo
    public static String MODEL_MOVIMIENTO_CUENTAS = "SELECT * from (select round(sum(mc.debe),2) as sum_debe, round(sum(mc.haber),2) as sum_haber\n"
            + "from contabilidad.movimiento_cuentas(?1,?2,?3,?4,?5,?6,?7) mc) as total_contab,\n"
            + "contabilidad.movimiento_cuentas(?1,?2,?3,?4,?5,?6,?7)";

    //Regresa el listado de las ceuntas contables relacionados al id del catalogo presupuestario
    public static String PRESUPUESTO_CONTABLE = "SELECT cc FROM ContCuentaPartida cp INNER JOIN cp.idCuenta cc WHERE cc.activo = true AND (cp.idPartida1 = ?1 OR cp.idPartida2= ?1) AND cc.pagadoDevengado = false";

    //Regresa el listado de las ceuntas contables relacionados al id del catalogo presupuestario
    public static String FIND_CONTRATO_RESERVA = "SELECT r FROM ContratosReservaEjecuion cp INNER JOIN cp.reserva r WHERE cp.reserva = ?1";

    //Siguiente numero del diario general
    public static String NEXT_NUM_DIARIO = "SELECT CASE WHEN max(cdg.numRegistro) is null THEN 1 ELSE (max(cdg.numRegistro)+1) END FROM ContDiarioGeneral cdg WHERE cdg.periodo = ?1 AND cdg.revisado = ?2";

    //Siguiente numero del comprobante de pago
    public static String NEXT_NUM_COMPROBANTE = "SELECT CASE WHEN max(cdg.numRegistro) is null THEN 1 ELSE (max(cdg.numRegistro)+1) END FROM ContComprobantePago cdg WHERE cdg.periodo = ?1";

    //Siguiente numero del beneficiario comprobante de pago
    public static String NEXT_NUM_BENEFICIARIO = "SELECT CASE WHEN max(cdg.numRegistro) is null THEN 1 ELSE (max(cdg.numRegistro)+1) END FROM ContBeneficiarioComprobantePago cdg INNER JOIN cdg.idComprobantePago cp WHERE cp.periodo = ?1";

    //Modelo de datos ThRubroAsignacionModel
    public static String MODEL_TH_RUBRO_ASIGNACION = "SELECT * FROM presupuesto.fs_model_thrubro_asignacion(?1,?2,?3,?4,?5,?6)";

    //Relacion presupuesto cuenta talento humano
    public static String LIST_RELACION_CUENTA = "SELECT cc FROM ContCuentaPartida";

    //Listado de cuentas contables de devengado y pagado
    public static String DEVENGADO_PAGADO = "SELECT cc FROM ContCuentas cc WHERE cc.activo = true AND cc.pagadoDevengado=true AND cc.estado=true AND cc.movimiento=true order by cc.codigo";

    //Busca el servidor en el repositorio de los cargos que ha tenido
    public static String FIND_SERVIDOR_CARGO_REPOSITORIO = "SELECT tcr FROM ThServidorCargoRepository tcr WHERE tcr.idServidor = ?1 order by tcr.fechaInicio ASC";

    //Listado de los tipo de rol por periodo
    public static String FIND_TIPO_ROL_PERIODO = "SELECT t FROM ThTipoRol t INNER JOIN t.idMes mes WHERE t.periodo=?1 AND t.estado=true ORDER BY mes.orden ASC";

    //Listado de los tipo de rol por periodo
    public static String FIND_BENEFICIO_SOCIALES = "SELECT t FROM ThBeneficiosSociales t WHERE t.idThTipoRol = ?1 AND t.estado=true";

    //Elimina los datos de los beneficio sociales (DECIMO TERCERO)
    public static String DELETE_DECIMO_TERCERO = "UPDATE talento_humano.th_beneficios_sociales SET estado = false WHERE id_th_tipo_rol=?1 AND decimo_tercero=true AND estado=true";

    //Elimina los datos de los beneficio sociales (DECIMO CUARTO)
    public static String DELETE_DECIMO_CUARTO = "UPDATE talento_humano.th_beneficios_sociales SET estado = false WHERE id_th_tipo_rol=?1 AND decimo_cuarto=true AND estado=true";

    //Elimina los datos de los beneficio sociales (FONDO DE RESERVA)
    public static String DELETE_FONDO_RESERVA = "UPDATE talento_humano.th_beneficios_sociales SET estado = false WHERE id_th_tipo_rol=?1 AND fondos_reserva=true AND estado=true";

    //Elimina los datos de los beneficio sociales (FONDO DE RESERVA)
    public static String CREATE_BENEFICIO = "SELECT * FROM talento_humano.create_beneficio_social(?1,?2,?3,?4,?5,?6,?7,?8)";

    //Detalle reserva model
    public static String DETALLE_RESERVA_MODEL = "SELECT * FROM contabilidad.fs_detalle_reserva_model(?1,?2,?3)";

    //Buscar la relacion del registro con un modulo
    public static String CONTABILIDAD_MODULO = "SELECT cm.idModulo FROM ContContabilidadModulo cm WHERE cm.idContDiarioGeneral = ?1 AND cm.codModulo = ?2";

    //UPDATE certificaciones presupuestaria
    public static String UPDATE_RESERVA_COMPROMISO = "UPDATE certificacion_presupuestaria_anual.solicitud_reserva_compromiso set contabilizado = ?2 WHERE id=?1";

    //Find cuentas marcadas para comprobante
    public static String CUENTAS_COMPROBANTE = "SELECT * FROM contabilidad.fs_cuenta_comprobante(?1)";

    //Find cuentas marcadas para comprobante partida
    public static String CUENTAS_COMPROBANTE_PARTIDA = "SELECT * FROM contabilidad.fs_cuenta_partida_comprobante(?1) WHERE saldodisponible > 0";

    //Listad diario general sin comprobante de pago
    public static String REGISTRO_CONTABLE_COMPROBANTE = "SELECT DISTINCT(dg) FROM ContDiarioGeneralDetalle dt "
            + "INNER JOIN dt.idContDiarioGeneral dg INNER JOIN dg.clase cl INNER JOIN dg.tipo tp WHERE cl.codigo = 'clase_diario' "
            + "AND tp.codigo = 'tipo_financiero' AND dt.comprobantePago = true AND dg.numRegistro= ?1 AND dg.periodo = ?2 AND dg.comprobantePago = false "
            + "AND dg.idDiarioGeneral is null AND dg.anulado = false ORDER BY dg.numRegistro ASC";

    //Siguiente numero del comprobante de pago
    public static String NEXT_NUM_TRANSFERENCIA = "SELECT CASE WHEN max(ct.numReferencia) is null THEN 1 ELSE (max(ct.numReferencia)+1) END FROM ContTransferencias ct WHERE ct.periodo = ?1";

    //Busca los roles del servidor de acuerdo a un perido en especifico
    public static String FIND_ROLES_SERVIDOR_PERIODO = "SELECT ttr FROM ThLiquidacionRol tlr INNER JOIN tlr.idTipoRol ttr INNER JOIN ttr.idMes mes INNER JOIN tlr.idServidorCargo sc WHERE tlr.estado = true AND ttr.periodo = ?2 AND ttr.estado = true AND sc.idServidor = ?1 ORDER BY mes.orden ASC";

    //Busca las horas extras por tipo de rol
    public static String FIND_HORAS_EXTRAS = "SELECT thh FROM ThHorasExtras thh WHERE thh.estado = true AND thh.idTipoRol = ?1";

    //Eliminar las horas extras por tipo de rol
    public static String DELETE_HORAS_EXTRAS = "UPDATE talento_humano.th_horas_extras SET estado = false WHERE id_tipo_rol=?1 AND estado=true";

    //Crea las horas extras por tipo de rol 
    public static String CREATE_HORAS_EXTRAS = "SELECT * FROM talento_humano.fs_generar_horas_extras(?1,?2,?3,?4,?5)";

    //Detalle del diario de tesoreria a registrarDETALLE_TESORERIA
    public static String DETALLE_TESORERIA = "SELECT\n"
            + "cdgd.id_cont_cuentas, SUM(cdgd.debe) as debe, SUM(cdgd.haber) as haber, cdgd.rubro_liquidacion,\n"
            + "cdgd.tipo_registro, cdgd.id_pres_catalogo_presupuestario, cdgd.id_pres_plan_programatico,\n"
            + "cdgd.id_pres_fuente_financiamiento, cdgd.partida_presupuestaria,\n"
            + "SUM(cdgd.devengado) AS devengado, SUM(cdgd.ejecutado) AS ejecutado,\n"
            + "ROW_NUMBER() OVER(ORDER BY cdgd.id_cont_cuentas) as numeracion,\n"
            + "cc.descripcion, ci.texto as cod_tipo, cc.codigo as cod_cuenta\n"
            + "FROM contabilidad.cont_diario_general_detalle cdgd\n"
            + "LEFT JOIN contabilidad.cont_cuentas cc ON cdgd.id_cont_cuentas = cc.id\n"
            + "LEFT JOIN catalogo_item ci ON cdgd.tipo_registro = ci.id\n"
            + "WHERE cdgd.id_cont_diario_general IN (?1)\n"
            + "GROUP BY 1,4,5,6,7,8,9, cc.codigo, cc.descripcion, ci.texto\n"
            + "ORDER BY cc.codigo ASC";

    public static String TIPO_LIQUIDACION = "SELECT b.* FROM contabilidad.cont_diario_general d\n"
            + "INNER JOIN asgard.fina_ren_tipo_liquidacion b ON d.tipo_liquidacion = b.id\n"
            + "WHERE d.estado = true AND d.revisado = false AND d.periodo = ?1\n"
            + "GROUP BY b.id\n"
            + "ORDER BY b.nombre_reporte ASC";

    public static String GENERAR_RC_EMISIONES = "SELECT * FROM contabilidad.fs_tesoreria_emisiones(?1,?2,?3,?4,?5)";

    public static String IS_CONTABILIZADA = "SELECT CASE WHEN COUNT(dg) = 0 THEN false ELSE true END \n"
            + "FROM contabilidad.cont_contabilidad_modulo ccm\n"
            + "INNER JOIN contabilidad.cont_diario_general dg ON ccm.id_cont_diario_general = dg.id\n"
            + "WHERE ccm.id_modulo = ?1 AND ccm.cod_modulo = ?2 AND dg.estado = true AND dg.revisado = true";

    public static String LIST_ID_MODULOS = "SELECT c.idModulo FROM ContContabilidadModulo c WHERE c.idContDiarioGeneral IN (?1) AND c.codModulo = ?2";

    public static String DELETE_EMISION = "SELECT * FROM contabilidad.fs_delete_registro_emision(?2,?1)";

    public static String DELETE_EMISION_GLOBAL = "SELECT * FROM  contabilidad.fs_delete_registro_emisiones(?1,?2,?3,?4)";

    public static String DETALLE_TESORERIA_PREDIOS = "SELECT\n"
            + "cdgd.id_cont_cuentas, SUM(cdgd.debe) as debe, SUM(cdgd.haber) as haber, cdgd.rubro_liquidacion,\n"
            + "cdgd.tipo_registro, cdgd.id_pres_catalogo_presupuestario, cdgd.id_pres_plan_programatico,\n"
            + "cdgd.id_pres_fuente_financiamiento, cdgd.partida_presupuestaria,\n"
            + "SUM(cdgd.devengado) AS devengado, SUM(cdgd.ejecutado) AS ejecutado,\n"
            + "ROW_NUMBER() OVER(ORDER BY cdgd.id_cont_cuentas) as numeracion,\n"
            + "cc.descripcion, ci.texto as cod_tipo, cc.codigo as cod_cuenta\n"
            + "FROM contabilidad.cont_diario_general_detalle cdgd\n"
            + "INNER JOIN contabilidad.cont_diario_general dg ON cdgd.id_cont_diario_general = dg.id\n"
            + "LEFT JOIN contabilidad.cont_cuentas cc ON cdgd.id_cont_cuentas = cc.id\n"
            + "LEFT JOIN catalogo_item ci ON cdgd.tipo_registro = ci.id\n"
            + "WHERE dg.cod_modulo = ?1 AND dg.tipo_liquidacion = ?4\n"
            + "AND dg.fecha_registro BETWEEN ?2 AND ?3\n"
            + "AND dg.estado = true AND dg.revisado = false\n"
            + "GROUP BY 1,4,5,6,7,8,9, cc.codigo, cc.descripcion, ci.texto\n"
            + "ORDER BY cc.codigo ASC";

    public static String LIST_ID_MODULOS_PREDIOS = "SELECT ccm.id_modulo FROM contabilidad.cont_contabilidad_modulo ccm\n"
            + "INNER JOIN contabilidad.cont_diario_general cdg ON ccm.id_cont_diario_general = cdg.id\n"
            + "WHERE cdg.cod_modulo  = ?1 AND cdg.tipo_liquidacion = ?4\n"
            + "AND cdg.fecha_registro BETWEEN ?2 AND ?3\n"
            + "AND dg.estado = true AND dg.revisado = false";
    
    //Retorna el listado de las estructuras probramatica por estado y activo
    public static String ALL_ESTRUCTURA_PROGRAMATICA_ESTADO = "SELECT ep FROM PresPlanProgramatico ep INNER JOIN ep.confId co INNER JOIN co.idTipo_estructura ie WHERE ep.estado = true and ep.activo = true ORDER BY ep.codigo ASC";

}
