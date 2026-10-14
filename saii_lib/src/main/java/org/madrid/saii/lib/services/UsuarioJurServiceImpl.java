package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.UsuarioJurDAO;
import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.UsuarioJurDTO;
import org.madrid.saii.lib.dto.UsuarioJurFiltrosDTO;
import org.madrid.saii.lib.dto.converters.UsuarioJurToDTO;
import org.madrid.saii.lib.dto.converters.UsuarioJurToEntity;
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
import atlas.core.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio UsuarioJur
 *
 * @author everis
 */
@Service("usuarioJurService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class UsuarioJurServiceImpl extends BaseServiceImpl<UsuarioJur, Integer, UsuarioJurDAO>
        implements UsuarioJurService {

    /**
     * Servicio de Interesado
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
     * Servicio de RepresentanteJur
     */
    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService representanteJurService;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param usuarioJurDAO
     */
    @Autowired
    public UsuarioJurServiceImpl(@Qualifier("usuarioJurDAO") UsuarioJurDAO usuarioJurDAO) {
        super();
        this.setDao(usuarioJurDAO);
    }

    /**
     * Buscar por id UsuarioJur.
     *
     * @param idUsuarioJur the id UsuarioJur
     * @return UsuarioJurDTO
     * @throws ServiceException the service exception
     */
    @Override
    public UsuarioJurDTO buscarId(Integer idUsuarioJur) throws ServiceException {
        return UsuarioJurToDTO.INSTANCE.apply(this.find(idUsuarioJur));
    }

    /**
     * Buscar paginadamente UsuarioJur.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<UsuarioJurDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<UsuarioJurDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                UsuarioJurToDTO.INSTANCE));
    }

    /**
     * Guarda un/a usuarioJur.
     *
     * @param usuarioJurDTO the usuarioJur DTO
     * @return UsuarioJurDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioJurDTO guardar(UsuarioJurDTO usuarioJurDTO) throws ServiceException {
        return UsuarioJurToDTO.INSTANCE
                .apply(this.insertOrUpdate(UsuarioJurToEntity.INSTANCE.apply(usuarioJurDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioJurDTO borradoLogico(final UsuarioJurDTO usuarioJurDTO) throws ServiceException {
        usuarioJurDTO.setCdUsuBaja(UserUtils.getUserName());
        usuarioJurDTO.setFcBajaFila(new Date());
        return UsuarioJurToDTO.INSTANCE
                .apply(this.update(UsuarioJurToEntity.INSTANCE.apply(usuarioJurDTO)));
    }

    /**
     *
     */
    @Override
    public UsuarioJurDTO altaLogica(final UsuarioJurDTO usuarioJurDTO) throws ServiceException {
        usuarioJurDTO.setCdUsuBaja(null);
        usuarioJurDTO.setFcBajaFila(null);
        return UsuarioJurToDTO.INSTANCE
                .apply(this.update(UsuarioJurToEntity.INSTANCE.apply(usuarioJurDTO)));
    }

    /**
     *
     */
    @Override
    public List<UsuarioJurDTO> filtrar(UsuarioJurFiltrosDTO usuarioJurFiltrosDTO)
            throws ServiceException {

        List<UsuarioJurDTO> lst;

        try {
            lst = Lists.transform(this.getDao().filtrar(usuarioJurFiltrosDTO),
                    UsuarioJurToDTO.INSTANCE);

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
    @Transactional(readOnly = false)
    public UsuarioJurDTO registrarPersonaJur(UsuarioJurDTO usuarioJurDTO) throws ServiceException {

        String dniAuditoria = usuarioJurDTO.getInteresadoDTO().getCdNumIden();

        // Buscamos si la empresa existe
        InteresadoDTO empresaDTO = this.interesadoService
                .obtenerPorIdentificacion(usuarioJurDTO.getEntidadJurDTO().getCdNumIden());

        // Si no existe, ajustamos auditoría
        if (empresaDTO.getIdInteresado() == null) {
            usuarioJurDTO.getEntidadJurDTO().setCdUsuAlta(dniAuditoria);
            usuarioJurDTO.getEntidadJurDTO().setFcAltaFila(new Date());
        }

        // Buscamos si el representante existe
        InteresadoDTO representanteDTO = this.interesadoService
                .obtenerPorIdentificacion(usuarioJurDTO.getInteresadoDTO().getCdNumIden());

        // Si no existe, ajustamos auditoría
        if (representanteDTO.getIdInteresado() == null) {
            usuarioJurDTO.getInteresadoDTO().setCdUsuAlta(dniAuditoria);
            usuarioJurDTO.getInteresadoDTO().setFcAltaFila(new Date());
        }

        Boolean existeRelacion = Boolean.FALSE;

        if (empresaDTO.getIdInteresado() != null && representanteDTO.getIdInteresado() != null) {
            // Buscamos si el representante está asociado a la empresa
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ENTIDAD_IDINTERESADO,
                    empresaDTO.getIdInteresado(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO,
                    representanteDTO.getIdInteresado(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            existeRelacion = Boolean.FALSE
                    .equals(this.representanteJurService.buscar(0, -1, filtros, null).isEmpty());
        }

        // Si alguno de los interesados no existía, o la relación no existía, la
        // creamos
        if (Boolean.FALSE.equals(existeRelacion)) {
            RepresentanteJurDTO representanteJurDTO = new RepresentanteJurDTO();
            // representanteJurDTO.setEntidadJurDTO(usuarioJurDTO.getEntidadJurDTO());
            representanteJurDTO.setInteresadoDTO(usuarioJurDTO.getInteresadoDTO());
            representanteJurDTO.setCdUsuAlta(dniAuditoria);
            representanteJurDTO.setFcAltaFila(new Date());

            this.representanteJurService.guardar(representanteJurDTO);
        }

        return new UsuarioJurDTO();
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public UsuarioJurDTO crearPersonaJur(UsuarioJurDTO usuarioJurDTO) throws ServiceException {

        String dniAuditoria = usuarioJurDTO.getInteresadoDTO().getCdNumIden();

        // Creamos el interesado
        usuarioJurDTO.getInteresadoDTO()
                .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.JURIDICA));
        usuarioJurDTO.getInteresadoDTO().setCdUsuAlta(dniAuditoria);
        usuarioJurDTO.getInteresadoDTO().setFcAltaFila(new Date());
        InteresadoDTO interesadoDTO = this.interesadoService
                .guardarPersona(usuarioJurDTO.getInteresadoDTO(), dniAuditoria, false);
        usuarioJurDTO.getInteresadoDTO().setIdInteresado(interesadoDTO.getIdInteresado());

        // Se inserta la auditoría al usuario jurídico
        usuarioJurDTO.setCdUsuAlta(dniAuditoria);
        usuarioJurDTO.setFcAltaFila(new Date());

        return this.guardar(usuarioJurDTO);
    }

}
