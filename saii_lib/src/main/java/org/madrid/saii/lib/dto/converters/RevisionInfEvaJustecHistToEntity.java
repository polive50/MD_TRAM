package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RevisionInfEvaJustecHist;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for RevisionInfEvaJustecHist
 *
 * @author quental
 */
public enum RevisionInfEvaJustecHistToEntity implements Function<RevisionInfEvaJustecHistDTO,RevisionInfEvaJustecHist > {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RevisionInfEvaJustecHist apply(RevisionInfEvaJustecHistDTO dto) {

        RevisionInfEvaJustecHist entity=null;

        if (dto != null) {
            entity = new RevisionInfEvaJustecHist();

            BeanUtils.copyProperties(dto, entity);
            
            if (dto.getRevisionInfEvaJustecDTO() != null) {
                entity.setRevisionInfEvaJustec(RevisionInfEvaJustecToEntity.INSTANCE.apply(dto.getRevisionInfEvaJustecDTO()));
            }

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
