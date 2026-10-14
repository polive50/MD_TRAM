package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteJustecDoc
 *
 * @author Quental
 */
public enum ExpedienteJustecDocToEntity implements Function<ExpedienteJustecDocDTO, ExpedienteJustecDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJustecDoc apply(ExpedienteJustecDocDTO dto) {

        ExpedienteJustecDoc entity = null;

        if (dto != null) {

            entity = new ExpedienteJustecDoc();

            BeanUtils.copyProperties(dto, entity);
            if (!Objects.isNull(dto.getCumple()))
                entity.setCumple(dto.getCumple() ? "1" : "0");
        }

        return entity;
    }
}
