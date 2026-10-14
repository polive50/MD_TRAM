package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.dto.NotificacionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio Notificacion
 *
 * @author nttdata
 */
public interface NotificacionDAO extends BaseDAO<NotificacionDTO, Integer> {

    /**
     * Método para obtener la lista de notificaciones como coordinador o
     * evaluador
     *
     * @param itCoorEval para identificar si es coordinador o evaluador
     * @param identificador de numero de identidad de usuario logueado
     * @return List<ExpedienteEvalDTO>
     * @ 
     */
    List<NotificacionDTO> obtenerNotificacionCoorEval(String itCoorEval, String identificador) ;

    /**
     * Método para obtener la lista de notificaciones del coordinador.
     * A diferencia de obtenerNotificacionCoorEval, no hace left join con
     * InformeEvalDoc ya que el coordinador de momento no tiene informes
     * asignados ni ninguna accion que hacer sobre ellos.
     *
     * @param identificador de numero de identidad de usuario logueado
     * @return List<NotificacionDTO>
     */
    List<NotificacionDTO> obtenerNotificacionCoordinador(String identificador);

    /**
     * Método para obtener la lista de notificaciones como 
     * evaluador externo
     *
     * @param itEval para identificar  evaluador
     * @param identificador de numero de identidad de usuario logueado
     * @return List<ExpedienteEvalJustecDTO>
     * @ 
     */
    List<NotificacionDTO> obtenerNotificacionEvalJustec(String identificador) ;

}
