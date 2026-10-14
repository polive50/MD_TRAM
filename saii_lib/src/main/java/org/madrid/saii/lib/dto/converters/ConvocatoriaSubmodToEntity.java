package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaSubmod
 *
 * @author nttdata
 */
public enum ConvocatoriaSubmodToEntity
        implements Function<ConvocatoriaSubmodDTO, ConvocatoriaSubmod> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaSubmod apply(ConvocatoriaSubmodDTO dto) {

        ConvocatoriaSubmod entity = null;

        if (dto != null) {

            entity = new ConvocatoriaSubmod();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
