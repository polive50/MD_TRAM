package org.madrid.saii.tramitacion.jsf.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * VMM 07/10/26 MD-2905: genera el Excel "Datos presupuestarios" de la solapa de Modificaciones
 * Presupuestarias/Plazos/Otras, con una hoja por cada tabla de presupuesto que se muestra en pantalla:
 * presupuesto antes de la solicitud de modificacion, modificaciones solicitadas y modificaciones
 * aceptadas. Recibe las filas ya extraidas de cada tabla (ver ExpedienteModifPresPlazoOtraBean) para
 * no depender de como se guarda cada una.
 */
public final class ExportadorDatosPresupuestariosModif {

    private ExportadorDatosPresupuestariosModif() {
    }

    /** Una fila (partida o concepto) de una tabla de presupuesto. */
    public static class Fila {
        private final String partida;
        private final Integer ordenPartida;
        private final String concepto;
        private final List<Double> importes;
        private final Double total;
        private final String observaciones;

        public Fila(String partida, Integer ordenPartida, String concepto, List<Double> importes,
                Double total, String observaciones) {
            this.partida = partida != null ? partida : "";
            this.ordenPartida = ordenPartida;
            this.concepto = concepto != null ? concepto : "";
            this.importes = importes;
            this.total = total;
            this.observaciones = observaciones != null ? observaciones : "";
        }
    }

    /** Una hoja del libro: una tabla de presupuesto. */
    public static class Hoja {
        private final String nombre;
        private final List<Fila> filas;
        private final boolean conObservaciones;

        public Hoja(String nombre, List<Fila> filas, boolean conObservaciones) {
            this.nombre = nombre;
            this.filas = filas != null ? filas : new ArrayList<>();
            this.conObservaciones = conObservaciones;
        }
    }

    /**
     * @param conConceptos true si la convocatoria es con conceptos (columna Concepto y subtotales por partida)
     * @param nAnualidades numero de anualidades (columnas de importes)
     * @param hojas tablas a volcar, una hoja por cada una
     * @return el libro .xlsx en bytes
     */
    public static byte[] generar(boolean conConceptos, int nAnualidades, List<Hoja> hojas)
            throws IOException {
        try (XSSFWorkbook libro = new XSSFWorkbook(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {

            CellStyle cabecera = libro.createCellStyle();
            Font negrita = libro.createFont();
            negrita.setBold(true);
            cabecera.setFont(negrita);
            cabecera.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            cabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle importe = libro.createCellStyle();
            // simbolos construidos por codigo de caracter para no depender de la codificacion del fuente
            String formatoEuro = "#,##0.00 \"" + (char) 0x20AC + "\"";
            importe.setDataFormat(libro.createDataFormat().getFormat(formatoEuro));

            CellStyle importeNegrita = libro.createCellStyle();
            importeNegrita.setDataFormat(libro.createDataFormat().getFormat(formatoEuro));
            importeNegrita.setFont(negrita);

            CellStyle textoNegrita = libro.createCellStyle();
            textoNegrita.setFont(negrita);

            for (Hoja hoja : hojas) {
                escribirHoja(libro, hoja, conConceptos, nAnualidades, cabecera, importe, importeNegrita,
                        textoNegrita);
            }

            libro.write(salida);
            return salida.toByteArray();
        }
    }

    private static void escribirHoja(XSSFWorkbook libro, Hoja hoja, boolean conConceptos, int n,
            CellStyle cabecera, CellStyle importe, CellStyle importeNegrita, CellStyle textoNegrita) {

        Sheet hojaExcel = libro.createSheet(hoja.nombre);
        int fila = 0;

        // Cabecera
        Row filaCabecera = hojaExcel.createRow(fila++);
        int col = 0;
        texto(filaCabecera, col++, "PARTIDA", cabecera);
        if (conConceptos) {
            texto(filaCabecera, col++, "CONCEPTO", cabecera);
        }
        for (int i = 1; i <= n; i++) {
            texto(filaCabecera, col++, i + String.valueOf((char) 0xBA) + " ANUALIDAD", cabecera);
        }
        texto(filaCabecera, col++, "TOTAL", cabecera);
        if (hoja.conObservaciones) {
            texto(filaCabecera, col++, "OBSERVACIONES", cabecera);
        }
        int totalColumnas = col;

        // Mismo orden que en pantalla: agrupado por partida segun su orden (sort estable, asi los
        // conceptos nuevos quedan junto a su partida)
        List<Fila> ordenadas = new ArrayList<>(hoja.filas);
        ordenadas.sort(Comparator.comparing(f -> f.ordenPartida != null ? f.ordenPartida : Integer.MAX_VALUE));
        Map<String, List<Fila>> porPartida = new LinkedHashMap<>();
        for (Fila f : ordenadas) {
            porPartida.computeIfAbsent(f.partida, k -> new ArrayList<>()).add(f);
        }

        double[] totalPorAnualidad = new double[n];
        double totalGeneral = 0.0;

        for (Map.Entry<String, List<Fila>> grupo : porPartida.entrySet()) {
            double[] subPorAnualidad = new double[n];
            double subTotal = 0.0;

            for (Fila f : grupo.getValue()) {
                Row r = hojaExcel.createRow(fila++);
                int c = 0;
                texto(r, c++, f.partida, null);
                if (conConceptos) {
                    texto(r, c++, f.concepto, null);
                }
                for (int i = 0; i < n; i++) {
                    Double valor = f.importes != null && i < f.importes.size() ? f.importes.get(i) : null;
                    numero(r, c++, valor, importe);
                    if (valor != null) {
                        subPorAnualidad[i] += valor;
                        totalPorAnualidad[i] += valor;
                    }
                }
                numero(r, c++, f.total, importe);
                if (f.total != null) {
                    subTotal += f.total;
                    totalGeneral += f.total;
                }
                if (hoja.conObservaciones) {
                    texto(r, c++, f.observaciones, null);
                }
            }

            if (conConceptos) {
                Row r = hojaExcel.createRow(fila++);
                int c = 0;
                texto(r, c++, grupo.getKey(), textoNegrita);
                texto(r, c++, "SUBTOTAL", textoNegrita);
                for (int i = 0; i < n; i++) {
                    numero(r, c++, subPorAnualidad[i], importeNegrita);
                }
                numero(r, c++, subTotal, importeNegrita);
            }
        }

        // Fila TOTAL
        Row filaTotal = hojaExcel.createRow(fila++);
        int c = 0;
        texto(filaTotal, c++, "TOTAL", textoNegrita);
        if (conConceptos) {
            texto(filaTotal, c++, "", textoNegrita);
        }
        for (int i = 0; i < n; i++) {
            numero(filaTotal, c++, totalPorAnualidad[i], importeNegrita);
        }
        numero(filaTotal, c++, totalGeneral, importeNegrita);

        for (int i = 0; i < totalColumnas; i++) {
            hojaExcel.autoSizeColumn(i);
        }
    }

    private static void texto(Row fila, int columna, String valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor != null ? valor : "");
        if (estilo != null) {
            celda.setCellStyle(estilo);
        }
    }

    private static void numero(Row fila, int columna, Double valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        if (valor != null) {
            celda.setCellValue(valor);
        }
        celda.setCellStyle(estilo);
    }
}
