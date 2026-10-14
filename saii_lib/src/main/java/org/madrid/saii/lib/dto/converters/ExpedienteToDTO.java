package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;
import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Expediente
 *
 * @author quental
 */
public enum ExpedienteToDTO implements Function<Expediente, ExpedienteDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
//    public ExpedienteDTO apply(Expediente entity) {
//        
//        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
//        
//        ExpedienteDTO dto = new ExpedienteDTO();
//
//        if (entity != null) {
//
//            BeanUtils.copyProperties(entity, dto);
//            this.replaceNull(dto, entity);
//
//            if (entity.getInteresado() != null) {
//                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
//            }
//            
//            if (entity.getRepresentanteLegal() != null) {
//                dto.setRepresentanteLegal(InteresadoToDTO.INSTANCE.apply(entity.getRepresentanteLegal()));
//            }
//
//            if (entity.getEstado() != null) {
//                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
//            }
//
//            if (entity.getEstadoBenef() != null) {
//                dto.setEstadoBeneficiarioDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoBenef()));
//            }
//
//            if (entity.getConvocatoriaAct() != null) {
//                dto.setConvocatoriaActDTO(
//                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaAct()));
//            }
//
//            if (entity.getEstadoProvisional() != null) {
//                dto.setEstadoProvisionalDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoProvisional()));
//            }
//            
//            dto.setImpresoDTO(new ImpresoDTO());
//            
//            dto.setImpresoJustecDTO(new ImpresoDTO());
//
//            dto.setImpresoJustecFinalDTO(new ImpresoDTO());
//            
//            dto.setImpresoJusEcoDTO(new ImpresoDTO());
//
//            dto.setImpresoJusEcoFinalDTO(new ImpresoDTO());
//            
//            dto.setImpresoContratoDTO(new ImpresoDTO());
//            
//            dto.setImpresoAceptacionDTO(new ImpresoDTO());
//
//            if (entity.getLstExpedienteDoc() != null) {
//                for (ExpedienteDoc expedienteDoc : new ArrayList<>(entity.getLstExpedienteDoc())) {
//                    dto.getLstExpedienteDocDTO()
//                            .add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
//                }
//            }
//
//            if (entity.getLstExpedienteReq() != null) {
//                for (ExpedienteReq expedienteReq : new ArrayList<>(entity.getLstExpedienteReq())) {
//                    dto.getLstExpedienteReqDTO()
//                            .add(ExpedienteReqToDTO.INSTANCE.apply(expedienteReq));
//                }
//            }
//
//            if (entity.getEstadoRevAdmin() != null) {
//                dto.setEstadoRevAdminDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevAdmin()));
//            }
//
//            if (entity.getEstadoRevRequi() != null) {
//                dto.setEstadoRevRequiDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevRequi()));
//            }
//            
//            if (entity.getEstadoRevFinan() != null) {
//                dto.setEstadoRevFinanDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevFinan()));
//            }
//            
//            if(entity.getEstadoRevJusTec() != null) {
//                dto.setEstadoRevJusTecDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevJusTec()));
//            }
//            
//            if(entity.getEstadoRevJusEco() != null) {
//                dto.setEstadoRevJusEcoDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevJusEco()));
//            }
//            
//            if(entity.getEstadoRevContrato()!= null) {
//                dto.setEstadoRevContratoDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevContrato()));
//            }
//            
//
//            if (entity.getEstadoRevEval() != null) {
//                dto.setEstadoRevEvalDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevEval()));
//
//                List<Integer> lstEstados = new ArrayList<>();
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));
//                
//
//                if (lstEstados.contains(dto.getEstadoRevEvalDTO().getIdValor())) {
//                    dto.setEsCerradoCoordinadorTodo(Boolean.TRUE);
//                    dto.setMostrarBotoneraAprobarRechazarEvaluacion(Boolean.FALSE);
//                }
//                
//                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)
//                        .equals(dto.getEstadoDTO().getIdValor())
//                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)
//                                .equals(dto.getEstadoDTO().getIdValor())) {
//                    dto.setSePuedeDescargarInformeCoor(Boolean.TRUE);
//                } else {
//                    dto.setSePuedeDescargarInformeCoor(Boolean.FALSE);
//                }
//            }    
//            
//            if(entity.getSolicitud() != null) {
//                dto.setIdSolicitud(entity.getSolicitud().getIdSolicitud());
//            }
//            
//
//            if (entity.getConvocatoriaArea() != null) {
//                dto.setConvocatoriaAreaDTO(
//                        ConvocatoriaAreaToDTO.INSTANCE.apply(entity.getConvocatoriaArea()));
//            }
//            
//            if (entity.getConvocatoriaAreaSubarea() != null) {
//                dto.setConvocatoriaAreaSubareaDTO(
//                        ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(entity.getConvocatoriaAreaSubarea()));
//            }
//            
//            if(entity.getConvocatoriaSubmod() != null) {
//                dto.setConvocatoriaSubmodDTO(ConvocatoriaSubmodToDTO.INSTANCE.apply(entity.getConvocatoriaSubmod()));
//                
//            }
//
//            dto.setEsEstadoEvalEnEvaluacion(this.calcularEstadoEvalEnEvaluacion(dto));
//
//            this.calcularResolucionProvisional(dto);
//            this.calcularExpedienteSolicitante(dto);
//            this.calcularEstadoProvisional(dto);
//            this.calcularColorFondoText(dto);
//            this.calcularEstadoDefinitivo(dto);
//            this.calcularFondoResolucionDefinitiva(dto);
//            this.calcularEstadoDesistidoExcluido(dto);
//            this.avisoDocumento(dto);
//            this.avisoRequisitos(dto);
//            this.avisoEvaluacion(dto);
//            this.avisoResolucion(dto);
//            this.avisoJustificacion(dto);
//            this.avisoJustificacionEco(dto);
//
//            this.aviso(dto);
//
//            if (entity.getIdPersonaSolicitante() != null && entity.getIdPersonaSolicitante().getIdValor() != null) {
//                dto.setTipoPersonaSolicitudDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdPersonaSolicitante()));
//            }
//
//            if (entity.getIdMedioNotificacion() != null && entity.getIdMedioNotificacion().getIdValor() != null) {
//                dto.setMedioComunicacionDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdMedioNotificacion()));
//            }
//            
//        }
//
//        return dto;
//    }

    public ExpedienteDTO apply(Expediente entity) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        ExpedienteDTO dto = new ExpedienteDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getInteresado() != null) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            }
            
            if (entity.getRepresentanteLegal() != null) {
                dto.setRepresentanteLegal(InteresadoToDTO.INSTANCE.apply(entity.getRepresentanteLegal()));
            }

            if(entity.getAutorizado() != null) {
                dto.setAutorizado(InteresadoToDTO.INSTANCE.apply(entity.getAutorizado()));
            }
			// === FALLBACK: ESTADO ===
            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstado().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado());
                }
                dto.setEstadoDTO(catDTO);
            }

            // === FALLBACK: ESTADO BENEFICIARIO ===
            if (entity.getEstadoBenef() != null && entity.getEstadoBenef().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoBenef().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoBenef());
                }
                dto.setEstadoBeneficiarioDTO(catDTO);
            }

            if (entity.getConvocatoriaAct() != null) {
                dto.setConvocatoriaActDTO(
                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaAct()));
            }

            // === FALLBACK: ESTADO PROVISIONAL ===
            if (entity.getEstadoProvisional() != null && entity.getEstadoProvisional().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoProvisional().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoProvisional());
                }
                dto.setEstadoProvisionalDTO(catDTO);
            }
            
            dto.setImpresoDTO(new ImpresoDTO());
            dto.setImpresoJustecDTO(new ImpresoDTO());
            dto.setImpresoJustecFinalDTO(new ImpresoDTO());
            dto.setImpresoJusEcoDTO(new ImpresoDTO());
            dto.setImpresoJusEcoFinalDTO(new ImpresoDTO());
            dto.setImpresoContratoDTO(new ImpresoDTO());
            dto.setImpresoAceptacionDTO(new ImpresoDTO());

            if (entity.getLstExpedienteDoc() != null) {
                for (ExpedienteDoc expedienteDoc : new ArrayList<>(entity.getLstExpedienteDoc())) {
                    dto.getLstExpedienteDocDTO()
                            .add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
                }
            }

            if (entity.getLstExpedienteReq() != null) {
                for (ExpedienteReq expedienteReq : new ArrayList<>(entity.getLstExpedienteReq())) {
                    dto.getLstExpedienteReqDTO()
                            .add(ExpedienteReqToDTO.INSTANCE.apply(expedienteReq));
                }
            }

            // === FALLBACK: ESTADO REVISIÓN ADMINISTRATIVA ===
            if (entity.getEstadoRevAdmin() != null && entity.getEstadoRevAdmin().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevAdmin().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevAdmin());
                }
                dto.setEstadoRevAdminDTO(catDTO);
            }

            // === FALLBACK: ESTADO REVISIÓN REQUISITOS ===
            if (entity.getEstadoRevRequi() != null && entity.getEstadoRevRequi().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevRequi().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevRequi());
                }
                dto.setEstadoRevRequiDTO(catDTO);
            }
            
            // === FALLBACK: ESTADO REVISIÓN FINANCIERA ===
            if (entity.getEstadoRevFinan() != null && entity.getEstadoRevFinan().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevFinan().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevFinan());
                }
                dto.setEstadoRevFinanDTO(catDTO);
            }
            
            // === FALLBACK: ESTADO REVISIÓN JUSTIFICACIÓN TÉCNICA ===
            if (entity.getEstadoRevJusTec() != null && entity.getEstadoRevJusTec().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevJusTec().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevJusTec());
                }
                dto.setEstadoRevJusTecDTO(catDTO);
            }
            
            // === FALLBACK: ESTADO REVISIÓN JUSTIFICACIÓN ECONÓMICA ===
            if (entity.getEstadoRevJusEco() != null && entity.getEstadoRevJusEco().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevJusEco().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevJusEco());
                }
                dto.setEstadoRevJusEcoDTO(catDTO);
            }
            
            // === FALLBACK: ESTADO REVISIÓN CONTRATO ===
            if (entity.getEstadoRevContrato() != null && entity.getEstadoRevContrato().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevContrato().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevContrato());
                }
                dto.setEstadoRevContratoDTO(catDTO);
            }
            
            // === FALLBACK: ESTADO REVISIÓN EVALUACIÓN ===
            if (entity.getEstadoRevEval() != null && entity.getEstadoRevEval().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstadoRevEval().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevEval());
                }
                dto.setEstadoRevEvalDTO(catDTO);

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));
                

                if (lstEstados.contains(dto.getEstadoRevEvalDTO().getIdValor())) {
                    dto.setEsCerradoCoordinadorTodo(Boolean.TRUE);
                    dto.setMostrarBotoneraAprobarRechazarEvaluacion(Boolean.FALSE);
                }
                
                // La validación de seguridad de dto.getEstadoDTO() se mantiene activa y robusta
                if (dto.getEstadoDTO() != null && (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)
                        .equals(dto.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)
                                .equals(dto.getEstadoDTO().getIdValor()))) {
                    dto.setSePuedeDescargarInformeCoor(Boolean.TRUE);
                } else {
                    dto.setSePuedeDescargarInformeCoor(Boolean.FALSE);
                }
            }    
            
            if (entity.getSolicitud() != null) {
                dto.setIdSolicitud(entity.getSolicitud().getIdSolicitud());
            }
            
            if (entity.getConvocatoriaArea() != null) {
                dto.setConvocatoriaAreaDTO(
                        ConvocatoriaAreaToDTO.INSTANCE.apply(entity.getConvocatoriaArea()));
            }
            
            if (entity.getConvocatoriaAreaSubarea() != null) {
                dto.setConvocatoriaAreaSubareaDTO(
                        ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(entity.getConvocatoriaAreaSubarea()));
            }
            
            if (entity.getConvocatoriaSubmod() != null) {
                dto.setConvocatoriaSubmodDTO(ConvocatoriaSubmodToDTO.INSTANCE.apply(entity.getConvocatoriaSubmod()));
            }

            dto.setEsEstadoEvalEnEvaluacion(this.calcularEstadoEvalEnEvaluacion(dto));

            this.calcularResolucionProvisional(dto);
            this.calcularExpedienteSolicitante(dto);
            this.calcularEstadoProvisional(dto);
            this.calcularColorFondoText(dto);
            this.calcularEstadoDefinitivo(dto);
            this.calcularFondoResolucionDefinitiva(dto);
            this.calcularEstadoDesistidoExcluido(dto);
            this.avisoDocumento(dto);
            this.avisoRequisitos(dto);
            this.avisoEvaluacion(dto);
            this.avisoResolucion(dto);
            this.avisoJustificacion(dto);
            this.avisoJustificacionEco(dto);

            this.aviso(dto);

            // === FALLBACK: TIPO PERSONA SOLICITANTE ===
            if (entity.getIdPersonaSolicitante() != null && entity.getIdPersonaSolicitante().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdPersonaSolicitante().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdPersonaSolicitante());
                }
                dto.setTipoPersonaSolicitudDTO(catDTO);
            }

            // === FALLBACK: MEDIO NOTIFICACIÓN ===
            if (entity.getIdMedioNotificacion() != null && entity.getIdMedioNotificacion().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdMedioNotificacion().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdMedioNotificacion());
                }
                dto.setMedioComunicacionDTO(catDTO);
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
    private void replaceNull(ExpedienteDTO dto, Expediente entity) {

        if (StringUtils.isBlank(entity.getCdExpediente())) {
            dto.setCdExpediente(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getCdEreg())) {
            dto.setCdEreg(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getCdSiex())) {
            dto.setCdSiex(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsDestinoEreg())) {
            dto.setDsDestinoEreg(StringUtils.EMPTY);
        }

    }

    /**
     * Metodo que calcula si se pueden anadir coordinadores
     *
     * @param dto
     * @return 
     */
    private Boolean calcularEstadoEvalEnEvaluacion(ExpedienteDTO dto) {
        Boolean bSalida = Boolean.FALSE;
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION)
                .equals(dto.getEstadoRevEvalDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR)
                        .equals(dto.getEstadoRevEvalDTO().getIdValor())) {
            bSalida = Boolean.TRUE;
        }
        return bSalida;

    }

    /**
     * Método para validar la activación de opciones de calificación
     *
     * @param dto Datos de expediente 
     */
    public ExpedienteDTO calcularResolucionProvisional(ExpedienteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);

        dto.setBlMostrarMensajeAprobarDenegarProvisional1(Boolean.FALSE);

        List<Integer> lstEstadoEval = new ArrayList<>();
        lstEstadoEval.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));
        lstEstadoEval.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));

        if (!utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK).equals(dto.getEstadoRevAdminDTO().getIdValor())
                || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                || !lstEstadoEval.contains(dto.getEstadoRevEvalDTO().getIdValor())) {
            dto.setBlMostrarMensajeAprobarDenegarProvisional1(Boolean.TRUE);
        }

        dto.setBlMostrarMensajeAprobarDenegarProvisional2(Boolean.FALSE);

        List<Integer> lsEstado = new ArrayList<>();
        lsEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA));
        lsEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lsEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));

        if (lsEstado.contains(dto.getEstadoDTO().getIdValor())) {
            dto.setBlMostrarMensajeAprobarDenegarProvisional2(Boolean.TRUE);
        }

        // Habilitar Resolucion Provisional
        dto.setBlHabilitarResolucionProvisional(Boolean.FALSE);

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setBlHabilitarResolucionProvisional(Boolean.TRUE);
        }

        return dto;

    }

    /**
     * método para calular el si el expediente es de tipo solicitante
     * solicitante
     *
     * @param dto
     */
    private void calcularExpedienteSolicitante(ExpedienteDTO dto) {

        dto.setEsExpedienteSolicitante(Boolean.FALSE);
        dto.setEsExpedienteCoordinador(Boolean.FALSE);
        dto.setEsExpedienteParticipante(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                .equals(dto.getConvocatoriaActDTO().getTipoActorDTO().getIdValor())) {
            dto.setEsExpedienteSolicitante(Boolean.TRUE);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                .equals(dto.getConvocatoriaActDTO().getTipoActorDTO().getIdValor())) {
            dto.setEsExpedienteCoordinador(Boolean.TRUE);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE)
                .equals(dto.getConvocatoriaActDTO().getTipoActorDTO().getIdValor())) {
            dto.setEsExpedienteParticipante(Boolean.TRUE);
        }
        
        if (dto.getConvocatoriaActDTO().getItContrato()) {
            dto.setEsExpedienteContrato(Boolean.TRUE);
        }

    }

    /**
     * Calcula si el estado es Provisional
     *
     * @param dto Datos de expediente
     */
    private void calcularEstadoProvisional(ExpedienteDTO dto) {

        dto.setBlEstadoProvisional(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        List<Integer> lstEstadoPermitido = new ArrayList<>();
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION));
        lstEstadoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES));

        if (lstEstadoPermitido.contains(dto.getEstadoProvisionalDTO().getIdValor())) {
            dto.setBlEstadoProvisional(Boolean.TRUE);
        }

    }

    /**
     * Calcula el color de Fondo.
     *
     * @param dto Datos de expediente
     */
    private void calcularColorFondoText(ExpedienteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)
                .equals(dto.getEstadoProvisionalDTO().getIdValor())) {
            dto.setColorFondoText("colorInputTextVerde");

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(dto.getEstadoProvisionalDTO().getIdValor())
                ||utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL).equals(dto.getEstadoProvisionalDTO().getIdValor())
                ||utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL).equals(dto.getEstadoProvisionalDTO().getIdValor())) {
            dto.setColorFondoText("colorInputTextRojo");
        }

    }

    /**
     * Calcula si el estado es Definitivo
     *
     * @param dto Datos de expediente
     */
    private void calcularEstadoDefinitivo(ExpedienteDTO dto) {

        dto.setBlEstadoDefinitivo(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO).equals(dto.getEstadoDTO().getIdValor())) {

            dto.setBlEstadoDefinitivo(Boolean.TRUE);
        }

    }

    /**
     *
     */
    private void calcularFondoResolucionDefinitiva(ExpedienteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if ( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO).equals(dto.getEstadoDTO().getIdValor())
              || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
              || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST).equals(dto.getEstadoDTO().getIdValor())
              || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setColorFondoTextResolucionDef("colorInputTextVerde");

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setColorFondoTextResolucionDef("colorInputTextRojo");
        }
    }

    /**
     * Calcula si el estado es Desistido o Excluido
     *
     * @param dto Datos de expediente
     */
    private void calcularEstadoDesistidoExcluido(ExpedienteDTO dto) {

        dto.setModoConsulta(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(dto.getEstadoDTO().getIdValor())) {

            dto.setModoConsulta(Boolean.TRUE);
        }

    }

    /**
     * Calcula si el estado es aviso de documento
     *
     * @param dto Datos de expediente
     */
    private void avisoDocumento(ExpedienteDTO dto) {

        dto.setBAvisoDocumento(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                .equals(dto.getEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(dto.getEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)
                        .equals(dto.getEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
                        .equals(dto.getEstadoRevAdminDTO().getIdValor()))
                // MD-2397 Incluimos la condición de que no sea excluido
                // provisional el estado provisional
                && !utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)
                        .equals(dto.getEstadoProvisionalDTO().getIdValor())
                && !Boolean.TRUE.equals(dto.getModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es modo consulta

            dto.setBAvisoDocumento(Boolean.TRUE);
        }
        
        Integer idEstDocEnvColNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
        List<ExpedienteDocDTO> lstExpedienteDocDTO = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> ed.getEstadoDTO().getIdValor()
                        .equals(idEstDocEnvColNot))
                .collect(Collectors.toList());
        
        if (!lstExpedienteDocDTO.isEmpty()) {
            dto.setBAvisoDocumento(Boolean.FALSE);
        }

    }

    /**
     * Calcula si el estado es aviso de requisitos
     *
     * @param dto Datos de expediente 
     */
    private void avisoRequisitos(ExpedienteDTO dto) {

        dto.setBAvisoRequisitos(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                .equals(dto.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())) 
                // MD-2397 Incluimos la condición de que no sea desistido provisional el estado provisional
                && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)
                        .equals(dto.getEstadoProvisionalDTO().getIdValor())
                && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL) // MD-2399 Incluimos estado provisional en excluido provisional
                        .equals(dto.getEstadoProvisionalDTO().getIdValor())
                && !Boolean.TRUE.equals(dto.getModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es modo consulta

            dto.setBAvisoRequisitos(Boolean.TRUE);
        }

    }

    /**
     * Calcula si el estado es aviso de evaluacion
     *
     * @param dto Datos de expediente 
     */
    private void avisoEvaluacion(ExpedienteDTO dto) {

        dto.setBAvisoEvaluacion(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR)
                .equals(dto.getEstadoRevEvalDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR)
                        .equals(dto.getEstadoRevEvalDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION)
                        .equals(dto.getEstadoRevEvalDTO().getIdValor())
                && !Boolean.TRUE.equals(dto.getModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es modo consulta

            dto.setBAvisoEvaluacion(Boolean.TRUE);
        }

    }

    /**
     * Calcula si el estado es aviso de resolucion
     *
     * @param dto Datos de expediente
     */
    private void avisoResolucion(ExpedienteDTO dto) {

        dto.setBAvisoResolucion(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        Boolean revDefOk = Boolean.FALSE;
        Boolean revProvOk = Boolean.FALSE;
        Integer idTipDocPropResDef = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
        Integer idTipDocPropResProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);
        Integer idTipDocPropExcluProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_EXCLUIDA);
        Integer idTipDocPropDesisProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_DESISTIDA);
        //Integer idTipDocRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
         
        List<ExpedienteDocDTO> lstExpedienteDoc = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropResDef.equals(ed.getTipoDocumentoDTO().getIdValor())
                        &&  ed.getIdDocumentoPtfr() != null)
                .collect(Collectors.toList());
        if (lstExpedienteDoc != null && !lstExpedienteDoc.isEmpty()) {
            revDefOk = Boolean.TRUE;
        }
        
        List<ExpedienteDocDTO> lstExpedienteProvDoc = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropResProv.equals(ed.getTipoDocumentoDTO().getIdValor())
                        && ed.getIdDocumentoPtfr() != null)
                .collect(Collectors.toList());
        
       
        
        List<ExpedienteDocDTO> lstExpedienteDocExclu = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropExcluProv.equals(ed.getTipoDocumentoDTO().getIdValor())
                        &&  ed.getIdDocumentoPtfr() != null)
                .collect(Collectors.toList());
        
        List<ExpedienteDocDTO> lstExpedienteDocDesis = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropDesisProv.equals(ed.getTipoDocumentoDTO().getIdValor())
                        &&  ed.getIdDocumentoPtfr() != null)
                .collect(Collectors.toList());
        
        if ((lstExpedienteProvDoc != null && !lstExpedienteProvDoc.isEmpty()) || (lstExpedienteDocExclu != null && !lstExpedienteDocExclu.isEmpty())  ||  
                (lstExpedienteDocDesis != null && !lstExpedienteDocDesis.isEmpty()) ) {
            revProvOk = Boolean.TRUE;
        }
        
        
        List<ExpedienteDocDTO> resolucionProvisionalSubida = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropResProv.equals(ed.getTipoDocumentoDTO().getIdValor()))
                .collect(Collectors.toList());
               
        List<ExpedienteDocDTO> resolucionDefinitivaSubida = dto.getLstExpedienteDocDTO().stream()
                .filter(ed -> idTipDocPropResDef.equals(ed.getTipoDocumentoDTO().getIdValor()))
                .collect(Collectors.toList());
           

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor())
                || (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor()) && resolucionDefinitivaSubida.isEmpty())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES).equals(dto.getEstadoDTO().getIdValor())
                                     
                || ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION).equals(dto.getEstadoDTO().getIdValor())) 
                        && resolucionProvisionalSubida.isEmpty())       
                        
                || ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor()) 
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor()) 
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor()) 
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor())) 
                        && !revProvOk)
                
                || ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor()) 
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor()) 
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL).equals(dto.getEstadoDTO().getIdValor())) 
                        && !resolucionProvisionalSubida.isEmpty() 
                        && revProvOk 
                        && resolucionDefinitivaSubida.isEmpty() 
                        && dto.getItAlegacionRevisada() != null &&  (dto.getItAlegacionRevisada().equals("S") || dto.getItAlegacionRevisada().equals("N")))
                
                || ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
                     || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO).equals(dto.getEstadoDTO().getIdValor()))
                        && !revDefOk)
                
                || (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST).equals(dto.getEstadoDTO().getIdValor())) 
                // MD-2397, MD-2399 incluimos la condición de que se tenga pendiente de
                // revisión de alegaciones y que la alegación no haya sido
                // revisada, que se vea el aviso en la pestaña Resolución
                || (!dto.getModoConsulta() && dto.getItAlegaciones() != null
                        && Constantes.AfirmacionNegacion.S.equals(dto.getItAlegaciones())
                        && Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES
                                .equals(dto.getEstadoDTO().getNmConstante())
                        && (Constantes.AfirmacionNegacion.N.equals(dto.getItAlegacionRevisada())
                                || dto.getItAlegacionRevisada() == null))
                
                ) {

            dto.setBAvisoResolucion(Boolean.TRUE);
        }

    }
    
    /**
     * Calcula si el estado es aviso de justificacion cientifico tecnica
     *
     * @param dto Datos de expediente
     */
    private void avisoJustificacion(ExpedienteDTO dto) {

        dto.setBAvisoJustificacion(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        /// GIM 01/08/24 Quitamos las validaciones de la revision administrativa y dejamos solo las de la revision JUSTIFICACION cientifico tecnica
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                .equals(dto.getEstadoRevJusTecDTO().getIdValor()) 
                && !Boolean.TRUE.equals(dto.getModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es modo consulta

            dto.setBAvisoJustificacion(Boolean.TRUE);
        }
        
//        List<ExpedienteDocDTO> listaExpdteDocsJCT = null; 
//         
//        //VMM esto lo tenemos que mirar por los periodos de la justec
//        if (dto.getLstExpedienteDocDTO() != null && !dto.getLstExpedienteDocDTO().isEmpty()) {
//            
//            // Nos quedamos solo con los documentos de JCT
//            listaExpdteDocsJCT = dto.getLstExpedienteDocDTO().stream()
//                    .filter(x -> x.getTramiteDTO().getIdValor().equals(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA))
//                    .collect(Collectors.toList());
//           
//            //vemos para cada doc del expdte que sea de la JUSTEC su correspondiente entidad por periodo de la JUSTEC
//            for (ExpedienteDocDTO doc : listaExpdteDocsJCT) {
//            
//                List<ExpedienteJustecDocDTO> lstExpJustecDocDTO = null;
//                lstExpJustecDocDTO = this.expedienteJusTecDocService.buscarPorExpedienteDoc(doc.getIdExpedienteDoc());
//                
//                 ExpedienteJusTecDTO expedienteJustecDTO = null;
//                 if (lstExpJustecDocDTO != null && !lstExpJustecDocDTO.isEmpty()) {
//                     //para cada expdte justec doc saco el ExpdteJustec y asi saber el estado de cada periodo de la JCT
//                     for (ExpedienteJustecDocDTO justecDoc: lstExpJustecDocDTO) {
//                         expedienteJustecDTO = this.expedienteJusTecService.buscarPorIdExpedienteJustec(justecDoc.getIdExpedienteJusTec());
//         
//                          /// GIM 01/08/24 Quitamos las validaciones de la revision administrativa y dejamos solo las de la revision JUSTIFICACION cientifico tecnica
//                         if (Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION
//                                         .equals(expedienteJustecDTO.getIdEstadoDTO().getIdValor())) {
//                             
//                             //si encuentra alguna justec pendiente de revision ya se pone en roja la pestaña
//                             dto.setBAvisoJustificacion(Boolean.TRUE);
//                             break;
//                         }
//                     }
//                 }
//                
//              
//            }
//        }
    }
    
    /**
     * Calcula si el estado es aviso de justificacion economica
     *
     * @param dto Datos de expediente
     */
    private void avisoJustificacionEco(ExpedienteDTO dto) {

        dto.setBAvisoJustificacionEco(Boolean.FALSE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        /// GIM 01/08/24 Quitamos las validaciones de la revision administrativa y dejamos solo las de la revision JUSTIFICACION economica
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                .equals(dto.getEstadoRevJusEcoDTO().getIdValor())
                && !Boolean.TRUE.equals(dto.getModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es modo consulta

            dto.setBAvisoJustificacionEco(Boolean.TRUE);
        }
    }
    
    
    /**
     * Calcula si el estado es aviso de expediente solicitud
     *
     * @param dto Datos de expediente
     */
    private void aviso(ExpedienteDTO dto) {

        dto.setBAviso(Boolean.FALSE);

        if (Boolean.TRUE.equals(dto.getBAvisoDocumento() 
                || Boolean.TRUE.equals(dto.getBAvisoRequisitos()) 
                || Boolean.TRUE.equals(dto.getBAvisoEvaluacion())
                || Boolean.TRUE.equals(dto.getBAvisoResolucion())) 
                || Boolean.TRUE.equals(dto.getBAvisoJustificacion())
                || Boolean.TRUE.equals(dto.getBAvisoJustificacionEco())) {

            dto.setBAviso(Boolean.TRUE);
        }

    }
    
    

}
