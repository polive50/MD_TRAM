package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaEvalJustec;
import org.madrid.saii.lib.dto.ConvocatoriaEvalJustecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaEvalJustec
 *
 * @author nttdata
 */
public enum ConvocatoriaEvalJustecToEntity
        implements Function<ConvocatoriaEvalJustecDTO, ConvocatoriaEvalJustec> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEvalJustec apply(ConvocatoriaEvalJustecDTO dto) {

        ConvocatoriaEvalJustec entity = null;

        if (dto != null) {

            entity = new ConvocatoriaEvalJustec();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getInteresadoDTO())) {
                entity.setEvaluador(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }

        }

        return entity;
    }
}
