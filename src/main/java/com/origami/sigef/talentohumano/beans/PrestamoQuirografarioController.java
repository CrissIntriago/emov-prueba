/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PrestamoIess;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.PrestamoIESService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "quirografarioView")
@ViewScoped
public class PrestamoQuirografarioController implements Serializable {

    @Inject
    private PrestamoIESService prestamoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private DistributivoEscalaService distributivoService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private ValoresRolesService valorService;
    @Inject
    private ServidorService servidorService;

    private LazyModel<PrestamoIess> lazy;
    private List<MasterCatalogo> periodos;
    private PrestamoIess prestamoQuirografario;
    private CatalogoItem hipotecario;
    private CatalogoItem quirografario;
    private OpcionBusqueda busqueda;
    private short periodo;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private Boolean collapsedIESS;
    private TipoRol rolCopia;
    private TipoRol rolSeleccionado;
    private String cedula;
//    private CatalogoItem rolGeneral;
    private ParametrosTalentoHumano valoPrametro;
    private List<Servidor> listaServidoresMostrar;

    @PostConstruct
    public void init() {
        cedula = "";
        collapsedIESS = Boolean.TRUE;
        rolCopia = new TipoRol();
        prestamoQuirografario = new PrestamoIess();
        prestamoQuirografario.setServidor(new Servidor());
        prestamoQuirografario.getServidor().setPersona(new Cliente());
        prestamoQuirografario.getServidor().setDistributivo(new Distributivo());
        prestamoQuirografario.setDistributivo(new Distributivo());
        prestamoQuirografario.setTipoRol(new TipoRol());
        prestamoQuirografario.setTipoPrestamo(new CatalogoItem());
        prestamoQuirografario.setValorParametrizado(new ParametrosTalentoHumano());
//        rolGeneral = catalogoItemService.getEstadoRol("rol_general");
        hipotecario = catalogoItemService.getEstadoRol("IESS_HIPOTECARIO");
        quirografario = catalogoItemService.getEstadoRol("IESS_QUIROGRAFARIO");
        valoPrametro = parametroService.valorParametroTipo("PRES_QUI");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        periodo = Utils.getAnio(new Date()).shortValue();
        rolesMensuales = tipoRolService.listaRolesXanio();
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }

