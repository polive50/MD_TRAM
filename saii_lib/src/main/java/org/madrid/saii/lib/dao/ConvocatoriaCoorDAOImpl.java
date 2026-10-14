package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.dto.ConvocatoriaCoorFiltrosDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorNavegacionDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCoor
 *
 * @author nttdata
 */
@Repository("convocatoriaCoorDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaCoorDAOImpl extends BaseDAOImpl<ConvocatoriaCoor, Integer>
        implements ConvocatoriaCoorDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * @throws ServiceException
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public ConvocatoriaCoorNavegacionDTO obtenerConvocatoriasCoor(
            ConvocatoriaCoorFiltrosDTO filtros) {

        ConvocatoriaCoorNavegacionDTO convocatoriaCoorNav = new ConvocatoriaCoorNavegacionDTO();
        StringBuilder queryHql = new StringBuilder();
        
        
        boolean isEval = filtros.getItCoorEval() != null && filtros.getItCoorEval().equalsIgnoreCase("E");

        queryHql.append("SELECT DISTINCT cv.nmAno,");
        queryHql.append("       cv.cdAcronimo,");
        queryHql.append("       cv.cdTitulo,");
        queryHql.append("       cv.dsDescripcion,");
        queryHql.append("       cv.fcIniTramiEval,");
        queryHql.append("       cv.fcFinTramiEval,");
        queryHql.append("       co.idConvocatoria,");
        queryHql.append("       i.idInteresado,");
        queryHql.append("       i.cdNumIden,");
        queryHql.append("       cv.idEstado.idValor,");
        queryHql.append("       cts.dsValor ");
        queryHql.append("  FROM ConvocatoriaCoor co,");
        queryHql.append("       Convocatoria cv,");
        queryHql.append("       Interesado i,");
        queryHql.append("       CatalogosValores cts ");
        queryHql.append(" WHERE co.fcBajaFila IS NULL");
        queryHql.append("   AND cv.fcBajaFila IS NULL");
        queryHql.append("   AND i.idInteresado");
        queryHql.append("    IN (SELECT idInteresado");
        queryHql.append("          FROM Interesado");
        queryHql.append("         WHERE cdNumIden = :cdNumIden");
        queryHql.append("         AND fcBajaFila IS NULL)");
        queryHql.append("   AND cv.idEstado.idValor = cts.idValor");
        queryHql.append("   AND cv.idConvocatoria = co.idConvocatoria");
        
        if(isEval) {
            queryHql.append("   AND EXISTS (SELECT 1 FROM ConvocatoriaCoorEval ce");
            queryHql.append("               WHERE ce.evaluador.idInteresado = i.idInteresado");
            queryHql.append("               AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
            queryHql.append("               AND EXISTS (SELECT 1 FROM ExpedienteEval ee");
            queryHql.append("                           WHERE ee.persona.idInteresado = ce.evaluador.idInteresado AND ee.fcBajaFila IS NULL))");
        }else {
            queryHql.append("   AND i.idInteresado = co.interesado.idInteresado");
        }
       
        queryHql.append("   AND cv.idEstado.idValor IN :estadosConvocatoria");
        queryHql.append("   AND cv.fcIniTramiEval IS NOT NULL AND cv.fcFinTramiEval IS NOT NULL");
        queryHql.append("   AND cv.fcIniTramiEval <= :currentDate");
        queryHql.append("   AND cv.fcFinTramiEval >= :currentDate");

        if (filtros.getNmAno() != null && filtros.getNmAno() != 0) {
            queryHql.append(" AND cv.nmAno = :nmAno");
        }

        if (StringUtils.isNotBlank(filtros.getCdAcronimo())) {
            queryHql.append(" AND UPPER(cv.cdAcronimo) LIKE UPPER(:cdAcronimo)");
        }

        if (StringUtils.isNotBlank(filtros.getCdTitulo())) {
            queryHql.append(" AND UPPER(cv.cdTitulo) LIKE UPPER(:cdTitulo)");
        }

        queryHql.append(" ORDER BY cv.nmAno DESC , cv.cdAcronimo ");

        Query<?> query = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(queryHql.toString());

        query.setParameter("cdNumIden", filtros.getCdNumIden());
        query.setParameter("currentDate", new Date());

        List<Integer> lstEstadoConvocatoria = new ArrayList<>();
        lstEstadoConvocatoria.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES));
        lstEstadoConvocatoria.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES));
        query.setParameter("estadosConvocatoria", lstEstadoConvocatoria);

        if (filtros.getNmAno() != null && filtros.getNmAno() != 0) {
            query.setParameter("nmAno", filtros.getNmAno());
        }

        if (StringUtils.isNotBlank(filtros.getCdAcronimo())) {
            query.setParameter("cdAcronimo", "%" + filtros.getCdAcronimo() + "%");
        }

        if (StringUtils.isNotBlank(filtros.getCdTitulo())) {
            query.setParameter("cdTitulo", "%" + filtros.getCdTitulo() + "%");
        }

        query.setFirstResult(filtros.getIndex());
        query.setMaxResults(filtros.getPageSize());

        Long countResults = this.obtenerCantidadtConvocatoriasCoor(filtros);

        convocatoriaCoorNav.setIndex(filtros.getIndex());
        convocatoriaCoorNav.setPageSize(filtros.getPageSize());
        convocatoriaCoorNav.setLength(countResults);

        List<Object[]> datos = (List<Object[]>) query.list();

        for (Object[] registro : datos) {

            ConvocatoriaCoorFiltrosDTO convCoorFiltros = new ConvocatoriaCoorFiltrosDTO();

            convCoorFiltros.setNmAno((Integer) registro[0]);
            convCoorFiltros.setCdAcronimo((String) registro[1]);
            convCoorFiltros.setCdTitulo((String) registro[2]);
            convCoorFiltros.setDsDescripcion((String) registro[3]);
            convCoorFiltros.setFcIniTramiEval((Date) registro[4]);
            convCoorFiltros.setFcFinTramiEval((Date) registro[5]);
            convCoorFiltros.setIdConvocatoria((Integer) registro[6]);
            convCoorFiltros.setIdInteresado((Integer) registro[7]);
            convCoorFiltros.setCdNumIden((String) registro[8]);
            convCoorFiltros.setIdEstado((Integer) registro[9]);
            convCoorFiltros.setDsEstado((String) registro[10]);
            
            
            convocatoriaCoorNav.getLstConvocatoriaCoorFiltrosDTO().add(convCoorFiltros);
        }

        return convocatoriaCoorNav;

    }

    /**
     * Metodo para obtener cantidad de registros
     *
     * @param filtros
     * @return
     */
    private Long obtenerCantidadtConvocatoriasCoor(ConvocatoriaCoorFiltrosDTO filtros) {

        StringBuilder countQueryHql = new StringBuilder();
        countQueryHql.append("SELECT count(DISTINCT co.idConvocatoria)");
        countQueryHql.append("  FROM ConvocatoriaCoor co,");
        if(filtros.getItCoorEval() != null && filtros.getItCoorEval().equalsIgnoreCase("E")) {
            countQueryHql.append("       ConvocatoriaCoorEval ce,");
        }
        countQueryHql.append("       Convocatoria cv,");
        countQueryHql.append("       Interesado i");
        countQueryHql.append(" WHERE co.fcBajaFila IS NULL");
        countQueryHql.append("   AND cv.fcBajaFila IS NULL");
        countQueryHql.append("   AND i.idInteresado");
        countQueryHql.append("    IN (SELECT idInteresado");
        countQueryHql.append("          FROM Interesado");
        countQueryHql.append("         WHERE cdNumIden = :cdNumIden");
        countQueryHql.append("         AND fcBajaFila IS NULL)");
        countQueryHql.append("   AND cv.idConvocatoria = co.idConvocatoria");
        
        if(filtros.getItCoorEval() != null && filtros.getItCoorEval().equalsIgnoreCase("E")) {
            countQueryHql.append("   AND i.idInteresado = ce.evaluador.idInteresado");
            countQueryHql.append("   AND ce.idConvocatoriaCoor = co.idConvocatoriaCoor");
        }else {
            countQueryHql.append("   AND i.idInteresado = co.interesado.idInteresado");
        }
        
       
        
        countQueryHql.append("   AND cv.idEstado.idValor IN :estadosConvocatoria");
        countQueryHql.append("   AND cv.fcIniTramiEval IS NOT NULL AND cv.fcFinTramiEval IS NOT NULL");
        countQueryHql.append("   AND cv.fcIniTramiEval <= :currentDate");
        countQueryHql.append("   AND cv.fcFinTramiEval >= :currentDate");

        if (filtros.getNmAno() != null && filtros.getNmAno() != 0) {
            countQueryHql.append(" AND cv.nmAno = :nmAno");
        }

        if (StringUtils.isNotBlank(filtros.getCdAcronimo())) {
            countQueryHql.append(" AND UPPER(cv.cdAcronimo) LIKE UPPER(:cdAcronimo)");
        }

        if (StringUtils.isNotBlank(filtros.getCdTitulo())) {
            countQueryHql.append(" AND UPPER(cv.cdTitulo) LIKE UPPER(:cdTitulo)");
        }

        Query<?> countQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(countQueryHql.toString());

        countQuery.setParameter("cdNumIden", filtros.getCdNumIden());
        countQuery.setParameter("currentDate", new Date());

        List<Integer> lstEstadoConvocatoria = new ArrayList<>();
        lstEstadoConvocatoria.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES));
        lstEstadoConvocatoria.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES));
        countQuery.setParameter("estadosConvocatoria", lstEstadoConvocatoria);

        if (filtros.getNmAno() != null && filtros.getNmAno() != 0) {
            countQuery.setParameter("nmAno", filtros.getNmAno());
        }

        if (StringUtils.isNotBlank(filtros.getCdAcronimo())) {
            countQuery.setParameter("cdAcronimo", "%" + filtros.getCdAcronimo() + "%");
        }

        if (StringUtils.isNotBlank(filtros.getCdTitulo())) {
            countQuery.setParameter("cdTitulo", "%" + filtros.getCdTitulo() + "%");
        }

        return (Long) countQuery.uniqueResult();

    }

}
