/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThFormulario107;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThFormulario107Service;
import com.origami.sigef.talentohumano.model.DatRetRelDep;
import com.origami.sigef.talentohumano.model.Empleado;
import com.origami.sigef.talentohumano.model.Rdep;
import com.origami.sigef.talentohumano.model.RetRelDep;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
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
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thFormulario107View")
@ViewScoped
public class ThFormulario107Controller implements Serializable {

    @Inject
    private ValoresService valoresService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThFormulario107Service thFormulario107Service;
    @Inject
    private DatosGeneralesEntidadService datosGeneralesEntidadService;

    private LazyModel<Servidor> servidorLazy;
    private LazyModel<Entidad> entidadLazy;
    private Date fechaEmision;
    private OpcionBusqueda opcionBusqueda;
    private DatosGeneralesEntidad datosGeneralesEntidad;
    private Entidad entidad;

    private final String RESIDENCIALOCAL = "01";
    private final String CODIGOPAIS = "593";

    private List<Short> periodos;

    @PostConstruct
    public void init() {
        fechaEmision = new Date();
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        datosGeneralesEntidad = datosGeneralesEntidadService.find(1L);
        //lazy de las entidades
        entidadLazy = new LazyModel<>(Entidad.class);
        //lazy de los servidores
        servidorLazy = new LazyModel<>(Servidor.class);
        servidorLazy.getSorteds().put("persona.apellido", "ASC");
        servidorLazy.setDistinct(false);
        entidad = null;
    }

