package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteAnualidadDAO;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteAnualidadService
 *
 * @author Quental
 */
public interface ExpedienteAnualidadService
        extends BaseService<ExpedienteAnualidad, Integer, ExpedienteAnualidadDAO> {

    /**
     * Buscar paginadamente ExpedienteAnualidad.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteAnualidadDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteAnualidadDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteAnualidad.
     *
     * @param ExpedienteAnualidadDTO the ExpedienteAnualidad DTO
     * @return ExpedienteAnualidadDTO
     * @throws ServiceException the service exception
     */
    ExpedienteAnualidadDTO guardar(ExpedienteAnualidadDTO expedienteAnualidadDTO) throws ServiceException;

    /**
     * Método que busca expedientes por id de expediente 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteAnualidadDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     *   Método para buscar ExpedienteAnualidadDTO por el id de expediente odenando por idExpAnualidad
     *   
     *   @param idExpediente id del expediente
     */
    List<ExpedienteAnualidadDTO> consultarPlazosExpAnualidadPorIdExp(Integer idExpediente)
            throws ServiceException;
}
