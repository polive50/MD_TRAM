package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteEvalJusTec
 *
 * @author quental
 */
public enum ExpedienteEvalJusTecToEntity implements Function<ExpedienteEvalJusTecDTO, ExpedienteEvalJusTec> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEvalJusTec apply(ExpedienteEvalJusTecDTO dto) {

        ExpedienteEvalJusTec entity = null;

        if (dto != null) {

            entity = new ExpedienteEvalJusTec();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getInteresadoDTO())) {
                entity.setPersona(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }
            
            if (Objects.nonNull(dto.getExpedienteJustecDTO())) {
                entity.setExpedienteJustec(ExpedienteJusTecToEntity.INSTANCE.apply(dto.getExpedienteJustecDTO()));
            }
            
            if (Objects.nonNull(dto.getEstadoDTO())) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

        }

        return entity;
    }
}
