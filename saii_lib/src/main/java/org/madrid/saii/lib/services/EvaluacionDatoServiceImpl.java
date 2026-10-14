package org.madrid.saii.lib.services;

import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.EvaluacionDatoDAO;
import org.madrid.saii.lib.dto.EvaluacionDatoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio Notificacion
 *
 * @author nttdata
 */
@Service("evaluacionDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionDatoServiceImpl
        extends BaseServiceImpl<EvaluacionDatoDTO, Integer, EvaluacionDatoDAO>
        implements EvaluacionDatoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionDatoDAO
     */
    @Autowired
    public EvaluacionDatoServiceImpl(
            @Qualifier("evaluacionDatoDAO") EvaluacionDatoDAO evaluacionDatoDAO) {
        super();
        this.setDao(evaluacionDatoDAO);
    }

    /**
     * 
     */
    @Override
    public List<EvaluacionDatoDTO> obtenerEvaluacionDato(Integer idExpediente)
            throws ServiceException {
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return this.getDao().obtenerEvaluacionDato(idExpediente);
    }

}
