package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusEcoHistToEntity
        implements Function<ConvocatoriaJusEcoHistDTO, ConvocatoriaJusEcoHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusEcoHist apply(ConvocatoriaJusEcoHistDTO dto) {

        ConvocatoriaJusEcoHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaJusEcoHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaJusEcoDTO())) {
                entity.setTipoConvocatoriaJusEco(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaJusEcoDTO()));
            }

        }

        return entity;
    }

}
