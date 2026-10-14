package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaPla
 *
 * @author everis
 */
public enum ConvocatoriaPlaToEntity implements Function<ConvocatoriaPlaDTO, ConvocatoriaPla> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPla apply(ConvocatoriaPlaDTO dto) {

        ConvocatoriaPla entity = null;

        if (dto != null) {

            entity = new ConvocatoriaPla();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getLstConvocatoriaPlaFirDTO() != null
                    && !dto.getLstConvocatoriaPlaFirDTO().isEmpty()) {
                entity.setLstConvocatoriaPlaFir(new ArrayList<>());
                for (ConvocatoriaPlaFirDTO firmanteDTO : dto.getLstConvocatoriaPlaFirDTO()) {
                    entity.getLstConvocatoriaPlaFir()
                            .add(ConvocatoriaPlaFirToEntity.INSTANCE.apply(firmanteDTO));
                }

            }

            if (Objects.nonNull(dto.getTramiteDTO())) {
            	entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            if (Objects.nonNull(dto.getImagenDTO())) {
            	entity.setImagen(ImagenToEntity.INSTANCE.apply(dto.getImagenDTO()));
            }
            
            if (dto.getLstConvocatoriaPlaDocDTO() != null
                    && !dto.getLstConvocatoriaPlaDocDTO().isEmpty()) {
                entity.setLstConvocatoriaPlaDoc(new ArrayList<>());
                for (ConvocatoriaPlaDocDTO documentacionDTO : dto.getLstConvocatoriaPlaDocDTO()) {
                    entity.getLstConvocatoriaPlaDoc()
                            .add(ConvocatoriaPlaDocToEntity.INSTANCE.apply(documentacionDTO));
                }

            }

        }

        return entity;
    }
}
