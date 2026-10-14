package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.madrid.saii.lib.dto.ConvocatoriaEnlDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaEnl
 *
 * @author everis
 */
public enum ConvocatoriaEnlToDTO implements Function<ConvocatoriaEnl, ConvocatoriaEnlDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEnlDTO apply(ConvocatoriaEnl entity) {

        ConvocatoriaEnlDTO dto = new ConvocatoriaEnlDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setIdEnlace(entity.getIdEnlace());
            dto.setDsDescripcion(entity.getDsDescripcion());
            dto.setTlEnlace(entity.getTlEnlace());

            if (entity.getIdConvocatoria() != null) {
                dto.setIdConvocatoria(entity.getIdConvocatoria());
            }

        }

        return dto;
    }
}
