package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Bloque
 *
 * @author everis
 */
public interface BloqueService extends BaseService<Bloque, Integer, BloqueDAO> {

    /**
     * Buscar por id id Bloque.
     *
     * @param idBloque the id Bloque
     * @return BloqueDTO
     * @throws ServiceException the service exception
     */
    BloqueDTO buscarId(final Integer idBloque) throws ServiceException;

    /**
     * Guarda un/a BloqueDTO.
     *
     * @param BloqueDTO the Bloque
     * @return BloqueDTO
     * @throws ServiceException the service exception
     */
    BloqueDTO guardar(final BloqueDTO bloqueDTO, ComponenteDTO componenteDTO)
            throws ServiceException;
    
    /**
     * Actualiza un/a BloqueDTO.
     *
     * @param BloqueDTO the Bloque
     * @return BloqueDTO
     * @throws ServiceException the service exception
     */
    BloqueDTO actualizarBloquePadre(final BloqueDTO bloqueDTO, ComponenteDTO componenteDTO)
            throws ServiceException;
    
    /**
     * Modifica un/a BloqueDTO.
     *
     * @param BloqueDTO the Bloque
     * @return BloqueDTO
     * @throws ServiceException the service exception
     */
    void modificarBloque(final BloqueDTO bloqueDTO)
            throws ServiceException;
    

    /**
     * Buscar paginadamente los Impresos.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<BloqueDTO>
     * @throws ServiceException the service exception
     */
    List<BloqueDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Metodo para buscar por formato de bloque
     * 
     * @param idImpreso
     * @param tipo formato
     * @return
     * @throws ServiceException
     */
    public List<BloqueDTO> buscarPorFormato(Integer idImpreso, Integer tipo)
            throws ServiceException;
    
    /**
     * Buscar por id id Impreso.
     *
     * @param idBloque the id Impreso
     * @throws ServiceException the service exception
     */
    public List<BloqueDTO> buscarByIdImpreso(final Integer idImpreso) throws ServiceException;
    
    /**
     * Buscar por id id Bloque.
     *
     * @param idBloque the id bloque
     * @throws ServiceException the service exception
     */
    List<BloqueDTO> buscarByIdBloquePadre(Integer idBloquePadre) throws ServiceException;
    
    /**
     * Buscar por id id Bloque.
     *
     * @param idBloque the id bloque
     * @throws ServiceException the service exception
     */
    BloqueDTO buscarByIdBlqPad(Integer idBloquePadre) throws ServiceException;

}
