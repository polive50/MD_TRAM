package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAreaDAO;
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaArea
 *
 * @author nttdata
 */
public interface ConvocatoriaAreaService
        extends BaseService<ConvocatoriaArea, Integer, ConvocatoriaAreaDAO> {

    /**
     * Buscar paginadamente ConvocatoriaArea.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaAreaDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaAreaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * agregar un/a convocatoriaArea.
     *
     * @param idConvocatoria
     * @param idTipoDocumento
     * @return ConvocatoriaTipoDocDTO
     * @throws ServiceException the service exception
     */
    public ConvocatoriaAreaDTO agregar(Integer idConvocatoria, Integer idValor)
            throws ServiceException;

    /**
     * Permite obtener las áreas científicas por id de convocatoria
     *
     * @param idConvocatoria Identificador de convocatoria
     * @return Lista de areas cientificas asociadas a la convocatoria
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    List<ConvocatoriaAreaDTO> obtenerAreasCientificasPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

}
