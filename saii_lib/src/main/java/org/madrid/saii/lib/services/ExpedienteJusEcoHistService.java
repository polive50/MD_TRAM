package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusEcoHist
 *
 * @author quental
 */
public interface ExpedienteJusEcoHistService
        extends BaseService<ExpedienteJusEcoHist, Integer, ExpedienteJusEcoHistDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusEcoHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusEcoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJusEcoHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

}
