package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for NotificacionCiudadano
 *
 * @author everis
 */
public enum NotificacionCiudadanoToDTO
        implements Function<NotificacionCiudadano, NotificacionCiudadanoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO apply(NotificacionCiudadano entity) {

        NotificacionCiudadanoDTO dto = new NotificacionCiudadanoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getAsunto() != null) {
                dto.setAsuntoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getAsunto()));
            }

            if (entity.getEstado() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }

            if (entity.getExpediente() != null && entity.getExpediente().getIdExpediente() != null) {
                if (dto.getExpedienteDTO() == null) {
                    dto.setExpedienteDTO(new ExpedienteDTO());
                }
                dto.getExpedienteDTO().setIdExpediente(entity.getExpediente().getIdExpediente());
            }

        }

        return dto;
    }
}
