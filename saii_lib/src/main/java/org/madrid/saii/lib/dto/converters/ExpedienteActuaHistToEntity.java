package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteActuaHist
 *
 * @author nttdata
 */
public enum ExpedienteActuaHistToEntity
        implements Function<ExpedienteActuaHistDTO, ExpedienteActuaHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteActuaHist apply(ExpedienteActuaHistDTO dto) {

        ExpedienteActuaHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteActuaHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoFinanciacionDTO())) {
                entity.setTipoFinanciacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }
        }

        return entity;
    }
}
