package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteContratoToEntity implements Function<ExpedienteContratoDTO, ExpedienteContrato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteContrato apply(ExpedienteContratoDTO dto) {

        ExpedienteContrato entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedienteContrato();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getIdTipoContratoDTO()) && Objects.nonNull(dto.getIdTipoContratoDTO().getIdValor())) {
                entity.setIdTipoContrato(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoContratoDTO()));
            }
            if (Objects.nonNull(dto.getIdCausaDTO()) &&Objects.nonNull(dto.getIdCausaDTO().getIdValor())) {
                entity.setIdCausa(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdCausaDTO()));
            }
            
            if (Objects.nonNull(dto.getIdEstadoDTO()) && Objects.nonNull(dto.getIdEstadoDTO().getIdValor())) {
                entity.setIdEstado(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoDTO()));
            }
            
            if (Objects.nonNull(dto.getIdEstadoRevAdminDTO()) &&Objects.nonNull(dto.getIdEstadoRevAdminDTO().getIdValor())) {
                entity.setIdEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoRevAdminDTO()));
            }
            
            if (Objects.nonNull(dto.getInteresadoDTO()) && Objects.nonNull(dto.getInteresadoDTO().getIdInteresado())) {
                entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }

        }

        return entity;
    }

    

}
