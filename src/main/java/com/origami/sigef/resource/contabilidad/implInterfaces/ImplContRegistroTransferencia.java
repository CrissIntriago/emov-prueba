/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.implInterfaces;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContTransferencias;
import com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroPago;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroTransferencia;
import com.origami.sigef.resource.contabilidad.services.ContTransferenciasDetalleService;
import com.origami.sigef.resource.contabilidad.services.ContTransferenciasService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Singleton
@ApplicationScoped
public class ImplContRegistroTransferencia implements ContRegistroTransferencia {

    @Inject
    private ContRegistroPago contRegistroPago;
    @Inject
    private ControlCuentaContableService controlCuentaContableService;
    @Inject
    private UserSession userSession;
    @Inject
    private ContTransferenciasService contTransferenciasService;
    @Inject
    private ContTransferenciasDetalleService contTransferenciasDetalleService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ValoresService valoresService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ThServidorCargoService thServidorCargoService;

    @Override
    public ContComprobantePago findNumComprobante(Long idComprobante, Short periodo) {
        return contRegistroPago.findByNumRegistro(idComprobante.intValue(), periodo);
    }

    @Override
    public List<ContBeneficiarioComprobantePago> findByBeneficiario(ContComprobantePago comprobante) {
        return contRegistroPago.findByIdBeneficiaciosPagos(comprobante);
    }

    @Override
    public List<ContTransferenciasDetalle> detalleTransferencia(ContComprobantePago comprobante, List<ContTransferenciasDetalle> contTransferenciasDetalleList) {
        if (contTransferenciasDetalleList.isEmpty()) {
            contTransferenciasDetalleList = new ArrayList<>();
        }
        List<ContBeneficiarioComprobantePago> auxList = findByBeneficiario(comprobante);
        if (auxList != null) {
            if (!auxList.isEmpty()) {
                for (ContBeneficiarioComprobantePago temp : auxList) {
                    ContTransferenciasDetalle detalle = new ContTransferenciasDetalle();
                    if (temp.getTipoBeneficiario()) {
                        detalle.setTipoBeneficiario("2");
                        detalle.setIdentificacion(temp.getIdCliente().getIdentificacionCompleta());
                        detalle.setNombreBeneficiario(temp.getIdCliente().getNombre());
                    } else {
                        detalle.setTipoBeneficiario("1");
                        detalle.setIdentificacion(temp.getIdCliente().getIdentificacion());
                        detalle.setNombreBeneficiario(temp.getIdCliente().getNombreCompleto());
                    }
                    detalle.setReferencia(new BigInteger(temp.getNumRegistro() + ""));
                    if (temp.getIdDetalleBanco() != null) {
                        detalle.setInstitucionFinanciera(temp.getIdDetalleBanco().getBanco());
                        detalle.setCuentaBcoBeneficiario(temp.getIdDetalleBanco().getCuentaBanco());
                        detalle.setTipoCuenta(String.valueOf(temp.getIdDetalleBanco().getTipoCuenta().getOrden()));
                    }
                    detalle.setValor(temp.getMonto());
                    detalle.setDetalle("SPI DEL COMPROBANTE DE PAGO DE NO." + comprobante.getNumRegistro());
                    detalle.setIdContComprobantePago(comprobante);
                    contTransferenciasDetalleList.add(detalle);
                }
            }
        }
        return contTransferenciasDetalleList;
    }

    @Override
    public ContTransferencias initObject(ContComprobantePago comprobante, ContTransferencias contTransferencias) {
        String descripcionComprobantePago = "TRANSFERENCIA DEL(LOS) COMPROBANTE(S) DE PAGO NO." + comprobante.getNumRegistro() + "-" + Utils.getAnio(comprobante.getFechaRegistro()) + ", ";
        if (contTransferencias.getDescripcion() != null) {
            contTransferencias.setDescripcion(contTransferencias.getDescripcion() + "NO." + comprobante.getNumRegistro() + "-" + Utils.getAnio(comprobante.getFechaRegistro()) + ", ");
        } else {
            contTransferencias.setDescripcion(descripcionComprobantePago);
        }
        if (comprobante.getCuentaBancaria() != null) {
            contTransferencias.setCtaCteBceIp(comprobante.getCuentaBancaria().getNumeroCuenta());
            contTransferencias.setNombreInstitucion(comprobante.getCuentaBancaria().getNombre());
        }
        return contTransferencias;
    }

