
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteDoc
 *
 * @author everis
 */
@Repository("expedienteDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteDocDAOImpl extends BaseDAOImpl<ExpedienteDoc, Integer>
        implements ExpedienteDocDAO {
    private static final ExpedienteDoc ExpedienteDoc = null;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Override
    public List<ExpedienteDoc> findByCdAnexoEreg(String cdAnexoEreg) {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT d from ExpedienteDoc as d ");
        queryHql.append("where d.cdAnexoEreg =:cdAnexoEreg ");

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("cdAnexoEreg", cdAnexoEreg);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteDoc> executeHQLQueryList = (List<ExpedienteDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    @Override
    public List<ExpedienteDocDTO> listExpedienteDocContratos(Integer idExpediente)  {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select doc ");
        queryHql.append(" from ExpedienteDoc doc");
        queryHql.append(" where ");
        queryHql.append(" doc.expediente.idExpediente = :idExpediente");
        queryHql.append(" and doc.estado.idValor = :idEstado ");
        queryHql.append(" and doc.fcBajaFila IS NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idEstado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {

            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            if (expedienteDoc.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO))) {
                lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
            }

        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteDocDTO> listExpedienteDocContratosPorIdActor(Integer idActor,
            Boolean subsanar, Integer tramite) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select doc ");
        queryHql.append(" from ExpedienteDoc doc");
        queryHql.append(" where ");
        queryHql.append(" doc.tramite.idValor = :tramite ");
        queryHql.append(" and doc.cdFica IS NOT NULL");
        queryHql.append(" and doc.expediente.idExpediente in ");
        queryHql.append(" (select exp.idExpediente ");
        queryHql.append(" from Expediente exp ");
        queryHql.append(" where ");
        queryHql.append(" exp.convocatoriaAct.idActor in ");
        queryHql.append(" (select cac.idActor ");
        queryHql.append(" from ConvocatoriaAct cac ");
        queryHql.append(" where ");
        queryHql.append(" cac.itContrato = 'S'");
        queryHql.append(" and (cac.idActorCoordinador = :idActor ");
        queryHql.append(" or cac.idActorSolicitante  = :idActor ");
        queryHql.append(" ))) ");

        Map<String, Object> params = new HashMap<>();
        params.put("idActor", idActor);
        params.put("tramite", tramite);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));

        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteDocDTO> listExpedienteDocContratosPorTramite(Integer idExpediente,
            Integer idTramite) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(
                " select expD.expedienteDoc from ExpedienteContratoDoc expD where expD.expedienteContrato.idExpedienteContrato in ( ");
        queryHql.append(" select expC.idExpedienteContrato from ExpedienteContrato expC  ");
        queryHql.append(" where expC.idExpediente = :idExpediente AND expC.fcBajaFila is null )");
        queryHql.append(" AND expD.expedienteDoc.tramite.idValor=:idTramite ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idTramite", idTramite);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteDocDTO> listExpedienteDocContratosSubsanadoPorTramite(
            Integer idExpediente)  {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select doc ");
        queryHql.append(" from ExpedienteDoc doc");
        queryHql.append(" where ");
        queryHql.append(" doc.expediente.idExpediente = :idExpediente");
        queryHql.append(" and doc.estado.idValor = :idEstado");
        queryHql.append(" and doc.fcBajaFila IS NULL");
        queryHql.append(" and doc.idDocumentoPtfr IS NOT NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idEstado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));

        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<Expediente[]> listExpedientePaticipantesContratosPorIdActor(Integer idActor) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select exp");
        queryHql.append(" from Expediente exp ");
        queryHql.append(" where ");
        queryHql.append(" exp.convocatoriaAct.idActor in");
        queryHql.append(" (select cac.idActor ");
        queryHql.append(" from ConvocatoriaAct cac");
        queryHql.append(" where ");
        queryHql.append(" cac.itContrato = 'S'");
        queryHql.append(" and (cac.idActorCoordinador = :idActor ");
        queryHql.append(" or cac.idActorSolicitante  = :idActor ");
        queryHql.append(" )) ");

        Map<String, Object> params = new HashMap<>();
        params.put("idActor", idActor);

        @SuppressWarnings("unchecked")
        List<Expediente[]> result = (List<Expediente[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return result;
    }

    @Override
    public List<ExpedienteDocDTO> listObtenerContratos(Integer idExpediente, Integer idTramite) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select doc ");
        queryHql.append(" from ExpedienteDoc doc");
        queryHql.append(" where ");
        queryHql.append(" doc.expediente.idExpediente = :idExpediente");
        queryHql.append(" and doc.tramite.idValor = :idTramite");
        queryHql.append(" and doc.fcBajaFila IS NULL");
        queryHql.append(" and doc.cdFica IS NOT NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idTramite", idTramite);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));

        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteDocDTO> listExpedienteDocContratosPorIdPadre(Integer idExpedientePadre,
            Integer tramite) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT doc ");
        queryHql.append(" FROM ExpedienteDoc doc");
        queryHql.append(" WHERE ");
        queryHql.append(" (doc.expediente.idExpediente IN (SELECT exp.idExpediente ");
        queryHql.append(" FROM Expediente exp WHERE exp.idExpedientePadre = :idExpedientePadre ");
        queryHql.append(" AND exp.fcBajaFila IS NULL AND doc.cdFica IS NOT NULL) ");
        queryHql.append(
                " OR (doc.expediente.idExpediente = :idExpedientePadre AND doc.cdAnexoEreg IS NOT NULL)) ");

        queryHql.append(" AND doc.tramite.idValor = :tramite AND ");
        queryHql.append(" doc.fcBajaFila IS NULL ");
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedientePadre", idExpedientePadre);
        params.put("tramite", tramite);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));

        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteDocDTO> buscarPorExpedienteJustecYTramite(Integer idExpedienteJusTec,
            List<Integer> idTramite) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT doc ");
        queryHql.append(" FROM ExpedienteDoc doc ");
        queryHql.append(
                " INNER JOIN ExpedienteJustecDoc ejd ON doc.idExpedienteDoc = ejd.idExpedienteDoc ");
        queryHql.append(" WHERE ejd.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND doc.tramite.idValor IN (:idTramite) ");
        queryHql.append(" AND doc.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);
        params.put("idTramite", idTramite);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
        }

        return lstExpedienteDocDTO;
    }

    @Override
    public List<ExpedienteContrato> listExpedienteContratosPaticipantesPorIdActor(Integer idActor,
            Integer idExpediente)  {

        StringBuilder queryHql = new StringBuilder();
        List<Integer> listaEstadosContratos = new ArrayList<>();
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_SIN_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_CON_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_CONTRATO));

        queryHql.append(" select expC from ExpedienteContrato expC where expC.idExpediente in (");
        queryHql.append(" select exp.idExpediente");
        queryHql.append(" from Expediente exp ");
        queryHql.append(" where ");
        queryHql.append(" exp.idExpedientePadre=:idExpediente ");
        queryHql.append(" AND exp.convocatoriaAct.idActor in");
        queryHql.append(" (select cac.idActor ");
        queryHql.append(" from ConvocatoriaAct cac");
        queryHql.append(" where ");
        queryHql.append(" cac.itContrato = 'S'");
        queryHql.append(" and (cac.idActorCoordinador = :idActor ");
        queryHql.append(" or cac.idActorSolicitante  = :idActor ");
        queryHql.append(
                " ))) and expC.idEstado.idValor in (:listaEstadosContratos) and expC.fcBajaFila is null");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idActor", idActor);
        params.put("listaEstadosContratos", listaEstadosContratos);

        @SuppressWarnings("unchecked")
        List<ExpedienteContrato> result = (List<ExpedienteContrato>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return result;
    }

    @Override
    public List<ExpedienteDoc> listExpedienteContratosDocPaticipantesPorIdActor(Integer idActor,
            Integer idExpediente)  {

        StringBuilder queryHql = new StringBuilder();
        List<Integer> listaEstadosContratos = new ArrayList<>();
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_SIN_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_CON_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUSTITUCION));
        listaEstadosContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_CONTRATO));

        List<Integer> listaEstadosDocContratos = new ArrayList<>();
        listaEstadosDocContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR));
        listaEstadosDocContratos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

        queryHql.append(
                " select expD.expedienteDoc from ExpedienteContratoDoc expD where expD.expedienteContrato.idExpedienteContrato in (");
        queryHql.append(
                " select expC.idExpedienteContrato from ExpedienteContrato expC where expC.idExpediente in (");
        queryHql.append(" select exp.idExpediente");
        queryHql.append(" from Expediente exp ");
        queryHql.append(" where ");
        queryHql.append(" exp.idExpedientePadre=:idExpediente ");
        queryHql.append(" AND exp.convocatoriaAct.idActor in");
        queryHql.append(" (select cac.idActor ");
        queryHql.append(" from ConvocatoriaAct cac");
        queryHql.append(" where ");
        queryHql.append(" cac.itContrato = 'S'");
        queryHql.append(" and (cac.idActorCoordinador = :idActor ");
        queryHql.append(" or cac.idActorSolicitante  = :idActor ");
        queryHql.append(
                " ))) and expC.idEstado.idValor in (:listaEstadosContratos) and expC.fcBajaFila is null ) and expD.expedienteDoc.estado.idValor in (:idEstado ) ");

        Map<String, Object> params = new HashMap<>();
        params.put("idActor", idActor);
        params.put("idExpediente", idExpediente);
        params.put("listaEstadosContratos", listaEstadosContratos);
        params.put("idEstado", listaEstadosDocContratos);

        @SuppressWarnings("unchecked")
        List<ExpedienteDoc> result = (List<ExpedienteDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return result;
    }

    @Override
    public List<ExpedienteDocDTO> consultarExpDocByDatoNmFila(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select doc ");
        queryHql.append(" from ExpedienteDoc doc");
        queryHql.append(" left join Dato dat on dat.bloqueComponente.idBloqueComponente = doc.bloqueComponente.idBloqueComponente");
        queryHql.append(" and dat.expediente.idExpediente = doc.expediente.idExpediente");
        queryHql.append(" and dat.idDato = doc.dato.idDato");
        queryHql.append(" where ");
        queryHql.append(" doc.expediente.idExpediente = :idExpediente");
        queryHql.append(" and dat.fcBajaFila IS NULL");
        queryHql.append(" and doc.fcBajaFila IS NULL");
        queryHql.append(" order by dat.nmFila asc");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        while (it.hasNext()) {
            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));

        }

        return lstExpedienteDocDTO;
        
    }
    

