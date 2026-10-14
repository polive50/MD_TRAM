package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.madrid.saii.lib.dto.PersonaOrgDTO;
import org.madrid.saii.lib.dto.PersonaTitDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Interesado
 *
 * @author everis
 */
public enum InteresadoToEntity implements Function<InteresadoDTO, Interesado> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Interesado apply(InteresadoDTO dto) {

        Interesado entity = null;

        if (dto != null) {

            entity = new Interesado();

            BeanUtils.copyProperties(dto, entity);

            // Ponemos en mayusculas el nombre
            if (StringUtils.isNotBlank(dto.getDsNombre())) {
                entity.setDsNombre(dto.getDsNombre().toUpperCase());
            }
            if (StringUtils.isNotBlank(dto.getDsApellido1())) {
                entity.setDsApellido1(dto.getDsApellido1().toUpperCase());
            }

            if (StringUtils.isNoneBlank(dto.getDsApellido2())) {
                entity.setDsApellido2(dto.getDsApellido2().toUpperCase());
            }

            if (StringUtils.isNoneBlank(dto.getDsEmail())) {
                entity.setDsEmail(dto.getDsEmail().toUpperCase());
            }

            if (dto.getTipoIdentificacionDTO() != null) {
                Integer idValor = dto.getTipoIdentificacionDTO().getIdValor();
                if (idValor != null && idValor != 0) {
                    entity.setTipoIdentificacion(
                            CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoIdentificacionDTO()));
                }
            }

            if (dto.getTipoActorDTO() != null) {
                Integer idValor = dto.getTipoActorDTO().getIdValor();
                if (idValor != null && idValor != 0) {
                    entity.setTipoActor(
                            CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoActorDTO()));
                }
            }

            this.applyListaArea(dto, entity);
            this.applyListaEspecialidad(dto, entity);
            this.applyListaOrganismo(dto, entity);
            this.applyListaTitulacion(dto, entity);

        }

        return entity;
    }

    /**
     * Metodo para convertir lista de área científicas DTO a Entity
     *
     * @param dto
     * @param entity
     */
    private void applyListaArea(InteresadoDTO dto, Interesado entity) {

        if (dto.getListaPersonaAreaDTO() != null && !dto.getListaPersonaAreaDTO().isEmpty()) {
            entity.setListaPersonaArea(new ArrayList<>());
            for (PersonaAreaDTO areaDTO : dto.getListaPersonaAreaDTO()) {
                entity.getListaPersonaArea().add(PersonaAreaToEntity.INSTANCE.apply(areaDTO));
            }
        }
    }

    /**
     * Metodo para convertir lista de especialidad DTO a Entity
     *
     * @param dto
     * @param entity
     */
    private void applyListaEspecialidad(InteresadoDTO dto, Interesado entity) {
        if (dto.getListaPersonaEspecialidadDTO() != null
                && !dto.getListaPersonaEspecialidadDTO().isEmpty()) {
            entity.setListaPersonaEspecialidad(new ArrayList<>());
            for (PersonaEspecDTO especDTO : dto.getListaPersonaEspecialidadDTO()) {
                entity.getListaPersonaEspecialidad()
                        .add(PersonaEspecToEntity.INSTANCE.apply(especDTO));
            }
        }
    }

    /**
     * Metodo para convertir lista de organismo DTO a Entity
     *
     * @param dto
     * @param entity
     */
    private void applyListaOrganismo(InteresadoDTO dto, Interesado entity) {
        if (dto.getListaPersonaOrganismoDTO() != null
                && !dto.getListaPersonaOrganismoDTO().isEmpty()) {
            entity.setListaPersonaOrganismo(new ArrayList<>());
            for (PersonaOrgDTO orgDTO : dto.getListaPersonaOrganismoDTO()) {
                entity.getListaPersonaOrganismo().add(PersonaOrgToEntity.INSTANCE.apply(orgDTO));
            }
        }
    }

    /**
     * Metodo para convertir lista de titulación DTO a Entity
     *
     * @param dto
     * @param entity
     */
    private void applyListaTitulacion(InteresadoDTO dto, Interesado entity) {
        if (dto.getListaPersonaTitulacionDTO() != null
                && !dto.getListaPersonaTitulacionDTO().isEmpty()) {
            entity.setListaPersonaTitulacion(new ArrayList<>());
            for (PersonaTitDTO titDTO : dto.getListaPersonaTitulacionDTO()) {
                entity.getListaPersonaTitulacion().add(PersonaTitToEntity.INSTANCE.apply(titDTO));
            }
        }

    }
}
