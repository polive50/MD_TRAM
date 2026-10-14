package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionNotaDAO;
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionNota
 *
 * @author nttdata
 */
public interface EvaluacionNotaService
        extends BaseService<EvaluacionNota, Integer, EvaluacionNotaDAO> {

    /**
     * Buscar por id EvaluacionNota.
     *
     * @param idEvaluacionNota the id EvaluacionNota
     * @return EvaluacionNotaDTO
     * @throws ServiceException the service exception
     */
    EvaluacionNotaDTO buscarId(Integer idEvaluacionNota) throws ServiceException;

    /**
     * Buscar paginadamente EvaluacionNota.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionNotaDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionNotaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a evaluacionNota.
     *
     * @param evaluacionNotaDTO the evaluacionNota DTO
     * @return evaluacionNotaDTO
     * @throws ServiceException the service exception
     */
    EvaluacionNotaDTO guardar(EvaluacionNotaDTO evaluacionNotaDTO) throws ServiceException;

    /**
     * Método para obtenerla lista notas de una evaluación
     *
     * @param idEvaluacion
     * @return
     * @throws ServiceException
     */
    List<EvaluacionNotaDTO> obtenerPorEvaluacion(Integer idEvaluacion) throws ServiceException;
    
    /**
     * Metodo para obtener la lista de notas por defecto
     *
     * @param lstRangos
     * @param idConvocatoria
     * @return List<EvaluacionNotaDTO>
     * @throws ServiceException
     */
    List<EvaluacionNotaDTO> obtenerListaRangoNotasPorDefecto(List<CatalogosValoresDTO> lstRangos, 
            Integer idConvocatoria) throws ServiceException;
}
