package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusTecToDTO implements Function<ConvocatoriaJusTec, ConvocatoriaJusTecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusTecDTO apply(ConvocatoriaJusTec entity) {

        ConvocatoriaJusTecDTO dto = new ConvocatoriaJusTecDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoConvocatoriaJusTec())) {
                dto.setTipoConvocatoriaJusTecDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoConvocatoriaJusTec()));
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
    private void replaceNull(ConvocatoriaJusTecDTO dto, ConvocatoriaJusTec entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
