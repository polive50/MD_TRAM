package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Imagen
 *
 * @author nttdata
 */
public enum ImagenToEntity implements Function<ImagenDTO, Imagen> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Imagen apply(ImagenDTO dto) {

        Imagen entity = null;

        if (dto != null) {

            entity = new Imagen();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getFondoEurDTO())
                    && Objects.nonNull(dto.getFondoEurDTO().getIdValor())) {
                entity.setFondoEur(CatalogosValoresToEntity.INSTANCE.apply(dto.getFondoEurDTO()));
            }
        }

        return entity;
    }
}
