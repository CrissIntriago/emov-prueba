/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.entities.Formulario107;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RetencionesImpuestoRenta;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.model.DatRetRelDep;
import com.origami.sigef.talentohumano.model.Empleado;
import com.origami.sigef.talentohumano.model.Rdep;
import com.origami.sigef.talentohumano.model.RetRelDep;
import com.origami.sigef.talentohumano.services.Formulario107Service;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jesus
 */
@Named(value = "formulario107View")
@ViewScoped
public class Formulario107Controller implements Serializable {

    @Inject
    private Formulario107Service formulario107Service;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private DatosGeneralesEntidadService datosGeneralesEntidadService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ValoresService valoresService;
    @Inject
    private KatalinaService katalinaService;

    private List<Formulario107> formularios107s;
    private Formulario107 formulario107;
    private RetencionesImpuestoRenta retencion;
    private List<MasterCatalogo> periodosFiscales;
    private OpcionBusqueda busqueda;

    private DatosGeneralesEntidad datosGeneralesEntidad;
    private Date fechaEmision;

    private List<Servidor> servidores;

    private GastoPersonal gastoPersonal;

    private List<TipoRol> tipoRoles;
    private TipoRol tipoRol;

    private final Long idParametroDecimoTercero = new Long(44);
    private final Long idParametroDecimoCuarto = new Long(35);
    private final Long idParametroFondoReserva = new Long(26);
    private final Long idParametroAporteIessLosep = new Long(49);
    private final Long idParametroAporteIessCodigo = new Long(50);
    private final Long idAprobadoItem = new Long(514);

    private Cajero cajero;

    private final String RESIDENCIALOCAL = "01";
    private final String CODIGOPAIS = "593";

    private Boolean listadoRendered;

    @PostConstruct
    public void init() {
        listadoRendered = Boolean.FALSE;
        fechaEmision = new Date();
        formulario107 = new Formulario107();
        formularios107s = new ArrayList<>();
        busqueda = new OpcionBusqueda();
        gastoPersonal = new GastoPersonal();
        tipoRoles = new ArrayList<>();
        tipoRol = new TipoRol();
        periodosFiscales = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        datosGeneralesEntidad = datosGeneralesEntidadService.find(1L);
        servidores = formulario107Service.findAllServidorRetencion(busqueda.getAnio());
        servidores.forEach((s) -> {
            s.getPersona().setNombreCompleto(s.getPersona().getNombre() + " " + s.getPersona().getApellido());
        });
    }

    public void buscar() {
        servidores = formulario107Service.findAllServidorRetencion(busqueda.getAnio());
    }

    public void renderizarListad() {
        listadoRendered = Boolean.FALSE;
    }

