package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for EvaluacionNota
 *
 * @author nttdata
 */
public enum EvaluacionJustecNotaToEntity
        implements Function<EvaluacionJustecNotaDTO, EvaluacionJustecNota> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecNota apply(EvaluacionJustecNotaDTO dto) {

        EvaluacionJustecNota entity = null;

        if (dto != null) {

            entity = new EvaluacionJustecNota();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getNotaDTO() != null) {
                entity.setNota(CatalogosValoresToEntity.INSTANCE.apply(dto.getNotaDTO()));
            }

        }

        return entity;
    }
}
