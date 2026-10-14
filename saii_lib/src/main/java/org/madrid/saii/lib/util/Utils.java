package org.madrid.saii.lib.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.DatoDTO;

import atlas.core.util.UserUtils;

/**
 * Clase de Utilidades.
 *
 * @author everis
 */
public final class Utils {

    /**
     * Instantiates a new utils.
     */
    private Utils() {

    }

    /**
     * Converte un String en un Date.
     *
     * @param fecha
     * @return date
     */
    public static Date convertADate(final String fecha) {

        Date datFecha = null;

        if (StringUtils.isNotBlank(fecha)) {
            SimpleDateFormat formato = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
            try {
                datFecha = formato.parse(fecha);
            } catch (ParseException e) {
                datFecha = null;
            }
        }

        return datFecha;
    }

    /**
     * Converte un String en un Date.
     *
     * @param fecha
     * @return date
     */
    public static Date convertADateTime(final String fecha, final String formatoFecha) {

        Date datFecha = null;

        if (StringUtils.isNotBlank(fecha)) {
            SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
            try {
                datFecha = formato.parse(fecha);
            } catch (ParseException e) {
                datFecha = null;
            }
        }

        return datFecha;
    }

    /**
     * Fecha entre rango.
     *
     * @param fechaIni
     * @param fechaFin
     * @return boolean
     */
    public static Boolean fechaEntreRango(final Date fechaIni, final Date fechaFin) {
        Date hoy = new Date();
        Boolean res = Boolean.FALSE;

        if (fechaIni != null && fechaFin != null
                && (fechaIni.before(hoy) && fechaFin.after(hoy)
                        || fechaIni.equals(hoy) && fechaFin.after(hoy)
                        || fechaIni.before(hoy) && fechaFin.equals(hoy))) {
            res = Boolean.TRUE;
        }

        return res;

    }

    /**
     * Sumar o restar fecha pasada al metodo.
     *
     * @param paramDate
     * @param dias
     * @return date
     */
    public static Date fechaSumarRestarDias(final Date paramDate, Integer dias) {

        Date datExit = null;
        GregorianCalendar gcTemp = null;

        if (paramDate != null) {
            gcTemp = new GregorianCalendar();
            gcTemp.clear();
            gcTemp.setTime(paramDate);
            gcTemp.add(Calendar.DAY_OF_YEAR, dias);

            datExit = gcTemp.getTime();
        }

        return datExit;

    }

    /**
     * Crea la fecha por defecto 1/1/1990
     */
    public static Date crearFechaDefecto() {

        Calendar calendarDate = Calendar.getInstance();

        calendarDate.set(Calendar.YEAR, 1990);
        calendarDate.set(Calendar.MONTH, Calendar.JANUARY);
        calendarDate.set(Calendar.DAY_OF_MONTH, 1);
        calendarDate.set(Calendar.HOUR_OF_DAY, 23);
        calendarDate.set(Calendar.MINUTE, 59);
        calendarDate.set(Calendar.SECOND, 59);
        calendarDate.set(Calendar.MILLISECOND, 59);

        return calendarDate.getTime();
    }

    /**
     * Crea la fecha por defecto fecha hoy sin la hora.
     */
    public static Date crearFechaActualSinHora() {

        Calendar calendarDate = Calendar.getInstance();

        calendarDate.set(Calendar.YEAR, 2020);
        calendarDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);

