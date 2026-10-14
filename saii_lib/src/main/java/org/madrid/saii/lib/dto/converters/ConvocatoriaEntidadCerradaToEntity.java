package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaEntidadCerradaToEntity implements Function<ConvocatoriaEntidadCerradaDTO, ConvocatoriaEntidadCerrada> {

    /**
    *
    */
    INSTANCE;
    
    /**
    *
    */
    @Override
    public ConvocatoriaEntidadCerrada apply(ConvocatoriaEntidadCerradaDTO dto) {

        ConvocatoriaEntidadCerrada entity = null;

        if (dto != null) {

            entity = new ConvocatoriaEntidadCerrada();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getConvocatoriaDTO() != null) {
                entity.setConvocatoria(
                        ConvocatoriaToEntity.INSTANCE.apply(dto.getConvocatoriaDTO()));
            }

            if (dto.getEntidadCerrada() != null) {
                entity.setEntidadCerrada(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEntidadCerrada()));
            }

        }

        return entity;

    }
    
}
