package org.madrid.saii.lib.dao;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import atlas.core.exceptions.ServiceException;

/**
 * Implementación para el DAO de la clase de dominio ExpedienteEval
 *
 * @author quental
 */

@Repository("expedienteEvalJusTecDAO")
public class ExpedienteEvalJusTecDAOImpl extends BaseDAOImpl<ExpedienteEvalJusTec, Integer> implements ExpedienteEvalJusTecDAO{
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @SuppressWarnings("unchecked")
    @Override
    public  List<ExpedienteEvalJusTec> buscarExpEvalJCT(Integer idExpedienteJusTec) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT expEvalJusTec");
        queryHql.append(" FROM ExpedienteEvalJusTec expEvalJusTec ");
        queryHql.append(" WHERE ");
        queryHql.append(" expEvalJusTec.expedienteJustec.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND expEvalJusTec.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);

        return (List<ExpedienteEvalJusTec>) super.executeHQLQueryList(queryHql.toString(), params);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public  List<ExpedienteEvalJusTec> buscarIdExpedienteEvalJCT(Integer idExpedienteJusTec) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT expEvalJusTec");
        queryHql.append(" FROM ExpedienteEvalJusTec expEvalJusTec ");
        queryHql.append(" WHERE ");
        queryHql.append(" expEvalJusTec.expedienteJustec.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND expEvalJusTec.fcBajaFila IS NOT NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);

        return (List<ExpedienteEvalJusTec>) super.executeHQLQueryList(queryHql.toString(), params);
    }
    
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarPeriodosPendienteEvalJustec(Integer idExpediente, Boolean pendienteEval)  {
        
        List<ExpedienteEvalJusTecDTO>  listaResult= new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("exe.idExpedienteEvalJusTec, "); // 0
        queryHql.append("exe.expedienteJustec.idExpedienteJusTec, "); // 1
        queryHql.append(" exe.expedienteJustec.tipoExpedienteJusTec.dsValor, "); // 2
        queryHql.append(" exe.expedienteJustec.tipoExpedienteJusTec.idValor, "); // 3
        queryHql.append(" exe.expedienteJustec.nmPeriodo, "); // 4
        queryHql.append(" exe.dsComentario, "); // 5
        queryHql.append(" exe.nmNotaFinal, "); // 6
        queryHql.append(" exe.itRechazado, "); // 7
        queryHql.append(" exe.estado.idValor, "); // 8
        queryHql.append(" exe.estado.dsValor, "); // 9
        queryHql.append(" exe.cdFica, "); //10
        queryHql.append(" exe.obsEval ");// 11
        queryHql.append("FROM ");
        queryHql.append("ExpedienteEvalJusTec exe ");
       // queryHql.append(" inner join Expediente e ");
       // queryHql.append(" on exe.expedienteJustec.idExpediente  = e.idExpediente ");
        
        queryHql.append("WHERE ");
        if(pendienteEval.equals(Boolean.TRUE)) {
            queryHql.append(" exe.estado.idValor = :estado ");
            queryHql.append(" AND exe.itNotificado = :itNotificado ");
            queryHql.append(" AND exe.expedienteJustec.idExpediente = :idExpediente ");
            queryHql.append(" AND exe.fcIniPlazo <= :currentDate ");
            queryHql.append(" AND exe.fcFinPlazo >= :currentDate "); 
            queryHql.append(" AND exe.fcBajaFila IS NULL "); 
            queryHql.append(" AND exe.expedienteJustec.idEstado.idValor = :idEstadoJustec "); 
            
            params = new HashMap<>();
            Date currentDate = new Date();
            params.put("itNotificado", Constantes.Notificacion.ITNOTIFICADO_S);
            params.put("idExpediente", idExpediente);
            params.put("estado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR_JUSTEC));
            params.put("idEstadoJustec", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION));
            params.put("currentDate", currentDate);
           
        }else {
            queryHql.append(" exe.estado.idValor = :estado ");
            queryHql.append(" AND exe.expedienteJustec.idExpediente = :idExpediente ");
            
            params = new HashMap<>();
            params.put("idExpediente", idExpediente);
            params.put("estado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR_JUSTEC));
           
        }
       
        
        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ExpedienteEvalJusTecDTO tempDTO = new ExpedienteEvalJusTecDTO();
            ExpedienteJusTecDTO expedienteJustecDTO =new ExpedienteJusTecDTO();
            tempDTO.setIdExpedienteEvalJusTec((Integer) object[0]);
            expedienteJustecDTO.setIdExpedienteJusTec((Integer) object[1]);
            CatalogosValoresDTO tipoJustec = new CatalogosValoresDTO();
            tipoJustec.setDsValor((String) object[2]);
            tipoJustec.setIdValor((Integer) object[3]);
            expedienteJustecDTO.setTipoExpedienteJusTecDTO(tipoJustec);
            expedienteJustecDTO.setNmPeriodo((Integer) object[4]);
            tempDTO.setExpedienteJustecDTO(expedienteJustecDTO);
            tempDTO.setDsComentario((String) object[5]);
            tempDTO.setNmNotaFinal(object[6] != null ? (Integer) object[6] : 0);
            tempDTO.setItRechazado(object[7] != null ? (String) object[7] : Constantes.AfirmacionNegacion.N);
            
            CatalogosValoresDTO estadoDTO = new CatalogosValoresDTO();
            estadoDTO.setDsValor((String) object[9]);
            estadoDTO.setIdValor((Integer) object[8]);
            tempDTO.setEstadoDTO(estadoDTO);
            tempDTO.setCdFica((String) object[10]);
            tempDTO.setObsEval((String) object[11]);
            listaResult.add(tempDTO);

         
        }
        return listaResult;
        
    }
    
    /**
    *
    */
    @Override
    @SuppressWarnings("unchecked")
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacionJustec(Integer idExpedienteEvalJustec) {

        List<InformeEvaluacionDTO> lst = new ArrayList<InformeEvaluacionDTO>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT con.idConvocatoria as convIdConvocatoria, ");
        queryHql.append(" con.cdTitulo as convCdTitulo, ");
        queryHql.append(" ex.cdExpediente as expCdExpediente, ");
        queryHql.append(" inte.dsNombre as inteDsNombre, ");
        queryHql.append(" inte.dsApellido1 as inteDsApellido1, ");
        queryHql.append(" inte.dsApellido2 as inteDsApellido2,  ");
        queryHql.append(" eval.itTipoEval as expEvaItCoorEval, ");
        queryHql.append(" eval.idEvaluacionJustec as evalIdEvaluacion, ");
        queryHql.append(" eval.dsTitulo as evalDsTitulo, ");
        queryHql.append(" eval.tlDescripcion as evalTlDescripcion, ");
        queryHql.append(" eval.nmOrden as evalNmOrden, ");
        queryHql.append(" eval.itCalificacion as evalItCalificacion, ");
        queryHql.append(" evanot.nmNota as evanotNmNota, ");// nota global
        queryHql.append(" apa.idEvaluacionJustec as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");// comentario
                                                                         // apartado
        queryHql.append(" apanot.nmNota as aparNotNmNota, ");// nota apartado
        queryHql.append(" exeva.idExpedienteEvalJusTec as expEvaIdExpedienteEval, ");
        queryHql.append(" exeva.dsComentario as expEvaDsComentario, ");// comentario
                                                                       // final
        queryHql.append(" exeva.nmNotaFinal as expEvaNmNotaFinal, ");// nota
                                                                     // final
        queryHql.append(" ex.nmNota as expNmNota");// nota expediente

        queryHql.append(" FROM EvaluacionJustec eval ");
        queryHql.append(" LEFT JOIN EvaluacionJustecNotaDato evanot ");
        queryHql.append(" ON eval.idEvaluacionJustec = evanot.idEvaluacionJustec ");
        queryHql.append(" INNER JOIN EvaluacionJustecAparta apa ");
        queryHql.append(" ON eval.idEvaluacionJustec= apa.idEvaluacionJustec ");
        queryHql.append(" LEFT JOIN ApartadoJustecDato apanot ");
        queryHql.append(" ON apa.idEvaluacionJustecAparta = apanot.idApartado ");
        queryHql.append(" INNER JOIN Convocatoria con ");
        queryHql.append(" ON eval.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" INNER JOIN Solicitud sol ");
        queryHql.append(" ON sol.convocatoria.idConvocatoria = con.idConvocatoria ");
        
        queryHql.append(" INNER JOIN Expediente ex ");
        queryHql.append(" ON ex.solicitud.idSolicitud = sol.idSolicitud ");
        
        queryHql.append(" INNER JOIN ExpedienteEvalJusTec exeva ");
        queryHql.append(" ON exeva.idExpedienteEvalJusTec = apanot.idExpedienteEvalJustec ");
        queryHql.append(" INNER JOIN Interesado inte ");
        queryHql.append(" ON exeva.persona.idInteresado = inte.idInteresado ");

        queryHql.append(" WHERE eval.fcBajaFila is null ");
        queryHql.append(" AND evanot.fcBajaFila is null ");
        queryHql.append(" AND apa.fcBajaFila is null ");
        queryHql.append(" AND apanot.fcBajaFila is null ");
        queryHql.append(" AND con.fcBajaFila is null ");
        queryHql.append(" AND sol.fcBajaFila is null ");
        queryHql.append(" AND ex.fcBajaFila is null ");
        queryHql.append(" AND exeva.fcBajaFila is null ");
        queryHql.append(" AND inte.fcBajaFila is null ");

        queryHql.append(" AND exeva.idExpedienteEvalJusTec = :idExpedienteEvalJustec ");
        queryHql.append(" AND apanot.idExpedienteEvalJustec = :idExpedienteEvalJustec ");

        queryHql.append(" ORDER BY 1, 4, 7 ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEvalJustec", idExpedienteEvalJustec);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            InformeEvaluacionDTO tempDTO = new InformeEvaluacionDTO();
            tempDTO.setConvIdConvocatoria((Integer) object[0]);
            tempDTO.setConvCdTitulo((String) object[1]);
            tempDTO.setExpCdExpediente(Objects.isNull(object[2]) ? "" : (String) object[2]);
            tempDTO.setInteDsNombre((String) object[3]);
            tempDTO.setInteDsApellido1(Objects.isNull(object[4]) ? "" : (String) object[4]);
            tempDTO.setInteDsApellido2(Objects.isNull(object[5]) ? "" : (String) object[5]);
            tempDTO.setExpEvaItCoorEval((String) object[6]);
            tempDTO.setEvalIdEvaluacion((Integer) object[7]);
            tempDTO.setEvalDsTitulo((String) object[8]);
            tempDTO.setEvalTlDescripcion((String) object[9]);
            tempDTO.setEvalNmOrden((Integer) object[10]);
            tempDTO.setEvalItCalificacion((String) object[11]);
            tempDTO.setEvanotNmNota((Integer) object[12]);
            tempDTO.setAparIdApartado((Integer) object[13]);
            tempDTO.setAparDsTitulo((String) object[14]);
            tempDTO.setAparTlDescripcion((String) object[15]);
            tempDTO.setAparTlTexto((String) object[16]);
            tempDTO.setApaNmOrden((Integer) object[17]);
            tempDTO.setApaNmCalificacionMax((Integer) object[18]);
            tempDTO.setAparNotDsComentario((String) object[19]);
            tempDTO.setAparNotNmNota((Integer) object[20]);
            tempDTO.setExpEvaIdExpedienteEval((Integer) object[21]);
            tempDTO.setExpEvaDsComentario((String) object[22]);
            tempDTO.setExpEvaNmNotaFinal((Integer) object[23]);
            tempDTO.setExpNmNota((Integer) object[24]);

            lst.add(tempDTO);
        }
        return lst;
    }

    /**
    *
    */
    @Override
    @SuppressWarnings("unchecked")
    public List<InfApartadoDatoDTO> obtenerApartadosJustecDatos(Integer idExpedienteEval,
            Integer idEvaluacion) {

        List<InfApartadoDatoDTO> lst = new ArrayList<InfApartadoDatoDTO>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ");
        queryHql.append(" apa.idEvaluacionJustecAparta as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");// comentario
                                                                         // apartado
        queryHql.append(" apanot.nmNota as aparNotNmNota ");// nota apartado
        queryHql.append(" FROM ");
        queryHql.append(" EvaluacionJustecAparta apa ");
        queryHql.append(" LEFT JOIN ApartadoJustecDato apanot ");
        queryHql.append(" ON apa.idEvaluacionJustecAparta = apanot.idApartado ");
        queryHql.append(" WHERE ");
        queryHql.append(" apa.fcBajaFila is null ");
        queryHql.append(" AND apanot.fcBajaFila is null ");
        queryHql.append(" AND apa.idEvaluacionJustec = :idEvaluacion ");
        queryHql.append(" AND apanot.idExpedienteEvalJustec = :idExpedienteEval ");
        queryHql.append(" ORDER BY 1, 4, 7 ");

        Map<String, Object> params = new HashMap<>();
        params.put("idEvaluacion", idEvaluacion);
        params.put("idExpedienteEval", idExpedienteEval);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            InfApartadoDatoDTO tempDTO = new InfApartadoDatoDTO();
            tempDTO.setAparIdApartado((Integer) object[0]);
            tempDTO.setAparDsTitulo((String) object[1]);
            tempDTO.setAparTlDescripcion((String) object[2]);
            tempDTO.setAparTlTexto((String) object[3]);
            tempDTO.setApaNmOrden((Integer) object[4]);
            tempDTO.setApaNmCalificacionMax((Integer) object[5]);
            tempDTO.setAparNotDsComentario((String) object[6]);
            tempDTO.setAparNotNmNota((Integer) object[7]);

            lst.add(tempDTO);
        }

        return lst;

    }

