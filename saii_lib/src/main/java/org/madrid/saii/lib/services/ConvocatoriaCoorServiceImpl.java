package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.ConvocatoriaCoorDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCoorEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorFiltrosDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorNavegacionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCoorToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCoorToEntity;
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
import lombok.Setter;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaCoor
 *
 * @author nttdata
 */
@Service("convocatoriaCoorService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaCoorServiceImpl
extends BaseServiceImpl<ConvocatoriaCoor, Integer, ConvocatoriaCoorDAO>
implements ConvocatoriaCoorService {
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCoorDAO
     */
    @Autowired
    public ConvocatoriaCoorServiceImpl(
            @Qualifier("convocatoriaCoorDAO") ConvocatoriaCoorDAO convocatoriaCoorDAO) {
        super();
        this.setDao(convocatoriaCoorDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaCoorServiceImpl() {
        super();
    }

    /**
     * DAO de ConvocatoriaCoorEval
     */
    @Autowired
    @Qualifier("convocatoriaCoorEvalDAO")
    @Setter
    private ConvocatoriaCoorEvalDAO convocatoriaCoorEvalDAO;
    
    @Autowired
    @Qualifier("convocatoriaEvalTipoDocService")
    private ConvocatoriaEvalTipoDocService convocatoriaEvalTipoDocService;
    
    @Autowired
    @Qualifier("convocatoriaDocEvalService")
    private ConvocatoriaDocEvalService convocatoriaDocEvalService;

    /**
     *
     */

    @Override
    public ConvocatoriaCoorDTO buscarId(Integer idConvocatoriaCoor) throws ServiceException {
        if (idConvocatoriaCoor == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        return ConvocatoriaCoorToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCoor));
    }

    /**
     *
     */

    @Override
    public List<ConvocatoriaCoorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaCoorToDTO.INSTANCE));
    }

    /**
     *
     */

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCoorDTO guardar(ConvocatoriaCoorDTO convocatoriaCoorDTO)
            throws ServiceException {
        if (convocatoriaCoorDTO.getIdConvocatoria() == null
//                || convocatoriaCoorDTO.getAreaDTO() == null
//                || convocatoriaCoorDTO.getAreaDTO().getIdValor() == null
                || convocatoriaCoorDTO.getInteresadoDTO() == null
                || convocatoriaCoorDTO.getInteresadoDTO().getIdInteresado() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        return ConvocatoriaCoorToDTO.INSTANCE.apply(
                this.insertOrUpdate(ConvocatoriaCoorToEntity.INSTANCE.apply(convocatoriaCoorDTO)));
    }

    /**
     *
     */

    @Override
    public List<ConvocatoriaCoorDTO> obtenerCoordinadoresPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        if (idConvocatoria == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), ConvocatoriaCoorToDTO.INSTANCE));
    }

    /**
     *
     */

    @Override
    public Boolean existeAreaPersonaConvocatoriaCoor(Integer idConvocatoria, Integer idArea,
            Integer idInteresado) throws ServiceException {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_AREA_CIENTIFICA, idArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }
    
    /**
    *
    */
    @Override
    public Boolean existeAreaPersonaConvocatoriaCoor(Integer idConvocatoria, Integer idArea,
            Integer idTipoinforme, Integer idInteresado) throws ServiceException {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_AREA_CIENTIFICA, idArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_INFORME_ID_VALOR, idTipoinforme,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public Boolean existeConvocatoriaCoorArea(Integer idConvocatoria, Integer idArea)
            throws ServiceException {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_AREA_CIENTIFICA, idArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias <= 1) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }
    
    /**
     * 
     */
    @Override
    public Boolean existeConvocatoriaCoorTipoInforme(Integer idConvocatoria, Integer idTipoInforme)
            throws ServiceException {
        Boolean resultado = Boolean.FALSE;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_INFORME_ID_VALOR, idTipoInforme,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        int coincidencias = this.getDao().count(filtros);
        if (coincidencias <= 1) {
            resultado = Boolean.TRUE;
        }
        return resultado;
    }

    /**
     *
     */
    @Override
    public ConvocatoriaCoorNavegacionDTO obtenerConvocatoriaCoor(ConvocatoriaCoorFiltrosDTO filtros)
            throws ServiceException {
        ConvocatoriaCoorNavegacionDTO result = this.getDao().obtenerConvocatoriasCoor(filtros);
        if (result != null && result.getLstConvocatoriaCoorFiltrosDTO() != null
                && !result.getLstConvocatoriaCoorFiltrosDTO().isEmpty()) {

            Integer revisionOkIdValor = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.REVISION_OK);

            for (ConvocatoriaCoorFiltrosDTO item : result.getLstConvocatoriaCoorFiltrosDTO()) {
                Boolean documentosConvocatoriaSubidos = Boolean.TRUE;
                Boolean documentosConvocatoriaRevisados = Boolean.TRUE;

                List<ConvocatoriaEvalTipoDocDTO> listaTipoDoc = this.convocatoriaEvalTipoDocService
                        .obtenerListaConvocatoriaEvalTipoDocPorNivel(
                                Constantes.TipoNivelAsociacion.NIVEL_CONVOCATORIA, item.getIdConvocatoria());
                if (listaTipoDoc != null && !listaTipoDoc.isEmpty()) {
                    listaTipoDoc = listaTipoDoc.stream()
                            .filter(t -> t.getItObligatorio() != null && t.getItObligatorio().equals(Boolean.TRUE))
                            .collect(Collectors.toList());
                }

                if (listaTipoDoc != null && !listaTipoDoc.isEmpty()) {
                    ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO = new ConvocatoriaDocEvalDTO();
                    convocatoriaDocEvalDTO.setIdConvocatoria(item.getIdConvocatoria());
                    InteresadoDTO personaDto = new InteresadoDTO();
                    personaDto.setIdInteresado(item.getIdInteresado());
                    convocatoriaDocEvalDTO.setPersona(personaDto);
                    convocatoriaDocEvalDTO.setItCoorEval(item.getItCoorEval());

                    List<ConvocatoriaDocEvalDTO> listaDocConvocatoria = this.convocatoriaDocEvalService
                            .obtenerDocumentosConvocatoriaEval(convocatoriaDocEvalDTO);

                    for (ConvocatoriaEvalTipoDocDTO tipo : listaTipoDoc) {
                        if (listaDocConvocatoria == null || listaDocConvocatoria.isEmpty()) {
                            documentosConvocatoriaSubidos = Boolean.FALSE;
                            documentosConvocatoriaRevisados = Boolean.FALSE;
                            break;
                        }
                        boolean existe = listaDocConvocatoria.stream()
                                .anyMatch(d -> d.getIdTipoDocumento() != null
                                        && d.getIdTipoDocumento().getIdValor()
                                                .equals(tipo.getTipoDocumentoDTO().getIdValor()));
                        if (!existe) {
                            documentosConvocatoriaSubidos = Boolean.FALSE;
                            documentosConvocatoriaRevisados = Boolean.FALSE;
                            break;
                        }
                        boolean revisado = listaDocConvocatoria.stream()
                                .anyMatch(d -> d.getIdTipoDocumento() != null
                                        && d.getIdTipoDocumento().getIdValor()
                                                .equals(tipo.getTipoDocumentoDTO().getIdValor())
                                        && d.getEstado() != null
                                        && revisionOkIdValor.equals(d.getEstado().getIdValor()));
                        if (!revisado) {
                            documentosConvocatoriaRevisados = Boolean.FALSE;
                        }
                    }
                }

                item.setDocumentosConvocatoriaSubidos(documentosConvocatoriaSubidos);
                item.setDocumentosConvocatoriaRevisados(documentosConvocatoriaRevisados);
            }
        }
        return result;
    }

    /**
     *
     */   
    @Override
    public List<ConvocatoriaCoorDTO> obtenerCoordinadorConvocatoriaArea(Integer idConvocatoria,
            Integer idArea) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

