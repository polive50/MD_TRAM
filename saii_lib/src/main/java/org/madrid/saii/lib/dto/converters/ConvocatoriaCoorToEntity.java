package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCoor
 *
 * @author nttdata
 */
public enum ConvocatoriaCoorToEntity implements Function<ConvocatoriaCoorDTO, ConvocatoriaCoor> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCoor apply(ConvocatoriaCoorDTO dto) {

        ConvocatoriaCoor entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCoor();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getInteresadoDTO() != null) {
                entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }

            if (dto.getAreaDTO() != null && dto.getAreaDTO().getIdValor() != null) {
                entity.setArea(CatalogosValoresToEntity.INSTANCE.apply(dto.getAreaDTO()));
            }
            
            if (dto.getTipoInformeDTO() != null && dto.getTipoInformeDTO().getIdValor() != null) {
                entity.setTipoInforme(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoInformeDTO()));
            }

        }

        return entity;
    }
}
