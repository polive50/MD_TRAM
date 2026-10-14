package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCoor
 *
 * @author nttdata
 */
public enum ConvocatoriaCoorToDTO implements Function<ConvocatoriaCoor, ConvocatoriaCoorDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCoorDTO apply(ConvocatoriaCoor entity) {

        ConvocatoriaCoorDTO dto = new ConvocatoriaCoorDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getInteresado() != null) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            }

            if (entity.getArea() != null) {
                dto.setAreaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getArea()));
            }
            
            if (entity.getTipoInforme() != null) {
                dto.setTipoInformeDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoInforme()));
            }
        }

        return dto;
    }
}
