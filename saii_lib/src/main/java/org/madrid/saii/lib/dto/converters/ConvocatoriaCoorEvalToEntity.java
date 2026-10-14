package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.ConvocatoriaCoorEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCoorEval
 *
 * @author nttdata
 */
public enum ConvocatoriaCoorEvalToEntity
        implements Function<ConvocatoriaCoorEvalDTO, ConvocatoriaCoorEval> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCoorEval apply(ConvocatoriaCoorEvalDTO dto) {

        ConvocatoriaCoorEval entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCoorEval();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getEvaluadorDTO())) {
                entity.setEvaluador(InteresadoToEntity.INSTANCE.apply(dto.getEvaluadorDTO()));
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
