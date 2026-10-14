package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.domain.ExpedienteJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusTecHist
 *
 * @author nttdata
 */
public interface ExpedienteJusTecHistService
        extends BaseService<ExpedienteJusTecHist, Integer, ExpedienteJusTecHistDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusTecHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusTecDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJusTecHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

}
