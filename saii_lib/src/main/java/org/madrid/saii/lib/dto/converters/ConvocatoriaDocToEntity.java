package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaDoc
 *
 * @author everis
 */
public enum ConvocatoriaDocToEntity implements Function<ConvocatoriaDocDTO, ConvocatoriaDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaDoc apply(ConvocatoriaDocDTO dto) {

        ConvocatoriaDoc entity = null;

        if (dto != null) {

            entity = new ConvocatoriaDoc();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdConvocatoria() != null) {
                entity.setIdConvocatoria(dto.getIdConvocatoria());
            }
            if (dto.getIdTipoDocumento().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoDocumento()));
            }

        }

        return entity;
    }
}
