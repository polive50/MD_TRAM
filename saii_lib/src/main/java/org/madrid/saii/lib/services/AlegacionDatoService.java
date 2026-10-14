package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.AlegacionDatoDAO;
import org.madrid.saii.lib.domain.AlegacionDato;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionDatoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio AlegacionDato
 *
 * @author nttdata
 */
public interface AlegacionDatoService
        extends BaseService<AlegacionDato, Integer, AlegacionDatoDAO> {

    /**
     * Método para listar las AlegacionDato por expediente
     *
     * @param idExpediente
     * @return List<AlegacionDatoDTO>
     * @throws ServiceException
     */
    List<AlegacionDatoDTO> obtenerPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Método para guardas los datos de alegacion
     *
     * @param evaluacionDatoDTO
     * @param cdNumIden
     * @return List<AlegacionDatoDTO>
     * @throws ServiceException
     */
    List<AlegacionDatoDTO> guardarDatosAlegacion(EvaluacionDatoDTO evaluacionDatoDTO,
            String cdNumIden) throws ServiceException;

}
