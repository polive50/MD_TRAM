package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Apartado
 *
 * @author nttdata
 */
public enum EvaluacionJustecApartaToEntity
        implements Function<EvaluacionJustecApartaDTO, EvaluacionJustecAparta> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecAparta apply(EvaluacionJustecApartaDTO dto) {

        EvaluacionJustecAparta entity = null;

        if (dto != null) {

            entity = new EvaluacionJustecAparta();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
