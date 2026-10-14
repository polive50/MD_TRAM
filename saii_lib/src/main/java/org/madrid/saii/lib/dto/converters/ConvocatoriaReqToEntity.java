package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaReq
 *
 * @author nttdata
 */
public enum ConvocatoriaReqToEntity implements Function<ConvocatoriaReqDTO, ConvocatoriaReq> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaReq apply(ConvocatoriaReqDTO dto) {

        ConvocatoriaReq entity = null;

        if (dto != null) {

            entity = new ConvocatoriaReq();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getRequisitoDTO() != null) {
                entity.setRequisito(CatalogosValoresToEntity.INSTANCE.apply(dto.getRequisitoDTO()));
            }
            
            if (dto.getConvocatoriaTipoDocDTO() != null) {
                entity.setConvocatoriaTipoDoc(ConvocatoriaTipoDocToEntity.INSTANCE
                        .apply(dto.getConvocatoriaTipoDocDTO()));
            }

//            if (dto.getConvocatoriaSubmodDTO() != null) {
//                entity.setConvocatoriaSubmod(
//                        ConvocatoriaSubmodToEntity.INSTANCE.apply(dto.getConvocatoriaSubmodDTO()));
//            }
//            
//            if (dto.getConvocatoriaActDTO() != null) {
//                entity.setConvocatoriaAct(
//                        ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActDTO()));
//            }
            
            // Realizamos las siguientes comparaciones y asignaciones para
            // evitar el error "references an unsaved transient instance "
            this.transformarConvocatoriaAct(dto, entity);
            this.transformarConvocatoriaSubmod(dto, entity);

        }

        return entity;
    }
    
    private void transformarConvocatoriaAct(ConvocatoriaReqDTO dto, ConvocatoriaReq entity) {

        if (Objects.isNull(dto.getConvocatoriaActDTO())
                || Objects.isNull(dto.getConvocatoriaActDTO().getIdActor())) {
            entity.setConvocatoriaAct(null);
        } else {
            entity.setConvocatoriaAct(ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActDTO()));
        }

    }
    
    private void transformarConvocatoriaSubmod(ConvocatoriaReqDTO dto, ConvocatoriaReq entity) {

        if (Objects.isNull(dto.getConvocatoriaSubmodDTO())
                || Objects.isNull(dto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod())) {
            entity.setConvocatoriaSubmod(null);
        } else {
            entity.setConvocatoriaSubmod(ConvocatoriaSubmodToEntity.INSTANCE.apply(dto.getConvocatoriaSubmodDTO()));
        }

    }

}
