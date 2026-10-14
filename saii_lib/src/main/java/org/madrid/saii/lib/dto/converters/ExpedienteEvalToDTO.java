package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import atlas.core.exceptions.ServiceException;

/**
 * Parser Entity to DTO for ExpedienteEval
 *
 * @author nttdata
 */
public enum ExpedienteEvalToDTO implements Function<ExpedienteEval, ExpedienteEvalDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEvalDTO apply(ExpedienteEval entity) {

        ExpedienteEvalDTO dto = new ExpedienteEvalDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getCausa() != null) {
                dto.setCausaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getCausa()));
            }

            if (Objects.nonNull(entity.getPersona())) {
                dto.setPersonaDTO(InteresadoToDTO.INSTANCE.apply(entity.getPersona()));
            }
            if (Objects.nonNull(entity.getEstado())
                    && Objects.nonNull(entity.getEstado().getIdValor())) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
                
                UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
                dto.setEsCerradoEvaludor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR).equals(entity.getEstado().getIdValor()));
                
            }
            if (Objects.nonNull(entity.getMotivoRechazo())
                    && Objects.nonNull(entity.getMotivoRechazo().getIdValor())) {
                dto.setMotivoRechazo(CatalogosValoresToDTO.INSTANCE.apply(entity.getMotivoRechazo()));
            }
            
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteEvalDTO dto, ExpedienteEval entity) {

        if (StringUtils.isBlank(entity.getCdFica())) {
            dto.setCdFica(StringUtils.EMPTY);
        }

    }
}
