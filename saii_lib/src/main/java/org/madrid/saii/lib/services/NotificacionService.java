package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.NotificacionDAO;
import org.madrid.saii.lib.dto.NotificacionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Notificacion
 *
 * @author nttdata
 */
public interface NotificacionService
extends BaseService<NotificacionDTO, Integer, NotificacionDAO> {

    /**
     * Método para obtener la cantidad de notificaciones como
     * coordinador/evaluador
     *
     * @param identificador de numero de identidad de usuario logueado
     * @return the Integer
     * @throws ServiceException the service exception
     */
    Integer obtenerTotalNotificaciones(String identificador) throws ServiceException;

    /**
     * Método para obtener la lista de notificaciones como coordinador o
     * evaluador
     *
     * @param itCoorEval para identificar si es coordinador o evaluador
     * @param identificador de numero de identidad de usuario logueado
     * @return List<ExpedienteEvalDTO>
     * @throws ServiceException
     */
    List<NotificacionDTO> obtenerNotificacionCoorEval(String itCoorEval, String identificador)
            throws ServiceException;

    /**
     * Método para obtener la lista de notificaciones del coordinador, sin
     * left join a InformeEvalDoc ya que el coordinador de momento no tiene
     * informes asignados
     *
     * @param identificador de numero de identidad de usuario logueado
     * @return List<NotificacionDTO>
     * @throws ServiceException
     */
    List<NotificacionDTO> obtenerNotificacionCoordinador(String identificador)
            throws ServiceException;

    List<NotificacionDTO> obtenerNotificacionEvalJustec(String identificador)
            throws ServiceException;
}