    @Override
    public String validaciones(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList) {
        if (contTransferencias.getPeriodo() == null) {
            return "Debe seleccionar un periodo";
        }
        if (Utils.getAnio(contTransferencias.getFechaAfectacion()) != contTransferencias.getPeriodo().intValue()) {
            return "El periodo seleccionado no es igual al periodo de la fecha del registro";
        }
        if (controlCuentaContableService.validarPeriodo(contTransferencias.getFechaAfectacion(), contTransferencias.getPeriodo())) {
            return "El periodo selecionado se encuentra cerrado";
        }
        if (contTransferencias.getDescripcion() == null || contTransferencias.getDescripcion().equals("")) {
            return "Debe ingresar una descripción";
        }
        if (contTransferencias.getLocalidad() == null || contTransferencias.getLocalidad().equals("")) {
            return "Debe ingresar una localida";
        }
        if (contTransferencias.getMaximaAutoridad() == null) {
            return "Se debe ingresar el responsable de máxima autoridad";
        }
        if (contTransferencias.getResponsableTesoreria() == null) {
            return "Se debe ingresar el responsable de tesoreria";
        }
        if (contTransferencias.getCtaCteBceIp() == null || contTransferencias.getCtaCteBceIp().equals("")) {
            return "No esta cargado la cuenta bancaria del banco";
        }
        if (contTransferencias.getNombreInstitucion() == null || contTransferencias.getNombreInstitucion().equals("")) {
            return "No esta cargado el nombre del banco";
        }
        if (contTransferenciasDetalleList.isEmpty()) {
            return "Se requiere el detalle del registro contable";
        }
        for (ContTransferenciasDetalle detalle : contTransferenciasDetalleList) {
            if (detalle.getConcepto() == null || detalle.getConcepto().equals("")) {
                return "El RUC/CI con No. " + detalle.getIdentificacion() + " no tiene ingresado el concepto";
            }
        }
        if (contTransferencias.getId() != null) {
            edit(contTransferencias, contTransferenciasDetalleList);
        } else {
            create(contTransferencias, contTransferenciasDetalleList);
        }
        return "";
    }

    @Override
    public ContTransferencias edit(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList) {
        contTransferencias.setUsuarioModificacion(userSession.getNameUser());
        contTransferencias.setFechaModificacion(new Date());
        contTransferenciasService.edit(contTransferencias);
        if (contTransferenciasDetalleList != null) {
            for (ContTransferenciasDetalle temp : contTransferenciasDetalleList) {
                contTransferenciasDetalleService.edit(temp);
                contRegistroPago.editTransferencia(temp.getIdContComprobantePago());
            }
        }
        return contTransferencias;
    }

    @Override
    public ContTransferencias create(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList) {
        contTransferencias.setUsuarioCreacion(userSession.getNameUser());
        contTransferencias.setFechaCreacion(new Date());
        contTransferencias.setNumReferencia(contTransferenciasService.nextRegistro(contTransferencias.getPeriodo()));
        contTransferencias = contTransferenciasService.create(contTransferencias);
        if (contTransferenciasDetalleList != null) {
            for (ContTransferenciasDetalle temp : contTransferenciasDetalleList) {
                temp.setDetalle((temp.getDetalle().replace(",", " ").replaceAll("[\r\n]", " ")).replaceAll("\\s{2,}", " ").trim());
                temp.setIdContTransferencia(contTransferencias);
                contTransferenciasDetalleService.create(temp);
                contRegistroPago.editTransferencia(temp.getIdContComprobantePago());
            }
        }
        return contTransferencias;
    }

