package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Objects;

import org.madrid.saii.lib.domain.Financiacion;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Financiacion
 *
 * @author nttdata
 */
public enum FinanciacionToEntity implements Function<FinanciacionDTO, Financiacion> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Financiacion apply(FinanciacionDTO dto) {

        Financiacion entity = null;

        if (Objects.nonNull(dto)) {

            entity = new Financiacion();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTematicaDTO())) {
                entity.setTematica(CatalogosValoresToEntity.INSTANCE.apply(dto.getTematicaDTO()));
            }
            
            if (Objects.nonNull(dto.getIdActor())) {
                entity.setIdActor(ConvocatoriaActToEntity.INSTANCE.apply(dto.getIdActor()));
                
            }
            

            if (dto.getListaFinanciacionDatoDTO() != null) {
                entity.setListaFinanciacionDato(new ArrayList<>());
                for (FinanciacionDatoDTO en : dto.getListaFinanciacionDatoDTO()) {
                    entity.getListaFinanciacionDato()
                            .add(FinanciacionDatoToEntity.INSTANCE.apply(en));
                }
            }
            
            if (Objects.nonNull(dto.getEsMostrarAyuda())) {
                if(dto.getEsMostrarAyuda()){
                    entity.setItMostrarAyuda(Constantes.AfirmacionNegacion.S);
                }else {
                    entity.setItMostrarAyuda(Constantes.AfirmacionNegacion.N);
                }
               
            }

        }

        return entity;
    }
}
