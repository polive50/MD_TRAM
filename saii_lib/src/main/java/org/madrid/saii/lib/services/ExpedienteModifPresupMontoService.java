package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifPresupMontoDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPresupMonto;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteModifPresupMonto
 *
 * @author quental
 */
public interface ExpedienteModifPresupMontoService
        extends BaseService<ExpedienteModifPresupMonto, Integer, ExpedienteModifPresupMontoDAO> {

    /**
     * Buscar todos los registros de ExpedienteModifPresupMonto.
     *
     * @return List<ExpedienteModifPresupMontoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModifPresupMontoDTO> obtener() throws ServiceException;

    /**
     * Buscar por idExpedienteModifPresupMonto.
     *
     * @param idExpedienteModifPresupMonto the id ExpedienteModifPresupMonto
     * @return ExpedienteModifPresupMontoDTO
     * @throws ServiceException the service exception
     */
    ExpedienteModifPresupMontoDTO buscarId(Integer idExpedienteModifPresupMonto) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteModifPresupMonto.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModifPresupMontoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModifPresupMontoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda insertando o actualizando un ExpedienteModifPresupMonto.
     *
     * @param ExpedienteModifPresupMontoDTO the ExpedienteModifPresupMonto DTO
     * @return ExpedienteModifPresupMontoDTO
     * @throws ServiceException the service exception
     */
    ExpedienteModifPresupMontoDTO guardar(ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException;

    /**
     * Borrado logico ExpedienteModifPresupMonto
     *
     * @param ExpedienteModifPresupMontoDTO
     * @throws ServiceException
     */
    void borradoLogico(final ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException;
    
    /**
     * Actualiza un ExpedienteModifPresupMonto.
     *
     * @param ExpedienteModifPresupMontoDTO the ExpedienteModifPresupMonto DTO
     * @return ExpedienteModifPresupMontoDTO
     * @throws ServiceException the service exception
     */
    void actualizar(ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException;

    List<ExpedienteModifPresupMontoDTO> consultarModifPresupPorIdModif(
            Integer IdExpedienteModifPresPlazoOtra) throws ServiceException;

    void guardarModifPresupConcepto(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
            Integer idExpedienteModPrePlaOtra) throws ServiceException;

    void guardarModifPresup(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
            Integer idExpedienteModPrePlaOtra) throws ServiceException;

}
