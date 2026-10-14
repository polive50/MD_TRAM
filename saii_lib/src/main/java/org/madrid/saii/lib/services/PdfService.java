package org.madrid.saii.lib.services;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio de llenado de PDF
 */
public interface PdfService {

    /**
     * Crear y firma el pdf del expediente
     *
     * @param dto ExpedienteDTO
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO crearFirmar(ExpedienteDTO dto) throws ServiceException;

    /**
     * Obtiene la plantilla
     *
     * @param dto
     * @return byte[]
     * @throws ServiceException
     */
    byte[] obtenerPlantilla(ExpedienteDTO dto) throws ServiceException;

    byte[] obtenerPlantilla(ExpedienteDTO dto, Map<String, String> mapaDatos)
            throws ServiceException;

    /**
     * Rellena el pdf del dto sin firmar
     *
     * @param expedienteDTO the expediente DTO
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarExpediente(ExpedienteDTO expedienteDTO) throws ServiceException;

    /**
     * Genera los pdf de los listados de documentos adjuntos
     *
     * @param expedienteDTO the expediente DTO
     * @return List<DocumentoDTO>
     * @throws ServiceException
     */
    List<DocumentoDTO> descargarListadoDocumentos(Integer idTramite, ExpedienteDTO expedienteDTO,String cdEreg)
            throws ServiceException;

    /**
     * Crea un zip con los documentos de la solicitud
     *
     * @param idSolicitud
     * @return DocumentoDTO
     * @throws ServiceException the service exception
     */
    DocumentoDTO descargarZipSolicitud(Integer idSolicitud) throws ServiceException;

    /**
     * Genera el zip de descarga del expediente
     *
     * @param idExpediente the id expediente
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipExpediente(Integer idExpediente) throws ServiceException;
    
    /**
     * Genera el zip de descarga del expediente
     * teniendo en cuenta si se ha marcado cada doc como descargable en eval
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarZipExpedienteEval(Integer idExpediente) throws ServiceException;

    /**
     * Genera el zip con los documentos del Informe de valoracion de Alegaciones
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarInformeValAlegacion(Integer idExpediente) throws ServiceException;

    /**
     * Genera, firma, anexa a Ereg el listado de Documentos
     *
     * @param dto
     * @throws ServiceException
     */
    String generarFirmarListadoAnexos(ExpedienteDTO dto, Integer idTramite, String asunto, List<ExpedienteDocDTO> lstExpDocDTO)
            throws ServiceException;

    /**
     * Crea un formulario de evaluación
     *
     * @param idEvaluacion
     * @param idConvocatoria
     * @return the byte[]
     */
    byte[] crearFormularioEvaluacion(Integer idConvocatoria, String itCoorEval)
            throws ServiceException;
    
    /**
     * Crea un formulario de evaluación
     *
     * @param idEvaluacion
     * @param idConvocatoria
     * @return the byte[]
     */
    byte[] crearFormularioEvaluacion(Integer idConvocatoria, Integer idTipoInforme)
            throws ServiceException;

    /**
     * Crea un formulario de evaluación
     *
     * @param idEvaluacion
     * @param idConvocatoria
     * @return the byte[]
     */
    byte[] crearFormularioEvaluacionJustec(Integer idConvocatoria, String itTipoEval)
            throws ServiceException;

    /**
     * Crea un formulario de evaluación con datos
     *
     * @param expedienteEvalDTO
     * @return the byte[]
     */
    Document crearFormularioEvaluacionDatosIdExpEval(ByteArrayOutputStream baos,Document documento,ExpedienteEvalDTO expedienteEvalDTO,Boolean docFirmado)
            throws ServiceException;