    public void visualizar(Servidor s) {
        tipoRoles = new ArrayList<>();
        formularios107s = formulario107Service.findAllFormulario107ByServidor(BigInteger.valueOf(s.getId()), "registrado-rol");
        if (!formularios107s.isEmpty()) {
            listadoRendered = Boolean.TRUE;
            formulario107 = formularios107s.get(0);
            decimoFondos();
            gastoPersonal = new GastoPersonal();
            if (formulario107.getGastoId() != null) {
                gastoPersonal.setOtrosIngresos(formulario107.getIngresosGravados() == null ? BigDecimal.ZERO : formulario107.getIngresosGravados());
                gastoPersonal.setGastoVivienda(formulario107.getGastoVivienda() == null ? BigDecimal.ZERO : formulario107.getGastoVivienda());
                gastoPersonal.setGastoEducacion(formulario107.getGastoEducacion() == null ? BigDecimal.ZERO : formulario107.getGastoEducacion());
                gastoPersonal.setGastoSalud(formulario107.getGastoSalud() == null ? BigDecimal.ZERO : formulario107.getGastoSalud());
                gastoPersonal.setGastoVestimenta(formulario107.getGastoVestimenta() == null ? BigDecimal.ZERO : formulario107.getGastoVestimenta());
                gastoPersonal.setGastoAlimentacion(formulario107.getGastoAlimentacion() == null ? BigDecimal.ZERO : formulario107.getGastoAlimentacion());
                gastoPersonal.setValorImpuestoRetenido(formulario107.getValorImpuestoRetenido() == null ? BigDecimal.ZERO : formulario107.getValorImpuestoRetenido());
                gastoPersonal.setExoneracionDiscapacidad(formulario107.getExoneracionDiscapacidad() == null ? BigDecimal.ZERO : formulario107.getExoneracionDiscapacidad());
                gastoPersonal.setExoneracionTerceraEdad(formulario107.getExoneracionTerceraEdad() == null ? BigDecimal.ZERO : formulario107.getExoneracionTerceraEdad());
                formulario107.setGastoPersonal(gastoPersonal);
            } else {
                formulario107.setGastoPersonal(new GastoPersonal());
                formulario107.setIngresosGravados(formulario107.getIngresosGravados() == null ? BigDecimal.ZERO : formulario107.getIngresosGravados());
                gastoPersonal = new GastoPersonal();
            }
            if (formularios107s.size() > 1) {
                formulario107.setTotalSueldo(BigDecimal.ZERO);
                formulario107.setTotalImpuestoRetenido(BigDecimal.ZERO);
                for (Formulario107 f : formularios107s) {
                    formulario107.setTotalSueldo(formulario107.getTotalSueldo().add(f.getSueldoMensual()));
                    formulario107.setTotalImpuestoRetenido(formulario107.getTotalImpuestoRetenido().add(f.getImpuestoRentaMensual() == null ? BigDecimal.ZERO : f.getImpuestoRentaMensual()));
                    setTipoRol(f.getTiporolId().longValue());
                }
            } else {
                setTipoRol(formulario107.getTiporolId().longValue());
                formulario107.setTotalSueldo(formulario107.getSueldoMensual());
            }
        } else {
            JsfUtil.addInformationMessage("", "No existen Datos o Liquidacion de Roles Aprobadas para " + s.getPersona().getNombreCompleto());
            loadModel();
        }
        //cargar todos los datos q se van a mostrar en el formulario 107 antes de imprimirlo
        // o generarlo validacion solo cuando tenga una o mas liquidacion de rol aprobada
    }

    private Formulario107 createFomulario107ForXml(Servidor s) {
        tipoRoles = new ArrayList<>();
        formularios107s = formulario107Service.findAllFormulario107ByServidor(BigInteger.valueOf(s.getId()), "APROBADO");
        if (!formularios107s.isEmpty()) {
            listadoRendered = Boolean.TRUE;
            formulario107 = formularios107s.get(0);
            decimoFondos();
            gastoPersonal = new GastoPersonal();
            if (formulario107.getGastoId() != null) {
                gastoPersonal.setOtrosIngresos(formulario107.getIngresosGravados() == null ? BigDecimal.ZERO : formulario107.getIngresosGravados());
                gastoPersonal.setGastoVivienda(formulario107.getGastoVivienda() == null ? BigDecimal.ZERO : formulario107.getGastoVivienda());
                gastoPersonal.setGastoEducacion(formulario107.getGastoEducacion() == null ? BigDecimal.ZERO : formulario107.getGastoEducacion());
                gastoPersonal.setGastoSalud(formulario107.getGastoSalud() == null ? BigDecimal.ZERO : formulario107.getGastoSalud());
                gastoPersonal.setGastoVestimenta(formulario107.getGastoVestimenta() == null ? BigDecimal.ZERO : formulario107.getGastoVestimenta());
                gastoPersonal.setGastoAlimentacion(formulario107.getGastoAlimentacion() == null ? BigDecimal.ZERO : formulario107.getGastoAlimentacion());
                gastoPersonal.setValorImpuestoRetenido(formulario107.getValorImpuestoRetenido() == null ? BigDecimal.ZERO : formulario107.getValorImpuestoRetenido());
                gastoPersonal.setExoneracionDiscapacidad(formulario107.getExoneracionDiscapacidad() == null ? BigDecimal.ZERO : formulario107.getExoneracionDiscapacidad());
                gastoPersonal.setExoneracionTerceraEdad(formulario107.getExoneracionTerceraEdad() == null ? BigDecimal.ZERO : formulario107.getExoneracionTerceraEdad());
                formulario107.setGastoPersonal(gastoPersonal);
            } else {
                formulario107.setGastoPersonal(new GastoPersonal());
                formulario107.setIngresosGravados(formulario107.getIngresosGravados() == null ? BigDecimal.ZERO : formulario107.getIngresosGravados());
                gastoPersonal = new GastoPersonal();
            }
            if (formularios107s.size() > 1) {
                formulario107.setTotalSueldo(BigDecimal.ZERO);
                formulario107.setTotalImpuestoRetenido(BigDecimal.ZERO);
                for (Formulario107 f : formularios107s) {
                    formulario107.setTotalSueldo(formulario107.getTotalSueldo().add(f.getSueldoMensual()));
                    formulario107.setTotalImpuestoRetenido(formulario107.getTotalImpuestoRetenido().add(f.getImpuestoRentaMensual() == null ? BigDecimal.ZERO : f.getImpuestoRentaMensual()));
                    setTipoRol(f.getTiporolId().longValue());
                }
            } else {
                setTipoRol(formulario107.getTiporolId().longValue());
                formulario107.setTotalSueldo(formulario107.getSueldoMensual());
            }
            return formulario107;
        } else {
            return null;
        }
    }

