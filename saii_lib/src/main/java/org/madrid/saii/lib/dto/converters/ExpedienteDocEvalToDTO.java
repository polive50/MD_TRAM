package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteDocEval
 *
 * @author everis
 */
public enum ExpedienteDocEvalToDTO implements Function<ExpedienteDocEval, ExpedienteDocEvalDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocEvalDTO apply(ExpedienteDocEval entity) {

        ExpedienteDocEvalDTO dto = new ExpedienteDocEvalDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdExpedienteEval() != null && entity.getIdExpedienteEval().getIdExpedienteEval()!= null) {
                ExpedienteEvalDTO expEvalDTO= new ExpedienteEvalDTO();
                expEvalDTO.setIdExpedienteEval(entity.getIdExpedienteEval().getIdExpedienteEval());
                dto.setIdExpedienteEval(expEvalDTO);
            }
            if (entity.getIdTipoDocumento() != null && entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setIdTipoDocumento(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }
            

        }

        return dto;
    }
}
