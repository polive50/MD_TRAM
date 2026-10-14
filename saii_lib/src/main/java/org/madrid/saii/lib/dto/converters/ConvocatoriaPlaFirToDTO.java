package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaPlaFir
 *
 * @author everis
 */
public enum ConvocatoriaPlaFirToDTO implements Function<ConvocatoriaPlaFir, ConvocatoriaPlaFirDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaFirDTO apply(ConvocatoriaPlaFir entity) {

        ConvocatoriaPlaFirDTO dto = new ConvocatoriaPlaFirDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
