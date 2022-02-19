package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThServidorService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
@Named(value = "thServidorView")
@ViewScoped
public class ThServidorController implements Serializable {

    @Inject
    private ThServidorService thServidorService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private detalleBancoServices detalleBancoService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private CantonService cantonService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ThInterfaces thInterfaces;

    private LazyModel<Servidor> thservidorLazy;
    private LazyModel<Banco> bancoLazy;

    private Servidor thServidor;
    private Cliente thServidorCliente;
    private Provincia provincia;
    private OpcionBusqueda opcionBusqueda;

    private List<CatalogoItem> generoList;
    private List<CatalogoItem> tipoSangresList;
    private List<CatalogoItem> etniaList;
    private List<CatalogoItem> estadoCivilList;
    private List<CatalogoItem> tipoCuentaBancoList;
    private List<DetalleBanco> bancoServidorList;
    private List<Provincia> provinciaLis;
    private List<Canton> cantonList;
    private Boolean editView, collapsed, tipo;

    @PostConstruct
    public void init() {
        //thServidorLazy
        this.thservidorLazy = new LazyModel<>(Servidor.class);
        this.thservidorLazy.getSorteds().put("persona.apellido", "ASC");
        this.thservidorLazy.getFilterss().put("estado", true);
        this.thservidorLazy.getFilterss().put("jubilado", false);
        this.thservidorLazy.setDistinct(false);
        //bancoLazy
        this.bancoLazy = new LazyModel<>(Banco.class);
        this.bancoLazy.getSorteds().put("nombreBanco", "ASC");
        this.bancoLazy.getFilterss().put("estado", true);
        //
        generoList = catalogoItemService.findByCatalogo("tipo_genero");
        tipoSangresList = catalogoItemService.findByCatalogo("tipo_sangre");
        etniaList = catalogoItemService.findByCatalogo("etnia");
        estadoCivilList = catalogoItemService.findByCatalogo("estado_civil");
        tipoCuentaBancoList = catalogoItemService.findByCatalogo("tipo_cuenta_bancaria");
        provinciaLis = provinciaService.getProvincias();
        cleanForm(false);
    }

