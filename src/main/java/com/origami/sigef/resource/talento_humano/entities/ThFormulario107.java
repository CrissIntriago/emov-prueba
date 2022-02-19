/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_formulario_107", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThFormulario107.findAll", query = "SELECT t FROM ThFormulario107 t"),
    @NamedQuery(name = "ThFormulario107.findById", query = "SELECT t FROM ThFormulario107 t WHERE t.id = :id"),
    @NamedQuery(name = "ThFormulario107.findByServidorId", query = "SELECT t FROM ThFormulario107 t WHERE t.servidorId = ?1 AND t.periodo = ?2"),
    @NamedQuery(name = "ThFormulario107.findByPersonaId", query = "SELECT t FROM ThFormulario107 t WHERE t.personaId = :personaId"),
    @NamedQuery(name = "ThFormulario107.findByIdentificacion", query = "SELECT t FROM ThFormulario107 t WHERE t.identificacion = :identificacion"),
    @NamedQuery(name = "ThFormulario107.findByTipoIdentificacion", query = "SELECT t FROM ThFormulario107 t WHERE t.tipoIdentificacion = :tipoIdentificacion"),
    @NamedQuery(name = "ThFormulario107.findByNombreCliente", query = "SELECT t FROM ThFormulario107 t WHERE t.nombreCliente = :nombreCliente"),
    @NamedQuery(name = "ThFormulario107.findByApellidoCliente", query = "SELECT t FROM ThFormulario107 t WHERE t.apellidoCliente = :apellidoCliente"),
    @NamedQuery(name = "ThFormulario107.findByNombreCompleto", query = "SELECT t FROM ThFormulario107 t WHERE t.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "ThFormulario107.findByDireccion", query = "SELECT t FROM ThFormulario107 t WHERE t.direccion = :direccion"),
    @NamedQuery(name = "ThFormulario107.findByFechaNacimiento", query = "SELECT t FROM ThFormulario107 t WHERE t.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "ThFormulario107.findByEnfermedadCatastrofica", query = "SELECT t FROM ThFormulario107 t WHERE t.enfermedadCatastrofica = :enfermedadCatastrofica"),
    @NamedQuery(name = "ThFormulario107.findByDiscapacidad", query = "SELECT t FROM ThFormulario107 t WHERE t.discapacidad = :discapacidad"),
    @NamedQuery(name = "ThFormulario107.findByNumConadis", query = "SELECT t FROM ThFormulario107 t WHERE t.numConadis = :numConadis"),
    @NamedQuery(name = "ThFormulario107.findByPorcentajeDiscapacidad", query = "SELECT t FROM ThFormulario107 t WHERE t.porcentajeDiscapacidad = :porcentajeDiscapacidad"),
    @NamedQuery(name = "ThFormulario107.findByFechaIngreso", query = "SELECT t FROM ThFormulario107 t WHERE t.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "ThFormulario107.findByFechaSalida", query = "SELECT t FROM ThFormulario107 t WHERE t.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "ThFormulario107.findByProvincia", query = "SELECT t FROM ThFormulario107 t WHERE t.provincia = :provincia"),
    @NamedQuery(name = "ThFormulario107.findByCanton", query = "SELECT t FROM ThFormulario107 t WHERE t.canton = :canton"),
    @NamedQuery(name = "ThFormulario107.findByPeriodo", query = "SELECT t FROM ThFormulario107 t WHERE t.periodo = ?1"),
    @NamedQuery(name = "ThFormulario107.findByGastoId", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoId = :gastoId"),
    @NamedQuery(name = "ThFormulario107.findByGastoVivienda", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoVivienda = :gastoVivienda"),
    @NamedQuery(name = "ThFormulario107.findByGastoSalud", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoSalud = :gastoSalud"),
    @NamedQuery(name = "ThFormulario107.findByGastoEducacion", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoEducacion = :gastoEducacion"),
    @NamedQuery(name = "ThFormulario107.findByGastoAlimentacion", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoAlimentacion = :gastoAlimentacion"),
    @NamedQuery(name = "ThFormulario107.findByGastoVestimenta", query = "SELECT t FROM ThFormulario107 t WHERE t.gastoVestimenta = :gastoVestimenta"),
    @NamedQuery(name = "ThFormulario107.findByIngresosGravados", query = "SELECT t FROM ThFormulario107 t WHERE t.ingresosGravados = :ingresosGravados"),
    @NamedQuery(name = "ThFormulario107.findByValorImpuestoRetenido", query = "SELECT t FROM ThFormulario107 t WHERE t.valorImpuestoRetenido = :valorImpuestoRetenido"),
    @NamedQuery(name = "ThFormulario107.findByExoneracionDiscapacidad", query = "SELECT t FROM ThFormulario107 t WHERE t.exoneracionDiscapacidad = :exoneracionDiscapacidad"),
    @NamedQuery(name = "ThFormulario107.findByExoneracionTerceraEdad", query = "SELECT t FROM ThFormulario107 t WHERE t.exoneracionTerceraEdad = :exoneracionTerceraEdad"),
    @NamedQuery(name = "ThFormulario107.findBySueldoMensual", query = "SELECT t FROM ThFormulario107 t WHERE t.sueldoMensual = :sueldoMensual"),
    @NamedQuery(name = "ThFormulario107.findByEstadoGastos", query = "SELECT t FROM ThFormulario107 t WHERE t.estadoGastos = :estadoGastos")})
public class ThFormulario107 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private BigInteger id;
    @Column(name = "servidor_id")
    private BigInteger servidorId;
    @Column(name = "persona_id")
    private BigInteger personaId;
    @Size(max = 250)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 50)
    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion;
    @Size(max = 2147483647)
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Size(max = 2147483647)
    @Column(name = "apellido_cliente")
    private String apellidoCliente;
    @Size(max = 2147483647)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Size(max = 250)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "enfermedad_catastrofica")
    private Boolean enfermedadCatastrofica;
    @Column(name = "discapacidad")
    private Boolean discapacidad;
    @Size(max = 10)
    @Column(name = "num_conadis")
    private String numConadis;
    @Column(name = "porcentaje_discapacidad")
    private Short porcentajeDiscapacidad;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Size(max = 150)
    @Column(name = "provincia")
    private String provincia;
    @Size(max = 150)
    @Column(name = "canton")
    private String canton;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "total_impuesto_retenido")
    private BigDecimal totalImpuestoRetenido;
    @Column(name = "gasto_id")
    private BigDecimal gastoId;
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
    @Column(name = "valor_impuesto_retenido")
    private BigDecimal valorImpuestoRetenido;
    @Column(name = "exoneracion_discapacidad")
    private BigDecimal exoneracionDiscapacidad;
    @Column(name = "exoneracion_tercera_edad")
    private BigDecimal exoneracionTerceraEdad;
    @Column(name = "sueldo_mensual")
    private BigDecimal sueldoMensual;
    @Column(name = "estado_gastos")
    private Boolean estadoGastos;

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
    private BigDecimal baseImponibleGravada;
    @Transient
    private BigDecimal ingresosGravadosEmpleador;

    public ThFormulario107() {
        decimoCuarto = BigDecimal.ZERO;
        decimoTercero = BigDecimal.ZERO;
        fondosReserva = BigDecimal.ZERO;
        aportePersonalIESSLosep = BigDecimal.ZERO;
        totalSueldo = BigDecimal.ZERO;
        aportePersonalIESSCodigo = null;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public Boolean getEnfermedadCatastrofica() {
        return enfermedadCatastrofica;
    }

    public void setEnfermedadCatastrofica(Boolean enfermedadCatastrofica) {
        this.enfermedadCatastrofica = enfermedadCatastrofica;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getEstadoGastos() {
        return estadoGastos;
    }

    public void setEstadoGastos(Boolean estadoGastos) {
        this.estadoGastos = estadoGastos;
    }

    public BigDecimal getGastoId() {
        return gastoId;
    }

    public void setGastoId(BigDecimal gastoId) {
        this.gastoId = gastoId;
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

    public BigDecimal getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(BigDecimal sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
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

}
