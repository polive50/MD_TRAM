package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.EntidadDAO;
import org.madrid.saii.lib.domain.Entidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.converters.EntidadToDTO;
import org.madrid.saii.lib.dto.converters.EntidadToEntity;
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

/**
 * Implementación del servicio para la clase de dominio Entidad
 *
 * @author everis
 */
@Service("entidadService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EntidadServiceImpl extends BaseServiceImpl<Entidad, Integer, EntidadDAO>
        implements EntidadService {

    /**
     * Servicio de CatalogoValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Servicio de Interesado
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param entidadDAO
     */
    @Autowired
    public EntidadServiceImpl(@Qualifier("entidadDAO")
    EntidadDAO entidadDAO) {
        super();
        this.setDao(entidadDAO);
    }

    /**
     *
     */
    @Override
    public List<EntidadDTO> obtener() throws ServiceException {
        return new ArrayList<>(Lists.transform(this.getDao().buscarTodos(), EntidadToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public EntidadDTO buscarId(Integer idEntidad) throws ServiceException {
        return EntidadToDTO.INSTANCE.apply(this.getDao().find(idEntidad));
    }

    /**
     *
     */
    @Override
    public List<EntidadDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), EntidadToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public EntidadDTO guardar(EntidadDTO entidadDTO) throws ServiceException {

        if (entidadDTO.getInteresadoDTO().getIdInteresado() == null) {
            entidadDTO.getInteresadoDTO().setTipoActorDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.JURIDICA)));
            entidadDTO.getInteresadoDTO().getTipoActorDTO().setNmConstante(Constantes.Identificacion.JURIDICA);
            entidadDTO.getInteresadoDTO()
                    .setCdUsuAlta(entidadDTO.getInteresadoDTO().getCdNumIden());
            entidadDTO.getInteresadoDTO().setFcAltaFila(new Date());
            entidadDTO.getInteresadoDTO()
                    .setTipoIdentificacionDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIF)));
            entidadDTO.getInteresadoDTO().getTipoIdentificacionDTO().setNmConstante(Constantes.TipoIdentificacion.NIF);
            InteresadoDTO interesadoDTO = this.interesadoService.guardarPersona(
                    entidadDTO.getInteresadoDTO(), entidadDTO.getInteresadoDTO().getCdNumIden(),
                    false);
            entidadDTO.getInteresadoDTO().setIdInteresado(interesadoDTO.getIdInteresado());
        }

        if (entidadDTO.getEstadoDTO().getIdValor() == null) {
            entidadDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEntidad.PENDIENTE)));
            entidadDTO.getEstadoDTO().setNmConstante(Constantes.EstadoEntidad.PENDIENTE);
        }

        if (entidadDTO.getTipoEntidadDTO() != null
                && entidadDTO.getTipoEntidadDTO().getIdValor() == null) {
            entidadDTO.setTipoEntidadDTO(null);
        }

        entidadDTO.setCdUsuAlta(entidadDTO.getInteresadoDTO().getCdNumIden());
        entidadDTO.setFcAltaFila(new Date());

        entidadDTO = EntidadToDTO.INSTANCE
                .apply(this.insertOrUpdate(EntidadToEntity.INSTANCE.apply(entidadDTO)));

        entidadDTO.setEstadoDTO(
                this.catalogosValoresService.buscarId(entidadDTO.getEstadoDTO().getIdValor()));

        return entidadDTO;
    }

    /**
     *
     */
    @Override
    public void altaLogica(final EntidadDTO entidadDTO) throws ServiceException {
        entidadDTO.setCdUsuBaja(null);
        entidadDTO.setFcBajaFila(null);
        this.getDao().update(EntidadToEntity.INSTANCE.apply(entidadDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final EntidadDTO entidadDTO) throws ServiceException {
        entidadDTO.setCdUsuBaja(UserUtils.getUserName());
        entidadDTO.setFcBajaFila(new Date());
        this.getDao().update(EntidadToEntity.INSTANCE.apply(entidadDTO));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaEstadosEntidad() throws ServiceException {
        // Obtenemos el valor del catálogo
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_ENTIDAD));
    }

    @Override
    public List<CatalogosValoresDTO> obtenerListaTipoEntidad() throws ServiceException {
        // Obtenemos el valor del catálogo
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ENTIDAD));
    }

    /**
     *
     */
    @Override
    public EntidadDTO buscarCif(String cif) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_CDNUMIDEN, cif,
                Operador.EQUALS));
        List<Entidad> lstEntidades = this.getDao().find(filtros, null);

        if (lstEntidades.isEmpty()) {
            return new EntidadDTO();
        } else {
            return EntidadToDTO.INSTANCE.apply(lstEntidades.get(0));
        }

    }

    @Override
    public EntidadDTO buscarEntidadInteresado(Integer idInteresado) {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        List<Entidad> lstEntidades = this.getDao().find(filtros, null);

        if (lstEntidades.isEmpty()) {
            return new EntidadDTO();
        } else {
            return EntidadToDTO.INSTANCE.apply(lstEntidades.get(0));
        }

    }

}
