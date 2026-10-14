package org.madrid.saii.lib.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
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
import org.madrid.saii.lib.dto.RepresentanteJurDTO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;

public interface InformeHelperPdfService {
    
   /**
    * Método para crear el titulo del informe
    *
    * @param document Documento PDF
    * @param tituloSup título superior
    * @param tituloInf título inferior
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTitulo(Document document, String tituloSup, String tituloInf, String desImpreso,String cdExpediente,
           Boolean agregarEspacioLogos, Boolean mostrarRegistroEntrada) throws DocumentException;

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
           throws DocumentException, ServiceException ;

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
           throws DocumentException, ServiceException;

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
           ConvocatoriaAct convocatoriaAct) throws DocumentException, ServiceException;

   /**
    * Método para crear celda con una componente tipo multitexto
    *
    * @param nombre Nombre del componente
    * @return PdfPCell Celda con el elemento
    */
   public PdfPCell celdaCampoMultiTexto(String nombre);

   /**
    * Método para crear celda con una componente tipo multitexto, calculando la
    * altura mínima de la celda en función de la longitud máxima permitida del
    * componente para que el contenido no salga cortado
    *
    * @param nombre Nombre del componente
    * @param nmLongitud Longitud máxima de caracteres permitida del componente
    * @return PdfPCell Celda con el elemento
    */
   public PdfPCell celdaCampoMultiTexto(String nombre, Integer nmLongitud);

   public void celdaCampoMultiTextoVacio(Document document) throws DocumentException;

   public void celdaCampoTextoVacio(Document document) throws DocumentException;

   public void celdaCampoMultiTextoExpEval(Document document, String dato)
           throws DocumentException;

   public void celdaCampoTextoExpEval(Document document, String dato)
           throws DocumentException;

   /**
    * Método para crear filas vacias en el documento
    *
    * @param document Documento PDF
    * @param repeticiones Cantidad de repeticiones
    */
   public void agregarFilaVacio(Document document, int repeticiones)
           throws DocumentException;

   /**
    * Método para agregar un espacio vertical más pequeño que agregarFilaVacio, para separar
    * ligeramente elementos consecutivos sin dejar tanto hueco entre ellos
    *
    * @param document Documento PDF
    */
   public void agregarEspacioPequeno(Document document)
           throws DocumentException;

   /**
    * Método para agregar campo cuando la convocatoria incluye fondos europeos
    *
    * @param document documentoPDF
    *
    */
   public void agregarTextoFondosEuropeos(Document document) throws DocumentException;

   /**
    * Método para añadir el campo de la huella digital
    *
    * @param document documentoPDF
    * @throws DocumentException excepción
    */
   public void agregarHuellaDigital(Document document) throws DocumentException;

   /**
    * Método para agregar cuando Lugar y Fecha
    *
    * @param document documentoPDF
    * @throws DocumentException excepción
    */
   public void agregarLugarFecha(Document document) throws DocumentException;

   /**
    * Método para agregar "Firmado electrónicamente" en una posición fija cerca del pie de página.
    * La fecha se muestra en la cabecera del documento, junto al nombre/razón social.
    *
    * @param document documentoPDF
    * @param writer PdfWriter del documento
    */
   public void agregarLugarFechaFirmaElectronica(Document document, PdfWriter writer);

   /**
    * Método para agregar destinatario
    *
    * @param writer writer
    * @param document documentoPDF
    * @param convocatoriaDTO convocatoriaDTO
    * @throws DocumentException excepción
    */
   public void agregarDestinatario(PdfWriter writer, Document document,
           ConvocatoriaDTO convocatoriaDTO,String consejeria) throws DocumentException;

