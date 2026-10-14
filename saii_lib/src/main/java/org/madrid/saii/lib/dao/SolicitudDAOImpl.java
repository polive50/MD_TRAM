package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SolicitudFiltrosDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio Solicitud
 *
 * @author everis
 */
@Repository("solicitudDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SolicitudDAOImpl extends BaseDAOImpl<Solicitud, Integer> implements SolicitudDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Solicitud> filtrar(SolicitudFiltrosDTO solicitudFiltros) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("select distinct s from Solicitud as s,");
        queryHql.append(" Convocatoria as c, Interesado as i, Expediente as e");
        queryHql.append(" where s.convocatoria = c.idConvocatoria");
        queryHql.append(" and s.interesado = i.idInteresado");
        queryHql.append(" and s.idSolicitud = e.solicitud.idSolicitud");
        queryHql.append(" and s.fcBajaFila is null");
        queryHql.append(" and e.fcBajaFila is null");

        queryHql.append(" and s.interesado.cdNumIden = :cdNumIdenSol");

        if (solicitudFiltros.getAcronimo() != null) {
            queryHql.append(" and upper(c.cdAcronimo) like upper(:cdAcronimo)");
        }

        if (solicitudFiltros.getDescripcion() != null) {
            queryHql.append(" and upper(c.dsDescripcion) like upper(:descripcion)");
        }

        if (solicitudFiltros.getIdEstado() != null) {
            queryHql.append(" and s.estado.idValor = :estado");
        }

        if (solicitudFiltros.getIdTipoInteresado() != null
                && solicitudFiltros.getIdTipoInteresado() != 0) {
            queryHql.append(
                    " and e.convocatoriaAct.tipoActorFisJur.idValor = :idTipoInteresado");
        }

        if (StringUtils.isNotBlank(solicitudFiltros.getDsNifInteresado())) {
            queryHql.append(" and upper(e.interesado.cdNumIden) like upper(:dsNifInteresado)");
        }

        if (StringUtils.isNotBlank(solicitudFiltros.getDsNombreInteresado())) {
            queryHql.append(" and upper(e.interesado.dsNombre) like upper(:dsNombreInteresado)");
        }

        if (StringUtils.isNotBlank(solicitudFiltros.getDsApellido1Interesado())) {
            queryHql.append(
                    " and upper(e.interesado.dsApellido1) like upper(:dsApellido1Interesado)");
        }

        if (StringUtils.isNotBlank(solicitudFiltros.getDsApellido2Interesado())) {
            queryHql.append(
                    " and upper(e.interesado.dsApellido2) like upper(:dsApellido2Interesado)");
        }

        queryHql.append(" order by s.idSolicitud");

        Map<String, Object> params = new HashMap<>();
        params.put("cdNumIdenExp", solicitudFiltros.getStrCdDocuExp());
        params.put("cdNumIdenSol", solicitudFiltros.getStrCdDocuSol());
        params.put("cdAcronimo", "%" + solicitudFiltros.getAcronimo() + "%");
        params.put("descripcion", "%" + solicitudFiltros.getDescripcion() + "%");
        params.put("estado", solicitudFiltros.getIdEstado());
        params.put("idTipoInteresado", solicitudFiltros.getIdTipoInteresado());
        params.put("dsNifInteresado", "%" + solicitudFiltros.getDsNifInteresado() + "%");
        params.put("dsNombreInteresado", "%" + solicitudFiltros.getDsNombreInteresado() + "%");
        params.put("dsApellido1Interesado",
                "%" + solicitudFiltros.getDsApellido1Interesado() + "%");
        params.put("dsApellido2Interesado",
                "%" + solicitudFiltros.getDsApellido2Interesado() + "%");

        return (List<Solicitud>) super.executeHQLQueryList(queryHql.toString(), params);
    }

    /**
     * @ 
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Solicitud> buscarSolicitudIniciada(SolicitudDTO solicitudDTO)  {

        StringBuilder queryHql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        boolean tieneSubmod = false;
        
        //VMM 25/11/24 MD-950: comprobamos si la solicitud a de tener en cuenta el submodulo de la convocatoria
        if (solicitudDTO.getConvocatoriaSubmodDTO() != null &&
                solicitudDTO.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() != null) {
            tieneSubmod = true;
        }

        queryHql.append("select s from Solicitud as s,");
        queryHql.append(" Expediente as e, Impreso as i");
        queryHql.append(" where s.idSolicitud = e.solicitud.idSolicitud");
        queryHql.append(" and s.interesado.idInteresado = :idInteresado");
        queryHql.append(" and s.convocatoria.idConvocatoria = :idConvocatoria");
        queryHql.append(" and s.tipoActor.idValor = :idTipoActor");
        queryHql.append(" and s.convocatoriaArea.idConvocatoriaArea = :idConvocatoriaArea");
        queryHql.append(" and e.convocatoriaAct.idActor = i.convocatoriaAct.idActor");
        queryHql.append(" and i.tipo.idValor = :tipoImpreso");
        queryHql.append(" and s.fcBajaFila is null");
        if (tieneSubmod) {
            queryHql.append(" and s.convocatoriaSubmod.idConvocatoriaSubmod = :idConvocatoriaSubmod");
        }

        params.put("idInteresado", solicitudDTO.getInteresadoDTO().getIdInteresado());
        params.put("idConvocatoria", solicitudDTO.getConvocatoriaDTO().getIdConvocatoria());
        params.put("idTipoActor", solicitudDTO.getTipoActorDTO().getIdValor());
        params.put("idConvocatoriaArea",
                solicitudDTO.getConvocatoriaAreaDTO().getIdConvocatoriaArea());     
        params.put("tipoImpreso", utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
        if (tieneSubmod) {
            params.put("idConvocatoriaSubmod",
                    solicitudDTO.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod());
        }

        return (List<Solicitud>) super.executeHQLQueryList(queryHql.toString(), params);
    }

    /**
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Solicitud> buscarPorExpediente(Integer idExpediente) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("select s from Solicitud as s,");
        queryHql.append(" Expediente as e");
        queryHql.append(" where s.idSolicitud = e.solicitud.idSolicitud");
        queryHql.append(" and e.idExpediente= :idExpediente");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        return (List<Solicitud>) super.executeHQLQueryList(queryHql.toString(), params);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Solicitud> buscarSolicitudOrganismoIniciada(SolicitudDTO solicitudDTO)  {

        StringBuilder queryHql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        queryHql.append("select s from Solicitud as s,");
        queryHql.append(" Expediente as e ");
        queryHql.append(" where s.idSolicitud = e.solicitud.idSolicitud");
        queryHql.append(" and s.interesado.idInteresado = :idInteresado");
        queryHql.append(" and s.convocatoria.idConvocatoria = :idConvocatoria");
       // queryHql.append(" and e.estadoBenef.idValor = :idEstado");
        
        params.put("idInteresado", solicitudDTO.getInteresadoDTO().getIdInteresado());
        params.put("idConvocatoria", solicitudDTO.getConvocatoriaDTO().getIdConvocatoria());
       // params.put("idEstado", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.INICIADA));
        return (List<Solicitud>) super.executeHQLQueryList(queryHql.toString(), params);
    }

}
