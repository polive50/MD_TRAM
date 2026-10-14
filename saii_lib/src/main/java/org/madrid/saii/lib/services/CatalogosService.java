package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.CatalogosDAO;
import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Catalogos
 *
 * @author everis
 */
public interface CatalogosService extends BaseService<Catalogos, Integer, CatalogosDAO> {
    /**
     * Buscar todos los registros de Catalogos.
     *
     * @return List<CatalogosDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosDTO> obtener() throws ServiceException;

    /**
     * Buscar por id Catalogos.
     *
     * @param idCatalogos the id Catalogos
     * @return CatalogosDTO
     * @throws ServiceException the service exception
     */
    CatalogosDTO buscarId(final Integer idCatalogos) throws ServiceException;

    /**
     * Buscar por dsNombre de Catalogos.
     *
     * @param dsNombre the dsNombre Catalogos
     * @return List<CatalogosDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosDTO> buscarPorNombre(final String dsNombre) throws ServiceException;

    /**
     * Buscar paginadamente Catalogos.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<CatalogosDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a Catalogos.
     *
     * @param CatalogosDTO the Catalogos DTO
     * @return CatalogosDTO
     * @throws ServiceException the service exception
     */
    CatalogosDTO guardar(final CatalogosDTO catalogosDTO) throws ServiceException;

    List<CatalogosDTO> buscarCatalogosMantenibles() throws ServiceException;
    
    CatalogosDTO guardarRevisionEvaluacion(final CatalogosValoresDTO catValorDTO) throws ServiceException;
    
//    /**
//     * Devuelve un listado de los registros con nmConstante
//     * 
//     * @return
//     * @throws ServiceException
//     */
//    List<Catalogos> cargaNmConstante() throws ServiceException;
    
}
