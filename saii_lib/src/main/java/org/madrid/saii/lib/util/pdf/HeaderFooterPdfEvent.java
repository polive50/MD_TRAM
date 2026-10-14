package org.madrid.saii.lib.util.pdf;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.dto.ImpresoDTO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de apoyo para la generación de cabecera y pie de página del documento
 * PDF
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@Slf4j
public class HeaderFooterPdfEvent extends PdfPageEventHelper {

	/**
	 * Tipo de letra en negrita de tamaño 14
	 */
	protected Font tipoLetra14Bold;

	/**
	 * Tipo de letra de tamaño 7
	 */
	protected Font tipoLetra7;

	/**
	 * Tipo de letra de tamaño 8
	 */
	protected Font tipoLetra8;
	
	static Font tipoFuente8 = new Font(FontFamily.HELVETICA, 8);
	
	/**
     * Tipo de letra en negrita de tamaño 8
     */
    protected Font tipoLetra8Bold;
    static Font tipoLetra8BOLD = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
	
	/**
     * Tipo de letra en negrita de tamaño 11
     */
    protected Font tipoLetra11Bold;

	/**
	 * Plantilla de documento
	 */
	protected PdfTemplate plantilla;

	/**
	 * Imágen que representa el total de páginas
	 */
	protected Image total;

	/**
	 * Propiedad ImpresoDTO
	 */
	private ImpresoDTO impresoDTO;

	/**
	 * Campo fondos europeos
	 */
	private Boolean siFondosEuropeos;

	/**
	 * Campo strConsejeria
	 */
	private String strConsejeria;

	/**
	 * Campo strDireccion
	 */
	private String strDireccion;

	/**
	 * Campo mostrarRegistroEntrada
	 */
	private Boolean mostrarRegistroEntrada;

	/** Lista de Logos a insertar **/
	private Map<String, byte[]> lstLogo;

	/**
	 *
	 */
	private byte[] logoOrganismo;

	private Map<String, String> mapaDatosEntrada;

	/**
	 * Constructor
	 *
	 * @param true1
	 */
	public HeaderFooterPdfEvent(ImpresoDTO impresoDTO, Boolean siFondosEuropeos, String strConsejeria,
			String strDireccion, Boolean mostrarRegistroEntrada, Map<String, byte[]> lstLogo, byte[] logoOrganismo) {
		this.impresoDTO = impresoDTO;
		this.siFondosEuropeos = siFondosEuropeos;
		this.strConsejeria = strConsejeria;
		this.strDireccion = strDireccion;
		this.mostrarRegistroEntrada = mostrarRegistroEntrada;
		this.lstLogo = lstLogo;
		this.logoOrganismo = logoOrganismo;
	}

	/**
	 * Constructor Plantillas
	 */
	public HeaderFooterPdfEvent(String strConsejeria, String strDireccion, byte[] logoOrganismo,
			Map<String, byte[]> lstLogo, Boolean siFondosEuropeos, Boolean mostrarRegistroEntrada) {
		this.strConsejeria = strConsejeria;
		this.strDireccion = strDireccion;
		this.logoOrganismo = logoOrganismo;
		this.lstLogo = lstLogo;
		this.siFondosEuropeos = siFondosEuropeos;
		this.mostrarRegistroEntrada = mostrarRegistroEntrada;
	}

	/**
	 * Constructor Plantillas
	 */
	public HeaderFooterPdfEvent(String strConsejeria, String strDireccion, byte[] logoOrganismo,
			Map<String, byte[]> lstLogo, Boolean siFondosEuropeos, Boolean mostrarRegistroEntrada,
			Map<String, String> mapaDatosEntrada) {
		this.strConsejeria = strConsejeria;
		this.strDireccion = strDireccion;
		this.logoOrganismo = logoOrganismo;
		this.lstLogo = lstLogo;
		this.siFondosEuropeos = siFondosEuropeos;
		this.mostrarRegistroEntrada = mostrarRegistroEntrada;
		this.mapaDatosEntrada = mapaDatosEntrada;
	}