    public void decimoFondos() {
        BigDecimal decimoCuarto = formulario107Service.findByFondosReservaByServidor(idAprobadoItem, formulario107.getServidorId().longValue(), idParametroDecimoCuarto);
        BigDecimal decimoTercero = formulario107Service.findByFondosReservaByServidor(idAprobadoItem, formulario107.getServidorId().longValue(), idParametroDecimoTercero);
        BigDecimal fondosReserva = formulario107Service.findByFondosReservaByServidor(idAprobadoItem, formulario107.getServidorId().longValue(), idParametroFondoReserva);
        BigDecimal aportePersonalLosep = formulario107Service.findByFondosReservaByServidor(idAprobadoItem, formulario107.getServidorId().longValue(), idParametroAporteIessLosep);
        BigDecimal aportePersonalCodigo = formulario107Service.findByFondosReservaByServidor(idAprobadoItem, formulario107.getServidorId().longValue(), idParametroAporteIessCodigo);
        formulario107.setDecimoCuarto(decimoCuarto != null ? decimoCuarto : BigDecimal.ZERO);
        formulario107.setDecimoTercero(decimoTercero != null ? decimoTercero : BigDecimal.ZERO);
        formulario107.setFondosReserva(fondosReserva != null ? fondosReserva : BigDecimal.ZERO);
        formulario107.setAportePersonalIESSLosep(aportePersonalLosep != null ? aportePersonalLosep : BigDecimal.ZERO);
        formulario107.setAportePersonalIESSCodigo(aportePersonalCodigo != null ? aportePersonalCodigo : BigDecimal.ZERO);
        if (formulario107.getAportePersonalIESSCodigo() != BigDecimal.ZERO && formulario107.getAportePersonalIESSLosep() != BigDecimal.ZERO) {
            formulario107.getAportePersonalIESSLosep().add(formulario107.getAportePersonalIESSCodigo());
        }
    }

    public void setTipoRol(Long idTipoRol) {
        tipoRol = tipoRolService.findByNamedQuery1("TipoRol.findById", idTipoRol);
        tipoRoles.add(tipoRol);
    }

    public void generarReporte() {
        if (validar()) {
            imprimirReporte("formulario107", "formulario107");
            loadModel();
            renderizarListad();
            JsfUtil.update("formMain");
            JsfUtil.addSuccessMessage("", "Comprobante Generado con Éxito");
        }
    }

