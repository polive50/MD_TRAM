package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Documentacion.
 *
 * @author everis
 */
public interface DocumentoService {

    /**
     * Devuelve el documento del documento del expediente
     *
     * @param idExpedienteDoc the id expediente doc
     * @return the documento DTO
     * @throws ServiceException the service exception
     */
    DocumentoDTO descargar(final Integer idExpedienteDoc) throws ServiceException;

    /**
     * Devuelve el documento del documento del expediente
     *
     * @param idExpedienteDoc the id expediente doc
     * @return the documento DTO
     * @throws ServiceException the service exception
     */
    DocumentoDTO descargarHistorico(final Integer idExpedienteDocHist) throws ServiceException;

    /**
     * Permite descargar la plantilla de los documentos de la convocatoria
     *
     * @param idConvocatoriaTipoDoc the id convocatoria Tipo DOc
     * @return DocumentoDTO
     * @throws ServiceException the service exception
     */
    DocumentoDTO descargarPlantilla(final Integer idConvocatoriaTipoDoc) throws ServiceException;

    /**
     * Obtiene la lista de documentos de un expediente.
     *
     * @param expedienteDTO
     * @param bFica true si recupera el fichero de fica / false si no
     * @param idTramite
     * @return List<DocumentoDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoDTO> obtenerDocumentosExpediente(final ExpedienteDTO expedienteDTO,
            final Boolean bFica, Integer idTramite, final List<ExpedienteDocDTO> lstExpDocDTO) throws ServiceException;
    
    /**
     * Obtener la lista de documentos de un expediente
     * y solo descarga los documentos marcados como descargables
     * 
     * @param expedienteDTO
     * @param bFica
     * @param idTramite
     * @param lstExpDocDTO
     * @return
     * @throws ServiceException
     */
    List<DocumentoDTO> obtenerDocumentosExpedienteEval(final ExpedienteDTO expedienteDTO,
            final Boolean bFica, Integer idTramite, final List<ExpedienteDocDTO> lstExpDocDTO) throws ServiceException;

    /**
     * Obtiene la lista de documentos que tiene una solicitud.
     *
     * @param idSolicitud the id solicitud
     * @para bFica true si recupera el fichero de fica / false si no
     * @return the list
     * @throws ServiceException the service exception
     */
    List<DocumentoDTO> obtenerDocumentosSubsanacionSolicitud(final Integer idSolicitud,
            final Boolean bFica) throws ServiceException;

    /**
     * Obtiene la lista de documentos de un expediente.
     *
     * @param expedienteDTO
     * @para bFica true si recupera el fichero de fica / false si no
     * @return List<DocumentoDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoDTO> obtenerDocumentosSubsanacionExpediente(final ExpedienteDTO expedienteDTO,
            final Boolean bFica) throws ServiceException;

    /**
     * descargar notificación subsanación.
     *
     * @param idDocumentoPtfr
     * @return DocumentoDTO
     * @throws ServiceException the service exception
     */
    public DocumentoDTO descargarNotificacionSubsanacion(Integer idDocumentoPtfr)
            throws ServiceException;

    /**
     * descargar notificacion desistido excluido.
     *
     * @param idExpediente
     * @return DocumentoDTO
     * @throws ServiceException the service exception
     */
    public DocumentoDTO descargarNotificacionDesistidoExcluido(Integer idExpediente)
            throws ServiceException;

    /**
     * Obtiene la lista de documentos de informe coordinador.
     *
     * @param idexpediente
     * @para bFica true si recupera el fichero de fica / false si no
     * @return List<DocumentoDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoDTO> obtenerDocumentosExpedienteEval(final Integer idExpediente,
            final Boolean bFica) throws ServiceException;

    /**
     * Obtiene la lista de documentos de informe coordinador.
     *
     * @param idexpediente
     * @para bFica true si recupera el fichero de fica / false si no
     * @return List<DocumentoDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoDTO> obtenerDocExpedienteEval(final Integer idExpedienteEval, String itCoorEva,
            final Boolean bFica) throws ServiceException;

    /**
     * Metodo para descargar fichero fica
     *
     * @param cdFica
     * @return
     * @throws ServiceException
     */
    public DocumentoDTO descargarFicheroFica(String cdFica) throws ServiceException;

    /**
     * Metodo para obtener el documento en base al codido de Fica
     *
     * @param cdFica
     * @return
     * @throws ServiceException
     */
    public DocumentoDTO obtenerDocAleg(Integer cdFica) throws ServiceException;

    /**
     * 
     * @param expedienteDTO
     * @param idExpedienteJustec
     * @param bFica
     * @return
     * @throws ServiceException
     */
    public List<DocumentoDTO> obtenerDocumentosExpedienteJustec(ExpedienteDTO expedienteDTO,
            Integer idExpedienteJustec, Boolean bFica, Boolean subsanar) throws ServiceException;

    /**
     * 
     * @param expedienteDTO
     * @param bFica
     * @param lstExpedienteDocDTO
     * @return
     * @throws ServiceException
     */
    List<DocumentoDTO> obtenerDocumentosExpedienteContrato(ExpedienteDTO expedienteDTO,
            Boolean bFica, List<ExpedienteDocDTO> lstExpedienteDocDTO)
            throws ServiceException;
    
    /**
     * 
     * @param cdFica
     * @return
     * @throws ServiceException
     */
    DocumentoDTO descargarExpedienteEvalJustec(Integer cdFica) throws ServiceException;
    
   /**
    *  
    * @param idExpedienteDoc
    * @return
    * @throws ServiceException
    */
    DocumentoDTO descargarExpedienteDocJustec(Integer idExpedienteDoc) throws ServiceException;

    List<DocumentoDTO> obtenerDocumentosExpModifPresupPlazosOtros(ExpedienteDTO expedienteDTO,
        List<ExpedienteDocDTO> lstExpedienteDocDTO, Boolean subsanar) throws ServiceException;

    DocumentoDTO descargarNotificacionResolucionProvisional(Integer idExpediente) throws ServiceException;

    DocumentoDTO descargarNotificacionResolucionDefinitiva(Integer idExpediente) throws ServiceException;

    List<DocumentoDTO> obtenerDocumentosExpedienteJusEco(
            List<ExpedienteDocDTO> listaDocs) throws ServiceException;

    DocumentoDTO subir(DocumentoDTO documentoDTO) throws ServiceException;

    List<ConvocatoriaDocEvalDTO> obtenerDocumentoConvocatoria(
            ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException;

    ConvocatoriaDocEvalDTO guardarDocumentoConvocatoria(
            ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException;

    List<ConvocatoriaEvalTipoDocDTO> obtenerTipoDocumentos(Integer idNivel, Integer idConvocatoria) throws ServiceException;

    DocumentoDTO descargarPlantillaConEvalTipoDoc(Integer idConvocatoriaTipoDoc) throws ServiceException;

    ExpedienteEvalDTO guardarDocumentoExpedienteEval(ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException;

    List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException;

    List<EvaluacionTipoDocDTO> obtenerEvalTipoDocumentosInforme(Integer idInforme) throws ServiceException;

    List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO) throws ServiceException;

    
    
    
}
