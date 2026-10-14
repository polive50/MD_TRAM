package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.springframework.beans.BeanUtils;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for InformeEvalDoc
 *
 * @author quental
 */
public enum InformeEvalDocToDTO implements Function<InformeEvalDoc, InformeEvalDocDTO> {

    INSTANCE;

    @Override
    public InformeEvalDocDTO apply(InformeEvalDoc entity) {

        InformeEvalDocDTO dto = new InformeEvalDocDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdExpedienteEval() != null) {
                dto.setIdExpedienteEval(entity.getIdExpedienteEval().getIdExpedienteEval());
            }

            if (entity.getIdInforme() != null) {
                dto.setIdInforme(InformeToDTO.INSTANCE.apply(entity.getIdInforme()));
            }

            if (entity.getIdEstado() != null) {
                dto.setIdEstado(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
            }

            if (entity.getIdCausa() != null) {
                dto.setIdCausa(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdCausa()));
            }

            if (entity.getIdExpedienteEval() != null) {
                dto.setIdExpedienteEvalDTO(ExpedienteEvalToDTO.INSTANCE.apply(entity.getIdExpedienteEval()));
            }
        }

        return dto;
    }
}
