package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionJustecDAO;
import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Evaluacion
 *
 * @author nttdata
 */
public interface EvaluacionJustecService
        extends BaseService<EvaluacionJustec, Integer, EvaluacionJustecDAO> {

    /**
     * Buscar por id Evaluacion.
     *
     * @param idEvaluacion the id Evaluacion
     * @return EvaluacionJustecDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecDTO buscarId(Integer idEvaluacion) throws ServiceException;

    /**
     * Buscar paginadamente Evaluacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionJustecDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionJustecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a evaluacion.
     *
     * @param EvaluacionJustecDTO the evaluacion DTO
     * @return EvaluacionJustecDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecDTO guardar(EvaluacionJustecDTO EvaluacionJustecDTO) throws ServiceException;

    /**
     * Borrado logico Evaluacion
     *
     * @param EvaluacionJustecDTO
     * @return EvaluacionJustecDTO
     * @throws ServiceException
     */
    void borradoLogico(final EvaluacionJustecDTO EvaluacionJustecDTO) throws ServiceException;

    /**
     * Método para obtener la lista de evaluaciones asociadas a la convocatoria
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<EvaluacionJustecDTO> obtenerPorConvocatoria(Integer idConvocatoria, String itTipoEval)
            throws ServiceException;
    
    
    /**
     * Método para obtener la lista de evaluacionesJustec asociadas a la convocatoria
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<EvaluacionJustec> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException;

}
