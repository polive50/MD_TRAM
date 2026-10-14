package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCauEval
 *
 * @author nttdata
 */
public enum ConvocatoriaCauEvalToDTO
        implements Function<ConvocatoriaCauEval, ConvocatoriaCauEvalDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCauEvalDTO apply(ConvocatoriaCauEval entity) {

        ConvocatoriaCauEvalDTO dto = new ConvocatoriaCauEvalDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getCausaEval())) {
                dto.setCausaEvalDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getCausaEval()));
            }

        }

        return dto;
    }

}
