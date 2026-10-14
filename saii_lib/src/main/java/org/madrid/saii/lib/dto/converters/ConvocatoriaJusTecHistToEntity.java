package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusTecHistToEntity
        implements Function<ConvocatoriaJusTecHistDTO, ConvocatoriaJusTecHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusTecHist apply(ConvocatoriaJusTecHistDTO dto) {

        ConvocatoriaJusTecHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ConvocatoriaJusTecHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoConvocatoriaJusTecDTO())) {
                entity.setTipoConvocatoriaJusTec(CatalogosValoresToEntity.INSTANCE
                        .apply(dto.getTipoConvocatoriaJusTecDTO()));
            }

        }

        return entity;
    }

}
