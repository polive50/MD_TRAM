package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaSubmodDAO;
import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaSubmod
 *
 * @author nttdata
 */
public interface ConvocatoriaSubmodService
        extends BaseService<ConvocatoriaSubmod, Integer, ConvocatoriaSubmodDAO> {

    /**
     * Buscar por id ConvocatoriaSubmod.
     *
     * @param idConvocatoriaSubmod the id ConvocatoriaSubmod
     * @return ConvocatoriaSubmodDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaSubmodDTO buscarId(Integer idConvocatoriaSubmod) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaSubmod.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaSubmodDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaSubmodDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaSubmod.
     *
     * @param convocatoriaSubmodDTO the convocatoriaSubmod DTO
     * @return convocatoriaSubmodDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaSubmodDTO guardar(ConvocatoriaSubmodDTO convocatoriaSubmodDTO)
            throws ServiceException;

    /**
     * Permite obtener las submodalidades por id de convocatoria
     *
     * @param idConvocatoria Identificador de convocatoria
     * @return Lista de submodalidades asociadas a la convocatoria
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    List<ConvocatoriaSubmodDTO> obtenerSubmodalidadesPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

}