   /**
    * Método para crear un cuerpo de tipo texto
    *
    * @param document Documento PDF
    * @param texto texto de la celda
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTextoCuerpo(Document document, String texto)
           throws DocumentException;

   /**
    * Crea la tabla de documentación
    *
    * @param document the document
    * @param lstDocumentosDTO lista documentos
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarListadoDocumentos(Document document,
           List<DocumentoDTO> lstDocumentosDTO) throws DocumentException;

   /**
    * Crea la tabla de notas
    *
    * @param document the document
    * @param lstEvaluacionNotaDTO lista notas
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarListadoEvaluacionesNota(Document document,
           List<EvaluacionNotaDTO> lstEvaluacionNotaDTO, Integer nota) throws DocumentException;

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
           throws DocumentException;

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
           throws DocumentException;

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
           throws DocumentException;

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
           throws DocumentException;

   /**
    * Método para crear el titulo de evaluación
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloEvaluacion(Document document, String titulo)
           throws DocumentException;

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
           throws DocumentException;
   
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
           throws DocumentException;

   /**
    * Método para crear el titulo de valoración global
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarValoracionGlobal(Document document, String titulo)
           throws DocumentException;

   /**
    * Método para crear el titulo de alegación
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloAlegacion(Document document, String titulo, float letterSize)
           throws DocumentException;

   /**
    * Método para crear la descripción de evaluación
    *
    * @param document Documento PDF
    * @param descripcion
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarDescripcion(Document document, String descripcion)
           throws DocumentException;

   /**
    * Método para crear la descripción de asunto
    *
    * @param document Documento PDF
    * @param descripcion
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarDescripcionAsunto(Document document, String descripcion)
           throws DocumentException;

   /**
    * Método para crear el titulo de apartado
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloApartado(Document document, String titulo)
           throws DocumentException;

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
   public void agregarDescripcionLeft(Document document, String descripcion, String textoApart,
           String comentario, String itInformativo, String observacionEnSucaso) throws DocumentException;

   /**
    * Método para crear la calificación maxima de evaluación apartado
    *
    * @param document Documento PDF
    * @param calificacion
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarCalificacion(Document document, String calificacion)
           throws DocumentException;

   /**
    * Método para crear la calificación maxima de evaluación apartado nota
    *
    * @param document Documento PDF
    * @param calificacion
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarCalificacionAparNota(Document document, String calificacion,
           String nota) throws DocumentException;

   /**
    * Método para crear el titulo de cabecera expediente
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloCabeceraExpediente(Document document, String subTitulo,
           String descripcion) throws DocumentException;

   /**
    * Método para crear el titulo de cabecera asunto
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloCabeceraAsunto(Document document, String subTitulo,
           String descripcion) throws DocumentException;

   /**
    * Método para crear el titulo del expediente de evaluación.
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloExpedienteEval(Document document, String subTitulo)
           throws DocumentException;

   /**
    * Método para crear el titulo de cabecera coordinador
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloCabeceraCoorEval(Document document, String subTitulo,
           String descripcion) throws DocumentException;

   /**
    * Método para crear el titulo de cabecera fecha
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarTituloCabeceraFecha(Document document, String subTitulo,
           String descripcion) throws DocumentException;

   /**
    * Método para crear el titulo de cabecera coordinador
    *
    * @param document Documento PDF
    * @param titulo
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento investigador
    */
   public void agregarTituloCabeceraInvestigador(Document document, String subTitulo,
           String descripcion) throws DocumentException;

   /**
    *
    * @param document
    * @param tipo
    * @throws DocumentException
 * @throws ServiceException 
    */

   public void agregarTipo(Document document, CatalogosValoresDTO tipo, String valor)
           throws DocumentException, ServiceException;

   public void agregarSubTituloPlantilla(Document document, String texto,
           String descripcion, int ancho, Boolean isHtml) throws DocumentException;

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
           throws DocumentException ;

   /**
    * Método para crear la descripción de las tablas en actuación
    *
    * @param document Documento PDF
    * @param descripcion
    * @throws DocumentException Excepción generada al generar contenido del
    *             documento
    */
   public void agregarDescripcionActuacion(Document document, String descripcion)
           throws DocumentException ;
   
   public void agregarBloqueParticipantesContrato(Document document, 
           ConvocatoriaAct convocatoriaAct, List<ExpedienteDTO> listExpedientesParticipantes) throws DocumentException;
   
   public void agregarBloqueDatosInteresado(Document document, 
           ExpedienteDTO expediente, String titulo1) throws DocumentException;
   
   public void agregarBloqueParticipantesSubsanacionReq(Document document, 
           ConvocatoriaAct convocatoriaAct, List<ExpedienteDTO> listExpedientesParticipantes) throws DocumentException;
   
   public void agregarBloqueDatosRepresentanteLegalSol(Document document, 
           RepresentanteJurDTO representanteLegal, String titulo) throws DocumentException;
   
   public void agregarBloqueDatosOrganismoSolicitante(Document document, 
           ExpedienteDTO expediente, String titulo1) throws DocumentException;
   
   public void agregarBloqueParticipantesOrganismo(Document document,
          List<ExpedienteDTO> listaParticipantes, String titulo) throws DocumentException, ServiceException;

   public void agregarBloqueMultiregistro(PdfWriter writer, Document document, BloqueDTO bloque,
           List<BloqueDTO> listaSubbloqueDTO, Date fechaFinPresentacion) throws DocumentException;
   