    public void cleanForm(Boolean accion) {
        editView = false;
        collapsed = true;
        thServidor = new Servidor();
        provincia = new Provincia();
        thServidorCliente = new Cliente();
        bancoServidorList = new ArrayList<>();
        cantonList = new ArrayList<>();
        if (accion) {
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void form(Servidor thServidor, Boolean view) {
        cleanForm(false);
        editView = view;
        collapsed = false;
        if (thServidor != null) {
            this.thServidor = thServidor;
            this.thServidorCliente = thServidor.getPersona();
            if (this.thServidorCliente.getCanton() != null) {
                provincia = this.thServidorCliente.getCanton().getIdProvincia();
                actualizarCanton();
            }
            List<DetalleBanco> detalleAux = detalleBancoService.findListBancoByServidor(thServidor);
            for (DetalleBanco db : detalleAux) {
                bancoServidorList.add(db);
            }
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    public void save() {
        boolean edit = thServidor.getId() != null;
        if (thServidorCliente.getIdentificacion() == null || thServidorCliente.getIdentificacion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el numero de CI ");
            return;
        }
        if (thServidorCliente.getNombre() == null || thServidorCliente.getNombre().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar los nombres");
            return;
        }
        if (thServidorCliente.getApellido() == null || thServidorCliente.getApellido().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar los apellidos");
            return;
        }
        if (thServidorCliente.getFechaNacimiento() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la fecha de nacimiento");
            return;
        }
        if (thServidorCliente.getEstadoCivil() == null || thServidorCliente.getEstadoCivil().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un estado civil");
            return;
        }
        if (thServidorCliente.getGenero() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un género");
            return;
        }
        if (thServidor.getTipoSangre() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar el tipo de sangre");
            return;
        }
        if (thServidorCliente.getCanton() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un cantón");
            return;
        }
        if (thServidorCliente.getEmail() == null || thServidorCliente.getEmail().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el correo");
            return;
        }
        if (!Utils.validarEmailConExpresion(thServidorCliente.getEmail())) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el correo con el formato correcto");
            return;
        }
        if (thServidor.getEmailInstitucion() == null || thServidor.getEmailInstitucion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el correo");
            return;
        }
        if (!Utils.validarEmailConExpresion(thServidor.getEmailInstitucion())) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el correo con el formato correcto");
            return;
        }
        if (thServidorCliente.getTelefono() == null && thServidorCliente.getTelefono().equals("")
                && thServidorCliente.getCelular() == null && thServidorCliente.getCelular().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un numero de telefono o un celular");
            return;
        }
        if (thServidorCliente.getDiscapacidad()) {
            if (thServidorCliente.getNumConadis() == null || thServidorCliente.getNumConadis().equals("")) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el numero del conadis");
                return;
            }
        }
        if (thServidor.getEtnia() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una etnia");
            return;
        }
        if (thServidor.getFechaIngreso() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la fecha de ingreso dels servidor");
            return;
        }
        if (thServidor.getNombresEmergencia() == null || thServidor.getNombresEmergencia().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar los nombres del contacto de emergencia");
            return;
        }
        if (thServidor.getApellidoEmergencia() == null || thServidor.getApellidoEmergencia().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar los apellido del contacto de emergencia");
            return;
        }
        if (thServidor.getTelefonoEmergencia() == null || thServidor.getTelefonoEmergencia().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el numero de telefono del contacto de emergencia");
            return;
        }
        if (bancoServidorList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la cuenta bancaria del servidor");
            return;
        }
        int cont = 0;
        for (DetalleBanco db : bancoServidorList) {
            if (!db.getEstadoCuenta()) {
                cont += 1;
            }
        }
        if (cont > 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Solo debe estar una cuenta de banco activa");
            return;
        }
        if (edit) {
            //modifica el cliente
            thServidorCliente.setUsuarioModificacion(userSession.getNameUser());
            thServidorCliente.setFechaModificacion(new Date());
            clienteService.edit(thServidorCliente);
            //modifica el servidor
            thServidor.setUsuarioModifica(userSession.getNameUser());
            thServidor.setFechaModifica(new Date());
            thServidorService.edit(thServidor);
            actualizarServidorBanco();
        } else {
            //crear el cliente
            if (thServidorCliente.getId() != null) {
                thServidorCliente.setUsuarioModificacion(userSession.getNameUser());
                thServidorCliente.setFechaModificacion(new Date());
                clienteService.edit(thServidorCliente);
            } else {
                thServidorCliente.setUsuarioCreacion(userSession.getNameUser());
                thServidorCliente.setFechaCreacion(new Date());
                thServidorCliente = clienteService.create(thServidorCliente);
            }
            //crear el servidor
            thServidor.setJubilado(false);
            thServidor.setPersona(thServidorCliente);
            thServidor.setUsuarioCreacion(userSession.getNameUser());
            thServidor.setFechaCreacion(new Date());
            thServidor = thServidorService.create(thServidor);
            actualizarServidorBanco();
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        PrimeFaces.current().ajax().update("thServidorTable");
        cleanForm(true);
    }

    public void actualizarServidorBanco() {
        for (DetalleBanco db : bancoServidorList) {
            if (db.getId() != null) {
                detalleBancoService.edit(db);
            } else {
                db.setServidorPublico(thServidor);
                detalleBancoService.create(db);
            }
        }
    }

    public void actualizarCanton() {
        cantonList = new ArrayList<>();
        if (provincia != null) {
            if (provincia.getId() != null) {
                cantonList = cantonService.getCantones(provincia);
            }
        }
    }

    public void openDlgBancoServidor() {
        JsfUtil.executeJS("PF('bancoDlg').show()");
        PrimeFaces.current().ajax().update("bancoForm");
        PrimeFaces.current().ajax().update("bancoTable");

    }

    public void closeDlgBancoServidor(Banco banco) {
        DetalleBanco db = new DetalleBanco();
        db.setBanco(banco);
        bancoServidorList.add(db);
        JsfUtil.executeJS("PF('bancoDlg').hide()");
        PrimeFaces.current().ajax().update("tablaBanco");
    }

    public void deleteBancoServidor(DetalleBanco db, int index) {
        if (db.getId() != null) {
            detalleBancoService.remove(db);
            bancoServidorList.remove(db);
        } else {
            bancoServidorList.remove(index);
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se elimino correctamente la cuenta bancaria");
        PrimeFaces.current().ajax().update("tablaBanco");
    }

    public void searchIdentificacion() {
        if (thServidorCliente.getIdentificacion() == null || thServidorCliente.getIdentificacion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el numero de identificación");
            return;
        }
        if (!Utils.validateCCRuc(thServidorCliente.getIdentificacion())) {
            JsfUtil.addWarningMessage("AVISO!!!", "El numero de identificación es invalido");
            return;
        }
        if (thServidorCliente.getIdentificacion().length() < 10) {
            JsfUtil.addWarningMessage("AVISO!!!", "La cantidad de digitos de la identificacion es incorrecta");
            return;
        }
        //validar que ya este registrado el servidor
        if (thServidorService.findServidor(thServidorCliente.getIdentificacion())) {
            JsfUtil.addWarningMessage("AVISO!!!", "El servidor ya esta registrado");
            return;
        }
        thServidorCliente = clienteService.buscarCliente(thServidorCliente.getIdentificacion());
        PrimeFaces.current().ajax().update("fielsetIngresoForm");
    }

    public void imprimirReporte(String tipoDocumento) {
        servletSession.addParametro("parametro", false);
        servletSession.setNombreReporte("servidor_publico");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirNomina(String tipoDocumento) {
        servletSession.addParametro("parametro", false);
        opcionBusqueda = new OpcionBusqueda();
        Calendar calendar = Calendar.getInstance();
        int mes = Utils.getMes(new Date()) - 1;
        calendar.set(opcionBusqueda.getAnio(), mes, 1);
        servletSession.addParametro("fecha_inicio", calendar.getTime());
        servletSession.addParametro("fecha_salida", new Date());
        servletSession.setNombreReporte("nomina_servidores");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void openDlgDeleteJubilado(Servidor servidor, Boolean accion) {
        this.thServidor = servidor;
        this.thServidor.setFechaSalida(new Date());
        tipo = accion;
        JsfUtil.executeJS("PF('eliminarDlg').show()");
        PrimeFaces.current().ajax().update("eliminarDlgForm");
    }

    public void delete() {
        if (thServidor.getFechaSalida() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar la fecha de salida o de jubilación");
            return;
        }
        if (thServidor.getDescripcionSalida() == null || thServidor.getDescripcionSalida().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el motivo de salida o  motivo de jubilación");
            return;
        }
        if (tipo) {
            thServidor.setEstado(false);
        } else {
            thServidor.setJubilado(true);
        }
        thServidorService.edit(thServidor);
        JsfUtil.executeJS("PF('eliminarDlg').hide()");
        PrimeFaces.current().ajax().update("eliminarDlgForm");
        if (tipo) {
            JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
        } else {
            JsfUtil.addSuccessMessage("INFO!!!", "Se traspaso correctamente a jubilado correctamente");
        }
    }

    public LazyModel<Servidor> getThservidorLazy() {
        return thservidorLazy;
    }

    public void setThservidorLazy(LazyModel<Servidor> thservidorLazy) {
        this.thservidorLazy = thservidorLazy;
    }

    public Servidor getThServidor() {
        return thServidor;
    }

    public void setThServidor(Servidor thServidor) {
        this.thServidor = thServidor;
    }

    public Cliente getThServidorCliente() {
        return thServidorCliente;
    }

    public void setThServidorCliente(Cliente thServidorCliente) {
        this.thServidorCliente = thServidorCliente;
    }

    public List<CatalogoItem> getGeneroList() {
        return generoList;
    }

    public void setGeneroList(List<CatalogoItem> generoList) {
        this.generoList = generoList;
    }

    public List<CatalogoItem> getTipoSangresList() {
        return tipoSangresList;
    }

    public void setTipoSangresList(List<CatalogoItem> tipoSangresList) {
        this.tipoSangresList = tipoSangresList;
    }

    public List<CatalogoItem> getEtniaList() {
        return etniaList;
    }

    public void setEtniaList(List<CatalogoItem> etniaList) {
        this.etniaList = etniaList;
    }

    public List<CatalogoItem> getEstadoCivilList() {
        return estadoCivilList;
    }

    public void setEstadoCivilList(List<CatalogoItem> estadoCivilList) {
        this.estadoCivilList = estadoCivilList;
    }

    public List<DetalleBanco> getBancoServidorList() {
        return bancoServidorList;
    }

    public void setBancoServidorList(List<DetalleBanco> bancoServidorList) {
        this.bancoServidorList = bancoServidorList;
    }

    public List<Provincia> getProvinciaLis() {
        return provinciaLis;
    }

    public void setProvinciaLis(List<Provincia> provinciaLis) {
        this.provinciaLis = provinciaLis;
    }

    public List<Canton> getCantonList() {
        return cantonList;
    }

    public void setCantonList(List<Canton> cantonList) {
        this.cantonList = cantonList;
    }

    public Boolean getEditView() {
        return editView;
    }

    public void setEditView(Boolean editView) {
        this.editView = editView;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public LazyModel<Banco> getBancoLazy() {
        return bancoLazy;
    }

    public void setBancoLazy(LazyModel<Banco> bancoLazy) {
        this.bancoLazy = bancoLazy;
    }

    public List<CatalogoItem> getTipoCuentaBancoList() {
        return tipoCuentaBancoList;
    }

    public void setTipoCuentaBancoList(List<CatalogoItem> tipoCuentaBancoList) {
        this.tipoCuentaBancoList = tipoCuentaBancoList;
    }

    public Boolean getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

}
