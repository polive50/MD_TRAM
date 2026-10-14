package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionJustecApartaDAO;
import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Apartado
 *
 * @author nttdata
 */
public interface EvaluacionJustecApartaService
        extends BaseService<EvaluacionJustecAparta, Integer, EvaluacionJustecApartaDAO> {

    /**
     * Buscar por id Apartado.
     *
     * @param idApartado the id Apartado
     * @return EvaluacionJustecApartaDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecApartaDTO buscarId(Integer idApartado) throws ServiceException;

    /**
     * Buscar paginadamente Apartado.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionJustecApartaDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionJustecApartaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a apartado.
     *
     * @param apartadoDTO the apartado DTO
     * @return apartadoDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecApartaDTO guardar(EvaluacionJustecApartaDTO apartadoDTO)
            throws ServiceException;

    /**
     * Método para obtener lista de apartados de una evaluación
     *
     * @param idEvaluacion
     * @return
     * @throws ServiceException
     */
    List<EvaluacionJustecApartaDTO> obtenerApartadosPorEvaluacion(Integer idEvaluacion)
            throws ServiceException;

}
