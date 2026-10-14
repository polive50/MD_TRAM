package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for EvaluacionNota
 *
 * @author nttdata
 */
public enum EvaluacionNotaToEntity implements Function<EvaluacionNotaDTO, EvaluacionNota> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionNota apply(EvaluacionNotaDTO dto) {

        EvaluacionNota entity = null;

        if (dto != null) {

            entity = new EvaluacionNota();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getNotaDTO() != null) {
                entity.setNota(CatalogosValoresToEntity.INSTANCE.apply(dto.getNotaDTO()));
            }

        }

        return entity;
    }
}
