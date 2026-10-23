package org.madrid.saii.lib.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.ResponseDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Expediente
 *
 * @author everis
 */
public interface ExpedienteService extends BaseService<Expediente, Integer, ExpedienteDAO> {

    /**
     * Método para obtener datos del expediente e información adicional.
     *
     * @param idExpediente Identificador de expediente
     * @return ExpedienteDTO Datos del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteDTO buscarId(Integer idExpediente) throws ServiceException;

    /**
     * Método para obtener datos del expediente.
     *
     * @param idExpediente Identificador de expediente
     * @return ExpedienteDTO Datos del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteDTO buscarIdSimple(Integer idExpediente, Boolean datosCompletos)
            throws ServiceException;

    /**
     * Método para obtener datos de la tabla Expediente y catalogosValores
     * Asociados.
     *
     * El resto de datos como otros DTO y Listado no los recuperara
     *
     * @param idExpediente Identificador de expediente
     * @return ExpedienteDTO Datos del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteDTO buscarIdMinimo(Integer idExpediente) throws ServiceException;

    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Genera un expediente.
     *
     * @param solicitudDTO the solicitud DTO
     * @param interesadoDTO the interesado DTO
     * @param convocatoriaActDTO the convocatoriaActDTO
     * @param generaExpediente the generaExpediente
     * @param idExpedientePadre the idExpedientePadre
     * @return ExpedienteDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDTO generar(SolicitudDTO solicitudDTO, InteresadoDTO interesadoDTO,
            ConvocatoriaActDTO convocatoriaActDTO, Boolean generaExpediente, Integer idExpedientePadre)
            throws ServiceException;

    /**
     * Guarda un/a expediente.
     *
     * @param expedienteDTO the expediente DTO
     * @return expedienteDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDTO guardar(ExpedienteDTO expedienteDTO) throws ServiceException;

    /**
     * Borrado logico Expediente
     *
     * @param idExpediente
     * @throws ServiceException
     */
    void borradoLogico(final Integer idExpediente) throws ServiceException;

    /**
     * Alta logica
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    void altaLogica(final Integer idExpediente) throws ServiceException;

    /**
     * Busca un expediente segun sus filtros. (descripción, acrónimo, idEstado,
     * número de expediente).
     *
     * @param expedienteFiltrosDTO
     * @return
     */
    List<ExpedienteFiltrosDTO> filtrar(ExpedienteFiltrosDTO expedienteFiltrosDTO)
            throws ServiceException;

    /**
     * Buscar por solicitud e interesado.
     *
     * @param idSolicitud the id solicitud
     * @param idInteresado the id interesado
     * @param personaJur the persona jur
     * @return ExpedienteDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDTO buscarSolicitudInteresado(Integer idSolicitud, Integer idInteresado)
            throws ServiceException;

    /**
     * Cambia estado del expediente.
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @param dni the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO cambiarEstado(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;

    /**
     * Cambia estado del tramite de evaluacion del expediente.
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @param dni the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO cambiarEstadoRevEval(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;
    
    /**
     * Cambia estado del tramite de contrato del expediente.
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO cambiarEstadoRevContrato(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;

    /**
     * Guarda los datos del expediente
     *
     * @param expedienteDTO the expediente DTO
     * @return expedienteDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDTO guardarDatosExpediente(ExpedienteDTO expedienteDTO) throws ServiceException;

    /**
     * Obtiene los estados de las expedientes
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     */
    List<CatalogosValoresDTO> obtenerListaEstadosExpediente() throws ServiceException;

    /**
     * Obtiene los estados de las expedientes del beneficiario
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     */
    List<CatalogosValoresDTO> obtenerListaEstadosExpedienteBenef() throws ServiceException;

    /**
     * Busca los expedientes participantes de una solicitud.
     *
     * @param idSolicitud
     * @param idActor
     * @return
     */
    List<ExpedienteDTO> buscarParticipantesPorSolicitud(Integer idSolicitud, Integer idActor)
            throws ServiceException;

    /**
     * Devuelve todos los expedientes de la solicitud
     *
     * @param idSolicitud
     * @return List<ExpedienteDTO>
     */
    List<ExpedienteDTO> obtenerExpedientesPorSolicitud(Integer idSolicitud) throws ServiceException;

