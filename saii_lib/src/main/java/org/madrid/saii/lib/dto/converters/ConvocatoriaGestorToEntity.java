package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaGestor
 *
 * @author nttdata
 */
public enum ConvocatoriaGestorToEntity
        implements Function<ConvocatoriaGestorDTO, ConvocatoriaGestor> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaGestor apply(ConvocatoriaGestorDTO dto) {

        ConvocatoriaGestor entity = null;

        if (dto != null) {

            entity = new ConvocatoriaGestor();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
