package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for EvaluacionNotaDato
 *
 * @author nttdata
 */
public enum EvaluacionNotaDatoToDTO implements Function<EvaluacionNotaDato, EvaluacionNotaDatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionNotaDatoDTO apply(EvaluacionNotaDato entity) {

        EvaluacionNotaDatoDTO dto = new EvaluacionNotaDatoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
        }

        return dto;
    }

}
