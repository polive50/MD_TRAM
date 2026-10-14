package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.dto.AlegacionDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Alegacion
 *
 * @author everis
 */
public enum AlegacionToDTO implements Function<Alegacion, AlegacionDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public AlegacionDTO apply(Alegacion entity) {

        AlegacionDTO dto = new AlegacionDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getTipo() != null && entity.getTipo().getIdValor() != null) {
                dto.setTipoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipo()));
            }
            if (entity.getImagenLogo() != null) {
                dto.setImagenLogoDTO(ImagenToDTO.INSTANCE.apply(entity.getImagenLogo()));
            }
        }

        return dto;
    }
}
