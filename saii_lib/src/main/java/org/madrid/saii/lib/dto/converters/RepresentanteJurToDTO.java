package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for RepresentanteJur
 *
 * @author everis
 */
public enum RepresentanteJurToDTO implements Function<RepresentanteJur, RepresentanteJurDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RepresentanteJurDTO apply(RepresentanteJur entity) {

        RepresentanteJurDTO dto = new RepresentanteJurDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

            dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            dto.setEntidadDTO(EntidadToDTO.INSTANCE.apply(entity.getEntidad()));
            dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));

        }

        return dto;
    }
}
