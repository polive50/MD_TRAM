package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaAnualidadToEntity
        implements Function<ConvocatoriaAnualidadDTO, ConvocatoriaAnualidad> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAnualidad apply(ConvocatoriaAnualidadDTO dto) {

        ConvocatoriaAnualidad entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaAnualidad();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaAnualidadDTO())) {
                entity.setTipoConvocatoriaAnualidad(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaAnualidadDTO()));
            }

        }

        return entity;
    }

}
