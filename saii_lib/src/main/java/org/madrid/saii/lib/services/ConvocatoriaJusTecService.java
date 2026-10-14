package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusTecDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusTec
 *
 * @author nttdata
 */
public interface ConvocatoriaJusTecService
        extends BaseService<ConvocatoriaJusTec, Integer, ConvocatoriaJusTecDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusTec.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusTecDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaJusTecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaJusTec.
     *
     * @param ConvocatoriaJusTecDTO the ConvocatoriaJusTec DTO
     * @return ConvocatoriaJusTecDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaJusTecDTO guardar(ConvocatoriaJusTecDTO convocatoriaJusTecDTO)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmPeriodo
     * @throws ServiceException
     */
    void cargarDatosJusTec(Integer idConvocatoria, Integer nmPeriodo) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaJusTecDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param nmPeriodo
     * @param cantidadRegistrosCargados
     * @throws ServiceException
     */
    void actualizarDatosJusTec(Integer idConvocatoria, Integer nmPeriodo,
            int cantidadRegistrosCargados) throws ServiceException;

    List<ConvocatoriaJusTec> buscarPeriodos(ConvocatoriaDTO dto) throws ServiceException;

    ConvocatoriaJusTecDTO buscarPorIdConvocatoriaJustec(Integer idConvocatoriaJustec)
            throws ServiceException;

}
