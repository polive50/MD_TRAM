package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteReq
 *
 * @author everis
 */
public enum ExpedienteReqToDTO implements Function<ExpedienteReq, ExpedienteReqDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteReqDTO apply(ExpedienteReq entity) {

        ExpedienteReqDTO dto = new ExpedienteReqDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setItCumpleMod(entity.getItCumple() == null ? "" : entity.getItCumple());
            dto.setTlObservacionesMod(
                    entity.getTlObservaciones() == null ? "" : entity.getTlObservaciones());

            if (entity.getRequisito() != null) {
                dto.setRequisitoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getRequisito()));
            }

            if (entity.getDocumentoPtfr() != null
                    && entity.getDocumentoPtfr().getIdDocumentoPtfr() != null) {
                dto.setDocumentoPtfrDTO(
                        DocumentoPtfrToDTO.INSTANCE.apply(entity.getDocumentoPtfr()));
            }
            
            if (entity.getExpediente() != null && entity.getExpediente().getIdExpediente() != null) {
                if (dto.getExpedienteDTO() == null) {
                    dto.setExpedienteDTO(new ExpedienteDTO());
                }
                dto.getExpedienteDTO().setIdExpediente(entity.getExpediente().getIdExpediente());
            }

        }

        return dto;
    }
}
