package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteModifPresupMonto;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteModifPresupMonto
 *
 * @author quental
 */
public enum ExpedienteModifPresupMontoToEntity
        implements Function<ExpedienteModifPresupMontoDTO, ExpedienteModifPresupMonto> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifPresupMonto apply(ExpedienteModifPresupMontoDTO dto) {

        ExpedienteModifPresupMonto entity = null;

        if (dto != null) {

            entity = new ExpedienteModifPresupMonto();

            BeanUtils.copyProperties(dto, entity);

            // VMM 11/09/26 MD-2725: tipoFinanciacionDTO no se copia por BeanUtils (nombre y tipo
            // distintos al del entity - tipoFinanciacion/CatalogosValores)
            if (dto.getTipoFinanciacionDTO() != null && dto.getTipoFinanciacionDTO().getIdValor() != null) {
                entity.setTipoFinanciacion(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }

            /*if (dto.getExpedienteDocDTO()!= null && dto.getExpedienteDocDTO().getIdExpedienteDoc()!= null) {
                entity.setExpedienteDoc(ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            }*/
        }

        return entity;
    }
}