    /**
     * Comprueba si el interesado asociado al expediente es una entidad
     * (participante) y lo cambia al representante del token
     *
     * @param expedienteDTO expediente recuperado
     * @param dniInteresado dni del token
     * @return expedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO comprobarInteresadoExpediente(ExpedienteDTO expedienteDTO, String dniInteresado)
            throws ServiceException;

    /**
     * Método para obtener los documentos pendientes de subsanacion
     *
     * @param idExpediente identificador de expediente
     * @return Lista de documentos
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<ExpedienteDocDTO> obtenerDocumentosSubsanar(Integer idExpediente) throws ServiceException;

    /**
     * Método para obtener los requisitos a subsanar para la notificación
     *
     * @param idExpediente identificador de expediente
     * @return Lista de documentos
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<ExpedienteReqDTO> obtenerRequisitosNotificaciones(Integer idExpediente)
            throws ServiceException;

    /**
     * Método para obtener los requisitos pendientes de subsanación
     *
     * @param idExpediente identificador de expediente
     * @return List<ExpedienteReqDTO>
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<ExpedienteReqDTO> obtenerRequisitosSubsanar(Integer idExpediente) throws ServiceException;

    /**
     * Método para obtener los documentos pendientes de subsanacion de
     * requisitos
     *
     * @param lstExpedienteReqDTO lista de requisitos
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<ExpedienteDocDTO> obtenerDocumentosRequisitos(List<ExpedienteReqDTO> lstExpedienteReqDTO)
            throws ServiceException;

    /**
     * Método para actualizar estado de expediente luego de subsanar
     *
     * @param lstExpedienteDocDTO
     * @param esAdministrativa
     * @param cdNumIden
     * @param dniUserLogin 
     * @return RegistroEregDTO con los datos del listado
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    RegistroEregDTO finalizarSubsanacion(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            Boolean esAdministrativa, String cdNumIden, String dniUserLogin,
            String cdNumIdenFuncional) throws ServiceException;

    /**
     * Método para actualizar el estado del expediente seleccionado
     *
     * @param idExpediente Identificador de expediente
     * @return
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteDTO actualizarEstadoExpedienteRevisionDocumento(Integer idExpediente)
            throws ServiceException;

    /**
     * Listar Expediente por numero de identidad
     *
     * @param String numIdentidad
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteDTO> obtenerExpedienteSubsanacion(String numIdentidad) throws ServiceException;

    /**
     * Método para crear un expediente
     *
     * @param expedienteDTO Datos del expediente a crear
     * @return ExpedienteDTO
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteDTO crear(ExpedienteDTO expedienteDTO) throws ServiceException;

    /**
     * Comprueba los participantes añadidos a la solicitud cumplen el mín/máx
     * permitidos
     *
     * @param idExpediente
     * @return ResponseDTO
     * @throws ServiceException
     */
    ResponseDTO comprobarParticipantes(Integer idExpediente) throws ServiceException;

    /**
     * Devuelve la lista de documentos del expediente
     *
     * @param idExpediente
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> obtenerDocumentos(Integer idExpediente) throws ServiceException;

    /**
     * Método para actualizar el estado interno revisión OK de requisito
     *
     * @param idExpediente
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoRevisionOkRequisito(Integer idExpediente)
            throws ServiceException;

    /**
     * Método para actualizar el estado interno pendiente de requisito. Este
     * método se llamará al pulsar el botón Guardar
     *
     * @param idExpediente
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoRequisito(Integer idExpediente) throws ServiceException;

    /**
     * Método para actualizar el estado interno revisión OK de documento
     *
     * @param idExpediente
     * @param dtoAux 
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoRevisionOkDocumento(Integer idExpediente, ExpedienteDTO dtoAux)
            throws ServiceException;
    
    /**
     * Método para actualizar el estado interno revisión del expediente de Contratos
     *
     * @param idExpediente
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoRevisionExpContratosOK(Integer idExpediente)
            throws ServiceException;
    
    /**
     * Método para actualizar el estado interno revisión OK de Contratos
     *
     * @param idExpediente
     * @param estadoRevision
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoRevisionOkContratos(Integer idExpediente, String estadoRevision)
            throws ServiceException;

    /**
     * Método para obtener la fecha limite de subsanación de documentos
     *
     * @param idExpediente
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    String obtenerFechaLimiteSubsanacionAdmin(Integer idExpediente, String cdNumIden)
            throws ServiceException;

    /**
     * Método para obtener la fecha limite de subsanación de requisitos
     *
     * @param idExpediente
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    String obtenerFechaLimiteSubsanacionRequi(Integer idExpediente, String cdNumIden)
            throws ServiceException;

    /**
     * método para aprobar evaluación
     *
     * @param idExpedienteEval
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO aprobarEvaluacion(Integer idExpediente) throws ServiceException;

    /**
     * método para rechazar evaluación
     *
     * @param idExpedienteEval
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO rechazarEvaluacion(Integer idExpediente) throws ServiceException;

    /**
     *
     * @param expedienteFiltro
     * @return
     * @throws ServiceException
     */
    List<ExpedienteFiltrosDTO> obtenerExpedienteCoorEval(ExpedienteFiltrosDTO expedienteFiltro)
            throws ServiceException;

