package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.UsuarioDAO;
import org.madrid.saii.lib.domain.Usuario;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.madrid.saii.lib.dto.converters.UsuarioToDTO;
import org.madrid.saii.lib.dto.converters.UsuarioToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio UsuarioJur
 *
 * @author everis
 */
@Service("usuarioService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Integer, UsuarioDAO>
        implements UsuarioService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param usuarioJurDAO
     */
    @Autowired
    public UsuarioServiceImpl(@Qualifier("usuarioDAO") UsuarioDAO usuarioDAO) {
        super();
        this.setDao(usuarioDAO);
    }

    /**
     * Constructor vacio
     */
    public UsuarioServiceImpl() {
        super();
    }

    /**
     * Buscar por id UsuarioJur.
     *
     * @param idUsuarioJur the id UsuarioJur
     * @return UsuarioJurDTO
     * @throws ServiceException the service exception
     */
    @Override
    public UsuarioDTO buscarId(Integer idUsuario) throws ServiceException {
        return UsuarioToDTO.INSTANCE.apply(this.find(idUsuario));
    }

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
    @Override
    public List<UsuarioDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), UsuarioToDTO.INSTANCE));
    }

    /**
     * Guarda un/a usuarioJur.
     *
     * @param usuarioJurDTO the usuarioJur DTO
     * @return UsuarioJurDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioDTO guardar(UsuarioDTO usuarioDTO) throws ServiceException {
        return UsuarioToDTO.INSTANCE
                .apply(this.insertOrUpdate(UsuarioToEntity.INSTANCE.apply(usuarioDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioDTO borradoLogico(final UsuarioDTO usuarioDTO) throws ServiceException {
        usuarioDTO.setCdUsuBaja(UserUtils.getUserName());
        usuarioDTO.setFcBajaFila(new Date());
        return UsuarioToDTO.INSTANCE.apply(this.update(UsuarioToEntity.INSTANCE.apply(usuarioDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioDTO altaLogica(final UsuarioDTO usuarioDTO) throws ServiceException {
        usuarioDTO.setCdUsuBaja(null);
        usuarioDTO.setFcBajaFila(null);
        return UsuarioToDTO.INSTANCE.apply(this.update(UsuarioToEntity.INSTANCE.apply(usuarioDTO)));
    }

    @Override
    public UsuarioDTO buscarCdUsuario(String login) throws ServiceException {

        Usuario usuario = new Usuario();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO, login, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Usuario> lstUsuarios = this.findAll();

        if (!lstUsuarios.isEmpty()) {
            for (Usuario usuarioAux : lstUsuarios) {
                if (login.equals(usuarioAux.getCdUsuario())) {
                    usuario = usuarioAux;
                }
            }
        }

        return UsuarioToDTO.INSTANCE.apply(usuario);
    }

    /**
     *
     */
    @Override
    public Boolean consultarTieneRolesUsuarioSaii(String cdUsuario) throws ServiceException {
        return this.getDao().consultarTieneRolesUsuarioSaii(cdUsuario);
    }

    /**
     *
     */
    @Override
    public Boolean consultarEsUsuarioGestor(String cdUsuario) throws ServiceException {
        return this.getDao().consultarEsUsuarioGestor(cdUsuario);
    }

}
