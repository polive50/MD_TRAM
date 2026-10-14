package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.dto.ConvocatoriaPlaVerDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaPlaVer
 *
 * @author everis
 */
public enum ConvocatoriaPlaVerToEntity
        implements Function<ConvocatoriaPlaVerDTO, ConvocatoriaPlaVer> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaVer apply(ConvocatoriaPlaVerDTO dto) {

        ConvocatoriaPlaVer entity = null;

        if (dto != null) {

            entity = new ConvocatoriaPlaVer();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
