package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteDocEvalHist
 *
 * @author everis
 */
public enum ExpedienteDocEvalHistToEntity implements Function<ExpedienteDocEvalHistDTO, ExpedienteDocEvalHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocEvalHist apply(ExpedienteDocEvalHistDTO dto) {

        ExpedienteDocEvalHist entity = null;

        if (dto != null) {

            entity = new ExpedienteDocEvalHist();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdExpedienteEval() != null && dto.getIdExpedienteEval().getIdExpedienteEval() != null) {
                entity.getIdExpedienteEval().setIdExpedienteEval(dto.getIdExpedienteEval().getIdExpedienteEval());
            }
            if (dto.getIdTipoDocumento().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoDocumento()));
            }
           
            if (dto.getExpedienteDocEval() != null && dto.getExpedienteDocEval().getIdDocumento() != null) {
                entity.getExpedienteDocEval().setIdDocumento(dto.getExpedienteDocEval().getIdDocumento());
            }
        }

        return entity;
    }
}
