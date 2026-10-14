package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.UsuarioDAO;
import org.madrid.saii.lib.domain.Usuario;
import org.madrid.saii.lib.dto.UsuarioDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio UsuarioJur
 *
 * @author everis
 */
public interface UsuarioService extends BaseService<Usuario, Integer, UsuarioDAO> {

    /**
     * Buscar por id UsuarioOrga.
     *
     * @param idUsuarioOrga the id UsuarioJur
     * @return UsuarioOrgaDTO
     * @throws ServiceException the service exception
     */
    UsuarioDTO buscarId(final Integer idUsuarioOrga) throws ServiceException;

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
    List<UsuarioDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a usuarioJur.
     *
     * @param usuarioJurDTO the usuarioJur DTO
     * @return usuarioJurDTO
     * @throws ServiceException the service exception
     */
    UsuarioDTO guardar(final UsuarioDTO usuarioOrgaDTO) throws ServiceException;

    /**
     * Borrado logico UsuarioJur
     *
     * @param UsuarioJurDTO
     * @return UsuarioJurDTO
     * @throws ServiceException
     */
    UsuarioDTO borradoLogico(final UsuarioDTO usuarioOrgaDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param usuariojurDTO
     * @throws ServiceException
     */
    UsuarioDTO altaLogica(final UsuarioDTO usuarioOrgaDTO) throws ServiceException;

    /**
     * Metodo para obtener la información de usuario por login
     *
     * @param login
     * @return
     * @throws ServiceException
     */
    UsuarioDTO buscarCdUsuario(String login) throws ServiceException;

    /**
     * Metodo de obtener la cantidad de rol de usuario
     *
     * @param cdUsuario
     * @return obtenerTotalRolesUsuario
     * @throws ServiceException
     */
    Boolean consultarTieneRolesUsuarioSaii(String cdUsuario) throws ServiceException;

    /**
     * Metodo para consultar si es usuario gestor
     *
     * @param cdUsuario
     * @return
     * @throws ServiceException
     */
    Boolean consultarEsUsuarioGestor(String cdUsuario) throws ServiceException;

}
