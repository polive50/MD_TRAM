package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.ApartadoDatoDAO;
import org.madrid.saii.lib.domain.ApartadoDato;
import org.madrid.saii.lib.dto.InformeEvaluacionApartadoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ApartadoDato
 *
 * @author nttdata
 */
public interface ApartadoDatoService extends BaseService<ApartadoDato, Integer, ApartadoDatoDAO> {

    /**
     * Obtener apartados dato por el identificador del expediente de evaluación.
     *
     * @param idExpedienteEval
     * @return InformeEvaluacionApartadoDTO
     * @throws ServiceException the service exception
     */
    InformeEvaluacionApartadoDTO obtenerApartadosDatoPorIdExpedienteEval(Integer idExpedienteEval)
            throws ServiceException;

    /**
     * Guarda una lista apartadoDato.
     *
     * @param InformeEvaluacionApartadoDTO the apartadoDato DTO
     * @return ApartadosDTO
     * @throws ServiceException the service exception
     */
    InformeEvaluacionApartadoDTO guardarListaApartadoDato(InformeEvaluacionApartadoDTO apartadosDTO,
            String cdUsuario) throws ServiceException;

}
