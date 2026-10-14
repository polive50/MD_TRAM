package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EntidadDAO;
import org.madrid.saii.lib.domain.Entidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Entidad
 *
 * @author everis
 */
public interface EntidadService extends BaseService<Entidad, Integer, EntidadDAO> {

    /**
     * Buscar todos los registros de Entidad.
     *
     * @return List<EntidadDTO>
     * @throws ServiceException the service exception
     */
    List<EntidadDTO> obtener() throws ServiceException;

    /**
     * Buscar por id Entidad.
     *
     * @param idEntidad the id Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    EntidadDTO buscarId(Integer idEntidad) throws ServiceException;

    /**
     * Buscar paginadamente Entidad.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EntidadDTO>
     * @throws ServiceException the service exception
     */
    List<EntidadDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a entidad.
     *
     * @param entidadDTO the entidad DTO
     * @return entidadDTO
     * @throws ServiceException the service exception
     */
    EntidadDTO guardar(EntidadDTO entidadDTO) throws ServiceException;

    /**
     * Borrado logico Entidad
     *
     * @param EntidadDTO
     * @return EntidadDTO
     * @throws ServiceException
     */
    void borradoLogico(final EntidadDTO entidadDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param entidadDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final EntidadDTO entidadDTO) throws ServiceException;

    /**
     * Obtiene los estados de las entidades
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     */
    List<CatalogosValoresDTO> obtenerListaEstadosEntidad() throws ServiceException;

    /**
     * Buscar por cif Entidad.
     *
     * @param cif the cif Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    EntidadDTO buscarCif(String cif) throws ServiceException;

    List<CatalogosValoresDTO> obtenerListaTipoEntidad() throws ServiceException;
    
    
    /**
     * 
     * @param idInteresado
     * @return
     */
    EntidadDTO buscarEntidadInteresado(Integer idInteresado);

}