	/**
	 *
	 */
	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		this.plantilla = writer.getDirectContent().createTemplate(30, 16);
		try {
			this.total = Image.getInstance(this.plantilla);
			this.total.setRole(PdfName.ARTIFACT);
			this.tipoLetra14Bold = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
			this.tipoLetra11Bold = new Font(FontFamily.HELVETICA,11, Font.BOLD);
			this.tipoLetra8 = new Font(FontFamily.HELVETICA, 8);
			this.tipoLetra7 = new Font(FontFamily.HELVETICA, 7);
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	/**
	 *
	 */
	@Override
	public void onEndPage(PdfWriter writer, Document document) {

		this.agregarCabeceraPaginaPlantilla(writer, document, this.strConsejeria, this.strDireccion, this.logoOrganismo,
				this.lstLogo, this.mostrarRegistroEntrada, this.siFondosEuropeos);

		this.agregarPiePagina(writer);
	}

	/**
	 *
	 */
	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(this.plantilla, Element.ALIGN_LEFT,
				new Phrase(String.valueOf(writer.getPageNumber() - 1), this.tipoLetra8), 2, 6, 0);
	}

	/**
	 *
	 * @param writer
	 * @param document
	 * @param strConsejeria
	 * @param strDireccion
	 * @param logoOrganismo
	 * @param lstLogo
	 * @param mostrarRegistroEntrada
	 */
	private void agregarCabeceraPaginaPlantilla(PdfWriter writer, Document document, String strConsejeria,
			String strDireccion, byte[] logoOrganismo, Map<String, byte[]> lstLogo, Boolean mostrarRegistroEntrada,
			Boolean siFondosEuropeos) {

		try {

			int margenSuperior = 50;

			Boolean validaLogoEuropeo = Objects.nonNull(lstLogo) && Boolean.FALSE.equals(lstLogo.isEmpty());

			if (Boolean.TRUE.equals(validaLogoEuropeo) || Objects.nonNull(logoOrganismo)) {

				// Creamos el espacio para agregar los logos
//				PdfPTable tablaLogos = new PdfPTable(3);
				PdfPTable tablaLogos = new PdfPTable(2);
//				tablaLogos.setWidths(new float[] { 4f, 1f, 4f });
				tablaLogos.setWidths(new float[] { 3f, 5f });
				tablaLogos.setTotalWidth(527);
				tablaLogos.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				tablaLogos.getDefaultCell().setPadding(0);

				// Agregamos el logo de organismo
//				PdfPTable tablaSeccionLogoOrganismo = new PdfPTable(2);
				PdfPTable tablaSeccionLogoOrganismo = new PdfPTable(1);
				tablaSeccionLogoOrganismo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//				tablaSeccionLogoOrganismo.setWidths(new float[] { 1f, 4f });
				tablaSeccionLogoOrganismo.setWidths(new float[] { 1f });

				PdfPCell celdaIzquierda = new PdfPCell();

				if (Objects.nonNull(logoOrganismo)) {
					try {
						Image logoComunidad = Image.getInstance(logoOrganismo);
						logoComunidad.scaleAbsolute(175, 50);
						celdaIzquierda = new PdfPCell(logoComunidad);
					} catch (Exception e) {
						// VMM 10/08/26: si el logo no se puede decodificar (p.ej. al repetirse en una segunda
						// pagina del mismo documento) no bloqueamos la generacion de todo el PDF por
						// una imagen; se deja esa pagina sin logo y se continua
						log.error("No se pudo cargar el logo del organismo en la cabecera de pagina: "
								+ e.getMessage());
					}
				}
//				celdaIzquierda.setRowspan(2);
				celdaIzquierda.setBorder(Rectangle.NO_BORDER);
				tablaSeccionLogoOrganismo.addCell(celdaIzquierda);

//				PdfPCell celdaDerechaTitulo = new PdfPCell(
//						new Paragraph(Objects.nonNull(logoOrganismo) ? strDireccion : "", this.tipoLetra8));
//				PdfPCell celdaDerechaTitulo = new PdfPCell();
//				celdaDerechaTitulo.setBorder(Rectangle.NO_BORDER);
//				tablaSeccionLogoOrganismo.addCell(celdaDerechaTitulo);

//				PdfPCell celdaDerechaSubTitulo = new PdfPCell(
//						new Paragraph(Objects.nonNull(logoOrganismo) ? strConsejeria : "", this.tipoLetra7));
//				PdfPCell celdaDerechaSubTitulo = new PdfPCell();
//				celdaDerechaSubTitulo.setBorder(Rectangle.NO_BORDER);
//				tablaSeccionLogoOrganismo.addCell(celdaDerechaSubTitulo);

//				PdfPCell celdaDerechaTituloInferior = new PdfPCell(new Paragraph(
//						Objects.nonNull(logoOrganismo) ? "Comunidad de Madrid" : "", this.tipoLetra14Bold));
//				PdfPCell celdaDerechaTituloInferior = new PdfPCell();
//				celdaDerechaTituloInferior.setColspan(2);
//				celdaDerechaTituloInferior.setBorder(Rectangle.NO_BORDER);
//				tablaSeccionLogoOrganismo.addCell(celdaDerechaTituloInferior);

				PdfPCell celdaLogoOrganismo = new PdfPCell(tablaSeccionLogoOrganismo);
				celdaLogoOrganismo.setBorder(Rectangle.NO_BORDER);
				celdaLogoOrganismo.setMinimumHeight(60f);  //MD-2433
				tablaLogos.addCell(celdaLogoOrganismo);

				// Agregamos un espacio vacio
//				PdfPCell celdaVacia = new PdfPCell(new Paragraph(""));
//				celdaVacia.setRowspan(2);
//				celdaVacia.setBorder(Rectangle.NO_BORDER);
//				tablaLogos.addCell(celdaVacia);

				// Agregamos los logos europeos en fila (una columna por logo)
				PdfPCell celdaLogoEuropeo = new PdfPCell();
				celdaLogoEuropeo.setHorizontalAlignment(Element.ALIGN_RIGHT);
				celdaLogoEuropeo.setVerticalAlignment(Element.ALIGN_MIDDLE);  //MD-2433
				celdaLogoEuropeo.setBorder(Rectangle.NO_BORDER);
				celdaLogoEuropeo.setRowspan(2);

				//MD-2433: generamos el espacio suficiente para que se muestren los logos europeos
				if (Boolean.TRUE.equals(siFondosEuropeos) && Objects.nonNull(lstLogo) && !lstLogo.isEmpty()) {
					// tabla anidada con N columnas: una por logo, todos en la misma fila
					PdfPTable tablaLogosEU = new PdfPTable(lstLogo.size());
					tablaLogosEU.setWidthPercentage(100f);
					tablaLogosEU.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					tablaLogosEU.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					tablaLogosEU.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					// ancho disponible para la celda europeos: 527 * 4/9 ≈ 234pt
//					float anchoCeldaEuropeos = 234f;
					float anchoCeldaEuropeos = 320f;
//					float maxLogoWidth = (anchoCeldaEuropeos / lstLogo.size()) - 4f;
					float maxLogoWidth = (anchoCeldaEuropeos / lstLogo.size()) - 3f;
//					float maxLogoHeight = 50f;
					float maxLogoHeight = 60f;

					for (Map.Entry<String, byte[]> entry : lstLogo.entrySet()) {
						Image logoUE = Image.getInstance(entry.getValue());
						logoUE.scaleToFit(maxLogoWidth, maxLogoHeight);
						PdfPCell celdaUnLogo = new PdfPCell(logoUE, false);
						celdaUnLogo.setBorder(Rectangle.NO_BORDER);
						celdaUnLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
						celdaUnLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
						tablaLogosEU.addCell(celdaUnLogo);
					}
					celdaLogoEuropeo.addElement(tablaLogosEU);
				}
				tablaLogos.addCell(celdaLogoEuropeo);
				
				if(Boolean.TRUE.equals(document.getPageNumber() != 1)){
				    margenSuperior= 30;
				}
				
				
				// Agregamos la tabla de logos al documento
				tablaLogos.writeSelectedRows(0, -1, 36, document.getPageSize().getHeight() - margenSuperior,
						writer.getDirectContent());
			}

			if (Boolean.TRUE.equals(mostrarRegistroEntrada) && Boolean.TRUE.equals(document.getPageNumber() == 1)) {

				if (Boolean.TRUE.equals(validaLogoEuropeo)) {
					margenSuperior = 110;
				}else {
				    margenSuperior = 50; 
				}

				PdfPTable table2 = new PdfPTable(5);

				table2.setWidths(new float[] { 1f, 4f, 0.25f, 4f, 1f });
				table2.setTotalWidth(527);
				table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				table2.getDefaultCell().setPadding(0);
				table2.getDefaultCell().setPaddingBottom(20);

				PdfPCell cell = new PdfPCell(new Phrase(""));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(5);
				table2.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				table2.addCell(cell);
				
				PdfPTable tablaRegistro=this.tableRegistro();
				cell = new PdfPCell(tablaRegistro);
				cell.setCellEvent(new StraightCellEventWhite());
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setPadding(5);
				cell.setBackgroundColor(BaseColor.WHITE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setColspan(3);
				table2.addCell(cell);
				
				float heigh= document.getPageSize().getHeight() - margenSuperior;
				log.info("tablaRegistro heigh documento:"+heigh);
				table2.writeSelectedRows(0, -1, 36, heigh,writer.getDirectContent());
				
			}
		} catch (DocumentException | IOException de) {
		    log.info(" Error durante la inserción del logo en el impreso "+de.getMessage());
			throw new ExceptionConverter(de);
		    
		}
	}
	
//	private void agregarCabeceraPaginaPlantilla(PdfWriter writer, Document document, String strConsejeria,
//            String strDireccion, byte[] logoOrganismo, Map<String, byte[]> lstLogo, Boolean mostrarRegistroEntrada,
//            Boolean siFondosEuropeos) {
//
//        try {
//
//            int margenSuperior = 50;
//
//            Boolean validaLogoEuropeo = Objects.nonNull(lstLogo) && Boolean.FALSE.equals(lstLogo.isEmpty());
//
//            if (Boolean.TRUE.equals(validaLogoEuropeo) || Objects.nonNull(logoOrganismo)) {
//
//                // Creamos el espacio para agregar los logos
//                PdfPTable tablaLogos = new PdfPTable(3);
//                tablaLogos.setWidths(new float[] { 4f, 1f, 4f });
//                tablaLogos.setTotalWidth(527);
//                tablaLogos.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//                tablaLogos.getDefaultCell().setPadding(0);
//
//                // Agregamos el logo de organismo
//                PdfPTable tablaSeccionLogoOrganismo = new PdfPTable(2);
//                tablaSeccionLogoOrganismo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//                tablaSeccionLogoOrganismo.setWidths(new float[] { 1f, 4f });
//
//                PdfPCell celdaIzquierda = new PdfPCell();
//
//                if (Objects.nonNull(logoOrganismo)) {
//                    try {
//                        Image logoComunidad = Image.getInstance(logoOrganismo);
//                        logoComunidad.scaleAbsolute(175, 50);
//                        celdaIzquierda = new PdfPCell(logoComunidad);
//                    } catch (Exception e) {
//                        // VMM 10/08/26: si el logo no se puede decodificar (p.ej. al repetirse en una segunda
//                        // pagina del mismo documento) no bloqueamos la generacion de todo el PDF por
//                        // una imagen; se deja esa pagina sin logo y se continua
//                        log.error("No se pudo cargar el logo del organismo en la cabecera de pagina: "
//                                + e.getMessage());
//                    }
//                }
//                celdaIzquierda.setRowspan(2);
//                celdaIzquierda.setBorder(Rectangle.NO_BORDER);
//                tablaSeccionLogoOrganismo.addCell(celdaIzquierda);
//
////              PdfPCell celdaDerechaTitulo = new PdfPCell(
////                      new Paragraph(Objects.nonNull(logoOrganismo) ? strDireccion : "", this.tipoLetra8));
//                PdfPCell celdaDerechaTitulo = new PdfPCell();
//                celdaDerechaTitulo.setBorder(Rectangle.NO_BORDER);
//                tablaSeccionLogoOrganismo.addCell(celdaDerechaTitulo);
//
////              PdfPCell celdaDerechaSubTitulo = new PdfPCell(
////                      new Paragraph(Objects.nonNull(logoOrganismo) ? strConsejeria : "", this.tipoLetra7));
//                PdfPCell celdaDerechaSubTitulo = new PdfPCell();
//                celdaDerechaSubTitulo.setBorder(Rectangle.NO_BORDER);
//                tablaSeccionLogoOrganismo.addCell(celdaDerechaSubTitulo);
//
////              PdfPCell celdaDerechaTituloInferior = new PdfPCell(new Paragraph(
////                      Objects.nonNull(logoOrganismo) ? "Comunidad de Madrid" : "", this.tipoLetra14Bold));
//                PdfPCell celdaDerechaTituloInferior = new PdfPCell();
//                celdaDerechaTituloInferior.setColspan(2);
//                celdaDerechaTituloInferior.setBorder(Rectangle.NO_BORDER);
//                tablaSeccionLogoOrganismo.addCell(celdaDerechaTituloInferior);
//
//                PdfPCell celdaLogoOrganismo = new PdfPCell(tablaSeccionLogoOrganismo);
//                celdaLogoOrganismo.setBorder(Rectangle.NO_BORDER);
//                celdaLogoOrganismo.setMinimumHeight(60f);  //MD-2433
//                tablaLogos.addCell(celdaLogoOrganismo);
//
//                // Agregamos un espacio vacio
//                PdfPCell celdaVacia = new PdfPCell(new Paragraph(""));
//                celdaVacia.setRowspan(2);
//                celdaVacia.setBorder(Rectangle.NO_BORDER);
//                tablaLogos.addCell(celdaVacia);
//
//                // Agregamos los logos europeos en fila (una columna por logo)
//                PdfPCell celdaLogoEuropeo = new PdfPCell();
//                celdaLogoEuropeo.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                celdaLogoEuropeo.setVerticalAlignment(Element.ALIGN_MIDDLE);  //MD-2433
//                celdaLogoEuropeo.setBorder(Rectangle.NO_BORDER);
//                celdaLogoEuropeo.setRowspan(2);
//
//                //MD-2433: generamos el espacio suficiente para que se muestren los logos europeos
//                if (Boolean.TRUE.equals(siFondosEuropeos) && Objects.nonNull(lstLogo) && !lstLogo.isEmpty()) {
//                    // tabla anidada con N columnas: una por logo, todos en la misma fila
//                    PdfPTable tablaLogosEU = new PdfPTable(lstLogo.size());
//                    tablaLogosEU.setWidthPercentage(100f);
//                    tablaLogosEU.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//                    tablaLogosEU.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    tablaLogosEU.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    // ancho disponible para la celda europeos: 527 * 4/9 ≈ 234pt
//                    float anchoCeldaEuropeos = 234f;
//                    float maxLogoWidth = (anchoCeldaEuropeos / lstLogo.size()) - 4f;
//                    float maxLogoHeight = 50f;
//
//                    for (Map.Entry<String, byte[]> entry : lstLogo.entrySet()) {
//                        Image logoUE = Image.getInstance(entry.getValue());
//                        logoUE.scaleToFit(maxLogoWidth, maxLogoHeight);
//                        PdfPCell celdaUnLogo = new PdfPCell(logoUE, false);
//                        celdaUnLogo.setBorder(Rectangle.NO_BORDER);
//                        celdaUnLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        celdaUnLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                        tablaLogosEU.addCell(celdaUnLogo);
//                    }
//                    celdaLogoEuropeo.addElement(tablaLogosEU);
//                }
//                tablaLogos.addCell(celdaLogoEuropeo);
//                
//                if(Boolean.TRUE.equals(document.getPageNumber() != 1)){
//                    margenSuperior= 30;
//                }
//                
//                
//                // Agregamos la tabla de logos al documento
//                tablaLogos.writeSelectedRows(0, -1, 36, document.getPageSize().getHeight() - margenSuperior,
//                        writer.getDirectContent());
//            }
//
//            if (Boolean.TRUE.equals(mostrarRegistroEntrada) && Boolean.TRUE.equals(document.getPageNumber() == 1)) {
//
//                if (Boolean.TRUE.equals(validaLogoEuropeo)) {
//                    margenSuperior = 110;
//                }else {
//                    margenSuperior = 50; 
//                }
//
//                PdfPTable table2 = new PdfPTable(5);
//
//                table2.setWidths(new float[] { 1f, 4f, 0.25f, 4f, 1f });
//                table2.setTotalWidth(527);
//                table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//                table2.getDefaultCell().setPadding(0);
//                table2.getDefaultCell().setPaddingBottom(20);
//
//                PdfPCell cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(5);
//                table2.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(2);
//                table2.addCell(cell);
//                
//                PdfPTable tablaRegistro=this.tableRegistro();
//                cell = new PdfPCell(tablaRegistro);
//                cell.setCellEvent(new StraightCellEventWhite());
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setPadding(5);
//                cell.setBackgroundColor(BaseColor.WHITE);
//                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                cell.setColspan(3);
//                table2.addCell(cell);
//                
//                float heigh= document.getPageSize().getHeight() - margenSuperior;
//                log.info("tablaRegistro heigh documento:"+heigh);
//                table2.writeSelectedRows(0, -1, 36, heigh,writer.getDirectContent());
//                
//            }
//        } catch (DocumentException | IOException de) {
//            log.info(" Error durante la inserción del logo en el impreso "+de.getMessage());
//            throw new ExceptionConverter(de);
//            
//        }
//    }
	
	/**
	 * Método para devolver tabla registro de entrada
	 */
	private PdfPTable tableRegistro() {
		PdfPTable table = new PdfPTable(4);
		PdfPCell cell = new PdfPCell(new Phrase(""));

		try {
			table.setWidths(new float[] { 4f, 4f, 2f, 4f });
			table.setTotalWidth(300);
			table.getDefaultCell().setBorder(Rectangle.BOX);
			table.getDefaultCell().setPadding(0);

			cell = new PdfPCell(new Phrase("REGISTRO DE ENTRADA", this.tipoLetra11Bold));
			cell.setColspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorder(Rectangle.BOX);
			cell.setBackgroundColor(BaseColor.WHITE);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Ref: ", tipoLetra8BOLD));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(""));
			if (Objects.nonNull(this.mapaDatosEntrada) && Objects.nonNull(this.mapaDatosEntrada.get("codigo"))) {
				cell = new PdfPCell(new Phrase(this.mapaDatosEntrada.get("codigo"), tipoFuente8));
			} else {
				cell.setCellEvent(new InlineCellEvent("cdNumRefDocum", BaseField.EDIT));
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingRight(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Fecha: ", tipoLetra8BOLD));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingLeft(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(""));
			if (Objects.nonNull(this.mapaDatosEntrada) && Objects.nonNull(this.mapaDatosEntrada.get("fecha"))) {
				cell = new PdfPCell(new Phrase(this.mapaDatosEntrada.get("fecha"), tipoFuente8));
			} else {
				cell.setCellEvent(new InlineCellEvent("fcRegistro", BaseField.EDIT));
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingRight(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Destino: ", tipoLetra8BOLD));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(10);
			cell.setColspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			
			String fase="";
			if (Objects.nonNull(this.mapaDatosEntrada) && Objects.nonNull(this.mapaDatosEntrada.get("destino"))) {
			    fase=this.mapaDatosEntrada.get("destino");
			    if(Objects.nonNull(fase)) {
			        cell = new PdfPCell(new Phrase(fase, tipoFuente8));
			    }else {
			        cell = new PdfPCell(new Phrase(""));
			    }
				
			} else {
			    cell = new PdfPCell(new Phrase(""));
				cell.setCellEvent(new InlineCellEvent("dsDestino", BaseField.EDIT));
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingRight(2);
			cell.setPaddingBottom(10);
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

		} catch (Exception de) {
		    throw new ExceptionConverter(de);
		}

		return table;
	}

	/**
	 * Método para agregar pie de página al documento
	 *
	 * @param writer   Escritura de documento
	 * @param document Documento PDF
	 */
	private void agregarPiePagina(PdfWriter writer) {

		PdfPTable table = new PdfPTable(3);
		try {
			table.setWidths(new int[] { 4, 28, 10 });
			table.setTotalWidth(527);
			table.getDefaultCell().setFixedHeight(20);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Phrase(String.format("Página %d de ", writer.getPageNumber()), this.tipoLetra8));

			PdfPCell cell = new PdfPCell(this.total);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			if (this.impresoDTO != null) {
				PdfPCell cellModelo = new PdfPCell(
						new Phrase("Modelo: " + this.impresoDTO.getCdIdentificacion(), this.tipoLetra8));
				cellModelo.setBorder(Rectangle.NO_BORDER);
				cellModelo.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cellModelo);
			}

			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			table.writeSelectedRows(0, -1, 36, 31, canvas);
			canvas.endMarkedContentSequence();
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

}
