package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteAnualidadHistDAO;
import org.madrid.saii.lib.domain.ExpedienteAnualidadHist;
import org.madrid.saii.lib.dto.ExpedienteAnualidadHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteAnualidadHist
 *
 * @author Quental
 */
public interface ExpedienteAnualidadHistService
        extends BaseService<ExpedienteAnualidadHist, Integer, ExpedienteAnualidadHistDAO> {

    /**
     * Buscar paginadamente ExpedienteAnualidadHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteAnualidadDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteAnualidadHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

}
