package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for RevisionInfEvaJustec
 *
 * @author quental
 */
public enum RevisionInfEvaJustecToEntity implements Function<RevisionInfEvaJustecDTO,RevisionInfEvaJustec > {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RevisionInfEvaJustec apply(RevisionInfEvaJustecDTO dto) {

        RevisionInfEvaJustec entity=null;

        if (dto != null) {
            entity = new RevisionInfEvaJustec();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getExpedienteDTO() != null) {
                entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));
            }

            if (dto.getExpedienteJusTecDTO() != null) {
                entity.setExpedienteJusTec(ExpedienteJusTecToEntity.INSTANCE.apply(dto.getExpedienteJusTecDTO()));
            }

            if (dto.getExpedienteDocDTO() != null) {
                entity.setExpedienteDoc(ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            }

            if (dto.getIdEstadoDTO() != null) {
                entity.setIdEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoDTO()));
            }
        }

        return entity;
    }
}
