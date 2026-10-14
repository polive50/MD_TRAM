package org.madrid.saii.lib.util.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

/**
 * Clase de apoyo para la generación de celdas inline
 *
 * @author everis
 * @version 1.0.0
 */
public class InlineCellEvent implements PdfPCellEvent {

    /**
     * Nombre de componente
     */
    protected String nombre;

    /**
     * Tipo de caja de texto
     */
    protected int opcion;

    /**
     * Tipo de letra para el componente
     */
    protected BaseFont tipoLetra;

    /**
     * Constructor de la clase
     *
     * @param nombre Nombre del componente
     * @param opcion Tipo de caja de texto
     */
    public InlineCellEvent(String nombre, int opcion) {
        this.nombre = nombre;
        this.opcion = opcion;
        try {
            this.tipoLetra = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250,
                    BaseFont.NOT_EMBEDDED);
            this.tipoLetra.setSubset(false);
        } catch (final IOException ioe) {
            throw new ExceptionConverter(ioe);
        } catch (final DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Método para generar el componente tipo caja de texto
     *
     * @param cell Celda donde es invocado a la clase
     * @param position Posiciones del contenedor del componente
     * @param canvases Datos del documento pdf donde se agregará el componente
     */
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        final PdfWriter writer = canvases[0].getPdfWriter();
        float y = (position.getTop() + position.getBottom()) / 2;
        Rectangle rect = new Rectangle(position.getLeft(), y - 9, position.getRight(), y + 9);

        final TextField textField = new TextField(writer, rect, this.nombre);
        textField.setOptions(this.opcion);
        textField.setFont(this.tipoLetra);
        textField.setFontSize(9);

        if (BaseField.HIDDEN == this.opcion) {
            textField.setOptions(BaseField.EDIT);
            textField.setVisibility(BaseField.HIDDEN);
        }

        try {
            final PdfFormField field = textField.getTextField();
            writer.addAnnotation(field);
        } catch (final IOException ioe) {
            throw new ExceptionConverter(ioe);
        } catch (final DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}
