package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.TramiteSolicitanteExpedienteDAO;
import org.madrid.saii.lib.domain.TramiteSolicitanteExpediente;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.TramiteSolicitanteExpedienteDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface TramiteSolicitanteExpedienteService extends BaseService<TramiteSolicitanteExpediente, Integer, TramiteSolicitanteExpedienteDAO>{

    public void guardarTramiteSolicitanteExpediente(Integer tramite, ExpedienteDTO expediente, String dniUserLogin, String cifEntidadLogin) throws ServiceException;

    /**
     * Buscar paginadamente TramiteSolicitanteExpediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<TramiteSolicitanteExpedienteDTO>
     * @throws ServiceException the service exception
     */
    List<TramiteSolicitanteExpedienteDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException;
}
