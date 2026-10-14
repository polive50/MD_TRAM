package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for EvaluacionJustecNotaDato
 *
 * @author nttdata
 */
public enum EvaluacionJustecNotaDatoToDTO implements Function<EvaluacionJustecNotaDato, EvaluacionJustecNotaDatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecNotaDatoDTO apply(EvaluacionJustecNotaDato entity) {

        EvaluacionJustecNotaDatoDTO dto = new EvaluacionJustecNotaDatoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
        }

        return dto;
    }

}
