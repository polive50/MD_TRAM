package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Apartado
 *
 * @author nttdata
 */
public enum EvaluacionJustecApartaToDTO
        implements Function<EvaluacionJustecAparta, EvaluacionJustecApartaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecApartaDTO apply(EvaluacionJustecAparta entity) {

        EvaluacionJustecApartaDTO dto = new EvaluacionJustecApartaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(EvaluacionJustecApartaDTO dto, EvaluacionJustecAparta entity) {

        if (StringUtils.isBlank(entity.getTlTexto())) {
            dto.setTlTexto(StringUtils.EMPTY);
        }
    }
}
