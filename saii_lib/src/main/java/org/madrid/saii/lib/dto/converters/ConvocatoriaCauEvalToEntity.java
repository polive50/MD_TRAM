package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCauEval
 *
 * @author nttdata
 */
public enum ConvocatoriaCauEvalToEntity
        implements Function<ConvocatoriaCauEvalDTO, ConvocatoriaCauEval> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCauEval apply(ConvocatoriaCauEvalDTO dto) {

        ConvocatoriaCauEval entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCauEval();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getCausaEvalDTO())) {
                entity.setCausaEval(CatalogosValoresToEntity.INSTANCE.apply(dto.getCausaEvalDTO()));
            }

        }

        return entity;
    }
}
