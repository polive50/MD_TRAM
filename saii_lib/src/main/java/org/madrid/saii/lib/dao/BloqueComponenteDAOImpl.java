package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio BloqueComponente
 *
 * @author everis
 */
@Repository("bloqueComponenteDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BloqueComponenteDAOImpl extends BaseDAOImpl<BloqueComponente, Integer>
        implements BloqueComponenteDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<BloqueComponente> generarFilasBloqueComponenteFinanciacion(Integer idBloque,
            Integer idFinanciacion) {
        List<BloqueComponente> lst = new ArrayList<BloqueComponente>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");
        queryHql.append("distinct(tipo.idValor) as fila ");
        queryHql.append("FROM ");
        queryHql.append("FinanciacionDato ");
        queryHql.append("WHERE idFinanciacion = :idFinanciacion ");
        queryHql.append("AND fcBajaFila is null ");

        Map<String, Object> params = new HashMap<>();
        params.put("idFinanciacion", idFinanciacion);

        int i = 1;
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Integer object = (Integer) it.next();

            BloqueComponente temp = new BloqueComponente();

            temp.setBloque(new Bloque());
            temp.getBloque().setIdBloque(idBloque);
            temp.setNmOrden(i);
            temp.setItObligatorio(Constantes.AfirmacionNegacion.S);
            temp.setItRelacion(Constantes.AfirmacionNegacion.N);
            temp.setTipoFinanciacion(new CatalogosValores(object));
            temp.setComponente(null);
            temp.setItFila(Constantes.AfirmacionNegacion.S);
            temp.setItColumna(Constantes.AfirmacionNegacion.N);

            lst.add(temp);
            i++;
        }

        return lst;
    }

    /**
     *
     */
    @Override
    public List<BloqueComponenteDTO> obtenerPorExpediente(Integer idExpediente,
            Integer idFormatoBloque) {

        List<BloqueComponenteDTO> lst = new ArrayList<BloqueComponenteDTO>();
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT bco ");
        queryHql.append("FROM BloqueComponente bco ");
        queryHql.append("INNER JOIN Bloque bo ON bco.bloque.idBloque = bo.idBloque ");
        queryHql.append("INNER JOIN Impreso imp ON bo.impreso.idImpreso = imp.idImpreso ");
        queryHql.append("INNER JOIN Expediente expe ON imp.convocatoriaAct.idActor = expe.convocatoriaAct.idActor ");
        queryHql.append("WHERE expe.idExpediente = :idExpediente ");
        if (Objects.nonNull(idFormatoBloque)) {
            queryHql.append("AND bo.idFormato.idValor = :idFormatoBloque ");
        }
        queryHql.append("AND expe.fcBajaFila IS NULL ");
        queryHql.append("AND imp.fcBajaFila IS NULL ");
        queryHql.append("AND bo.fcBajaFila IS NULL ");
        queryHql.append("AND bco.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        if (Objects.nonNull(idFormatoBloque)) {
            params.put("idFormatoBloque", idFormatoBloque);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            BloqueComponente object = (BloqueComponente) it.next();
            BloqueComponenteDTO bloqueComponenteDTO = BloqueComponenteToDTO.INSTANCE.apply(object);
            lst.add(bloqueComponenteDTO);
        }

        return lst;
    }
    
    
    @Override
    public List<BloqueComponente> listaBloqueComponentesPorConvYTipoDoc(Integer idConvocatoria, Integer idTipoDoc) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select bc from BloqueComponente bc,");
        queryHql.append(" Convocatoria  co, Impreso im, ");
        queryHql.append(" Bloque bl, Componente cm ");
        queryHql.append(" where ");
        queryHql.append(" co.idConvocatoria = im.convocatoria.idConvocatoria");
        queryHql.append(" and im.idImpreso = bl.impreso.idImpreso");
        queryHql.append(" and bl.idBloque = bc.bloque.idBloque");
        queryHql.append(" and bc.componente.idComponente = cm.idComponente");
        
        queryHql.append(" and co.fcBajaFila IS NULL");
        queryHql.append(" and im.fcBajaFila IS NULL");
        queryHql.append(" and cm.fcBajaFila IS NULL");
        queryHql.append(" and bl.fcBajaFila IS NULL");
        queryHql.append(" and bc.fcBajaFila IS NULL");
        queryHql.append(" and co.idConvocatoria = :idConvocatoria");
        queryHql.append(" and cm.tipoDocumento.idValor = :idTipoDoc");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idTipoDoc", idTipoDoc);

        @SuppressWarnings("unchecked")
        List<BloqueComponente> executeHQLQueryList = (List<BloqueComponente>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }
}
