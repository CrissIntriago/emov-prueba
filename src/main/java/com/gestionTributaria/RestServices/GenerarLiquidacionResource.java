/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.RestServices;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.DetaLiquidacionDTO;
import com.gestionTributaria.models.LiquidacionDTO;
import com.gestionTributaria.models.PagoLiquidacionModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.Constantes;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.ServicioTipo;
import com.ventanilla.Entity.SolicitudDepartamento;
import com.ventanilla.Entity.SolicitudDocumento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Models.SolicitudDocumentoDTO;
import com.ventanilla.Models.SolicitudServiciosDTO;
import com.ventanilla.Models.TramiteRespuesta;
import com.ventanilla.Services.VentanillaService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("/generarLiquidacion/")
@Produces({"application/Json", "text/xml"})
@ApplicationScope
public class GenerarLiquidacionResource extends BpmnBaseRoot{

    private static final Logger LOG = Logger.getLogger(GenerarLiquidacionResource.class.getName());

    @Inject
    private ManagerService services;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private FinaRenDetalleLiquidacionService detalleLiquidacionService;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private BancoService bancoService;
    @Inject
    private ClienteService clienteService;
    @Inject 
    private VentanillaService ventanillaService;
    @Inject
    private UserSession userSession;
    @Inject
    private FileUploadDoc uploadDoc;

    private CtlgSalario salario;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenDetLiquidacion> detalle;
    private Map<String, Object> param;
    private static final BigDecimal PORCENTAJE = new BigDecimal("100");
    private CatPredio predio;
    private ServicioTipo servicioTipo;
    private List<ServicioRequisito> listaRequisitos;
    private List<SolicitudDocumentoDTO> solicitudDocumentos;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenerarLiquidacionResource
     */
    public GenerarLiquidacionResource() {
        param = new HashMap<>();
        liquidacion = new FinaRenLiquidacion();
        detalle = new ArrayList<>();
        salario = new CtlgSalario();
    }

