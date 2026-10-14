package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAnualidadDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaAnualidad
 *
 * @author Quental
 */
public interface ConvocatoriaAnualidadService
        extends BaseService<ConvocatoriaAnualidad, Integer, ConvocatoriaAnualidadDAO> {

    /**
     * Buscar paginadamente ConvocatoriaAnualidad.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaAnualidadDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaAnualidadDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaAnualidad.
     *
     * @param ConvocatoriaAnualidadDTO the ConvocatoriaAnualidad DTO
     * @return ConvocatoriaAnualidadDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaAnualidadDTO guardar(ConvocatoriaAnualidadDTO convocatoriaAnualidadDTO)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmAnualidad
     * @throws ServiceException
     */
    void cargarDatosAnualidad(Integer idConvocatoria, Integer nmAnualidad) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaAnualidadDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmAnualidad
     * @param cantidadRegistrosCargados
     * @throws ServiceException
     */
    void actualizarDatosAnualidad(Integer idConvocatoria, Integer nmAnualidad,
            int cantidadRegistrosCargados) throws ServiceException;

    List<ConvocatoriaAnualidad> buscarAnualidades(ConvocatoriaDTO dto) throws ServiceException;

}
