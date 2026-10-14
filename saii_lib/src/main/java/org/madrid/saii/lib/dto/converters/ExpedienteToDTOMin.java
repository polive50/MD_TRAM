package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.ExpedienteDTO;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Expediente
 *
 * @author everis
 */
public enum ExpedienteToDTOMin implements Function<Expediente, ExpedienteDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDTO apply(Expediente entity) {

        ExpedienteDTO dto = new ExpedienteDTO();

        if (entity != null) {

            dto.setIdExpediente(entity.getIdExpediente());
            dto.setCdExpediente(entity.getCdExpediente());
            dto.setNmNota(entity.getNmNota());
            dto.setIdSolicitud(entity.getSolicitud().getIdSolicitud());
            dto.setNmPaso(entity.getNmPaso());
            dto.setCdEreg(entity.getCdEreg());
            dto.setDsDestinoEreg(entity.getDsDestinoEreg());
            dto.setFcFirma(entity.getFcFirma());
            dto.setCdSiex(entity.getCdSiex());
            dto.setCdUsuAlta(entity.getCdUsuAlta());
            dto.setFcAltaFila(entity.getFcAltaFila());
            dto.setCdUsuModif(entity.getCdUsuModif());
            dto.setFcModifFila(entity.getFcModifFila());
            dto.setCdUsuBaja(entity.getCdUsuBaja());
            dto.setFcBajaFila(entity.getFcBajaFila());

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
            
            if (entity.getEstadoRevContrato() != null) {
                dto.setEstadoRevContratoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevContrato()));
            }

        }

        return dto;
    }

}
