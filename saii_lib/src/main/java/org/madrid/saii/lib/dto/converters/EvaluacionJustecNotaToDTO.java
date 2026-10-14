package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for EvaluacionNota
 *
 * @author nttdata
 */
public enum EvaluacionJustecNotaToDTO
        implements Function<EvaluacionJustecNota, EvaluacionJustecNotaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecNotaDTO apply(EvaluacionJustecNota entity) {

        EvaluacionJustecNotaDTO dto = new EvaluacionJustecNotaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getNota() != null) {
                dto.setNotaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getNota()));
            }

        }

        return dto;
    }

}