   public PdfPCell celdaEtiquetaCabecera(String texto, boolean border);
   
   public PdfPCell celdaEtiqueta(String texto, boolean border);
   
   public void agregarTextoEnriquecido(Document document, String texto,
           String descripcion, int ancho, Boolean isHtml) throws DocumentException;

   public void agregarTipoComponenteCheck(PdfPTable table, BloqueComponenteDTO componente,
           boolean mostrarEtiqueta, String nombreFila, boolean mostrarUnaColumna)
           throws DocumentException;
   
   public void agregarTipoComponenteRadio(PdfWriter writer, PdfPTable table,
           BloqueComponenteDTO componente, List<PdfFormField> agrupadores, String nombreFila,
           boolean mostrarUnaColumna, boolean mostrarEtiquetaCheck, boolean mostrarBorde) throws DocumentException;
   
   public void agregarBloqueTipoNotificacion(PdfWriter writer, Document document,
           BloqueDTO bloque) throws DocumentException, ServiceException;

   public void agregarBloquePersonaNotificacion(PdfWriter writer, Document document,
                                                String tipo, Integer idSeleccionado, Boolean existeEmail) throws DocumentException, ServiceException;

   public void agregarBloqueAceptarRecharAyuda(PdfWriter writer, Document document,
        String selectValue) throws DocumentException, ServiceException;

public void crearInformeJustificacionEco(Document document,
        List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO, ExpedienteDTO expedienteDTO,
        String titulo, ConvocatoriaDTO convocatoriaDTO) throws DocumentException, ServiceException;

public void agregarDatosEntidadSolicitante(Document document, ExpedienteDTO expedienteDTO, ConvocatoriaActDTO convocatoriaActDTO) throws DocumentException, ServiceException;

public void agregarBloqueModalidadExpediente(Document document,
        ExpedienteDTO expedienteDTO) throws DocumentException, ServiceException;

public void agregarBloqueSubArea(Document document, ExpedienteDTO expedienteDTO) throws DocumentException, ServiceException;

    public FinanciacionDTO obtenerFinanciacion(String itTipoFinanciacion,
        Integer idConvocatoria, Integer idTematica,Integer idActor) throws ServiceException;
    
    public int compareByNumOrden(BloqueComponenteDTO c1, BloqueComponenteDTO c2, List<FinanciacionDatoDTO> listaFinancDato);

    public int compareByNumOrdenPresup(ExpedientePresupDTO p1, ExpedientePresupDTO p2,
            List<FinanciacionDatoDTO> listaFinancDato);

    public void agregarDatosAutorizado(PdfWriter writer, Document document,
            ExpedienteDTO expedienteDTO) throws DocumentException, ServiceException;

    public void agregarDocumentoJustifAutorizado(Document document, String string, Boolean subido) throws DocumentException, ServiceException;

	public int compareByNumOrdenAndConcept(BloqueComponenteDTO c1, BloqueComponenteDTO c2, List<FinanciacionDatoDTO> listaFinancDato);
	
    /**
     * Método para añadir título de datos del proyecto
     * @param document
     */
    public void agregarTituloDatosProyecto(Document document) throws DocumentException;
    
    /**
     * 
     * @param document
     * @param cdExpediente
     * @param cdAcronimo
     * @param nifSolicitante
     * @param razonSocial
     * @param evaluador
     * @param coordinador
     * @throws DocumentException
     */
    public void agregarDatosProyecto(Document document, String cdExpediente, String nifRazonSocial,
            String evaluador, String coordinador) throws DocumentException;

    public void generarFirmaAutor(Document documentInforme, String tituloFirma,String datoFirma) throws DocumentException;

    public void agregarDocumentosInforme(Document document, List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO) throws DocumentException, ServiceException;
    
    
    /**
     * Agrega el título del informe de evaluación centrado.
     *
     * @param document Documento PDF
     * @param titulo Título del informe
     * @throws DocumentException Excepción generada al generar contenido del
     *             documento
     */
    public void agregarTituloInformeEvaluacion(Document document, String titulo)
            throws DocumentException;

    public void agregarContenidoFinalAdministrativo(Document document, PdfWriter writer,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ConvocatoriaDTO convocatoriaDTO) throws DocumentException;

    public void agregarLugarFechaFirmaElectronicaADM(Document document, PdfWriter writer,
            String dsFirmante) throws DocumentException;
    
    
    public void agregarTextoEnriquecidoADM(Document document, String texto,
            String descripcion, int ancho, Boolean isHtml) throws DocumentException; 


}
