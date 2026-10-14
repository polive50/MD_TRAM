package org.madrid.saii.lib.util.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
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
 * Clase de apoyo para la generación de componente tipo caja de texto para PDF
 *
 * @author everis
 * @version 1.0.0
 */
public class TextFieldCellEvent implements PdfPCellEvent {

    /**
     * Nombre de componente
     */
    protected String nombre;

    /**
     * Tipo de caja de texto
     */
    protected int opcion;

    /**
     * Variable que representa el alto del campo
     */
    protected int lineaCampo;

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
    public TextFieldCellEvent(String nombre, int opcion) {
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
     * Constructor de la clase
     *
     * @param nombre Nombre del componente
     * @param opcion Tipo de caja de texto
     */
    public TextFieldCellEvent(String nombre, int opcion, int lineaCampo) {
        this.nombre = nombre;
        this.opcion = opcion;
        this.lineaCampo = lineaCampo;
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
        Rectangle rect = new Rectangle(position.getLeft() + 3, y - 10, position.getRight() - 3,
                y + 10);

        if (this.opcion == BaseField.MULTILINE) {

           /* int margen = 20;

            if (this.lineaCampo > 0) {
                margen = this.lineaCampo * 6;
            }

            rect = new Rectangle(position.getLeft() + 3, y - margen, position.getRight() - 3,
                    y + margen); */
            
            float padding = 4f;
            rect = new Rectangle(
                position.getLeft()   + 3,
                position.getBottom() + padding,   // desde el fondo de la celda
                position.getRight()  - 3,
                position.getTop()    - padding    // hasta el tope de la celda
            );
        }

        final TextField textField = new TextField(writer, rect, this.nombre);
        textField.setOptions(this.opcion);
        textField.setFont(this.tipoLetra);
        textField.setFontSize(8);
        textField.setBorderColor(BaseColor.WHITE);
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
