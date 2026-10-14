package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Solicitud
 *
 * @author everis
 */
public enum SolicitudToEntity implements Function<SolicitudDTO, Solicitud> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Solicitud apply(SolicitudDTO dto) {

        Solicitud entity = null;

        if (dto != null) {

            entity = new Solicitud();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getConvocatoriaDTO() != null) {
                entity.setConvocatoria(
                        ConvocatoriaToEntity.INSTANCE.apply(dto.getConvocatoriaDTO()));
            }

            if (dto.getInteresadoDTO() != null) {
                entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }

            if (dto.getEstadoDTO() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

            if (dto.getTipoActorDTO() != null && dto.getTipoActorDTO().getIdValor() != null) {
                entity.setTipoActor(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoActorDTO()));
            }

            if (dto.getConvocatoriaSubmodDTO() != null
                    && dto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() != null) {
                entity.setConvocatoriaSubmod(
                        ConvocatoriaSubmodToEntity.INSTANCE.apply(dto.getConvocatoriaSubmodDTO()));
            }

            if (dto.getConvocatoriaAreaDTO() != null
                    && dto.getConvocatoriaAreaDTO().getIdConvocatoriaArea() != null) {
                entity.setConvocatoriaArea(
                        ConvocatoriaAreaToEntity.INSTANCE.apply(dto.getConvocatoriaAreaDTO()));
            }
            
            if (dto.getConvocatoriaAreaSubareaDTO() != null
                    && dto.getConvocatoriaAreaSubareaDTO().getIdConvocatoriaAreaSubarea() != null) {
                entity.setConvocatoriaAreaSubarea(
                        ConvocatoriaAreaSubareaToEntity.INSTANCE.apply(dto.getConvocatoriaAreaSubareaDTO()));
            }

            if (dto.getIdSolicitud() != null) {
                entity.setIdSolicitud(dto.getIdSolicitud());
            }

            entity.setItExpeCoor(
                    Boolean.TRUE.equals(dto.getItExpeCoor()) ? Constantes.AfirmacionNegacion.S
                            : Constantes.AfirmacionNegacion.N);
        }

        return entity;
    }
}
