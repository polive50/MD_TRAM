package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaAnualidadToDTO implements Function<ConvocatoriaAnualidad, ConvocatoriaAnualidadDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAnualidadDTO apply(ConvocatoriaAnualidad entity) {

        ConvocatoriaAnualidadDTO dto = new ConvocatoriaAnualidadDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoConvocatoriaAnualidad())) {
                dto.setTipoConvocatoriaAnualidadDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoConvocatoriaAnualidad()));
            }

        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ConvocatoriaAnualidadDTO dto, ConvocatoriaAnualidad entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
