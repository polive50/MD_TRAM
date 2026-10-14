package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteModifOtra;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteModifOtra
 *
 * @author quental
 */
public enum ExpedienteModifOtraToEntity implements Function<ExpedienteModifOtraDTO, ExpedienteModifOtra> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifOtra apply(ExpedienteModifOtraDTO dto) {

        ExpedienteModifOtra entity = null;

        if (dto != null) {
            entity = new ExpedienteModifOtra();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getExpedienteModifPresPlazoOtraDTO())) {
                entity.setExpedienteModifPresPlazoOtra
                    (ExpedienteModifPresPlazoOtraToEntity.INSTANCE.apply(dto.getExpedienteModifPresPlazoOtraDTO()));
            }
        }

        return entity;
    }
}
