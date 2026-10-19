package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for NotificacionCiudadanoDoc
 *
 * @author everis
 */
public enum NotificacionCiudadanoDocToEntity
        implements Function<NotificacionCiudadanoDocDTO, NotificacionCiudadanoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDoc apply(NotificacionCiudadanoDocDTO dto) {

        NotificacionCiudadanoDoc entity = null;

        if (dto != null) {

            entity = new NotificacionCiudadanoDoc();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getNotificacionCiudadanoDTO() != null
                    && dto.getNotificacionCiudadanoDTO().getIdNotificacion() != null) {
                NotificacionCiudadano notifAux = new NotificacionCiudadano();
                notifAux.setIdNotificacion(dto.getNotificacionCiudadanoDTO().getIdNotificacion());
                entity.setNotificacionCiudadano(notifAux);
            }

            if (dto.getExpedienteDocDTO() != null
                    && dto.getExpedienteDocDTO().getIdExpedienteDoc() != null) {
                ExpedienteDoc expedienteDocAux = new ExpedienteDoc();
                expedienteDocAux.setIdExpedienteDoc(dto.getExpedienteDocDTO().getIdExpedienteDoc());
                entity.setExpedienteDoc(expedienteDocAux);
            }

        }

        return entity;
    }
}
