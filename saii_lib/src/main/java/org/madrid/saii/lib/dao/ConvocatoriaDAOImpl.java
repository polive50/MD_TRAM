package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio Convocatoria
 *
 * @author everis
 */
@Repository("convocatoriaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaDAOImpl extends BaseDAOImpl<Convocatoria, Integer>
implements ConvocatoriaDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * @throws ServiceException 
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Convocatoria> buscarConvocatoriasActivas()  {

        Integer idEstConvApro = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.APROBADA);
        Integer idEstConvIniPlanPreSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES);
        Integer idEstConvFinPlanPreSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES);
        Integer idEstConvEnCons = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.EN_CONSTRUCCION);
        
        // Se buscan las convocatorias activas
        // que la modalidad sea a instancia del solicitanta
        // y que esten aprobadas o iniciado plazo o bien esten en revision
        // evaluacion pero con el solicitudes fuera de plazo a S
        // o hasta agotar fondos y fecha fin presentacion esta vacia
        // o abierta permanente y fecha fin presentacion esta vacia
        
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("Select c from Convocatoria as c");
        queryHql.append(" where ");
        queryHql.append(" c.itConvocatoriaActiva = 'S'");
        // Se ha comentado debido a que hay tipo modalidad administrativo que no va a salir nunca!!
       /* queryHql.append(
                " and idTipoModalidad.idValor = " + Constantes.TipoModalidad.SOLICITANTE_CODIGO);*/
        queryHql.append(" and (");
        queryHql.append(" c.idEstado IN (" + idEstConvIniPlanPreSol + ")");
        queryHql.append(" or ");
        queryHql.append(" ( c.idEstado IN (" + idEstConvFinPlanPreSol + ") and itPresFueraPlazo = 'S' )");
        queryHql.append(" )");
        queryHql.append(" or ");
        queryHql.append("( c.idEstado not in ("+  idEstConvApro + "," + idEstConvEnCons +") AND c.itConvocatoriaActiva = 'S' and ( c.fcFinPresentacion IS NULL AND c.itAgotarFondos='S' ) )");
        queryHql.append(" or ");
        queryHql.append(" ( c.idEstado not in ("+  idEstConvApro + "," + idEstConvEnCons +") AND  c.itConvocatoriaActiva = 'S' and ( c.fcFinPresentacion IS NULL AND c.itAbiertaPermanente='S' ) )");

        return (List<Convocatoria>) super.executeHQLQueryList(queryHql.toString(), null);
    }

    /**
     * @throws ServiceException 
     *
     */
    @Override
    public Boolean esPuedeSubirPropuestaResProvisional(Integer idConvocatoria)  {

        Boolean permiteGrabar = Boolean.TRUE;

        // No permitimos Subir Propuesta si la convocatoria esta en estado en
        // contruccion o aprobada
        Convocatoria convocatoria = this.find(idConvocatoria);

        Integer idEstado = convocatoria.getIdEstado().getIdValor();

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.APROBADA).equals(idEstado) ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES).equals(idEstado) ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES).equals(idEstado)) {
     
            StringBuilder queryHql = new StringBuilder();

            queryHql.append(" SELECT COUNT(DISTINCT ex.idExpediente) FROM Expediente ex,");
            queryHql.append("  Solicitud so, Convocatoria  co ");
            queryHql.append(" where ");
            queryHql.append(" so.idSolicitud = ex.solicitud.idSolicitud");
            queryHql.append(" and so.convocatoria.idConvocatoria = co.idConvocatoria");

            queryHql.append(" and co.fcBajaFila IS NULL");
            queryHql.append(" and so.fcBajaFila IS NULL");
            queryHql.append(" and ex.fcBajaFila IS NULL");
            queryHql.append(" and co.idConvocatoria = :idConvocatoria");
            queryHql.append(" AND ex.convocatoriaAct.idActor IN ( SELECT b.idActor FROM ConvocatoriaAct "
                    + "b WHERE b.idConvocatoria = :idConvocatoria AND b.tipoActor.idValor IN ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE) +") ) ");
            queryHql.append(" and ex.estadoRevEval.idValor not in(");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));
            queryHql.append(" )");
            queryHql.append(" and ex.estado.idValor not in(");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.ACEPTADA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.SELECCIONADA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_SELECCIONADA));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA));
            queryHql.append(" )");


            Map<String, Object> params = new HashMap<>();
            params.put("idConvocatoria", idConvocatoria);

            Object resultado = super.executeUniqueHQLQuery(queryHql.toString(), params);

            Long existeDatosIncorrectos = (Long) resultado;
            if (existeDatosIncorrectos > 0) {
                permiteGrabar = Boolean.FALSE;
            }

        }

        return permiteGrabar;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public ConvocatoriaDTO buscarConvocatoriaByIdConvocatoria(Integer idConvocatoria) {
     
            StringBuilder queryHql = new StringBuilder();
            
            // Construye la consulta HQL
            queryHql.append("SELECT c from Convocatoria as c "); 
            queryHql.append("where c.idConvocatoria = :idConvocatoria ");
            queryHql.append("and c.fcBajaFila IS NULL");

            // Crea un mapa para los parámetros
            Map<String, Object> params = new HashMap<>();
            params.put("idConvocatoria", idConvocatoria);

            ConvocatoriaDTO executeUniqueHQLQuery =  (ConvocatoriaDTO) super.executeUniqueHQLQuery(
                    queryHql.toString(), params);

            return executeUniqueHQLQuery;
        }
 
}
