package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.dto.NotificacionDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio Notificacion
 *
 * @author nttdata
 */
@Repository("notificacionDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class NotificacionDAOImpl extends BaseDAOImpl<NotificacionDTO, Integer>
implements NotificacionDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * @ 
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NotificacionDTO> obtenerNotificacionCoorEval(String itCoorEval,
            String identificador)  {

        List<NotificacionDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("exe.idExpedienteEval, "); // 0
        queryHql.append("exe.idExpediente, "); // 1
        queryHql.append("e.cdExpediente, "); // 2
        queryHql.append("e.convocatoriaAct.tipoActor.dsValor, "); // 3
        queryHql.append("e.interesado.cdNumIden, "); // 4
        queryHql.append("e.interesado.dsNombre, "); // 5
        queryHql.append("e.interesado.dsApellido1, "); // 6
        queryHql.append("e.interesado.dsApellido2, "); // 7
        queryHql.append("exe.estado.dsValor, "); // 8
        queryHql.append("exe.itCoorEval,  "); // 9
        queryHql.append("e.fcFirma, "); // 10
        queryHql.append("c.idConvocatoria, "); // 11
        queryHql.append("c.cdAcronimo, "); // 12
        queryHql.append("c.nmCalificacionMax, "); // 13
        queryHql.append("e.estado.idValor, "); // 14
        queryHql.append("e.estado.dsValor, "); // 15
        queryHql.append("e.convocatoriaAct.identificacion.dsValor, "); // 16
        queryHql.append("c.dsDescripcion, "); // 17
        queryHql.append("s.convocatoriaArea.area.idValor, "); // 18
        queryHql.append("ied.dsComentario, "); // 19
        queryHql.append("ied.nmNotaFinal, "); // 20
        queryHql.append("exe.estado.idValor, "); // 21
        queryHql.append("ied.idCausa.idValor, "); // 22
        queryHql.append("ied.itRechazado, "); // 23
        queryHql.append("exe.estado.nmConstante, "); // 24
        queryHql.append("ied.idInforme.idInforme, "); // 25
        queryHql.append("ied.idEstado.idValor, "); // 26
        queryHql.append("ied.idInforme.tipoInforme.idValor, "); // 27
        queryHql.append("ied.idInforme.tipoInforme.dsValor "); // 28
        queryHql.append("FROM ");
        queryHql.append("ExpedienteEval exe ");
        queryHql.append(" inner join Expediente e ");
        queryHql.append(" on exe.idExpediente = e.idExpediente ");
        queryHql.append(" inner join Interesado i ");
        queryHql.append(" on exe.persona.idInteresado  = i.idInteresado ");
        queryHql.append(" inner join Solicitud s ");
        queryHql.append(" on e.solicitud.idSolicitud = s.idSolicitud ");
        queryHql.append(" inner join Convocatoria c ");
        queryHql.append(" on s.convocatoria.idConvocatoria = c.idConvocatoria ");
        queryHql.append(" left join InformeEvalDoc ied ");
        queryHql.append(" on ied.idExpedienteEval.idExpedienteEval = exe.idExpedienteEval ");
        queryHql.append(" and ied.fcBajaFila is null ");
        
        queryHql.append("WHERE ");
        queryHql.append(" exe.itCoorEval = :itCoorEval ");
        queryHql.append(" AND exe.itNotificado = :itNotificado ");
        queryHql.append(" AND i.cdNumIden = :cdNumIden ");
        queryHql.append(" AND exe.fcBajaFila IS NULL ");
        queryHql.append(" AND e.fcBajaFila is null ");
        queryHql.append(" AND c.fcBajaFila is null ");
        queryHql.append(" AND s.fcBajaFila is null ");
        queryHql.append(" AND c.fcIniTramiEval IS NOT NULL AND c.fcFinTramiEval IS NOT NULL ");
        queryHql.append(" AND c.fcIniTramiEval <= :currentDate ");
       queryHql.append(" AND c.fcFinTramiEval >= :currentDate ");

        Map<String, Object> params = new HashMap<>();
        params.put("itCoorEval", itCoorEval);
        params.put("itNotificado", Constantes.Notificacion.ITNOTIFICADO_S);
        params.put("cdNumIden", identificador);
        params.put("currentDate", new Date());

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            NotificacionDTO tempDTO = new NotificacionDTO();
            tempDTO.setIdExpedienteEval((Integer) object[0]);
            tempDTO.setIdExpediente((Integer) object[1]);
            tempDTO.setCdExpediente((String) object[2]);
            tempDTO.setDsTipoActor((String) object[3]);
            tempDTO.setDsNifInteresado((String) object[4]);
            tempDTO.setDsNombreInteresado((String) object[5]);
            tempDTO.setDsApellido1Interesado((String) object[6]);
            tempDTO.setDsApellido2Interesado((String) object[7]);
            tempDTO.setDsEstadoCoordinadorEvaluador((String) object[8]);
            tempDTO.setItCoorEval((String) object[9]);

            Date fcPresentacionEreg = (Date) object[10];
            tempDTO.setFcPresentacionEreg(
                    Utils.dateToString(fcPresentacionEreg, Constantes.Utiles.FORMATO_FECHA_HORA));

            tempDTO.setIdConvocatoria((Integer) object[11]);
            tempDTO.setCdAcronimo((String) object[12]);
            tempDTO.setNmCalificacionMax(object[13] != null ? (Integer) object[13] : 0);
            tempDTO.setIdEstadoExpediente((Integer) object[14]);
            tempDTO.setDsEstadoExpediente((String) object[15]);

            tempDTO.setDsTipoIdentificacion((String) object[16]);

            tempDTO.setDsDescripcion((String) object[17]);
            tempDTO.setIdAreaCientifica((Integer) object[18]);

            tempDTO.setDsComentarioFinal(object[19] != null ? (String) object[19] : "");
            tempDTO.setNmNotaFinal(object[20] != null ? (Integer) object[20] : null);
            tempDTO.setIdEstadoExpedienteEval((Integer) object[21]);
            tempDTO.setIdCausaDenegacionEvaluacion((Integer) object[22]);
            tempDTO.setItRezachado(object[23] != null ? (String) object[23] : Constantes.AfirmacionNegacion.N);
            tempDTO.setNmConstanteEstadoCoordinadorEvaluador((Integer) object[24]);
            tempDTO.setIdInforme(object[25] != null ? (Integer) object[25] : null);
            tempDTO.setIdEstadoInforme(object[26] != null ? (Integer) object[26] : null);
            tempDTO.setIdTipoInforme(object[27] != null ? (Integer) object[27] : null);
            tempDTO.setDsTipoInforme(object[28] != null ? (String) object[28] : null);

            // Si el estado del expediente es Pendiente de Revisión Alegaciones
            // Se muestra la notificacion al evaluador y coordinador
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES)
                    .equals(tempDTO.getIdEstadoExpediente())) {
                tempDTO.setMensajeNotificacion(
                        Constantes.Notificacion.ALEGACIONES_EXPEDIENTE + tempDTO.getCdExpediente()
                        + Constantes.Notificacion.ALEGACIONES_EXPEDIENTE_2);
                lst.add(tempDTO);
            }

            if (Constantes.TipoCoorEval.COORDINADOR.equals(itCoorEval)) {

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR)
                        .equals(tempDTO.getIdEstadoExpedienteEval())) {

                    if (Constantes.AfirmacionNegacion.S.equals(tempDTO.getItRezachado())) {
                        tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.RECHAZADA_POR_GESTOR);
                    } else {
                        tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.CUMPLIMENTAR_COORDINADOR_PENDIENTE);
                    }
                    lst.add(tempDTO);
                }

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES)
                        .equals(tempDTO.getIdEstadoExpedienteEval())) {
                    tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                            + tempDTO.getCdExpediente()
                            + Constantes.Notificacion.ASIGNACION_EVALUADORES);
                    lst.add(tempDTO);
                }

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO)
                        .equals(tempDTO.getIdEstadoExpedienteEval())) {
                    tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                            + tempDTO.getCdExpediente()
                            + Constantes.Notificacion.PENDIENTE_ACEPTACION_EXPERTO);
                    lst.add(tempDTO);
                }

            }

            if (Constantes.TipoCoorEval.EVALUADOR.equals(itCoorEval)) {

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR)
                        .equals(tempDTO.getIdEstadoExpedienteEval())) {

                    if (Objects.nonNull(tempDTO.getIdCausaDenegacionEvaluacion() )
                         && Objects.nonNull(tempDTO.getIdEstadoInforme())
                         && tempDTO.getIdEstadoInforme().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR))
                            ) {
                        tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.RECHAZADA_POR_COORDINADOR);
                        lst.add(tempDTO); 
                    } else if(tempDTO.getIdEstadoInforme() == null || tempDTO.getIdEstadoInforme().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR))) {
                        tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUADOR_ASIGNADO_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.PENDIENTE_CUMPLEMENTAR_EVALUACION);
                        lst.add(tempDTO);
                    }
                    
                }

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO)
                        .equals(tempDTO.getIdEstadoExpedienteEval())) {
                    tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUADOR_ASIGNADO_EXPEDIENTE
                            + tempDTO.getCdExpediente()
                            + Constantes.Notificacion.PENDIENTE_ACEPTACION_EXPERTO);
                    lst.add(tempDTO);
                }

            }

        }
        return lst;

    }

    /**
     * Método para obtener la lista de notificaciones del coordinador, sin
     * left join a InformeEvalDoc porque el coordinador de momento no tiene
     * informes asignados ni ninguna accion que hacer sobre ellos.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NotificacionDTO> obtenerNotificacionCoordinador(String identificador) {

        List<NotificacionDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("exe.idExpedienteEval, "); // 0
        queryHql.append("exe.idExpediente, "); // 1
        queryHql.append("e.cdExpediente, "); // 2
        queryHql.append("e.convocatoriaAct.tipoActor.dsValor, "); // 3
        queryHql.append("e.interesado.cdNumIden, "); // 4
        queryHql.append("e.interesado.dsNombre, "); // 5
        queryHql.append("e.interesado.dsApellido1, "); // 6
        queryHql.append("e.interesado.dsApellido2, "); // 7
        queryHql.append("exe.estado.dsValor, "); // 8
        queryHql.append("exe.itCoorEval,  "); // 9
        queryHql.append("e.fcFirma, "); // 10
        queryHql.append("c.idConvocatoria, "); // 11
        queryHql.append("c.cdAcronimo, "); // 12
        queryHql.append("c.nmCalificacionMax, "); // 13
        queryHql.append("e.estado.idValor, "); // 14
        queryHql.append("e.estado.dsValor, "); // 15
        queryHql.append("e.convocatoriaAct.identificacion.dsValor, "); // 16
        queryHql.append("c.dsDescripcion, "); // 17
        queryHql.append("s.convocatoriaArea.area.idValor, "); // 18
        queryHql.append("exe.estado.idValor, "); // 19
        queryHql.append("exe.estado.nmConstante "); // 20
        queryHql.append("FROM ");
        queryHql.append("ExpedienteEval exe ");
        queryHql.append(" inner join Expediente e ");
        queryHql.append(" on exe.idExpediente = e.idExpediente ");
        queryHql.append(" inner join Interesado i ");
        queryHql.append(" on exe.persona.idInteresado  = i.idInteresado ");
        queryHql.append(" inner join Solicitud s ");
        queryHql.append(" on e.solicitud.idSolicitud = s.idSolicitud ");
        queryHql.append(" inner join Convocatoria c ");
        queryHql.append(" on s.convocatoria.idConvocatoria = c.idConvocatoria ");

        queryHql.append("WHERE ");
        queryHql.append(" exe.itCoorEval = :itCoorEval ");
        queryHql.append(" AND exe.itNotificado = :itNotificado ");
        queryHql.append(" AND i.cdNumIden = :cdNumIden ");
        queryHql.append(" AND exe.fcBajaFila IS NULL ");
        queryHql.append(" AND e.fcBajaFila is null ");
        queryHql.append(" AND c.fcBajaFila is null ");
        queryHql.append(" AND s.fcBajaFila is null ");
        queryHql.append(" AND c.fcIniTramiEval IS NOT NULL AND c.fcFinTramiEval IS NOT NULL ");
        queryHql.append(" AND c.fcIniTramiEval <= :currentDate ");
        queryHql.append(" AND c.fcFinTramiEval >= :currentDate ");

        Map<String, Object> params = new HashMap<>();
        params.put("itCoorEval", Constantes.TipoCoorEval.COORDINADOR);
        params.put("itNotificado", Constantes.Notificacion.ITNOTIFICADO_S);
        params.put("cdNumIden", identificador);
        params.put("currentDate", new Date());

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            NotificacionDTO tempDTO = new NotificacionDTO();
            tempDTO.setIdExpedienteEval((Integer) object[0]);
            tempDTO.setIdExpediente((Integer) object[1]);
            tempDTO.setCdExpediente((String) object[2]);
            tempDTO.setDsTipoActor((String) object[3]);
            tempDTO.setDsNifInteresado((String) object[4]);
            tempDTO.setDsNombreInteresado((String) object[5]);
            tempDTO.setDsApellido1Interesado((String) object[6]);
            tempDTO.setDsApellido2Interesado((String) object[7]);
            tempDTO.setDsEstadoCoordinadorEvaluador((String) object[8]);
            tempDTO.setItCoorEval((String) object[9]);

            Date fcPresentacionEreg = (Date) object[10];
            tempDTO.setFcPresentacionEreg(
                    Utils.dateToString(fcPresentacionEreg, Constantes.Utiles.FORMATO_FECHA_HORA));

            tempDTO.setIdConvocatoria((Integer) object[11]);
            tempDTO.setCdAcronimo((String) object[12]);
            tempDTO.setNmCalificacionMax(object[13] != null ? (Integer) object[13] : 0);
            tempDTO.setIdEstadoExpediente((Integer) object[14]);
            tempDTO.setDsEstadoExpediente((String) object[15]);

            tempDTO.setDsTipoIdentificacion((String) object[16]);

            tempDTO.setDsDescripcion((String) object[17]);
            tempDTO.setIdAreaCientifica((Integer) object[18]);

            // El coordinador de momento no tiene informes asignados, por lo que no hay
            // datos de InformeEvalDoc que consultar: se dejan a sus valores por defecto
            tempDTO.setDsComentarioFinal("");
            tempDTO.setNmNotaFinal(null);
            tempDTO.setIdEstadoExpedienteEval((Integer) object[19]);
            tempDTO.setIdCausaDenegacionEvaluacion(null);
            tempDTO.setItRezachado(Constantes.AfirmacionNegacion.N);
            tempDTO.setNmConstanteEstadoCoordinadorEvaluador((Integer) object[20]);
            tempDTO.setIdInforme(null);
            tempDTO.setIdEstadoInforme(null);
            tempDTO.setIdTipoInforme(null);
            tempDTO.setDsTipoInforme(null);

            // Si el estado del expediente es Pendiente de Revisión Alegaciones
            // Se muestra la notificacion al coordinador
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES)
                    .equals(tempDTO.getIdEstadoExpediente())) {
                tempDTO.setMensajeNotificacion(
                        Constantes.Notificacion.ALEGACIONES_EXPEDIENTE + tempDTO.getCdExpediente()
                        + Constantes.Notificacion.ALEGACIONES_EXPEDIENTE_2);
                lst.add(tempDTO);
            }

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR)
                    .equals(tempDTO.getIdEstadoExpedienteEval())) {
                tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                        + tempDTO.getCdExpediente()
                        + Constantes.Notificacion.CUMPLIMENTAR_COORDINADOR_PENDIENTE);
                lst.add(tempDTO);
            }

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES)
                    .equals(tempDTO.getIdEstadoExpedienteEval())) {
                tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                        + tempDTO.getCdExpediente()
                        + Constantes.Notificacion.ASIGNACION_EVALUADORES);
                lst.add(tempDTO);
            }

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO)
                    .equals(tempDTO.getIdEstadoExpedienteEval())) {
                tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                        + tempDTO.getCdExpediente()
                        + Constantes.Notificacion.PENDIENTE_ACEPTACION_EXPERTO);
                lst.add(tempDTO);
            }

        }
        return lst;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NotificacionDTO> obtenerNotificacionEvalJustec(String identificador)  {

        List<NotificacionDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("exe.idExpedienteEvalJusTec, "); // 0
        queryHql.append("exe.expedienteJustec.idExpedienteJusTec, "); // 1
        queryHql.append("e.cdExpediente, "); // 2
        queryHql.append("e.convocatoriaAct.tipoActor.dsValor, "); // 3
        queryHql.append("e.interesado.cdNumIden, "); // 4
        queryHql.append("e.interesado.dsNombre, "); // 5
        queryHql.append("e.interesado.dsApellido1, "); // 6
        queryHql.append("e.interesado.dsApellido2, "); // 7
        queryHql.append("exe.estado.dsValor, "); // 8
        queryHql.append("e.fcFirma, "); //9
        queryHql.append("c.idConvocatoria, "); // 10
        queryHql.append("c.cdAcronimo, "); // 11
        queryHql.append("c.nmCalificacionMax, "); // 12
        queryHql.append("e.estado.idValor, "); // 13
        queryHql.append("e.estado.dsValor, "); // 14
        queryHql.append("e.convocatoriaAct.identificacion.dsValor, "); // 15
        queryHql.append("c.dsDescripcion, "); // 16
        queryHql.append("s.convocatoriaArea.area.idValor, "); // 17
        queryHql.append("exe.dsComentario, "); // 18
        queryHql.append("exe.nmNotaFinal, "); // 19
        queryHql.append("exe.estado.idValor, "); // 20
        queryHql.append("exe.causa.idValor, "); // 21
        queryHql.append("exe.itRechazado, "); // 22
        queryHql.append("e.idExpediente, "); // 23
        queryHql.append("exe.expedienteJustec.tipoExpedienteJusTec.dsValor, "); //24
        queryHql.append("exe.expedienteJustec.nmPeriodo,"); //25
        queryHql.append("exe.obsEval "); //26
        queryHql.append("FROM ");
        queryHql.append("ExpedienteEvalJusTec exe ");
        queryHql.append(" inner join Expediente e ");
        queryHql.append(" on exe.expedienteJustec.idExpediente  = e.idExpediente ");
        queryHql.append(" inner join Interesado i ");
        queryHql.append(" on exe.persona.idInteresado  = i.idInteresado ");
        queryHql.append(" inner join Solicitud s ");
        queryHql.append(" on e.solicitud.idSolicitud = s.idSolicitud ");
        queryHql.append(" inner join Convocatoria c ");
        queryHql.append(" on s.convocatoria.idConvocatoria = c.idConvocatoria ");
        queryHql.append("WHERE ");
        queryHql.append(" exe.estado.idValor = :estado ");
        queryHql.append(" AND exe.itNotificado = :itNotificado ");
        queryHql.append(" AND i.cdNumIden = :cdNumIden ");
        queryHql.append(" AND exe.fcBajaFila IS NULL ");
        queryHql.append(" AND e.fcBajaFila is null ");
        queryHql.append(" AND c.fcBajaFila is null ");
        queryHql.append(" AND s.fcBajaFila is null ");
        queryHql.append(" AND exe.fcIniPlazo <= :currentDate ");
        queryHql.append(" AND exe.fcFinPlazo >= :currentDate ");

        Map<String, Object> params = new HashMap<>();
        Date currentDate = new Date();
        params.put("itNotificado", Constantes.Notificacion.ITNOTIFICADO_S);
        params.put("cdNumIden", identificador);
        params.put("estado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR_JUSTEC));
        params.put("currentDate", currentDate);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            NotificacionDTO tempDTO = new NotificacionDTO();
            tempDTO.setIdExpedienteEvalJusTec((Integer) object[0]);
            tempDTO.setIdExpedienteJustec((Integer) object[1]);
            tempDTO.setIdExpediente((Integer) object[23]);
            tempDTO.setCdExpediente((String) object[2]);
            tempDTO.setDsTipoActor((String) object[3]);
            tempDTO.setDsNifInteresado((String) object[4]);
            tempDTO.setDsNombreInteresado((String) object[5]);
            tempDTO.setDsApellido1Interesado((String) object[6]);
            tempDTO.setDsApellido2Interesado((String) object[7]);
            tempDTO.setDsEstadoCoordinadorEvaluador((String) object[8]);
            tempDTO.setItCoorEval("E");

            Date fcPresentacionEreg = (Date) object[9];
            tempDTO.setFcPresentacionEreg(
                    Utils.dateToString(fcPresentacionEreg, Constantes.Utiles.FORMATO_FECHA_HORA));

            tempDTO.setIdConvocatoria((Integer) object[10]);
            tempDTO.setCdAcronimo((String) object[11]);
            tempDTO.setNmCalificacionMax(object[12] != null ? (Integer) object[12] : 0);
            tempDTO.setIdEstadoExpediente((Integer) object[13]);
            tempDTO.setDsEstadoExpediente((String) object[14]);

            tempDTO.setDsTipoIdentificacion((String) object[15]);

            tempDTO.setDsDescripcion((String) object[16]);
            tempDTO.setIdAreaCientifica((Integer) object[17]);

            tempDTO.setDsComentarioFinal(object[18] != null ? (String) object[18] : "");
            tempDTO.setNmNotaFinal(object[19] != null ? (Integer) object[19] : null);
            tempDTO.setIdEstadoExpedienteEval((Integer) object[20]);
            tempDTO.setIdCausaDenegacionEvaluacion((Integer) object[21]);
            tempDTO.setItRezachado(object[22] != null ? (String) object[22] : Constantes.AfirmacionNegacion.N);


              if (Objects.nonNull(tempDTO.getIdCausaDenegacionEvaluacion())) {
                        tempDTO.setMensajeNotificacion(Constantes.Notificacion.EVALUACION_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.RECHAZADA_POR_COORDINADOR);
                    } else {
                        tempDTO.setMensajeNotificacion(
                                Constantes.Notificacion.EVALUADOR_ASIGNADO_EXPEDIENTE
                                + tempDTO.getCdExpediente()
                                + Constantes.Notificacion.PENDIENTE_CUMPLEMENTAR_EVALUACION_JUSTEC
                                + " "+(String) object[24] + " - "+(Integer) object[25]);
             }

              tempDTO.setObsEval((String) object[26]);
              
                    lst.add(tempDTO);

         
        }
        return lst;

    }
}
