package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteFin
 *
 * @author nttdata
 */
public enum ExpedientePresupMontoToEntity
        implements Function<ExpedientePresupMontoDTO, ExpedientePresupMonto> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresupMonto apply(ExpedientePresupMontoDTO dto) {

        ExpedientePresupMonto entity = null;

        if (dto != null) {

            entity = new ExpedientePresupMonto();

            BeanUtils.copyProperties(dto, entity);
            
            /*if (dto.getLstExpedienteJusEcoDocDTO()!= null) {
                entity.setLstExpedienteJusEcoDoc(new ArrayList<>());
                for (ExpedienteJusEcoDocDTO jusEcoDocDTO : dto.getLstExpedienteJusEcoDocDTO()) {
                    entity.getLstExpedienteJusEcoDoc()
                            .add(ExpedienteJusEcoDocToEntity.INSTANCE.apply(jusEcoDocDTO));
                }
            }*/
            
            if (dto.getEstadoDTO() != null && dto.getEstadoDTO().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }
        }

        return entity;
    }
}
