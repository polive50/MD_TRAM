package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaFechaTram;
import org.madrid.saii.lib.dto.ConvocatoriaFechaTramDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaFechaTram
 *
 * @author everis
 */
public enum ConvocatoriaFechaTramToEntity implements Function<ConvocatoriaFechaTramDTO, ConvocatoriaFechaTram> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaFechaTram apply(ConvocatoriaFechaTramDTO dto) {

        ConvocatoriaFechaTram entity = null;

        if (dto != null) {

            entity = new ConvocatoriaFechaTram();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdConvocatoriaFechaTram(dto.getIdConvocatoriaFechaTram());
            entity.setDsDescripcion(dto.getDsDescripcion());
            entity.setFcFechaTramite(dto.getFcFechaTramite());

            if (dto.getIdConvocatoria() != null) {
                entity.setIdConvocatoria(dto.getIdConvocatoria());
            }

        }

        return entity;
    }
}
