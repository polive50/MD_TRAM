package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaEvalJustec;
import org.madrid.saii.lib.dto.ConvocatoriaEvalJustecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaEvalJustec
 *
 * @author nttdata
 */
public enum ConvocatoriaEvalJustecToDTO
        implements Function<ConvocatoriaEvalJustec, ConvocatoriaEvalJustecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEvalJustecDTO apply(ConvocatoriaEvalJustec entity) {

        ConvocatoriaEvalJustecDTO dto = new ConvocatoriaEvalJustecDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getEvaluador())) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getEvaluador()));
            }
        }

        return dto;
    }

}
