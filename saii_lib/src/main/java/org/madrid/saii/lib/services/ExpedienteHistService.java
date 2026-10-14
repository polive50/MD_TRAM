package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.domain.ExpedienteHist;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteHist
 *
 * @author everis
 */
public interface ExpedienteHistService
        extends BaseService<ExpedienteHist, Integer, ExpedienteHistDAO> {

    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Recupera el historico para la resolucion provisional
     *
     * @param idExpediente
     * @return ExpedienteHistDTO
     * @throws ServiceException
     */
    ExpedienteHistDTO obtenerResolucionProvisional(Integer idExpediente) throws ServiceException;

    /**
     * Recupera el historico para la resolucion definitiva
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    ExpedienteHistDTO obtenerResolucionDefinitiva(Integer idExpediente) throws ServiceException;

//    Boolean obtenerEstadoPendienteDeResolver(Integer idExpediente) throws ServiceException;

}
