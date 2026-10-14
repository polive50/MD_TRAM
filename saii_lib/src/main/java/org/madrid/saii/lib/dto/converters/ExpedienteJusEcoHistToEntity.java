package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusEcoHistToEntity
        implements Function<ExpedienteJusEcoHistDTO, ExpedienteJusEcoHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusEcoHist apply(ExpedienteJusEcoHistDTO dto) {

        ExpedienteJusEcoHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteJusEcoHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoExpedienteJusEcoDTO())) {
                entity.setTipoExpedienteJusEco(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteJusEcoDTO()));
            }
            if (Objects.nonNull(dto.getIdEstadoRevAdminDTO())) {
                entity.setIdEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoRevAdminDTO()));
            }
            if (Objects.nonNull(dto.getIdEstadoDTO())) {
                entity.setIdEstado(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoDTO()));
            }

        }

        return entity;
    }

}
