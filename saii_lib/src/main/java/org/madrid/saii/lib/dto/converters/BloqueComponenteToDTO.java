package org.madrid.saii.lib.dto.converters;

import java.util.stream.Collectors;

import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Componente
 *
 * @author everis
 */
public enum BloqueComponenteToDTO implements Function<BloqueComponente, BloqueComponenteDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public BloqueComponenteDTO apply(BloqueComponente entity) {

        BloqueComponenteDTO dto = new BloqueComponenteDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getBloque() != null) {
                dto.setIdBloque(entity.getBloque().getIdBloque());
            }

            if (entity.getComponente() != null) {
                dto.setComponenteDTO(ComponenteToDTO.INSTANCE.apply(entity.getComponente()));
            }

            if (entity.getTipoFinanciacion() != null) {
                dto.setTipoFinanciacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoFinanciacion()));
            }

            if (entity.getTramite() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            dto.setItFila(Boolean.FALSE);
            dto.setItColumna(Boolean.FALSE);
            dto.setItObligatorio(Boolean.FALSE);
            dto.setItRelacion(Boolean.FALSE);

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItFila())) {
                dto.setItFila(Boolean.TRUE);
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItColumna())) {
                dto.setItColumna(Boolean.TRUE);
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItObligatorio())) {
                dto.setItObligatorio(Boolean.TRUE);
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItRelacion())) {
                dto.setItRelacion(Boolean.TRUE);
            }
            
            if(entity.getLstDato() != null && !entity.getLstDato().isEmpty())
                dto.setDatos(entity.getLstDato().stream().map(DatoToDTO.INSTANCE::apply).collect(Collectors.toList()));
            
        }

        return dto;
    }
}
