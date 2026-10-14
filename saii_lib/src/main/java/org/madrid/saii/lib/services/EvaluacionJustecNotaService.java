package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionJustecNotaDAO;
import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionNota
 *
 * @author nttdata
 */
public interface EvaluacionJustecNotaService
        extends BaseService<EvaluacionJustecNota, Integer, EvaluacionJustecNotaDAO> {

    /**
     * Buscar por id EvaluacionNota.
     *
     * @param idEvaluacionNota the id EvaluacionNota
     * @return EvaluacionJustecNotaDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecNotaDTO buscarId(Integer idEvaluacionNota) throws ServiceException;

    /**
     * Buscar paginadamente EvaluacionNota.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionJustecNotaDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionJustecNotaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a evaluacionNota.
     *
     * @param evaluacionNotaDTO the evaluacionNota DTO
     * @return evaluacionNotaDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecNotaDTO guardar(EvaluacionJustecNotaDTO evaluacionNotaDTO)
            throws ServiceException;

    /**
     * Método para obtenerla lista notas de una evaluación
     *
     * @param idEvaluacion
     * @return
     * @throws ServiceException
     */
    List<EvaluacionJustecNotaDTO> obtenerPorEvaluacion(Integer idEvaluacion)
            throws ServiceException;

    /**
     * Metodo para obtener la lista de notas por defecto
     *
     * @param lstRangos
     * @param idConvocatoria
     * @return List<EvaluacionJustecNotaDTO>
     * @throws ServiceException
     */
    List<EvaluacionJustecNotaDTO> obtenerListaRangoNotasPorDefecto(
            List<CatalogosValoresDTO> lstRangos, Integer idConvocatoria) throws ServiceException;
}
