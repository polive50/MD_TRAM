package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ApartadoJustecDato;
import org.madrid.saii.lib.dto.ApartadoJustecDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ApartadoJustecDato
 *
 * @author nttdata
 */
public enum ApartadoJustecDatoToDTO implements Function<ApartadoJustecDato, ApartadoJustecDatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ApartadoJustecDatoDTO apply(ApartadoJustecDato entity) {

        ApartadoJustecDatoDTO dto = new ApartadoJustecDatoDTO();

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
    private void replaceNull(ApartadoJustecDatoDTO dto, ApartadoJustecDato entity) {

        if (StringUtils.isBlank(entity.getDsComentario())) {
            dto.setDsComentario(StringUtils.EMPTY);
        }

    }
}
