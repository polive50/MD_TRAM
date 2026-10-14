package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteEval
 *
 * @author nttdata
 */
public enum ExpedienteEvalToEntity implements Function<ExpedienteEvalDTO, ExpedienteEval> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEval apply(ExpedienteEvalDTO dto) {

        ExpedienteEval entity = null;

        if (dto != null) {

            entity = new ExpedienteEval();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getCausaDTO() != null) {
                entity.setCausa(CatalogosValoresToEntity.INSTANCE.apply(dto.getCausaDTO()));
            }

            if (Objects.nonNull(dto.getPersonaDTO())) {
                entity.setPersona(InteresadoToEntity.INSTANCE.apply(dto.getPersonaDTO()));
            }
            if (Objects.nonNull(dto.getEstadoDTO())
                    && Objects.nonNull(dto.getEstadoDTO().getIdValor())) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }
            if(Objects.nonNull(dto.getMotivoRechazo()) && dto.getMotivoRechazo().getIdValor() != null) {
                entity.setMotivoRechazo(CatalogosValoresToEntity.INSTANCE.apply(dto.getMotivoRechazo()));
            }
        }

        return entity;
    }
}
