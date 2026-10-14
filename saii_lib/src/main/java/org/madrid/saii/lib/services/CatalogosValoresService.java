package org.madrid.saii.lib.services;

import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio CatalogosValores
 *
 * @author everis
 */
public interface CatalogosValoresService
        extends BaseService<CatalogosValores, Integer, CatalogosValoresDAO> {

    /**
     * Buscar todos los registros de CatalogosValores que no tengan un dsValor
     * repetido
     *
     * @param filtros para obtener los catalogos valores
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> obtenerPorFiltros(List<AtlasFilter> filtros) throws ServiceException;

    /**
     * Buscar por id idValor.
     *
     * @param idValor the id CatalogosValores
     * @return CatalogosValoresDTO
     * @throws ServiceException the service exception
     */
    CatalogosValoresDTO buscarId(final Integer idValor) throws ServiceException;

    /**
     * Buscar por nmConstante.
     *
     * @param idValor the id CatalogosValores
     * @return CatalogosValoresDTO
     * @throws ServiceException the service exception
     */
    CatalogosValoresDTO buscarNmConstante(final Integer nmConstante) throws ServiceException;

    /**
     * Buscar por idCatalogo de CatalogosValores.
     *
     * @param idCatalogo the idCatalogo CatalogosValores
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> buscarPorCatalogo(final Integer idCatalogo) throws ServiceException;

    List<CatalogosValoresDTO> buscarPorCatalogoByNmConstante(final Integer nmConstante) throws ServiceException;

    /**
     * Buscar por idCatalogo de CatalogosValores.
     *
     * @param idCatalogo the idCatalogo CatalogosValores
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> buscarPorCatalogoOrderNmConstante(final Integer idCatalogo) throws ServiceException;

    /**
     * Buscar por idCatalogo de CatalogosValores.
     *
     * @param idCatalogo the idCatalogo CatalogosValores
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> obtenerCatalogoConvocatoria(final Integer idCatalogo) throws ServiceException;

    /**
     * Buscar paginadamente CatalogosValores.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar paginadamente CatalogosValores. En caso de existir repetidos, solo
     * se muestran una vez.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> buscarNoRep(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Devuelve la cantidad de CatalogosValores de forma unica (Count distict en
     * SQL)
     *
     * @return Integer
     * @throws ServiceException the service exception
     */
    Integer countDistinctDsValor() throws ServiceException;

    /**
     * Guarda un/a CatalogosValores.
     *
     * @param CatalogosValoresDTO the CatalogosValores DTO
     * @return CatalogosValoresDTO
     * @throws ServiceException the service exception
     */
    CatalogosValoresDTO guardar(final CatalogosValoresDTO catalogosValoresDTO)
            throws ServiceException;

    List<CatalogosValoresDTO> obtenerTipoEntidades() throws ServiceException;
    
//    /**
//     * Devuelve un listado de los registros con nmConstante
//     * 
//     * @return
//     * @throws ServiceException
//     */
//    List<CatalogosValores> cargaNmConstante() throws ServiceException;
    
    /**
     * Método recupera catalogos valores por idConvocatoria y IdTipoCausa
     * 
     * @param idConvocatoria
     * @param idTipoCausa
     * @return
     * @throws ServiceException
     */
    List<CatalogosValoresDTO> obtenerCatalogoConvocatoriaTipoCausa(final Integer idConvocatoria,
            final Integer idTipoCausa) throws ServiceException;

}
