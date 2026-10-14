package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaDoc
 *
 * @author everis
 */
public enum ExpedienteDocEvalToEntity implements Function<ExpedienteDocEvalDTO, ExpedienteDocEval> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocEval apply(ExpedienteDocEvalDTO dto) {

        ExpedienteDocEval entity = null;

        if (dto != null) {

            entity = new ExpedienteDocEval();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdExpedienteEval() != null && dto.getIdExpedienteEval().getIdExpedienteEval() != null) {
                ExpedienteEval expEval = new ExpedienteEval();
                expEval.setIdExpedienteEval(dto.getIdExpedienteEval().getIdExpedienteEval());
                entity.setIdExpedienteEval(expEval);
            }
            if (dto.getIdTipoDocumento().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoDocumento()));
            }
           

        }

        return entity;
    }
}
