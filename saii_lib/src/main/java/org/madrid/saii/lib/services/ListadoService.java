package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ListadoDAO;
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ListadoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Listado
 *
 * @author nttdata
 */
public interface ListadoService extends BaseService<Listado, Integer, ListadoDAO> {

    /**
     * Buscar por id Listado.
     *
     * @param idListado the id Listado
     * @return ListadoDTO
     * @throws ServiceException the service exception
     */
    ListadoDTO buscarId(Integer idListado) throws ServiceException;

    /**
     * Buscar paginadamente Informe.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ListadoDTO>
     * @throws ServiceException the service exception
     */
    List<ListadoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a listado.
     *
     * @param listadoDTO the informe DTO
     * @return listadoDTO
     * @throws ServiceException the service exception
     */
    ListadoDTO guardar(ListadoDTO listadoDTO) throws ServiceException;

    /**
     * Obtiene el listadoExcel
     *
     * @param id
     * @return byte[]
     * @throws ServiceException
     */
    byte[] obtenerListadoExcel(Integer idListado) throws ServiceException;

    /**
     * Obtiene el listadoExcel
     *
     * @param id
     * @return byte[]
     * @throws ServiceException
     */
    byte[] obtenerListadoExcel2(Integer idConvocatoria, Integer idListado) throws ServiceException;

}
