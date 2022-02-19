/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.Utils.OSType;
import com.origami.sigef.contabilidad.model.BeneficiarioTransferenciasModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleTransferenciasService extends AbstractService<DetalleTransferencias> {

    private static final Logger LOG = Logger.getLogger(DetalleTransferenciasService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;
    @Inject
    private ValoresService valoresService;

    public DetalleTransferenciasService() {
        super(DetalleTransferencias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleTransferencias> getDetallesTransferencia(Transferencias transferencia) {
        List<DetalleTransferencias> resultado = (List<DetalleTransferencias>) em.createQuery("SELECT d FROM DetalleTransferencias d "
                + "WHERE d.transferencia=:transferencia ORDER BY d.referencia ASC")
                .setParameter("transferencia", transferencia)
                .getResultList();
        return resultado;
    }

    public List<ComprobantePago> getComprobanteDePagos(Transferencias transferencia) {
        List<ComprobantePago> resultado = (List<ComprobantePago>) em.createQuery("SELECT DISTINCT(dt.comprobantePago) FROM DetalleTransferencias dt "
                + "WHERE dt.transferencia=:transferencia ORDER BY dt.comprobantePago ASC")
                .setParameter("transferencia", transferencia)
                .getResultList();
        return resultado;
    }

    public DetalleTransferencias getUltimaTransferencia(Short periodo) {
        try {
            DetalleTransferencias resultado = (DetalleTransferencias) em.createQuery("SELECT dt FROM DetalleTransferencias dt INNER JOIN dt.transferencia t WHERE t.periodo=:periodo ORDER BY dt.referencia DESC")
                    .setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<BeneficiarioTransferenciasModel> getBeneficiarioTransferencias(Short periodo) {
        String sql = "SELECT \n"
                + "dt.identificacion As identificacion, \n"
                + "dt.nombre_beneficiario As nombre\n"
                + "FROM contabilidad.detalle_transferencias dt\n"
                + "INNER JOIN contabilidad.transferencias t\n"
                + "ON dt.transferencia = t.id\n"
                + "WHERE t.periodo=?1\n"
                + "GROUP BY 1,2\n"
                + "ORDER BY dt.nombre_beneficiario ASC";
        Query query = em.createNativeQuery(sql).setParameter(1, periodo);
        List<Object[]> result = query.getResultList();
        if (result != null) {
            List<BeneficiarioTransferenciasModel> list = new ArrayList<>();
            for (Object[] objecto : result) {
                BeneficiarioTransferenciasModel data = new BeneficiarioTransferenciasModel();
                data.setIdentificacion((String) objecto[0]);
                data.setNombre((String) objecto[1]);
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }

    public List<String> getEstadosTransferencias(Short periodo) {
        List<String> resultado = (List<String>) em.createQuery("SELECT dt.estado FROM DetalleTransferencias dt INNER JOIN dt.transferencia t WHERE t.periodo=:periodo GROUP BY dt.estado ORDER BY dt.estado ASC")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;

    }

    private String fragmentacionNombre(String nombreBeneficiario) {
        String[] splitStr = nombreBeneficiario.split("\\s+");
        String resultado_1 = splitStr[0] + " " + splitStr[1] + " " + splitStr[3];
        if (resultado_1.length() > 30) {
            return resultado_1.substring(0, 30);
        } else {
            return resultado_1;
        }
    }

    public Boolean getCompletarTarea(Transferencias transferencia) {
        List<DetalleTransferencias> resultado = (List<DetalleTransferencias>) em.createQuery("SELECT d FROM DetalleTransferencias d "
                + "WHERE d.transferencia=:transferencia AND d.estado <> 'EMITIDO' ORDER BY d.id ASC")
                .setParameter("transferencia", transferencia)
                .getResultList();
        List<DetalleTransferencias> resultado_1 = getDetallesTransferencia(transferencia);
        if (resultado.size() == resultado_1.size()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public String getHeaderTransferecia(Transferencias transferencias, int numTransferencias, Integer sumCuenta, Integer sumaNControl, Integer sumConcepto) {
        StringBuilder aux = new StringBuilder("");
        try {
            aux.append(Utils.dateFormatPattern("dd/MM/yyyy HH:mm:ss", transferencias.getFechaAfectacion())).append(",");
            aux.append(transferencias.getNumReferencia()).append(",");
            aux.append(numTransferencias).append(",");
            aux.append(1).append(",");
            String valor = transferencias.getValor().setScale(2, RoundingMode.HALF_DOWN).toString();
            aux.append(valor).append(",");
            sumaNControl = sumaNControl + (transferencias.getValor().intValue() * 100)
                    + (Integer.valueOf(transferencias.getCtaCteBceIp()) * 2) + sumCuenta + (sumConcepto * 5) + 107;
            aux.append(sumaNControl).append(",");
            aux.append(transferencias.getCtaCteBceIp()).append(",");
            aux.append(transferencias.getCtaCteBceIp()).append(",");
            aux.append(transferencias.getNombreInstitucion().trim()).append(",");
            aux.append(transferencias.getLocalidad()).append(",");
            aux.append(Utils.dateFormatPattern("MM/yyyy", transferencias.getFechaAfectacion()));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cabecera pago", e);
        }
        return aux.toString();
    }

    /**
     * Genera la cabezera del archivo de proveedores
     *
     * @param transferencias
     * @return Cabezera de la transferencia separado por tab
     */
    public String getHeaderTransfereciaAP(Transferencias transferencias) {
        StringBuilder aux = new StringBuilder("");
        try {
            aux.append(Utils.dateFormatPattern("dd/MM/yyyy", transferencias.getFechaAfectacion())).append("\t");
            aux.append(transferencias.getCtaCteBceIp()).append("\t");
            aux.append(transferencias.getNombreInstitucion().trim()).append("\t");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cabecera pago", e);
        }
        return aux.toString();
    }

    public StringBuffer getRowTransferencia(DetalleTransferencias detalle) {
        StringBuffer aux = new StringBuffer("");
        try {
            aux.append(detalle.getReferencia()).append(",");
            String valor = detalle.getValor().setScale(2, RoundingMode.HALF_DOWN).toString();
            aux.append(valor).append(",");
            aux.append(detalle.getConcepto().trim()).append(",");
            aux.append(detalle.getInstitucionFinanciera().getCuentaCorriente()).append(",");
            aux.append(detalle.getCuentaBcoBeneficiario()).append(",");
            aux.append(detalle.getTipoCuenta()).append(",");
            if (detalle.getNombreBeneficiario().length() > 30) {
                aux.append(detalle.getNombreBeneficiario().substring(0, 30)).append(",");
            } else {
                aux.append(detalle.getNombreBeneficiario()).append(",");
            }
            aux.append((detalle.getDetalle().replace(",", " ").replaceAll("[\r\n]", " ")).replaceAll("\\s{2,}", " ").trim()).append(",");
            aux.append(detalle.getIdentificacion());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "detalle pago", e);
        }
        return aux;
    }

    /**
     * Metodo para generar la fila del archivo de proveedor.
     *
     * @param detalle Detalle de la transferencia
     * @return Fila generada separada por tab
     */
    public StringBuffer getRowTransferenciaAP(DetalleTransferencias detalle) {
        StringBuffer aux = new StringBuffer("");
        try {
            aux.append(detalle.getIdentificacion()).append("\t");
            if (detalle.getNombreBeneficiario().length() > 30) {
                aux.append(detalle.getNombreBeneficiario().substring(0, 30)).append("\t");
            } else {
                aux.append(detalle.getNombreBeneficiario()).append("\t");
            }
            aux.append(detalle.getCuentaBcoBeneficiario()).append("\t");
            String valor = detalle.getValor().setScale(2, RoundingMode.HALF_DOWN).toString();
            aux.append(valor).append("\t");
            aux.append(detalle.getInstitucionFinanciera().getCuentaCorriente()).append("\t");
            aux.append(detalle.getTipoBeneficiario());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "detalle pago", e);
        }
        return aux;
    }

    public String createFileSPI(StringBuffer data) {
        try {
            String ruta = "/SPI-2005/";
            String txt = valoresService.findByCodigo("SPI_FILE_TXT");
            String md5 = valoresService.findByCodigo("SPI_FILE_MD5");
            File fzip = new File(valoresService.findByCodigo("SPI_FILE_ZIP"));
            Utils.createDirectoryIfNotExist(ruta);
            File ftxt = createTxt(txt, data.toString());
            session.setActKey(ftxt.getAbsolutePath());
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (Utils.getOperatingSystemType().equals(OSType.Linux)) {
                processBuilder.command("bash", "-c", String.format("openssl MD5 %s", ftxt.getAbsoluteFile()));
            } else if (Utils.getOperatingSystemType().equals(OSType.Windows)) {
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
//                int exitVal = process.waitFor(2000l,TimeUnit.MILLISECONDS);
                Boolean exitVal = process.waitFor(2000l, TimeUnit.MILLISECONDS);
                if (exitVal) {
                    String name = output.toString().split("=")[1];
                    session.setVarTemp(name.trim());
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
                    System.out.println("Algo paso ");
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
//            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF8")))) {
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

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE contabilidad.detalle_transferencias SET contabilizado = false WHERE id_diario_general = ?1")
                .setParameter(1, diarioGeneral);
        return query.executeUpdate();
    }
}
