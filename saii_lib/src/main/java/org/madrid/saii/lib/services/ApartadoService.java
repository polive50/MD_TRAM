package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ApartadoDAO;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.dto.ApartadoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Apartado
 *
 * @author nttdata
 */
public interface ApartadoService extends BaseService<Apartado, Integer, ApartadoDAO> {

    /**
     * Buscar por id Apartado.
     *
     * @param idApartado the id Apartado
     * @return ApartadoDTO
     * @throws ServiceException the service exception
     */
    ApartadoDTO buscarId(Integer idApartado) throws ServiceException;

    /**
     * Buscar paginadamente Apartado.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ApartadoDTO>
     * @throws ServiceException the service exception
     */
    List<ApartadoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a apartado.
     *
     * @param apartadoDTO the apartado DTO
     * @return apartadoDTO
     * @throws ServiceException the service exception
     */
    ApartadoDTO guardar(ApartadoDTO apartadoDTO) throws ServiceException;

    /**
     * Método para obtener lista de apartados de una evaluación
     *
     * @param idEvaluacion
     * @return
     * @throws ServiceException
     */
    List<ApartadoDTO> obtenerApartadosPorEvaluacion(Integer idEvaluacion) throws ServiceException;

}