    /**
     * Método para actualizar estado de expediente luego de guardar los
     * documentos de alegaciones
     *
     * @param exepdienteDocDTO registro de la alegacion
     * @return RegistroEregDTO con los datos del listado
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    RegistroEregDTO guardarAlegacion(ExpedienteDocDTO expedienteDocDto, String cdNumIden,
            String dniLogin, String cdNumIdenFuncional) throws ServiceException;

    /**
     * Método para presentar el trámite de Notificación del ciudadano.
     *
     * @param idExpediente id del expediente
     * @param idAsunto id del valor de catálogo del asunto (idCatalogo=64)
     * @param tlExpone texto del campo "Expone"
     * @param tlSolicita texto del campo "Solicita"
     * @param cdNumIden cd num iden
     * @param dniLogin dni login
     * @param cdNumIdenFuncional cd num iden funcional
     * @return RegistroEregDTO con los datos del registro EREG
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    RegistroEregDTO presentarDesistimientoExpreso(Integer idExpediente, Integer idAsunto,
            String tlExpone, String tlSolicita, String cdNumIden, String dniLogin,
            String cdNumIdenFuncional) throws ServiceException;

    /**
     * Método para buscar la notificación del ciudadano "en curso" (dada de
     * alta pero todavía no presentada) de un expediente y asunto, junto con
     * sus documentos adicionales ya enlazados. Si no existe ninguna, se
     * devuelve un DTO vacío.
     *
     * @param idExpediente Identificador del expediente
     * @param idAsunto Identificador del valor de catálogo del asunto (idCatalogo=64)
     * @return NotificacionCiudadanoDTO
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    NotificacionCiudadanoDTO buscarNotificacionCiudadanoEnCurso(Integer idExpediente,
            Integer idAsunto) throws ServiceException;

    /**
     * Método para buscar las notificaciones del ciudadano ya presentadas de
     * un expediente, junto con sus documentos adicionales ya enlazados, para
     * su consulta en el apartado "Aportación de documentos y
     * comunicaciones".
     *
     * @param idExpediente Identificador del expediente
     * @return List<NotificacionCiudadanoDTO>
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<NotificacionCiudadanoDTO> buscarNotificacionesCiudadanoPresentadas(Integer idExpediente)
            throws ServiceException;

    /**
     * Método para comprobar que exista el tipo de documento "Resolución
     * concesión" y habilitar los botones de resolucion definitiva
     *
     * @param idExpediente
     * @return
     */
    ExpedienteDTO comprobarResolucionDefinitiva(Integer idExpediente, ConvocatoriaDTO convocatoriaDTO) throws ServiceException;

    /**
     * método para Batch que permite actualizar al Expediente el campo
     * itAlegaciones a "N" si la fecha de fin de alegaciones es menor a la fecha
     * actual
     *
     * @throws ServiceException
     */
    void actualizarExpedienteConFcAlegacionNoVencidaBatch() throws ServiceException;

    /**
     * método para batch que realizará un update de SAII_EXPEDIENTE. ID_ESTADO a
     * ‘PROPUESTA_DE_RESOLUCION”. Para todos los expedientes IT_ALEGACIONES a
     * ‘N’. y estado expediente es Aprobado provisional o Denegado provisional.
     * @return 
     *
     */
    void actualizarEstadoExpedienteAlegacionBatch() throws ServiceException;
    
    
    List<Integer> actualizarEstadoExpedienteContratoBatch() throws ServiceException;

