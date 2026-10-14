package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.EvaluacionTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionTipoDocToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionTipoDocToEntity;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio EvaluacionTipoDoc
 *
 * @author quental
 */
@Service("evaluacionTipoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class EvaluacionTipoDocServiceImpl
        extends BaseServiceImpl<EvaluacionTipoDoc, Integer, EvaluacionTipoDocDAO>
        implements EvaluacionTipoDocService {
    
    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param EvaluacionTipoDocDAO
     */
    @Autowired
    public EvaluacionTipoDocServiceImpl(@Qualifier("evaluacionTipoDocDAO")
    EvaluacionTipoDocDAO evaluacionTipoDocDAO) {
        super();
        this.setDao(evaluacionTipoDocDAO);
    }

    /**
     * Constructor Vacio
     */
    public EvaluacionTipoDocServiceImpl() {
        super();
    }

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /** Repositorio ConvocatoriaService */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /** DAO **/
    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /** Repositorio ExpedienteDAO */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    

    /**
     *
     */
    @Override
    public List<EvaluacionTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionTipoDocToDTO.INSTANCE));

    }

       
    /**
     *
     */
    @Override
    public List<EvaluacionTipoDocDTO> obtenerListaEvalTipoDocPorInforme(Integer idInforme, Integer idConvocatoria)
            throws ServiceException {
        List<EvaluacionTipoDocDTO> lstEvalTipoDocDTO = null;

        if (idInforme != null && idConvocatoria != null) {
         //   lstEvalTipoDocDTO = this.getDao().obtenerTipoDocumentosEval(idConvocatoria, idInforme);
        }
        return lstEvalTipoDocDTO;
    }

    /**
     * 
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionTipoDocDTO agregar(Integer idConvocatoria, Integer idInforme,
            Integer idTipoDocumento) throws ServiceException {
        
        EvaluacionTipoDoc evalTipoDoc = new EvaluacionTipoDoc();
//        EvaluacionTipoDoc evalTipoDocExist = this.buscarIdConvocatoriaEvalTipoDoc(
//                idConvocatoria, idNivelAsociacion, idTipoDocumento);
//        if (evalTipoDocExist == null
//                || evalTipoDocExist.getIdConvocatoriaEvalTipoDoc() == null) {
//            CatalogosValores nivelAsociacion = this.catalogosValoresDAO.find(idNivelAsociacion);
//            CatalogosValores tipoDocumento = this.catalogosValoresDAO.find(idTipoDocumento);
//            if (nivelAsociacion != null || tipoDocumento != null) {
//                evalTipoDoc.setItObligatorio(Constantes.AfirmacionNegacion.N);
//                evalTipoDoc.setIdNivel(nivelAsociacion);
//                evalTipoDoc.setIdTipoDocumento(tipoDocumento);
//                evalTipoDoc.setIdConvocatoria(idConvocatoria);
//                evalTipoDoc = this.insertOrUpdate(evalTipoDoc);
//            }
//        } else if (evalTipoDocExist.getFcBajaFila() != null){
//            evalTipoDocExist.setCdUsuBaja(null);
//            evalTipoDocExist.setFcBajaFila(null);
//            evalTipoDoc = this.insertOrUpdate(evalTipoDoc);
//        }
        return EvaluacionTipoDocToDTO.INSTANCE.apply(evalTipoDoc);
    }
    
    /**
     * 
     * @param idConvocatoria
     * @param idInforme
     * @param idTipoDocumento
     * @return
     */
//    private EvaluacionTipoDoc buscarIdEvalTipoDoc(final Integer idConvocatoria,
//            final Integer idInforme, final Integer idTipoDocumento)
//            throws ServiceException {
//        ConvocatoriaEvalTipoDoc convocatoriaEvalTipoDoc = new ConvocatoriaEvalTipoDoc();
//        List<AtlasFilter> filtros = new ArrayList<>();
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
//                Operador.EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_NIVEL, idNivelAsociacion,
//                Operador.EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO,
//                idTipoDocumento, Operador.EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        List<ConvocatoriaEvalTipoDoc> lstConvocatoriaEvalTipoDoc = new ArrayList<>(
//                this.find(filtros, null));
//        if (!lstConvocatoriaEvalTipoDoc.isEmpty()) {
//            convocatoriaEvalTipoDoc = lstConvocatoriaEvalTipoDoc.get(0);
//        }
//        return convocatoriaEvalTipoDoc;
//    }
 
    /**
     * 
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionTipoDocDTO guardar(EvaluacionTipoDocDTO evaluacionTipoDocDTO)
            throws ServiceException {
        return EvaluacionTipoDocToDTO.INSTANCE.apply(this.insertOrUpdate(
                EvaluacionTipoDocToEntity.INSTANCE.apply(evaluacionTipoDocDTO)));
    }

    /**
     * 
     */
    @Override
    public EvaluacionTipoDocDTO buscarId(Integer idEvalTipoDoc)
            throws ServiceException {
        EvaluacionTipoDoc evalTipoDoc = this.find(idEvalTipoDoc);
        return EvaluacionTipoDocToDTO.INSTANCE.apply(evalTipoDoc);
    }
   
    
    /**
     * Método que se ejecuta al pulsar el botón eliminar documento eval
     *
     * @param evaluacionTipoDocDTO
     * @throws ServiceException 
     */
    @Transactional(readOnly = false)
    public void borradoLogicoEvalTipoDoc(EvaluacionTipoDocDTO evaluacionTipoDocDTO) throws ServiceException {
        
        evaluacionTipoDocDTO.setCdUsuBaja(UserUtils.getUserName().toUpperCase());
        evaluacionTipoDocDTO.setFcBajaFila(new Date());
        
        this.guardar(evaluacionTipoDocDTO);

    }
}
