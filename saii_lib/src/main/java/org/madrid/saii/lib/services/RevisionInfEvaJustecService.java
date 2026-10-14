package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.RevisionInfEvaJustecDAO;
import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio RevisionInfEvaJustecService
 *
 * @author quental
 */
public interface RevisionInfEvaJustecService
        extends BaseService<RevisionInfEvaJustec, Integer, RevisionInfEvaJustecDAO> {

    /**
     * Buscar paginadamente RevisionInfEvaJustec.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<RevisionInfEvaJustecDTO>
     * @throws ServiceException the service exception
     */
    List<RevisionInfEvaJustecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a RevisionInfEvaJustec.
     *
     * @param RevisionInfEvaJustecDTO the RevisionInfEvaJustec DTO
     * @return RevisionInfEvaJustecDTO
     * @throws ServiceException the service exception
     */
    RevisionInfEvaJustecDTO guardar(RevisionInfEvaJustecDTO expedienteJusTecDTO) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<RevisionInfEvaJustecDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

}
