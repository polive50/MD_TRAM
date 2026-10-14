package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaArea
 *
 * @author nttdata
 */
public enum ConvocatoriaAreaToEntity implements Function<ConvocatoriaAreaDTO, ConvocatoriaArea> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaArea apply(ConvocatoriaAreaDTO dto) {

        ConvocatoriaArea entity = null;

        if (dto != null) {

            entity = new ConvocatoriaArea();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getAreaDTO() != null) {
                entity.setArea(CatalogosValoresToEntity.INSTANCE.apply(dto.getAreaDTO()));
            }

        }

        return entity;
    }
}
