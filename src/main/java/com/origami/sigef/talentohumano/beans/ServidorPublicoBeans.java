package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.RegimenLaboralService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import com.origami.sigef.talentohumano.services.detalleRegistroService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "servidorPublicoView")
@ViewScoped
public class ServidorPublicoBeans implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Servidor servidor;
    private Boolean nuevodig = false;
    private Boolean variable = true;
    private Boolean accionPersonal = true;
    private Integer pgcolumn = 2;
    private Integer heightDlg = 290;
    private Integer weightDig = 675;
    private String btnNew;
    private Boolean desabilitar = false;
    private Boolean desabilitarEditar = false;
//    private Boolean view = false;
    private DetalleBanco detalleBanco;
    private ArrayList<DetalleBanco> bancoList;
    private List<DetalleBanco> listaB;
    private List<DetalleBanco> bancoSeleccionado;
    private LazyModel<Servidor> servidorLazy;
    private DetalleRegistro detalle_registro;
    private LazyModel<DetalleRegistro> registroLazy;

    private Cliente cliente;
    private Distributivo distributivo;
    private Banco banco;
    @Inject
    private DistributivoService distributivoservice;
    @Inject
    private detalleRegistroService detalleService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ServletSession ss;
    @Inject
    private RegimenLaboralService regimenService;
    private List<RegimenLaboral> listRegimen;
    private List<RegimenLaboral> regimen;
    @Inject
    private UserSession userSession;
    private List<CatalogoItem> listCatalogo;
    private List<CatalogoItem> listCatalogo1;
    private List<CatalogoItem> listCatalogo2;
    private List<CatalogoItem> listCatalogo3;
    @Inject
    private CatalogoItemService estadoService;
    @Inject
    private BancoService bancoService;
    @Inject
    private detalleBancoServices detalleBancoService;
    @Inject
    private MasterCatalogoService catalogoService;
    private Boolean editAgg;

    //para mostrar datos escala
    @Inject
    private DistributivoEscalaService distributivoEsalaService;
    private OpcionBusqueda busqueda;
    private OpcionBusqueda anio;
    private DistributivoEscala distributivoEscala;
    private Distributivo distributivoEditar;
    private Integer maxHoras;
    private List<MasterCatalogo> periodos;

    @PostConstruct
    public void inicializate() {

        cliente = new Cliente();
        nuevodig = false;
        variable = false;
        accionPersonal = false;
        pgcolumn = 2;
        heightDlg = 290;
        weightDig = 675;
        servidorLazy = new LazyModel<>(Servidor.class);
        servidorLazy.getSorteds().put("estado", "DESC");
        servidorLazy.getSorteds().put("persona.apellido", "ASC");
        registroLazy = new LazyModel<>(DetalleRegistro.class);
        servidorLazy.setDistinct(false);
        registroLazy.setDistinct(false);
        bancoSeleccionado = new ArrayList<>();
        listaB = new ArrayList<>();
        this.bancoList = new ArrayList<>();
        this.servidor = new Servidor();
        this.detalleBanco = new DetalleBanco();
        this.banco = new Banco();
        this.servidor.setPersona(new Cliente());
        this.servidor.setDistributivo(new Distributivo());
        this.servidor.getDistributivo().setUnidadAdministrativa(new UnidadAdministrativa());
        this.servidor.getDistributivo().setCargo(new Cargo());
        this.detalle_registro = new DetalleRegistro();
        listRegimen = regimenService.findByNamedQuery("RegimenLaboral.findByFiltro");
        listCatalogo = estadoService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "estado_civil");
        listCatalogo1 = estadoService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "etnia");
        listCatalogo2 = estadoService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "tipo_cuenta_bancaria");
        listCatalogo3 = estadoService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "tipo_sangre");
        distributivoEscala = new DistributivoEscala();
        maxHoras = TalentoHumano.maximoHorasExtras;
        busqueda = new OpcionBusqueda();
        anio = new OpcionBusqueda();
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
    }

    public void buscarCliente() {
        if (servidor.getPersona().getIdentificacion() != null) {
            Cliente c = servidorService.findByCliente(servidor.getPersona().getIdentificacion());
            if (c != null) {
                if (servidorService.findByServidor(c.getIdentificacion())) {
                    Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
                } else {
                    this.servidor.setPersona(c);
                }
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
        }
    }

    public void openDlgDis() {
        if (servidor.getDistributivo() != null) {
            distributivoEditar = servidor.getDistributivo();
        }
        userSession.setVarTemp(busqueda.getAnio());
        Utils.openDialog("/facelet/talentoHumano/dialogDistributivo", "70%", "400");
    }

    public void openDlgBan() {
        Utils.openDialog("/facelet/talentoHumano/dialogBanco", "550", "400");
    }

    public void openDlgSer() {
        Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Dato Ingresado Correctamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void selectData(SelectEvent evt) {
        Servidor selectCli = new Servidor();
        selectCli.setPersona((Cliente) evt.getObject());
        Servidor existeServidor = servidorService.existeServidor(selectCli);
        if (servidor.getId() == null && existeServidor != null) {
            if (selectCli.getPersona().getIdentificacion().equals(existeServidor.getPersona().getIdentificacion())) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Información", existeServidor.getPersona().getNombreCompleto() + " con Cedula " + existeServidor.getPersona().getIdentificacion() + " se encuentra registrado/a en el sistema");
                return;
            }
        }
        servidor.setPersona(selectCli.getPersona());
        PrimeFaces.current().ajax().update("frmdlg");
        PrimeFaces.current().ajax().update("formServidorPublico");
    }

    public void selectDataDis(SelectEvent evt) {
        distributivoEscala = (DistributivoEscala) evt.getObject();
        servidor.setDistributivo(distributivoEscala.getDistributivo());
        distributivoEscala = distributivoEsalaService.getEscalaDistributivoAnio(distributivoEscala.getDistributivo(), busqueda);
        distributivo = servidor.getDistributivo();
        JsfUtil.addInformationMessage("Información", "Al seleccionar Cargo nuevo, Cargar Rubros en Asignación Patidas - Cuenta Contable");

    }

    public void selectDataBan(SelectEvent evt) {
        banco = (Banco) evt.getObject();
        detalleBanco.setServidorPublico(servidor);
        detalleBanco.setBanco(banco);
        detalleBanco.setEstado(Boolean.TRUE);
        this.bancoList.add(this.detalleBanco);
        detalleBanco = new DetalleBanco();

    }

    public void generarDoc() {
        ss.setNombreReporte("dataServidorList");
        ss.setNombreSubCarpeta("ReporteTTHH");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void selectDataSer(SelectEvent evt) {
        detalle_registro.setSupervisor((Servidor) evt.getObject());
    }

    public void borrarLista(int db) {
        if (bancoList.get(db).getServidorPublico() != null) {
            bancoSeleccionado.add(bancoList.get(db));
        }
        bancoList.remove(db);
    }

    public void btn(String btn) {
        if (btn.equals("btn-servidor")) {
            nuevodig = true;
            variable = false;
            accionPersonal = false;
            pgcolumn = 1;
            heightDlg = 630;
            weightDig = 720;
        }
        if (btn.equals("btn-cargo")) {
            nuevodig = false;
            variable = true;
            accionPersonal = false;
            heightDlg = 630;
            weightDig = 600;
        }
        if (btn.equals("btn-accion")) {
            accionPersonal = true;
            nuevodig = false;
            variable = false;
            weightDig = 600;
            heightDlg = 300;
        }
    }

    public void nuevo(Servidor s, String btn, Boolean aggEdit) {
        this.servidor = new Servidor();
        this.bancoList = new ArrayList<>();
        distributivo = new Distributivo();
        btn(btn);
        setEditAgg(aggEdit);
        if (aggEdit && s != null) {
            this.servidor = s;
            this.servidor.setMaximoHorasExtras(168);
            this.servidor.setRealizaHorasExtras(Boolean.TRUE);
            if (variable) {
                //para editar lo que esta en la tabla detalle registro
                detalle_registro = new DetalleRegistro();
                distributivoEscala = new DistributivoEscala();
            }
        } else if (!aggEdit) {
            this.servidor = s;
            if (variable) {
                //para editar lo que esta en la tabla detalle registro
                detalle_registro = detalleService.getDetalleRegistroByServidor(s);
                if (detalle_registro == null) {
                    detalle_registro = new DetalleRegistro();
                }
                distributivo = distributivoservice.findByDistributivo(s);
                if (distributivoEscala != null) {
                    distributivoEscala = new DistributivoEscala();
                    distributivoEscala = distributivoEsalaService.getEscalaDistributivoAnio(s.getDistributivo(), busqueda);
                }
                if (distributivo != null) {
                    distributivo.setServidorPublico(s);
                } else {
                    distributivo = new Distributivo();
                    distributivo.setServidorPublico(s);
                }
            }
        }
        if (variable) {
            detalle_registro.setFechaModifica(new Date());
            detalle_registro.setUsuarioModifica(userSession.getNameUser());
            detalle_registro.setEstado(true);
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
        PrimeFaces.current().ajax().update("formServidorPublico");
        PrimeFaces.current().ajax().update(":servidorPublicoDialog");
    }

    public Boolean renderedEstadoCivil(String estado) {
        if (estado.equals("CASADO")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void nuevoDatos(Servidor s, String btn) {
        btn(btn);
        if (s != null) {
            setEditAgg(null);
            this.servidor = s;
            if (nuevodig) {
                bancoList = new ArrayList<>();
                listaB = new ArrayList<>();
                listaB = detalleBancoService.findListBancoByServidor(s);
                if (bancoList.isEmpty() && listaB != null) {
                    for (DetalleBanco db : listaB) {
                        bancoList.add(db);
                    }
                }
            }
        } else {
            servidor = new Servidor();
            servidor.setPersona(new Cliente());
            servidor.setFechaIngreso(new Date());
            servidor.setEstado(true);
            if (variable) {
                detalle_registro = new DetalleRegistro();
                distributivoEscala = new DistributivoEscala();
                this.servidor.setDistributivo(new Distributivo());
                this.servidor.getDistributivo().setUnidadAdministrativa(new UnidadAdministrativa());
                this.servidor.getDistributivo().setCargo(new Cargo());
                servidor.setFechaModifica(new Date());
                servidor.setUsuarioModifica(userSession.getNameUser());
                bancoList = new ArrayList<>();
            }
        }

        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
        PrimeFaces.current().ajax().update("formServidorPublico");
        PrimeFaces.current().ajax().update(":servidorPublicoDialog");
    }

    public void guardarServidor() {
        boolean edit = servidor.getId() != null;
        if (servidor.getPersona() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe registrar o seleccionar la información de un cliente");
            return;
        }
        if (!bancoList.isEmpty()) {
            int contador = 0;
            for (DetalleBanco banco : bancoList) {
                if (banco.getEstadoCuenta() == true) {
                    contador = contador + 1;
                }
            }
            if (contador > 1 || contador == 0) {
                JsfUtil.addWarningMessage("CUENTA BANCARIA", "Debe haber una cuenta bancaria activa");
                PrimeFaces.current().ajax().update("entidadBancariaTable");
                return;
            }
        }
        if (variable) {
            if (servidor.getActividades() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Información", "Debe Ingresar actividades");
                return;
            }
            if (servidor.getDistributivo().getCargo().getNombreCargo() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Información", "Debe Ingresar un Cargo");
                return;
            }
            if (!Utils.validarEmailConExpresion(servidor.getEmailInstitucion())) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Información", "Email Incorrecto");
                return;
            }
        }
        if (nuevodig) {
            if (servidor.getCedulaConyugue() != null) {
                if (!Utils.validateCCRuc(servidor.getCedulaConyugue())) {
                    JsfUtil.addWarningMessage("Información", "Cedula incorrecta del conyuge");
                    return;
                }
            }
        }
        if (!edit) {
            servidor.setUsuarioCreacion(userSession.getNameUser());
            servidor.setFechaCreacion(new Date());
            servidor = servidorService.create(servidor);
            if (nuevodig) {
                for (DetalleBanco db : bancoList) {
                    db = detalleBancoService.create(db);
                }
            }
        } else {
            servidor.setUsuarioCreacion(userSession.getNameUser());
            servidor.setFechaCreacion(new Date());
            servidorService.edit(servidor);
            for (int i = 0; i < bancoList.size(); i++) {
                detalleBancoService.edit(bancoList.get(i));
            }
            cambioEstado();
            if (editAgg != null) {
                if (editAgg) {
                    detalle_registro.setServidor(servidor);
                    detalle_registro.setUsuarioCreacion(userSession.getNameUser());
                    detalle_registro.setFechaRegistro(servidor.getFechaIngreso());
                    detalle_registro.setDistributivo(servidor.getDistributivo());
                    detalle_registro = detalleService.create(detalle_registro);
                    JsfUtil.addSuccessMessage("Servidor Público", "Asignacion de Cargo con Exito");
                } else {
                    detalle_registro.setUsuarioCreacion(userSession.getNameUser());
                    detalle_registro.setFechaRegistro(new Date());
                    detalleService.edit(detalle_registro);
                }
            }
            if (distributivoEditar != null) {
                distributivoEditar.setServidorPublico(null);
                distributivoservice.edit(distributivoEditar);
            }
        }
        if (variable) {
            if (servidor.getId() != null) {
                distributivo = servidor.getDistributivo();
                if (distributivo != null) {
                    distributivo.setServidorPublico(servidor);
                    distributivoservice.edit(distributivo);
                    distributivo = new Distributivo();
                }
            }
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        JsfUtil.addSuccessMessage("Servidor Público", (edit ? " Editada" : " Registrada") + " con Exito.");
        servidor = new Servidor();
        bancoList = new ArrayList<>();
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public void eliminarServdor(Servidor s) {
        s.setEstado(Boolean.FALSE);
        List<DetalleBanco> listaB = detalleBancoService.findListBancoByServidor(s);
        if (bancoList != null) {
            for (DetalleBanco db : listaB) {
                db.setEstado(false);
                detalleBancoService.edit(db);
            }
        }
        servidorService.edit(s);
        distributivo = distributivoservice.findByDistributivo(s);
        distributivo.setServidorPublico(null);
        distributivoservice.edit(distributivo);
        detalle_registro = detalleService.getDetalleRegistroByServidor(s);
        detalle_registro.setEstado(Boolean.FALSE);
        detalleService.edit(detalle_registro);
        JsfUtil.addSuccessMessage("Servidor Público", s.getPersona().getNombre() + " " + s.getPersona().getApellido() + " Eliminada con Exito");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public void cambioEstado() {

        if (!bancoSeleccionado.isEmpty()) {
            for (DetalleBanco db : bancoSeleccionado) {
                db.setEstado(Boolean.FALSE);
                detalleBancoService.edit(db);
            }
        }

    }

    public Integer getMaxHoras() {
        return maxHoras;
    }

    public void setMaxHoras(Integer maxHoras) {
        this.maxHoras = maxHoras;
    }

//<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public DetalleBanco getDetalleBanco() {
        return detalleBanco;
    }

    public void setDetalleBanco(DetalleBanco detalleBanco) {
        this.detalleBanco = detalleBanco;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public List<RegimenLaboral> getListRegimen() {
        return listRegimen;
    }

    public void setListRegimen(List<RegimenLaboral> listRegimen) {
        this.listRegimen = listRegimen;
    }

    public List<RegimenLaboral> getRegimen() {
        return regimen;
    }

    public void setRegimen(List<RegimenLaboral> regimen) {
        this.regimen = regimen;
    }

    public DetalleRegistro getDetalle_registro() {
        return detalle_registro;
    }

    public void setDetalle_registro(DetalleRegistro detalle_registro) {
        this.detalle_registro = detalle_registro;
    }

    public LazyModel<DetalleRegistro> getRegistroLazy() {
        return registroLazy;
    }

    public void setRegistroLazy(LazyModel<DetalleRegistro> registroLazy) {
        this.registroLazy = registroLazy;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public List<CatalogoItem> getListCatalogo1() {
        return listCatalogo1;
    }

    public void setListCatalogo1(List<CatalogoItem> listCatalogo1) {
        this.listCatalogo1 = listCatalogo1;
    }

    public List<CatalogoItem> getListCatalogo2() {
        return listCatalogo2;
    }

    public void setListCatalogo2(List<CatalogoItem> listCatalogo2) {
        this.listCatalogo2 = listCatalogo2;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public ArrayList<DetalleBanco> getBancoList() {
        return bancoList;
    }

    public void setBancoList(ArrayList<DetalleBanco> bancoList) {
        this.bancoList = bancoList;
    }

    public List<DetalleBanco> getListaB() {
        return listaB;
    }

    public void setListaB(List<DetalleBanco> listaB) {
        this.listaB = listaB;
    }

    public List<DetalleBanco> getBancoSeleccionado() {
        return bancoSeleccionado;
    }

    public void setBancoSeleccionado(List<DetalleBanco> bancoSeleccionado) {
        this.bancoSeleccionado = bancoSeleccionado;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

//
//    public Boolean getView() {
//        return view;
//    }
//
//    public void setView(Boolean view) {
//        this.view = view;
//    }
    public Boolean getNuevodig() {
        return nuevodig;
    }

    public void setNuevodig(Boolean nuevodig) {
        this.nuevodig = nuevodig;
    }

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    public Integer getPgcolumn() {
        return pgcolumn;
    }

    public void setPgcolumn(Integer pgcolumn) {
        this.pgcolumn = pgcolumn;
    }

    public Integer getHeightDlg() {
        return heightDlg;
    }

    public void setHeightDlg(Integer heightDlg) {
        this.heightDlg = heightDlg;
    }

    public String getBtnNew() {
        return btnNew;
    }

    public void setBtnNew(String btnNew) {
        this.btnNew = btnNew;
    }

    public Boolean getEditAgg() {
        return editAgg;
    }

    public void setEditAgg(Boolean editAgg) {
        this.editAgg = editAgg;
    }

    public Integer getWeightDig() {
        return weightDig;
    }

    public void setWeightDig(Integer weightDig) {
        this.weightDig = weightDig;
    }

    public DistributivoEscala getDistributivoEscala() {
        return distributivoEscala;
    }

    public void setDistributivoEscala(DistributivoEscala distributivoEscala) {
        this.distributivoEscala = distributivoEscala;
    }

    public Boolean getDesabilitar() {
        return desabilitar;
    }

    public void setDesabilitar(Boolean desabilitar) {
        this.desabilitar = desabilitar;
    }

    public List<CatalogoItem> getListCatalogo3() {
        return listCatalogo3;
    }

    public void setListCatalogo3(List<CatalogoItem> listCatalogo3) {
        this.listCatalogo3 = listCatalogo3;
    }

    public Boolean getDesabilitarEditar() {
        return desabilitarEditar;
    }

    public void setDesabilitarEditar(Boolean desabilitarEditar) {
        this.desabilitarEditar = desabilitarEditar;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getAnio() {
        return anio;
    }

    public void setAnio(OpcionBusqueda anio) {
        this.anio = anio;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Boolean getAccionPersonal() {
        return accionPersonal;
    }

    public void setAccionPersonal(Boolean accionPersonal) {
        this.accionPersonal = accionPersonal;
    }

    //</editor-fold>
}
