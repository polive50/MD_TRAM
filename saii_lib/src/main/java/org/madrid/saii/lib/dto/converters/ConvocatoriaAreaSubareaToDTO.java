package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaSubareaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaAreaSubarea
 *
 * @author quental
 */
public enum ConvocatoriaAreaSubareaToDTO implements Function<ConvocatoriaAreaSubarea, ConvocatoriaAreaSubareaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAreaSubareaDTO apply(ConvocatoriaAreaSubarea entity) {

        ConvocatoriaAreaSubareaDTO dto = new ConvocatoriaAreaSubareaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getSubarea() != null) {
                dto.setSubareaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getSubarea()));
            }

        }

        return dto;
    }
}
