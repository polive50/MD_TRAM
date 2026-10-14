package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteContratoHistToDTO
        implements Function<ExpedienteContratoHist, ExpedienteContratoHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteContratoHistDTO apply(ExpedienteContratoHist entity) {

        ExpedienteContratoHistDTO dto = new ExpedienteContratoHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getIdTipoContrato())) {
                dto.setTipoContratoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoContrato()));
            }
            if (Objects.nonNull(entity.getIdEstadoRevAdmin())) {
                dto.setIdEstadoRevAdminDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstadoRevAdmin()));
            }
            if (Objects.nonNull(entity.getIdEstado())) {
                dto.setEstadoDTO(
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
    private void replaceNull(ExpedienteContratoHistDTO dto, ExpedienteContratoHist entity) {

        if (StringUtils.isBlank(entity.getTlCausaDescripcion())) {
            dto.setTlCausaDescripcion(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlObservaciDelCiudadano())) {
            dto.setTlObservaciDelCiudadano(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlObservaciParaCiudadano())) {
            dto.setTlObservaciParaCiudadano(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getDsDestinoEreg())) {
            dto.setDsDestinoEreg(StringUtils.EMPTY);
        }

    }

}