    /**
     * Metodo para enviar note provisional
     */
    ExpedienteDTO enviarNoteProvisional(Integer idExpediente, Integer idSolicitud)
            throws ServiceException;

    /**
     * Metodo para calcular fecha limite de subsanación
     *
     * @param diasSubsanacion
     * @param docuPtfr
     * @return
     * @throws ServiceException
     */
    LocalDateTime calcularFechaLimiteSubsanacion(Integer diasSubsanacion, DocumentoPtfrDTO docuPtfr, Date fechaLectura)
            throws ServiceException;

    /**
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    Map<String, Object> obtenerMapaDatosExcel(Integer idExpediente) throws ServiceException;

    /**
     *
     * @param lstIdExpediente
     * @return
     * @throws ServiceException
     */
    int obtenerCantidadColumnasReservadaActuacion(List<Integer> lstIdExpediente)
            throws ServiceException;

    /**
     *
     * @param lstIdExpediente
     * @return
     * @throws ServiceException
     */
    Map<String, String> obtenerMapaDatosComunExcel(List<Integer> lstIdExpediente)
            throws ServiceException;

    /**
     *
     * @param lstIdExpediente
     * @return
     * @throws ServiceException
     */
    Map<String, String> obtenerMapaDatosActuacionFinExcel(List<Integer> lstIdExpediente)
            throws ServiceException;

    /**
     * Método para obtener los requisitos subsanados.
     *
     * @param idExpediente Identificador de expediente
     * @return List<ExpedienteDocDTO> Datos del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    List<ExpedienteDocDTO> obtenerRequisitoSubsanado(Integer idExpediente) throws ServiceException;

    /**
     * Método para obtener el detalle de la notificacion respecto del documento
     * de portafirmas
     *
     * @param idDocumentoPtf
     * @return DocumentoPtfrDTO
     * @throws ServiceException
     */
    DocumentoPtfrDTO obtenerInformacionNotificacion(Integer idDocumentoPtfr)
            throws ServiceException;

    /**
     * Recupera los documentos a subsanar del expediente
     *
     * @param idExpediente
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> obtenerDocumentoPendienteSubsanar(Integer idExpediente)
            throws ServiceException;
    
    /**
     * Recupera los documentos de los requisitos asubsanar del expediente
     * 
     * @param idExpediente
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> obtenerDocumentoRequisitoPendienteSubsanar(Integer idExpediente)
            throws ServiceException;

    /**
     * Recupera los documentos subsanados del expediente
     *
     * @param idExpediente
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> obtenerDocumentoSubsanado(Integer idExpediente) throws ServiceException;

    /**
     *
     * @param idSolicitud
     * @return
     * @throws ServiceException
     */
    List<ExpedienteDTO> obtenerExpedienteEstructurado(Integer idSolicitud, String todos) throws ServiceException;

    /**
     * Comprueba los coordinadores añadidos a la solicitud cumplen el mín/máx
     * permitidos
     *
     * @param idExpediente
     * @return ResponseDTO
     * @throws ServiceException
     */
    ResponseDTO comprobarCoordinadores(Integer idExpediente) throws ServiceException;

    /**
     * Busca los expedientes coordinadores de una solicitud.
     *
     * @param idSolicitud
     * @param idActor
     * @return
     */
    List<ExpedienteDTO> buscarCoordinadoresPorSolicitud(Integer idSolicitud, Integer idActor)
            throws ServiceException;

    /**
     * Permite obtener expedientes por solicitud, actor y tipo
     *
     * @param idSolicitud
     * @param idActor
     * @param itTipo
     * @param idExpedientePadre
     * @return
     */
    List<ExpedienteDTO> buscarExpedientePorSolicitudActor(Integer idSolicitud, Integer idActor,
            String itTipo, Integer idExpedientePadre) throws ServiceException;

    /**
     * Metodo para actualizar fechas de contrato a los expedientes
     *
     * @param idConvocatoria
     * @param fcIniContrato
     * @param fcFinContrato
     */
    void actualizarFechaContratoPorConvocatoria(Integer idConvocatoria, Date fcIniContrato,
            Date fcFinContrato) throws ServiceException;