    public void imprimirReporte(String nombreReporte, String nombreSubCarpeta) {
        servletSession.addParametro("periodo_fiscal", busqueda.getAnio());
        servletSession.addParametro("fecha_entrega", new SimpleDateFormat("dd/MM/yyyy").format(fechaEmision));
        servletSession.addParametro("ruc_establecimiento", datosGeneralesEntidad.getRuc());
        servletSession.addParametro("razon_social", datosGeneralesEntidad.getNombreEntidad());
        servletSession.addParametro("identificacion", formulario107.getIdentificacion());
        servletSession.addParametro("nombres_completo", formulario107.getNombreCompleto());
        servletSession.addParametro("sueldos", formulario107.getTotalSueldo());
        servletSession.addParametro("ingresos_gravados", formulario107.getIngresosGravados());
        servletSession.addParametro("decimo_3ero", formulario107.getDecimoTercero());
        servletSession.addParametro("decimo_4to", formulario107.getDecimoCuarto());
        servletSession.addParametro("fondo_reserva", formulario107.getFondosReserva());
        servletSession.addParametro("aporte_personal_iess", formulario107.getAportePersonalIESSLosep());
        servletSession.addParametro("gastos_vivienda", gastoPersonal.getGastoVivienda());
        servletSession.addParametro("gastos_salud", gastoPersonal.getGastoSalud());
        servletSession.addParametro("gastos_educacion", gastoPersonal.getGastoEducacion());
        servletSession.addParametro("gastos_alimentacion", gastoPersonal.getGastoAlimentacion());
        servletSession.addParametro("gastos_vestimenta", gastoPersonal.getGastoVestimenta());
        servletSession.addParametro("impuesto_retenido", formulario107.getTotalImpuestoRetenido());
        servletSession.addParametro("gastos_arte_cultura", BigDecimal.ZERO); //// falta preguntar
        servletSession.addParametro("exonero_discapacidad", gastoPersonal.getExoneracionDiscapacidad());
        servletSession.addParametro("exonero_tercera_edad", gastoPersonal.getExoneracionTerceraEdad());
        servletSession.addParametro("impuesto_periodo_declarado", gastoPersonal.getValorImpuestoRetenido());
        servletSession.addParametro("impuesto_empleador", BigDecimal.ZERO);
        servletSession.addParametro("impuesto_renta_causada", BigDecimal.ZERO);
        servletSession.addParametro("sobresueldos_comisiones", BigDecimal.ZERO);
        servletSession.addParametro("fecha_impresion", fechaEmision);
        servletSession.addParametro("base_imponible_gravada", valBaseImponibleGravada());
        servletSession.addParametro("ingresos_gravados_empleador", valIngresosGravadosEmpleador());
        servletSession.setNombreReporte(nombreReporte);
        servletSession.setNombreSubCarpeta(nombreSubCarpeta);
        /*se guarda el tipo de reporte a generar*/
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public Boolean validar() {
        if (formulario107.getServidorId() == null) {
            JsfUtil.addInformationMessage("", "Seleccione un Servidor");
            return false;
        }
        return true;
    }

    public BigDecimal valBaseImponibleGravada() {
        //FALTA GASTOS POR ARTE Y CULTURA ???
        BigDecimal total = formulario107.getTotalSueldo().add(formulario107.getIngresosGravados()).subtract(formulario107.getAportePersonalIESSLosep())
                .subtract(gastoPersonal.getGastoVivienda()).subtract(gastoPersonal.getGastoSalud()).subtract(gastoPersonal.getGastoEducacion())
                .subtract(gastoPersonal.getGastoAlimentacion()).subtract(gastoPersonal.getGastoVestimenta()).subtract(gastoPersonal.getExoneracionDiscapacidad())
                .subtract(gastoPersonal.getExoneracionTerceraEdad());
        formulario107.setBaseImponibleGravada(total);
        return formulario107.getBaseImponibleGravada();
    }

    public BigDecimal valIngresosGravadosEmpleador() {
        formulario107.setIngresosGravadosEmpleador(formulario107.getTotalSueldo());/// + 303 (0)  + 305 (0) + 381 (0)
        return formulario107.getIngresosGravadosEmpleador();
    }

    public void loadModel() {
        listadoRendered = Boolean.FALSE;
        gastoPersonal = new GastoPersonal();
        tipoRol = new TipoRol();
        tipoRoles = new ArrayList<>();
        formulario107 = new Formulario107();
        formulario107.setGastoPersonal(new GastoPersonal());
    }

    public void generarComprobanteXML() {
        if (servidores == null) {
            return;
        }
        if (!servidores.isEmpty()) {
            String fechaFormat = new SimpleDateFormat("yyyy").format(fechaEmision);
            String ruta = valoresService.findByCodigo("RUTA_COMPROBANTE_FORMULARIO_107") + "RDEP_" + fechaFormat + ".xml";
            Rdep resp = convertirFormulario107ToRdep(servidores);
            if (DocumentosUtil.crearArchivo(resp, ruta)) {
                servletSession.borrarDatos();
                servletSession.borrarParametros();
                servletSession.setNombreDocumento(ruta);
                servletSession.setContentType("text/xml");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
            } else {
                JsfUtil.addErrorMessage("", "Comprobante no generado");
            }
        } else {
            JsfUtil.addInformationMessage("", "No existen Servidores");
        }
    }

    public Rdep convertirFormulario107ToRdep(List<Servidor> servidores) {
        Rdep rdep = new Rdep();
        rdep.setNumRuc(datosGeneralesEntidad.getRuc());
        rdep.setAnio(busqueda.getAnio().intValue());
        rdep.setRetRelDep(new RetRelDep());
        rdep.getRetRelDep().setDatRetRelDep(new ArrayList<>());
        for (Servidor s : servidores) {
            DatRetRelDep datRetRelDep = new DatRetRelDep();
            datRetRelDep.setEmpleado(new Empleado());
            Formulario107 f = createFomulario107ForXml(s);
            if (f != null) {
                datRetRelDep.setSuelSal(f.getTotalSueldo().doubleValue());
                datRetRelDep.setSobSuelComRemu(new Double(0)); // NO SE USA
                datRetRelDep.setPartUtil(new Double(0)); // NO SE USA
                datRetRelDep.setIntGrabGen(f.getIngresosGravados().doubleValue());
                datRetRelDep.setImpRentEmpl(new Double(0)); // NO SE USA
                datRetRelDep.setDecimTer(f.getDecimoTercero().doubleValue());
                datRetRelDep.setDecimCuar(f.getDecimoCuarto().doubleValue());
                datRetRelDep.setFondoReserva(f.getFondosReserva().doubleValue());
                datRetRelDep.setSalarioDigno(new Double(0)); // NO SE USA FECHA DE VIGENCIA EXPIRADO
                datRetRelDep.setOtrosIngRenGrav(new Double(0)); // NO SE USA
                //DATO PARA CALCULAR LOS INGRESOS GRAVADOS CON ESTE EMPLEADOR(INFORMATIVO)
                Double infoIngGravEmpleado = f.getTotalSueldo().doubleValue(); // SUMA DE : sobSuelComRemu(0) + partUtil(0) + impRentEmpl(0)
                datRetRelDep.setIngGravConEsteEmpl(infoIngGravEmpleado);
                datRetRelDep.setSisSalNet(1); //SIN SISTEMA DE SALARIO NETO
                datRetRelDep.setApoPerIess(f.getAportePersonalIESSLosep().doubleValue());
                datRetRelDep.setAporPerIessConOtrosEmpls(new Double(0)); // NO SE USA
                datRetRelDep.setDeducVivienda(f.getGastoPersonal().getGastoVivienda().doubleValue());
                datRetRelDep.setDeducSalud(f.getGastoPersonal().getGastoSalud().doubleValue()); //// URG PREGUNTAR POR LAS VALIDACIONES
                datRetRelDep.setDeducEducartcult(f.getGastoPersonal().getGastoEducacion().doubleValue()); //// URG PREGUNTAR POR LAS VALIDACIONES
                datRetRelDep.setDeducAliement(f.getGastoPersonal().getGastoAlimentacion().doubleValue());
                datRetRelDep.setDeducVestim(f.getGastoPersonal().getGastoVestimenta().doubleValue());
                datRetRelDep.setExoDiscap(f.getGastoPersonal().getExoneracionDiscapacidad().doubleValue());
                datRetRelDep.setExoTerEd(f.getGastoPersonal().getExoneracionTerceraEdad().doubleValue());
                datRetRelDep.setBasImp(valBaseImponibleGravada().doubleValue());
                datRetRelDep.setImpRentCaus(new Double(0)); // No SE USA
                datRetRelDep.setValRetAsuOtrosEmpls(f.getGastoPersonal().getValorImpuestoRetenido().doubleValue());
                datRetRelDep.setValImpAsuEsteEmpl(new Double(0)); // NO SE USA
                datRetRelDep.setValRet(f.getTotalImpuestoRetenido().doubleValue());
                datRetRelDep.getEmpleado().setBenGalpg("NO");
                datRetRelDep.getEmpleado().setEnfcatastro(f.getEnfermedadCatastrofica() ? "SI" : "NO");
                datRetRelDep.getEmpleado().setTipIdRet(f.getTipoIdentificacion().equals("RUC") ? "C" : f.getTipoIdentificacion().equals("C")
                        ? f.getTipoIdentificacion() : f.getTipoIdentificacion().equals("P") ? f.getTipoIdentificacion() : "");
                datRetRelDep.getEmpleado().setNombreTrab(Utils.quitaDiacriticos(f.getNombreCliente()));
                datRetRelDep.getEmpleado().setApellidoTrab(Utils.quitaDiacriticos(f.getApellidoCliente()));
                datRetRelDep.getEmpleado().setIdRet(f.getIdentificacion());
                datRetRelDep.getEmpleado().setEstab(cajero.getEntidad().getEstablecimiento());
                datRetRelDep.getEmpleado().setResidenciaTrab(RESIDENCIALOCAL);
                datRetRelDep.getEmpleado().setPaisResidencia(CODIGOPAIS);
                datRetRelDep.getEmpleado().setAplicaConvenio("NA");
                datRetRelDep.getEmpleado().setTipoTrabajDiscap(f.getDiscapacidad() ? "02" : "01");
                datRetRelDep.getEmpleado().setPorcentajeDiscap(f.getPorcentajeDiscapacidad().intValue());
                datRetRelDep.getEmpleado().setTipIdDiscap("N");
                datRetRelDep.getEmpleado().setIdDiscap("999");
                rdep.getRetRelDep().getDatRetRelDep().add(datRetRelDep);
            }
        }
        return rdep;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Boolean getListadoRendered() {
        return listadoRendered;
    }

    public void setListadoRendered(Boolean listadoRendered) {
        this.listadoRendered = listadoRendered;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public List<TipoRol> getTipoRoles() {
        return tipoRoles;
    }

    public void setTipoRoles(List<TipoRol> tipoRoles) {
        this.tipoRoles = tipoRoles;
    }

    public GastoPersonal getGastoPersonal() {
        return gastoPersonal;
    }

    public void setGastoPersonal(GastoPersonal gastoPersonal) {
        this.gastoPersonal = gastoPersonal;
    }

    public List<Servidor> getServidores() {
        return servidores;
    }

    public void setServidores(List<Servidor> servidores) {
        this.servidores = servidores;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public DatosGeneralesEntidad getDatosGeneralesEntidad() {
        return datosGeneralesEntidad;
    }

    public void setDatosGeneralesEntidad(DatosGeneralesEntidad datosGeneralesEntidad) {
        this.datosGeneralesEntidad = datosGeneralesEntidad;
    }

    public Formulario107 getFormulario107() {
        return formulario107;
    }

    public void setFormulario107(Formulario107 formulario107) {
        this.formulario107 = formulario107;
    }

    public List<Formulario107> getFormularios107s() {
        return formularios107s;
    }

    public void setFormularios107s(List<Formulario107> formularios107s) {
        this.formularios107s = formularios107s;
    }

    public RetencionesImpuestoRenta getRetencion() {
        return retencion;
    }

    public void setRetencion(RetencionesImpuestoRenta retencion) {
        this.retencion = retencion;
    }

    public List<MasterCatalogo> getPeriodosFiscales() {
        return periodosFiscales;
    }

    public void setPeriodosFiscales(List<MasterCatalogo> periodosFiscales) {
        this.periodosFiscales = periodosFiscales;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }
//</editor-fold>

}
