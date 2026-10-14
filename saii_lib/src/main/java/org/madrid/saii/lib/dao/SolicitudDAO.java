package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SolicitudFiltrosDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio Solicitud
 *
 * @author everis
 */
public interface SolicitudDAO extends BaseDAO<Solicitud, Integer> {

    /**
     * Realiza un filtrado de solicitudes
     *
     * @param solicitudFiltros
     * @return List<Solicitud>
     */
    List<Solicitud> filtrar(SolicitudFiltrosDTO solicitudFiltros);

    /**
     * Devuelve la solicitud del interesado en estado iniciada para la
     * convocatoria
     *
     * @param solicitudDTO
     * @return List<Solicitud>
     * @ 
     */
    List<Solicitud> buscarSolicitudIniciada(SolicitudDTO solicitudDTO) ;

    /**
     * Busca soliditud por idExpediente
     *
     * @param idExpediente
     * @return List<Solicitud>
     */
    List<Solicitud> buscarPorExpediente(Integer idExpediente);

    List<Solicitud> buscarSolicitudOrganismoIniciada(SolicitudDTO solicitudDTO) ;
}
