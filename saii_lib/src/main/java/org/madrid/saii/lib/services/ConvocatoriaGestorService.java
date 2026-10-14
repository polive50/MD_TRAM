package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaGestorDAO;
import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaGestor
 *
 * @author nttdata
 */
public interface ConvocatoriaGestorService
        extends BaseService<ConvocatoriaGestor, Integer, ConvocatoriaGestorDAO> {

    /**
     * Guarda un/a convocatoriaGestor.
     *
     * @param convocatoriaGestorDTO the convocatoriaGestor DTO
     * @return convocatoriaGestorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaGestorDTO guardar(ConvocatoriaGestorDTO convocatoriaGestorDTO)
            throws ServiceException;

    /**
     * Buscar por id Convocatoria.
     *
     * @param idConvocatoriaGestor the id ConvocatoriaGestor
     * @return List<ConvocatoriaGestorDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaGestorDTO> obtenerListaConvGestorPorIdConv(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Obtener por IdConvocatoria y CodigoUsuario
     *
     * @param idConvocatoria
     * @param cdUsuario
     * @return ConvocatoriaGestorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaGestorDTO obtenerPorIdConvocatoriaCodigoUsuario(Integer idConvocatoria,
            String cdUsuario) throws ServiceException;

    /**
     * Buscar por cdUsuarioGestor.
     *
     * @param cdUsuarioGestor the UsuarioGestor
     * @return List<ConvocatoriaGestorDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaGestorDTO> buscarPorUsuario(String cdUsuarioGestor) throws ServiceException;
    
    
    /**
     * Buscar por id Convocatoria y filtros.
     *
     * @param idConvocatoriaGestor the id ConvocatoriaGestor
     * @return List<ConvocatoriaGestorDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaGestorDTO> obtenerListaGestoresPorFiltro(final int index, final int pageSize, 
            final List<AtlasFilter> filtros, final List<AtlasSort> orden)
            throws ServiceException;
    
    
    /**
     * Obtener por IdConvocatoria
     *
     * @param idConvocatoria
     * @return ConvocatoriaGestorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaGestorDTO obtenerPorIdConvocatoria(Integer idConvocatoria) throws ServiceException;
    
    /**
     * Método para traer los tipos de trámites
     * @param idConvocatoria
     * @param login
     * @return
     */
    public List<Integer> obtenerTiposTramite(Integer idConvocatoria, String login);
    
}
