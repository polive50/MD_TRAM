package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Dato
 *
 * @author everis
 */
public enum DatoToDTO implements Function<Dato, DatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public DatoDTO apply(Dato entity) {

        DatoDTO dto = new DatoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if(entity.getBloqueComponente() != null) {
                dto.setIdBloqueComponente(entity.getBloqueComponente().getIdBloqueComponente());
            }
            
            if(entity.getExpediente()!= null) {
                dto.setExpedienteDTO(new ExpedienteDTO(entity.getExpediente().getIdExpediente()));
            }
            
            if (dto.getDsValor() != null) {
                dto.setDsValorMultiple(dto.getDsValor().split(";"));
            }
            if(entity.getExpedienteJustec() !=null) {
                ExpedienteJusTecDTO expedienteJustecDTO = new ExpedienteJusTecDTO();
                expedienteJustecDTO.setIdExpedienteJusTec(entity.getExpedienteJustec().getIdExpedienteJusTec());
                dto.setExpedienteJustecDTO(expedienteJustecDTO);
            }
            
            if(entity.getExpedienteModifPresPlazoOtra() !=null) {
                ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO = new ExpedienteModifPresPlazoOtraDTO();
                expedienteModifPresPlazoOtraDTO.setIdExpedienteModifPresPlazoOtra(entity.getExpedienteModifPresPlazoOtra().getIdExpedienteModifPresPlazoOtra());
                dto.setExpedienteModifPresPlazoOtraDTO(expedienteModifPresPlazoOtraDTO);
            }
            
            if(entity.getExpedienteJusEco() !=null) {
                ExpedienteJusEcoDTO expedienteJusEcoDTO = new ExpedienteJusEcoDTO();
                expedienteJusEcoDTO.setIdExpedienteJusEco(entity.getExpedienteJusEco().getIdExpedienteJusEco());
                dto.setExpedienteJusEcoDTO(expedienteJusEcoDTO);
            }
            
            if(entity.getExpedientePresupMonto() != null) {
                ExpedientePresupMontoDTO expedientePresupMontoDTO = new ExpedientePresupMontoDTO();
                
                // Mapeamos el ID
                expedientePresupMontoDTO.setIdExpedientePresupMonto(entity.getExpedientePresupMonto().getIdExpedientePresupMonto());
                
                // Mapeamos el importe concedido original (esencial para tu lógica posterior)
                if (entity.getExpedientePresupMonto().getNmImporte() != null) {
                    expedientePresupMontoDTO.setNmImporte(entity.getExpedientePresupMonto().getNmImporte());
                }
                
                dto.setExpedientePresupMonto(expedientePresupMontoDTO);
            }

        }

        return dto;
    }
}