    /**
     * Genera el zip de descarga de informe coordinador
     *
     * @param idExpediente the id expediente
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarInformeCoordinador(Integer idExpediente) throws ServiceException;

    /**
     * Genera el zip de descarga de informe coordinador o Evaluador
     *
     * @param idExpediente the id expediente
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarInformeCoorEval(Integer idExpedienteEval, String itCoorEval)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    byte[] crearFormularioAlegacion(Integer idConvocatoria) throws ServiceException;

    /**
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    byte[] crearFormularioAlegacionDato(Integer idExpediente, String cdNumIden)
            throws ServiceException;

    /**
     *
     * @param convocatoriaPlaDTO
     * @return
     * @throws ServiceException
     */
    byte[] crearFormularioPlantilla(ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException;

    /**
     *
     * @param convocatoriaPlaDTO
     * @return
     */
    Boolean esPorNote(ConvocatoriaPlaDTO convocatoriaPlaDTO);

    /**
     * Metodo para generar la sección de cabecera de un PDF de trámite
     *
     * @param convocatoriaDTO
     * @param convocatoriaPlaDTO
     * @param document
     * @param baos
     * @param esRegistroEreg
     * @throws ServiceException
     */
    void generarCabeceraPdfTramite(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, PdfWriter writer, Boolean esRegistroEreg)
            throws ServiceException;

    /**
     * Genera el zip de descarga alegaciones
     *
     * @param idExpedientedoc the id expediente doc
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipAlegacion(Integer idExpedienteDoc) throws ServiceException;

    /**
     * Genera el zip de descarga alegaciones
     *
     * @param idExpedientedoc the id expediente doc
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipTramite(Integer idExpedienteDoc) throws ServiceException;

    /**
     * 
     * @param expedienteDTO
     * @param expedienteJustec
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarExpedienteJustec(ExpedienteDTO expedienteDTO,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException;

    /**
     * 
     * @param dto
     * @param mapaDatos
     * @param impresoFormulario
     * @return
     * @throws ServiceException
     */
    byte[] obtenerPlantillaJustec(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario) throws ServiceException;

    /**
     * 
     * @param idExpediente
     * @param idExpedienteJustec
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarZipExpedienteJustec(Integer idExpediente, Integer idExpedienteJustec)
            throws ServiceException;

    /**
     * 
     * @param dto
     * @param expedienteJustec
     * @param asunto
     * @return
     * @throws ServiceException
     */
    String generarFirmarListadoJusTecAnexos(ExpedienteDTO dto, ExpedienteJusTecDTO expedienteJustec,
            String asunto, List<ExpedienteDocDTO> listaDocumentosDTO,Boolean subsanado) throws ServiceException;

    /**
     * 
     * @param expedienteDTO
     * @param idExpedienteJustec
     * @param object
     * @throws ServiceException 
     */
    void generaFirmaReciboPresentacionJustec(ExpedienteDTO expedienteDTO,
            Integer idExpedienteJustec, Object object,List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException;
    
    
    /**
     * Genera el zip de descarga contratos
     *
     * @param idExpediente the id expediente 
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipContratos(Integer idExpediente) throws ServiceException;
    
    /**
     * 
     * @param expedienteDTO
     * @param idExpedienteJustec
     * @param object
     * @return 
     * @throws ServiceException 
     */
    String generaFirmaReciboPresentacionContrato(ExpedienteDTO expedienteDTO,
            Object object,List<ExpedienteDocDTO> lstExpedienteDocDTOAux, List<ExpedienteContrato> lstExpedienteContrato) throws ServiceException;


   
    /**
     * 
     * @param dto
     * @param mapaDatos
     * @param impresoFormulario
     * @return
     * @throws ServiceException
     */

    byte[] obtenerPlantillaContrato(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException;


    /**
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarZipExpedienteContrato(Integer idExpedienteDoc, String eregPresent)
            throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarZipDocsExpedienteContrato(Integer idExpediente,Integer tramite)
            throws ServiceException;

    /**
    *
    */
    DocumentoDTO descargarExpedienteContrato(ExpedienteDTO expedienteDTO) throws ServiceException;

    String generarFirmarListadoContratoAnexos(ExpedienteDTO dto, String asunto,
            List<ExpedienteDocDTO> listaDocumentosDTO) throws ServiceException;

    byte[] obtenerInformePresentacionContrato(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException;

    /**
     * Genera el zip de descarga del expediente Eval Justec
     *
     * @param idExpedienteEvalJusTec the id ExpedienteEvalJusTec
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipExpedienteEvaluacionJustecCT(Integer idExpedienteEvalJusTec) throws ServiceException;

    /**
    *
    */
    byte[] crearFormularioEvaluacionJustecDatosIdExpEval(
         ExpedienteEvalJusTecDTO expedienteEvalJustecDTO) throws ServiceException;

    /**
       * Método que genera el documento PDF de la subsanacion JCT con el anexo, lo firma y lo sube a
       * EREG
       *
       * @param dto ExpedienteDTDo
       * @return String código del anexo
       * @throws ServiceException
       */
    void generaFirmaReciboSubsanacionJustec(ExpedienteDTO dto, ExpedienteJusTecDTO expedienteJustec,
            Object object, List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException;

    /**
      *
      */
    DocumentoDTO descargarPdfExpedienteContrato(ExpedienteDTO expedienteDTO,
            List<ExpedienteContrato> contratos, String eregPresent) throws ServiceException;

    void generaFirmaReciboPresentacionPresupPlazosOtros(ExpedienteDTO expedienteDTO) throws ServiceException;

    DocumentoDTO descargarModificacionesPresupPlazosOtros(Integer idModificacion) throws ServiceException;

    void generaFirmaReciboPresentacionReq(ExpedienteDTO expedienteDTO, Object object,
            List<ExpedienteDocDTO> lstExpedienteDocDTO, List<ExpedienteDTO> listExpParticipantes) throws ServiceException;

    byte[] obtenerPlantillaSubsanacionReq(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException;
    
    /**
     * Genera el zip de gestion subsanacion
     *
     * @param idExpedientedoc the id expediente doc
     * @return DocumentoDTO
     * @throws ServiceException
     */
    DocumentoDTO descargarZipTramiteSubsanacionReq(Integer idExpedienteDoc) throws ServiceException;
    
    /**
     * Genera firma recibo presentacion administrativa
     * @param expedienteDTO
     * @param object
     * @param lstExpedienteDocDTOAux
     * @param lstExpedienteContrato
     * @return
     * @throws ServiceException
     */
    String generaFirmaReciboPresentacionAdm(ExpedienteDTO expedienteDTO, Object object,
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux,
            List<ExpedienteDTO> lstExpediente) throws ServiceException;
    
    /**
     * Obtiene plantilla subsanacion administrativa
     * @param dto
     * @param mapaDatos
     * @param impresoFormulario
     * @param listExpedientesParticipantes
     * @param convocatoriaDTOIn
     * @return
     * @throws ServiceException
     */
    byte[] obtenerPlantillaSubsanacionAdm(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException;

    void generaFirmaReciboPresentacionAceptacion(ExpedienteDTO expedienteDTO,
            List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException;

    DocumentoDTO descargarAceptacion(Integer idExpediente) throws ServiceException;

    Integer calcularUnidadAdmin(Solicitud solicitud)throws ServiceException;

    void generaFirmaReciboPresentacionJusEco(ExpedienteDTO expedienteDTO,
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO) throws ServiceException;

    DocumentoDTO descargarZipExpedienteJusEco(Integer idExpedienteJusEco) throws ServiceException;

    DocumentoDTO descargarFormulariosSolicitudExpediente(Integer idExpediente) throws ServiceException;

    Document datosFirmanteEvalCoor(Document documentInforme, String tituloFirma, String datoFirma) throws ServiceException;

    byte[] generarFirmaInformeEvalCoor(byte[] byteArray) throws ServiceException;

    DocumentoDTO descargarInforme(Integer cdFica) throws ServiceException;

    DocumentoDTO descargarZipBorradorSolicitud(Integer idSolicitud) throws ServiceException;

    DocumentoDTO descargarReciboFirmadoSolicitud(Integer idSolicitud) throws ServiceException;


   
}
