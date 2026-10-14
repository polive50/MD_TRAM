package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.dto.ConvocatoriaPlaVerDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaPlaVer
 *
 * @author everis
 */
public enum ConvocatoriaPlaVerToDTO implements Function<ConvocatoriaPlaVer, ConvocatoriaPlaVerDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaVerDTO apply(ConvocatoriaPlaVer entity) {

        ConvocatoriaPlaVerDTO dto = new ConvocatoriaPlaVerDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
