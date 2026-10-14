package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.ApartadoJustecDatoDAO;
import org.madrid.saii.lib.domain.ApartadoJustecDato;
import org.madrid.saii.lib.dto.InformeEvaluacionApartadoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionJustecApartadoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ApartadoDato
 *
 * @author nttdata
 */
public interface ApartadoJustecDatoService extends BaseService<ApartadoJustecDato, Integer, ApartadoJustecDatoDAO> {

    /**
     * Obtener apartados dato por el identificador del expediente de evaluación.
     *
     * @param idExpedienteEval
     * @return InformeEvaluacionApartadoDTO
     * @throws ServiceException the service exception
     */
    InformeEvaluacionJustecApartadoDTO obtenerApartadosDatoPorIdExpedienteEvalJustec(Integer idExpedienteEval)
            throws ServiceException;

    /**
     * Guarda una lista apartadoDato.
     *
     * @param InformeEvaluacionApartadoDTO the apartadoJustecDato DTO
     * @return ApartadosDTO
     * @throws ServiceException the service exception
     */
    InformeEvaluacionJustecApartadoDTO guardarListaApartadoJustecDato(InformeEvaluacionJustecApartadoDTO apartadosDTO,
            String cdUsuario) throws ServiceException;

}
