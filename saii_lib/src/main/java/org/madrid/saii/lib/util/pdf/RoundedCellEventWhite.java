package org.madrid.saii.lib.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Clase de apoyo para la generación de cuadros con esquinas redondeadas para
 * PDF
 *
 * @author everis
 * @version 1.0.0
 */
public class RoundedCellEventWhite implements PdfPCellEvent {

    /**
     * Método para generar el componente tipo cuadro con esquinas redondeadas
     *
     * @param cell Celda donde es invocado a la clase
     * @param position Posiciones del contenedor del componente
     * @param canvases Datos del documento pdf donde se agregará el componente
     */
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.roundRectangle(position.getLeft() + 1.5f, position.getBottom() + 1.5f,
                position.getWidth() - 3, position.getHeight() - 3, 4);
        canvas.setColorFill(BaseColor.WHITE);
        canvas.fillStroke();
    }
}
