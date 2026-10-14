package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaFechaTram;
import org.madrid.saii.lib.dto.ConvocatoriaFechaTramDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaFechaTram
 *
 * @author everis
 */
public enum ConvocatoriaFechaTramToDTO implements Function<ConvocatoriaFechaTram, ConvocatoriaFechaTramDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaFechaTramDTO apply(ConvocatoriaFechaTram entity) {

        ConvocatoriaFechaTramDTO dto = new ConvocatoriaFechaTramDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setIdConvocatoriaFechaTram(entity.getIdConvocatoriaFechaTram());
            dto.setDsDescripcion(entity.getDsDescripcion());
            dto.setFcFechaTramite(entity.getFcFechaTramite());

            if (entity.getIdConvocatoria() != null) {
                dto.setIdConvocatoria(entity.getIdConvocatoria());
            }

        }

        return dto;
    }
}