    @Override
    public void generarArchivosDescargas(int accion, ContTransferencias contTransferencias) throws FileNotFoundException {
        //generar archivos
        StringBuffer data = new StringBuffer();
        documentoZip(data, contTransferencias);
        StringBuilder archivoProveedor = new StringBuilder();
        documentArchivoProveedor(archivoProveedor, contTransferencias);
        String zipSPI = createFileSPI(data);
        File archivoProvee = createTxt(valoresService.findByCodigo("SPI_FILE_APR"), archivoProveedor.toString());
        switch (accion) {
            case 1:
                servletSession.addParametro("download", true);
                servletSession.setNombreDocumento(zipSPI);
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
                break;
            case 2:
                servletSession.addParametro("download", true);
                servletSession.setNombreDocumento(archivoProvee.getAbsolutePath());
                servletSession.setContentType("text/plain");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
                break;
            case 3:
                servletSession.addParametro("id_transferencia", contTransferencias.getId());
                servletSession.addParametro("fecha_reporte", new Date());
                servletSession.addParametro("num_control", formatoNumControl((String) this.userSession.getVarTemp()));// Obtiene el hash del archivo md5
                servletSession.setNombreReporte("transferenciasDetalle");
                servletSession.setNombreSubCarpeta("_tesoreria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                break;
            case 4:
                String detalle = "Por medio de la presente, solicito a usted se sirva al Banco Central del Ecuador el detalle de los pagos contenidos"
                        + " en el medio magnético que adjunto y que deberá ejecutarse el " + Utils.dateFormatPattern("dd/MM/yyyy", contTransferencias.getFechaAfectacion())
                        + " a través del Sistema de Pagos Interbancarios SPI, con los recursos que el/la " + contTransferencias.getNombreInstitucion()
                        + " mantiene en la cuenta corriente No. " + contTransferencias.getCtaCteBceIp() + " en el  Banco Central del Ecuador.";
                String piePagina = "Suscribimos este pedido en calidad de firma(s) autorizada(s) de la cuenta rotativa de pagos"
                        + " No. " + contTransferencias.getCtaCteBceIp() + " que el/la " + contTransferencias.getNombreInstitucion() + " mantiene en el " + contTransferencias.getNombreCorresponsal();
                servletSession.addParametro("id_transferencia", contTransferencias.getId());
                servletSession.addParametro("num_control", formatoNumControl((String) this.userSession.getVarTemp()));// Optiene el hash del archivo md5
                servletSession.addParametro("detalle", detalle);
                servletSession.addParametro("piePagina", piePagina);
                servletSession.setNombreReporte("transferenciasVentanilla");
                servletSession.setNombreSubCarpeta("_tesoreria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                break;
            default:
                JsfUtil.redirect(CONFIG.URL_APP + "tesoreria/registros/transferencias");
                break;
        }
    }

    private String formatoNumControl(String cadena) {
        int contador = 0;
        String codigo = "";
        while (contador < cadena.length()) {
            codigo = codigo.concat(cadena.substring(contador, contador + 4));
            contador += 4;
            if (contador != cadena.length()) {
                codigo = codigo.concat(" - ");
            }
        }
        return codigo;
    }

    private String createFileSPI(StringBuffer data) {
        try {
            String ruta = "/SPI-2005/";
            String txt = valoresService.findByCodigo("SPI_FILE_TXT");
            String md5 = valoresService.findByCodigo("SPI_FILE_MD5");
            File fzip = new File(valoresService.findByCodigo("SPI_FILE_ZIP"));
            Utils.createDirectoryIfNotExist(ruta);
            File ftxt = createTxt(txt, data.toString());
            userSession.setActKey(ftxt.getAbsolutePath());
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (Utils.getOperatingSystemType().equals(Utils.OSType.Linux)) {
                processBuilder.command("bash", "-c", String.format("openssl MD5 %s", ftxt.getAbsoluteFile()));
            } else if (Utils.getOperatingSystemType().equals(Utils.OSType.Windows)) {
                processBuilder.command("cmd.exe", "/c", String.format("openssl.exe MD5 %s", ftxt.getAbsoluteFile()));
            }
            try {
                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                Boolean exitVal = process.waitFor(2000l, TimeUnit.MILLISECONDS);
                if (exitVal) {
                    String name = output.toString().split("=")[1];
                    userSession.setVarTemp(name.trim());
                    File fmd5 = createTxt(md5, name.trim().concat("  ".concat(ftxt.getAbsolutePath())));
                    try ( // Crear archivo zip
                            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fzip))) {
                        out.putNextEntry(new ZipEntry(ftxt.getName()));
                        out.write(IOUtils.toByteArray(ftxt.toURI()));
                        out.putNextEntry(new ZipEntry(fmd5.getName()));
                        out.write(IOUtils.toByteArray(fmd5.toURI()));
                        out.closeEntry();
                    }
                } else {
                    System.out.println("Algo paso");
                }
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "createFileSPI pago", e);
            }
            return fzip.getAbsolutePath();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "createFileSPI pago", e);
        }
        return null;
    }

    public File createTxt(String file, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
            bw.write(data);
            bw.flush();
            bw.close();
            return new File(file);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "createTxt createFileSPI pago", e);
        }
        return null;
    }

    private void documentArchivoProveedor(StringBuilder archivoProveedor, ContTransferencias contTransferencias) {
        //detalle
        List<String> rowsList = contTransferenciasDetalleService.getRowTransferenciaAP(contTransferencias);
        for (String temp : rowsList) {
            archivoProveedor.append(temp).append("\n");
        }
        //cabecera
        archivoProveedor.insert(0, contTransferenciasService.getHeaderTransfereciaAP(contTransferencias) + "\n");
    }

    private void documentoZip(StringBuffer data, ContTransferencias contTransferencias) {
        //detalle
        List<String> rowsList = contTransferenciasDetalleService.getRowTransferenciaZip(contTransferencias);
        for (String temp : rowsList) {
            data.append(temp).append("\n");
        }
        //cabecera
        data.insert(0, contTransferenciasService.getHeaderTransfereciaZip(contTransferencias) + "\n");
    }

    @Override
    public ContTransferencias findById(Long idTransferencia) {
        return contTransferenciasService.find(idTransferencia);
    }

    @Override
    public List<ContTransferenciasDetalle> findByIdTransferencia(ContTransferencias contTransferencias) {
        return contTransferenciasDetalleService.findByNamedQuery("ContTransferenciasDetalle.findByIdTransferencia", contTransferencias);
    }

    @Override
    public void generarReporte(ContTransferencias contTransferencias, String tipoArchivo) {
        servletSession.addParametro("id_transferencia", contTransferencias.getId());
        servletSession.setNombreReporte("transferencia");
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_tesoreria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    @Override
    public void anularGeneral(ContTransferencias contTransferencias) {
        contTransferencias.setEstadoTransferencia("ANULADO");
        contTransferencias.setFechaAnulacion(contTransferencias.getFechaAfectacion());
        contTransferenciasService.edit(contTransferencias);
        List<ContComprobantePago> contComprobantePagos = contTransferenciasDetalleService.findTransferenciaComprobante(contTransferencias);
        if (contComprobantePagos != null) {
            if (!contComprobantePagos.isEmpty()) {
                for (ContComprobantePago temp : contComprobantePagos) {
                    contRegistroPago.anular(temp);
                }
            }
        }

    }

    @Override
    public void upload(ContTransferencias contTransferencias, List<UploadedFile> files, Date fechaAcreditacion) {
        contTransferencias.setFechaAcreditacion(fechaAcreditacion);
        contTransferenciasService.edit(contTransferencias);
        if (!files.isEmpty()) {
            uploadDoc.upload(contTransferencias, files);
            contTransferenciasService.updateEstado(contTransferencias, fechaAcreditacion);
        }
    }

    @Override
    public ThServidorCargo getCargoServidor(Servidor servidor) {
        return thServidorCargoService.findByNamedQuery1("ThServidorCargo.findByIdServidor", servidor);
    }
}