    public void generarXML() {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Se debe seleccionar un periodo fiscal");
            return;
        }
        if (entidad == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Se debe seleccionar un establecimiento");
            return;
        }
        String fechaFormat = new SimpleDateFormat("yyyy").format(fechaEmision);
        String ruta = valoresService.findByCodigo("RUTA_COMPROBANTE_FORMULARIO_107") + "RDEP_" + fechaFormat + ".xml";
        Rdep resp = convertirFormulario107ToRdep();
        if (DocumentosUtil.crearArchivo(resp, ruta)) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            servletSession.setNombreDocumento(ruta);
            servletSession.setContentType("text/xml");
            servletSession.addParametro("download", true);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            JsfUtil.addErrorMessage("", "Comprobante no generado");
        }
    }

    public Rdep convertirFormulario107ToRdep() {
        Rdep rdep = new Rdep();
        rdep.setNumRuc(datosGeneralesEntidad.getRuc());
        rdep.setAnio(opcionBusqueda.getAnio().intValue());
        rdep.setRetRelDep(new RetRelDep());
        rdep.getRetRelDep().setDatRetRelDep(new ArrayList<>());
        List<DatRetRelDep> result = new ArrayList<>();
        List<ThFormulario107> temp = thFormulario107Service.getList(opcionBusqueda.getAnio());
        for (ThFormulario107 f : temp) {
            decimoFondos(f);
            DatRetRelDep datRetRelDep = new DatRetRelDep();
            datRetRelDep.setEmpleado(new Empleado());
            datRetRelDep.setSuelSal(f.getSueldoMensual().doubleValue());
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
            Double infoIngGravEmpleado = f.getSueldoMensual().doubleValue(); // SUMA DE : sobSuelComRemu(0) + partUtil(0) + impRentEmpl(0)
            datRetRelDep.setIngGravConEsteEmpl(infoIngGravEmpleado);
            datRetRelDep.setSisSalNet(1); //SIN SISTEMA DE SALARIO NETO
            datRetRelDep.setApoPerIess(f.getAportePersonalIESSLosep().doubleValue());
            datRetRelDep.setAporPerIessConOtrosEmpls(new Double(0)); // NO SE USA
            datRetRelDep.setDeducVivienda(f.getGastoVivienda().doubleValue());
            datRetRelDep.setDeducSalud(f.getGastoSalud().doubleValue()); //// URG PREGUNTAR POR LAS VALIDACIONES
            datRetRelDep.setDeducEducartcult(f.getGastoEducacion().doubleValue()); //// URG PREGUNTAR POR LAS VALIDACIONES
            datRetRelDep.setDeducAliement(f.getGastoAlimentacion().doubleValue());
            datRetRelDep.setDeducVestim(f.getGastoVestimenta().doubleValue());
            datRetRelDep.setExoDiscap(f.getExoneracionDiscapacidad().doubleValue());
            datRetRelDep.setExoTerEd(f.getExoneracionTerceraEdad().doubleValue());
            datRetRelDep.setBasImp(valBaseImponibleGravada(f).doubleValue());
            datRetRelDep.setImpRentCaus(new Double(0)); // No SE USA
            datRetRelDep.setValRetAsuOtrosEmpls(f.getValorImpuestoRetenido().doubleValue());
            datRetRelDep.setValImpAsuEsteEmpl(new Double(0)); // NO SE USA
            datRetRelDep.setValRet(f.getTotalImpuestoRetenido().doubleValue());
            datRetRelDep.getEmpleado().setBenGalpg("NO");
            datRetRelDep.getEmpleado().setEnfcatastro(f.getEnfermedadCatastrofica() ? "SI" : "NO");
            datRetRelDep.getEmpleado().setTipIdRet(f.getTipoIdentificacion().equals("RUC") ? "C" : f.getTipoIdentificacion().equals("C")
                    ? f.getTipoIdentificacion() : f.getTipoIdentificacion().equals("P") ? f.getTipoIdentificacion() : "");
            datRetRelDep.getEmpleado().setNombreTrab(Utils.quitaDiacriticos(f.getNombreCliente()));
            datRetRelDep.getEmpleado().setApellidoTrab(Utils.quitaDiacriticos(f.getApellidoCliente()));
            datRetRelDep.getEmpleado().setIdRet(f.getIdentificacion());
            datRetRelDep.getEmpleado().setEstab(entidad.getEstablecimiento());
            datRetRelDep.getEmpleado().setResidenciaTrab(RESIDENCIALOCAL);
            datRetRelDep.getEmpleado().setPaisResidencia(CODIGOPAIS);
            datRetRelDep.getEmpleado().setAplicaConvenio("NA");
            datRetRelDep.getEmpleado().setTipoTrabajDiscap(f.getDiscapacidad() ? "02" : "01");
            datRetRelDep.getEmpleado().setPorcentajeDiscap(f.getPorcentajeDiscapacidad().intValue());
            datRetRelDep.getEmpleado().setTipIdDiscap("N");
            datRetRelDep.getEmpleado().setIdDiscap("999");
            result.add(datRetRelDep);
        }
        rdep.getRetRelDep().setDatRetRelDep(result);
        return rdep;
    }

    public void decimoFondos(ThFormulario107 thFormulario107) {
        BigDecimal decimoCuarto = thFormulario107Service.findByRubroByServidor(thFormulario107.getServidorId().longValue(), CONFIG.CONFIG_DECIMO_CUARTO, opcionBusqueda.getAnio());
        BigDecimal decimoTercero = thFormulario107Service.findByRubroByServidor(thFormulario107.getServidorId().longValue(), CONFIG.CONFIG_DECIMO_TERCERO, opcionBusqueda.getAnio());
        BigDecimal fondosReserva = thFormulario107Service.findByRubroByServidor(thFormulario107.getServidorId().longValue(), CONFIG.CONFIG_FONDOS_RESERVA, opcionBusqueda.getAnio());
        BigDecimal aportePersonalLosep = thFormulario107Service.findByRubroByServidor(thFormulario107.getServidorId().longValue(), CONFIG.CONFIG_APORTE_IESS_LOSEP, opcionBusqueda.getAnio());
        BigDecimal aportePersonalCodigo = thFormulario107Service.findByRubroByServidor(thFormulario107.getServidorId().longValue(), CONFIG.CONFIG_APORTE_IESS_CODIGO, opcionBusqueda.getAnio());
        thFormulario107.setDecimoCuarto(decimoCuarto != null ? decimoCuarto : BigDecimal.ZERO);
        thFormulario107.setDecimoTercero(decimoTercero != null ? decimoTercero : BigDecimal.ZERO);
        thFormulario107.setFondosReserva(fondosReserva != null ? fondosReserva : BigDecimal.ZERO);
        thFormulario107.setAportePersonalIESSLosep(aportePersonalLosep != null ? aportePersonalLosep : BigDecimal.ZERO);
        thFormulario107.setAportePersonalIESSCodigo(aportePersonalCodigo != null ? aportePersonalCodigo : BigDecimal.ZERO);
        if (thFormulario107.getAportePersonalIESSCodigo() != BigDecimal.ZERO && thFormulario107.getAportePersonalIESSLosep() != BigDecimal.ZERO) {
            thFormulario107.getAportePersonalIESSLosep().add(thFormulario107.getAportePersonalIESSCodigo());
        }
    }

    public BigDecimal valBaseImponibleGravada(ThFormulario107 thFormulario107) {
        //FALTA GASTOS POR ARTE Y CULTURA ???
        BigDecimal total = thFormulario107.getSueldoMensual().add(thFormulario107.getIngresosGravados()).subtract(thFormulario107.getAportePersonalIESSLosep())
                .subtract(thFormulario107.getGastoVivienda()).subtract(thFormulario107.getGastoSalud()).subtract(thFormulario107.getGastoEducacion())
                .subtract(thFormulario107.getGastoAlimentacion()).subtract(thFormulario107.getGastoVestimenta()).subtract(thFormulario107.getExoneracionDiscapacidad())
                .subtract(thFormulario107.getExoneracionTerceraEdad());
        thFormulario107.setBaseImponibleGravada(total);
        return thFormulario107.getBaseImponibleGravada();
    }

    public void findEstablecimiento() {
        JsfUtil.executeJS("PF('entidadDlg').show()");
        PrimeFaces.current().ajax().update("entidadForm");
    }

    public void entidadSeleccionada(Entidad entidad) {
        this.entidad = entidad;
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha cargado correctamente la entidad");
        JsfUtil.executeJS("PF('entidadDlg').hide()");
        PrimeFaces.current().ajax().update("idPanelEntidad");
    }

    public BigDecimal valIngresosGravadosEmpleador(ThFormulario107 thFormulario107) {
        thFormulario107.setIngresosGravadosEmpleador(thFormulario107.getSueldoMensual());/// + 303 (0)  + 305 (0) + 381 (0)
        return thFormulario107.getIngresosGravadosEmpleador();
    }

    public void printReport(Servidor servidor, Boolean accion) {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Se debe seleccionar un periodo fiscal");
            return;
        }
        ThFormulario107 thFormulario107 = thFormulario107Service.findByNamedQuery1("ThFormulario107.findByServidorId", new BigInteger(servidor.getId() + ""), opcionBusqueda.getAnio());
        if (thFormulario107 != null) {
            addParametrosReporte(thFormulario107);
            if (accion) {
                List<ThTipoRol> tiposRol = thInterfaces.getRoles(servidor, opcionBusqueda.getAnio());
                servletSession.addParametro("tipos_rol", tiposRol);
                servletSession.setNombreReporte("formulario_107_detalle");
            } else {
                servletSession.setNombreReporte("formulario_107");
            }
            servletSession.setNombreSubCarpeta("_talento_humano");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay declaración de gastos del servidor publico en el periodo fiscal seleccionado");
        }
    }

    private void addParametrosReporte(ThFormulario107 thFormulario107) {
        decimoFondos(thFormulario107);
        servletSession.addParametro("periodo_fiscal", opcionBusqueda.getAnio());
        servletSession.addParametro("fecha_entrega", new SimpleDateFormat("dd/MM/yyyy").format(fechaEmision));
        servletSession.addParametro("ruc_establecimiento", datosGeneralesEntidad.getRuc());
        servletSession.addParametro("razon_social", datosGeneralesEntidad.getNombreEntidad());
        servletSession.addParametro("identificacion", thFormulario107.getIdentificacion());
        servletSession.addParametro("nombres_completo", thFormulario107.getNombreCompleto());
        servletSession.addParametro("sueldos", thFormulario107.getSueldoMensual());
        servletSession.addParametro("ingresos_gravados", thFormulario107.getIngresosGravados());
        servletSession.addParametro("decimo_3ero", thFormulario107.getDecimoTercero());
        servletSession.addParametro("decimo_4to", thFormulario107.getDecimoCuarto());
        servletSession.addParametro("fondo_reserva", thFormulario107.getFondosReserva());
        servletSession.addParametro("aporte_personal_iess", thFormulario107.getAportePersonalIESSLosep());
        servletSession.addParametro("gastos_vivienda", thFormulario107.getGastoVivienda());
        servletSession.addParametro("gastos_salud", thFormulario107.getGastoSalud());
        servletSession.addParametro("gastos_educacion", thFormulario107.getGastoEducacion());
        servletSession.addParametro("gastos_alimentacion", thFormulario107.getGastoAlimentacion());
        servletSession.addParametro("gastos_vestimenta", thFormulario107.getGastoVestimenta());
        servletSession.addParametro("impuesto_retenido", thFormulario107.getTotalImpuestoRetenido());
        servletSession.addParametro("gastos_arte_cultura", BigDecimal.ZERO); //// falta preguntar
        servletSession.addParametro("exonero_discapacidad", thFormulario107.getExoneracionDiscapacidad());
        servletSession.addParametro("exonero_tercera_edad", thFormulario107.getExoneracionTerceraEdad());
        servletSession.addParametro("impuesto_periodo_declarado", thFormulario107.getValorImpuestoRetenido());
        servletSession.addParametro("impuesto_empleador", BigDecimal.ZERO);
        servletSession.addParametro("impuesto_renta_causada", BigDecimal.ZERO);
        servletSession.addParametro("sobresueldos_comisiones", BigDecimal.ZERO);
        servletSession.addParametro("fecha_impresion", fechaEmision);
        servletSession.addParametro("base_imponible_gravada", valBaseImponibleGravada(thFormulario107).doubleValue());
        servletSession.addParametro("ingresos_gravados_empleador", valIngresosGravadosEmpleador(thFormulario107));
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public DatosGeneralesEntidad getDatosGeneralesEntidad() {
        return datosGeneralesEntidad;
    }

    public void setDatosGeneralesEntidad(DatosGeneralesEntidad datosGeneralesEntidad) {
        this.datosGeneralesEntidad = datosGeneralesEntidad;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public DatosGeneralesEntidadService getDatosGeneralesEntidadService() {
        return datosGeneralesEntidadService;
    }

    public void setDatosGeneralesEntidadService(DatosGeneralesEntidadService datosGeneralesEntidadService) {
        this.datosGeneralesEntidadService = datosGeneralesEntidadService;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public LazyModel<Entidad> getEntidadLazy() {
        return entidadLazy;
    }

    public void setEntidadLazy(LazyModel<Entidad> entidadLazy) {
        this.entidadLazy = entidadLazy;
    }

}
