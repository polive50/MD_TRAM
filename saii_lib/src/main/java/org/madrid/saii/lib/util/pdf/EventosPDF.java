package org.madrid.saii.lib.util.pdf;

import org.madrid.saii.lib.dto.FichAnexoDocumDTO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author everis
 *
 */
public class EventosPDF extends PdfPageEventHelper {

    /** Teto bold */
    private static Font fontBold = FontFactory.getFont("Arial", "ISO-8859-1", false, 10, Font.BOLD);

    /** FichAnexoDocumDTO */
    private FichAnexoDocumDTO fichAnexoDocumDTO;

    /**
     * Constructor
     *
     * @param eventoDTO
     */
    public EventosPDF(FichAnexoDocumDTO eventoDTO) {
        this.fichAnexoDocumDTO = eventoDTO;
    }

    /**
     * Evento al iniciar la creacion de la pagina
     *
     * @param writer
     * @param document
     */
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            // Crear logo
            PdfPTable tablaLogo = new PdfPTable(1);

            float anchoTotal = document.getPageSize().getWidth();
            float columnaImagen = anchoTotal * 22 / 100;

            tablaLogo.setWidthPercentage(new float[] {columnaImagen }, document.getPageSize());

            // Cambiar tamaño de la imagen
            Image logo = this.fichAnexoDocumDTO.getLogo();
            logo.setWidthPercentage(columnaImagen);

            PdfPCell celdaLogo = new PdfPCell(logo, true);
            celdaLogo.setBorder(0);
            celdaLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaLogo.setVerticalAlignment(Element.ALIGN_CENTER);

            tablaLogo.addCell(celdaLogo);
            tablaLogo.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cellSeparacion = new PdfPCell(new Phrase("\n\n"));
            cellSeparacion.setBorderWidth(0);
            tablaLogo.addCell(cellSeparacion);

            // Crear cabecera
            PdfPTable tablaCabecera = new PdfPTable(1);
            PdfPCell celdaCabecera = new PdfPCell(
                    new Phrase(this.fichAnexoDocumDTO.getTextoTitulo(), fontBold));
            celdaCabecera.setBorder(0);
            celdaCabecera.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            tablaCabecera.addCell(celdaCabecera);

            // Añadir elementos al documento
            document.add(tablaLogo);
            document.add(tablaCabecera);

        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }
}
