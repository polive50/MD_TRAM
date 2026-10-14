package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for EvaluacionNota
 *
 * @author nttdata
 */
public enum EvaluacionNotaToDTO implements Function<EvaluacionNota, EvaluacionNotaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionNotaDTO apply(EvaluacionNota entity) {

        EvaluacionNotaDTO dto = new EvaluacionNotaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getNota() != null) {
                dto.setNotaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getNota()));
            }

        }

        return dto;
    }

}