    /**
     * Metodo para actualizar fechas de contrato de un expediente
     *
     * @param idExpediente
     * @param fcIniContrato
     * @param fcFinContrato
     * @return
     */
    ExpedienteDTO actualizarFechaContrato(Integer idExpediente, Date fcIniContrato,
            Date fcFinContrato) throws ServiceException;

    /**
     * Cambia estado del expediente just tec
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @param dni the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO cambiarEstadoJusTec(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;
    
    /**
     * Método para crear el registro expedienteDoc a partir del expediente y
     * bloque componente. Un documento por bloque componente
     *
     * @param expedienteDTO
     * @param bloqueComponenteDTO
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    ExpedienteDocDTO crearExpedienteDoc(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO,Integer tramite) throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @param idExpedienteJusTec
     * @return
     * @throws ServiceException
     */
    
    public List<ExpedienteDocDTO> obtenerDocJusTec(Integer idExpedienteJusTec)
            throws ServiceException ;
    
    
    /**
     * Devuelve la lista de contratos del expediente por el id tipo tramite
     *
     * @param idTipoTramite
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> obtenerContratos(Integer idTipoTramite, Integer idExpediente) throws ServiceException;

    List<ExpedienteDocDTO> obtenerExpedienteContratos(Integer idExpediente) throws ServiceException;

    ExpedienteDTO buscarIdExp(Integer idExpediente, List<ExpedienteDocDTO> listExpedienteDocDTOAux) throws ServiceException;
    
    ExpedienteDTO buscarIdExpediente(Integer idExpediente) throws ServiceException;

    List<ExpedienteDocDTO> obtenerDocPendienteSubsanarJusTec(Integer idExpedienteJustec) throws ServiceException ;

    void actualizarExpedienteJustec(ExpedienteJusTecDTO expedienteJustecDTO);
    
    public ExpedienteDocDTO crearExpedienteDocJustec(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException;

    public  List<ExpedienteDTO> buscarParticipantesPorExpedienteAgrupador(Integer idExpediente,
            Integer idActor);
    
    /**
     * Devuelve todos los expedientes 
     *
     * @param idExpediente
     * @return List<ExpedienteDTO>
     */
    List<ExpedienteDTO> obtenerExpedientesPorIdExpediente(Integer idExpediente) throws ServiceException;

    RegistroEregDTO finalizarSubsanacionJustec(List<ExpedienteDocDTO> lstExpedienteDoc,
            ExpedienteJusTecDTO expedienteJustecDTO, Boolean tramiteAdm, String cdNumIden,
            String dniLogin, String cdNumIdenFuncional) throws ServiceException;

    RegistroEregDTO finalizarSubsanacionContrato(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            Boolean esAdministrativo, String cdNumIden, String dniUsuario,
            String cdNumIdenFuncional) throws ServiceException;

    /**
     * Método que rellena los documentos del expediente
     *
     * @param List<ExpedienteDocDTO>
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> rellenarDocumentosExpediente(List<ExpedienteDocDTO> listExpedienteDocDTO)
            throws ServiceException;

    /**
     * @param listaDocs 
     * @param expedienteJusTecSeleccionado 
    *
    */
    ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoJCT(Integer idExpediente, List<ExpedienteDocDTO> listaDocs, ExpedienteJusTecDTO expedienteJusTecSeleccionado)  throws ServiceException;
    
    /**
     * @param listaDocs 
     * @param expedienteModifSeleccionado 
    *
    */
    ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoActas(Integer idExpediente, List<ExpedienteDocDTO> listaDocs, ExpedienteModifPresPlazoOtraDTO expedienteModifSeleccionado)  throws ServiceException;

    ExpedienteDTO actualizarEstadoInternoActasRevision(Integer idExpediente,
            ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO,
            Integer idEstadoNuevoRev) throws ServiceException;

    /**
    *
    */
    List<ExpedienteDTO> buscarExpedientePorExpedientePadre(Integer idSolicitud, Integer idExpedientePadre) throws ServiceException;

    public List<ExpedienteDTO> buscarExpedientesPorPadreYPorIdActor(Integer idExpediente, Integer idActor);

    List<ExpedienteDocDTO> recuperarExpediente(List<ExpedienteDocDTO> listadoDatos,
            List<ExpedienteDTO> lstExpedienteDTO);

    List<CatalogosValoresDTO> buscarPorCatalogoId(Integer idCatalogo) throws ServiceException ;

