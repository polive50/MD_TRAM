package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteModifPresupMonto;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteModifPresupMonto
 *
 * @author quental
 */
public enum ExpedienteModifPresupMontoToDTO
        implements Function<ExpedienteModifPresupMonto, ExpedienteModifPresupMontoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifPresupMontoDTO apply(ExpedienteModifPresupMonto entity) {

        ExpedienteModifPresupMontoDTO dto = new ExpedienteModifPresupMontoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setCdObservaciones(entity.getCdObservaciones());

            // VMM 11/09/26 MD-2725: tipoFinanciacion no se copia por BeanUtils (nombre y tipo
            // distintos al del DTO - tipoFinanciacionDTO/CatalogosValoresDTO)
            if (Objects.nonNull(entity.getTipoFinanciacion())) {
                dto.setTipoFinanciacionDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoFinanciacion()));
            }

            /*if (Objects.nonNull(entity.getExpedienteDoc())) {
                dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            }*/
            
            this.replaceNull(dto, entity);

        }

        return dto;
    }
    

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteModifPresupMontoDTO dto, ExpedienteModifPresupMonto entity) {
// TODO pendiente de rellenar
    }
}
