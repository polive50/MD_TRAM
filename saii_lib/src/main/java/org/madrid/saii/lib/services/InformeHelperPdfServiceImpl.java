package org.madrid.saii.lib.services;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Constantes.FormularioPdf;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.lib.util.pdf.CheckboxCellEvent;
import org.madrid.saii.lib.util.pdf.CheckboxCheckedCellEvent;
import org.madrid.saii.lib.util.pdf.InlineCellEvent;
import org.madrid.saii.lib.util.pdf.RadioCellEvent;
import org.madrid.saii.lib.util.pdf.RadioCheckedCellEvent;
import org.madrid.saii.lib.util.pdf.SelectCellEvent;
import org.madrid.saii.lib.util.pdf.TextFieldCellEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Service("informeHelperPdfService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class InformeHelperPdfServiceImpl implements InformeHelperPdfService {

    /**
     * Tipo de letra de tamaño 7 cursiva
     */
//    static Font tipoLetra7Italic = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);
    static Font tipoLetra7Italic = new Font(createCustomFont(), 7, Font.ITALIC);

    /**
     * Tipo de letra de tamaño 8
     */
//    static Font tipoLetra8 = new Font(FontFamily.HELVETICA, 8);
    static Font tipoLetra8 = new Font(createCustomFont(), 8);

    /**
     * Tipo de letra en negrita de tamaño 8
     */
//    static Font tipoLetra8Bold = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
    static Font tipoLetra8Bold = new Font(createCustomFont(), 8, Font.BOLD);

    /**
     * Tipo de letra de tamaño 9
     */
//    static Font tipoLetra9 = new Font(FontFamily.HELVETICA, 9);
    static Font tipoLetra9 = new Font(createCustomFont(), 9);

    /**
     * Tipo de letra en negrita de tamaño 9
     */
//    static Font tipoLetra9Bold = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
    static Font tipoLetra9Bold = new Font(createCustomFont(), 9, Font.BOLD);

    /**
     * Tipo de letra de tamaño 10
     */
//    static Font tipoLetra10 = new Font(FontFamily.HELVETICA, 10);
    static Font tipoLetra10 = new Font(createCustomFont(), 10);

    /**
     * Tipo de letra en negrita de tamaño 10
     */
//    static Font tipoLetra10Bold = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
    static Font tipoLetra10Bold = new Font(createCustomFont(), 10, Font.BOLD);

    /**
     * Tipo de letra subrayada de tamaño 10
     */
    static Font tipoLetra10Underline = new Font(createCustomFont(), 10, Font.UNDERLINE);

    /**
     * Tipo de letra en negrita de tamaño 11
     */
//    static Font tipoLetra11Bold = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
    static Font tipoLetra11Bold = new Font(createCustomFont(), 11, Font.BOLD);

    /**
     * Tipo de letra en negrita de tamaño 16
     */
//    static Font tipoLetra16Bold = new Font(FontFamily.HELVETICA, 16, Font.BOLD);
    static Font tipoLetra16Bold = new Font(createCustomFont(), 16, Font.BOLD);

    /**
     * Alias de configuración adicional para mostrar etiqueta junto al
     * componente
     */
    static String aliasMostrarEtiquetaCheck = "mostrarEtiquetaCheck";

    /**
     * Alias de configuración adicional para mostrar en una columna el
     * componente
     */
    static String aliasMostrarUnaColumna = "mostrarUnaColumna";
    
    /**
     * Alias de configuración adicional para mostrar en una columna el
     * componente
     */
    static String aliasMostrarBorde = "mostrarBorde";
    
    /**
     * Servicio CatalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
    * Servicio de financion
    */
    @Lazy
    @Autowired
    @Qualifier("financiacionService")
    private FinanciacionService financiacionService;

    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
    
    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Lazy
    @Autowired
    @Qualifier("pdfCamposService")
    private PdfCamposService pdfCamposService;

    @Lazy
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;

    /**
     * Método para crear el titulo del informe
     *
     * @param document Documento PDF
     * @param tituloSup título superior
     * @param tituloInf título inferior
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTitulo(Document document, String tituloSup, String tituloInf,String desImpreso,String cdExpediente,
            Boolean agregarEspacioLogos, Boolean mostrarRegistroEntrada) throws DocumentException {

        PdfPTable tablaTitulo = new PdfPTable(1);
        tablaTitulo.setWidths(new int[] {1 });
        tablaTitulo.setWidthPercentage(100);
        tablaTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTitulo.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        if (Boolean.TRUE.equals(agregarEspacioLogos)) {
            tablaTitulo.addCell(new Phrase("\n"));
            tablaTitulo.addCell(new Phrase("\n"));
            
        }

        if (Boolean.TRUE.equals(mostrarRegistroEntrada)) {
            tablaTitulo.addCell(new Phrase("\n"));
            tablaTitulo.addCell(new Phrase("\n"));
            tablaTitulo.addCell(new Phrase("\n"));
            
        }

        PdfPTable table = new PdfPTable(new float[] { 1, 99});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
        celdaTituloSuperior.setBorder(Rectangle.NO_BORDER);
        celdaTituloSuperior.setBackgroundColor(BaseColor.RED);
       // celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        table.addCell(celdaTituloSuperior);
        
        
        celdaTituloSuperior = new PdfPCell(new Phrase(tituloInf, tipoLetra10Bold));
        celdaTituloSuperior.setBorder(Rectangle.NO_BORDER);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        table.addCell(celdaTituloSuperior);
        tablaTitulo.addCell(table);
        
        tablaTitulo.addCell(new Phrase("\n"));

        PdfPTable tabla2 = new PdfPTable(1);

        tabla2.setWidthPercentage(100);
        tabla2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        
       
        
        if (Objects.nonNull(desImpreso)) {
            PdfPCell celdaTituloDesImpreso = new PdfPCell(new Phrase(desImpreso, tipoLetra9));
            celdaTituloDesImpreso.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaTituloDesImpreso.setBorder(Rectangle.NO_BORDER);
            celdaTituloDesImpreso.setPadding(10);
            tabla2.addCell(celdaTituloDesImpreso);
        }
        tablaTitulo.addCell(tabla2);
        
        tablaTitulo.addCell(new Phrase("\n"));

        tabla2 = new PdfPTable(new float[] { 50, 50});
        
        if (Objects.nonNull(cdExpediente)
                && cdExpediente.equals(Constantes.Evaluacion.TEXTO_SIN_CD_EXPEDIENTE)) {
            tabla2 = new PdfPTable(new float[] {50 });
        }

        tabla2.setWidthPercentage(100);
        tabla2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        
       
        
        if (Objects.nonNull(cdExpediente)) {
            if (!cdExpediente.equals(Constantes.Evaluacion.TEXTO_SIN_CD_EXPEDIENTE)) {
                PdfPCell celdaTituloCdExpediente = new PdfPCell(
                        new Phrase(cdExpediente, tipoLetra9));
            celdaTituloCdExpediente.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaTituloCdExpediente.setBorder(Rectangle.NO_BORDER);
            celdaTituloCdExpediente.setPadding(10);
            tabla2.addCell(celdaTituloCdExpediente);
            }
        }else {
            PdfPCell celdaTituloCdExpediente= new PdfPCell(new Phrase(cdExpediente, tipoLetra9));
            celdaTituloCdExpediente.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaTituloCdExpediente.setBorder(Rectangle.NO_BORDER);
            celdaTituloCdExpediente.setPadding(10);
            tabla2.addCell(" ");
        }
        
        if (Objects.nonNull(tituloSup)) {
            PdfPCell celdaTituloInferior = new PdfPCell(new Phrase(tituloSup, tipoLetra11Bold));
            if (Objects.nonNull(cdExpediente)
                    && cdExpediente.equals(Constantes.Evaluacion.TEXTO_SIN_CD_EXPEDIENTE)) {
                celdaTituloInferior.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
            celdaTituloInferior.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            celdaTituloInferior.setBorder(Rectangle.NO_BORDER);
            celdaTituloInferior.setPadding(10);
            tabla2.addCell(celdaTituloInferior);
        }

        tablaTitulo.addCell(tabla2);
        document.add(tablaTitulo);
    }

    /**
     * Método para crear el bloque asociado al impreso
     *
     * @param writer Escritor PDF
     * @param document Documento PDF
     * @param bloque bloque asociados al informe
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    public void agregarBloque(PdfWriter writer, Document document, BloqueDTO bloque,
            ConvocatoriaDTO convocatoriaDTO, Map<String, String> mapaDatos,ExpedienteDTO dto,
            Integer idTipoImpreso)
            throws DocumentException, ServiceException {

        agregarTituloBloque(document, bloque);

        Integer idFormatoBloque = bloque.getIdFormato().getIdValor();
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO).equals(idFormatoBloque)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);           
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_NOTIFICACION).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_PERSONA_SOLICITANTE).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);  
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO).equals(idFormatoBloque) 
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_CUENTA_BANCARIA).equals(idFormatoBloque)) {
            agregarBloqueListado(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA).equals(idFormatoBloque)) {
            agregarBloqueTabla(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO).equals(idFormatoBloque) ) {
            agregarBloqueTexto(document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO_FIJO).equals(idFormatoBloque)) {
            agregarBloqueTextoFijo(document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO).equals(idFormatoBloque)) {
            agregarBloqueDocumento(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PARTICIPANTES).equals(idFormatoBloque)) {
            // Los bloques participante se añaden aparte
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO).equals(idFormatoBloque) && convocatoriaDTO.getItConceptoPresup()!=null
                && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S") ) {
            Boolean mostrarAyuda= Boolean.TRUE;
            Integer idActor=0;
            if(dto.getConvocatoriaActDTO() !=null && dto.getConvocatoriaActDTO().getIdActor() != null ) {
                idActor=dto.getConvocatoriaActDTO().getIdActor();
            }
            FinanciacionDTO financiacionDTO= obtenerFinanciacion("PP",convocatoriaDTO.getIdConvocatoria(),bloque.getIdTematica().getIdValor(),idActor);
            
            if(financiacionDTO!= null && financiacionDTO.getItMostrarAyuda() != null && financiacionDTO.getItMostrarAyuda().equalsIgnoreCase("N")) {
                mostrarAyuda = Boolean.FALSE;
            }
            
           /* if(financiacionDTO!= null && !financiacionDTO.getListaFinanciacionDatoDTO().isEmpty()) {
                listaFinanciacionDatos=financiacionDTO.getListaFinanciacionDatoDTO();
            }*/
            
            agregarBloqueTablaPresupuestoConcepto(document, bloque, convocatoriaDTO.getNmDurMaxAyuda(),convocatoriaDTO.getPorcentajeIntensidadAyuda(), true,mostrarAyuda,financiacionDTO,idTipoImpreso);
        }else if(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO).equals(idFormatoBloque)) {
            
            Boolean mostrarAyuda= Boolean.TRUE;
            Integer idActor=0;
            if(dto.getConvocatoriaActDTO() !=null && dto.getConvocatoriaActDTO().getIdActor() != null ) {
                idActor=dto.getConvocatoriaActDTO().getIdActor();
            }
            FinanciacionDTO financiacionDTO= obtenerFinanciacion("PP",convocatoriaDTO.getIdConvocatoria(),bloque.getIdTematica().getIdValor(),idActor);
            if(financiacionDTO!= null && financiacionDTO.getItMostrarAyuda() != null && financiacionDTO.getItMostrarAyuda().equalsIgnoreCase("N")) {
                mostrarAyuda = Boolean.FALSE;
            }
            agregarBloqueTablaPresupuesto(document, bloque, convocatoriaDTO.getNmDurMaxAyuda(),convocatoriaDTO.getPorcentajeIntensidadAyuda(), true, mostrarAyuda,financiacionDTO);
            
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA).equals(idFormatoBloque)) {
            agregarBloqueTablaAyuda(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES).equals(idFormatoBloque)) {
            agregarBloqueTablaActuacion(writer, document, bloque, mapaDatos);
        //incluimos nuevos bloques para actas de comites de gestion
        
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE).equals(idFormatoBloque) && convocatoriaDTO.getItConceptoPresup()!=null
                && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S") ) {
            Boolean mostrarAyuda= Boolean.TRUE;
            Integer idActor=0;
           
            if(dto.getConvocatoriaActDTO() !=null && dto.getConvocatoriaActDTO().getIdActor() != null ) {
                idActor=dto.getConvocatoriaActDTO().getIdActor();
            }
            FinanciacionDTO financiacionDTO= obtenerFinanciacion("PP",convocatoriaDTO.getIdConvocatoria(),bloque.getIdTematica().getIdValor(),idActor);
            
            if(financiacionDTO!= null && financiacionDTO.getItMostrarAyuda() != null && financiacionDTO.getItMostrarAyuda().equalsIgnoreCase("N")) {
                mostrarAyuda = Boolean.FALSE;
            }
            
            
            agregarBloqueTablaPresupuestoConcepto(document, bloque, convocatoriaDTO.getNmDurMaxAyuda(),convocatoriaDTO.getPorcentajeIntensidadAyuda(), true,mostrarAyuda,financiacionDTO,idTipoImpreso);

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE).equals(idFormatoBloque)) {
            for(BloqueComponenteDTO bloqCom:bloque.getLstBloqueComponenteDTO()) {
                List<DatoDTO> datosBloq= bloqCom.getDatos().stream().filter(x -> 
                x.getExpedienteModifPresPlazoOtraDTO() != null 
                && dto.getExpedienteModifOtraDTO()!= null
                && dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO() != null
                
                && x.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra()
                .equals(dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra())).collect(Collectors.toList()); 
                bloqCom.setDatos(datosBloq); 
            }
            
            Boolean mostrarAyuda= Boolean.TRUE;
            Integer idActor=0;
            if(dto.getConvocatoriaActDTO() !=null && dto.getConvocatoriaActDTO().getIdActor() != null ) {
                idActor=dto.getConvocatoriaActDTO().getIdActor();
            }
            FinanciacionDTO financiacionDTO= obtenerFinanciacion("PP",convocatoriaDTO.getIdConvocatoria(),bloque.getIdTematica().getIdValor(),idActor);
            if(financiacionDTO!= null && financiacionDTO.getItMostrarAyuda() != null && financiacionDTO.getItMostrarAyuda().equalsIgnoreCase("N")) {
                mostrarAyuda = Boolean.FALSE;
            }
            
            agregarBloqueTablaPresupuestoFinan(document, bloque, convocatoriaDTO.getNmDurMaxAyuda(),convocatoriaDTO.getPorcentajeIntensidadAyuda(), true, mostrarAyuda,financiacionDTO);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL).equals(idFormatoBloque)) {
            for(BloqueComponenteDTO bloqCom:bloque.getLstBloqueComponenteDTO()) {
                List<DatoDTO> datosBloq= bloqCom.getDatos().stream().filter(x -> 
                x.getExpedienteModifPresPlazoOtraDTO() != null 
                && dto.getExpedienteModifOtraDTO()!= null
                && dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO() != null
                
                && x.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra()
                .equals(dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra())).collect(Collectors.toList()); 
                bloqCom.setDatos(datosBloq); 
            }
            agregarBloquePlazosEjecucionAnualidades(document,bloque, convocatoriaDTO.getNmDurMaxAyuda());
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PERSONAL_SIN_CARGO).equals(idFormatoBloque)) {
            agregarBloqueTablaPersonalActas(document, false);
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PERSONAL_CON_CARGO).equals(idFormatoBloque)) {
            agregarBloqueTablaPersonalActas(document, true);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DISCAPACIDAD).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD).equals(idFormatoBloque)) {
            agregarBloqueFormulario(writer, document, bloque);
        } else {
            log.error("Tipo de bloque no contemplado");
        }

        agregarBloqueTextoAuxiliarInferior(document, bloque);
    }

    public FinanciacionDTO obtenerFinanciacion(String itTipoFinanciacion,
            Integer idConvocatoria, Integer idTematica,Integer idActor) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FINANCIACION_IT_TIPO,
                itTipoFinanciacion, Operador.EQUALS));
        if (Objects.nonNull(idTematica) && idTematica != 0) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TEMATICA, idTematica));
        }
        
        if (Objects.nonNull(idActor) && idActor != 0) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ACTOR, idActor));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        FinanciacionDTO financiacionDTO = new FinanciacionDTO();

        List<FinanciacionDTO> listaFinanciacionDTO = this.financiacionService.buscar(0, -1, filtros,
                null);

        if (!listaFinanciacionDTO.isEmpty()) {
            financiacionDTO = listaFinanciacionDTO.get(0);
        }

        return financiacionDTO;
    }
    
    private void agregarBloqueTablaPersonalActas(Document document,
            boolean conCargo) throws ServiceException {
        try {
            if (conCargo)
                agregarDescSubTabla(document, "Personal con cargo al proyecto actual");
            else
                agregarDescSubTabla(document, "Personal sin cargo al proyecto actual");
            
            //Tabla del personal con o sin cargo al proyecto inicial (no editable)
            agregarBloqueTablaPersonal(document, false, conCargo);
            
            agregarFilaVacio(document, 1);
            
            if (conCargo)
                agregarDescSubTabla(document, "Personal con cargo al proyecto a modificar");
            else
                agregarDescSubTabla(document, "Personal sin cargo al proyecto a modificar");
            
            //Tabla del personal con o sin cargo al proyecto a modificar (editable)
            agregarBloqueTablaPersonal(document, true, conCargo);            

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }        
        
    }

    private void agregarBloqueTablaPresupuestoFinan(Document document,
            BloqueDTO bloque, Integer nmDurMaxAyuda,Double intensidad, boolean esEditable,  Boolean mostrarAyuda,FinanciacionDTO financiacionDTO ) throws ServiceException {
        try {
            
            //agregarDescSubTabla(document, "Presupuesto financiable Modificado");
            
            //Tabla del presupuesto financiable o concedido inicial (no editable)
           // agregarBloqueTablaPresupFinanciacion(document, dto, false);
            
            agregarBloqueTablaPresupuesto(document, bloque, nmDurMaxAyuda,intensidad, true,mostrarAyuda,financiacionDTO);
            agregarFilaVacio(document, 1);
                     

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }        
    }
    
    private void agregarBloquePlazosEjecucionAnualidades(Document document,  BloqueDTO bloque,
            Integer nmDurMaxAyuda) throws ServiceException {
        try {
            
           // agregarDescSubTabla(document, "Plazos de ejecución de anualidades iniciales");
            
            //Tabla de plazos de ejecución de anualidades inicial (no editable)
           // agregarBloquePlazosEjecucionAnualidadesSubTabla(document,bloque, nmDurMaxAyuda, false);
            
           // agregarFilaVacio(document, 1);
            
          //  agregarDescSubTabla(document, "Plazos de ejecución de anualidades a modificar");
            
            //Tabla de plazos de ejecución de anualidades a modificar (editable)
            agregarBloquePlazosEjecucionAnualidadesSubTabla(document,bloque, nmDurMaxAyuda, true);            

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }        
    }

    /**
     * @param document
     * @param descripcion
     * 
     * @throws DocumentException
     */
    private void agregarDescSubTabla(Document document, String descripcion) throws DocumentException {
                
        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.addCell(new Phrase(descripcion, tipoLetra9));
        document.add(table);
        
        agregarFilaVacio(document, 1);
    }

    private void agregarBloqueTablaPersonal(Document document,
            boolean esEditable, boolean conCargo) throws ServiceException {
        try {
            //ponemos 5 filas de momento por defecto aunque solo se supone que puede haber dos participantes
            Integer numFilas = 5;
            Integer numColumnas = 0;
  
            //ponemos 5 columnas para personal con cargo y 3 columnas para personal sin cargo
            if (conCargo) numColumnas = 5;
            else numColumnas = 3;
               
            // creacion de la tabla
            PdfPTable table = new PdfPTable(numColumnas);          
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);

            // Creación de la cabecera
            table.addCell(celdaEtiquetaCabecera("Nombre", true));
            table.addCell(celdaEtiquetaCabecera("Apellidos", true));
            table.addCell(celdaEtiquetaCabecera("NIF/NIE", true));
            if (conCargo) {
                table.addCell(celdaEtiquetaCabecera("Titulación", true));
                table.addCell(celdaEtiquetaCabecera("Contrato", true));
            }
   
            // creacion de cuerpo
            String nombreCampo = null;
            // cada fila serán los datos de los actores participantes con o sin cargo que tenga la convocatoria
            for (int indexFila=1; indexFila <= numFilas; indexFila++) {
                //Nombre celda
                nombreCampo = "Nombre_Celda_" + indexFila;                    
                addCampoCelda(esEditable, table, nombreCampo);
                
                //Apellidos celda
                nombreCampo = "Apellidos_Celda_" + indexFila;                   
                addCampoCelda(esEditable, table, nombreCampo);
                
                //DNI celda
                nombreCampo = "NIF_Celda_" + indexFila;                    
                addCampoCelda(esEditable, table, nombreCampo);
                
                if (conCargo) {
                    //Titulación celda
                    nombreCampo = "Titulacion_Celda_" + indexFila;                       
                    addCampoCelda(esEditable, table, nombreCampo);
                    
                    //Contrato celda
                    nombreCampo = "Contrato_Celda_" + indexFila;                      
                    addCampoCelda(esEditable, table, nombreCampo);
                }
            }

            document.add(table);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
        
    }

    /**
     * @param esEditable
     * @param table
     * @param nombreCampo
     */
    private void addCampoCelda(boolean esEditable, PdfPTable table, String nombreCampo) {
        if (esEditable) {
            table.addCell(celdaCampoTexto(nombreCampo));
        }else {
            table.addCell(celdaEtiqueta(" ", true));
        }
    }

    private void addCampoCeldaTextoLargo(boolean esEditable, PdfPTable table, String nombreCampo) {
        if (esEditable) {
            PdfPCell cell = celdaCampoMultiTexto(nombreCampo);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingTop(20);
            cell.setPadding(10);
            table.addCell(cell);
        }else {
            table.addCell(celdaEtiqueta(" ", true));
        }
        
    }
    
    /**
     * Método para crear el titulo y texto auxiliar del bloque
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarTituloBloque(Document document, BloqueDTO bloque)
            throws DocumentException {
        agregarFilaVacio(document, 1);
        String titulo= bloque.getIdTematica().getDsValor();
        
        PdfPTable tableResult = new PdfPTable(1);
        
        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        
        document.add(tableResult);
        
        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(6);
        table.addCell(new Phrase(bloque.getTlAuxiliar(), tipoLetra9));
        document.add(table);
       
    }

    /**
     * Método para Calcular la distribución de componentes del bloque Formulario
     *
     * @param lstBloqueComponenteDTO
     */
    private List<Integer> calcularDistribucionBloqueFormulario(
            List<BloqueComponenteDTO> lstBloqueComponenteDTO) {

        List<Integer> lstDistribucion = new ArrayList<>();

        Float sobraAnterior = 100f;
        int iContador = 0;
        for (int i = 0; i < lstBloqueComponenteDTO.size(); i++) {

            ComponenteDTO componenteDTO = lstBloqueComponenteDTO.get(i).getComponenteDTO();

            if (esCampoTextoGrande(componenteDTO)) {
                // Un campo de texto/multitexto largo (etiqueta y/o valor extensos) debe
                // ocupar siempre toda la fila, aunque su porcentaje configurado permita
                // compartir fila con otros campos, para que no se deforme la celda del valor.
                if (iContador > 0) {
                    lstDistribucion.add(iContador);
                }
                lstDistribucion.add(1);
                iContador = 0;
                sobraAnterior = 100f;
                continue;
            }

            Float ocupa = componenteDTO.getLngPorcentajeEtiquetaPDF()
                    + componenteDTO.getLngPorcentajeCampoPDF();

//            if (ocupa < sobraAnterior) {
            if (ocupa <= sobraAnterior) {
                iContador++;
                sobraAnterior = sobraAnterior - ocupa;
            } else {
//                lstDistribucion.add(iContador);
                if (iContador > 0) {
                    lstDistribucion.add(iContador);
                }
                iContador = 1;
                sobraAnterior = 100f - ocupa;
            }
            // Si es el ulitmo añadimos.
//            if (lstBloqueComponenteDTO.size() - 1 == i) {
            if (lstBloqueComponenteDTO.size() - 1 == i && iContador > 0) {
                lstDistribucion.add(iContador);
            }

        }

        return lstDistribucion;
    }

    /**
     * Indica si un componente de tipo campo es un texto/multitexto extenso, y por
     * tanto debe ocupar toda la fila del formulario en el PDF en lugar de
     * compartirla con otros campos según los porcentajes configurados.
     *
     * @param componenteDTO Datos del componente
     * @return true si es un campo de texto largo (multitexto, o texto con
     *         longitud configurada superior a 160 caracteres)
     */
    private boolean esCampoTextoGrande(ComponenteDTO componenteDTO) {
        if (componenteDTO.getSubTipoDTO() == null || componenteDTO.getSubTipoDTO().getIdValor() == null) {
            return false;
        }
        Integer idSubTipoComponente = componenteDTO.getSubTipoDTO().getIdValor();

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO)
                .equals(idSubTipoComponente)) {
            return true;
        }
        return utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_TEXTO)
                .equals(idSubTipoComponente)
                && componenteDTO.getNmLongitud() != null && componenteDTO.getNmLongitud() > 120;
    }

    /**
     * Resuelve el sufijo de nombre de campo (nombreFila) a usar en el PDF para un
     * componente del bloque formulario, teniendo en cuenta si está marcado como
     * campo relacionable.
     *
     * @param bloqueComponenteDTO Componente del bloque
     * @param fila Número de fila actual del bloque
     * @return sufijo del nombre de campo (p.ej. "_1" o "_tipoVia")
     * @throws ServiceException Excepción generada al obtener el componente
     *             relacionable
     */
    private String resolverNombreFila(BloqueComponenteDTO bloqueComponenteDTO, int fila)
            throws ServiceException {

        String nombreFila = StringUtils.EMPTY;

        // Si el campo está marcado para relacionar, le cambiamos el nombre
        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItRelacion())) {

            ComponenteDTO componenteRelacionable = componenteService
                    .buscarId(bloqueComponenteDTO.getComponenteDTO().getIdComponente());
            Integer nmConstante = componenteRelacionable != null
                    ? componenteRelacionable.getNmConstante() : null;

            if (Constantes.ComponentesRelacionables.TIPO_VIA.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_TIPO_VIA;
            } else if (Constantes.ComponentesRelacionables.NOMBRE_VIA.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_NOMBRE_VIA;
            } else if (Constantes.ComponentesRelacionables.NUMERO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_NUMERO;
            } else if (Constantes.ComponentesRelacionables.PISO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_PISO;
            } else if (Constantes.ComponentesRelacionables.PUERTA.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_PUERTA;
            } else if (Constantes.ComponentesRelacionables.CP.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_CP;
            } else if (Constantes.ComponentesRelacionables.MUNICIPIO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_MUNICIPIO;
            } else if (Constantes.ComponentesRelacionables.PROVINCIA.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_PROVINCIA;
            } else if (Constantes.ComponentesRelacionables.NIF_NIE.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_NIF_NIE;
            } else if (Constantes.ComponentesRelacionables.RAZON_SOCIAL.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_RAZON_SOCIAL;
            } else if (Constantes.ComponentesRelacionables.NOMBRE.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_NOMBRE;
            } else if (Constantes.ComponentesRelacionables.APELLIDO_1.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_APELLIDO_1;
            } else if (Constantes.ComponentesRelacionables.APELLIDO_2.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_APELLIDO_2;
            } else if (Constantes.ComponentesRelacionables.TELEFONO_FIJO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_TELEFONO_FIJO;
            } else if (Constantes.ComponentesRelacionables.TELEFONO_MOVIL.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_TELEFONO_MOVIL;
            } else if (Constantes.ComponentesRelacionables.CORREO_ELECTRONICO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_CORREO_ELECTRONICO;
            } else if (Constantes.ComponentesRelacionables.CARGO.equals(nmConstante)) {
                nombreFila = "_" + Constantes.ComponentesRelacionables.RELACION_CARGO;
            }
        } else {
            // En caso contrario, lo tratamos como un campo normal
            nombreFila = "_" + fila;
        }

        return nombreFila;
    }

    /**
     * Añade al documento la fila de un campo de texto/multitexto grande del
     * bloque formulario, con la etiqueta ocupando toda la fila superior y el
     * valor ocupando toda la fila inferior (a ancho completo), en lugar de
     * colocarlos uno al lado del otro repartiéndose el ancho según los
     * porcentajes configurados del componente. Así se evita que la celda del
     * valor quede deformada cuando la etiqueta y/o el valor son extensos.
     *
     * @param writer Escritor PDF
     * @param document Documento PDF
     * @param bloqueComponenteDTO Componente de texto/multitexto a añadir
     * @param agrupadores Lista de agrupadores de campos del formulario
     * @param configuracionAdicional Configuración adicional para la
     *            construcción del componente
     * @param fila Número de fila actual del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException Excepción generada al obtener datos del
     *             componente
     */
    private void agregarFilaFormularioTextoGrande(PdfWriter writer, Document document,
            BloqueComponenteDTO bloqueComponenteDTO, List<PdfFormField> agrupadores,
            Map<String, Object> configuracionAdicional, int fila)
            throws DocumentException, ServiceException {

        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);

        table.addCell(celdaEtiquetaNegrita(bloqueComponenteDTO.getComponenteDTO().getDsNombre(), true));

        Integer idTipoComponente = bloqueComponenteDTO.getComponenteDTO().getTipoDTO().getIdValor();
        String nombreFila = resolverNombreFila(bloqueComponenteDTO, fila);

        agregarComponente(idTipoComponente, bloqueComponenteDTO, agrupadores, table, writer,
                nombreFila, configuracionAdicional);

        document.add(table);
    }

    /**
     * Método para crear el contenido del bloque tipo formulario
     *
     * @param writer Escritor PDF
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarBloqueFormulario(PdfWriter writer, Document document,
            BloqueDTO bloque) throws DocumentException, ServiceException {

        List<BloqueComponenteDTO> lstBloqueComponenteDTO = obtenerComponentes(bloque);

        Map<String, Object> configuracionAdicional = new HashMap<>();
        configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
        configuracionAdicional.put(aliasMostrarUnaColumna, false);
       
        List<PdfFormField> agrupadores = new ArrayList<>();

        int fila = 1;

        List<Integer> lstDistribucion = calcularDistribucionBloqueFormulario(
                lstBloqueComponenteDTO);

        Integer iContadorBloque = 0;
        Iterator<Integer> itDistribucion = lstDistribucion.iterator();
        while (itDistribucion.hasNext()) {

            Integer iComponentes = itDistribucion.next();

            if (iComponentes == 1 && esCampoTextoGrande(
                    lstBloqueComponenteDTO.get(iContadorBloque).getComponenteDTO())) {
                agregarFilaFormularioTextoGrande(writer, document,
                        lstBloqueComponenteDTO.get(iContadorBloque), agrupadores,
                        configuracionAdicional, fila);
                iContadorBloque++;
                continue;
            }

            Integer iNumeroColumna = iComponentes * 2 + 1;
            float[] arrayFloat = new float[iNumeroColumna];

            int iContadorComponente = 0;
            for (int i = 0; i < iComponentes; i++) {
                arrayFloat[i + iContadorComponente] = lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getLngPorcentajeEtiquetaPDF();
                arrayFloat[i + iContadorComponente + 1] = lstBloqueComponenteDTO
                        .get(iContadorBloque).getComponenteDTO().getLngPorcentajeCampoPDF();
                iContadorBloque++;
                iContadorComponente++;
            }
            iContadorBloque = iContadorBloque - iComponentes;

            // Calcula sobra
            float sobra = 100;
            for (float element : arrayFloat) {
                sobra = sobra - element;
            }
            arrayFloat[arrayFloat.length - 1] = sobra;

            PdfPTable table = new PdfPTable(arrayFloat);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            for (int i = 0; i < iComponentes; i++) {
                
                BloqueComponenteDTO bloqueComponenteDTO = lstBloqueComponenteDTO.get(iContadorBloque);
                
                if ((Constantes.ComponenteSolicitud.CHECK_OPONGO.equals(bloqueComponenteDTO.getComponenteDTO().getNmConstante()) 
                        || Constantes.ComponenteSolicitud.MULTITEXTO_OPONGO.equals(bloqueComponenteDTO.getComponenteDTO().getNmConstante()))
                        && "".equals(lstBloqueComponenteDTO.get(iContadorBloque).getComponenteDTO().getDsNombre())) {
                    arrayFloat = new float[2];
                    arrayFloat[0] = 100f;
                    arrayFloat[1] = 0f;
                    table = new PdfPTable(arrayFloat);
                    table.setWidthPercentage(100);
                    table.getDefaultCell().setBorder(Rectangle.BOX);
                    table.getDefaultCell().setBorderColor(BaseColor.BLACK);
                } else {
                    table.addCell(celdaEtiquetaNegrita(lstBloqueComponenteDTO.get(iContadorBloque)
                            .getComponenteDTO().getDsNombre(), true));
                }
                
                Integer idTipoComponente = lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getTipoDTO().getIdValor();

                String nombreFila = resolverNombreFila(lstBloqueComponenteDTO.get(iContadorBloque), fila);

                agregarComponente(idTipoComponente, lstBloqueComponenteDTO.get(iContadorBloque),
                        agrupadores, table, writer, nombreFila, configuracionAdicional);

                // Si es el ultimo es el sobra
                if (i == iComponentes - 1) {
                    table.addCell(celdaEtiquetaNegrita("", true));
                }

                iContadorBloque++;
            }

            document.add(table);
        }

        for (PdfFormField agrupador : agrupadores) {
            writer.addAnnotation(agrupador);
        }

    }

    /**
     * Método para crear el contenido del bloque tipo texto
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarBloqueTexto(Document document, BloqueDTO bloque)
            throws DocumentException {
        List<BloqueComponenteDTO> lstBloqueCompDTO = bloque.getLstBloqueComponenteDTO();

        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(0);

        for (BloqueComponenteDTO componente : lstBloqueCompDTO) {
            table.addCell(celdaEtiqueta(componente.getComponenteDTO().getDsNombre(), false));
            PdfPCell cellMultiText = celdaCampoMultiTexto(
                    componente.getIdBloqueComponente() + "_1",
                    componente.getComponenteDTO().getNmLongitud());
            cellMultiText.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
            cellMultiText.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED);
            cellMultiText.setPadding(0);
            table.addCell(cellMultiText);
            table.addCell(celdaEtiqueta("", false));

            PdfPTable tableIndoor = new PdfPTable(3); // tabla para el pie
            // de bloque
            tableIndoor.setWidths(new int[] {10, 28, 10 });
            tableIndoor.setWidthPercentage(100);

            tableIndoor.addCell(celdaEtiquetaPie(
                    "Longitud máxima: " + componente.getComponenteDTO().getNmLongitud(), "left"));
            tableIndoor.addCell(celdaEtiquetaPie("", "left"));

            // Se imprime máximo de palabras, si el valor no es nulo ni cero
            if (null != bloque.getNmMaxPalabras()) {
                tableIndoor.addCell(celdaEtiquetaPie(
                        "Máximo de palabras: " + bloque.getNmMaxPalabras(), "right"));
            } else {
                tableIndoor.addCell(celdaEtiquetaPie("", "left"));
            }

            table.addCell(tableIndoor);

        }

        document.add(table);

    }

    /**
     * Método para crear el contenido del bloque tipo texto fijo
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarBloqueTextoFijo(Document document, BloqueDTO bloque)
            throws DocumentException {
        List<BloqueComponenteDTO> lstBloqueCompDTO = bloque.getLstBloqueComponenteDTO();

        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
       // table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        PdfPTable tableRow = new PdfPTable(1);
        tableRow.setWidthPercentage(100);

        for (BloqueComponenteDTO componente : lstBloqueCompDTO) {
            // tableRow.addCell(celdaEtiqueta(componente.getComponenteDTO().getTlTextoFijo(), false));
            // MD-2469 [ADM] Ampliar campo texto fijo en formulario
            tableRow.addCell(celdaEtiqueta(componente.getComponenteDTO().getClTextoFijo(), false));
        }

        table.addCell(tableRow);

        document.add(table);

    }

    /**
     * Método para crear el contenido del bloque tipo listado
     *
     * @param writer Escritor PDF
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    public void agregarBloqueListado(PdfWriter writer, Document document, BloqueDTO bloque)
            throws DocumentException, ServiceException {
        List<BloqueComponenteDTO> componentes = obtenerComponentes(bloque);

        Comparator<BloqueComponenteDTO> compareByNumOrden = (BloqueComponenteDTO c1,
                BloqueComponenteDTO c2) -> c1.getNmOrden().compareTo(c2.getNmOrden());

        if (!componentes.isEmpty()) {

            Collections.sort(componentes, compareByNumOrden);

            PdfPTable table = new PdfPTable(componentes.size());
            table.setWidthPercentage(100);

            for (BloqueComponenteDTO componente : componentes) {
                table.addCell(
                        celdaEtiquetaCabecera(componente.getComponenteDTO().getDsNombre(), true));
            }

            document.add(table);

            int numFilas = bloque.getNmFilas();

            for (int i = 0; i < numFilas; i++) {

                table = new PdfPTable(componentes.size());
                table.setWidthPercentage(100);
                List<PdfFormField> agrupadores = new ArrayList<>();

                for (BloqueComponenteDTO componente : componentes) {
                    String nombreFila = "_" + (i + 1);
                    Integer idTipoComponente = componente.getComponenteDTO().getTipoDTO()
                            .getIdValor();

                    Map<String, Object> configuracionAdicional = new HashMap<>();
                    configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
                    configuracionAdicional.put(aliasMostrarUnaColumna, true);

                    agregarComponente(idTipoComponente, componente, agrupadores, table, writer,
                            nombreFila, configuracionAdicional);

                }
                document.add(table);

                for (PdfFormField agrupador : agrupadores) {
                    writer.addAnnotation(agrupador);
                }
            }

        }

    }

    /**
     * Método para crear el contenido del bloque tipo tabla
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarBloqueTabla(PdfWriter writer, Document document, BloqueDTO bloque)
            throws DocumentException, ServiceException {
        List<BloqueComponenteDTO> componentes = obtenerComponentes(bloque);
        List<BloqueComponenteDTO> columnas = new ArrayList<>();
        List<BloqueComponenteDTO> filas = new ArrayList<>();

        for (BloqueComponenteDTO componente : componentes) {
            if (Boolean.TRUE.equals(componente.getItColumna())) {
                columnas.add(componente);
            } else if (Boolean.TRUE.equals(componente.getItFila())) {
                filas.add(componente);
            }
        }
        Comparator<BloqueComponenteDTO> compareByNumOrden = (BloqueComponenteDTO c1,
                BloqueComponenteDTO c2) -> c1.getNmOrden().compareTo(c2.getNmOrden());
        if (!columnas.isEmpty()) {
            Collections.sort(columnas, compareByNumOrden);
        }
        if (!filas.isEmpty()) {
            Collections.sort(filas, compareByNumOrden);
        }
        PdfPTable table = new PdfPTable(columnas.size() + 1);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.addCell(celdaEtiqueta("", true));
        for (BloqueComponenteDTO columna : columnas) {
            table.addCell(celdaEtiquetaCabecera(columna.getComponenteDTO().getDsNombre(), true));
        }
        document.add(table);
        int contadorFilas = 1;
        for (BloqueComponenteDTO fila : filas) {
            table = new PdfPTable(columnas.size() + 1);
            table.setWidthPercentage(100);
            List<PdfFormField> agrupadores = new ArrayList<>();
            table.addCell(celdaEtiqueta(fila.getComponenteDTO().getDsNombre(), true));
            for (BloqueComponenteDTO columna : columnas) {
                String nombreFila = "_" + contadorFilas;
                Integer idTipoComponente = columna.getComponenteDTO().getTipoDTO().getIdValor();

                Map<String, Object> configuracionAdicional = new HashMap<>();
                configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
                configuracionAdicional.put(aliasMostrarUnaColumna, true);

                agregarComponente(idTipoComponente, columna, agrupadores, table, writer, nombreFila,
                        configuracionAdicional);
            }
            contadorFilas++;
            document.add(table);
            for (PdfFormField agrupador : agrupadores) {
                writer.addAnnotation(agrupador);
            }
        }

    }
    
    
    public int compareByNumOrden(BloqueComponenteDTO c1, BloqueComponenteDTO c2, List<FinanciacionDatoDTO> listaFinancDato) {
        
        // Extraer el numOrden para c1
        if(listaFinancDato != null && !listaFinancDato.isEmpty()) {
            Integer orden1 =listaFinancDato.stream()
                    .filter(e -> e.getTipoDTO().getIdValor().equals(c1.getTipoFinanciacionDTO().getIdValor()))
                    .map(FinanciacionDatoDTO::getNmOrden)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

                // Extraer el numOrden para c2
                Integer orden2 = listaFinancDato.stream()
                    .filter(e -> e.getTipoDTO().getIdValor().equals(c2.getTipoFinanciacionDTO().getIdValor()))
                    .map(FinanciacionDatoDTO::getNmOrden)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

                // Manejo de nulos para evitar NullPointerException en la comparación
                if (orden1 == null && orden2 == null) return 0;
                if (orden1 == null) return 1; // Nulos al final
                if (orden2 == null) return -1;

                return orden1.compareTo(orden2);
        }
        
        return 0;
       
    }

    /**
     * Método para crear el contenido del bloque tipo tabla
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarBloqueTablaPresupuesto(Document document,
            BloqueDTO bloque, Integer nmDurMaxAyuda,Double intensidad, boolean esEditable, Boolean mostrarAyuda,FinanciacionDTO financiacionDTO) throws ServiceException {

        try {
            // Quita los bloqueComponente de baja y ordena
            List<BloqueComponenteDTO> lstBloqueComponentes = obtenerComponentes(bloque);
            List<BloqueComponenteDTO> filas = lstBloqueComponentes.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());
            List<FinanciacionDatoDTO> listaFinancDato= financiacionDTO.getListaFinanciacionDatoDTO();
            
            if(!filas.isEmpty()) {
                filas = filas.stream()
                        .sorted((c1, c2) -> compareByNumOrden(c1, c2, listaFinancDato))
                        .collect(Collectors.toList());

            }
            
            PdfPCell cell = null;

            // creacion de la tabla
            PdfPTable table;
            if(nmDurMaxAyuda== 1) {
                table = new PdfPTable(nmDurMaxAyuda + 1);
            }else {
                table = new PdfPTable(nmDurMaxAyuda + 2);
            }
             
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            // Creación de la cabecera
            cell = celdaEtiquetaCabecera(" ", true);
            table.addCell(cell);

            if(nmDurMaxAyuda== 1) {
                table.addCell(celdaEtiquetaCabecera("IMPORTE", true));
            }else {
                for (int i = 0; i < nmDurMaxAyuda; i++) {
                    Integer id = i + 1;
                    table.addCell(celdaEtiquetaCabecera(id + "° " + "Anualidad", true));
                }

                cell = celdaEtiquetaCabecera("TOTAL", true);
                table.addCell(cell);
            }
            

            // creacion de cuerpo
            int indexFila = 0;
            for (BloqueComponenteDTO fila : filas) {

                table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));

                if(nmDurMaxAyuda== 1) {
                    for (int i = 0; i < nmDurMaxAyuda; i++) {
                        String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 ) + "_" + (i + 1);
                        addCampoCelda(esEditable, table, nombreCampo);
                        
                    }
                }else {
                    for (int i = 0; i <= nmDurMaxAyuda; i++) {
                        String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 )+ "_" + (i + 1);
                        if (i == nmDurMaxAyuda) {
                            nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 );
                        }
                        addCampoCelda(esEditable, table, nombreCampo);
                        
                    } 
                }
                

                indexFila++;
            }

            // Creación de pie de tabla
            table.addCell(celdaEtiqueta("TOTAL", true));
            if(nmDurMaxAyuda== 1) {
                for (int i = 0; i < nmDurMaxAyuda; i++) {
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                            + (i + 1);
                    addCampoCelda(esEditable, table, nombreCampo);
                }

            }else {
                for (int i = 0; i <= nmDurMaxAyuda; i++) {
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                            + (i + 1);
                    if (i == nmDurMaxAyuda) {
                        nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (nmDurMaxAyuda + 1);
                    }
                    addCampoCelda(esEditable, table, nombreCampo);
                }

            }
            table.setSplitLate(false);
            document.add(table);
            
            // Añadir tabla de ayuda solicitada calculada en fucion de intensidad
            //Añadirla si intensidad < 100
            if(mostrarAyuda.equals(Boolean.TRUE) && intensidad != null && intensidad < 100) {
                // MD-1574 JRFM Formularios con financiación la intensidad texto inferior
                agregarFilaVacio(document, 1);
                table = new PdfPTable(new float[] {1 });
                table.setWidthPercentage(100);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell();
                table.addCell(new Phrase("Intensidad de la ayuda: " 
                        + BigDecimal.valueOf(intensidad).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%",
                        tipoLetra9));
                document.add(table);
                agregarFilaVacio(document, 1);
                
                String titulo= "Ayuda solicitada calculada en función del porcentaje de intensidad de ayuda";
                
                PdfPTable tableResult = new PdfPTable(1);
             
                tableResult.setWidths(new int[] {1 });
                tableResult.setWidthPercentage(100);
                tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                tableResult.getDefaultCell().setPadding(0);
                tableResult.getDefaultCell().setBorder(Rectangle.BOX);
                tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
                
                PdfPTable tableTitulo = new PdfPTable(1);
                tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
                tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
                
                PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
                
                celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
                celdaTituloSuperior.setPadding(6);
                tableTitulo.addCell(celdaTituloSuperior);
                tableResult.addCell(tableTitulo);
                
                tableResult.setSplitLate(false); 
                document.add(tableResult);
                
                // creacion de la tabla
                if(nmDurMaxAyuda== 1) {
                    table = new PdfPTable(nmDurMaxAyuda + 1);
                }else {
                    table = new PdfPTable(nmDurMaxAyuda + 2);
                }
                    
               
                table.setWidthPercentage(100);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                table.getDefaultCell().setBorder(Rectangle.BOX);
    
                // Creación de la cabecera
                cell = celdaEtiquetaCabecera(" ", true);
                table.addCell(cell);
                if(nmDurMaxAyuda== 1) {
                    table.addCell(celdaEtiquetaCabecera("IMPORTE", true));
                }else {
                    for (int i = 0; i < nmDurMaxAyuda; i++) {
                        Integer id = i + 1;
                        table.addCell(celdaEtiquetaCabecera(id + "° " + "Anualidad", true));
                    }
        
                    cell = celdaEtiquetaCabecera("TOTAL", true);
                    table.addCell(cell);
                }
               
    
                // creacion de cuerpo
                indexFila = 0;
                
                for (BloqueComponenteDTO fila : filas) {
    
                    table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));
    
                    if(nmDurMaxAyuda== 1) {
                        for (int i = 0; i < nmDurMaxAyuda; i++) {
                            String nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 ) + "_" + (i + 1);
                            addCampoCelda(esEditable, table, nombreCampo);
                            
                        }
                    }else {
                        for (int i = 0; i <= nmDurMaxAyuda; i++) {
                            String nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 ) + "_" + (i + 1);
                            if (i == nmDurMaxAyuda) {
                                nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                                        + fila.getIdBloqueComponente() + "_" + (getFilaBloqueComponente(fila,i) +1 );
                            }
                            addCampoCelda(esEditable, table, nombreCampo);
                            
                        }
                    }
                    
    
                    indexFila++;
                }
    
                // Creación de pie de tabla
                table.addCell(celdaEtiqueta("TOTAL", true));
                if(nmDurMaxAyuda== 1) {
                    for (int i = 0; i < nmDurMaxAyuda; i++) {
                        String nombreCampo = "ATC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (i + 1);
                        addCampoCelda(esEditable, table, nombreCampo);
                    }
                }else {
                    for (int i = 0; i <= nmDurMaxAyuda; i++) {
                        String nombreCampo = "ATC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (i + 1);
                        if (i == nmDurMaxAyuda) {
                            nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                    + (nmDurMaxAyuda + 1);
                        }
                        addCampoCelda(esEditable, table, nombreCampo);
                    }
                }
                
                table.setSplitLate(false); 
                document.add(table);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
    
    private Integer getFilaBloqueComponente(BloqueComponenteDTO fila,Integer indexFila) {
        
        if(fila != null && fila.getDatos() != null && !fila.getDatos().isEmpty() && fila.getDatos().size() >= indexFila) {
            if(fila.getDatos().size() > indexFila) {
                return fila.getDatos().get(indexFila).getNmFila();
            }else {
                return fila.getDatos().get(indexFila-1).getNmFila();
            }
            
        }
        return -1;
    }
    
    private void agregarBloqueTablaPresupFinanciacion(Document document,
            ExpedienteDTO dto, boolean esEditable) throws ServiceException {

        try {
            
            if(dto.getListaExpedientePresup()!= null && !dto.getListaExpedientePresup().isEmpty()) {

                PdfPCell cell = null;

                // creacion de la tabla
                PdfPTable table = new PdfPTable(dto.getListaExpedientePresup().get(0).getLstExpedientePresupMontoDTO().size() + 2);
                table.setWidthPercentage(100);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                table.getDefaultCell().setBorder(Rectangle.BOX);
                table.getDefaultCell().setBorderColor(BaseColor.BLACK);

                // Creación de la cabecera
                cell = celdaEtiquetaCabecera(" ", true);
                table.addCell(cell);

                for (int i = 0; i < dto.getListaExpedientePresup().get(0).getLstExpedientePresupMontoDTO().size(); i++) {
                    Integer id = i + 1;
                    table.addCell(celdaEtiquetaCabecera(id + "° " + "Anualidad", true));
                }

                cell = celdaEtiquetaCabecera("TOTAL", true);
                table.addCell(cell);

                // creacion de cuerpo
                for(ExpedientePresupDTO presup:dto.getListaExpedientePresup()) {
              

                    table.addCell(celdaEtiqueta(presup.getTipoFinanciacionDTO().getDsValor(), true));

                    for (int i = 0; i <= presup.getLstExpedientePresupMontoDTO().size(); i++) {
                        String nombreCampo = "IC_" + presup.getIdExpedientePresup() + "_"  +i + 1;
                        if (i == presup.getLstExpedientePresupMontoDTO().size()) {
                            nombreCampo = "TF_" + presup.getIdExpedientePresup();
                        }
                        addCampoCelda(esEditable, table, nombreCampo);
                        
                    }

                    
                }

                // Creación de pie de tabla
                table.addCell(celdaEtiqueta("TOTAL", true));

                for (int i = 0; i <= dto.getListaExpedientePresup().get(0).getLstExpedientePresupMontoDTO().size(); i++) {
                    String nombreCampo = "TC_" + dto.getListaExpedientePresup().get(0).getIdExpedientePresup() +"_" + (i + 1);
                    if (i == dto.getListaExpedientePresup().get(0).getLstExpedientePresupMontoDTO().size()) {
                        nombreCampo = "TT_" + dto.getListaExpedientePresup().get(0).getIdExpedientePresup();
                    }
                    addCampoCelda(esEditable, table, nombreCampo);
                }

                document.add(table);
                
                // Añadir tabla de ayuda solicitada calculada en fucion de intensidad
                
                agregarFilaVacio(document, 1);
                
            }

          
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
    
    
    /**
     *
     * @param writer
     * @param document
     * @param bloque
     * @throws ServiceException
     */
    private void agregarBloqueTablaAyuda(PdfWriter writer, Document document,
            BloqueDTO bloque) throws ServiceException {

        try {

            // Quita los bloqueComponente de baja y ordena
            List<BloqueComponenteDTO> lstBloqueComponentes = obtenerComponentes(bloque);
            List<BloqueComponenteDTO> filas = lstBloqueComponentes.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());
            List<BloqueComponenteDTO> columnas = lstBloqueComponentes.stream()
                    .filter(f -> f.getItColumna()).collect(Collectors.toList());

            // creacion de cabecera
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            // Agregamos la cabecera
            table.addCell(celdaEtiquetaCabecera(" ", true));
            table.addCell(celdaEtiquetaCabecera(Constantes.TablaFinanciacion.IMPORTE, true));

            // creacion de cuerpo
            for (BloqueComponenteDTO fila : filas) {
                for (int i = 0; i < columnas.size(); i++) {
                    table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));
                    table.addCell(celdaCampoTexto(
                            columnas.get(i).getIdBloqueComponente() + "_" + (i + 1)));
                }
            }

            document.add(table);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

    }

    /**
     *
     * @param writer
     * @param document
     * @param bloque
     * @throws DocumentException
     */
    private void agregarBloqueTablaActuacion(PdfWriter writer, Document document,
            BloqueDTO bloque, Map<String, String> mapaDatos) throws ServiceException {

        try {

            List<BloqueComponenteDTO> componentes = obtenerComponentes(bloque);
            List<BloqueComponenteDTO> filas = new ArrayList<>();
            List<BloqueComponenteDTO> columas = new ArrayList<>();
            List<BloqueComponenteDTO> colTabla1 = new ArrayList<>();
            List<BloqueComponenteDTO> colTabla2 = new ArrayList<>();
            List<BloqueComponenteDTO> colTabla3 = new ArrayList<>();
            List<BloqueComponenteDTO> colTabla4 = new ArrayList<>();

            List<String> check = new ArrayList<>();
            check.add("");

            // filas
            componentes.stream().filter(f -> Boolean.TRUE.equals(f.getItFila()))
                    .forEach(filas::add);

            // Columnas
            componentes.stream().filter(f -> Boolean.TRUE.equals(f.getItColumna()))
                    .forEach(columas::add);
            
            Integer idComFinCodActu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.CODIGO_ACTUACION);
            Integer idComFinActu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.ACTUACION);
            Integer idComFinMemExpl = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.MEMORIAS_EXPLICATIVA);
            Integer idComFinSelProve = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR);
            Integer idComFinSelImpo = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE);
            Integer idComFinTotImpInv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_INVERSION);
            Integer idComFinTotImpAyu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA);
            
            
            columas.stream()
                    .filter(c -> idComFinCodActu <= c.getComponenteDTO().getIdComponente()
                            && idComFinActu >= c.getComponenteDTO().getIdComponente())
                    .forEach(colTabla1::add);

            columas.stream()
                    .filter(c -> !idComFinActu.equals(c.getComponenteDTO().getIdComponente()))
                    .filter(c -> idComFinCodActu <= c.getComponenteDTO().getIdComponente()
                            && idComFinMemExpl >= c.getComponenteDTO().getIdComponente())
                    .forEach(colTabla2::add);

            columas.stream()
                    .filter(c -> idComFinCodActu.equals(c.getComponenteDTO().getIdComponente()))
                    .forEach(colTabla3::add);

            columas.stream().filter(
                    c -> idComFinSelProve <= c.getComponenteDTO().getIdComponente()
                            && idComFinSelImpo >= c.getComponenteDTO().getIdComponente())
                    .forEach(colTabla3::add);

            columas.stream()
                    .filter(c -> idComFinTotImpInv <= c.getComponenteDTO().getIdComponente()
                            && idComFinTotImpAyu >= c.getComponenteDTO().getIdComponente())
                    .forEach(colTabla4::add);

            // Primera Tabla - Actuaciones Sudvencionales
            PdfPTable table = new PdfPTable(3);
            float[] widths = new float[] {1f, 3f, 17f };
            table.setWidths(widths);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            table.addCell(celdaEtiquetaCabecera("", true));
            for (BloqueComponenteDTO col : colTabla1) {
                table.addCell(celdaEtiquetaCabecera(col.getComponenteDTO().getDsNombre(), true));
            }

            // creacion de cuerpo
            for (BloqueComponenteDTO fila : filas) {

                List<String> opciones = new ArrayList<>();
                opciones.add(fila.getTipoFinanciacionDTO().getCdCodigo() + "");

                List<String> valores = new ArrayList<>();
                for (Integer i = 0; i < opciones.size(); i++) {
                    valores.add("chk_" + fila.getTipoFinanciacionDTO().getCdCodigo() + "_"
                            + fila.getNmOrden());
                }

                table.addCell(
                        celdaCampoMultipleCheck(opciones, valores, Boolean.FALSE, Boolean.FALSE));

                table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getCdCodigo(), true));
                table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));

            }

            document.add(table);

            // Segunda Tabla - Detalle de los propuesto
            // creacion de cabecera
            agregarDescripcionActuacion(document, Constantes.TablaFinanciacion.DETALLE_OFERTA);

            table = new PdfPTable(colTabla2.size());
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            for (BloqueComponenteDTO col : colTabla2) {
                String ref = col.getComponenteDTO().getIdComponente().equals(idComFinMemExpl) ? " (3)" : "";
                table.addCell(celdaEtiquetaCabecera(col.getComponenteDTO().getDsNombre() + ref, true));
            }

            // creacion de cuerpo
            for (BloqueComponenteDTO fila : filas) {

                int indexColumna = 1;
                for (BloqueComponenteDTO columna : colTabla2) {

                    if (idComFinMemExpl.equals(columna.getComponenteDTO().getIdComponente())) {

                        List<String> valores = new ArrayList<>();
                        for (Integer i = 0; i < columna.getComponenteDTO().getLstCheckItems()
                                .size(); i++) {

                            String nombreCampo = "chk_" + fila.getNmOrden() + "_" + indexColumna
                                    + "_" + columna.getComponenteDTO().getLstCheckItems().get(i);
                            valores.add(nombreCampo);

                        }

                        table.addCell(celdaCampoMultipleCheck(
                                columna.getComponenteDTO().getLstCheckItems(), valores,
                                Boolean.TRUE, Boolean.TRUE));

                    } else if (idComFinActu.equals(columna.getComponenteDTO().getIdComponente())) {
                        String nombreCampo = "DT_COD_" + fila.getNmOrden() + "_" + indexColumna;
                        table.addCell(celdaCampoTexto(nombreCampo));
                    } else {
                        String nombreCampo = "DT_IMP_" + fila.getNmOrden() + "_" + indexColumna;

                        if (Objects.nonNull(mapaDatos)
                                && Objects.nonNull(mapaDatos.get(nombreCampo))) {
                            String dato = mapaDatos.get(nombreCampo);
                            int tam = dato.trim().length();
                            if (tam <= 9) {
                                table.addCell(celdaCampoTexto(nombreCampo));
                            } else {
                                tam = tam / 9;
                                table.addCell(celdaCampoTextoM(nombreCampo, tam));
                            }
                        } else {
                            table.addCell(celdaCampoTexto(nombreCampo));
                        }
                    }

                    indexColumna++;

                }
            }

            document.add(table);

            // tercera Tabla -- Ofertas seleccionadas
            agregarDescripcionActuacion(document, Constantes.TablaFinanciacion.OFERTA_SELECCIONADA);

            table = new PdfPTable(colTabla3.size());
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            // Agregamos la cabecera
            for (BloqueComponenteDTO col : colTabla3) {
                table.addCell(celdaEtiquetaCabecera(col.getComponenteDTO().getDsNombre(), true));
            }

            // creacion de cuerpo
            for (BloqueComponenteDTO fila : filas) {
                int indexColumna = 1;
                for (BloqueComponenteDTO columna : colTabla3) {

                    String nombreCampo = "DT_IMP_SEL_" + fila.getNmOrden() + "_" + indexColumna;

                    if (Objects.nonNull(mapaDatos) && Objects.nonNull(mapaDatos.get(nombreCampo))) {
                        String dato = mapaDatos.get(nombreCampo);
                        int tam = dato.trim().length();
                        log.info("tamaño " + String.valueOf(tam));

                        if (tam <= 27) {
                            table.addCell(celdaCampoTexto(nombreCampo));
                        } else {
                            tam = tam / 27;
                            log.info("seleccionado " + String.valueOf(tam));
                            table.addCell(celdaCampoTextoM(nombreCampo, 2));
                        }
                    } else {
                        table.addCell(celdaCampoTexto(nombreCampo));
                    }

                    indexColumna++;
                }
            }
            document.add(table);

            // Cuarta Tabla -- ayuda solicitada
            agregarDescripcionActuacion(document, Constantes.TablaFinanciacion.AYUDA_SOLICITADA);

            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.setSpacingAfter(15f);

            // Agregamos la cabecera
            for (BloqueComponenteDTO col : colTabla4) {
                String ref = col.getComponenteDTO().getIdComponente().equals(idComFinTotImpAyu) ? " (1)(2)" : "";
                table.addCell(celdaEtiquetaCabecera(col.getComponenteDTO().getDsNombre() + ref, true));
            }

            // creacion de cuerpo
            int indexColumna = 1;
            for (BloqueComponenteDTO columna : colTabla4) {
                table.addCell(celdaCampoTexto("DT_IMP_AS_1_" + indexColumna));
                indexColumna++;
            }

            document.add(table);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

    }

    /**
     * Método para crear el contenido del bloque tipo documento
     *
     * @param writer Escritor PDF
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarBloqueDocumento(PdfWriter writer, Document document,
            BloqueDTO bloque) throws DocumentException, ServiceException {
        List<BloqueComponenteDTO> componentes = obtenerComponentes(bloque);

        Comparator<BloqueComponenteDTO> compareByNumOrden = (BloqueComponenteDTO c1,
                BloqueComponenteDTO c2) -> c1.getNmOrden().compareTo(c2.getNmOrden());

        if (!componentes.isEmpty()) {
            Collections.sort(componentes, compareByNumOrden);
        }

        PdfPTable table = null;
        if (Boolean.FALSE.equals(bloque.getItAutorizaConsulta())
                && (bloque.getIdTematica() == null || bloque.getIdTematica().getDsValor() == null
                        || bloque.getIdTematica().getDsValor().length() == 0)) {
            table = new PdfPTable(new float[] {100 });
            table.setWidthPercentage(100);
            PdfPCell cellTitulo;
            cellTitulo = celdaEtiquetaNegrita("Documentos que se aportan junto a la solicitud", true);
            cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellTitulo.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellTitulo);
            document.add(table);
        } else if (Boolean.TRUE.equals(bloque.getItAutorizaConsulta())) {
            if (Constantes.TipoSeleccionDocumento.R.equals(bloque.getItTipoSelDoc())) {
                table = new PdfPTable(new float[] {70, 15, 15 });
                table.setWidthPercentage(100);
                PdfPCell cellTitulo;
                cellTitulo = celdaEtiqueta(
                        "La Comunidad de Madrid consultará, por medios electrónicos, los datos de los siguientes "
                                + "documentos si lo autoriza expresamente. De lo contrario, deberá aportarlos al procedimiento",
                        true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                cellTitulo = celdaEtiqueta("Autorizo la consulta", true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                cellTitulo = celdaEtiqueta("No autorizo la consulta y aporto el documento", true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                document.add(table);
            } else if (Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc())) {
                table = new PdfPTable(new float[] {100});
                table.setWidthPercentage(100);
                PdfPCell cellTitulo = celdaEtiqueta("La Comunidad de Madrid consultará, por medios electrónicos, los datos de los siguientes documentos(*)", true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                document.add(table);
            } else {
                table = new PdfPTable(new float[] {70, 30 });
                table.setWidthPercentage(100);
                PdfPCell cellTitulo;
                cellTitulo = celdaEtiqueta(
                        "La Comunidad de Madrid consultará, por medios electrónicos, los datos de los "
                                + "siguientes documentos, excepto que expresamente desautorice la consulta (*)",
                        true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                cellTitulo = celdaEtiqueta("No autorizo la consulta y aporto documento", true);
                cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTitulo);
                document.add(table);
            } 
        }

        List<PdfFormField> agrupadores = new ArrayList<>();

        int contadorFilas = 1;
        for (BloqueComponenteDTO componente : componentes) {
            
            Integer idTipoComponente = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK);
            
            BloqueComponenteDTO componenteCheck = new BloqueComponenteDTO();
            componenteCheck.setIdBloqueComponente(componente.getIdBloqueComponente());
            componenteCheck.setComponenteDTO(new ComponenteDTO());
            componenteCheck.getComponenteDTO().setDsValorMultiple("");
            
            Map<String, Object> configuracionAdicional = new HashMap<>();
            configuracionAdicional.put("mostrarEtiquetaCheck", false);
            configuracionAdicional.put("mostrarUnaColumna", false);
            configuracionAdicional.put(aliasMostrarBorde, false);
            
            if (Boolean.FALSE.equals(bloque.getItAutorizaConsulta())) {
                table = new PdfPTable(new float[] {95, 5 });
            } else {
                table = new PdfPTable(new float[] {70, 30 });
                if (Constantes.TipoSeleccionDocumento.R.equals(bloque.getItTipoSelDoc())) {
                    idTipoComponente = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO);
                    componenteCheck.getComponenteDTO().setDsValorMultiple("Autorizo la consulta;No autorizo la consulta y aporto el documento");
                    configuracionAdicional.put(aliasMostrarBorde, true);
                }
                if (Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc())) {
                    table = new PdfPTable(new float[] {100});
                }
            }

            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(0);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            PdfPCell cell = celdaEtiqueta(
                    componente.getComponenteDTO().getTipoDocumentoDTO().getDsValor(), true);
            table.addCell(cell);
            
            if (Boolean.FALSE.equals(bloque.getItAutorizaConsulta()) || (Boolean.TRUE
                    .equals(bloque.getItAutorizaConsulta())
                    && !Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc()))) {
                String nombreFila = "_" + contadorFilas + "_doc";
                agregarComponente(idTipoComponente, componenteCheck, agrupadores, table, writer,
                        nombreFila, configuracionAdicional);
                contadorFilas++;
            }
            
//            if (!Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc())) {

//                String nombreFila = "_" + contadorFilas + "_doc";
    
//                agregarComponente(idTipoComponente, componenteCheck, agrupadores, table, writer,
//                        nombreFila, configuracionAdicional);
//                contadorFilas++;
//            }

            document.add(table);
        }
        for (PdfFormField agrupador : agrupadores) {
            writer.addAnnotation(agrupador);
        }
    }

    /**
     * Método para crear el contenido del bloque tipo participantes
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @param convocatoriaAct Convocatoria Actor a la que pertenecen los
     *            participantes
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    public void agregarBloqueParticipantes(Document document, BloqueDTO bloque,
            ConvocatoriaAct convocatoriaAct) throws DocumentException, ServiceException {
        agregarTituloBloque(document, bloque);

        Boolean esPF = utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA)
                .equals(bloque.getActor().getTipoActorDTO().getIdValor());

        // Se añade la cabecera de la tabla
        PdfPTable table;

        table = new PdfPTable(new float[] {100 });
        table.setWidthPercentage(100);

        String titulo = "Listado de participantes " + convocatoriaAct.getTipoActor().getDsValor()
                + "\n";
        titulo += StringUtils.isNotBlank(convocatoriaAct.getTlDescripcion())
                ? convocatoriaAct.getTlDescripcion()
                : StringUtils.EMPTY;

        PdfPCell cell = celdaEtiqueta(titulo, Boolean.TRUE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        
        document.add(table);

        Integer idBloque = bloque.getIdBloque();

        // Si es física
        if (Boolean.TRUE.equals(esPF)) {

            table = new PdfPTable(new float[] {16, 28, 28, 28 });
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("Identificación", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Nombre", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Primer apellido", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Segundo apellido", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            for (Integer i = 0; i < convocatoriaAct.getNmMax(); i++) {
                String nombre = "_" + idBloque + "_" + i;

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("dni" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("nombre" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("apellido1" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("apellido2" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
        } else {
            // No es física, entonces es jurídica

            table = new PdfPTable(new float[] {16, 84 });
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("NIF", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Razón Social", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            for (Integer i = 0; i < convocatoriaAct.getNmMax(); i++) {
                String nombre = "_" + idBloque + "_" + i;

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("nif" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("razonSocial" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
        }

        document.add(table);
        agregarBloqueTextoAuxiliarInferior(document, bloque);
    }

    /**
     * Método para crear el contenido del bloque tipo tabla de plazos de
     * ejecución de las anualidades
     *
     * @param document Documento PDF
     * @param bloque Datos del bloque
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarBloquePlazosEjecucionAnualidadesSubTabla(Document document,BloqueDTO bloque,
            Integer nmDurMaxAyuda, boolean esEditable) throws  ServiceException {

        try {

            // creacion de la tabla
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);

            // Creación de la cabecera
            table.addCell(celdaEtiquetaCabecera("Anualidad", true));
           /* table.addCell(celdaEtiquetaCabecera("Fecha inicio ejecución", true)); */
            table.addCell(celdaEtiquetaCabecera("Fecha fin ejecución", true));

            // creacion de cuerpo
           
            
            List<BloqueComponenteDTO> listaBloqueComponente = bloque
                    .getLstBloqueComponenteDTO().stream()
                    .filter(x -> x.getFcBajaFila() == null)
                    .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                    .collect(Collectors.toList());

        
            List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());
            
            for (int i = 0; i < lstFilas.size(); i++) {
                table.addCell(celdaEtiqueta(i+1 + "° " + "Anualidad", true));
               
                if (lstFilas.get(i).getDatos() != null && lstFilas.get(i).getDatos().size() >0) {
                    /*DatoDTO datoDTO =lstFilas.get(i).getDatos().get(0);
                    if (StringUtils.isNotBlank(datoDTO.getDsValor())) {
                        addCampoCelda(esEditable, table, "fecha_ini_ejecucion_anualidad_" + bloque.getIdBloque() + "_"
                                + lstFilas.get(i).getIdBloqueComponente() + "_"
                                + (datoDTO.getNmFila() + 1) + "_"
                                + (datoDTO.getNmColumna() + 1));
                        

                     }*/
                    
                    DatoDTO datoDTO =lstFilas.get(i).getDatos().get(0);
                    if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                        addCampoCelda(esEditable, table,  "fecha_fin_ejecucion_anualidad_" + bloque.getIdBloque() + "_"
                                + lstFilas.get(i).getIdBloqueComponente() + "_"
                                + (datoDTO.getNmFila() + 1) + "_"
                                + (datoDTO.getNmColumna() + 1));
   
                     }

                }


            }
            
            
            
            
            
            
            
            document.add(table);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Método para crear el contenido del componente
     *
     * @param idTipoComponente Identificador de tipo de componente
     * @param componente Datos de componente
     * @param agrupadores Lista de agrupadores
     * @param table Tabla donde se agregará el componente
     * @param writer Escritor PDF
     * @param nombreFila Nombre de fila
     * @param configuracionAdicional Configuración adicional para la contruccion
     *            del componente
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     * @throws ServiceException 
     */
    private void agregarComponente(Integer idTipoComponente, BloqueComponenteDTO componente,
            List<PdfFormField> agrupadores, PdfPTable table, PdfWriter writer, String nombreFila,
            Map<String, Object> configuracionAdicional) throws DocumentException, ServiceException {

        boolean mostrarEtiquetaCheck = false;
        boolean mostrarUnaColumna = false;
        boolean mostrarBorde = false;

        if (configuracionAdicional != null && configuracionAdicional.size() > 0) {
            mostrarEtiquetaCheck = (boolean) configuracionAdicional.get(aliasMostrarEtiquetaCheck);
            mostrarUnaColumna = (boolean) configuracionAdicional.get(aliasMostrarUnaColumna);
            mostrarBorde = configuracionAdicional.get(aliasMostrarBorde)!=null && (boolean) configuracionAdicional.get(aliasMostrarBorde);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO).equals(idTipoComponente)) {
            Integer idSubTipoComponente = componente.getComponenteDTO().getSubTipoDTO()
                    .getIdValor();
            agregarTipoComponenteCampo(idSubTipoComponente, table, componente, nombreFila);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK).equals(idTipoComponente)) {
            agregarTipoComponenteCheck(table, componente, mostrarEtiquetaCheck, nombreFila,
                    mostrarUnaColumna);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO).equals(idTipoComponente)) {
            agregarTipoComponenteRadio(writer, table, componente, agrupadores, nombreFila,
                    mostrarUnaColumna, mostrarEtiquetaCheck, mostrarBorde);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO).equals(idTipoComponente)) {
            agregarTipoComponenteCombo(writer, table, componente, agrupadores, nombreFila);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CATALOGO).equals(idTipoComponente)) {
            agregarTipoComponenteCatalogo(writer, table, componente, agrupadores, nombreFila);
        } else {
            table.addCell(celdaEtiqueta("", false));
        }
    }

    /**
     * Método para crear el contenido de tipo de componente campo
     *
     * @param idSubTipoComponente Identificador de sub tipo de componente
     * @param table Tabla donde se agregará el componente
     * @param componente Datos de componente
     * @param nombreFila Nombre de fila
     * @throws ServiceException 
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarTipoComponenteCampo(Integer idSubTipoComponente, PdfPTable table,
            BloqueComponenteDTO componente, String nombreFila) {
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_TEXTO).equals(idSubTipoComponente)) {
            if( componente.getComponenteDTO().getNmLongitud()!= null &&  componente.getComponenteDTO().getNmLongitud()> 120) {
              // table.addCell(celdaCampoTextoLargo(componente.getIdBloqueComponente() + nombreFila));
               table.addCell(celdaCampoMultiTexto(componente.getIdBloqueComponente() + nombreFila));
            }else {
                table.addCell(celdaCampoTexto(componente.getIdBloqueComponente() + nombreFila));
            }
            
           
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_NUMERO).equals(idSubTipoComponente)) {
            table.addCell(celdaCampoTexto(componente.getIdBloqueComponente() + nombreFila));
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA).equals(idSubTipoComponente)) {
            table.addCell(celdaCampoTexto(componente.getIdBloqueComponente() + nombreFila));
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO).equals(idSubTipoComponente)) {
            
            
            if( componente.getComponenteDTO().getNmLongitud()!= null &&  componente.getComponenteDTO().getNmLongitud()>= 500) {
                  
                  Integer tam = componente.getComponenteDTO().getNmLongitud() / 10;
                  log.info("seleccionado " + String.valueOf(tam));
                  //table.addCell(celdaCampoTextoLargo(componente.getIdBloqueComponente() + nombreFila,tam));
                  if(componente.getDatos()!= null && !componente.getDatos().isEmpty()) {
                      table.addCell(celdaEtiqueta(componente.getDatos().get(0).getDsValor(), false));
                  }else {
                      table.addCell(celdaEtiqueta(" ", false));
                  }
                  
              }else {
                  table.addCell(celdaCampoMultiTexto(componente.getIdBloqueComponente() + nombreFila));  
              }
            
            
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.TEXTO_FIJO).equals(idSubTipoComponente)) {
                // table.addCell(celdaEtiqueta(componente.getComponenteDTO().getTlTextoFijo(), false));
                // MD-2469 [ADM] Ampliar campo texto fijo en formulario
                table.addCell(celdaEtiqueta(componente.getComponenteDTO().getClTextoFijo(), false));
        
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.MONEDA).equals(idSubTipoComponente)) {
              table.addCell(celdaCampoTexto(componente.getIdBloqueComponente() + nombreFila));
         
        } else {
            table.addCell(celdaEtiqueta("", false));
        }
    }

    /**
     * Método para crear el contenido de tipo de componente check
     *
     * @param table Tabla donde se agregará el componente
     * @param componente Datos de componente
     * @param mostrarEtiqueta Indicador para mostrar etiqueta del componente
     *            checkbox
     * @param nombreFila Nombre de fila
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    
    public void agregarTipoComponenteCheck(PdfPTable table, BloqueComponenteDTO componente,
            boolean mostrarEtiqueta, String nombreFila, boolean mostrarUnaColumna)
            throws DocumentException {
        List<String> lstOpciones = Arrays
                .asList(componente.getComponenteDTO().getDsValorMultiple().split(";"));
       
        List<String> lstValores = new ArrayList<>();
        for (Integer i = 0; i < lstOpciones.size(); i++) {
            String nombreCheck = "chk_" + componente.getIdBloqueComponente() + nombreFila + "_"
                    + (i + 1);
            lstValores.add(nombreCheck);
        }

        PdfPCell cellChk = new PdfPCell();
        if (Constantes.ComponenteSolicitud.CHECK_OPONGO.equals(componente.getComponenteDTO().getNmConstante())) {
            cellChk.addElement(celdaCampoUnCheck(lstOpciones, lstValores, mostrarEtiqueta,
                    mostrarUnaColumna));
        } else {
            cellChk.addElement(celdaCampoMultipleCheck(lstOpciones, lstValores, mostrarEtiqueta,
                    mostrarUnaColumna));
        }
        cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cellChk);
    }

    /**
     * Método para crear el contenido de tipo de componente radio
     *
     * @param writer Escritor PDF
     * @param table Tabla donde se agregará el componente
     * @param componente Datos del componente
     * @param agrupadores Lista de agrupadores
     * @param nombreFila Nombre de fila
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTipoComponenteRadio(PdfWriter writer, PdfPTable table,
            BloqueComponenteDTO componente, List<PdfFormField> agrupadores, String nombreFila,
            boolean mostrarUnaColumna, boolean mostrarEtiquetaCheck, boolean mostrarBorde) throws DocumentException {
        List<String> lstValores = Arrays
                .asList(componente.getComponenteDTO().getDsValorMultiple().split(";"));
        PdfFormField agrupador = PdfFormField.createRadioButton(writer, true);
        agrupador.setFieldName("radio_" +componente.getIdBloqueComponente() + nombreFila);

        PdfPCell cellRad = new PdfPCell();
        cellRad.addElement(celdaCampoMultipleRadio(agrupador, lstValores, mostrarEtiquetaCheck, mostrarUnaColumna, mostrarBorde));
        cellRad.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellRad.setPadding(0);
        table.addCell(cellRad);

        agrupadores.add(agrupador);
    }

    /**
     * Método para crear el contenido de tipo de componente combo
     *
     * @param writer Escritor PDF
     * @param table Tabla donde se agregará el componente
     * @param componente Datos del componente
     * @param agrupadores Lista de agrupadores
     * @param nombreFila Nombre de fila
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarTipoComponenteCombo(PdfWriter writer, PdfPTable table,
            BloqueComponenteDTO componente, List<PdfFormField> agrupadores, String nombreFila) {
        String[] lstValores = componente.getComponenteDTO().getDsValorMultiple().split(";");
        String nombreCombo = componente.getIdBloqueComponente() + nombreFila;
       

        PdfFormField agrupador = PdfFormField.createEmpty(writer);
        agrupador.setFieldName(nombreCombo);

        table.addCell(celdaCampoCombo(agrupador, nombreCombo, lstValores, lstValores));

        agrupadores.add(agrupador);
    }
    
    private void agregarTipoComponenteCatalogo(PdfWriter writer, PdfPTable table,
            BloqueComponenteDTO componente, List<PdfFormField> agrupadores, String nombreFila) throws ServiceException {
        List<CatalogosValoresDTO> listaValores = catalogosValoresService.buscarPorCatalogo(componente.getComponenteDTO().getSubTipoCatalogoDTO().getIdCatalogo());
        String[] valores = listaValores.stream()
            .map(CatalogosValoresDTO::getDsValor) 
            .toArray(String[]::new);
        String nombreCombo = componente.getIdBloqueComponente() + nombreFila;

        PdfFormField agrupador = PdfFormField.createEmpty(writer);
        agrupador.setFieldName(nombreCombo);

        table.addCell(celdaCampoCombo(agrupador, nombreCombo, valores, valores));

        agrupadores.add(agrupador);
    }

    /**
     * Método para crear celda con una etiqueta de pie de texto
     *
     * @param texto Texto a colocar
     * @param align Indicador para la orientación horizontal del texto
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaEtiquetaPie(String texto, String align) {
        tipoLetra7Italic.setColor(BaseColor.DARK_GRAY);
        PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra7Italic));

        if (align.equals("right")) {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        } else {
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        }

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setPaddingTop(2);
        return cell;
    }

    /**
     * Método para crear celda con una etiqueta simple
     *
     * @param texto Texto a colocar
     * @param border Indicador si la celda tiene borde
     * @return PdfPCell Celda con el elemento
     */
    public PdfPCell celdaEtiqueta(String texto, boolean border) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra8));
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(6);
        return cell;
    }

    /**
     * Método para crear celda con una etiqueta en negrita
     *
     * @param texto Texto a colocar
     * @param border Indicador si la celda tiene borde
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaEtiquetaNegrita(String texto, boolean border) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra8Bold));
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setPadding(6);
        return cell;
    }

    /**
     * Método para crear celda con una etiqueta en negrita para las cabeceras de
     * las tablas
     *
     * @param texto Texto a colocar
     * @param border Indicador si la celda tiene borde
     * @return PdfPCell Celda con el elemento
     */
    public PdfPCell celdaEtiquetaCabecera(String texto, boolean border) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra8Bold));
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(6);
        return cell;
    }

    /**
     * Método para crear celda con una componente tipo texto
     *
     * @param nombre Nombre del componente
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaCampoTexto(String nombre) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new TextFieldCellEvent(nombre, BaseField.EDIT));
        cell.setPadding(6);
        return cell;
    }
    
    private PdfPCell celdaCampoTextoLargo(String nombre,Integer height) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setFixedHeight(height);
        cell.setCellEvent(new TextFieldCellEvent(nombre, BaseField.MULTILINE));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell celdaCampoTextoM(String nombre, int nroFilasCampo) {

        Integer heightInt = (nroFilasCampo + 2) * 10;
        PdfPCell cell = new PdfPCell(new Paragraph(" "));
        cell.setFixedHeight(heightInt.floatValue());
        cell.setCellEvent(new TextFieldCellEvent(nombre, BaseField.MULTILINE, nroFilasCampo));
        cell.setPadding(6);
        return cell;
    }

    /**
     * Método para crear celda con una componente tipo multitexto
     *
     * @param nombre Nombre del componente
     * @return PdfPCell Celda con el elemento
     */
    public PdfPCell celdaCampoMultiTexto(String nombre) {
        return celdaCampoMultiTexto(nombre, null);
    }

    @Override
    public PdfPCell celdaCampoMultiTexto(String nombre, Integer nmLongitud) {
        PdfPCell cell = new PdfPCell(new Phrase(" ", tipoLetra8));
        cell.setMinimumHeight(calcularAlturaCampoMultiTexto(nmLongitud));
        cell.setCellEvent(new TextFieldCellEvent(nombre, BaseField.MULTILINE));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(10);
        cell.setPadding(6);
        return cell;
    }

    /**
     * Calcula la altura mínima necesaria para que un campo de texto multilínea
     * pueda mostrar completo un contenido de hasta nmLongitud caracteres, sin
     * que se muestre cortado dentro del recuadro del campo.
     *
     * @param nmLongitud longitud máxima de caracteres permitida del componente
     * @return altura mínima en puntos
     */
    private float calcularAlturaCampoMultiTexto(Integer nmLongitud) {
        float alturaMinima = 42f;

        if (nmLongitud == null || nmLongitud <= 0) {
            return alturaMinima;
        }

        int caracteresPorLinea = 90;
        float alturaLinea = 11f;
        float margenVertical = 20f;

        int numLineas = (int) Math.ceil(nmLongitud / (double) caracteresPorLinea);
        float alturaCalculada = numLineas * alturaLinea + margenVertical;

        return Math.max(alturaMinima, alturaCalculada);
    }

    public void celdaCampoMultiTextoVacio(Document document) throws DocumentException {
        PdfPTable tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);

        PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra9));
        cellDoc.setBorder(Rectangle.BOX);
        cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDoc.setPadding(30);
        tabla.addCell(cellDoc);
        document.add(tabla);
    }

    public void celdaCampoTextoVacio(Document document) throws DocumentException {
        PdfPTable tablaDocumentacion = new PdfPTable(1);
        tablaDocumentacion.setWidthPercentage(20);
        tablaDocumentacion.setHorizontalAlignment(0);

        PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra9));
        cellDoc.setBorder(Rectangle.BOX);
        cellDoc.setPadding(10);
        tablaDocumentacion.addCell(cellDoc);
        document.add(tablaDocumentacion);
    }

    public void celdaCampoMultiTextoExpEval(Document document, String dato)
            throws DocumentException {
        PdfPTable tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);

        PdfPCell cellDoc = new PdfPCell(new Phrase(dato, tipoLetra9));
        cellDoc.setBorder(Rectangle.BOX);
        cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDoc.setPaddingTop(10);
        cellDoc.setPaddingBottom(10);
        tabla.addCell(cellDoc);
        document.add(tabla);
    }

    public void celdaCampoTextoExpEval(Document document, String dato)
            throws DocumentException {
        PdfPTable tablaDocumentacion = new PdfPTable(1);
        tablaDocumentacion.setWidthPercentage(10);
        tablaDocumentacion.setHorizontalAlignment(0);

        PdfPCell cellDoc = new PdfPCell(new Phrase(dato, tipoLetra9));
        cellDoc.setBorder(Rectangle.BOX);
        cellDoc.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDoc.setPadding(10);
        tablaDocumentacion.addCell(cellDoc);
        document.add(tablaDocumentacion);
    }

    /**
     * Método para crear celda con una componente tipo check
     *
     * @param opcion Nombre del componente
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaCampoCheck(String opcion) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new CheckboxCellEvent(opcion));
        cell.setPadding(0);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    /**
     * Método para crear tabla con un multiples componentes tipo check
     *
     * @param opciones Lista de opciones
     * @param nombres Lista de nombres
     * @param mostrarEtiqueta Indicador para mostrar etiqueta asociada al
     *            checkbox
     * @return PdfPTable Tabla con la colección de componentes
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private PdfPTable celdaCampoMultipleCheck(List<String> opciones, List<String> nombres,
            boolean mostrarEtiqueta, boolean mostrarUnaColumna) throws DocumentException {

        int columnas = 1;
        int[] array = new int[] {1 };
        if (mostrarEtiqueta && !mostrarUnaColumna) {
            columnas = 4;
            array = new int[] {2, 1, 2, 1 };
        } else if (mostrarUnaColumna) {
            columnas = 2;
            array = new int[] {2, 1 };
        }
        
        PdfPTable tableCheck = new PdfPTable(columnas);
        tableCheck.setWidths(array);
        tableCheck.setWidthPercentage(100);
        tableCheck.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        for (int indice = 0; indice < nombres.size(); indice++) {
            if (mostrarEtiqueta) {
                PdfPCell cellChkLabel = new PdfPCell(new Phrase(opciones.get(indice), tipoLetra8));
                cellChkLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellChkLabel.setBorder(Rectangle.NO_BORDER);
                cellChkLabel.setPadding(6);
                tableCheck.addCell(cellChkLabel);
            }
            PdfPCell cellChk = celdaCampoCheck(nombres.get(indice));
            cellChk.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableCheck.addCell(cellChk);
        }

        if (mostrarEtiqueta && opciones.size() % 2 != 0 && !mostrarUnaColumna) {
            tableCheck.addCell(new Phrase(" "));
            tableCheck.addCell(new Phrase(" "));
        }

        return tableCheck;
    }
    
    private PdfPTable celdaCampoUnCheck(List<String> opciones, List<String> nombres,
            boolean mostrarEtiqueta, boolean mostrarUnaColumna) throws DocumentException {

        int columnas = 2;
        int[] array = new int[] {1,20};

        PdfPTable tableCheck = new PdfPTable(columnas);
        tableCheck.setWidths(array);
        tableCheck.setWidthPercentage(100);
        tableCheck.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        for (int indice = 0; indice < nombres.size(); indice++) {
            PdfPCell cellChk = celdaCampoCheck(nombres.get(indice));
            cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellChk.setBorder(Rectangle.NO_BORDER);
            tableCheck.addCell(cellChk);
            if (mostrarEtiqueta) {
                PdfPCell cellChkLabel = new PdfPCell(new Phrase(opciones.get(indice), tipoLetra8));
                cellChkLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellChkLabel.setPadding(6);
                cellChkLabel.setBorder(Rectangle.NO_BORDER);
                tableCheck.addCell(cellChkLabel);
            }
        }

//        if (mostrarEtiqueta && opciones.size() % 2 != 0 && !mostrarUnaColumna) {
//            tableCheck.addCell(new Phrase(" "));
//            tableCheck.addCell(new Phrase(" "));
//        }

        return tableCheck;
    }

    /**
     * Método para crear celda con una componente tipo radio
     *
     * @param agrupador Agrupador del componente
     * @param opcion Nombre de opción
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaCampoRadio(PdfFormField agrupador, String opcion, boolean mostrarBorde) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new RadioCellEvent(agrupador, opcion));
        cell.setPadding(8);
        if (!mostrarBorde) {
            cell.setPadding(2);
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    /**
     * Método para crear tabla con un multiples componentes tipo radio
     *
     * @param agrupador Agrupador del componente
     * @param opciones Lista de opciones
     * @param mostrarEtiqueta Indicador para mostrar etiqueta
     * @return PdfPTable Tabla con la colección de componentes
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private PdfPTable celdaCampoMultipleRadio(PdfFormField agrupador, List<String> opciones,
            boolean mostrarEtiqueta, boolean mostrarUnaColumna, boolean mostrarBorde) throws DocumentException {

        int columnas = 1;
        int[] array = new int[] {1 };
        if (mostrarEtiqueta && !mostrarUnaColumna && !mostrarBorde) {
            columnas = 4;
            array = new int[] {2, 1, 2, 1 };
        } else if (mostrarUnaColumna && !mostrarBorde) {
            columnas = 2;
            array = new int[] {2, 1 };
        } else if (mostrarBorde) {
            columnas = 2;
            array = new int[] {1, 1 };
        }

        PdfPTable tableRadio = new PdfPTable(columnas);
        tableRadio.setWidths(array);
        tableRadio.setWidthPercentage(100);
        tableRadio.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        for (String opcion : opciones) {
            if (mostrarEtiqueta) {
                PdfPCell cellRadLabel = new PdfPCell(new Phrase(opcion, tipoLetra8));
                cellRadLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRadLabel.setBorder(Rectangle.NO_BORDER);
                tableRadio.addCell(cellRadLabel);
            }
            tableRadio.addCell(celdaCampoRadio(agrupador, opcion, mostrarBorde));
        }

        if (mostrarEtiqueta && opciones.size() % 2 != 0 && !mostrarUnaColumna) {
            tableRadio.addCell(new Phrase(" "));
            tableRadio.addCell(new Phrase(" "));
        }
        return tableRadio;
    }

    /**
     * Método para crear celda con una componente tipo combo
     *
     * @param agrupador Agrupador del componente
     * @param nombreCombo String nombre del combo
     * @param opciones Arreglo de opciones
     * @param exportados Arreglo de exportados
     * @return PdfPCell Celda con el elemento
     */
    private PdfPCell celdaCampoCombo(PdfFormField agrupador, String nombreCombo,
            String[] opciones, String[] exportados) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new SelectCellEvent(agrupador, nombreCombo, exportados, opciones));
        cell.setPadding(6);
        return cell;
    }

    /**
     * Método para crear filas vacias en el documento
     *
     * @param document Documento PDF
     * @param repeticiones Cantidad de repeticiones
     */
    public void agregarFilaVacio(Document document, int repeticiones)
            throws DocumentException {
       
        while (repeticiones > 0 ) {
           // document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(1);
            table.setWidths(new int[] {1 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setPadding(0);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            PdfPCell cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);
            
            repeticiones--;
        }
    }

    /**
     * Método para agregar un espacio vertical más pequeño que agregarFilaVacio (que usa la
     * fuente por defecto de iText, más grande de lo esperado). Util para separar ligeramente
     * elementos consecutivos sin dejar tanto hueco como una linea en blanco completa.
     *
     * @param document documentoPDF
     * @throws DocumentException excepción
     */
    public void agregarEspacioPequeno(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell = new PdfPCell(new Phrase(" ", new Font(createCustomFont(), 4)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        table.addCell(cell);
        document.add(table);
    }

    /**
     * Método que devuelve los componentes que no están dados de baja ordenados
     * por nmOrden
     *
     * @param bloque
     * @return List<BloqueComponenteDTO>
     */
    private List<BloqueComponenteDTO> obtenerComponentes(BloqueDTO bloque) {
        return bloque.getLstBloqueComponenteDTO().stream().filter(x -> x.getFcBajaFila() == null)
                .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                .collect(Collectors.toList());
    }

    /**
     * Método para agregar campo cuando la convocatoria incluye fondos europeos
     *
     * @param document documentoPDF
     *
     */

    public void agregarTextoFondosEuropeos(Document document) throws DocumentException {

        agregarFilaVacio(document, 1);

        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setPadding(0);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(
                "Las ayudas que son objeto de la presente solicitud están cofinanciadas por el Fondo Europeo de Desarrollo Regional para el periodo de programación 2021-2027.",
                tipoLetra8));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        document.add(table);

    }

    /**
     * Método para agregar descripción auxiliar inferior.
     *
     * @param document Documento PDF
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    private void agregarBloqueTextoAuxiliarInferior(Document document, BloqueDTO bloque)
            throws DocumentException {

        if (StringUtils.isNotBlank(bloque.getTlAuxiliarInf())) {

            PdfPTable table = new PdfPTable(new float[] {1 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.getDefaultCell().setPadding(6);
            PdfPTable tableRow = new PdfPTable(1);
            tableRow.setWidthPercentage(100);

            table.addCell(new Phrase(bloque.getTlAuxiliarInf(), tipoLetra9));
            table.addCell(tableRow);

            document.add(table);
        }
    }

    /**
     * Método para añadir el campo de la huella digital
     *
     * @param document documentoPDF
     * @throws DocumentException excepción
     */
    public void agregarHuellaDigital(Document document) throws DocumentException {
        try {
            agregarFilaVacio(document, 1);
            
            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = null;

            table.setWidths(new int[] {1 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setPadding(0);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            PdfPTable tableTituloFecha = new PdfPTable(1);
            tableTituloFecha.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableTituloFecha.getDefaultCell().setBorder(Rectangle.BOX);
            tableTituloFecha.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase("FIRMA:", tipoLetra9Bold));
            
            celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaTituloSuperior.setPadding(6);
            tableTituloFecha.addCell(celdaTituloSuperior);
            table.addCell(tableTituloFecha);
            
            
            PdfPTable tableFirma = new PdfPTable(1);
            tableFirma.setWidthPercentage(100);
            tableFirma.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFirma.getDefaultCell().setBorder(Rectangle.BOX);
            tableFirma.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = new PdfPCell(new Phrase(" ", tipoLetra9));
            cell.setCellEvent(new InlineCellEvent(FormularioPdf.HUELLA, BaseField.EDIT)); 
            cell.setPadding(6);
            cell.setColspan(4);
            cell.setFixedHeight(25);
            tableFirma.addCell(cell);
            table.addCell(tableFirma);
            
            document.add(table);
        }catch (Exception e) {
            log.error(e.getMessage().toString());
        }
        
       
    }
    
    public void agregarHuellaDigitalOLD(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("HUELLA", BaseField.HIDDEN));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);

        document.add(table);
    }


    /**
     * Método para agregar cuando Lugar y Fecha
     *
     * @param document documentoPDF
     * @throws DocumentException excepción
     */
    public void agregarLugarFecha(Document document) throws DocumentException {

        agregarFilaVacio(document, 1);
        
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = null;

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTituloFecha = new PdfPTable(1);
        tableTituloFecha.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTituloFecha.getDefaultCell().setBorder(Rectangle.BOX);
        tableTituloFecha.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase("FECHA:", tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTituloFecha.addCell(celdaTituloSuperior);
        table.addCell(tableTituloFecha);

        
        PdfPTable tableFecha = new PdfPTable(new float[] {10, 40, 10,40 });
        tableFecha.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableFecha.getDefaultCell().setBorder(Rectangle.BOX);
        tableFecha.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = new PdfPCell(new Phrase("En ", tipoLetra9));
        cell.setPadding(6);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("lugar", BaseField.EDIT));
        cell.setPadding(6);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" a ", tipoLetra9));
        cell.setPadding(6);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("fechaTexto", BaseField.EDIT));
        cell.setPadding(6);
        tableFecha.addCell(cell);

       /* cell = new PdfPCell(new Phrase(" de ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("mes", BaseField.EDIT));
        //cell.setBorder(Rectangle.NO_BORDER);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" de ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        tableFecha.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("ano", BaseField.EDIT));
        //cell.setBorder(Rectangle.NO_BORDER);
        tableFecha.addCell(cell);
        */
        table.addCell(tableFecha);

        document.add(table);

    }

    /**
     * VMM: la fecha ya no se pinta aqui (vuelve a la cabecera, junto al nombre/razon social,
     * ver TramiteSubsanacionAdministrativa). Este metodo pinta solo "Firmado electrónicamente",
     * centrada, en una posicion fija cerca del pie de pagina.
     *
     * @param document
     * @param writer
     */
    public void agregarLugarFechaFirmaElectronica(Document document, PdfWriter writer) {

        float centroX = document.getPageSize().getWidth() / 2;
        PdfContentByte canvas = writer.getDirectContent();

        canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                new Phrase("Firmado electrónicamente", tipoLetra10Bold),
                centroX, 175, 0);
        canvas.endMarkedContentSequence();
    }

    public void agregarLugarFechaFirmaElectronicaADM(Document document, PdfWriter writer, String dsFirmante) throws DocumentException {
        
        Paragraph pFirma = new Paragraph();
        pFirma.setAlignment(Element.ALIGN_CENTER);
        pFirma.setSpacingBefore(15f);
        pFirma.setSpacingAfter(10f);

        // 1. Fecha
        pFirma.add(new Chunk("Madrid, a fecha de firma", tipoLetra8));
        pFirma.add(Chunk.NEWLINE);

        // 2. Título
        pFirma.add(new Chunk("Firmado electrónicamente", tipoLetra10Bold));

        // 3. Firmante (al venir ya validado desde fuera, entra siempre que tenga texto)
        if (StringUtils.isNotBlank(dsFirmante)) {
            pFirma.add(Chunk.NEWLINE);
            pFirma.add(new Chunk(dsFirmante, tipoLetra8));
        }

        document.add(pFirma);
    }
   
    
    
    public void agregarLugarFechaOld(Document document) throws DocumentException {

        agregarFilaVacio(document, 1);

        PdfPTable table = new PdfPTable(8);
        PdfPCell cell = null;

        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        // Los usamos para medir el tamaño de los huecos
        String lugar = ".....................................";
        String dia = "......";
        String mes = ".........................";
        String ano = "............";

        List<Float> widths = new ArrayList<>();
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint("En ",
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(lugar,
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(", a ",
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(dia,
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(" de ",
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(mes,
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(" de ",
                tipoLetra9.getCalculatedSize()));
        widths.add(tipoLetra9.getCalculatedBaseFont(Boolean.TRUE).getWidthPoint(ano,
                tipoLetra9.getCalculatedSize()));

        table.setWidths(ArrayUtils.toPrimitive(widths.toArray(new Float[8])));

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-10F);
        dottedline.setGap(3F);

        cell = new PdfPCell(new Phrase("En ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("lugar", BaseField.EDIT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(dottedline);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(", a ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("dia", BaseField.EDIT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(dottedline);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" de ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("mes", BaseField.EDIT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(dottedline);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" de ", tipoLetra9));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setCellEvent(new InlineCellEvent("ano", BaseField.EDIT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(dottedline);
        table.addCell(cell);

        document.add(table);

    }


    /**
     * Método para agregar destinatario
     *
     * @param writer writer
     * @param document documentoPDF
     * @param convocatoriaDTO convocatoriaDTO
     * @throws DocumentException excepción
     */
    public void agregarDestinatario(PdfWriter writer, Document document,
            ConvocatoriaDTO convocatoriaDTO,String strConsejeria) throws DocumentException {
        //agregarFilaVacio(document, 1);
       
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = null;

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTituloFecha = new PdfPTable(1);
        tableTituloFecha.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTituloFecha.getDefaultCell().setBorder(Rectangle.BOX);
        tableTituloFecha.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase("ORGANISMO DESTINATARIO", tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTituloFecha.addCell(celdaTituloSuperior);
        table.addCell(tableTituloFecha);

        
        PdfPTable tableDestinatario = new PdfPTable(new float[] { 30, 70 });
        String frase = StringUtils.EMPTY;
        cell = new PdfPCell(new Phrase("DIRECCIÓN GENERAL / ORGANISMO:", tipoLetra8));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setRowspan(6);
        cell.setPadding(6);
        tableDestinatario.addCell(cell);

        // se imprime si idDireccion no es nulo
        if (Objects.nonNull(convocatoriaDTO.getIdDireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdDireccion().getDsUnidad())) {
//            frase = convocatoriaDTO.getIdDireccion().getDsUnidad() + "\n\n";
            frase = convocatoriaDTO.getIdDireccion().getDsUnidad() + "\n";
        }
//        if (Objects.nonNull(convocatoriaDTO.getIdSubdireccion())
//                && StringUtils.isNotBlank(convocatoriaDTO.getIdSubdireccion().getDsUnidad())) {
//            frase = convocatoriaDTO.getIdSubdireccion().getDsUnidad() + "\n\n";
//        }
//        if (Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa()) && StringUtils
//                .isNotBlank(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad())) {
//            frase = convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad() + "\n\n";
//        }

        cell = new PdfPCell(new Phrase(frase, tipoLetra8));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(6);
        tableDestinatario.addCell(cell);
        table.addCell(tableDestinatario);
        
        
        tableDestinatario = new PdfPTable(new float[] { 30, 70 });
        
        //tableDestinatario.setWidths(new int[] {100, 425 });
        //tableDestinatario.setTotalWidth(525);
        //tableDestinatario.getDefaultCell().setPadding(0);

        cell = new PdfPCell(new Phrase("CONSEJERÍA:", tipoLetra8));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setRowspan(6);
        cell.setPadding(6);
        tableDestinatario.addCell(cell);

        cell = new PdfPCell(new Phrase(strConsejeria, tipoLetra8));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(6);
        tableDestinatario.addCell(cell);
        table.addCell(tableDestinatario);
        
        document.add(table);
        
       /* PdfContentByte canvas = writer.getDirectContent();
        canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
        table.writeSelectedRows(0, -1, 40, 80, canvas);
        canvas.endMarkedContentSequence();*/

    }
    
    public void agregarDestinatarioOLD(PdfWriter writer, Document document,
            ConvocatoriaDTO convocatoriaDTO) throws DocumentException {
        agregarFilaVacio(document, 1);

        PdfPTable table = new PdfPTable(2);
        PdfPCell cell = null;
        String frase = StringUtils.EMPTY;

        table.setWidths(new int[] {100, 425 });
        table.setTotalWidth(525);
        table.getDefaultCell().setPadding(0);

        cell = new PdfPCell(new Phrase("DESTINATARIO", tipoLetra8Bold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(3);
        table.addCell(cell);

        // se imprime si idDireccion no es nulo
        if (Objects.nonNull(convocatoriaDTO.getIdDireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdDireccion().getDsUnidad())) {
            frase = convocatoriaDTO.getIdDireccion().getDsUnidad() + "\n\n";
        }
        if (Objects.nonNull(convocatoriaDTO.getIdSubdireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdSubdireccion().getDsUnidad())) {
            frase = convocatoriaDTO.getIdSubdireccion().getDsUnidad() + "\n\n";
        }
        if (Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa()) && StringUtils
                .isNotBlank(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad())) {
            frase = convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad() + "\n\n";
        }

        cell = new PdfPCell(new Phrase(frase, tipoLetra9));
        table.addCell(cell);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
        table.writeSelectedRows(0, -1, 40, 80, canvas);
        canvas.endMarkedContentSequence();

    }


    /**
     * Método para crear un cuerpo de tipo texto
     *
     * @param document Documento PDF
     * @param texto texto de la celda
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTextoCuerpo(Document document, String texto)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table.addCell(new Phrase(texto, tipoLetra9));
        table.addCell(new Phrase("\n"));

        document.add(table);
    }

    /**
     * Crea la tabla de documentación
     *
     * @param document the document
     * @param lstDocumentosDTO lista documentos
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoDocumentos(Document document,
            List<DocumentoDTO> lstDocumentosDTO) throws DocumentException {

        if (Boolean.FALSE.equals(lstDocumentosDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(4);
            tablaDocumentacion.setWidthPercentage(100);

            lstDocumentosDTO.sort(Comparator.comparing(DocumentoDTO::getCdFica));

            // cabecera de la tabla
            PdfPCell cCodigo = new PdfPCell(new Phrase("Código", tipoLetra10Bold));
            cCodigo.setBorder(Rectangle.BOTTOM);
            cCodigo.setHorizontalAlignment(Element.ALIGN_LEFT);
            cCodigo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cCodigo.setPadding(10);
            tablaDocumentacion.addCell(cCodigo);

            PdfPCell cTipo = new PdfPCell(new Phrase("Tipo", tipoLetra10Bold));
            cTipo.setBorder(Rectangle.BOTTOM);
            cTipo.setHorizontalAlignment(Element.ALIGN_LEFT);
            cTipo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cTipo.setPadding(10);
            tablaDocumentacion.addCell(cTipo);

            PdfPCell cArchivo = new PdfPCell(new Phrase("Archivo", tipoLetra10Bold));
            cArchivo.setBorder(Rectangle.BOTTOM);
            cArchivo.setHorizontalAlignment(Element.ALIGN_LEFT);
            cArchivo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cArchivo.setPadding(10);
            tablaDocumentacion.addCell(cArchivo);

            PdfPCell cEmpty = new PdfPCell(new Phrase("", tipoLetra10Bold));
            cEmpty.setBorder(Rectangle.BOTTOM);
            cEmpty.setHorizontalAlignment(Element.ALIGN_LEFT);
            cEmpty.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cEmpty.setPadding(10);
            tablaDocumentacion.addCell(cEmpty);

            // Cuerpo de la tabla - recorremos la lista de documentos
            for (DocumentoDTO dto : lstDocumentosDTO) {

                StringBuilder tipoDoc = new StringBuilder();

                if (Objects.isNull(dto.getTipoDoc())) {
                    tipoDoc.append(Constantes.Utiles.ETIQUETA_OTROS_MINUSCULA);
                } else {
                    tipoDoc.append(dto.getTipoDoc());
                }

                PdfPCell cellCdFica = new PdfPCell(
                        new Phrase(dto.getCdFica().toString(), tipoLetra9));
                cellCdFica.setBorder(Rectangle.NO_BORDER);
                cellCdFica.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellCdFica.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCdFica.setPadding(10);
                tablaDocumentacion.addCell(cellCdFica);

                PdfPCell cellTipoDoc = new PdfPCell(new Phrase(tipoDoc.toString(), tipoLetra9));
                cellTipoDoc.setBorder(Rectangle.NO_BORDER);
                cellTipoDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellTipoDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTipoDoc.setPadding(10);
                tablaDocumentacion.addCell(cellTipoDoc);

                PdfPCell cellNombreFichero = new PdfPCell(
                        new Phrase(dto.getNombreFichero(), tipoLetra9));
                cellNombreFichero.setBorder(Rectangle.NO_BORDER);
                cellNombreFichero.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellNombreFichero.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNombreFichero.setPadding(10);
                tablaDocumentacion.addCell(cellNombreFichero);

                PdfPCell cellEmpty = new PdfPCell(new Phrase("", tipoLetra9));
                cellEmpty.setBorder(Rectangle.NO_BORDER);
                cellEmpty.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellEmpty.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellEmpty.setPadding(10);
                tablaDocumentacion.addCell(cellEmpty);

            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Crea la tabla de notas
     *
     * @param document the document
     * @param lstEvaluacionNotaDTO lista notas
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoEvaluacionesNota(Document document,
            List<EvaluacionNotaDTO> lstEvaluacionNotaDTO, Integer nota) throws DocumentException {

        if (Boolean.FALSE.equals(lstEvaluacionNotaDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(lstEvaluacionNotaDTO.size());
            tablaDocumentacion.setWidthPercentage(100);

            // Recorremos la lista de evaluaciones de notas
            for (EvaluacionNotaDTO dto : lstEvaluacionNotaDTO) {

                StringBuilder nombreDocumento = new StringBuilder();
                nombreDocumento.append(dto.getNotaDTO().getDsValor() + " De " + dto.getNmDe()
                        + " a " + dto.getNmA());

                PdfPCell cDocumentacion = new PdfPCell(
                        new Phrase(nombreDocumento.toString(), tipoLetra10Bold));
                cDocumentacion.setBorder(Rectangle.NO_BORDER);
                cDocumentacion.setHorizontalAlignment(Element.ALIGN_LEFT);
                cDocumentacion.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cDocumentacion.setPadding(10);
                tablaDocumentacion.addCell(cDocumentacion);

            }

            for (EvaluacionNotaDTO dto : lstEvaluacionNotaDTO) {

                if (nota == null) {

                    PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
                    cellDoc.setBorder(Rectangle.BOX);
                    cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellDoc.setPadding(10);
                    tablaDocumentacion.addCell(cellDoc);

                } else {

                    if (nota >= dto.getNmDe() && nota <= dto.getNmA()) {

                        PdfPCell cellDoc = new PdfPCell(
                                new Phrase(nota.toString(), tipoLetra10Bold));
                        cellDoc.setBorder(Rectangle.BOX);
                        cellDoc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cellDoc.setPadding(10);
                        tablaDocumentacion.addCell(cellDoc);

                    } else {

                        PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra9));
                        cellDoc.setBorder(Rectangle.BOX);
                        cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cellDoc.setPadding(10);
                        tablaDocumentacion.addCell(cellDoc);
                    }

                }

            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Crea la tabla de notas
     *
     * @param document the document
     * @param lstEvaluacionNotaDTO lista notas
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoEvaluacionesJustecNota(Document document,
            List<EvaluacionJustecNotaDTO> lstEvaluacionNotaDTO, Integer nota)
            throws DocumentException {

        if (Boolean.FALSE.equals(lstEvaluacionNotaDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(lstEvaluacionNotaDTO.size());
            tablaDocumentacion.setWidthPercentage(100);

            // Recorremos la lista de evaluaciones de notas
            for (EvaluacionJustecNotaDTO dto : lstEvaluacionNotaDTO) {

                StringBuilder nombreDocumento = new StringBuilder();
                nombreDocumento.append(dto.getNotaDTO().getDsValor() + " De " + dto.getNmDe()
                        + " a " + dto.getNmA());

                PdfPCell cDocumentacion = new PdfPCell(
                        new Phrase(nombreDocumento.toString(), tipoLetra10Bold));
                cDocumentacion.setBorder(Rectangle.NO_BORDER);
                cDocumentacion.setHorizontalAlignment(Element.ALIGN_LEFT);
                cDocumentacion.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cDocumentacion.setPadding(10);
                tablaDocumentacion.addCell(cDocumentacion);

            }

            for (EvaluacionJustecNotaDTO dto : lstEvaluacionNotaDTO) {

                if (nota == null) {

                    PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
                    cellDoc.setBorder(Rectangle.BOX);
                    cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellDoc.setPadding(10);
                    tablaDocumentacion.addCell(cellDoc);

                } else {

                    if (nota >= dto.getNmDe() && nota <= dto.getNmA()) {

                        PdfPCell cellDoc = new PdfPCell(
                                new Phrase(nota.toString(), tipoLetra10Bold));
                        cellDoc.setBorder(Rectangle.BOX);
                        cellDoc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cellDoc.setPadding(10);
                        tablaDocumentacion.addCell(cellDoc);

                    } else {

                        PdfPCell cellDoc = new PdfPCell(new Phrase(" ", tipoLetra9));
                        cellDoc.setBorder(Rectangle.BOX);
                        cellDoc.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellDoc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cellDoc.setPadding(10);
                        tablaDocumentacion.addCell(cellDoc);
                    }

                }

            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Crea la tabla de apartado
     *
     * @param document the document
     * @param lstApartadoDTO lista apartados
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoEvaluacionesApart(Document document,
            List<ApartadoDTO> lstApartadoDTO, String itCalificacionGlobal)
            throws DocumentException {

        if (Boolean.FALSE.equals(lstApartadoDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(1);
            tablaDocumentacion.setWidthPercentage(100);

            // Recorremos la lista de apartados
            for (ApartadoDTO dto : lstApartadoDTO) {

                agregarTituloApartado(document, dto.getDsTitulo());

                if (dto.getNmCalificacionMax() == null
                       || dto.getNmCalificacionMax() == 0) {
                    agregarDescripcionLeft(document, dto.getTlDescripcion(), dto.getTlTexto(), null,
                            dto.getItInformativo(),null);
                }else if (dto.getNmCalificacionMax() != null
                        && dto.getNmCalificacionMax() != 0) {
                    agregarDescripcionLeft(document, dto.getTlDescripcion(), null, null,
                            dto.getItInformativo(),dto.getTlTexto());
                    agregarCalificacion(document, String.valueOf(dto.getNmCalificacionMax()));
                }
                agregarFilaVacio(document, 1);
            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Crea la tabla de apartado
     *
     * @param document the document
     * @param lstApartadoDTO lista apartados
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoEvaluacionesApartJustec(Document document,
            List<EvaluacionJustecApartaDTO> lstApartadoDTO, String itCalificacionGlobal)
            throws DocumentException {

        if (Boolean.FALSE.equals(lstApartadoDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(1);
            tablaDocumentacion.setWidthPercentage(100);

            // Recorremos la lista de apartados
            for (EvaluacionJustecApartaDTO dto : lstApartadoDTO) {

                agregarTituloApartado(document, dto.getDsTitulo());

                agregarDescripcionLeft(document, dto.getTlDescripcion(), dto.getTlTexto(), null, null,null);

                if (Constantes.AfirmacionNegacion.N.equals(itCalificacionGlobal)) {
                    agregarCalificacion(document, String.valueOf(dto.getNmCalificacionMax()));
                }
                agregarFilaVacio(document, 1);
            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Crea la tabla de apartado notas
     *
     * @param document the document
     * @param lstInfoEvalDTO lista apartados
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarListadoEvaluacionesApartNotas(Document document,
            List<InfApartadoDatoDTO> lstInfoEvalDTO, String itCalificacionGlobal)
            throws DocumentException {

        if (Boolean.FALSE.equals(lstInfoEvalDTO.isEmpty())) {

            PdfPTable tablaDocumentacion = new PdfPTable(1);
            tablaDocumentacion.setWidthPercentage(100);

            // Recorremos la lista de apartados
            for (InfApartadoDatoDTO dto : lstInfoEvalDTO) {

                agregarTituloApartado(document, dto.getAparDsTitulo());
                
                if ((dto.getAparItObligatorio() == null || !dto.getAparItObligatorio().equalsIgnoreCase(Constantes.AfirmacionNegacion.S) )
                        &&  dto.getApaNmCalificacionMax() != null  && dto.getApaNmCalificacionMax() > 0 ) {
                    agregarDescripcionLeft(document, dto.getAparTlDescripcion(), dto.getAparTlTexto(),
                            null, dto.getAparItInformativo(),dto.getAparNotDsComentario());
                    
                    
                    
                }else if((dto.getAparItObligatorio() == null || !dto.getAparItObligatorio().equalsIgnoreCase(Constantes.AfirmacionNegacion.S) )
                        &&  dto.getApaNmCalificacionMax() != null  && dto.getApaNmCalificacionMax() == 0) {
                    agregarDescripcionLeft(document, dto.getAparTlDescripcion(), dto.getAparTlTexto(),
                            dto.getAparNotDsComentario(), dto.getAparItInformativo(),null);
                }else  {
                    agregarDescripcionLeft(document, dto.getAparTlDescripcion(), dto.getAparTlTexto(),
                            dto.getAparNotDsComentario(), dto.getAparItInformativo(),null); 
                }
                
                
                if( dto.getApaNmCalificacionMax() != null
                        && dto.getApaNmCalificacionMax() != 0) {
                        agregarCalificacionAparNota(document,
                                String.valueOf(dto.getApaNmCalificacionMax()),
                                String.valueOf(dto.getAparNotNmNota()));
                    }
                agregarFilaVacio(document, 1);
            }

            document.add(tablaDocumentacion);
        }
    }

    /**
     * Método para crear el titulo de evaluación
     *
     * @param document Documento PDF
     * @param titulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloEvaluacion(Document document, String titulo)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(titulo, tipoLetra11Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);
//        table.addCell(new Phrase("\n")); 

        document.add(table);
    }

    /**
     * Método para crear el subtitulo con descripción o sin descripción y tipo
     * de letra
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarSubTituloDes(Document document, String subTitulo, String descripcion,
            Font tipoLetraTitulo, Font tipoLetraDescripcion, Integer numTableColumn)
            throws DocumentException {

        PdfPTable table = new PdfPTable(numTableColumn);

        if (numTableColumn > 1) {
            table.setWidths(new int[] {12, 88 });
        } else {
            table.setWidths(new int[] {1 });
        }

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetraTitulo));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        if (Objects.nonNull(descripcion)) {

            PdfPCell cells = null;

            cells = new PdfPCell(new Phrase(descripcion, tipoLetraDescripcion));
            cells.setBorder(Rectangle.NO_BORDER);

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cells.setHorizontalAlignment(Element.ALIGN_LEFT);

            table.addCell(cells);

        }

        document.add(table);
    }
    
    /**
     * Método para crear el subtitulo CENTRADO con descripción o sin descripción y tipo
     * de letra
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarSubTituloDesCentrado(Document document, String subTitulo, String descripcion,
            Font tipoLetraTitulo, Font tipoLetraDescripcion, Integer numTableColumn)
                    throws DocumentException {
        
        this.agregarFilaVacio(document, 1);
        
        PdfPTable table = new PdfPTable(numTableColumn);
        
        if (numTableColumn > 1) {
            table.setWidths(new int[] {12, 88 });
        } else {
            table.setWidths(new int[] {1 });
        }
        
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        
        PdfPCell cell = null;
        
        cell = new PdfPCell(new Phrase(subTitulo, tipoLetraTitulo));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        table.addCell(cell);
        
        if (Objects.nonNull(descripcion)) {
            
            PdfPCell cells = null;
            
            cells = new PdfPCell(new Phrase(descripcion, tipoLetraDescripcion));
            cells.setBorder(Rectangle.NO_BORDER);
            
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cells.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            table.addCell(cells);
            
        }
        
        document.add(table);
    }

    /**
     * Método para crear el titulo de valoración global
     *
     * @param document Documento PDF
     * @param titulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarValoracionGlobal(Document document, String titulo)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(titulo, tipoLetra11Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);
        table.addCell(new Phrase("\n"));

        document.add(table);
    }

    /**
     * Método para crear el titulo de alegación
     *
     * @param document Documento PDF
     * @param titulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloAlegacion(Document document, String titulo, float letterSize)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(
                new Phrase(titulo, new Font(FontFamily.HELVETICA, letterSize, Font.BOLD)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.addCell(new Phrase("\n"));

        document.add(table);
    }

    /**
     * Método para crear la descripción de evaluación
     *
     * @param document Documento PDF
     * @param descripcion
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarDescripcion(Document document, String descripcion)
            throws DocumentException {
        if (descripcion != null && !"".equals(descripcion)) {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(0);
        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(descripcion, tipoLetra10));
//        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(5);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        table.addCell(cell);
//        table.addCell(new Phrase("\n"));

        document.add(table);
    }
    }

    /**
     * Método para crear la descripción de asunto
     *
     * @param document Documento PDF
     * @param descripcion
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarDescripcionAsunto(Document document, String descripcion)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(descripcion, tipoLetra10));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(5);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        table.addCell(cell);
        table.addCell(new Phrase("\n"));

        document.add(table);
    }

    /**
     * Método para crear el titulo de apartado
     *
     * @param document Documento PDF
     * @param titulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloApartado(Document document, String titulo)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(titulo, tipoLetra11Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);
        document.add(table);
    }

    /**
     * Método para crear la descripción de evaluación apartado
     *
     * @param document Documento PDF
     * @param descripcion
     * @param textoApart
     * @param comentario
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarDescripcionLeft(Document document, String descripcion,
            String textoApart, String comentario, String itInformativo, String observacionEnSuCaso) throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPCell cell = null;
        if (StringUtils.isNotBlank(descripcion)) {

            cell = new PdfPCell(new Phrase(descripcion, tipoLetra10));
            cell.setBorderWidthBottom(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(5);
            cell.setPaddingTop(10);
            cell.setPaddingBottom(5);
            table.addCell(cell);

        }

        if (StringUtils.isNotBlank(textoApart)) {

            cell = new PdfPCell(new Phrase(textoApart, tipoLetra10));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorderWidthTop(Rectangle.NO_BORDER);
            cell.setPaddingLeft(5);
            cell.setPaddingBottom(10);
            table.addCell(cell);

        } /* else {

            cell = new PdfPCell(new Phrase(" ", tipoLetra10));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorderWidthTop(Rectangle.NO_BORDER);
            cell.setPaddingLeft(5);
            cell.setPaddingBottom(10);
            table.addCell(cell);

        } */
        if (itInformativo == null || itInformativo.equals(Constantes.AfirmacionNegacion.N)) {
            
            if(StringUtils.isNotBlank(observacionEnSuCaso)) {
                cell = new PdfPCell(new Phrase("Observaciones en su caso", tipoLetra10Underline));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorderWidthBottom(Rectangle.NO_BORDER);
                cell.setPaddingLeft(5);
                cell.setPaddingTop(10);
                cell.setPaddingBottom(0);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(observacionEnSuCaso, tipoLetra10));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorderWidthTop(Rectangle.NO_BORDER);
                cell.setPaddingLeft(5);
                cell.setPaddingTop(8);
                cell.setPaddingBottom(10);
                table.addCell(cell);
            }else if (StringUtils.isNotBlank(comentario)) {
    
                cell = new PdfPCell(new Phrase(comentario, tipoLetra10));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    //            cell.setBorderWidthTop(Rectangle.NO_BORDER);
                cell.setPaddingLeft(5);
                cell.setPaddingTop(10);
                cell.setPaddingBottom(10);
                table.addCell(cell);
    
            } else {
    
                cell = new PdfPCell(new Phrase(" ", tipoLetra10));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    //            cell.setBorderWidthTop(Rectangle.NO_BORDER);
                cell.setPaddingLeft(5);
                cell.setPaddingTop(10);
                cell.setPaddingBottom(10);
                table.addCell(cell);
            }
        }

        document.add(table);
    }

    /**
     * Método para crear la calificación maxima de evaluación apartado
     *
     * @param document Documento PDF
     * @param calificacion
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarCalificacion(Document document, String calificacion)
            throws DocumentException {
        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        if (calificacion != null) {

            cell = new PdfPCell(
                    new Phrase("CALIFICACIÓN (0 - " + calificacion + " puntos)", tipoLetra10Bold));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(10);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(10);
            table.addCell(cell);

        }

        document.add(table);
    }

    /**
     * Método para crear la calificación maxima de evaluación apartado nota
     *
     * @param document Documento PDF
     * @param calificacion
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarCalificacionAparNota(Document document, String calificacion,
            String nota) throws DocumentException {
        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        if (calificacion != null) {
            cell = new PdfPCell(
                    new Phrase("CALIFICACIÓN (0 - " + calificacion + " puntos)", tipoLetra10Bold));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(10);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota, tipoLetra10Bold));
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(10);
            table.addCell(cell);

        }

        document.add(table);
    }

    /**
     * Método para crear el titulo de cabecera expediente
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloCabeceraExpediente(Document document, String subTitulo,
            String descripcion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {12, 88 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        cells = new PdfPCell(new Phrase(descripcion, tipoLetra9));
        cells.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cells);

        document.add(table);
    }

    /**
     * Método para crear el titulo de cabecera asunto
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloCabeceraAsunto(Document document, String subTitulo,
            String descripcion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {12, 88 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        cells = new PdfPCell(new Phrase(descripcion, tipoLetra9));
        cells.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cells);

        document.add(table);
    }

    /**
     * Método para crear el titulo del expediente de evaluación.
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloExpedienteEval(Document document, String subTitulo)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(5);
        table.addCell(cell);

        document.add(table);
    }

    /**
     * Método para crear el titulo de cabecera coordinador
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloCabeceraCoorEval(Document document, String subTitulo,
            String descripcion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {25, 75 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        cells = new PdfPCell(new Phrase(descripcion, tipoLetra9));
        cells.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cells);

        document.add(table);
    }

    /**
     * Método para crear el titulo de cabecera fecha
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloCabeceraFecha(Document document, String subTitulo,
            String descripcion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {7, 93 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        cells = new PdfPCell(new Phrase(descripcion, tipoLetra10));
        cells.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cells);

        document.add(table);
    }

    /**
     * Método para crear el titulo de cabecera coordinador
     *
     * @param document Documento PDF
     * @param subTitulo
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento investigador
     */
    public void agregarTituloCabeceraInvestigador(Document document, String subTitulo,
            String descripcion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {10, 65 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(subTitulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        cells = new PdfPCell(new Phrase(descripcion, tipoLetra9));
        cells.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cells);

        document.add(table);
    }

    /**
     *
     * @param document
     * @param tipo
     * @throws DocumentException
     * @throws ServiceException 
     */

    public void agregarTipo(Document document, CatalogosValoresDTO tipo, String valor)
            throws DocumentException, ServiceException {

        PdfPTable tablaDocumentacion = new PdfPTable(1);
        tablaDocumentacion.setWidthPercentage(100);

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO).equals(tipo.getIdValor())) {
            if (Objects.isNull(valor)) {

                PdfPCell cellMultiTexto = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
                cellMultiTexto.setBorder(Rectangle.BOX);
                cellMultiTexto.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellMultiTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellMultiTexto.setPadding(20);
                tablaDocumentacion.addCell(cellMultiTexto);

            } else {
                PdfPCell cellMultiTexto = new PdfPCell(new Phrase(valor, tipoLetra10Bold));
                cellMultiTexto.setBorder(Rectangle.BOX);
                cellMultiTexto.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellMultiTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellMultiTexto.setPadding(20);
                tablaDocumentacion.addCell(cellMultiTexto);
            }

        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA).equals(tipo.getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_NUMERO).equals(tipo.getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_TEXTO).equals(tipo.getIdValor())) {

            if (Objects.isNull(valor)) {
                PdfPCell celltext = new PdfPCell(new Phrase(" ", tipoLetra10Bold));
                celltext.setBorder(Rectangle.BOX);
                celltext.setHorizontalAlignment(Element.ALIGN_LEFT);
                celltext.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celltext.setPadding(5);
                tablaDocumentacion.addCell(celltext);
            } else {
                PdfPCell celltext = new PdfPCell(new Phrase(valor, tipoLetra10Bold));
                celltext.setBorder(Rectangle.BOX);
                celltext.setHorizontalAlignment(Element.ALIGN_LEFT);
                celltext.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celltext.setPadding(5);
                tablaDocumentacion.addCell(celltext);
            }
        }

        document.add(tablaDocumentacion);

    }

    public void agregarSubTituloPlantilla(Document document, String texto,
            String descripcion, int ancho, Boolean isHtml) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setWidths(new int[] {ancho, 65 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(texto, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);

        PdfPCell cells = null;

        if (Boolean.TRUE.equals(isHtml)) {
            Phrase frase = new Phrase("", tipoLetra10);
            String textoDesc = descripcion.replace("&nbsp;", "<br>");
            try {
                List<Element> elementos = HTMLWorker.parseToList(new StringReader(textoDesc), null);
                for (Element element : elementos) {
                    frase.add(element);
                }
                cells = new PdfPCell(frase);
                cells.setBorder(Rectangle.NO_BORDER);

                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cells.setHorizontalAlignment(Element.ALIGN_LEFT);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            cells = new PdfPCell(new Phrase(descripcion, tipoLetra10));
            cells.setBorder(Rectangle.NO_BORDER);

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cells.setHorizontalAlignment(Element.ALIGN_LEFT);
        }

        table.addCell(cells);

        document.add(table);
    }

    /**
     * Metodo para agregar contenido final al informe de trámite
     *
     * @param document
     * @param writer
     * @param convocatoriaPlaDTO
     * @param convocatoriaDTO
     * @throws DocumentException
     */
    public void agregarContenidoFinal(Document document, PdfWriter writer,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ConvocatoriaDTO convocatoriaDTO)
            throws DocumentException {
        float width = document.getPageSize().getWidth() / 2;
        PdfContentByte canvas = writer.getDirectContent();

        // VMM MD-2962:: la linea del firmante (dsFirmante) se pinta aparte y mas arriba, justo debajo de
        // "Firmado electrónicamente" (en y=175, ver agregarLugarFechaFirmaElectronica). El resto
        // del bloque (unidad administrativa/subdireccion/direccion/consejeria) se queda en su
        // posicion original, al pie de pagina.
        if (Objects.nonNull(convocatoriaPlaDTO)
                && Objects.nonNull(convocatoriaPlaDTO.getLstConvocatoriaPlaFirDTO())
                && StringUtils.isNotBlank(convocatoriaPlaDTO.getDsFirmante())) {
            PdfPTable tableFirmante = new PdfPTable(1);
            tableFirmante.setWidths(new float[] {width });
            tableFirmante.setTotalWidth(527);
            tableFirmante.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPCell cellFirma = new PdfPCell(
                    new Phrase(convocatoriaPlaDTO.getDsFirmante(), tipoLetra8));
            cellFirma.setBorder(Rectangle.NO_BORDER);
            cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFirmante.addCell(cellFirma);

            //MD-2962: Dejamos un hueco de un par de lineas en blanco respecto a "Firmado electrónicamente" (y=175)
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            tableFirmante.writeSelectedRows(0, -1, 40, 135, canvas);
            canvas.endMarkedContentSequence();
        }
        

        PdfPTable table = new PdfPTable(1);
        table.setWidths(new float[] {width });
        table.setTotalWidth(527);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        if (Objects.nonNull(convocatoriaDTO)
                && Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa())) {
            PdfPCell cellArea = new PdfPCell(new Phrase(
                    convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad(), tipoLetra8));
            cellArea.setBorder(Rectangle.NO_BORDER);
            cellArea.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellArea);
        }

        if (Objects.nonNull(convocatoriaDTO)
                && Objects.nonNull(convocatoriaDTO.getIdSubdireccion())) {
            PdfPCell cellSubdireccion = new PdfPCell(
                    new Phrase(convocatoriaDTO.getIdSubdireccion().getDsUnidad(), tipoLetra8));
            cellSubdireccion.setBorder(Rectangle.NO_BORDER);
            cellSubdireccion.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellSubdireccion);
        }

        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdDireccion())) {
            PdfPCell cellDireccion = new PdfPCell(
                    new Phrase(convocatoriaDTO.getIdDireccion().getDsUnidad(), tipoLetra8));
            cellDireccion.setBorder(Rectangle.NO_BORDER);
            cellDireccion.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellDireccion);
        }

        PdfPCell cellConsejeria = new PdfPCell(
                new Phrase(Constantes.Plantilla.TEXTO_CONTENIDO_CONSEJERIA, tipoLetra8));
        cellConsejeria.setBorder(Rectangle.NO_BORDER);
        cellConsejeria.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cellConsejeria);

        canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
        table.writeSelectedRows(0, -1, 40, 80, canvas);
        canvas.endMarkedContentSequence();
    }
    