    /**
     * Retrieves representation of an instance of
     * com.gestionTributaria.RestServices.GenerarLiquidacionResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of
     * GenerarLiquidacionResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @POST
    @Path("save")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public LiquidacionDTO generaLiquidacion(LiquidacionDTO liq) {
        this.liquidacion = new FinaRenLiquidacion();
        this.liquidacion.setAnio(liq.getAnio());
        this.liquidacion.setAvaluoMunicipal(BigDecimal.ZERO);
        this.liquidacion.setAvaluoConstruccion(BigDecimal.ZERO);
        this.liquidacion.setAvaluoSolar(BigDecimal.ZERO);
        this.liquidacion.setIpUserSession(liq.getIpDireccion());
        this.liquidacion.setMacAddresUsuarioIngreso(liq.getMacAdress());
        this.liquidacion.setLiquidadorResponsable(liq.getLiquidadorReposanble());
        this.liquidacion.setUsuarioIngreso(liq.getLiquidadorReposanble());
        this.liquidacion.setLiquidadorAprobador(liq.getLiquidadorAprobador());
        this.liquidacion.setFechaIngreso(new Date());

        if (liq.getIdPredio() != null) {
            CatPredio predio = services.find(CatPredio.class, liq.getIdPredio());
            if (predio != null && predio.getId() != null) {
                this.liquidacion.setPredio(predio);
                this.liquidacion.setClaveCatastral(predio.getClaveCat());
                this.liquidacion.setAvaluoConstruccion(predio.getAvaluoConstruccion());
                this.liquidacion.setAvaluoMunicipal(predio.getAvaluoMunicipal());
                this.liquidacion.setAvaluoSolar(predio.getAvaluoSolar());
            }
        }

        if (liq.getIdlocal() != null) {
            FinaRenLocalComercial local = services.find(FinaRenLocalComercial.class, liq.getIdlocal());
            if (local != null && local.getId() != null) {
                this.liquidacion.setLocalComercial(local);
                this.liquidacion.setCodigoLocal(local.getNumLocal());

                if (local.getNumPredio() != null) {
                    predio = services.find(CatPredio.class, local.getNumPredio());
                    if (predio != null && predio != null) {
                        this.liquidacion.setPredio(predio);
                        this.liquidacion.setClaveCatastral(predio.getClaveCat());
                        this.liquidacion.setAvaluoConstruccion(predio.getAvaluoConstruccion());
                        this.liquidacion.setAvaluoMunicipal(predio.getAvaluoMunicipal());
                        this.liquidacion.setAvaluoSolar(predio.getAvaluoSolar());
                    }
                }

            }
        }

        if (liq.getIdCliente() != null) {
            Cliente cliente = services.find(Cliente.class, liq.getIdCliente());
            if (cliente != null && cliente.getId() != null) {
                this.liquidacion.setComprador(cliente);
                this.liquidacion.setNombreComprador(cliente.getNombreCompleto());
                this.liquidacion.setIdentificacion(cliente.getIdentificacionCompleta());
            }
        }

        if (liq.getIdTipoLiquidacion() != null) {
            FinaRenTipoLiquidacion tipoLiquidacion = services.find(FinaRenTipoLiquidacion.class, liq.getIdTipoLiquidacion());
            if (tipoLiquidacion != null && tipoLiquidacion.getId() != null) {
                this.liquidacion.setTipoLiquidacion(tipoLiquidacion);
            }

        }

        if (this.liquidacion.getTipoLiquidacion() != null) {
            if (this.liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas() != null && this.liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas().equals(Boolean.TRUE)) {
                this.liquidacion.setValidada(Boolean.FALSE);
            } else {
                this.liquidacion.setValidada(Boolean.TRUE);
            }

        }

        this.liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        this.liquidacion.setNumLiquidacion(services.getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), this.liquidacion.getTipoLiquidacion().getId()));
        this.liquidacion.setIdLiquidacion(this.liquidacion.getTipoLiquidacion().getPrefijo() + "-" + Utils.completarCadenaConCeros(this.liquidacion.getNumLiquidacion().toString(), 6));
        this.liquidacion.setNumComprobante(BigInteger.ZERO);
        this.liquidacion.setCoactiva(Boolean.FALSE);
        this.liquidacion.setEstadoCoactiva(1);
        this.liquidacion.setCodigoVerificador(Utils.generarCodigoVerificacion());

        if (Utils.isNotEmpty(liq.getDetalle())) {
            detalle = new ArrayList<>();
            for (DetaLiquidacionDTO rubro : liq.getDetalle()) {
                FinaRenDetLiquidacion detaLiqui = new FinaRenDetLiquidacion();
                detaLiqui.setEstado(Boolean.TRUE);
                detaLiqui.setValorRecaudado(BigDecimal.ZERO);
                detaLiqui.setValorSinDescuento(BigDecimal.ZERO);
                detaLiqui.setCantidad(rubro.getCantidad());
                detaLiqui.setValor(valorRubroCal(rubro.getIdRubro(), detaLiqui.getCantidad(), this.liquidacion));
                detalle.add(detaLiqui);
            }
        }

        this.liquidacion.setSaldo(gettotalPagar(detalle));
        this.liquidacion.setTotalPago(gettotalPagar(detalle));
        this.liquidacion = liquidacionService.create(this.liquidacion);

        if (detalle != null && !detalle.isEmpty()) {
            for (FinaRenDetLiquidacion item : detalle) {
                FinaRenDetLiquidacion data = Utils.clone(item);
                data.setLiquidacion(this.liquidacion);
                detalleLiquidacionService.create(data);
            }
        }
liq.setId(this.liquidacion.getId());
liq.setIdLiquidacion(this.liquidacion.getIdLiquidacion());  
liq.setTotalPago(gettotalPagar(detalle));
        System.out.println("liquidacion: " + liq.toString());
        //return gettotalPagar(detalle);
        return liq;
    }

    private BigDecimal valorRubroCal(Long idRubro, Integer cantidad, FinaRenLiquidacion liquidacion) {
        try {
            BigDecimal valorCal = BigDecimal.ZERO;
            BigDecimal result = BigDecimal.ZERO;
            BigDecimal valorSalario = BigDecimal.ZERO;
            param = new HashMap<>();
            salario = new CtlgSalario();
            FinaRenRubrosLiquidacion rubro = services.find(FinaRenRubrosLiquidacion.class, idRubro);
            if (cantidad == null || cantidad == 0) {
                cantidad = 1;
            }

            if (rubro != null && rubro.getId() != null && rubro.getTipoValor() != null) {
                if (rubro.getValor() == null) {
                    rubro.setValor(BigDecimal.ONE);
                }
            if(rubro.getTipoValor().getPrefijo()!=null){
                switch (rubro.getTipoValor().getPrefijo()) {
                    case "SBU":
                        valorSalario = services.getSalarioAnio(liquidacion.getAnio());
                        result = valorSalario.multiply(rubro.getValor().divide(PORCENTAJE)).multiply(new BigDecimal(cantidad.toString()));
                        break;
                    case "AVA":
                        result = liquidacion.getAvaluoConstruccion().multiply(rubro.getValor()).setScale(2, RoundingMode.HALF_UP);
                        break;
                    default:
                        result = rubro.getValor().multiply(new BigDecimal(cantidad.toString()));
                        break;
                }    
            }else{
                 result = rubro.getValor().multiply(new BigDecimal(cantidad.toString()));
            }

            }

            return result;
        } catch (ArithmeticException ae) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal gettotalPagar(List<FinaRenDetLiquidacion> detalleRubros) {
        BigDecimal total = BigDecimal.ZERO;
        if (detalleRubros != null && !detalleRubros.isEmpty()) {
            for (FinaRenDetLiquidacion item : detalleRubros) {
                if (item.getValor() != null && item.getValor().compareTo(BigDecimal.ZERO) == 1) {
                    total = total.add(item.getValor());
                }
            }
        }

        return total;
    }

    @POST
    @Path(value = "onlinePayment")
    @Consumes(MediaType.APPLICATION_JSON)
    public PagoLiquidacionModel pagoOnLine(String modelpago) {
        Gson gson = new Gson();
        PagoLiquidacionModel pagoLqui = gson.fromJson(modelpago, PagoLiquidacionModel.class);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            System.out.println("ipAdres>>" + ipAddress);
            BigInteger numeroComprobante = null;//new BigInteger(this.services.getNumComprobante().toString());
            FinaRenLiquidacion liquidacion = null;
            FinaRenPago pago = null;
            Cajero cajero = null;

            System.out.println("idliquidacion>>" + pagoLqui.getIdLiquidacion());
            if (pagoLqui.getIdLiquidacion() != null) {
                liquidacion = recaudacionService.realizarDescuentoRecargaInteresPredial(liquidacionService.find(pagoLqui.getIdLiquidacion()), new Date());
                liquidacion.calcularPagoConCoactiva();
                System.out.println("pagofinal>>" + liquidacion.getPagoFinal() + " valor model>>" + pagoLqui.getTotalPago());
                if (liquidacion.getPagoFinal().compareTo(pagoLqui.getTotalPago()) == 0.00) {
                    if (pagoLqui.getCajero() != null) {
                        cajero = cajeroService.find(pagoLqui.getCajero());
                        numeroComprobante = new BigInteger(this.services.getNumComprobante().toString());
                        pago = new FinaRenPago();
                        pago = recaudacionService.realizarPagoLiquidacion(liquidacion, this.procesarPago(liquidacion, numeroComprobante, pagoLqui, ipAddress), cajero, Boolean.TRUE);
//                        pago = this.procesarPago(liquidacion, numeroComprobante, pagoLqui);
                        recaudacionService.emitirFactura(pago);
                        System.out.println("comprobante>>" + numeroComprobante);
                        pagoLqui.setNumeroComprobante(numeroComprobante.toString());
                        pagoLqui.setEstadoPago("Pago satisfactorio");
                        pagoLqui.setEstadoCodigo(4);
                    } else {
                        pagoLqui.setEstadoPago("No existe cajero");
                        pagoLqui.setEstadoCodigo(3);
                    }
                } else {
                    pagoLqui.setEstadoPago("Valores incorrectos");
                    pagoLqui.setEstadoCodigo(2);
                }
            } else {
                pagoLqui.setEstadoPago("Codigo de liquidación incorrecto");
                pagoLqui.setEstadoCodigo(1);
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "pagoOnLine>>>", e);
            pagoLqui.setEstadoPago("Ocurrio un error al generar el pago");
            pagoLqui.setEstadoCodigo(0);
        }
        return pagoLqui;
    }

    private FinaRenPago procesarPago(FinaRenLiquidacion liquidacion, BigInteger numeroComprobante, PagoLiquidacionModel pagoLqui, String ipPago) {
        try {
            FinaRenPago pago = new FinaRenPago();
            FinaRenPagoDetalle detalle;
            List<FinaRenPagoDetalle> listDetPago = new ArrayList<>();
            BigDecimal valorPago = new BigDecimal("0.00");
            Banco bco = null;
            if (pagoLqui.getEntidadBancaria() != null) {
                bco = bancoService.find(pagoLqui.getEntidadBancaria());
            }
            detalle = new FinaRenPagoDetalle();
            detalle.setValor(liquidacion.getPagoFinal());
            liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
            detalle.setTcBanco(bco);
            detalle.setTipoPago(pagoLqui.getTipoPago());
            detalle.setTcTitular(pagoLqui.getTitularPago());
//            System.out.println("detalle>>"+detalle.toString());
            listDetPago.add(detalle);
            for (FinaRenPagoDetalle d : listDetPago) {
                valorPago = valorPago.add(d.getValor());
            }
            pago.setNumComprobante(numeroComprobante);
            pago.setRenPagoDetalles(listDetPago);
            pago.setValor(valorPago);
            pago.setObservacion(pagoLqui.getObservacion());
            pago.setIpUserSession(ipPago);
            return pago;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @POST
    @Path("iniciarTramite")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public TramiteRespuesta iniciarTramite(String json){
        try{
            SolicitudServiciosDTO solicitudDto = new Gson().fromJson(json, SolicitudServiciosDTO.class);
            tramite = new HistoricoTramites();
            TramiteRespuesta tr = validarSolicitud(solicitudDto);
            //SI ES DIFERENTE DE NULL EXISTEN ERRORES.
            if(tr != null){
                return tr;
            }
            String usuario = asignarUsuario();
            tramite.setTipoTramite(tramite.getServicio().getTipoTramite());
            tramite.setFechaIngreso(new Date());
            tramite.setPeriodo(Utils.getAnio(new Date()).shortValue());
            tramite.setFecha(new Date());
            tramite = this.saveTramite();
            if(tramite != null){
                userSession.setNameUser("admin");
                crearSolicitudesTramite(usuario, solicitudDto);
                this.setObservacion(new Observaciones());
                this.getObservacion().setObservacion("Inicio de Trámite en línea");
                this.getObservacion().setTarea("Inicio");
                this.saveObs();
                //HAY Q GUARDAR LOS DOCUMENTOS EN LA RUTA???
//                uploadDoc.upload(tramite, file);
            }
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDOK, "Trámite iniciado", solicitudDto); 
        }catch(JsonSyntaxException e){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDERROR, e.getMessage(), null);
        }
        catch(Exception ex){
            System.out.println("Exception iniciarTramite " + ex.getMessage());
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDERROR, ex.getMessage(), null);
        }
    }
    
    private String asignarUsuario(){
        List<UsuarioRol> users = ventanillaService.findAllUsuariosRolByDepartamento(tramite.getServicio().getDepartamento(), Boolean.TRUE, null, null, null);
        String usuario = "";
        if (!users.isEmpty()) {
            usuario = users.get(0).getUsuario().getUsuario();
        } else{
            List<UsuarioRol> usersDB = ventanillaService.findAllUsuariosRolByDepartamento(tramite.getServicio().getDepartamento(),null, null, null, null);
            if(usersDB!=null){
               usuario = usersDB.get(0).getUsuario().getUsuario();
            }
        }
         String[] codes = {"usuario", usuario};
            if (tramite.getTipoTramite().getUserDireccion() != null && tramite.getTipoTramite().getUserDireccion().trim().length() > 0) {
                // Si solo existe un solo valor envia como variable
                String[] temp = tramite.getTipoTramite().getUserDireccion().split(":");
                if (temp.length == 1) {
                    codes[0] = temp[0];
                } else {
                    codes = temp;
                    codes[1] = usuario;
                }
        }
        if (tramite.getTipoTramite().getAbreviatura().equals("PRORESOL")) {
            usuario = "";
            if (tramite.getServicio().getAbreviatura().equals("SETE") || tramite.getServicio().getAbreviatura().equals("SED")) {
                usuario = clienteService.getrolsUser(RolUsuario.abogadoResoucion1);
                this.getParamts().put("resolucion", usuario.equals("") ? "admin_1" : usuario);
                this.getParamts().put("usuario", session.getNameUser());
                this.getParamts().put("tipo", 1);
            } else {
                usuario = clienteService.getrolsUser(RolUsuario.jefeFinanciero);
                this.getParamts().put("financiero_revision", usuario.equals("") ? "admin_1" : usuario);
                this.getParamts().put("usuario", session.getNameUser());
                this.getParamts().put("tipo", 0);
            }
        } else {
            System.out.println("codes " + codes[0] + " " + codes[1]);
            this.getParamts().put(codes[0], codes[1]);
            this.getParamts().put("usuario", usuario);
        }
        return usuario;
    }
    
    private TramiteRespuesta validarSolicitud(SolicitudServiciosDTO s){
        if(s.getClienteId()==null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "La referencia del cliente es requerido", s); 
        }
        if(s.getServicioTipoId()==null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "La referencia del servicio es requerido", s); 
        }
        if(s.getLiquidacionId()==null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "La referencia de la liquidacion es requerido", s);
        }
        Cliente cliente = ventanillaService.findByNamedQuery1("Cliente.findById", s.getClienteId());
        if(cliente==null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "No existe un cliente con los datos enviados", s); 
        }
        servicioTipo = ventanillaService.findByNamedQuery1("ServicioTipo.findById", s.getServicioTipoId());
        if(servicioTipo==null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "No existe un servicio Tipo con los datos enviados", s); 
        }
        liquidacion = ventanillaService.findByNamedQuery1("FinaRenLiquidacion.findById", s.getLiquidacionId());
        if(liquidacion == null){
            return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "No existe una liquidacion con los datos enviados", s); 
        }
        if(servicioTipo.getServicio().getVinculadoPredio()!=null && servicioTipo.getServicio().getVinculadoPredio()){
            if(s.getPredio()==null){
                return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "Debe vincular un predio", s); 
            }
            predio = ventanillaService.findByNamedQuery1("CatPredio.findById", s.getPredio());
            if(predio == null) {
                return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "No existe un predio con los datos enviados", s); 
            }
        }
        Map<String, Object> paramsReq = new HashMap<>();
        paramsReq.put("servicio", servicioTipo.getId());
        listaRequisitos = ventanillaService.findAllQuery("SELECT s FROM ServicioRequisito s  WHERE servicioTipo.id=:servicio ORDER BY s.posicion", paramsReq);
        if(!Utils.isEmpty(listaRequisitos)){
            if(Utils.isEmpty(s.getSolicitudDocumento())){
                return new TramiteRespuesta(Constantes.ESTADOSOLICITUDRECHAZADO, "Existen requisitos que no han sido adjuntados", s);
            }
            solicitudDocumentos = new ArrayList<>();
            solicitudDocumentos = s.getSolicitudDocumento();
        }
        tramite.setSolicitante(cliente);
        tramite.setServicio(servicioTipo.getServicio());
        tramite.setTipoTramite(tramite.getServicio().getTipoTramite());
        return null;
    }
    
    private void crearSolicitudesTramite(String usuariosAsignados, SolicitudServiciosDTO solicitudDto) {
        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento();
        solicitudDepartamento.setEstado(Boolean.TRUE);
        solicitudDepartamento.setDepartamento(tramite.getServicio().getDepartamento());
        //CREANDO LA SOLICITUD SERVICIO
        solicitudDepartamento.setSolicitudServicio(crearSolicitudServicio(solicitudDto));
        //CREANDO LOS REQUISITOS QUE ESTAN Y NO EN LA SOLICITUD
        crearRegistroSolicitudRequisitos(solicitudDepartamento.getSolicitudServicio());
        solicitudDepartamento.setResponsables(usuariosAsignados);
        solicitudDepartamento.setFecha(new Date());
        //ACTUALIZANDO LA LIQUIDACION SI ES QUE TIENE ALGUNA TASA QUE COMPRAR
        if (liquidacion != null && liquidacion.getId() != null) {
            liquidacion.setCodigoVerificado(Boolean.TRUE);
            liquidacionService.edit(liquidacion);
        }
        solicitudDto.setTramiteId(tramite.getId());
    }
    
    private SolicitudServicios crearSolicitudServicio(SolicitudServiciosDTO solicitudDto) {
        SolicitudServicios solicitudServicios = new SolicitudServicios();
        solicitudServicios.setEnLinea(Boolean.TRUE);
        solicitudServicios.setSolicitudInterna(Boolean.TRUE);
        solicitudServicios.setDescripcionInconveniente(solicitudDto.getDescripcion());
        solicitudServicios.setFechaCreacion(new Date());
        solicitudServicios.setStatus("A");
        solicitudServicios.setServicioTipo(servicioTipo);
        solicitudServicios.setTramite(tramite);
        solicitudServicios.setEnteSolicitante(tramite.getSolicitante());
        solicitudServicios.setUsuarioIngreso(clienteService.getrolsUsuario("admin"));
        solicitudServicios.setReferenciaLiquidacion(liquidacion.getCodigoVerificador());
        solicitudServicios.setTipoContribuyente(servicioTipo.getTipoContribuyentes().getNombre());
        solicitudServicios.setFinalizado(Boolean.FALSE);
        solicitudServicios.setPredio(predio);
        solicitudServicios.setPendientePago(Boolean.FALSE);
        solicitudServicios.setEnObservacion(Boolean.FALSE);
        solicitudServicios = (SolicitudServicios) ventanillaService.save(solicitudServicios);
        return solicitudServicios;
    }
    
    private void crearRegistroSolicitudRequisitos(SolicitudServicios solicitudServicios) {
       if (!Utils.isEmpty(listaRequisitos) &&  !Utils.isEmpty(solicitudDocumentos)) {
           List<ServicioRequisito> requisitos = new ArrayList<>();
           for(ServicioRequisito sr : listaRequisitos) {
               for (SolicitudDocumentoDTO sDto : solicitudDocumentos) {
                   //SI EXISTE EL REQUISITO DENTRO DE LOS DOCUMENTOS ENVIADOS
                   if(sDto.getRequisitoId().equals(sr.getId())){
                       sr.setEntregado(Boolean.TRUE);
                       crearSolicitudDocumento(sDto, solicitudServicios.getId()); 
                       if(!requisitos.contains(sr)) requisitos.add(sr);
                   }else if(!requisitos.contains(sr)){
                       sr.setEntregado(Boolean.FALSE);
                       requisitos.add(sr);
                   }
               }
           }
           if(!Utils.isEmpty(requisitos)){
               for(ServicioRequisito r: requisitos){
                   RegistroSolicitudRequisitos rs = new RegistroSolicitudRequisitos(new SolicitudServicios(solicitudServicios.getId()),
                   r, Boolean.FALSE, r.getEntregado(), Boolean.TRUE, Boolean.FALSE, "", new Date(), session.getNameUser());
                   ventanillaService.save(rs);
               }
           }
           for (SolicitudDocumentoDTO data : solicitudDocumentos) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(tramite.getTipoTramite());
                objeto.setServicioRequisito(new ServicioRequisito(data.getRequisitoId()));
                tramiteRequisitoHistorialService.create(objeto);
            }
        }     
    }

    private void crearSolicitudDocumento(SolicitudDocumentoDTO documento, Long idSolicitud){
        SolicitudDocumento sd = new SolicitudDocumento();
        sd.setDescripcion(documento.getDescripcion());
        sd.setUsuario("");
        sd.setEstado(documento.getEstado());
        sd.setFechaCreacion(new Date());
        sd.setNombreArchivo(documento.getNombreArchivo());
        sd.setRutaArchivo(documento.getRutaArchivo());
        sd.setSolicitudServicio(new SolicitudServicios(idSolicitud));
        sd.setTipoArchivo(documento.getTipoArchivo());
        ventanillaService.save(sd);
    }
}
