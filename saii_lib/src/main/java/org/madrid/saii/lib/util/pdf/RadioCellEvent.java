package org.madrid.saii.lib.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

/**
 * Clase de apoyo para la generación de componente tipo radio para PDF
 *
 * @author everis
 * @version 1.0.0
 */
public class RadioCellEvent implements PdfPCellEvent {

    /**
     * Agrupador de radios
     */
    protected PdfFormField grupoRadio;

    /**
     * Texto del elemento radio
     */
    protected String valor;

    /**
     * Constructor de la clase
     *
     * @param grupoRadio Agrupador de radios
     * @param valor Texto del elemento radio
     */
    public RadioCellEvent(PdfFormField grupoRadio, String valor) {
        this.grupoRadio = grupoRadio;
        this.valor = valor;
    }

    /**
     * Método para generar el componente tipo radio
     *
     * @param cell Celda donde es invocado a la clase
     * @param position Posiciones del contenedor del componente
     * @param canvases Datos del documento pdf donde se agregará el componente
     */
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        final PdfWriter writer = canvases[0].getPdfWriter();
        int maxWidth=127;
        float x = (position.getLeft() + position.getRight()) / 2;
        float y = (position.getTop() + position.getBottom()) / 2;
        
        Rectangle rect = new Rectangle(x - 6, y - 6, x + 6, y + 6);
        if(this.valor != null && this.valor.length()>maxWidth) {
            this.valor= this.valor.substring(0,maxWidth);
        }
        
        RadioCheckField radio = new RadioCheckField(writer, rect, null, this.valor);
        radio.setCheckType(RadioCheckField.TYPE_CIRCLE);
        radio.setBackgroundColor(new GrayColor(0.8f));
        radio.setBorderColor(BaseColor.GRAY);
        try {
            this.grupoRadio.addKid(radio.getRadioField());
            
        } catch (final Exception ioe) {
            throw new ExceptionConverter(ioe);
        }
        
    }
}