//    public void agregarContenidoFinalAdministrativo(Document document, PdfWriter writer,
//            ConvocatoriaPlaDTO convocatoriaPlaDTO, ConvocatoriaDTO convocatoriaDTO) throws DocumentException {
//
//        PdfPTable table = new PdfPTable(1);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(15f); // Margen de separación respecto a la firma
//        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//
//        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa())) {
//            PdfPCell cellArea = new PdfPCell(new Phrase(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad(), tipoLetra8));
//            cellArea.setBorder(Rectangle.NO_BORDER);
//            cellArea.setHorizontalAlignment(Element.ALIGN_LEFT);
//            table.addCell(cellArea);
//        }
//
//        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdSubdireccion())) {
//            PdfPCell cellSubdireccion = new PdfPCell(new Phrase(convocatoriaDTO.getIdSubdireccion().getDsUnidad(), tipoLetra8));
//            cellSubdireccion.setBorder(Rectangle.NO_BORDER);
//            cellSubdireccion.setHorizontalAlignment(Element.ALIGN_LEFT);
//            table.addCell(cellSubdireccion);
//        }
//
//        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdDireccion())) {
//            PdfPCell cellDireccion = new PdfPCell(new Phrase(convocatoriaDTO.getIdDireccion().getDsUnidad(), tipoLetra8));
//            cellDireccion.setBorder(Rectangle.NO_BORDER);
//            cellDireccion.setHorizontalAlignment(Element.ALIGN_LEFT);
//            table.addCell(cellDireccion);
//        }
//
//        PdfPCell cellConsejeria = new PdfPCell(new Phrase(Constantes.Plantilla.TEXTO_CONTENIDO_CONSEJERIA, tipoLetra8));
//        cellConsejeria.setBorder(Rectangle.NO_BORDER);
//        cellConsejeria.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table.addCell(cellConsejeria);
//
//        // EN LUGAR DE writeSelectedRows(..., 80), LO AÑADIMOS AL FLUJO
//        document.add(table);
//    }
    
    
    public void agregarContenidoFinalAdministrativo(Document document, PdfWriter writer,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ConvocatoriaDTO convocatoriaDTO) {

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginMarkedContentSequence(PdfName.ARTIFACT);

        Paragraph pOrganismo = new Paragraph();
        pOrganismo.setLeading(9.5f); // Interlineado ajustado entre líneas

        // 1. Área
        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad())) {
            pOrganismo.add(new Chunk(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad() + "\n", tipoLetra8));
        }

        // 2. Subdirección
        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdSubdireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdSubdireccion().getDsUnidad())) {
            pOrganismo.add(new Chunk(convocatoriaDTO.getIdSubdireccion().getDsUnidad() + "\n", tipoLetra8));
        }

        // 3. Dirección General
        if (Objects.nonNull(convocatoriaDTO) && Objects.nonNull(convocatoriaDTO.getIdDireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdDireccion().getDsUnidad())) {
            pOrganismo.add(new Chunk(convocatoriaDTO.getIdDireccion().getDsUnidad() + "\n", tipoLetra8));
        }

        // 4. Consejería
        pOrganismo.add(new Chunk(Constantes.Plantilla.TEXTO_CONTENIDO_CONSEJERIA, tipoLetra8));

        // Fijamos las coordenadas en el pie de página (Abajo a la izquierda)
        // Coordenadas: llx = 36pt (margen izq), lly = 35pt (fondo), urx = 450pt (ancho máximo), ury = 100pt (altura límite)
        ColumnText ct = new ColumnText(canvas);
        ct.setSimpleColumn(36f, 35f, 450f, 100f);
        ct.addElement(pOrganismo);

        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Error al añadir el contenido administrativo final en el pie de página", e);
        }

        canvas.endMarkedContentSequence();
    }
    

    /**
     * Método para crear la descripción de las tablas en actuación
     *
     * @param document Documento PDF
     * @param descripcion
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarDescripcionActuacion(Document document, String descripcion)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(descripcion, tipoLetra10));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(5);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(5);
        table.addCell(cell);

        document.add(table);
    }
    
    public void agregarBloqueParticipantesContrato(Document document, 
            ConvocatoriaAct convocatoriaAct, List<ExpedienteDTO> listExpedientesParticipantes) throws DocumentException {   
        agregarFilaVacio(document, 1);
       // Se añade la cabecera de la tabla
        PdfPTable table;

        table = new PdfPTable(new float[] {100 });
        table.setWidthPercentage(100);

        String titulo = "Listado de participantes " + convocatoriaAct.getTipoActor().getDsValor()
                + "\n";
        titulo += StringUtils.isNotBlank(convocatoriaAct.getTlDescripcion())
                ? convocatoriaAct.getTlDescripcion()
                : StringUtils.EMPTY;

        PdfPCell cell = celdaEtiqueta(titulo, Boolean.TRUE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        document.add(table);
        agregarFilaVacio(document, 1);
        
            table = new PdfPTable(new float[] {20, 20, 10, 20,30 });
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("Expediente", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Tipo Contrato", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Identificación", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Nombre y apellidos", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            cell = celdaEtiqueta("Estado Contrato", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            

            for (ExpedienteDTO expedienteDTO:listExpedientesParticipantes) {
                String nombre = "_" +expedienteDTO.getIdExpediente();

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("expediente" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("tipoContrato" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("dni" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("nombreParticipante" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("EstadoContrato" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
               
            
            }
        
        

        document.add(table);
        
        agregarFilaVacio(document, 1);
        
    }
    
    public void agregarBloqueDatosInteresado(Document document, 
            ExpedienteDTO expediente, String titulo) throws DocumentException {   
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        
        PdfPTable tableResult = new PdfPTable(1);
        PdfPCell cell = null;

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        document.add(tableResult);
        
        PdfPTable table = new PdfPTable(new float[] { 12, 28,12, 48,});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);

        cell = celdaEtiqueta("NIF/NIE:", Boolean.TRUE);
        table.addCell(cell);

        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdNumIden(), "campo_nif",
                true);
        
        cell = celdaEtiqueta("Nombre:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNombre(), "campo_nombre",
                true);
        
        document.add(table);
        
        table = new PdfPTable(new float[] {12,38,12,38});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        
        cell = celdaEtiqueta("Apellido 1:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsApellido1(), "campo_Apellido1",
                true);
        
        cell = celdaEtiqueta("Apellido 2:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsApellido2(), "campo_Apellido2",
                true);
        
        
        document.add(table);
        
        
        if (this.esInteresadoExtranjero(expediente.getInteresadoDTO())) {
            table = new PdfPTable(new float[]  {20, 30, 20, 30 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = celdaEtiqueta("País:", Boolean.TRUE);
            table.addCell(cell);

            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPaisResidencia(), "campo_Pais",
                    true);

            cell = celdaEtiqueta("Nacionalidad:", Boolean.TRUE);
            table.addCell(cell);

            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNacionalidad(),
                    "campo_Nacionalidad", true);
            document.add(table);
            table = new PdfPTable(new float[]  {15,85 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = celdaEtiqueta("Dirección:", Boolean.TRUE);
            table.addCell(cell);

            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsDireccion(), "campo_Direccion", true);
            document.add(table);
        } else {
            table = new PdfPTable(new float[] {12,28,20,40 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = celdaEtiqueta("Tipo Vía:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdTipoVia(), "campo_TipoVia",
                    true);
            
            cell = celdaEtiqueta("Nombre Vía:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsDireccion(), "campo_NombreVia",
                    true);
           
            document.add(table);
            
            table = new PdfPTable(new float[] {12, 10, 8, 10, 8, 10, 9, 8,13, 12 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = celdaEtiqueta(" Número/Km:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNumero(), "campo_Numero",
                    true);
            cell = celdaEtiqueta("Piso:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPiso(), "campo_Piso",
                    true);
            
            cell = celdaEtiqueta("Puerta:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPuerta(), "campo_Puerta",
                    true);
            
            cell = celdaEtiqueta("Escalera:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsEscalera(), "campo_Escalera",
                    true);
            
            cell = celdaEtiqueta("Código postal:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdCodigoPostal(), "campo_CP",
                    true);
            
            document.add(table);
            
            table = new PdfPTable(new float[] {12,38,12,38});
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = celdaEtiqueta("Provincia:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsProvincia(), "campo_Provincia",
                    true);
            
            cell = celdaEtiqueta("Municipio:", Boolean.TRUE);
            table.addCell(cell);
            
            agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsLocalidad(), "campo_Localidad",
                    true);
            
            document.add(table);
            
        }
        
        table = new PdfPTable(new float[] {12, 30, 10, 19, 10, 19 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell =   celdaEtiqueta("e-mail:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsEmail(), "campo_Email",
                true);
        
        cell =   celdaEtiqueta("Teléfono 1:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsMovil(), "campo_Movil",
                true);

        cell =  celdaEtiqueta("Teléfono 2:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsTelefono(), "campo_Fijo",
                true);
        
        document.add(table);
        
       /* table = new PdfPTable(new float[] {15, 25, 15, 15 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        //table.getDefaultCell().setBorder(Rectangle.BOX);
        
        cell =   celdaEtiqueta("País de residencia", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPaisResidencia(), "campo_Pais",
                true);
        
        cell =   celdaEtiqueta("Nacionalidad", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNacionalidad(), "campo_Nacionalidad",
                true);
        
        document.add(table);*/
        
       // agregarFilaVacio(document, 1);
        
    }
    
    private boolean esInteresadoExtranjero(InteresadoDTO interesado) {
        String pais = normalizarTexto(interesado.getDsPaisResidencia());
        return StringUtils.isNotBlank(pais) && !"es".equals(pais) && !"esp".equals(pais)
                && !"espana".equals(pais);
    }
    
    private void agregarBloqueTablaPresupuestoConcepto(Document document,
            BloqueDTO bloque, Integer nmDurMaxAyuda,Double intensidad, boolean esEditable, Boolean mostrarAyuda,FinanciacionDTO financiacionDTO,
            Integer idTipoImpreso) throws  ServiceException {

        boolean esModificacionPresupPlazosOtros = idTipoImpreso != null && idTipoImpreso.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));

        try {
            // Quita los bloqueComponente de baja y ordena
            List<BloqueComponenteDTO> lstBloqueComponentes = obtenerComponentes(bloque);
            List<BloqueComponenteDTO> filasAux = lstBloqueComponentes.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());
            
            List<BloqueComponenteDTO> filas = new ArrayList<>();
            
            for(BloqueComponenteDTO bloqueComp:filasAux) {
                if(bloqueComp.getDatos().size()< nmDurMaxAyuda.intValue()+1) {
                    filas.add(bloqueComp);
                }else if(bloqueComp.getDatos().size()== nmDurMaxAyuda.intValue()+1) {
                    bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList()));
                    filas.add(bloqueComp);
                }else if(bloqueComp.getDatos().size()> nmDurMaxAyuda.intValue()+2) {
                    int posCelda= 0;
                    bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList()));
                    for (int j=posCelda; j< bloqueComp.getDatos().size();j++){
                        List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                        BloqueComponenteDTO bloqueComConcepto= new BloqueComponenteDTO();
                        for (int index = 0; index <  nmDurMaxAyuda.intValue()+1; index++) {
                            datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                            posCelda++;
                            j=posCelda;
                        }
                        
                        bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                        datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                        bloqueComConcepto.setDatos(datosFilaConcepto);
                        bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                        bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                        bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                        bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                        bloqueComConcepto.setItFila(bloqueComp.getItFila());
                        bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                        bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                        bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                        bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                        bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                        filas.add(bloqueComConcepto);
                        
                    }
                       
                        
                }
            }
            
            List<FinanciacionDatoDTO> listaFinancDato= financiacionDTO.getListaFinanciacionDatoDTO();
            
            if(!filas.isEmpty()) {
                filas = filas.stream()
                        .sorted((c1, c2) -> compareByNumOrden(c1, c2, listaFinancDato))
                        .collect(Collectors.toList());

            }
           
           /* filas.sort((o1, o2) -> {
                List<FinanciacionDatoDTO> listaFinancDatosO1= listaFinanciacionDatos.stream().filter(e-> e.getTipoDTO().getIdValor().equals(o1.getTipoFinanciacionDTO().getIdValor())).collect(Collectors.toList());
                List<FinanciacionDatoDTO> listaFinancDatosO2= listaFinanciacionDatos.stream().filter(e-> e.getTipoDTO().getIdValor().equals(o2.getTipoFinanciacionDTO().getIdValor())).collect(Collectors.toList());
                
                if(!listaFinancDatosO1.isEmpty() && listaFinancDatosO1.get(0).getNmOrden() != null && !listaFinancDatosO2.isEmpty() && listaFinancDatosO2.get(0).getNmOrden() != null) {
                    return listaFinancDatosO1.get(0).getNmOrden() - listaFinancDatosO2.get(0).getNmOrden();
                }
                
                return 0;
                     
            });*/
            
            //ordenar partidas presupuestarias
            

            PdfPCell cell = null;

            // creacion de la tabla
            int numColumnas = nmDurMaxAyuda + (nmDurMaxAyuda == 1 ? 2 : 3);
            if (esModificacionPresupPlazosOtros) {
                numColumnas++;
            }
            PdfPTable table = new PdfPTable(numColumnas);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorder(Rectangle.BOX);

            // Creación de la cabecera
            cell = celdaEtiquetaCabecera(" ", true);
            table.addCell(cell);

            table.addCell(celdaEtiquetaCabecera( "CONCEPTO", true));
            if(nmDurMaxAyuda== 1) {
                table.addCell(celdaEtiquetaCabecera("IMPORTE", true));
            }else{
                for (int i = 0; i < nmDurMaxAyuda; i++) {
                    Integer id = i + 1;
                    table.addCell(celdaEtiquetaCabecera(id + "° " + "Anualidad", true));
                }
                cell = celdaEtiquetaCabecera("TOTAL", true);
                table.addCell(cell);
            }
            if (esModificacionPresupPlazosOtros) {
                table.addCell(celdaEtiquetaCabecera("DOCUMENTO", true));
            }


            // creacion de cuerpo
             
            int indexFilaTotal = 1;
            int indexFila=1;
            for (BloqueComponenteDTO fila : filas) {
                table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));
                if(!fila.getDatos().isEmpty()) {
                    indexFila = fila.getDatos().get(0).getNmFila()+1;
                }
                if(nmDurMaxAyuda == 1) {
                    for (int i = 0; i <= nmDurMaxAyuda; i++) {
                        String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                + fila.getIdBloqueComponente() + "_" + indexFila + "_" + (i + 1);
                        if(i== 0) {
                            addCampoCeldaTextoLargo(esEditable, table, nombreCampo);
                        }else {
                            addCampoCelda(esEditable, table, nombreCampo); 
                        }
                    }  
                }else {
                    for (int i = 0; i <= nmDurMaxAyuda+1; i++) {
                        String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                + fila.getIdBloqueComponente() + "_" + indexFila + "_" + (i + 1);
                        if (i == nmDurMaxAyuda+1) {
                            nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + indexFilaTotal;
                        }
                        if(i== 0) {
                            addCampoCeldaTextoLargo(esEditable, table, nombreCampo);
                        }else {
                            addCampoCelda(esEditable, table, nombreCampo); 
                        }
                    }  
                }

                if (esModificacionPresupPlazosOtros) {
                    DatoDTO datoConcepto = fila.getDatos().stream()
                            .filter(d -> d.getNmColumna() != null && d.getNmColumna() == 0)
                            .findFirst().orElse(null);
                    String nombreDocumento = "";
                    if (datoConcepto != null && datoConcepto.getIdDato() != null) {
                        String nombre = this.expedienteDocService
                                .obtenerNombreDocumentoConceptoPresupuesto(datoConcepto.getIdDato());
                        if (nombre != null) {
                            nombreDocumento = nombre;
                        }
                    }
                    table.addCell(celdaEtiqueta(nombreDocumento, true));
                }

                indexFilaTotal++;

            }

            // Creación de pie de tabla
            
            if(nmDurMaxAyuda == 1) {
                PdfPCell pdfPCell = new PdfPCell(new Phrase("TOTAL", tipoLetra8));
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setPadding(6);
                pdfPCell.setColspan(2);
                table.addCell(pdfPCell);
                for (int i = 1; i <= nmDurMaxAyuda; i++) {
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                           + (i + 1);
                  addCampoCelda(esEditable, table, nombreCampo);

               }
               if (esModificacionPresupPlazosOtros) {
                   table.addCell(celdaEtiqueta(" ", true));
               }

            }else {
               
                PdfPCell pdfPCell = new PdfPCell(new Phrase("TOTAL", tipoLetra8));
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setPadding(6);
                pdfPCell.setColspan(2);
                table.addCell(pdfPCell);
                for (int i = 1; i <= nmDurMaxAyuda+1; i++) {
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                            + (i + 1);
                    if (i == nmDurMaxAyuda+1) {
                        nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (nmDurMaxAyuda + 1);
                    }
                   
                        addCampoCelda(esEditable, table, nombreCampo);


                }
                if (esModificacionPresupPlazosOtros) {
                    table.addCell(celdaEtiqueta(" ", true));
                }
            }
            
            //table.setSplitLate(false);
            document.add(table);
            
            // Añadir tabla de ayuda solicitada calculada en fucion de intensidad
            //Añadirla si intensidad < 100
            if(mostrarAyuda.equals(Boolean.TRUE) && intensidad != null && intensidad < 100) {
                // MD-1574 JRFM Formularios con financiación la intensidad texto inferior
                agregarFilaVacio(document, 1);
                table = new PdfPTable(new float[] {1 });
                table.setWidthPercentage(100);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell();
                table.addCell(new Phrase("Intensidad de la ayuda: " 
                        + BigDecimal.valueOf(intensidad).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%",
                        tipoLetra9));
                document.add(table);
                agregarFilaVacio(document, 1);
                
                String titulo= "Ayuda solicitada calculada en función del porcentaje de intensidad de ayuda";
                
               /* PdfPTable tableResult = new PdfPTable(1);
             
                tableResult.setWidths(new int[] {1 });
                tableResult.setWidthPercentage(100);
                tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                tableResult.getDefaultCell().setPadding(0);
                tableResult.getDefaultCell().setBorder(Rectangle.BOX);
                tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
                
                PdfPTable tableTitulo = new PdfPTable(1);
                tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                tableTitulo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                //tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
                
                PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
                
                celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
                celdaTituloSuperior.setPadding(6);
                tableTitulo.addCell(celdaTituloSuperior);
                tableResult.addCell(tableTitulo);*/
                
               // tableResult.setSplitLate(false);
               // document.add(tableResult);
                
                // creacion de la tabla
                if(nmDurMaxAyuda== 1) {
                    table = new PdfPTable(nmDurMaxAyuda + 2);
                    PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
                    celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celdaTituloSuperior.setPadding(6);
                    //cell.addElement(celdaTituloSuperior);
                   // cell.setColspan(nmDurMaxAyuda + 2);
                    celdaTituloSuperior.setColspan(nmDurMaxAyuda + 2);
                    table.addCell(celdaTituloSuperior);
                }else {
                    table = new PdfPTable(nmDurMaxAyuda + 3);
                    //cell.addElement(tableResult);
                    //cell.setColspan(nmDurMaxAyuda + 3);
                    PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
                    celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celdaTituloSuperior.setPadding(6);
                    celdaTituloSuperior.setColspan(nmDurMaxAyuda + 3);
                    table.addCell(celdaTituloSuperior);
                }
                
                table.setWidthPercentage(100);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                table.getDefaultCell().setBorder(Rectangle.BOX);
    
                // Creación de la cabecera
                cell = celdaEtiquetaCabecera(" ", true);
                table.addCell(cell);
                
                table.addCell(celdaEtiquetaCabecera( "CONCEPTO", true));
                if(nmDurMaxAyuda== 1) {
                    table.addCell(celdaEtiquetaCabecera("IMPORTE", true));
                }else {
                    for (int i = 0; i < nmDurMaxAyuda; i++) {
                        Integer id = i + 1;
                        table.addCell(celdaEtiquetaCabecera(id + "° " + "Anualidad", true));
                    }
        
                    cell = celdaEtiquetaCabecera("TOTAL", true);
                    table.addCell(cell); 
                }
                
    
                // creacion de cuerpo
                
                indexFilaTotal=1;
                indexFila = 1;
                for (BloqueComponenteDTO fila : filas) {
                    if(!fila.getDatos().isEmpty()) {
                        indexFila = fila.getDatos().get(0).getNmFila()+1;
                    }
                     
                    table.addCell(celdaEtiqueta(fila.getTipoFinanciacionDTO().getDsValor(), true));
                    if(nmDurMaxAyuda == 1) {
                        for (int i = 0; i <= nmDurMaxAyuda; i++) {
                            String nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + indexFila + "_" + (i + 1);
                            if(i== 0) {
                                addCampoCeldaTextoLargo(esEditable, table, nombreCampo);
                            }else {
                                addCampoCelda(esEditable, table, nombreCampo); 
                            }
                            
                        }
                    }else{
                        for (int i = 0; i <= nmDurMaxAyuda+1; i++) {
                            String nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + fila.getIdBloqueComponente() + "_" + indexFila + "_" + (i + 1);
                            if (i == nmDurMaxAyuda+1) {
                                nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                                        + fila.getIdBloqueComponente() + "_" + indexFilaTotal;
                            }
                            if(i== 0) {
                                addCampoCeldaTextoLargo(esEditable, table, nombreCampo);
                            }else {
                                addCampoCelda(esEditable, table, nombreCampo); 
                            }
                            
                        }
                    }
                   
                    indexFilaTotal++;
                   
                }
    
                // Creación de pie de tabla
                //table.addCell(celdaEtiqueta("TOTAL", true));
                PdfPCell pdfPCell = new PdfPCell(new Phrase("TOTAL", tipoLetra8));
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setPadding(6);
                pdfPCell.setColspan(2);
                table.addCell(pdfPCell);
                if(nmDurMaxAyuda == 1) {
                    for (int i = 1; i <= nmDurMaxAyuda; i++) {
                        String nombreCampo = "ATC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (i + 1);
                       addCampoCelda(esEditable, table, nombreCampo);
                    }
                }else {
                    for (int i = 1; i <= nmDurMaxAyuda+1; i++) {
                        String nombreCampo = "ATC_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                + (i + 1);
                        if (i == nmDurMaxAyuda+1) {
                            nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (filas.size() + 1) + "_"
                                    + (nmDurMaxAyuda + 1);
                        }
                        addCampoCelda(esEditable, table, nombreCampo);
                    }
                }
                
               // table.setSplitLate(false);
                document.add(table);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
    
    public void agregarBloqueParticipantesSubsanacionReq(Document document, 
            ConvocatoriaAct convocatoriaAct, List<ExpedienteDTO> listExpedientesParticipantes) throws DocumentException {   
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        PdfPTable table;

        table = new PdfPTable(new float[] {100 });
        table.setWidthPercentage(100);

        String titulo = "Listado de participantes " + convocatoriaAct.getTipoActor().getDsValor()
                + "\n";
        titulo += StringUtils.isNotBlank(convocatoriaAct.getTlDescripcion())
                ? convocatoriaAct.getTlDescripcion()
                : StringUtils.EMPTY;

        PdfPCell cell = celdaEtiqueta(titulo, Boolean.TRUE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        document.add(table);
        agregarFilaVacio(document, 1);
        
            table = new PdfPTable(new float[] {20, 20, 10, 20,30 });
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("Expediente", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Identificación", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Nombre y apellidos", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            cell = celdaEtiqueta("Estado Beneficiario", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            for (ExpedienteDTO expedienteDTO:listExpedientesParticipantes) {
                String nombre = "_" +expedienteDTO.getIdExpediente();

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("expediente" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("dni" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("nombreParticipante" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new InlineCellEvent("estadoBeneficiario" + nombre, BaseField.EDIT));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            
            }
            
        document.add(table);
        
        agregarFilaVacio(document, 1);
        
    }
    
    public void agregarBloqueDatosRepresentanteLegalSol(Document document, 
            RepresentanteJurDTO representanteLegal, String titulo) throws DocumentException { 
        
        if( representanteLegal== null ||representanteLegal.getInteresadoDTO()== null ) {
            representanteLegal=new RepresentanteJurDTO();
            representanteLegal.setInteresadoDTO(new InteresadoDTO());
        }
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = null;

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        table.addCell(tableTitulo);
        
        document.add(table);
        
        table = new PdfPTable(new float[] {12, 32, 20, 36});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        cell = celdaEtiqueta("NIF/NIE:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getCdNumIden(), "campo_Rep_nif",
                true);
        
        cell = celdaEtiqueta("Razón social / entidad:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table,"", "campo_Rep_nombre_entidad",
                true);
        
        document.add(table);
        
        table = new PdfPTable(new float[] {12,26,10,21,10,21});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        cell = celdaEtiqueta("Nombre:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsNombre(), "campo_Rep_nombre",
                true);
        
        cell = celdaEtiqueta("Apellido 1:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsApellido1(), "campo_Rep_Apellido1",
                true);
        
        cell = celdaEtiqueta("Apellido 2:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsApellido2(), "campo_Rep_Apellido2",
                true);
        
        document.add(table);
       
        table = new PdfPTable(new float[] {12, 30, 14, 15, 14, 15}); 
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
       
        cell = celdaEtiqueta("e-mail:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsEmail(), "campo_Rep_Email",
                true);
        
        cell = celdaEtiqueta("Teléfono 1:", Boolean.TRUE);
        //cell.setPadding(8);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsMovil(), "campo_Rep_Movil",
                true);
        
        cell = celdaEtiqueta("Teléfono 2:", Boolean.TRUE);
       //cell.setPadding(8);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getInteresadoDTO().getDsTelefono(), "campo_Rep_Telefono",
                true);
        
        
        document.add(table);
        
        table = new PdfPTable(new float[] {15, 85});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        cell = celdaEtiqueta("En calidad de:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, representanteLegal.getDsCargo(), "campo_Rep_Cargo",
                true);
         
       
        document.add(table);
        
        
        
        
    }
    
    private void agregarCeldaInteresado(PdfPTable table, String valorAtributo, String nombreCampo, boolean esEditable) {
        // Crear celda con el valor o espacio en blanco si está vacío
        PdfPCell cell = new PdfPCell(new Phrase(
            StringUtils.isBlank(valorAtributo) ? " " : valorAtributo, 
            tipoLetra8
        ));
        
        // Configurar como editable si es necesario
        if (esEditable) {
            cell.setCellEvent(new TextFieldCellEvent(nombreCampo, BaseField.EDIT));
        }
        
        // Estilos comunes
        cell.setBorder(Rectangle.BOX);
        cell.setBorderWidth(1f);
        cell.setBorderColor(BaseColor.BLACK);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        table.addCell(cell);
    }
    
    
    public void agregarBloqueDatosOrganismoSolicitante(Document document, 
            ExpedienteDTO expediente, String titulo) throws DocumentException {   
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        PdfPTable tableResult = new PdfPTable(1);
        PdfPCell cell = null;

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        
        PdfPTable table = new PdfPTable(new float[] {12, 40,20,28});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = celdaEtiqueta("NIF/NIE:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdNumIden(), "campo_NumIden",
                true);
        
        cell = celdaEtiqueta("Razón social / entidad:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNombre(), "campo_Nombre",
                true);
        
        tableResult.addCell(table);
        
        table = new PdfPTable(new float[] {12,28,20,40 });
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = celdaEtiqueta("Tipo Vía:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdTipoVia(), "campo_TipoVia",
                true);
        
        cell = celdaEtiqueta("Nombre Vía:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsDireccion(), "campo_Direccion",
                true);

        tableResult.addCell(table);
        
        table = new PdfPTable(new float[] {12, 10, 8, 10, 8, 10, 9, 8,13, 12});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = celdaEtiqueta("Número/Km:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsNumero(), "campo_Numero",
                true);
        
        cell = celdaEtiqueta("Piso:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPiso(), "campo_Piso",
                true);

        cell = celdaEtiqueta("Puerta:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsPuerta(), "campo_Puerta",
                true);

        cell = celdaEtiqueta("Escalera:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsEscalera(), "campo_Escalera",
                true);
       
        cell = celdaEtiqueta("Código postal:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getCdCodigoPostal(), "campo_CodigoPostal",
                true);

        tableResult.addCell(table);
        
        table = new PdfPTable(new float[] {12,38,12,38});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = celdaEtiqueta("Provincia:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsProvincia(), "campo_Provincia",
                true);

        cell = celdaEtiqueta("Localidad:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsLocalidad(), "campo_Localidad",
                true);
        
        tableResult.addCell(table);
        
        table = new PdfPTable(new float[] {12, 30, 10, 19, 10, 19 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell =   celdaEtiqueta("e-mail:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsEmail(), "campo_Email",
                true);
        cell = celdaEtiqueta("Teléfono 1:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsMovil(), "campo_Movil",
                true);
        
        cell = celdaEtiqueta("Teléfono 2:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expediente.getInteresadoDTO().getDsTelefono(), "campo_Telefono",
                true);
        tableResult.addCell(table);
        document.add(tableResult);
        
        
    }
    
    
    public void agregarBloqueParticipantesOrganismo(Document document,
           List<ExpedienteDTO> listaParticipantes, String titulo) throws DocumentException, ServiceException {
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        
        PdfPTable tableResult = new PdfPTable(1);
        PdfPCell cell = null;

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        
        PdfPTable table = new PdfPTable(new float[] {14, 26, 20, 20, 20});
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("NIF/NIE ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            String tipoActor="";
            
            if(listaParticipantes != null && !listaParticipantes.isEmpty() && listaParticipantes.get(0).getConvocatoriaActDTO() != null &&listaParticipantes.get(0).getConvocatoriaActDTO().getTipoActorDTO() != null ) {
                tipoActor = listaParticipantes.get(0).getConvocatoriaActDTO().getTipoActorDTO().getDsValor();
            }

            cell = celdaEtiqueta("Nombre y apellido del "+tipoActor, Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Nº referencia de la ayuda solicitada ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

           

            cell = celdaEtiqueta("Presupuesto solicitado para la financiación adicional (euros) ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("Área científica (según Anexo I) ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            tableResult.addCell(table);
           

            if(!listaParticipantes.isEmpty()) {
                for (ExpedienteDTO expParticipante : listaParticipantes) {
                    table = new PdfPTable(new float[] {14, 26, 20, 20, 20 });
                    table.setWidthPercentage(100);
                    cell = celdaEtiqueta(expParticipante.getInteresadoDTO().getCdNumIden(), Boolean.TRUE);
                    table.addCell(cell);
                    String nombre= expParticipante.getInteresadoDTO().getDsNombre();
                    if(expParticipante.getInteresadoDTO().getDsApellido1() != null) {
                        nombre = nombre+" "+expParticipante.getInteresadoDTO().getDsApellido1();
                    }
                    if(expParticipante.getInteresadoDTO().getDsApellido2() != null) {
                        nombre = nombre+" "+expParticipante.getInteresadoDTO().getDsApellido2();
                    }
                    cell = celdaEtiqueta(nombre, Boolean.TRUE);
                    table.addCell(cell);

                    cell = celdaEtiqueta(expParticipante.getCdExpediente(), Boolean.TRUE);
                    table.addCell(cell);

                    
                    BigDecimal decimalMontoTotalColumna = this.pdfCamposService
                            .calcularImporteTotalPresupuesto(expParticipante);
                    String presupuestoSolicitado = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);

                    cell = celdaEtiqueta(presupuestoSolicitado, Boolean.TRUE);
                    table.addCell(cell);
                    
                    String area = expParticipante.getConvocatoriaAreaDTO() != null
                            && expParticipante.getConvocatoriaAreaDTO().getAreaDTO() != null
                            && expParticipante.getConvocatoriaAreaDTO().getAreaDTO().getDsValor() != null
                            ? expParticipante.getConvocatoriaAreaDTO().getAreaDTO().getDsValor() : "";

                    cell = celdaEtiqueta(area, Boolean.TRUE);
                    table.addCell(cell);
                    
                    tableResult.addCell(table);
                 }
            }else {

                for (int i = 0; i < 3; i++) {
                    table = new PdfPTable(new float[] {14, 26, 20, 20, 20 });
                    table.setWidthPercentage(100);

                    PdfPCell cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    cell2 = celdaEtiqueta(" ", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    tableResult.addCell(table);

                }
            }
            
            document.add(tableResult);

        agregarFilaVacio(document, 1);

        PdfPTable tableTotalCandidatos = new PdfPTable(new float[] {70, 30});
        tableTotalCandidatos.setWidthPercentage(100);

        cell = celdaEtiqueta("Nº total de  candidatos presentados en la solicitud", Boolean.TRUE);
        tableTotalCandidatos.addCell(cell);

        cell = celdaEtiqueta(String.valueOf(listaParticipantes.size()), Boolean.TRUE);
        tableTotalCandidatos.addCell(cell);

        document.add(tableTotalCandidatos);

        agregarFilaVacio(document, 1);
    }

    @Override
    public void agregarBloqueMultiregistro(PdfWriter writer, Document document, BloqueDTO bloque,
            List<BloqueDTO> listaSubbloqueDTO, Date fechaFinPresentacion)
            throws DocumentException {
       
        
//        int totalRegistros= 0;
        int totalComponentes=0;
        
        agregarTituloBloque( document,  bloque);
        
        if(!listaSubbloqueDTO.isEmpty()) {
            for(BloqueDTO subBloque:listaSubbloqueDTO) {
                if(subBloque.getEsTipoDocumento().equals(Boolean.FALSE)
                        && !subBloque.getLstBloqueComponenteDTO().isEmpty()){
//                    BloqueComponenteDTO blqComp= subBloque.getLstBloqueComponenteDTO().get(0);
                    totalComponentes = totalComponentes+subBloque.getLstBloqueComponenteDTO().size();
//                    if(blqComp.getDatos() != null && !blqComp.getDatos().isEmpty()) {
//                        totalRegistros=Math.max(totalRegistros, blqComp.getDatos().size());
//                    }
                }
            }
            
            List<Integer> filasMultiregistro = obtenerFilasMultiregistro(listaSubbloqueDTO);
            
            // Si ningún bloqueComponente tiene datos (p.ej. sin expediente todavía),
            // pintamos igualmente una fila en blanco para mostrar la tabla vacía.
            if(filasMultiregistro.isEmpty()) {
                filasMultiregistro.add(0);
            }
            
           /* agregarFilaVacio(document, 1);
            // Se añade la cabecera de la tabla
            PdfPTable table = new PdfPTable(new float[] {1 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            
            table.addCell(new Phrase(bloque.getIdTematica().getDsValor(),
                    tipoLetra9Bold));
            document.add(table);*/
                       
            //agregarFilaVacio(document, 1);
            
            boolean mostrarCalculosVinculacion = this.esBloqueVinculaciones(listaSubbloqueDTO)
                    && fechaFinPresentacion != null;
            if (mostrarCalculosVinculacion) {
                totalComponentes += 3;
            }
            
            float[] cabeceraTabla = new float[totalComponentes];
            for(int i= 0; i< totalComponentes;i++) {
                cabeceraTabla[i]=(float)100/totalComponentes;
            }
            
            PdfPTable table = new PdfPTable(cabeceraTabla);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            for(BloqueDTO subBloque:listaSubbloqueDTO) {
                if(subBloque.getEsTipoDocumento().equals(Boolean.FALSE) 
                        && !subBloque.getLstBloqueComponenteDTO().isEmpty()){
                    for(BloqueComponenteDTO blqComp:subBloque.getLstBloqueComponenteDTO()) {
                        PdfPCell cell = celdaEtiqueta(blqComp.getComponenteDTO().getDsNombre(), Boolean.TRUE);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                        //Si hay que mostrar  datosVinculacion
                        if (mostrarCalculosVinculacion && this.esComponente(blqComp, "Fecha inicio")) {
                            table.addCell(celdaCabeceraMultiregistro("Fecha inicio computable"));
                        }
                        if (mostrarCalculosVinculacion && this.esComponente(blqComp, "Fecha fin")) {
                            table.addCell(celdaCabeceraMultiregistro("Fecha fin computable"));
                            table.addCell(celdaCabeceraMultiregistro("Tiempo vinculación computable (años)"));
                        }
                    }
                } 
            }
            document.add(table);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
            for(Integer numeroFila:filasMultiregistro) {
                table = new PdfPTable(cabeceraTabla);
                table.setWidthPercentage(100);
                
                String[] calculosVinculacion = mostrarCalculosVinculacion
                        ? obtenerCalculosVinculacion(listaSubbloqueDTO, numeroFila, fechaFinPresentacion) : null;
                
                for(BloqueDTO subBloque:listaSubbloqueDTO) {
                    if(subBloque.getEsTipoDocumento().equals(Boolean.FALSE) 
                            && !subBloque.getLstBloqueComponenteDTO().isEmpty()){
                        for(BloqueComponenteDTO blqComp:subBloque.getLstBloqueComponenteDTO()) {
                            try {
                                DatoDTO dato = obtenerDatoFila(blqComp, numeroFila);
                                if(dato != null) {
                                    String valorDato = dato.getDsValor();
                                    if(utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA)
                                                .equals(blqComp.getComponenteDTO().getSubTipoDTO().getIdValor()) && valorDato != null ) {
                                            // El valor se guarda en SAII_DATO como ISO-8601 UTC (p.ej. "2026-09-07T22:00:00.000Z").
                                            // Hay que interpretarlo como instante UTC y formatearlo en Europe/Madrid,
                                            // si no, se pierde un día (07/09/2026 en vez de 08/09/2026).
                                            valorDato = dateFormat.format(Date.from(Instant.parse(valorDato)));
                                        }

                                    PdfPCell cell = celdaEtiqueta(valorDato, Boolean.TRUE);
                                    table.addCell(cell);
                                } else {
                                    // Sin dato para esta posición (p.ej. sin expediente todavía):
                                    // pintamos la celda en blanco para no romper la alineación de columnas.
                                    // Se usa " " (espacio) y no "" porque iText puede calcular una altura
                                    // de línea igual a cero para un Phrase totalmente vacío, haciendo que
                                    // la fila colapse y no se vea en el PDF aunque la celda se haya añadido.
                                    table.addCell(celdaEtiqueta(" ", Boolean.TRUE));
                                }
                            } catch (DateTimeParseException e) {
                                log.error(e.getMessage());
                            }
                            if (mostrarCalculosVinculacion && this.esComponente(blqComp, "Fecha inicio")) {
                                table.addCell(celdaEtiqueta(calculosVinculacion[0], Boolean.TRUE));
                            }
                            if (mostrarCalculosVinculacion && this.esComponente(blqComp, "Fecha fin")) {
                                table.addCell(celdaEtiqueta(calculosVinculacion[1], Boolean.TRUE));
                                table.addCell(celdaEtiqueta(calculosVinculacion[2], Boolean.TRUE));
                            }

                        }
                    }
                }
                document.add(table);
            }
        }
        agregarFilaVacio(document, 1);
        
    }
    
    private PdfPCell celdaCabeceraMultiregistro(String texto) {
        PdfPCell cell = celdaEtiqueta(texto, Boolean.TRUE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private List<Integer> obtenerFilasMultiregistro(List<BloqueDTO> listaSubbloqueDTO) {
        List<Integer> filas = new ArrayList<>();
        for (BloqueDTO subBloque : listaSubbloqueDTO) {
            for (BloqueComponenteDTO componente : subBloque.getLstBloqueComponenteDTO()) {
                if (componente.getDatos() == null) {
                    continue;
                }
                for (DatoDTO dato : componente.getDatos()) {
                    if (dato != null && dato.getIdDato() != null && dato.getNmFila() != null
                            && StringUtils.isNotBlank(dato.getDsValor()) && !filas.contains(dato.getNmFila())) {
                        filas.add(dato.getNmFila());
                    }
                }
            }
        }
        Collections.sort(filas);
        return filas;
    }

    private DatoDTO obtenerDatoFila(BloqueComponenteDTO componente, Integer numeroFila) {
        if (componente.getDatos() != null) {
            for (DatoDTO dato : componente.getDatos()) {
                if (dato != null && numeroFila.equals(dato.getNmFila())) {
                    return dato;
                }
            }
        }
        return null;
    }

    private boolean esComponente(BloqueComponenteDTO componente, String nombre) {
        return componente != null && componente.getComponenteDTO() != null
                && normalizarTexto(nombre).equals(
                        normalizarTexto(componente.getComponenteDTO().getDsNombre()));
    }

    private boolean esBloqueVinculaciones(List<BloqueDTO> listaSubbloqueDTO) {
        return buscarComponente(listaSubbloqueDTO, "Fecha inicio") != null
                && buscarComponente(listaSubbloqueDTO, "Fecha fin") != null
                && buscarComponente(listaSubbloqueDTO, "Tipo de estancia") != null
                && buscarComponente(listaSubbloqueDTO, "País") != null;
    }

    private String[] obtenerCalculosVinculacion(List<BloqueDTO> listaSubbloqueDTO,
            int indice, Date fechaFinPresentacion) {
        Date fechaInicio = obtenerFechaComponente(listaSubbloqueDTO, "Fecha inicio", indice);
        Date fechaFin = obtenerFechaComponente(listaSubbloqueDTO, "Fecha fin", indice);

        if (fechaInicio == null || fechaFin == null) {
            return new String[] {" ", " ", " "};
        }

        Calendar limite = Calendar.getInstance();
        limite.setTime(fechaFinPresentacion);
        limite.add(Calendar.YEAR, -5);
        Date fechaInicioComputable = fechaInicio.after(limite.getTime()) ? fechaInicio : limite.getTime();
        Date fechaFinComputable = fechaFin.before(fechaFinPresentacion) ? fechaFin : fechaFinPresentacion;

        if (fechaFinComputable.before(fechaInicioComputable)) {
            return new String[] {" ", " ", "0"};
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFecha.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        int anios = calcularAniosCompletos(fechaInicioComputable, fechaFinComputable);

        return new String[] {formatoFecha.format(fechaInicioComputable),
                formatoFecha.format(fechaFinComputable), String.valueOf(anios)};
    }

    private int calcularAniosCompletos(Date fechaInicio, Date fechaFin) {
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fechaInicio);
        Calendar fin = Calendar.getInstance();
        fin.setTime(fechaFin);

        int anios = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        boolean aniversarioPendiente = fin.get(Calendar.MONTH) < inicio.get(Calendar.MONTH)
                || (fin.get(Calendar.MONTH) == inicio.get(Calendar.MONTH)
                        && fin.get(Calendar.DAY_OF_MONTH) < inicio.get(Calendar.DAY_OF_MONTH));
        return aniversarioPendiente ? anios - 1 : anios;
    }

    private Date obtenerFechaComponente(List<BloqueDTO> listaSubbloqueDTO, String nombre, int numeroFila) {
        BloqueComponenteDTO componente = buscarComponente(listaSubbloqueDTO, nombre);
        if (componente == null) {
            return null;
        }

        DatoDTO dato = obtenerDatoFila(componente, numeroFila);
        String valor = dato == null ? null : dato.getDsValor();
        if (valor == null) {
            return null;
        }

        try {
            // Formato ISO-8601 UTC guardado por el frontal (p.ej. "2026-09-07T22:00:00.000Z"):
            // hay que interpretarlo como instante UTC, si no se pierde un día al mostrarlo.
            return Date.from(Instant.parse(valor));
        } catch (DateTimeParseException e) {
            // No es un instante UTC: se prueba con los formatos de fecha local admitidos.
        }

        String[] formatos = {"yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy", "yyyy-MM-dd"};
        for (String formato : formatos) {
            try {
                return new SimpleDateFormat(formato).parse(valor);
            } catch (ParseException e) {
                // Se prueba el siguiente formato admitido.
            }
        }
        return null;
    }

    private BloqueComponenteDTO buscarComponente(List<BloqueDTO> listaSubbloqueDTO, String nombre) {
        String nombreNormalizado = normalizarTexto(nombre);
        for (BloqueDTO subBloque : listaSubbloqueDTO) {
            for (BloqueComponenteDTO componente : subBloque.getLstBloqueComponenteDTO()) {
                if (componente.getComponenteDTO() != null && nombreNormalizado.equals(
                        normalizarTexto(componente.getComponenteDTO().getDsNombre()))) {
                    return componente;
                }
            }
        }
        return null;
    }

    private String normalizarTexto(String valor) {
        return valor == null ? StringUtils.EMPTY : Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", StringUtils.EMPTY).toLowerCase().trim();
    }

//    public void agregarTextoEnriquecido(Document document, String texto,
//            String descripcion, int ancho, Boolean isHtml) throws DocumentException {
//        PdfPTable table = new PdfPTable(2);
//
//        table.setWidths(new int[] {ancho, 50 });
//        table.setWidthPercentage(100);
//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//
//        PdfPCell cell = null;
//
//        cell = new PdfPCell(new Phrase(texto, tipoLetra8Bold));
//        cell.setBorder(Rectangle.NO_BORDER);
//        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//
//        table.addCell(cell);
//
//        PdfPCell cells = null;
//        
//        if (Boolean.TRUE.equals(isHtml)) {
////            Phrase frase = new Phrase();
//
//            //metemos los saltos de linea despues de cada parrafo
//            //String textoDesc = descripcion.replace("&nbsp;", "<br>").replaceAll("</p>", "</p><br/>");
//            String textoDesc = descripcion
//                  .replace("&nbsp;", " ")
//                  .replaceAll("(?i)<br\\s*/?>", "<br/>")
//                  .replaceAll("(?i)<p>", "<p style='margin:0;padding:0;'>")
//                  .replaceAll("</p>", "</p><br/>");
//
//            // VMM MD-2950: Quill (el editor de texto enriquecido) guarda la alineacion y el sangrado
//            // como clases CSS (ql-align-*, ql-indent-*) en cada <p>, que HTMLWorker no interpreta
//            // (solo entiende estilos en linea). Por eso las leemos aqui, en el mismo orden en que
//            // aparecen los parrafos, para aplicarlas manualmente al reconstruir cada Paragraph.
//            final float sangriaPuntosPorNivel = 36f; // ~0.5" por nivel de sangria (equivalente a ql-indent-N)
//            List<Integer> alineacionesPorParrafo = new ArrayList<>();
//            List<Float> sangriasPorParrafo = new ArrayList<>();
//            Matcher tagP = Pattern.compile("(?i)<p\\b([^>]*)>").matcher(textoDesc);
//            while (tagP.find()) {
//                String clase = "";
//                Matcher claseMatcher = Pattern.compile("class=\"([^\"]*)\"").matcher(tagP.group(1));
//                if (claseMatcher.find()) {
//                    clase = claseMatcher.group(1);
//                }
//                int alineacion = Element.ALIGN_LEFT;
//                if (clase.contains("ql-align-center")) {
//                    alineacion = Element.ALIGN_CENTER;
//                } else if (clase.contains("ql-align-right")) {
//                    alineacion = Element.ALIGN_RIGHT;
//                } else if (clase.contains("ql-align-justify")) {
//                    alineacion = Element.ALIGN_JUSTIFIED;
//                }
//                alineacionesPorParrafo.add(alineacion);
//
//                float sangria = 0f;
//                Matcher indentMatcher = Pattern.compile("ql-indent-(\\d+)").matcher(clase);
//                if (indentMatcher.find()) {
//                    sangria = Integer.parseInt(indentMatcher.group(1)) * sangriaPuntosPorNivel;
//                }
//                sangriasPorParrafo.add(sangria);
//            }
//            int indiceParrafo = 0;
//
//            try {
//                    List<Element> elementos = HTMLWorker.parseToList(new StringReader(textoDesc), null);
//
//                    for (Element element : elementos) {
//
//                        Phrase frase = new Phrase();
//                        boolean numeral = false;
//
//                        if (element instanceof Paragraph) {
//                            // Reconstruir Paragraph respetando la alineacion y sangria establecidas en el editor
//                            Paragraph original = (Paragraph) element;
//                            Paragraph justificado = new Paragraph();
//                            int alineacion = indiceParrafo < alineacionesPorParrafo.size()
//                                    ? alineacionesPorParrafo.get(indiceParrafo) : Element.ALIGN_LEFT;
//                            float sangria = indiceParrafo < sangriasPorParrafo.size()
//                                    ? sangriasPorParrafo.get(indiceParrafo) : 0f;
//                            justificado.setAlignment(alineacion);
//                            justificado.setIndentationLeft(sangria);
//                            indiceParrafo++;
//                            numeral = Utils.esPrimerCaracterNumero(original.getContent());
//
//                            for (Chunk chunk : original.getChunks()) {
//                                Font f = new Font(chunk.getFont());
//                                f.setSize(tipoLetra8.getSize());
//                                chunk.setFont(f);
//                                justificado.add(chunk);
//                            }
//                            frase.add(justificado);
//
//                        } else if (element instanceof Chunk) {
//                            Chunk chunk = (Chunk) element;
//                            Font f = new Font(chunk.getFont());
//                            f.setSize(tipoLetra8.getSize());
//                            chunk.setFont(f);
//
//                            Paragraph justificado = new Paragraph();
//                            justificado.setAlignment(Element.ALIGN_JUSTIFIED);
//                            justificado.add(chunk);
//                            frase.add(justificado);
//
//                        } else if (element instanceof com.itextpdf.text.List) {
//                            // También añadimos listas completas si las hay
//                            frase.add(element);
//
//                        } else {
//                            frase.add(element); // Otros elementos (tablas, imágenes…)
//                        }
//                        
//                        cells = new PdfPCell(frase);
//                        cells.setBorder(Rectangle.NO_BORDER);
//                        cells.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//                        cells.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                        cells.setLeading(24, 0);
//                        if (!numeral) {
//                            cells.setPaddingLeft(24f);
//                            cells.setLeading(12, 0);
//                        }
//                        table.addCell(cells);
//                        
//                    }
//
//                } catch (IOException e) {
//                    log.error("Error procesando HTML enriquecido: " + e.getMessage(), e);
//                }
//                /*List<Element> elementos = HTMLWorker.parseToList(new StringReader(textoDesc), null);
//                
//                for (Element element : elementos) {
//                    if (element instanceof Paragraph) {
//                        Paragraph originalParagraph = (Paragraph) element;
//                        Paragraph justifiedParagraph = new Paragraph(); // nuevo párrafo justificado
//                        justifiedParagraph.setAlignment(Element.ALIGN_JUSTIFIED); // justificar
//                        
//                        for (Chunk c : originalParagraph.getChunks()) {
//                            Font originalFont = c.getFont();
//                            Font newFont = new Font(originalFont);
//                            newFont.setSize(tipoLetra8.getSize());
//                            c.setFont(newFont);
//                            justifiedParagraph.add(c);
//                        }
//                        frase.add(justifiedParagraph);
//                        
//                    } else if (element instanceof Chunk) {
//                        Chunk chunk = (Chunk) element;
//                        Font originalFont = chunk.getFont();
//                        Font newFont = new Font(originalFont);
//                        newFont.setSize(tipoLetra8.getSize());
//                        chunk.setFont(newFont);
//
//                        Paragraph p = new Paragraph();
//                        p.setAlignment(Element.ALIGN_JUSTIFIED);
//                        p.add(chunk);
//                        frase.add(p);
//                        
//                    } else {
//                        frase.add(element); // si es otro tipo (Image, etc.)
//                    }
//                }
//    
//                cells = new PdfPCell(frase);
//                cells.setBorder(Rectangle.NO_BORDER);
//                cells.setHorizontalAlignment(Element.ALIGN_LEFT);
//                cells.setVerticalAlignment(Element.ALIGN_MIDDLE);
//    
//            } catch (IOException e) {
//                log.error("Error al parsear HTML: " + e.getMessage(), e);
//                cells = new PdfPCell(new Phrase("[Error al mostrar HTML]", tipoLetra8));
//                cells.setBorder(Rectangle.NO_BORDER);
//            }*/
//        } else {
//            cells = new PdfPCell(new Phrase(descripcion, tipoLetra8));
//            cells.setBorder(Rectangle.NO_BORDER);
//
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cells.setHorizontalAlignment(Element.ALIGN_LEFT);
//        }
//
//        table.addCell(cells);
//
//        document.add(table);
//    }
    
    
    
    public void agregarTextoEnriquecido(Document document, String texto,
            String descripcion, int ancho, Boolean isHtml) throws DocumentException {

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[] { ancho, 50 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // 1. Columna izquierda (Etiqueta/Título)
        PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra8Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);

        // 2. Columna derecha (Contenido principal)
        PdfPCell cells;

        if (Boolean.TRUE.equals(isHtml)) {

            // Normalizamos saltos de línea y párrafos HTML
            String textoDesc = descripcion
                  .replace("&nbsp;", " ")
                  .replaceAll("(?i)<br\\s*/?>", "<br/>")
                  .replaceAll("(?i)<p>", "<p style='margin:0;padding:0;'>")
                  .replaceAll("</p>", "</p><br/>");

            // Extraemos las clases CSS de alineación y sangría generadas por Quill (ql-align-*, ql-indent-*)
            final float sangriaPuntosPorNivel = 36f;
            List<Integer> alineacionesPorParrafo = new ArrayList<>();
            List<Float> sangriasPorParrafo = new ArrayList<>();
            
            Matcher tagP = Pattern.compile("(?i)<p\\b([^>]*)>").matcher(textoDesc);
            while (tagP.find()) {
                String clase = "";
                Matcher claseMatcher = Pattern.compile("class=\"([^\"]*)\"").matcher(tagP.group(1));
                if (claseMatcher.find()) {
                    clase = claseMatcher.group(1);
                }
                
                int alineacion = Element.ALIGN_JUSTIFIED; // Por defecto justificamos el texto
                if (clase.contains("ql-align-center")) {
                    alineacion = Element.ALIGN_CENTER;
                } else if (clase.contains("ql-align-right")) {
                    alineacion = Element.ALIGN_RIGHT;
                } else if (clase.contains("ql-align-justify")) {
                    alineacion = Element.ALIGN_JUSTIFIED;
                }
                alineacionesPorParrafo.add(alineacion);

                float sangria = 0f;
                Matcher indentMatcher = Pattern.compile("ql-indent-(\\d+)").matcher(clase);
                if (indentMatcher.find()) {
                    sangria = Integer.parseInt(indentMatcher.group(1)) * sangriaPuntosPorNivel;
                }
                sangriasPorParrafo.add(sangria);
            }

            int indiceParrafo = 0;
            
            // Creamos la celda contenedora SIN forzar alineación global
            cells = new PdfPCell();
            cells.setBorder(Rectangle.NO_BORDER);
            cells.setVerticalAlignment(Element.ALIGN_TOP);
            cells.setPaddingLeft(24f);

            try {
                List<Element> elementos = HTMLWorker.parseToList(new StringReader(textoDesc), null);

                for (Element element : elementos) {

                    if (element instanceof Paragraph) {
                        Paragraph original = (Paragraph) element;
                        Paragraph pModificado = new Paragraph();
                        
                        int alineacion = indiceParrafo < alineacionesPorParrafo.size()
                                ? alineacionesPorParrafo.get(indiceParrafo) 
                                : Element.ALIGN_JUSTIFIED;
                                
                        float sangria = indiceParrafo < sangriasPorParrafo.size()
                                ? sangriasPorParrafo.get(indiceParrafo) : 0f;
                        
                        pModificado.setAlignment(alineacion);
                        pModificado.setIndentationLeft(sangria);
                        pModificado.setLeading(12, 0); // Interlineado ajustado
                        
                        indiceParrafo++;

                        for (Chunk chunk : original.getChunks()) {
                            Font f = new Font(chunk.getFont());
                            f.setSize(tipoLetra8.getSize());
                            chunk.setFont(f);
                            pModificado.add(chunk);
                        }
                        
                        // Se añade el párrafo a la celda conservando su alineación propia (p.ej. CENTRADO)
                        cells.addElement(pModificado);

                    } else if (element instanceof Chunk) {
                        Chunk chunk = (Chunk) element;
                        Font f = new Font(chunk.getFont());
                        f.setSize(tipoLetra8.getSize());
                        chunk.setFont(f);

                        Paragraph pModificado = new Paragraph();
                        pModificado.setAlignment(Element.ALIGN_JUSTIFIED);
                        pModificado.setLeading(12, 0);
                        pModificado.add(chunk);
                        cells.addElement(pModificado);

                    } else {
                        cells.addElement(element); // Soporte para listas y otros elementos HTML
                    }
                }

            } catch (IOException e) {
                log.error("Error procesando HTML enriquecido: " + e.getMessage(), e);
                cells = new PdfPCell(new Phrase("[Error al mostrar HTML]", tipoLetra8));
                cells.setBorder(Rectangle.NO_BORDER);
            }

        } else {
            cells = new PdfPCell(new Phrase(descripcion, tipoLetra8));
            cells.setBorder(Rectangle.NO_BORDER);
            cells.setHorizontalAlignment(Element.ALIGN_LEFT);
            cells.setVerticalAlignment(Element.ALIGN_TOP);
        }

        // Añadimos la celda ya montada con todos los párrafos a la tabla
        table.addCell(cells);

        document.add(table);
    }
    
    
    public void agregarTextoEnriquecidoADM(Document document, String texto,
            String descripcion, int ancho, Boolean isHtml) throws DocumentException {

        // Si no hay título a la izquierda (ancho == 0), no usamos tabla para evitar bloqueos de página
        if (ancho == 0 && Boolean.TRUE.equals(isHtml)) {
            procesarYAgregarHtmlDirecto(document, descripcion);
            return;
        }

        PdfPTable table = new PdfPTable(ancho > 0 ? 2 : 1);
        if (ancho > 0) {
            table.setWidths(new int[] { ancho, 100 - ancho });
        }
        table.setWidthPercentage(100);
        
        // Configuración CLAVE para permitir que la celda se divida entre páginas
        table.setSplitRows(true);
        table.setKeepTogether(false);

        // 1. Columna izquierda (solo si ancho > 0)
        if (ancho > 0) {
            PdfPCell cell = new PdfPCell(new Phrase(texto, tipoLetra8Bold));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell(cell);
        }

        // 2. Columna derecha / Principal
        PdfPCell cells = new PdfPCell();
        cells.setBorder(Rectangle.NO_BORDER);
        cells.setVerticalAlignment(Element.ALIGN_TOP);

        if (Boolean.TRUE.equals(isHtml)) {
            String textoDesc = normalizarHtml(descripcion);
            List<Element> elementos = parsearHtmlAElementos(textoDesc);

            for (Element element : elementos) {
                cells.addElement(element);
            }
        } else {
            cells.addElement(new Phrase(descripcion, tipoLetra8));
        }

        table.addCell(cells);
        document.add(table);
    }

    // Método auxiliar cuando el texto no lleva columna lateral (ancho == 0)
    private void procesarYAgregarHtmlDirecto(Document document, String html) throws DocumentException {
        String textoDesc = normalizarHtml(html);
        List<Element> elementos = parsearHtmlAElementos(textoDesc);

        // Se añaden uno a uno directamente al documento (flujo 100% libre)
        for (Element element : elementos) {
            document.add(element);
        }
    }

    // Limpieza de estilos y párrafos de Quill
    private String normalizarHtml(String html) {
        if (StringUtils.isBlank(html)) return "";
        return html.replace("&nbsp;", " ")
                   .replaceAll("(?i)<br\\s*/?>", "<br/>")
                   .replaceAll("(?i)<p>", "<p style='margin:0;padding:0;'>")
                   .replaceAll("</p>", "</p><br/>");
    }

    // Procesa clases ql-align y ql-indent
    // VMM 15/10/26 MD-3142: cada <p> se parsea por separado y se le aplica SU alineacion y SU sangria.
    // Antes se parseaba todo el HTML de una vez y se asignaban las clases por posicion (i-esimo <p> ->
    // i-esimo Paragraph), pero normalizarHtml anade un <br/> tras cada </p> y HTMLWorker genera un
    // Paragraph por cada uno, asi que hay el doble de Paragraph que de <p> y las sangrias/centrados
    // caian en lineas que no eran (holaaaa sin sangria, "esto es cursiva" con ella, etc.)
    private List<Element> parsearHtmlAElementos(String textoDesc) {
        List<Element> resultado = new ArrayList<>();

        final float sangriaPuntosPorNivel = 36f;

        Matcher bloqueP = Pattern.compile("(?is)<p\\b([^>]*)>(.*?)</p>").matcher(textoDesc);
        int posicion = 0;
        while (bloqueP.find()) {
            // Lo que haya fuera de los <p> (los <br/> que anade normalizarHtml, o contenido sin <p>)
            agregarFragmentoHtml(resultado, textoDesc.substring(posicion, bloqueP.start()),
                    Element.ALIGN_JUSTIFIED, 0f);

            String clase = "";
            Matcher claseMatcher = Pattern.compile("class=\"([^\"]*)\"").matcher(bloqueP.group(1));
            if (claseMatcher.find()) {
                clase = claseMatcher.group(1);
            }

            int alineacion = Element.ALIGN_JUSTIFIED;
            if (clase.contains("ql-align-center")) alineacion = Element.ALIGN_CENTER;
            else if (clase.contains("ql-align-right")) alineacion = Element.ALIGN_RIGHT;
            else if (clase.contains("ql-align-justify")) alineacion = Element.ALIGN_JUSTIFIED;

            float sangria = 0f;
            Matcher indentMatcher = Pattern.compile("ql-indent-(\\d+)").matcher(clase);
            if (indentMatcher.find()) {
                sangria = Integer.parseInt(indentMatcher.group(1)) * sangriaPuntosPorNivel;
            }

            agregarFragmentoHtml(resultado,
                    "<p style='margin:0;padding:0;'>" + bloqueP.group(2) + "</p>", alineacion, sangria);
            posicion = bloqueP.end();
        }
        agregarFragmentoHtml(resultado, textoDesc.substring(posicion), Element.ALIGN_JUSTIFIED, 0f);

        return resultado;
    }

    // Parsea un fragmento HTML y aplica la misma alineacion y sangria a todos sus parrafos
    private void agregarFragmentoHtml(List<Element> resultado, String html, int alineacion,
            float sangria) {
        if (StringUtils.isBlank(html)) {
            return;
        }
        try {
            List<Element> elementos = HTMLWorker.parseToList(new StringReader(html), null);

            for (Element element : elementos) {
                if (element instanceof Paragraph) {
                    Paragraph original = (Paragraph) element;
                    Paragraph pModificado = new Paragraph();

                    pModificado.setAlignment(alineacion);
                    pModificado.setIndentationLeft(sangria);
                    pModificado.setLeading(12, 0);

                    for (Chunk chunk : original.getChunks()) {
                        Font f = new Font(chunk.getFont());
                        f.setSize(tipoLetra8.getSize());
                        chunk.setFont(f);
                        pModificado.add(chunk);
                    }
                    resultado.add(pModificado);
                } else {
                    resultado.add(element);
                }
            }
        } catch (IOException e) {
            log.error("Error procesando HTML: " + e.getMessage(), e);
        }
    }
    
    
    
    public void agregarBloqueTipoNotificacion(PdfWriter writer, Document document,
            BloqueDTO bloque) throws DocumentException, ServiceException {

        agregarTituloBloque(document, bloque);
        List<BloqueComponenteDTO> lstBloqueComponenteDTO = obtenerComponentes(bloque);

        Map<String, Object> configuracionAdicional = new HashMap<>();
        configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
        
       configuracionAdicional.put(aliasMostrarUnaColumna, false);

        List<PdfFormField> agrupadores = new ArrayList<>();

        int fila = 1;

        List<Integer> lstDistribucion = calcularDistribucionBloqueFormulario(
                lstBloqueComponenteDTO);

        Integer iContadorBloque = 0;
        Iterator<Integer> itDistribucion = lstDistribucion.iterator();
        while (itDistribucion.hasNext()) {

            Integer iComponentes = itDistribucion.next();

            Integer iNumeroColumna = iComponentes * 2 + 1;
            float[] arrayFloat = new float[iNumeroColumna];

            int iContadorComponente = 0;
            for (int i = 0; i < iComponentes; i++) {
                arrayFloat[i + iContadorComponente] = lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getLngPorcentajeEtiquetaPDF();
                arrayFloat[i + iContadorComponente + 1] = lstBloqueComponenteDTO
                        .get(iContadorBloque).getComponenteDTO().getLngPorcentajeCampoPDF();
                iContadorBloque++;
                iContadorComponente++;
            }
            iContadorBloque = iContadorBloque - iComponentes;

            // Calcula sobra
            float sobra = 100;
            for (float element : arrayFloat) {
                sobra = sobra - element;
            }
            arrayFloat[arrayFloat.length - 1] = sobra;

            PdfPTable table = new PdfPTable(arrayFloat);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.getDefaultCell().setPadding(0);
            for (int i = 0; i < iComponentes; i++) {
                
                if(  lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getDsNombre() != null 
                        && !lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getDsNombre().equals(StringUtils.EMPTY) ){
                    table.addCell(celdaEtiquetaNegrita(lstBloqueComponenteDTO.get(iContadorBloque)
                            .getComponenteDTO().getDsNombre(), true));
                }
               
                Integer idTipoComponente = lstBloqueComponenteDTO.get(iContadorBloque)
                        .getComponenteDTO().getTipoDTO().getIdValor();

                String nombreFila = StringUtils.EMPTY;

                nombreFila = "_" + fila;
                BloqueComponenteDTO  componente=lstBloqueComponenteDTO.get(iContadorBloque);
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO).equals(idTipoComponente)) {
                   
                    List<String> lstValores = Arrays
                            .asList(componente.getComponenteDTO().getDsValorMultiple().split(";"));
                    PdfFormField agrupador = PdfFormField.createRadioButton(writer, true);
                    agrupador.setFieldName(componente.getIdBloqueComponente() + nombreFila);

                    PdfPCell cellRad = new PdfPCell();
                    if(bloque.getEsTipoNotificacion().equals(Boolean.TRUE)) {
                        table = new PdfPTable(1);
                        table.setWidthPercentage(100);
                        table.getDefaultCell().setBorder(Rectangle.BOX);
                        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
                        table.getDefaultCell().setPadding(0);
                        cellRad.addElement(celdaCampoMultipleRadioNotificacion(agrupador, lstValores, true, null));
                    }else {
                        cellRad.addElement(celdaCampoMultipleRadioTipoPersona(agrupador, lstValores, true, null));
                        cellRad.setColspan(2);
                    }

                    
                    
                    cellRad.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellRad.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellRad.setBorder(Rectangle.BOX);
                    cellRad.setBorderColor(BaseColor.BLACK);
                    cellRad.setPadding(0);
                    table.addCell(cellRad);
                    
                    agrupadores.add(agrupador);

                    
                }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK).equals(idTipoComponente)) {
                   
                    List<String> lstOpciones = Arrays
                            .asList(componente.getComponenteDTO().getDsValorMultiple().split(";"));
                   
                    List<String> lstValores = new ArrayList<>();
                    for (Integer i1 = 0; i1 < lstOpciones.size(); i1++) {
                        String nombreCheck = "chk_" + componente.getIdBloqueComponente() + nombreFila + "_"
                                + (i1 + 1);
                        lstValores.add(nombreCheck);
                    }


                    PdfPTable tableCheck = new PdfPTable(new float[] {4.7f, 0.3f });
                    tableCheck.setWidthPercentage(100);
                    tableCheck.getDefaultCell().setBorder(Rectangle.BOX);
                    for (int indice = 0; indice < lstValores.size(); indice++) {
                       PdfPCell cellChkLabel = new PdfPCell(new Phrase(lstOpciones.get(indice), tipoLetra8));
                            cellChkLabel.setVerticalAlignment(Element.ALIGN_LEFT);
                            cellChkLabel.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                            cellChkLabel.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                            //cellChkLabel.setNoWrap(false);
                            cellChkLabel.setBorder(Rectangle.BOX);
                            cellChkLabel.setPadding(6);
                            tableCheck.addCell(cellChkLabel);
                        
                       // PdfPCell cellChk1 = celdaCampoCheck(lstValores.get(indice));
                        
                        PdfPCell cellChk1 = new PdfPCell(new Phrase(" "));
                        cellChk1.setCellEvent(new CheckboxCellEvent(lstValores.get(indice)));
                        cellChk1.setPadding(0);
                        cellChk1.setBorder(Rectangle.BOX);
                        cellChk1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tableCheck.addCell(cellChk1);
                    }

                    PdfPCell cellChk = new PdfPCell();
                    cellChk.addElement(tableCheck);

                    cellChk.setColspan(2);
                    cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellChk.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellChk.setBorder(Rectangle.BOX);
                    cellChk.setPadding(0);
                    cellChk.setBorderColor(BaseColor.BLACK);
                    table.addCell(cellChk);

                }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO).equals(idTipoComponente)) {
                    // PdfPCell cellTextFijo = new PdfPCell(new Phrase(componente.getComponenteDTO().getTlTextoFijo(), tipoLetra8));
                    // MD-2469 [ADM] Ampliar campo texto fijo en formulario
                    PdfPCell cellTextFijo = new PdfPCell(new Phrase(componente.getComponenteDTO().getClTextoFijo(), tipoLetra8));
                    cellTextFijo.setColspan(2);
                    cellTextFijo.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellTextFijo.setBorder(Rectangle.BOX);
                    cellTextFijo.setBorderColor(BaseColor.BLACK);

                    cellTextFijo.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                    cellTextFijo.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                    //cellTextFijo.setNoWrap(false);
                    cellTextFijo.setPadding(6);
                    table.addCell(cellTextFijo);
                }

                // Si es el ultimo es el sobra
                if (i == iComponentes - 1) {
                    table.addCell(celdaEtiquetaNegrita("", true));
                }

                iContadorBloque++;
            }

            document.add(table);
        }

        for (PdfFormField agrupador : agrupadores) {
            writer.addAnnotation(agrupador);
        }

    }

    public void agregarBloquePersonaNotificacion(PdfWriter writer, Document document,
                                                 String tipo, Integer idSeleccionado, Boolean correoExiste) throws DocumentException, ServiceException {
        if (correoExiste == null){
            correoExiste = Boolean.FALSE;
        }
        

        int numComponente = 0;
        if (tipo.equals("PERSONA") ) {
            agregarTituloPersonaNotificacion(document, catalogosValoresService.buscarNmConstante(186).getDsValor());
            numComponente = 1;
        } else if ( tipo.equals("NOTIFICACION")) {
            agregarTituloPersonaNotificacion(document, catalogosValoresService.buscarNmConstante(187).getDsValor());
            numComponente = 2;
        }

        List<float[]> listaMargenes = new ArrayList<>();

        float[] margenes1 = new float[2];
        margenes1[0] = 5.0f;
        margenes1[1] = 95.0f;

        float[] margenes2 = new float[2];
        margenes2[0] = 5.0f;
        margenes2[1] = 64.8f;

        if (tipo.equals("PERSONA") ) {
            listaMargenes = Arrays.asList(margenes1);
        } else if ( tipo.equals("NOTIFICACION")) {
            listaMargenes = Arrays.asList(margenes2, margenes1, margenes1);
        }

        Map<String, Object> configuracionAdicional = new HashMap<>();
        configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
        configuracionAdicional.put(aliasMostrarUnaColumna, false);

        List<PdfFormField> agrupadores = new ArrayList<>();

        int fila = 1;

        List<Integer> lstDistribucion = new ArrayList<>();
        if ("PERSONA".equals(tipo)) {
            lstDistribucion = Arrays.asList(0, 1);
        } else if ("NOTIFICACION".equals(tipo)) {
            lstDistribucion = Arrays.asList(1, 1, 1);
        }

        List<Integer> listaComponentes = new ArrayList<>();
        if ("PERSONA".equals(tipo)) {
            listaComponentes = Arrays.asList(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO));
        } else if ("NOTIFICACION".equals(tipo)) {
            listaComponentes = Arrays.asList(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO), utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK));
        }

        Integer iContadorBloque = 0;
        Iterator<Integer> itDistribucion = lstDistribucion.iterator();
        while (itDistribucion.hasNext()) {

            Integer iComponentes = itDistribucion.next();

            Integer iNumeroColumna = iComponentes * 2 + 1;
            float[] arrayFloat = new float[iNumeroColumna];

            int iContadorComponente = 0;
            for (int i = 0; i < iComponentes; i++) {
                arrayFloat[i + iContadorComponente] = listaMargenes.get(iContadorBloque)[0];
                arrayFloat[i + iContadorComponente + 1] = listaMargenes.get(iContadorBloque)[1];
                iContadorBloque++;
                iContadorComponente++;
            }
            iContadorBloque = iContadorBloque - iComponentes;

            float sobra = 100;
            for (float element : arrayFloat) {
                sobra = sobra - element;
            }
            arrayFloat[arrayFloat.length - 1] = sobra;

            PdfPTable table = new PdfPTable(arrayFloat);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.getDefaultCell().setPadding(0);

            for (int i = 0; i < iComponentes; i++) {

                Integer idTipoComponente = listaComponentes.get(iContadorBloque);
                String nombreFila = StringUtils.EMPTY;
                nombreFila = "_" + fila;

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO).equals(idTipoComponente)) {

                    List<CatalogosValoresDTO> catalogo = new ArrayList<>();
                    if ("PERSONA".equals(tipo)) {
                        catalogo = interesadoService.obtenerPersonaSolicitante();
                    } else if ("NOTIFICACION".equals(tipo) ){
                        catalogo = interesadoService.obtenerMedioNotificaciones();
                    }

                    List<String> lstValores = catalogo.stream().map(CatalogosValoresDTO::getDsValor)
                            .collect(Collectors.toList());

                    PdfFormField agrupador = PdfFormField.createRadioButton(writer, true);
                    agrupador.setFieldName(numComponente + nombreFila);

                    String selectValue = null;
                    if (idSeleccionado != null && idSeleccionado != 0){
                        CatalogosValoresDTO elegido = catalogo.stream()
                                .filter(c -> Objects.equals(c.getIdValor(), idSeleccionado)).findFirst().orElse(null);

                        if (elegido != null) {
                            selectValue = elegido.getDsValor();
                        }
                    }

                    PdfPCell cellRad = new PdfPCell();
                    if ("NOTIFICACION".equals(tipo)) {
                        table = new PdfPTable(1);
                        table.setWidthPercentage(100);
                        table.getDefaultCell().setBorder(Rectangle.BOX);
                        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
                        table.getDefaultCell().setPadding(0);
                        cellRad.addElement(celdaCampoMultipleRadioNotificacion(agrupador, lstValores, true, selectValue));
                    } else if ("PERSONA".equals(tipo)) {
                        cellRad.addElement(celdaCampoMultipleRadioTipoPersona(agrupador, lstValores, true, selectValue));
                        cellRad.setColspan(2);
                    }

                    cellRad.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellRad.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellRad.setBorder(Rectangle.BOX);
                    cellRad.setBorderColor(BaseColor.BLACK);
                    cellRad.setPadding(0);
                    table.addCell(cellRad);

                    agrupadores.add(agrupador);


                }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK).equals(idTipoComponente) && correoExiste.equals(Boolean.FALSE)) {

                    String textoFijo = "La persona designada para recibir notificaciones electrónicas no está dada de alta en el Servicio " +
                            "de Notificaciones Electrónicas de la Comunidad de Madrid. Puedes darte de alta a través de " +
                            "https://gestiona3.madrid.org/note_public. Si no lo haces en este momento, se habilitará de forma automática " +
                            "una dirección electrónica (DEH) cuando envíes la solicitud a registro. En ella recibirás tus notificaciones " +
                            "electrónicas. Además, cada vez que se envíe una notificación, recibirás un aviso en el correo electrónico " +
                            "que has informado en esta solicitud. Para continuar y aceptar que has leído este aviso, marca esta casilla.\n";

                    List<String> lstOpciones = Arrays
                            .asList(textoFijo);

                    List<String> lstValores = new ArrayList<>();
                    for (Integer i1 = 0; i1 < lstOpciones.size(); i1++) {
                        String nombreCheck = "chk_" + numComponente + nombreFila + "_"
                                + (i1 + 1);
                        lstValores.add(nombreCheck);
                    }

                    PdfPTable tableCheck = new PdfPTable(new float[] {4.7f, 0.3f });
                    tableCheck.setWidthPercentage(100);
                    tableCheck.getDefaultCell().setBorder(Rectangle.BOX);

                    for (int indice = 0; indice < lstValores.size(); indice++) {
                        PdfPCell cellChkLabel = new PdfPCell(new Phrase(lstOpciones.get(indice), tipoLetra8));
                        cellChkLabel.setVerticalAlignment(Element.ALIGN_LEFT);
                        cellChkLabel.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                        cellChkLabel.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                        //cellChkLabel.setNoWrap(false);
                        cellChkLabel.setBorder(Rectangle.BOX);
                        cellChkLabel.setPadding(6);
                        tableCheck.addCell(cellChkLabel);

                        // PdfPCell cellChk1 = celdaCampoCheck(lstValores.get(indice));

                        PdfPCell cellChk1 = new PdfPCell(new Phrase(" "));
                        if (!correoExiste){
                            cellChk1.setCellEvent(new CheckboxCheckedCellEvent(lstValores.get(indice), !correoExiste));
                        } else {
                            cellChk1.setCellEvent(new CheckboxCellEvent(lstValores.get(indice)));
                        }

                        cellChk1.setPadding(0);
                        cellChk1.setBorder(Rectangle.BOX);
                        cellChk1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tableCheck.addCell(cellChk1);
                    }

                    PdfPCell cellChk = new PdfPCell();
                    cellChk.addElement(tableCheck);

                    cellChk.setColspan(2);
                    cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellChk.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellChk.setBorder(Rectangle.BOX);
                    cellChk.setPadding(0);
                    cellChk.setBorderColor(BaseColor.BLACK);
                    table.addCell(cellChk);

                }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO).equals(idTipoComponente)) {

                    String textoFijo = "Las notificaciones se realizarán a través de medios electrónicos por lo que, con " +
                            "carácter previo a la presentación de esta solicitud, la persona a la que se notifica deberá estar " +
                            "dada de alta en el Sistema de Notificaciones Electrónicas de la Comunidad de Madrid. Puede " +
                            "darse de alta accediendo a https://gestiona3.madrid.org/note_public";

                    PdfPCell cellTextFijo = new PdfPCell(new Phrase(textoFijo, tipoLetra8));
                    cellTextFijo.setColspan(2);
                    cellTextFijo.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cellTextFijo.setBorder(Rectangle.BOX);
                    cellTextFijo.setBorderColor(BaseColor.BLACK);

                    cellTextFijo.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                    cellTextFijo.setVerticalAlignment(PdfPCell.ALIGN_JUSTIFIED);
                    //cellTextFijo.setNoWrap(false);
                    cellTextFijo.setPadding(6);
                    table.addCell(cellTextFijo);
                }

                // Si es el ultimo es el sobra
                if (i == iComponentes - 1) {
                    table.addCell(celdaEtiquetaNegrita("", true));
                }

                iContadorBloque++;
            }

            document.add(table);
