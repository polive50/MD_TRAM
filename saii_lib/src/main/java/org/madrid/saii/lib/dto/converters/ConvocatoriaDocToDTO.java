package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaDoc
 *
 * @author everis
 */
public enum ConvocatoriaDocToDTO implements Function<ConvocatoriaDoc, ConvocatoriaDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaDocDTO apply(ConvocatoriaDoc entity) {

        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdConvocatoria() != null) {
                dto.setIdConvocatoria(entity.getIdConvocatoria());
            }
            if (entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setIdTipoDocumento(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }

        }

        return dto;
    }
}
