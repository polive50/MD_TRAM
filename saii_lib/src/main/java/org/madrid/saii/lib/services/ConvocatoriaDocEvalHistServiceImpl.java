package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalHistToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalHistToEntity;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaDocEvalHist
 *
 * @author everis
 */
@Service("convocatoriaDocEvalHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaDocEvalHistServiceImpl
        extends BaseServiceImpl<ConvocatoriaDocEvalHist, Integer, ConvocatoriaDocEvalHistDAO>
        implements ConvocatoriaDocEvalHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDocDAOHist
     */
    @Autowired
    public ConvocatoriaDocEvalHistServiceImpl(@Qualifier("convocatoriaDocEvalHistDAO")
    ConvocatoriaDocEvalHistDAO convocatoriaDocEvalHistDAO) {
        super();
        this.setDao(convocatoriaDocEvalHistDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaDocEvalHistServiceImpl() {
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
    public List<ConvocatoriaDocEvalHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaDocEvalHistToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDocEvalHistDTO guardar(ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO)
            throws ServiceException {

        if (Objects.isNull(convocatoriaDocEvalHistDTO.getIdConvocatoria()) || Objects.isNull(convocatoriaDocEvalHistDTO.getPersona()) 
                || Objects.isNull(convocatoriaDocEvalHistDTO.getIdConvocatoriaDocEval()) || Objects.isNull(convocatoriaDocEvalHistDTO.getItCoorEval()) ) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
 
       ConvocatoriaDocEvalHist convocatoriaDocEvalHist = this
                   .insertOrUpdate(ConvocatoriaDocEvalHistToEntity.INSTANCE.apply(convocatoriaDocEvalHistDTO));
           return ConvocatoriaDocEvalHistToDTO.INSTANCE.apply(convocatoriaDocEvalHist);
      

    }

    
    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalHistDTO buscarId(final Integer idDocumentacion) throws ServiceException {

        // Validacion de entrada
        if (idDocumentacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return ConvocatoriaDocEvalHistToDTO.INSTANCE.apply(this.find(idDocumentacion));

    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalHistDTO descargar(Integer cdFica) throws ServiceException {

        if (Objects.isNull(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO = new ConvocatoriaDocEvalHistDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
        convocatoriaDocEvalHistDTO.setFicaFichero(ficaFichero);
        convocatoriaDocEvalHistDTO.setContenidoFichero(ficaFichero.getDatosFichero());
        convocatoriaDocEvalHistDTO.setCdFica(cdFica);
        return convocatoriaDocEvalHistDTO;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocEvalHistDTO> obtenerDocumentosConvocatoriaEvalHist(ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO)
            throws ServiceException {
        List<ConvocatoriaDocEvalHistDTO> lstConvocatoriaDocEvalHistDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        if (convocatoriaDocEvalHistDTO != null && convocatoriaDocEvalHistDTO.getIdConvocatoria() != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, convocatoriaDocEvalHistDTO.getIdConvocatoria(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, convocatoriaDocEvalHistDTO.getPersona().getIdInteresado(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, convocatoriaDocEvalHistDTO.getItCoorEval(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }

        lstConvocatoriaDocEvalHistDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalHistToDTO.INSTANCE));

        return lstConvocatoriaDocEvalHistDTO;
    }

   

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocEvalHistDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalHistToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public Boolean existeTipoDocumento(Integer idConvocatoria, Integer idTipoDocumento)
            throws ServiceException {

        if (Objects.isNull(idConvocatoria) || Objects.isNull(idTipoDocumento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        Integer total = this.getDao().count(filtros);

        return total > 0;
    }

}