        return calendarDate.getTime();
    }

    /**
     * El método comprobarFechas sirve para saber cuando mostrar o no la fecha
     * de convocatoria. Esta fecha de comparacion es la fecha por defecto
     *
     * @param fecha a comprobar
     * @return TRUE si la fecha que pasamos por parametro es igual a la fecha
     *         por defecto. FALSE si es menor o null
     */
    public static Boolean comprobarFechas(Date fecha) {

        Boolean bIguales = Boolean.FALSE;

        Date fechaDefecto = Utils.crearFechaDefecto();

        if (fecha == null) {
            bIguales = Boolean.TRUE;
        }

        if (fecha != null && fecha.compareTo(fechaDefecto) <= 0) {
            bIguales = Boolean.TRUE;
        }
        return bIguales;

    }

    /**
     * Días entre fechas.
     *
     * @param fechaInicio
     * @param fechaFin
     * @return Integer
     */
    public static Integer diasEntreFechas(final Date fechaInicio, final Date fechaFin) {
        return (int) Math.abs((fechaInicio.getTime() - fechaFin.getTime()) / (1000 * 60 * 60 * 24))
                + 1;
    }

    /**
     * Devuelve un String a partir de una fecha. Nunca nulo
     *
     * @param date
     * @return string
     */
    public static String dateToString(final Date date) {

        String strSalida = StringUtils.EMPTY;

        if (date != null) {
            strSalida = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA).format(date);
        }

        return strSalida;
    }

    /**
     * Devuelve un String a partir de una fecha. Nunca nulo
     *
     * @param date
     * @param formato
     * @return string
     */
    public static String dateToString(final Date date, final String formato) {

        String strSalida = StringUtils.EMPTY;

        if (date != null) {
            strSalida = new SimpleDateFormat(formato).format(date);
        }

        return strSalida;
    }

    /**
     * Devuele empty si es null.
     *
     * @param input
     * @return string
     */
    public static String tratar(Object input) {
        return input == null ? StringUtils.EMPTY : input.toString();
    }

    /**
     * Devuele el usuario logado en mayusculas. Si estamos en text devuelve
     * "TEST".
     *
     * @return string
     */
    public static String userName() {

        String userName = UserUtils.getUserName();

        if (StringUtils.isNotBlank(userName)) {
            userName = userName.toUpperCase();
        } else {
            userName = "TEST";
        }

        return userName;
    }

    /**
     * Formatea el nº de Ereg
     *
     * @param nmEreg
     * @return string
     */
    public static String formatearNmEreg(String nmEreg) {
        if (StringUtils.isNotBlank(nmEreg)) {
            String nReg = nmEreg.substring(0, 2);
            String sol = nmEreg.substring(4, 10);
            String anio = nmEreg.substring(2, 4);
            String fin = nmEreg.substring(nmEreg.length() - 1, nmEreg.length());
            nmEreg = nReg + "/" + sol + "." + fin + "/" + anio;
        }

        return nmEreg;
    }
    
    
    public static String desformatearNmEreg(String nmEregFormateado) {
        String[] partes = nmEregFormateado.split("[/.]");
            return partes[0] + partes[3] + partes[1] + partes[2];
    }
    

    /**
     * Texto mes.
     *
     * @param nroMes
     * @return string
     */
    public static String textoMes(Integer nroMes) {
        Locale local = new Locale("es", "ES");
        Month mes = Month.of(nroMes);
        return mes.getDisplayName(TextStyle.FULL, local);
    }

    /**
     * Metodo que nos devuelve si la fecha que le pasamos por parametro cumple
     * el formato dd/MM/yyyy
     *
     * @param fecha
     * @return
     */
    public static Boolean validarFecha(final String fecha) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
        sdfrmt.setLenient(Boolean.FALSE);
        try {
            sdfrmt.parse(fecha);
        } catch (ParseException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Metodo que nos devuelve si el string ingresado puede castearse a entero
     *
     * @param cadena
     * @return
     */
    public static Boolean validarEntero(final String cadena) {
        try {
            Integer.parseInt(cadena);
            return Boolean.TRUE;
        } catch (NumberFormatException nfe) {
            return Boolean.FALSE;
        }

    }

    /**
     * Metodo para validar si el objeto es null
     *
     * @param objeto
     * @return
     */
    public static Boolean isNull(Object objeto) {
        Boolean validacion = Boolean.FALSE;
        if (objeto == null) {
            validacion = Boolean.TRUE;
        }
        return validacion;
    }

    /**
     * Salvaguarda para los campos de auditoría (CD_USU_ALTA, CD_USU_MODIF,
     * CD_USU_BAJA), que según la normativa de modelado de BBDD son
     * VARCHAR2(12). Devuelve el identificador tal cual si cabe en la columna,
     * o la constante de usuario extracomunitario en caso contrario (por
     * ejemplo, si el identificador es un email de un usuario extracomunitario
     * cuyo tipo de identificación no se haya podido determinar correctamente),
     * evitando así un ORA-12899 independientemente del origen del valor.
     *
     * @param identificador valor candidato para un campo de auditoría
     * @return el propio identificador, o
     *         {@link Constantes.Identificacion#CD_USU_EXTRACOMUNITARIO} si no
     *         cabe en la columna
     */
    public static String valorAuditoria(String identificador) {
        return (identificador != null
                && identificador.length() > Constantes.Utiles.LONGITUD_MAX_CD_USU_AUDITORIA)
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : identificador;
    }

    /**
     * Metodo para convertir objeto Date a LocalDate
     *
     * @param dateToConvert
     * @return
     */
    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        LocalDate fecha = null;
        if (Objects.nonNull(dateToConvert)) {
            fecha = dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return fecha;
    }

    /**
     * Metodo para convertir objeto Date a LocalDateTime
     *
     * @param dateToConvert
     * @return
     */
    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        LocalDateTime fechaHora = null;
        if (Objects.nonNull(dateToConvert)) {
            fechaHora = dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return fechaHora;
    }

    /**
     * Metodo para convertir Date a LocalDateTime usando la clase Instant
     *
     * @param dateToConvert
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateToLocalDateTimeInstant(Date dateToConvert) {
        LocalDateTime fechaHora = null;
        if (Objects.nonNull(dateToConvert)) {
            fechaHora = Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        return fechaHora;
    }

    /**
     * Metodo para convertir Date a LocalDate usando la clase Instant
     *
     * @param localDateTimeToConvert
     * @return Date
     */
    public static Date convertLocalDateToDateInstant(LocalDateTime localDateTimeToConvert) {
        Date fechaHora = null;
        if (Objects.nonNull(localDateTimeToConvert)) {
            fechaHora = Date
                    .from(localDateTimeToConvert.atZone(ZoneId.systemDefault()).toInstant());
        }
        return fechaHora;
    }

    /**
     * Metodo para convertir LocalDateTime a Date usando la clase Instant
     *
     * @param dateToConvert
     * @return LocalDateTime
     */
    public static LocalDate convertDateToLocalDateInstant(Date dateToConvert) {
        LocalDate fechaHora = null;
        if (Objects.nonNull(dateToConvert)) {
            fechaHora = Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return fechaHora;
    }

    /**
     * Función que elimina acentos y caracteres especiales de una cadena de
     * texto.
     *
     * @param cadena Cadena de texto a tratar
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String remplazarEspeciales(String cadena) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = cadena;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        } // for i
        return output;

    }

    /**
     * Valida si el correo es válido
     *
     * @param correo
     * @return
     */
    public static Boolean esCorreoValido(String correo) {
        Pattern correoPattern = Pattern.compile(Constantes.CorreoValido.PATRON_CORREO);
        return correoPattern.matcher(correo).matches();
    }

    /**
     * Copiar tiempo a fecha
     */
    public static Date copiarTiempoFecha(Date date, Date time) {
        Calendar t = Calendar.getInstance();
        t.setTime(time);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, t.get(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, t.get(Calendar.MILLISECOND));
        return c.getTime();
    }

    /**
     * Asignar tiempo a fecha para inicio de día
     */
    public static Date asignarTiempoFechaInicioDia(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Asignar tiempo a fecha para final de día
     */
    public static Date asignarTiempoFechaFinalDia(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Fecha y hora actual entre rango de fechas.
     *
     * @param fechaIni
     * @param fechaFin
     * @return boolean
     */
    public static Boolean fechaHoraActualEntreRango(final LocalDateTime fechaIni,
            final LocalDateTime fechaFin) {
        LocalDateTime hoy = LocalDateTime.now();
        Boolean res = Boolean.FALSE;

        if (fechaIni != null && fechaFin != null
                && (fechaIni.isBefore(hoy) && fechaFin.isAfter(hoy)
                        || fechaIni.equals(hoy) && fechaFin.isAfter(hoy)
                        || fechaIni.isBefore(hoy) && fechaFin.equals(hoy))) {
            res = Boolean.TRUE;
        }

        return res;

    }
    
    public static Boolean fechasEntreRango(final Date fechaIni, final Date fechaFin) {
        Date hoy = new Date();
        Boolean res = Boolean.FALSE;

        if (fechaIni != null && fechaFin != null
                && ((fechaIni.before(hoy)  || fechaIni.equals(hoy) )
                        && fechaFin.after(hoy)) ) {
            res = Boolean.TRUE;
        }

        return res;

    }
    
    public static Boolean fechasIsAnterior(final Date fechaIni) {
        Date hoy = new Date();
        Boolean res = Boolean.FALSE;

        if (fechaIni != null 
                && (fechaIni.after(hoy))) {
            res = Boolean.TRUE;
        }

        return res;

    }
    
    public static Boolean fechasInicioEntreRango(final Date fechaIni) {
        Date hoy = new Date();
        Boolean res = Boolean.FALSE;

        if (fechaIni != null 
                && (fechaIni.before(hoy)  || fechaIni.equals(hoy) )) {
            res = Boolean.TRUE;
        }

        return res;

    }
    public static String formatearBigDecimalEuropeo(BigDecimal valor) {
        DecimalFormat formatoEuropeo;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        formatoEuropeo = new DecimalFormat("#,##0.00", symbols);
        formatoEuropeo.setDecimalSeparatorAlwaysShown(true);
        return formatoEuropeo.format(valor) + " €";
    }
    
    public static boolean esPrimerCaracterNumero (String texto) {
        if (texto != null && !texto.isEmpty()) {
            char primerCaracter = texto.charAt(0);
            return Character.isDigit(primerCaracter);
        }
        return false;
    }
    
    public static Integer getIdExpedienteDeAcronimo(String acronimoExpe) {
        Integer idExpediente = 0;
        if (acronimoExpe != null && !acronimoExpe.isEmpty()) {
            String[] cadena = acronimoExpe.split("-");
            idExpediente = Integer.valueOf(cadena[cadena.length - 1]);
        }
        return idExpediente;
    }
    
    //MD-2821 normalizar las busquedas sin acentos ni otras marcas diacríticas (dieresis, etc...)
    public static String normalizarSinAcentos(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFD)
                         .replaceAll("\\p{M}", "")   // elimina marcas diacríticas
                         .toUpperCase();
    }
    
    public static Boolean validarDouble(final String cadena) {
        try {
            Double.parseDouble(cadena);
            return Boolean.TRUE;
        } catch (NumberFormatException nfe) {
            return Boolean.FALSE;
        }
    }

    /**
     * Normaliza un numero decimal escrito con separadores ambiguos (coma y/o punto) al formato
     * que entiende Double.parseDouble (punto decimal, sin separador de miles). Util cuando el
     * valor viene tecleado libremente por el ciudadano y puede llegar en formato español
     * (coma decimal, punto de miles), anglosajon (punto decimal) o una mezcla/error de tecleo.
     *
     * Regla aplicada:
     * - Si aparecen coma Y punto a la vez, el que va mas a la derecha es el separador decimal y
     *   el otro se trata como separador de miles y se elimina (ej. "1.234,56" -> "1234.56",
     *   "1,234.56" -> "1234.56").
     * - Si solo aparece uno de los dos, se asume separador decimal (ej. "12,5" -> "12.5"), salvo
     *   que la cadena coincida exactamente con el patron de un separador de miles sin parte
     *   decimal (grupos de 3 digitos, ej. "1.234" o "1,234"), en cuyo caso se elimina como
     *   separador de miles (ej. "1.234" -> "1234").
     *
     * @param valor cadena introducida por el usuario
     * @return la cadena normalizada con punto decimal, o el valor original si es null/vacio
     */
    public static String normalizarSeparadorDecimal(final String valor) {
        if (StringUtils.isBlank(valor)) {
            return valor;
        }

        String cadena = valor.trim();
        int posComa = cadena.lastIndexOf(',');
        int posPunto = cadena.lastIndexOf('.');

        if (posComa >= 0 && posPunto >= 0) {
            if (posComa > posPunto) {
                // la coma es el separador decimal, el/los punto(s) son de miles
                cadena = cadena.replace(".", "").replace(',', '.');
            } else {
                // el punto es el separador decimal, la(s) coma(s) son de miles
                cadena = cadena.replace(",", "");
            }
        } else if (posComa >= 0) {
            if (cadena.matches("^-?\\d{1,3}(,\\d{3})+$")) {
                cadena = cadena.replace(",", "");
            } else {
                cadena = cadena.replace(',', '.');
            }
        } else if (posPunto >= 0 && cadena.matches("^-?\\d{1,3}(\\.\\d{3})+$")) {
            cadena = cadena.replace(".", "");
        }

        return cadena;
    }

    /**
     * Normaliza in-place el dsValor de cada DatoDTO de la lista con normalizarSeparadorDecimal.
     * Pensado para listas que contienen EXCLUSIVAMENTE valores numericos (importes/porcentajes de
     * bloques tipo presupuesto/ayuda solicitada/actuaciones); no usar sobre listas que puedan
     * contener texto libre, ya que el valor se sobrescribe sin comprobar si es o no numerico.
     *
     * @param datos lista de DatoDTO a normalizar (se ignoran los elementos/lista null)
     */
    public static void normalizarSeparadorDecimalDatos(final List<DatoDTO> datos) {
        if (datos == null) {
            return;
        }
        for (DatoDTO dato : datos) {
            if (dato != null) {
                dato.setDsValor(normalizarSeparadorDecimal(dato.getDsValor()));
            }
        }
    }

}
