package org.madrid.saii.lib.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.NotificacionDAO;
import org.madrid.saii.lib.dto.NotificacionDTO;
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
@Service("notificacionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class NotificacionServiceImpl extends
BaseServiceImpl<NotificacionDTO, Integer, NotificacionDAO> implements NotificacionService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param notificacionDAO
     */
    @Autowired
    public NotificacionServiceImpl(@Qualifier("notificacionDAO") NotificacionDAO notificacionDAO) {
        super();
        this.setDao(notificacionDAO);
    }

    /**
     *
     */
    @Override
    public Integer obtenerTotalNotificaciones(String identificador) throws ServiceException {
        if (StringUtils.isBlank(identificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<NotificacionDTO> lstCoor = this.getDao().obtenerNotificacionCoordinador(identificador);
        List<NotificacionDTO> lstEval = this.getDao().obtenerNotificacionCoorEval(Constantes.TipoCoorEval.EVALUADOR, identificador);
        List<NotificacionDTO> lstEvalJustec = this.getDao().obtenerNotificacionEvalJustec(identificador);

        return lstCoor.size() + lstEval.size() + lstEvalJustec.size();
    }

    /**
     *
     */
    @Override
    public List<NotificacionDTO> obtenerNotificacionCoorEval(String itCoorEval,
            String identificador) throws ServiceException {
        if (StringUtils.isBlank(itCoorEval) || StringUtils.isBlank(identificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return this.getDao().obtenerNotificacionCoorEval(itCoorEval, identificador);
    }

    /**
     *
     */
    @Override
    public List<NotificacionDTO> obtenerNotificacionCoordinador(String identificador) throws ServiceException {
        if (StringUtils.isBlank(identificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return this.getDao().obtenerNotificacionCoordinador(identificador);
    }

    @Override
    public List<NotificacionDTO> obtenerNotificacionEvalJustec(String identificador) throws ServiceException {
        if ( StringUtils.isBlank(identificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return this.getDao().obtenerNotificacionEvalJustec(identificador);
    }
}
