/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.EntitiesValidacion.*;
import com.gestionTributaria.EntitiesValidacion.HisPersonaReferencias;
import com.gestionTributaria.ServiceValidacionData.ControlValidacionService;
import com.gestionTributaria.EntitiesValidacion.SeguimientoContibuyente;
import com.gestionTributaria.ServiceValidacionData.HisPersonaReferenciasService;
import com.gestionTributaria.ServiceValidacionData.SeguimientoContibuyenteService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CaberasExcelModels;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.PrimeFaces;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class ControlValidacionMb implements Serializable {

    @Inject
    private ManagerService manager;
    @Inject
    private UserSession user;
    @Inject
    private HisPersonaReferenciasService service;
    @Inject
    private ControlValidacionService controlValidacionService;
    @Inject
    private SeguimientoContibuyenteService seguimientoContibuyenteService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    private List<Usuarios> listUser;
    private Map<String, Object> parametros;
    private LazyModel<SeguimientoContibuyente> lazyListaSeguimiento;
    private SeguimientoContibuyente seguimientoContibuyente;
    private ControlValidacion controlValidacion;
    private LazyModel<ControlValidacion> lazyValidadores;
    private Long totalRegistroValidados = 0L;
    private Long totalRegistro = 0L;
    private Date fechaDesde, fechaHasta;
    private BigInteger addRegistro;
    private LazyModel<HisPersonaReferencias> referenciaConsultas;
    private LazyModel<ViewClientesValidados> clienteHisotrialIdHisotorial;

    @PostConstruct
    public void init() {
        parametros = new HashMap<>();
        totalRegistroValidados = service.totalRegistroUser(Boolean.TRUE);
        controlValidacion = new ControlValidacion();
        lazyValidadores = new LazyModel(ControlValidacion.class);
        lazyValidadores.getSorteds().put("fecha", "DESC");
        lazyValidadores.setDistinct(false);
        totalRegistro = service.totalRegistro();
        fechaDesde = new Date();
        fechaHasta = new Date();
        seguimientoContibuyente = new SeguimientoContibuyente();
        lazyListaSeguimiento = new LazyModel(SeguimientoContibuyente.class);
        lazyListaSeguimiento.getSorteds().put("fecha", "DESC");
        lazyListaSeguimiento.setDistinct(false);
        listUser = new ArrayList<>();
        addRegistro = BigInteger.ZERO;

    }

    public void editar(SeguimientoContibuyente s) {
        seguimientoContibuyente = new SeguimientoContibuyente();
        seguimientoContibuyente = s;

    }

    public void actualizHisotrialconsultas() {
        referenciaConsultas = new LazyModel(HisPersonaReferencias.class);
        JsfUtil.update("fmConsultasHisotrial");
        JsfUtil.executeJS("PF('dligHistorialConsultas').show()");
    }

    public void idClienteHisotrial() {
        clienteHisotrialIdHisotorial = new LazyModel(ViewClientesValidados.class);
        JsfUtil.update("fmClienteHistorial");
        JsfUtil.executeJS("PF('clienteHistorico').show()");
    }

    public void armandoExcel() throws FileNotFoundException {
        int conteoColumna = 0;
        Integer totalRealizados = 0;
        List<Date> resultFechas = manager.findAllQuery("SELECT distinct(c.fecha) FROM ViewReporteAdminControl c order by c.fecha asc", null);
        List<ViewReporteAdminControl> resultData = manager.findAllQuery("SELECT c FROM ViewReporteAdminControl c order by c.fecha asc", null);
        List<String> usuariosSinRepetir = manager.getResultUserValidadores();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "HojaExcel");
        HSSFRow headerRow = sheet.createRow(0);

        int conteo = 0;
        HSSFCell cell = headerRow.createCell(conteo++);
        cell.setCellValue("usuario");
        HSSFCell cellUser = headerRow.createCell(conteo++);
        cellUser.setCellValue("Nombres");
        List<CaberasExcelModels> cabeceras = new ArrayList<>();
        int conteoCabcera = 0;
        HSSFCell celdas = null;
        for (int i = 0; i < resultFechas.size(); i++) {

            celdas = headerRow.createCell(conteo++);
            celdas.setCellValue(resultFechas.get(i).toString());
            conteoColumna++;
            cabeceras.add(new CaberasExcelModels(resultFechas.get(i).toString(), conteo));

        }
        HSSFCell celltotalSaginado = headerRow.createCell(conteo++);
        celltotalSaginado.setCellValue("Total Asignados");
        HSSFCell celltotalRealizados = headerRow.createCell(conteo++);
        celltotalRealizados.setCellValue("Total Realizados");
        HSSFCell celltEficiencia = headerRow.createCell(conteo++);
        celltEficiencia.setCellValue("Eficiencia");

        List<ViewReporteAdminControl> filtroList = new ArrayList<>();
        int filas = 1;
        int cont = 0;
        for (int j = 0; j < usuariosSinRepetir.size(); j++) {
            filtroList = new ArrayList<>();
            parametros = new HashMap<>();
            parametros.put("usuario", usuariosSinRepetir.get(j));

            filtroList = manager.findAllQuery("SELECT c FROM ViewReporteAdminControl c where c.usuario=:usuario order by c.registroValidados DESC", parametros);
            HSSFRow dataRow = sheet.createRow(j + 1);
            for (int k = 0; k < filtroList.size(); k++) {
                if (k == 0 || k == 1) {
                    dataRow.createCell(0).setCellValue(filtroList.get(k).getUsuario().toUpperCase());
                    dataRow.createCell(1).setCellValue(filtroList.get(k).getNombreCompleto().toUpperCase());
                }
                cont = j;
                totalRealizados = 0;
                int col = 0;
                for (int x = 0; x < filtroList.size(); x++) {

                    col = col + 1;
                    conteoCabcera = 0;

                    int columa_temp = 0;
                    for (CaberasExcelModels item : cabeceras) {
                        if (item.getCabecera().equals(filtroList.get(x).getFecha().toString())) {
                            columa_temp = item.getColumna();

                        }
                    }

                    dataRow.createCell(columa_temp - 1).setCellValue(filtroList.get(x).getRegistroValidados().toString());
                    totalRealizados += filtroList.get(x).getRegistroValidados().intValue();

                }

                dataRow.createCell(conteoColumna + 2).setCellValue(filtroList.get(k).getRegistrosAsgnados());
                dataRow.createCell(conteoColumna + 3).setCellValue(totalRealizados);

                if (totalRealizados == 0 || filtroList.get(k).getRegistrosAsgnados() == 0) {
                    dataRow.createCell(conteoColumna + 4).setCellValue(0);
                } else {

                    Double result = (totalRealizados.doubleValue() / Double.valueOf(filtroList.get(k).getRegistrosAsgnados().toString())) * 100;
                    dataRow.createCell(conteoColumna + 4).setCellValue(result);
                }
                k = filtroList.size();
            }
        }

//        HSSFRow dataRow = sheet.createRow(1 + DATA.size());
//        HSSFCell total = dataRow.createCell(1);
//        total.setCellType(CellType.FORMULA);
//        total.setCellStyle(style);
//        total.setCellFormula(String.format("SUM(B2:B%d)", 1 + DATA.size()));
        Calendar fecha = Calendar.getInstance();
        Integer anio = fecha.get(Calendar.YEAR);
        Integer mes = fecha.get(Calendar.MONTH) + 1;
        Integer dia = fecha.get(Calendar.DAY_OF_MONTH);
        Integer hora = fecha.get(Calendar.HOUR_OF_DAY);
        Integer milisegundos = fecha.get(Calendar.MILLISECOND);

        String fecharuta = anio.toString() + mes.toString() + dia.toString() + hora.toString() + milisegundos.toString();

        String ruta = SisVars.RUTA_REPORTE_PRACTICAS_PRODUCCION + "reporte_pasante_" + fecharuta + ".xls";
         //SisVars.RUTA_REPORTE_PRACTICAS_PRUEBAS + "reporte_pasante_" + fecharuta + ".xls";
        //SisVars.RUTA_REPORTE_PRACTICAS_PRODUCCION + "reporte_pasante_" + fecharuta + ".xls";
        try {
            FileOutputStream file = new FileOutputStream(ruta);

            workbook.write(file);
            file.close();

            File fichero = new File(ruta);

            ss.addParametro("download", true);
            ss.setNombreDocumento(ruta);
            ss.setContentType("xls");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
            Thread data = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ValidAdminMb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (fichero.delete()) {
                        System.out.println("El fichero ha sido borrado satisfactoriamente");
                    } else {
                        System.out.println("El fichero no puede ser borrado");
                    }
                }
            };
            data.start();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void calculateRegister() {
        if (seguimientoContibuyente.getIdDesde() != null && addRegistro != null) {
            seguimientoContibuyente.setIdHasta(seguimientoContibuyente.getIdDesde().add(addRegistro));
        }
    }

    public void saveSeguimiento() {
        try {
            if (seguimientoContibuyente.getIdHasta().compareTo(seguimientoContibuyente.getIdDesde()) == -1) {
                JsfUtil.addWarningMessage("Aviso", "El id desde es mayor al id hasta");
                return;
            }

            if (seguimientoContibuyente.getId() == null) {
                seguimientoContibuyente.setUsuarioCreacion(user.getNameUser());
                seguimientoContibuyente.setUsuarioModificacion(user.getNameUser());
                seguimientoContibuyente.setFechaCreacion(new Date());
                seguimientoContibuyente.setFechaModificacion(new Date());
                seguimientoContibuyenteService.create(seguimientoContibuyente);
            } else {
                seguimientoContibuyente.setUsuarioModificacion(user.getNameUser());
                seguimientoContibuyente.setFechaModificacion(new Date());
                seguimientoContibuyenteService.edit(seguimientoContibuyente);
            }
            int a = clienteService.registroAisgnados(seguimientoContibuyente.getUsuario(), seguimientoContibuyente.getIdDesde(), seguimientoContibuyente.getIdHasta());
            System.out.println("editados o asigandos" + a);

            seguimientoContibuyente = new SeguimientoContibuyente();
            lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
            lazyListaSeguimiento.getSorteds().put("id", "DESC");
            JsfUtil.addInformationMessage("Info", "Su registro/edito se realizo con exito");
            JsfUtil.update("frmRegisterHistorial");
        } catch (Exception e) {
            JsfUtil.addWarningMessage("Aviso", "No se pudo Registrar");
        }
    }

    public void openDlogoHistory() {
        listUser = new ArrayList<>();
        listUser = clienteService.listUserValidador("VALI");
        seguimientoContibuyente = new SeguimientoContibuyente();
        lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
        lazyListaSeguimiento.getSorteds().put("id", "DESC");
        JsfUtil.update("frmRegisterHistorial");
        JsfUtil.executeJS("PF('dlogoHistorial').show()");
    }

    public ControlValidacion getControlValidacion() {
        return controlValidacion;
    }

    public void setControlValidacion(ControlValidacion controlValidacion) {
        this.controlValidacion = controlValidacion;
    }

    public Long getTotalRegistroValidados() {
        return totalRegistroValidados;
    }

    public void setTotalRegistroValidados(Long totalRegistroValidados) {
        this.totalRegistroValidados = totalRegistroValidados;
    }

    public LazyModel<ControlValidacion> getLazyValidadores() {
        return lazyValidadores;
    }

    public void setLazyValidadores(LazyModel<ControlValidacion> lazyValidadores) {
        this.lazyValidadores = lazyValidadores;
    }

    public Long getTotalRegistro() {
        return totalRegistro;
    }

    public void setTotalRegistro(Long totalRegistro) {
        this.totalRegistro = totalRegistro;
    }

    public LazyModel<SeguimientoContibuyente> getLazyListaSeguimiento() {
        return lazyListaSeguimiento;
    }

    public void setLazyListaSeguimiento(LazyModel<SeguimientoContibuyente> lazyListaSeguimiento) {
        this.lazyListaSeguimiento = lazyListaSeguimiento;
    }

    public SeguimientoContibuyente getSeguimientoContibuyente() {
        return seguimientoContibuyente;
    }

    public void setSeguimientoContibuyente(SeguimientoContibuyente seguimientoContibuyente) {
        this.seguimientoContibuyente = seguimientoContibuyente;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<Usuarios> getListUser() {
        return listUser;
    }

    public void setListUser(List<Usuarios> listUser) {
        this.listUser = listUser;
    }

    public BigInteger getAddRegistro() {
        return addRegistro;
    }

    public void setAddRegistro(BigInteger addRegistro) {
        this.addRegistro = addRegistro;
    }

    public LazyModel<HisPersonaReferencias> getReferenciaConsultas() {
        return referenciaConsultas;
    }

    public void setReferenciaConsultas(LazyModel<HisPersonaReferencias> referenciaConsultas) {
        this.referenciaConsultas = referenciaConsultas;
    }

    public LazyModel<ViewClientesValidados> getClienteHisotrialIdHisotorial() {
        return clienteHisotrialIdHisotorial;
    }

    public void setClienteHisotrialIdHisotorial(LazyModel<ViewClientesValidados> clienteHisotrialIdHisotorial) {
        this.clienteHisotrialIdHisotorial = clienteHisotrialIdHisotorial;
    }

}
