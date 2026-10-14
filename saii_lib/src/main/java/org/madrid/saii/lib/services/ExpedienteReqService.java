package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteReq
 *
 * @author everis
 */
public interface ExpedienteReqService
        extends BaseService<ExpedienteReq, Integer, ExpedienteReqDAO> {

    /**
     * Buscar paginadamente ExpedienteReq.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteReqDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteReqDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar ExpedienteReq de un Expediente.
     *
     * @param idExpediente the id
     * @return List<ExpedienteReqDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteReqDTO> buscarPorExpediente(final Integer idExpediente) throws ServiceException;

    /**
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    ExpedienteReqDTO buscarId(final Integer id) throws ServiceException;

    /**
     *
     * @param expedienteReqDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteReqDTO guardar(ExpedienteReqDTO expedienteReqDTO) throws ServiceException;

    /**
     * 
     * @param expedienteReqDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteReqDTO guardarConHistorico(ExpedienteReqDTO expedienteReqDTO) throws ServiceException;
    
}
