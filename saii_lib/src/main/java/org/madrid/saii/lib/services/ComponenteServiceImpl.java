package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.BloqueComponenteDAO;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ComponenteToDTO;
import org.madrid.saii.lib.dto.converters.ComponenteToEntity;
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
import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio Componente
 *
 * @author everis
 */
@Service("componenteService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ComponenteServiceImpl extends BaseServiceImpl<Componente, Integer, ComponenteDAO>
        implements ComponenteService {

    /** catalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * bloqueComponenteService
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

    /**
     * bloqueComponenteDAO
     */
    @Autowired
    @Qualifier("bloqueComponenteDAO")
    private BloqueComponenteDAO bloqueComponenteDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param componenteDAO
     */
    @Autowired
    public ComponenteServiceImpl(@Qualifier("componenteDAO") ComponenteDAO componenteDAO) {
        super();
        this.setDao(componenteDAO);
    }

    /**
     * Buscar id.
     *
     * @param idComponente the id Componente
     * @return the Componente DTO
     * @throws ServiceException the service exception
     */

    @Override
    public ComponenteDTO buscarId(Integer idComponente) throws ServiceException {

        Componente componente = this.find(idComponente);
        return ComponenteToDTO.INSTANCE.apply(componente);
    }

    /**
     * Guardar.
     *
     * @param ComponenteDTO the Catalogos DTO
     * @return the Catalogos DTO
     * @throws ServiceException the service exception
     */

    @Override
    @Transactional(readOnly = false)
    public ComponenteDTO guardar(final ComponenteDTO componenteDTO) throws ServiceException {

        // insertamos el componente
        Componente componente = this
                .insertOrUpdate(ComponenteToEntity.INSTANCE.apply(componenteDTO));
        return ComponenteToDTO.INSTANCE.apply(componente);
    }

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<ComponenteDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ComponenteToDTO.INSTANCE));

    }

    /**
     * Guardar Documento.
     *
     * @param Identificador de bloque
     * @param Identificador de tipo de documento
     * @return the Catalogos DTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ComponenteDTO guardarDocumento(final Integer idBloque,
            ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO,
            List<BloqueComponenteDTO> listaBloqueComponente) throws ServiceException {

        try {

            if (listaBloqueComponente == null) {
                listaBloqueComponente = new ArrayList<>();
            }

            CatalogosValoresDTO tipoDocumentoDTO = this.catalogosValoresService
                    .buscarId(convocatoriaTipoDocDTO.getTipoDocumentoDTO().getIdValor());
            CatalogosValores tipoDocumento = CatalogosValoresToEntity.INSTANCE
                    .apply(tipoDocumentoDTO);

            CatalogosValoresDTO tramiteDTO = this.catalogosValoresService
                    .buscarId(convocatoriaTipoDocDTO.getTramiteDTO().getIdValor());
            CatalogosValores tramite = CatalogosValoresToEntity.INSTANCE.apply(tramiteDTO);

            if (tipoDocumentoDTO.getFcBajaFila() == null) {

                // Buscar componente
                BloqueComponenteDTO bloqueComponenteDTO = new BloqueComponenteDTO();
                BloqueComponente bloqueComponente = new BloqueComponente();
                Componente componente = this.buscarPorTipoDocumento(
                        convocatoriaTipoDocDTO.getTipoDocumentoDTO().getIdValor());

                if (componente.getIdComponente() == null) {
                    componente.setTipoDocumento(tipoDocumento);
                    componente = this.insertOrUpdate(componente);
                } else {
                    bloqueComponenteDTO = this.bloqueComponenteService.buscarPorBloqueComponente(
                            idBloque, componente.getIdComponente(),
                            convocatoriaTipoDocDTO.getTramiteDTO().getIdValor());
                }

                // Buscar Bloque Componente
                Bloque bloque = new Bloque();
                bloque.setIdBloque(idBloque);

                if (bloqueComponenteDTO.getIdBloqueComponente() == null) {
                    int ordenBC = 1;
                    if (!listaBloqueComponente.isEmpty()) {
                        ordenBC = listaBloqueComponente.size() + 1;
                    }
                    bloqueComponente.setBloque(bloque);
                    bloqueComponente.setComponente(componente);
                    bloqueComponente.setNmOrden(ordenBC);

                    if (Boolean.TRUE.equals(convocatoriaTipoDocDTO.getItObligatorio())) {
                        bloqueComponente.setItObligatorio(Constantes.AfirmacionNegacion.S);
                    } else {
                        bloqueComponente.setItObligatorio(Constantes.AfirmacionNegacion.N);
                    }

                    bloqueComponente.setTramite(tramite);

                    this.bloqueComponenteDAO.insert(bloqueComponente);

                } else {
                    if (bloqueComponenteDTO.getFcBajaFila() != null) {
                        this.bloqueComponenteDAO
                                .revertirBaja(bloqueComponenteDTO.getIdBloqueComponente());

                    }
                }

                return ComponenteToDTO.INSTANCE.apply(componente);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /*
     *
     */
    @Override
    public Componente buscarPorTipoDocumento(final Integer idTipoDocumento)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));

        List<ComponenteDTO> lstComponente = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ComponenteToDTO.INSTANCE));

        Componente componente = new Componente();

        if (!lstComponente.isEmpty()) {
            componente = ComponenteToEntity.INSTANCE.apply(lstComponente.get(0));
        }
        return componente;
    }

//    @Override
//    public List<Componente> cargaNmConstante() throws ServiceException {
//        
//        List<AtlasFilter> filtros = new ArrayList<>();
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
//        return this.find(filtros, null);
//        
//    }

}
