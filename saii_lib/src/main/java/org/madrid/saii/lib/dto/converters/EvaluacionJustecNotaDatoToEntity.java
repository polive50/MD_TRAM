package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for EvaluacionJustecNotaDato
 *
 * @author nttdata
 */
public enum EvaluacionJustecNotaDatoToEntity
        implements Function<EvaluacionJustecNotaDatoDTO, EvaluacionJustecNotaDato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecNotaDato apply(EvaluacionJustecNotaDatoDTO dto) {

        EvaluacionJustecNotaDato entity = null;

        if (dto != null) {

            entity = new EvaluacionJustecNotaDato();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