//        }
//
//        for (PdfFormField agrupador : agrupadores) {
//            writer.addAnnotation(agrupador);
            for (PdfFormField agrupador : agrupadores) {
                writer.addAnnotation(agrupador);
            }
            agrupadores.clear();
        }
}

    private void agregarTituloPersonaNotificacion(Document document, String titulo)
            throws DocumentException {
        agregarFilaVacio(document, 1);

        PdfPTable tableResult = new PdfPTable(1);

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));

        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);

        document.add(tableResult);

        PdfPTable table = new PdfPTable(new float[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(6);
        table.addCell(new Phrase("", tipoLetra9));
        document.add(table);

    }

    private PdfPTable celdaCampoMultipleRadioNotificacion(PdfFormField agrupador, List<String> opciones,
            boolean mostrarEtiqueta, String seleccion) throws DocumentException {

        PdfPTable tableRadio  = new PdfPTable(new float[] {10,20,10,20,40}); 
        //PdfPTable tableRadio  = new PdfPTable(5); 
            tableRadio.setWidthPercentage(100);
            tableRadio.getDefaultCell().setBorder(Rectangle.NO_BORDER);
      

        for (String opcion : opciones) {
            boolean estaMarcado = opcion.equals(seleccion);

            PdfPCell cell = new PdfPCell(new Phrase(" "));
            cell.setCellEvent(new RadioCheckedCellEvent(agrupador, opcion, estaMarcado));
            cell.setPadding(4);
            cell.setBorder(Rectangle.BOX);
            cell.setBorderColor(BaseColor.BLACK);
            tableRadio.addCell(cell);
            if (mostrarEtiqueta) {
                PdfPCell cellRadLabel = new PdfPCell(new Phrase(opcion, tipoLetra8));
                cellRadLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRadLabel.setBorder(Rectangle.BOX);
                cellRadLabel.setBorderColor(BaseColor.BLACK);
                cellRadLabel.setPadding(4);
                tableRadio.addCell(cellRadLabel);
            }
           
        }

        PdfPCell cellRadLabel = new PdfPCell(new Phrase("(Indique a quién desea que se envíe la notificación)", tipoLetra8));
        cellRadLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellRadLabel.setBorder(Rectangle.BOX);
        cellRadLabel.setBorderColor(BaseColor.BLACK);
        
        cellRadLabel.setPadding(4);
       // cellRadLabel.setColspan(2);
        tableRadio.addCell(cellRadLabel);

        if (seleccion != null && opciones.contains(seleccion)) {
            agrupador.setValueAsName(seleccion);
        }

        return tableRadio;
    }
    
    
    private PdfPTable celdaCampoMultipleRadioTipoPersona(PdfFormField agrupador, List<String> opciones,
            boolean mostrarEtiqueta, String seleccion) throws DocumentException, ServiceException {

        PdfPTable tableRadio = new PdfPTable(new float[] {0.5f, 0.8f, 0.5f, 0.9f,0.5f,3.8f});
        tableRadio.setWidthPercentage(100);
        tableRadio.getDefaultCell().setBorder(Rectangle.BOX);
        tableRadio.getDefaultCell().setBorderColor(BaseColor.BLACK);
           
        for (String opcion : opciones) {
            boolean estaMarcado = opcion.equals(seleccion);
            
            PdfPCell cell = new PdfPCell(new Phrase(" "));
            cell.setCellEvent(new RadioCheckedCellEvent(agrupador, opcion, estaMarcado));
            cell.setPadding(4);
            cell.setBorder(Rectangle.BOX);
            cell.setBorderColor(BaseColor.BLACK);
            tableRadio.addCell(cell);
            if (mostrarEtiqueta) {
                PdfPCell cellRadLabel = new PdfPCell(new Phrase(opcion, tipoLetra8));
                cellRadLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRadLabel.setBorder(Rectangle.BOX);
                cellRadLabel.setBorderColor(BaseColor.BLACK);
                cellRadLabel.setPadding(4);
                tableRadio.addCell(cellRadLabel);
            }
           
        }
 
        if (mostrarEtiqueta && opciones.size() % 2 != 0 ) {
            tableRadio.addCell(new Phrase(" "));
            tableRadio.addCell(new Phrase(" "));
        }

        if (seleccion != null && opciones.contains(seleccion)) {
            agrupador.setDefaultValueAsName(seleccion);
        }

        return tableRadio;
    }

    /**
     * 
     * @return
     * @throws ServiceException 
     * @throws  
     */
    private static BaseFont createCustomFont() {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("/font/arial.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage());
        }
        return bf;
    }
    
   
    public void agregarBloqueAceptarRecharAyuda(PdfWriter writer, Document document,
              String selectValue) throws DocumentException, ServiceException {

    int numComponente = 0;
    agregarTituloPersonaNotificacion(document, "Aceptar o rechar la ayuda concedida");
    numComponente = 1;
   
    float[] margenes1 = new float[2];
    margenes1[0] = 5.0f;
    margenes1[1] = 95.0f;

    float[] margenes2 = new float[2];
    margenes2[0] = 5.0f;
    margenes2[1] = 64.8f;

    List<float[]> listaMargenes = Arrays.asList(margenes1);


    Map<String, Object> configuracionAdicional = new HashMap<>();
    configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
    configuracionAdicional.put(aliasMostrarUnaColumna, false);
    
    List<PdfFormField> agrupadores = new ArrayList<>();
    
    int fila = 1;

    List<Integer> lstDistribucion = Arrays.asList(0, 1);
    
    Integer iContadorBloque = 0;
    Iterator<Integer> itDistribucion = lstDistribucion.iterator();
    while (itDistribucion.hasNext()) {

        Integer iComponentes = itDistribucion.next();

        Integer iNumeroColumna = iComponentes * 2 + 1;
        float[] arrayFloat = new float[iNumeroColumna];

        int iContadorComponente = 0;
        for (int i = 0; i < iComponentes; i++) {
            arrayFloat[i + iContadorComponente] = listaMargenes.get(iContadorBloque)[0];
            arrayFloat[i + iContadorComponente + 1] = listaMargenes.get(iContadorBloque)[1];
            iContadorBloque++;
            iContadorComponente++;
        }
        
        iContadorBloque = iContadorBloque - iComponentes;

        float sobra = 100;
        for (float element : arrayFloat) {
        sobra = sobra - element;
        }
        arrayFloat[arrayFloat.length - 1] = sobra;
    
        PdfPTable table = new PdfPTable(arrayFloat);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(0);
    
        for (int i = 0; i < iComponentes; i++) {
            String nombreFila = "_" + fila;
            List<String> lstValores = new ArrayList<>();
            lstValores.add("Acepta la ayuda concedida.");
            lstValores.add("No acepta la ayuda concedida.");
    
            PdfFormField agrupador = PdfFormField.createRadioButton(writer, true);
            agrupador.setFieldName(numComponente + nombreFila);
    
            PdfPCell cellRad = new PdfPCell();
            
            PdfPTable tableRadio = new PdfPTable(new float[] {0.5f, 3f, 0.5f, 3f});
            tableRadio.setWidthPercentage(100);
            tableRadio.getDefaultCell().setBorder(Rectangle.BOX);
            tableRadio.getDefaultCell().setBorderColor(BaseColor.BLACK);
               
            for (String opcion : lstValores) {
                boolean estaMarcado = opcion.equals(selectValue);
                
                PdfPCell cell = new PdfPCell(new Phrase(" "));
                cell.setCellEvent(new RadioCheckedCellEvent(agrupador, opcion, estaMarcado));
                cell.setPadding(4);
                cell.setBorder(Rectangle.BOX);
                cell.setBorderColor(BaseColor.BLACK);
                tableRadio.addCell(cell);
                PdfPCell cellRadLabel = new PdfPCell(new Phrase(opcion, tipoLetra8));
                cellRadLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRadLabel.setBorder(Rectangle.BOX);
                cellRadLabel.setBorderColor(BaseColor.BLACK);
                cellRadLabel.setPadding(4);
                tableRadio.addCell(cellRadLabel);
                
               
            }
     
            if ( lstValores.size() % 2 != 0 ) {
                tableRadio.addCell(new Phrase(" "));
                tableRadio.addCell(new Phrase(" "));
            }

            if (selectValue != null && lstValores.contains(selectValue)) {
                agrupador.setDefaultValueAsName(selectValue);
            }

            
            cellRad.addElement(tableRadio);
            cellRad.setColspan(2);
    
    
            cellRad.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellRad.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellRad.setBorder(Rectangle.BOX);
            cellRad.setBorderColor(BaseColor.BLACK);
            cellRad.setPadding(0);
            table.addCell(cellRad);
            
            agrupadores.add(agrupador);
    
            // Si es el ultimo es el sobra
            if (i == iComponentes - 1) {
                table.addCell(celdaEtiquetaNegrita("", true));
            }
    
            iContadorBloque++;
        }

        document.add(table);
  
    }

    for (PdfFormField agrupador : agrupadores) {
        writer.addAnnotation(agrupador);
    }
}

    @Override
    public void crearInformeJustificacionEco(Document document,
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO, ExpedienteDTO expedienteDTO,
            String titulo,ConvocatoriaDTO convocatoriaDTO) throws DocumentException, ServiceException {
        
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        
        PdfPTable tableResult = new PdfPTable(1);
        PdfPCell cell = null;

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        
        PdfPTable table = null;
        if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
            table = new PdfPTable(new float[] {35, 35, 10, 10,10 });  
        }else {
            table = new PdfPTable(new float[] {40, 20, 20,20 }); 
        }
       
            table.setWidthPercentage(100);

            cell = celdaEtiqueta("PARTIDA PRESUP. ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
                cell = celdaEtiqueta("CONCEPTO ", Boolean.TRUE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            

            cell = celdaEtiqueta("PRESUP. FINANCIABLE ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("AYUDA CONCEDIDA ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = celdaEtiqueta("PRESUP. JUSTIFICADO ", Boolean.TRUE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            
            tableResult.addCell(table);
           
            if(!listExpedienteJusEcoDTO.isEmpty()) {
                for (ExpedienteJusEcoDocDTO item : listExpedienteJusEcoDTO) {
                    if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
                        table = new PdfPTable(new float[] {35, 35, 10, 10,10 });  
                    }else {
                        table = new PdfPTable(new float[] {40, 20, 20,20 }); 
                    }
                    
                    table.setWidthPercentage(100);
                    ExpedientePresup  presup= this.expedientePresupService.find(item.getExpedientePresupMontoDTO().getIdExpedientePresup());
                    cell = celdaEtiqueta(presup.getTipoFinanciacion().getDsValor(), Boolean.TRUE);
                    table.addCell(cell);
                    if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
                        cell = celdaEtiqueta(presup.getTlConcepto(), Boolean.TRUE);
                        table.addCell(cell);
                    }
                   
                    if(item.getExpedientePresupMontoDTO().getNmImporte() != null) {
                        BigDecimal totalPresup=  BigDecimal.valueOf(item.getExpedientePresupMontoDTO().getNmImporte());
                        cell = celdaEtiqueta(Utils.formatearBigDecimalEuropeo(totalPresup), Boolean.TRUE);
                        table.addCell(cell);
                        BigDecimal ayudaConcedida = totalPresup;
                        if(convocatoriaDTO.getPorcentajeIntensidadAyuda() != null) {
                            ayudaConcedida = (totalPresup.multiply(BigDecimal.valueOf(convocatoriaDTO.getPorcentajeIntensidadAyuda())))
                                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                            
                        }
                        
                        String decimalValorString = Utils.formatearBigDecimalEuropeo(ayudaConcedida);
                        cell = celdaEtiqueta(decimalValorString, Boolean.TRUE);
                        table.addCell(cell);
                        
                        
                        
                    }else {
                        cell = celdaEtiqueta("", Boolean.TRUE);
                        table.addCell(cell);
                        
                        cell = celdaEtiqueta("", Boolean.TRUE);
                        table.addCell(cell);
                    }
                    
                    
                    if(item.getExpedientePresupMontoDTO().getNmImporteJustificado() != null) {
                        BigDecimal totalPresup=  BigDecimal.valueOf(item.getExpedientePresupMontoDTO().getNmImporteJustificado());
                        cell = celdaEtiqueta(Utils.formatearBigDecimalEuropeo(totalPresup), Boolean.TRUE);
                        table.addCell(cell);
                    }else {
                        cell = celdaEtiqueta("", Boolean.TRUE);
                        table.addCell(cell);
                    }
                    tableResult.addCell(table);
                 }
            }else {
                
                for (int i = 0; i < 4; i++) {
                    if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
                        table = new PdfPTable(new float[] {35, 35, 10, 10,10 });
                    }else {
                        table = new PdfPTable(new float[] {40,20, 20,20 });
                    }
                   
                    table.setWidthPercentage(100);

                    PdfPCell cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    if(convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
                        cell2 = celdaEtiqueta("", Boolean.TRUE);
                        cell2.setBackgroundColor(BaseColor.WHITE);
                        table.addCell(cell2);
                    }
                    

                    cell2 = celdaEtiqueta(" ", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);

                    cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);
                    
                    cell2 = celdaEtiqueta("", Boolean.TRUE);
                    cell2.setBackgroundColor(BaseColor.WHITE);
                    table.addCell(cell2);
                    
                    tableResult.addCell(table);
                  
                } 
            }
            
            document.add(tableResult);

        agregarFilaVacio(document, 1);  
        
    }

    @Override
    public void agregarDatosEntidadSolicitante(Document document, ExpedienteDTO expediente, ConvocatoriaActDTO convocaActPadre)
            throws DocumentException, ServiceException {
        
       
        String titulo="Entidad a la que se presenta la candidatura";
        if(convocaActPadre != null &&  convocaActPadre.getTlDescripcion() != null) {
            titulo = convocaActPadre.getTlDescripcion();
        }
        this.agregarTituloBloque(document,titulo);
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(new float[] { 12, 28,12, 48,});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);

        cell = celdaEtiqueta("Acrónimo Entidad:", Boolean.TRUE);
        table.addCell(cell);
        if(expediente.getEntidadDTO() != null && expediente.getEntidadDTO().getInteresadoDTO() != null) {
            agregarCeldaInteresado(table, expediente.getEntidadDTO().getDsAbrev(), "campo_acronimo",
                    false);
            cell = celdaEtiqueta("Nombre:", Boolean.TRUE);
            table.addCell(cell);
            agregarCeldaInteresado(table, expediente.getEntidadDTO().getInteresadoDTO().getDsNombre(), "campo_nombre_entidad",
                    false);
            
           
        }else {
            // Añadir fila vacia para cuando llamamos desde ADMIN
            agregarCeldaInteresado(table, " ", "campo_acronimo",
                    false);
            cell = celdaEtiqueta("Nombre:", Boolean.TRUE);
            table.addCell(cell);
            agregarCeldaInteresado(table, " ", "campo_nombre_entidad",
                    false);
        }
        
        document.add(table);
        
    }

    @Override
    public void agregarBloqueModalidadExpediente(Document document,
            ExpedienteDTO expedienteDTO) throws DocumentException, ServiceException {
        
        String titulo="Línea de Actuación";
        this.agregarTituloBloque(document,titulo);
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(1);
       
        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        String descSubModalidad= expedienteDTO.getConvocatoriaSubmodDTO() != null 
                && expedienteDTO.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() != null? expedienteDTO.getConvocatoriaSubmodDTO().getDsSubmodalidad():" ";

        cell = celdaEtiqueta(descSubModalidad, Boolean.TRUE);
        table.addCell(cell);
        document.add(table);
        
        
    }
    
    private void agregarTituloBloque(Document document,String titulo) throws DocumentException, ServiceException  {
        
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
       
        PdfPTable tableResult = new PdfPTable(1);
       
        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);
        document.add(tableResult);
        
    }

    @Override
    public void agregarBloqueSubArea(Document document, ExpedienteDTO expedienteDTO)
            throws DocumentException, ServiceException {
        String titulo="Área científica";
        String descAreaSubarea= expedienteDTO.getConvocatoriaAreaDTO() != null
                && expedienteDTO.getConvocatoriaAreaDTO().getIdConvocatoriaArea() != null? expedienteDTO.getConvocatoriaAreaDTO().getAreaDTO().getDsValor():" ";
                
        if(expedienteDTO.getConvocatoriaAreaSubareaDTO() != null 
                && expedienteDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO() != null 
                && expedienteDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO().getIdValor() != null ) {
            descAreaSubarea = descAreaSubarea+" - "+expedienteDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO().getDsValor();
            titulo = "Área y subárea científica";
        }
        
        this.agregarTituloBloque(document,titulo);
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(1);
       
        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        cell = celdaEtiqueta(descAreaSubarea, Boolean.TRUE);
        table.addCell(cell);
        document.add(table);
        
    }
    
    @Override
    public int compareByNumOrdenPresup(ExpedientePresupDTO p1, ExpedientePresupDTO p2, List<FinanciacionDatoDTO> listaFinancDato) {
        // Extraer el numOrden para p1
        if(listaFinancDato != null && !listaFinancDato.isEmpty()) {
            Integer orden1 = listaFinancDato.stream()
                    .filter(e -> e.getTipoDTO().getIdValor().equals(p1.getTipoFinanciacionDTO().getIdValor()))
                    .map(FinanciacionDatoDTO::getNmOrden)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
 
            // Extraer el numOrden para p2
            Integer orden2 = listaFinancDato.stream()
                .filter(e -> e.getTipoDTO().getIdValor().equals(p2.getTipoFinanciacionDTO().getIdValor()))
                .map(FinanciacionDatoDTO::getNmOrden)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
 
            // Manejo de nulos
            if (orden1 == null && orden2 == null) return compareConceptos(p1, p2);
            if (orden1 == null) return 1;
            if (orden2 == null) return -1;

            // Si el orden de la partida es el mismo, comparamos por concepto
            int resultadoPartida = orden1.compareTo(orden2);
            if (resultadoPartida == 0) {
                return compareConceptos(p1, p2);
            }
            
            return resultadoPartida;
        }
        return compareConceptos(p1, p2);
    }

    @Override
    public void agregarDatosAutorizado(PdfWriter writer, Document document,
            ExpedienteDTO expedienteDTO) throws DocumentException, ServiceException {
        
        String titulo="Datos del autorizado para la presentación de la solicitud";
        agregarFilaVacio(document, 1);
        // Se añade la cabecera de la tabla
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = null;

        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);
        
        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(titulo, tipoLetra9Bold));
        
        celdaTituloSuperior.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        table.addCell(tableTitulo);
        
        document.add(table);
        
        // cuerpo del doc datos autorizado
        
        table = new PdfPTable(new float[] {12, 32, 20, 36});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        cell = celdaEtiqueta("NIF/NIE:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expedienteDTO.getAutorizado().getCdNumIden(), "campo_Auto_nif",
                true);
        
        cell = celdaEtiqueta("Nombre:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expedienteDTO.getAutorizado().getDsNombre(), "campo_Auto_nombre",
                true);
        
        document.add(table);
        
        table = new PdfPTable(new float[] {12,38,12,38});
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
       
        
        cell = celdaEtiqueta("Apellido 1:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expedienteDTO.getAutorizado().getDsApellido1(), "campo_Auto_Apellido1",
                true);
        
        cell = celdaEtiqueta("Apellido 2:", Boolean.TRUE);
        table.addCell(cell);
        
        agregarCeldaInteresado(table, expedienteDTO.getAutorizado().getDsApellido2(), "campo_Auto_Apellido2",
                true);
        
        document.add(table);
       
        
        
    }
    
    @Override
    public void agregarDocumentoJustifAutorizado(Document document,
            String tituloDoc,  Boolean subidoDoc) throws DocumentException, ServiceException {
        
            if (subidoDoc == null){
                subidoDoc = Boolean.FALSE;
            }

            agregarTituloPersonaNotificacion(document, tituloDoc); // modificar a constantes
           
   
            Map<String, Object> configuracionAdicional = new HashMap<>();
            configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
            configuracionAdicional.put(aliasMostrarUnaColumna, false);


            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.getDefaultCell().setPadding(0);

            List<String> lstOpciones = Arrays
             .asList(catalogosValoresService.buscarNmConstante(Constantes.TipoDocumento.DOCUMENTO_AUTORIZADO).getDsValor());

             List<String> lstValores = new ArrayList<>();
             String nombreCheck = "chk_ Doc_Autorizado";
             lstValores.add(nombreCheck);
                

            PdfPTable tableCheck = new PdfPTable(new float[] {4.7f, 0.3f });
            tableCheck.setWidthPercentage(100);
            tableCheck.getDefaultCell().setBorder(Rectangle.BOX);

        for (int indice = 0; indice < lstValores.size(); indice++) {
            PdfPCell cellChkLabel = new PdfPCell(new Phrase(lstOpciones.get(indice), tipoLetra8));
            cellChkLabel.setVerticalAlignment(Element.ALIGN_LEFT);
            cellChkLabel.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cellChkLabel.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
           
            cellChkLabel.setBorder(Rectangle.BOX);
            cellChkLabel.setPadding(6);
            tableCheck.addCell(cellChkLabel);



            PdfPCell cellChk1 = new PdfPCell(new Phrase(" "));
            if (subidoDoc.equals(Boolean.TRUE)){
                cellChk1.setCellEvent(new CheckboxCheckedCellEvent(lstValores.get(indice), subidoDoc));
            
            } else {
                cellChk1.setCellEvent(new CheckboxCellEvent(lstValores.get(indice)));
            }

            cellChk1.setPadding(0);
            cellChk1.setBorder(Rectangle.BOX);
            cellChk1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableCheck.addCell(cellChk1);
            
        }

            PdfPCell cellChk = new PdfPCell();
            cellChk.addElement(tableCheck);

            cellChk.setColspan(2);
            cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellChk.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellChk.setBorder(Rectangle.BOX);
            cellChk.setPadding(0);
            cellChk.setBorderColor(BaseColor.BLACK);
            table.addCell(cellChk);

            document.add(table);
        }

   
    // Método auxiliar para comparar conceptos
    private int compareConceptos(ExpedientePresupDTO p1, ExpedientePresupDTO p2) {
        String c1 = p1.getTlConcepto() != null ? p1.getTlConcepto() : "";
        String c2 = p2.getTlConcepto() != null ? p2.getTlConcepto() : "";
        int resultadoCompare = c1.compareToIgnoreCase(c2);
        return resultadoCompare;
    }
    
    
    public int compareByNumOrdenAndConcept(BloqueComponenteDTO c1, BloqueComponenteDTO c2, List<FinanciacionDatoDTO> listaFinancDato) {
        
        // Extraer el numOrden para c1
        if(listaFinancDato != null && !listaFinancDato.isEmpty()) {
            Integer orden1 =listaFinancDato.stream()
                    .filter(e -> e.getTipoDTO().getIdValor().equals(c1.getTipoFinanciacionDTO().getIdValor()))
                    .map(FinanciacionDatoDTO::getNmOrden)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

                // Extraer el numOrden para c2
                Integer orden2 = listaFinancDato.stream()
                    .filter(e -> e.getTipoDTO().getIdValor().equals(c2.getTipoFinanciacionDTO().getIdValor()))
                    .map(FinanciacionDatoDTO::getNmOrden)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

                // Manejo de nulos
                if (orden1 == null && orden2 == null) return compareConceptosBC(c1, c2);
                if (orden1 == null) return 1;
                if (orden2 == null) return -1;

                // Si el orden de la partida es el mismo, comparamos por concepto
                int resultadoPartida = orden1.compareTo(orden2);
                if (resultadoPartida == 0) {
                    return compareConceptosBC(c1, c2);
                }
                
                return resultadoPartida;
            }
            return compareConceptosBC(c1, c2);
    }
    
    
    // Método auxiliar para comparar conceptos de bloques componentes
    private int compareConceptosBC(BloqueComponenteDTO c1, BloqueComponenteDTO c2) {
        String b1 = extraerTextoConcepto(c1);
        String b2 = extraerTextoConcepto(c2);
        
        return b1.compareToIgnoreCase(b2);
    }
    
   // Método de soporte ultra-seguro contra NullPointerException para obtener la descripcion de los conceptos
    private String extraerTextoConcepto(BloqueComponenteDTO bc) {
        if (bc == null || bc.getDatos() == null || bc.getDatos().isEmpty()) {
            return "";
        }
        
        // Viajamos por la sublista buscando el primer dsValor que no sea nulo
        return bc.getDatos().stream()
                .filter(Objects::nonNull)
                .map(dato -> dato.getDsValor()) // Reemplaza 'getDsValor()' por el getter real de tu objeto interno si se llama diferente
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
    }

    @Override
    public void agregarTituloDatosProyecto(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidths(new int[] {1 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        Chunk chunk = new Chunk(Constantes.Evaluacion.TEXTO_DATOS_DEL_PROYECTO,
                tipoLetra10Bold);
        chunk.setUnderline(1.0f, -2.0f);
        Paragraph paragraph = new Paragraph(chunk);
        PdfPCell cell = null;
        cell = new PdfPCell(paragraph);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(5);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        table.addCell(cell);
        document.add(table);
    }

    @Override
    public void agregarDatosProyecto(Document document, String cdExpediente, String nifRazonSocial,
            String evaluador, String coordinador) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[] {40, 40 });
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPCell cell1 = null;
        cell1 = new PdfPCell(new Phrase(Constantes.Evaluacion.TEXTO_REFERENCIA, tipoLetra10));
        cell1.setBorderWidthRight(Rectangle.NO_BORDER);
        cell1.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(5);
        cell1.setPaddingTop(5);
        cell1.setPaddingBottom(5);
        table.addCell(cell1);

        PdfPCell cell2 = null;
        cell2 = new PdfPCell(new Phrase(cdExpediente != null ? cdExpediente : "", tipoLetra10));
        cell2.setBorderWidthLeft(Rectangle.NO_BORDER);
        cell2.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2.setPaddingLeft(5);
        cell2.setPaddingTop(5);
        cell2.setPaddingBottom(5);
        table.addCell(cell2);

        PdfPCell cell3 = null;
        cell3 = new PdfPCell(new Phrase(Constantes.Evaluacion.TEXTO_NIF_RAZON_SOCIAL, tipoLetra10));
        cell3.setBorderWidthTop(Rectangle.NO_BORDER);
        cell3.setBorderWidthRight(Rectangle.NO_BORDER);
        cell3.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setPaddingLeft(5);
        cell3.setPaddingTop(5);
        cell3.setPaddingBottom(5);
        table.addCell(cell3);

        PdfPCell cell4 = null;
        cell4 = new PdfPCell(new Phrase(nifRazonSocial != null ? nifRazonSocial : "", tipoLetra10));
        cell4.setBorderWidthTop(Rectangle.NO_BORDER);
        cell4.setBorderWidthLeft(Rectangle.NO_BORDER);
        cell4.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(5);
        cell4.setPaddingTop(5);
        cell4.setPaddingBottom(5);
        table.addCell(cell4);

        PdfPCell cell5 = null;
        cell5 = new PdfPCell(new Phrase(Constantes.Evaluacion.TEXTO_COORDINADOR, tipoLetra10));
        cell5.setBorderWidthTop(Rectangle.NO_BORDER);
        cell5.setBorderWidthRight(Rectangle.NO_BORDER);
        cell5.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell5.setPaddingLeft(5);
        cell5.setPaddingTop(5);
        cell5.setPaddingBottom(5);
        table.addCell(cell5);

        PdfPCell cell6 = null;
        cell6 = new PdfPCell(new Phrase(coordinador != null ? coordinador : "", tipoLetra10));
        cell6.setBorderWidthTop(Rectangle.NO_BORDER);
        cell6.setBorderWidthLeft(Rectangle.NO_BORDER);
        cell6.setBorderWidthBottom(Rectangle.NO_BORDER);
        cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell6.setPaddingLeft(5);
        cell6.setPaddingTop(5);
        cell6.setPaddingBottom(5);
        table.addCell(cell6);

        PdfPCell cell7 = null;
        cell7 = new PdfPCell(new Phrase(Constantes.Evaluacion.TEXTO_EVALUADOR, tipoLetra10));
        cell7.setBorderWidthTop(Rectangle.NO_BORDER);
        cell7.setBorderWidthRight(Rectangle.NO_BORDER);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(5);
        cell7.setPaddingTop(5);
        cell7.setPaddingBottom(5);
        table.addCell(cell7);

        PdfPCell cell8 = null;
        cell8 = new PdfPCell(new Phrase(evaluador != null ? evaluador : "", tipoLetra10));
        cell8.setBorderWidthTop(Rectangle.NO_BORDER);
        cell8.setBorderWidthLeft(Rectangle.NO_BORDER);
        cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell8.setPaddingLeft(5);
        cell8.setPaddingTop(5);
        cell8.setPaddingBottom(5);
        table.addCell(cell8);

        document.add(table);
    }

    @Override
    public void generarFirmaAutor(Document document, String tituloFirma, String datoFirma) throws DocumentException {
        try {
            
            agregarFilaVacio(document, 1);
            
            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = null;

            table.setWidths(new int[] {1 });
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setPadding(0);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            PdfPTable tableTituloFecha = new PdfPTable(1);
            tableTituloFecha.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableTituloFecha.getDefaultCell().setBorder(Rectangle.BOX);
            tableTituloFecha.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase(tituloFirma, tipoLetra9Bold));
            
            celdaTituloSuperior.setBackgroundColor(BaseColor.WHITE);
            celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaTituloSuperior.setPadding(6);
            tableTituloFecha.addCell(celdaTituloSuperior);
            table.addCell(tableTituloFecha);
            
            
            PdfPTable tableFirma = new PdfPTable(1);
            tableFirma.setWidthPercentage(100);
            tableFirma.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFirma.getDefaultCell().setBorder(Rectangle.BOX);
            tableFirma.getDefaultCell().setBorderColor(BaseColor.BLACK);
            
            cell = new PdfPCell(new Phrase(datoFirma, tipoLetra9));
            cell.setPadding(6);
            cell.setColspan(4);
            cell.setFixedHeight(25);
            tableFirma.addCell(cell);
            table.addCell(tableFirma);
            
            document.add(table);
           
        }catch (Exception e) {
            log.error(e.getMessage().toString());
        }
        
       
        
    }

    @Override
    public void agregarDocumentosInforme(Document document,
            List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO) throws DocumentException, ServiceException {
        
      //insertar titulo tabla documentos informe
        agregarFilaVacio(document, 1);

        PdfPTable tableResult = new PdfPTable(1);

        tableResult.setWidths(new int[] {1 });
        tableResult.setWidthPercentage(100);
        tableResult.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableResult.getDefaultCell().setPadding(0);
        tableResult.getDefaultCell().setBorder(Rectangle.BOX);
        tableResult.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPTable tableTitulo = new PdfPTable(1);
        tableTitulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tableTitulo.getDefaultCell().setBorder(Rectangle.BOX);
        tableTitulo.getDefaultCell().setBorderColor(BaseColor.BLACK);

        PdfPCell celdaTituloSuperior = new PdfPCell(new Phrase("Documentos aportados en la evaluación", tipoLetra9Bold));

        celdaTituloSuperior.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaTituloSuperior.setPadding(6);
        tableTitulo.addCell(celdaTituloSuperior);
        tableResult.addCell(tableTitulo);

        document.add(tableResult);

        PdfPTable table1 = new PdfPTable(new float[] {1 });
        table1.setWidthPercentage(100);
        table1.getDefaultCell().setBorder(Rectangle.BOX);
        table1.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table1.getDefaultCell().setPadding(6);
        table1.addCell(new Phrase("", tipoLetra9));
        document.add(table1);

        
        // fin
       
        for(ExpedienteDocEvalDTO docItem:listExpedienteDocEvalDTO) {
            Boolean subido=Boolean.FALSE;
            if(docItem.getCdFica() != null) {
                subido=Boolean.TRUE;
            }
            this.agregarDocumentoInforme(document,docItem.getIdTipoDocumento().getDsValor(),subido);
            
            
        }
        
    }
    
    
    private void agregarDocumentoInforme(Document document,
            String dsTipoDoc,  Boolean subidoDoc) throws DocumentException, ServiceException {
        
            if (subidoDoc == null){
                subidoDoc = Boolean.FALSE;
            }
 
            Map<String, Object> configuracionAdicional = new HashMap<>();
            configuracionAdicional.put(aliasMostrarEtiquetaCheck, true);
            configuracionAdicional.put(aliasMostrarUnaColumna, false);


            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);
            table.getDefaultCell().setPadding(0);

            
            List<String> lstOpciones = Arrays
             .asList(dsTipoDoc);

             List<String> lstValores = new ArrayList<>();
             String nombreCheck = "chk_ Doc_Subido";
             lstValores.add(nombreCheck);
                

            PdfPTable tableCheck = new PdfPTable(new float[] {4.7f, 0.3f });
            tableCheck.setWidthPercentage(100);
            tableCheck.getDefaultCell().setBorder(Rectangle.BOX);

        for (int indice = 0; indice < lstValores.size(); indice++) {
            PdfPCell cellChkLabel = new PdfPCell(new Phrase(lstOpciones.get(indice), tipoLetra8));
            cellChkLabel.setVerticalAlignment(Element.ALIGN_LEFT);
            cellChkLabel.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cellChkLabel.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
           
            cellChkLabel.setBorder(Rectangle.BOX);
            cellChkLabel.setPadding(6);
            tableCheck.addCell(cellChkLabel);



            PdfPCell cellChk1 = new PdfPCell(new Phrase(" "));
            if (subidoDoc.equals(Boolean.TRUE)){
                cellChk1.setCellEvent(new CheckboxCheckedCellEvent(lstValores.get(indice), subidoDoc));
            
            } else {
                cellChk1.setCellEvent(new CheckboxCellEvent(lstValores.get(indice)));
            }

            cellChk1.setPadding(0);
            cellChk1.setBorder(Rectangle.BOX);
            cellChk1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableCheck.addCell(cellChk1);
            
        }

            PdfPCell cellChk = new PdfPCell();
            cellChk.addElement(tableCheck);

            cellChk.setColspan(2);
            cellChk.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellChk.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellChk.setBorder(Rectangle.BOX);
            cellChk.setPadding(0);
            cellChk.setBorderColor(BaseColor.BLACK);
            table.addCell(cellChk);

            document.add(table);
        }
    
    /**
     * Agrega el título del informe de evaluación centrado.
     *
     * @param document Documento PDF
     * @param titulo Título del informe
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloInformeEvaluacion(Document document, String titulo)
            throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cell = new PdfPCell(new Phrase(titulo, tipoLetra10Bold));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);
    }

 



}
