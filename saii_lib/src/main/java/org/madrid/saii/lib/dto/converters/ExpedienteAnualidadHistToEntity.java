package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteAnualidadHist;
import org.madrid.saii.lib.dto.ExpedienteAnualidadHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteAnualidadHistToEntity
        implements Function<ExpedienteAnualidadHistDTO, ExpedienteAnualidadHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteAnualidadHist apply(ExpedienteAnualidadHistDTO dto) {

        ExpedienteAnualidadHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteAnualidadHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoExpedienteAnualidadDTO())) {
                entity.setTipoExpedienteAnualidad(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteAnualidadDTO()));
            }
        }

        return entity;
    }

}
