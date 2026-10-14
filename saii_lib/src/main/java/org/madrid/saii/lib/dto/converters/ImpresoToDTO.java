package org.madrid.saii.lib.dto.converters;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Impreso
 *
 * @author everis
 */
public enum ImpresoToDTO implements Function<Impreso, ImpresoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ImpresoDTO apply(Impreso entity) {

        ImpresoDTO dto = new ImpresoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            dto.setIdConvocatoria(entity.getConvocatoria().getIdConvocatoria());

            if (entity.getTipo() != null) {
                dto.setTipoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipo()));
            }

            if (entity.getConvocatoriaAct() != null) {
                dto.setConvocatoriaActDTO(
                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaAct()));

                dto.setTipoActorDTO(dto.getConvocatoriaActDTO().getTipoActorDTO());

            }

            if (Objects.nonNull(entity.getLstBloque())
                    && !entity.getLstBloque().isEmpty()) {

                Iterator<Bloque> it = entity.getLstBloque().iterator();
                while (it.hasNext()) {
                    Bloque bloque = it.next();
                    dto.getLstBloqueDTO().add(BloqueToDTO.INSTANCE.apply(bloque));
                }

                // Ordenamos siempre los bloque por orden
                Comparator<BloqueDTO> compareByNumOrden = (BloqueDTO b1, BloqueDTO b2) -> b1
                        .getNmOrden().compareTo(b2.getNmOrden());

                Collections.sort(dto.getLstBloqueDTO(), compareByNumOrden);
            }

            if (entity.getImagenLogo() != null) {
                dto.setImagenLogoDTO(ImagenToDTO.INSTANCE.apply(entity.getImagenLogo()));
            }

            dto.setCdFicaImpreso(entity.getCdFicaImpreso());
            dto.setCdFicaPlantilla(entity.getCdFicaPlantilla());
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ImpresoDTO dto, Impreso entity) {

        if (StringUtils.isBlank(entity.getItGeneraImpreso())) {
            dto.setItGeneraImpreso(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsImpresoOriginal())) {
            dto.setDsImpresoOriginal(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsPlantillaTecnica())) {
            dto.setDsPlantillaTecnica(StringUtils.EMPTY);
        }

    }
}
