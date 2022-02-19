/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Comisaria.Entities.Citaciones;
import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.gestionTributaria.Comisaria.Service.CitacionesService;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.CmMultas;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named
@ViewScoped
public class ConstruccionProcessMB extends BpmnBaseRoot implements Serializable {

    /*AGREGADO*/
    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private CitacionesService citacionesService;
    @Inject
    private KatalinaService katalinaService;

    protected List<String> user = new ArrayList<>();
    private Integer tipoCons, opc;
    private CatPredio predio;
    private String ciRucCobros;
    private CatPredioModel predioModel;
    private String observacionAnteriro;
    private Integer esUrbano;
    private Long tipoConsulta = 2L;
    private CatPredio predioConsulta;
    private Cliente contribuyenteConsulta;
    private String tabName;
    private String nombreContribuyente;
    private String identificacion;
    private Long tipoBusqueda = 1L;
    private Cliente propietario;
    private List<CatalogoItem> tipos;
    private ComisariaRegistros comisaria;
    private List<FinaRenDetLiquidacion> detalleLista;
    private FinaRenDetLiquidacion detalle;
    private FinaRenLiquidacion finaRenLiquidacion;
    private BigDecimal sumaTotal = BigDecimal.ZERO;

    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenRubrosLiquidacion> rubro;
    private CmMultas multas;
    private CatalogoItem comisariaSelect;
    private Cliente propietarioConsulta;
    private List<CatPredio> prediosPropiestarios;
    private LazyModel<Citaciones> lazyCitaciones;
    private Citaciones citacionComparecencia;
    private Boolean renderConstruccion = Boolean.FALSE;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                propietario = new Cliente();
                predio = new CatPredio();
                tipos = new ArrayList<>();
                tipos = catalogoService.MostarTodoCatalogo("GT_tipo_construccion_comisaria");
                comisaria = new ComisariaRegistros();

