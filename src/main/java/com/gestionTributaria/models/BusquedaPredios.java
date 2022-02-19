/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.*;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.*;
import com.origami.sigef.arrendamiento.entities.DetalleMercado;
import com.origami.sigef.arrendamiento.service.DetalleMercadoService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Administrator
 */
public abstract class BusquedaPredios {

    private static final Logger LOG = Logger.getLogger(BusquedaPredios.class.getName());

    @Inject
    private ManagerService services;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private InteresesService interesService;
    @Inject
    private CatPredioService predioService;
    @EJB
    private CatPredioPropietarioService propietarioService;
    @Inject
    private TipoLiquidacionService tipoLiquidacionService;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private MercadoService mercadoService;
    @Inject
    private DetalleMercadoService detalleMercadoService;

    protected List<FinaRenLiquidacion> emisionesPrediales;
    protected List<FnConvenioPagoDetalle> emisionesConvenios;
    protected List<FinaRenLiquidacion> emisionesPredialesTemp;
    protected CatPredio predioConsulta;
    protected List<CatPredio> prediosConsulta;
    protected PagoTituloReporteModel modelPago;
    protected Boolean pagoRealizado = Boolean.FALSE;
    protected BigDecimal totalEmisionesGeneral;
    protected BigDecimal totalEmisiones;
    protected BigDecimal totalCoactiva;
    protected Long tipoConsulta = 3L;
    protected CatPredioModel predioModel;
    protected Map<String, Object> paramtr;
    protected FnSolicitudExoneracion exoneracion;
    protected String mensajeExoneracion;
    protected Boolean variosPagos = Boolean.FALSE;
    protected Cliente contribuyenteConsulta;

    protected LazyModel<Cliente> propietarios;
    protected LazyModel<CatPredioRusticoDTO> propietariosRustico;

    protected Long tipoConsultaRural = 1L;
    protected List<CatPredioRusticoDTO> prediosRusticoConsulta;
    protected CatPredioRusticoDTO predioRuralConsulta;

    protected Boolean controlDocumento = Boolean.FALSE;
    protected Boolean esUrbano = Boolean.TRUE;

    protected String tabName;
    protected String nombreContribuyente;
    protected String identificacion;
    protected Long tipoBusqueda = 2L;
    protected String tipoPredio = "";
    protected List<DetalleMercado> detalleMercadoList;
    protected DetalleMercado detalleMercado;
    protected Mercado mercado;
    protected BigInteger nLocal;
    protected List<Mercado> mercadosList;
    /**
     * PARA SAN MIGUEL DEBE ESTAR EN isSanMiguel = Boolean.TRUE selectionMode =
     * "multiple";
     *
     */
    protected Boolean isSanMiguel = Boolean.FALSE;
    protected String selectionMode = "single";

    protected BigDecimal sumaTotalConv;
    protected List<FnConvenioPagoDetalle> convenidos;
    protected FnConvenioPagoDetalle cpd;

    protected List<FinaRenLiquidacion> cuotasPredios;

    protected Boolean tieneConvenio;

    protected List<FinaRenLiquidacion> liquidacionesCoactivaCuotas;
    protected List<FinaRenTipoLiquidacion> tipoLiquidaciones;
    protected FinaRenTipoLiquidacion tipoSelect;
    protected FinaRenLiquidacion liquidacionCoactivaCuota;
    protected BigDecimal sumaTotalCoactivaConv = new BigDecimal("0.00");
    protected List<FinaRenLiquidacion> liquidaionesCoactiva;

    public BusquedaPredios() {
        propietarios = new LazyModel<>(Cliente.class);
        predioModel = new CatPredioModel();
        modelPago = new PagoTituloReporteModel();
        tieneConvenio = Boolean.FALSE;
        mercadosList = new ArrayList<>();
        mercado = new Mercado();
    }
    
