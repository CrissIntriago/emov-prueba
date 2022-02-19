/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "dialogCliente")
@ViewScoped
public class dialogCliente implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyModel<Cliente> clienteServido;
    private List<Cliente> clientes;
    private Cliente cliente;
    private LazyModel<Distributivo> distributivoMostrar;
    private Distributivo distributivo;
    private Banco banco;
    private LazyModel<Banco> bancoMostrar;
    private List<CatalogoItem> tiposDeIdentificacion;
    private List<CatalogoItem> tiposDeGenero;
    private List<Distributivo> listDis;

    //erwin
    private List<CatalogoItem> catClasificacion;
    private List<CatalogoItem> catTipo;
    private Boolean bandera = Boolean.FALSE;
    private Boolean BoolFecha = Boolean.TRUE;
    private String TipoGlobal;
    private Integer activeIndex = 0;

    @Inject
    private UserSession userSession;
//    @Inject
//    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CatalogoItemService catalogo;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private DistributivoEscalaService distributivoEscalaService;
    @Inject
    private DatosGeneralesEntidadService provinciaCantonService;

    //para traer escala de un distributivo
    private OpcionBusqueda busqueda;
    private List<DistributivoEscala> distributivoEscalaList;

    private List<Provincia> provincias;
    private List<Canton> cantones;
    private Provincia provinciaSelecionada;

    @PostConstruct
    public void inicializate() {
        busqueda = new OpcionBusqueda();
        String tipo = JsfUtil.getRequestParameter("PROV");
        TipoGlobal = tipo;
        clienteServido = new LazyModel<>(Cliente.class);
        this.cliente = new Cliente();
        //distributivoMostrar = new LazyModel<>(Distributivo.class);
        this.distributivo = new Distributivo();
        bancoMostrar = new LazyModel<>(Banco.class);
        this.banco = new Banco();
        this.provinciaSelecionada = new Provincia();
        //lista con todo creo
        distributivoEscalaList = distributivoEscalaService.getEscalaDistributivoServidorNull(busqueda);
        clienteServido.getFilterss().put("estado", true);
        clienteServido.getFilterss().put("tipoIdentificacion.codigo:equal", "C");
        clienteServido.getSorteds().put("apellido", "ASC");
        String b = "C";

        try {
            if (tipo.equals("PROVEEDOR")) {
                clientes = clienteService.getClienteNoProNoSer();
                b = "R";
                bandera = Boolean.TRUE;
            } else {
                b = "C";
                bandera = Boolean.FALSE;

            }
        } catch (Exception e) {
        }
        String b2 = "tipo_identificacion_beneficiario";
        tiposDeIdentificacion = catalogo.findByCedulaRuc(b, b2);
        this.tiposDeGenero = catalogoService.getItemsByCatalogo("tipo_genero");
        //erwin
        catClasificacion = catalogo.findCatalogoClasificacion1("personeria_juridica");
        this.provincias = provinciaCantonService.getProvincias();
        if (userSession.getVarTemp() != null) {
            if (userSession.getVarTemp() instanceof Integer) {
                activeIndex = (Integer) userSession.getVarTemp();
            }
        }
    }

    public int edad(Date fecha_nac) throws ParseException {
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fecha_nac);
        Calendar fechaActual = Calendar.getInstance();
        // Cálculo de las diferencias.
        int years = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int months = fechaActual.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int days = fechaActual.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        // Hay que comprobar si el día de su cumpleaños es posterior
        // a la fecha actual, para restar 1 a la diferencia de años,
        // pues aún no ha sido su cumpleaños.
        if (months < 0 // Aún no es el mes de su cumpleaños
                || (months == 0 && days < 0)) { // o es el mes pero no ha llegado el día.
            years--;
        }
        return years;
    }

    public void save() throws ParseException {

        boolean edit = cliente.getId() != null;
        try {
            if (TipoGlobal.equals("PROVEEDOR")) {
                if (cliente.getIdentificacion().length() < 13) {
                    JsfUtil.addWarningMessage("La identificación", cliente.getIdentificacion() + " esta incompleta. Digite 13 digitos");
                    return;
                } else {
                    cliente.setIdentificacion(cliente.getIdentificacion().substring(0, 10));
                    cliente.setRuc(cliente.getRuc());
                }
            }
        } catch (Exception e) {
        }
        if (cliente.getId() == null) {
            Cliente existeCliente = clienteService.existeCliente(cliente);
            if (cliente.getId() == null && existeCliente != null) {
                JsfUtil.addWarningMessage("Cliente", cliente.getIdentificacion() + " se encuentra registrado/a en el sistema");
                return;
            }
            if (cliente.getId() == null && existeCliente != null) {
                if (!Objects.equals(cliente.getId(), existeCliente.getId())) {
                    JsfUtil.addWarningMessage("Cliente", cliente.getIdentificacion() + " se encuentra registrado/a en el sistema");
                    return;
                }
            }
            if (edad(cliente.getFechaNacimiento()) < 18) {
                JsfUtil.addWarningMessage("Menor de edad", "la persona no puede ser registrada en el sistema");
                return;
            }
            if (!Utils.validarEmailConExpresion(cliente.getEmail())) {
                JsfUtil.addWarningMessage("Correo Incorrecto", "No se puede registrar en el Sistema");
                return;
            }
            cliente.setNombre(cliente.getNombre().toUpperCase());
            cliente.setApellido(cliente.getApellido().toUpperCase());
            cliente.setUsuarioModificacion(userSession.getNameUser());
            cliente.setFechaModificacion(new Date());
            this.cliente.setEstado(Boolean.TRUE);
            clienteService.create(cliente);
        } else {
            cliente.setNombre(cliente.getNombre().toUpperCase());
            cliente.setApellido(cliente.getApellido().toUpperCase());
            clienteService.edit(cliente);
        }
        JsfUtil.addSuccessMessage("Cliente", cliente.getIdentificacion() + (edit ? " editada" : " registrada") + " con éxito.");
        close(cliente);

    }

    public void cambioTipo() {
        String opcion = cliente.getClasificacionProv().getTexto();
        if (opcion.equals("NATURAL")) {
            BoolFecha = Boolean.TRUE;
        } else {
            BoolFecha = Boolean.FALSE;
        }
        catTipo = catalogo.findCatalogotipoLike("tipos_personeria_juridica_natural", opcion);
        cliente.setContribuyenteEspecial(Boolean.FALSE);
        PrimeFaces.current().ajax().update("gridMain");

    }

    public void mostrarCon() {
        if ("JURIDICA-CON_ESP".equals(cliente.getTipoProv().getCodigo())) {
            cliente.setContribuyenteEspecial(Boolean.TRUE);
            PrimeFaces.current().ajax().update("gridMain");
        } else {
            cliente.setContribuyenteEspecial(Boolean.FALSE);
            PrimeFaces.current().ajax().update("gridMain");
        }
    }

    public boolean readOnlyElementForm(boolean bol) {
        if (bol) {
            return true;
        } else {
            return false;
        }
    }

    public void buscarCliente() {
        try {
            if (cliente.getId() == null) {
                if (cliente.getTipoIdentificacion() == null) {
                    JsfUtil.addErrorMessage("Tipo Identificacion", "Debe seleccionar el tipo de identificacion");
                    return;
                }
                if (cliente.getIdentificacion() == null) {
                    JsfUtil.addErrorMessage("Identificacion", "Debe ingresar el número identificacion");
                    return;
                }
                Boolean tipo = false;
                if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
                    tipo = true;
                }
                Cliente c = clienteService.buscarCliente(cliente, tipo);
                if (c.getIdentificacion() != null || c.getId() != null) {
                    cliente = c;
                    if (!tipo) {
                        if (cliente.getCanton() != null) {
                            provinciaSelecionada = c.getCanton().getIdProvincia();
                            actualizarCantones();
                        }
                        if (provinciaSelecionada.getId() == null && c.getDireccion() != null) {
                            String[] split = c.getDireccion().split("/");
                            provinciaSelecionada = proveedorService.findByNamedQuery1("Provincia.findByProvincia", split[0].replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "_"));
                            actualizarCantones();
                        }
                    } else {
                        c.setIdentificacion(c.getIdentificacion().concat(c.getRuc()));
                    }
                    JsfUtil.update("formCliente");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda de cliente", e);
        }
    }

    public void actualizarCantones() {
        this.cantones = provinciaCantonService.getCantones(provinciaSelecionada);
    }

    public void close(Cliente c) {
        PrimeFaces.current().dialog().closeDynamic(c);
    }

    public void closeDis(DistributivoEscala d) {
        PrimeFaces.current().dialog().closeDynamic(d);
    }

    public void closeBan(Banco b) {
        PrimeFaces.current().dialog().closeDynamic(b);
    }

    public LazyModel<Cliente> getClienteServido() {
        return clienteServido;
    }

    public void setClienteServido(LazyModel<Cliente> clienteServido) {
        this.clienteServido = clienteServido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<CatalogoItem> getTiposDeIdentificacion() {
        return tiposDeIdentificacion;
    }

    public void setTiposDeIdentificacion(List<CatalogoItem> tiposDeIdentificacion) {
        this.tiposDeIdentificacion = tiposDeIdentificacion;
    }

    public List<CatalogoItem> getTiposDeGenero() {
        return tiposDeGenero;
    }

    public void setTiposDeGenero(List<CatalogoItem> tiposDeGenero) {
        this.tiposDeGenero = tiposDeGenero;
    }

    public LazyModel<Distributivo> getDistributivoMostrar() {
        return distributivoMostrar;
    }

    public void setDistributivoMostrar(LazyModel<Distributivo> distributivoMostrar) {
        this.distributivoMostrar = distributivoMostrar;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public List<Distributivo> getListDis() {
        return listDis;
    }

    public void setListDis(List<Distributivo> listDis) {
        this.listDis = listDis;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public LazyModel<Banco> getBancoMostrar() {
        return bancoMostrar;
    }

    public void setBancoMostrar(LazyModel<Banco> bancoMostrar) {
        this.bancoMostrar = bancoMostrar;
    }

    public List<CatalogoItem> getCatClasificacion() {
        return catClasificacion;
    }

    public void setCatClasificacion(List<CatalogoItem> catClasificacion) {
        this.catClasificacion = catClasificacion;
    }

    public List<CatalogoItem> getCatTipo() {
        return catTipo;
    }

    public void setCatTipo(List<CatalogoItem> catTipo) {
        this.catTipo = catTipo;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public Boolean getBoolFecha() {
        return BoolFecha;
    }

    public void setBoolFecha(Boolean BoolFecha) {
        this.BoolFecha = BoolFecha;
    }

    public String getTipoGlobal() {
        return TipoGlobal;
    }

    public void setTipoGlobal(String TipoGlobal) {
        this.TipoGlobal = TipoGlobal;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<DistributivoEscala> getDistributivoEscalaList() {
        return distributivoEscalaList;
    }

    public void setDistributivoEscalaList(List<DistributivoEscala> distributivoEscalaList) {
        this.distributivoEscalaList = distributivoEscalaList;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public Provincia getProvinciaSelecionada() {
        return provinciaSelecionada;
    }

    public void setProvinciaSelecionada(Provincia provinciaSelecionada) {
        this.provinciaSelecionada = provinciaSelecionada;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

}
