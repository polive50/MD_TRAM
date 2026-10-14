package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.UnidadAdminDAO;
import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.UnidadAdminDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio UnidadAdmin
 *
 * @author everis
 */
public interface UnidadAdminService extends BaseService<UnidadAdmin, Integer, UnidadAdminDAO> {

    /**
     * Buscar todos los registros de UnidadAdmin por filtros.
     *
     * @return List<UnidadAdminDTO>
     * @throws ServiceException the service exception
     */
    List<UnidadAdminDTO> obtenerPorFiltros(List<AtlasFilter> filtros) throws ServiceException;

    /**
     * Buscar por id idUnidadAdmin.
     *
     * @param idUnidadAdmin the id UnidadAdmin
     * @return UnidadAdminDTO
     * @throws ServiceException the service exception
     */
    UnidadAdminDTO buscarId(final Integer idUnidadAdmin) throws ServiceException;

    /**
     * Buscar por cdUnidad
     *
     * @param cdUnidad the cdUnidad
     * @return UnidadAdminDTO
     * @throws ServiceException the service exception
     */
    UnidadAdminDTO buscarCdUnidad(final String cdUnidad) throws ServiceException;

    /**
     * Buscar paginadamente UnidadAdmin.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<UnidadAdminDTO>
     * @throws ServiceException the service exception
     */
    List<UnidadAdminDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Metodo que devuelve la consejeria
     *
     * @return
     * @throws ServiceException
     */
    String obtenerConsejeria() throws ServiceException;

    /**
     * Metodo que devuelve la descripcion de una Unidad
     *
     * @param cdUnidad
     * @return
     * @throws ServiceException
     */
    String obtenerDescripcionUnidad(Integer cdUnidad) throws ServiceException;

}
