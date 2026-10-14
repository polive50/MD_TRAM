package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ExpedienteReqHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteReqHist
 *
 * @author everis
 */
public enum ExpedienteReqHistToDTO implements Function<ExpedienteReqHist, ExpedienteReqHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteReqHistDTO apply(ExpedienteReqHist entity) {

        ExpedienteReqHistDTO dto = new ExpedienteReqHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setItCumpleMod(entity.getItCumple() == null ? "" : entity.getItCumple());
            dto.setTlObservacionesMod(
                    entity.getTlObservaciones() == null ? "" : entity.getTlObservaciones());
            
            dto.setTlObservacionesCiudadano( entity.getTlObservacionesCiudadano() == null ? "" : entity.getTlObservacionesCiudadano());

            if (entity.getRequisito() != null && entity.getRequisito().getIdValor() != null) {
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