    public ExpedienteDTO actualizarEstadoInternoJCT(Expediente expediente,ExpedienteJusTecDTO expedienteJustecDTO, Integer idEstadoNuevo, Integer idEstadoNuevoRev ) throws ServiceException;

    String obtenerFechaLimiteSubsanacionJustec(List<ExpedienteDocDTO> expedienteDocDTO,
            String cdNumIden) throws ServiceException;

    /**
     * 
     * @param actual
     * @param anterior
     * @return
     */
    ExpedienteDTO revertirEstadosExpediente(Integer idSolicitud, ExpedienteHistDTO anterior, Boolean provisional);

    /**
     * Método para procesar el cambio de alegaciones
     * @param idExpediente
     * @param idConvocatoria 
     * @param activarConcederDenegar 
     * @param idEstado
     * @param alegacionrevisada
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO procesarRevisionAlegacionGestor(Integer idExpediente, Integer idConvocatoria, String alegacionRevisada, Boolean activarConcederDenegar)
            throws ServiceException;

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoJCTRevision(Expediente expediente,
            ExpedienteJusTecDTO expedienteJustecDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException;
    
    
    List<ExpedienteDTO> buscarIdSolicitud(Integer idSolicitud) throws ServiceException;

    ExpedienteDTO actualizarEstadoExpedienteContrato(Integer idExpediente, Integer idEstadoNuevo)
            throws ServiceException;

    void registrarDocuemntosRenunciaContrato(Integer idExpediente, Integer tramite) throws ServiceException;

    ExpedienteContratoDTO actualizarRevisionContratos(ExpedienteContratoDTO expContratoDTO,
            String estadoRevision) throws ServiceException;

    ExpedienteDTO actualizarFechaMaxContrato(ExpedienteDTO expDTO) throws ServiceException;
    
    ExpedienteDTO rellenarDatosExpediente(ExpedienteDTO dto) throws ServiceException;

    void generarDatosImpresos(ExpedienteDTO expedienteDTO, ImpresoDTO impreso,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException;
    
    void generarDatosImpresosEco(ExpedienteDTO expedienteDTO, ImpresoDTO impreso,
            ExpedienteJusEcoDTO expedienteJusEco) throws ServiceException;

    BloqueComponenteDTO eliminarDatos(BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException;

    void rellenarCdAsuntoExp(ExpedienteDTO dto, ImpresoDTO impresos);

    List<ExpedienteDTO> buscarExpedienteConvocatoriaInteresado(Integer idConvocatoria,
            Integer idInteresado) throws ServiceException;

    List<ExpedienteDTO> buscarExpedientesHijoSubsanar(Integer idSolicitud, Integer idActor,
            String itTipo, Integer idExpedientePadre) throws ServiceException;

    Boolean iniciarSubsanacionReq(List<ExpedienteDocDTO> lstExpedienteDocDTO, String cdNumIden)
            throws ServiceException;
    
    List<ExpedienteDTO> buscarExpedientesHijoSubsanarAdmin(Integer idSolicitud, Integer idActor,
            String itTipo, Integer idExpedientePadre) throws ServiceException;

    /**
    *
    */
    List<ExpedienteDTO> buscarExpedientePorSolicitud(Integer idSolicitud) throws ServiceException;
    
    /**
    *
    * @param idSolicitud
    * @return
    * @throws ServiceException
    */
   List<ExpedienteDTO> obtenerExpedienteEstructuradoSinActorParticipante(Integer idSolicitud, String todos, Integer idExpediente) throws ServiceException;

    ExpedienteDTO guardarDatosBloquesMultiregistro(List<BloqueDTO> listaBloquesDTO,
            List<ExpedienteDocDTO> lstExpedienteDoc) throws ServiceException;

    List<BloqueDTO> buscarMultibloquesExpediente(Integer idBloque, Integer idExpediente) throws ServiceException;

    List<ExpedienteDocDTO> inicializarDocumentosMultibloquesExpediente(Integer idBloque,
            Integer idExpediente) throws ServiceException;

