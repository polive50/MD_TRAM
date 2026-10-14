package org.madrid.saii.lib.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalDAO;
import org.madrid.saii.lib.dao.ConvocatoriaEvalTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalTipoDocToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalTipoDocToEntity;
import org.madrid.saii.lib.dto.converters.InteresadoToEntity;
import org.madrid.saii.lib.util.Constantes;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
@Service("convocatoriaEvalTipoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaEvalTipoDocServiceImpl
        extends BaseServiceImpl<ConvocatoriaEvalTipoDoc, Integer, ConvocatoriaEvalTipoDocDAO>
        implements ConvocatoriaEvalTipoDocService {
    
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
     * @param convocatoriaEvalTipoDocDAO
     */
    @Autowired
    public ConvocatoriaEvalTipoDocServiceImpl(@Qualifier("convocatoriaEvalTipoDocDAO")
    ConvocatoriaEvalTipoDocDAO convocatoriaEvalTipoDocDAO) {
        super();
        this.setDao(convocatoriaEvalTipoDocDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaEvalTipoDocServiceImpl() {
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
    public List<ConvocatoriaEvalTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaEvalTipoDocToDTO.INSTANCE));

    }

    
   
    /**
     *
     */
    @Override
    public List<ConvocatoriaEvalTipoDocDTO> obtenerListaConvocatoriaEvalTipoDocPorNivel(Integer nivel, Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaEvalTipoDocDTO> lstConvocatoriaEvalTipoDocDTO = null;

        if (nivel != null && idConvocatoria != null) {
            lstConvocatoriaEvalTipoDocDTO = this.getDao().obtenerTipoDocumentosConvocatoriaEval(idConvocatoria, nivel);
        }
        return lstConvocatoriaEvalTipoDocDTO;
    }

    /**
     * 
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEvalTipoDocDTO agregar(Integer idConvocatoria, Integer idNivelAsociacion,
            Integer idTipoDocumento) throws ServiceException {
        ConvocatoriaEvalTipoDoc convEvalTipoDoc = new ConvocatoriaEvalTipoDoc();
        ConvocatoriaEvalTipoDoc convEvalTipoDocExist = this.buscarIdConvocatoriaEvalTipoDoc(
                idConvocatoria, idNivelAsociacion, idTipoDocumento);
        if (convEvalTipoDocExist == null
                || convEvalTipoDocExist.getIdConvocatoriaEvalTipoDoc() == null) {
            CatalogosValores nivelAsociacion = this.catalogosValoresDAO.find(idNivelAsociacion);
            CatalogosValores tipoDocumento = this.catalogosValoresDAO.find(idTipoDocumento);
            if (nivelAsociacion != null || tipoDocumento != null) {
                convEvalTipoDoc.setItObligatorio(Constantes.AfirmacionNegacion.N);
                convEvalTipoDoc.setIdNivel(nivelAsociacion);
                convEvalTipoDoc.setIdTipoDocumento(tipoDocumento);
                convEvalTipoDoc.setIdConvocatoria(idConvocatoria);
                convEvalTipoDoc = this.insertOrUpdate(convEvalTipoDoc);
            }
        } else if (convEvalTipoDocExist.getFcBajaFila() != null){
            convEvalTipoDocExist.setCdUsuBaja(null);
            convEvalTipoDocExist.setFcBajaFila(null);
            convEvalTipoDoc = this.insertOrUpdate(convEvalTipoDoc);
        }
        return ConvocatoriaEvalTipoDocToDTO.INSTANCE.apply(convEvalTipoDoc);
    }
    
    /**
     * 
     * @param idConvocatoria
     * @param idNivelAsociacion
     * @param idTipoDocumento
     * @return
     */
    private ConvocatoriaEvalTipoDoc buscarIdConvocatoriaEvalTipoDoc(final Integer idConvocatoria,
            final Integer idNivelAsociacion, final Integer idTipoDocumento)
            throws ServiceException {
        ConvocatoriaEvalTipoDoc convocatoriaEvalTipoDoc = new ConvocatoriaEvalTipoDoc();
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_NIVEL, idNivelAsociacion,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ConvocatoriaEvalTipoDoc> lstConvocatoriaEvalTipoDoc = new ArrayList<>(
                this.find(filtros, null));
        if (!lstConvocatoriaEvalTipoDoc.isEmpty()) {
            convocatoriaEvalTipoDoc = lstConvocatoriaEvalTipoDoc.get(0);
        }
        return convocatoriaEvalTipoDoc;
    }

    /**
     * 
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEvalTipoDocDTO guardar(ConvocatoriaEvalTipoDocDTO convocatoriaEvalTipoDocDTO)
            throws ServiceException {
        return ConvocatoriaEvalTipoDocToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaEvalTipoDocToEntity.INSTANCE.apply(convocatoriaEvalTipoDocDTO)));
    }

    /**
     * 
     */
    @Override
    public ConvocatoriaEvalTipoDocDTO buscarId(Integer convocatoriaEvalTipoDocId)
            throws ServiceException {
        ConvocatoriaEvalTipoDoc convocatoriaEvalTipoDoc = this.find(convocatoriaEvalTipoDocId);
        return ConvocatoriaEvalTipoDocToDTO.INSTANCE.apply(convocatoriaEvalTipoDoc);
    }
   
}