//    @Override
//    public List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize,Integer idSolicitud, List<Integer> lstTramites, Integer idExpediente, final List<AtlasSort> orden) {
//
//
//        StringBuilder queryHql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//
//        queryHql.append(" SELECT doc ");
//        queryHql.append(" FROM ExpedienteDoc doc ");
//        queryHql.append(" WHERE doc.tramite.idValor IN (:idTramites) ");
//        queryHql.append(" AND doc.fcBajaFila IS NULL ");
//        queryHql.append(" AND doc.expediente.solicitud.idSolicitud = :idSolicitud");     
//        if(idExpediente != null ) {
//            queryHql.append(" AND doc.expediente.idExpediente = :idExpediente");   
//            params.put("idExpediente", idExpediente);
//        }
//        queryHql.append(" ORDER BY doc.cdAnexoEreg , doc.idExpedienteDoc"); 
//        
//      
//        params.put("idSolicitud", idSolicitud); 
//        params.put("idTramites", lstTramites);
//
//
//        @SuppressWarnings("unchecked")
//        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
//                queryHql.toString(), params, index, pageSize);
//
//        Iterator<?> it = executeHQLQueryList.iterator();
//
//        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
//        while (it.hasNext()) {
//            ExpedienteDoc expedienteDoc = (ExpedienteDoc) it.next();
//            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
//        }
//
//        return lstExpedienteDocDTO;
//    }
    
