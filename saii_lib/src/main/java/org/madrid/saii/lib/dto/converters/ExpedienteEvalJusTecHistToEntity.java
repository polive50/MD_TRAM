package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteEvalJusTec
 *
 * @author quental
 */
public enum ExpedienteEvalJusTecHistToEntity implements Function<ExpedienteEvalJusTecHistDTO, ExpedienteEvalJusTecHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEvalJusTecHist apply(ExpedienteEvalJusTecHistDTO dto) {

        ExpedienteEvalJusTecHist entity = null;

        if (dto != null) {

            entity = new ExpedienteEvalJusTecHist();

            BeanUtils.copyProperties(dto, entity);
            
            if (Objects.nonNull(dto.getExpedienteEvalJusTecDTO())) {
                entity.setExpedienteEvalJusTec(ExpedienteEvalJusTecToEntity.INSTANCE.apply(dto.getExpedienteEvalJusTecDTO()));
            }

            if (Objects.nonNull(dto.getInteresadoDTO())) {
                entity.setPersona(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }
            
            if (Objects.nonNull(dto.getExpedienteJusTecDTO())) {
                entity.setExpedienteJusTec(ExpedienteJusTecToEntity.INSTANCE.apply(dto.getExpedienteJusTecDTO()));
            }
            
            if (Objects.nonNull(dto.getEstadoDTO())) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

        }

        return entity;
    }
}
