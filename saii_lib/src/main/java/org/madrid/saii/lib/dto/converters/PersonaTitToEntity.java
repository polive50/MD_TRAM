package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.PersonaTitDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for PersonaTit
 *
 * @author nttdata
 */
public enum PersonaTitToEntity implements Function<PersonaTitDTO, PersonaTit> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaTit apply(PersonaTitDTO dto) {

        PersonaTit entity = null;

        if (dto != null) {

            entity = new PersonaTit();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTitulacionDTO() != null && dto.getTitulacionDTO().getIdValor() != null) {
                entity.setTitulacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTitulacionDTO()));
            }

        }

        return entity;
    }
}
