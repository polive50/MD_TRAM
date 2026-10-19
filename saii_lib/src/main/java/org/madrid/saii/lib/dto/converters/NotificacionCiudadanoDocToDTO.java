package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for NotificacionCiudadanoDoc
 *
 * @author everis
 */
public enum NotificacionCiudadanoDocToDTO
        implements Function<NotificacionCiudadanoDoc, NotificacionCiudadanoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDocDTO apply(NotificacionCiudadanoDoc entity) {

        NotificacionCiudadanoDocDTO dto = new NotificacionCiudadanoDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getNotificacionCiudadano() != null
                    && entity.getNotificacionCiudadano().getIdNotificacion() != null) {
                if (dto.getNotificacionCiudadanoDTO() == null) {
                    dto.setNotificacionCiudadanoDTO(new NotificacionCiudadanoDTO());
                }
                dto.getNotificacionCiudadanoDTO()
                        .setIdNotificacion(entity.getNotificacionCiudadano().getIdNotificacion());
            }

            if (entity.getExpedienteDoc() != null
                    && entity.getExpedienteDoc().getIdExpedienteDoc() != null) {
                if (dto.getExpedienteDocDTO() == null) {
                    dto.setExpedienteDocDTO(new ExpedienteDocDTO());
                }
                dto.getExpedienteDocDTO()
                        .setIdExpedienteDoc(entity.getExpedienteDoc().getIdExpedienteDoc());
            }

        }

        return dto;
    }
}
