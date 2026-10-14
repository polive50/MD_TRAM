package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ApartadoDato;
import org.madrid.saii.lib.dto.ApartadoDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ApartadoDato
 *
 * @author nttdata
 */
public enum ApartadoDatoToDTO implements Function<ApartadoDato, ApartadoDatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ApartadoDatoDTO apply(ApartadoDato entity) {

        ApartadoDatoDTO dto = new ApartadoDatoDTO();

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
    private void replaceNull(ApartadoDatoDTO dto, ApartadoDato entity) {

        if (StringUtils.isBlank(entity.getDsComentario())) {
            dto.setDsComentario(StringUtils.EMPTY);
        }

    }
}
