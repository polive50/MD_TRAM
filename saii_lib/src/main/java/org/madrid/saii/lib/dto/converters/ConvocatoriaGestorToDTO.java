package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaGestor
 *
 * @author nttdata
 */
public enum ConvocatoriaGestorToDTO implements Function<ConvocatoriaGestor, ConvocatoriaGestorDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaGestorDTO apply(ConvocatoriaGestor entity) {

        ConvocatoriaGestorDTO dto = new ConvocatoriaGestorDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ConvocatoriaGestorDTO dto, ConvocatoriaGestor entity) {

        if (StringUtils.isBlank(entity.getCdUsuario())) {
            dto.setCdUsuario(StringUtils.EMPTY);
        }

    }

}