    public void consultarPrediosEmisiones() {
            FinaRenLiquidacion liq;
            emisionesPrediales = null;
            predioConsulta = null;
            prediosConsulta = null;
            //modelPago = new PagoTituloReporteModel();
            modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
            pagoRealizado = Boolean.FALSE;
            totalEmisionesGeneral = null;
            totalEmisiones = null;
            String claveCatastral = "";
            try {
                switch (this.tipoBusqueda.intValue()){
                    case 1:
                        tipoPredio = "";
                        break;
                    case 2:
                        tipoPredio = "U";
                        break;
                    case 3:
                        tipoPredio = "R";
                        break;
                }
                switch (tipoConsulta.intValue()) {
                    case 1://NUMERO PREDIAL
                        if (predioModel.getNumPredio() != null && predioModel.getNumPredio().compareTo(BigInteger.ZERO) > 0) {
                            prediosConsulta = predioService.findByNamedQuery("CatPredio.findByCodigoPredioAndTipo", new Object[]{predioModel.getNumPredio().toString(), tipoPredio});
                            if(Utils.isNotEmpty(prediosConsulta)){
                                predioConsulta = prediosConsulta.get(0);
                            }else{
                                JsfUtil.addErrorMessage("Predio no encontrado...", "");
                            }
                            
                        } else {
                            JsfUtil.addErrorMessage("Error", "Numero de Predio no es valido.");
                        }
                        break;
                    case 2://busqueda por contribuyente
                        String parametroBusqueda = "";
                        if (this.identificacion != null && this.identificacion.length() > 0) {
                            parametroBusqueda = identificacion.replace("-", "").replace(" ", "");
                            prediosConsulta = propietarioService.findByIdentificacionPropietario("%" + parametroBusqueda + "%");
                        } else if (nombreContribuyente != null && nombreContribuyente.length() > 0) {
                            parametroBusqueda = nombreContribuyente.trim().toUpperCase().replaceAll(" +", " ").replaceAll(" ", "%");
                            prediosConsulta = propietarioService.prediosNombrePropietario("%" + parametroBusqueda + "%");
                        } else {
                            JsfUtil.addErrorMessage("Error", "nombre o cedúla de contribuyente.");
                        }
                        if (Utils.isNotEmpty(prediosConsulta)) {
                            if (prediosConsulta.size() > 1) {//                        JsfUtil.update("pnListPredios");

                            } else {
                                predioConsulta = prediosConsulta.get(0);
                            }
                        }
                        break;
                    case 8:
                    case 3://busqueda por cedula catastral                        
                        claveCatastral = getClaveCat(tipoPredio);
                        if(!claveCatastral.isEmpty()){
                            prediosConsulta = predioService.findByNamedQuery("CatPredio.findByClaveCatAndTipo", new Object[]{claveCatastral, tipoPredio});
                            if (prediosConsulta != null && !prediosConsulta.isEmpty()) {
                                if (prediosConsulta.size() == 1) {
                                    predioConsulta = prediosConsulta.get(0);
                                }
                            } else {
                                JsfUtil.addWarningMessage("Predio no encontrado.", "");
                            }
                        }else{
                            JsfUtil.addWarningMessage("Predio no encontrado.", "");
                        }
                        break;
                    case 4://busqueda por codigo anterior
                        if (predioModel.getPredialAnt() != null) {
                            prediosConsulta = predioService.findByNamedQuery("CatPredio.findByPredialant", new Object[]{predioModel.getPredialAnt(),tipoPredio});
                            if (Utils.isNotEmpty(prediosConsulta)) {
                                predioConsulta = prediosConsulta.get(0);
                            }else{
                                 JsfUtil.addWarningMessage("Predio no encontrado.", "");
                            }
                        }
                        break;
                    case 5://busqueda por clave catastral
                        if (predioModel.getClaveCat() != null) {
                            prediosConsulta = predioService.findByNamedQuery("CatPredio.findByClaveCatAndTipo", new Object[]{predioModel.getClaveCat(), tipoPredio});
                            if (Utils.isNotEmpty(prediosConsulta)) {
                                predioConsulta = prediosConsulta.get(0);
                            }else{
                                JsfUtil.addWarningMessage("Predio no encontrado.", "");
                            }
                        }
                        break;
                    case 7:
                        this.emisionesPrediales = liquidacionService.getLiquidacionesMercado(mercado, nLocal.longValue(), detalleMercado);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
            }
        }
    
    public String getClaveCat(String tipo){
        if(tipo.equals("U")){
            if ((predioModel.getParroquia()!=null&&predioModel.getParroquia() > 0) || 
                    (predioModel.getSector()!=null&&predioModel.getSector() > 0) || 
                    (predioModel.getMz()!=null&&predioModel.getMz() > 0) || 
                    (predioModel.getSolar()!=null&&predioModel.getSolar() > 0) || 
                    (predioModel.getDiv1()!=null&&predioModel.getDiv1() >= 0) || 
                    (predioModel.getDiv2()!=null&&predioModel.getDiv2() >= 0) || 
                    (predioModel.getDiv3()!=null&&predioModel.getDiv3() >= 0) || 
                    (predioModel.getDiv4()!=null&&predioModel.getDiv4() >= 0) || 
                    (predioModel.getPhv()!=null&&predioModel.getPhv() >= 0) || 
                    (predioModel.getPhh()!=null&&predioModel.getPhh() >= 0)) {
                
                return  predioModel.getParroquia()+(".")+
                    predioModel.getSector()+(".")+
                    predioModel.getMz()+(".")+
                    predioModel.getSolar()+(".")+
                    predioModel.getDiv1()+(".")+
                    predioModel.getDiv2()+(".")+
                    predioModel.getDiv3()+(".")+
                    predioModel.getDiv4()+(".")+
                    predioModel.getPhv()+(".")+
                    predioModel.getPhh();
            }else{
                return "";
            }
            
        }else{
            if ((predioModel.getParroquia()!= null && predioModel.getParroquia() > 0) || 
                    (predioModel.getSector()!=null && predioModel.getSector() > 0) || 
                    (predioModel.getNumPredio()!=null && predioModel.getNumPredio().intValue() > 0) || 
                    (predioModel.getDiv1()!=null&&predioModel.getDiv1() >= 0) || 
                    (predioModel.getDiv2()!=null&&predioModel.getDiv2() >= 0)) {
                return  predioModel.getParroquia()+(".")+
                    predioModel.getSector()+(".")+
                    predioModel.getNumPredio()+(".")+
                    predioModel.getDiv1()+(".")+
                    predioModel.getDiv2();
            }else{
               return "" ;
            }
            
        }
    }
    
    public void consultarTipoLiquidacionesEmisiones() {
        try {
            /*comprobar si no tiene liquidación*/
            this.consultarPrediosEmisiones();
            switch(tipoConsulta.intValue()){
                case 8:
                    emisionesPrediales = liquidacionService.liquidacionePermisoFuncionamiento(predioConsulta, predioModel.getNumLocal());
                    break;
                default:
                    if (predioModel.getIdLiquidacion() == null || predioModel.getIdLiquidacion().isEmpty()) {                        
        //                System.out.println("predios Consultas>>" + prediosConsulta.toString() + " size>>" + prediosConsulta.size());
                        tipoLiquidaciones = new ArrayList<>();
                        if (prediosConsulta.size() > 1) {
                            for (CatPredio cp : prediosConsulta) {
                                List<FinaRenTipoLiquidacion> listTipoLiq = tipoLiquidacionService.tipoliquidacionespredio(cp);
                                for (FinaRenTipoLiquidacion t : listTipoLiq) {
                                    if (!tipoLiquidaciones.contains(t)) {
                                        tipoLiquidaciones.add(t);
                                    }
                                }
                            }
                        } else {
                            tipoLiquidaciones = tipoLiquidacionService.tipoliquidacionespredio(predioConsulta);
                        }
                    } else {
                        emisionesPrediales = liquidacionService.liquidacionesConsultaByIdLiquidacion(predioModel.getIdLiquidacion());
                    } 
                break;
            }

//            System.out.println("tipo liquidaciones>> " + tipoLiquidaciones.toString());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void getLiquidacionesCoactiva() {
        this.consultarPrediosEmisiones();
        liquidaionesCoactiva = new ArrayList<>();

    }

    public void consultarEmisiones() {
        FinaRenLiquidacion liq;

        emisionesPrediales = null;
        predioConsulta = null;
        prediosConsulta = null;
        //modelPago = new PagoTituloReporteModel();
        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());

        pagoRealizado = Boolean.FALSE;
        totalEmisionesGeneral = null;
        totalEmisiones = null;
        try {
            this.consultarPrediosEmisiones();
            if (predioConsulta != null) {
                if (tipoPredio.equals("U")) {
                    emisionesPrediales = liquidacionService.liquidacionesByPredio(predioConsulta, new FinaRenTipoLiquidacion(2L));
                } else if (tipoPredio.equals("R")){
                    emisionesPrediales = liquidacionService.liquidacionesByPredio(predioConsulta, new FinaRenTipoLiquidacion(3L));
                }else{
                    
                }
            }
            if (emisionesPrediales != null && Utils.isNotEmpty(emisionesPrediales)) {
                tipoSelect = emisionesPrediales.get(0).getTipoLiquidacion();
                // BUSCA SI TIENE SOLICITUD PENDIENTE DE EXONERACION --
                // BUSCA SOLICITUDES DE EXONERACION POR PREDIO EN ESTADO 1 o 2, DE LEY DEL ANCIANO O DISCAPACIDAD, (LA MAS ATUAL) --
                //SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC
                exoneracion = (FnSolicitudExoneracion) services.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{predioConsulta});

                if (exoneracion != null) {
                    //LA CONSULTA ANTERIOR VERIFICA QUE LOS ESTADOS SEAN (1,2)
                    if (exoneracion.getEstado().getId() == 3L || exoneracion.getEstado().getId() == 4L) {
                        exoneracion = null;
                        mensajeExoneracion = "";
                    } else {
                        Boolean tieneExo = Boolean.FALSE;
                        for (FnExoneracionLiquidacion exo : exoneracion.getFnExoneracionLiquidacionList()) {
                            if (exo.getEstado()) {
                                tieneExo = Boolean.TRUE;
                            }
                        }
                        if (tieneExo) {
                            mensajeExoneracion = "Tiene una exoneración de: " + exoneracion.getExoneracionTipo().getDescripcion().toUpperCase()
                                    + "\nNúmero de resolución: " + exoneracion.getNumResolucionSac();
                            //LA CONSULTA ANTERIOR VERIFICA QUE LOS TIPO SON (17,18,37,44)
                            if (exoneracion.getExoneracionTipo().getId() == 17L || exoneracion.getExoneracionTipo().getId() == 18L || exoneracion.getExoneracionTipo().getId() == 37L || exoneracion.getExoneracionTipo().getId() == 44L) {
                                //if(exoneracion.getExoneracionTipo().getId() == 17L || exoneracionSolicitud.getExoneracionTipo().getId() == 37L){
//                                JsfUtil.update("formMensajeExo");
//                                JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                            } else {
                                exoneracion = null;
                                mensajeExoneracion = "";
                            }
                        }

                    }
                }
                calculoTotalPago(emisionesPrediales, null);
                cuotasConvenio();
                cuotasCoactivaConvenio();
                totalEmisionesGeneral = new BigDecimal(totalEmisiones.toString());
                if (tieneConvenio) {
                    mensajeExoneracion = mensajeExoneracion + "  PREDIO TIENE CUOTAS DE CONVENIO";
//                    JsfUtil.update("formMensajeExo");
//                    JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                }
                // ENVIA MOSTRAR ADVERTENCIA SI EL PAGO YA FUE REALIZADO POR MEDIO DEL BANCO
                //NO SE SI SE USE
//                if (tipoLiquidacion.getId().equals(13L)) {
//                try {
//                    if (totalEmisionesGeneral.compareTo(BigDecimal.ZERO) > 0) {
//                        if (services.verificarPagoBanco(predioConsulta.getNumPredio())) {
//                            JsfUtil.executeJS("PF('dlgPagoBanco').show()");
//                        }
//                    }
//                } catch (Exception e) {
//                    LOG.log(Level.SEVERE, "se cayo este man...", e);
//                }
////                }

            } else {
                //DIALOGO DE SELECCION DE PREDIOS
                if (prediosConsulta == null && prediosConsulta.isEmpty()) {
                    JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                }
                if (prediosConsulta != null && prediosConsulta.size() > 1) {
//                    JsfUtil.update("frmPredios");
//                    JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                }
            }
            if (Utils.isEmpty(emisionesPrediales)) {
                JsfUtil.addInformationMessage("Mensaje", "Predio No posee Deuda");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }
    
    
    public void consultarEmisiones2() {
        FinaRenLiquidacion liq;

        emisionesPrediales = null;
        predioConsulta = null;
        prediosConsulta = null;
        //modelPago = new PagoTituloReporteModel();
        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());

        pagoRealizado = Boolean.FALSE;
        totalEmisionesGeneral = null;
        totalEmisiones = null;
        try {
            this.consultarPrediosEmisiones();
            if (predioConsulta != null) {
                if (tipoPredio.equals("U")) {
                    emisionesPrediales = liquidacionService.allLiquidacionesByPredio(predioConsulta, new FinaRenTipoLiquidacion(2L));
                } else if (tipoPredio.equals("R")){
                    emisionesPrediales = liquidacionService.allLiquidacionesByPredio(predioConsulta, new FinaRenTipoLiquidacion(3L));
                }else{
                    
                }
            }
            if (emisionesPrediales != null && Utils.isNotEmpty(emisionesPrediales)) {
                tipoSelect = emisionesPrediales.get(0).getTipoLiquidacion();
                // BUSCA SI TIENE SOLICITUD PENDIENTE DE EXONERACION --
                // BUSCA SOLICITUDES DE EXONERACION POR PREDIO EN ESTADO 1 o 2, DE LEY DEL ANCIANO O DISCAPACIDAD, (LA MAS ATUAL) --
                //SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC
                exoneracion = (FnSolicitudExoneracion) services.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{predioConsulta});

                if (exoneracion != null) {
                    //LA CONSULTA ANTERIOR VERIFICA QUE LOS ESTADOS SEAN (1,2)
                    if (exoneracion.getEstado().getId() == 3L || exoneracion.getEstado().getId() == 4L) {
                        exoneracion = null;
                        mensajeExoneracion = "";
                    } else {
                        Boolean tieneExo = Boolean.FALSE;
                        for (FnExoneracionLiquidacion exo : exoneracion.getFnExoneracionLiquidacionList()) {
                            if (exo.getEstado()) {
                                tieneExo = Boolean.TRUE;
                            }
                        }
                        if (tieneExo) {
                            mensajeExoneracion = "Tiene una exoneración de: " + exoneracion.getExoneracionTipo().getDescripcion().toUpperCase()
                                    + "\nNúmero de resolución: " + exoneracion.getNumResolucionSac();
                            //LA CONSULTA ANTERIOR VERIFICA QUE LOS TIPO SON (17,18,37,44)
                            if (exoneracion.getExoneracionTipo().getId() == 17L || exoneracion.getExoneracionTipo().getId() == 18L || exoneracion.getExoneracionTipo().getId() == 37L || exoneracion.getExoneracionTipo().getId() == 44L) {
                                //if(exoneracion.getExoneracionTipo().getId() == 17L || exoneracionSolicitud.getExoneracionTipo().getId() == 37L){
//                                JsfUtil.update("formMensajeExo");
//                                JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                            } else {
                                exoneracion = null;
                                mensajeExoneracion = "";
                            }
                        }

                    }
                }
                calculoTotalPago(emisionesPrediales, null);
                cuotasConvenio();
                cuotasCoactivaConvenio();
                totalEmisionesGeneral = new BigDecimal(totalEmisiones.toString());
                if (tieneConvenio) {
                    mensajeExoneracion = mensajeExoneracion + "  PREDIO TIENE CUOTAS DE CONVENIO";
//                    JsfUtil.update("formMensajeExo");
//                    JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                }
                // ENVIA MOSTRAR ADVERTENCIA SI EL PAGO YA FUE REALIZADO POR MEDIO DEL BANCO
                //NO SE SI SE USE
//                if (tipoLiquidacion.getId().equals(13L)) {
//                try {
//                    if (totalEmisionesGeneral.compareTo(BigDecimal.ZERO) > 0) {
//                        if (services.verificarPagoBanco(predioConsulta.getNumPredio())) {
//                            JsfUtil.executeJS("PF('dlgPagoBanco').show()");
//                        }
//                    }
//                } catch (Exception e) {
//                    LOG.log(Level.SEVERE, "se cayo este man...", e);
//                }
////                }

            } else {
                //DIALOGO DE SELECCION DE PREDIOS
                if (prediosConsulta == null && prediosConsulta.isEmpty()) {
                    JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                }
                if (prediosConsulta != null && prediosConsulta.size() > 1) {
//                    JsfUtil.update("frmPredios");
//                    JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                }
            }
            if (Utils.isEmpty(emisionesPrediales)) {
                JsfUtil.addInformationMessage("Mensaje", "Predio No posee Deuda");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void consultaLiquidacones() {
        emisionesPrediales = null;
        //modelPago = new PagoTituloReporteModel();
//        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
        pagoRealizado = Boolean.FALSE;
        totalEmisionesGeneral = null;
        totalEmisiones = null;
        try {
            //SI EL TIPO DE LIQUIDACIÓN ES CATASTRO PASADO LOS TREINTA DIAS
            if (tipoSelect.getId().equals(257L)) {
                emisionesPrediales = liquidacionService.liquidacionesByCuotasConvenio(predioConsulta, tipoSelect);
            } else {
                emisionesPrediales = liquidacionService.liquidacionesByPredio(predioConsulta, tipoSelect);
            }
            System.out.println("emisiones prediales>>>"+emisionesPrediales.size());
            if (emisionesPrediales != null && Utils.isNotEmpty(emisionesPrediales)) {

                // BUSCA SI TIENE SOLICITUD PENDIENTE DE EXONERACION --
                // BUSCA SOLICITUDES DE EXONERACION POR PREDIO EN ESTADO 1 o 2, DE LEY DEL ANCIANO O DISCAPACIDAD, (LA MAS ATUAL) --
                //SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC
                exoneracion = (FnSolicitudExoneracion) services.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{predioConsulta});

                if (exoneracion != null) {
                    //LA CONSULTA ANTERIOR VERIFICA QUE LOS ESTADOS SEAN (1,2)
                    if (exoneracion.getEstado().getId() == 3L || exoneracion.getEstado().getId() == 4L) {
                        exoneracion = null;
                        mensajeExoneracion = "";
                    } else {
                        Boolean tieneExo = Boolean.FALSE;
                        for (FnExoneracionLiquidacion exo : exoneracion.getFnExoneracionLiquidacionList()) {
                            if (exo.getEstado()) {
                                tieneExo = Boolean.TRUE;
                            }
                        }
                        if (tieneExo) {
                            mensajeExoneracion = "Tiene una exoneración de: " + exoneracion.getExoneracionTipo().getDescripcion().toUpperCase()
                                    + "\nNúmero de resolución: " + exoneracion.getNumResolucionSac();
                            //LA CONSULTA ANTERIOR VERIFICA QUE LOS TIPO SON (17,18,37,44)
                            if (exoneracion.getExoneracionTipo().getId() == 17L || exoneracion.getExoneracionTipo().getId() == 18L || exoneracion.getExoneracionTipo().getId() == 37L || exoneracion.getExoneracionTipo().getId() == 44L) {
                                //if(exoneracion.getExoneracionTipo().getId() == 17L || exoneracionSolicitud.getExoneracionTipo().getId() == 37L){
//                                JsfUtil.update("formMensajeExo");
//                                JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                            } else {
                                exoneracion = null;
                                mensajeExoneracion = "";
                            }
                        }

                    }
                }
//                System.out.println("size()>>" + emisionesPrediales.size());
                calculoTotalPago(emisionesPrediales, null);
                cuotasConvenio();
                cuotasCoactivaConvenio();
                totalEmisionesGeneral = new BigDecimal(totalEmisiones.toString());
                if (tieneConvenio) {
                    mensajeExoneracion = mensajeExoneracion + "  PREDIO TIENE CUOTAS DE CONVENIO";
//                    JsfUtil.update("formMensajeExo");
//                    JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    /*Trae las liquidaciones pagadas y no pagadas*/
    public void consultarLiquidaciones() {
        emisionesPrediales = null;
        pagoRealizado = Boolean.FALSE;
        totalEmisionesGeneral = null;
        totalEmisiones = null;
        try {
            //SI EL TIPO DE LIQUIDACIÓN ES CATASTRO PASADO LOS TREINTA DIAS
            if (tipoSelect.getId().equals(257L)) {
                emisionesPrediales = liquidacionService.allLiquidacionesByCuotasConvenio(predioConsulta, tipoSelect);
            } else {
                emisionesPrediales = liquidacionService.allLiquidacionesByPredio(predioConsulta, tipoSelect);
            }
            System.out.println("emisiones prediales>>>"+emisionesPrediales.size());
            if (emisionesPrediales != null && Utils.isNotEmpty(emisionesPrediales)) {

                // BUSCA SI TIENE SOLICITUD PENDIENTE DE EXONERACION --
                // BUSCA SOLICITUDES DE EXONERACION POR PREDIO EN ESTADO 1 o 2, DE LEY DEL ANCIANO O DISCAPACIDAD, (LA MAS ATUAL) --
                //SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC
                exoneracion = (FnSolicitudExoneracion) services.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{predioConsulta});

                if (exoneracion != null) {
                    //LA CONSULTA ANTERIOR VERIFICA QUE LOS ESTADOS SEAN (1,2)
                    if (exoneracion.getEstado().getId() == 3L || exoneracion.getEstado().getId() == 4L) {
                        exoneracion = null;
                        mensajeExoneracion = "";
                    } else {
                        Boolean tieneExo = Boolean.FALSE;
                        for (FnExoneracionLiquidacion exo : exoneracion.getFnExoneracionLiquidacionList()) {
                            if (exo.getEstado()) {
                                tieneExo = Boolean.TRUE;
                            }
                        }
                        if (tieneExo) {
                            mensajeExoneracion = "Tiene una exoneración de: " + exoneracion.getExoneracionTipo().getDescripcion().toUpperCase()
                                    + "\nNúmero de resolución: " + exoneracion.getNumResolucionSac();
                            //LA CONSULTA ANTERIOR VERIFICA QUE LOS TIPO SON (17,18,37,44)
                            if (exoneracion.getExoneracionTipo().getId() == 17L || exoneracion.getExoneracionTipo().getId() == 18L || exoneracion.getExoneracionTipo().getId() == 37L || exoneracion.getExoneracionTipo().getId() == 44L) {
                            } else {
                                exoneracion = null;
                                mensajeExoneracion = "";
                            }
                        }

                    }
                }
                calculoTotalPago(emisionesPrediales, null);
                cuotasConvenio();
                cuotasCoactivaConvenio();
                totalEmisionesGeneral = new BigDecimal(totalEmisiones.toString());
                if (tieneConvenio) {
                    mensajeExoneracion = mensajeExoneracion + "  PREDIO TIENE CUOTAS DE CONVENIO";
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void calculoTotalPago(List<FinaRenLiquidacion> listado, Date fechaPago) {
        Boolean flag = true;
        totalEmisiones = new BigDecimal("0.00");
        for (FinaRenLiquidacion e : listado) {
            if (tipoSelect != null && (tipoSelect.getId().equals(2L) || tipoSelect.getId().equals(3L))) {
                // Pregunta por el año actual, si ya fue exonerado y si se encontró la solicitud de exoneración en el anterior método.
                if (Objects.equals(e.getAnio(), Utils.getAnio(new Date())) && e.getEstaExonerado() && exoneracion != null) {
                    exoneracion = null;
                }
                //

                if (flag && e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    if (e.getEstadoCoactiva() == 2) {
                        flag = false;
//                    JsfUtil.executeJS("PF('dlgMensaje').show();");
                    }
                }
//                System.out.println("lista ren_pago>>" + Utils.isNotEmpty(e.getRenPagoCollection()));
                if (e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    try {
                        if(e.getTipoLiquidacion().getId().equals(2L) || e.getTipoLiquidacion().getId().equals(3L)){
                            e = recaudacionService.realizarDescuentoRecargaInteresPredial(e, fechaPago);
                        }
                        e.calcularPagoConCoactiva();
                        totalEmisiones = totalEmisiones.add(e.getPagoFinal());
                        totalEmisiones = totalEmisiones.setScale(2, RoundingMode.HALF_UP);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, null, e);
                    }
                }
            } else {
                e = recaudacionService.realizarDescuentoRecargaInteresPredial(e, fechaPago);
                e.calcularPagoConCoactiva();
                totalEmisiones = totalEmisiones.add(e.getPagoFinal()); 
                
            }
            this.totalCoactiva = BigDecimal.ZERO;
            this.totalCoactiva = this.totalCoactiva.add(e.getValorCoactiva() != null ? e.getValorCoactiva() : BigDecimal.ZERO);
        }
    }

    public void onChangeTab(TabChangeEvent event) {
        this.tabName = event.getTab().getId().toString();
        if (tabName.equals("tabPagoPredial") || tabName.equals("pagoPredialRural")) {
            emisionesPrediales = null;
            predioModel = new CatPredioModel();
        } else {
            predioModel = new CatPredioModel();
        }
    }

    public void onChangeRadio() {

        //emisionesPrediales = null;
        predioModel = new CatPredioModel();
        predioConsulta = null;
        totalEmisiones = new BigDecimal("0.00");
    }

    public void handleFileUpload(FileUploadEvent event) {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
//            Date d = new Date();
//            int numero = 0;
//            String rutaArchivo = SisVars.rutaRepotiorioArchivo + d.getTime() + event.getFile().getFileName();
//            File file = new File(rutaArchivo);
//            InputStream is;
//            is = event.getFile().getInputstream();
//            OutputStream out = new FileOutputStream(file);
//            byte buf[] = new byte[1024];
//            int len;
//            while ((len = is.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            is.close();
//            out.close();
//            Archivo documento = new Archivo();
//            documento.setNombre(d.getTime() + "_" + event.getFile().getFileName());
//
//            documento.setTipo(event.getFile().getContentType());
//            documento.setRuta(rutaArchivo);
//
//            controlDocumento = Boolean.TRUE;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarEmisionesPendientesPago() {
        emisionesPrediales = null;
        predioConsulta = null;
        prediosConsulta = null;
        //modelPago = new PagoTituloReporteModel();
        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());

        pagoRealizado = Boolean.FALSE;
        totalEmisionesGeneral = null;
        totalEmisiones = null;
        try {
            switch (tipoConsulta.intValue()) {
                case 1://NUMERO PREDIAL
                    if (predioModel.getNumPredio() != null && predioModel.getNumPredio().compareTo(BigInteger.ZERO) > 0) {
                        paramtr = new HashMap<>();
//                        paramtr.put("numPredio", predioModel.getNumPredio());
//                        paramtr.put("estado", "A");
//                        predioConsulta = (CatPredioModel) services.buscarNumPredio(predioModel.getNumPredio());
                    } else {
                        JsfUtil.addErrorMessage("Error", "Numero de Predio no es valido.");
                    }
                    break;
                case 2://CONTRIBUYENTE
                    if (contribuyenteConsulta != null) {
                        List<CatPredioRusticoDTO> listaPrediosrusticoPropietarios = services.predioRusticoPropuietarios(contribuyenteConsulta.getIdentificacion());
                        if (listaPrediosrusticoPropietarios != null && !listaPrediosrusticoPropietarios.isEmpty()) {
                            if (listaPrediosrusticoPropietarios.size() == 1) {
                                paramtr = new HashMap<>();
//                                paramtr.put("numPredio", contribuyenteConsulta.getCatPredioPropietarioCollection().get(0).getPredio().getNumPredio());
//                                paramtr.put("estado", "A");
//                                predioConsulta = (CatPredioModel) services.buscarNumPredio(listaPrediosrusticoPropietarios.get(0).getNumPredioRustico());
                            } else {
                                prediosConsulta = new ArrayList<>();
//                                for (CatPredioPropieatrioDTO p : listaPrediosrusticoPropietarios) {
//                                    if (p.getPredio().getPropiedadHorizontal() == null || !p.getPredio().getPropiedadHorizontal()) //PARA Q NO APARESCAN LAS PROPIEDADES HORIZONTALES
//                                    {
//                                        prediosConsulta.add(p.getPredio());
//                                    }
//                                }
                            }
                        } else {
                            JsfUtil.addInformationMessage("Contribuyente no posee Predios", "");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Realice la busqueda del Contribuyente.");
                    }
                    break;
                case 3://CODIGO PREDIAL
                    if (predioModel.getSector() > 0 || predioModel.getMz() > 0 || predioModel.getProvincia() > 0 || predioModel.getCanton() > 0
                            || predioModel.getParroquiaShort() > 0 || predioModel.getZona() > 0 || predioModel.getSolar() > 0 || predioModel.getPiso() >= 0
                            || predioModel.getUnidad() >= 0 || predioModel.getBloque() >= 0) {
                        paramtr = new HashMap<>();
//                        paramtr.put("estado", "A");
//                        paramtr.put("sector", predioModel.getSector());
//                        paramtr.put("mz", predioModel.getMz());
//                        paramtr.put("provincia", predioModel.getProvincia());
//                        paramtr.put("canton", predioModel.getCanton());
//                        paramtr.put("parroquia", predioModel.getParroquiaShort());
//                        paramtr.put("zona", predioModel.getZona());
//                        paramtr.put("solar", predioModel.getSolar());
//                        paramtr.put("piso", predioModel.getPiso());
//                        paramtr.put("unidad", predioModel.getUnidad());
//                        paramtr.put("bloque", predioModel.getBloque());

//                        prediosConsulta = services.busquedaPredioDivisiones("A", predioModel.getSector(), predioModel.getMz(), predioModel.getProvincia(), predioModel.getCanton(),
//                                predioModel.getParroquiaShort(), predioModel.getZona(), predioModel.getSolar(), predioModel.getPiso(), predioModel.getUnidad(), predioModel.getBloque());
                        if (prediosConsulta != null && !prediosConsulta.isEmpty()) {
                            if (prediosConsulta.size() == 1) {
                                predioConsulta = prediosConsulta.get(0);
                            }
                        } else {
                            JsfUtil.addInformationMessage("Predio no encontrado.", "");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Codigo Predial no es valido.");
                    }
                    break;
                case 4:
                    if (predioModel.getCiudadela() != null && predioModel.getMzUrb() != null && predioModel.getSlUrb() != null) {
                        paramtr = new HashMap<>();
//                        paramtr.put("estado", "A");
//                        paramtr.put("ciudadela", predioModel.getCiudadela());
//                        paramtr.put("urbMz", predioModel.getMzUrb());
//                        paramtr.put("urbSolarnew", predioModel.getSlUrb());
//                        prediosConsulta = services.busquedadPredioPocosParametros("A", "", predioModel.getMzUrb(), predioModel.getSlUrb());
                        if (prediosConsulta != null && !prediosConsulta.isEmpty()) {
                            if (prediosConsulta.size() == 1) {
                                predioConsulta = prediosConsulta.get(0);
                            }
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Datos no validos para la Consulta");
                    }
                    break;
                case 5://CODIGO ANTERIOR
                    if (predioModel.getPredialAnt() != null) {
                        paramtr = new HashMap<>();
                        paramtr.put("estado", "A");
                        paramtr.put("predialant", predioModel.getPredialAnt());
//                        prediosConsulta = services.buscarCodigoAnterio(predioModel.getPredialAnt(), "A");
                        if (prediosConsulta != null && !prediosConsulta.isEmpty()) {
                            if (prediosConsulta.size() == 1) {
                                predioConsulta = prediosConsulta.get(0);
                            }
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Datos no validos para la Consulta");
                    }
                    break;
                case 6:
                    if (predioModel.getClaveCat() != null) {
                        paramtr = new HashMap<>();
                        paramtr.put("claveCat", predioModel.getClaveCat());
                        paramtr.put("estado", "A");
//                        prediosConsulta = services.buscarClavAnterior(predioModel.getClaveCat(), "A");
                        if (prediosConsulta == null && prediosConsulta.isEmpty()) {
                            JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                        } else {
                            predioConsulta = prediosConsulta.get(0);
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Datos no validos para la Consulta");
                    }
                    break;
                default:
                    break;
            }
            if (predioConsulta != null) {
                paramtr = new HashMap<>();

                paramtr.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
                paramtr.put("predioRustico", predioConsulta);
                emisionesPrediales = services.findAllQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio AND r.estadoLiquidacion IN (1, 2, 7) ORDER BY r.anio ASC",
                        paramtr);

                // BUSCA SI TIENE SOLICITUD PENDIENTE DE EXONERACION --JOAO
                // BUSCA SOLICITUDES DE EXONERACION POR PREDIO EN ESTADO 1 o 2, DE LEY DEL ANCIANO O DISCAPACIDAD, (LA MAS ATUAL) --HENRY
                exoneracion = (FnSolicitudExoneracion) services.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2)"
                        + " ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{predioConsulta});

                if (exoneracion != null) {
                    //LA CONSULTA ANTERIOR VERIFICA QUE LOS ESTADOS SEAN (1,2)
                    if (exoneracion.getEstado().getId() == 3L || exoneracion.getEstado().getId() == 4L) {
                        exoneracion = null;
                        mensajeExoneracion = null;
                    } else {
                        Boolean tieneExo = Boolean.FALSE;
                        for (FnExoneracionLiquidacion exo : exoneracion.getFnExoneracionLiquidacionList()) {
                            if (exo.getEstado()) {
                                tieneExo = Boolean.TRUE;
                            }
                        }
                        if (tieneExo) {
                            mensajeExoneracion = "Tiene una exoneración de: " + exoneracion.getExoneracionTipo().getDescripcion().toUpperCase()
                                    + "\nNúmero de resolución: " + exoneracion.getNumResolucionSac();
                            //LA CONSULTA ANTERIOR VERIFICA QUE LOS TIPO SON (17,18,37,44)
                            if (exoneracion.getExoneracionTipo().getId() == 17L || exoneracion.getExoneracionTipo().getId() == 18L || exoneracion.getExoneracionTipo().getId() == 37L || exoneracion.getExoneracionTipo().getId() == 44L) {
                                //if(exoneracion.getExoneracionTipo().getId() == 17L || exoneracionSolicitud.getExoneracionTipo().getId() == 37L){
//                                JsfUtil.update("formMensajeExo");
//                                JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                            } else {
                                exoneracion = null;
                                mensajeExoneracion = null;
                            }
                        }

                    }
                }
                calculoTotalPago(emisionesPrediales, null);
                totalEmisionesGeneral = new BigDecimal(totalEmisiones.toString());
                // ENVIA MOSTRAR ADVERTENCIA SI EL PAGO YA FUE REALIZADO POR MEDIO DEL BANCO
//                if (tipoLiquidacion.getId().equals(13L)) {
                try {
                    if (totalEmisionesGeneral.compareTo(BigDecimal.ZERO) > 0) {
                        if (services.verificarPagoBanco(new BigInteger(predioConsulta.getId().toString()))) {
//                            JsfUtil.executeJS("PF('dlgPagoBanco').show()");
                        }
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, null, e);
                }
//                }

            } else {
                //DIALOGO DE SELECCION DE PREDIOS
                if (prediosConsulta == null && prediosConsulta.isEmpty()) {
                    JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                }
                if (prediosConsulta != null && prediosConsulta.size() > 1) {
//                    JsfUtil.update("frmPredios");
//                    JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                }
            }
            if (emisionesPrediales.isEmpty()) {
                JsfUtil.addInformationMessage("Mensaje", "Predio No posee Deuda");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void cuotasConvenio() {
        try {

            if (predioConsulta != null) {
                sumaTotalConv = BigDecimal.ZERO;
                paramtr = new HashMap<>();
                paramtr.put("predio", predioConsulta);

                List<FnConvenioPago> convenioPagos = (List<FnConvenioPago>) services.findAllQuery("SELECT  e from FnConvenioPago e where e.predio = :predio",
                        paramtr);
                if (Utils.isNotEmpty(convenioPagos)) {
                    convenidos = new ArrayList();
                    for (FnConvenioPago fcp : convenioPagos) {
                        if (Utils.isNotEmpty(fcp.getCuotasConvenio())) {
                            convenidos.addAll(fcp.getCuotasConvenio());
                        } else {
                            JsfUtil.addErrorMessage("Info", "No se han Elaborado las Coutas de Convenios");
                        }
                    }
                    if (Utils.isNotEmpty(convenidos)) {
                        for (FnConvenioPagoDetalle fcpd : convenidos) {
                            if (fcpd.getLiquidacion().getEstadoLiquidacion().getId().equals(8L)) {
                                if (fcpd.getFechaMaximaPago().before(new Date())) {
                                    sumaTotalConv = sumaTotalConv.add(fcpd.getDeuda());
                                }
                            }
                        }
                        tieneConvenio = Boolean.TRUE;
                    }
                }
                totalEmisiones = totalEmisiones.add(sumaTotalConv);
//                JsfUtil.update("mainForm:tvRecaudaciones:tvRecaudacionesPredios:panelConvenios");
            } else {
                JsfUtil.addErrorMessage("Info", "Debe Seleccionar Un predio");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cuotasCoactivaConvenio() {
        try {
            sumaTotalCoactivaConv = BigDecimal.ZERO;

            if (predioConsulta != null) {
                paramtr = new HashMap<>();
                paramtr.put("predio", predioConsulta);
                paramtr.put("tipoLiq", new FinaRenTipoLiquidacion(281L));

                liquidacionesCoactivaCuotas = services.findAllQuery("Select r from FinaRenLiquidacion r where r.predio = :predio and r.tipoLiquidacion = :tipoLiq ", paramtr);
                if (Utils.isNotEmpty(liquidacionesCoactivaCuotas)) {
                    for (FinaRenLiquidacion rl : liquidacionesCoactivaCuotas) {
                        rl.calcularPagoAP();
                        rl.calcularPagoAPConCoactiva();
                        sumaTotalCoactivaConv = sumaTotalCoactivaConv.add(rl.getPagoFinal());
                    }
                    totalEmisiones = totalEmisiones.add(sumaTotalCoactivaConv);
                }

//                JsfUtil.update("mainForm:tvRecaudaciones:tvRecaudacionesPredios:panelCoactivaConvenios");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onSelectCuotaCoactiva(SelectEvent event) {
        sumaTotalCoactivaConv = BigDecimal.ZERO;
        cuotasPredios = new ArrayList<>();
        sumaTotalCoactivaConv = sumaTotalCoactivaConv.add(liquidacionCoactivaCuota.getPagoFinal());
        cuotasPredios.add(liquidacionCoactivaCuota);
//        JsfUtil.update("mainForm:tvRecaudaciones:tvRecaudacionesPredios:panelCoactivaConvenios");
    }

    public void onSelectCuota(SelectEvent event) {
        cpd = (FnConvenioPagoDetalle) event.getObject();
        cuotasPredios = new ArrayList<>();
        sumaTotalConv = new BigDecimal("0.00");
        for (FnConvenioPagoDetalle d : convenidos) {
            if (d.getMes() <= cpd.getMes()) {
                if (d.getLiquidacion().getEstadoLiquidacion().getId() == 8L) {
                    cuotasPredios.add(d.getLiquidacion());
                    sumaTotalConv = sumaTotalConv.add(d.getDeuda());
                }
            }
        }
//        JsfUtil.update("mainForm:tvRecaudaciones:tvRecaudacionesPredios:panelConvenios");
    }

    public void seleccionarPredio(Long tipo) {
        //aqui va el codigo pilas...
        try {
            List<FinaRenLiquidacion> emisionesPredialesTemp = new ArrayList<>();
            switch (tipo.intValue()) {
                case 1: // PREDIO URBANO INDIVIDUAL
                    if (getPredioConsulta() != null) {
                        setEmisionesPrediales(liquidacionService.liquidacionesConsultaByTipoPredio(predioConsulta, new FinaRenEstadoLiquidacion(2L), new FinaRenTipoLiquidacion(2L)));
                        calculoTotalPago(getEmisionesPrediales(), null);
//                        JsfUtil.executeJS("PF('dlgPrediosConsulta').hide();");
                    } else {
                        JsfUtil.addWarningMessage("Mensaje", "Seleccione un predio, luego clic en Seleccionar");
                    }
                    break;
                case 2: // PREDIO RURAL INDIVIDUAL
                    if (getPredioConsulta() != null) {
                        setEmisionesPrediales(liquidacionService.liquidacionesConsultaByTipoPredio(predioConsulta, new FinaRenEstadoLiquidacion(2L), new FinaRenTipoLiquidacion(3L)));
                        calculoTotalPago(getEmisionesPrediales(), null);
//                        JsfUtil.executeJS("PF('dlgPrediosRuralConsulta').hide();");
                    } else {
                        JsfUtil.addWarningMessage("Mensaje", "Seleccione un predio, luego clic en Seleccionar");
                    }
                    break;
                case 3: // TODAS LA EMISIONES URBANAS QUE TENGA EL CONTRIBUYENTE
                    List<FinaRenLiquidacion> tempEmisiones = new ArrayList<>();
                    FinaRenTipoLiquidacion tipoPredio = null;
                    for (CatPredio catPredio : this.getPrediosConsulta()) {
                        if (catPredio.getTipoPredio().equals("U")) {
                            tipoPredio = new FinaRenTipoLiquidacion(2L);
                        } else {
                            tipoPredio = new FinaRenTipoLiquidacion(3L);
                        }
                        emisionesPredialesTemp = liquidacionService.liquidacionesConsultaByTipoPredio(catPredio, new FinaRenEstadoLiquidacion(2L), tipoPredio);
                        if (Utils.isNotEmpty(emisionesPredialesTemp)) {
                            tempEmisiones.addAll(emisionesPredialesTemp);
                        }
                    }
                    setEmisionesPrediales(tempEmisiones);
                    calculoTotalPago(getEmisionesPrediales(), null);
//                    JsfUtil.executeJS("PF('dlgPrediosConsulta').hide();");
                    break;
                default:
                    break;
            }
            if(getEmisionesPrediales().isEmpty()){
                this.cuotaInicial();
                this.cuotasConveniosLiquidacion();
            }
            if(tieneConvenio){
                JsfUtil.addSuccessMessage("CONVENIO", "Predio posee cuotas de Convenios pendientes de pago...");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void cuotasConveniosLiquidacion(){
        if(getPredioConsulta()!=null){
            emisionesPrediales = liquidacionService.getCuotaDeralleConvenioByPredio(predioConsulta, 8L);
            if(!emisionesPrediales.isEmpty()){
                tieneConvenio = Boolean.TRUE;
            }
        }
    }
    
    public void cuotaInicial(){
        if(getPredioConsulta()!=null){
            if(getTipoSelect()!=null){
                emisionesPrediales = liquidacionService.liquidacionesConsultaByTipoPredio(predioConsulta, new FinaRenEstadoLiquidacion(2L), tipoSelect);
            }else{
                emisionesPrediales.addAll(liquidacionService.liquidacionesConsultaByTipoPredio(predioConsulta, new FinaRenEstadoLiquidacion(2L), new FinaRenTipoLiquidacion(256L)));
            }
        }
    }
    
    public void initModelPago(){
        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
    }
    
    public void onRowSelect(SelectEvent event) {
        FinaRenLiquidacion index = emisionesPredialesTemp.get(0);
        emisionesPredialesTemp.clear();
        totalEmisiones = BigDecimal.ZERO;
        for (int i = 0; i <= emisionesPrediales.indexOf(index); i++) {
            emisionesPredialesTemp.add(emisionesPrediales.get(i));
            totalEmisiones = emisionesPrediales.get(i).getPagoFinal().add(totalEmisiones);
        }
//        totalEmisiones = emisionesPredialesTemp.stream().map(FinaRenLiquidacion::getPagoFinal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void onRowUnselect(UnselectEvent event) {
        FinaRenLiquidacion index = emisionesPredialesTemp.get(0);
        emisionesPredialesTemp.clear();
        totalEmisiones = BigDecimal.ZERO;
        for (int i = 0; i <= emisionesPrediales.indexOf(index); i++) {
            emisionesPredialesTemp.add(emisionesPrediales.get(i));
            totalEmisiones = emisionesPrediales.get(i).getPagoFinal().add(totalEmisiones);
        }
//        totalEmisiones = emisionesPredialesTemp.stream().map(FinaRenLiquidacion::getPagoFinal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void onRowToggle() {
        FinaRenLiquidacion index = emisionesPredialesTemp.get(0);
        emisionesPredialesTemp.clear();
        totalEmisiones = BigDecimal.ZERO;
        for (int i = 0; i <= emisionesPrediales.indexOf(index); i++) {
            emisionesPredialesTemp.add(emisionesPrediales.get(i));
            totalEmisiones = emisionesPrediales.get(i).getPagoFinal().add(totalEmisiones);
        }
//        totalEmisiones = emisionesPredialesTemp.stream().map(FinaRenLiquidacion::getPagoFinal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void consultaConvenios(){
        FinaRenLiquidacion index = emisionesPredialesTemp.get(0);
        emisionesPredialesTemp.clear();
        totalEmisiones = BigDecimal.ZERO;
        for (int i = 0; i <= emisionesPrediales.indexOf(index); i++) {
            emisionesPredialesTemp.add(emisionesPrediales.get(i));
            totalEmisiones = emisionesPrediales.get(i).getPagoFinal().add(totalEmisiones);
        }
    }
    
    public void loadMercados(){
        if(this.tipoConsulta.equals(7L)){
            this.mercadosList = mercadoService.getMercadosActivos();    
        }
    }
    
    public void loadDetalleMercado(){
        try {
            detalleMercadoList = new ArrayList<>();
            if(this.mercado!=null && this.mercado.getId()!=null){
                this.detalleMercadoList = detalleMercadoService.getDetalle(this.mercado);    
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
//<editor-fold defaultstate="collapsed" desc="getter and setter">

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public List<DetalleMercado> getDetalleMercadoList() {
        return detalleMercadoList;
    }

    public void setDetalleMercadoList(List<DetalleMercado> detalleMercadoList) {
        this.detalleMercadoList = detalleMercadoList;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
    }

    public List<Mercado> getMercadosList() {
        return mercadosList;
    }

    public void setMercadosList(List<Mercado> mercadosList) {
        this.mercadosList = mercadosList;
    }

    public List<FinaRenLiquidacion> getEmisionesPredialesTemp() {
        return emisionesPredialesTemp;
    }

    public void setEmisionesPredialesTemp(List<FinaRenLiquidacion> emisionesPredialesTemp) {
        this.emisionesPredialesTemp = emisionesPredialesTemp;
    }

    public List<FinaRenLiquidacion> getEmisionesPrediales() {
        return emisionesPrediales;
    }

    public void setEmisionesPrediales(List<FinaRenLiquidacion> emisionesPrediales) {
        this.emisionesPrediales = emisionesPrediales;
    }

    public List<FnConvenioPagoDetalle> getEmisionesConvenios() {
        return emisionesConvenios;
    }

    public void setEmisionesConvenios(List<FnConvenioPagoDetalle> emisionesConvenios) {
        this.emisionesConvenios = emisionesConvenios;
    }

    public CatPredio getPredioConsulta() {
        return predioConsulta;
    }

    public void setPredioConsulta(CatPredio predioConsulta) {
        this.predioConsulta = predioConsulta;
    }

    public List<CatPredio> getPrediosConsulta() {
        return prediosConsulta;
    }

    public void setPrediosConsulta(List<CatPredio> prediosConsulta) {
        this.prediosConsulta = prediosConsulta;
    }

    public PagoTituloReporteModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoTituloReporteModel modelPago) {
        this.modelPago = modelPago;
    }

    public Boolean getPagoRealizado() {
        return pagoRealizado;
    }

    public void setPagoRealizado(Boolean pagoRealizado) {
        this.pagoRealizado = pagoRealizado;
    }

    public BigDecimal getTotalEmisionesGeneral() {
        return totalEmisionesGeneral;
    }

    public void setTotalEmisionesGeneral(BigDecimal totalEmisionesGeneral) {
        this.totalEmisionesGeneral = totalEmisionesGeneral;
    }

    public BigDecimal getTotalEmisiones() {
        return totalEmisiones;
    }

    public void setTotalEmisiones(BigDecimal totalEmisiones) {
        this.totalEmisiones = totalEmisiones;
    }

    public Long getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(Long tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

    public Map<String, Object> getParamtr() {
        return paramtr;
    }

    public void setParamtr(Map<String, Object> paramtr) {
        this.paramtr = paramtr;
    }

    public FnSolicitudExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(FnSolicitudExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public String getMensajeExoneracion() {
        return mensajeExoneracion;
    }

    public void setMensajeExoneracion(String mensajeExoneracion) {
        this.mensajeExoneracion = mensajeExoneracion;
    }

    public Boolean getVariosPagos() {
        return variosPagos;
    }

    public void setVariosPagos(Boolean variosPagos) {
        this.variosPagos = variosPagos;
    }

    public Cliente getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(Cliente contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public LazyModel<Cliente> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(LazyModel<Cliente> propietarios) {
        this.propietarios = propietarios;
    }

    public LazyModel<CatPredioRusticoDTO> getPropietariosRustico() {
        return propietariosRustico;
    }

    public Long getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Long tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public void setPropietariosRustico(LazyModel<CatPredioRusticoDTO> propietariosRustico) {
        this.propietariosRustico = propietariosRustico;
    }

    public Long getTipoConsultaRural() {
        return tipoConsultaRural;
    }

    public void setTipoConsultaRural(Long tipoConsultaRural) {
        this.tipoConsultaRural = tipoConsultaRural;
    }

    public List<CatPredioRusticoDTO> getPrediosRusticoConsulta() {
        return prediosRusticoConsulta;
    }

    public DetalleMercado getDetalleMercado() {
        return detalleMercado;
    }

    public void setDetalleMercado(DetalleMercado detalleMercado) {
        this.detalleMercado = detalleMercado;
    }

    public void setPrediosRusticoConsulta(List<CatPredioRusticoDTO> prediosRusticoConsulta) {
        this.prediosRusticoConsulta = prediosRusticoConsulta;
    }

    public CatPredioRusticoDTO getPredioRuralConsulta() {
        return predioRuralConsulta;
    }

    public void setPredioRuralConsulta(CatPredioRusticoDTO predioRuralConsulta) {
        this.predioRuralConsulta = predioRuralConsulta;
    }

    public Boolean getControlDocumento() {
        return controlDocumento;
    }

    public void setControlDocumento(Boolean controlDocumento) {
        this.controlDocumento = controlDocumento;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Boolean getIsSanMiguel() {
        return isSanMiguel;
    }

    public void setIsSanMiguel(Boolean isSanMiguel) {
        this.isSanMiguel = isSanMiguel;
    }

    public BigInteger getnLocal() {
        return nLocal;
    }

    public void setnLocal(BigInteger nLocal) {
        this.nLocal = nLocal;
    }

    public String getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(String selectionMode) {
        this.selectionMode = selectionMode;
    }

    public BigDecimal getSumaTotalConv() {
        return sumaTotalConv;
    }

    public void setSumaTotalConv(BigDecimal sumaTotalConv) {
        this.sumaTotalConv = sumaTotalConv;
    }

    public List<FnConvenioPagoDetalle> getConvenidos() {
        return convenidos;
    }

    public void setConvenidos(List<FnConvenioPagoDetalle> convenidos) {
        this.convenidos = convenidos;
    }

    public FnConvenioPagoDetalle getCpd() {
        return cpd;
    }

    public void setCpd(FnConvenioPagoDetalle cpd) {
        this.cpd = cpd;
    }

    public List<FinaRenLiquidacion> getCuotasPredios() {
        return cuotasPredios;
    }

    public void setCuotasPredios(List<FinaRenLiquidacion> cuotasPredios) {
        this.cuotasPredios = cuotasPredios;
    }

    public Boolean getTieneConvenio() {
        return tieneConvenio;
    }

    public void setTieneConvenio(Boolean tieneConvenio) {
        this.tieneConvenio = tieneConvenio;
    }

    public List<FinaRenLiquidacion> getLiquidacionesCoactivaCuotas() {
        return liquidacionesCoactivaCuotas;
    }

    public void setLiquidacionesCoactivaCuotas(List<FinaRenLiquidacion> liquidacionesCoactivaCuotas) {
        this.liquidacionesCoactivaCuotas = liquidacionesCoactivaCuotas;
    }

    public FinaRenLiquidacion getLiquidacionCoactivaCuota() {
        return liquidacionCoactivaCuota;
    }

    public void setLiquidacionCoactivaCuota(FinaRenLiquidacion liquidacionCoactivaCuota) {
        this.liquidacionCoactivaCuota = liquidacionCoactivaCuota;
    }

    public BigDecimal getSumaTotalCoactivaConv() {
        return sumaTotalCoactivaConv;
    }

    public void setSumaTotalCoactivaConv(BigDecimal sumaTotalCoactivaConv) {
        this.sumaTotalCoactivaConv = sumaTotalCoactivaConv;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public Boolean getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidaciones() {
        return tipoLiquidaciones;
    }

    public void setTipoLiquidaciones(List<FinaRenTipoLiquidacion> tipoLiquidaciones) {
        this.tipoLiquidaciones = tipoLiquidaciones;
    }

    public FinaRenTipoLiquidacion getTipoSelect() {
        return tipoSelect;
    }

    public void setTipoSelect(FinaRenTipoLiquidacion tipoSelect) {
        this.tipoSelect = tipoSelect;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public BigDecimal getTotalCoactiva() {
        return totalCoactiva;
    }

    public void setTotalCoactiva(BigDecimal totalCoactiva) {
        this.totalCoactiva = totalCoactiva;
    }

//</editor-fold>
}
