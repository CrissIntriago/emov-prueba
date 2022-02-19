/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Commons;

import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class SisVars {

    public static String actividad;// = "/asdf/geoPortalSni/";
    public static String localImageDirectory;// = "/asdf/geoPortalSni/";
    /**
     * Retorna el contexto de la Aplicacion "application/'
     */
    public static String urlbase;// = "/asdf/geoPortalSni/";
    public static String facesUrl = "/faces";
    public static String urlbaseFaces;// = "/asdf/geoPortalSni/faces/";
    public static String urlbaseFacesSinBarra;// = "/asdf/geoPortalSni/faces";
    public static String ejbRuta;// = "java:global/JavaServerFaces/";
    public static String formatoArchivos;// ="/(\\.|\\/)(gif|jpe?g|png|pdf|xlsx|docx|xlsm|dwg|shp|doc|xls|ppt|pptx|tif|txt)$/";
    public static String geoserverUrl;
    public static String geoserverProxyUrl;
    public static String urlServidorWorkFlow;// ="http://localhost:8084/JavaServerFaces";
    public static String urlServidorCatastro;// ="http://localhost:8084/smbCatastro";
    public static String urlServidorSGM;// ="http://localhost:8084/sgm";
    public static String correoClienteGenerico;
    public static String rutaReportesDinardap;
    public static String rutaRepotiorioArchivo;
    public static String rutaRepotiorioFichas;//= "C:\\servers_files\\cmbFlow\\repo\\";
    public static String rutaRepositorioUsuarios;
    public static String rutaRepositorioUsuariosInternos;
    public static String urlServidorCatastroPublica;
    public static String urlServidorWorkFlowPublica;
    public static String urlServidorWorkFlowPublicaNotificacion;
    public static String urlServerWorkFlowPublic;
    public static String urlWorkFlowVentanilla;
    public static String urlServidorAlfrescoPublica;
    public static String urlServidorAlfrescoInterna;
    public static String ipPublica;
    public static String ipInterna;
    public static String ipPublicaAlfresco;
    public static String ipInternaAlfresco;
    public static String ipSqlServer;
    public static String nameDBSqlServer;
    public static String userSqlServer;
    public static String passwordSqlServer;
    public static String smtp_Host;
    public static String smtp_Port;
    public static String correo;
    public static String pass;
    public static boolean ssl;
    public static String carpetaAdministrativaAlfresco;
    public static String sqlServerDriverClass;
    public static String sqlServerUrl;
    public static String logoReportes;
    public static String sisFooter;
    public static String sisFooter2;
    public static String sisLado;

    public static String sisLogo;
    public static String sisLogo1;
    public static String sisLogoSL;

    public static String sisLogoAgua;
    /*Agregado*/
    public static String sisFirmaRentas;
    public static String sisFirmaTesorero;
    public static String sisFirmaFinanciero;


    /*Fondo Marca de Agua*/
    public static String sisMarcaAgua;

    /*Permiso de Fucionamiento*/
    public static String sisPermisoFuncionamiento;

    /*Hoja Menbretada*/
    public static String sisHojaMenbretada;

    /*Director de Agua Potable*/
    public static String sisFirmaDirectorAguaPotable;

    /*Llave Pico*/
    public static String sisllavePico;

    public static String varbackground;

    //variables rutas archivos de propiedades
    public static String driverClass;
    public static String url;
    public static String userName;
    public static String password;
    public static int minPoolSize = 1;
    public static int maxPoolSize = 1;
    public static int maxIdleTime = 1;

    //variables  sgmdoc
    public static String driverClassSgmDocs;
    public static String urlSgmDocs;
    public static String userNameSgmDocs;
    public static String passwordSgmDocs;
    public static int minPoolSizeSgmDocs = 1;
    public static int maxPoolSizeSgmDocs = 1;
    public static int maxIdleTimeSgmDocs = 1;

    //variables nuevas de SGM
    public static String urlPublica;
    public static String urlServidorPublica;
    public static String urlServidorCompleta;

    //JNDI EJB
    public static String entityManager;
    public static String bpmBaseEngine;
    public static String bpmProcessEngine;
    public static String datasource;
    public static String solicitud;

    //zoning
    public static String region;

    //conexion a base postgresql anterior smbcatas
    public static String driverClassDbOld;
    public static String urlDbOld;
    public static String userNameDbOld;
    public static String passwordDbOld;
    public static int minPoolSizeDbOld = 1;
    public static int maxPoolSizeDbOld = 1;
    public static int maxIdleTimeDbOld = 1;

    //conexion a base postgresql anterior activiti
    public static String driverClassDbOldAct;
    public static String urlDbOldAct;
    public static String userNameDbOldAct;
    public static String passwordDbOldAct;
    public static int minPoolSizeDbOldAct = 1;
    public static int maxPoolSizeDbOldAct = 1;
    public static int maxIdleTimeDbOldAct = 1;

    //conexion a base postgresql censoCat
    public static String ccdriverClass;
    public static String ccUrl;
    public static String ccUserName;
    public static String ccPassword;
    public static int ccMinPoolSize = 1;
    public static int ccMaxPoolSize = 1;
    public static int ccMaxIdleTime = 1;

    //PPLICATION EMPRESA
    public static Short PROVINCIA = 9;
    public static Short CANTON = 78;
    public static String NOMBREMUNICIPIO;
    public static String GADMUNICIPIO;
    public static String URLPLANOIMAGENPREDIO;
    public static String URLPLANOIMAGENPREDIOCOL;
    public static String NOMBRECANTON;
    public static String NOMBREPROVINCIA;

    public static String REPORTES = "reportes";
    /**
     *
     */
    public static String URL_REPORTES;

    public static String sitioWebMunicipio;

    public static String bandera;

    // sql server
    public static String SQL_SERVER_URL;
    public static String SQL_SERVER_USERNAME;
    public static String SQL_SERVER_PASSWORD;
    public static String SQL_SERVER_DATABASE;
    public static int SQL_SERVER_PORT;
    public static String SQL_SERVER_URL_AGUA;
    public static String SQL_SERVER_USERNAME_AGUA;
    public static String SQL_SERVER_PASSWORD_AGUA;
    public static String SQL_SERVER_DATABASE_AGUA;
    public static int SQL_SERVER_PORT_AGUA;

    public static String firmaUsuario;
    public static Integer mesesDeudaConvenio;

    // variables agua
    public static BigDecimal deudaTotalPermisibleCorte;
    public static int mesesCorteInicial;
    public static int mesesFullCorte;
    public static int mesOrdenanzaNueva;
    public static int anioNuevaOrdenanza;
    public static Boolean actualizarEstadoAgua;

    //VARIBLE REMISION DE INTERES
    public static String fechaMaximaRemisionInteres;
    public static String fechaPublicacionOrdenanza;

    public static String rutaImagenesPoligonos;
    public static String SERVICE_URL = "http://190.57.139.138:8785/api/dinardap/aplicacion/MOCACHE/persona/identificacion/%s";
    public static String SERVICE_USER = "Em0T-D1n4rD4p";
    public static String SERVICE_PASS = "IbBTF;e;Fomj0du4H@M5";
    public static String SERVICE_URL1 = "http://190.57.139.138:8790/api/rp/persona/aplicacion/SGR/codigoPaquete/%s/identificacion/%s";
    public static String SERVICE_USER1 = "adminVentanilla";
    public static String SERVICE_PASS1 = "NKPCSQ10@PROPIEDAD";

    // Variables Ventanilla
    public static String ws="http://127.0.0.1:8814/ws/duran/api/";
    public static String wsZull = "http://127.0.0.1:8813";
    public static String appWEB;
    public static String wsMedia;

    public static String RUTA_REPORTE_PRACTICAS_PRUEBAS = "C:/";
    public static String RUTA_REPORTE_PRACTICAS_PRODUCCION = "/servers_files/duran/pasante/";

    /**
     * *RUTAS QUE SE CARGAN EN PropertiesLoader **
     */
    public static String RUTA_IMAGES;
    public static String RUTA_PROFORMA;
    public static String RUTA_DOCUMENTOS;
    public static String RUTA_IMAGES_QR;
    public static String RUTA_IMAGES_TEMP;
    public static String RUTA_FILES_TEMP;
    public static String RUTA_FILES_PRESUPUESTO;
    public static String RUTA_FILES_CERTIFICACIONPAPP;
    public static String RUTA_DOCS_TEMP;
    public static String RUTA_CERTIFICADOS;
    public static String RUTA_DOCUMENTOS_VENTANILLA;
    public static String RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS;
    public static String RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS;
    public static String appQrService;
    public static String appAtlantisUrl;
    public static String wsEmail;
    public static String wsFirmaEC;
    public static String rutaFirmasElectronicas;
    public static Boolean IMPRIMIR_ETIQUETAS;
    public static int CANTIDAD = 1;

    public static String RUTA_FORMATS_REGISTROS_CONTABLES;
    public static String RUTA_DOCUMENTOS_GENERADOS_REFORMAT1;
    public static String RUTA_GESTION_TRIBUTARIA;
    public static String RUTA_EVIDENCIA;
    public static String RUTA_REPOSITORIO;
    
   // SQL SERVER CONNET
    

    /**
     * Si va s a agregar una nueva ruta debes tambein agregarla en Utils ...
     * getFilterRuta en ese metodo para poder hacer el reemplazo al moneto d
     * obtener el archivo y agregar en asgar media la condicion todo esto es
     * para que no se vea la ruta del servidor en la url
     */
    public static String rutaRepositorioArchivo; //= "D:/servers_files/archivos/ ";

    public static String getPathGeoApi() {
        String cp = SisVars.URLPLANOIMAGENPREDIO;
        return cp.substring(0, cp.indexOf("/predio/") + 1);
    }
}
