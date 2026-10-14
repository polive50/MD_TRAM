package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusTecHistToDTO
        implements Function<ExpedienteJusTecHist, ExpedienteJusTecHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusTecHistDTO apply(ExpedienteJusTecHist entity) {

        ExpedienteJusTecHistDTO dto = new ExpedienteJusTecHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoExpedienteJusTec())) {
                dto.setTipoExpedienteJusTecDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoExpedienteJusTec()));
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
    private void replaceNull(ExpedienteJusTecHistDTO dto, ExpedienteJusTecHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getTlObservacionParaCiudadano())) {
            dto.setTlObservacionParaCiudadano(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getTlObservacionDelCiudadano())) {
            dto.setTlObservacionDelCiudadano(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getCdEregPresent())) {
            dto.setCdEregPresent(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getCdEregSubsana())) {
            dto.setCdEregSubsana(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getDsDestinoEreg())) {
            dto.setDsDestinoEreg(StringUtils.EMPTY);
        }

    }

}
