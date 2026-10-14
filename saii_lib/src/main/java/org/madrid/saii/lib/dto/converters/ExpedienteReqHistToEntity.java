package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.madrid.saii.lib.dto.ExpedienteReqHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteReqHist
 *
 * @author everis
 */
public enum ExpedienteReqHistToEntity implements Function<ExpedienteReqHistDTO, ExpedienteReqHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteReqHist apply(ExpedienteReqHistDTO dto) {

        ExpedienteReqHist entity = null;

        if (dto != null) {

            entity = new ExpedienteReqHist();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getRequisitoDTO() != null) {
                entity.setRequisito(CatalogosValoresToEntity.INSTANCE.apply(dto.getRequisitoDTO()));
            }

            if (dto.getDocumentoPtfrDTO() != null
                    && dto.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null) {
                entity.setDocumentoPtfr(
                        DocumentoPtfrToEntity.INSTANCE.apply(dto.getDocumentoPtfrDTO()));
            }
            
            if (dto.getExpedienteDTO() != null) {
                entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));
            }

        }

        return entity;
    }
}
