package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifOtraDAO;
import org.madrid.saii.lib.domain.ExpedienteModifOtra;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteModifOtra
 *
 * @author quental
 */
public interface ExpedienteModifOtraService extends BaseService<ExpedienteModifOtra, Integer, ExpedienteModifOtraDAO> {

    /**
     * Método para obtener datos de las modificaciones del expediente.
     *
     * @param idExpedienteModifOtra Identificador de expedienteModifOtra
     * @return ExpedienteModifOtraDTO Datos del ExpedienteModifOtra
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteModifOtraDTO buscarId(Integer idExpedienteModifOtra) throws ServiceException;
    
    /**
     * Método para obtener el texto de otras modificaciones
     *
     * @param idExpedienteModifPresPlazoOtra Identificador de ExpedienteModifPresPlazoOtra
     * @return ExpedienteModifOtraDTO Datos del ExpedienteModifOtra
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteModifOtraDTO buscarIdExpedienteModif(Integer idExpedienteModifPresPlazoOtra) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteModifOtra.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModifOtraDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModifOtraDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteModifOtra.
     *
     * @param expedienteModifOtraDTO 
     * @return expedienteModifOtraDTO
     * @throws ServiceException the service exception
     */
    ExpedienteModifOtraDTO guardar(ExpedienteModifOtraDTO expedienteModifOtraDTO) throws ServiceException;

    /**
     * Borrado logico ExpedienteModifOtra
     *
     * @param idExpedienteModifOtra
     * @throws ServiceException
     */
    void borradoLogico(final Integer idExpedienteModifOtra) throws ServiceException;

    ExpedienteModifOtraDTO obtenerExpedientePrespModifOtra(Integer idExpediente) throws ServiceException;

    List<ExpedienteModifOtraDTO> buscarPorIdExpModifPresPlazoOtra(Integer idExpModifPresPlazoOtra, boolean ordenAsc)
            throws ServiceException;

}
