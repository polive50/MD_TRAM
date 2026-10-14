package org.madrid.saii.lib.dto.converters;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.TramiteSolicitanteExpediente;
import org.madrid.saii.lib.dto.TramiteSolicitanteExpedienteDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum TramiteSolicitanteExpedienteToEntity implements Function<TramiteSolicitanteExpedienteDTO,TramiteSolicitanteExpediente> {
    


    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public TramiteSolicitanteExpediente apply(TramiteSolicitanteExpedienteDTO dto) {
        
        TramiteSolicitanteExpediente entity= null;
        if (dto != null) {

            entity= new TramiteSolicitanteExpediente();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTramiteDTO() != null && dto.getTramiteDTO().getIdValor() != null) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            if (dto.getInteresadoDTO() != null
                    && dto.getInteresadoDTO().getIdInteresado() != null) {
                entity.setInteresado(
                        InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }
            
            if (dto.getExpedienteDTO() != null) {
                entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));
            }

        }
        
       return entity;
       
    }

}
