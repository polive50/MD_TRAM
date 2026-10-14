package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionNotaDatoDAO;
import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionNotaDato
 *
 * @author nttdata
 */
public interface EvaluacionNotaDatoService
        extends BaseService<EvaluacionNotaDato, Integer, EvaluacionNotaDatoDAO> {

    /**
     * Buscar todos los registros de EvaluacionNotaDato.
     *
     * @return List<EvaluacionNotaDatoDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionNotaDatoDTO> obtener() throws ServiceException;

    /**
     * Buscar por id EvaluacionNotaDato.
     *
     * @param idEvaluacionNotaDato the id EvaluacionNotaDato
     * @return EvaluacionNotaDatoDTO
     * @throws ServiceException the service exception
     */
    EvaluacionNotaDatoDTO buscarId(Integer idEvaluacionNotaDato) throws ServiceException;

    /**
     * Buscar paginadamente EvaluacionNotaDato.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<EvaluacionNotaDatoDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionNotaDatoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Borrado logico EvaluacionNotaDato
     *
     * @param EvaluacionNotaDatoDTO
     * @return EvaluacionNotaDatoDTO
     * @throws ServiceException
     */
    void borradoLogico(final EvaluacionNotaDatoDTO evaluacionnotadatoDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param evaluacionnotadatoDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final EvaluacionNotaDatoDTO evaluacionnotadatoDTO) throws ServiceException;

    /**
     * obtener Evaluacion nota dato por el identificador del expediente de
     * evaluación.
     *
     * @param idExpedienteEval the ApartadoDatoDTO
     * @return List<EvaluacionNotaDatoDTO> the EvaluacionNotaDatoDTO
     * @throws ServiceException the service exception
     */
    List<EvaluacionNotaDatoDTO> obtenerEvaluacionNotaDatoPorIdExpedienteEval(
            Integer idExpedienteEval) throws ServiceException;

    /**
     * obtener la Evaluacion nota dato por el identificador del expediente y el
     * identificador de la evaluación.
     *
     * @param idExpedienteEval the ApartadoDatoDTO
     * @param idEvaluacion the ApartadoDatoDTO
     * @return List<EvaluacionNotaDatoDTO> the EvaluacionNotaDatoDTO
     * @throws ServiceException the service exception
     */
    public List<EvaluacionNotaDatoDTO> obtenerEvaluacionNotaDatoPorIdEvalIdExpEval(
            Integer idExpedienteEval, Integer idEvaluacion) throws ServiceException;

}
