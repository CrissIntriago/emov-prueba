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
@Named(value = "hipotecarioIESSView")
@ViewScoped
public class PrestamoHipotecarioController implements Serializable {

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
    private PrestamoIess prestamoHipotecario;
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
        rolSeleccionado = new TipoRol();
        prestamoHipotecario = new PrestamoIess();
        prestamoHipotecario.setServidor(new Servidor());
        prestamoHipotecario.getServidor().setPersona(new Cliente());
        prestamoHipotecario.getServidor().setDistributivo(new Distributivo());
        prestamoHipotecario.setDistributivo(new Distributivo());
        prestamoHipotecario.setTipoRol(new TipoRol());
        prestamoHipotecario.setTipoPrestamo(new CatalogoItem());
        prestamoHipotecario.setValorParametrizado(new ParametrosTalentoHumano());
//        rolGeneral = catalogoItemService.getEstadoRol("rol_general");
        hipotecario = catalogoItemService.getEstadoRol("IESS_HIPOTECARIO");
        quirografario = catalogoItemService.getEstadoRol("IESS_QUIROGRAFARIO");
        valoPrametro = parametroService.valorParametroTipo("PRES_HIP");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        periodo = Utils.getAnio(new Date()).shortValue();
        rolesMensuales = tipoRolService.listaRolesXanio();
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }

    public void buscar() {
        rolSeleccionado = prestamoHipotecario.getTipoRol();
        lazy = new LazyModel<>(PrestamoIess.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("periodo", periodo);
        lazy.getFilterss().put("tipoPrestamo", hipotecario);
        lazy.getFilterss().put("tipoRol", prestamoHipotecario.getTipoRol());
    }

    public void buscarServidor() {
        if (prestamoHipotecario.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un Tipo Rol.");
            return;
        }
        Servidor serv = distributivoService.getServidorAnio(periodo, this.cedula);
        if (serv != null) {
            prestamoHipotecario.setServidor(serv);
            prestamoHipotecario.setDistributivo(serv.getDistributivo());
        } else {
//            Utils.openDialog("/facelet/talentoHumano/dialogServidor", null);
            listaServidoresMostrar = new ArrayList<>();
//            if (prestamoHipotecario.getTipoRol().getTipoRol().getCodigo().equals("rol_adicional")) {
//                listaServidoresMostrar = prestamoService.getServidorXmesNotInPrestamosIessMes("rol_general", prestamoHipotecario.getTipoRol().getMes().getCodigo(), hipotecario);
//            }
//            if ((prestamoHipotecario.getTipoRol().getTipoRol().getCodigo().equals("rol_general"))) {
            listaServidoresMostrar = servidorService.getServidorInDiaLaborado(rolSeleccionado);
//            }
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
        valores = valorService.FindValoresXServidor(ser, prestamoHipotecario.getTipoRol().getAnio());

        if (!valores.isEmpty()) {
            for (ValoresRoles item : valores) {
                if ("PRES_HIP".equals(item.getValorParametrizable().getTipo().getCodigo())) {
                    aprobar = true;
                }
            }
        }
        if (aprobar == false) {
            JsfUtil.addWarningMessage("Advertencia", "El Servidor Público no tiene agregado una cuenta contable de Prestamos Hipotecarios");
            return;
        }
        prestamoHipotecario.setServidor(ser);
        cedula = prestamoHipotecario.getServidor().getPersona().getIdentificacion();
        if (prestamoHipotecario.getServidor() != null) {
            prestamoHipotecario.setDistributivo(prestamoHipotecario.getServidor().getDistributivo());
        }
    }

    public void selectDataServidor(Servidor servidor) {
        boolean aprobar = false;
//        Servidor ser;
//        ser = (Servidor) evt.getObject();
        List<ValoresRoles> valores;
        valores = valorService.FindValoresXServidor(servidor, prestamoHipotecario.getTipoRol().getAnio());

        if (!valores.isEmpty()) {
            for (ValoresRoles item : valores) {
                if ("PRES_HIP".equals(item.getValorParametrizable().getTipo().getCodigo())) {
                    aprobar = true;
                }
            }
        }
        if (aprobar == false) {
            JsfUtil.addWarningMessage("Advertencia", "El Servidor Público no tiene agregado una cuenta contable de Prestamos Hipotecarios");
            return;
        }
        prestamoHipotecario.setServidor(servidor);
        cedula = prestamoHipotecario.getServidor().getPersona().getIdentificacion();
        if (prestamoHipotecario.getServidor() != null) {
            prestamoHipotecario.setDistributivo(prestamoHipotecario.getServidor().getDistributivo());
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public void cancelar() {
        cedula = "";
        collapsedIESS = Boolean.TRUE;
        lazy = null;
        rolSeleccionado = new TipoRol();
        resetValues();
    }

    public void guardar() {
        boolean edit = prestamoHipotecario.getId() != null;
        if (prestamoHipotecario.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Registro de Servidor", "El Servidor ya se encuentra registrado");
            return;
        }
        List<PrestamoIess> lista = prestamoService.listPrestamoIESS(prestamoHipotecario.getTipoRol(), hipotecario);
        try {
            if (edit) {
                prestamoHipotecario.setFechaModificacion(new Date());
                prestamoHipotecario.setUsuarioModifica(userSession.getNameUser());
                prestamoService.edit(prestamoHipotecario);
            } else {
                if (buscarServidor(lista, prestamoHipotecario)) {
                    JsfUtil.addWarningMessage("Registro de Servidor", "El Servidor ya se encuentra registrado");
                    return;
                }
                if (!TalentoHumano.validarFechaInicio(prestamoHipotecario.getServidor().getFechaIngreso(), rolSeleccionado)) {
                    JsfUtil.addWarningMessage("Registro de Servidor", "Verifique la fecha de ingreso del servidor");
                    return;
                }
                prestamoHipotecario.setFechaCreacion(new Date());
                prestamoHipotecario.setUsuarioCreacion(userSession.getNameUser());
                prestamoHipotecario.setTipoPrestamo(hipotecario);
                prestamoHipotecario.setValorParametrizado(valoPrametro);
                prestamoHipotecario.setPeriodo(rolSeleccionado.getAnio());
                prestamoHipotecario = prestamoService.create(prestamoHipotecario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resetValues();
        collapsedIESS = Boolean.TRUE;
        JsfUtil.addInformationMessage("Prestamo Hipotecario", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
    }

    public boolean buscarServidor(List<PrestamoIess> lista, PrestamoIess servidor) {
        return lista.stream().anyMatch((p) -> (servidor.getServidor().equals(p.getServidor())));
    }

    public void editar(PrestamoIess p) {
        this.prestamoHipotecario = p;
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
        prestamoHipotecario = new PrestamoIess();
        prestamoHipotecario.setServidor(new Servidor());
        prestamoHipotecario.getServidor().setPersona(new Cliente());
        prestamoHipotecario.getServidor().setDistributivo(new Distributivo());
        prestamoHipotecario.setDistributivo(new Distributivo());
        prestamoHipotecario.setTipoRol(new TipoRol());
        prestamoHipotecario.setTipoPrestamo(new CatalogoItem());
        prestamoHipotecario.setValorParametrizado(new ParametrosTalentoHumano());
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
                JsfUtil.addSuccessMessage("Prestamo Hipotecario", "Guardado con Exito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("formIESS");
    }

    public void form() {
        if (prestamoHipotecario.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Error", "El Servidor ya se encuentra registrado");
            return;
        }
        List<PrestamoIess> lista = prestamoService.listPrestamoIESS(prestamoHipotecario.getTipoRol(), hipotecario);
        if (!lista.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "Ya cuenta con datos registrados");
            return;
        }
        if (prestamoHipotecario.getTipoRol().getId() == null) {
            this.collapsedIESS = Boolean.FALSE;
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        if (!prestamoHipotecario.getTipoRol().getEstadoAprobacion().equals(registrado)) {
            JsfUtil.addWarningMessage("Rol seleccionado se encuentra ya aprobado", "");
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
        if (this.rolCopia.equals(prestamoHipotecario.getTipoRol())) {
            JsfUtil.addWarningMessage("Error", "No puede realizar Copia del mismo rol");
            return;
        }
        Date fechaCreacion = new Date();
        executeUpdate = prestamoService.getcopia(prestamoHipotecario.getTipoRol(), fechaCreacion, userSession.getNameUser(), rolCopia, hipotecario);
        if (executeUpdate > 0) {
            JsfUtil.addSuccessMessage("Datos", "Los Datos del Rol Anterior fueron Traidos con éxito");
        }
        PrimeFaces.current().executeScript("PF('dialogQuirografario').hide()");
        PrimeFaces.current().ajax().update(":formIESS");
    }

    public boolean disabledBtn() {
        if (prestamoHipotecario.getId() != null) {
            if (prestamoHipotecario.getTipoRol().getEstadoAprobacion().equals(registrado)) {
                return true;
            }
        }
        return false;
    }

    public boolean renderedColumn() {
        if (prestamoHipotecario.getId() != null && prestamoHipotecario.getTipoRol().getId() != null) {
            if (prestamoHipotecario.getTipoRol().getEstadoAprobacion().equals(registrado)) {
                return true;
            }
        }
        return false;
    }

    public LazyModel<PrestamoIess> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PrestamoIess> lazy) {
        this.lazy = lazy;
    }

    public PrestamoIess getPrestamoHipotecario() {
        return prestamoHipotecario;
    }

    public void setPrestamoHipotecario(PrestamoIess prestamoHipotecario) {
        this.prestamoHipotecario = prestamoHipotecario;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
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