    String toUnicodeEscaped(String rawFilename);

   
   /**
    * Metodo que devuelve la lista de expedientes extructurados en base a criterios de busqueda y listado de solicitudes
    * @param lstIdSolicitud
    * @param index
    * @param pageSize
    * @param orderBean
    * @param mostrarIniciadas 
    * @param filtros
    * @return
    * @throws ServiceException
    */
   List<ExpedienteDTO> obtenerExpedienteEstructurado(List<Integer> lstIdSolicitud, final int index, final int pageSize, Object orderBean, Boolean mostrarIniciadas) throws ServiceException;
   
   /**
    * Metodo que devuelve el count de expedientes extructurados en base a criterios de busqueda y listado de solicitudes
    * @param lstIdSolicitud
    * @param orderBean
    * @param filtros
    * @param mostrarIniciadas 
    * @return
    * @throws ServiceException
    */
   Integer countExpedienteEstructurado(List<Integer> lstIdSolicitud, Boolean mostrarIniciadas) throws ServiceException;

   /**
    * VMM 16/10/26 MD-3125: como obtenerExpedienteEstructurado(lstIdSolicitud, index, pageSize, orderBean,
    * mostrarIniciadas) pero devolviendo solo los expedientes (y participantes) cuyo estado esta en
    * lstEstadoExpediente (si es null o vacia no se filtra por estado)
    */
   List<ExpedienteDTO> obtenerExpedienteEstructurado(List<Integer> lstIdSolicitud, final int index, final int pageSize, Object orderBean, Boolean mostrarIniciadas, List<Integer> lstEstadoExpediente) throws ServiceException;

   /**
    * VMM 16/10/26 MD-3125: como countExpedienteEstructurado(lstIdSolicitud, mostrarIniciadas) pero contando
    * solo los expedientes cuyo estado esta en lstEstadoExpediente (si es null o vacia no se filtra por estado)
    */
   Integer countExpedienteEstructurado(List<Integer> lstIdSolicitud, Boolean mostrarIniciadas, List<Integer> lstEstadoExpediente) throws ServiceException;
   
   /**
    * Obtiene la fecha de presentacion del expediente en base a la solicitud indicada
    * @param idSolicitud
    * @param idExpediente 
    * @return
    * @throws ServiceException
    */
   Date obtenerFcPresentacionBySolicitud(Integer idSolicitud, Integer idExpediente) throws ServiceException;
   
   public ExpedienteDTO generarDocumentosExpedienteContrato(ExpedienteDTO expedienteDTO)
           throws ServiceException;
   
   /**
    * Este método actualiza el estado de la convocatoria a “Resuelta”. Pero
    * antes de realizar el cambio de estado comprueba que todos los expedientes
    * de la convocatoria estén en un estado final
    *
    * @param idConvocatoria
    * @return
    * @throws ServiceException
    */
   ConvocatoriaDTO resolver(Integer idConvocatoria, List<String> listMensajesError) throws ServiceException;
   
   /**
    * Este método comprueba la configuracion de impresos de justec, juseco, etc..
    * impresos que no sean de solicitud ni complementario para que su configuración sea correcta
    *
    * @param convocatoria
    * @param existeError
    * @param lstImpreso
    * @param listMensajesError
    * 
    * @return
    * @throws ServiceException
    */
   Boolean comprobarConfigOtrosImpresos(Convocatoria convocatoria, Boolean existeError,
          List<ImpresoDTO> lstImpreso, List<String> listMensajesError) throws ServiceException;

   /**
    * Este método crea los impresos de justec, juseco, etc..en el expediente,
    * impresos que no sean de solicitud ni complementario para que posteriormente
    * se haga la presentación de los mismos
    *
    * @param solicitudDTO
    * @param convocatoriaActDTO
    * @param expedienteDTO
    * 
    * @return
    * @throws ServiceException
    */
   void crearOtrosImpresosExpdte(SolicitudDTO solicitudDTO, ConvocatoriaActDTO convocatoriaActDTO,
          ExpedienteDTO expedienteDTO, Convocatoria convocatoria) throws ServiceException;

    /**
     * Devuelve la lista de datos por idExpediente y una lista de tipos de impresos
     *
     * @param idExpediente
     * @param lstTipoImpreso
     * @return List<DatoDTO>
     * @throws ServiceException 
     */
    List<DatoDTO> buscarPorIdExpTipoImpreso(Integer idExpediente, List<Integer> lstTipoImpreso)
            throws ServiceException;

    ExpedienteDTO conceder(SolicitudDTO solicitudDTO, Integer idExpediente) throws ServiceException;

