package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteReq
 *
 * @author everis
 */
public enum ExpedienteReqToEntity implements Function<ExpedienteReqDTO, ExpedienteReq> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteReq apply(ExpedienteReqDTO dto) {

        ExpedienteReq entity = null;

        if (dto != null) {

            entity = new ExpedienteReq();

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
