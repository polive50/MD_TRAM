package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauContDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.madrid.saii.lib.dto.ConvocatoriaCauContDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCauContToDTO;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaCauCont
 *
 * @author nttdata
 */
@Service("convocatoriaCauContService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaCauContServiceImpl
        extends BaseServiceImpl<ConvocatoriaCauCont, Integer, ConvocatoriaCauContDAO>
        implements ConvocatoriaCauContService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCauContDAO
     */
    @Autowired
    public ConvocatoriaCauContServiceImpl(
            @Qualifier("convocatoriaCauContDAO") ConvocatoriaCauContDAO convocatoriaCauContDAO) {
        super();
        this.setDao(convocatoriaCauContDAO);
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
    public ConvocatoriaCauContDTO buscarId(Integer idConvocatoriaCauCont) throws ServiceException {
        return ConvocatoriaCauContToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCauCont));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaCauContDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaCauContToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauContDTO agregar(Integer idConvocatoria, Integer idCausaCont)
            throws ServiceException {
        if (Objects.isNull(idConvocatoria) || Objects.isNull(idCausaCont)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ConvocatoriaCauCont convCauCont = new ConvocatoriaCauCont();
        ConvocatoriaCauCont convCauContExist = this.buscarIdConvocatoriaCau(idConvocatoria,
                idCausaCont);

        if (Objects.isNull(convCauContExist)
                || Objects.isNull(convCauContExist.getIdConvocatoriaCauCont())) {

            CatalogosValores causaCont = this.catalogosValoresDAO.find(idCausaCont);

            if (Objects.nonNull(causaCont)) {
                convCauCont.setCausaCont(causaCont);
                convCauCont.setIdConvocatoria(idConvocatoria);
                convCauCont = this.insertOrUpdate(convCauCont);
            }

        } else if (Objects.nonNull(convCauContExist.getFcBajaFila())) {
            convCauContExist.setCdUsuBaja(null);
            convCauContExist.setFcBajaFila(null);
            convCauCont = this.insertOrUpdate(convCauContExist);
        } else if (Objects.isNull(convCauContExist.getFcBajaFila())) {
            throw new ServiceException(Constantes.Error.ERROR_CAUSA_DENEGACION_DUPLICADA, null,
                    Constantes.Error.MSG_WARNING);
        }

        return ConvocatoriaCauContToDTO.INSTANCE.apply(convCauCont);
    }

    /**
     * Verifica la existencia de un causa de Contuacion
     *
     * @param idConvocatoria
     * @param idCausaCont
     * @return
     * @throws ServiceException
     */
    private ConvocatoriaCauCont buscarIdConvocatoriaCau(final Integer idConvocatoria,
            final Integer idCausaCont) throws ServiceException {

        ConvocatoriaCauCont convocatoriaCauCont = new ConvocatoriaCauCont();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION_CONT,
                idCausaCont, Operador.EQUALS));

        List<ConvocatoriaCauCont> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCauCont = lstConvocatoriaCau.get(0);
        }
        return convocatoriaCauCont;
    }
    
    /**
    *
    */
    @Override
    public List<ConvocatoriaCauCont> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(this.find(filtros, null));
    }
}
