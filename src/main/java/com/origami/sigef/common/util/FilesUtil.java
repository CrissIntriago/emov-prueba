/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gutya
 */
public class FilesUtil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(FilesUtil.class.getName());

    /**
     * Return Copia el archivo en el servidor y de vualve el archivo guardado
     *
     * @param event FileUploadEvent de primefaces
     * @param ruta RUTA_FIRMA_ELECTRONICA => C:\servers_files\fd
     * @return InputStream
     * @throws java.io.IOException
     */
    public static InputStream copyFileServer(UploadedFile event, String ruta) throws IOException {
        try {
            Date d = new Date();
            File file = new File(ruta + event.getFileName());

            InputStream is;
            is = event.getInputstream();
            try ( OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close();
            return new FileInputStream(file);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServer(List<UploadedFile> files, String directorio) throws IOException {
        try {
            Path path = Paths.get(directorio);
            Files.createDirectories(path);
            for (UploadedFile uFile : files) {
                File file = new File(directorio + "/" + uFile.getFileName());
                try ( InputStream is = uFile.getInputstream();  OutputStream out = new FileOutputStream(file)) {
                    byte buf[] = new byte[2048];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                return path.toFile();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    /**
     * archivo a copiar
     *
     * @param event
     *
     * direcctorio del archivo
     *
     * se reemplaza los espacios porque en las firmas electronicas da un error
     * por lo mismo
     *
     * @param ruta
     * @return
     * @throws IOException
     */
    public static InputStream copyFileServer(FileUploadEvent event, String ruta) throws IOException {
        try {
            Path path = Paths.get(ruta);
            Files.createDirectories(path);
            File file = new File(path.toFile().getAbsoluteFile() + File.separator + Utils.reemplazarEspacionEnBlanco(event.getFile().getFileName(), "_"));
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream is;
            is = event.getFile().getInputstream();
            try ( OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close();
            return new FileInputStream(file);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServer1(FileUploadEvent event, String ruta) throws IOException {
        try {
            Path path = Paths.get(ruta.endsWith("/") ? ruta.concat("/") : ruta);
            Files.createDirectories(path);
            File file = new File(path.toFile().getAbsoluteFile() + File.separator + new Date().getTime() + event.getFile().getFileName());
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream is;
            is = event.getFile().getInputstream();
            try ( OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close();
            return file;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static void createDirectory(String newDirectory) {
        try {
            if (newDirectory != null) {
                Path path = Paths.get(newDirectory.endsWith("/") ? newDirectory : newDirectory.concat("/"));
                Path parent = path.getParent();
                if (parent != null) {
                    if (parent.toFile().exists()) {
                        if (!path.toFile().exists()) {
                            Files.createDirectory(path);
                        } else {
                            System.out.println("Exists directory " + newDirectory);
                        }
                    } else {
                        Files.createDirectories(path);
                    }
                } else {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FilesUtil.class.getName()).log(Level.INFO, "No se pudo crear el directorio: {0}", ex.getMessage());
        }
    }

    public static String replaceRutaArchivo(String ruta) {
        char[] unidad = ruta.toCharArray();
        char a = unidad[0];
        if ((a + "").equals("C") || (a + "").equals("D") || (a + "").equals("F")) {
            unidad[1] = ':';
        }
        return new String(unidad).replace("-", "/");
    }

    public static void eliminarArchivoServer(String ruta) {
        File archivo = new File(ruta);
        boolean estatus = archivo.delete();
        if (!estatus) {
            System.out.println("Error no se ha podido eliminar el  archivo");
        } else {
            System.out.println("Se ha eliminado el archivo exitosamente");
        }
    }
}
