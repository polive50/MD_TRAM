package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ListadoColumnaDAO;
import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoColumnaDTO;
import org.madrid.saii.lib.dto.ListadoConfiguracionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ListadoColumna
 *
 * @author nttdata
 */
public interface ListadoColumnaService
        extends BaseService<ListadoColumna, Integer, ListadoColumnaDAO> {

    /**
     * Buscar paginadamente Informe.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ListadoDTO>
     * @throws ServiceException the service exception
     */
    List<ListadoColumnaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guardar informe columna.
     *
     * @param listadoColumnaDTO the ListadoColumna DTO
     * @return listadoColumnaDTO
     * @throws ServiceException the service exception
     */
    ListadoColumnaDTO guardar(ListadoColumnaDTO listadoColumnaDTO) throws ServiceException;

    void guardarConfiguracion(List<ListadoConfiguracionDTO> lista) throws ServiceException;

    /**
     * Obtener columnas de Listado
     *
     * @param idConvocatoria
     * @param idListado
     * @return List<ListadoConfiguracionDTO>
     */
    List<ListadoConfiguracionDTO> obtenerListadoColumnas(Integer idConvocatoria, Integer idListado)
            throws ServiceException;

    List<ListadoConfiguracionDTO> obtenerListaColumnaDisponible(Integer idConvocatoria)
            throws ServiceException;

    List<ListadoConfiguracionDTO> obtenerListaColumnaConfigurada(Integer idListado)
            throws ServiceException;

}
