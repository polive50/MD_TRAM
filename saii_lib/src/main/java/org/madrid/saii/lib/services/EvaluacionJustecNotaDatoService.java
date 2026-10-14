package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionJustecNotaDatoDAO;
import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDatoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionNotaDato
 *
 * @author nttdata
 */
public interface EvaluacionJustecNotaDatoService
        extends BaseService<EvaluacionJustecNotaDato, Integer, EvaluacionJustecNotaDatoDAO> {

    /**
     * Buscar todos los registros de EvaluacionNotaDato.
     *
     * @return List<EvaluacionNotaDatoDTO>
     * @throws ServiceException the service exception
     */
    List<EvaluacionJustecNotaDatoDTO> obtener() throws ServiceException;

    /**
     * Buscar por id EvaluacionNotaDato.
     *
     * @param idEvaluacionNotaDato the id EvaluacionNotaDato
     * @return EvaluacionNotaDatoDTO
     * @throws ServiceException the service exception
     */
    EvaluacionJustecNotaDatoDTO buscarId(Integer idEvaluacionJustecNotaDato) throws ServiceException;

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
    List<EvaluacionJustecNotaDatoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Borrado logico EvaluacionNotaDato
     *
     * @param EvaluacionNotaDatoDTO
     * @return EvaluacionNotaDatoDTO
     * @throws ServiceException
     */
    void borradoLogico(final EvaluacionJustecNotaDatoDTO evaluacionJustecnotadatoDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param evaluacionnotadatoDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final EvaluacionJustecNotaDatoDTO evaluacionnoJustectadatoDTO) throws ServiceException;

    /**
     * obtener Evaluacion nota dato por el identificador del expediente de
     * evaluación Justec.
     *
     * @param idExpedienteEvalJustec the ApartadoJustecDatoDTO
     * @return List<EvaluacionJustecNotaDatoDTO> the EvaluacionJustecNotaDatoDTO
     * @throws ServiceException the service exception
     */
    List<EvaluacionJustecNotaDatoDTO> obtenerEvaluacionJustecNotaDatoPorIdExpedienteEvalJustec(
            Integer idExpedienteEvalJustec) throws ServiceException;

    /**
     * obtener la Evaluacion nota dato por el identificador del expediente y el
     * identificador de la evaluación.
     *
     * @param idExpedienteEvalJustec the ApartadoJustecDatoDTO
     * @param idEvaluacionJustec the ApartadoJustecDatoDTO
     * @return List<EvaluacionJustecNotaDatoDTO> the EvaluacionJustecNotaDatoDTO
     * @throws ServiceException the service exception
     */
    public List<EvaluacionJustecNotaDatoDTO> obtenerEvaluacionJustecNotaDatoPorIdEvalIdExpEvalJustec(
            Integer idExpedienteEvalJustec, Integer idEvaluacionJustec) throws ServiceException;

}
