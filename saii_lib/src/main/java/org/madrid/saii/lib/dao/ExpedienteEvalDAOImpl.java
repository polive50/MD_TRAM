package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteEval
 *
 * @author nttdata
 */
@Repository("expedienteEvalDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteEvalDAOImpl extends BaseDAOImpl<ExpedienteEval, Integer>
        implements ExpedienteEvalDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
    *
    */
    @Override
    @SuppressWarnings("unchecked")
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            String itCoorEval) {

        List<InformeEvaluacionDTO> lst = new ArrayList<InformeEvaluacionDTO>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT con.idConvocatoria as convIdConvocatoria, ");
        queryHql.append(" con.cdTitulo as convCdTitulo, ");
        queryHql.append(" ex.cdExpediente as expCdExpediente, ");
        queryHql.append(" inte.dsNombre as inteDsNombre, ");
        queryHql.append(" inte.dsApellido1 as inteDsApellido1, ");
        queryHql.append(" inte.dsApellido2 as inteDsApellido2,  ");
        queryHql.append(" exeva.itCoorEval as expEvaItCoorEval, ");
        queryHql.append(" eval.idEvaluacion as evalIdEvaluacion, ");
        queryHql.append(" eval.dsTitulo as evalDsTitulo, ");
        queryHql.append(" eval.tlDescripcion as evalTlDescripcion, ");
        queryHql.append(" eval.nmOrden as evalNmOrden, ");
        queryHql.append(" eval.itCalificacion as evalItCalificacion, ");
        queryHql.append(" evanot.nmNota as evanotNmNota, ");// nota global
        queryHql.append(" apa.idApartado as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");// comentario
                                                                         // apartado
        queryHql.append(" apanot.nmNota as aparNotNmNota, ");// nota apartado
        queryHql.append(" exeva.idExpedienteEval as expEvaIdExpedienteEval, ");
        queryHql.append(" exeva.dsComentario as expEvaDsComentario, ");// comentario
                                                                       // final
        queryHql.append(" exeva.nmNotaFinal as expEvaNmNotaFinal, ");// nota
                                                                     // final
        queryHql.append(" ex.nmNota as expNmNota");// nota expediente

        queryHql.append(" FROM Evaluacion eval ");
        queryHql.append(" LEFT JOIN EvaluacionNotaDato evanot ");
        queryHql.append(" ON eval.idEvaluacion = evanot.idEvaluacion ");
        queryHql.append(" INNER JOIN Apartado apa ");
        queryHql.append(" ON eval.idEvaluacion= apa.idEvaluacion ");
        queryHql.append(" LEFT JOIN ApartadoDato apanot ");
        queryHql.append(" ON apa.idApartado = apanot.idApartado ");
        queryHql.append(" INNER JOIN Convocatoria con ");
        queryHql.append(" ON eval.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" INNER JOIN Solicitud sol ");
        queryHql.append(" ON sol.convocatoria.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" INNER JOIN Expediente ex ");
        queryHql.append(" ON ex.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(" INNER JOIN ExpedienteEval exeva ");
        queryHql.append(" ON exeva.idExpediente = ex.idExpediente ");
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

        queryHql.append(" AND exeva.itCoorEval = :itCoorEval ");
        queryHql.append(" AND exeva.idExpedienteEval = :idExpedienteEval ");
        queryHql.append(" AND apanot.idExpedienteEval = :idExpedienteEval ");

        queryHql.append(" ORDER BY 1, 4, 7 ");

        Map<String, Object> params = new HashMap<>();
        params.put("itCoorEval", itCoorEval);
        params.put("idExpedienteEval", idExpedienteEval);

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
    public List<InfApartadoDatoDTO> obtenerApartadosDatos(Integer idExpedienteEval,
            Integer idEvaluacion) {

        List<InfApartadoDatoDTO> lst = new ArrayList<InfApartadoDatoDTO>();

        StringBuilder queryHql = new StringBuilder();
       
        /*queryHql.append(" SELECT ");
        queryHql.append(" apa.idApartado as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");// comentario
                                                                         // apartado
        queryHql.append(" apanot.nmNota as aparNotNmNota ");// nota apartado
        queryHql.append(" FROM ");
        queryHql.append(" Apartado apa ");
        queryHql.append(" LEFT JOIN ApartadoDato apanot ");
        queryHql.append(" ON apa.idApartado = apanot.idApartado ");
        queryHql.append(" WHERE ");
        queryHql.append(" apa.fcBajaFila is null ");
        queryHql.append(" AND apanot.fcBajaFila is null ");
        queryHql.append(" AND apa.idEvaluacion = :idEvaluacion ");
        queryHql.append(" AND apanot.idExpedienteEval = :idExpedienteEval ");
        queryHql.append(" ORDER BY 1, 4, 7 ");*/
        
        queryHql.append(" SELECT ");
        queryHql.append(" apa.idApartado as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");
        queryHql.append(" apanot.nmNota as aparNotNmNota, ");
        queryHql.append(" apa.itInformativo as aparItInformativo, ");
        queryHql.append(" apa.itObligatorio as itObligatorio ");
        queryHql.append(" FROM Apartado apa ");
        queryHql.append(" LEFT JOIN ApartadoDato apanot ");
        queryHql.append("   ON apa.idApartado = apanot.idApartado ");
        queryHql.append("   AND apanot.fcBajaFila IS NULL ");
        queryHql.append("   AND apanot.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("   AND apanot.idApartadoDato = ( ");
        queryHql.append("       SELECT MAX(a2.idApartadoDato) ");
        queryHql.append("       FROM ApartadoDato a2 ");
        queryHql.append("       WHERE a2.idApartado = apa.idApartado ");
        queryHql.append("       AND a2.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("       AND a2.fcBajaFila IS NULL ");
        queryHql.append("   ) ");
        queryHql.append(" WHERE apa.fcBajaFila IS NULL ");
        queryHql.append(" AND apa.idEvaluacion = :idEvaluacion ");
        queryHql.append(" ORDER BY apa.nmOrden ");

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
            tempDTO.setAparItInformativo((String) object[8]);
            if(object[9] != null) {
                tempDTO.setAparItObligatorio ((String) object[9]); 
            }
            
            lst.add(tempDTO);
        }

        return lst;

    }

    /**
     * @ 
     *
     */
    @Override
    public Integer calcularNotaMediaExpediente(Integer idExpediente)  {
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");
        queryHql.append("SUM(").append("exe.nmNotaFinal").append("), ");
        queryHql.append("COUNT(").append("exe.idExpedienteEval").append(") ");
        queryHql.append("FROM ExpedienteEval exe ");
        queryHql.append("WHERE exe.idExpediente = :idExpediente ");
        queryHql.append("AND exe.fcBajaFila IS NULL ");
        queryHql.append("AND exe.itCoorEval = 'C' ");
        queryHql.append("AND exe.estado.idValor = :idEstado ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idEstado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR));

        Object[] obj = (Object[]) super.executeUniqueHQLQuery(queryHql.toString(), params);

        long sumaRegistros = obj[0] != null ? (Long) obj[0] : 0;
        long totalRegistros = obj[1] != null ? (Long) obj[1] : 0;

        long notaMedia = 0;

        if (totalRegistros > 0) {
            notaMedia = sumaRegistros / totalRegistros;
        }

        return (int) notaMedia;
    }

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<ExpedienteEval> obtenerInformeEvaluacionValidarDesasociar(Integer idPersona,
            String itCoorEval, Integer idConvocatoria) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ee ");
        queryHql.append(" FROM ExpedienteEval ee ");
        queryHql.append(" INNER JOIN Expediente e ");
        queryHql.append(" ON ee.idExpediente = e.idExpediente ");
        queryHql.append(" INNER JOIN Solicitud s ");
        queryHql.append(" ON s.idSolicitud = e.solicitud.idSolicitud ");

        queryHql.append(" WHERE ee.persona.idInteresado = :idInteresado ");
        queryHql.append(" AND ee.itCoorEval = :itCoorEval ");
        queryHql.append(" AND ee.fcBajaFila is null ");
        queryHql.append(" AND s.convocatoria.idConvocatoria = :idConvocatoria ");

        Map<String, Object> params = new HashMap<>();
        params.put("idInteresado", idPersona);
        params.put("itCoorEval", itCoorEval);
        params.put("idConvocatoria", idConvocatoria);

        return (List<ExpedienteEval>) super.executeHQLQueryList(queryHql.toString(), params);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ExpedienteEval> obtenerCoordinadorPorEvaluador(Integer idExpediente, Integer idInteresadoEvaluador, Integer idTipoInforme) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ee FROM ExpedienteEval ee ");
        queryHql.append(" WHERE ee.idExpediente = :idExpediente ");
        queryHql.append(" AND ee.itCoorEval = :itCoorEval ");
        queryHql.append(" AND ee.fcBajaFila IS NULL ");
        queryHql.append(" AND ee.persona.idInteresado IN (");
        queryHql.append("   SELECT coor.interesado.idInteresado FROM ConvocatoriaCoor coor ");
        queryHql.append("   WHERE coor.fcBajaFila IS NULL ");
        queryHql.append("   AND coor.idConvocatoriaCoor IN (");
        queryHql.append("     SELECT eval.idConvocatoriaCoor FROM ConvocatoriaCoorEval eval ");
        queryHql.append("     WHERE eval.evaluador.idInteresado = :idInteresadoEvaluador ");
        queryHql.append("     AND eval.tipoInforme.idValor = :idTipoInforme ");
        queryHql.append("     AND eval.fcBajaFila IS NULL ");
        queryHql.append("   )");
        queryHql.append(" )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("itCoorEval", Constantes.TipoCoorEval.COORDINADOR);
        params.put("idInteresadoEvaluador", idInteresadoEvaluador);
        params.put("idTipoInforme", idTipoInforme);

        return (List<ExpedienteEval>) super.executeHQLQueryList(queryHql.toString(), params);
    }

    @Override
    public List<CatalogosValoresDTO> obtenerListaTipoInformesCoordinador(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador) {
        StringBuilder queryHql = new StringBuilder();
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        queryHql.append(" SELECT DISTINCT ");
        queryHql.append("   co.tipoInforme.idValor,");
        queryHql.append("   cts.dsValor  ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       ExpedienteEval ee, ");
        queryHql.append("       CatalogosValores cts ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND ee.fcBajaFila IS NULL");
        queryHql.append("   AND ee.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("   AND co.tipoInforme.idValor = cts.idValor");
        queryHql.append("   AND co.idConvocatoria = :idConvocatoria ");
        queryHql.append("   AND NOT EXISTS (");
        queryHql.append("       SELECT 1 FROM InformeEvalDoc ied");
        queryHql.append("       WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append("         AND ied.idInforme.tipoInforme.idValor = co.tipoInforme.idValor");
        queryHql.append("         AND ied.fcBajaFila IS NULL");
        queryHql.append("         AND ied.idEstado.idValor = :estadoCerradoCoordinador");
        queryHql.append("   )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", item.getIdExpedienteEval());
        params.put("idConvocatoria", item.getIdConvocatoria());
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor= new CatalogosValoresDTO();
            if( object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]); 
                tempDTO.add(valor);
            }
       }
        
        return tempDTO;

    }
    
    @Override
    public List<CatalogosValoresDTO> obtenerListaTipoInformesEvaluador(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador, Integer estadoCerradoEvaluador) {
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT DISTINCT ce.tipoInforme.idValor,");
        queryHql.append("   ct.dsValor ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       ConvocatoriaCoorEval ce,");
        queryHql.append("       ExpedienteEval ee,");
        queryHql.append("       ExpedienteEval eeCoor,");
        queryHql.append("       CatalogosValores ct ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND ee.fcBajaFila IS NULL");
        queryHql.append("   AND eeCoor.fcBajaFila IS NULL");
        queryHql.append("   AND ee.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("   AND ee.idExpedienteEvalPadre = eeCoor.idExpedienteEval ");
        queryHql.append("   AND co.interesado.idInteresado = eeCoor.persona.idInteresado ");
        queryHql.append("   AND co.idConvocatoria = :idConvocatoria ");
        queryHql.append("   AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
        queryHql.append("   AND ce.tipoInforme.idValor = ct.idValor");
        queryHql.append("   AND ce.evaluador = ee.persona.idInteresado");
        queryHql.append("   AND NOT EXISTS (");
        queryHql.append("       SELECT 1 FROM InformeEvalDoc ied");
        queryHql.append("       WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append("         AND ied.idInforme.tipoInforme.idValor = ce.tipoInforme.idValor");
        queryHql.append("         AND ied.fcBajaFila IS NULL");
        queryHql.append("         AND (ied.idEstado.idValor = :estadoCerradoCoordinador");
        queryHql.append("              OR ied.idEstado.idValor = :estadoCerradoEvaluador)");
        queryHql.append("   )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", item.getIdExpedienteEval());
        params.put("idConvocatoria", item.getIdConvocatoria());
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);
        params.put("estadoCerradoEvaluador", estadoCerradoEvaluador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor= new CatalogosValoresDTO();
            if( object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]);
                tempDTO.add(valor);
            }
       }

        return tempDTO;
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador, Integer estadoCerradoEvaluador) {
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT DISTINCT ce.tipoInforme.idValor,");
        queryHql.append("   ct.dsValor ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       ConvocatoriaCoorEval ce,");
        queryHql.append("       ExpedienteEval ee,");
        queryHql.append("       ExpedienteEval eeCoor,");
        queryHql.append("       CatalogosValores ct ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND ee.fcBajaFila IS NULL");
        queryHql.append("   AND eeCoor.fcBajaFila IS NULL");
        queryHql.append("   AND ee.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("   AND ee.idExpedienteEvalPadre = eeCoor.idExpedienteEval ");
        queryHql.append("   AND co.interesado.idInteresado = eeCoor.persona.idInteresado ");
        queryHql.append("   AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
        queryHql.append("   AND ce.tipoInforme.idValor = ct.idValor");
        queryHql.append("   AND ce.evaluador = ee.persona.idInteresado");
        queryHql.append("   AND NOT EXISTS (");
        queryHql.append("       SELECT 1 FROM InformeEvalDoc ied");
        queryHql.append("       WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append("         AND ied.idInforme.tipoInforme.idValor = ce.tipoInforme.idValor");
        queryHql.append("         AND ied.fcBajaFila IS NULL");
        queryHql.append("         AND (ied.idEstado.idValor = :estadoCerradoCoordinador");
        queryHql.append("              OR ied.idEstado.idValor = :estadoCerradoEvaluador)");
        queryHql.append("   )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", item.getIdExpedienteEval());
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);
        params.put("estadoCerradoEvaluador", estadoCerradoEvaluador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor = new CatalogosValoresDTO();
            if (object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]);
                tempDTO.add(valor);
            }
        }

        return tempDTO;
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesCierreCoordinadorEvaluador(
            Integer idExpedienteEval, Integer estadoCerradoCoordinador) {
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        // Igual que obtenerTipoInformesPendientesEvaluadorInformeEvalDoc, pero exige
        // estrictamente que el tipo de informe este cerrado POR EL COORDINADOR (no
        // basta con que el evaluador lo haya presentado). Se usa para decidir si el
        // ExpedienteEval de ESTE evaluador puede pasar a CERRADO_COORDINADOR al
        // cerrar uno de sus informes (solo si no le queda ningun otro tipo pendiente).
        queryHql.append(" SELECT DISTINCT ce.tipoInforme.idValor,");
        queryHql.append("   ct.dsValor ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       ConvocatoriaCoorEval ce,");
        queryHql.append("       ExpedienteEval ee,");
        queryHql.append("       ExpedienteEval eeCoor,");
        queryHql.append("       CatalogosValores ct ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND ee.fcBajaFila IS NULL");
        queryHql.append("   AND eeCoor.fcBajaFila IS NULL");
        queryHql.append("   AND ee.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("   AND ee.idExpedienteEvalPadre = eeCoor.idExpedienteEval ");
        queryHql.append("   AND co.interesado.idInteresado = eeCoor.persona.idInteresado ");
        queryHql.append("   AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
        queryHql.append("   AND ce.tipoInforme.idValor = ct.idValor");
        queryHql.append("   AND ce.evaluador = ee.persona.idInteresado");
        queryHql.append("   AND NOT EXISTS (");
        queryHql.append("       SELECT 1 FROM InformeEvalDoc ied");
        queryHql.append("       WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append("         AND ied.idInforme.tipoInforme.idValor = ce.tipoInforme.idValor");
        queryHql.append("         AND ied.fcBajaFila IS NULL");
        queryHql.append("         AND ied.cdFica IS NOT NULL");
        queryHql.append("         AND ied.idEstado.idValor = :estadoCerradoCoordinador");
        queryHql.append("   )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor = new CatalogosValoresDTO();
            if (object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]);
                tempDTO.add(valor);
            }
        }

        return tempDTO;
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesRevisionCoordinadorEvaluadores(
            Integer idExpedienteEvalPadre, Integer estadoCerradoCoordinador) {
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        // Igual que obtenerTipoInformesPendientesEvaluadorInformeEvalDoc pero
        // comprobando TODOS los evaluadores (ExpedienteEval) cuyo
        // idExpedienteEvalPadre apunte al coordinador indicado, en lugar de un
        // unico evaluador. Solo se considera resuelto un tipo de informe si
        // esta cerrado POR EL COORDINADOR (no basta con que el evaluador lo
        // haya cerrado).
        queryHql.append(" SELECT DISTINCT ce.tipoInforme.idValor,");
        queryHql.append("   ct.dsValor ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       ConvocatoriaCoorEval ce,");
        queryHql.append("       ExpedienteEval ee,");
        queryHql.append("       ExpedienteEval eeCoor,");
        queryHql.append("       CatalogosValores ct ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND ee.fcBajaFila IS NULL");
        queryHql.append("   AND eeCoor.fcBajaFila IS NULL");
        queryHql.append("   AND eeCoor.idExpedienteEval = :idExpedienteEvalPadre ");
        queryHql.append("   AND ee.idExpedienteEvalPadre = eeCoor.idExpedienteEval ");
        queryHql.append("   AND co.interesado.idInteresado = eeCoor.persona.idInteresado ");
        queryHql.append("   AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
        queryHql.append("   AND ce.tipoInforme.idValor = ct.idValor");
        queryHql.append("   AND ce.evaluador = ee.persona.idInteresado");
        queryHql.append("   AND NOT EXISTS (");
        queryHql.append("       SELECT 1 FROM InformeEvalDoc ied");
        queryHql.append("       WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append("         AND ied.idInforme.tipoInforme.idValor = ce.tipoInforme.idValor");
        queryHql.append("         AND ied.fcBajaFila IS NULL");
        queryHql.append("         AND ied.cdFica IS NOT NULL");
        queryHql.append("         AND ied.idEstado.idValor = :estadoCerradoCoordinador");
        queryHql.append("   )");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEvalPadre", idExpedienteEvalPadre);
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor = new CatalogosValoresDTO();
            if (object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]);
                tempDTO.add(valor);
            }
        }

        return tempDTO;
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(
            ExpedienteFiltrosDTO item, Integer estadoCerradoCoordinador) {
        List<CatalogosValoresDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT DISTINCT co.tipoInforme.idValor, cts.dsValor ");
        queryHql.append("   FROM ConvocatoriaCoor co, ExpedienteEval ee, CatalogosValores cts ");
        queryHql.append("  WHERE co.fcBajaFila IS NULL ");
        queryHql.append("    AND ee.fcBajaFila IS NULL ");
        queryHql.append("    AND ee.idExpedienteEval = :idExpedienteEval ");
        queryHql.append("    AND co.tipoInforme.idValor = cts.idValor ");
        queryHql.append("    AND co.idConvocatoria = :idConvocatoria ");
        queryHql.append("    AND NOT EXISTS ( ");
        queryHql.append("        SELECT 1 FROM InformeEvalDoc ied ");
        queryHql.append("        WHERE ied.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval ");
        queryHql.append("          AND ied.idInforme.tipoInforme.idValor = co.tipoInforme.idValor ");
        queryHql.append("          AND ied.cdFica IS NOT NULL ");
        queryHql.append("          AND ied.idEstado.idValor = :estadoCerradoCoordinador ");
        queryHql.append("          AND ied.fcBajaFila IS NULL ");
        queryHql.append("    ) ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", item.getIdExpedienteEval());
        params.put("idConvocatoria", item.getIdConvocatoria());
        params.put("estadoCerradoCoordinador", estadoCerradoCoordinador);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            CatalogosValoresDTO valor = new CatalogosValoresDTO();
            if (object[0] != null) {
                valor.setIdValor((Integer) object[0]);
                valor.setDsValor((String) object[1]);
                tempDTO.add(valor);
            }
        }

        return tempDTO;
    }

    @Override
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            InformeDTO informeDTO) {
        List<InformeEvaluacionDTO> lst = new ArrayList<InformeEvaluacionDTO>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT inf.idConvocatoria as convIdConvocatoria, ");
        queryHql.append(" con.cdTitulo as convCdTitulo, ");
        queryHql.append(" ex.cdExpediente as expCdExpediente, ");
        queryHql.append(" inte.dsNombre as inteDsNombre, ");
        queryHql.append(" inte.dsApellido1 as inteDsApellido1, ");
        queryHql.append(" inte.dsApellido2 as inteDsApellido2,  ");
        queryHql.append(" exeva.itCoorEval as expEvaItCoorEval, ");
        queryHql.append(" eval.idEvaluacion as evalIdEvaluacion, ");
        queryHql.append(" eval.dsTitulo as evalDsTitulo, ");
        queryHql.append(" eval.tlDescripcion as evalTlDescripcion, ");
        queryHql.append(" eval.nmOrden as evalNmOrden, ");
        queryHql.append(" eval.itCalificacion as evalItCalificacion, ");
        queryHql.append(" evanot.nmNota as evanotNmNota, ");// nota global
        queryHql.append(" apa.idApartado as aparIdApartado, ");
        queryHql.append(" apa.dsTitulo as aparDsTitulo, ");
        queryHql.append(" apa.tlDescripcion as aparTlDescripcion, ");
        queryHql.append(" apa.tlTexto as aparTlTexto, ");
        queryHql.append(" apa.nmOrden as apaNmOrden, ");
        queryHql.append(" apa.nmCalificacionMax as apaNmCalificacionMax, ");
        queryHql.append(" apanot.dsComentario as aparNotDsComentario, ");// comentario
                                                                         // apartado
        queryHql.append(" apanot.nmNota as aparNotNmNota, ");// nota apartado
        queryHql.append(" exeva.idExpedienteEval as expEvaIdExpedienteEval, ");
        queryHql.append(" exeva.dsComentario as expEvaDsComentario, ");// comentario
                                                                       // final
        queryHql.append(" exeva.nmNotaFinal as expEvaNmNotaFinal, ");// nota
                                                                     // final
        queryHql.append(" ex.nmNota as expNmNota,");// nota expediente
        queryHql.append(" solic.dsNombre as solicDsNombre, ");
        queryHql.append(" solic.dsApellido1 as solicDsApellido1, ");
        queryHql.append(" solic.dsApellido2 as solicDsApellido2,  ");
        queryHql.append(" solic.cdNumIden as solicCdNumIden,  ");
        queryHql.append(" inte.cdNumIden as inteCdNumIden  ");
        
        queryHql.append(" FROM Informe inf ");
        queryHql.append(" INNER JOIN Evaluacion eval ");
        queryHql.append(" ON eval.informe.idInforme= inf.idInforme ");
        queryHql.append(" LEFT JOIN EvaluacionNotaDato evanot ");
        queryHql.append(" ON eval.idEvaluacion = evanot.idEvaluacion AND evanot.fcBajaFila is null ");
        queryHql.append(" INNER JOIN Apartado apa ");
        queryHql.append(" ON eval.idEvaluacion= apa.idEvaluacion ");
        queryHql.append(" LEFT JOIN ApartadoDato apanot  ");
        queryHql.append(" ON apa.idApartado = apanot.idApartado  AND apanot.fcBajaFila is null ");
        queryHql.append(" INNER JOIN Convocatoria con ");
        queryHql.append(" ON inf.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" INNER JOIN Solicitud sol ");
        queryHql.append(" ON sol.convocatoria.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" INNER JOIN Expediente ex ");
        queryHql.append(" ON ex.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(" INNER JOIN ExpedienteEval exeva ");
        queryHql.append(" ON exeva.idExpediente = ex.idExpediente ");
        queryHql.append(" INNER JOIN Interesado inte ");
        queryHql.append(" ON exeva.persona.idInteresado = inte.idInteresado ");
        queryHql.append(" INNER JOIN Interesado solic ");
        queryHql.append(" ON ex.interesado.idInteresado = solic.idInteresado ");
        
        queryHql.append(" WHERE inf.fcBajaFila is null ");
        queryHql.append(" AND eval.fcBajaFila is null ");
        
        queryHql.append(" AND apa.fcBajaFila is null ");
        
        queryHql.append(" AND con.fcBajaFila is null ");
        queryHql.append(" AND sol.fcBajaFila is null ");
        queryHql.append(" AND ex.fcBajaFila is null ");
        queryHql.append(" AND exeva.fcBajaFila is null ");
        queryHql.append(" AND inte.fcBajaFila is null ");

        queryHql.append(" AND inf.idInforme = :idInforme ");
        queryHql.append(" AND exeva.idExpedienteEval = :idExpedienteEval ");
        queryHql.append(" AND apanot.idExpedienteEval = :idExpedienteEval ");

        queryHql.append(" ORDER BY 1, 4, 7 ");

        Map<String, Object> params = new HashMap<>();
        Integer idInforme= informeDTO.getIdInforme();
        params.put("idInforme", idInforme);
        params.put("idExpedienteEval", idExpedienteEval);

        @SuppressWarnings("unchecked")
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
            InteresadoDTO solicitante = new InteresadoDTO();
            solicitante.setDsNombre((String) object[25]);
            solicitante.setDsApellido1(Objects.isNull(object[26]) ? "" : (String) object[26]);
            solicitante.setDsApellido2(Objects.isNull(object[27]) ? "" : (String) object[27]);
            solicitante.setCdNumIden((String) object[28]);
            tempDTO.setSolictante(solicitante);
            tempDTO.setInteCdNumIden((String) object[29]);
            lst.add(tempDTO);
        }
        return lst;
    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerListaInformesEvaluador(Integer idExpedienteEval, Integer tipoInforme) {
        List<ExpedienteDocEvalDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT DISTINCT ede");
       
        queryHql.append("   FROM    ExpedienteEval ee,");
        queryHql.append("       ExpedienteDocEval ede ");
       queryHql.append(" WHERE ee.fcBajaFila IS NULL");
        queryHql.append(" AND ede.fcBajaFila IS NULL");
        queryHql.append(" AND ee.idExpedienteEval = :idExpedienteEval");
        queryHql.append(" AND ede.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval");
        queryHql.append(" AND ede.idTipoDocumento.idValor = :tipoInforme");
        queryHql.append(" AND ede.cdFica IS NOT NULL");

       
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        params.put("tipoInforme", tipoInforme);

        @SuppressWarnings("unchecked")
        List<ExpedienteDocEval> executeHQLQueryList = (List<ExpedienteDocEval>) super.executeHQLQueryList(
                queryHql.toString(), params);

        for (ExpedienteDocEval ede : executeHQLQueryList) {
            ExpedienteDocEvalDTO valor = new ExpedienteDocEvalDTO();
            valor.setIdDocumento(ede.getIdDocumento());
            valor.setCdFica(ede.getCdFica());
            valor.setDsNombreFichero(ede.getDsNombreFichero());
            valor.setIdTipoDocumento(CatalogosValoresToDTO.INSTANCE.apply(ede.getIdTipoDocumento()));
            tempDTO.add(valor);
        }

        return tempDTO;

    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerListaDocEvalPorCdFicaInformeEvalDoc(String cdFica) {
        List<ExpedienteDocEvalDTO> tempDTO = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT DISTINCT ede");
        queryHql.append("   FROM ExpedienteDocEval ede, EvaluacionTipoDoc etd, InformeEvalDoc ied");
        queryHql.append("  WHERE ied.cdFica = :cdFica");
        queryHql.append("    AND ied.fcBajaFila IS NULL");
        queryHql.append("    AND etd.fcBajaFila IS NULL");
        queryHql.append("    AND ede.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idInforme.idInforme = etd.idInforme.idInforme");
        queryHql.append("    AND ede.idTipoDocumento.idValor = etd.idTipoDocumento.idValor");
        queryHql.append("    AND ede.idExpedienteEval.idExpedienteEval = ied.idExpedienteEval.idExpedienteEval");
        queryHql.append("    AND ede.cdFica IS NOT NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("cdFica", cdFica);

        @SuppressWarnings("unchecked")
        List<ExpedienteDocEval> result = (List<ExpedienteDocEval>) super.executeHQLQueryList(
                queryHql.toString(), params);

        for (ExpedienteDocEval ede : result) {
            ExpedienteDocEvalDTO valor = new ExpedienteDocEvalDTO();
            valor.setIdDocumento(ede.getIdDocumento());
            valor.setCdFica(ede.getCdFica());
            valor.setDsNombreFichero(ede.getDsNombreFichero());
            valor.setIdTipoDocumento(CatalogosValoresToDTO.INSTANCE.apply(ede.getIdTipoDocumento()));
            tempDTO.add(valor);
        }

        return tempDTO;
    }
}
