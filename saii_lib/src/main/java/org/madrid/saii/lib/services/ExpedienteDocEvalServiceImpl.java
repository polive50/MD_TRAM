package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalToEntity;
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
import atlas.fica.services.FicaService;
import lombok.Setter;

/**
 * Implementación del servicio para la clase de dominio ExpedienteDocEval
 *
 * @author nttdata
 */
@Service("expedienteDocEvalService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteDocEvalServiceImpl
        extends BaseServiceImpl<ExpedienteDocEval, Integer, ExpedienteDocEvalDAO>
        implements ExpedienteDocEvalService {

   
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

    @Autowired
    @Qualifier("expedienteDocEvalHistService")
    @Setter
    private ExpedienteDocEvalHistService expedienteDocEvalHistService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    @Qualifier("evaluacionTipoDocService")
    @Setter
    private EvaluacionTipoDocService evaluacionTipoDocService;
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteEvalDAO
     */
    @Autowired
    public ExpedienteDocEvalServiceImpl(@Qualifier("expedienteDocEvalDAO")
    ExpedienteDocEvalDAO expedienteDocEvalDAO) {
        super();
        this.setDao(expedienteDocEvalDAO);
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteDocEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteDocEvalDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteDocEvalToDTO.INSTANCE.apply(this.find(id));
    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerExpedienteDocEvalByIdExpEval(Integer idExpedienteEval) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXP_DOC_EVAL, idExpedienteEval,
                Operador.EQUALS));
       
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocEvalToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO guardar(ExpedienteEvalDTO expedienteEvalDTO)
            throws ServiceException {
        if (Objects.isNull(expedienteEvalDTO.getIdExpedienteEval())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        ExpedienteDocEvalDTO expDocEvalDTO = null;
        if(expedienteEvalDTO.getItAceptaEval() != null && expedienteEvalDTO.getItAceptaEval().equals(Constantes.AfirmacionNegacion.S) ) {
            ExpedienteEval expEval=  this.expedienteEvalDAO.find(expedienteEvalDTO.getIdExpedienteEval());
            expEval.setItAceptaEval(expedienteEvalDTO.getItAceptaEval());
            
            CatalogosValores estado = new CatalogosValores();
            if(expEval.getItCoorEval().equalsIgnoreCase(Constantes.TipoCoorEval.EVALUADOR)) {
                estado.setIdValor((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR)));
                estado.setNmConstante(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR);
                expEval.setEstado(estado);
            }else {
                estado.setIdValor((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES)));
                estado.setNmConstante(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES);
                expEval.setEstado(estado);
            }
            
            expEval.setItNotificado(Constantes.AfirmacionNegacion.S);
            
            this.expedienteEvalDAO.update(expEval);
            
            List<ExpedienteDocEvalDTO> listaDocExpEval =expedienteEvalDTO.getListaExpedienteDocEvalDTO();
            if(listaDocExpEval != null && !listaDocExpEval.isEmpty()) {
                for(ExpedienteDocEvalDTO doc:listaDocExpEval) {
                    doc.setCdUsuAlta(expEval.getCdUsuAlta());
                    doc.setFcAltaFila(new Date());
                    ExpedienteDocEval expDocEvalEntity = this.insertOrUpdate(ExpedienteDocEvalToEntity.INSTANCE.apply(doc));
                    expDocEvalDTO = ExpedienteDocEvalToDTO.INSTANCE.apply(expDocEvalEntity);
                    this.expedienteDocEvalHistService.crearHistorico(expDocEvalDTO);
                }
            }
            
            return expedienteEvalDTO;
        }else {
          ExpedienteEval expEval=  this.expedienteEvalDAO.find(expedienteEvalDTO.getIdExpedienteEval());
          expEval.setDsComentarioRechazoAceptacion(expedienteEvalDTO.getDsComentarioRechazoAceptacion());
          expEval.setItAceptaEval(expedienteEvalDTO.getItAceptaEval());
          CatalogosValores causaRechazo = new CatalogosValores();
          causaRechazo.setIdValor(expedienteEvalDTO.getMotivoRechazo().getIdValor());
          expEval.setMotivoRechazo(causaRechazo);
          CatalogosValores estado = new CatalogosValores();
          estado.setIdValor((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EVALUACION_RECHAZADA_EXPERTO)));
          estado.setNmConstante(Constantes.EstadoCoordinador.EVALUACION_RECHAZADA_EXPERTO);
          expEval.setEstado(estado);
          expEval.setItNotificado(Constantes.AfirmacionNegacion.S);
          this.expedienteEvalDAO.update(expEval);

          if (expEval.getItCoorEval() != null && expEval.getItCoorEval().equalsIgnoreCase(Constantes.TipoCoorEval.EVALUADOR)) {
              List<AtlasFilter> filtrosCoor = new ArrayList<>();
              filtrosCoor.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, expEval.getIdExpediente(), Operador.EQUALS));
              filtrosCoor.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));
              filtrosCoor.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
              List<ExpedienteEval> lstCoordinador = this.expedienteEvalDAO.find(filtrosCoor, null);
              if (lstCoordinador != null && !lstCoordinador.isEmpty()) {
                  for (ExpedienteEval coordinador : lstCoordinador) {
                      CatalogosValores estadoCoor = new CatalogosValores();
                      estadoCoor.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
                      estadoCoor.setNmConstante(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES);
                      coordinador.setEstado(estadoCoor);
                      coordinador.setItNotificado(Constantes.AfirmacionNegacion.S);
                      this.expedienteEvalDAO.update(coordinador);
                  }
              }
          }
        }
        return expedienteEvalDTO;
        
    }

    @Override
    public void guardarInformeEvaluacion(ExpedienteDocEvalDTO docFirmado) throws ServiceException {
        ExpedienteDocEval expDocEvalEntity = this.insertOrUpdate(ExpedienteDocEvalToEntity.INSTANCE.apply(docFirmado));
        ExpedienteDocEvalDTO expDocEvalDTO = ExpedienteDocEvalToDTO.INSTANCE.apply(expDocEvalEntity);
        this.expedienteDocEvalHistService.crearHistorico(expDocEvalDTO);
        
    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException {
        List<ExpedienteDocEvalDTO> listaExpDocEval= new ArrayList<>();
        if(expedienteEvalDTO.getInformeDTO() != null) {
            List<EvaluacionTipoDocDTO> listaEvalTipoDoc=  this.evaluacionTipoDocService.getDao().obtenerTipoDocumentosEvaluacion(expedienteEvalDTO.getInformeDTO().getIdInforme());
            if(listaEvalTipoDoc != null && !listaEvalTipoDoc.isEmpty()) {
                for(EvaluacionTipoDocDTO tipo : listaEvalTipoDoc ) {
                    ExpedienteDocEvalDTO doc= this.getDao().obtenerPorTipoDocPorExpedienteEval(tipo.getIdTipoDocumento().getIdValor(),expedienteEvalDTO.getIdExpedienteEval());
                    if(doc != null && doc.getIdDocumento()!= null) {
                        listaExpDocEval.add(doc);
                        
                    }else {
                        ExpedienteDocEval docExpEvalNuevo= new ExpedienteDocEval();
                        docExpEvalNuevo.setFcAltaFila(expedienteEvalDTO.getFcAltaFila());
                        docExpEvalNuevo.setCdUsuAlta(expedienteEvalDTO.getCdUsuAlta());
                        ExpedienteEval expEvalEntity= new ExpedienteEval();
                        expEvalEntity.setIdExpedienteEval(expedienteEvalDTO.getIdExpedienteEval());
                        docExpEvalNuevo.setIdExpedienteEval(expEvalEntity);
                        CatalogosValores tipoDocEntity= new CatalogosValores();
                        tipoDocEntity.setIdValor(tipo.getIdTipoDocumento().getIdValor());
                        docExpEvalNuevo.setIdTipoDocumento(tipoDocEntity);
                        
                        ExpedienteDocEval expDocEvalEntity = this.insertOrUpdate(docExpEvalNuevo);
                        doc = ExpedienteDocEvalToDTO.INSTANCE.apply(expDocEvalEntity);
                        if (doc.getIdTipoDocumento() != null) {
                            doc.getIdTipoDocumento().setDsValor(tipo.getIdTipoDocumento().getDsValor());
                        }
                        this.expedienteDocEvalHistService.crearHistorico(doc);
                        listaExpDocEval.add(doc);
                        
                    }
                }
            }
            
        }
        
        return listaExpDocEval;
    }

    @Override
    public List<ExpedienteDocEvalDTO> buscarPorFica(Integer cdFica) throws ServiceException {
        List<ExpedienteDocEvalDTO>  listaDoc= new ArrayList<>();
        if(cdFica != null) {
            List<ExpedienteDocEvalDTO> listaResult= this.getDao().buscarDocumentosInformePorFica(cdFica);
            if(listaResult != null && !listaResult.isEmpty()) {
                ExpedienteDocEvalDTO docuInforme = listaResult.get(0);
                
                listaDoc= this.getDao().buscarDocumentosPortipoDocumento(docuInforme.getIdExpedienteEval().getIdExpedienteEval(),docuInforme.getDsNombreFichero());
                
            }
        }
        
       
        
        return listaDoc;
    }

   

}
