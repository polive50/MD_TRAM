package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for NotificacionCiudadano
 *
 * @author everis
 */
public enum NotificacionCiudadanoToEntity
        implements Function<NotificacionCiudadanoDTO, NotificacionCiudadano> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NotificacionCiudadano apply(NotificacionCiudadanoDTO dto) {

        NotificacionCiudadano entity = null;

        if (dto != null) {

            entity = new NotificacionCiudadano();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getAsuntoDTO() != null && dto.getAsuntoDTO().getIdValor() != null) {
                entity.setAsunto(CatalogosValoresToEntity.INSTANCE.apply(dto.getAsuntoDTO()));
            }

            if (dto.getEstadoDTO() != null && dto.getEstadoDTO().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

            if (dto.getExpedienteDTO() != null && dto.getExpedienteDTO().getIdExpediente() != null) {
                Expediente expAux = new Expediente();
                expAux.setIdExpediente(dto.getExpedienteDTO().getIdExpediente());
                entity.setExpediente(expAux);
            }

        }

        return entity;
    }
}
