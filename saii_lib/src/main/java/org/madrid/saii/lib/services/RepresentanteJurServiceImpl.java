package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.RepresentanteJurDAO;
import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.RepresentanteJurFiltrosDTO;
import org.madrid.saii.lib.dto.converters.RepresentanteJurToDTO;
import org.madrid.saii.lib.dto.converters.RepresentanteJurToEntity;
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
import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio RepresentanteJur
 *
 * @author everis
 */
@Service("representanteJurService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class RepresentanteJurServiceImpl
        extends BaseServiceImpl<RepresentanteJur, Integer, RepresentanteJurDAO>
        implements RepresentanteJurService {

    /**
     * Servicio de catálogo de valores
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

    /**
     * Servicio de Entidad
     */
    @Autowired
    @Qualifier("entidadService")
    private EntidadService entidadService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param representanteJurDAO
     */
    @Autowired
    public RepresentanteJurServiceImpl(@Qualifier("representanteJurDAO")
    RepresentanteJurDAO representanteJurDAO) {
        super();
        this.setDao(representanteJurDAO);
    }

    /**
     *
     */
    @Override
    public RepresentanteJurDTO buscarId(Integer idRepresentanteJur) throws ServiceException {
        return RepresentanteJurToDTO.INSTANCE.apply(this.find(idRepresentanteJur));
    }

    /**
     *
     */
    @Override
    public List<RepresentanteJurDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                RepresentanteJurToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RepresentanteJurDTO guardar(RepresentanteJurDTO representanteJurDTO)
            throws ServiceException {

        String dniAuditoria = representanteJurDTO.getInteresadoDTO().getCdNumIden();

        if (representanteJurDTO.getInteresadoDTO().getIdInteresado() == null) {
            representanteJurDTO.getInteresadoDTO()
                    .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.FISICA));
            representanteJurDTO.getInteresadoDTO().setCdUsuAlta(dniAuditoria);
            representanteJurDTO.getInteresadoDTO().setFcAltaFila(new Date());
            InteresadoDTO interesadoDTO = this.interesadoService.guardarPersona(
                    representanteJurDTO.getInteresadoDTO(),
                    representanteJurDTO.getInteresadoDTO().getCdNumIden(), false);
            representanteJurDTO.getInteresadoDTO().setIdInteresado(interesadoDTO.getIdInteresado());
        }

        if (representanteJurDTO.getEstadoDTO().getIdValor() == null) {
            representanteJurDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoRepresentantesLegales.PENDIENTE)));
            representanteJurDTO.getEstadoDTO().setNmConstante(Constantes.EstadoRepresentantesLegales.PENDIENTE);
        }

        if (representanteJurDTO.getIdRepresentanteJur() == null) {
            representanteJurDTO.setCdUsuAlta(dniAuditoria);
            representanteJurDTO.setFcAltaFila(new Date());
        } else {
            representanteJurDTO.setCdUsuModif(dniAuditoria);
            representanteJurDTO.setFcModifFila(new Date());
        }

        representanteJurDTO = RepresentanteJurToDTO.INSTANCE.apply(
                this.insertOrUpdate(RepresentanteJurToEntity.INSTANCE.apply(representanteJurDTO)));

        representanteJurDTO.setEstadoDTO(this.catalogosValoresService
                .buscarId(representanteJurDTO.getEstadoDTO().getIdValor()));

        return representanteJurDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RepresentanteJurDTO registrar(RepresentanteJurDTO representanteJurDTO)
            throws ServiceException {

        RepresentanteJurDTO dto = new RepresentanteJurDTO();
        String dniAuditoria = representanteJurDTO.getInteresadoDTO().getCdNumIden();

        // Comprobamos si el interesado existe
        InteresadoDTO interesadoDTO = this.interesadoService
                .obtenerPorIdentificacion(representanteJurDTO.getInteresadoDTO().getCdNumIden());

        if (interesadoDTO.getIdInteresado() == null) {
            // No existe, lo creamos
            representanteJurDTO.getInteresadoDTO().getTipoActorDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA));
            representanteJurDTO.getInteresadoDTO().getTipoActorDTO().setNmConstante(Constantes.Identificacion.FISICA);
            representanteJurDTO.getInteresadoDTO().setCdUsuAlta(dniAuditoria);
            representanteJurDTO.getInteresadoDTO().setFcAltaFila(new Date());
            interesadoDTO = this.interesadoService.guardarPersona(
                    representanteJurDTO.getInteresadoDTO(),
                    representanteJurDTO.getInteresadoDTO().getCdNumIden(), false);
        }

        dto.getInteresadoDTO().setIdInteresado(interesadoDTO.getIdInteresado());

        // Comprobamos si la entidad existe
        EntidadDTO entidadDTO = this.entidadService
                .buscarCif(representanteJurDTO.getEntidadDTO().getInteresadoDTO().getCdNumIden());

        if (entidadDTO.getIdEntidad() == null) {
            // No existe, creamos la entidad.
            // El interesado se comprueba y crea al guardar la entidad
            representanteJurDTO.getEntidadDTO()
                    .setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEntidad.PENDIENTE)));
            representanteJurDTO.getEntidadDTO().getEstadoDTO().setNmConstante(Constantes.EstadoEntidad.PENDIENTE);
            representanteJurDTO.getEntidadDTO().setCdUsuAlta(dniAuditoria);
            representanteJurDTO.getEntidadDTO().setFcAltaFila(new Date());
            entidadDTO = this.entidadService.guardar(representanteJurDTO.getEntidadDTO());
        }

        dto.getEntidadDTO().setIdEntidad(entidadDTO.getIdEntidad());

        // Comprobamos si existe la relación empresa-interesado
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO,
                dto.getInteresadoDTO().getIdInteresado(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ENTIDAD_INTERESADO_IDINTERESADO,
                entidadDTO.getInteresadoDTO().getIdInteresado(), Operador.EQUALS));

        List<RepresentanteJurDTO> lstRepresentanteJurDTO = this.buscar(0, -1, filtros, null);

        if (Boolean.TRUE.equals(lstRepresentanteJurDTO.isEmpty())) {
            // Está en blanco, creamos al representante jurídico

            // Asignamos el cdNumIden del representante para la auditoría
            dto.getInteresadoDTO().setCdNumIden(dniAuditoria);
            return this.guardar(dto);
        } else {
            return lstRepresentanteJurDTO.get(0);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RepresentanteJurDTO borradoLogico(final RepresentanteJurDTO representanteJurDTO)
            throws ServiceException {
        representanteJurDTO.setCdUsuBaja(representanteJurDTO.getInteresadoDTO().getCdNumIden());
        representanteJurDTO.setFcBajaFila(new Date());
        return RepresentanteJurToDTO.INSTANCE
                .apply(this.update(RepresentanteJurToEntity.INSTANCE.apply(representanteJurDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RepresentanteJurDTO altaLogica(final RepresentanteJurDTO representanteJurDTO)
            throws ServiceException {
        representanteJurDTO.setCdUsuBaja(null);
        representanteJurDTO.setFcBajaFila(null);
        return RepresentanteJurToDTO.INSTANCE
                .apply(this.update(RepresentanteJurToEntity.INSTANCE.apply(representanteJurDTO)));
    }

    /**
     *
     */
    @Override
    public List<RepresentanteJurDTO> filtrar(RepresentanteJurFiltrosDTO usuarioJurFiltrosDTO)
            throws ServiceException {

        List<RepresentanteJurDTO> lst;

        try {
            List<RepresentanteJur> lista = this.getDao().filtrar(usuarioJurFiltrosDTO);

            lst = new ArrayList<>(Lists.transform(lista, RepresentanteJurToDTO.INSTANCE));

        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

        return lst;
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerEstadoRepresentanteLegal() throws ServiceException {
        // Obtenemos el valor del catálogo
        return this.catalogosValoresService
                .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_REPRESENTANTE_LEGAL));
    }

    /**
     *
     */
    @Override
    public RepresentanteJurDTO obtenerPorInteresado(String dniRepresentante, String cifEmpresa)
            throws ServiceException {

        RepresentanteJurDTO dto = new RepresentanteJurDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        if (dniRepresentante != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_CDNUMIDEN,
                    dniRepresentante, Operador.EQUALS));
        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ENTIDAD_CDNUMIDEN, cifEmpresa,
                Operador.EQUALS));

        List<RepresentanteJurDTO> lstRepresentanteJurDTO = this.buscar(0, -1, filtros, null);

        if (!lstRepresentanteJurDTO.isEmpty()) {
            dto = lstRepresentanteJurDTO.get(0);
        }

        return dto;
    }

    @Override
    public List<RepresentanteJurDTO> obtenerEntidadesPorInteresado(String dniRepresentante,
            String cifEmpresa) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_CDNUMIDEN, dniRepresentante,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ENTIDAD_CDNUMIDEN, cifEmpresa,
                Operador.EQUALS));

        List<RepresentanteJurDTO> lstRepresentanteJurDTO = this.buscar(0, -1, filtros, null);

        return lstRepresentanteJurDTO;
    }

}
