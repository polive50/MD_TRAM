package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for RevisionInfEvaJustec
 *
 * @author quental
 */
public enum RevisionInfEvaJustecToDTO implements Function<RevisionInfEvaJustec, RevisionInfEvaJustecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RevisionInfEvaJustecDTO apply(RevisionInfEvaJustec entity) {

        RevisionInfEvaJustecDTO dto = new RevisionInfEvaJustecDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getExpediente() != null) {
                dto.setExpedienteDTO(ExpedienteToDTO.INSTANCE.apply(entity.getExpediente()));
            }

            if (entity.getExpedienteJusTec() != null) {
                dto.setExpedienteJusTecDTO(ExpedienteJusTecToDTO.INSTANCE.apply(entity.getExpedienteJusTec()));
            }

            if (entity.getExpedienteDoc() != null) {
                dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            }

            if (entity.getIdEstado() != null) {
                dto.setIdEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
            }
        }

        return dto;
    }
}