    ExpedienteDocDTO getExpedienteDocDTO(Integer tipoDocumento, Integer idExpediente);

    Map<String, Object> revertirExpedienteCompleto(Integer idExpediente, Integer idSolicitud,
            ExpedienteHistDTO registroHistoricoEstadoAnterior) throws ServiceException;

    public ExpedienteDTO calcularTramitesFinalizados(ExpedienteDTO dto) throws ServiceException;

    /**
     * Método para actualizar el estado interno revisión OK de financiacion
     *
     * @param idEstadoFinan
     * @param dtoAux 
     * @return ExpedienteDTO
     * @throws ServiceException
     */
    ExpedienteDTO cambiarEstadoExpFinan(Integer idEstadoFinan, ExpedienteDTO dtoAux) throws ServiceException;

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoJE(Expediente expediente,
            ExpedienteJusEcoDTO expedienteJusEcoDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException;

    /**
    *
    */
    ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoJE(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocs, ExpedienteJusEcoDTO expedienteJusEcoSeleccionado)
            throws ServiceException;

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO actualizarEstadoInternoJERevision(Expediente expediente,
            ExpedienteJusEcoDTO expedienteJusEcoDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException;

    ExpedienteDocDTO crearExpedienteDocJusEco(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException;

    /**
     * Devuelve el nombre del fichero subido a FICA
     *
     * @param cdFica código de FICA
     * @return nombre del fichero
     */
    String recuperarNombreFicheroFica(Integer cdFica) throws ServiceException;

    /**
     * Este método crea el impreso deaceptacion.en el expediente
     *
     * @param solicitudDTO
     * @param convocatoriaActDTO
     * @param expedienteDTO
     * 
     * @return
     * @throws ServiceException
     */
    void crearImpresosAceptacionExpdte(SolicitudDTO solicitudDTO,
            ConvocatoriaActDTO convocatoriaActDTO, ExpedienteDTO expedienteDTO)
            throws ServiceException;
    
    /**
     * Cambia el estado de la rev de req
     * 
     * @param idExpediente
     * @param idEstadoRevAdmin
     * @return
     */
    ExpedienteDTO guardarCambioEstadoRevReq(Integer idExpediente, Integer idEstadoRevReq)
            throws ServiceException;
    
    /**
     * Cambia estado del expediente.
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @param dni the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO actualizarEstadoProvisionalExpediente(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @param idEstadoRevAdmin
     * @return
     */
    ExpedienteDTO guardarCambioEstadoRevAdmin(Integer idExpediente, Integer idEstadoRevAdmin)
            throws ServiceException;
    
    /**
     * Cambia estado del expediente.
     *
     * @param idExpediente the id expediente
     * @param idEstado the id estado
     * @param dni the id estado
     * @return
     * @throws ServiceException the service exception
     */
    ExpedienteDTO actualizarEstadoYEstadoProvisionalExpediente(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException;

    List<EntidadDTO> obtenerEntidadesConvocatoriaActivas(Integer idConvocatoria) throws ServiceException;
    
    /**
     * Actualiza el estado, estado de revisión de requisitos y estado
     * provisional
     * 
     * @param idExpediente
     * @param idEstado
     * @param dni
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO actualizarCambioEstEstRevReqEstPro(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException;
    
    /**
     * Actualiza el estado, estado revisión de adminstrativo y estado
     * provisional
     * 
     * @param idExpediente
     * @param idEstado
     * @param dni
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO actualizarCambioEstEstRevAdmEstPro(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException;

    /** Guarda únicamente la nota de evaluación; un valor nulo no modifica el expediente. */
    public void guardarNotaEvaluacion(Integer idExpediente, Integer nmNota) throws ServiceException;
    
    Map<Integer, Date> obtenerFcPresentacionPorLote(List<Integer> idsExpedienteLote);

    Map<Integer, Object[]> obtenerEstadosExpedienteSolicitantePorLote(List<Integer> idsSolicitud);

    /**
     * Método para cambiar el estado del beneficiario
     * @param idExpediente
     * @param idEstado
     * @return
     * @throws ServiceException
     */
    public ExpedienteDTO actualizarEstadoBeneficiario(Integer idExpediente, Integer idEstado)
            throws ServiceException;

}