//    @Override
//    public List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud, List<Integer> lstTramites, Integer idExpediente, final List<AtlasSort> orden) {
//
//        StringBuilder queryHql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//
//        queryHql.append(" SELECT doc FROM ExpedienteDoc doc ");
//        queryHql.append(" WHERE doc.tramite.idValor IN (:idTramites) ");
//        queryHql.append(" AND doc.fcBajaFila IS NULL ");
//        queryHql.append(" AND doc.expediente.solicitud.idSolicitud = :idSolicitud ");
//
//        params.put("idSolicitud", idSolicitud);
//        params.put("idTramites", lstTramites);
//
//        // Solo añadimos el filtro y el parámetro si el ID existe
//        if (idExpediente != null) {
//            queryHql.append(" AND doc.expediente.idExpediente = :idExpediente ");
//            params.put("idExpediente", idExpediente);
//        }
//
//        queryHql.append(" ORDER BY doc.cdAnexoEreg, doc.idExpedienteDoc ");
//
//        @SuppressWarnings("unchecked")
//        List<ExpedienteDoc> resultado = (List<ExpedienteDoc>) super.executeHQLQueryList(queryHql.toString(), params, index, pageSize);
//
//        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
//        if (resultado != null) {
//            for (ExpedienteDoc ent : resultado) {
//                lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(ent));
//            }
//        }
//
//        return lstExpedienteDocDTO;
//    }
    
