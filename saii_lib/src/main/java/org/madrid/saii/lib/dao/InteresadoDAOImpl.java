package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Interesado
 *
 * @author nttdatas
 */

@Repository("interesadoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class InteresadoDAOImpl extends BaseDAOImpl<Interesado, Integer> implements InteresadoDAO {

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<InteresadoDTO> obtenerEvaluador(String identificadorCoordinador, String cdUsuario) {
        List<InteresadoDTO> lst = new ArrayList<>();
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append(" idInteresado, ");
        queryHql.append(" cdNumIden, ");
        queryHql.append(" dsNombre, ");
        queryHql.append(" dsApellido1, ");
        queryHql.append(" dsApellido2, ");

        queryHql.append(
                " cdNumIden || ' - ' || dsNombre || ' ' || dsApellido1 || ' ' || dsApellido2 AS dsInteresado ");
        queryHql.append("FROM ");
        queryHql.append("Interesado ");
        queryHql.append("WHERE itCoorEval = :itCoorEval ");

        if (StringUtils.isBlank(cdUsuario)) {
            queryHql.append("AND cdNumIden <> :cdNumIden ");
        } else {
            queryHql.append("AND (cdNumIden <> :cdNumIden or cdUsuario <> :cdUsuario) ");
        }

        queryHql.append("AND fcBajaFila is null ");

        queryHql.append(" ORDER BY dsNombre, dsApellido1, dsApellido2 ");

        Map<String, Object> params = new HashMap<>();
        params.put("itCoorEval", Constantes.TipoCoorEval.INTERESADO_IT_COOR_EVAL);
        if (StringUtils.isBlank(cdUsuario)) {
            params.put("cdNumIden", identificadorCoordinador);
        } else {
            params.put("cdNumIden", identificadorCoordinador);
            params.put("cdUsuario", cdUsuario);
        }

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            InteresadoDTO tempDTO = new InteresadoDTO();
            tempDTO.setIdInteresado((Integer) object[0]);
            tempDTO.setCdNumIden((String) object[1]);
            tempDTO.setDsNombre((String) object[2]);
            tempDTO.setDsApellido1((String) object[3]);
            tempDTO.setDsApellido2((String) object[4]);
            tempDTO.setDsInteresado((String) object[5]);
            lst.add(tempDTO);

        }
        return lst;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InteresadoDTO> obtenerEvaluadoresxCoordinador(String identificadorCoordinador,
            Integer idConvocatoria, String cdCodigo) {
        List<InteresadoDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT distinct ");
        queryHql.append(" inte.idInteresado, ");
        queryHql.append(" inte.cdNumIden, ");
        queryHql.append(" inte.dsNombre, ");
        queryHql.append(" inte.dsApellido1, ");
        queryHql.append(" inte.dsApellido2, ");
        queryHql.append(
                " inte.cdNumIden || ' - ' || inte.dsNombre || ' ' || inte.dsApellido1 || ' ' || inte.dsApellido2 AS dsInteresado ");
        queryHql.append("FROM ConvocatoriaCoorEval eval ");
        queryHql.append(
                " INNER JOIN Interesado inte on eval.evaluador.idInteresado = inte.idInteresado ");
        queryHql.append(" WHERE eval.idConvocatoriaCoor IN ( ");
        queryHql.append("   SELECT coor.idConvocatoriaCoor FROM ConvocatoriaCoor coor ");
        queryHql.append(
                "   WHERE coor.interesado.cdNumIden = :cdNumIden AND coor.idConvocatoria = :idConvocatoria ");
        queryHql.append("   AND coor.fcBajaFila IS NULL ");
        queryHql.append(" ) ");
        queryHql.append(" AND eval.fcBajaFila IS NULL AND inte.fcBajaFila IS NULL ");
        queryHql.append(" ORDER BY inte.dsNombre, inte.dsApellido1, inte.dsApellido2 ");

        Map<String, Object> params = new HashMap<>();
        params.put("cdNumIden", identificadorCoordinador);
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            InteresadoDTO tempDTO = new InteresadoDTO();
            tempDTO.setIdInteresado((Integer) object[0]);
            tempDTO.setCdNumIden((String) object[1]);
            tempDTO.setDsNombre((String) object[2]);
            tempDTO.setDsApellido1((String) object[3]);
            tempDTO.setDsApellido2((String) object[4]);
            tempDTO.setDsInteresado((String) object[5]);

            lst.add(tempDTO);

        }

        return lst;
    }
    
    @Override
    public  List<Interesado> obtenerInteresadosByCdNumIdem(String cdNumIden) {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT i from Interesado as i "); 
        queryHql.append("where i.cdNumIden =:cdNumIden ");
       

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("cdNumIden", cdNumIden);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<Interesado> executeHQLQueryList = (List<Interesado>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Interesado> buscarPersonaNormalizado(String nombre, String apellido1, String apellido2, String cdNumIden,
            String cdUsuario, Integer idArea) {

        String translateExpr = "TRANSLATE(UPPER(%s), 'ÀÁÂÃÄÅÈÉÊËÌÍÎÏÐÑÒÓÔÕÖÙÚÛÜÝÇ', 'AAAAAAEEEEIIIIDNOOOOOOUUUUYC')";

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT DISTINCT i FROM Interesado i, PersonaArea a WHERE i.idInteresado = a.idInteresado and i.itCoorEval = :itCoorEval AND i.fcBajaFila IS NULL and a.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("itCoorEval", Constantes.TipoCoorEval.INTERESADO_IT_COOR_EVAL);

        if (StringUtils.isNotBlank(nombre)) {
            queryHql.append("AND ").append(String.format(translateExpr, "i.dsNombre"))
                    .append(" LIKE :nom ");
            params.put("nom", "%" + Utils.normalizarSinAcentos(nombre) + "%");
        }
        if (StringUtils.isNotBlank(apellido1)) {
            queryHql.append("AND ").append(String.format(translateExpr, "i.dsApellido1"))
                    .append(" LIKE :ap1 ");
            params.put("ap1", "%" + Utils.normalizarSinAcentos(apellido1) + "%");
        }
        if (StringUtils.isNotBlank(apellido2)) {
            queryHql.append("AND ").append(String.format(translateExpr, "i.dsApellido2"))
                    .append(" LIKE :ap2 ");
            params.put("ap2", "%" + Utils.normalizarSinAcentos(apellido2) + "%");
        }
        if (StringUtils.isNotBlank(cdNumIden)) {
            queryHql.append("AND UPPER(i.cdNumIden) = :dni ");
            params.put("dni", cdNumIden.trim().toUpperCase());
        }
        if (StringUtils.isNotBlank(cdUsuario)) {
            queryHql.append("AND UPPER(i.cdUsuario) LIKE :usu ");
            params.put("usu", "%" + cdUsuario.trim().toUpperCase() + "%");
        }
        
        if (idArea != null) {
            queryHql.append("AND a.area.idValor = :idArea ");
            params.put("idArea", idArea);
        }

        return (List<Interesado>) super.executeHQLQueryList(queryHql.toString(), params);
    }
}
