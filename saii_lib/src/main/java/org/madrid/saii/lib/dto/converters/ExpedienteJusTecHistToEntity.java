package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusTecHistToEntity
        implements Function<ExpedienteJusTecHistDTO, ExpedienteJusTecHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusTecHist apply(ExpedienteJusTecHistDTO dto) {

        ExpedienteJusTecHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteJusTecHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoExpedienteJusTecDTO())) {
                entity.setTipoExpedienteJusTec(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteJusTecDTO()));
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
