package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RevisionInfEvaJustecHist;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for RevisionInfEvaJustecHist
 *
 * @author quental
 */
public enum RevisionInfEvaJustecHistToDTO implements Function<RevisionInfEvaJustecHist, RevisionInfEvaJustecHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RevisionInfEvaJustecHistDTO apply(RevisionInfEvaJustecHist entity) {

        RevisionInfEvaJustecHistDTO dto = new RevisionInfEvaJustecHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getRevisionInfEvaJustec() != null) {
                dto.setRevisionInfEvaJustecDTO(RevisionInfEvaJustecToDTO.INSTANCE.apply(entity.getRevisionInfEvaJustec()));
            }

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
