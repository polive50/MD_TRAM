package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoDocDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteJusEcoDocService
 *
 * @author quental
 */
public interface ExpedienteJusEcoDocService
        extends BaseService<ExpedienteJusEcoDoc, Integer, ExpedienteJusEcoDocDAO> {

    /**
     * Buscar paginadamente ExpedienteJusEcoDoc.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteJusEcoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJusEcoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

  
    /**
     * Método que busca expedientes por id de expediente 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusEcoDocDTO> buscarPorExpedienteDoc(Integer idExpedienteDoc) throws ServiceException;

    /**
     * 
     * @param idExpedienteJusEcoDoc
     * @throws ServiceException
     */
     void borradoLogico(Integer idExpedienteJusEcoDoc) throws ServiceException;

    /**
     * 
     * @param idExpedienteJuseco
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusEcoDocDTO> buscarPorExpedienteJuseco(Integer idExpedienteJuseco)
            throws ServiceException;
    
    /**
     * 
     * @param expedienteJusEcoDocDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteJusEcoDocDTO guardar (ExpedienteJusEcoDocDTO expedienteJusEcoDocDTO) throws ServiceException;

    ExpedienteJusEcoDocDTO buscarId(Integer id) throws ServiceException;

    List<ExpedienteJusEcoDocDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

   
}
