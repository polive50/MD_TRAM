package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.UsuarioJurDAO;
import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.UsuarioJurDTO;
import org.madrid.saii.lib.dto.UsuarioJurFiltrosDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio UsuarioJur
 *
 * @author everis
 */
public interface UsuarioJurService extends BaseService<UsuarioJur, Integer, UsuarioJurDAO> {

    /**
     * Buscar por id UsuarioJur.
     *
     * @param idUsuarioJur the id UsuarioJur
     * @return UsuarioJurDTO
     * @throws ServiceException the service exception
     */
    UsuarioJurDTO buscarId(final Integer idUsuarioJur) throws ServiceException;

    /**
     * Buscar paginadamente UsuarioJur.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<UsuarioJurDTO>
     * @throws ServiceException the service exception
     */
    List<UsuarioJurDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a usuarioJur.
     *
     * @param usuarioJurDTO the usuarioJur DTO
     * @return usuarioJurDTO
     * @throws ServiceException the service exception
     */
    UsuarioJurDTO guardar(final UsuarioJurDTO usuarioJurDTO) throws ServiceException;

    /**
     * Borrado logico UsuarioJur
     *
     * @param UsuarioJurDTO
     * @return UsuarioJurDTO
     * @throws ServiceException
     */
    UsuarioJurDTO borradoLogico(final UsuarioJurDTO usuarioJurDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param usuariojurDTO
     * @throws ServiceException
     */
    UsuarioJurDTO altaLogica(final UsuarioJurDTO usuariojurDTO) throws ServiceException;

    /**
     * Busca una usuario Juridico según sus filtros.
     *
     * @param UsuarioJurFiltro
     * @return List<UsuarioJurDTO>
     * @throws Service Exception
     */
    List<UsuarioJurDTO> filtrar(final UsuarioJurFiltrosDTO usuarioJurFiltroDTO)
            throws ServiceException;

    /**
     * Método que crea la empresa y el representante si no existen
     *
     * @return UsuarioJurDTO
     * @throws Service Exception
     */
    UsuarioJurDTO registrarPersonaJur(final UsuarioJurDTO usuarioJurDTO) throws ServiceException;

    /**
     * Método que crea un representante nuevo
     *
     * @return UsuarioJurDTO
     * @throws Service Exception
     */
    UsuarioJurDTO crearPersonaJur(final UsuarioJurDTO usuarioJurDTO) throws ServiceException;

}
