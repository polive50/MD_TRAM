package org.madrid.saii.lib.dto.converters;

import java.util.List;

import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Expediente
 *
 * @author everis
 */
public enum ExpedienteToEntity implements Function<ExpedienteDTO, Expediente> {

    /**
     *
     */
    INSTANCE;
    
    /**
     *
     */
    @Override
    public Expediente apply(ExpedienteDTO dto) {

        Expediente entity = null;

        if (dto != null) {
            entity = new Expediente();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getEstadoDTO() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
                entity.setEstadoBenef(this.calcularEstadoBenef(dto.getEstadoDTO().getIdValor()));
            }

            if (dto.getEstadoProvisionalDTO() != null
                    && dto.getEstadoProvisionalDTO().getIdValor() != null) {
                entity.setEstadoProvisional(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoProvisionalDTO()));

            }

            if (dto.getInteresadoDTO() != null) {
                entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }

            if (dto.getConvocatoriaActDTO() != null
                    && dto.getConvocatoriaActDTO().getIdActor() != null) {
                entity.setConvocatoriaAct(
                        ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActDTO()));
            }

            if (dto.getEstadoRevAdminDTO() != null
                    && dto.getEstadoRevAdminDTO().getIdValor() != null) {
                entity.setEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevAdminDTO()));
            }

            if (dto.getEstadoRevRequiDTO() != null
                    && dto.getEstadoRevRequiDTO().getIdValor() != null) {
                entity.setEstadoRevRequi(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevRequiDTO()));
            }

            if (dto.getEstadoRevEvalDTO() != null
                    && dto.getEstadoRevEvalDTO().getIdValor() != null) {
                entity.setEstadoRevEval(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevEvalDTO()));
            }
            
            if (dto.getEstadoRevFinanDTO() != null
                    && dto.getEstadoRevFinanDTO().getIdValor() != null) {
                entity.setEstadoRevFinan(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevFinanDTO()));
            }

            if (dto.getEstadoRevContratoDTO() != null
                    && dto.getEstadoRevContratoDTO().getIdValor() != null) {
                entity.setEstadoRevContrato(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevContratoDTO()));
            }

            if (entity.getSolicitud() == null && dto.getIdSolicitud() != null) {
                entity.setSolicitud(new Solicitud());
            }

            if (entity.getSolicitud() != null) {
                entity.getSolicitud().setIdSolicitud(dto.getIdSolicitud());
            }

            if (dto.getConvocatoriaAreaDTO() != null
                    && dto.getConvocatoriaAreaDTO().getIdConvocatoriaArea() != null) {
                entity.setConvocatoriaArea(
                        ConvocatoriaAreaToEntity.INSTANCE.apply(dto.getConvocatoriaAreaDTO()));
            }

            if (dto.getConvocatoriaAreaSubareaDTO() != null
                    && dto.getConvocatoriaAreaSubareaDTO().getIdConvocatoriaAreaSubarea() != null) {
                entity.setConvocatoriaAreaSubarea(ConvocatoriaAreaSubareaToEntity.INSTANCE
                        .apply(dto.getConvocatoriaAreaSubareaDTO()));
            }
            
            if(dto.getConvocatoriaSubmodDTO() != null
                    && dto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() != null) {
                entity.setConvocatoriaSubmod(ConvocatoriaSubmodToEntity.INSTANCE.apply(dto.getConvocatoriaSubmodDTO()));
            }
            
            if (dto.getRepresentanteLegal() != null && dto.getRepresentanteLegal().getIdInteresado() != null) {
                entity.setRepresentanteLegal(InteresadoToEntity.INSTANCE.apply(dto.getRepresentanteLegal()));
            }

            if (dto.getTipoPersonaSolicitudDTO() != null && dto.getTipoPersonaSolicitudDTO().getIdValor() != null) {
                entity.setIdPersonaSolicitante(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoPersonaSolicitudDTO()));
            }

            if (dto.getMedioComunicacionDTO() != null && dto.getMedioComunicacionDTO().getIdValor() != null) {
                entity.setIdMedioNotificacion(CatalogosValoresToEntity.INSTANCE.apply(dto.getMedioComunicacionDTO()));
            }
            
            if (dto.getCdEreg() != null && dto.getCdEreg().length() > 11) {
                entity.setCdEreg(Utils.desformatearNmEreg(dto.getCdEreg()));
            }
            
            if (dto.getAutorizado() != null) {
                entity.setAutorizado(InteresadoToEntity.INSTANCE.apply(dto.getAutorizado()));
            }

        }

        return entity;
    }

    /**
     * Calculo el estado del expediente beneficiario a partir del estado del
     * expediente
     *
     * @param idEstado
     * @return CatalogosValoresDTO
     */
    public CatalogosValores calcularEstadoBenef(Integer idEstado) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        Integer idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.INICIADA);

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.INICIADA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.PROPUESTA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.ACEPTADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.ACEPTADA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.NO_ACEPTADA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.SELECCIONADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.SELECCIONADA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_SELECCIONADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.NO_SELECCIONADA);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EN_TRAMITACION).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS).equals(idEstado)) {
//            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS);
            // Ya no se cambia el estado del beneficiario aquí a PENDIENTE_SUBSANACION_REQUISITOS, sino cuando se envía a note
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.DESISTIDO);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EXCLUIDO);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.DESISTIDO_PROVISIOANL);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EXCLUIDO_PROVISIONAL);
        }
          
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.RENUNCIA_EXPRESA);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_DEFINITIVO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.EN_TRAMITACION);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.DENEGADO_PROVISIONAL);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.APROBADO_PROVISIONAL);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.PROPUESTA_DE_RESOLUCION);
        }

                                                                                        //244
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.CONDEDIDO_DEFINITIVO_PEDIENTE_DE_ACEPTACION);
        } 
                                                                                      
                                                                                        //245
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.CONDEDIDO_DEFINITIVO_PEDIENTE_DE_ACEPTACION); 
        } 
        
                                                                                        //246                
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.CONCEDIDO_DEFINITIVO); 
        }        
                                                                                                 //223
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO).equals(idEstado)) {
        idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.CONCEDIDO_DEFINITIVO_ACEPTADO);
        }  
                                                                                            //247
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO).equals(idEstado)) {
        idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.CONCEDIDO_DEFINITIVO_DESISTIDO);
        }
        
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.DENEGADO_DEFINITIVO);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_CONTRATOS).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.PENDIENTE_PRESENTACION_CONTRATOS);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO).equals(idEstado)) {
            idValorEstadoBenef = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.RECHAZADO_DEFINITIVO);
        }

        CatalogosValores catalogosV = new CatalogosValores();
        catalogosV.setCatalogo(new Catalogos());
        catalogosV.getCatalogo().setIdCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_EXPEDIENTE_BENEFICIARIO));
        catalogosV.setIdValor(idValorEstadoBenef);

        return catalogosV;

    }

    /**
     * Método para obtener el estado del expediente
     *
     * @param entity obj Expediente
     * @return estado del expediente
     */
    public Integer calcularEstadoMayor(Expediente entity) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        Integer estadoExpediente = 0;
        Integer estadoRevAdmin = entity.getEstadoRevAdmin().getIdValor();
        Integer estadoRevRequi = entity.getEstadoRevRequi().getIdValor();
        
        Integer estExpRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK);
        Integer estExpDesis = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO);
        Integer estExpExclu = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO);
        Integer estExpPenReqFirRec = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA);
        
        if (estadoRevAdmin.equals(estExpRevOk) && !estadoRevRequi.equals(estExpRevOk)) {
            estadoExpediente = estadoRevRequi;
        } else if (!estadoRevAdmin.equals(estExpRevOk) && estadoRevRequi.equals(estExpRevOk)) {
            estadoExpediente = estadoRevAdmin;
        } else if (estadoRevAdmin.equals(estExpRevOk) && estadoRevRequi.equals(estExpRevOk)) {
            estadoExpediente = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EN_TRAMITACION);
        } else if (estadoRevAdmin.equals(estExpDesis)) {
            estadoExpediente = estExpDesis;
        } else if (estadoRevRequi.equals(estExpExclu)) {
            estadoExpediente = estExpExclu;
        } else if (estadoRevAdmin.equals(estExpPenReqFirRec)
                || estadoRevRequi.equals(estExpPenReqFirRec)) {
            estadoExpediente = estExpPenReqFirRec;
        } else if (estadoRevAdmin.equals(estadoRevRequi)) {
            estadoExpediente = entity.getEstadoRevAdmin().getIdValor();
        } else {
            estadoExpediente = this.compararEstadoInternos(estadoRevAdmin, estadoRevRequi);

            if (estadoExpediente.equals(0)) {
                estadoExpediente = this.compararEstadoInternos(estadoRevRequi, estadoRevAdmin);
            }
        }

        return estadoExpediente;

    }

    /**
     * Método para verificar el tipo de estado del expediente, siendo el estado
     * interno de RevAdmin mayor
     *
     * @param estadoRevAdmin Integer
     * @param estadoRevRequi Integer
     * @return el tipo de estado del expediente
     */
    private Integer compararEstadoInternos(Integer estadoInterno1, Integer estadoInterno2) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        Integer estadoExpediente = 0;

        /*
         * if
         * (estadoInterno1.equals(Constantes.EstadoExpediente.EN_TRAMITACION)) {
         * if
         * (estadoInterno2.equals(Constantes.EstadoExpediente.PENDIENTE_REVISION
         * )) { estadoExpediente =
         * Constantes.EstadoExpediente.PENDIENTE_REVISION; } else if
         * (estadoInterno2.equals(Constantes.EstadoExpediente.
         * PENDIENTE_REQUERIMIENTO)) { estadoExpediente =
         * Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO; } else if
         * (estadoInterno2
         * .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         * ) { estadoExpediente =
         * Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS; } else
         * if (estadoInterno2
         * .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION))
         * { estadoExpediente =
         * Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION; } else if
         * (estadoInterno2.equals(
         * Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS
         * )) { estadoExpediente = Constantes.EstadoExpediente.
         * PENDIENTE_REVISION_SUBSANACION_REQUISITOS; } } else
         */
        
        Integer idEstExpPenReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO);
        Integer idEstExpPenSubReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS);
        Integer idEstExpPenRevSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
        Integer idEstExpPenRevSubReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS);
        
        
        if (estadoInterno1.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION))) {
            if (estadoInterno2.equals(idEstExpPenReq)) {
                estadoExpediente = idEstExpPenReq;
            } else if (estadoInterno2.equals(idEstExpPenSubReq)) {
                estadoExpediente = idEstExpPenSubReq;
            } else if (estadoInterno2.equals(idEstExpPenRevSub)) {
                estadoExpediente = idEstExpPenRevSub;
            } else if (estadoInterno2.equals(idEstExpPenRevSubReq)) {
                estadoExpediente = idEstExpPenRevSubReq;
            }
        } else if (estadoInterno1.equals(idEstExpPenReq)) {
            if (estadoInterno2.equals(idEstExpPenSubReq)) {
                estadoExpediente = idEstExpPenSubReq;
            } else if (estadoInterno2.equals(idEstExpPenRevSub)) {
                estadoExpediente = idEstExpPenRevSub;
            } else if (estadoInterno2.equals(idEstExpPenRevSubReq)) {
                estadoExpediente = idEstExpPenRevSubReq;
            }
        } else if (estadoInterno1.equals(idEstExpPenSubReq)) {
            if (estadoInterno2.equals(idEstExpPenRevSubReq)) {
                estadoExpediente = idEstExpPenSubReq;
            }
        } else if (estadoInterno1.equals(idEstExpPenRevSub)) {
            if (estadoInterno2.equals(idEstExpPenSubReq)) {
                estadoExpediente = idEstExpPenSubReq;
            } else {
                estadoExpediente = idEstExpPenRevSub;
            }
        } else if (estadoInterno1.equals(idEstExpPenRevSubReq)) {
            if (estadoInterno2.equals(idEstExpPenSubReq)) {
                estadoExpediente = idEstExpPenSubReq;
            }
        }

        return estadoExpediente;
    }

    /**
     *
     * @param entity
     * @return
     */
    public Integer calcularEstadoMayorDocumento(List<ExpedienteDoc> lstEntity) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        Integer idEstadoExpedienteDoc = null;

        if (lstEntity != null && !lstEntity.isEmpty()) {
            
            Integer idEstDocPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            Integer idEstDocReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Integer idEstDocReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
            Integer idEstDocPenRevSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
            
            int totalPendienteRevision = (int) lstEntity.stream().filter(x -> x.getEstado()
                    .getIdValor().compareTo(idEstDocPenRev) == 0).count();

            int totalRequiereSubsanacion = (int) lstEntity.stream()
                    .filter(x -> x.getEstado().getIdValor().compareTo(idEstDocReqSub) == 0).count();

            int totalRequiereSubsanacionNotificado = (int) lstEntity.stream()
                    .filter(x -> x.getEstado().getIdValor().compareTo(idEstDocReqSubNot) == 0).count();

            int totalPendienteRevisionSubsanacion = (int) lstEntity.stream()
                    .filter(x -> x.getEstado().getIdValor().compareTo(idEstDocPenRevSub) == 0).count();

            if (totalPendienteRevision > 0) {
                idEstadoExpedienteDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION); // 208
            }
            if (totalRequiereSubsanacion > 0) {
                idEstadoExpedienteDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO);
            }
            if (totalRequiereSubsanacionNotificado > 0) {
                idEstadoExpedienteDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS); // 211
            }
            if (totalPendienteRevisionSubsanacion > 0) {
                idEstadoExpedienteDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION); // 238
            }

        }

        return idEstadoExpedienteDoc;

    }
}
