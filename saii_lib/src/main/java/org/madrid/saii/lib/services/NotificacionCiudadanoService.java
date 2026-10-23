package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio NotificacionCiudadano
 *
 * @author everis
 */
public interface NotificacionCiudadanoService
        extends BaseService<NotificacionCiudadano, Integer, NotificacionCiudadanoDAO> {

    /**
     * Buscar NotificacionCiudadano por id.
     *
     * @param id the id
     * @return NotificacionCiudadanoDTO
     * @throws ServiceException the service exception
     */
    NotificacionCiudadanoDTO buscarId(final Integer id) throws ServiceException;

    /**
     * Buscar las NotificacionCiudadano de un Expediente.
     *
     * @param idExpediente the id
     * @return List<NotificacionCiudadanoDTO>
     * @throws ServiceException the service exception
     */
    List<NotificacionCiudadanoDTO> buscarPorExpediente(final Integer idExpediente)
            throws ServiceException;

    /**
     * Guardar NotificacionCiudadano.
     *
     * @param notificacionCiudadanoDTO the dto
     * @return NotificacionCiudadanoDTO
     * @throws ServiceException the service exception
     */
    NotificacionCiudadanoDTO guardar(NotificacionCiudadanoDTO notificacionCiudadanoDTO)
            throws ServiceException;

    /**
     * Busca la notificación "en curso" de un expediente y asunto, es decir,
     * la que todavía no ha sido presentada (no tiene CD_EREG), junto con sus
     * documentos adicionales ya enlazados (SAII_EXPE_NOTIF_DOC). Si no
     * existe ninguna, devuelve un DTO vacío (idNotificacion null).
     *
     * @param idExpediente the id expediente
     * @param idAsunto the id asunto
     * @return NotificacionCiudadanoDTO
     * @throws ServiceException the service exception
     */
    NotificacionCiudadanoDTO buscarEnCurso(Integer idExpediente, Integer idAsunto)
            throws ServiceException;

    /**
     * Busca la entidad NotificacionCiudadano "en curso" de un expediente y
     * asunto (no presentada todavía: sin CD_EREG). Devuelve null si no
     * existe.
     *
     * @param idExpediente the id expediente
     * @param idAsunto the id asunto
     * @return NotificacionCiudadano o null
     * @throws ServiceException the service exception
     */
    NotificacionCiudadano buscarEnCursoEntidad(Integer idExpediente, Integer idAsunto)
            throws ServiceException;
    
    /** Recupera una página de comunicaciones sin cargar sus documentos adjuntos. */
    public List<NotificacionCiudadanoDTO> buscarComunicaciones(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException;

    /** Obtiene los valores del catálogo Tipos de notificaciones del ciudadano. */
    public List<CatalogosValoresDTO> obtenerTiposComunicacion()
            throws ServiceException;

    /** Registra una comunicación resuelta y sus documentos en una transacción. */
    public NotificacionCiudadanoDTO registrarComunicacion(NotificacionCiudadanoDTO comunicacion)
            throws ServiceException;

    /** Actualiza únicamente el estado de revisión de una comunicación del expediente. */
    public CatalogosValoresDTO actualizarRevision(Integer idNotificacion, Integer idExpediente, boolean revisada)
            throws ServiceException;

    /** Recupera los adjuntos activos de una comunicación del expediente seleccionado. */
    public List<ExpedienteDocDTO> buscarDocumentosComunicacion(
            Integer idNotificacion, Integer idExpediente) throws ServiceException;

    /**
     * Busca las notificaciones del ciudadano ya presentadas (con CD_EREG
     * informado) de un expediente, junto con sus documentos adicionales
     * enlazados (SAII_EXPE_NOTIF_DOC), para su consulta en el apartado
     * "Aportación de documentos y comunicaciones".
     *
     * @param idExpediente the id expediente
     * @return List<NotificacionCiudadanoDTO>
     * @throws ServiceException the service exception
     */
    List<NotificacionCiudadanoDTO> buscarPresentadasPorExpediente(Integer idExpediente)
            throws ServiceException;

}
