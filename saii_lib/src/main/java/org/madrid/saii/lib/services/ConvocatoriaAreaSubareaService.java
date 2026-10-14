package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAreaSubareaDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaSubareaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaAreaSubarea
 *
 * @author quntal
 */
public interface ConvocatoriaAreaSubareaService
        extends BaseService<ConvocatoriaAreaSubarea, Integer, ConvocatoriaAreaSubareaDAO> {

    /**
     * Buscar paginadamente ConvocatoriaAreaSubarea.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaAreaDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaAreaSubareaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * agregar un/a convocatoriaAreaSubarea.
     *
     * @param idConvocatoriaArea
     * @param idTipoDocumento
     * @return ConvocatoriaTipoDocDTO
     * @throws ServiceException the service exception
     */
    public ConvocatoriaAreaSubareaDTO agregar(Integer idConvocatoriaArea, Integer idValor)
            throws ServiceException;

    /**
     * Permite obtener las subáreas científicas por id de convocatoria area
     *
     * @param idConvocatoriaArea Identificador de convocatoria area
     * @return Lista de subareas cientificas asociadas a la convocatoria
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    List<ConvocatoriaAreaSubareaDTO> obtenerSubareasCientificasPorConvocatoriaArea(Integer idConvocatoriaArea)
            throws ServiceException;

}
