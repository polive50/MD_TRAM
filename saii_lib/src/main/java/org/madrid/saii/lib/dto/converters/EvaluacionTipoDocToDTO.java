package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for EvaluacionTipoDoc
 *
 * @author everis
 */
public enum EvaluacionTipoDocToDTO implements Function<EvaluacionTipoDoc, EvaluacionTipoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionTipoDocDTO apply(EvaluacionTipoDoc entity) {

        EvaluacionTipoDocDTO dto = new EvaluacionTipoDocDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            if (entity.getIdTipoDocumento() != null && entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setIdTipoDocumento(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }
            
            dto.setItObligatorio(Constantes.AfirmacionNegacion.S.equals(entity.getItObligatorio()));
        }

        return dto;
    }
}
