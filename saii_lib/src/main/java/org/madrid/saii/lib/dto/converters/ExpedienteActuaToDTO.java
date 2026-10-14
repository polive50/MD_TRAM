package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.dto.ExpedienteActuaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteActua
 *
 * @author nttdata
 */
public enum ExpedienteActuaToDTO implements Function<ExpedienteActua, ExpedienteActuaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteActuaDTO apply(ExpedienteActua entity) {

        ExpedienteActuaDTO dto = new ExpedienteActuaDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoFinanciacion())) {
                dto.setTipoFinanciacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoFinanciacion()));
            }
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteActuaDTO dto, ExpedienteActua entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }
}
