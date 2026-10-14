package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteContratoHistToEntity
        implements Function<ExpedienteContratoHistDTO, ExpedienteContratoHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteContratoHist apply(ExpedienteContratoHistDTO dto) {

        ExpedienteContratoHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteContratoHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoContratoDTO())) {
                entity.setIdTipoContrato(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoContratoDTO()));
            }
            
            if (Objects.nonNull(dto.getIdEstadoRevAdminDTO())) {
                entity.setIdEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoRevAdminDTO()));
            }
            
            if (Objects.nonNull(dto.getEstadoDTO())) {
              entity.setIdEstado(
                      CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
          }
        }

        return entity;
    }

}
