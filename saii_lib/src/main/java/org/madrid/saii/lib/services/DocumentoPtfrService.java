package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EnvioDocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio DocumentoPtfr
 *
 * @author everis
 */
public interface DocumentoPtfrService
        extends BaseService<DocumentoPtfr, Integer, DocumentoPtfrDAO> {

    /**
     * Guarda un/a documentoPtfr.
     *
     * @param documentoPtfrDTO the documentoPtfr DTO
     * @return documentoPtfrDTO
     * @throws ServiceException the service exception
     */
    DocumentoPtfrDTO guardar(DocumentoPtfrDTO documentoPtfrDTO) throws ServiceException;

    /**
     * Enviar documento a Portafirma.
     *
     * @param idConvocatoria Id Convocatoria
     * @param idExpediente Id Expediente
     * @param envio 
     * @return documentoPtfrDTO
     * @throws ServiceException the service exception
     */
    DocumentoPtfrDTO enviarPortaFirmas(ConvocatoriaDTO convocatoriaDTO, Integer idTipoTramite,
            Integer idExpediente, EnvioDocumentoPtfrDTO envio, Integer idDocumentoPTFR)
            throws ServiceException;

    /**
     * Método para verificar la lectura de la notificación NOTE y el plazo de
     * lectura
     *
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    void consultarLecturaNotificacionNoteBatch() throws ServiceException;

    /**
     * VMM 26/10/26 MD-3168: procesa la lectura de NOTE de un unico envio en su propia transaccion
     * (REQUIRES_NEW). Lo usa consultarLecturaNotificacionNoteBatch para que el fallo de un envio no
     * revierta el resto.
     *
     * @param idDocumentoPtfr identificador del envio a Portafirmas/NOTE
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    void procesarLecturaNotificacionNoteEnvio(Integer idDocumentoPtfr) throws ServiceException;

    /**
     * Método para validar la respuesta de Portafirmas
     *
     * @throws ServiceException
     */
    void consultarRespuestaPortafirmasBatch() throws ServiceException;

    /**
     * VMM 26/10/26 MD-3168: procesa la respuesta de Portafirmas de un unico envio en su propia
     * transaccion (REQUIRES_NEW). Lo usa consultarRespuestaPortafirmasBatch para que el fallo de un
     * envio no revierta el resto.
     *
     * @param idDocumentoPtfr identificador del envio a Portafirmas
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    void procesarRespuestaPortafirmasEnvio(Integer idDocumentoPtfr) throws ServiceException;

    /**
     * Busca los documento en portafirma de un expediente.
     *
     * @param Integer idExpediente
     * @return List<DocumentoPtfrDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoPtfrDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Busca los documento en portafirma de un expediente.
     *
     * @param  idExpediente
     * @param  nmTramite
     * @return DocumentoPtfrDTO
     * @throws ServiceException the service exception
     */
    List<DocumentoPtfrDTO> buscarPorExpedienteTramite(Integer idExpediente, Integer nmTramite) throws ServiceException;

    /**
     *
     * @param idDocumentoPtfr
     * @return
     * @throws ServiceException
     */
    DocumentoPtfrDTO buscarPorId(Integer idDocumentoPtfr) throws ServiceException;

    /**
     * Buscar paginadamente DocumentoPtfr.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<DocumentoPtfrDTO>
     * @throws ServiceException the service exception
     */
    List<DocumentoPtfrDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Enviar documento a Portafirma.
     *
     * @param idConvocatoria Id Convocatoria
     * @param idTipoTramite
     * @param idExpediente Id Expediente
     * @return byte[]
     * @throws ServiceException the service exception
     */
    byte[] enviarPortaFirmasBorrador(Integer idConvocatoria, Integer idTipoTramite,
            Integer idExpediente, Integer nmPeriodo) throws ServiceException;

    /**
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<DocumentoPtfrDTO> obtenerHistoricoNotif(Integer idExpediente) throws ServiceException;

    /**
     *
     * @param listaHistorico
     * @param index
     * @param pageSize
     * @return
     */
    List<DocumentoPtfrDTO> obtenerPaginadoHistoricoNotif(List<DocumentoPtfrDTO> listaHistorico,
            int index, int pageSize);

    /**
     * Recupera el historico para la opción excluido
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoPtfrDTO obtenerInformacionExcluido(Integer idExpediente) throws ServiceException;

    /**
     * Recupera el historico para la opción desistido
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    DocumentoPtfrDTO obtenerInformacionDesistido(Integer idExpediente) throws ServiceException;

//    /**
//     * @param envio 
//     * @return 
//    *
//    */
//    Boolean consultarRespuestaPortafirmas(Integer idConvocatoria, Integer idTramite,
//            Integer idExpediente) throws ServiceException;

    /**
     * @param idPresupuestoModif 
    *
    */
    Boolean guardarEnvioEnDcoumentoPTFR(ConvocatoriaDTO convocatoria, ExpedienteDTO expediente,
            Integer idExpediente, Integer idtramite, byte[] documentoAdjunto, Integer idExpedienteJustec, Integer idPresupuestoModif, Integer idExpedienteJusEco)
            throws ServiceException;

    public List<String> procesarEnvioANote(Integer idDocumentoPtfr, Integer idExpediente) throws ServiceException;
    
    public List<ExpedienteDoc> obtenerDocumentosAReportar(Integer idTipoTramite,
            Integer idExpediente, Integer idExpedienteJustec, Integer idExpedienteJusEco) throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @param idTramite
     * @return
     */
    Boolean consultarDocumentoPortafirmaPorTramite(Integer idExpediente, Integer idTramite) throws ServiceException;

    void procesarConsultarRespuestaAyuda() throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @param idTramite
     * @return
     * @throws ServiceException
     */
    Boolean consultarDocumentoPortafirmaPorTramites(Integer idExpediente, List<Integer> idTramite) throws ServiceException;
    
    public int actualizarEstadoDocumentoBatchPublic(Integer idDocumentoPtfr, Integer idEstadoFinal);
    
    public void actualizarEstadoInternoExpedienteBatchPublic(Expediente expediente,
            Integer idEstadoInterno, Boolean esRevAdmin) throws ServiceException;

    /**
     * VMM 06/10/26 MD-3066: cambia el estado de una modificacion presupuestaria/plazo/otra (la que
     * referencia DocumentoPtfr.idPresupuestoModif). No retrocede una modificacion ya Resuelta.
     *
     * @param idModificacion idExpedienteModifPresPlazoOtra
     * @param nmConstanteEstado constante de Constantes.EstadoExpedienteResolucion
     * @throws ServiceException
     */
    public void cambiarEstadoModificacionPresPlazoOtraPublic(Integer idModificacion,
            Integer nmConstanteEstado) throws ServiceException;
    
    /**
     * Registra en cola la resolución individual de exclusión o desistimiento y
     * actualiza el documento y el expediente en la misma transacción.
     *
     * @param convocatoria convocatoria de la resolución
     * @param idExpedienteDoc documento 303 asociado al expediente
     * @param tramite constante del trámite 412 o 411
     * @param documentoAdjunto contenido del fichero recuperado de FICA
     * @param idDocPtfrPadre cero para la primera petición o identificador de su padre
     * @param usuario usuario que realiza el envío
     * @return identificador de la petición creada o null si ya existe una activa
     * @throws ServiceException si el documento no corresponde o falla el envío
     */
    public Integer enviarResolucionIndividualDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, Integer tramite, byte[] documentoAdjunto,
            Integer idDocPtfrPadre, String usuario)
            throws ServiceException;
    
    /**
     *
     * @param convocatoria convocatoria de la resolución
     * @param idExpedienteDoc documento conjunto asociado al expediente
     * @param documentoAdjunto contenido del PDF recuperado de FICA
     * @param idDocPtfrPadre cero para la primera petición o identificador de su padre
     * @param usuario usuario que realiza el envío
     * @return identificador de la petición creada o null si ya existe una activa
     * @throws ServiceException si el documento no corresponde o falla el envío
     */
    public Integer enviarResolucionConjuntaDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, byte[] documentoAdjunto, Integer idDocPtfrPadre,
            String usuario) throws ServiceException;
    
    /**
     * Constantes de los trámites de exclusión/desistimiento con peticiones sin baja.
     * Incluye peticiones en cola, firmadas o rechazadas; no exige fecha de envío.
     * Una lista con trámites individuales y conjuntos identifica datos incompatibles.
     *
     * @param idConvocatoria convocatoria consultada
     * @return constantes 411, 412 y/o 450 presentes, sin duplicados
     * @throws ServiceException si falta la convocatoria o falla la consulta
     */
    List<Integer> obtenerTramitesResolucionEnviados(Integer idConvocatoria)
            throws ServiceException;
    
    /**
     * Comrpueba si los destinatarios están dados de alta en NOTE
     * @param idDocumentoPtfr
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    public List<String> comprobarAbonados(Integer idDocumentoPtfr, Integer idExpediente)
            throws ServiceException;
    
}
