package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteHist;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Expediente
 *
 * @author everis
 */
public enum ExpedienteHistToDTO implements Function<ExpedienteHist, ExpedienteHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteHistDTO apply(ExpedienteHist entity) {

        ExpedienteHistDTO dto = new ExpedienteHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getInteresado() != null) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            }

            if (entity.getEstado() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }

            if (entity.getEstadoBenef() != null) {
                dto.setEstadoBeneficiarioDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoBenef()));
            }

            if (entity.getEstadoRevAdmin() != null) {
                dto.setEstadoRevAdminDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevAdmin()));
            }

            if (entity.getEstadoRevRequi() != null) {
                dto.setEstadoRevRequiDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevRequi()));
            }

            if (entity.getEstadoRevEval() != null) {
                dto.setEstadoRevEvalDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevEval()));
            }
            
            if (entity.getEstadoRevFinan() != null) {
                dto.setEstadoRevFinanDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevFinan()));
            }

            if (entity.getEstadoProvisional() != null) {
                dto.setEstadoProvisionalDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoProvisional()));
            }
            
            if (entity.getEstadoRevContrato() != null) {
                dto.setEstadoRevContDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevContrato()));
            }

            if (entity.getAutorizado() != null) {
                dto.setAutorizado(InteresadoToDTO.INSTANCE.apply(entity.getAutorizado()));
            }

        }

        return dto;
    }

}
