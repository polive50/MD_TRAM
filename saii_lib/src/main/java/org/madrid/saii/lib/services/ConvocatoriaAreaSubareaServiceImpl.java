package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaSubareaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaSubareaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAreaSubareaToDTO;
import org.madrid.saii.lib.util.Constantes;
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

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaAreaSubarea
 *
 * @author quental
 */
@Service("convocatoriaAreaSubareaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaAreaSubareaServiceImpl
        extends BaseServiceImpl<ConvocatoriaAreaSubarea, Integer, ConvocatoriaAreaSubareaDAO>
        implements ConvocatoriaAreaSubareaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaAreaSubareaDAO
     */
    @Autowired
    public ConvocatoriaAreaSubareaServiceImpl(
            @Qualifier("convocatoriaAreaSubareaDAO") ConvocatoriaAreaSubareaDAO convocatoriaAreaSubareaDAO) {
        super();
        this.setDao(convocatoriaAreaSubareaDAO);
    }

    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /** Repositorio ConvocatoriaAreaDAO */
    @Autowired
    @Qualifier("convocatoriaAreaDAO")
    private ConvocatoriaAreaDAO convocatoriaAreaDAO;

    /**
     *
     */
    @Override
    public List<ConvocatoriaAreaSubareaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaAreaSubareaToDTO.INSTANCE));
    }

    /**
     * Buscar por Id convocatoria area y id subárea científica
     */
    private ConvocatoriaAreaSubarea buscarIdConvocatoriaAreaSubarea(final Integer idConvocatoriaArea,
            final Integer idValor) throws ServiceException {

        ConvocatoriaAreaSubarea convocatoriaAreaSubarea = new ConvocatoriaAreaSubarea();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_AREA, idConvocatoriaArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_SUBAREA_CIENTIFICA, idValor,
                Operador.EQUALS));
        List<ConvocatoriaAreaSubarea> lstConvocatoriaAreaSubarea = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaAreaSubarea.isEmpty()) {
            convocatoriaAreaSubarea = lstConvocatoriaAreaSubarea.get(0);
        }
        return convocatoriaAreaSubarea;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaAreaSubareaDTO agregar(Integer idConvocatoriaArea, Integer idValor)
            throws ServiceException {

        ConvocatoriaAreaSubarea convAreaSubarea = new ConvocatoriaAreaSubarea();

        ConvocatoriaAreaSubarea convAreaSubareaExist = this.buscarIdConvocatoriaAreaSubarea(idConvocatoriaArea, idValor);

        if (convAreaSubareaExist == null || convAreaSubareaExist.getIdConvocatoriaArea() == null) {

            CatalogosValores subareaCientifica = this.catalogosValoresDAO.find(idValor);

            if (subareaCientifica != null) {
                convAreaSubarea.setSubarea(subareaCientifica);
                convAreaSubarea.setIdConvocatoriaArea(idConvocatoriaArea);
                convAreaSubarea = this.insertOrUpdate(convAreaSubarea);
            }

        } else if (convAreaSubareaExist.getFcBajaFila() != null) {
            convAreaSubareaExist.setCdUsuBaja(null);
            convAreaSubareaExist.setFcBajaFila(null);
            convAreaSubarea = this.insertOrUpdate(convAreaSubareaExist);
            
        } else if (convAreaSubareaExist.getFcBajaFila() == null) {
            throw new ServiceException(Constantes.Error.ERROR_TIPO_AREA_DUPLICADO, null,
                    Constantes.Error.MSG_ERROR);
        }

        return ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(convAreaSubarea);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaAreaSubareaDTO> obtenerSubareasCientificasPorConvocatoriaArea(Integer idConvocatoriaArea)
            throws ServiceException {
        List<ConvocatoriaAreaSubareaDTO> listaDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_AREA, idConvocatoriaArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaAreaSubarea> lista = this.find(filtros, null);

        if (lista != null && !lista.isEmpty()) {
            listaDTO = new ArrayList<>();
            for (ConvocatoriaAreaSubarea subarea : lista) {
                listaDTO.add(ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(subarea));
            }
        }

        return listaDTO;
    }

}