    public void buscar() {
        rolSeleccionado = prestamoQuirografario.getTipoRol();
        lazy = new LazyModel<>(PrestamoIess.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("periodo", periodo);
        lazy.getFilterss().put("tipoPrestamo", quirografario);
        lazy.getFilterss().put("tipoRol", prestamoQuirografario.getTipoRol());
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
    }

    public void buscarServidor() {
        if (prestamoQuirografario.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un Tipo Rol.");
            return;
        }
        Servidor serv = distributivoService.getServidorAnio(periodo, cedula);
        if (serv != null) {
            prestamoQuirografario.setServidor(serv);
            prestamoQuirografario.setDistributivo(serv.getDistributivo());
        } else {
            listaServidoresMostrar = new ArrayList<>();
            listaServidoresMostrar = servidorService.getServidorInDiaLaborado(rolSeleccionado);
            PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
            PrimeFaces.current().ajax().update("formServidorPublico");
            PrimeFaces.current().ajax().update(":servidorPublicoDialog");
        }
    }

    public void selectData(SelectEvent evt) {
        boolean aprobar = false;
        Servidor ser;
        ser = (Servidor) evt.getObject();
        List<ValoresRoles> valores;
        valores = valorService.FindValoresXServidor(ser, prestamoQuirografario.getTipoRol().getAnio());
        if (!valores.isEmpty()) {
            for (ValoresRoles item : valores) {
                if ("PRES_QUI".equals(item.getValorParametrizable().getTipo().getCodigo())) {
                    System.out.println(item.getValorParametrizable().getTipo().getTexto());
                    aprobar = true;
                }
            }
        }
        if (aprobar == false) {
            JsfUtil.addWarningMessage("Advertencia", "El Servidor Público no tiene agregado una cuenta contable de Prestamos Quirografarios");
            return;
        }
        prestamoQuirografario.setServidor(ser);
        cedula = prestamoQuirografario.getServidor().getPersona().getIdentificacion();
        if (prestamoQuirografario.getServidor() != null) {
            prestamoQuirografario.setDistributivo(prestamoQuirografario.getServidor().getDistributivo());
        }
    }

    public void selectDataServidor(Servidor servidor) {
        boolean aprobar = false;
        List<ValoresRoles> valores;
        valores = valorService.FindValoresXServidor(servidor, prestamoQuirografario.getTipoRol().getAnio());
        if (!valores.isEmpty()) {
            for (ValoresRoles item : valores) {
                if ("PRES_QUI".equals(item.getValorParametrizable().getTipo().getCodigo())) {
                    System.out.println(item.getValorParametrizable().getTipo().getTexto());
                    aprobar = true;
                }
            }
        }
        if (aprobar == false) {
            JsfUtil.addWarningMessage("Advertencia", "El Servidor Público no tiene agregado una cuenta contable de Prestamos Quirografarios");
            return;
        }
        prestamoQuirografario.setServidor(servidor);
        cedula = prestamoQuirografario.getServidor().getPersona().getIdentificacion();
        if (prestamoQuirografario.getServidor() != null) {
            prestamoQuirografario.setDistributivo(prestamoQuirografario.getServidor().getDistributivo());
        }
        System.out.println("entro");
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public void cancelar() {
        collapsedIESS = Boolean.TRUE;
        lazy = null;
        cedula = "";
        rolSeleccionado = new TipoRol();
        resetValues();
    }

    public void guardar() {
        boolean edit = prestamoQuirografario.getId() != null;
        if (prestamoQuirografario.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Error", "El Servidor ya se encuentra registrado");
            return;
        }
        List<PrestamoIess> lista = prestamoService.listPrestamoIESS(prestamoQuirografario.getTipoRol(), quirografario);
        try {
            if (edit) {
                prestamoQuirografario.setFechaModificacion(new Date());
                prestamoQuirografario.setUsuarioModifica(userSession.getNameUser());
                prestamoService.edit(prestamoQuirografario);
            } else {
                if (buscarServidor(lista, prestamoQuirografario)) {
                    JsfUtil.addWarningMessage("Registro de Servidor", "El Servidor ya se encuentra registrado");
                    return;
                }
                if (!TalentoHumano.validarFechaInicio(prestamoQuirografario.getServidor().getFechaIngreso(), rolSeleccionado)) {
                    JsfUtil.addWarningMessage("Registro de Servidor", "Verifique la fecha de ingreso del servidor");
                    return;
                }
                prestamoQuirografario.setFechaCreacion(new Date());
                prestamoQuirografario.setUsuarioCreacion(userSession.getNameUser());
                prestamoQuirografario.setTipoPrestamo(quirografario);
                prestamoQuirografario.setValorParametrizado(valoPrametro);
                prestamoQuirografario.setPeriodo(rolSeleccionado.getAnio());
                prestamoQuirografario = prestamoService.create(prestamoQuirografario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resetValues();
        collapsedIESS = Boolean.TRUE;
        JsfUtil.addInformationMessage("Prestamo Quirografario", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
//        PrimeFaces.current().ajax().update("formMain");
    }

    public boolean buscarServidor(List<PrestamoIess> lista, PrestamoIess servidor) {
        return lista.stream().anyMatch((p) -> (servidor.getServidor().equals(p.getServidor())));
    }

    public boolean disabledCellEdit() {
        if (prestamoQuirografario.getId() != null) {
            if (prestamoQuirografario.getTipoRol().getEstadoAprobacion().equals(registrado)) {
                return false;
            }
        }
        return true;
    }

    public boolean renderedColumn() {
        if (prestamoQuirografario.getId() != null && prestamoQuirografario.getTipoRol().getId() != null) {
            if (prestamoQuirografario.getTipoRol().getEstadoAprobacion().equals(registrado)) {
                return true;
            }
        }
        return false;
    }

    public void editar(PrestamoIess p) {
        this.prestamoQuirografario = p;
        collapsedIESS = Boolean.FALSE;
    }

    public void eliminar(PrestamoIess p) {
        if (p.getTipoRol().getEstadoAprobacion().equals(registrado)) {
            p.setEstado(Boolean.FALSE);
            prestamoService.edit(p);
        } else {
            JsfUtil.addWarningMessage("Eliminar", "No puede Eliminar con Roles Aprobados o Pagados");
        }
    }

    public void resetValues() {
        cedula = "";
        prestamoQuirografario = new PrestamoIess();
        prestamoQuirografario.setServidor(new Servidor());
        prestamoQuirografario.getServidor().setPersona(new Cliente());
        prestamoQuirografario.getServidor().setDistributivo(new Distributivo());
        prestamoQuirografario.setDistributivo(new Distributivo());
        prestamoQuirografario.setTipoRol(new TipoRol());
        prestamoQuirografario.setTipoPrestamo(new CatalogoItem());
        prestamoQuirografario.setValorParametrizado(new ParametrosTalentoHumano());
    }

    public void guardarCellEdit(PrestamoIess p) {
        try {
            if (p.getNumeroCuota() < 0) {
                JsfUtil.addSuccessMessage("Error", "El número de cuota no valido");
                return;
            } else {
                p.setFechaModificacion(new Date());
                p.setUsuarioModifica(userSession.getNameUser());
                prestamoService.edit(p);
                JsfUtil.addSuccessMessage("Prestamo Quirografario", "Guardado con Exito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("formIESS");
    }

    public void form() {
        List<PrestamoIess> lista = prestamoService.listPrestamoIESS(prestamoQuirografario.getTipoRol(), quirografario);
        if (lista != null) {
            if (!lista.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Ya cuenta con datos registrados");
                return;
            }
        }
        if (prestamoQuirografario.getTipoRol().getId() == null) {
            this.collapsedIESS = Boolean.FALSE;
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        PrimeFaces.current().executeScript("PF('dialogQuirografario').show()");
        PrimeFaces.current().ajax().update(":formIESS");
    }

    public void generarCopia() {
        int executeUpdate;
        if (this.rolCopia.getId() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        if (this.rolCopia.equals(prestamoQuirografario.getTipoRol())) {
            JsfUtil.addWarningMessage("Error", "No puede realizar Copia del mismo rol");
            return;
        }
        Date fechaCreacion = new Date();
        executeUpdate = prestamoService.getcopia(prestamoQuirografario.getTipoRol(), fechaCreacion, userSession.getNameUser(), rolCopia, quirografario);
        if (executeUpdate > 0) {
            JsfUtil.addSuccessMessage("Datos", "Los Datos del Rol Anterior fueron Traidos con éxito");
        }
        PrimeFaces.current().executeScript("PF('dialogQuirografario').hide()");
        PrimeFaces.current().ajax().update(":formIESS");
    }

    public LazyModel<PrestamoIess> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PrestamoIess> lazy) {
        this.lazy = lazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public PrestamoIess getPrestamoQuirografario() {
        return prestamoQuirografario;
    }

    public void setPrestamoQuirografario(PrestamoIess prestamoQuirografario) {
        this.prestamoQuirografario = prestamoQuirografario;
    }

    public CatalogoItem getHipotecario() {
        return hipotecario;
    }

    public void setHipotecario(CatalogoItem hipotecario) {
        this.hipotecario = hipotecario;
    }

    public CatalogoItem getQuirografario() {
        return quirografario;
    }

    public void setQuirografario(CatalogoItem quirografario) {
        this.quirografario = quirografario;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public CatalogoItem getRegistrado() {
        return registrado;
    }

    public void setRegistrado(CatalogoItem registrado) {
        this.registrado = registrado;
    }

    public Boolean getCollapsedIESS() {
        return collapsedIESS;
    }

    public void setCollapsedIESS(Boolean collapsedIESS) {
        this.collapsedIESS = collapsedIESS;
    }

    public TipoRol getRolCopia() {
        return rolCopia;
    }

    public void setRolCopia(TipoRol rolCopia) {
        this.rolCopia = rolCopia;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<Servidor> getListaServidoresMostrar() {
        return listaServidoresMostrar;
    }

    public void setListaServidoresMostrar(List<Servidor> listaServidoresMostrar) {
        this.listaServidoresMostrar = listaServidoresMostrar;
    }

}
