package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusEcoHistToDTO
        implements Function<ExpedienteJusEcoHist, ExpedienteJusEcoHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusEcoHistDTO apply(ExpedienteJusEcoHist entity) {

        ExpedienteJusEcoHistDTO dto = new ExpedienteJusEcoHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoExpedienteJusEco())) {
                dto.setTipoExpedienteJusEcoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoExpedienteJusEco()));
            }
            if (Objects.nonNull(entity.getIdEstadoRevAdmin())) {
                dto.setIdEstadoRevAdminDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstadoRevAdmin()));
            }
            if (Objects.nonNull(entity.getIdEstado())) {
                dto.setIdEstadoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
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
    private void replaceNull(ExpedienteJusEcoHistDTO dto, ExpedienteJusEcoHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
