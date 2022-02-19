/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.service;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.arrendamiento.entities.Arrendamiento;
import com.origami.sigef.arrendamiento.entities.Arrendatarios;
import com.origami.sigef.arrendamiento.entities.OrdenesEmitidas;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.models.CorreoArchivo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Cabecera;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.LiquidacionPagoService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import ec.com.portaltramite.tc.ResultPortalWsdl;
import ec.com.portaltramite.tc.TramitePortalWS;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class OrdenesEmitidasService extends AbstractService<OrdenesEmitidas> {

    private static final Logger LOG = Logger.getLogger(OrdenesEmitidasService.class.getName());

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private TramitePortalWS portalWS;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private ArrendamientoService arrendamientoService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private LiquidacionPagoService liquidacionPagoService;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ValoresService valService;
    @Inject
    private ManagerService manager;
    @Inject
    private FinaRenLiquidacionService recaudacionService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    protected static final Integer ANIO = Utils.getAnio(new Date());
    protected static final Integer MES = Utils.getMes(new Date());
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final Integer DIA_CIERRE = 30;
    protected static final String CODIGO_SERVICIO = "167";
    protected static final Integer SUMAR_DIAS = 365;
    private String USER_SERVIDOR_RESPONSABLE;
    private Boolean SERVICE_ONLYCONTROL_ORDEN_PAGO = Boolean.FALSE;
//    private Liquidacion idLiquidacion = new Liquidacion();
    private FinaRenLiquidacion idLiquidacion = new FinaRenLiquidacion();

    public OrdenesEmitidasService() {
        super(OrdenesEmitidas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Schedule(dayOfWeek = "*", month = "*", hour = "18", dayOfMonth = "Last", year = "*", minute = "0", second = "0", persistent = false)
    public void actualizarRenovacion() {
        List<Arrendamiento> list = new ArrayList<>();
        list = (List<Arrendamiento>) em.createQuery("SELECT a FROM Arrendamiento a WHERE a.renovacionAutomatica = TRUE AND a.estado = TRUE AND a.fechaVigencia<=:fecha")
                .setParameter("fecha", new Date())
                .getResultList();
        int index = 0;
        if (!list.isEmpty()) {
            for (Arrendamiento a : list) {
                a.setFechaVigencia(Utils.sumarRestarDiasFecha(a.getFechaVigencia(), SUMAR_DIAS));
                a.setFechaModificacion(new Date());
                a.setUsuarioModificacion("SYSTEM");
                arrendamientoService.edit(a);
                index++;
            }
            System.out.println(ANSI_GREEN + "No. Arriendos renovados: ".concat(Integer.toString(index)) + ANSI_GREEN);
        }
    }

//    @Schedule(dayOfWeek = "*", month = "*", hour = "8", dayOfMonth = "1-5", year = "*", minute = "0", second = "0", persistent = false)
    public void generarOrdenesAutomaticamente() {
//        String ordenesAuto = valoresService.findByCodigo("GENARAR_ORDENES_ARRIENDO_AUTO");
//        if (Boolean.valueOf(ordenesAuto)) {
        Query query = em.createNativeQuery("SELECT a.id,a.id_arriendamiento,a.id_operador, a.iva,a.subtotal,a.valor_arriendo,a.alicuota,a.canon_arriendo FROM arriendo.arrendatarios a\n"
                + "INNER JOIN arriendo.arrendamiento b ON a.id_arriendamiento = b.id WHERE NOT EXISTS (SELECT NULL FROM arriendo.ordenes_emitidas oe \n"
                + "WHERE oe.id_arrendamiento = a.id_arriendamiento AND oe.id_arrendatario = a.id AND oe.mes = ?1 AND oe.anio = ?2 AND oe.estado =TRUE)\n"
                + "AND a.estado = TRUE AND b.estado=TRUE  AND b.fecha_suscripcion <= ?3 AND b.fecha_vigencia > ?3 ORDER BY a.id ASC")
                .setParameter(1, (MES))
                .setParameter(2, ANIO.shortValue())
                .setParameter(3, new Date());
        List<Object[]> result = query.getResultList();
        int index = 0;
        if (result != null) {
            List<Arrendatarios> list = new ArrayList<>();
            for (Object[] data : result) {
                Proveedor proveedor = proveedorService.findById(((BigInteger) data[2]));
                Arrendamiento arrendamiento = arrendamientoService.findById(((BigInteger) data[1]));
                Arrendatarios d = new Arrendatarios(((BigInteger) data[0]).longValue(), arrendamiento, proveedor, (BigDecimal) data[3], (BigDecimal) data[4], (BigDecimal) data[5], (BigDecimal) data[6], (BigDecimal) data[7]);
                list.add(d);
            }
            if (!list.isEmpty()) {
                for (Arrendatarios a : list) {
                    int aux = getGenerarOrdenPagoArriendo(a, MES, ANIO.shortValue());
                    if (aux == 0) {
                        index++;
                    }
                }
                System.out.println(ANSI_GREEN + "No. Ordenes de pagos emitidos: ".concat(Integer.toString(index)) + ANSI_GREEN);
            }
        }
//        }
    }

    public int getGenerarOrdenPagoArriendo(Arrendatarios arrendatario, Integer mes, Short anio) {
        int resultBoolean = 1;
//        SERVICE_ONLYCONTROL_ORDEN_PAGO = Boolean.parseBoolean(valService.findByCodigo(Constantes.SERVICE_ONLYCONTROL_ORDEN_PAGO));
//        ResultPortalWsdl result = new ResultPortalWsdl();
        guardarEmitirFactura(arrendatario, mes, anio.intValue(), Long.parseLong("0"));
        generarOrden(arrendatario, mes, anio, "0");
        return resultBoolean = 0;
//        }
    }

    public boolean emitirOrden(Arrendatarios arrendatario, String codigoError, String idSolicitud, Integer mes) {
        String pathPDF = obtenerRutaPDF(codigoError, idSolicitud);
        //byte[] dataPDF = portalWS.getMetodosPort().obtienePdfXSolicitud(Long.parseLong(idSolicitud), "", "");
        Proveedor proveedor = getProveedor(arrendatario);
        String email = determinarCorreo(proveedor);
        String nombre = proveedor.getCliente().getNombre();
        String mensaje = formatoMensaje(nombre, mes);
        //String archivoBase64 = Base64.getEncoder().encodeToString(dataPDF);
        Path path = Paths.get(pathPDF);
        System.out.println("RESULTADO: " + !path.toFile().exists());
        if (!email.equals("")) {
            List<CorreoArchivo> archivos = new ArrayList<>();
            System.out.println("PathPDF: " + pathPDF);
            CorreoArchivo correoArchivo = new CorreoArchivo();
            correoArchivo.setNombreArchivo(pathPDF);
            correoArchivo.setTipoArchivo("pdf");
            correoArchivo.setArchivoBase64("");
            archivos.add(correoArchivo);
            Correo c = new Correo();
            c.setDestinatario(email);
            c.setAsunto("ORDEN DE PAGO");
            c.setMensaje(mensaje);
            c.setArchivos(archivos);
            katalinaService.enviarCorreo(c);
            return true;
        } else {
            return false;
        }
    }

    public String obtenerRutaPDF(String codigoError, String idSolicitud) {
        String resultado = "";
        if (codigoError.equals("0")) {
            byte[] dataPDF = portalWS.getMetodosPort().obtienePdfXSolicitud(Long.parseLong(idSolicitud), "", "");
            String ruta = valoresService.findByCodigo("ORDEN_PAGO_FILE") + "ORDEN_PAGO_" + Utils.convertirMesALetra(MES) + ".pdf";
            Utils.createDirectoryIfNotExist(ruta);
            //ESCRIBIMOS EN EL DIRECTORIO
            File fileOPPDF = new File(ruta);
            try ( FileOutputStream fos = new FileOutputStream(fileOPPDF);) {
                fos.write(dataPDF);
                resultado = ruta;
                System.out.println("RUTA: " + resultado);
            } catch (Exception e) {
                System.out.println("No se genero documento");
                e.printStackTrace();
            }
        }
        return resultado;
    }

    private String determinarCorreo(Proveedor proveedor) {
        String resultado = "";
        if (proveedor != null) {
            if (proveedor.getCliente() != null) {
                if (proveedor.getCliente().getEmail() != null) {
                    resultado = proveedor.getCliente().getEmail();
                } else {
                    if (proveedor.getContacto() != null) {
                        if (proveedor.getContacto().getEmail() != null) {
                            resultado = proveedor.getContacto().getEmail();
                        }
                    }
                }
            }
        }
        return resultado;
    }

    public void generarOrden(Arrendatarios arrendatario, Integer mes, short anio, String idSolicitud) {
        OrdenesEmitidas ordenesEmitidas = new OrdenesEmitidas();
        ordenesEmitidas.setIdArrendamiento(arrendatario.getIdArriendamiento());
        ordenesEmitidas.setIdArrendatario(arrendatario);
        ordenesEmitidas.setMontoPagar(getCalcularValor(arrendatario));
        ordenesEmitidas.setMes(new BigInteger("" + (mes)));
        ordenesEmitidas.setMesLetra(Utils.convertirMesALetra(mes).toUpperCase());
        ordenesEmitidas.setAnio(anio);
        if (idLiquidacion.getId() != null) {
            ordenesEmitidas.setLiquidacion(idLiquidacion);
            idLiquidacion.setMes(ordenesEmitidas.getMes().longValue());
            idLiquidacion.setArriendo(ordenesEmitidas.getIdArrendatario());
            recaudacionService.edit(idLiquidacion);
        }
        if (idSolicitud != null) {
            ordenesEmitidas.setIdSolicitud(idSolicitud);
            create(ordenesEmitidas);
        }
    }

    public Boolean anularOrdenPago(OrdenesEmitidas orden) {
        Boolean resultado = true;
        try {
            FinaRenLiquidacion lq = orden.getLiquidacion();
            lq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));
            recaudacionService.edit(lq);
        } catch (Exception jse) {
            LOG.log(Level.SEVERE, "OrdenesEmitidas Id:" + orden.getId(), jse);
            return Boolean.FALSE;
        }
        return resultado;
    }

    private String formatoMensaje(String destinatario, Integer mes) {
        return "<p>Estimado(a) Cliente,</p>"
                + "<p><br></p>"
                + "<p><strong>" + destinatario + "&nbsp;</strong></p>"
                + "<p><br></p>"
                + "<p>Gracias por preferirnos, adjunto encontrar&aacute; su <strong>ORDEN DE PAGO</strong> correspondiente al mes de <strong>" + Utils.convertirMesALetra(mes) + "</strong></p>"
                + "<p><br></p><p><br></p>"
                + "<p><strong>NOTA:</strong> Esta orden de pago tiene una vigencia de tres (3) d&iacute;as calendario. Una vez vencida, la orden deber&aacute; ser renovada.</p><p><br></p>";
    }

    public BigDecimal getCalcularValor(Arrendatarios arrendatario) {
        double valor = 0;
        if (Utils.getMes(arrendatario.getIdArriendamiento().getFechaSuscripcion()) == MES && Utils.getAnio(arrendatario.getIdArriendamiento().getFechaSuscripcion()).intValue() == ANIO.intValue()) {
            if (Utils.getDia(arrendatario.getIdArriendamiento().getFechaSuscripcion()) >= 1 && Utils.getDia(arrendatario.getIdArriendamiento().getFechaSuscripcion()) <= 5) {
                valor = arrendatario.getCanonArriendo().doubleValue();
            } else {
                valor = (arrendatario.getCanonArriendo().doubleValue() * (DIA_CIERRE - Utils.getDia(arrendatario.getIdArriendamiento().getFechaSuscripcion()))) / DIA_CIERRE;
            }
        } else {
            valor = arrendatario.getCanonArriendo().doubleValue();
        }
        return new BigDecimal(valor);
    }

    public int getConsultarOrdenes(Arrendatarios arrendatario) {
        Long resultado = (Long) em.createQuery("SELECT count(oe) FROM OrdenesEmitidas oe WHERE oe.estado=TRUE AND oe.idArrendamiento=:arrendamiento AND oe.idArrendatario=:arrendatario")
                .setParameter("arrendamiento", arrendatario.getIdArriendamiento())
                .setParameter("arrendatario", arrendatario)
                .getSingleResult();
        return resultado.intValue();
    }

    public Boolean getConsultarOrdenPorMes(Arrendatarios arrendatario, Integer mes, Integer anio) {
        try {
            FinaRenLiquidacion resultado = (FinaRenLiquidacion) em.createQuery("SELECT oe FROM FinaRenLiquidacion oe WHERE oe.estadoLiquidacion in (1,2) AND oe.mes=:mes AND oe.anio=:anio AND oe.arriendo=:arrendatario")
                    .setParameter("arrendatario", arrendatario)
                    .setParameter("mes", mes.longValue())
                    .setParameter("anio", anio)
                    .getSingleResult();
            if (resultado != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Proveedor getProveedor(Arrendatarios arrendatario) {
        try {
            Proveedor resultado = (Proveedor) em.createQuery("SELECT p FROM Arrendatarios a INNER JOIN a.idOperador p WHERE a.id=:arrendatario")
                    .setParameter("arrendatario", arrendatario.getId())
                    .getSingleResult();
            if (resultado != null) {
                return resultado;
            } else {
                return new Proveedor();
            }
        } catch (Exception e) {
            return new Proveedor();
        }
    }

    public Boolean volverGenerarOrdenPago(OrdenesEmitidas ordenEmitida) {
        Boolean accion = false;
        try {
            Gson gs = new Gson();
            String data = portalWS.getMetodosPort().actualizaSolicitudXCaducidad(Long.parseLong(ordenEmitida.getIdSolicitud()), "", "");
            String json = data;
            json = json.replace("\\", "");
            ResultPortalWsdl result = gs.fromJson(json, ResultPortalWsdl.class);
            if (result.getCodigoerror().equals("0")) {
                accion = true;
                Boolean aux = emitirOrden(ordenEmitida.getIdArrendatario(), "0", result.getIdsolicitud(), ordenEmitida.getMes().intValue());
                ordenEmitida.setIdSolicitud(result.getIdsolicitud());
                edit(ordenEmitida);
                guardarEmitirFactura(ordenEmitida.getIdArrendatario(), ordenEmitida.getMes().intValue(), ordenEmitida.getAnio().intValue(), Long.parseLong(ordenEmitida.getIdSolicitud()));
            }
        } catch (JsonSyntaxException jse) {
            LOG.log(Level.SEVERE, "OrdenesEmitidas Id:" + ordenEmitida.getId(), jse);
        }
        return accion;
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral, Boolean accion) {
        String sql = "";
        if (accion) {
            sql = "UPDATE arriendo.ordenes_emitidas SET diario_cobrar = false WHERE id_diario_cobrar = ?1";
        } else {
            sql = "UPDATE arriendo.ordenes_emitidas SET diario_pagar = false WHERE id_diario_pagar = ?2";
        }
        Query query = getEntityManager().createNativeQuery(sql)
                .setParameter(1, diarioGeneral.getId().intValue());
        return query.executeUpdate();
    }

    public void guardarEmitirFactura(Arrendatarios arrendatario, Integer mes, Integer anio, Long num_tramite) {
        if (arrendatario != null) {
            FinaRenTipoLiquidacion tipo = new FinaRenTipoLiquidacion();
            List<FinaRenDetLiquidacion> rubros = new ArrayList<>();
            FinaRenDetLiquidacion detalle = new FinaRenDetLiquidacion();
            this.idLiquidacion = new FinaRenLiquidacion();
            this.idLiquidacion.setTotalPago(getCalcularValor(arrendatario));
            this.idLiquidacion.setSaldo(getCalcularValor(arrendatario));
            this.idLiquidacion.setComprador(arrendatario.getPersona());
            this.idLiquidacion.setCodigoLocal(arrendatario.getIdArriendamiento().getLocal().getIdPuesto().toString());
//            this.idLiquidacion.setPredio(null);
            tipo = recaudacionService.getTipoLiquidacionByPrefijo("PME");
            System.out.println("prefijo>>" + tipo.getNombreTitulo());
            this.idLiquidacion.setTipoLiquidacion(tipo);
            this.idLiquidacion.setValidada(Boolean.FALSE);
            for (FinaRenRubrosLiquidacion rb : tipo.getRenRubrosLiquidacionCollection()) {
                detalle = new FinaRenDetLiquidacion();
                detalle.setRubro(rb);
                detalle.setValor(idLiquidacion.getTotalPago());
                detalle.setCantidad(1);
                rubros.add(detalle);
            }
            idLiquidacion = recaudacionService.crearLiquidacion(idLiquidacion, rubros);
        }
    }

    private ComprobanteElectronico initComprobanteElectronico(Liquidacion liquidacion, Cajero cajero) {
        ComprobanteElectronico comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronico.setNumComprobante(liquidacion.getNumeroComprobante());
        comprobanteElectronico.setAmbiente(ComprobantesCode.AMBIENTE);
        comprobanteElectronico.setIsOnline(ComprobantesCode.ONLINE);
        comprobanteElectronico.setPuntoEmision(cajero.getPuntoEmision());
        comprobanteElectronico.setRucEntidad(cajero.getEntidad().getRucEntidad());
        comprobanteElectronico.setGeneraLiquidacion(Boolean.FALSE);
        comprobanteElectronico.setTipoLiquidacion("RET");
        //INICIO - CABECERA
        comprobanteElectronico.setCabecera(loadCabecera(liquidacion.getFechaCreacion(), liquidacion.getCliente()));
        //FIN - CABECERA
        return comprobanteElectronico;
    }

    private Cabecera loadCabecera(Date fechaCreacion, Cliente cliente) {
        Cabecera cabecera = new Cabecera();
        if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            cabecera.setCedulaRuc(cliente.getIdentificacionCompleta());
            cabecera.setPropietario(cliente.getNombre());
        } else {
            cabecera.setCedulaRuc(cliente.getIdentificacion());
            cabecera.setPropietario(cliente.getNombreCompleto());
        }
        cabecera.setFechaEmision(fechaCreacion + "");
        cabecera.setDireccion(cliente.getDireccion());
        cabecera.setCorreo(cliente.getEmail());
        cabecera.setTelefono(cliente.getTelefono());
        if (cliente.getTipoIdentificacion().getId().equals(12L)) {
            cabecera.setEsPasaporte(Boolean.TRUE);
        } else {
            cabecera.setEsPasaporte(Boolean.FALSE);
        }
        return cabecera;
    }

    public List<OrdenesEmitidas> getListOrdenes(boolean accion, Integer mes, Integer periodo) {
        String sql = "";
        if (accion) {
            sql = "SELECT oe FROM OrdenesEmitidas oe WHERE oe.anio=:periodo AND oe.mes=:mes AND oe.diarioCobrar=FALSE AND oe.estado=TRUE";
        } else {
            sql = "SELECT oe FROM OrdenesEmitidas oe WHERE oe.anio=:periodo AND oe.mes=:mes AND oe.diarioCobrar=TRUE AND oe.diarioPagar=FALSE AND oe.estado=TRUE AND oe.ordenPagada=TRUE";
        }
        List<OrdenesEmitidas> list = (List<OrdenesEmitidas>) em.createQuery(sql)
                .setParameter("mes", BigInteger.valueOf(mes.intValue()))
                .setParameter("periodo", periodo.shortValue())
                .getResultList();
        return list;
    }

    public OrdenesEmitidas getOrden(Liquidacion liquidacion) {
        try {
            OrdenesEmitidas result = (OrdenesEmitidas) em.createQuery("SELECT o FROM OrdenesEmitidas o WHERE o.estado=TRUE AND o.idLiquidacion=:liquidacion")
                    .setParameter("liquidacion", liquidacion)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return new OrdenesEmitidas();
        }
    }

}
