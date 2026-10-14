package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.dto.AlegacionDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Alegacion
 *
 * @author everis
 */
public enum AlegacionToEntity implements Function<AlegacionDTO, Alegacion> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Alegacion apply(AlegacionDTO dto) {

        Alegacion entity = null;

        if (dto != null) {

            entity = new Alegacion();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTipoDTO() != null && dto.getTipoDTO().getIdValor() != null) {
                entity.setTipo(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDTO()));
            }

            if (dto.getImagenLogoDTO() != null && dto.getImagenLogoDTO().getIdImagen() != null) {
                entity.setImagenLogo(ImagenToEntity.INSTANCE.apply(dto.getImagenLogoDTO()));
            }

        }

        return entity;
    }
}
