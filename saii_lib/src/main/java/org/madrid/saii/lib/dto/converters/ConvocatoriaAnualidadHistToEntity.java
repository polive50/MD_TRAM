package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaAnualidadHist;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaAnualidadHistToEntity
        implements Function<ConvocatoriaAnualidadHistDTO, ConvocatoriaAnualidadHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAnualidadHist apply(ConvocatoriaAnualidadHistDTO dto) {

        ConvocatoriaAnualidadHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaAnualidadHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaAnualidadDTO())) {
                entity.setTipoConvocatoriaAnualidad(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaAnualidadDTO()));
            }

        }

        return entity;
    }

}
