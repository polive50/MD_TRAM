package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Imagen
 *
 * @author nttdata
 */
public enum ImagenToDTO implements Function<Imagen, ImagenDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ImagenDTO apply(Imagen entity) {

        ImagenDTO dto = new ImagenDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getFondoEur())
                    && Objects.nonNull(entity.getFondoEur().getIdValor())) {

                dto.setFondoEurDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getFondoEur()));
            }

        }

        return dto;
    }

}
