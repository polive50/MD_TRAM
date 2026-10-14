package org.madrid.saii.lib.dto.converters;


import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteDocEvalHist
 *
 * @author everis
 */
public enum ExpedienteDocEvalHistToDTO implements Function<ExpedienteDocEvalHist, ExpedienteDocEvalHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocEvalHistDTO apply(ExpedienteDocEvalHist entity) {

        ExpedienteDocEvalHistDTO dto = new ExpedienteDocEvalHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdExpedienteEval() != null && entity.getIdExpedienteEval().getIdExpedienteEval()!= null) {
                dto.getIdExpedienteEval().setIdExpedienteEval(entity.getIdExpedienteEval().getIdExpedienteEval());
            }
            if (entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setIdTipoDocumento(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }
            
            if (entity.getExpedienteDocEval() != null && entity.getExpedienteDocEval().getIdDocumento()!= null) {
                dto.getExpedienteDocEval().setIdDocumento(entity.getExpedienteDocEval().getIdDocumento());
            }

        }

        return dto;
    }
}
