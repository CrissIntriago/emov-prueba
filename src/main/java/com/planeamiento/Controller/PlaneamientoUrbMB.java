/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planeamiento.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.models.BusquedaPredios;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.planeamiento.Entities.PlaneamientoUrbano;
import com.planeamiento.Services.PlaneamientoUrbanoService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class PlaneamientoUrbMB extends BusquedaPredios implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private CatPredioPropietarioService propietarioService;
    @Inject
    private FinaRenLiquidacionService renLiquidacionService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private PlaneamientoUrbanoService planeamientoService;
    private Cliente solicitante;
    private String tipoPersona, detalle, reporte, observaciones, conclusiones;
    private Cliente propietario;
    private String motivoSolciitud, numTramite, numExpendiente, usoPermitido, tecnico, rutaPredio, rutaCroki, rutaPlanimetrico;

    @PostConstruct
    public void initView() {
        solicitante = new Cliente();
    }

    public void generarReporte(Boolean excel) {
//        try {
        if (solicitante == null || solicitante.getId() != null) {
            String urlImagen = valoresService.findByCodigo("IMAGEN_PREDIO");
            PlaneamientoUrbano planeamientoUrbano = new PlaneamientoUrbano();
            planeamientoUrbano.setFechaCreacion(new Date());
            planeamientoUrbano.setSecuencia(tabName);
            planeamientoUrbano.setSolicitante(solicitante);
            planeamientoUrbano.setUsuario(new Usuarios(us.getUserId()));
            planeamientoUrbano.setTipoPersona(tipoPersona);
            planeamientoUrbano.setTipoCertificado(reporte);
            planeamientoUrbano.setPredio(predioConsulta);
            planeamientoUrbano.setConclusion(conclusiones);
            planeamientoUrbano.setObservaciones(observaciones);
            planeamientoUrbano.setTramite(numTramite);
            planeamientoUrbano.setNumExpendiente(numExpendiente);
            planeamientoUrbano.setUsoPermitido(usoPermitido);
            planeamientoUrbano.setRutaSolar(rutaPlanimetrico);
            planeamientoUrbano.setRutaCrokis(rutaCroki);
            planeamientoUrbano.setRutaPredio(urlImagen + predioModel.getNumPredio());
            planeamientoUrbano.setTitulo(reporte + " - " + tipoPersona);
            planeamientoUrbano.setTecnico(getBuscarTecnico());
            planeamientoUrbano = planeamientoService.create(planeamientoUrbano);

//                ss.borrarDatos();
//                ss.borrarParametros();
//            ss.instanciarParametros();
            if (reporte != null) {

                switch (reporte) {
                    case "ACTUALIZACION DE PERMISO DE CONSTRUCCION CON PLANOS INDUSTRIAL": //
                        ss.addParametro("persona", null);
                        ss.addParametro("fecha_desde", null);
                        ss.addParametro("fecha_hasta", null);
                        ss.addParametro("rubro", null);
                        ss.setNombreReporte("");
                        ss.setNombreSubCarpeta("Construcciones");
                        break;

                    case "INSPECCION FINAL": //
//
//                        System.out.println("INSPECCION FINAL");
//                        ss.addParametro("tipoInspeccion", reporte + " - " + tipoPersona);
//                        ss.addParametro("ID", planeamientoUrbano.getId());
//                        ss.setNombreReporte("sInspeccionFinal");
//                        ss.setNombreSubCarpeta("Construcciones");
                        ss.addParametro("id_comprobante", 123);
                        ss.setContentType("pdf");
                        ss.setNombreReporte("comprobante_pago");
                        ss.setNombreSubCarpeta("_contabilidad");
                        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                        break;
                    case "LICENCIA URBANISTICA INDUSTRIAL":
                    case "LICENCIA URBANISTICA RESIDENCIAL":
                        ss.addParametro("", us);
                        break;
                    case "REGISTRO DE SOLAR INDUSTRIAL":
                    case "REGISTRO DE SOLAR RESIDENCIAL":
                        ss.setNombreReporte("registroSolar");
                        ss.setNombreSubCarpeta("Construcciones");

                        break;
                }
                if (excel) {
                    ss.setOnePagePerSheet(false);
                    ss.setContentType("xlsx");
                }
//                System.out.println("tipoInspeccion " + ss.getParametros().toString());
//                System.out.println("ssgetNombreReporte " + ss.getNombreReporte());
//                System.out.println("ssgetNombreReporte " + ss.estaVacio());
                //planeamientoUrbano.set
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
            } else {
                JsfUtil.addWarningMessage("Error", "Seleccione un tipo de certificado");
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese un solicitante");
        }

//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
    }

    public String getBuscarTecnico() {
        if (us.getUsuario() != null && us.getUsuario().getFuncionario() != null && us.getUsuario().getFuncionario().getPersona() != null) {
            return us.getUsuario().getFuncionario().getPersona().getNombreCompleto();
        }
        return "";
    }

    public void handleFileUploadCroki(FileUploadEvent event) {
        try {
            rutaCroki = "";
            String ruta = SisVars.RUTA_CERTIFICADOS + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            rutaCroki = ruta;
            System.out.println("ruta " + ruta);
            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            JsfUtil.addInformationMessage("Nota", "Foto guardada satisfactoriamente");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleFileUploadPlanimetrico(FileUploadEvent event) {
        try {
            rutaPlanimetrico = "";
            String ruta = SisVars.RUTA_CERTIFICADOS + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            rutaCroki = rutaPlanimetrico;

            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            JsfUtil.addInformationMessage("Nota", "Foto guardada satisfactoriamente");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void searchBeneficiario(Boolean parameter) {
        System.out.println("tramite.getSolicitante().getIdentificacion() " + solicitante.getIdentificacion());
        if (!Utils.isEmptyString(solicitante.getIdentificacion())) {
            Cliente c = clienteService.buscarCliente(solicitante.getIdentificacion());
            if (c != null && c.getId() != null) {
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else if (c != null && c.getId() == null) {
                c = clienteService.create(c);
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else {
                openDialogCliente();
            }
        } else {
            openDialogCliente();
        }
    }

    private void openDialogCliente() {
        Map<String, List<String>> params = new HashMap<>();
        params.put(CONFIG.PARAMETER_TIPO, Arrays.asList("true"));
        params.put(CONFIG.PARAMETER_RENDER, Arrays.asList("true"));
        params.put(CONFIG.ONE_TYPE, Arrays.asList("1"));
        Utils.openDialog("/facelet/talentoHumano/dialogCliente", "45%", "70%", params);
    }

    public void selectBeneficiario(SelectEvent evt) {
        try {
            solicitante = ((Cliente) evt.getObject());
            PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar beneficiario", e);
        }
    }

    public void limpiarDatosSolicitante() {
        solicitante = new Cliente();
    }

    public void actualizarSolicitante() {
        if (solicitante != null) {
            if (!Utils.validarEmailConExpresion(solicitante.getEmail())) {
                JsfUtil.addErrorMessage("", "Ingrese un correo correcto");
                return;
            }
            clienteService.edit(solicitante);
            JsfUtil.addSuccessMessage("", "Datos actualizados");
        }
    }

    public void consultarEmi() {
        System.out.println("consultando");
        this.consultarPrediosEmisiones();
        int anios = 0;
        Date fechaActual = new Date();
        if (Utils.isNotEmpty(prediosConsulta)) {
            CatPredioPropietario prop = propietarioService.findByPropietario(prediosConsulta.get(0));
            System.out.println("propietario>>" + prop.getEnte());
            propietario = new Cliente();
            if (prop != null) {
                propietario = prop.getEnte();
            }
            if (propietario.getIdentificacion() != null) {
                System.out.println("identificacion>>" + propietario.getIdentificacion());
                propietario = clienteService.buscarClienteData(propietario, Boolean.TRUE);
                if (this.propietario.getCondicionCiudadano() == null) {
                    propietario.setCondicionCiudadano("");
                }
            }
            System.out.println("condicion ciudadano>>>" + this.propietario.getCondicionCiudadano());
        } else {
            JsfUtil.addWarningMessage("Información", "Criterios de Busqueda no encontrado...");
        }
//        System.out.println("año>>" + anios + " fecha nac>>" + Utils.dateFormatPattern("dd/MM/yyyy", propietario.getFechaNacimiento()));
    }

    public List<FinaRenLiquidacion> getLiquidacionesByPredio(CatPredio cp) {
        FinaRenTipoLiquidacion tipo = null;
        if (cp.getTipoPredio().equals("U")) {
            tipo = new FinaRenTipoLiquidacion(2L);
        } else {
            tipo = new FinaRenTipoLiquidacion(3L);
        }
        List<FinaRenLiquidacion> listaLiquidacion = renLiquidacionService.liquidacionesConsultaByTipoPredio(cp, new FinaRenEstadoLiquidacion(2L), tipo);
        return listaLiquidacion;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMotivoSolciitud() {
        return motivoSolciitud;
    }

    public void setMotivoSolciitud(String motivoSolciitud) {
        this.motivoSolciitud = motivoSolciitud;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getNumExpendiente() {
        return numExpendiente;
    }

    public void setNumExpendiente(String numExpendiente) {
        this.numExpendiente = numExpendiente;
    }

    public String getUsoPermitido() {
        return usoPermitido;
    }

    public void setUsoPermitido(String usoPermitido) {
        this.usoPermitido = usoPermitido;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

}
