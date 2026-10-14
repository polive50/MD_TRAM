package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.ExpedienteReqHistDAO;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ExpedienteReqHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteReqHist
 *
 * @author everis
 */
public interface ExpedienteReqHistService
        extends BaseService<ExpedienteReqHist, Integer, ExpedienteReqHistDAO> {

    /**
     * Buscar paginadamente ExpedienteReqHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteReqHistDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteReqHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar ExpedienteReqHist de un Expediente.
     *
     * @param idExpediente the id
     * @return List<ExpedienteReqDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteReqHistDTO> buscarPorExpediente(final Integer idExpediente) throws ServiceException;

    /**
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    ExpedienteReqHistDTO buscarId(final Integer id) throws ServiceException;

    /**
     *
     * @param expedienteReqHistDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteReqHistDTO guardar(ExpedienteReqHistDTO expedienteReqDTO) throws ServiceException;

}
