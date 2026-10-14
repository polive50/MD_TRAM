package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaSubmod
 *
 * @author nttdata
 */
public enum ConvocatoriaSubmodToDTO implements Function<ConvocatoriaSubmod, ConvocatoriaSubmodDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaSubmodDTO apply(ConvocatoriaSubmod entity) {

        ConvocatoriaSubmodDTO dto = new ConvocatoriaSubmodDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
