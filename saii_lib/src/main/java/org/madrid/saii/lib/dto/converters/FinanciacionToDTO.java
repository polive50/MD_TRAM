package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.domain.Financiacion;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Financiacion
 *
 * @author nttdata
 */
public enum FinanciacionToDTO implements Function<Financiacion, FinanciacionDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public FinanciacionDTO apply(Financiacion entity) {

        FinanciacionDTO dto = new FinanciacionDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getTematica())) {
                dto.setTematicaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTematica()));
                dto.setEsTematicaAsignada(Boolean.TRUE);
            }
            
            if (Objects.nonNull(entity.getIdActor())) {
                dto.setIdActor(ConvocatoriaActToDTO.INSTANCE.apply(entity.getIdActor()));
                dto.setEsIdActorAsignada(Boolean.TRUE);
            }

            if (entity.getListaFinanciacionDato() != null) {
                dto.setListaFinanciacionDatoDTO(new ArrayList<>());
                entity.getListaFinanciacionDato().forEach(e -> {
                    if (e.getFcBajaFila() == null) {
                        dto.getListaFinanciacionDatoDTO()
                                .add(FinanciacionDatoToDTO.INSTANCE.apply(e));
                    }
                });
                //VMM 15/09/25: arreglo por nullpointer exception cuando algun orden es nulo (no es obligatorio)
                List<FinanciacionDatoDTO> lista = dto.getListaFinanciacionDatoDTO();

                boolean tieneNulos = lista.stream().anyMatch(o -> o.getNmOrden() == null);

                if (!tieneNulos) {
                    lista = lista.stream()
                                 .sorted(Comparator.comparing(FinanciacionDatoDTO::getNmOrden))
                                 .collect(Collectors.toList());
                }

                dto.setListaFinanciacionDatoDTO(lista);

            }
            
            if (Objects.nonNull(entity.getItMostrarAyuda())) {                
                if(entity.getItMostrarAyuda().equals(Constantes.AfirmacionNegacion.S)) {
                    dto.setEsMostrarAyuda(Boolean.TRUE);
                }else if(entity.getItMostrarAyuda().equals(Constantes.AfirmacionNegacion.N)) {
                    dto.setEsMostrarAyuda(Boolean.FALSE);
                }
            }      
        }

        return dto;
    }
}
