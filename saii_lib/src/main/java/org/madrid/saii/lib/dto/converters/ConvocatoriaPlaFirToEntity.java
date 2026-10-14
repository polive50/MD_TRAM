package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaPlaFir
 *
 * @author everis
 */
public enum ConvocatoriaPlaFirToEntity
        implements Function<ConvocatoriaPlaFirDTO, ConvocatoriaPlaFir> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaFir apply(ConvocatoriaPlaFirDTO dto) {

        ConvocatoriaPlaFir entity = null;

        if (dto != null) {

            entity = new ConvocatoriaPlaFir();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
