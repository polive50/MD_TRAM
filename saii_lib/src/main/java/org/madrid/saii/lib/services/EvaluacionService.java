package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionDAO;
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.InformeDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Evaluacion
 *
 * @author nttdata
 */
public interface EvaluacionService extends BaseService<Evaluacion, Integer, EvaluacionDAO> {

    /**
     * Buscar por id Evaluacion.
     *
     * @param idEvaluacion the id Evaluacion
     * @return EvaluacionDTO
     * @throws ServiceException the service exception
     */
    EvaluacionDTO buscarId(Integer idEvaluacion) throws ServiceException;

    /**
     * Buscar paginadamente Evaluacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a evaluacion.
     *
     * @param evaluacionDTO the evaluacion DTO
     * @return evaluacionDTO
     * @throws ServiceException the service exception
     */
    EvaluacionDTO guardar(EvaluacionDTO evaluacionDTO, Integer idConvocatoria) throws ServiceException;

    /**
     * Borrado logico Evaluacion
     *
     * @param EvaluacionDTO
     * @return EvaluacionDTO
     * @throws ServiceException
     */
    void borradoLogico(final EvaluacionDTO evaluacionDTO) throws ServiceException;

    /**
     * Método para obtener la lista de evaluaciones asociadas a la convocatoria segun sea coordonador o evaluador
     *
     * @param idConvocatoria
     * @param itCoorEval
     * @return
     * @throws ServiceException
     */
    List<EvaluacionDTO> obtenerPorConvocatoria(Integer idConvocatoria, String itCoorEval)
            throws ServiceException;
    
    /**
     * Método para obtener la lista de evaluaciones asociadas a la convocatoria segun sea el tipo de informe
     *
     * @param idConvocatoria
     * @param idTipoinforme
     * @return
     * @throws ServiceException
     */
    List<EvaluacionDTO> obtenerPorConvocatoria(Integer idConvocatoria, Integer idTipoinforme)
            throws ServiceException;

    /**
     * Método para obtener la lista de evaluaciones asociadas a la convocatoria
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<EvaluacionDTO> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException;
    
    /**
     * 
     * @param idInforme
     * @return
     * @throws ServiceException
     */
    List<Evaluacion> buscarPorInforme(Integer idInforme) throws ServiceException;

    /**
     * Método para obtener la lista de evaluaciones asociadas a la convocatoria segun sea coordonador o evaluador
     *
     * @param idConvocatoria
     * @param informe
     * @return
     * @throws ServiceException
     */
    List<EvaluacionDTO> obtenerPorInforme(InformeDTO informe) throws ServiceException;

    
}
