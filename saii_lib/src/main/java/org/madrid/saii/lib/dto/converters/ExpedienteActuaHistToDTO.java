package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteActuaHist
 *
 * @author nttdata
 */
public enum ExpedienteActuaHistToDTO
        implements Function<ExpedienteActuaHist, ExpedienteActuaHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteActuaHistDTO apply(ExpedienteActuaHist entity) {

        ExpedienteActuaHistDTO dto = new ExpedienteActuaHistDTO();

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
    private void replaceNull(ExpedienteActuaHistDTO dto, ExpedienteActuaHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }
}
