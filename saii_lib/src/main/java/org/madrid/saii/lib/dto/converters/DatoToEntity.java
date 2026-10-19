package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.DatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Dato
 *
 * @author everis
 */
public enum DatoToEntity implements Function<DatoDTO, Dato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Dato apply(DatoDTO dto) {

        Dato entity = null;

        if (dto != null) {

            entity = new Dato();

            BeanUtils.copyProperties(dto, entity);

            if(entity.getBloqueComponente() == null) {
                entity.setBloqueComponente(new BloqueComponente());
            }
            
            if(entity.getBloqueComponente() != null) {
                entity.getBloqueComponente().setIdBloqueComponente(dto.getIdBloqueComponente());
            }
            
            if(entity.getExpediente() == null) {
                entity.setExpediente(new Expediente());
            }

            if(entity.getExpediente() != null && dto.getExpedienteDTO() != null
                    && dto.getExpedienteDTO().getIdExpediente() != null) {
                entity.getExpediente().setIdExpediente(dto.getExpedienteDTO().getIdExpediente());
            }
            
            
            if(entity.getExpedienteJustec() == null && dto.getExpedienteJustecDTO() != null 
                    && dto.getExpedienteJustecDTO().getIdExpedienteJusTec() != null) {
                entity.setExpedienteJustec(new ExpedienteJusTec());
                entity.getExpedienteJustec().setIdExpedienteJusTec(dto.getExpedienteJustecDTO().getIdExpedienteJusTec());
            }
            
            if(entity.getExpedienteModifPresPlazoOtra() ==null && dto.getExpedienteModifPresPlazoOtraDTO() != null 
                    && dto.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra() != null) {
                ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra = new ExpedienteModifPresPlazoOtra();
                expedienteModifPresPlazoOtra.setIdExpedienteModifPresPlazoOtra(dto.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra());
                entity.setExpedienteModifPresPlazoOtra(expedienteModifPresPlazoOtra);
            }
            
            if(entity.getExpedienteJusEco() == null && dto.getExpedienteJusEcoDTO() != null 
                    && dto.getExpedienteJusEcoDTO().getIdExpedienteJusEco() != null) {
                entity.setExpedienteJusEco(new ExpedienteJusEco());
                entity.getExpedienteJusEco().setIdExpedienteJusEco(dto.getExpedienteJusEcoDTO().getIdExpedienteJusEco());
            }
            
            if (entity.getExpedientePresupMonto() == null && dto.getExpedientePresupMonto() != null 
                    && dto.getExpedientePresupMonto().getIdExpedientePresupMonto() != null) {
                
                ExpedientePresupMonto monto = new ExpedientePresupMonto();
                monto.setIdExpedientePresupMonto(dto.getExpedientePresupMonto().getIdExpedientePresupMonto());
                
                if (dto.getExpedientePresupMonto().getNmImporte() != null) {
                    monto.setNmImporte(dto.getExpedientePresupMonto().getNmImporte());
                }
                
                entity.setExpedientePresupMonto(monto);
            }
            
        }

        return entity;
    }
}
