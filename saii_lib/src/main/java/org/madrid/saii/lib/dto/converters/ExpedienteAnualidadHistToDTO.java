package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteAnualidadHist;
import org.madrid.saii.lib.dto.ExpedienteAnualidadHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteAnualidadHistToDTO
        implements Function<ExpedienteAnualidadHist, ExpedienteAnualidadHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteAnualidadHistDTO apply(ExpedienteAnualidadHist entity) {

        ExpedienteAnualidadHistDTO dto = new ExpedienteAnualidadHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoExpedienteAnualidad())) {
                dto.setTipoExpedienteAnualidadDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoExpedienteAnualidad()));
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
    private void replaceNull(ExpedienteAnualidadHistDTO dto, ExpedienteAnualidadHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
