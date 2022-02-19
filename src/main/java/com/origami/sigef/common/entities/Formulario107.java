/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author jesus
 */
@Entity
@Table(name = "formulario107", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Formulario107.findAll", query = "SELECT f FROM Formulario107 f")})
public class Formulario107 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @Id
    private BigInteger id;
    @Column(name = "servidor_id")
    private BigInteger servidorId;
    @Column(name = "persona_id")
    private BigInteger personaId;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "descripcion_rol")
    private String descripcionRol;
    @Column(name = "estado_aprobado")
    private String estadoAprobado;
    @Column(name = "tiporol_id")
    private BigInteger tiporolId;
    @Column(name = "sueldo_mensual")
    private BigDecimal sueldoMensual;
    @Column(name = "impuesto_renta_mensual")
    private BigDecimal impuestoRentaMensual;
    @Column(name = "gasto_vivienda")
    private BigDecimal gastoVivienda;
    @Column(name = "gasto_salud")
    private BigDecimal gastoSalud;
    @Column(name = "gasto_educacion")
    private BigDecimal gastoEducacion;
    @Column(name = "gasto_alimentacion")
    private BigDecimal gastoAlimentacion;
    @Column(name = "gasto_vestimenta")
    private BigDecimal gastoVestimenta;
    @Column(name = "ingresos_gravados")
    private BigDecimal ingresosGravados;
    @Column(name = "estado_gastos")
    private Boolean estadoGastos;
    @Column(name = "canton")
    private String canton;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Column(name = "gasto_id")
    private BigInteger gastoId;
    @Column(name = "valor_impuesto_retenido")
    private BigDecimal valorImpuestoRetenido;
    @Column(name = "exoneracion_discapacidad")
    private BigDecimal exoneracionDiscapacidad;
    @Column(name = "exoneracion_tercera_edad")
    private BigDecimal exoneracionTerceraEdad;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "discapacidad")
    private Boolean discapacidad;
    @Column(name = "num_conadis")
    private String numConadis;
    @Column(name = "porcentaje_discapacidad")
    private Short porcentajeDiscapacidad;
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Column(name = "apellido_cliente")
    private String apellidoCliente;
    @Column(name = "enfermedad_catastrofica")
    private Boolean enfermedadCatastrofica;
    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion;

    @Transient
    private BigDecimal decimoTercero;
    @Transient
    private BigDecimal decimoCuarto;
    @Transient
    private BigDecimal fondosReserva;
    @Transient
    private BigDecimal aportePersonalIESSLosep;
    @Transient
    private BigDecimal totalSueldo;
    @Transient
    private BigDecimal aportePersonalIESSCodigo;
    @Transient
    private BigDecimal totalImpuestoRetenido;
    @Transient
    private GastoPersonal gastoPersonal;
    @Transient
    private BigDecimal baseImponibleGravada;
    @Transient
    private BigDecimal ingresosGravadosEmpleador;

    public Formulario107() {
        decimoCuarto = BigDecimal.ZERO;
        decimoTercero = BigDecimal.ZERO;
        fondosReserva = BigDecimal.ZERO;
        aportePersonalIESSLosep = BigDecimal.ZERO;
        totalSueldo = BigDecimal.ZERO;
        aportePersonalIESSCodigo = null;
        totalImpuestoRetenido = BigDecimal.ZERO;
    }

    public BigInteger getServidorId() {
        return servidorId;
    }

    public void setServidorId(BigInteger servidorId) {
        this.servidorId = servidorId;
    }

    public BigInteger getPersonaId() {
        return personaId;
    }

    public void setPersonaId(BigInteger personaId) {
        this.personaId = personaId;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public BigInteger getTiporolId() {
        return tiporolId;
    }

    public void setTiporolId(BigInteger tiporolId) {
        this.tiporolId = tiporolId;
    }

    public BigDecimal getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(BigDecimal sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

    public BigDecimal getImpuestoRentaMensual() {
        return impuestoRentaMensual;
    }

    public void setImpuestoRentaMensual(BigDecimal impuestoRentaMensual) {
        this.impuestoRentaMensual = impuestoRentaMensual;
    }

    public BigDecimal getGastoVivienda() {
        return gastoVivienda;
    }

    public void setGastoVivienda(BigDecimal gastoVivienda) {
        this.gastoVivienda = gastoVivienda;
    }

    public BigDecimal getGastoSalud() {
        return gastoSalud;
    }

    public void setGastoSalud(BigDecimal gastoSalud) {
        this.gastoSalud = gastoSalud;
    }

    public BigDecimal getGastoEducacion() {
        return gastoEducacion;
    }

    public void setGastoEducacion(BigDecimal gastoEducacion) {
        this.gastoEducacion = gastoEducacion;
    }

    public BigDecimal getGastoAlimentacion() {
        return gastoAlimentacion;
    }

    public void setGastoAlimentacion(BigDecimal gastoAlimentacion) {
        this.gastoAlimentacion = gastoAlimentacion;
    }

    public BigDecimal getGastoVestimenta() {
        return gastoVestimenta;
    }

    public void setGastoVestimenta(BigDecimal gastoVestimenta) {
        this.gastoVestimenta = gastoVestimenta;
    }

    public BigDecimal getIngresosGravados() {
        return ingresosGravados;
    }

    public void setIngresosGravados(BigDecimal ingresosGravados) {
        this.ingresosGravados = ingresosGravados;
    }

    public Boolean getEstadoGastos() {
        return estadoGastos;
    }

    public void setEstadoGastos(Boolean estadoGastos) {
        this.estadoGastos = estadoGastos;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigInteger getGastoId() {
        return gastoId;
    }

    public void setGastoId(BigInteger gastoId) {
        this.gastoId = gastoId;
    }

    public String getEstadoAprobado() {
        return estadoAprobado;
    }

    public void setEstadoAprobado(String estadoAprobado) {
        this.estadoAprobado = estadoAprobado;
    }

    public BigDecimal getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(BigDecimal decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public BigDecimal getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(BigDecimal decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public BigDecimal getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(BigDecimal fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public BigDecimal getAportePersonalIESSLosep() {
        return aportePersonalIESSLosep;
    }

    public void setAportePersonalIESSLosep(BigDecimal aportePersonalIESSLosep) {
        this.aportePersonalIESSLosep = aportePersonalIESSLosep;
    }

    public BigDecimal getTotalSueldo() {
        return totalSueldo;
    }

    public void setTotalSueldo(BigDecimal totalSueldo) {
        this.totalSueldo = totalSueldo;
    }

    public BigDecimal getAportePersonalIESSCodigo() {
        return aportePersonalIESSCodigo;
    }

    public void setAportePersonalIESSCodigo(BigDecimal aportePersonalIESSCodigo) {
        this.aportePersonalIESSCodigo = aportePersonalIESSCodigo;
    }

    public BigDecimal getTotalImpuestoRetenido() {
        return totalImpuestoRetenido;
    }

    public void setTotalImpuestoRetenido(BigDecimal totalImpuestoRetenido) {
        this.totalImpuestoRetenido = totalImpuestoRetenido;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigDecimal getValorImpuestoRetenido() {
        return valorImpuestoRetenido;
    }

    public void setValorImpuestoRetenido(BigDecimal valorImpuestoRetenido) {
        this.valorImpuestoRetenido = valorImpuestoRetenido;
    }

    public BigDecimal getExoneracionDiscapacidad() {
        return exoneracionDiscapacidad;
    }

    public void setExoneracionDiscapacidad(BigDecimal exoneracionDiscapacidad) {
        this.exoneracionDiscapacidad = exoneracionDiscapacidad;
    }

    public BigDecimal getExoneracionTerceraEdad() {
        return exoneracionTerceraEdad;
    }

    public void setExoneracionTerceraEdad(BigDecimal exoneracionTerceraEdad) {
        this.exoneracionTerceraEdad = exoneracionTerceraEdad;
    }

    public GastoPersonal getGastoPersonal() {
        return gastoPersonal;
    }

    public void setGastoPersonal(GastoPersonal gastoPersonal) {
        this.gastoPersonal = gastoPersonal;
    }

    public BigDecimal getBaseImponibleGravada() {
        return baseImponibleGravada;
    }

    public void setBaseImponibleGravada(BigDecimal baseImponibleGravada) {
        this.baseImponibleGravada = baseImponibleGravada;
    }

    public BigDecimal getIngresosGravadosEmpleador() {
        return ingresosGravadosEmpleador;
    }

    public void setIngresosGravadosEmpleador(BigDecimal ingresosGravadosEmpleador) {
        this.ingresosGravadosEmpleador = ingresosGravadosEmpleador;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Boolean getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Boolean discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getNumConadis() {
        return numConadis;
    }

    public void setNumConadis(String numConadis) {
        this.numConadis = numConadis;
    }

    public Short getPorcentajeDiscapacidad() {
        return porcentajeDiscapacidad;
    }

    public void setPorcentajeDiscapacidad(Short porcentajeDiscapacidad) {
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public Boolean getEnfermedadCatastrofica() {
        return enfermedadCatastrofica;
    }

    public void setEnfermedadCatastrofica(Boolean enfermedadCatastrofica) {
        this.enfermedadCatastrofica = enfermedadCatastrofica;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

}
