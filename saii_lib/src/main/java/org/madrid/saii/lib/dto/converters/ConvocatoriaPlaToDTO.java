package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaPla
 *
 * @author everis
 */
public enum ConvocatoriaPlaToDTO implements Function<ConvocatoriaPla, ConvocatoriaPlaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaDTO apply(ConvocatoriaPla entity) {

        ConvocatoriaPlaDTO dto = new ConvocatoriaPlaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getLstConvocatoriaPlaFir() != null
                    && !entity.getLstConvocatoriaPlaFir().isEmpty()) {
                dto.setLstConvocatoriaPlaFirDTO(new ArrayList<>());
                for (ConvocatoriaPlaFir firmante : entity.getLstConvocatoriaPlaFir()) {
                    dto.getLstConvocatoriaPlaFirDTO()
                    .add(ConvocatoriaPlaFirToDTO.INSTANCE.apply(firmante));
                }

            }

            if (entity.getLstConvocatoriaPlaVer() != null
                    && !entity.getLstConvocatoriaPlaVer().isEmpty()) {
                dto.setLstConvocatoriaPlaVerDTO(new ArrayList<>());
                for (ConvocatoriaPlaVer verificador : entity.getLstConvocatoriaPlaVer()) {
                    dto.getLstConvocatoriaPlaVerDTO()
                    .add(ConvocatoriaPlaVerToDTO.INSTANCE.apply(verificador));
                }

            }

            if (entity.getTramite() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            if (Objects.nonNull(entity.getImagen())) {
            	dto.setImagenDTO(ImagenToDTO.INSTANCE.apply(entity.getImagen()));
            }
            
            if (entity.getLstConvocatoriaPlaDoc() != null
                    && !entity.getLstConvocatoriaPlaDoc().isEmpty()) {
                dto.setLstConvocatoriaPlaDocDTO(new ArrayList<>());
                for (ConvocatoriaPlaDoc documentacion : entity.getLstConvocatoriaPlaDoc()) {
                    dto.getLstConvocatoriaPlaDocDTO()
                    .add(ConvocatoriaPlaDocToDTO.INSTANCE.apply(documentacion));
                }

            }

        }

        return dto;
    }
}
