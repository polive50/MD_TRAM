package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaSubareaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaAreaSubarea
 *
 * @author quental
 */
public enum ConvocatoriaAreaSubareaToEntity implements Function<ConvocatoriaAreaSubareaDTO, ConvocatoriaAreaSubarea> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAreaSubarea apply(ConvocatoriaAreaSubareaDTO dto) {

        ConvocatoriaAreaSubarea entity = null;

        if (dto != null) {

            entity = new ConvocatoriaAreaSubarea();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getSubareaDTO() != null) {
                entity.setSubarea(CatalogosValoresToEntity.INSTANCE.apply(dto.getSubareaDTO()));
            }

        }

        return entity;
    }
}