//        List<AtlasFilter> filtrosAreaOr = new ArrayList<>();
//
//        filtrosAreaOr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDAREA, idArea, Operador.EQUALS));
//        filtrosAreaOr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDAREA, Operador.NULL));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDAREA, filtrosAreaOr, Operador.OR));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaCoorToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void eliminarCoordinadorEvaluador(Integer idConvocatoriaCoor) throws ServiceException {

        // Obtenemos los evaluadores asociados al coordinador
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_COOR,
                idConvocatoriaCoor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ConvocatoriaCoorEval> lista = this.convocatoriaCoorEvalDAO.find(filtros, null);

        // Eliminamos a los evaluadores
        for (ConvocatoriaCoorEval convocatoriaCoorEval : lista) {
            this.convocatoriaCoorEvalDAO
            .deleteLogico(convocatoriaCoorEval.getIdConvocatoriaCoorEval());
        }

        // Eliminamos al coordinador
        this.deleteLogico(idConvocatoriaCoor);

    }
    
    /**
    *
    */

   @Override
   public ConvocatoriaCoorDTO buscarByIdConvocatoria(Integer idConvocatoria) throws ServiceException {
       if (idConvocatoria == null) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
       }
       return ConvocatoriaCoorToDTO.INSTANCE.apply(this.getDao().find(idConvocatoria));
   }
   
   @Override
   public List<ConvocatoriaCoorDTO> buscarByIdConvocatoriaAndIdArea(Integer idConvocatoria, Integer idArea)
           throws ServiceException {
       if (idConvocatoria == null || idArea == null) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
       }
       List<AtlasFilter> filtros = new ArrayList<>();

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDAREA, idArea,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       return new ArrayList<>(
               Lists.transform(this.getDao().find(filtros, null), ConvocatoriaCoorToDTO.INSTANCE));
   }

}
