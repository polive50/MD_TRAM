package org.madrid.saii.lib.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ConvocatoriaInformeExcelDTO;
import org.madrid.saii.lib.dto.ListadoSituacionEvaluadoresDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementacion para el DAO de la clase de dominio Informe
 *
 * @author nttdata
 */
@Repository("listadoDAO")
@SuppressWarnings("unchecked")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ListadoDAOImpl extends BaseDAOImpl<Listado, Integer> implements ListadoDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * @ 
     *
     */
    @Override
    public List<ConvocatoriaInformeExcelDTO> obtenerListadoExcel(Integer idListado)  {
        List<ConvocatoriaInformeExcelDTO> lstInformeExcel = new ArrayList<>();
        Integer idConvocatoria = this.obtenerIdConvocatoria(idListado);

        // Se recupera primero los expedientes
        StringBuilder queryExpedientes = new StringBuilder();
        queryExpedientes.append("select ");
        // [0] acronimo convocatoria
        queryExpedientes.append("sc.cdAcronimo, ");
        // [1] numero de anualidades de la convocatoria
        queryExpedientes.append("sc.nmDurMaxAyuda, ");
        // [2] id expediente
        queryExpedientes.append("se.idExpediente, ");
        // [3] código del expediente
        queryExpedientes.append("se.cdExpediente, ");
        // [4] nombre del interesado
        queryExpedientes.append("si.dsNombre, ");
        // [5] apellido1 del interesado
        queryExpedientes.append("si.dsApellido1, ");
        // [6] apellido2 del interesado
        queryExpedientes.append("si.dsApellido2, ");
        // [7] tipo de indentificacion del interesado
        queryExpedientes.append("si.tipoIdentificacion.dsValor, ");
        // [8] numero de indentificacion del interesado
        queryExpedientes.append("si.cdNumIden, ");
        // [9] nota final del expediente
        queryExpedientes.append("se.nmNota, ");
        // [10] estado del expediente
        queryExpedientes.append("se.estado.dsValor, ");
        // [11] indicador de alegaciones del expediente
        queryExpedientes.append("se.itAlegaciones ");

        queryExpedientes.append("from Expediente se ");
        queryExpedientes.append(
                "inner join Interesado si on si.idInteresado = se.interesado.idInteresado ");
        queryExpedientes.append("inner join Solicitud ss on ss.idSolicitud = se.solicitud.idSolicitud ");
        queryExpedientes.append(
                "inner join Convocatoria sc on sc.idConvocatoria = ss.convocatoria.idConvocatoria ");

        queryExpedientes.append("where ss.estado.idValor not in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA) +") ");
        queryExpedientes.append("and sc.idConvocatoria = " + idConvocatoria + " ");
        queryExpedientes.append("and si.fcBajaFila is null ");
        queryExpedientes.append("and se.fcBajaFila is null ");
        queryExpedientes.append("and sc.fcBajaFila is null ");
        queryExpedientes.append("and ss.fcBajaFila is null ");
        queryExpedientes.append("order by 1, 3 ");

        List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                queryExpedientes.toString(), new HashMap<>());

        Integer nmAnualidades = null;
        for (Object[] object : listQuery) {
            nmAnualidades = (Integer) object[1];

            ConvocatoriaInformeExcelDTO registro = new ConvocatoriaInformeExcelDTO();

            registro.setCdAcronimoConvocatoria(
                    Objects.nonNull(object[0]) ? String.valueOf(object[0]) : ""); // CONVOCATORIA
            registro.setIdExpediente(
                    Objects.nonNull(object[2]) ? Integer.valueOf(String.valueOf(object[2])) : null); // EXPEDIENTE
            registro.setCdExpediente(Objects.nonNull(object[3]) ? String.valueOf(object[3]) : ""); // EXPEDIENTE

            String dsInteresado = Objects.nonNull(object[4]) ? String.valueOf(object[4]) : "";
            dsInteresado += Objects.nonNull(object[5]) ? " " + object[5] : "";
            dsInteresado += Objects.nonNull(object[6]) ? " " + object[6] : "";
            registro.setDsNombreInteresado(dsInteresado); // INTERESADO
            registro.setDsTipoIdentificadorInteresado(
                    Objects.nonNull(object[7]) ? String.valueOf(object[7]) : ""); // TIPO_IDENTIFICACION
            registro.setCdIdentificadorInteresado(
                    Objects.nonNull(object[8]) ? String.valueOf(object[8]) : ""); // IDENTIFICACION
            registro.setNmNotaFinalExpediente(
                    Objects.nonNull(object[9]) ? String.valueOf(object[9]) : ""); // NOTA
            registro.setDsValor(Objects.nonNull(object[10]) ? String.valueOf(object[10]) : ""); // ESTADO

            String itAlegaciones = "No";
            if (Objects.nonNull(object[11])) {
                itAlegaciones = String.valueOf(object[11]).equals(Constantes.AfirmacionNegacion.S)
                        ? "Si"
                        : "No";
            }
            registro.setItAlegaciones(itAlegaciones);
            lstInformeExcel.add(registro);
        }

        // Se capturan la columnas adicionales configuradas
        listQuery = (List<Object[]>) super.executeHQLQueryList(
                this.obtenerConsultaColumnas(idListado, Boolean.FALSE), new HashMap<>());

        this.agregarDatosDinamicos(listQuery, lstInformeExcel, idConvocatoria, nmAnualidades,
                Boolean.FALSE);

        return lstInformeExcel;
    }

    /**
     * @ 
    *
    */
    @Override
    public List<ConvocatoriaInformeExcelDTO> obtenerListadoExcel2(Integer idConvocatoria)  {
        List<ConvocatoriaInformeExcelDTO> lstInformeExcel = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");

        queryHql.append("conv.cdAcronimo, ");// 0 acronimo convocatoria
        queryHql.append("expe.idExpediente, ");// 1 id expediente
        queryHql.append("expe.cdExpediente, ");// 2 codigo expediente
        queryHql.append("inte.dsNombre, ");// 3 nombre interesado
        queryHql.append("inte.dsApellido1, ");// 4 apellido 1 interesado
        queryHql.append("inte.dsApellido2, ");// 5 apellido 2 interesado
        queryHql.append("cvti.dsValor, ");// 6 tipo identificacion
        queryHql.append("inte.cdNumIden, ");// 7 identificacion
        queryHql.append("expe.nmNota, ");// 8 nota final expediente
        queryHql.append("expe.estado.dsValor, ");// 9 estado expediente
        queryHql.append("expe.itAlegaciones ");// 10 indicador alegaciones

        queryHql.append("FROM ");

        queryHql.append("Convocatoria conv ");
        queryHql.append("LEFT JOIN Solicitud soli ");
        queryHql.append("ON conv.idConvocatoria = soli.convocatoria.idConvocatoria ");
        queryHql.append("LEFT JOIN Expediente expe ");
        queryHql.append("ON soli.idSolicitud = expe.solicitud.idSolicitud ");
        queryHql.append("LEFT JOIN Interesado inte ");
        queryHql.append("ON expe.interesado.idInteresado = inte.idInteresado ");
        queryHql.append("LEFT JOIN CatalogosValores cvti ");
        queryHql.append("ON inte.tipoIdentificacion.idValor = cvti.idValor ");

        queryHql.append("WHERE ");

        queryHql.append("conv.fcBajaFila is null ");
        queryHql.append("AND soli.fcBajaFila is null ");
        queryHql.append("AND expe.fcBajaFila is null ");
        queryHql.append("AND inte.fcBajaFila is null ");
        queryHql.append("AND cvti.fcBajaFila is null ");

        queryHql.append("AND soli.estado.idValor NOT IN ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA) +") ");

        queryHql.append("AND conv.idConvocatoria = :idConvocatoria ");

        queryHql.append("ORDER BY 1, 2");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ConvocatoriaInformeExcelDTO registro = new ConvocatoriaInformeExcelDTO();

            registro.setCdAcronimoConvocatoria(
                    Objects.nonNull(object[0]) ? String.valueOf(object[0]) : ""); // CONVOCATORIA
            registro.setIdExpediente(
                    Objects.nonNull(object[1]) ? Integer.valueOf(String.valueOf(object[1])) : null); // EXPEDIENTE
            registro.setCdExpediente(Objects.nonNull(object[2]) ? String.valueOf(object[2]) : ""); // EXPEDIENTE

            String dsInteresado = Objects.nonNull(object[3]) ? String.valueOf(object[3]) : "";
            dsInteresado += Objects.nonNull(object[4]) ? " " + object[4] : "";
            dsInteresado += Objects.nonNull(object[5]) ? " " + object[5] : "";
            registro.setDsNombreInteresado(dsInteresado); // INTERESADO
            registro.setDsTipoIdentificadorInteresado(
                    Objects.nonNull(object[6]) ? String.valueOf(object[6]) : ""); // TIPO_IDENTIFICACION
            registro.setCdIdentificadorInteresado(
                    Objects.nonNull(object[7]) ? String.valueOf(object[7]) : ""); // IDENTIFICACION
            registro.setNmNotaFinalExpediente(
                    Objects.nonNull(object[8]) ? String.valueOf(object[8]) : ""); // NOTA
            registro.setDsValor(Objects.nonNull(object[9]) ? String.valueOf(object[9]) : ""); // ESTADO
            registro.setItAlegaciones(Objects.nonNull(object[10])
                    ? String.valueOf(object[10]).equals("S") ? "Si" : "No"
                    : "No"); // ALEGACIONES

            lstInformeExcel.add(registro);

        }

        return lstInformeExcel;
    }

    @Override
    public List<ConvocatoriaInformeExcelDTO> obtenerColumnasAdicionales(Integer idListado)  {
        List<ConvocatoriaInformeExcelDTO> lstColumnasAdicionales = new ArrayList<>();
        ConvocatoriaInformeExcelDTO registro = new ConvocatoriaInformeExcelDTO();
        lstColumnasAdicionales.add(registro);

        Integer idConvocatoria = this.obtenerIdConvocatoria(idListado);

        // Se obtiene las columnas adicionales
        List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                this.obtenerConsultaColumnas(idListado, Boolean.TRUE), new HashMap<>());

        Integer nmAnualidades = 0;
        if (!listQuery.isEmpty()) {
            nmAnualidades = (Integer) listQuery.get(0)[4];
        }

        this.agregarDatosDinamicos(listQuery, lstColumnasAdicionales, idConvocatoria, nmAnualidades,
                Boolean.TRUE);

        return lstColumnasAdicionales;
    }
    
    @Override
    public List<ListadoSituacionEvaluadoresDTO> obtenerListadoSituacionEvaluadores(
            Integer idListado) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT x.idExpediente, x.cdExpediente, ");
        query.append("isSol.cdNumIden, isSol.dsNombre, ic.cdNumIden, ");
        query.append("concat(concat(concat(ic.dsApellido1, ' '), ic.dsApellido2), ");
        query.append("concat(', ', ic.dsNombre)), vc.dsValor, vOrg.dsValor, ie.cdNumIden, ");
        query.append("concat(concat(concat(ie.dsApellido1, ' '), ie.dsApellido2), ");
        query.append("concat(', ', ie.dsNombre)), ve.dsValor, vInf.dsValor, infDoc.nmNotaFinal ");
        query.append("FROM Expediente x ");
        query.append("JOIN Solicitud s ON s.idSolicitud = x.solicitud.idSolicitud ");
        query.append("AND s.convocatoria.idConvocatoria = :idConvocatoria ");
        query.append("AND s.fcBajaFila IS NULL ");
        query.append("JOIN Interesado isSol ");
        query.append("ON isSol.idInteresado = x.interesado.idInteresado ");
        query.append("AND isSol.fcBajaFila IS NULL ");
        query.append("JOIN ExpedienteEval ec ON ec.idExpediente = x.idExpediente ");
        query.append("AND ec.itCoorEval = 'C' ");
        query.append("AND ec.fcBajaFila IS NULL ");
        query.append("JOIN Interesado ic ON ic.idInteresado = ec.persona.idInteresado ");
        query.append("AND ic.fcBajaFila IS NULL ");
        query.append("JOIN CatalogosValores vc ON vc.idValor = ec.estado.idValor ");
        query.append("AND vc.fcBajaFila IS NULL ");
        query.append("LEFT JOIN PersonaOrg po ON po.idInteresado = ec.persona.idInteresado ");
        query.append("AND po.fcBajaFila IS NULL ");
        query.append("LEFT JOIN CatalogosValores vOrg ON vOrg.idValor = po.organismo.idValor ");
        query.append("AND vOrg.fcBajaFila IS NULL ");
        query.append("JOIN ConvocatoriaCoor cc ");
        query.append("ON cc.interesado.idInteresado = ec.persona.idInteresado ");
        query.append("AND cc.idConvocatoria = s.convocatoria.idConvocatoria ");
        query.append("AND cc.fcBajaFila IS NULL ");
        query.append("LEFT JOIN ExpedienteEval ee ");
        query.append("ON ee.idExpedienteEvalPadre = ec.idExpedienteEval ");
        query.append("AND ee.itCoorEval = 'E' ");
        query.append("AND ee.fcBajaFila IS NULL ");
        query.append("LEFT JOIN Interesado ie ON ie.idInteresado = ee.persona.idInteresado ");
        query.append("AND ie.fcBajaFila IS NULL ");
        query.append("LEFT JOIN CatalogosValores ve ON ve.idValor = ee.estado.idValor ");
        query.append("AND ve.fcBajaFila IS NULL ");
        query.append("LEFT JOIN ConvocatoriaCoorEval cce ");
        query.append("ON cce.idConvocatoriaCoor = cc.idConvocatoriaCoor ");
        query.append("AND cce.evaluador.idInteresado = ee.persona.idInteresado ");
        query.append("AND cce.fcBajaFila IS NULL ");
        query.append("LEFT JOIN CatalogosValores vInf ");
        query.append("ON vInf.idValor = cce.tipoInforme.idValor ");
        query.append("AND vInf.fcBajaFila IS NULL ");
        query.append("LEFT JOIN Informe inf ");
        query.append("ON inf.idConvocatoria = s.convocatoria.idConvocatoria ");
        query.append("AND inf.tipoInforme.idValor = cce.tipoInforme.idValor ");
        query.append("AND inf.fcBajaFila IS NULL ");
        query.append("LEFT JOIN InformeEvalDoc infDoc ");
        query.append("ON infDoc.idExpedienteEval.idExpedienteEval = ee.idExpedienteEval ");
        query.append("AND infDoc.idInforme.idInforme = inf.idInforme ");
        query.append("AND infDoc.itFirmado = 'S' ");
        query.append("AND infDoc.fcBajaFila IS NULL ");
        query.append("WHERE x.fcBajaFila IS NULL ");
        query.append("ORDER BY x.idExpediente");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", this.obtenerIdConvocatoria(idListado));
        List<Object[]> resultados = (List<Object[]>) super.executeHQLQueryList(query.toString(),
                params);
        List<ListadoSituacionEvaluadoresDTO> listado = new ArrayList<>();
        for (Object[] fila : resultados) {
            ListadoSituacionEvaluadoresDTO registro = new ListadoSituacionEvaluadoresDTO();
            registro.setIdExpediente(fila[0] == null ? null
                    : Integer.valueOf(String.valueOf(fila[0])));
            registro.setCdExpediente(this.valorTexto(fila[1]));
            registro.setNifSolicitante(this.valorTexto(fila[2]));
            registro.setSolicitante(this.valorTexto(fila[3]));
            registro.setNifCoordinador(this.valorTexto(fila[4]));
            registro.setCoordinador(this.valorTexto(fila[5]));
            registro.setEstadoCoordinador(this.valorTexto(fila[6]));
            registro.setOrganismoCoordinador(this.valorTexto(fila[7]));
            registro.setNifEvaluador(this.valorTexto(fila[8]));
            registro.setEvaluador(this.valorTexto(fila[9]));
            registro.setEstadoEvaluador(this.valorTexto(fila[10]));
            registro.setTipoInforme(this.valorTexto(fila[11]));
            registro.setNotaFinal(this.valorTexto(fila[12]));
            listado.add(registro);
        }
        return listado;
    }

    private String valorTexto(Object valor) {
        return valor == null ? "" : String.valueOf(valor);
    }

    private String obtenerConsultaColumnas(Integer idListado, Boolean conConvocatoria) {
        StringBuilder queryColumnas = new StringBuilder();
        queryColumnas.append("select slc, ");
        queryColumnas.append("scv.dsValor, ");
        queryColumnas.append("sc.dsNombre, ");
        queryColumnas.append("cvtf.dsValor ");

        if (Boolean.TRUE.equals(conConvocatoria)) {
            queryColumnas.append(", sco.nmDurMaxAyuda ");
        }

        queryColumnas.append("from ListadoColumna slc ");
        queryColumnas.append("inner join CatalogosValores scv on scv.idValor = slc.idTematica ");
        queryColumnas.append("left join Componente sc on sc.idComponente = slc.idComponente ");
        queryColumnas.append(
                "left join CatalogosValores cvtf on cvtf.idValor = slc.idTipoFinanciacion ");

        if (Boolean.TRUE.equals(conConvocatoria)) {
            queryColumnas.append("inner join Listado sl on sl.idListado = slc.idListado ");
            queryColumnas.append(
                    "inner join Convocatoria sco on sco.idConvocatoria = sl.idConvocatoria ");
        }
        queryColumnas.append("where slc.idListado = " + idListado + " ");
        queryColumnas.append("and slc.fcBajaFila is null ");
        queryColumnas.append("and sc.fcBajaFila is null ");
        queryColumnas.append("order by slc.nmOrden ");

        return queryColumnas.toString();
    }

    private Integer obtenerIdConvocatoria(Integer idListado) {
        StringBuilder queryListado = new StringBuilder();
        queryListado.append("select sl.idConvocatoria ");
        queryListado.append("from Listado sl ");
        queryListado.append("where sl.idListado = " + idListado + " ");
        queryListado.append("and sl.fcBajaFila is null");

        List<Object[]> executeQuery = (List<Object[]>) super.executeHQLQueryList(
                queryListado.toString(), new HashMap<>());
        Iterator<?> it = executeQuery.iterator();

        Integer idConvocatoria = null;
        while (it.hasNext()) {
            idConvocatoria = (Integer) it.next();
        }

        return idConvocatoria;
    }

    private Integer obtenerFormato(ListadoColumna columna) {
        StringBuilder queryFormato = new StringBuilder();
        queryFormato.append("select distinct sb.idFormato.idValor ");
        queryFormato.append("from BloqueComponente sbc ");
        queryFormato.append("inner join Bloque sb on sb.idBloque = sbc.bloque.idBloque ");

        if (Objects.isNull(columna.getIdComponente())) {
            queryFormato.append(
                    "where sbc.tipoFinanciacion.idValor = " + columna.getIdTipoFinanciacion());
        } else {
            queryFormato.append("where sb.idTematica.idValor = " + columna.getIdTematica() + " ");
            queryFormato.append("and sbc.componente.idComponente = " + columna.getIdComponente());
        }

        List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                queryFormato.toString(), new HashMap<>());
        Iterator<?> it = listQuery.iterator();

        Integer formato = null;
        while (it.hasNext()) {
            formato = (Integer) it.next();

        }

        return formato;
    }

    private void agregarPresupuesto(ConvocatoriaInformeExcelDTO registro, String nombreColumna,
            Boolean soloCabecera, Integer nmAnualidades, Integer idTipoFinanciacion,
            Integer idTematica) {
        int[] anualidadesArray = IntStream.range(1, nmAnualidades + 1).toArray();
        Arrays.stream(anualidadesArray).forEach(nmAnualidad -> {
            String key = nombreColumna + " - " + nmAnualidad + "º Anualidad";
            registro.getListaCamposDinamicos().put(key, "");
        });

        if (Boolean.FALSE.equals(soloCabecera)) {
            StringBuilder queryPresupuesto = new StringBuilder();
            queryPresupuesto.append("select ");
            queryPresupuesto.append("sepm.nmImporte, "); // 0
            queryPresupuesto.append("sepm.nmAnualidad "); // 1
            queryPresupuesto.append("from ExpedientePresupMonto sepm ");
            queryPresupuesto.append("inner join ExpedientePresup sep ");
            queryPresupuesto.append("on sep.idExpedientePresup = sepm.idExpedientePresup ");
            queryPresupuesto
                    .append("where sep.tipoFinanciacion.idValor = " + idTipoFinanciacion + " ");
            queryPresupuesto.append("and sep.idExpediente = " + registro.getIdExpediente() + " ");
            queryPresupuesto.append("and sep.tematica.idValor = " + idTematica + " ");
            queryPresupuesto.append("and sep.fcBajaFila is null ");
            queryPresupuesto.append("and sepm.fcBajaFila is null ");
            queryPresupuesto.append("order by 2 ");

            List<Object[]> executeQueryPresup = (List<Object[]>) super.executeHQLQueryList(
                    queryPresupuesto.toString(), new HashMap<>());
            Iterator<?> it = executeQueryPresup.iterator();

            DecimalFormat nf = new DecimalFormat("0.00");
            while (it.hasNext()) {
                Object[] object = (Object[]) it.next();
                Double nmImporte = (Double) object[0];
                String value = nf.format(nmImporte).replace(".", ",") + " €";
                Integer nmAnualidad = (Integer) object[1] + 1;

                String key = nombreColumna + " - " + nmAnualidad + "º Anualidad";
                registro.getListaCamposDinamicos().put(key, value);

            }
        }

    }

    private void agregarAyudaSolicitada(ConvocatoriaInformeExcelDTO registro, String nombreColumna,
            Boolean soloCabecera) {
        registro.getListaCamposDinamicos().put(nombreColumna, "");

        if (Boolean.FALSE.equals(soloCabecera)) {
            StringBuilder queryAyuda = new StringBuilder();
            queryAyuda.append("select distinct sep.nmAyuda ");
            queryAyuda.append("from ExpedientePresup sep ");
            queryAyuda.append("where sep.idExpediente = " + registro.getIdExpediente() + " ");
            queryAyuda.append("and sep.fcBajaFila is null");

            List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                    queryAyuda.toString(), new HashMap<>());
            Iterator<?> it = listQuery.iterator();

            DecimalFormat nf = new DecimalFormat("0.00");
            while (it.hasNext()) {
                Double nmAyuda = (Double) it.next();
                String value = nf.format(nmAyuda).replace(".", ",") + " €";

                registro.getListaCamposDinamicos().put(nombreColumna, value);
            }
        }

    }

    private void agregarDato(ConvocatoriaInformeExcelDTO registro, String nombreColumna,
            Boolean soloCabecera, Integer idComponente) {
        registro.getListaCamposDinamicos().put(nombreColumna, "");

        if (Boolean.FALSE.equals(soloCabecera)) {
            StringBuilder queryAyuda = new StringBuilder();
            queryAyuda.append("select sd.dsValor ");
            queryAyuda.append("from Dato sd ");
            //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
            queryAyuda.append(
                    "inner join BloqueComponente sbc on sbc.idBloqueComponente = sd.bloqueComponente.idBloqueComponente ");
            queryAyuda.append("where sd.expediente.idExpediente = " + registro.getIdExpediente() + " ");
            queryAyuda.append("and sbc.componente.idComponente = " + idComponente + " ");
            queryAyuda.append("and sbc.fcBajaFila is null ");
            queryAyuda.append("and sd.fcBajaFila is null");

            List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                    queryAyuda.toString(), new HashMap<>());
            Iterator<?> it = listQuery.iterator();

            while (it.hasNext()) {
                String value = (String) it.next();
                registro.getListaCamposDinamicos().put(nombreColumna, value);
            }
        }
    }

    private void agregarActuaciones(ConvocatoriaInformeExcelDTO registro, List<String> actuaciones,
            Map<String, String> valuesActuaciones, Boolean soloCabecera) {

        DecimalFormat nf = new DecimalFormat("0.00");
        for (String cdActuacion : actuaciones) {
            for (Map.Entry<String, String> entry : valuesActuaciones.entrySet()) {

                String valueKey = entry.getValue().split("-")[0];
                Integer idComponente = Integer.parseInt(entry.getValue().split("-")[1]);

                String dsColumnna = valueKey + " - " + cdActuacion.toUpperCase() + " - "
                        + entry.getKey();
                registro.getListaCamposDinamicos().put(dsColumnna, "");

                if (Boolean.FALSE.equals(soloCabecera)
                        && !Constantes.ComponentesFinanciacion.DS_MEMORIAS_EXPLICATIVAS
                                .equalsIgnoreCase(entry.getKey())) {

                    String campo = Constantes.Filtro.FILTRO_DS_PROVEEDOR;
                    if (Constantes.ComponentesFinanciacion.DS_OFERTA_IMPORTE
                            .equalsIgnoreCase(entry.getKey())) {
                        campo = Constantes.Filtro.FILTRO_NM_IMPORTE;
                    }

                    StringBuilder queryActuacion = new StringBuilder();
                    queryActuacion.append("select sea." + campo + " ");
                    queryActuacion.append("from ExpedienteActua sea ");
                    queryActuacion
                            .append("where sea.idExpediente = " + registro.getIdExpediente() + " ");
                    queryActuacion.append("and sea.tipoFinanciacion.cdCodigo = '"
                            + cdActuacion.toUpperCase() + "' ");
                    queryActuacion.append("and sea.fcBajaFila is null ");

                    List<Object[]> query = (List<Object[]>) super.executeHQLQueryList(
                            queryActuacion.toString(), new HashMap<>());
                    Iterator<?> it = query.iterator();

                    String value = StringUtils.EMPTY;
                    while (it.hasNext()) {
                        if (Constantes.Filtro.FILTRO_DS_PROVEEDOR.equals(campo)) {
                            value = (String) it.next();
                        } else {
                            Double nmImporte = (Double) it.next();
                            value = nf.format(nmImporte).replace(".", ",") + " €";
                        }
                    }

                    registro.getListaCamposDinamicos().put(dsColumnna, value);
                }

                if (Boolean.FALSE.equals(soloCabecera)
                        && Constantes.ComponentesFinanciacion.DS_MEMORIAS_EXPLICATIVAS
                                .equalsIgnoreCase(entry.getKey())) {
                    String value = this.obtenerValorMemoria(registro.getIdExpediente(),
                            cdActuacion.toUpperCase(), idComponente);
                    registro.getListaCamposDinamicos().put(dsColumnna, value);
                }
            }
        }
    }

    private List<String> obtenerActuaciones(Integer idConvocatoria) {
        List<String> cdActuaciones = new ArrayList<>();

        StringBuilder queryCdActus = new StringBuilder();
        queryCdActus.append("select distinct sea.tipoFinanciacion.cdCodigo ");
        queryCdActus.append("from ExpedienteActua sea ");
        queryCdActus.append("inner join Expediente se on se.idExpediente = sea.idExpediente ");
        queryCdActus.append("inner join Solicitud ss on ss.idSolicitud = se.solicitud.idSolicitud ");
        queryCdActus.append("where ss.estado.idValor not in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA) +") ");
        queryCdActus.append("and ss.convocatoria.idConvocatoria = " + idConvocatoria + " ");
        queryCdActus.append("and ss.fcBajaFila is null ");
        queryCdActus.append("and se.fcBajaFila is null ");
        queryCdActus.append("and sea.fcBajaFila is null ");
        queryCdActus.append("order by 1 ");

        List<Object[]> listQuery = (List<Object[]>) super.executeHQLQueryList(
                queryCdActus.toString(), new HashMap<>());
        Iterator<?> it = listQuery.iterator();
        while (it.hasNext()) {
            String value = (String) it.next();
            cdActuaciones.add(value);
        }

        if (cdActuaciones.isEmpty()) {
            queryCdActus = new StringBuilder();
            queryCdActus.append("select distinct sfd.tipo.cdCodigo ");
            queryCdActus.append("from FinanciacionDato sfd ");
            queryCdActus.append(
                    "inner join Financiacion sf on sf.idFinanciacion = sfd.idFinanciacion ");
            queryCdActus.append("where sf.idConvocatoria = " + idConvocatoria + " ");
            queryCdActus.append("and sf.itTipo = 'AC' ");
            queryCdActus.append("and sf.fcBajaFila is null ");
            queryCdActus.append("and sfd.fcBajaFila is null ");
            queryCdActus.append("order by 1 ");

            listQuery = (List<Object[]>) super.executeHQLQueryList(queryCdActus.toString(),
                    new HashMap<>());

            it = listQuery.iterator();
            while (it.hasNext()) {
                String value = (String) it.next();
                cdActuaciones.add(value);
            }
        }

        return cdActuaciones;
    }

    private String obtenerValorMemoria(Integer idExpediente, String cdActuacion,
            Integer idComponente) {
        String dsValorMemoria = StringUtils.EMPTY;

        // Se obtiene el numero de columna
        Integer nmColumna = 0;
        StringBuilder queryNmColumna = new StringBuilder();
        queryNmColumna.append("select sd.nmColumna ");
        queryNmColumna.append("from Dato sd ");
        //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
        queryNmColumna.append(
                "inner join BloqueComponente sbc on sbc.idBloqueComponente = sd.bloqueComponente.idBloqueComponente ");
        queryNmColumna.append("where sd.expediente.idExpediente = " + idExpediente + " ");
        queryNmColumna.append("and sbc.componente.idComponente = " + idComponente + " ");
        queryNmColumna.append("and sd.fcBajaFila is null ");
        queryNmColumna.append("and sbc.fcBajaFila is null ");

        List<Object[]> query = (List<Object[]>) super.executeHQLQueryList(queryNmColumna.toString(),
                new HashMap<>());

        Iterator<?> it = query.iterator();
        while (it.hasNext()) {
            nmColumna = (Integer) it.next();
            break;
        }

        int numeroDeFilas = query.size();
        if (numeroDeFilas > 0) {
            int posProveedor = -1;
            Boolean posEncontrada = Boolean.FALSE;

            // Obtenemos el proveedorSelecionado
            ExpedienteActua expeActua = new ExpedienteActua();
            StringBuilder querySeleccionado = new StringBuilder();
            querySeleccionado.append("select sea ");
            querySeleccionado.append("from ExpedienteActua sea ");
            querySeleccionado.append("where sea.idExpediente = " + idExpediente + " ");
            querySeleccionado.append("and sea.fcBajaFila is null ");

            query = (List<Object[]>) super.executeHQLQueryList(querySeleccionado.toString(),
                    new HashMap<>());

            it = query.iterator();
            while (it.hasNext()) {
                posProveedor++;
                expeActua = (ExpedienteActua) it.next();
                if (expeActua.getTipoFinanciacion().getCdCodigo().equals(cdActuacion)) {
                    posEncontrada = Boolean.TRUE;
                    break;
                }
            }

            if (Objects.nonNull(expeActua.getIdExpedienteActua())
                    && Boolean.TRUE.equals(posEncontrada)) {
                // Obtenemos la fila de la memoria correspondiente a la
                // actuacion, en
                // base al numero de columna y numero de filas
                // asi como el valor del proveedor seleccionado

                StringBuilder queryFila = new StringBuilder();
                queryFila.append("select sd ");
                queryFila.append("from Dato sd ");
                //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad expediente y no el idExpdte
                queryFila.append("where sd.expediente.idExpediente = " + idExpediente + " ");
                queryFila.append("and sd.nmColumna > " + nmColumna + " ");
                queryFila.append("and rownum <= " + numeroDeFilas + " ");
                queryFila.append("and sd.fcBajaFila is null ");

                query = (List<Object[]>) super.executeHQLQueryList(queryFila.toString(),
                        new HashMap<>());
                Integer nmFila = -1;

                List<Dato> lstDatos = new ArrayList<>();
                it = query.iterator();
                while (it.hasNext()) {
                    lstDatos.add((Dato) it.next());
                }

                nmFila = lstDatos.get(posProveedor).getNmFila();

                if (nmFila != -1) {
                    // Seleccionar el valor de la memoria
                    StringBuilder queryMemoria = new StringBuilder();
                    queryMemoria.append("select sd.dsValor ");
                    queryMemoria.append("from Dato sd ");
                    //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
                    queryMemoria.append(
                            "inner join BloqueComponente sbc on sbc.idBloqueComponente = sd.bloqueComponente.idBloqueComponente ");
                    queryMemoria.append("where sd.expediente.idExpediente = " + idExpediente + " ");
                    queryMemoria.append("and sbc.componente.idComponente = " + idComponente + " ");
                    queryMemoria.append("and sd.nmFila = " + nmFila + " ");
                    queryMemoria.append("and sd.fcBajaFila is null ");
                    queryMemoria.append("and sbc.fcBajaFila is null ");

                    query = (List<Object[]>) super.executeHQLQueryList(queryMemoria.toString(),
                            new HashMap<>());
                    it = query.iterator();
                    while (it.hasNext()) {
                        dsValorMemoria = (String) it.next();
                    }
                }
            }
        }

        return dsValorMemoria;
    }

    private void agregarDatosDinamicos(List<Object[]> listQuery,
            List<ConvocatoriaInformeExcelDTO> lstInformeExcel, Integer idConvocatoria,
            Integer nmAnualidades, Boolean soloCabecera) {
        List<String> actuaciones = new ArrayList<>();
        Boolean seEjecutoQueryActuaciones = Boolean.FALSE;
        LinkedHashMap<String, String> valuesActuaciones = new LinkedHashMap<>();

        for (ConvocatoriaInformeExcelDTO registro : lstInformeExcel) {
            for (Object[] object : listQuery) {
                ListadoColumna columna = (ListadoColumna) object[0];
                Integer indiceActual = listQuery.indexOf(object) + 1;

                String componente = Objects.nonNull(object[2]) ? String.valueOf(object[2]) : "";
                String tematica = Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "";
                String nombreColumna = StringUtils.isBlank(componente) ? tematica
                        : tematica + "-" + componente;

                if (Objects.isNull(columna.getIdComponente())) {
                    String tipoFinanciacion = Objects.nonNull(object[3]) ? String.valueOf(object[3])
                            : "";
                    nombreColumna = tematica + " - " + tipoFinanciacion;
                }

                if (actuaciones.isEmpty() && Boolean.FALSE.equals(seEjecutoQueryActuaciones)) {
                    actuaciones = this.obtenerActuaciones(idConvocatoria);
                    seEjecutoQueryActuaciones = Boolean.TRUE;
                }

                Integer formato = this.obtenerFormato(columna);
                Boolean esDatoNormal = Boolean.TRUE;

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO).equals(formato)) {
                    this.agregarActuaciones(registro, actuaciones, valuesActuaciones, soloCabecera);
                    this.agregarPresupuesto(registro, nombreColumna, soloCabecera, nmAnualidades,
                            columna.getIdTipoFinanciacion(), columna.getIdTematica());
                    esDatoNormal = Boolean.FALSE;
                    valuesActuaciones = new LinkedHashMap<>();
                }

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA).equals(formato)) {
                    this.agregarActuaciones(registro, actuaciones, valuesActuaciones, soloCabecera);
                    this.agregarAyudaSolicitada(registro, nombreColumna, soloCabecera);
                    esDatoNormal = Boolean.FALSE;
                    valuesActuaciones = new LinkedHashMap<>();
                }

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES).equals(formato)) {
                    if (utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.MEMORIAS_EXPLICATIVA)
                            .equals(columna.getIdComponente())
                            || utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR)
                                    .equals(columna.getIdComponente())
                            || utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE)
                                    .equals(columna.getIdComponente())) {

                        valuesActuaciones.put(componente,
                                tematica + "-" + columna.getIdComponente());

                        // Verificamos si el elemento actual es el ultimo
                        if (indiceActual.equals(listQuery.size())) {
                            this.agregarActuaciones(registro, actuaciones, valuesActuaciones,
                                    soloCabecera);
                            valuesActuaciones = new LinkedHashMap<>();
                        }
                    }

                    if (utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_INVERSION)
                            .equals(columna.getIdComponente())) {
                        this.agregarActuaciones(registro, actuaciones, valuesActuaciones,
                                soloCabecera);
                        this.agregarTotalImporteInversion(registro, nombreColumna, soloCabecera);
                        valuesActuaciones = new LinkedHashMap<>();
                    }

                    if (utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA)
                            .equals(columna.getIdComponente())) {
                        this.agregarActuaciones(registro, actuaciones, valuesActuaciones,
                                soloCabecera);
                        this.agregarTotalImporteAyuda(registro, nombreColumna, soloCabecera);
                        valuesActuaciones = new LinkedHashMap<>();
                    }
                    esDatoNormal = Boolean.FALSE;
                }

                if (Boolean.TRUE.equals(esDatoNormal)) {
                    this.agregarActuaciones(registro, actuaciones, valuesActuaciones, soloCabecera);
                    this.agregarDato(registro, nombreColumna, soloCabecera,
                            columna.getIdComponente());
                    valuesActuaciones = new LinkedHashMap<>();
                }
            }
        }
    }

    private void agregarTotalImporteInversion(ConvocatoriaInformeExcelDTO registro,
            String nombreColumna, Boolean soloCabecera) {
        registro.getListaCamposDinamicos().put(nombreColumna, "");

        if (Boolean.FALSE.equals(soloCabecera)) {
            StringBuilder queryInversion = new StringBuilder();
            queryInversion.append("select sum(sea.nmImporte) ");
            queryInversion.append("from ExpedienteActua sea ");
            queryInversion.append("where sea.idExpediente = " + registro.getIdExpediente() + " ");
            queryInversion.append("and sea.fcBajaFila is null ");

            List<Object[]> query = (List<Object[]>) super.executeHQLQueryList(
                    queryInversion.toString(), new HashMap<>());

            Iterator<?> it = query.iterator();
            String value = StringUtils.EMPTY;

            DecimalFormat nf = new DecimalFormat("0.00");
            while (it.hasNext()) {
                Double inversion = (Double) it.next();
                if (Objects.nonNull(inversion)) {
                    value = nf.format(inversion).replace(".", ",") + " €";
                }
            }
            registro.getListaCamposDinamicos().put(nombreColumna, value);
        }
    }

    private void agregarTotalImporteAyuda(ConvocatoriaInformeExcelDTO registro,
            String nombreColumna, Boolean soloCabecera) {
        registro.getListaCamposDinamicos().put(nombreColumna, "");
        if (Boolean.FALSE.equals(soloCabecera)) {
            StringBuilder queryAyuda = new StringBuilder();
            queryAyuda.append("select distinct sea.nmAyuda ");
            queryAyuda.append("from ExpedienteActua sea ");
            queryAyuda.append("where sea.idExpediente = " + registro.getIdExpediente() + " ");
            queryAyuda.append("and sea.fcBajaFila is null ");

            List<Object[]> query = (List<Object[]>) super.executeHQLQueryList(queryAyuda.toString(),
                    new HashMap<>());

            Iterator<?> it = query.iterator();
            String value = StringUtils.EMPTY;

            DecimalFormat nf = new DecimalFormat("0.00");
            while (it.hasNext()) {
                Double nmAyuda = (Double) it.next();
                if (Objects.nonNull(nmAyuda)) {
                    value = nf.format(nmAyuda).replace(".", ",") + " €";
                }
            }
            registro.getListaCamposDinamicos().put(nombreColumna, value);
        }
    }
}
