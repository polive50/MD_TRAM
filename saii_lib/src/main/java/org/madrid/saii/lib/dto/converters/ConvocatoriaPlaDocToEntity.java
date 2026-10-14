package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaPlaDoc
 *
 * @author everis
 */
public enum ConvocatoriaPlaDocToEntity
        implements Function<ConvocatoriaPlaDocDTO, ConvocatoriaPlaDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaDoc apply(ConvocatoriaPlaDocDTO dto) {

        ConvocatoriaPlaDoc entity = null;

        if (dto != null) {

            entity = new ConvocatoriaPlaDoc();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
