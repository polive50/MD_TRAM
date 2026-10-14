package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for RepresentanteJur
 *
 * @author everis
 */
public enum RepresentanteJurToEntity implements Function<RepresentanteJurDTO, RepresentanteJur> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public RepresentanteJur apply(RepresentanteJurDTO dto) {

        RepresentanteJur entity = new RepresentanteJur();

        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);

            entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            entity.setEntidad(EntidadToEntity.INSTANCE.apply(dto.getEntidadDTO()));
            entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));

        }

        return entity;
    }
}
