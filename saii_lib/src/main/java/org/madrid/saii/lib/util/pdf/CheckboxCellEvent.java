package org.madrid.saii.lib.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

/**
 * Clase de apoyo para la generación de componente tipo checkbox para PDF
 *
 * @author everis
 * @version 1.0.0
 */
public class CheckboxCellEvent implements PdfPCellEvent {

    /**
     * Nombre de componente
     */
    protected String nombre;

    /**
     * Constructor de la clase
     *
     * @param nombre Nombre del componente
     */
    public CheckboxCellEvent(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para generar el componente tipo checkbox
     *
     * @param cell Celda donde es invocado a la clase
     * @param position Posiciones del contenedor del componente
     * @param canvases Datos del documento pdf donde se agregará el componente
     */
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();

        float x = (position.getLeft() + position.getRight()) / 2;
        float y = (position.getTop() + position.getBottom()) / 2;

        Rectangle rect = new Rectangle(x - 6, y - 6, x + 6, y + 6);

        RadioCheckField checkbox = new RadioCheckField(writer, rect, this.nombre, "Yes");
        checkbox.setCheckType(RadioCheckField.TYPE_CROSS);
        checkbox.setBorderColor(BaseColor.GRAY);
        try {
            writer.addAnnotation(checkbox.getCheckField());
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
