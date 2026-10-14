package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModfPresupPlazosOtrosDocDAO;
import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface ExpedienteModfPresupPlazosOtrosDocService extends BaseService<ExpedienteModfPresupPlazosOtrosDoc, Integer, ExpedienteModfPresupPlazosOtrosDocDAO> {
    
    ExpedienteModfPresupPlazosOtrosDocDTO guardarExpedienteModfPresupPlazosOtrosDoc(ExpedienteModfPresupPlazosOtrosDocDTO expedienteModfPresupPlazosOtrosDocDTO) throws ServiceException;

    ExpedienteModfPresupPlazosOtrosDocDTO actualizarExpedienteModfPresupPlazosOtrosDoc(
            ExpedienteModfPresupPlazosOtrosDocDTO expedienteModfPresupPlazosOtrosDocDTO) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteModfPresupPlazosOtrosDocDTO.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModfPresupPlazosOtrosDocDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModfPresupPlazosOtrosDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    List<ExpedienteModfPresupPlazosOtrosDocDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    List<ExpedienteModfPresupPlazosOtrosDocDTO> buscarPorExpedienteModfPresupPlazosOtros(
            Integer idExpedienteModfPresupPlazosOtrosDoc) throws ServiceException;

   
}
