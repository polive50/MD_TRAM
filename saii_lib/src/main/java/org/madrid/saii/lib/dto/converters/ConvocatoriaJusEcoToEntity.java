package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusEcoToEntity
        implements Function<ConvocatoriaJusEcoDTO, ConvocatoriaJusEco> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusEco apply(ConvocatoriaJusEcoDTO dto) {

        ConvocatoriaJusEco entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaJusEco();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaJusEcoDTO())) {
                entity.setTipoConvocatoriaJusEco(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaJusEcoDTO()));
            }

        }

        return entity;
    }

}
