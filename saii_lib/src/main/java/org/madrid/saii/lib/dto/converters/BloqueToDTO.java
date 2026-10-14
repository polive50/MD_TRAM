package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import atlas.core.exceptions.ServiceException;

/**
 * Parser Entity to DTO for Bloque
 *
 * @author everis
 */
public enum BloqueToDTO implements Function<Bloque, BloqueDTO> {

    /**
     *
     */
    INSTANCE;

    UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
    /**
     *
     */
    @Override
    public BloqueDTO apply(Bloque entity) {

        BloqueDTO dto = new BloqueDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            dto.setIdImpreso(entity.getImpreso().getIdImpreso());

            if (Objects.nonNull(entity.getLstBloqueComponente())
                    && !entity.getLstBloqueComponente().isEmpty()) {
                Iterator<BloqueComponente> it = entity.getLstBloqueComponente().iterator();
                while (it.hasNext()) {
                    BloqueComponente bloqueComponente = it.next();
                    // Verificar si fcBajaFila es nulo antes de agregar el DTO a la lista
                    if (Objects.isNull(bloqueComponente.getFcBajaFila())) {
                        dto.getLstBloqueComponenteDTO()
                                .add(BloqueComponenteToDTO.INSTANCE.apply(bloqueComponente));
                    }
                }
            }

            if (entity.getIdFormato() != null) {
                // 1. Intentamos obtenerlo del mapa de la caché global
                CatalogosValoresDTO formatoDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdFormato().getIdValor());
                
                // 2. Fallback: Si no está en el mapa, lo convertimos directamente desde la entidad de Hibernate
                if (formatoDTO == null) {
                    formatoDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdFormato());
                }
                
