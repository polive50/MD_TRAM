package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;

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

}
