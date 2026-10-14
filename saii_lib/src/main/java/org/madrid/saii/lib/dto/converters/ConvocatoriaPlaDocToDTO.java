package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaPlaDoc
 *
 * @author quental
 */
public enum ConvocatoriaPlaDocToDTO implements Function<ConvocatoriaPlaDoc, ConvocatoriaPlaDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaDocDTO apply(ConvocatoriaPlaDoc entity) {

        ConvocatoriaPlaDocDTO dto = new ConvocatoriaPlaDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
