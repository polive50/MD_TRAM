package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusTecToEntity
        implements Function<ConvocatoriaJusTecDTO, ConvocatoriaJusTec> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusTec apply(ConvocatoriaJusTecDTO dto) {

        ConvocatoriaJusTec entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaJusTec();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaJusTecDTO())) {
                entity.setTipoConvocatoriaJusTec(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaJusTecDTO()));
            }

        }

        return entity;
    }

}
