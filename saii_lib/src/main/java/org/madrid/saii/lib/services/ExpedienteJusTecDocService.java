package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteJusTecDocService
 *
 * @author quental
 */
public interface ExpedienteJusTecDocService
        extends BaseService<ExpedienteJustecDoc, Integer, ExpedienteJustecDocDAO> {

    /**
     * Buscar paginadamente ExpedienteJusTecDoc.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteJusTecDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJustecDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

  
    /**
     * Método que busca expedientes por id de expediente 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJustecDocDTO> buscarPorExpedienteDoc(Integer idExpedienteDoc) throws ServiceException;


    void borradoLogico(Integer idExpedienteJustecDoc) throws ServiceException;

   
}
