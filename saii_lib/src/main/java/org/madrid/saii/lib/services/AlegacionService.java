package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.AlegacionDAO;
import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.dto.AlegacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Alegacion
 *
 * @author everis
 */
public interface AlegacionService extends BaseService<Alegacion, Integer, AlegacionDAO> {

    /**
     * Buscar todos los registros de Alegacion.
     *
     * @return List<AlegacionDTO>
     * @throws ServiceException the service exception
     */
    List<AlegacionDTO> obtener() throws ServiceException;

    /**
     * Buscar por id Alegacion.
     *
     * @param idAlegacion the id Alegacion
     * @return AlegacionDTO
     * @throws ServiceException the service exception
     */
    AlegacionDTO buscarId(Integer idAlegacion) throws ServiceException;

    /**
     * Buscar paginadamente Alegacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<AlegacionDTO>
     * @throws ServiceException the service exception
     */
    List<AlegacionDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a alegacion.
     *
     * @param alegacionDTO the alegacion DTO
     * @return alegacionDTO
     * @throws ServiceException the service exception
     */
    AlegacionDTO guardar(AlegacionDTO alegacionDTO) throws ServiceException;

    /**
     * Metodo que se encarga de borrar del interesadoDTO.
     *
     * @param interesadoDTO the interesado DTO
     * @throws ServiceException the service exception
     */

    void borradoLogico(AlegacionDTO alegacionDTO) throws ServiceException;

    /**
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<AlegacionDTO> obtenerPorConvocatoria(Integer idConvocatoria) throws ServiceException;

}
