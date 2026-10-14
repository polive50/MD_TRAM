package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifPlazoDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPlazo;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteModifPlazoService
 *
 * @author Quental
 */
public interface ExpedienteModifPlazoService
        extends BaseService<ExpedienteModifPlazo, Integer, ExpedienteModifPlazoDAO> {

    /**
    * Buscar por identificador
    */
    ExpedienteModifPlazoDTO buscarId(Integer idExpedienteModifPlazo) throws ServiceException;
    
    /**
     * Buscar paginadamente ExpedienteModifPlazo.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteAnualidadDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModifPlazoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteModifPlazo.
     *
     * @param ExpedienteModifPlazoDTO the ExpedienteModifPlazo DTO
     * @return ExpedienteModifPlazoDTO
     * @throws ServiceException the service exception
     */
    ExpedienteModifPlazoDTO guardar(ExpedienteModifPlazoDTO expedienteModifPlazoDTO) throws ServiceException;

    /**
     * Método que busca ExpedienteModifPlazo por id de expediente 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteModifPlazoDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Actualizar ExpedienteModifPlazoDTO
     */
    void actualizar(ExpedienteModifPlazoDTO expedienteModifPlazoDTO) throws ServiceException;

    List<ExpedienteModifPlazoDTO> consultarModifPlazoPorIdModif(
            Integer idExpedienteModifPresPlazoOtra, boolean ordenAsc) throws ServiceException;



}
