package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for EvaluacionNotaDato
 *
 * @author nttdata
 */
public enum EvaluacionNotaDatoToEntity
        implements Function<EvaluacionNotaDatoDTO, EvaluacionNotaDato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionNotaDato apply(EvaluacionNotaDatoDTO dto) {

        EvaluacionNotaDato entity = null;

        if (dto != null) {

            entity = new EvaluacionNotaDato();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