                dto.setIdFormato(formatoDTO);
            }

            if (entity.getIdTematica() != null) {
                // 1. Intentamos obtenerlo del mapa de la caché global
                CatalogosValoresDTO tematicaDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdTematica().getIdValor());
                
                // 2. Fallback: Si no está en el mapa (por falta de constante), usamos el Converter tradicional
                if (tematicaDTO == null) {
                    tematicaDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTematica());
                }
                
                dto.setIdTematica(tematicaDTO);
            }
            
            
            if (entity.getActor()!= null && entity.getActor().getIdActor() != null) {
                if (dto.getActor() == null) {
                    dto.setActor(new ConvocatoriaActDTO());
                }
                dto.getActor().setIdActor(entity.getActor().getIdActor());
            }


            if (StringUtils.isNotBlank(entity.getItAutorizaConsulta())
                    && Constantes.AfirmacionNegacion.S.equals(entity.getItAutorizaConsulta())) {
                dto.setItAutorizaConsulta(Boolean.TRUE);
            } else {
                dto.setItAutorizaConsulta(Boolean.FALSE);
            }
          
            try {
                this.calcularHabilitarSeccionAgregar(dto);
                this.calcularTipo(dto);
            } catch (ServiceException e) {
               // e.printStackTrace();
            }
            
            this.calcularExistenCamposRelacionables(dto);
            this.calcularFilasBloqueComponente(dto);

        }

        return dto;
    }

    /**
     * Calcula variables Boolean para el tipo.
     *
     * @param bloqueDTO
     * @return BloqueDTO
     * @throws ServiceException 
     */
    public BloqueDTO calcularTipo(BloqueDTO bloqueDTO) throws ServiceException {
        
        CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
        
        Integer idFormato = catalogosValoresService.buscarId(bloqueDTO.getIdFormato().getIdValor()).getNmConstante();

        bloqueDTO.setEsTipoTexto(Boolean.FALSE);
        bloqueDTO.setEsTipoTextoFijo(Boolean.FALSE);
        bloqueDTO.setEsTipoDocumento(Boolean.FALSE);
        bloqueDTO.setEsTipoFormulario(Boolean.FALSE);
        bloqueDTO.setEsTipoFormularioContrato(Boolean.FALSE);
        bloqueDTO.setEsTipoListado(Boolean.FALSE);
        bloqueDTO.setEsTipoTabla(Boolean.FALSE);
        bloqueDTO.setEsTipoParticipantes(Boolean.FALSE);
        bloqueDTO.setEsTipoPresupuestoPresentado(Boolean.FALSE);
        bloqueDTO.setEsTipoAyudaSolicitada(Boolean.FALSE);
        bloqueDTO.setEsTipoActuaciones(Boolean.FALSE);
        bloqueDTO.setEsTipoPlazosEjecAnual(Boolean.FALSE);
        bloqueDTO.setEsTipoPersonalSinCargo(Boolean.FALSE);
        bloqueDTO.setEsTipoPersonalConCargo(Boolean.FALSE);
        bloqueDTO.setEsTipoPresupuestoFinanciable(Boolean.FALSE);
        bloqueDTO.setEsTipoFormularioContratoRenuncia(Boolean.FALSE); 
        bloqueDTO.setEsTipoFormularioContratoSusti(Boolean.FALSE); 
        bloqueDTO.setEsTipoOtrasModificaciones(Boolean.FALSE);
        bloqueDTO.setEsTipoObservaciones(Boolean.FALSE);
        bloqueDTO.setEsTipoNotificacion(Boolean.FALSE);
        bloqueDTO.setEsTipoMultiregistro(Boolean.FALSE);
        bloqueDTO.setEsTipoFormularioContratoRenuncia(Boolean.FALSE); 
        bloqueDTO.setEsCuentaBancaria(Boolean.FALSE);
        bloqueDTO.setEsTipoDiscapacidad(Boolean.FALSE);
        bloqueDTO.setEsTipoIntensidadAyuda(Boolean.FALSE);

        if (Constantes.Formatos.ID_TEXTO.equals(idFormato)) {
            bloqueDTO.setEsTipoTexto(Boolean.TRUE);
            if (bloqueDTO.getIdTematica() != null && bloqueDTO.getIdTematica().getDsValor() != null) {
                if(bloqueDTO.getIdTematica().getDsValor().equalsIgnoreCase("Observaciones")) {
                    bloqueDTO.setEsTipoObservaciones(Boolean.TRUE);
                }else if (bloqueDTO.getIdTematica().getDsValor().equalsIgnoreCase("Otras modificaciones")) {
                    bloqueDTO.setEsTipoOtrasModificaciones(Boolean.TRUE);
                }
            }
        }

        if (Constantes.Formatos.ID_TEXTO_FIJO.equals(idFormato)) {
            bloqueDTO.setEsTipoTextoFijo(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_DOCUMENTO.equals(idFormato)) {
            bloqueDTO.setEsTipoDocumento(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FORMULARIO.equals(idFormato)) {
            bloqueDTO.setEsTipoFormulario(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL.equals(idFormato)) {
            bloqueDTO.setEsTipoFormularioContrato(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_DOCUMENTO.equals(idFormato)) {
            bloqueDTO.setEsTipoFormularioContrato(Boolean.TRUE);
            bloqueDTO.setEsTipoFormularioContratoRenuncia(Boolean.TRUE);
            bloqueDTO.setEsTipoFormularioContratoSusti(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_LISTADO.equals(idFormato)) {
            bloqueDTO.setEsTipoListado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_TABLA.equals(idFormato)) {
            bloqueDTO.setEsTipoTabla(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PARTICIPANTES.equals(idFormato)) {
            bloqueDTO.setEsTipoParticipantes(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(idFormato)) {
            bloqueDTO.setEsTipoPresupuestoPresentado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(idFormato)) {
            bloqueDTO.setEsTipoAyudaSolicitada(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(idFormato)) {
            bloqueDTO.setEsTipoActuaciones(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL.equals(idFormato)) {
            bloqueDTO.setEsTipoPlazosEjecAnual(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_CON_CARGO.equals(idFormato)) {
            bloqueDTO.setEsTipoPersonalConCargo(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_SIN_CARGO.equals(idFormato)) {
            bloqueDTO.setEsTipoPersonalSinCargo(Boolean.TRUE);
        }
                
        if (Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE.equals(idFormato)) {
            bloqueDTO.setEsTipoPresupuestoFinanciable(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA.equals(idFormato)) {
            bloqueDTO.setEsTipoFormularioContratoRenuncia(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION.equals(idFormato)) {
            bloqueDTO.setEsTipoFormularioContratoSusti(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FORMULARIO_NOTIFICACION.equals(idFormato)) {
            bloqueDTO.setEsTipoNotificacion(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_CUENTA_BANCARIA.equals(idFormato)) {
            bloqueDTO.setEsCuentaBancaria(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_PERSONA_SOLICITANTE.equals(idFormato)) {
            bloqueDTO.setEsTipoPersonaSolicitante(Boolean.TRUE);
        }
        //VMM 03/06/25 MD-1522: contemplamos el tipo bloque multiregistro //GIM 16/06/25 añadimos al if el comprobar que no venga nulo
        if (bloqueDTO.getItMultiRegistro() != null && bloqueDTO.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)) {
            bloqueDTO.setEsTipoMultiregistro(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_DISCAPACIDAD.equals(idFormato)) {
            bloqueDTO.setEsTipoDiscapacidad(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD.equals(idFormato)) {
            bloqueDTO.setEsTipoIntensidadAyuda(Boolean.TRUE);
        }
        
        return bloqueDTO;

    }
    
    
    /**
     * Calcula variables Boolean para el tipo.
     *
     * @param bloqueDTO
     * @return BloqueDTO
     * @throws ServiceException 
     */
    public BloqueDTO calcularTipoMultiple(BloqueDTO bloqueMultipleDTO) throws ServiceException {

        CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
        Integer idFormato = catalogosValoresService.buscarId(bloqueMultipleDTO.getIdFormato().getIdValor()).getNmConstante();

        bloqueMultipleDTO.setEsTipoTexto(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoTextoFijo(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoDocumento(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoFormulario(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoFormularioContrato(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoListado(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoTabla(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoParticipantes(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoPresupuestoPresentado(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoAyudaSolicitada(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoActuaciones(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoPlazosEjecAnual(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoPersonalSinCargo(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoPersonalConCargo(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoPresupuestoFinanciable(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoFormularioContratoRenuncia(Boolean.FALSE); 
        bloqueMultipleDTO.setEsTipoFormularioContratoSusti(Boolean.FALSE); 
        bloqueMultipleDTO.setEsTipoOtrasModificaciones(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoObservaciones(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoMultiregistro(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoDiscapacidad(Boolean.FALSE);
        bloqueMultipleDTO.setEsTipoIntensidadAyuda(Boolean.FALSE);

        if (Constantes.Formatos.ID_TEXTO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoTexto(Boolean.TRUE);
            if (bloqueMultipleDTO.getIdTematica() != null && bloqueMultipleDTO.getIdTematica().getDsValor() != null) {
                if(bloqueMultipleDTO.getIdTematica().getDsValor().equalsIgnoreCase("Observaciones")) {
                    bloqueMultipleDTO.setEsTipoObservaciones(Boolean.TRUE);
                }else if (bloqueMultipleDTO.getIdTematica().getDsValor().equalsIgnoreCase("Otras modificaciones")) {
                    bloqueMultipleDTO.setEsTipoOtrasModificaciones(Boolean.TRUE);
                }
            }
        }

        if (Constantes.Formatos.ID_TEXTO_FIJO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoTextoFijo(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_DOCUMENTO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoDocumento(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FORMULARIO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoFormulario(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoFormularioContrato(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_LISTADO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoListado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_TABLA.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoTabla(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PARTICIPANTES.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoParticipantes(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoPresupuestoPresentado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoAyudaSolicitada(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoActuaciones(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoPlazosEjecAnual(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_CON_CARGO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoPersonalConCargo(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_SIN_CARGO.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoPersonalSinCargo(Boolean.TRUE);
        }
                
        if (Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoPresupuestoFinanciable(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoFormularioContratoRenuncia(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoFormularioContratoSusti(Boolean.TRUE);
        }
        
        //VMM 03/06/25 MD-1522: contemplamos el tipo bloque multiregistro
        if (bloqueMultipleDTO.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)) {
            bloqueMultipleDTO.setEsTipoMultiregistro(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_DISCAPACIDAD.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoDiscapacidad(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD.equals(idFormato)) {
            bloqueMultipleDTO.setEsTipoIntensidadAyuda(Boolean.TRUE);
        }

        return bloqueMultipleDTO;

    }
    
    /**
     * Calcula variables Boolean para el tipo.
     *
     * @param bloqueDTO
     * @return BloqueDTO
     * @throws ServiceException 
     */
    public BloqueDTO calcularTipoMultipleOne(BloqueDTO bloqueMultipleDTOOne) throws ServiceException {

        CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
        Integer idFormato = catalogosValoresService.buscarId(bloqueMultipleDTOOne.getIdFormato().getIdValor()).getNmConstante();

        bloqueMultipleDTOOne.setEsTipoTexto(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoTextoFijo(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoDocumento(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoFormulario(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoFormularioContrato(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoListado(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoTabla(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoParticipantes(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoPresupuestoPresentado(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoAyudaSolicitada(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoActuaciones(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoPlazosEjecAnual(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoPersonalSinCargo(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoPersonalConCargo(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoPresupuestoFinanciable(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoFormularioContratoRenuncia(Boolean.FALSE); 
        bloqueMultipleDTOOne.setEsTipoFormularioContratoSusti(Boolean.FALSE); 
        bloqueMultipleDTOOne.setEsTipoOtrasModificaciones(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoObservaciones(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoMultiregistro(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoDiscapacidad(Boolean.FALSE);
        bloqueMultipleDTOOne.setEsTipoIntensidadAyuda(Boolean.FALSE);

        if (Constantes.Formatos.ID_TEXTO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoTexto(Boolean.TRUE);
            if (bloqueMultipleDTOOne.getIdTematica() != null && bloqueMultipleDTOOne.getIdTematica().getDsValor() != null) {
                if(bloqueMultipleDTOOne.getIdTematica().getDsValor().equalsIgnoreCase("Observaciones")) {
                    bloqueMultipleDTOOne.setEsTipoObservaciones(Boolean.TRUE);
                }else if (bloqueMultipleDTOOne.getIdTematica().getDsValor().equalsIgnoreCase("Otras modificaciones")) {
                    bloqueMultipleDTOOne.setEsTipoOtrasModificaciones(Boolean.TRUE);
                }
            }
        }

        if (Constantes.Formatos.ID_TEXTO_FIJO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoTextoFijo(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_DOCUMENTO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoDocumento(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FORMULARIO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoFormulario(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoFormularioContrato(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_LISTADO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoListado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_TABLA.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoTabla(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PARTICIPANTES.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoParticipantes(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoPresupuestoPresentado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoAyudaSolicitada(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoActuaciones(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoPlazosEjecAnual(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_CON_CARGO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoPersonalConCargo(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_SIN_CARGO.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoPersonalSinCargo(Boolean.TRUE);
        }
                
        if (Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoPresupuestoFinanciable(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoFormularioContratoRenuncia(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoFormularioContratoSusti(Boolean.TRUE);
        }
        
        //VMM 03/06/25 MD-1522: contemplamos el tipo bloque multiregistro
        if (bloqueMultipleDTOOne.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)) {
            bloqueMultipleDTOOne.setEsTipoMultiregistro(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_DISCAPACIDAD.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoDiscapacidad(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD.equals(idFormato)) {
            bloqueMultipleDTOOne.setEsTipoIntensidadAyuda(Boolean.TRUE);
        }
        
        return bloqueMultipleDTOOne;

    }
    
    
    /**
     * Calcula variables Boolean para el tipo.
     *
     * @param bloqueDTO
     * @return BloqueDTO
     * @throws ServiceException 
     */
    public BloqueDTO calcularTipoMultipleTwo(BloqueDTO bloqueMultipleDTOTwo) throws ServiceException {

        CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
        Integer idFormato = catalogosValoresService.buscarId(bloqueMultipleDTOTwo.getIdFormato().getIdValor()).getNmConstante();

        bloqueMultipleDTOTwo.setEsTipoTexto(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoTextoFijo(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoDocumento(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoFormulario(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoFormularioContrato(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoListado(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoTabla(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoParticipantes(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoPresupuestoPresentado(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoAyudaSolicitada(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoActuaciones(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoPlazosEjecAnual(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoPersonalSinCargo(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoPersonalConCargo(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoPresupuestoFinanciable(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoFormularioContratoRenuncia(Boolean.FALSE); 
        bloqueMultipleDTOTwo.setEsTipoFormularioContratoSusti(Boolean.FALSE); 
        bloqueMultipleDTOTwo.setEsTipoOtrasModificaciones(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoObservaciones(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoMultiregistro(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoDiscapacidad(Boolean.FALSE);
        bloqueMultipleDTOTwo.setEsTipoIntensidadAyuda(Boolean.FALSE);

        if (Constantes.Formatos.ID_TEXTO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoTexto(Boolean.TRUE);
            if (bloqueMultipleDTOTwo.getIdTematica() != null && bloqueMultipleDTOTwo.getIdTematica().getDsValor() != null) {
                if(bloqueMultipleDTOTwo.getIdTematica().getDsValor().equalsIgnoreCase("Observaciones")) {
                    bloqueMultipleDTOTwo.setEsTipoObservaciones(Boolean.TRUE);
                }else if (bloqueMultipleDTOTwo.getIdTematica().getDsValor().equalsIgnoreCase("Otras modificaciones")) {
                    bloqueMultipleDTOTwo.setEsTipoOtrasModificaciones(Boolean.TRUE);
                }
            }
        }

        if (Constantes.Formatos.ID_TEXTO_FIJO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoTextoFijo(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_DOCUMENTO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoDocumento(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FORMULARIO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoFormulario(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoFormularioContrato(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_LISTADO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoListado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_TABLA.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoTabla(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PARTICIPANTES.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoParticipantes(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoPresupuestoPresentado(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoAyudaSolicitada(Boolean.TRUE);
        }

        if (Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoActuaciones(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoPlazosEjecAnual(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_CON_CARGO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoPersonalConCargo(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PERSONAL_SIN_CARGO.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoPersonalSinCargo(Boolean.TRUE);
        }
                
        if (Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoPresupuestoFinanciable(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoFormularioContratoRenuncia(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoFormularioContratoSusti(Boolean.TRUE);
        }
        
        //VMM 03/06/25 MD-1522: contemplamos el tipo bloque multiregistro
        if (bloqueMultipleDTOTwo.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)) {
            bloqueMultipleDTOTwo.setEsTipoMultiregistro(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_DISCAPACIDAD.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoDiscapacidad(Boolean.TRUE);
        }
        
        if (Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD.equals(idFormato)) {
            bloqueMultipleDTOTwo.setEsTipoIntensidadAyuda(Boolean.TRUE);
        }

        return bloqueMultipleDTOTwo;

    }

    /**
     * Calcula si el bloque contiene algún campo relacionable
     *
     * @param bloqueDTO
     */
    private void calcularExistenCamposRelacionables(BloqueDTO bloqueDTO) {

        // Sólo hay campos relacionables en los formularios
        if (Boolean.FALSE.equals(bloqueDTO.getEsTipoFormulario())) {
            bloqueDTO.setTieneCamposRelacionables(Boolean.FALSE);
            return;
        }

        // Calculamos si este bloque puede tener campos relacionables a true
        bloqueDTO.setTieneCamposRelacionables(bloqueDTO.getLstBloqueComponenteDTO().stream()
                .filter(x -> Boolean.TRUE.equals(x.getItRelacion())).count() > 0);
    }

    /**
     *
     * @param bloqueDTO
     */
    private void calcularFilasBloqueComponente(BloqueDTO bloqueDTO) {
        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())) {
            bloqueDTO.setLstBloqueComponentesFilaDTO(bloqueDTO.getLstBloqueComponenteDTO().stream()
                    .filter(f -> Boolean.TRUE.equals(f.getItFila()
                            && Objects.isNull(f.getFcBajaFila()))).collect(Collectors.toList()));
        }
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(BloqueDTO dto, Bloque entity) {

        if (StringUtils.isBlank(entity.getTlAuxiliar())) {
            dto.setTlAuxiliar(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getTlAuxiliarInf())) {
            dto.setTlAuxiliarInf(StringUtils.EMPTY);
        }
    }

    /**
     *
     * @param dto
     */
    private void calcularHabilitarSeccionAgregar(BloqueDTO dto) {

        dto.setHabilitarSeccionAgregarComponentes(Boolean.FALSE);
        dto.setHabilitarSeccionAgregarDocumentos(Boolean.FALSE);

        if (Objects.nonNull(dto.getIdFormato())) {
            
            UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
            
            List<Integer> lstFormatos = new ArrayList<>();
            lstFormatos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO));
            lstFormatos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO));
            lstFormatos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA));

            if (lstFormatos.contains(dto.getIdFormato().getIdValor())) {
                dto.setHabilitarSeccionAgregarComponentes(Boolean.TRUE);
            }

            lstFormatos = new ArrayList<>();
            lstFormatos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO));

            if (lstFormatos.contains(dto.getIdFormato().getIdValor())) {
                dto.setHabilitarSeccionAgregarDocumentos(Boolean.TRUE);
            }

        }

    }

}
