package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusEcoDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusEco
 *
 * @author quental
 */
public interface ConvocatoriaJusEcoService
        extends BaseService<ConvocatoriaJusEco, Integer, ConvocatoriaJusEcoDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusEco.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusEcoDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaJusEcoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaJusEco.
     *
     * @param ConvocatoriaJusEcoDTO the ConvocatoriaJusEco DTO
     * @return ConvocatoriaJusEcoDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaJusEcoDTO guardar(ConvocatoriaJusEcoDTO convocatoriaJusEcoDTO)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmPeriodo
     * @throws ServiceException
     */
    void cargarDatosJusEco(Integer idConvocatoria, Integer nmPeriodo) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaJusEcoDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmPeriodo
     * @param cantidadRegistrosCargados
     * @throws ServiceException
     */
    void actualizarDatosJusEco(Integer idConvocatoria, Integer nmPeriodo,
            int cantidadRegistrosCargados) throws ServiceException;

    List<ConvocatoriaJusEco> buscarPeriodos(ConvocatoriaDTO dto) throws ServiceException;

}
