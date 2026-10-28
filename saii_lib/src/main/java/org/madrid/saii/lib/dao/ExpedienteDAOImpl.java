package org.madrid.saii.lib.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ComisionImportesDTO;
import org.madrid.saii.lib.dto.ComisionSeleccionDTO;
import org.madrid.saii.lib.dto.ResolucionConcesionDTO;
import org.madrid.saii.lib.dto.ResolucionExclusionDesistimientoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAreaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.dto.converters.SolicitudToDTO;
import org.madrid.saii.lib.dto.converters.TipoResolModalidadToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Expediente
 *
 * @author everis
 */
@Repository("expedienteDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteDAOImpl extends BaseDAOImpl<Expediente, Integer> implements ExpedienteDAO {

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;
    
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;
    
//    @PersistenceContext
//    private EntityManager entityManager;
    
    @Autowired
    private SessionFactory sessionFactory;

    /**
     *
     */
    @Override
    public List<ExpedienteFiltrosDTO> filtrar(ExpedienteFiltrosDTO dto) {

        List<ExpedienteFiltrosDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("Select c, e, ca, s, t ");
        queryHql.append(
                " from Expediente as e, Solicitud as s, Convocatoria as c, ConvocatoriaArea as ca, "
                + " TipoResolModalidad as t ");
        queryHql.append(" where e.solicitud.idSolicitud = s.idSolicitud");
        queryHql.append(" and s.convocatoria.idConvocatoria = c.idConvocatoria");
        queryHql.append(" and s.convocatoriaArea.idConvocatoriaArea = ca.idConvocatoriaArea");
        
        queryHql.append(" and e.fcBajaFila is null");
        queryHql.append(" and s.fcBajaFila is null");
        queryHql.append(" and c.fcBajaFila is null");
        queryHql.append(" and ca.fcBajaFila is null");
        queryHql.append(" and e.interesado.cdNumIden = :cdNumIden");
        
        queryHql.append(" and s.convocatoria.idModalidadConvocatoria.idValor=t.idModalidad.idValor ");
       
        // TODO - JSC - Se quita la query de Persona JURIDICA hasta que se haga
        // gestion de entidades
        /*
         * if
         * (Constantes.Identificacion.DS_FISICA.equals(dto.getStrTipoPersona()))
         * { queryHql.append(" and e.interesado.cdNumIden = :cdNumIden"); } else
         * { // Solicitudes empezadas por la entidad
         * queryHql.append(" and (e.idSolicitud in (select s.idSolicitud");
         * queryHql.append(" from Solicitud as s, Interesado as i");
         * queryHql.append(" where s.interesado.idInteresado = i.idInteresado");
         * queryHql.append(" and i.cdNumIden = :cdNumIden)"); // Expedientes
         * donde tenemos un representante jurídico, pero que la // solicitud no
         * es de la empresa (la empresa era participante y se // ha asignado aun
         * representante jurídico) queryHql.append(" or e.idExpediente in");
         * queryHql.append(" (select e.idExpediente");
         * queryHql.append(" from Expediente as e");
         * queryHql.append(" where e.interesado.idInteresado in");
         * queryHql.append(" (select j.interesado.idInteresado");
         * queryHql.append(" from RepresentanteJur as j");
         * queryHql.append(" where j.entidad.idEntidad = ");
         * queryHql.append(" (select e.idEntidad");
         * queryHql.append(" from Entidad as e,");
         * queryHql.append(" Interesado i");
         * queryHql.append(" where i.idInteresado = ");
         * queryHql.append(" e.interesado.idInteresado");
         * queryHql.append(" and i.cdNumIden = :cdNumIden ))))"); }
         */

        if (StringUtils.isNotBlank(dto.getCdAcronimoConvocatoria())) {
            queryHql.append(" and upper(c.cdAcronimo) like upper(:cdAcronimo)");
        }

        if (StringUtils.isNotBlank(dto.getCdExpediente())) {
            queryHql.append(" and upper(e.cdExpediente) like upper(:cdExpediente)");
        }

        if (StringUtils.isNotBlank(dto.getDsTituloConvocatoria())) {
            queryHql.append(" and upper(c.cdTitulo) like upper(:cdTitulo)");
        }

        if (dto.getNmAnoConvocatoria() != null && dto.getNmAnoConvocatoria() != 0) {
            queryHql.append(" and c.nmAno = :nmAno");
        }

        if (dto.getIdAreaCientifica() != null && dto.getIdAreaCientifica() != 0) {
            queryHql.append(" and upper(ca.area.idValor) = upper(:idAreaCientifica)");
        }

        if (dto.getEstadoBeneficiarioDTO() != null) {
            Integer idValor = dto.getEstadoBeneficiarioDTO().getIdValor();
            if (idValor != null && idValor != 0) {
                queryHql.append(" and e.estadoBenef.idValor = :idEstadoBeneficiario");
            }
        }

        if (dto.getTipoInteresadoDTO() != null) {
            Integer idValor = dto.getTipoInteresadoDTO().getIdValor();
            if (idValor != null && idValor != 0) {
                queryHql.append(
                        " and e.convocatoriaAct.tipoActorFisJur.idValor = :idTipoInteresado");
            }
        }
        
        
        /*if (StringUtils.isNotBlank(dto.getNifInteresado())) {
            queryHql.append(" and upper(e.interesado.cdNumIden) = upper(:nifInteresado)");
        }*/
        if (StringUtils.isNotBlank(dto.getDsNifInteresado())) {
            queryHql.append(" and upper(e.interesado.cdNumIden) = upper(:dsNifInteresado)");
        }

        if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
            queryHql.append(" and upper(e.interesado.dsNombre) like upper(:dsNombreInteresado)");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
            queryHql.append(
                    " and upper(e.interesado.dsApellido1) like upper(:dsApellido1Interesado)");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
            queryHql.append(
                    " and upper(e.interesado.dsApellido2) like upper(:dsApellido2Interesado)");
        }
        
       
        if (StringUtils.isNotBlank(dto.getNifRepresentanteAutorizado())  
                && !Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_REPRESENTANTE.equals(dto.getIdPersonaSolicitante())) {
            queryHql.append(
                    " and upper(e.autorizado.cdNumIden) = upper(:nifRepresentanteAutorizado)");
        }
        
        if (dto.getIdPersonaSolicitante() != null && !Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_REPRESENTANTE.equals(dto.getIdPersonaSolicitante() )) {
            queryHql.append(
                    " and e.idPersonaSolicitante.nmConstante =:idPersonaSolicitante");
        }

        queryHql.append(" order by e.idExpediente");

        Map<String, Object> params = new HashMap<>();
        params.put("cdAcronimo", "%" + dto.getCdAcronimoConvocatoria() + "%");
        params.put("cdExpediente", "%" + dto.getCdExpediente() + "%");
        params.put("cdTitulo", "%" + dto.getDsTituloConvocatoria() + "%");
        params.put("nmAno", dto.getNmAnoConvocatoria());
        params.put("idAreaCientifica", dto.getIdAreaCientifica());
        params.put("cdNumIden", dto.getStrCdDocu());
        params.put("idEstadoBeneficiario", dto.getEstadoBeneficiarioDTO().getIdValor());
        params.put("idTipoInteresado", dto.getTipoInteresadoDTO().getIdValor());
        params.put("dsNifInteresado", dto.getDsNifInteresado());
        params.put("dsNombreInteresado", "%" + dto.getDsNombreInteresado() + "%");
        params.put("dsApellido1Interesado", "%" + dto.getDsApellido1Interesado() + "%");
        params.put("dsApellido2Interesado", "%" + dto.getDsApellido2Interesado() + "%");
        params.put("nifRepresentanteAutorizado", dto.getNifRepresentanteAutorizado());
        //params.put("nifInteresado", dto.getNifInteresado());
        params.put("idPersonaSolicitante", dto.getIdPersonaSolicitante());

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                    .apply((Convocatoria) object[0]);
            ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE.apply((Expediente) object[1]);
            ConvocatoriaAreaDTO convocatoriaAreaDTO = ConvocatoriaAreaToDTO.INSTANCE
                    .apply((ConvocatoriaArea) object[2]);

            SolicitudDTO solicitudDTO = SolicitudToDTO.INSTANCE.apply((Solicitud) object[3]);
            TipoResolModalidadDTO tipoResolModalidadDTO= TipoResolModalidadToDTO.INSTANCE.apply((TipoResolModalidad)  object[4]);
            TipoResolModalidad tipoResolModalidad= (TipoResolModalidad) object[4];
            CatalogosValoresDTO catalogosValoresDTO = new CatalogosValoresDTO();
            if (tipoResolModalidad != null && tipoResolModalidad.getIdModalidad().getIdValor() != null) {
                catalogosValoresDTO = CatalogosValoresToDTO.INSTANCE.apply(catalogosValoresDAO.find(tipoResolModalidad.getIdModalidad().getIdValor()));
            }
            tipoResolModalidadDTO.setIdModalidad(catalogosValoresDTO);

            ExpedienteFiltrosDTO tempDTO = new ExpedienteFiltrosDTO();
            tempDTO.setIdConvocatoria(convocatoriaDTO.getIdConvocatoria());
            tempDTO.setCdAcronimoConvocatoria(convocatoriaDTO.getCdAcronimo());
            tempDTO.setIdExpediente(expedienteDTO.getIdExpediente());
            tempDTO.setDsTituloConvocatoria(convocatoriaDTO.getCdTitulo());
            tempDTO.setNmAnoConvocatoria(convocatoriaDTO.getNmAno());
            tempDTO.setEstadoDTO(expedienteDTO.getEstadoDTO());
            tempDTO.setEstadoBeneficiarioDTO(expedienteDTO.getEstadoBeneficiarioDTO());
            tempDTO.setEstadoRevAdminDTO(expedienteDTO.getEstadoRevAdminDTO());
            tempDTO.setEstadoRevRequiDTO(expedienteDTO.getEstadoRevRequiDTO());
            tempDTO.setEstadoRevEvaDTO(expedienteDTO.getEstadoRevEvalDTO());
            tempDTO.setCdExpediente(expedienteDTO.getCdExpediente());
            tempDTO.setDsNifInteresado(expedienteDTO.getInteresadoDTO().getCdNumIden());
            tempDTO.setDsNombreInteresado(expedienteDTO.getInteresadoDTO().getDsNombre());
            tempDTO.setDsApellido1Interesado(expedienteDTO.getInteresadoDTO().getDsApellido1());
            tempDTO.setDsApellido2Interesado(expedienteDTO.getInteresadoDTO().getDsApellido2());
            tempDTO.setTipoInteresadoDTO(
                    expedienteDTO.getConvocatoriaActDTO().getTipoPersonaFisJurDTO());
            tempDTO.setIdAreaCientifica(convocatoriaAreaDTO.getAreaDTO().getIdValor());
            tempDTO.setDsAreaCientifica(convocatoriaAreaDTO.getAreaDTO().getDsValor());
            tempDTO.setEsProvisional(Constantes.EstadoExpedienteBeneficiario.APROBADO_PROVISIONAL
                    .equals(tempDTO.getEstadoBeneficiarioDTO().getNmConstante())
                    || Constantes.EstadoExpedienteBeneficiario.DENEGADO_PROVISIONAL
                            .equals(tempDTO.getEstadoBeneficiarioDTO().getNmConstante()));
            tempDTO.setFcIniAlegaciones(convocatoriaDTO.getFcIniAlegaciones());
            tempDTO.setFcFinAlegaciones(convocatoriaDTO.getFcFinAlegaciones());
            tempDTO.setConvocatoriaDTO(convocatoriaDTO);

            //VMM 20/09/24 MD-614: controlamos si es una convocatoria con alegaciones
            if (convocatoriaDTO.getItAlegacion().equals(Constantes.AfirmacionNegacion.S)
                    && convocatoriaDTO.getFcIniAlegaciones() != null
                    && convocatoriaDTO.getFcFinAlegaciones() != null) {

                tempDTO.setEsPermiteAlegar(tempDTO.getEsProvisional()
                        && this.verificarFechaFinInicioAlegaciones(tempDTO.getFcIniAlegaciones(),
                                tempDTO.getFcFinAlegaciones()));
            }else {
                tempDTO.setEsPermiteAlegar(Boolean.FALSE);
            }

            tempDTO.setEsPermiteAlegarIndividual(Boolean.FALSE);

            tempDTO.setSePuedeDescargarInformeCoor(expedienteDTO.getSePuedeDescargarInformeCoor());
            tempDTO.setFcInicioPresentacion(convocatoriaDTO.getFcIniPresentacion());
            tempDTO.setFcFinPresentacion(convocatoriaDTO.getFcFinPresentacion());
            tempDTO.setItPresFueraPlazo(convocatoriaDTO.getItPresFueraPlazo());
            tempDTO.setEstadoRevContDTO(expedienteDTO.getEstadoRevContratoDTO());
            
            if(expedienteDTO.getTipoPersonaSolicitudDTO() != null) {
                tempDTO.setIdPersonaSolicitante(expedienteDTO.getTipoPersonaSolicitudDTO().getNmConstante());
            }
            
            if(expedienteDTO.getRepresentanteLegal() != null && expedienteDTO.getTipoPersonaSolicitudDTO() !=null 
                    &&  Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_REPRESENTANTE.equals(expedienteDTO.getTipoPersonaSolicitudDTO().getNmConstante())) {
                tempDTO.setNifRepresentanteAutorizado(expedienteDTO.getRepresentanteLegal().getCdNumIden());
            }else if(expedienteDTO.getAutorizado() != null &&
                    expedienteDTO.getTipoPersonaSolicitudDTO() !=null  && Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_AUTORIZADO.equals(expedienteDTO.getTipoPersonaSolicitudDTO().getNmConstante())) {
                tempDTO.setNifRepresentanteAutorizado(expedienteDTO.getAutorizado().getCdNumIden());
            }
            

            if (tempDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)) {

                // Obtenemos un registro de los asociados a la petición de
                // subsanación
                Optional<ExpedienteReqDTO> opExpedienteReq = expedienteDTO.getLstExpedienteReqDTO()
                        .stream()
                        .filter(f -> f.getDocumentoPtfrDTO() != null
                                && f.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null
                                && Constantes.AfirmacionNegacion.N.equals(f.getItCumple()))
                        .findFirst();

                if (opExpedienteReq.isPresent()) {
                    tempDTO.setIdDocumentoPtfr(opExpedienteReq.get().getDocumentoPtfrDTO().getIdDocumentoPtfr());
                }

            }

            tempDTO.setEstadoConvocatoriaDTO(convocatoriaDTO.getIdEstado());
            tempDTO.setEstadoSolicitudDTO(solicitudDTO.getEstadoDTO());
            tempDTO.setTipoResolModalidadConvocatoria(tipoResolModalidadDTO.getCdTipoResolucion());
            tempDTO.setItCumplimentaFormulario(expedienteDTO.getConvocatoriaActDTO().getItCumplimentaFormulario());
            tempDTO.setItEvaluacion(convocatoriaDTO.getItEvaluacion());
            if(convocatoriaDTO.getIdTipoConcesion() != null && convocatoriaDTO.getIdTipoConcesion().getIdValor() != null) {
                tempDTO.setTipoConcesionConvocatoriaDTO(convocatoriaDTO.getIdTipoConcesion());
            }

            tempDTO.setIdTipoSolicitud(convocatoriaDTO.getIdTipoSolicitud());
            tempDTO.setTipoActorDTO(expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO());
            tempDTO.setIdSolicitud(solicitudDTO.getIdSolicitud());
            tempDTO.setIdActor(expedienteDTO.getConvocatoriaActDTO().getIdActor());

            tempDTO.setItAlegacionRevisada(expedienteDTO.getItAlegacionRevisada());
            tempDTO.setItAlegaciones(expedienteDTO.getItAlegaciones());
            lst.add(tempDTO);

        }

        return lst;

    }

    /**
     *
     */
    @Override
    public List<ExpedienteFiltrosDTO> obtenerExpedienteCoorEval(ExpedienteFiltrosDTO dto) {

        List<ExpedienteFiltrosDTO> lst = new ArrayList<>();

        StringBuilder queryHqlDatos = this.obtenerExpedienteCoorEvalHQLRegistros(dto);

        Query<?> query = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(queryHqlDatos.toString());
        if (dto.getIndicePaginacion() != null) {
        query.setFirstResult(dto.getIndicePaginacion());
        }
        if (dto.getRegistroPorPagina() != null) {
        query.setMaxResults(dto.getRegistroPorPagina());
        }

        this.obtenerExpedienteCoorEvalHQLParametros(query, dto);
        
        @SuppressWarnings("unchecked")
        List<Object[]> datos = (List<Object[]>) query.list();

        Long cantidad = 0L;
        Long cantidadJustec = 0L;
        if (!datos.isEmpty()) {

            StringBuilder queryHqlCantidad = this
                    .obtenerExpedienteCoorEvalHQLCantidadRegistros(dto);

            query = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                    .createQuery(queryHqlCantidad.toString());

            this.obtenerExpedienteCoorEvalHQLParametros(query, dto);

             cantidad = (Long) query.uniqueResult();
            
            this. getListExpedienteFiltoDTO( lst,  datos, Boolean.FALSE);
            
           
        }
        
        if (StringUtils.isNotBlank(dto.getItCoorEval()) && dto.getItCoorEval().equalsIgnoreCase("E")) {
            dto.setItCoorEval(null);
            StringBuilder queryHqlDatosJustec = this.obtenerExpedienteEvalJustecHQLRegistros(dto);

            Query<?> queryJustec = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                    .createQuery(queryHqlDatosJustec.toString());

            queryJustec.setFirstResult(dto.getIndicePaginacion());
            queryJustec.setMaxResults(dto.getRegistroPorPagina());

            this.obtenerExpedienteCoorEvalHQLParametros(queryJustec, dto);
            
            @SuppressWarnings("unchecked")
            List<Object[]> datosEvalJustec= (List<Object[]>) queryJustec.list();
            
            if (!datosEvalJustec.isEmpty()) {

                StringBuilder queryHqlCantidadJustec = this
                        .obtenerExpedienteEvalJustecHQLCantidadRegistros(dto);

                query = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                        .createQuery(queryHqlCantidadJustec.toString());

                this.obtenerExpedienteCoorEvalHQLParametros(query, dto);

                cantidadJustec = (Long) query.uniqueResult();
            
            this.getListExpedienteFiltoDTO( lst,  datosEvalJustec,Boolean.TRUE);
            
            }
        
        
        dto.setItCoorEval("E");

     }
      if (!lst.isEmpty()) {
            lst.get(0).setTotalRegistrosPaginacion(cantidad.intValue()+cantidadJustec.intValue());
            
        }
        
        return lst;
    }

    /**
     * Metodo para construir consulta HQL para obtener los expediente de un
     * coordinador / evaluador
     *
     * @param dto
     * @return
     */
    private StringBuilder obtenerExpedienteCoorEvalHQLRegistros(ExpedienteFiltrosDTO dto) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("select distinct ");
        queryHql.append("e.cdExpediente, ");
        queryHql.append("c.cdAcronimo, ");
        queryHql.append("e.convocatoriaAct.tipoActor.idValor, ");
        queryHql.append("e.convocatoriaAct.tipoActor.dsValor, ");
        queryHql.append("e.interesado.cdNumIden, ");
        queryHql.append("e.interesado.dsNombre, ");
        queryHql.append("e.interesado.dsApellido1, ");
        queryHql.append("e.interesado.dsApellido2, ");
        queryHql.append("ev.idExpedienteEval, ");
        queryHql.append("ev.estado.idValor, ");
        queryHql.append("ev.estado.dsValor, ");
        queryHql.append("ev.itCoorEval, ");
        queryHql.append("ev.cdFica, ");
        queryHql.append("e.fcFirma, ");
        queryHql.append("e.idExpediente, ");
        queryHql.append("c.idConvocatoria, ");
        queryHql.append("c.nmCalificacionMax, ");
        queryHql.append("ev.dsComentario, ");
        queryHql.append("ev.nmNotaFinal, ");
        queryHql.append("e.convocatoriaAct.identificacion.idValor, ");
        queryHql.append("e.convocatoriaAct.identificacion.dsValor, ");
        queryHql.append("e.itAlegaciones, ");
        queryHql.append("e.cdFicaValoracionAleg ");
        queryHql.append("from Expediente as e ");
        queryHql.append("inner join Solicitud as s ");
        queryHql.append("on e.solicitud.idSolicitud = s.idSolicitud ");
        queryHql.append("inner join Convocatoria as c ");
        queryHql.append("on s.convocatoria.idConvocatoria = c.idConvocatoria ");
        queryHql.append("inner join ExpedienteEval as ev ");
        queryHql.append("on ev.idExpediente = e.idExpediente ");
        queryHql.append("where e.fcBajaFila is null ");
        queryHql.append("and ev.fcBajaFila is null ");
        queryHql.append("and c.fcBajaFila is null ");
        queryHql.append("and s.fcBajaFila is null ");

        if (StringUtils.isNotBlank(dto.getCdUsuario())) {
            queryHql.append("and ev.persona.cdNumIden = :cdUsuarioLogin ");
        }

        if (Objects.nonNull(dto.getIdAreaCientifica())) {
            queryHql.append("and e.convocatoriaArea.idConvocatoriaArea = :idAreaCientifica ");
        }

        if (Objects.nonNull(dto.getIdConvocatoria()) && dto.getIdConvocatoria() != 0) {
            queryHql.append("and c.idConvocatoria = :idConvocatoria ");
        }

        if (StringUtils.isNotBlank(dto.getItCoorEval())) {
            queryHql.append("and ev.itCoorEval = :itCoorEval ");
        }

        if (Objects.nonNull(dto.getIdExpediente())) {
            queryHql.append("and e.idExpediente = :idExpediente ");
        }

        if (StringUtils.isNotBlank(dto.getCdExpediente())) {
            queryHql.append("and upper(e.cdExpediente) like upper(:cdExpediente) ");
        }

        if (Objects.nonNull(dto.getEstadoCoordinadorEvaluadorDTO())
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != null
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != 0) {
            queryHql.append("and ev.estado.idValor = :idEstadoEvaluador ");
        }

        if (Objects.nonNull(dto.getTipoInteresadoDTO()) && dto.getTipoInteresadoDTO().getIdValor() != null && dto.getTipoInteresadoDTO().getIdValor() != 0) {
            queryHql.append("and e.convocatoriaAct.tipoActor.idValor = :idTipoInteresado ");
        }

        if (StringUtils.isNotBlank(dto.getStrCdDocu())) {
            queryHql.append("and upper(e.interesado.cdNumIden) = upper(:dsNifInteresado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
            queryHql.append("and upper(e.interesado.dsNombre) like upper(:dsNombreInteresado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
            queryHql.append(
                    "and upper(e.interesado.dsApellido1) like upper(:dsApellido1Interesado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
            queryHql.append(
                    "and upper(e.interesado.dsApellido2) like upper(:dsApellido2Interesado) ");
        }

        queryHql.append(" order by e.idExpediente");

        return queryHql;
    }

    /**
     * Metodo para construir consulta HQL para obtener la cantidad de
     * expedientes de un coordinador / evaluador
     *
     * @param dto
     * @return
     */
    private StringBuilder obtenerExpedienteCoorEvalHQLCantidadRegistros(ExpedienteFiltrosDTO dto) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("select count( distinct e.cdExpediente) ");
        queryHql.append("from Expediente as e ");
        queryHql.append("inner join Solicitud as s ");
        queryHql.append("on e.solicitud.idSolicitud = s.idSolicitud ");
        queryHql.append("inner join Convocatoria as c ");
        queryHql.append("on s.convocatoria.idConvocatoria = c.idConvocatoria ");
        queryHql.append("inner join ExpedienteEval as ev ");
        queryHql.append("on ev.idExpediente = e.idExpediente ");
        queryHql.append("where e.fcBajaFila is null ");
        queryHql.append("and ev.fcBajaFila is null ");
        queryHql.append("and c.fcBajaFila is null ");
        queryHql.append("and s.fcBajaFila is null ");

        if (StringUtils.isNotBlank(dto.getCdUsuario())) {
            queryHql.append("and ev.persona.cdNumIden = :cdUsuarioLogin ");
        }

        if (Objects.nonNull(dto.getIdAreaCientifica())) {
            queryHql.append("and e.convocatoriaArea.idConvocatoriaArea = :idAreaCientifica ");
        }

        if (Objects.nonNull(dto.getIdConvocatoria()) && dto.getIdConvocatoria() != 0) {
            queryHql.append("and c.idConvocatoria = :idConvocatoria ");
        }

        if (StringUtils.isNotBlank(dto.getItCoorEval())) {
            queryHql.append("and ev.itCoorEval = :itCoorEval ");
        }

        if (Objects.nonNull(dto.getIdExpediente())) {
            queryHql.append("and e.idExpediente = :idExpediente ");
        }

        if (StringUtils.isNotBlank(dto.getCdExpediente())) {
            queryHql.append("and upper(e.cdExpediente) like upper(:cdExpediente) ");
        }

        if (Objects.nonNull(dto.getEstadoCoordinadorEvaluadorDTO())
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != null
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != 0) {
            queryHql.append("and ev.estado.idValor = :idEstadoEvaluador ");
        }

        if (Objects.nonNull(dto.getTipoInteresadoDTO())
                && dto.getTipoInteresadoDTO().getIdValor() != null
                && dto.getTipoInteresadoDTO().getIdValor() != 0) {
            queryHql.append("and e.convocatoriaAct.tipoActor.idValor = :idTipoInteresado ");
        }

        if (StringUtils.isNotBlank(dto.getStrCdDocu())) {
            queryHql.append("and upper(e.interesado.cdNumIden) = upper(:dsNifInteresado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
            queryHql.append("and upper(e.interesado.dsNombre) like upper(:dsNombreInteresado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
            queryHql.append(
                    "and upper(e.interesado.dsApellido1) like upper(:dsApellido1Interesado) ");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
            queryHql.append(
                    "and upper(e.interesado.dsApellido2) like upper(:dsApellido2Interesado) ");
        }

        queryHql.append(" order by e.idExpediente");

        return queryHql;
    }

    /**
     * Metodo para configurar los parámetros para obtener los expediente de un
     * coordinador / evaluador
     *
     * @param query
     * @param dto
     */
    private void obtenerExpedienteCoorEvalHQLParametros(Query<?> query, ExpedienteFiltrosDTO dto) {
        if (StringUtils.isNotBlank(dto.getCdUsuario())) {
            query.setParameter("cdUsuarioLogin", dto.getCdUsuario());
        }

        if (Objects.nonNull(dto.getIdAreaCientifica())) {
            query.setParameter("idAreaCientifica", dto.getIdAreaCientifica());
        }

        if (Objects.nonNull(dto.getIdConvocatoria()) && dto.getIdConvocatoria() != 0) {
            query.setParameter("idConvocatoria", dto.getIdConvocatoria());
        }

        if (StringUtils.isNotBlank(dto.getItCoorEval())) {
            query.setParameter("itCoorEval", dto.getItCoorEval());
        }

        if (Objects.nonNull(dto.getIdExpediente())) {
            query.setParameter("idExpediente", dto.getIdExpediente());
        }

        if (StringUtils.isNotBlank(dto.getCdExpediente())) {
            query.setParameter("cdExpediente", "%" + dto.getCdExpediente().trim() + "%");
        }

        if (Objects.nonNull(dto.getEstadoCoordinadorEvaluadorDTO())
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != null
                && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != 0) {
            query.setParameter("idEstadoEvaluador", dto.getEstadoCoordinadorEvaluadorDTO().getIdValor());
        }

        if (Objects.nonNull(dto.getTipoInteresadoDTO()) && dto.getTipoInteresadoDTO().getIdValor() != null
                && dto.getTipoInteresadoDTO().getIdValor() != 0) {
            query.setParameter("idTipoInteresado", dto.getTipoInteresadoDTO().getIdValor());
        }

        if (StringUtils.isNotBlank(dto.getStrCdDocu())) {
            query.setParameter("dsNifInteresado", dto.getStrCdDocu().trim());
        }

        if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
            query.setParameter("dsNombreInteresado",
                    "%" + dto.getDsNombreInteresado().trim() + "%");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
            query.setParameter("dsApellido1Interesado",
                    "%" + dto.getDsApellido1Interesado().trim() + "%");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
            query.setParameter("dsApellido2Interesado",
                    "%" + dto.getDsApellido2Interesado().trim() + "%");
        }

    }

    /**
     * se verifica la fecha actual está entre FC_INI_ALEGACIONES y
     * FC_FIN_ALEGACIONES
     *
     * @param fcInicio
     * @param fcFin
     * @return
     */
    private Boolean verificarFechaFinInicioAlegaciones(Date fcInicio, Date fcFin) {

        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAlegacionesInicio = fcInicio.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate fechaAlegacionesFin = fcFin.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        return fechaHoy.compareTo(fechaAlegacionesInicio) >= 0
                && fechaHoy.compareTo(fechaAlegacionesFin) <= 0;

    }

    /**
     *
     */
    @Override
    public List<Expediente> listaExpedientePorConvocatoria(Integer idConvocatoria) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select ex from Expediente ex,");
        queryHql.append(" Solicitud so, Convocatoria  co ");
        queryHql.append(" where ");
        queryHql.append(" so.idSolicitud = ex.solicitud.idSolicitud");
        queryHql.append(" and so.convocatoria.idConvocatoria = co.idConvocatoria");

        queryHql.append(" and co.fcBajaFila IS NULL");
        queryHql.append(" and so.fcBajaFila IS NULL");
        queryHql.append(" and ex.fcBajaFila IS NULL");
        queryHql.append(" and co.idConvocatoria = :idConvocatoria");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        @SuppressWarnings("unchecked")
        List<Expediente> executeHQLQueryList = (List<Expediente>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    /**
     * Consulta ligera que solo trae el idExpediente de los expedientes de una convocatoria
     * cuyo estado esté entre los indicados, para comprobaciones de existencia sin cargar
     * el listado completo de expedientes (evita el N+1 de buscarId por cada expediente).
     */
    @Override
    public List<Integer> listaExpConcedidosPorConvocatoria(Integer idConvocatoria, List<Integer> lstEstados) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" select ex.idExpediente from Expediente ex,");
        queryHql.append(" Solicitud so, Convocatoria co ");
        queryHql.append(" where ");
        queryHql.append(" so.idSolicitud = ex.solicitud.idSolicitud");
        queryHql.append(" and so.convocatoria.idConvocatoria = co.idConvocatoria");

        queryHql.append(" and co.fcBajaFila IS NULL");
        queryHql.append(" and so.fcBajaFila IS NULL");
        queryHql.append(" and ex.fcBajaFila IS NULL");
        queryHql.append(" and co.idConvocatoria = :idConvocatoria");
        queryHql.append(" and ex.estado.idValor in (:lstEstados)");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("lstEstados", lstEstados);

        @SuppressWarnings("unchecked")
        List<Integer> executeHQLQueryList = (List<Integer>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    /**
     * Consulta ligera que solo trae el idExpediente de los expedientes de una convocatoria
     * que tengan alegaciones presentadas (itAlegaciones = 'S'), para comprobaciones de
     * existencia sin cargar el listado completo de expedientes.
     */
    @Override
    public List<Integer> listaExpConAlegacionPorConvocatoria(Integer idConvocatoria) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" select ex.idExpediente from Expediente ex,");
        queryHql.append(" Solicitud so, Convocatoria co ");
        queryHql.append(" where ");
        queryHql.append(" so.idSolicitud = ex.solicitud.idSolicitud");
        queryHql.append(" and so.convocatoria.idConvocatoria = co.idConvocatoria");

        queryHql.append(" and co.fcBajaFila IS NULL");
        queryHql.append(" and so.fcBajaFila IS NULL");
        queryHql.append(" and ex.fcBajaFila IS NULL");
        queryHql.append(" and co.idConvocatoria = :idConvocatoria");
        queryHql.append(" and ex.itAlegaciones = '" + Constantes.AfirmacionNegacion.S + "'");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        @SuppressWarnings("unchecked")
        List<Integer> executeHQLQueryList = (List<Integer>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    /**
     * @
     *
     */
    @Override
    public List<Integer> listaExpedienteConContrato()  {

       StringBuilder queryHql = new StringBuilder();

        queryHql.append("select distinct ex.idExpedientePadre from Expediente ex ");
        queryHql.append(",Solicitud sol, Convocatoria  con , ConvocatoriaAct act ");
        queryHql.append(" where  ");
        queryHql.append(" sol.idSolicitud = ex.solicitud.idSolicitud ");
        queryHql.append(" and sol.convocatoria.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" and act.idConvocatoria = con.idConvocatoria ");
        queryHql.append(" and act.itContrato = 'S' ");
        queryHql.append(" and con.itContratos = 'S' ");
        queryHql.append(" and act.idActor = ex.convocatoriaAct.idActor "); // GIM 21/08/2024 CAMBIO QUERY
        queryHql.append(" and con.idEstado = "+ utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA) +" ");
        queryHql.append(" and sol.estado = "+ utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA) +" "); // GIM 21/08/2024 CAMBIO QUERY
        queryHql.append(" and con.fcIniContrato is not null ");
        queryHql.append(" and con.fcFinContrato is not null ");
        queryHql.append(" and current_date between con.fcIniContrato  and con.fcFinContrato ");
        queryHql.append(" and act.tipoActor in ("); // GIM 21/08/2024 CAMBIO QUERY
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR));
        queryHql.append(") ");
        queryHql.append(" and ex.estadoRevContrato is null ");// GIM 21/08/2024 CAMBIO QUERY
        queryHql.append(" and ex.fcBajaFila is null ");
        queryHql.append(" and sol.fcBajaFila is null ");
        queryHql.append(" and con.fcBajaFila is null ");
        queryHql.append(" and act.fcBajaFila is null");

       Map<String, Object> params = new HashMap<>();
       
       @SuppressWarnings("unchecked")
       List<Integer> executeHQLQueryList = (List<Integer>) super.executeHQLQueryList(
               queryHql.toString(),params  );

       return executeHQLQueryList;
   }

    /**
     * @ 
     *
     */
    @Override
    public Boolean validarExpedienteSolicitanteHijoEstadoFinal(
            ExpedienteDTO expedienteSolicitante)  {

        Boolean habilitarBoton = Boolean.TRUE;

        StringBuilder queryHql = new StringBuilder();

        // validamos que existan expedientes hijos
        queryHql.append(" Select  count(1) from Expediente ex");
        queryHql.append(" where ");
        queryHql.append(" ex.solicitud.idSolicitud = :idSolicitud");
        queryHql.append(" and ex.idExpediente <> :idExpedienteSolicitante");
        queryHql.append(" and ex.fcBajaFila IS NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteSolicitante", expedienteSolicitante.getIdExpediente());
        params.put("idSolicitud", expedienteSolicitante.getIdSolicitud());

        Object resultado = super.executeUniqueHQLQuery(queryHql.toString(), params);

        Long existeExpedientesHijos = (Long) resultado;

        if (existeExpedientesHijos != 0) {

            // validamos los estados finales de los expediente hijos
            queryHql = new StringBuilder();
            queryHql.append(" Select  count(1) from Expediente ex");
            queryHql.append(" where ");
            queryHql.append(" ex.solicitud.idSolicitud = :idSolicitud");
            queryHql.append(" and ex.idExpediente <> :idExpedienteSolicitante");
            queryHql.append(" and ex.fcBajaFila IS NULL");
            queryHql.append(" and  ex.estado.idValor not in(");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            queryHql.append(",");
            queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA));
            queryHql.append(")");

            resultado = super.executeUniqueHQLQuery(queryHql.toString(), params);

            Long existeExpedienteHijoNoFinal = (Long) resultado;

            if (existeExpedienteHijoNoFinal == 0) {
                habilitarBoton = Boolean.FALSE;
            }
        }

        return habilitarBoton;
    }

    /**
     * Método para calcular la cantidad de expedientes pendientes por solicitud
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @
     */
    @Override
    public Integer alertaGestorSolicitud(Integer idSolicitud, Boolean solicitudEnTramitacion)  {

        Integer alertaGestorSolicitud = null;
        
        Integer idEstExpPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION);
        Integer idEstExpPenReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO);
        Integer idEstExpPenReqFirRec = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA);
        
        // estados administrativos que lanzan alerta
        List<Integer> lstEstadosAdministrativo = new ArrayList<>();
        lstEstadosAdministrativo.add(idEstExpPenRev);
        lstEstadosAdministrativo.add(idEstExpPenReq);
        lstEstadosAdministrativo.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
        lstEstadosAdministrativo.add(idEstExpPenReqFirRec);
        
        AtlasFilter filRevAdmin = new AtlasFilter(
                Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_ADMIN, lstEstadosAdministrativo,
                Operador.IN);

        // estados requisitos que lanzan alerta
        List<Integer> lstEstadosRequisito = new ArrayList<>();
        lstEstadosRequisito.add(idEstExpPenRev);
        lstEstadosRequisito.add(idEstExpPenReq);
        lstEstadosRequisito.add(idEstExpPenReqFirRec);
        lstEstadosRequisito.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS));
        
        AtlasFilter filRevRequi = new AtlasFilter(
                Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_REQUI, lstEstadosRequisito,
                Operador.IN);

        // estados evaluacion que lanzan alerta
        List<Integer> lstEstadosEvaluacion = new ArrayList<>();
        lstEstadosEvaluacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR));
        lstEstadosEvaluacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR));
        
        AtlasFilter filRevEval = new AtlasFilter(
                Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_EVAL, lstEstadosEvaluacion,
                Operador.IN);

        // estados expediente que lanzan alerta
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES));

        AtlasFilter filRevExpe = new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                lstEstadoExpediente, Operador.IN);

        // estados expediente que no lanzan alerta
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        //VMM 15/10/25 MD-1433: no incluir los estados rechazados para las alertas (pongo estos a falta de info)
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
                
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
        }
        
        //consultar todos los expedientes activos con las anteriores listas de estados
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(AtlasFilter.or(filRevAdmin, filRevRequi, filRevEval, filRevExpe));

        List<Expediente> expedienteAdmiRequi = this.find(filtros, null);

        List<Integer> listaIdExpediente = new ArrayList<>();
        for (Expediente exp : expedienteAdmiRequi) {
            if (!listaIdExpediente.contains(exp.getIdExpediente())) {
                listaIdExpediente.add(exp.getIdExpediente());
            }
        }

        this.alertaFinanciacion(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);

        // VMM 10/02/25 MD-578: aniadimos alerta de JCT
        this.alertaJCT(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);

        // VMM 10/02/25 MD-582: aniadimos alerta de contratos
        this.alertaContratos(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);
        
        //VMM 10/02/25 MD-112: aniadimos alerta de modificaciones presupuestarias con y sin actas
        this.alertaModifPesupFinanciable(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);
        
        //VMM 10/02/25 MD-112: aniadimos alerta de modificaciones de plazos de anualidades con y sin actas
        this.alertaModifPlazosAnualidades(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);
        
        //VMM 10/02/25 MD-112: aniadimos alerta de otras modificaciones con y sin actas
        alertaGestorSolicitud = this.alertaOtrasModificaciones(listaIdExpediente, idSolicitud, null, solicitudEnTramitacion);

        return alertaGestorSolicitud;
    }

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @ 
     */
    @Override
    @SuppressWarnings("unchecked")
    public Integer alertaJCT(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion)  {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append("inner join ExpedienteJusTec jus on jus.idExpediente = exp.idExpediente ");
        //VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        // estados JCT que lanzan alerta
        queryHql.append("and jus.idEstadoRevAdmin in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_REVISIION_EVALUACION));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and jus.fcBajaFila is null ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("idSolicitud", idSolicitud);
        if (Objects.nonNull(idExpediente)) {
            params.put("idExpediente", idExpediente);
        }

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object object = it.next();
            Integer idExpedienteTemp = Integer.valueOf(object.toString());

            if (Objects.isNull(listaIdExpediente)) {
                listaIdExpediente = new ArrayList<>();
            }
            if (!listaIdExpediente.contains(idExpedienteTemp)) {
                listaIdExpediente.add(idExpedienteTemp);
            }
        }

        if (listaIdExpediente != null) {
            return listaIdExpediente.size();
        } else {
            return 0;
        }
    }
    
    
    /**
    *
    * @param idSolicitud
    * @param listaIdExpediente
    * @ 
    */
   @Override
   @SuppressWarnings("unchecked")
   public Integer alertaJEco(List<Integer> listaIdExpediente, Integer idSolicitud,
           Integer idExpediente, Boolean solicitudEnTramitacion)  {
       
       StringBuilder queryHql = new StringBuilder();
       queryHql.append("select distinct exp.idExpediente ");
       queryHql.append("from Expediente exp ");
       queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
       queryHql.append("inner join ExpedienteJusEco jus on jus.idExpediente = exp.idExpediente ");
       //VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
       queryHql.append("where exp.estado.idValor not in (");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
       //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
       if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
           //quitamos los exps iniciados, propuestos, no aceptados o rechazados
           queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
           queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
           queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
       }
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
       queryHql.append(") ");
       // estados JCT que lanzan alerta
       queryHql.append("and jus.idEstadoRevAdmin in (");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION));
       queryHql.append(") ");
       queryHql.append("and exp.fcBajaFila is null ");
       queryHql.append("and sol.fcBajaFila is null ");
       queryHql.append("and jus.fcBajaFila is null ");
       queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
       if (Objects.nonNull(idExpediente)) {
           queryHql.append("and exp.idExpediente = :idExpediente ");
       }

       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);
       if (Objects.nonNull(idExpediente)) {
           params.put("idExpediente", idExpediente);
       }

       List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
               queryHql.toString(), params);

       Iterator<?> it = executeHQLQueryList.iterator();

       while (it.hasNext()) {
           Object object = it.next();
           Integer idExpedienteTemp = Integer.valueOf(object.toString());

           if (Objects.isNull(listaIdExpediente)) {
               listaIdExpediente = new ArrayList<>();
           }
           if (!listaIdExpediente.contains(idExpedienteTemp)) {
               listaIdExpediente.add(idExpedienteTemp);
           }
       }

       if (listaIdExpediente != null) {
           return listaIdExpediente.size();
       } else {
           return 0;
       }
   }
   

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @ 
     */
    @Override
    @SuppressWarnings("unchecked")
    public Integer alertaContratos(List<Integer> listaIdExpediente, Integer idSolicitud, Integer idExpediente, Boolean solicitudEnTramitacion)  {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(
                "inner join ExpedienteContrato con on con.idExpediente = exp.idExpediente ");
        //VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        // estados contratos que lanzan alerta
        queryHql.append("and con.idEstadoRevAdmin in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and con.fcBajaFila is null ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("idSolicitud", idSolicitud);
        if (Objects.nonNull(idExpediente)) {
            params.put("idExpediente", idExpediente);
        }

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object object = it.next();
            Integer idExpedienteTemp = Integer.valueOf(object.toString());

            if (Objects.isNull(listaIdExpediente)) {
                listaIdExpediente = new ArrayList<>();
            }
            if (!listaIdExpediente.contains(idExpedienteTemp)) {
                listaIdExpediente.add(idExpedienteTemp);
            }
        }

        if (listaIdExpediente != null) {
            return listaIdExpediente.size();
        } else {
            return 0;
        }
    }
    

    
    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @ 
     */
    @Override
    @SuppressWarnings("unchecked")
    public int alertaFinanciacion(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion)  {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(
                "inner join Impreso imp on exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor ");
        queryHql.append("inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso ");
        //VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and blo.fcBajaFila is null ");
        queryHql.append("and blo.idFormato.idValor in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES));
        queryHql.append(") ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("idSolicitud", idSolicitud);
        if (Objects.nonNull(idExpediente)) {
            params.put("idExpediente", idExpediente);
        }

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object object = it.next();
            Integer idExpedienteTemp = Integer.valueOf(object.toString());

            queryHql = new StringBuilder();
            queryHql.append("select ex.idExpediente ");
            queryHql.append("from ExpedientePresup ex ");
            queryHql.append("where ex.idExpediente = :idExpediente ");
            queryHql.append("and ex.fcBajaFila is null ");

            params = new HashMap<>();
            params.put("idExpediente", idExpedienteTemp);

            List<Object[]> resultPres = (List<Object[]>) super.executeHQLQueryList(
                    queryHql.toString(), params);

            //si no encuentra en tablas presupuestarias busca en tabla de actuaciones del expediente
            if (resultPres.isEmpty()) {

                queryHql = new StringBuilder();
                queryHql.append("select ex.idExpediente ");
                queryHql.append("from ExpedienteActua ex ");
                queryHql.append("where ex.idExpediente = :idExpediente ");
                queryHql.append("and ex.fcBajaFila is null ");

                params = new HashMap<>();
                params.put("idExpediente", idExpedienteTemp);

                List<Object[]> resultActua = (List<Object[]>) super.executeHQLQueryList(
                        queryHql.toString(), params);

                if (resultActua.isEmpty()) {

                    if (Objects.isNull(listaIdExpediente)) {
                        listaIdExpediente = new ArrayList<>();
                    }

                    if (!listaIdExpediente.contains(idExpedienteTemp)) {
                        listaIdExpediente.add(idExpedienteTemp);
                    }
                }
            }
        }
        if (listaIdExpediente != null) {
            return listaIdExpediente.size();
        } else {
            return 0;
        }
    }
    
    
    /**
     * VMM 06/10/26 MD-3066: condicion HQL (sobre el alias "mod" de ExpedienteModifPresPlazoOtra) con los
     * estados de modificacion que lanzan alerta en el listado de expedientes. Debe ir en
     * concordancia con el rojo del titulo de la solapa dentro del expediente
     * (ExpedienteModifPresPlazoOtraBean.validarEstilo / buscarPorExpedienteEstadoAbiertas): la
     * modificacion requiere accion del gestor mientras este en
     * <ul>
     * <li>Pendiente, Pendiente revision subsanacion o Pendiente Resolucion Modificaciones
     * (tras Finalizar revision), o</li>
     * <li>Pendiente Envio Cola SIN que su resolucion se haya enviado a la cola de portafirmas (boton
     * amarillo). Tras la firma el batch la deja otra vez en ese estado, pero entonces ya existe el
     * envio en SAII_DOCUMENTO_PTFR y no hay nada que hacer. Un envio rechazado en Portafirmas cuenta
     * como no enviado.</li>
     * </ul>
     *
     * @return fragmento HQL que empieza por "and (" y termina con ") "
     */
    private String construirCondicionEstadosModifAlerta() {
        Map<Integer, Integer> catalogos = utilConstantes.getGlobalCatalogosValores();

        StringBuilder condicion = new StringBuilder();
        condicion.append("and (mod.estado.idValor in (");
        condicion.append(catalogos.get(Constantes.EstadoExpedienteResolucion.PEDIENTE)).append(",");
        condicion.append(catalogos.get(Constantes.EstadoExpedienteResolucion.PENDIENTE_REVISION_SUBSANACION)).append(",");
        condicion.append(catalogos.get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES));
        condicion.append(") ");
        condicion.append("or (mod.estado.idValor = ");
        condicion.append(catalogos.get(Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA));
        condicion.append(" and not exists (select 1 from DocumentoPtfr dp ");
        condicion.append("where dp.idPresupuestoModif = mod.idExpedienteModifPresPlazoOtra ");
        condicion.append("and dp.fcBajaFila is null ");
        condicion.append("and (dp.cdEstadoPtfr is null or dp.cdEstadoPtfr <> '");
        condicion.append(Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO);
        condicion.append("')))) ");
        return condicion.toString();
    }

    /**
    *
    * @param idSolicitud
    * @param listaIdExpediente
    * @
    */
    @Override
    @SuppressWarnings("unchecked")
    public int alertaModifPesupFinanciable(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion)  {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(
                "inner join Impreso imp on exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor ");
        queryHql.append("inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso ");
        // VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and blo.fcBajaFila is null ");
        queryHql.append("and blo.idFormato.idValor in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE) +") ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);
       if (Objects.nonNull(idExpediente)) {
           params.put("idExpediente", idExpediente);
       }

       List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
               queryHql.toString(), params);

       Iterator<?> it = executeHQLQueryList.iterator();

       while (it.hasNext()) {
           Object object = it.next();
           Integer idExpedienteTemp = Integer.valueOf(object.toString());

            queryHql = new StringBuilder();
            queryHql.append("select ex.idExpediente ");
            queryHql.append("from ExpedientePresup ex ");
            queryHql.append("inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ex.idExpediente ");
            queryHql.append("where ex.idExpediente = :idExpediente ");
            queryHql.append("and mod.itModPres = 'S' ");
            // estados modificaciones P/P/O que lanzan alerta
            // VMM 05/10/26 MD-3066: solo PENDIENTE - una vez enviada a la cola de portafirmas (o ya en
            // portafirmas) el gestor no tiene nada mas que hacer en la solapa, no debe salir en rojo.
            // VMM 06/10/26 MD-3066: en concordancia con el rojo de la solapa (ver
            // ExpedienteModifPresPlazoOtraBean.validarEstilo) - ver construirCondicionEstadosModifAlerta.
            queryHql.append(this.construirCondicionEstadosModifAlerta());
            queryHql.append("and ex.fcBajaFila is null ");
            queryHql.append("and mod.fcBajaFila is null ");

           params = new HashMap<>();
           params.put("idExpediente", idExpedienteTemp);

           List<Object[]> resultPresup = (List<Object[]>) super.executeHQLQueryList(
                   queryHql.toString(), params);

           if (resultPresup.isEmpty()) {

               if (Objects.isNull(listaIdExpediente)) {
                   listaIdExpediente = new ArrayList<>();
               }

               if (!listaIdExpediente.contains(idExpedienteTemp)) {
                   listaIdExpediente.add(idExpedienteTemp);
               }
           }
       }
       if (listaIdExpediente != null) {
           return listaIdExpediente.size();
       } else {
           return 0;
       }
   }
    
    
    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @ 
     */
    @Override
    @SuppressWarnings("unchecked")
    public int alertaModifPlazosAnualidades(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion)  {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(
                "inner join Impreso imp on exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor ");
        queryHql.append("inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso ");
        //VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and blo.fcBajaFila is null ");
        queryHql.append("and blo.idFormato.idValor in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL) +") ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);
       if (Objects.nonNull(idExpediente)) {
           params.put("idExpediente", idExpediente);
       }

       List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
               queryHql.toString(), params);

       Iterator<?> it = executeHQLQueryList.iterator();

       while (it.hasNext()) {
           Object object = it.next();
           Integer idExpedienteTemp = Integer.valueOf(object.toString());

            queryHql = new StringBuilder();
            queryHql.append("select ex.idExpediente ");
            queryHql.append("from ExpedienteAnualidad ex ");
            queryHql.append("inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ex.idExpediente ");
            queryHql.append("where ex.idExpediente = :idExpediente ");
            queryHql.append("and mod.itModPlazo = 'S' ");
            // estados modificaciones P/P/O que lanzan alerta
            // VMM 05/10/26 MD-3066: solo PENDIENTE (ver alertaModifPesupFinanciable)
            // VMM 06/10/26 MD-3066: ver construirCondicionEstadosModifAlerta
            queryHql.append(this.construirCondicionEstadosModifAlerta());
            queryHql.append("and ex.fcBajaFila is null ");
            queryHql.append("and mod.fcBajaFila is null ");

           params = new HashMap<>();
           params.put("idExpediente", idExpedienteTemp);

           List<Object[]> resultAnual = (List<Object[]>) super.executeHQLQueryList(
                   queryHql.toString(), params);

           if (resultAnual.isEmpty()) {

               if (Objects.isNull(listaIdExpediente)) {
                   listaIdExpediente = new ArrayList<>();
               }

               if (!listaIdExpediente.contains(idExpedienteTemp)) {
                   listaIdExpediente.add(idExpedienteTemp);
               }
           }
       }
       if (listaIdExpediente != null) {
           return listaIdExpediente.size();
       } else {
           return 0;
       }
   }
    
    
    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @ 
     */
    @Override
    @SuppressWarnings("unchecked")
    public int alertaOtrasModificaciones(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion)  {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select distinct exp.idExpediente ");
        queryHql.append("from Expediente exp ");
        queryHql.append("inner join Solicitud sol on exp.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append(
                "inner join Impreso imp on exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor ");
        queryHql.append("inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso ");
        // VMM 15/10/25 MD-1433: no incluir los estados 222 y 226 para las
        // alertas (añado solo estos a falta de info)
        queryHql.append("where exp.estado.idValor not in (");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA) +",");
            queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA) +",");
        }
        queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        queryHql.append(") ");
        queryHql.append("and exp.fcBajaFila is null ");
        queryHql.append("and sol.fcBajaFila is null ");
        queryHql.append("and blo.fcBajaFila is null ");
        queryHql.append("and blo.idFormato.idValor in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO) +") ");
        queryHql.append("and blo.idTematica.idValor in ("+ utilConstantes.getGlobalCatalogosValores().get(Constantes.TematicaBloque.OTRAS_MODIFICACIONES) +") ");
        queryHql.append("and exp.solicitud.idSolicitud = :idSolicitud ");
        if (Objects.nonNull(idExpediente)) {
            queryHql.append("and exp.idExpediente = :idExpediente ");
        }

       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);
       if (Objects.nonNull(idExpediente)) {
           params.put("idExpediente", idExpediente);
       }

       List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
               queryHql.toString(), params);

       Iterator<?> it = executeHQLQueryList.iterator();

       while (it.hasNext()) {
           Object object = it.next();
           Integer idExpedienteTemp = Integer.valueOf(object.toString());

            queryHql = new StringBuilder();
            queryHql.append("select mod.expediente.idExpediente ");
            queryHql.append("from ExpedienteModifPresPlazoOtra mod ");
            queryHql.append("left join ExpedienteModifOtra ex ");
            queryHql.append(
                    "on mod.idExpedienteModifPresPlazoOtra = ex.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra ");
            queryHql.append("where mod.expediente.idExpediente = :idExpediente ");
            queryHql.append("and mod.itModOtra = 'S' ");
            // estados modificaciones P/P/O que lanzan alerta
            // VMM 05/10/26 MD-3066: solo PENDIENTE (ver alertaModifPesupFinanciable)
            // VMM 06/10/26 MD-3066: ver construirCondicionEstadosModifAlerta
            queryHql.append(this.construirCondicionEstadosModifAlerta());
            queryHql.append("and ex.fcBajaFila is null ");
            queryHql.append("and mod.fcBajaFila is null ");
            //quiza haya que meter condicion aqui de que el texto otras modif no sea vacio...o no porque a lo mejor las han borrado...

           params = new HashMap<>();
           params.put("idExpediente", idExpedienteTemp);

           List<Object[]> resultOtras = (List<Object[]>) super.executeHQLQueryList(
                   queryHql.toString(), params);

           if (resultOtras.isEmpty()) {

               if (Objects.isNull(listaIdExpediente)) {
                   listaIdExpediente = new ArrayList<>();
               }

               if (!listaIdExpediente.contains(idExpedienteTemp)) {
                   listaIdExpediente.add(idExpedienteTemp);
               }
           }
       }
       if (listaIdExpediente != null) {
           return listaIdExpediente.size();
       } else {
           return 0;
       }
   }
    

    /**
     * @ 
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> obtenerMapaDatosExcel(Integer idExpediente)  {
        
        Map<String, Object> mapaColeccionDatos = new HashMap<>();

        mapaColeccionDatos.put("listado", new HashMap<String, String>());
        mapaColeccionDatos.put("tabla", new HashMap<String, String>());
        mapaColeccionDatos.put("presupuesto_fin", new HashMap<String, String>());
        mapaColeccionDatos.put("actuaciones_pre", new HashMap<String, String>());

        // recuperamos los datos presentado de los bloques formulario, listado,
        // tabla, texto, texto fijo
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");
        queryHql.append("bco.componente.idComponente, ");
        queryHql.append("da.dsValor ");
        queryHql.append("FROM ");
        queryHql.append("Dato da ");
        queryHql.append("INNER JOIN BloqueComponente bco ");
        //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
        queryHql.append("ON bco.idBloqueComponente = da.bloqueComponente.idBloqueComponente ");
        queryHql.append("INNER JOIN Bloque bc ");
        queryHql.append("ON bco.bloque.idBloque = bc.idBloque ");
        queryHql.append("INNER JOIN Expediente ex ");
        queryHql.append("ON ex.idExpediente = da.expediente.idExpediente ");
        queryHql.append("WHERE ex.idExpediente = :idExpediente ");
        queryHql.append("AND bc.idFormato.idValor IN (");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO_FIJO));
        queryHql.append(") ");
        queryHql.append("AND da.fcBajaFila IS NULL ");
        queryHql.append("AND bc.fcBajaFila IS NULL ");
        queryHql.append("AND bco.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        Map<String, Object> mapaDatos = new HashMap<>();

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            String key = Objects.nonNull(object[0]) ? String.valueOf(object[0]) : "";
            String value = Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "";

            mapaDatos.put(key, value);
        }

        mapaColeccionDatos.put("comun", mapaDatos);

        // recuperamos los datos presentados de financiacion

        // recuperamos los datos financiables de presupuesto

        // recuperamos los datos financiables de actuaciones
        queryHql = new StringBuilder();
        queryHql.append(
                "SELECT eac.idExpedienteActua, eac.tipoFinanciacion.cdCodigo, eac.dsProveedor, eac.nmImporte, eac.nmAyuda, eac.itMemoria ");
        queryHql.append("FROM ExpedienteActua eac ");
        queryHql.append("WHERE ");
        queryHql.append("eac.fcBajaFila IS NULL ");
        queryHql.append("AND ");
        queryHql.append("eac.idExpediente = :idExpediente ");
        queryHql.append("ORDER BY 1");

        executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(),
                params);

        it = executeHQLQueryList.iterator();

        mapaDatos = new HashMap<>();

        int fila = 1;
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            String valueTitu = Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "";
            mapaDatos.put("TIT_" + fila, valueTitu);

            String valueProv = Objects.nonNull(object[2]) ? String.valueOf(object[2]) : "";
            mapaDatos.put("PROV_" + fila, valueProv);
            String valueImpo = Objects.nonNull(object[3]) ? String.valueOf(object[3]) : "0.0";
            mapaDatos.put("IMPO_" + fila, valueImpo);

            if (fila == 1) {
                String valueAyuda = Objects.nonNull(object[4]) ? String.valueOf(object[4]) : "0.0";
                mapaDatos.put("AYUDA", valueAyuda);
            }

            String valueMemo = Objects.nonNull(object[5]) ? String.valueOf(object[5]) : "No";
            mapaDatos.put("MEMO_" + fila, valueMemo);

            fila++;
        }

        mapaColeccionDatos.put("actuaciones_fin", mapaDatos);

        return mapaColeccionDatos;
    }

    @Override
    public int obtenerCantidadColumnasReservadaActuacion(List<Integer> lstIdExpediente) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select max(count(idExpediente)) ");
        queryHql.append("from ExpedienteActua ");
        queryHql.append("where fcBajaFila is null ");
        queryHql.append("and idExpediente IN (:lstIdExpediente) ");
        queryHql.append("group by idExpediente ");

        Map<String, Object> params = new HashMap<>();
        params.put("lstIdExpediente", lstIdExpediente);

        Object resultado = super.executeUniqueHQLQuery(queryHql.toString(), params);

        return Objects.nonNull(resultado) ? Long.valueOf(resultado.toString()).intValue() : 0;

    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> obtenerMapaDatosComunExcel(List<Integer> lstIdExpediente)  {
        
        Map<String, String> mapaColeccionDatos = new HashMap<>();

        // recuperamos los datos presentado de los bloques formulario, listado,
        // tabla, texto, texto fijo
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");
        queryHql.append("ex.id_expediente, bco.componente.idComponente, da.dsValor ");
        queryHql.append("FROM ");
        queryHql.append("Dato da ");
        queryHql.append("INNER JOIN BloqueComponente bco ");
        //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
        queryHql.append("ON bco.idBloqueComponente = da.bloqueComponente.idBloqueComponente ");
        queryHql.append("INNER JOIN Bloque bc ");
        queryHql.append("ON bco.bloque.idBloque = bc.idBloque ");
        queryHql.append("INNER JOIN Expediente ex ");
        queryHql.append("ON ex.idExpediente = da.expediente.idExpediente ");
        queryHql.append("WHERE ex.idExpediente IN (:lstIdExpediente) ");
        queryHql.append("AND bc.idFormato.idValor IN (");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO) + ",");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO_FIJO));
        queryHql.append(") ");
        queryHql.append("AND da.fcBajaFila IS NULL ");
        queryHql.append("AND bc.fcBajaFila IS NULL ");
        queryHql.append("AND bco.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("lstIdExpediente", lstIdExpediente);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            String expediente = Objects.nonNull(object[0]) ? String.valueOf(object[0]) : "";
            String componente = Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "";
            String valor = Objects.nonNull(object[2]) ? String.valueOf(object[2]) : "";

            mapaColeccionDatos.put(expediente + "_" + componente, valor);
        }

        return mapaColeccionDatos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> obtenerMapaDatosActuacionFinExcel(List<Integer> lstIdExpediente) {

        Map<String, String> mapaColeccionDatos = new HashMap<>();

        // recuperamos los datos financiables de actuaciones
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(
                "SELECT eac.idExpediente, idExpedienteActua, eac.tipoFinanciacion.cdCodigo, ");
        queryHql.append("eac.dsProveedor, eac.nmImporte, eac.nmAyuda, eac.itMemoria ");
        queryHql.append("FROM ExpedienteActua eac ");
        queryHql.append("WHERE ");
        queryHql.append("eac.fcBajaFila IS NULL ");
        queryHql.append("AND ");
        queryHql.append("eac.idExpediente IN (:lstIdExpediente) ");
        queryHql.append("ORDER BY 1,2 ");

        Map<String, Object> params = new HashMap<>();
        params.put("lstIdExpediente", lstIdExpediente);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        int fila = 0;
        int columna = 1;
        String expediente = "";

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            if (!expediente.equals(String.valueOf(object[0]))) {
                expediente = Objects.nonNull(object[0]) ? String.valueOf(object[0]) : "";
                fila++;
            } else {
                fila = 1;
            }

            String valueProv = Objects.nonNull(object[2]) ? String.valueOf(object[2]) : "";
            mapaColeccionDatos.put(expediente + "_PROV_" + columna, valueProv);
            String valueImpo = Objects.nonNull(object[3]) ? String.valueOf(object[3]) : "0.0";
            mapaColeccionDatos.put(expediente + "_IMPO_" + columna, valueImpo);

            if (fila == 1) {
                String valueAyuda = Objects.nonNull(object[4]) ? String.valueOf(object[4]) : "0.0";
                mapaColeccionDatos.put(expediente + "_AYUDA", valueAyuda);
            }

            String valueMemoria = Objects.nonNull(object[5]) ? String.valueOf(object[5]) : "N";
            mapaColeccionDatos.put(expediente + "_MEMO_" + columna, valueMemoria);
        }

        return mapaColeccionDatos;
    }
    
    
    /**
    *
    */
   @Override
   public List<ExpedienteDTO> listExpedientePorExpediente(Integer idExpediente) {

        StringBuilder queryHql = new StringBuilder();

       queryHql.append(" Select idExpediente,");
       queryHql.append(" cdExpediente");
       queryHql.append(" from Expediente");
       queryHql.append(" where ");
       queryHql.append(" idExpediente = :idExpediente");
       queryHql.append(" and fcBajaFila IS NULL");
     

       Map<String, Object> params = new HashMap<>();
       params.put("idExpediente", idExpediente);

      
       List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
               queryHql.toString(), params);
       
       Iterator<?> it = executeHQLQueryList.iterator();

       List<ExpedienteDTO> lstExpedienteDTO = new ArrayList<>();
       while (it.hasNext()) {
           Object[] object = (Object[]) it.next();

           ExpedienteDTO expedienteDTO = new ExpedienteDTO();
           expedienteDTO.setIdExpediente(((Integer) object[0]));
           expedienteDTO.setCdExpediente((String) object[1]);

           lstExpedienteDTO.add(expedienteDTO);
       }

       return lstExpedienteDTO;
   }

   @Override
   public List<ExpedienteContratoDTO> findAllExpContract(Integer idExpediente) {
       StringBuilder queryHql = new StringBuilder();
       
       // Construye la consulta HQL
       queryHql.append("SELECT e from ExpedienteContrato as e "); 
       queryHql.append("where e.idExpediente = :idExpediente ");
       queryHql.append("and e.fcBajaFila IS NULL");


       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idExpediente", idExpediente);

       // Ejecuta la consulta HQL
       @SuppressWarnings("unchecked")
       List<ExpedienteContratoDTO> executeHQLQueryList = (List<ExpedienteContratoDTO>) super.executeHQLQueryList(
               queryHql.toString(), params);

       return executeHQLQueryList;
   }
   
   @Override
   public ExpedienteContrato findExpContratById(Integer idExpediente) {
       StringBuilder queryHql = new StringBuilder();
       
       // Construye la consulta HQL
       queryHql.append("SELECT e from ExpedienteContrato as e "); 
       queryHql.append("where e.idExpediente = :idExpediente ");
       queryHql.append("and e.fcBajaFila IS NULL");


       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idExpediente", idExpediente);

       // Ejecuta la consulta HQL
       return (ExpedienteContrato) super.executeUniqueHQLQuery(
               queryHql.toString(), params);
   }
   
   @Override
   public List<Expediente> listExpedientePorExpedientePadre(Integer idSolicitud, Integer idExpedientePadre)  {

       StringBuilder queryHql = new StringBuilder();

       queryHql.append(" Select e ");
       queryHql.append(" from Expediente as e ");
       queryHql.append(" where ");
       queryHql.append(" e.solicitud.idSolicitud = :idSolicitud ");
       queryHql.append(" and e.fcBajaFila IS NULL ");
       queryHql.append(" and e.idExpedientePadre = :idExpedientePadre ");
       //VMM 26/05/25: metemos el resto de estados que no se deben tener en cuenta como hacemos siempre
       //queryHql.append(" and e.estado.idValor <> :idEstado ");
       queryHql.append(" and e.estado.idValor not in (");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO) +",");
       queryHql.append( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
       queryHql.append(") ");

        Map<String, Object> params = new HashMap<>();
        params.put("idSolicitud", idSolicitud);
        params.put("idExpedientePadre", idExpedientePadre);
        //params.put("idEstado", catalogosValoresService.buscarIdValorPorNmConstante(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));

       @SuppressWarnings("unchecked")
       List<Expediente> executeHQLQueryList = (List<Expediente>) super.executeHQLQueryList(
               queryHql.toString(), params);

       return executeHQLQueryList;
   }
   
   @Override
   public  List<Expediente[]> listExpedientesContratoPorPadre(Integer idExpedientePadre) {
       
       StringBuilder queryHql = new StringBuilder();
       queryHql.append(" SELECT exp");
       queryHql.append(" FROM Expediente exp ");
       queryHql.append(" WHERE ");
       queryHql.append("  exp.convocatoriaAct.itContrato = 'S' ");
       queryHql.append(" AND exp.idExpedientePadre = :idExpedientePadre ");
       queryHql.append(" AND exp.fcBajaFila IS NULL ");

       Map<String, Object> params = new HashMap<>();
       params.put("idExpedientePadre", idExpedientePadre);

       @SuppressWarnings("unchecked")
       List<Expediente[]> result = ( List<Expediente[]>) super.executeHQLQueryList(
               queryHql.toString(), params);

       return result;
   }
   
   private StringBuilder obtenerExpedienteEvalJustecHQLRegistros(ExpedienteFiltrosDTO dto) {

       StringBuilder queryHql = new StringBuilder();
       
       queryHql.append("select distinct  ");
       queryHql.append("ex.cdExpediente, ");
       queryHql.append("c.cdAcronimo, ");
       queryHql.append("ex.convocatoriaAct.tipoActor.idValor, ");
       queryHql.append("ex.convocatoriaAct.tipoActor.dsValor, ");
       queryHql.append("ex.interesado.cdNumIden, ");
       queryHql.append("ex.interesado.dsNombre, ");
       queryHql.append("ex.interesado.dsApellido1, ");
       queryHql.append("ex.interesado.dsApellido2, ");
       queryHql.append(" 'null', ");
       queryHql.append(" 'null', ");
       queryHql.append(" 'null', ");
       queryHql.append(" 'E' as itCoorEval, ");
       queryHql.append("'null', ");
       queryHql.append("ex.fcFirma, ");
       queryHql.append("ex.idExpediente, ");
       queryHql.append("c.idConvocatoria, ");
       queryHql.append("c.nmCalificacionMax, ");
       queryHql.append("'null', ");
       queryHql.append("'null', ");
       queryHql.append("ex.convocatoriaAct.identificacion.idValor, ");
       queryHql.append("ex.convocatoriaAct.identificacion.dsValor, ");
       queryHql.append("ex.itAlegaciones, ");
       queryHql.append("ex.cdFicaValoracionAleg ");
       queryHql.append("from Expediente as ex ");
       queryHql.append("inner join Solicitud as s ");
       queryHql.append("on ex.solicitud.idSolicitud = s.idSolicitud ");
       queryHql.append("inner join Convocatoria as c ");
       queryHql.append("on s.convocatoria.idConvocatoria = c.idConvocatoria ");
       
       queryHql.append(" inner join ExpedienteJusTec as jt ");
       queryHql.append(" on jt.idExpediente  = ex.idExpediente "); 
       
       queryHql.append(" inner join ExpedienteEvalJusTec as evj ");
       queryHql.append(" on evj.expedienteJustec.idExpedienteJusTec  = jt.idExpedienteJusTec ");  
       
       queryHql.append("where ex.fcBajaFila is null ");
       queryHql.append("and  evj.fcBajaFila is null ");
       queryHql.append("and c.fcBajaFila is null ");
       queryHql.append("and s.fcBajaFila is null ");

       if (StringUtils.isNotBlank(dto.getCdUsuario())) {
           queryHql.append("and ( evj.persona.cdNumIden = :cdUsuarioLogin )");
       }

       if (Objects.nonNull(dto.getIdAreaCientifica())) {
           queryHql.append("and s.convocatoriaArea.area.idValor = :idAreaCientifica ");
       }

       if (Objects.nonNull(dto.getIdConvocatoria()) && dto.getIdConvocatoria() != 0) {
           queryHql.append("and c.idConvocatoria = :idConvocatoria ");
       }

       if (Objects.nonNull(dto.getIdExpediente())) {
           queryHql.append("and ex.idExpediente = :idExpediente ");
       }

       if (StringUtils.isNotBlank(dto.getCdExpediente())) {
           queryHql.append("and upper(ex.cdExpediente) like upper(:cdExpediente) ");
       }

       if (Objects.nonNull(dto.getEstadoCoordinadorEvaluadorDTO())
               && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != null
               && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != 0) {
           queryHql.append("and ( evj.estado.idValor = :idEstadoEvaluador ) ");
       }

       if (Objects.nonNull(dto.getTipoInteresadoDTO()) 
               && dto.getTipoInteresadoDTO().getIdValor() != null
               && dto.getTipoInteresadoDTO().getIdValor() != 0) {
           queryHql.append("and ex.convocatoriaAct.tipoActor.idValor = :idTipoInteresado ");
       }

       if (StringUtils.isNotBlank(dto.getStrCdDocu())) {
           queryHql.append("and upper(ex.interesado.cdNumIden) = upper(:dsNifInteresado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
           queryHql.append("and upper(ex.interesado.dsNombre) like upper(:dsNombreInteresado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
           queryHql.append(
                   "and upper(ex.interesado.dsApellido1) like upper(:dsApellido1Interesado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
           queryHql.append(
                   "and upper(ex.interesado.dsApellido2) like upper(:dsApellido2Interesado) ");
       }
         
       queryHql.append("  ORDER BY ex.idExpediente ");

       return queryHql;
   }
   
   
   private StringBuilder obtenerExpedienteEvalJustecHQLCantidadRegistros(
           ExpedienteFiltrosDTO dto) {
       StringBuilder queryHql = new StringBuilder();

       queryHql.append("select count( distinct ex.cdExpediente) ");
       queryHql.append(" from Expediente as ex ");
       queryHql.append("inner join Solicitud as s ");
       queryHql.append("on ex.solicitud.idSolicitud = s.idSolicitud ");
       queryHql.append("inner join Convocatoria as c ");
       queryHql.append("on s.convocatoria.idConvocatoria = c.idConvocatoria ");
       
       queryHql.append(" inner join ExpedienteJusTec as jt ");
       queryHql.append(" on jt.idExpediente  = ex.idExpediente "); 
       
       queryHql.append(" inner join ExpedienteEvalJusTec as evj ");
       queryHql.append(" on evj.expedienteJustec.idExpedienteJusTec  = jt.idExpedienteJusTec ");  
       
       queryHql.append("where ex.fcBajaFila is null ");
       queryHql.append("and  evj.fcBajaFila is null ");
       queryHql.append("and c.fcBajaFila is null ");
       queryHql.append("and s.fcBajaFila is null ");

       if (StringUtils.isNotBlank(dto.getCdUsuario())) {
           queryHql.append("and ( evj.persona.cdNumIden = :cdUsuarioLogin )");
       }

       if (Objects.nonNull(dto.getIdAreaCientifica())) {
           queryHql.append("and s.convocatoriaArea.area.idValor = :idAreaCientifica ");
       }

       if (Objects.nonNull(dto.getIdConvocatoria()) && dto.getIdConvocatoria() != 0) {
           queryHql.append("and c.idConvocatoria = :idConvocatoria ");
       }     

       if (Objects.nonNull(dto.getIdExpediente())) {
           queryHql.append("and ex.idExpediente = :idExpediente ");
       }

       if (StringUtils.isNotBlank(dto.getCdExpediente())) {
           queryHql.append("and upper(ex.cdExpediente) like upper(:cdExpediente) ");
       }

       if (Objects.nonNull(dto.getEstadoCoordinadorEvaluadorDTO())
               && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != null
               && dto.getEstadoCoordinadorEvaluadorDTO().getIdValor() != 0) {
           queryHql.append("and ( evj.estado.idValor = :idEstadoEvaluador ) ");
       }

       if (Objects.nonNull(dto.getTipoInteresadoDTO()) 
               && dto.getTipoInteresadoDTO().getIdValor() != null
               && dto.getTipoInteresadoDTO().getIdValor() != 0) {
           queryHql.append("and ex.convocatoriaAct.tipoActor.idValor = :idTipoInteresado ");
       }

       if (StringUtils.isNotBlank(dto.getStrCdDocu())) {
           queryHql.append("and upper(ex.interesado.cdNumIden) = upper(:dsNifInteresado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsNombreInteresado())) {
           queryHql.append("and upper(ex.interesado.dsNombre) like upper(:dsNombreInteresado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsApellido1Interesado())) {
           queryHql.append(
                   "and upper(ex.interesado.dsApellido1) like upper(:dsApellido1Interesado) ");
       }

       if (StringUtils.isNotBlank(dto.getDsApellido2Interesado())) {
           queryHql.append(
                   "and upper(ex.interesado.dsApellido2) like upper(:dsApellido2Interesado) ");
       }
      
       queryHql.append("  ORDER BY ex.idExpediente ");

       return queryHql;

   }

   private List<ExpedienteFiltrosDTO> getListExpedienteFiltoDTO( List<ExpedienteFiltrosDTO> lst, List<Object[]> datos, Boolean tipoEval) {
       if(lst != null) {

               for (Object[] object : datos) {

                   ExpedienteFiltrosDTO tempDTO = new ExpedienteFiltrosDTO();
                   tempDTO.setCdExpediente((String) object[0]);
                   tempDTO.setCdAcronimoConvocatoria((String) object[1]);
                   tempDTO.setTipoActorDTO(
                           object[2] != null ? CatalogosValoresToDTO.INSTANCE.apply(catalogosValoresDAO.find((Integer) object[2])) : new CatalogosValoresDTO());
                   //tempDTO.setDsTipoActor((String) object[3]);
                   tempDTO.setDsNifInteresado((String) object[4]);
                   tempDTO.setDsNombreInteresado((String) object[5]);
                   tempDTO.setDsApellido1Interesado((String) object[6]);
                   tempDTO.setDsApellido2Interesado(object[7] != null ? (String) object[7] : null);
                   if(Boolean.FALSE.equals(tipoEval)) {
                       tempDTO.setIdExpedienteEval(object[8] != null ? (Integer) object[8] : null);
                       tempDTO.setEstadoCoordinadorEvaluadorDTO(
                               object[9] != null ? CatalogosValoresToDTO.INSTANCE.apply(catalogosValoresDAO.find((Integer) object[9])) : new CatalogosValoresDTO());
                       //tempDTO.setDsEstadoCoordinadorEvaluador((String) object[10]);
                       tempDTO.setCdFicaInformeEvaluacion(object[12] != null ? (String) object[12] : null);


                       tempDTO.setDsComentarioFinal(object[17] != null ? (String) object[17] : "");
                       tempDTO.setNmNotaFinal(object[18] != null ? (Integer) object[18] : null);
                       Date fcPresentacionEreg = (Date) object[13];

                       tempDTO.setFcPresentacionEreg(Utils.dateToString(fcPresentacionEreg,
                               Constantes.Utiles.FORMATO_FECHA_HORA));

                       tempDTO.setItAlegaciones(
                               object[21] != null ? (String) object[21] : Constantes.AfirmacionNegacion.N);
                       tempDTO.setCdFicaValoracionAleg(
                               object[22] != null ? String.valueOf(object[22]) : null);
                   }else {
                       tempDTO.setIdExpedienteEval(object[8] != null ? (Integer) object[8] : null);
                       tempDTO.setEstadoCoordinadorEvaluadorDTO(
                               object[9] != null ? CatalogosValoresToDTO.INSTANCE.apply(catalogosValoresDAO.find((Integer) object[9])) : new CatalogosValoresDTO());
                   }

                   tempDTO.setNmCalificacionMax(object[16] != null ? (Integer) object[16] : 0);
                   tempDTO.setItCoorEval((String) object[11]);

                   tempDTO.setIdExpediente((Integer) object[14]);
                   tempDTO.setIdConvocatoria((Integer) object[15]);
                   tempDTO.setTipoIdentificacionDTO(
                           object[19] != null ? CatalogosValoresToDTO.INSTANCE.apply(catalogosValoresDAO.find((Integer) object[19])) : new CatalogosValoresDTO());
                   //tempDTO.setDsTipoIdentificacion((String) object[20]);

                   lst.add(tempDTO);
               }

       }

       return lst;
   }
   
   @Override
   public ExpedienteDTO buscarIdSolicitud(Integer idSolicitud) {
       StringBuilder queryHql = new StringBuilder();
       
       // Construye la consulta HQL
       queryHql.append("SELECT e from Expediente as e "); 
       queryHql.append("where e.solicitud.idSolicitud = :idSolicitud ");
       queryHql.append("and e.fcBajaFila IS NULL");


       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);

       return (ExpedienteDTO) super.executeUniqueHQLQuery(
               queryHql.toString(), params);
   }
   
   @Override
   public List<Expediente> recuperarExpedientesByIdSolicitud(Integer idSolicitud) {
       StringBuilder queryHql = new StringBuilder();
       
       // Construye la consulta HQL
       queryHql.append("SELECT e "); 
       queryHql.append("from Expediente as e "); 
       queryHql.append("where e.solicitud.idSolicitud = :idSolicitud ");
       queryHql.append("and e.fcBajaFila IS NULL ");

       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idSolicitud", idSolicitud);

       // Ejecuta la consulta HQL
       @SuppressWarnings("unchecked")
       List<Expediente> executeHQLQueryList = (List<Expediente>) super.executeHQLQueryList(
               queryHql.toString(), params);

        return executeHQLQueryList;
    }

    @Override
    public List<ExpedienteDoc> findExpJusTecDocByIdExp(Integer idExpediente)  {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT doc from Expediente as exp ");
        queryHql.append("left join ExpedienteDoc as doc on doc.expediente.idExpediente  = exp.idExpediente ");
        queryHql.append("left join ExpedienteJustecDoc as jtd on jtd.idExpedienteDoc  = doc.idExpedienteDoc ");
        queryHql.append("left join ExpedienteJusTec as jct on jct.idExpedienteJusTec  = jtd.idExpedienteJusTec ");
        queryHql.append("where exp.idExpediente = :idExpediente ");
        queryHql.append("and jct.idEstadoRevAdmin.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION) + " ");
        queryHql.append("and doc.fcBajaFila IS NULL ");
        queryHql.append("and jtd.fcBajaFila IS NULL ");
        queryHql.append("and jct.fcBajaFila IS NULL ");
        queryHql.append("and exp.fcBajaFila IS NULL");

       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idExpediente", idExpediente);

       // Ejecuta la consulta HQL
       @SuppressWarnings("unchecked")
       List<ExpedienteDoc> executeHQLQueryList = (List<ExpedienteDoc>) super.executeHQLQueryList(
               queryHql.toString(), params);

        return executeHQLQueryList;
    }
    
    
    @Override
    public List<ExpedienteDoc> findExpJusEcoDocByIdExp(Integer idExpediente)  {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT doc from Expediente as exp ");
        queryHql.append("left join ExpedienteDoc as doc on doc.expediente.idExpediente  = exp.idExpediente ");
        queryHql.append("left join ExpedienteJusEcoDoc as jed on jed.expedienteDoc.idExpedienteDoc  = doc.idExpedienteDoc ");
        queryHql.append("left join ExpedienteJusEco as je on je.idExpedienteJusEco  = jed.expedienteJusEco.idExpedienteJusEco ");
        queryHql.append("where exp.idExpediente = :idExpediente ");
        queryHql.append("and je.idEstadoRevAdmin.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION) + " ");
        queryHql.append("and doc.fcBajaFila IS NULL ");
        queryHql.append("and jed.fcBajaFila IS NULL ");
        queryHql.append("and je.fcBajaFila IS NULL ");
        queryHql.append("and exp.fcBajaFila IS NULL");

       // Crea un mapa para los parámetros
       Map<String, Object> params = new HashMap<>();
       params.put("idExpediente", idExpediente);

       // Ejecuta la consulta HQL
       @SuppressWarnings("unchecked")
       List<ExpedienteDoc> executeHQLQueryList = (List<ExpedienteDoc>) super.executeHQLQueryList(
               queryHql.toString(), params);

        return executeHQLQueryList;
    }
    

    @Override
    public List<ExpedienteDoc> findExpContratosDocByIdExp(Integer idExpediente)
             {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT doc from Expediente as exp ");
        queryHql.append("left join ExpedienteDoc as doc on doc.expediente.idExpediente  = exp.idExpediente ");
        queryHql.append("left join ExpedienteContratoDoc as ecd on ecd.expedienteDoc.idExpedienteDoc  = doc.idExpedienteDoc ");
        queryHql.append("left join ExpedienteContrato as con on con.idExpedienteContrato  = ecd.expedienteContrato.idExpedienteContrato ");
        queryHql.append("where exp.idExpediente = :idExpediente ");
        queryHql.append("and con.idEstadoRevAdmin.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_SUBSANACION) + " ");
        queryHql.append("and doc.fcBajaFila IS NULL ");
        queryHql.append("and ecd.fcBajaFila IS NULL ");
        queryHql.append("and con.fcBajaFila IS NULL ");
        queryHql.append("and exp.fcBajaFila IS NULL");

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteDoc> executeHQLQueryList = (List<ExpedienteDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    @Override
    public List<ExpedienteDoc> findExpModifDocByIdExp(Integer idExpediente)
             {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("SELECT doc from Expediente as exp ");
        queryHql.append("left join ExpedienteDoc as doc on doc.expediente.idExpediente  = exp.idExpediente ");
        queryHql.append("left join ExpedienteModfPresupPlazosOtrosDoc as emd on emd.expedienteDoc.idExpedienteDoc  = doc.idExpedienteDoc ");
        queryHql.append("left join ExpedienteModifPresPlazoOtra as mod ");
        queryHql.append("on mod.idExpedienteModifPresPlazoOtra  = emd.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra ");
        queryHql.append("where exp.idExpediente = :idExpediente ");
        queryHql.append("and mod.estadoRevAdmin.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REQUIERE_SUBSANACION) + " ");
        queryHql.append("and doc.fcBajaFila IS NULL ");
        queryHql.append("and emd.fcBajaFila IS NULL ");
        queryHql.append("and mod.fcBajaFila IS NULL ");
        queryHql.append("and exp.fcBajaFila IS NULL");

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteDoc> executeHQLQueryList = (List<ExpedienteDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }
   
   @Override
   public List<Expediente> listExpedientesHijoPorExpedientePadre(Integer idExpedientePadre) {

       StringBuilder queryHql = new StringBuilder();

       queryHql.append(" Select e ");
       queryHql.append(" from Expediente as e ");
       queryHql.append(" where ");
       queryHql.append(" e.fcBajaFila IS NULL ");
       queryHql.append(" and e.idExpedientePadre = :idExpedientePadre ");

       Map<String, Object> params = new HashMap<>();
       params.put("idExpedientePadre", idExpedientePadre);

       @SuppressWarnings("unchecked")
       List<Expediente> executeHQLQueryList = (List<Expediente>) super.executeHQLQueryList(
               queryHql.toString(), params);

       return executeHQLQueryList;
   }

    @Override
    public Date obtenerFcPresentacionBySolicitud(Integer idSolicitud, Integer idExpediente) {
        
        StringBuilder queryHql = new StringBuilder();
        
        queryHql.append(" Select e.fcFirma ");
        queryHql.append(" from Expediente as e ");
        queryHql.append(" where ");
        queryHql.append(" e.convocatoriaAct.tipoActor.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE) + " ");
        queryHql.append(" and e.solicitud.idSolicitud = :idSolicitud ");
        //VMM 27/01/26 MD-2216: controlamos caso en el que devuelva más de un registro de expediente la solicitud para el mismo actor
        queryHql.append(" and e.idExpediente = :idExpediente ");
        
        Map<String, Object> params = new HashMap<>();
        params.put("idSolicitud", idSolicitud);
        params.put("idExpediente", idExpediente);
        
        Object resultado = super.executeUniqueHQLQuery(queryHql.toString(), params);
        
        return (Date) resultado;
    }

    public Map<Integer, Date> obtenerFcPresentacionPorLote(List<Integer> idsExpediente) {
        if (idsExpediente == null || idsExpediente.isEmpty()) {
            return new HashMap<>();
        }

        StringBuilder queryHql = new StringBuilder();
        
        // Traemos el idSolicitud y la fcFirma mapeados por el lote de idExpediente
        queryHql.append(" select e.solicitud.idSolicitud, e.fcFirma ");
        queryHql.append(" from Expediente as e ");
        queryHql.append(" where ");
        queryHql.append(" e.convocatoriaAct.tipoActor.idValor = ");
        queryHql.append(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE) + " ");
        queryHql.append(" and e.idExpediente in (:idsExpediente) ");
        
        Map<String, Object> params = new HashMap<>();
        params.put("idsExpediente", idsExpediente);
        
        // Ejecutamos la consulta que devuelve una lista de arrays Object[] { idSolicitud, fcFirma }
        List<Object[]> resultados = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(), params);
        
        // Lo transformamos en un Map<idSolicitud, fcFirma> para asociarlo en memoria fácilmente
        // VMM 07/08/26: Collectors.toMap no admite valores null (usa HashMap.merge internamente), y fcFirma
        // puede ser null si el expediente aun no esta firmado, asi que descartamos esas filas antes
        return resultados.stream()
            .filter(row -> row[1] != null)
            .collect(Collectors.toMap(
                row -> (Integer) row[0],
                row -> (Date) row[1],
                (existente, reemplazo) -> existente // Salvaguarda por si acaso
            ));
    }

    /**
     * Recupera, para un lote de solicitudes, los estados (Estado, Rev.Admin, Rev.Req, Rev.Eva, Rev.Finan)
     * del expediente SOLICITANTE de cada una (el expediente con idExpedientePadre a null). Se usan LEFT
     * JOIN explicitos en vez de navegacion por punto en el SELECT para no perder filas cuando alguno de
     * los estados aun no esta informado (una navegacion e.estado.dsValor generaria un INNER JOIN implicito).
     * y la nota
     *
     * @param idsSolicitud lote de idSolicitud a consultar
     * @return mapa idSolicitud -> Object[]{ dsEstado, dsEstadoRevAdmin, dsEstadoRevRequi, dsEstadoRevEval, dsEstadoRevFinan, nmNota }
     */
    public Map<Integer, Object[]> obtenerEstadosExpedienteSolicitantePorLote(List<Integer> idsSolicitud) {
        if (idsSolicitud == null || idsSolicitud.isEmpty()) {
            return new HashMap<>();
        }

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select e.solicitud.idSolicitud, est.dsValor, estAdmin.dsValor, estReq.dsValor, estEva.dsValor, estFinan.dsValor, e.nmNota ");
        queryHql.append(" from Expediente as e ");
        queryHql.append(" left join e.estado as est ");
        queryHql.append(" left join e.estadoRevAdmin as estAdmin ");
        queryHql.append(" left join e.estadoRevRequi as estReq ");
        queryHql.append(" left join e.estadoRevEval as estEva ");
        queryHql.append(" left join e.estadoRevFinan as estFinan ");
        queryHql.append(" where e.idExpedientePadre is null ");
        queryHql.append(" and e.solicitud.idSolicitud in (:idsSolicitud) ");

        Map<String, Object> params = new HashMap<>();
        params.put("idsSolicitud", idsSolicitud);

        List<Object[]> resultados = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(), params);

        return resultados.stream()
            .collect(Collectors.toMap(
                row -> (Integer) row[0],
                row -> new Object[] { row[1], row[2], row[3], row[4], row[5], row[6] },
                (existente, reemplazo) -> existente // Salvaguarda por si acaso
            ));
    }



    public Map<Integer, Long> contarExpedientesPendientesPorConvocatoria(List<Integer> idsConvocatoria) {
        if (idsConvocatoria == null || idsConvocatoria.isEmpty()) {
            return Collections.emptyMap();
        }

        // Mapa para acumular IDs únicos de expedientes por cada ID de convocatoria
        Map<Integer, Set<Integer>> acumulado = new HashMap<>();

        // Definición de las 7 consultas HQL masivas que replican la lógica del gestor antiguo
        List<String> hqlCriterios = Arrays.asList(
                // =========================================================================
                // CRITERIO 1: Filtro Base Común
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) " +
                "     or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) " +
                "     or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) " +
                "     or exp.estado.idValor in (:estadosExpedienteAlerta))",

                // =========================================================================
                // CRITERIO 2: Alerta JCT (Agotada con el filtro base de revisión)
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "inner join ExpedienteJusTec jus on jus.idExpediente = exp.idExpediente " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null and jus.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and jus.idEstadoRevAdmin in (:estadosJusTecAlerta)",

                // =========================================================================
                // CRITERIO 3: Alerta Contratos
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "inner join ExpedienteContrato con on con.idExpediente = exp.idExpediente " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null and con.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and con.idEstadoRevAdmin in (:estadosContratoAlerta)",

                // =========================================================================
                // CRITERIO 4: Alerta Financiación (Acotado a expedientes en revisión)
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor in (:formatosFinanciacion)) " +
                "and not exists (select 1 from ExpedientePresup ep where ep.idExpediente = exp.idExpediente and ep.fcBajaFila is null) " +
                "and not exists (select 1 from ExpedienteActua ea where ea.idExpediente = exp.idExpediente and ea.fcBajaFila is null)",

                // =========================================================================
                // CRITERIO 5: Alerta Modif Presup Financiable
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoPresupuestoFinanciable) " +
                "and not exists (select 1 from ExpedientePresup ep inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ep.idExpediente " +
                "                where ep.idExpediente = exp.idExpediente and ep.fcBajaFila is null and mod.fcBajaFila is null " +
                "                and mod.itModPres = 'S' and mod.estado.idValor in (:estadosModifPendientes))",

                // =========================================================================
                // CRITERIO 6: Alerta Modif Plazos Anualidades
                // =========================================================================
                "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoPlazosEjecAnual) " +
                "and not exists (select 1 from ExpedienteAnualidad ea inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ea.idExpediente " +
                "                where ea.idExpediente = exp.idExpediente and ea.fcBajaFila is null and mod.fcBajaFila is null " +
                "                and mod.itModPlazo = 'S' and mod.estado.idValor in (:estadosModifPendientes))",

             // =========================================================================
             // CRITERIO 7: Alerta Otras Modificaciones (Corregido idTematica.idValor)
             // =========================================================================
             "select distinct sol.convocatoria.idConvocatoria, exp.idExpediente " +
             "from Expediente exp inner join exp.solicitud sol " +
             "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
             "and exp.estado.idValor not in (:estadosBase) " +
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
             "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
             "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
             "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoTexto and blo.idTematica.idValor = :tematicaOtrasModificaciones) " +
             "and not exists (select 1 from ExpedienteModifPresPlazoOtra mod left join ExpedienteModifOtra ex on mod.idExpedienteModifPresPlazoOtra = ex.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra " +
             "                where mod.expediente.idExpediente = exp.idExpediente and mod.fcBajaFila is null and ex.fcBajaFila is null " +
             "                and mod.itModOtra = 'S' and mod.estado.idValor in (:estadosModifPendientes))"
            );

        Session session = sessionFactory.getCurrentSession();
        int contadorCriterio = 1;

        for (String hql : hqlCriterios) {
            Query<Object[]> query = session.createQuery(hql);
            
            // Parámetro de lote común a todas las queries
            query.setParameterList("idsConvocatoria", idsConvocatoria, IntegerType.INSTANCE);
            
            // Inyección dinámica de todas las constantes de negocio
            asignarParametrosAlertaGestor(query, hql);

            List<Object[]> resultados = query.list();
            System.out.println("CRITERIO DEFINITIVO " + contadorCriterio + " devuelve: " + resultados.size() + " filas de alertas.");
            contadorCriterio++;
            
            // El uso de Set garantiza que si un expediente entra por varias alertas, no se duplica la cuenta
            for (Object[] fila : resultados) {
                Integer idConv = (Integer) fila[0];
                Integer idExp = (Integer) fila[1];
                
                if (!acumulado.containsKey(idConv)) {
                    acumulado.put(idConv, new HashSet<>());
                }
                acumulado.get(idConv).add(idExp);
            }
        }

        // Transformamos los conjuntos de IDs en totales numéricos limpios
        return acumulado.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (long) e.getValue().size()
                ));
    }
    
    /**
     *
     */
    @Override
    public List<ResolucionExclusionDesistimientoDTO> listaResolucionExclusionDesistimiento(
            Integer idConvocatoria, List<Integer> idsEstado, Integer idCatalogoRequisitos, Integer idEstadoPendRevisionAleg, Integer idEstadoExclProvis) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select new org.madrid.saii.lib.dto.ResolucionExclusionDesistimientoDTO(");
        queryHql.append(" ex.idExpediente,  ex.convocatoriaSubmod.tlSubmodalidad, ex.interesado.dsNombre, ex.cdExpediente,");
        queryHql.append(" ex.estado.dsValor, ex.interesado.cdNumIden, req.requisito.dsValor)");
        queryHql.append(" from ExpedienteReq req");
        queryHql.append(" join req.expediente ex");
        queryHql.append(" join ex.solicitud so");
        queryHql.append(" join so.convocatoria co");
        queryHql.append(" join ex.convocatoriaSubmod coSub");
        queryHql.append(" where co.idConvocatoria = :idConvocatoria");
        
        queryHql.append(" and (");
        queryHql.append("      (ex.estado.idValor in (:idsEstado) and ex.estado.idValor != :idEstadoPendRevisionAleg)");
        queryHql.append("      or (ex.estado.idValor = :idEstadoPendRevisionAleg and ex.estadoProvisional.idValor = :idEstadoExclProvis)");
        queryHql.append(" )");        
//        queryHql.append(" and ex.estado.idValor in (:idsEstado)");
        
        queryHql.append(" and req.requisito.catalogo.idCatalogo = :idCatalogoRequisitos ");
        queryHql.append(" and req.itCumple = :itCumple");
        queryHql.append(" and ex.convocatoriaSubmod.idConvocatoriaSubmod = coSub.idConvocatoriaSubmod ");
        queryHql.append(" and co.fcBajaFila is null");
        queryHql.append(" and so.fcBajaFila is null");
        queryHql.append(" and ex.fcBajaFila is null");
        queryHql.append(" and req.fcBajaFila is null");
        queryHql.append(" order by ex.convocatoriaSubmod.tlSubmodalidad, ex.interesado.dsNombre,");
        queryHql.append(" ex.cdExpediente, req.requisito.cdCodigo");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idsEstado", idsEstado);
        params.put("idCatalogoRequisitos", idCatalogoRequisitos);
        params.put("itCumple", Constantes.AfirmacionNegacion.N);
        params.put("idEstadoPendRevisionAleg", idEstadoPendRevisionAleg);
        params.put("idEstadoExclProvis", idEstadoExclProvis);

        @SuppressWarnings("unchecked")
        List<ResolucionExclusionDesistimientoDTO> listado =
                (List<ResolucionExclusionDesistimientoDTO>) super.executeHQLQueryList(
                        queryHql.toString(), params);
        return listado;
    }
    
    
    @Override
    public List<ResolucionExclusionDesistimientoDTO> listaResolucionDesistimiento(
            Integer idConvocatoria, List<Integer> idsEstado, Integer idEstadoPendRevisionAleg, Integer idEstadoExclProvis) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select new org.madrid.saii.lib.dto.ResolucionExclusionDesistimientoDTO(");
        queryHql.append(" ex.idExpediente,  ex.convocatoriaSubmod.tlSubmodalidad, ex.interesado.dsNombre, ex.cdExpediente,");
        queryHql.append(" ex.estado.dsValor, ex.interesado.cdNumIden)");
        queryHql.append(" from Expediente ex");
        queryHql.append(" join ex.solicitud so");
        queryHql.append(" join so.convocatoria co");
        queryHql.append(" join ex.convocatoriaSubmod coSub");
        queryHql.append(" where co.idConvocatoria = :idConvocatoria");
        
//        queryHql.append(" and ex.estado.idValor in (:idsEstado)");
        queryHql.append(" and (");
        queryHql.append("      (ex.estado.idValor in (:idsEstado) and ex.estado.idValor != :idEstadoPendRevisionAleg)");
        queryHql.append("      or (ex.estado.idValor = :idEstadoPendRevisionAleg and ex.estadoProvisional.idValor = :idEstadoExclProvis)");
        queryHql.append(" )");   
        
        queryHql.append(" and ex.convocatoriaSubmod.idConvocatoriaSubmod = coSub.idConvocatoriaSubmod ");
        queryHql.append(" and co.fcBajaFila is null");
        queryHql.append(" and so.fcBajaFila is null");
        queryHql.append(" and ex.fcBajaFila is null");
        queryHql.append(" order by ex.convocatoriaSubmod.tlSubmodalidad, ex.interesado.dsNombre,");
        queryHql.append(" ex.cdExpediente");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idsEstado", idsEstado);
        params.put("idEstadoPendRevisionAleg", idEstadoPendRevisionAleg);
        params.put("idEstadoExclProvis", idEstadoExclProvis);

        @SuppressWarnings("unchecked")
        List<ResolucionExclusionDesistimientoDTO> listado =
                (List<ResolucionExclusionDesistimientoDTO>) super.executeHQLQueryList(
                        queryHql.toString(), params);
        return listado;
    }
    
    
    @Override
    public List<ResolucionConcesionDTO> listaResolucionConcesion(
            Integer idConvocatoria, Integer idEstado) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select new org.madrid.saii.lib.dto.ResolucionConcesionDTO(");
        queryHql.append(" co.idLineaAyuda.dsValor, ex.interesado.dsNombre,");
        queryHql.append(" ex.cdExpediente, ex.interesado.cdNumIden)");
        queryHql.append(" from Expediente ex");
        queryHql.append(" join ex.solicitud so");
        queryHql.append(" join so.convocatoria co");
        queryHql.append(" where co.idConvocatoria = :idConvocatoria");
        queryHql.append(" and ex.estado.idValor = :idEstado");
        queryHql.append(" and co.fcBajaFila is null");
        queryHql.append(" and so.fcBajaFila is null");
        queryHql.append(" and ex.fcBajaFila is null");
        queryHql.append(" order by co.idLineaAyuda.dsValor, ex.interesado.dsNombre,");
        queryHql.append(" ex.cdExpediente, ex.idExpediente");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idEstado", idEstado);

        @SuppressWarnings("unchecked")
        List<ResolucionConcesionDTO> listado =
                (List<ResolucionConcesionDTO>) super.executeHQLQueryList(
                        queryHql.toString(), params);
        return listado;
    }
    
    /**
     * VMM 27/10/26 MD-3094: expedientes de la convocatoria que NO estan en los estados indicados,
     * para el listado y la exportacion de la Comision de seleccion (se excluyen los que ya aparecen
     * en la pestana de Resolucion Exclusion/Desistimiento). La linea es la submodalidad del
     * expediente (SAII_EXPEDIENTE.ID_CONVOCATORIA_SUBMOD); puede venir vacia.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ComisionSeleccionDTO> listaComisionSeleccion(Integer idConvocatoria,
            List<Integer> idsEstadoExcluidos) {
        List<ComisionSeleccionDTO> resultado = new ArrayList<>();
        if (idConvocatoria == null) {
            return resultado;
        }

        // left join en todo lo opcional (submodalidad del expediente, interesado, estado
        // provisional) para no perder expedientes que no lo tengan informado. La linea es la
        // submodalidad elegida al crear el expediente (ID_CONVOCATORIA_SUBMOD), que puede ser nula.
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select ex.idExpediente, so.idSolicitud, ex.cdExpediente, ex.nmNota,");
        queryHql.append(" sm.dsSubmodalidad, it.dsNombre, it.cdNumIden, ep.idValor, ep.dsValor,");
        queryHql.append(" sm.tlSubmodalidad, es.dsValor");
        queryHql.append(" from Expediente ex");
        queryHql.append(" join ex.solicitud so");
        queryHql.append(" join so.convocatoria co");
        queryHql.append(" left join ex.convocatoriaSubmod sm");
        queryHql.append(" left join ex.estado es");
        queryHql.append(" left join ex.interesado it");
        queryHql.append(" left join ex.estadoProvisional ep");
        queryHql.append(" where co.idConvocatoria = :idConvocatoria");
        if (idsEstadoExcluidos != null && !idsEstadoExcluidos.isEmpty()) {
            queryHql.append(" and ex.estado.idValor not in (:idsEstadoExcluidos)");
        }
        queryHql.append(" and co.fcBajaFila is null");
        queryHql.append(" and so.fcBajaFila is null");
        queryHql.append(" and ex.fcBajaFila is null");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        if (idsEstadoExcluidos != null && !idsEstadoExcluidos.isEmpty()) {
            params.put("idsEstadoExcluidos", idsEstadoExcluidos);
        }

        List<Object[]> filas = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(),
                params);

        List<Integer> idsExpediente = new ArrayList<>();
        Map<Integer, Integer> idSolicitudPorExpediente = new HashMap<>();
        for (Object[] fila : filas) {
            ComisionSeleccionDTO dto = new ComisionSeleccionDTO();
            dto.setIdExpediente((Integer) fila[0]);
            idSolicitudPorExpediente.put(dto.getIdExpediente(), (Integer) fila[1]);
            dto.setNumeroExpediente((String) fila[2]);
            dto.setPuntuacion((Integer) fila[3]);
            // "Linea 3 - Pymes innovadoras de mas de 5 anos de antiguedad": nombre + descripcion
            String dsSubmod = (String) fila[4];
            String tlSubmod = (String) fila[9];
            if (dsSubmod != null && tlSubmod != null) {
                dto.setLinea(dsSubmod + " - " + tlSubmod);
            } else {
                dto.setLinea(dsSubmod != null ? dsSubmod : tlSubmod);
            }
            dto.setDsEstado((String) fila[10]);
            dto.setNombreEmpresa((String) fila[5]);
            dto.setNif((String) fila[6]);
            dto.setIdEstadoProvisional((Integer) fila[7]);
            dto.setDsEstadoProvisional((String) fila[8]);
            resultado.add(dto);
            idsExpediente.add(dto.getIdExpediente());
        }

        // Cuantia propuesta = ayuda concedida. nmAyuda se repite en todas las filas de una misma
        // tabla de presupuesto (nmPresup) del expediente, por eso se toma una vez por tabla (max) y
        // se suma entre las tablas del expediente.
        // Fecha de presentacion (para el desempate): la del expediente solicitante de la solicitud.
        Map<Integer, Double> ayudaPorExpediente = new HashMap<>();
        Map<Integer, Double> ayudaSolPorExpediente = new HashMap<>();
        Map<Integer, Date> fcPresentacionPorSolicitud = new HashMap<>();
        for (int desde = 0; desde < idsExpediente.size(); desde += 500) {
            List<Integer> lote = new ArrayList<>(
                    idsExpediente.subList(desde, Math.min(desde + 500, idsExpediente.size())));

            StringBuilder queryAyuda = new StringBuilder();
            queryAyuda.append(" select pr.idExpediente, max(pr.nmAyuda), max(pr.nmAyudaSol)");
            queryAyuda.append(" from ExpedientePresup pr");
            queryAyuda.append(" where pr.idExpediente in (:idsExpediente)");
            queryAyuda.append(" and pr.fcBajaFila is null");
            queryAyuda.append(" group by pr.idExpediente, pr.nmPresup");

            Map<String, Object> paramsAyuda = new HashMap<>();
            paramsAyuda.put("idsExpediente", lote);

            List<Object[]> ayudas = (List<Object[]>) super.executeHQLQueryList(
                    queryAyuda.toString(), paramsAyuda);
            for (Object[] ayuda : ayudas) {
                if (ayuda[1] != null) {
                    ayudaPorExpediente.merge((Integer) ayuda[0], (Double) ayuda[1], Double::sum);
                }
                if (ayuda[2] != null) {
                    ayudaSolPorExpediente.merge((Integer) ayuda[0], (Double) ayuda[2], Double::sum);
                }
            }

            fcPresentacionPorSolicitud.putAll(this.obtenerFcPresentacionPorLote(lote));
        }
        for (ComisionSeleccionDTO dto : resultado) {
            dto.setCuantiaPropuesta(ayudaPorExpediente.get(dto.getIdExpediente()));
            dto.setAyudaSolicitada(ayudaSolPorExpediente.get(dto.getIdExpediente()));
            dto.setFcPresentacion(fcPresentacionPorSolicitud
                    .get(idSolicitudPorExpediente.get(dto.getIdExpediente())));
        }

        // Puntuacion descendente (sin puntuacion al final); a igualdad, la presentacion mas
        // reciente primero (sin fecha al final) y, por ultimo, por idExpediente para que el orden
        // sea siempre el mismo
        resultado.sort((a, b) -> {
            int comparacion = Integer.compare(
                    b.getPuntuacion() == null ? Integer.MIN_VALUE : b.getPuntuacion(),
                    a.getPuntuacion() == null ? Integer.MIN_VALUE : a.getPuntuacion());
            if (comparacion == 0) {
                long fechaA = a.getFcPresentacion() == null ? Long.MIN_VALUE
                        : a.getFcPresentacion().getTime();
                long fechaB = b.getFcPresentacion() == null ? Long.MIN_VALUE
                        : b.getFcPresentacion().getTime();
                comparacion = Long.compare(fechaB, fechaA);
            }
            return comparacion != 0 ? comparacion
                    : Integer.compare(a.getIdExpediente(), b.getIdExpediente());
        });
        return resultado;
    }

    /**
     * VMM 27/10/26 MD-3094: desglose de importes por partida (tipo de financiacion) y anualidad.
     * Misma logica de anualidades que la consulta de MD-3179: con conceptos
     * (IT_CONCEPTO_PRESUP = 'S') la anualidad 0 es la cabecera y la primera real es la 1; sin
     * conceptos empieza en 0 y se suma 1. Solicitado sale de SAII_DATO por la cadena impreso ->
     * bloque (formato presupuesto presentado) -> componente, y lo financiable/concedido de
     * SAII_EXPEDIENTE_PRESUP_MONTO. Los importes de varios conceptos o tablas de una misma partida
     * se suman.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ComisionImportesDTO obtenerImportesComisionSeleccion(List<Integer> idsExpediente) {
        ComisionImportesDTO resultado = new ComisionImportesDTO();
        if (idsExpediente == null || idsExpediente.isEmpty()) {
            return resultado;
        }

        Map<Integer, String> nombresPartida = new HashMap<>();
        java.util.TreeSet<Integer> idsPartida = new java.util.TreeSet<>();

        for (int desde = 0; desde < idsExpediente.size(); desde += 500) {
            // Los ids salen de nuestra propia consulta (enteros), asi que se pueden incrustar en el
            // SQL sin riesgo; el limite de Oracle para un IN es de 1000 elementos
            String ids = idsExpediente
                    .subList(desde, Math.min(desde + 500, idsExpediente.size())).stream()
                    .map(String::valueOf).collect(Collectors.joining(","));

            StringBuilder sqlSolicitado = new StringBuilder();
            sqlSolicitado.append(" SELECT x.ID_EXPEDIENTE, x.ID_TIPO_FINANCIACION, x.ANUALIDAD,");
            sqlSolicitado.append(" SUM(x.IMPORTE) FROM (");
            sqlSolicitado.append(" SELECT e.ID_EXPEDIENTE, bc.ID_TIPO_FINANCIACION,");
            sqlSolicitado.append(" c.IT_CONCEPTO_PRESUP AS CONCEPTOS, d.NM_COLUMNA,");
            sqlSolicitado.append(" CASE WHEN c.IT_CONCEPTO_PRESUP = 'S' THEN d.NM_COLUMNA");
            sqlSolicitado.append(" ELSE d.NM_COLUMNA + 1 END AS ANUALIDAD,");
            sqlSolicitado.append(" CASE WHEN REGEXP_LIKE(TRIM(d.DS_VALOR), '^-?[0-9]+([.,][0-9]+)?$')");
            sqlSolicitado.append(" THEN TO_NUMBER(REPLACE(TRIM(d.DS_VALOR), ',', '.'),");
            sqlSolicitado.append(" '999999999999D999999', 'NLS_NUMERIC_CHARACTERS=''.,''') END AS IMPORTE");
            sqlSolicitado.append(" FROM SAII_EXPEDIENTE e");
            sqlSolicitado.append(" JOIN SAII_SOLICITUD s ON s.ID_SOLICITUD = e.ID_SOLICITUD");
            sqlSolicitado.append(" AND s.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" JOIN SAII_CONVOCATORIA c ON c.ID_CONVOCATORIA = s.ID_CONVOCATORIA");
            sqlSolicitado.append(" AND c.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" LEFT JOIN SAII_CATALOGOS_VALORES tc ON tc.ID_VALOR = c.ID_TIPO_CONCESION");
            sqlSolicitado.append(" AND tc.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" JOIN SAII_IMPRESO imp ON imp.ID_CONVOCATORIA = c.ID_CONVOCATORIA");
            sqlSolicitado.append(" AND imp.ID_ACTOR = e.ID_ACTOR AND imp.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" JOIN SAII_CATALOGOS_VALORES ti ON ti.ID_VALOR = imp.ID_TIPO");
            sqlSolicitado.append(" AND ti.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" AND ti.NM_CONSTANTE = CASE WHEN tc.NM_CONSTANTE = 1763 THEN 31 ELSE 30 END");
            sqlSolicitado.append(" JOIN SAII_BLOQUE b ON b.ID_IMPRESO = imp.ID_IMPRESO AND b.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" JOIN SAII_CATALOGOS_VALORES f ON f.ID_VALOR = b.ID_FORMATO");
            sqlSolicitado.append(" AND f.FC_BAJA_FILA IS NULL AND f.NM_CONSTANTE = 190");
            sqlSolicitado.append(" JOIN SAII_BLOQUE_COMPONENTE bc ON bc.ID_BLOQUE = b.ID_BLOQUE");
            sqlSolicitado.append(" AND bc.FC_BAJA_FILA IS NULL AND bc.ID_TIPO_FINANCIACION IS NOT NULL");
            sqlSolicitado.append(" JOIN SAII_DATO d ON d.ID_BLOQUE_COMPONENTE = bc.ID_BLOQUE_COMPONENTE");
            sqlSolicitado.append(" AND d.ID_EXPEDIENTE = e.ID_EXPEDIENTE AND d.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" AND d.ID_EXPEDIENTE_PRESUP_MONTO IS NULL AND d.ID_EXPEDIENTE_JUSTEC IS NULL");
            sqlSolicitado.append(" AND d.ID_EXPE_MOD_PRE_PLA_OTRA IS NULL AND d.ID_EXPEDIENTE_JUSECO IS NULL");
            sqlSolicitado.append(" WHERE e.ID_EXPEDIENTE IN (").append(ids).append(")");
            sqlSolicitado.append(" AND e.FC_BAJA_FILA IS NULL");
            sqlSolicitado.append(" ) x");
            sqlSolicitado.append(" WHERE NOT (x.CONCEPTOS = 'S' AND x.NM_COLUMNA = 0)");
            sqlSolicitado.append(" GROUP BY x.ID_EXPEDIENTE, x.ID_TIPO_FINANCIACION, x.ANUALIDAD");

            this.acumularImportesComision(
                    (List<Object[]>) super.executeSQLQueryList(sqlSolicitado.toString(),
                            new HashMap<>()),
                    resultado.getSolicitado(), resultado, idsPartida);

            StringBuilder sqlConcedido = new StringBuilder();
            sqlConcedido.append(" SELECT p.ID_EXPEDIENTE, p.ID_TIPO_FINANCIACION,");
            sqlConcedido.append(" CASE WHEN c.IT_CONCEPTO_PRESUP = 'S' THEN m.NM_ANUALIDAD");
            sqlConcedido.append(" ELSE m.NM_ANUALIDAD + 1 END AS ANUALIDAD, SUM(m.NM_IMPORTE)");
            sqlConcedido.append(" FROM SAII_EXPEDIENTE_PRESUP p");
            sqlConcedido.append(" JOIN SAII_EXPEDIENTE_PRESUP_MONTO m");
            sqlConcedido.append(" ON m.ID_EXPEDIENTE_PRESUP = p.ID_EXPEDIENTE_PRESUP AND m.FC_BAJA_FILA IS NULL");
            sqlConcedido.append(" JOIN SAII_EXPEDIENTE e0 ON e0.ID_EXPEDIENTE = p.ID_EXPEDIENTE");
            sqlConcedido.append(" AND e0.FC_BAJA_FILA IS NULL");
            sqlConcedido.append(" JOIN SAII_SOLICITUD s0 ON s0.ID_SOLICITUD = e0.ID_SOLICITUD");
            sqlConcedido.append(" AND s0.FC_BAJA_FILA IS NULL");
            sqlConcedido.append(" JOIN SAII_CONVOCATORIA c ON c.ID_CONVOCATORIA = s0.ID_CONVOCATORIA");
            sqlConcedido.append(" AND c.FC_BAJA_FILA IS NULL");
            sqlConcedido.append(" WHERE p.ID_EXPEDIENTE IN (").append(ids).append(")");
            sqlConcedido.append(" AND p.FC_BAJA_FILA IS NULL AND p.ID_TIPO_FINANCIACION IS NOT NULL");
            sqlConcedido.append(" AND NOT (c.IT_CONCEPTO_PRESUP = 'S' AND m.NM_ANUALIDAD = 0)");
            sqlConcedido.append(" GROUP BY p.ID_EXPEDIENTE, p.ID_TIPO_FINANCIACION,");
            sqlConcedido.append(" CASE WHEN c.IT_CONCEPTO_PRESUP = 'S' THEN m.NM_ANUALIDAD");
            sqlConcedido.append(" ELSE m.NM_ANUALIDAD + 1 END");

            this.acumularImportesComision(
                    (List<Object[]>) super.executeSQLQueryList(sqlConcedido.toString(),
                            new HashMap<>()),
                    resultado.getConcedido(), resultado, idsPartida);
        }

        // Descripcion de las partidas (tipo de financiacion), ordenadas por identificador
        if (!idsPartida.isEmpty()) {
            String ids = idsPartida.stream().map(String::valueOf)
                    .collect(Collectors.joining(","));
            List<Object[]> nombres = (List<Object[]>) super.executeSQLQueryList(
                    " SELECT ID_VALOR, DS_VALOR FROM SAII_CATALOGOS_VALORES WHERE ID_VALOR IN ("
                            + ids + ")",
                    new HashMap<>());
            for (Object[] nombre : nombres) {
                nombresPartida.put(((Number) nombre[0]).intValue(), (String) nombre[1]);
            }
            for (Integer idPartida : idsPartida) {
                resultado.getPartidas().put(idPartida,
                        nombresPartida.getOrDefault(idPartida, "Partida " + idPartida));
            }
        }
        return resultado;
    }

    /**
     * Vuelca en el mapa destino las filas (idExpediente, idPartida, anualidad, importe) de una de
     * las dos consultas del desglose.
     */
    private void acumularImportesComision(List<Object[]> filas,
            Map<Integer, Map<Integer, Map<Integer, Double>>> destino,
            ComisionImportesDTO resultado, Set<Integer> idsPartida) {
        for (Object[] fila : filas) {
            if (fila[1] == null || fila[2] == null || fila[3] == null) {
                continue;
            }
            Integer idExpediente = ((Number) fila[0]).intValue();
            Integer idPartida = ((Number) fila[1]).intValue();
            Integer anualidad = ((Number) fila[2]).intValue();
            Double importe = ((Number) fila[3]).doubleValue();

            idsPartida.add(idPartida);
            resultado.getAnualidades().add(anualidad);
            destino.computeIfAbsent(idExpediente, k -> new HashMap<>())
                    .computeIfAbsent(idPartida, k -> new HashMap<>())
                    .merge(anualidad, importe, Double::sum);
        }
    }

    public Map<Integer, Long> contarExpedientesPendientesPorSolicitud(List<Integer> idsSolicitud) {
        if (idsSolicitud == null || idsSolicitud.isEmpty()) {
            return Collections.emptyMap();
        }

        // Mapa para acumular IDs únicos de expedientes por cada ID de solicitud
        Map<Integer, Set<Integer>> acumulado = new HashMap<>();

        // Definición de las 7 consultas HQL masivas adaptadas a Solicitud
        List<String> hqlCriterios = Arrays.asList(
                // =========================================================================
                // CRITERIO 1: Filtro Base Común
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) " +
                "     or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) " +
                "     or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) " +
                "     or exp.estado.idValor in (:estadosExpedienteAlerta))",

                // =========================================================================
                // CRITERIO 2: Alerta JCT
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "inner join ExpedienteJusTec jus on jus.idExpediente = exp.idExpediente " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null and jus.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and jus.idEstadoRevAdmin in (:estadosJusTecAlerta)",

                // =========================================================================
                // CRITERIO 3: Alerta Contratos
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "inner join ExpedienteContrato con on con.idExpediente = exp.idExpediente " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null and con.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and con.idEstadoRevAdmin in (:estadosContratoAlerta)",

                // =========================================================================
                // CRITERIO 4: Alerta Financiación
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor in (:formatosFinanciacion)) " +
                "and not exists (select 1 from ExpedientePresup ep where ep.idExpediente = exp.idExpediente and ep.fcBajaFila is null) " +
                "and not exists (select 1 from ExpedienteActua ea where ea.idExpediente = exp.idExpediente and ea.fcBajaFila is null)",

                // =========================================================================
                // CRITERIO 5: Alerta Modif Presup Financiable
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoPresupuestoFinanciable) " +
                "and not exists (select 1 from ExpedientePresup ep inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ep.idExpediente " +
                "                where ep.idExpediente = exp.idExpediente and ep.fcBajaFila is null and mod.fcBajaFila is null " +
                "                and mod.itModPres = 'S' and mod.estado.idValor in (:estadosModifPendientes))",

                // =========================================================================
                // CRITERIO 6: Alerta Modif Plazos Anualidades
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoPlazosEjecAnual) " +
                "and not exists (select 1 from ExpedienteAnualidad ea inner join ExpedienteModifPresPlazoOtra mod on mod.expediente.idExpediente = ea.idExpediente " +
                "                where ea.idExpediente = exp.idExpediente and ea.fcBajaFila is null and mod.fcBajaFila is null " +
                "                and mod.itModPlazo = 'S' and mod.estado.idValor in (:estadosModifPendientes))",

                // =========================================================================
                // CRITERIO 7: Alerta Otras Modificaciones
                // =========================================================================
                "select distinct sol.idSolicitud, exp.idExpediente " +
                "from Expediente exp inner join exp.solicitud sol " +
                "where sol.idSolicitud in (:idsSolicitud) " +
                "and exp.fcBajaFila is null and sol.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBase) " +
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and (exp.estadoRevAdmin.idValor in (:estadosRevAdminAlerta) or exp.estadoRevRequi.idValor in (:estadosRevRequiAlerta) or exp.estadoRevEval.idValor in (:estadosRevEvalAlerta) or exp.estado.idValor in (:estadosExpedienteAlerta)) " +
                "and exists (select 1 from Impreso imp inner join Bloque blo on imp.idImpreso = blo.impreso.idImpreso " +
                "            where exp.convocatoriaAct.idActor = imp.convocatoriaAct.idActor " +
                "            and blo.fcBajaFila is null and blo.idFormato.idValor = :formatoTexto and blo.idTematica.idValor = :tematicaOtrasModificaciones) " +
                "and not exists (select 1 from ExpedienteModifPresPlazoOtra mod left join ExpedienteModifOtra ex on mod.idExpedienteModifPresPlazoOtra = ex.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra " +
                "                where mod.expediente.idExpediente = exp.idExpediente and mod.fcBajaFila is null and ex.fcBajaFila is null " +
                "                and mod.itModOtra = 'S' and mod.estado.idValor in (:estadosModifPendientes))"
        );

        Session session = sessionFactory.getCurrentSession();
        int contadorCriterio = 1;

        for (String hql : hqlCriterios) {
            Query<Object[]> query = session.createQuery(hql);
            
            // Se mapea la lista de IDs de solicitudes usando el nuevo nombre de parámetro
            query.setParameterList("idsSolicitud", idsSolicitud, IntegerType.INSTANCE);
            
            // Conserva el método auxiliar para setear el resto de parámetros dinámicos
            asignarParametrosAlertaGestor(query, hql);

            List<Object[]> resultados = query.list();
            System.out.println("CRITERIO DEFINITIVO " + contadorCriterio + " devuelve: " + resultados.size() + " filas de alertas.");
            contadorCriterio++;
            
            // Mapeo en el acumulador utilizando el ID de la Solicitud (fila[0])
            for (Object[] fila : resultados) {
                Integer idSol = (Integer) fila[0];
                Integer idExp = (Integer) fila[1];
                
                if (!acumulado.containsKey(idSol)) {
                    acumulado.put(idSol, new HashSet<>());
                }
                acumulado.get(idSol).add(idExp);
            }
        }

        // Retorna el mapa final transformando el conteo del Set a un tipo Long
        return acumulado.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (long) e.getValue().size()
                ));
    }
    
    

    
    private void asignarParametrosAlertaGestor(org.hibernate.query.Query<?> query, String hql) {
        // Mapeo dinámico usando utilConstantes resolviendo según el entorno
        
        // OJO: Usamos setParameterList especificando IntegerType para evitar que busque entidades
        query.setParameterList("estadosBase", Arrays.asList(
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
        ), IntegerType.INSTANCE);

        query.setParameterList("estadosExtra", Arrays.asList(
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA)
        ), IntegerType.INSTANCE);

        // Valores unitarios siguen con setParameter habitual
        query.setParameter("estadoSolIniciada", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

        // =========================================================================
        // CRITERIO 1: Filtro Base de Revisiones (Extraído exactamente de tu código antiguo)
        // =========================================================================
        if (hql.contains("estadosRevAdminAlerta")) {
            query.setParameterList("estadosRevAdminAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
            ), IntegerType.INSTANCE);
        }

        if (hql.contains("estadosRevRequiAlerta")) {
            query.setParameterList("estadosRevRequiAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)
            ), IntegerType.INSTANCE);
        }

        if (hql.contains("estadosRevEvalAlerta")) {
            query.setParameterList("estadosRevEvalAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR)
            ), IntegerType.INSTANCE);
        }

        if (hql.contains("estadosExpedienteAlerta")) {
            query.setParameterList("estadosExpedienteAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES)
            ), IntegerType.INSTANCE);
        }

        // =========================================================================
        // RESTO DE CRITERIOS: Manteniendo tu lógica y tipos intactos
        // =========================================================================
        if (hql.contains("jus.idEstadoRevAdmin")) {
            query.setParameterList("estadosJusTecAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_REVISIION_EVALUACION)
            ), IntegerType.INSTANCE);
        }       
        
        if (hql.contains("con.idEstadoRevAdmin")) {
            query.setParameterList("estadosContratoAlerta", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION)
            ), IntegerType.INSTANCE);
        }

        if (hql.contains("formatosFinanciacion")) {
            query.setParameterList("formatosFinanciacion", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES)
            ), IntegerType.INSTANCE);
        }
        
        if (hql.contains("formatoPresupuestoFinanciable")) {
            query.setParameter("formatoPresupuestoFinanciable", utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE));
        }
        
        if (hql.contains("formatoPlazosEjecAnual")) {
            query.setParameter("formatoPlazosEjecAnual", utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL));
        }
        
        if (hql.contains("formatoTexto")) {
            query.setParameter("formatoTexto", utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO));
            query.setParameter("tematicaOtrasModificaciones", utilConstantes.getGlobalCatalogosValores().get(Constantes.TematicaBloque.OTRAS_MODIFICACIONES));
        }
        
        if (hql.contains("estadosModifPendientes")) {
            query.setParameterList("estadosModifPendientes", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.ENVIADA_PORTAFIRMAS),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_PORTAFIRMAS)
            ), IntegerType.INSTANCE);
        }  
    }
    
    @Override
    public List<Integer> obtenerEstadosProvisionalesResolucion(Integer idConvocatoria,
            List<Integer> idsEstado) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" select distinct ex.estado.idValor from Expediente ex");
        queryHql.append(" join ex.solicitud so join so.convocatoria co");
        queryHql.append(" where co.idConvocatoria = :idConvocatoria");
        queryHql.append(" and ex.estado.idValor in (:idsEstado)");
        queryHql.append(" and co.fcBajaFila is null");
        queryHql.append(" and so.fcBajaFila is null");
        queryHql.append(" and ex.fcBajaFila is null");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idsEstado", idsEstado);

        @SuppressWarnings("unchecked")
        List<Integer> estados = (List<Integer>) super.executeHQLQueryList(
                queryHql.toString(), params);
        return estados;
    }
  
}
