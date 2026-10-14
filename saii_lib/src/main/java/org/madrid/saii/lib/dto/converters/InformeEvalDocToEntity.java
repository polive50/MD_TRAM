package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for InformeEvalDoc
 *
 * @author quental
 */
public enum InformeEvalDocToEntity implements Function<InformeEvalDocDTO, InformeEvalDoc> {

    INSTANCE;

    @Override
    public InformeEvalDoc apply(InformeEvalDocDTO dto) {

        InformeEvalDoc entity = null;

        if (dto != null) {
            entity = new InformeEvalDoc();
            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdExpedienteEval() != null) {
                ExpedienteEval expedienteEval = new ExpedienteEval();
                expedienteEval.setIdExpedienteEval(dto.getIdExpedienteEval());
                entity.setIdExpedienteEval(expedienteEval);
            }

            if (dto.getIdInforme() != null && dto.getIdInforme().getIdInforme() != null) {
                entity.setIdInforme(InformeToEntity.INSTANCE.apply(dto.getIdInforme()));
            }

            if (dto.getIdEstado() != null && dto.getIdEstado().getIdValor() != null) {
                entity.setIdEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstado()));
            }

            if (dto.getIdCausa() != null && dto.getIdCausa().getIdValor() != null) {
                entity.setIdCausa(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdCausa()));
            }
        }

        return entity;
    }
}
