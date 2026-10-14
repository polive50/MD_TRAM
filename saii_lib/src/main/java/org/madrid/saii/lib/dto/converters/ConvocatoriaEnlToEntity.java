package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.madrid.saii.lib.dto.ConvocatoriaEnlDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaEnl
 *
 * @author everis
 */
public enum ConvocatoriaEnlToEntity implements Function<ConvocatoriaEnlDTO, ConvocatoriaEnl> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEnl apply(ConvocatoriaEnlDTO dto) {

        ConvocatoriaEnl entity = null;

        if (dto != null) {

            entity = new ConvocatoriaEnl();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdEnlace(dto.getIdEnlace());
            entity.setDsDescripcion(dto.getDsDescripcion());
            entity.setTlEnlace(dto.getTlEnlace());

            if (dto.getIdConvocatoria() != null) {
                entity.setIdConvocatoria(dto.getIdConvocatoria());
            }

        }

        return entity;
    }
}