    @Override
    public List<ExpedienteEvalJusTecDTO> obtenerListaExpedienteEvalJustec(
            Integer idExpediente, Integer idInteresado) {
        List<ExpedienteEvalJusTecDTO>  listaResult= new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("exe.idExpedienteEvalJusTec, "); // 0
        queryHql.append("exe.expedienteJustec.idExpedienteJusTec, "); // 1
        queryHql.append(" exe.expedienteJustec.tipoExpedienteJusTec.dsValor, "); // 2
        queryHql.append(" exe.expedienteJustec.tipoExpedienteJusTec.idValor, "); // 3
        queryHql.append(" exe.expedienteJustec.nmPeriodo, "); // 4
        queryHql.append(" exe.dsComentario, "); // 5
        queryHql.append(" exe.nmNotaFinal, "); // 6
        queryHql.append(" exe.itRechazado, "); // 7 
        queryHql.append(" exe.estado.idValor, "); //8
        queryHql.append(" exe.estado.dsValor, "); //9
        queryHql.append(" exe.cdFica "); //10
        queryHql.append("FROM ");
        queryHql.append("ExpedienteEvalJusTec exe ");
       // queryHql.append(" inner join Expediente e ");
       // queryHql.append(" on exe.expedienteJustec.idExpediente  = e.idExpediente ");
        
        queryHql.append("WHERE ");
        queryHql.append(" exe.expedienteJustec.idExpediente = :idExpediente ");
        queryHql.append(" AND exe.fcIniPlazo <= :currentDate ");
        queryHql.append(" AND exe.fcFinPlazo >= :currentDate "); 
        queryHql.append(" AND exe.persona.idInteresado = :idInteresado "); 
         Map<String, Object> params = new HashMap<>();
        Date currentDate = new Date();
        params.put("idExpediente", idExpediente);
        params.put("currentDate", currentDate);
        params.put("idInteresado", idInteresado);
        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ExpedienteEvalJusTecDTO tempDTO = new ExpedienteEvalJusTecDTO();
            ExpedienteJusTecDTO expedienteJustecDTO =new ExpedienteJusTecDTO();
            tempDTO.setIdExpedienteEvalJusTec((Integer) object[0]);
            expedienteJustecDTO.setIdExpedienteJusTec((Integer) object[1]);
            CatalogosValoresDTO tipoJustec = new CatalogosValoresDTO();
            tipoJustec.setDsValor((String) object[2]);
            tipoJustec.setIdValor((Integer) object[3]);
            expedienteJustecDTO.setTipoExpedienteJusTecDTO(tipoJustec);
            expedienteJustecDTO.setNmPeriodo((Integer) object[4]);
            tempDTO.setExpedienteJustecDTO(expedienteJustecDTO);
            tempDTO.setDsComentario((String) object[5]);
            tempDTO.setNmNotaFinal(object[6] != null ? (Integer) object[6] : 0);
            tempDTO.setItRechazado(object[7] != null ? (String) object[7] : Constantes.AfirmacionNegacion.N);
            CatalogosValoresDTO  estadoDTO = new CatalogosValoresDTO ();
            estadoDTO.setIdValor((Integer) object[8]);
            estadoDTO.setDsValor((String) object[9]);
            tempDTO.setEstadoDTO(estadoDTO);
            tempDTO.setCdFica((String) object[10]);
            listaResult.add(tempDTO);

         
        }
        return listaResult;
        
    }

   

}