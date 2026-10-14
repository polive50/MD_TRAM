package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteFin
 *
 * @author nttdata
 */
public enum ExpedientePresupMontoToDTO
        implements Function<ExpedientePresupMonto, ExpedientePresupMontoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresupMontoDTO apply(ExpedientePresupMonto entity) {

        ExpedientePresupMontoDTO dto = new ExpedientePresupMontoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            /*if (entity.getLstExpedienteJusEcoDoc() != null) {
                entity.getLstExpedienteJusEcoDoc().forEach(e -> {
                    dto.getLstExpedienteJusEcoDocDTO()
                            .add(ExpedienteJusEcoDocToDTO.INSTANCE.apply(e));
                });
            }*/
            
            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
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
    private void replaceNull(ExpedientePresupMontoDTO dto, ExpedientePresupMonto entity) {
       
    }
}
