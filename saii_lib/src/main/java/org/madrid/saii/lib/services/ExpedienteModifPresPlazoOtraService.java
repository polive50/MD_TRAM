package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifPresPlazoOtraDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
public interface ExpedienteModifPresPlazoOtraService extends BaseService<ExpedienteModifPresPlazoOtra, Integer, ExpedienteModifPresPlazoOtraDAO> {

    /**
     * Método para obtener datos de las modificaciones del expediente.
     *
     * @param idExpedienteModifPresPlazoOtra Identificador de expedienteModifPresPlazoOtra
     * @return ExpedienteModifPresPlazoOtraDTO Datos del ExpedienteModifPresPlazoOtra
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteModifPresPlazoOtraDTO buscarId(Integer idExpedienteModifPresPlazoOtra) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteModifPresPlazoOtra.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModifPresPlazoOtraDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteModifPresPlazoOtraDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteModifPresPlazoOtra.
     *
     * @param expedienteModifPresPlazoOtraDTO 
     * @return expedienteModifPresPlazoOtraDTO
     * @throws ServiceException the service exception
     */
    ExpedienteModifPresPlazoOtraDTO guardar(ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO) throws ServiceException;

    /**
     * Borrado logico ExpedienteModifPresPlazoOtra
     *
     * @param idExpedienteModifPresPlazoOtra
     * @throws ServiceException
     */
    void borradoLogico(final Integer idExpedienteModifPresPlazoOtra) throws ServiceException;

    /**
     * Método para crear un ExpedienteModifPresPlazoOtra
     *
     * @param expedienteModifPresPlazoOtraDTO Datos de la modificacion de expediente a crear
     * @return ExpedienteModifPresPlazoOtraDTO
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    ExpedienteModifPresPlazoOtraDTO crear(ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO) throws ServiceException;

    List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpedienteEstado(Integer idExpediente,
            Integer idEstado) throws ServiceException;

    List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpedienteEstadoAbiertas(Integer idExpediente)
            throws ServiceException;

}
