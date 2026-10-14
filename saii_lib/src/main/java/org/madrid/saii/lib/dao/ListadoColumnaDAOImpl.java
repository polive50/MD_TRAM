package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoConfiguracionDTO;
import org.madrid.saii.lib.services.ComponenteService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio ListadoColumna
 *
 * @author nttdata
 */
@Repository("listadoColumnaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ListadoColumnaDAOImpl extends BaseDAOImpl<ListadoColumna, Integer>
        implements ListadoColumnaDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * @ 
     *
     */
    @Override
    public List<ListadoConfiguracionDTO> obtenerListadoColumnas(Integer idConvocatoria,
            Integer idListado)  {
        
        Integer idForDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO);
        Integer idForPresuPrese = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO);
        Integer idForAyuSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA);
        
        List<ListadoConfiguracionDTO> lst = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        if (Objects.nonNull(idListado)) {
            params.put("idListado", idListado);
        }

        String queryPresup = this.queryCabecerasPresupuestos(idListado);

        params.put("idDocumento", idForDoc);
        params.put("presupPresentado", idForPresuPrese);
        params.put("ayudaSol", idForAyuSol);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQuery = (List<Object[]>) super.executeHQLQueryList(queryPresup,
                params);
        Iterator<?> it = executeHQLQuery.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdConvocatoria((Integer) object[0]);
            tempDTO.setCdAcronimo((String) object[1]);
            tempDTO.setIdImpreso((Integer) object[2]);
            tempDTO.setCdIdentificacion((String) object[3]);
            tempDTO.setIdBloque((Integer) object[4]);
            tempDTO.setIdTematica((Integer) object[5]);
            tempDTO.setDsTematica((String) object[6]);
            tempDTO.setIdBloqueComponente((Integer) object[7]);
            tempDTO.setIdComponente((Integer) object[8]);
            tempDTO.setDsComponente((String) object[9]);
            if (Objects.nonNull(idListado)) {
                tempDTO.setIdListadoColumna((Integer) object[10]);
                tempDTO.setNmOrdenListadoColumna((Integer) object[11]);
            }

            lst.add(tempDTO);
        }

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(
                "select c.idConvocatoria, c.cdAcronimo, i.idImpreso, i.cdIdentificacion, b.idBloque,");
        queryHql.append(
                " b.idTematica.idValor, cvt.dsValor, bc.idBloqueComponente, cp.idComponente, cp.dsNombre ");
        if (Objects.nonNull(idListado)) {
            queryHql.append(", ic.idListadoColumna, ic.nmOrden ");
        }
        queryHql.append(" from Impreso i, Convocatoria c, Bloque b,");
        queryHql.append(" BloqueComponente bc, Componente cp, ");
        queryHql.append(" CatalogosValores cvt");
        if (Objects.nonNull(idListado)) {
            queryHql.append(", ListadoColumna ic");
        }
        queryHql.append(
                " where i.convocatoria.idConvocatoria = c.idConvocatoria and b.impreso.idImpreso = i.idImpreso");
        queryHql.append(
                " and bc.bloque.idBloque = b.idBloque and cp.idComponente = bc.componente.idComponente");
        queryHql.append(" and cvt.idValor = b.idTematica.idValor");
        queryHql.append(" and b.idFormato.idValor not in (" + idForDoc + ", ");
        queryHql.append(idForPresuPrese + ", " + idForAyuSol + ")");
        queryHql.append(
                " and cp.idComponente not between " + utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.IMPORTE)
                        + " and " + utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA3_IMPORTE));
        queryHql.append(" and c.idConvocatoria = :idConvocatoria");
        if (Objects.nonNull(idListado)) {
            queryHql.append(" and bc.idBloqueComponente = ic.idBloqueComponente ");
            queryHql.append(" and ic.idListado = :idListado");
            queryHql.append(" and ic.fcBajaFila is null ");
        }

        queryHql.append(" and c.fcBajaFila is null");
        queryHql.append(" and bc.fcBajaFila is null");
        queryHql.append(" and cp.fcBajaFila is null");
        queryHql.append(" and cvt.fcBajaFila is null");
        queryHql.append(" and i.fcBajaFila is null");
        queryHql.append(" and b.fcBajaFila is null");

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdConvocatoria((Integer) object[0]);
            tempDTO.setCdAcronimo((String) object[1]);
            tempDTO.setIdImpreso((Integer) object[2]);
            tempDTO.setCdIdentificacion((String) object[3]);
            tempDTO.setIdBloque((Integer) object[4]);
            tempDTO.setIdTematica((Integer) object[5]);
            tempDTO.setDsTematica((String) object[6]);
            tempDTO.setIdBloqueComponente((Integer) object[7]);
            tempDTO.setIdComponente((Integer) object[8]);
            tempDTO.setDsComponente((String) object[9]);
            if (Objects.nonNull(idListado)) {
                tempDTO.setIdListadoColumna((Integer) object[10]);
                tempDTO.setNmOrdenListadoColumna((Integer) object[11]);
            }

            lst.add(tempDTO);

        }

        if (Objects.nonNull(idListado) && !lst.isEmpty()) {
            lst.sort(Comparator.comparing(ListadoConfiguracionDTO::getNmOrdenListadoColumna));
        }

        return lst;

    }

    private String queryCabecerasPresupuestos(Integer idListado) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select ");
        queryHql.append("sconv.idConvocatoria, "); // [0]
        queryHql.append("sconv.cdAcronimo, "); // [1]
        queryHql.append("si.idImpreso, "); // [2]
        queryHql.append("si.cdIdentificacion, "); // [3]
        queryHql.append("sb.idBloque, "); // [4]
        queryHql.append("sb.idTematica.idValor, "); // [5]
        queryHql.append("scv.dsValor, "); // [6]
        queryHql.append("sbc.idBloqueComponente, "); // [7]
        queryHql.append("sbc.componente.idComponente, "); // [8]
        queryHql.append("sbc.tipoFinanciacion.dsValor "); // [9]
        if (Objects.nonNull(idListado)) {
            queryHql.append(", slc.idListadoColumna, "); // [10]
            queryHql.append("slc.nmOrden "); // [11]
        }
        queryHql.append("from BloqueComponente sbc ");
        queryHql.append("inner join Bloque sb on sb.idBloque = sbc.bloque.idBloque ");
        queryHql.append("inner join CatalogosValores scv on scv.idValor = sb.idTematica.idValor ");
        queryHql.append("inner join Impreso si on si.idImpreso = sb.impreso.idImpreso ");
        queryHql.append(
                "inner join Convocatoria sconv on sconv.idConvocatoria = si.idConvocatoria ");
        if (Objects.nonNull(idListado)) {
            queryHql.append(
                    "inner join ListadoColumna slc on slc.idBloqueComponente = sbc.idBloqueComponente ");
        }
        queryHql.append("where sb.idBloque in ( ");
        queryHql.append("select sb.idBloque from Bloque sb ");
        queryHql.append("inner join Impreso si on si.idImpreso = sb.impreso.idImpreso ");
        queryHql.append(
                "inner join Convocatoria sconv on sconv.idConvocatoria = si.idConvocatoria ");
        queryHql.append("where ");
        queryHql.append("sb.idFormato.idValor != :idDocumento and ");
        queryHql.append("(sb.idFormato.idValor = :presupPresentado or");
        queryHql.append(" sb.idFormato.idValor = :ayudaSol) and ");
        queryHql.append("sconv.fcBajaFila is null and ");
        queryHql.append("si.fcBajaFila is null and ");
        queryHql.append("sb.fcBajaFila is null and ");
        queryHql.append("sconv.idConvocatoria = :idConvocatoria ) ");
        queryHql.append("and sbc.fcBajaFila is null ");
        queryHql.append("and sb.fcBajaFila is null ");
        queryHql.append("and scv.fcBajaFila is null ");
        queryHql.append("and si.fcBajaFila is null ");
        queryHql.append("and sconv.fcBajaFila is null ");
        if (Objects.nonNull(idListado)) {
            queryHql.append(" and slc.idListado = :idListado");
            queryHql.append(" and slc.fcBajaFila is null ");
        }

        return queryHql.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ListadoConfiguracionDTO> obtenerListaColumnaDisponible(Integer idConvocatoria)  {
        
        Integer idFormPresuPresen = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO);
        Integer idFormAyuSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA);
        Integer idFormDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO);
        
        List<ListadoConfiguracionDTO> opcionesDisponibles = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(
                "SELECT DISTINCT scv.idValor, scv.dsValor, sc.dsNombre, sbc.componente.idComponente ");
        queryHql.append("FROM Bloque sb ");
        queryHql.append("INNER JOIN CatalogosValores scv ON scv.idValor = sb.idTematica.idValor ");
        queryHql.append("INNER JOIN BloqueComponente sbc ON sbc.bloque.idBloque = sb.idBloque ");
        queryHql.append(
                "INNER JOIN Componente sc ON sc.idComponente = sbc.componente.idComponente ");
        queryHql.append("INNER JOIN Impreso si ON si.idImpreso = sb.impreso.idImpreso ");
        queryHql.append("WHERE sb.idFormato.idValor NOT IN (");
        queryHql.append(idFormPresuPresen+ ",");
        queryHql.append(idFormAyuSol+ ",");
        queryHql.append(idFormDoc);
        queryHql.append(") ");
        queryHql.append("AND sc.idComponente NOT IN ( ");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.CODIGO_ACTUACION) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.ACTUACION) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA2_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA2_IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA3_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA3_IMPORTE));
        queryHql.append(") ");
        queryHql.append("AND sb.fcBajaFila IS NULL ");
        queryHql.append("AND scv.fcBajaFila IS NULL ");
        queryHql.append("AND sbc.fcBajaFila IS NULL ");
        queryHql.append("AND sc.fcBajaFila IS NULL ");
        queryHql.append("AND si.fcBajaFila IS NULL ");
        queryHql.append("AND si.convocatoria.idConvocatoria = :idConvocatoria ");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdTematica((Integer) object[0]);
            tempDTO.setDsTematica((String) object[1]);
            tempDTO.setDsComponente((String) object[2]);
            tempDTO.setIdComponente((Integer) object[3]);

            tempDTO.setDsNombreElemento(
                    tempDTO.getDsTematica() + (Objects.nonNull(tempDTO.getDsComponente())
                            ? " - " + tempDTO.getDsComponente()
                            : ""));

            opcionesDisponibles.add(tempDTO);

        }

        // Recuperamos las opciones de los bloques presupuestos y ayuda
        queryHql = new StringBuilder();
        queryHql.append(
                "SELECT DISTINCT scv.idValor, scv.dsValor, sc.dsValor, sbc.tipoFinanciacion.idValor ");
        queryHql.append("FROM Bloque sb ");
        queryHql.append("INNER JOIN CatalogosValores scv ON scv.idValor = sb.idTematica.idValor ");
        queryHql.append("INNER JOIN BloqueComponente sbc ON sbc.bloque.idBloque = sb.idBloque ");
        queryHql.append(
                "INNER JOIN CatalogosValores sc ON sc.idValor = sbc.tipoFinanciacion.idValor ");
        queryHql.append("INNER JOIN Impreso si ON si.idImpreso = sb.impreso.idImpreso ");
        queryHql.append("WHERE sb.idFormato.idValor IN (");
        queryHql.append(idFormPresuPresen+ ",");
        queryHql.append(idFormAyuSol);
        queryHql.append(") ");
        queryHql.append("AND sbc.tipoFinanciacion.idValor IS NOT NULL ");
        queryHql.append("AND sb.fcBajaFila IS NULL ");
        queryHql.append("AND scv.fcBajaFila IS NULL ");
        queryHql.append("AND sbc.fcBajaFila IS NULL ");
        queryHql.append("AND sc.fcBajaFila IS NULL ");
        queryHql.append("AND si.fcBajaFila IS NULL ");
        queryHql.append("AND si.convocatoria.idConvocatoria = :idConvocatoria ");
        
        executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(),
                params);

        it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdTematica((Integer) object[0]);
            tempDTO.setDsTematica((String) object[1]);
            tempDTO.setDsComponente((String) object[2]);
            tempDTO.setIdTipoFinanciacion((Integer) object[3]);

            tempDTO.setDsNombreElemento(
                    tempDTO.getDsTematica() + (Objects.nonNull(tempDTO.getDsComponente())
                            ? " - " + tempDTO.getDsComponente()
                            : ""));

            opcionesDisponibles.add(tempDTO);

        }

        return opcionesDisponibles;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ListadoConfiguracionDTO> obtenerListaColumnaConfigurada(Integer idListado)  {
        
        Integer idFormPresuPresen = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO);
        Integer idFormAyuSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA);
        Integer idFormDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO);
        
        List<ListadoConfiguracionDTO> opcionesDisponibles = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(
                "SELECT DISTINCT scv.idValor, scv.dsValor, sc.dsNombre, sbc.componente.idComponente, ");
        queryHql.append("lc.idListadoColumna, lc.nmOrden ");
        queryHql.append("FROM Bloque sb ");
        queryHql.append("INNER JOIN CatalogosValores scv ON scv.idValor = sb.idTematica.idValor ");
        queryHql.append("INNER JOIN BloqueComponente sbc ON sbc.bloque.idBloque = sb.idBloque ");
        queryHql.append(
                "INNER JOIN Componente sc ON sc.idComponente = sbc.componente.idComponente ");

        queryHql.append(
                "INNER JOIN ListadoColumna lc ON sc.idComponente = lc.idComponente AND sb.idTematica.idValor = lc.idTematica ");

        queryHql.append("WHERE sb.idFormato.idValor NOT IN (");
        queryHql.append(idFormPresuPresen+ ",");
        queryHql.append(idFormAyuSol+ ",");
        queryHql.append(idFormDoc);
        queryHql.append(") ");
        queryHql.append("AND sc.idComponente NOT IN ( ");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.CODIGO_ACTUACION) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.ACTUACION) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA2_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA2_IMPORTE) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA3_PROVEEDOR) +",");
        queryHql.append(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA3_IMPORTE));
        queryHql.append(") ");
        queryHql.append("AND sb.fcBajaFila IS NULL ");
        queryHql.append("AND scv.fcBajaFila IS NULL ");
        queryHql.append("AND sbc.fcBajaFila IS NULL ");
        queryHql.append("AND sc.fcBajaFila IS NULL ");

        queryHql.append("AND lc.fcBajaFila IS NULL ");
        queryHql.append("AND lc.idListado = :idListado ");

        Map<String, Object> params = new HashMap<>();
        params.put("idListado", idListado);
        
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdTematica((Integer) object[0]);
            tempDTO.setDsTematica((String) object[1]);
            tempDTO.setDsComponente((String) object[2]);
            tempDTO.setIdComponente((Integer) object[3]);
            tempDTO.setIdListadoColumna((Integer) object[4]);
            tempDTO.setNmOrdenListadoColumna((Integer) object[5]);

            tempDTO.setDsNombreElemento(
                    tempDTO.getDsTematica() + (Objects.nonNull(tempDTO.getDsComponente())
                            ? " - " + tempDTO.getDsComponente()
                            : ""));

            opcionesDisponibles.add(tempDTO);

        }

        // Recuperamos las opciones de los bloques presupuestos y ayuda
        queryHql = new StringBuilder();
        queryHql.append(
                "SELECT DISTINCT scv.idValor, scv.dsValor, sc.dsValor, sbc.tipoFinanciacion.idValor, ");
        queryHql.append("lc.idListadoColumna, lc.nmOrden ");
        queryHql.append("FROM Bloque sb ");
        queryHql.append("INNER JOIN CatalogosValores scv ON scv.idValor = sb.idTematica.idValor ");
        queryHql.append("INNER JOIN BloqueComponente sbc ON sbc.bloque.idBloque = sb.idBloque ");
        queryHql.append(
                "INNER JOIN CatalogosValores sc ON sc.idValor = sbc.tipoFinanciacion.idValor ");
        queryHql.append(
                "INNER JOIN ListadoColumna lc ON sc.idValor = lc.idTipoFinanciacion AND sb.idTematica.idValor = lc.idTematica ");

        queryHql.append("WHERE sb.idFormato.idValor IN (");
        queryHql.append(idFormPresuPresen+ ",");
        queryHql.append(idFormAyuSol);
        queryHql.append(") ");
        queryHql.append("AND sbc.tipoFinanciacion.idValor IS NOT NULL ");
        queryHql.append("AND sb.fcBajaFila IS NULL ");
        queryHql.append("AND scv.fcBajaFila IS NULL ");
        queryHql.append("AND sbc.fcBajaFila IS NULL ");
        queryHql.append("AND sc.fcBajaFila IS NULL ");

        queryHql.append("AND lc.fcBajaFila IS NULL ");
        queryHql.append("AND lc.idListado = :idListado ");

        executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(),
                params);

        it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ListadoConfiguracionDTO tempDTO = new ListadoConfiguracionDTO();

            tempDTO.setIdTematica((Integer) object[0]);
            tempDTO.setDsTematica((String) object[1]);
            tempDTO.setDsComponente((String) object[2]);
            tempDTO.setIdTipoFinanciacion((Integer) object[3]);
            tempDTO.setIdListadoColumna((Integer) object[4]);
            tempDTO.setNmOrdenListadoColumna((Integer) object[5]);

            tempDTO.setDsNombreElemento(
                    tempDTO.getDsTematica() + (Objects.nonNull(tempDTO.getDsComponente())
                            ? " - " + tempDTO.getDsComponente()
                            : ""));

            opcionesDisponibles.add(tempDTO);

        }

        opcionesDisponibles
                .sort(Comparator.comparing(ListadoConfiguracionDTO::getNmOrdenListadoColumna));

        return opcionesDisponibles;
    }

}
