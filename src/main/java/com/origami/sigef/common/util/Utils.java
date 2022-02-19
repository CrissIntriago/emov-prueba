package com.origami.sigef.common.util;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.marshalling.cloner.ClonerConfiguration;
import org.jboss.marshalling.cloner.ObjectCloner;
import org.jboss.marshalling.cloner.ObjectClonerFactory;
import org.jboss.marshalling.cloner.ObjectCloners;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *
 * @author User
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static final String SCHEMA_ASGARD = "asgard";
    public static final String SCHEMA_SGM = "sgm";
    public static final String SCHEMA_COMISARIA = "comisaria";
    public static final String SCHEMA_CATASTRO = "catastro";
    public static final String SCHEMA_ACTIVOS = "activos";
    public static final String SCHEMA_CONTABILIDAD = "contabilidad";
    public static final String SCHEMA_PROCESOS = "procesos";
    public static final String SCHEMA_PUBLIC = "public";
    public static final String SCHEMA_PRESUPUESTO = "presupuesto";
    public static final String SCHEMA_MIGRACION = "migracion";
    public static final String SCHEMA_VENTANILLA = "ventanilla";
    public static final String PROVINCIA = "09";
    public static final String CANTON = "0907";
    //c:/images/
    public static final String RUTA_IMAGES_DEV = "/server-files/duran/images/";
    public static final String RUTA_IMAGES_PRO = "/server-files/duran/images/";
    ///server-files/duran/images/
    //c:/images/documentos/
    public static final String RUTA_DOCUMENTOS_DEV = "D:/server-files/duranDocumentos/";

    public static Integer getAnio(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH) + 1;
    }

    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getHour(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.HOUR);
    }

    public static Integer getMinute(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MINUTE);
    }

    public static Integer getSecond(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.SECOND);
    }

    public static int obtenerDistanciaMeses(Date fechaInicio, Date fechaFin) {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(fechaInicio);
        //Fecha finalización en objeto Calendar
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(fechaFin);
        //Cálculo de meses para las fechas de inicio y finalización
        int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
        int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
        //Diferencia en meses entre las dos fechas
        int diffMonth = endMes - startMes;
        System.out.println("diffMonth " + diffMonth);
        return diffMonth + 2;

    }
    
    public static Date obtenerElPrimerDiaDelMes(String format, int mes){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format); 
            Calendar calendar = Calendar.getInstance(); 
            calendar.set(mes,1);
            String fecha = sdf.format(calendar.getTime());
            return sdf.parse(fecha);
        }catch(ParseException e){
            return new Date();
        }
    }

    public static Integer obtenerDistanciaDias(String fechados, String fechaUno) throws ParseException {

        return 0;

    }

    public static Integer getMiliSecond(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MILLISECOND);
    }

    public static int getMonthDiff(Date inicio, Date fin) {

        Calendar start = Calendar.getInstance();
        start.setTime(inicio);

        Calendar end = Calendar.getInstance();
        end.setTime(fin);

        Integer difA = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        return (difA * 12 + end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 1);

    }

    public static String generarCodigoVerificacion() {
        char[][] pairs = {{'a', 'z'}, {'0', '9'}};
        RandomStringGenerator sg = new RandomStringGenerator.Builder().withinRange(pairs).build();
        return sg.generate(12);
    }

    public static Integer getDiasResta(Date inicio, Date fin) {
        int res = (int) ((fin.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24));
        return Integer.valueOf(res);
    }

    public static int getDiasAnio(Date inicio) {

        Calendar start = Calendar.getInstance();
        start.setTime(inicio);

        return start.get(Calendar.DAY_OF_YEAR);
    }

    private static final int[] PATTERN = {2, 1, 2, 1, 2, 1, 2, 1, 2};
    private static final int[] CASO_9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] CASO_6 = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final String NUMERIC_REGEX = "^[0-9]+$";
    private static final String DECIMAL_REGEX = "^[+]?\\d+([.]\\d+)?$";
    private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[A-Za-z]{1,})$";

    public static BigDecimal bigdecimalTo2Decimals(BigDecimal inNumber) {
        String temp = inNumber.toString();
        BigDecimal outNumber;
        int indice = temp.indexOf('.');
        if (((inNumber.toString().length() - 1) - indice) > 2) {
            String tempNew = temp.substring(0, indice + 3);
            outNumber = new BigDecimal(tempNew);
            if (((temp.length()) - (indice + 1)) >= 3) {
                if (Integer.parseInt(temp.substring(tempNew.length(), tempNew.length() + 1)) >= 5) {
                    outNumber = outNumber.add(new BigDecimal("0.01"));
                }
            }
        } else {
            outNumber = inNumber;
        }
        return outNumber;
    }

    public static int getPartDate(Date date, int part) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (part) {
            case 1: {
                return calendar.get(Calendar.DAY_OF_MONTH);
            }
            case 2: {
                return calendar.get(Calendar.MONTH) + 1;
            }
            case 3: {
                return calendar.get(Calendar.YEAR);
            }
            default: {
                return 0;
            }
        }
    }

    public static List<String> separadorComas(String correos) {
        List<String> correosResulList = new ArrayList<>();
        String temp = correos;
        int indice = temp.indexOf(',');
        if (indice > 0) {
            do {
                String correo1 = temp.substring(0, indice);
                correosResulList.add(correo1);
                String correoRestante = temp.substring(indice + 1, temp.length());
                temp = correoRestante;
                indice = temp.indexOf(',');

            } while (indice > 0);
            correosResulList.add(temp);
        } else {
            correosResulList.add(correos);
        }
        return correosResulList;
    }

    public static Boolean isRepetido(Collection val, Object nuevo) {
        boolean i = false;
        for (Object x : val) {
            if (x.equals(nuevo)) {
                i = true;
                return i;
            }
        }
        return i;
    }

    public static synchronized boolean validarEmailConExpresion(String email) {
        return validatePattern(EMAIL_REGEX, email);
    }

    public static Long restarFechas(Date fechaMenor, Date fechaMayor) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fechaMenor);
        cal2.setTime(fechaMayor);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        /*
        long diffHour = diff / (60 * 60 * 1000);
        diffHour = diffHour - (diffDays * 24);
        if (diffHour > 0) {
            diffDays++;
        }
         */
        return diffDays;
    }

    public static synchronized boolean validateCCRuc(final String identificacion) {
        if (identificacion == null) {
            return false;
        }
        if (identificacion.trim().isEmpty()) {
            return false;
        }
        if (!validateNumberPattern(identificacion)) {
            return false;
        }
        if (identificacion.length() != 10 & identificacion.length() != 13) {
            return false;
        }
        int[] coeficientes = null;
        int indiceDigitoVerificador = 9;
        int modulo = 11;

        if ((identificacion.length() == 13) && !identificacion.substring(10, 13).equals("001")) {
            return false;
        }
        if (identificacion.charAt(2) == '9') {
            coeficientes = CASO_9;
        } else if (identificacion.charAt(2) == '6') {
            coeficientes = CASO_6;
            indiceDigitoVerificador = 8;
        } else if (identificacion.charAt(2) < '6') {
            coeficientes = PATTERN;
            modulo = 10;
        }
        return verify(identificacion.toCharArray(), coeficientes, indiceDigitoVerificador, modulo);
    }

    private static boolean verify(final char[] array, final int[] coeficientes,
            final int indiceDigitoVerificador, final int modulo) {
        if (coeficientes == null) {
            return false;
        }
        int sum = 0;
        int aux;
        for (int i = 0; i < coeficientes.length; i++) {
            aux = Integer.valueOf(String.valueOf(array[i])) * coeficientes[i];
            if ((modulo == 10) && (aux > 9)) {
                aux -= 9;
            }
            sum += aux;
        }
        int mod = sum % modulo;
        mod = mod == 0 ? modulo : mod;
        final int res = (modulo - mod);
        Integer valorVerificar = null;
        if (array.length == 13) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (13 - indiceDigitoVerificador)]));
        } else if (array.length == 10) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (10 - indiceDigitoVerificador)]));
        }
        return res == valorVerificar;
    }

    public static synchronized boolean validateNumberPattern(final String valor) {
        return validatePattern(NUMERIC_REGEX, valor);
    }

    public static synchronized boolean validateDecimalPattern(final String valor) {
        return validatePattern(DECIMAL_REGEX, valor);
    }

    public static synchronized boolean validatePattern(final String patron, final String valor) {
        final Pattern patter = Pattern.compile(patron);
        final Matcher matcher = patter.matcher(valor);
        return matcher.matches();
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public static Date sumarDiasFechaSinWeekEnd(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int hoy;
        do {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            hoy = cal.get(Calendar.DAY_OF_WEEK);
            if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                dias--;
            }
        } while (dias > 0);
        return cal.getTime();
    }

    public static String pasarUtf(String cadena) throws UnsupportedEncodingException {
        if (cadena != null) {
            byte[] bytes = cadena.getBytes("ISO-8859-1");
            cadena = new String(bytes, "UTF-8");
        }
        return cadena;
    }

    public static String randomNumericString() {
        int i = (int) (Math.random() * 100000);
        return String.valueOf(i);
    }

    public static Integer getDateValues(String formatValue, Date value) {
        Integer res = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        f.format(value);
        switch (formatValue.toUpperCase()) {
            case "Y":
                res = f.getCalendar().get(Calendar.YEAR);
                break;
            case "M":
                res = f.getCalendar().get(Calendar.MONTH);
                break;
            case "D":
                res = f.getCalendar().get(Calendar.DAY_OF_MONTH);
                break;
        }
        return res;
    }

    public static String completarCadenaConCeros(String cadena, Integer longitud) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String ceros = "";
        for (int i = 0; i < longitud; i++) {
            ceros = ceros + "0";
        }
        int tamanio = cadena.length();
        ceros = ceros.substring(0, longitud - tamanio);
        cadena = ceros + cadena;
        return cadena;
    }

    public static String convertirMesALetra(Integer fechames) {
        String mes;
        switch (fechames) {
            case 1:
                mes = "ENERO";
                break;
            case 2:
                mes = "FEBRERO";
                break;

            case 3:
                mes = "MARZO";
                break;

            case 4:
                mes = "ABRIL";
                break;

            case 5:
                mes = "MAYO";
                break;

            case 6:
                mes = "JUNIO";
                break;

            case 7:
                mes = "JULIO";
                break;

            case 8:
                mes = "AGOSTO";
                break;

            case 9:
                mes = "SEPTIEMBRE";
                break;

            case 10:
                mes = "OCTUBRE";
                break;

            case 11:
                mes = "NOVIEMBRE";
                break;

            default:
                mes = "DICIEMBRE";
        }
        return mes;
    }

    public static String quitarSaltos(String cadena) {
        return cadena.replace("\r", "").replace("\n", "");
    }

    public static boolean isDecimal(String cad) {
        try {
            Double.parseDouble(cad);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection l) {
        if (l == null) {
            return true;
        } else if (l.size() == 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    /**
     * Si el String es nulo returna vacio, caso contrario el mismo valor.
     *
     * @param nombres Object
     * @return Object
     */
    public static String isEmpty(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "";
        }
        return nombres;
    }

    /**
     * Verifica que el valor numerico no sea nulo <code>value</code> y retorna
     * el mismo valor de <code>value</code> caso contrario retorna -1.
     *
     * @param <T> Object
     * @param value Valor a verificar.
     * @return si el valor de <code>value</code> es nulo retorna -1 caso
     * contrario el valor de <code>value</code>
     */
    @SuppressWarnings("unchecked")
    public static <T> T isNull(T value) {
        if (value == null || value.toString().trim().length() < 0) {
            return (T) new BigInteger("-1");
        }
        return (T) value;
    }

    /**
     * Verifica si <code>value</code> es nulo y retorna <code>true</code>, caso
     * contrario retorna <code>false</code>.
     *
     * @param value Tipo de Dato Númerico de cualquier tipo primitivo o objecto.
     * @return True si el null caso contrario false.
     */
    public static Boolean isNumberNull(Number value) {
        if (value == null || value.longValue() < 0L) {
            return true;
        }
        return false;
    }

    public static <T> T get(final List<T> values, int idx) {
        if (values.size() > idx) {
            return values.get(idx);
        }
        return null;
    }

    public static <T> T get(final Collection<T> values, int idx) {
        if (values.size() > idx) {
            List<T> result = new ArrayList<>(values);
            return result.get(idx);
        }
        return null;
    }

    public static Date validateDate(String fecha) throws ParseException {
//        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(fecha);
        String formattedDate = formatter.format(date);
        date = formatter.parse(formattedDate);
//        System.out.println("fecha--->" + date);
        return date;
    }

    public static Date devolverFecha(Date fecha, String formato) throws ParseException {
//        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateFormat formatter = new SimpleDateFormat(formato);
        String dateString = formatter.format(fecha);
        Date date = formatter.parse(dateString);
//        String formattedDate = formatter.format(date);
//        date = formatter.parse(formattedDate);
        System.out.println("fecha--->" + date);
        return date;
    }

    /**
     * Le da formato a la fecha con el pattern que se le pasa como parametro
     *
     * @param pattern Formato que se desea obtener.
     * @param fechaFin Fecha a dar formato.
     * @return Fecha con el formato esperado.
     */
    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static boolean isNum(String nom) {
        try {
            Long.parseLong(nom);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static int boolean2int(Boolean x) {
        if (x) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param <T> Object
     * @param object Lista de objecto principal
     * @param previousValues lista a retornar
     * @param duplicateArray Lista a comparar si estan repetidos.
     * @param compare 0 para realizar comparacion en binario, para hacer la
     * comparacion como texto 1
     * @return Object
     */
    public static <T> List<T> verificarRepetidos(final List<T> object, final List<T> previousValues, final List<T> duplicateArray, final int compare) {
        Iterator<T> iterator = object.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            int count = 0;
            for (T t : duplicateArray) {
                if (compare == 0) {
                    if (next.equals(t)) {
                        count++;
                    }
                } else {
                    if (String.valueOf(next).equalsIgnoreCase(String.valueOf(t))) {
                        count++;
                    }
                }
            }
            if (count == 0 && !previousValues.contains(next)) {
                previousValues.add(next);
            }

        }
        return previousValues;
    }

    public static String getValorUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String quitarTildes(String cadena) {
        if (cadena == null) {
            return "";
        } else {
            cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
            cadena = cadena.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            return cadena;
        }

    }

    /**
     * TRANSFORMA LA PRIMERA LETRA EN MAYUSCULA
     *
     * @param text Texto a procesar.
     * @return Texto con la primera letra en mayucula y despues de cada _ la
     * pasa a mayucula
     */
    public static String transformUpperCase(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String[] aux = text.split("_");
        if (aux.length <= 1) {
            return text;
        }
        String result = "";
        int count = 0;
        for (String string : aux) {
            if (count > 0) {
                result += string.substring(0, 1).toUpperCase().concat(string.substring(1));
            } else {
                result = string;
            }
            count++;
        }
        return result;
    }

    /**
     * Realiza un split a los apellidos si encuenta un de o del lo ubica como
     * parte de uno de los apellidos.
     *
     * @param apellidos
     * @return
     */
    public static List<String> obtenerApellidos(String apellidos) {
        if (apellidos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        apellidos = reemplazarEspacionEnBlanco(apellidos, " ");
        String[] split = apellidos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";

        for (String cadena : split) {
            if (countApellidos != 0) {
                switch (split.length) {
                    case 2:
                        result.add(cadena);
                        countApellidos--;
                        break;
                    default:
                        if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                            cadenaAux = cadena + " ";
                            continue;
                        }
                        result.add(cadenaAux + cadena);
                        cadenaAux = "";
                        countApellidos--;
                        break;
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Busca los apellidos en toda la cadena si existe en la cadena 'de' o 'del'
     * los concatena a apellidos siguiente.
     *
     * @param nombresCompletos Nombres y apellidos completos
     * @param inicioApellidos0 Si es false indica que empezara a recorrer desde
     * la ultima cadena hacia atras
     * @return Los 2 apellidos
     */
    public static List<String> obtenerApellidos(String nombresCompletos, boolean inicioApellidos0) {
        if (nombresCompletos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        nombresCompletos = reemplazarEspacionEnBlanco(nombresCompletos, " ");
        String[] split = nombresCompletos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";
        if (inicioApellidos0) {
            for (String cadena : split) {
                if (countApellidos != 0) {
                    switch (split.length) {
                        case 4:
                            result.add(cadena);
                            countApellidos--;
                            break;
                        default:
                            if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                                cadenaAux = cadena + " ";
                                continue;
                            }
                            result.add(cadenaAux + cadena);
                            cadenaAux = "";
                            countApellidos--;
                            break;
                    }
                } else {
                    break;
                }
            }
        } else {
            for (int i = split.length - 1; i >= 0; i--) {
                if (countApellidos > -1 || (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de"))) {
                    switch (split.length) {
                        case 4:
                            if (countApellidos > 0) {
                                result.add(0, split[i]);
                                countApellidos--;
                            }
                            break;
                        default:
                            if (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de")) {
                                cadenaAux = split[i] + " ";
                                System.out.println("Cadena " + cadenaAux);
                                if (result.size() > 0) {
                                    result.set(0, cadenaAux + result.get(0));
                                    cadenaAux = "";
                                    countApellidos--;
                                }
                            } else {
                                result.add(0, cadenaAux + split[i]);
                                cadenaAux = "";
                                countApellidos--;
                            }
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static String reemplazarEspacionEnBlanco(String cadena, String caracterNuevo) {
        return cadena.replaceAll("\\s+", caracterNuevo);
    }

    public static String convertirFechaLetra(Date fecha) {
        String mesLetra = Utils.convertirMesALetra(Utils.getMes(fecha));
        String fechaLetra = Utils.getDia(fecha).toString() + " de "
                + mesLetra.substring(0, 1).toUpperCase() + mesLetra.substring(1)
                + " del " + Utils.getAnio(fecha).toString();
        return fechaLetra;
    }

    public static File copyFileServer(List<UploadedFile> files, String directorio) throws IOException {
        try {
            //Path path = Paths.get(SisVars.rutaRepositorioArchivo);
            Path path = Paths.get("ruta del archivo");
            Files.createDirectories(path);
            for (UploadedFile uFile : files) {
                File file = new File("ruta del archivo" + "/" + uFile.getFileName());
//                System.out.println(file.getName() + " >> " + file.length()); // tamanio de foto.
                try (InputStream is = uFile.getInputstream(); OutputStream out = new FileOutputStream(file)) {
                    byte buf[] = new byte[2048];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                return path.toFile();
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServer(UploadedFile event, String ruta) throws IOException {
        try {
            String archivo = new String(event.getFileName().getBytes(Charset.defaultCharset()), "UTF-8");
            String nombreArchivo = new Date().getTime() + "_" + StringUtils.stripAccents(archivo).replace(" ", "_").replace("-", "_");
            File file = new File(ruta + nombreArchivo);

            InputStream is;
            is = event.getInputstream();
            try (OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close();
            return file;
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServerOne(RenLocalComercialFoto ar) throws IOException {
        try {
            //Path path = Paths.get(SisVars.rutaRepositorioArchivo);
            Path path = Paths.get("ruta del archivo");
            Files.createDirectories(path);

            File file = new File(ar.getRuta());
            try (InputStream is = ar.getInputStream(); OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                return path.toFile();
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static File copyFileServerOneBoveda(EspFotoBovedas ar) throws IOException {
        try {
            //Path path = Paths.get(SisVars.rutaRepositorioArchivo);
            Path path = Paths.get("ruta del archivo");
            Files.createDirectories(path);

            File file = new File(ar.getRuta());
            try (InputStream is = ar.getInputStream(); OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                return path.toFile();
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static void copyFileServerDoc(String ruta, InputStream inputStream) throws IOException {
        try {

            File file = new File(ruta);
            try (InputStream is = inputStream; OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }

    }

    public static String createDirectoryIfNotExist(String directorio) {
        Path path = Paths.get(directorio);
        String file = null;
        String dir = directorio;
        if (dir.contains(".")) {
            file = dir;
            dir = file.substring(0, file.replace("\\", "/").lastIndexOf("/"));
            path = Paths.get(dir);
        }
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        if (file != null) {
//            try {
//                new File(file).createNewFile();
//            } catch (IOException ex) {
//                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        return directorio;
    }

    public static String completarCadenaL(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = addCadena + cadena;
        return cadena;
    }

    public static String completarCadenaR(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = cadena + addCadena;
        return cadena;
    }

    /**
     *
     * @param pageDlg
     * @param params
     */
    public static void openDialog(String pageDlg, Map<String, List<String>> params) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "70%");
        options.put("height", "75%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic(pageDlg, options, params);
    }

    public static void openDialog(String pageDlg, String width, String height) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", width);
        options.put("height", height);
        options.put("closable", true);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic(pageDlg, options, null);
    }

    public static void openDialog(String pageDlg, String width, String height, Map<String, List<String>> params) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", width);
        options.put("height", height);
        options.put("closable", true);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic(pageDlg, options, params);
    }

    public static List<String> getMeses() {
        List<String> meses = new ArrayList<>();
        meses.add("ENERO");
        meses.add("FEBRERO");
        meses.add("MARZO");
        meses.add("ABRIL");
        meses.add("MAYO");
        meses.add("JUNIO");
        meses.add("JULIO");
        meses.add("AGOSTO");
        meses.add("SEPTIEMBRE");
        meses.add("OCTUBRE");
        meses.add("NOVIEMBRE");
        meses.add("DICIEMBRE");
        return meses;
    }

    public static Integer convertirLetraAMes(String mes) {
        Integer mesNumerito;
        switch (mes) {
            case "Ene":
            case "ENERO":
                mesNumerito = 1;
                break;
            case "Feb":
            case "FEBRERO":
                mesNumerito = 2;
                break;
            case "Mar":
            case "MARZO":
                mesNumerito = 3;
                break;
            case "Abr":
            case "ABRIL":
                mesNumerito = 4;
                break;
            case "May":
            case "MAYO":
                mesNumerito = 5;
                break;
            case "Jun":
            case "JUNIO":
                mesNumerito = 6;
                break;
            case "Jul":
            case "JULIO":
                mesNumerito = 7;
                break;
            case "Ago":
            case "AGOSTO":
                mesNumerito = 8;
                break;
            case "Sep":
            case "SEPTIEMBRE":
                mesNumerito = 9;
                break;
            case "Oct":
            case "OCTUBRE":
                mesNumerito = 10;
                break;
            case "Nov":
            case "NOVIEMBRE":
                mesNumerito = 11;
                break;
            default:
                mesNumerito = 12;
        }
        return mesNumerito;
    }

    public static Long idTemp() {
        return new Random(System.currentTimeMillis()).nextLong();
    }

    public static <T> T clone(Object source) {
        final ObjectClonerFactory clonerFactory = ObjectCloners.getSerializingObjectClonerFactory();
        final ClonerConfiguration configuration = new ClonerConfiguration();
//        configuration.setObjectResolver(new EntityResolver());
        final ObjectCloner cloner = clonerFactory.createCloner(configuration);
        Object result = null;
        try {
            result = cloner.clone(source);
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Clone", e);
        }
        return (T) result;

    }

    public static String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    public static String cifrarBase64(String a) {
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        String b = encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8));
        return b;
    }

    public static String quitaDiacriticos(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.toUpperCase();
        return s;
    }

    /**
     * Compara dos objectos convirtiendolos a string
     *
     * @param nue Objecto 1 a comparar
     * @param sys Objecto 2 a comparar
     * @return True si son iguales, caso contrario false.
     */
    public static Boolean compararObjectos(Object nue, Object sys) {
        return (nue + "").trim().equalsIgnoreCase((sys + "").trim());
    }

    /**
     * Compara caracter en un String
     *
     * @param character String 1 a buscar
     * @param codigo String 2 a comparar
     * @return True si caracter esta dentro de String, caso contrario false.
     */
    public static boolean haveCharacter(String character, String codigo) {
        boolean result = false;
        for (int x = 0; x < codigo.length(); x++) {
            if (codigo.charAt(x) == character.charAt(0)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static Date getPrimerDiaAnio(Integer anio) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicial = anio.toString() + "-01-01";
        return formato.parse(fechaInicial);
    }

    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    public static OSType getOperatingSystemType() {
        OSType detectedOS;
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            detectedOS = OSType.MacOS;
        } else if (OS.indexOf("win") >= 0) {
            detectedOS = OSType.Windows;
        } else if (OS.indexOf("nux") >= 0) {
            detectedOS = OSType.Linux;
        } else {
            detectedOS = OSType.Other;
        }
        return detectedOS;
    }

    public static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(String user, String pass) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient(user, pass));
        return clientHttpRequestFactory;
    }

    public static HttpClient httpClient(String user, String pass) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        HttpClient client1 = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        return client1;
    }

    public static String colorRandom() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public static int getDiasDelMes(int mes, int año) {
        switch (mes) {
            case 0:  // Enero
            case 2:  // Marzo
            case 4:  // Mayo
            case 6:  // Julio
            case 7:  // Agosto
            case 9:  // Octubre
            case 11: // Diciembre
                return 31;
            case 3:  // Abril
            case 5:  // Junio
            case 8:  // Septiembre
            case 10: // Noviembre
                return 30;
            case 1:  // Febrero
                if (((año % 100 == 0) && (año % 400 == 0))
                        || ((año % 100 != 0) && (año % 4 == 0))) {
                    return 29;  // Año Bisiesto
                } else {
                    return 28;
                }
            default:
                throw new java.lang.IllegalArgumentException(
                        "El mes debe estar entre 0 y 11");
        }
    }

    public static String codigoVerificacion() {
        int rangoCodigo = 99999;
        Random aleatorio = new Random(System.currentTimeMillis());
        return completarCadenaConCeros(String.valueOf(aleatorio.nextInt(rangoCodigo)), 5);
    }

    public static String contraseniaTemporal() {
        int rangoCodigo = 999999999;
        Random aleatorio = new Random(System.currentTimeMillis());
        return completarCadenaConCeros(String.valueOf(aleatorio.nextInt(rangoCodigo)), 9);
    }

    public static int betweenAnios(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
                || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String encriptaEnMD5(String stringAEncriptar) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(stringAEncriptar);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     *
     * Cambiar el directorio
     *
     * @param ruta
     * @return ruta sin el directorio
     */
    public static String getFilterRuta(String ruta) {
//        char[] unidad = ruta.toCharArray();
//        char a = unidad[0];
//        if ((a + "").equals("C") || (a + "").equals("D") || (a + "").equals("F")) {
//            unidad[1] = '_';
//        }
//        ruta = new String(unidad).replace("\\", "-").replace("/", "-");
        ruta = ruta.replace("\\", "/");
        if (ruta.startsWith(SisVars.RUTA_DOCUMENTOS)) {
            ruta = ruta.replaceFirst(SisVars.RUTA_DOCUMENTOS, "doc_ar_");
        } else if (ruta.startsWith(SisVars.RUTA_FILES_TEMP)) {
            ruta = ruta.replaceFirst(SisVars.RUTA_FILES_TEMP, "ar_temp_");
        } else if (ruta.startsWith(SisVars.RUTA_FILES_PRESUPUESTO)) {
            ruta = ruta.replaceFirst(SisVars.RUTA_FILES_PRESUPUESTO, "ar_pres_");
        }
        return ruta;
    }

    /**
     * Recuperar directorio
     *
     * @param ruta
     * @return ruta con el directorio completo
     */
    public static String urlRuta(String ruta) {
        if (ruta.startsWith("doc_ar_")) {
            ruta = ruta.replaceFirst("doc_ar_", SisVars.RUTA_DOCUMENTOS);
        } else if (ruta.startsWith("ar_temp_")) {
            ruta = ruta.replaceFirst("ar_temp_", SisVars.RUTA_FILES_TEMP);
        } else if (ruta.startsWith("ar_pres_")) {
            ruta = ruta.replaceFirst("ar_pres_", SisVars.RUTA_FILES_PRESUPUESTO);
        }
        return ruta;
    }

    public static double redondearDosDecimales(double valor) {
        return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = (Map<Object, Boolean>) new ConcurrentHashMap<T, T>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

    public static boolean existsURLActiva(String URLName) {

        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                    .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public static String mailHtmlNotificacion(String titulo, String texto, String footerTitulo, String footerTexto) {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"utf-8\">\n"
                + "        <style>\n"
                + "\n"
                + "            body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }\n"
                + "\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body style=\"background: #E1E1E1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\">\n"
                + "        <table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\">\n"
                + "            <tr>\n"
                + "                <td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\">\n"
                + "                    <table cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                        <tr>\n"
                + "                            <td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\">\n"
                + "                                <br><br><br><br>\n"
                + "                                <table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\">\n"
                + "                                    <table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                <tr>\n"
                + "                                                    <td style=\"padding-bottom: 20px;\">\n"
                + "                                                        <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                            <tr>\n"
                + "                                                                <td class=\"featured-story__inner\" style=\"background: #fff;\">\n"
                + "                                                                    <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                        <tr>\n"
                + "                                                                            <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">\n"
                + "                                                                                <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                                    <h2 style=\"text-decoration: none; color: #464646;\">\n"
                + "                                                                                                      " + titulo + "</h2>\n"
                + "                                                                                    <tr>\n"
                + "                                                                                        <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">\n"
                + "                                                                                            <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                                                <tr>\n"
                + "                                                                                                    <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">\n"
                + "                                                                                                        " + texto + "\n"
                + "                                                                                                    </td>\n"
                + "                                                                                                </tr>\n"
                + "                                                                                            </table>\n"
                + "                                                                                        </td>\n"
                + "                                                                                    </tr>\n"
                + "                                                                                </table>\n"
                + "                                                                            </td>\n"
                + "                                                                        </tr>\n"
                + "                                                                    </table>\n"
                + "                                                                </td>\n"
                + "                                                            </tr>\n"
                + "                                                        </table>\n"
                + "                                                    </td>\n"
                + "                                                </tr>\n"
                + "                                            </table>\n"
                + "                                        </td>\n"
                + "                                    </tr>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                        <tr>\n"
                + "                            <td class=\"footer\" width=\"640\" align=\"center\" style=\"padding-top: 10px;\">\n"
                + "                                <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                    <tr>\n"
                + "                                        <td align=\"center\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 14px; line-height: 18px; color: #738597; padding: 0 20px 40px;\">\n"
                + "                                            <br>      <br>\n"
                + "                                            <strong>" + footerTitulo + "</strong>\n"
                + "                                            <br>\n"
                + "                                            " + footerTexto + "\n"
                + "                                        </td>\n"
                + "                                    </tr>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                    </table>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>\n"
                + "    </body>\n"
                + "</html>";
    }

    public static Gson getInstanceGson() {
//        GsonBuilder builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
//                .registerTypeAdapter(Date.class, new JsonDateDeserializer());
        return new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    }
}
