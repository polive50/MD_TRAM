package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.InformeEvalDocDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalToEntity;
import org.madrid.saii.lib.dto.converters.InformeEvalDocToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Constantes.TipoDocumento;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
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
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ExpedienteEval
 *
 * @author nttdata
 */
@Slf4j
@Service("expedienteEvalService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteEvalServiceImpl
        extends BaseServiceImpl<ExpedienteEval, Integer, ExpedienteEvalDAO>
        implements ExpedienteEvalService {

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /**
     * Servicio de CatalogoValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * DAO de ExpedienteEvalDAO
     */
    @Autowired
    @Qualifier("expedienteEvalDAO")
    @Setter
    private ExpedienteEvalDAO expedienteEvalDAO;

    /**
     * Service de ExpedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    @Setter
    private ExpedienteService expedienteService;

    /**
     * DAO de ExpedienteDAO
     */
    @Autowired
    @Qualifier("expedienteDAO")
    @Setter
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    /**
     * DAO de InteresadoDAO
     */
    @Autowired
    @Qualifier("interesadoDAO")
    private InteresadoDAO interesadoDAO;

    /**
     * DAO de InteresadoDAO
     */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;
    
    
    @Autowired
    @Qualifier("informeEvalDocDAO")
    private InformeEvalDocDAO informeEvalDocDAO;
    
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("expedienteDocEvalService")
    private ExpedienteDocEvalService expedienteDocEvalService;

    @Autowired
    @Qualifier("informeEvalDocService")
    private InformeEvalDocService informeEvalDocService;
    

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteEvalDAO
     */
    @Autowired
    public ExpedienteEvalServiceImpl(@Qualifier("expedienteEvalDAO")
    ExpedienteEvalDAO expedienteEvalDAO) {
        super();
        this.setDao(expedienteEvalDAO);
    }

    /**
     *
     */
    @Override
    public List<ExpedienteEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteEvalDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteEvalToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteEvalDTO> obtenerCoordinadorExpediente(Integer idExpediente,
            String itCoorEval) throws ServiceException {

        if (Objects.isNull(idExpediente) || StringUtils.isBlank(itCoorEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO altaCoordinadorExpediente(ExpedienteEvalDTO expEvalCoorDTO)
            throws ServiceException {

        if (Objects.isNull(expEvalCoorDTO.getIdExpediente())
                || Objects.isNull(expEvalCoorDTO.getPersonaDTO().getIdInteresado())
                || Objects.isNull(expEvalCoorDTO.getEstadoDTO().getIdValor())
                || Objects.isNull(expEvalCoorDTO.getItCoorEval())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                expEvalCoorDTO.getIdExpediente(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO,
                expEvalCoorDTO.getPersonaDTO().getIdInteresado(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int existeDato = this.count(filtros);

        if (existeDato > 0) {
            throw new ServiceException(Constantes.Error.ERROR_COORDINADOR_ASIGNADO_EXPEDIENTE, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        this.insertOrUpdate(ExpedienteEvalToEntity.INSTANCE.apply(expEvalCoorDTO));

        return this.expedienteService.cambiarEstadoRevEval(expEvalCoorDTO.getIdExpediente(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION),
                null);

    }

    /**
     *
     */
    @Override
    public Boolean existenVariosCoordinadorExpediente(Integer idExpediente)
            throws ServiceException {

        Boolean resultado = Boolean.FALSE;
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));

        Integer coincide = this.getDao().count(filtros);

        if (coincide > 1) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public Boolean verificarPermisoCoordinadorExpediente(Integer idExpediente, String cdUsuario)
            throws ServiceException {
        if (Objects.isNull(idExpediente) || StringUtils.isBlank(cdUsuario)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_CDNUMIDEN, cdUsuario,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, cdUsuario, Operador.NULL));

        List<ExpedienteEval> lstCoordinador = this.getDao().find(filtros, null);

        return !lstCoordinador.isEmpty();

    }

    /**
     *
     */
    @Override
    public List<ExpedienteEvalDTO> obtenerEvaluadoresAsignados(Integer idExpedienteEvalPadre)
            throws ServiceException {

        if (Objects.isNull(idExpedienteEvalPadre)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.EVALUADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_PADRE, idExpedienteEvalPadre,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

         List<ExpedienteEvalDTO> listaExpedientesEval= new ArrayList<>(
                 Lists.transform(this.getDao().find(filtros, null), ExpedienteEvalToDTO.INSTANCE));
         
         if (!listaExpedientesEval.isEmpty()) {
             for (ExpedienteEvalDTO item : listaExpedientesEval) {
                 item.setListaInformesEvaluacion(
                         this.informeEvalDocService.obtenerListaPorExpedienteEval(item.getIdExpedienteEval()));
             }
         }

         return listaExpedientesEval;

    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerListaDocEvalPorCdFicaInformeEvalDoc(String cdFica)
            throws ServiceException {
        return this.expedienteEvalDAO.obtenerListaDocEvalPorCdFicaInformeEvalDoc(cdFica);
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(
            ExpedienteFiltrosDTO item, Integer estadoCerradoCoordinador,
            Integer estadoCerradoEvaluador) throws ServiceException {
        return this.expedienteEvalDAO.obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(
                item, estadoCerradoCoordinador, estadoCerradoEvaluador);
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(
            ExpedienteFiltrosDTO item, Integer estadoCerradoCoordinador) throws ServiceException {
        return this.expedienteEvalDAO.obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(item, estadoCerradoCoordinador);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO asignarEvaluador(Integer idExpediente, Integer idEvaluador,
            String cdUsuario) throws ServiceException {

        ExpedienteEvalDTO evaluadorDTO = null;

        if (Objects.isNull(idExpediente) || Objects.isNull(idEvaluador)
                || StringUtils.isBlank(cdUsuario)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        boolean verificar = this.verificarPermisoCoordinadorExpediente(idExpediente, cdUsuario);

        if (verificar) {
            ExpedienteEval evaluador = new ExpedienteEval();
            evaluador.setCdUsuAlta(cdUsuario);
            evaluador.setFcAltaFila(new Date());
            evaluador.setEstado(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO)));
            evaluador.setItCoorEval(Constantes.TipoCoorEval.EVALUADOR);
            evaluador.setIdExpediente(idExpediente);
            evaluador.setPersona(new Interesado());
            evaluador.getPersona().setIdInteresado(idEvaluador);
            evaluador.setItNotificado(Constantes.AfirmacionNegacion.S);
            evaluador = this.getDao().insert(evaluador);

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                    Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    evaluador.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_CDNUMIDEN, cdUsuario,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteEval> lstCoordinador = this.find(filtros, null);
            if (!lstCoordinador.isEmpty()) {
                ExpedienteEval coordinador = lstCoordinador.get(0);
                coordinador.setEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EVALUADORES_ASIGNADOS)));
                coordinador.setItNotificado(Constantes.AfirmacionNegacion.N);
                coordinador.setCdUsuAlta(cdUsuario);
                coordinador.setFcAltaFila(new Date());
                this.getDao().merge(coordinador);
                evaluador.setIdExpedienteEvalPadre(coordinador.getIdExpedienteEval());
                evaluador = this.getDao().merge(evaluador);
            }

            evaluadorDTO = ExpedienteEvalToDTO.INSTANCE.apply(evaluador);

        } else {
            throw new ServiceException("El usuario no es un coordinador asociado al expediente",
                    null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        return evaluadorDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO desasociarEvaluador(Integer idExpediente, Integer idEvaluador,
            String cdUsuario) throws ServiceException {
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        try {
            if (Objects.isNull(idExpediente) || Objects.isNull(idEvaluador)
                    || Objects.isNull(cdUsuario)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }
            
           // localizamos el coordinador y le actualizamos el estado
            ExpedienteEval coordinador = null;
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                    Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    idExpediente, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_CDNUMIDEN, cdUsuario,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteEval> lstCoordinador = this.find(filtros, null);
            if (!lstCoordinador.isEmpty()) {
                coordinador = lstCoordinador.get(0);
                coordinador.setEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES)));
                coordinador.setItNotificado(Constantes.AfirmacionNegacion.S);
                coordinador.setCdUsuAlta(cdUsuario);
                coordinador.setFcAltaFila(new Date());
                this.getDao().merge(coordinador);
                
            }

            
            
            if(coordinador != null) {
                filtros = new ArrayList<>();

                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, idEvaluador,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_PADRE, coordinador.getIdExpedienteEval(),
                        Operador.EQUALS));
                
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteEval> listexpedienteEval = this.getDao().find(filtros, null);

                if (!listexpedienteEval.isEmpty()) {
                    ExpedienteEval expeEVAL = listexpedienteEval.get(0);
                    if (StringUtils.isNotBlank(cdUsuario)) {
                        expeEVAL.setCdUsuBaja(cdUsuario);
                        expeEVAL.setFcBajaFila(new Date());
                    }
                    expedienteEvalDTO = ExpedienteEvalToDTO.INSTANCE.apply(this.update(expeEVAL));
                }
            }
            
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        return expedienteEvalDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO rechazarInformeCoordinador(Integer idExpedienteEval)
            throws ServiceException {

        if (Objects.isNull(idExpedienteEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteEval expedienteEval = this.find(idExpedienteEval);

        if (Objects.nonNull(expedienteEval)
                && Objects.nonNull(expedienteEval.getIdExpedienteEval())) {

            expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.S);
            expedienteEval.setItRechazado(Constantes.AfirmacionNegacion.S);
            expedienteEval.setEstado(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR)));

//            // actualizamos estado interno del expediente
//            Expediente expediente = this.expedienteService.find(expedienteEval.getIdExpediente());
//            CatalogosValores estadoRevInterno = this.catalogosValoresService
//                    .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION));
//            expediente.setEstadoRevEval(estadoRevInterno);
//            expediente = this.expedienteService.update(expediente);
//            this.expedienteHistDAO.crearHistorico(expediente, null);
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(this.update(expedienteEval));
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public InformeEvalDocDTO rechazarInformeCoordinador(InformeEvalDocDTO informe)
            throws ServiceException {

        if (Objects.isNull(informe)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        InformeEvalDoc  informeEvalDoc = this.informeEvalDocService.find(informe.getIdEvalInfrmDoc());

        //informe.setItNotificado(Constantes.AfirmacionNegacion.S);
        informeEvalDoc.setItRechazado(Constantes.AfirmacionNegacion.S);
        informeEvalDoc.setIdEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR)));

//            // actualizamos estado interno del expediente
//            Expediente expediente = this.expedienteService.find(expedienteEval.getIdExpediente());
//            CatalogosValores estadoRevInterno = this.catalogosValoresService
//                    .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION));
//            expediente.setEstadoRevEval(estadoRevInterno);
//            expediente = this.expedienteService.update(expediente);
//            this.expedienteHistDAO.crearHistorico(expediente, null);
        
        
        return InformeEvalDocToDTO.INSTANCE.apply(this.informeEvalDocService.update(informeEvalDoc));
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO rechazarInformeEvaluador(Integer idExpedienteEval, Integer idInforme )
            throws ServiceException {

        if (Objects.isNull(idExpedienteEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteEval expedienteEval = this.find(idExpedienteEval);

        if (Objects.nonNull(expedienteEval)
                && Objects.nonNull(expedienteEval.getIdExpedienteEval())) {
            
            List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(idExpedienteEval);
            
            if (listaInformeEval != null && idInforme != null) {
                listaInformeEval = listaInformeEval.stream()
                        .filter(inf -> inf.getIdInforme() != null 
                                    && idInforme.equals(inf.getIdInforme().getIdInforme()))
                        .collect(Collectors.toList());
            }

            expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.S);
            expedienteEval.setItRechazado(Constantes.AfirmacionNegacion.S);
            expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR)));
            
            if(expedienteEval.getIdExpedienteEvalPadre() != null) {
                ExpedienteEval expedienteEvalPadre = this.find(expedienteEval.getIdExpedienteEvalPadre());
                if(expedienteEvalPadre != null) {
                    expedienteEvalPadre.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EVALUADORES_ASIGNADOS)));                 
                    this.update(expedienteEvalPadre);
                }
            }
            
            
            if (listaInformeEval != null && !listaInformeEval.isEmpty()) {

                Integer idEstadoEnEvaluacion = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR);

                for (InformeEvalDocDTO informeDto : listaInformeEval) {
                    
                    InformeEvalDoc informe = this.informeEvalDocDAO.find(informeDto.getIdEvalInfrmDoc()); 
                    
                    if (informe != null) {
                        if ("S".equalsIgnoreCase(informe.getItFirmado()) || informe.getItFirmado() == null) {
                            informe.setFcBajaFila(new Date());                            
                        } else if ("N".equalsIgnoreCase(informe.getItFirmado())) {
                            informe.setIdEstado(new CatalogosValores(idEstadoEnEvaluacion));   
                            informe.setItRechazado(Constantes.AfirmacionNegacion.S);
                        }
                        

                        this.informeEvalDocDAO.update(informe);
                    }
                }
            }
            
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(this.update(expedienteEval));
    }
    
    
    

    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public InformeEvalDocDTO aprobarInformeCoordinador(InformeEvalDocDTO informe)
           throws ServiceException {
       if (Objects.isNull(informe)) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
       }

       InformeEvalDoc  informeEvalDoc = this.informeEvalDocService.find(informe.getIdEvalInfrmDoc());

       if (Objects.nonNull(informeEvalDoc)
               && Objects.nonNull(informeEvalDoc.getIdInforme())) {

           informeEvalDoc.setIdEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR)));
       }

       return InformeEvalDocToDTO.INSTANCE.apply(this.informeEvalDocService.update(informeEvalDoc));

   }
    
    
    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO aprobarInformeCoordinador(Integer idExpedienteEval)
            throws ServiceException {
        if (Objects.isNull(idExpedienteEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteEval expedienteEval = this.find(idExpedienteEval);

        if (Objects.nonNull(expedienteEval)
                && Objects.nonNull(expedienteEval.getIdExpedienteEval())) {

            expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR)));
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(this.update(expedienteEval));

    }

    /**
     *
     */
    @Override
    public Boolean esCerradoGestorTodos(Integer idExpediente) throws ServiceException {

        Boolean resultado = Boolean.TRUE;

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        
        List<Integer> estadosExcluidos = new ArrayList<>();
        estadosExcluidos.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoCoordinador.CERRADO_GESTOR));
        estadosExcluidos.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                estadosExcluidos, Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> listaCoorNoCerradoGestor = this.getDao().find(filtros, null);

        if (!listaCoorNoCerradoGestor.isEmpty()) {
            resultado = Boolean.FALSE;
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public Boolean esCerradoCoordinadorTodos(Integer idExpediente) throws ServiceException {

        Boolean resultado = Boolean.TRUE;

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> listaCoor = this.getDao().find(filtros, null);

        if (listaCoor.isEmpty()) {
            resultado = Boolean.FALSE;
        } else {
            Integer idCerCoor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR);
            long noCerrados = listaCoor.stream()
                    .filter(p -> !idCerCoor.equals(p.getEstado().getIdValor()))
                    .count();
            if (noCerrados > 0) {
                resultado = Boolean.FALSE;
            }
        }

        return resultado;
    }

    /**
     * Comprueba si existe algun evaluador asignado a un coordinador
     * (ExpedienteEval con idExpedienteEvalPadre = idExpedienteEvalPadre, no
     * dado de baja) cuyo propio estado todavia no sea CERRADO_COORDINADOR.
     * Cubre el caso de evaluadores recien asignados que todavia no han
     * iniciado ninguna evaluacion (y por tanto no tienen registros en
     * InformeEvalDoc/ConvocatoriaCoorEval que permitan detectarlos mediante
     * obtenerTipoInformesPendientesRevisionCoordinadorEvaluadores).
     *
     * @param idExpedienteEvalPadre idExpedienteEval del coordinador
     * @param estadoCerradoCoordinador idValor del estado "Cerrado coordinador"
     * @return true si hay al menos un evaluador asignado sin cerrar por el coordinador
     * @throws ServiceException the service exception
     */
    private boolean existeEvaluadorAsignadoPendienteDeCierre(Integer idExpedienteEvalPadre,
            Integer estadoCerradoCoordinador) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_PADRE,
                idExpedienteEvalPadre));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.EVALUADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> listaEvaluadores = this.getDao().find(filtros, null);

        if (listaEvaluadores.isEmpty()) {
            return false;
        }

        return listaEvaluadores.stream()
                .anyMatch(p -> p.getEstado() == null
                        || !estadoCerradoCoordinador.equals(p.getEstado().getIdValor()));
    }

    /**
     * Devuelve TRUE si no hay ninguno que no sea CERRADO EVALUADOR
     */
    @Override
    public Boolean esCerradoEvaluadorTodos(Integer idExpediente) throws ServiceException {

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR), Operador.NOT_EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.EVALUADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        Integer total = this.getDao().count(filtros);

        return total == 0;
    }

    /**
     *
     */
    @Override
    public ExpedienteEvalDTO obtenerCoorEvalExpedienteEval(Integer idExpedienteEval,
            String itCoorEval) throws ServiceException {

        if (Objects.isNull(idExpedienteEval) || StringUtils.isBlank(itCoorEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL, idExpedienteEval,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> lst = this.find(filtros, null);
        ExpedienteEval ent = new ExpedienteEval();

        if (!lst.isEmpty()) {
            ent = lst.get(0);
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(ent);
    }

    /**
    *
    */
    @Override
    public String obtenerCausaRechazoCoordinador(Integer idExpedienteEval) throws ServiceException {

        if (Objects.isNull(idExpedienteEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL, idExpedienteEval,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> lst = this.find(filtros, null);
        ExpedienteEval ent = new ExpedienteEval();

        if (!lst.isEmpty() && lst.get(0).getCausa() != null) {
            ent = lst.get(0);

            String motivoRechazo = ent.getCausa().getDsValor();

            if (StringUtils.isNotBlank(ent.getDsObsRechazoInfEval())) {
                motivoRechazo += ". Observaciones: " + ent.getDsObsRechazoInfEval();
            }

            return motivoRechazo;
        }

        return null;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO desaprobarInformeEvaluador(String cdNumIden,
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException {
        
        Integer idExpediente = expedienteEvalDTO.getIdExpediente();
        Integer idEvaluador = expedienteEvalDTO.getIdExpedienteEval();
        Integer idCausa = expedienteEvalDTO.getCausaDTO().getIdValor();

        if (Objects.isNull(idExpediente) || Objects.isNull(idEvaluador)
                || StringUtils.isBlank(cdNumIden) || Objects.isNull(idCausa)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        ExpedienteEval expedienteEval = new ExpedienteEval();

        // obtener informe evaluador
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL, idEvaluador,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.EVALUADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteEval> lstEvaluadores = this.find(filtros, null);

        if (!lstEvaluadores.isEmpty()) {

            // actualizar estado de informe de evaluador
            expedienteEval = lstEvaluadores.get(0);
            expedienteEval.setEstado(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR)));
            expedienteEval.setCausa(new CatalogosValores(idCausa));
            expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.S);
            expedienteEval.setFcModifFila(new Date());
            expedienteEval.setCdUsuModif(cdNumIden);
            expedienteEval.setDsObsRechazoInfEval(expedienteEvalDTO.getDsObsRechazoInfEval());
            expedienteEval = this.getDao().merge(expedienteEval);
            
            if(expedienteEval.getIdExpedienteEvalPadre() != null) {
                ExpedienteEval coor =this.getDao().find(expedienteEval.getIdExpedienteEvalPadre());
                if (coor != null ) {

                    // actualizar estado de todos los informes de coordinadores
                    // asociados al expediente
                    coor.setEstado(
                                new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EVALUADORES_ASIGNADOS)));
                    coor.setFcModifFila(new Date());
                    coor.setCdUsuModif(cdNumIden);
                    coor.setItNotificado(Constantes.AfirmacionNegacion.S);
                    this.getDao().merge(coor);

                    

                }
            }

            

           

            // actualizar el InformeEvalDoc concreto con causa, observacion e itRechazado
            InformeEvalDocDTO informeEvalDocDTO = expedienteEvalDTO.getInformeEvalDocDTO();
            if (informeEvalDocDTO != null && informeEvalDocDTO.getIdEvalInfrmDoc() != null) {
                InformeEvalDocDTO informeEvalDocGuardar =
                        this.informeEvalDocService.buscarId(informeEvalDocDTO.getIdEvalInfrmDoc());
                if (informeEvalDocGuardar != null) {
                    informeEvalDocGuardar.setIdCausa(expedienteEvalDTO.getCausaDTO());
                    informeEvalDocGuardar.setDsObsRechazoInfEval(
                            expedienteEvalDTO.getDsObsRechazoInfEval());
                    informeEvalDocGuardar.setItRechazado(Constantes.AfirmacionNegacion.S);
                    CatalogosValoresDTO estadoEnEvaluacionDTO = new CatalogosValoresDTO();
                    estadoEnEvaluacionDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR));
                    informeEvalDocGuardar.setIdEstado(estadoEnEvaluacionDTO);
                    this.informeEvalDocService.mergeActualizar(informeEvalDocGuardar);
                }
            }

        }

        return ExpedienteEvalToDTO.INSTANCE.apply(expedienteEval);
    }

    /**
     *
     */
    @Override
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            String itCoorEval) throws ServiceException {
        return this.getDao().obtenerInformeEvaluacion(idExpedienteEval, itCoorEval);
    }

    /**
     *
     */
    @Override
    public List<InfApartadoDatoDTO> obtenerApartadosDatos(Integer idExpedienteEval,
            Integer idEvaluacion) throws ServiceException {
        return this.getDao().obtenerApartadosDatos(idExpedienteEval, idEvaluacion);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO finalizarInformeEvaluacion(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] documento) throws ServiceException {

        if (Objects.isNull(expedienteEvalDTO) || StringUtils.isBlank(strIdentificador)
                || Objects.isNull(documento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        ExpedienteEval expedienteEval = this.find(expedienteEvalDTO.getIdExpedienteEval());
        if (Objects.nonNull(expedienteEval)) {
            expedienteEval.setCdFica(
                    this.guardarFicaFichero(expedienteEval, documento, strIdentificador));

            if (Constantes.TipoCoorEval.COORDINADOR.equals(expedienteEvalDTO.getItCoorEval())) {
                expedienteEval.setDsComentario(expedienteEvalDTO.getDsComentario());
                expedienteEval.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                this.finalizarInformeCoordinador(expedienteEval, strIdentificador);
            } else if (Constantes.TipoCoorEval.EVALUADOR
                    .equals(expedienteEvalDTO.getItCoorEval())) {
                expedienteEval.setDsComentario(null);
                expedienteEval.setNmNotaFinal(null);
                this.finalizarInformeEvaluador(expedienteEval, strIdentificador,
                        expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getIdValor());
            }

        }

        return ExpedienteEvalToDTO.INSTANCE.apply(expedienteEval);
    }

    /**
     * Metodo para finalizar informe de coordinador con informe
     *
     * @param expedienteEval
     * @param strIdentificador
     * @throws ServiceException
     */
    private void finalizarInformeCoordinador(ExpedienteEval expedienteEval, String strIdentificador)
            throws ServiceException {

        Expediente expediente = this.expedienteDAO.find(expedienteEval.getIdExpediente());
        boolean todosInformesConFica = false;
        boolean todosEvaluadoresCerrados = false;
        if (expediente != null && expediente.getConvocatoriaAct() != null) {
            Integer estadoCerrado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR);

            // 1) tipos de informe propios del coordinador (ConvocatoriaCoor)
            ExpedienteFiltrosDTO filtroTipos = new ExpedienteFiltrosDTO();
            filtroTipos.setIdExpedienteEval(expedienteEval.getIdExpedienteEval());
            filtroTipos.setIdConvocatoria(expediente.getConvocatoriaAct().getIdConvocatoria());
            List<CatalogosValoresDTO> tiposPendientes =
                    this.expedienteEvalDAO.obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(filtroTipos, estadoCerrado);
            todosInformesConFica = (tiposPendientes == null || tiposPendientes.isEmpty());

            // 2) tipos de informe de TODOS los evaluadores asignados a este coordinador
            // (ExpedienteEval con idExpedienteEvalPadre = expedienteEval.getIdExpedienteEval()).
            // Un coordinador puede tener varios evaluadores asignados y cada evaluador puede
            // tener asignados uno o varios tipos de informe; hasta que todos ellos no estén
            // cerrados y revisados por el coordinador, el expedienteEval del coordinador no
            // puede pasar a CERRADO_COORDINADOR.
            List<CatalogosValoresDTO> tiposPendientesEvaluadores = this.expedienteEvalDAO
                    .obtenerTipoInformesPendientesRevisionCoordinadorEvaluadores(
                            expedienteEval.getIdExpedienteEval(), estadoCerrado);
            todosEvaluadoresCerrados = (tiposPendientesEvaluadores == null || tiposPendientesEvaluadores.isEmpty());

            // 3) Comprobacion adicional a nivel de ExpedienteEval del propio evaluador.
            // La comprobacion (2) se basa en InformeEvalDoc/ConvocatoriaCoorEval, que solo
            // tienen registros cuando el evaluador ya ha iniciado (o le han rechazado) alguna
            // evaluacion. Un evaluador recien asignado, que todavia no ha iniciado ningun
            // informe, no genera ese tipo de registros y quedaria sin contabilizar como
            // pendiente. Por eso se comprueba tambien el estado del propio ExpedienteEval de
            // cada evaluador hijo: si alguno no esta en CERRADO_COORDINADOR, sigue pendiente.
            if (todosEvaluadoresCerrados) {
                todosEvaluadoresCerrados = !this.existeEvaluadorAsignadoPendienteDeCierre(
                        expedienteEval.getIdExpedienteEval(), estadoCerrado);
            }
        }

        expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.S);
        expedienteEval.setItRechazado(Constantes.AfirmacionNegacion.N);
        expedienteEval.setCdUsuModif(strIdentificador);
        expedienteEval.setFcModifFila(new Date());
        if (todosInformesConFica && todosEvaluadoresCerrados) {
            expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR)));
        }
        expedienteEval = this.getDao().merge(expedienteEval);

        // verifica si todos los coordinadores del expediente han
        // cumplimentado su informe
        if (expediente != null && Boolean.TRUE.equals(this.esCerradoCoordinadorTodos(expedienteEval.getIdExpediente()))) {

                expediente.setCdUsuModif(strIdentificador);
                expediente.setFcModifFila(new Date());

                expediente.setNmNota(this.getDao()
                        .calcularNotaMediaExpediente(expedienteEval.getIdExpediente()));

                expediente.setEstadoRevEval(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR)));

                this.expedienteDAO.update(expediente);
                this.expedienteHistDAO.crearHistorico(expediente, strIdentificador);

            
        }

    }

    /**
     * Metodo para finalizar informe de evaluador con informe
     *
     * @param expedienteEval
     * @param strIdentificador
     * @throws ServiceException
     */
    private void finalizarInformeEvaluador(ExpedienteEval expedienteEval, String strIdentificador, Integer idTipoInforme)
            throws ServiceException { // idTipoInforme no se usa; se mantiene por compatibilidad con los call sites

       // Expediente expediente = this.expedienteDAO.find(expedienteEval.getIdExpediente());
       // boolean todosInformesConFica = true;
        //escenario donde el evaluador pueda evaluar dos tipos de informes
       /* if (expediente != null && expediente.getConvocatoriaAct() != null) {
            ExpedienteFiltrosDTO filtroTipos = new ExpedienteFiltrosDTO();
            filtroTipos.setIdExpedienteEval(expedienteEval.getIdExpedienteEval());
            filtroTipos.setIdConvocatoria(expediente.getConvocatoriaAct().getIdConvocatoria());
            filtroTipos.setItCoorEval(Constantes.TipoCoorEval.EVALUADOR);
            List<CatalogosValoresDTO> tiposPendientes =
                    this.expedienteEvalDAO.obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(
                            filtroTipos,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR));
            todosInformesConFica = (tiposPendientes == null || tiposPendientes.isEmpty());
        }*/

        expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.S);
        expedienteEval.setCdUsuModif(strIdentificador);
        expedienteEval.setFcModifFila(new Date());
        //if (todosInformesConFica) {
            expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR)));
       // }
        expedienteEval.setDsComentario(null);
        expedienteEval.setNmNotaFinal(null);
        expedienteEval = this.getDao().merge(expedienteEval);

        // si se han cerrado todos los informes de evaluador actualizamos el
        // estado del coordinador padre
        //List<InformeEvalDocDTO> informesCerradosEvaluador = this.informeEvalDocService.obtenerListaPorExpedienteEval(expedienteEval.getIdExpedienteEval());
        if ( /*informesCerradosEvaluador != null && !informesCerradosEvaluador.isEmpty() && */
                 expedienteEval.getIdExpedienteEvalPadre() != null) {

            ExpedienteEval coordinador = this.getDao().find(expedienteEval.getIdExpedienteEvalPadre());
            if (coordinador != null) {
                coordinador.setEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR)));
                coordinador.setItNotificado(Constantes.AfirmacionNegacion.S);
                coordinador.setItRechazado(Constantes.AfirmacionNegacion.N);
                coordinador.setCdUsuAlta(strIdentificador);
                coordinador.setFcAltaFila(new Date());
                this.getDao().merge(coordinador);
            }

        }

    }

    /**
     * Metodo de obtener de ficaFichero de expedienteEval
     *
     * @throws ServiceException
     */
    private String guardarFicaFichero(ExpedienteEval expedienteEval, byte[] documento,
            String strIdentificador) throws ServiceException {

        String cdFica = "";

        // Validacion
        if (documento == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        // Nombre, fecha y extension del fichero
        ficaMetadatos.setNombreFichero(Constantes.TipoCoorEval.INFORME_EVALUACION);
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
        ficaMetadatos.setUsuario(strIdentificador);
        ficaMetadatos.setEncriptar(false);
        ficaMetadatos.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        FicaFichero fichero = null;
        if (expedienteEval.getCdFica() != null) {
            fichero = this.ficaService.actualizar(Integer.parseInt(expedienteEval.getCdFica()),
                    documento, ficaMetadatos);
            cdFica = String.valueOf(fichero.getIdInterno());
        } else {
            fichero = this.ficaService.guardar(documento, ficaMetadatos);
            cdFica = String.valueOf(fichero.getIdInterno());
        }
        return cdFica;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO finalizarSinInformeEvaluacion(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador) throws ServiceException {

        if (Objects.isNull(expedienteEvalDTO)
                || Objects.isNull(expedienteEvalDTO.getIdExpedienteEval())
                || StringUtils.isBlank(strIdentificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteEval expedienteEval = this.find(expedienteEvalDTO.getIdExpedienteEval());

        if (Objects.nonNull(expedienteEval)) {

            expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR)));
            expedienteEval.setDsComentario(expedienteEvalDTO.getDsComentario());
            expedienteEval.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
            expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.N);
            expedienteEval.setCdFica(null);
            expedienteEval.setCdUsuModif(strIdentificador);
            expedienteEval.setFcModifFila(new Date());
            expedienteEval = this.getDao().merge(expedienteEval);

            if (Boolean.TRUE
                    .equals(this.esCerradoCoordinadorTodos(expedienteEvalDTO.getIdExpediente()))) {

                Expediente expediente = this.expedienteDAO
                        .find(expedienteEvalDTO.getIdExpediente());

                if (Objects.nonNull(expediente)) {

                    expediente.setNmNota(this.getDao()
                            .calcularNotaMediaExpediente(expedienteEvalDTO.getIdExpediente()));
                    expediente.setCdUsuModif(strIdentificador);
                    expediente.setFcModifFila(new Date());

                    expediente.setEstadoRevEval(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR)));

                    this.expedienteDAO.update(expediente);
                    this.expedienteHistDAO.crearHistorico(expediente, strIdentificador);

                }

            }

        }

        return ExpedienteEvalToDTO.INSTANCE.apply(expedienteEval);
    }

    @Override
    public Boolean existePersonaExpedienteEval(Integer idInteresado, Integer IdExpediente) {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, IdExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }

    @Override
    public Boolean existePersonaExpedienteEvalComoCoordinador(Integer idInteresado,
            Integer IdExpediente) {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, IdExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }

    @Override
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacionByInforme(Integer idExpedienteEval,
            InformeDTO informeDTO) throws ServiceException {
        return this.getDao().obtenerInformeEvaluacion(idExpedienteEval, informeDTO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO finalizarInformeEvaluacionFirmado(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] documentoFirmado,byte[] documentoSinFirma) throws ServiceException {

        if (Objects.isNull(expedienteEvalDTO) || StringUtils.isBlank(strIdentificador)
                || Objects.isNull(documentoFirmado)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        if(documentoSinFirma != null) {
            log.debug("impreso evaluacion sin firmar: " + Base64.getEncoder().encodeToString(documentoSinFirma));  
        }
       
        if(documentoFirmado != null) {
            log.debug("impreso evaluacion firmado: " + Base64.getEncoder().encodeToString(documentoFirmado));
        }
           
        
       
      
        ExpedienteEval expedienteEval = this.find(expedienteEvalDTO.getIdExpedienteEval());
        if (Objects.nonNull(expedienteEval) && expedienteEvalDTO.getInformeDTO() != null && expedienteEvalDTO.getInformeDTO().getTipoInformeDTO() != null) {
                Integer cdFica =this.guardarInformeFicaFichero(expedienteEval, documentoFirmado, strIdentificador,expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getDsValor());
                if (Constantes.TipoCoorEval.COORDINADOR.equals(expedienteEvalDTO.getItCoorEval())) {
                    CatalogosValoresDTO estadoCerradoDTO = new CatalogosValoresDTO();
                    estadoCerradoDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR));

                    // guardar el informe firmado del coordinador en InformeEvalDoc
                    Integer idInforme = expedienteEvalDTO.getInformeDTO().getIdInforme();
                    InformeEvalDocDTO informeEvalDocFirmado = this.informeEvalDocService
                            .obtenerPorExpedienteEvalInformeItFirmado(
                                    expedienteEvalDTO.getIdExpedienteEval(), idInforme,
                                    Constantes.AfirmacionNegacion.S);
                    if (informeEvalDocFirmado != null) {
                        informeEvalDocFirmado.setCdFica(String.valueOf(cdFica));
                        informeEvalDocFirmado.setIdEstado(estadoCerradoDTO);
                        this.informeEvalDocService.mergeActualizar(informeEvalDocFirmado);
                    } else {
                        informeEvalDocFirmado = new InformeEvalDocDTO();
                        informeEvalDocFirmado.setCdFica(String.valueOf(cdFica));
                        informeEvalDocFirmado.setCdUsuAlta(expedienteEval.getCdUsuAlta());
                        informeEvalDocFirmado.setFcAltaFila(new Date());
                        informeEvalDocFirmado.setIdExpedienteEval(expedienteEvalDTO.getIdExpedienteEval());
                        informeEvalDocFirmado.setIdInforme(expedienteEvalDTO.getInformeDTO());
                        informeEvalDocFirmado.setDsComentario(expedienteEvalDTO.getDsComentario());
                        informeEvalDocFirmado.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                        informeEvalDocFirmado.setItRechazado(Constantes.AfirmacionNegacion.N);
                        informeEvalDocFirmado.setItFirmado(Constantes.AfirmacionNegacion.S);
                        informeEvalDocFirmado.setIdEstado(estadoCerradoDTO);
                        this.informeEvalDocService.guardar(informeEvalDocFirmado);
                    }

                    // guardar el informe sin firmar del coordinador en InformeEvalDoc
                    if (documentoSinFirma != null) {
                        cdFica = this.guardarInformeFicaFichero(expedienteEval, documentoSinFirma, strIdentificador, expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getDsValor());

                        InformeEvalDocDTO informeEvalDocSinFirma = this.informeEvalDocService
                                .obtenerPorExpedienteEvalInformeItFirmado(
                                        expedienteEvalDTO.getIdExpedienteEval(), idInforme,
                                        null);
                        if (informeEvalDocSinFirma != null) {
                            informeEvalDocSinFirma.setCdFica(String.valueOf(cdFica));
                            informeEvalDocSinFirma.setIdEstado(estadoCerradoDTO);
                            this.informeEvalDocService.mergeActualizar(informeEvalDocSinFirma);
                        } else {
                            informeEvalDocSinFirma = new InformeEvalDocDTO();
                            informeEvalDocSinFirma.setCdFica(String.valueOf(cdFica));
                            informeEvalDocSinFirma.setCdUsuAlta(expedienteEval.getCdUsuAlta());
                            informeEvalDocSinFirma.setFcAltaFila(new Date());
                            informeEvalDocSinFirma.setIdExpedienteEval(expedienteEvalDTO.getIdExpedienteEval());
                            informeEvalDocSinFirma.setIdInforme(expedienteEvalDTO.getInformeDTO());
                            informeEvalDocSinFirma.setDsComentario(expedienteEvalDTO.getDsComentario());
                            informeEvalDocSinFirma.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                            informeEvalDocSinFirma.setItRechazado(Constantes.AfirmacionNegacion.N);
                            informeEvalDocSinFirma.setIdEstado(estadoCerradoDTO);
                            this.informeEvalDocService.guardar(informeEvalDocSinFirma);
                        }
                    }

                    // actualizar el InformeEvalDoc del evaluador a estado CERRADO_COORDINADOR
                    if (expedienteEvalDTO.getInformeEvalDocDTO() != null
                            && expedienteEvalDTO.getInformeEvalDocDTO().getIdEvalInfrmDoc() != null) {
                        InformeEvalDocDTO informeEvalDocEval = this.informeEvalDocService
                                .buscarId(expedienteEvalDTO.getInformeEvalDocDTO().getIdEvalInfrmDoc());
                        if (informeEvalDocEval != null) {
                            informeEvalDocEval.setIdEstado(estadoCerradoDTO);
                            this.informeEvalDocService.mergeActualizar(informeEvalDocEval);
                        }
                    }

                    expedienteEval.setDsComentario(expedienteEvalDTO.getDsComentario());
                    expedienteEval.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                    this.finalizarInformeCoordinador(expedienteEval, strIdentificador);
                } else if (Constantes.TipoCoorEval.EVALUADOR
                        .equals(expedienteEvalDTO.getItCoorEval())) {
                    // guardar el informe del evaluador en InformeEvalDoc
                    Integer idInformeEval = expedienteEvalDTO.getInformeDTO().getIdInforme();
                    InformeEvalDocDTO informeEvalDocEvaluador = this.informeEvalDocService
                            .obtenerPorExpedienteEvalInformeItFirmado(
                                    expedienteEvalDTO.getIdExpedienteEval(), idInformeEval,
                                    Constantes.AfirmacionNegacion.N);
                    if (informeEvalDocEvaluador != null) {

                        informeEvalDocEvaluador.setCdFica(String.valueOf(cdFica));
                        informeEvalDocEvaluador.setDsComentario(expedienteEvalDTO.getDsComentario());
                        informeEvalDocEvaluador.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());

                        CatalogosValoresDTO estadoCerradoEvaluadorDTO = new CatalogosValoresDTO();
                        estadoCerradoEvaluadorDTO.setIdValor(
                                utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR));

                        informeEvalDocEvaluador.setIdEstado(estadoCerradoEvaluadorDTO);
                        this.informeEvalDocService.mergeActualizar(informeEvalDocEvaluador);
                    } else {
                    
                        informeEvalDocEvaluador = new InformeEvalDocDTO();
                        informeEvalDocEvaluador.setCdFica(String.valueOf(cdFica));
                        informeEvalDocEvaluador.setCdUsuAlta(expedienteEval.getCdUsuAlta());
                        informeEvalDocEvaluador.setFcAltaFila(new Date());
                        informeEvalDocEvaluador.setIdExpedienteEval(
                                expedienteEvalDTO.getIdExpedienteEval());
                        informeEvalDocEvaluador.setIdInforme(expedienteEvalDTO.getInformeDTO());
                        informeEvalDocEvaluador.setDsComentario(expedienteEvalDTO.getDsComentario());
                        informeEvalDocEvaluador.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                        informeEvalDocEvaluador.setItRechazado(
                                Constantes.AfirmacionNegacion.N);
                        informeEvalDocEvaluador.setItFirmado(
                                Constantes.AfirmacionNegacion.N);

                        CatalogosValoresDTO estadoCerradoEvaluadorDTO =
                                new CatalogosValoresDTO();

                        estadoCerradoEvaluadorDTO.setIdValor(
                                utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR));

                        informeEvalDocEvaluador.setIdEstado(estadoCerradoEvaluadorDTO);
                        this.informeEvalDocService.guardar(informeEvalDocEvaluador);
                    }

                    expedienteEval.setDsComentario(null);
                    expedienteEval.setNmNotaFinal(null);
                    this.finalizarInformeEvaluador(expedienteEval, strIdentificador,
                            expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getIdValor());
                }




        }

        return ExpedienteEvalToDTO.INSTANCE.apply(expedienteEval);
    }


    private Integer guardarInformeFicaFichero(ExpedienteEval expedienteEval, byte[] documento,
            String strIdentificador, String nombreFichero) throws ServiceException {

        Integer cdFica = null;

        // Validacion
        if (documento == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Expediente expediente = this.expedienteDAO.find(expedienteEval.getIdExpediente());
        String nombreFicheroCompleto = nombreFichero;
        if (expediente != null && expediente.getCdExpediente() != null) {
            nombreFicheroCompleto = nombreFichero + "_" + expediente.getCdExpediente().replace("/", "-");
        }

        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        // Nombre, fecha y extension del fichero
        ficaMetadatos.setNombreFichero(Utils.normalizarSinAcentos(nombreFicheroCompleto) + ".pdf");
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
        ficaMetadatos.setUsuario(strIdentificador);
        ficaMetadatos.setEncriptar(false);
        ficaMetadatos.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        FicaFichero fichero = null;
        if (expedienteEval.getCdFica() != null) {
            fichero = this.ficaService.actualizar(Integer.parseInt(expedienteEval.getCdFica()),
                    documento, ficaMetadatos);
            cdFica = fichero.getIdInterno();
        } else {
            fichero = this.ficaService.guardar(documento, ficaMetadatos);
            cdFica =fichero.getIdInterno();
        }
        return cdFica;
    }

    @Override
    @Transactional(readOnly = false)
    public void finalizarInformeEvaluadorFirmado(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] docPdfFirmado, byte[] docPdfSinFirmado) throws ServiceException {
        ExpedienteEval expedienteEval = this.find(expedienteEvalDTO.getIdExpedienteEval());
        if (Objects.nonNull(expedienteEval)
                && expedienteEvalDTO.getInformeDTO() != null
                && expedienteEvalDTO.getInformeDTO().getTipoInformeDTO() != null) {
            Integer idInforme = expedienteEvalDTO.getInformeDTO().getIdInforme();
            CatalogosValoresDTO estadoCerradoDTO = new CatalogosValoresDTO();
            estadoCerradoDTO.setIdValor(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR));

            // guardar el informe sin firmar del evaluador en InformeEvalDoc
            if (docPdfSinFirmado != null) {
                Integer cdFicaSinFirma = this.guardarInformeFicaFichero(expedienteEval,
                        docPdfSinFirmado, strIdentificador,
                        expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getDsValor());
                InformeEvalDocDTO informeEvalDocSinFirma = this.informeEvalDocService
                        .obtenerPorExpedienteEvalInformeItFirmado(
                                expedienteEvalDTO.getIdExpedienteEval(), idInforme,
                               null);
                if (informeEvalDocSinFirma != null) {
                    informeEvalDocSinFirma.setCdFica(String.valueOf(cdFicaSinFirma));
                    informeEvalDocSinFirma.setIdEstado(estadoCerradoDTO);
                    this.informeEvalDocService.mergeActualizar(informeEvalDocSinFirma);
                } else {
                    informeEvalDocSinFirma = new InformeEvalDocDTO();
                    informeEvalDocSinFirma.setCdFica(String.valueOf(cdFicaSinFirma));
                    informeEvalDocSinFirma.setCdUsuAlta(expedienteEval.getCdUsuAlta());
                    informeEvalDocSinFirma.setFcAltaFila(new Date());
                    informeEvalDocSinFirma.setIdExpedienteEval(expedienteEvalDTO.getIdExpedienteEval());
                    informeEvalDocSinFirma.setIdInforme(expedienteEvalDTO.getInformeDTO());
                    informeEvalDocSinFirma.setDsComentario(expedienteEvalDTO.getDsComentario());
                    informeEvalDocSinFirma.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
                    informeEvalDocSinFirma.setItRechazado(Constantes.AfirmacionNegacion.N);
                    
                    informeEvalDocSinFirma.setIdEstado(estadoCerradoDTO);
                    this.informeEvalDocService.guardar(informeEvalDocSinFirma);
                }
            }

            // guardar el informe firmado del evaluador en InformeEvalDoc
            Integer cdFicaFirmado = this.guardarInformeFicaFichero(expedienteEval, docPdfFirmado,
                    strIdentificador,
                    expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getDsValor());
            InformeEvalDocDTO informeEvalDocFirmado = new InformeEvalDocDTO();
            informeEvalDocFirmado.setCdFica(String.valueOf(cdFicaFirmado));
            informeEvalDocFirmado.setCdUsuAlta(expedienteEval.getCdUsuAlta());
            informeEvalDocFirmado.setFcAltaFila(new Date());
            informeEvalDocFirmado.setIdExpedienteEval(expedienteEvalDTO.getIdExpedienteEval());
            informeEvalDocFirmado.setIdInforme(expedienteEvalDTO.getInformeDTO());
            informeEvalDocFirmado.setDsComentario(expedienteEvalDTO.getDsComentario());
            informeEvalDocFirmado.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
            informeEvalDocFirmado.setItRechazado(Constantes.AfirmacionNegacion.N);
            informeEvalDocFirmado.setItFirmado(Constantes.AfirmacionNegacion.S);
            informeEvalDocFirmado.setIdEstado(estadoCerradoDTO);
            this.informeEvalDocService.guardar(informeEvalDocFirmado);
            
            // Actualizamos el informe del evaluador si ha sido rechazado anterirormente, le quitamos el rechazo 
            
            InformeEvalDocDTO informeEvalDocSinFirma = this.informeEvalDocService
                    .obtenerPorExpedienteEvalInformeItFirmado(
                            expedienteEvalDTO.getIdExpedienteEval(), idInforme,
                           "N");
            
            if (informeEvalDocSinFirma != null && informeEvalDocSinFirma.getItRechazado().equalsIgnoreCase(Constantes.AfirmacionNegacion.S)) {
                informeEvalDocSinFirma.setFcModifFila(new Date());
                informeEvalDocSinFirma.setCdUsuModif(expedienteEval.getCdUsuAlta());
                informeEvalDocSinFirma.setDsObsRechazoInfEval(null);
                informeEvalDocSinFirma.setIdCausa(null);
                informeEvalDocSinFirma.setItRechazado(Constantes.AfirmacionNegacion.N);
                this.informeEvalDocService.mergeActualizar(informeEvalDocSinFirma);
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO cerrarExpedienteEvalPorCoordinador(ExpedienteEvalDTO expEvalCoordinadorDTO,
            ExpedienteEvalDTO expEvalEvaluadorDTO, String strIdentificador) throws ServiceException {

        ExpedienteEval expEvalCoordinador = this.find(expEvalCoordinadorDTO.getIdExpedienteEval());
        ExpedienteEval expEvalEvaluador = this.find(expEvalEvaluadorDTO.getIdExpedienteEval());

        if (expEvalCoordinador == null || expEvalEvaluador == null) {
            return expEvalEvaluadorDTO;
        }

        // Comprobar si al evaluador que se esta cerrando le queda ALGUN OTRO tipo de
        // informe (de los que tiene asignados) todavia sin evaluar o sin cerrar por
        // el coordinador. El informe que se esta cerrando en esta misma llamada ya se
        // ha marcado como CERRADO_COORDINADOR en InformeEvalDoc previamente (ver
        // finalizarInformeEvaluadorFirmado), asi que si no queda ningun otro tipo
        // pendiente, este era el ultimo y el ExpedienteEval del evaluador puede
        // cerrarse.
        Expediente expediente = this.expedienteDAO.find(expEvalCoordinador.getIdExpediente());
        boolean todosCerrados = false;
        if (expediente != null && expediente.getConvocatoriaAct() != null) {
            Integer estadoCerrado = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR);
            List<CatalogosValoresDTO> tiposPendientes =
                    this.expedienteEvalDAO.obtenerTipoInformesPendientesCierreCoordinadorEvaluador(
                            expEvalEvaluador.getIdExpedienteEval(), estadoCerrado);
            todosCerrados = (tiposPendientes == null || tiposPendientes.isEmpty());
        }

        if (todosCerrados) {
            // Ya no le queda ningun tipo de informe pendiente a este evaluador: se
            // cierra su ExpedienteEval y, a continuacion, se reevalua si con esto el
            // coordinador puede darse por cerrado (esCerradoCoordinadorTodos/
            // existeEvaluadorAsignadoPendienteDeCierre necesitan leer este estado ya
            // actualizado).
            expEvalEvaluador.setItNotificado(Constantes.AfirmacionNegacion.N);
            expEvalEvaluador.setItRechazado(Constantes.AfirmacionNegacion.N);
            expEvalEvaluador.setCausa(null);
            expEvalEvaluador.setDsObsRechazoInfEval(null);
            expEvalEvaluador.setCdUsuModif(strIdentificador);
            expEvalEvaluador.setFcModifFila(new Date());
            expEvalEvaluador.setEstado(new CatalogosValores(
                    utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR)));
            expEvalEvaluador = this.getDao().merge(expEvalEvaluador);

            // Asignar CERRADO_COORDINADOR al expedienteEval del coordinador, si procede
            this.finalizarInformeCoordinador(expEvalCoordinador, strIdentificador);
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(expEvalEvaluador);
    }

    @Override
    public ExpedienteEvalDTO obtenerExpedienteEvalByIdExpediente(Integer idExpediente,
            String itCoorEval) throws ServiceException {

        if (Objects.isNull(idExpediente) || StringUtils.isBlank(itCoorEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEval> lst = this.find(filtros, null);
        ExpedienteEval ent = new ExpedienteEval();

        if (!lst.isEmpty()) {
            ent = lst.get(0);
        }

        return ExpedienteEvalToDTO.INSTANCE.apply(ent);
    }

   

}