//    @Override
//    public List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud, List<Integer> lstTramites, Integer idExpediente, final List<AtlasSort> orden) {
//
//        StringBuilder queryHql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//       
//
//
//        queryHql.append(" SELECT new org.madrid.saii.lib.dto.ExpedienteDocDTO( ");
//        // 1-3. Básicos
//        queryHql.append("  doc.idExpedienteDoc, doc.cdAnexoEreg, doc.cdFica, ");
//        // 4-5. Convocatoria Identificación
//        queryHql.append("  doc.expediente.convocatoriaAct.identificacion.idValor, doc.expediente.convocatoriaAct.identificacion.dsValor, ");
//        // 6. ID Expediente (NUEVO: faltaba en la anterior)
//        queryHql.append("  doc.expediente.idExpediente, ");
//        // 7-8. Convocatoria Tipo Persona
//        queryHql.append("  doc.expediente.convocatoriaAct.tipoActorFisJur.idValor, doc.expediente.convocatoriaAct.tipoActorFisJur.dsValor, ");
//        // 9. Convocatoria Descripción
//        queryHql.append("  doc.expediente.convocatoriaAct.tlDescripcion, ");
//        // 10-13. Interesado
//        queryHql.append("  doc.expediente.interesado.cdNumIden, doc.expediente.interesado.dsNombre, doc.expediente.interesado.dsApellido1, doc.expediente.interesado.dsApellido2, ");
//        // 14-15. Documento
//        queryHql.append("  doc.tipoDocumento.idValor, doc.tipoDocumento.dsValor, ");
//        // 16-17. Trámite
//        queryHql.append("  doc.tramite.idValor, doc.tramite.dsValor, ");
//        // 18-20. Estado (id, ds, nmConstante)
//        queryHql.append("  doc.estado.idValor, doc.estado.dsValor, doc.estado.nmConstante, ");
//        // 21-24. Extras (itAutoriza, cdEreg, fcRegistro, tlObservaciones)
//        queryHql.append("  doc.itAutoriza, doc.codigoEreg, doc.fcRegistro, doc.tlObservaciones, ");
//
//        // 25-27. Subconsultas de Requisitos
//        queryHql.append("  (SELECT CASE WHEN COUNT(req) > 0 THEN true ELSE false END FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL), ");
//        queryHql.append("  (SELECT MAX(req.requisito.dsValor) FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL), ");
//        queryHql.append("  (SELECT MAX(req.tlObservaciones) FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL) ");
//
//       queryHql.append(" ) ");
//        queryHql.append(" FROM org.madrid.saii.lib.domain.ExpedienteDoc doc ");
//        queryHql.append(" WHERE doc.expediente.solicitud.idSolicitud = :idSolicitud ");
//        queryHql.append(" AND doc.tramite.idValor IN (:idTramites) ");
//        queryHql.append(" AND doc.fcBajaFila IS NULL ");
//           
//        params.put("idSolicitud", idSolicitud);
//        params.put("idTramites", lstTramites);
//
//        if (idExpediente != null) {
//            queryHql.append(" AND doc.expediente.idExpediente = :idExpediente ");
//            params.put("idExpediente", idExpediente);
//        }
//
//        // Orden fijo (puedes hacerlo dinámico con la lista 'orden' si quieres)
//        queryHql.append(" ORDER BY doc.cdAnexoEreg, doc.idExpedienteDoc ");
//        
//
//        @SuppressWarnings("unchecked")
//        List<ExpedienteDocDTO> resultado = (List<ExpedienteDocDTO>) super.executeHQLQueryList(
//                queryHql.toString(), params, index, pageSize);
//
//        return (resultado != null) ? resultado : new ArrayList<ExpedienteDocDTO>();
//    }
    
    @Override
    public List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud, List<Integer> lstTramites, Integer idExpediente, final List<AtlasSort> orden) {

        StringBuilder queryHql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        // 1. SELECT con Constructor (usando alias definidos en los JOINs)
        queryHql.append(" SELECT new org.madrid.saii.lib.dto.ExpedienteDocDTO( ");
        queryHql.append("  doc.idExpedienteDoc, doc.cdAnexoEreg, doc.cdFica, "); // 1-3
        queryHql.append("  convIdent.idValor, convIdent.dsValor, ");           // 4-5
        queryHql.append("  exped.idExpediente, ");                             // 6
        queryHql.append("  convTipoAct.idValor, convTipoAct.dsValor, ");       // 7-8
        queryHql.append("  conv.tlDescripcion, ");                             // 9
        queryHql.append("  intere.cdNumIden, intere.dsNombre, intere.dsApellido1, intere.dsApellido2, "); // 10-13
        queryHql.append("  tDoc.idValor, tDoc.dsValor, ");                     // 14-15
        queryHql.append("  tram.idValor, tram.dsValor, ");                     // 16-17
        queryHql.append("  est.idValor, est.dsValor, est.nmConstante, ");       // 18-20
        queryHql.append("  doc.itAutoriza, doc.codigoEreg, doc.fcRegistro, doc.tlObservaciones, "); // 21-24
        
        
        // 25-27. Subconsultas (se mantienen sobre doc)
        queryHql.append("  (SELECT CASE WHEN COUNT(req) > 0 THEN true ELSE false END FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL), ");
        queryHql.append("  (SELECT MAX(req.requisito.dsValor) FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL), ");
        queryHql.append("  (SELECT MAX(req.tlObservaciones) FROM ExpedienteReq req WHERE req.idExpedienteDoc = doc.idExpedienteDoc AND req.fcBajaFila IS NULL), ");
        queryHql.append("  cau.idConvocatoriaCau , ");
        queryHql.append("  doc.fcAltaFila, ");
        queryHql.append("  bloque.itObligatorio,  ");
        
        // 28 Observaciones gestor
        queryHql.append("  doc.tlObservacionesGestor ) ");

        // 2. FROM y LEFT JOINS
        queryHql.append(" FROM org.madrid.saii.lib.domain.ExpedienteDoc doc ");
        queryHql.append(" LEFT JOIN doc.expediente exped ");
        queryHql.append(" LEFT JOIN exped.solicitud soli ");
        queryHql.append(" LEFT JOIN exped.interesado intere ");
        queryHql.append(" LEFT JOIN exped.convocatoriaAct conv ");
        queryHql.append(" LEFT JOIN conv.identificacion convIdent ");
        queryHql.append(" LEFT JOIN conv.tipoActorFisJur convTipoAct ");
        queryHql.append(" LEFT JOIN doc.tipoDocumento tDoc "); // Crucial: si es null, el doc sigue saliendo
        queryHql.append(" LEFT JOIN doc.tramite tram ");       // Crucial: si es null, el doc sigue saliendo
        queryHql.append(" LEFT JOIN doc.estado est ");
        queryHql.append(" LEFT JOIN doc.convocatoriaCau cau ");
        queryHql.append(" LEFT JOIN doc.bloqueComponente bloque ");
        

        // 3. WHERE
        queryHql.append(" WHERE soli.idSolicitud = :idSolicitud ");
        
        // IMPORTANTE: Si los que te faltan tienen trámite NULL, 
        // el filtro "IN" los va a borrar a menos que permitas nulos explícitamente.
        queryHql.append(" AND (tram.idValor IN (:idTramites) OR tram.idValor IS NULL) ");
        
        queryHql.append(" AND doc.fcBajaFila IS NULL ");

        params.put("idSolicitud", idSolicitud);
        params.put("idTramites", lstTramites);

        if (idExpediente != null) {
            queryHql.append(" AND exped.idExpediente = :idExpediente ");
            params.put("idExpediente", idExpediente);
        }

        // 4. ORDER BY (usando alias)
        queryHql.append(" ORDER BY doc.cdAnexoEreg ASC, doc.idExpedienteDoc ASC ");

        @SuppressWarnings("unchecked")
        List<ExpedienteDocDTO> resultado = (List<ExpedienteDocDTO>) super.executeHQLQueryList(
                queryHql.toString(), params, index, pageSize);

        return (resultado != null) ? resultado : new ArrayList<ExpedienteDocDTO>();
    }
    
}
