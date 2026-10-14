package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Impreso
 *
 * @author everis
 */
public enum ImpresoToEntity implements Function<ImpresoDTO, Impreso> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Impreso apply(ImpresoDTO dto) {

        Impreso entity = null;

        if (dto != null) {

            entity = new Impreso();

            BeanUtils.copyProperties(dto, entity);
            
            Convocatoria convAux = new Convocatoria();
            convAux.setIdConvocatoria(dto.getIdConvocatoria());
            entity.setConvocatoria(convAux);

            if (dto.getTipoDTO() != null) {
                entity.setTipo(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDTO()));
            }

            if (dto.getConvocatoriaActDTO() != null
                    && dto.getConvocatoriaActDTO().getIdActor() != null) {
                entity.setConvocatoriaAct(
                        ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActDTO()));
            }

            if (dto.getImagenLogoDTO() != null && dto.getImagenLogoDTO().getIdImagen() != null) {
                entity.setImagenLogo(ImagenToEntity.INSTANCE.apply(dto.getImagenLogoDTO()));
            }

            entity.setCdFicaImpreso(dto.getCdFicaImpreso());
            entity.setCdFicaPlantilla(dto.getCdFicaPlantilla());
        }

        return entity;
    }
}
