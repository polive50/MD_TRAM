package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoDocDAO;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface ExpedienteContratoDocService extends BaseService<ExpedienteContratoDoc, Integer, ExpedienteContratoDocDAO> {
    
    ExpedienteContratoDocDTO guardarExpedienteContratoDoc(ExpedienteContratoDocDTO expedienteContratoDocDTO) throws ServiceException;

    ExpedienteContratoDocDTO actualizarExpedienteContratoDoc(
            ExpedienteContratoDocDTO expContratoDocDTO) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteContratoDoc.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteContratoDocDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteContratoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    void borradoLogico(Integer idExpedienteContratoDoc) throws ServiceException;

   
}
