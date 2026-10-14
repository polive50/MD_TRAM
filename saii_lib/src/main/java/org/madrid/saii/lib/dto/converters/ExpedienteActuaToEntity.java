package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.dto.ExpedienteActuaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteActua
 *
 * @author nttdata
 */
public enum ExpedienteActuaToEntity implements Function<ExpedienteActuaDTO, ExpedienteActua> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteActua apply(ExpedienteActuaDTO dto) {

        ExpedienteActua entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteActua();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoFinanciacionDTO())) {
                entity.setTipoFinanciacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }
        }

        return entity;
    }
}
