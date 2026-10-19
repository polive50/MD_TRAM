package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDocDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio NotificacionCiudadanoDoc
 *
 * @author everis
 */
public interface NotificacionCiudadanoDocService
        extends BaseService<NotificacionCiudadanoDoc, Integer, NotificacionCiudadanoDocDAO> {

    /**
     * Buscar las NotificacionCiudadanoDoc de una NotificacionCiudadano.
     *
     * @param idNotificacion the id
     * @return List<NotificacionCiudadanoDocDTO>
     * @throws ServiceException the service exception
     */
    List<NotificacionCiudadanoDocDTO> buscarPorNotificacion(final Integer idNotificacion)
            throws ServiceException;

    /**
     * Guardar NotificacionCiudadanoDoc.
     *
     * @param notificacionCiudadanoDocDTO the dto
     * @return NotificacionCiudadanoDocDTO
     * @throws ServiceException the service exception
     */
    NotificacionCiudadanoDocDTO guardar(NotificacionCiudadanoDocDTO notificacionCiudadanoDocDTO)
            throws ServiceException;

}
