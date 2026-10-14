package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ExpedienteEvalJusTecDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.dao.PersonaEspecDAO;
import org.madrid.saii.lib.dao.PersonaOrgDAO;
import org.madrid.saii.lib.dao.PersonaTitDAO;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.madrid.saii.lib.dto.PersonaOrgDTO;
import org.madrid.saii.lib.dto.PersonaTitDTO;
import org.madrid.saii.lib.dto.converters.InteresadoToDTO;
import org.madrid.saii.lib.dto.converters.InteresadoToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Interesado
 *
 * @author everis
 */
@Service("interesadoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class InteresadoServiceImpl extends BaseServiceImpl<Interesado, Integer, InteresadoDAO>
        implements InteresadoService {

    /**
     * Servicio CatalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("personaAreaDAO")
    private PersonaAreaDAO personaAreaDAO;

    @Autowired
    @Qualifier("personaEspecDAO")
    private PersonaEspecDAO personaEspecDAO;

    @Autowired
    @Qualifier("personaOrgDAO")
    private PersonaOrgDAO personaOrgDAO;

    @Autowired
    @Qualifier("personaTitDAO")
    private PersonaTitDAO personaTitDAO;

    @Autowired
    @Qualifier("expedienteEvalJusTecDAO")
    private ExpedienteEvalJusTecDAO expedienteEvalJusTecDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param interesadoDAO
     */
    @Autowired
    public InteresadoServiceImpl(@Qualifier("interesadoDAO") InteresadoDAO interesadoDAO) {
        super();
        this.setDao(interesadoDAO);
    }

    /**
     *
     */
    @Override
    public InteresadoDTO buscarId(final Integer idInteresado) throws ServiceException {
        return InteresadoToDTO.INSTANCE.apply(this.find(idInteresado));
    }

    /**
     *
     */
    @Override
    public List<InteresadoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                InteresadoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<InteresadoDTO> buscarPersona(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea)
            throws ServiceException {

        String propertyOrden = StringUtils.EMPTY;
        Boolean ordenAsc = Boolean.FALSE;
        if (Objects.nonNull(orden) && !orden.isEmpty()) {
            propertyOrden = orden.get(0).getProperty();
            ordenAsc = orden.get(0).isAscendente();
        }

        List<Interesado> lista;
        if (StringUtils.isNotBlank(propertyOrden) && propertyOrden.equals("areasCientificas")) {
            lista = this.find(index, pageSize, filtros, null);
        } else {
            lista = this.find(index, pageSize, filtros, orden);
        }

        if (Boolean.FALSE.equals(lista.isEmpty())) {

            for (Interesado interesado : lista) {

                List<AtlasFilter> filtroAux = new ArrayList<>();
                filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO,
                        interesado.getIdInteresado(), Operador.EQUALS));
                filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                interesado.setListaPersonaEspecialidad(this.personaEspecDAO.find(filtroAux, null));
                interesado.setListaPersonaTitulacion(this.personaTitDAO.find(filtroAux, null));
                interesado.setListaPersonaOrganismo(this.personaOrgDAO.find(filtroAux, null));
                List<PersonaArea> areas = this.personaAreaDAO.find(filtroAux, null);

                if (Objects.nonNull(idArea) && !areas.isEmpty()) {
                    List<Integer> lstAreas = areas.stream().map(area -> area.getArea().getIdValor())
                            .collect(Collectors.toList());
                    if (!lstAreas.contains(idArea)) {
                        areas = new ArrayList<>();
                    }
                }

                interesado.setListaPersonaArea(areas);

            }
            if (Objects.nonNull(idArea)) {
                lista.removeIf(interesado -> interesado.getListaPersonaArea().isEmpty());
            }
        }

        List<InteresadoDTO> lstInteresado = new ArrayList<>(
                Lists.transform(lista, InteresadoToDTO.INSTANCE));
        if (StringUtils.isNotBlank(propertyOrden) && propertyOrden.equals("areasCientificas")) {
            Comparator<InteresadoDTO> compareByAreas = (InteresadoDTO i1, InteresadoDTO i2) -> i1
                    .getAreasCientificas().compareTo(i2.getAreasCientificas());
            if (Boolean.TRUE.equals(ordenAsc)) {
                Collections.sort(lstInteresado, compareByAreas);
            } else {
                Collections.sort(lstInteresado, compareByAreas.reversed());
            }

        }

        return lstInteresado;
    }

    /**
     *
     */
    @Override
    public InteresadoDTO obtenerPorIdentificacion(String cdNumIden) throws ServiceException {
        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN, cdNumIden, Operador.EQUALS));
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.obtenerDatosInteresado(filter);
    }

    /**
     *
     */
    @Override
    public InteresadoDTO obtenerPorCdUsuario(String cdUsuario) throws ServiceException {
        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO, cdUsuario, Operador.EQUALS));

        return this.obtenerDatosInteresado(filter);
    }

    /**
     *
     */
    @Override
    public InteresadoDTO obtenerPorIdentificadorCdUsuario(String identificador, String cdUsuario)
            throws ServiceException {
        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN, identificador,
                Operador.EQUALS));
        if (StringUtils.isNotBlank(cdUsuario)) {
            filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO, cdUsuario,
                    Operador.EQUALS));
        }
        return this.obtenerDatosInteresado(filter);
    }

    /**
     * Devuelve un interesado a partir de filtros
     *
     * @param filter
     * @return
     * @throws ServiceException
     */
    private InteresadoDTO obtenerDatosInteresado(List<AtlasFilter> filter) throws ServiceException {

        List<Interesado> listaInteresados = this.find(filter, null);

        Interesado interesado = new Interesado();

        if (Boolean.FALSE.equals(listaInteresados.isEmpty())) {

            interesado = listaInteresados.get(0);

            // mostrar datos academicos
            if (Objects.isNull(interesado.getCdUsuario())
                    && Constantes.AfirmacionNegacion.S.equals(interesado.getItCoorEval())) {
                List<AtlasFilter> filtroAux = new ArrayList<>();
                filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO,
                        interesado.getIdInteresado(), Operador.EQUALS));
                filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                interesado.setListaPersonaArea(this.personaAreaDAO.find(filtroAux, null));
                interesado.setListaPersonaEspecialidad(this.personaEspecDAO.find(filtroAux, null));
                interesado.setListaPersonaTitulacion(this.personaTitDAO.find(filtroAux, null));
                interesado.setListaPersonaOrganismo(this.personaOrgDAO.find(filtroAux, null));
            }
        }

        return InteresadoToDTO.INSTANCE.apply(interesado);
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerTiposIdentificacion() throws ServiceException {
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPOS_IDENTIFICACION));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(InteresadoDTO interesadoDTO) throws ServiceException {
        this.deleteLogico(InteresadoToEntity.INSTANCE.apply(interesadoDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public InteresadoDTO guardarPersona(InteresadoDTO interesadoDTO, String cdUsuario,
            Boolean datosAcademicos) throws ServiceException {

        Interesado interesado = InteresadoToEntity.INSTANCE.apply(interesadoDTO);

        if (StringUtils.isBlank(cdUsuario)) {
            cdUsuario = UserUtils.getUserName().toUpperCase();
        }

        if (interesado.getIdInteresado() == null) {
            interesado.setCdUsuAlta(cdUsuario);
            interesado.setFcAltaFila(new Date());
        } else {
            interesado.setCdUsuModif(cdUsuario);
            interesado.setFcModifFila(new Date());
        }

        interesado = this.insertOrUpdate(interesado);

        if (Boolean.TRUE.equals(datosAcademicos)) {
            this.guardarPersonaArea(interesado, cdUsuario);
            this.guardarPersonaEspecialidad(interesado, cdUsuario);
            this.guardarPersonaTitulacion(interesado, cdUsuario);
            this.guardarPersonaOrganismo(interesado, cdUsuario);

            List<AtlasFilter> filtroAux = new ArrayList<>();
            filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO,
                    interesado.getIdInteresado(), Operador.EQUALS));
            filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            interesado.setListaPersonaArea(this.personaAreaDAO.find(filtroAux, null));
            interesado.setListaPersonaEspecialidad(this.personaEspecDAO.find(filtroAux, null));
            interesado.setListaPersonaTitulacion(this.personaTitDAO.find(filtroAux, null));
            interesado.setListaPersonaOrganismo(this.personaOrgDAO.find(filtroAux, null));
        }

        return InteresadoToDTO.INSTANCE.apply(interesado);

    }

    /**
     * Metodo para guardar la información de titulacion asociada a un
     * coordinador/evaluador
     *
     * @param interesadoDTO
     * @param cdUsuario
     */
    private void guardarPersonaTitulacion(Interesado interesado, String cdUsuario) {
        if(interesado.getListaPersonaTitulacion() != null) {
            for (PersonaTit titulacion : interesado.getListaPersonaTitulacion()) {
                if (titulacion.getIdPersonaTit() == null) {
                    titulacion.setIdInteresado(interesado.getIdInteresado());
                    titulacion.setCdUsuAlta(cdUsuario);
                    titulacion.setFcAltaFila(new Date());
                } else {
                    titulacion.setCdUsuModif(cdUsuario);
                    titulacion.setFcModifFila(new Date());
                }
                this.personaTitDAO.insertOrUpdate(titulacion);
            } 
        }
    }

    /**
     * Metodo para guardar la información de especialidad asociada a un
     * coordinador/evaluador
     *
     * @param interesadoDTO
     * @param cdUsuario
     */
    private void guardarPersonaEspecialidad(Interesado interesado, String cdUsuario) {
        if(interesado.getListaPersonaEspecialidad() != null) {
            for (PersonaEspec especialidad : interesado.getListaPersonaEspecialidad()) {
                if (especialidad.getIdPersonaEspec() == null) {
                    especialidad.setIdInteresado(interesado.getIdInteresado());
                    especialidad.setCdUsuAlta(cdUsuario);
                    especialidad.setFcAltaFila(new Date());
                } else {
                    especialidad.setCdUsuModif(cdUsuario);
                    especialidad.setFcModifFila(new Date());
                }
                this.personaEspecDAO.insertOrUpdate(especialidad);
            }
        }
    }

    /**
     * Metodo para guardar la información de area asociada a un
     * coordinador/evaluador
     *
     * @param interesadoDTO
     * @param cdUsuario
     */
    private void guardarPersonaArea(Interesado interesado, String cdUsuario) {
        for (PersonaArea area : interesado.getListaPersonaArea()) {
            if (area.getIdPersonaArea() == null) {
                area.setIdInteresado(interesado.getIdInteresado());
                area.setCdUsuAlta(cdUsuario);
                area.setFcAltaFila(new Date());
            } else {
                area.setCdUsuModif(cdUsuario);
                area.setFcModifFila(new Date());
            }
            this.personaAreaDAO.insertOrUpdate(area);
        }
    }

    /**
     * Metodo para guardar la información de organismo asociada a un
     * coordinador/evaluador
     *
     * @param interesadoDTO
     * @param cdUsuario
     */
    private void guardarPersonaOrganismo(Interesado interesado, String cdUsuario) {
        for (PersonaOrg organismo : interesado.getListaPersonaOrganismo()) {
            if (organismo.getIdPersonaOrg() == null) {
                organismo.setIdInteresado(interesado.getIdInteresado());
                organismo.setCdUsuAlta(cdUsuario);
                organismo.setFcAltaFila(new Date());
            } else {
                organismo.setCdUsuModif(cdUsuario);
                organismo.setFcModifFila(new Date());
            }
            this.personaOrgDAO.insertOrUpdate(organismo);
        }
    }

    /**
     *
     */
    @Override
    public Boolean verificarInteresadoCoorEval(String cdNumIden) throws ServiceException {
        Boolean verificacion = Boolean.TRUE;

        log.info("en metodo verificarInteresadoCoorEval");

        log.info("filtrando con cdNumIden: " + cdNumIden);

        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN, cdNumIden, Operador.EQUALS));

        List<InteresadoDTO> listDTO = this.buscar(0, -1, filter, null);

        log.info("total registros: " + listDTO.size());

        if (listDTO.isEmpty() || listDTO.size() > 1) {
            verificacion = Boolean.FALSE;
        }

        log.info("resultado: " + verificacion);

        return verificacion;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public InteresadoDTO darBajaPersonaCoor(InteresadoDTO interesadoDTO) throws ServiceException {

        if (interesadoDTO.getListaPersonaAreaDTO() != null) {
            for (PersonaAreaDTO areaDTO : interesadoDTO.getListaPersonaAreaDTO()) {
                this.personaAreaDAO.deleteLogico(areaDTO.getIdPersonaArea());
            }
        }

        if (interesadoDTO.getListaPersonaEspecialidadDTO() != null) {
            for (PersonaEspecDTO especialidadDTO : interesadoDTO.getListaPersonaEspecialidadDTO()) {
                this.personaEspecDAO.deleteLogico(especialidadDTO.getIdPersonaEspec());
            }
        }

        if (interesadoDTO.getListaPersonaOrganismoDTO() != null) {
            for (PersonaOrgDTO organismoDTO : interesadoDTO.getListaPersonaOrganismoDTO()) {
                this.personaOrgDAO.deleteLogico(organismoDTO.getIdPersonaOrg());
            }
        }

        if (interesadoDTO.getListaPersonaTitulacionDTO() != null) {
            for (PersonaTitDTO titulacionDTO : interesadoDTO.getListaPersonaTitulacionDTO()) {
                this.personaTitDAO.deleteLogico(titulacionDTO.getIdPersonaTit());
            }
        }

        interesadoDTO.setItCoorEval(Constantes.AfirmacionNegacion.N);
        Interesado interesado = this
                .insertOrUpdate(InteresadoToEntity.INSTANCE.apply(interesadoDTO));

        return InteresadoToDTO.INSTANCE.apply(this.find(interesado.getIdInteresado()));

    }

    /**
     *
     */
    @Override
    public List<InteresadoDTO> obtenerEvaluadores(String identificadorCoordinador, String cdUsuario)
            throws ServiceException {

        if (StringUtils.isBlank(identificadorCoordinador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        return this.getDao().obtenerEvaluador(identificadorCoordinador, cdUsuario);
    }

    /**
     *
     */
    @Override
    public List<InteresadoDTO> obtenerEvaluadoresxCoordinador(String identificadorCoordinador,
            Integer idConvocatoria, String cdCodigo) throws ServiceException {
        if (StringUtils.isBlank(identificadorCoordinador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return this.getDao().obtenerEvaluadoresxCoordinador(identificadorCoordinador,
                idConvocatoria, cdCodigo);
    }

    /**
    *
    */
    @Override
    public List<InteresadoDTO> retornarPaginado(List<InteresadoDTO> listaInteresadosFiltrada,
            int index, int pageSize) {

        List<InteresadoDTO> listaInteresadosPaginada = new ArrayList<>();

        if (!listaInteresadosFiltrada.isEmpty()) {

            if (pageSize != -1) {

                int lastIndex = 0;

                if (listaInteresadosFiltrada.size() <= 10) {
                    index = 0;
                    lastIndex = listaInteresadosFiltrada.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaInteresadosFiltrada.size()
                            ? listaInteresadosFiltrada.size()
                            : lastIndex;
                }

                for (int i = index; i < lastIndex; i++) {
                    listaInteresadosPaginada.add(listaInteresadosFiltrada.get(i));
                }
            } else {
                listaInteresadosPaginada.addAll(listaInteresadosFiltrada);
            }
        }

        return listaInteresadosPaginada;

    }

    /**
     * Buscar paginadamente Evaluadores de la justificacion cientifico técnica.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<InteresadoDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<InteresadoDTO> obtenerEvaluadoresJusTec(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden, int idExpediente) throws ServiceException {
        List<InteresadoDTO> lstEvaluadores=this.buscar(index, pageSize, filtros, orden);
        
        List<AtlasFilter> filtroAux = new ArrayList<>();
        filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
        filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<Interesado> coordinadores=this.find(filtroAux,null);
        final List<Integer> coordinadoresFinal=coordinadores.stream().map(x->x.getIdInteresado()).collect(Collectors.toList());
        
        filtroAux = new ArrayList<>();
        filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        List<ExpedienteEvalJusTec> listaExpJustTec=expedienteEvalJusTecDAO.find(filtroAux,null);
        final List<Integer> listaExpJustTecFinal=listaExpJustTec.stream().map(x->x.getPersona().getIdInteresado()).collect(Collectors.toList());
        lstEvaluadores=lstEvaluadores.stream()
                .filter(x->!coordinadoresFinal.contains(x.getIdInteresado()))
                .filter(x->!listaExpJustTecFinal.contains(x.getIdInteresado()))
                .collect(Collectors.toList());
        return lstEvaluadores;
    }
    
    /**
    *
    */
   @Override
   public List<Interesado> obtenerInteresadosByCdNumIdem(String cdNumIden)
           throws ServiceException {

       if (StringUtils.isBlank(cdNumIden)) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
       }

       return this.getDao().obtenerInteresadosByCdNumIdem(cdNumIden);
   }

    @Override
    public List<CatalogosValoresDTO> obtenerMedioNotificaciones() throws ServiceException {
        return this.catalogosValoresService.buscarPorCatalogoOrderNmConstante(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.MEDIO_NOTIFICACION));
    }

    @Override
    public List<CatalogosValoresDTO> obtenerPersonaSolicitante() throws ServiceException {
        return this.catalogosValoresService.buscarPorCatalogoOrderNmConstante(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.PERSONA_SOLICITANTE));
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTiposActor() throws ServiceException {
       return  this.catalogosValoresService.buscarPorCatalogoByNmConstante(Constantes.Cata.IDENTIFICACION);
        
    }
    
    @Override
    public List<InteresadoDTO> obtenerInteresados(String cdNumIden) throws ServiceException {
        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN, cdNumIden, Operador.EQUALS));
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        return new ArrayList<>(Lists.transform(this.find(filter, null),
                InteresadoToDTO.INSTANCE));
    }
    
    @Override
    public List<InteresadoDTO> buscarPersonaNormalizado(String nombre, String apellido1,
            String apellido2, String cdNumIden, String cdUsuario, Integer idArea)
            throws ServiceException {

        List<Interesado> lista = this.getDao().buscarPersonaNormalizado(
                nombre, apellido1, apellido2, cdNumIden, cdUsuario, idArea);

        for (Interesado interesado : lista) {
            List<AtlasFilter> filtroAux = new ArrayList<>();
            filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO,
                    interesado.getIdInteresado(), Operador.EQUALS));
            filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            interesado.setListaPersonaEspecialidad(this.personaEspecDAO.find(filtroAux, null));
            interesado.setListaPersonaTitulacion(this.personaTitDAO.find(filtroAux, null));
            interesado.setListaPersonaOrganismo(this.personaOrgDAO.find(filtroAux, null));
            List<PersonaArea> areas = this.personaAreaDAO.find(filtroAux, null);
            if (Objects.nonNull(idArea) && !areas.isEmpty()) {
                List<Integer> lstAreas = areas.stream()
                        .map(area -> area.getArea().getIdValor()).collect(Collectors.toList());
                if (!lstAreas.contains(idArea)) areas = new ArrayList<>();
            }
            interesado.setListaPersonaArea(areas);
        }
        if (Objects.nonNull(idArea)) {
            lista.removeIf(i -> i.getListaPersonaArea().isEmpty());
        }

        return new ArrayList<>(Lists.transform(lista, InteresadoToDTO.INSTANCE));
    }

}
