package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.PersonaTitDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for PersonaTit
 *
 * @author nttdata
 */
public enum PersonaTitToDTO implements Function<PersonaTit, PersonaTitDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaTitDTO apply(PersonaTit entity) {

        PersonaTitDTO dto = new PersonaTitDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getTitulacion() != null && entity.getTitulacion().getIdValor() != null) {
                dto.setTitulacionDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTitulacion()));
            }

        }

        return dto;
    }
}
