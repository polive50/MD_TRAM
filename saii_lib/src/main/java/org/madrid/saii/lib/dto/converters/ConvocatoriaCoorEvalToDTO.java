package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.ConvocatoriaCoorEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCoorEval
 *
 * @author nttdata
 */
public enum ConvocatoriaCoorEvalToDTO
        implements Function<ConvocatoriaCoorEval, ConvocatoriaCoorEvalDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCoorEvalDTO apply(ConvocatoriaCoorEval entity) {

        ConvocatoriaCoorEvalDTO dto = new ConvocatoriaCoorEvalDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getEvaluador())) {
                dto.setEvaluadorDTO(InteresadoToDTO.INSTANCE.apply(entity.getEvaluador()));
            }
            
            if (entity.getArea() != null) {
                dto.setAreaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getArea()));
            }
            
            if (entity.getTipoInforme() != null) {
                dto.setTipoInformeDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoInforme()));
            }
        }

        return dto;
    }

}
