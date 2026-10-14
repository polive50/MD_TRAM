package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAreaToDTO;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaArea
 *
 * @author nttdata
 */
@Service("convocatoriaAreaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaAreaServiceImpl
        extends BaseServiceImpl<ConvocatoriaArea, Integer, ConvocatoriaAreaDAO>
        implements ConvocatoriaAreaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaAreaDAO
     */
    @Autowired
    public ConvocatoriaAreaServiceImpl(
            @Qualifier("convocatoriaAreaDAO") ConvocatoriaAreaDAO convocatoriaAreaDAO) {
        super();
        this.setDao(convocatoriaAreaDAO);
    }

    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     *
     */
    @Override
    public List<ConvocatoriaAreaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaAreaToDTO.INSTANCE));
    }

    /**
     * Buscar por Id convocatoria y id área científica
     */
    private ConvocatoriaArea buscarIdConvocatoriaArea(final Integer idConvocatoria,
            final Integer idValor) throws ServiceException {

        ConvocatoriaArea convocatoriaArea = new ConvocatoriaArea();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_AREA_CIENTIFICA, idValor,
                Operador.EQUALS));
        List<ConvocatoriaArea> lstConvocatoriaArea = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaArea.isEmpty()) {
            convocatoriaArea = lstConvocatoriaArea.get(0);
        }
        return convocatoriaArea;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaAreaDTO agregar(Integer idConvocatoria, Integer idValor)
            throws ServiceException {

        ConvocatoriaArea convArea = new ConvocatoriaArea();

        ConvocatoriaArea convAreaExist = this.buscarIdConvocatoriaArea(idConvocatoria, idValor);

        if (convAreaExist == null || convAreaExist.getIdConvocatoriaArea() == null) {

            CatalogosValores areaCientifica = this.catalogosValoresDAO.find(idValor);

            if (areaCientifica != null) {
                convArea.setArea(areaCientifica);
                convArea.setIdConvocatoria(idConvocatoria);
                convArea = this.insertOrUpdate(convArea);
            }

        } else if (convAreaExist.getFcBajaFila() != null) {
            convAreaExist.setCdUsuBaja(null);
            convAreaExist.setFcBajaFila(null);
            convArea = this.insertOrUpdate(convAreaExist);
        } else if (convAreaExist.getFcBajaFila() == null) {
            throw new ServiceException(Constantes.Error.ERROR_TIPO_AREA_DUPLICADO, null,
                    Constantes.Error.MSG_ERROR);
        }

        return ConvocatoriaAreaToDTO.INSTANCE.apply(convArea);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaAreaDTO> obtenerAreasCientificasPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaAreaDTO> listaDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaArea> lista = this.find(filtros, null);

        if (lista != null && !lista.isEmpty()) {
            listaDTO = new ArrayList<>();
            for (ConvocatoriaArea area : lista) {
                listaDTO.add(ConvocatoriaAreaToDTO.INSTANCE.apply(area));
            }
        }

        return listaDTO;
    }

}