                lazyCitaciones = new LazyModel<>(Citaciones.class);
                lazyCitaciones.getFilterss().put("tramite", tramite.getId());

                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                predioModel = new CatPredioModel();
                observacionAnteriro = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1).getObservacion();
                finaRenLiquidacion = new FinaRenLiquidacion();
                tipoLiquidacion = new FinaRenTipoLiquidacion();
                //ES MULTA POR INFRACCIÒN A LA ORDENANZA
                tipoLiquidacion = finaRenLiquidacionService.getTipoLiquidacionByPrefijo("MOM");
                rubro = new ArrayList<>();
                rubro = finaRenLiquidacionService.listaRubros(tipoLiquidacion);
                multas = new CmMultas();
                comisariaSelect = catalogoService.getItemByCatalogoAndCodigo("GT_lista_comisarias", "CONSTRUCCION");
                if (comisariaSelect.getCodigo().equals("CONSTRUCCION")) {
                    renderConstruccion = Boolean.TRUE;
                }
                propietarioConsulta = new Cliente();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onChangeRadio() {
        predioModel = new CatPredioModel();
        predioConsulta = null;
    }

    public void consultarEmi() {
        int anios = 0;
        Date fechaActual = new Date();
        if (predioConsulta != null) {
            CatPredioPropietario prop = null;
            Iterator<CatPredioPropietario> propiesarios = predioConsulta.getCatPredioPropietarioCollection().iterator();
            while (propiesarios.hasNext()) {
                prop = propiesarios.next();
                break;
            }
            propietario = new Cliente();
            if (prop != null) {
                propietario = prop.getEnte();
            }
            if (propietario.getIdentificacion() != null) {
                propietario = clienteService.buscarClienteData(propietario, Boolean.TRUE);
            }
            setPropietario(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
        } else {
            JsfUtil.addWarningMessage("Información", "Criterios de Busqueda no encontrado...");
        }
//        System.out.println("año>>" + anios + " fecha nac>>" + Utils.dateFormatPattern("dd/MM/yyyy", propietario.getFechaNacimiento()));

    }

    public void consultar() {
        System.out.println("predios " + esUrbano + "\t\t" + tipoCons + "\t\t" + predio.getNumPredio() + " \t\t" + predio.getClaveCat());
        try {
            CatPredio temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    propietario = Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte();
                }
                cargarRubros();
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("mainForm");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "consultar()", e);
        }
    }

    public void cargarRubros() {
        detalleLista = new ArrayList<>();
        for (FinaRenRubrosLiquidacion item : rubro) {

            detalle = new FinaRenDetLiquidacion();
            detalle.setCantidad(1);
            detalle.setRubro(item);
            detalle.setCobrar(Boolean.FALSE);
            detalle.setValor(item.getValor());
            detalleLista.add(detalle);
        }
    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior
                propietario = new Cliente();
                propietario = clienteService.buscarCliente(propietarioConsulta.getIdentificacion());
                System.out.println("propietario id" + propietario.getId());
                if (propietario != null) {
                    if (propietario.getId() != null) {
                        prediosPropiestarios = service.preidosPropietarios(propietario, "A");
                        if (prediosPropiestarios != null && !prediosPropiestarios.isEmpty()) {
                            if (prediosPropiestarios.size() == 1) {
                                temp = prediosPropiestarios.get(0);
                            } else {
                                JsfUtil.executeJS("PF('dlogoPpredioPropiestario').show()");
                                JsfUtil.update("frmPrediosPropiestarios");
                                return null;
                            }
                        }
                    } else {
                        propietario = new Cliente();
                        JsfUtil.addErrorMessage(MessagesRentas.error, "No se encontro un cliente con la identificación " + propietarioConsulta.getIdentificacion() + ", agregar en el listado de Clientes.");
                    }
                }
                break;
            case 2: // Codigo Nuevo                
                if (predio.getSector() > 0 || predio.getMz() > 0 || predio.getProvincia() > 0 || predio.getCanton() > 0
                        || predio.getParroquia() > 0 || predio.getZona() > 0 || predio.getSolar() > 0 || predio.getPiso() >= 0
                        || predio.getUnidad() >= 0 || predio.getBloque() >= 0) {
                    Map<String, Object> paramtr = new HashMap<>();
                    paramtr.put("estado", "A");
                    paramtr.put("sector", predio.getSector());
                    paramtr.put("mz", predio.getMz());
                    paramtr.put("provincia", predio.getProvincia());
                    paramtr.put("canton", predio.getCanton());
                    paramtr.put("parroquia", predio.getParroquia());
                    paramtr.put("zona", predio.getZona());
                    paramtr.put("solar", predio.getSolar());
                    paramtr.put("piso", predio.getPiso());
                    paramtr.put("unidad", predio.getUnidad());
                    paramtr.put("bloque", predio.getBloque());
                    if (esUrbano == 1) {
                        paramtr.put("tipoPredio", "U");
                    } else {
                        paramtr.put("tipoPredio", "R");
                    }
                    temp = service.findByParameter(CatPredio.class, paramtr);
                }
                break;

            case 3:// Numero de Predio
                if (predio.getNumPredio() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaNumPredio);
                    return null;
                }
                temp = service.getPredioNumPredio(predio.getNumPredio().longValue());
                break;
            case 4:
                if (predio.getClaveCat() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaClavePredio);
                    return null;
                }
                temp = service.getPredioByClaveCat(predio.getClaveCat());
                break;
            case 5:// Clave anterior
                if (predio.getPredialant() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaClaveAnterior);
                    return null;
                }
                temp = service.getPredioByClaveCatAnt(predio.getPredialant());
                break;
        }
        if (temp != null) {
            JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.predioEncontrado + temp.getClaveCat());
            return temp;
        } else {
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            return null;
        }
    }

    public void validarClaveOtroCanton() {
        predio = new CatPredio();
//        if (cobrosGenerales.getTipoLiquidacion() == null) {
//            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el tipo de liquidación a realizar");
//            return;
//        }
        try {
            if (predioModel.getProvincia() > 0 && predioModel.getCanton() > 0
                    && predioModel.getParroquiaShort() > 0 && predioModel.getZona() > 0 && predioModel.getSector() > 0
                    && predioModel.getMz() > 0 && predioModel.getLote() > 0) {
            } else {
                JsfUtil.addWarningMessage("Advertencia", "Verifique los datos ingresados");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
//            System.out.println("predio " + predio);
            if (predio.getId() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar el predio");
                return false;
            }
            if (comisaria.getObservacion().isEmpty() || comisaria.getObservacion() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que llenar el campo observación");
                return false;
            }

            comisaria.setEnte(propietario);
            comisaria.setOrigen("CONST");
            comisaria.setPredio(predio.getId());
            comisaria = comisariaServices.create(comisaria);

            finaRenLiquidacion.setAnio(Utils.getAnio(new Date()));
            finaRenLiquidacion.setAvaluoMunicipal(predio.getAvaluoMunicipal());
            finaRenLiquidacion.setPredio(predio);
            finaRenLiquidacion.setFechaIngreso(new Date());
            finaRenLiquidacion.setFechaCreacionOriginal(new Date());
            finaRenLiquidacion.setTipoLiquidacion(tipoLiquidacion);
            finaRenLiquidacion.setObservacion("COMISARIA " + comisariaSelect.getDescripcion());
            finaRenLiquidacion.setComprador(propietario);
            /*NO NECESITAN VALIDAR LA LIQUIDACION EN RENTAS POR ESO TRUE*/
            finaRenLiquidacion.setValidada(Boolean.TRUE);
            /*ENVIANDO EL ID A GUARDAR*/
            finaRenLiquidacion.setTramite(BigInteger.valueOf(tramite.getId()));
            this.finaRenLiquidacion.calcularPago();
            //SE AGREGA PARA SALDO DE PAGO
            this.generarLiquidacion();
            JsfUtil.addInformationMessage("", "Transacción exitosa");
//            resetear();
            return true;
        } catch (Exception e) {
            JsfUtil.addWarningMessage("", "La Transacción fue rechazada");
            e.printStackTrace();
            return false;
        }
    }

    public void generarLiquidacion() {
        finaRenLiquidacion.setUsuarioIngreso(userSession.getNameUser());
        finaRenLiquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
        finaRenLiquidacion.setComprador(propietario);
        finaRenLiquidacion.setNombreComprador(propietario.getNombreCompleto());
        finaRenLiquidacion.setIdentificacion(propietario.getIdentificacionCompleta());
        finaRenLiquidacion.setRentas(Boolean.TRUE);
        finaRenLiquidacion.setSaldo(sumaTotal);

        finaRenLiquidacion = finaRenLiquidacionService.crearLiquidacion(finaRenLiquidacion, detalleLista);
//        System.out.println("creando liquidación " + finaRenLiquidacion.getId());
        multas = new CmMultas();
        multas.setCatastro(predio);
        multas.setContribuyente(propietario);
        multas.setComisariaRegistro(comisaria);
        Usuarios deman = usuarioService.findByUsuario(userSession.getNameUser());
        if (deman != null && deman.getFuncionario() != null && deman.getFuncionario().getPersona() != null) {
            multas.setDemandante(deman.getFuncionario().getPersona());
        }
        multas.setDemandado(propietario);
        multas.setFechaIngreso(new Date());
        multas.setObservacion(finaRenLiquidacion.getObservacion());
        multas.setUsuarioIngreso(userSession.getNameUser());
        multas.setLiquidacion(finaRenLiquidacion);
        multas.setComisaria(comisariaSelect);
        multas = (CmMultas) service.updateEntity(multas);
        JsfUtil.executeJS("PF('dlogoNumLiquidacion').show()");
        JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + finaRenLiquidacion.getIdLiquidacion() + " genrada con exito");
    }

    public void resetear() {
        comisaria = new ComisariaRegistros();
        tipos = new ArrayList<>();
        tipos = catalogoService.MostarTodoCatalogo("GT_tipo_construccion_comisaria");
        predio = new CatPredio();
        propietario = new Cliente();
        detalleLista = new ArrayList<>();
    }

    public void suma() {
        sumaTotal = BigDecimal.ZERO;
        for (FinaRenDetLiquidacion finaRenDetLiquidacion : detalleLista) {
            if (finaRenDetLiquidacion.getCobrar()) {
                sumaTotal = sumaTotal.add(finaRenDetLiquidacion.getValor());
            }
        }
        finaRenLiquidacion.setTotalPago(sumaTotal);
    }

    public void viewLiquidacion(CmMultas c) {
        finaRenLiquidacion = new FinaRenLiquidacion();
        finaRenLiquidacion = c.getLiquidacion();
    }

    public void editarLista(FinaRenDetLiquidacion li, int index) {
        this.detalleLista.add(this.detalleLista.indexOf(li), li);
        this.detalleLista.remove(this.detalleLista.indexOf(li));
        suma();
        JsfUtil.update("datosLista");
    }

    public void seleccionarPredio(CatPredio p) {
        predio = p;
        if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
            comisaria.setEnte(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
            propietario = comisaria.getEnte();
        }
    }

    public void dialogCitacionesComparecencia(Citaciones c) {
        citacionComparecencia = c;
        JsfUtil.update("idFormCompadecencia");
        JsfUtil.executeJS("PF('dialogCompadecencia').show()");
    }

    public void saveCompadecencia() {
        if (citacionComparecencia.getAcuerdo().isEmpty() || citacionComparecencia.getAcuerdo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe agregar el acuerdo");
            return;
        }
        if (citacionComparecencia.getAcuerdo().isEmpty() || citacionComparecencia.getAcuerdo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe agregar el acuerdo");
            return;
        }
        citacionComparecencia.setUsuarioMod(userSession.getNameUser());
        citacionComparecencia.setFechaMod(new Date());
        citacionesService.edit(citacionComparecencia);
    }

    public void opendialog(Integer opc) {
        this.opc = opc;
//        if (opc == 1) {
        List<Citaciones> validarCitaciones = citacionesService.findCitacionesNotComparecenciaByTramite(tramite);
        if (Utils.isNotEmpty(validarCitaciones)) {
            JsfUtil.addWarningMessage("Advertencia", "Para continuar debe Comparecer la citación emitida.");
            return;
        }
//        }
        if (comisariaSelect.getCodigo().equals("")) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Escoger el tipo.");
            return;
        }
        if (detalleLista.get(0).getCobrar() == false) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Escoger la multa");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            Boolean returnResult = false;
            switch (opc) {
                case 1:
                    returnResult = save();
                    if (finaRenLiquidacion.getTipoLiquidacion() != null) {
                        if (propietario != null) {
//                            enviarCorreo("luisnandopg@gmail.com",
//                                    "EMISIÓN DE LIQUIDACIÓN POR TRÁMITE " + tramite.getTipoTramite().getDescripcion(),
//                                    "LUIS FERNANDO POZO GONZABAY");
                            if (Utils.isNotEmptyString(propietario.getEmail())) {
                                enviarCorreo(propietario.getEmail(),
                                        "EMISIÓN DE LIQUIDACIÓN POR TRÁMITE " + tramite.getTipoTramite().getDescripcion(),
                                        propietario.getNombreCompleltoSql());
                            } else {
                                JsfUtil.addWarningMessage("Correo", "LA NOTIFICACIÓN NO PUDO ENVIARSE, EL PROPIETARIO NO TIENE CORREO.");
                            }
                        }
                    }
                    String usuario2 = clienteService.getrolsUser(RolUsuario.tesorero);
                    getParamts().put("tesoreria", usuario2.equals("") ? "admin_1" : usuario2);
                    break;
                case 2:
                    String usuario = (String) getVariable("usuarioDelegado");
                    getParamts().put("usuarioDelegado", usuario.equals("") ? "admin_1" : usuario);
                    this.enviarCorreo();
                    break;
                case 3:
                    String usuario3 = clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia);
                    getParamts().put("usuarioDirectorVerifica", usuario3.equals("") ? "admin_1" : usuario3);
                    break;
            }
            if (!returnResult && opc == 1) {
                System.out.println("error en save revisar");
                return;
            }
            resetear();
            getParamts().put("reagendar", opc);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void enviarCorreo(String email, String asunto, String usuario) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getNumTramite() + " - " + tramite.getTipoTramite().getDescripcion(),
                "Estimado Usuario " + usuario.toUpperCase() + " se le notifica que se ha emitido una liquidación de tipo " + finaRenLiquidacion.getTipoLiquidacion().getNombreTitulo() + ". Acercarse a pagar.",
                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito a la dirección de email: " + email + " relacionada con: " + usuario);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getCiRucCobros() {
        return ciRucCobros;
    }

    public void setCiRucCobros(String ciRucCobros) {
        this.ciRucCobros = ciRucCobros;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

    public String getObservacionAnteriro() {
        return observacionAnteriro;
    }

    public void setObservacionAnteriro(String observacionAnteriro) {
        this.observacionAnteriro = observacionAnteriro;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }

    public Integer getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Integer esUrbano) {
        this.esUrbano = esUrbano;
    }

    public Long getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(Long tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public CatPredio getPredioConsulta() {
        return predioConsulta;
    }

    public void setPredioConsulta(CatPredio predioConsulta) {
        this.predioConsulta = predioConsulta;
    }

    public Cliente getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(Cliente contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Long getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Long tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public List<CatalogoItem> getTipos() {
        return tipos;
    }

    public void setTipos(List<CatalogoItem> tipos) {
        this.tipos = tipos;
    }

    public ComisariaRegistros getComisaria() {
        return comisaria;
    }

    public void setComisaria(ComisariaRegistros comisaria) {
        this.comisaria = comisaria;
    }

    public List<FinaRenDetLiquidacion> getDetalleLista() {
        return detalleLista;
    }

    public void setDetalleLista(List<FinaRenDetLiquidacion> detalleLista) {
        this.detalleLista = detalleLista;
    }

    public FinaRenDetLiquidacion getDetalle() {
        return detalle;
    }

    public void setDetalle(FinaRenDetLiquidacion detalle) {
        this.detalle = detalle;
    }

    public FinaRenLiquidacion getFinaRenLiquidacion() {
        return finaRenLiquidacion;
    }

    public void setFinaRenLiquidacion(FinaRenLiquidacion finaRenLiquidacion) {
        this.finaRenLiquidacion = finaRenLiquidacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public BigDecimal getSumaTotal() {
        return sumaTotal;
    }

    public void setSumaTotal(BigDecimal sumaTotal) {
        this.sumaTotal = sumaTotal;
    }

    public Cliente getPropietarioConsulta() {
        return propietarioConsulta;
    }

    public void setPropietarioConsulta(Cliente propietarioConsulta) {
        this.propietarioConsulta = propietarioConsulta;
    }

    public List<CatPredio> getPrediosPropiestarios() {
        return prediosPropiestarios;
    }

    public void setPrediosPropiestarios(List<CatPredio> prediosPropiestarios) {
        this.prediosPropiestarios = prediosPropiestarios;
    }

    public LazyModel<Citaciones> getLazyCitaciones() {
        return lazyCitaciones;
    }

    public void setLazyCitaciones(LazyModel<Citaciones> lazyCitaciones) {
        this.lazyCitaciones = lazyCitaciones;
    }

    public Citaciones getCitacionComparecencia() {
        return citacionComparecencia;
    }

    public void setCitacionComparecencia(Citaciones citacionComparecencia) {
        this.citacionComparecencia = citacionComparecencia;
    }

    public CatalogoItem getComisariaSelect() {
        return comisariaSelect;
    }

    public void setComisariaSelect(CatalogoItem comisariaSelect) {
        this.comisariaSelect = comisariaSelect;
    }
//</editor-fold>

    public Boolean getRenderConstruccion() {
        return renderConstruccion;
    }

    public void setRenderConstruccion(Boolean renderConstruccion) {
        this.renderConstruccion = renderConstruccion;
    }

}
