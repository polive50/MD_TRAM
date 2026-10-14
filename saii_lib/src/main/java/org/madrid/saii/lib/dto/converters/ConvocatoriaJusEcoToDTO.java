package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusEcoToDTO implements Function<ConvocatoriaJusEco, ConvocatoriaJusEcoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusEcoDTO apply(ConvocatoriaJusEco entity) {

        ConvocatoriaJusEcoDTO dto = new ConvocatoriaJusEcoDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoConvocatoriaJusEco())) {
                dto.setTipoConvocatoriaJusEcoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoConvocatoriaJusEco()));
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
    private void replaceNull(ConvocatoriaJusEcoDTO dto, ConvocatoriaJusEco entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
