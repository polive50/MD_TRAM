package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.converters.BloqueToDTO;
import org.madrid.saii.lib.dto.converters.BloqueToEntity;
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

/**
 * Implementación del servicio para la clase de dominio Bloque
 *
 * @author everis
 */
@Service("bloqueService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class BloqueServiceImpl extends BaseServiceImpl<Bloque, Integer, BloqueDAO>
        implements BloqueService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param bloqueDAO
     */
    @Autowired
    public BloqueServiceImpl(@Qualifier("bloqueDAO") BloqueDAO bloqueDAO) {
        super();
        this.setDao(bloqueDAO);
    }

    /**
     * Servicio ComponenteService
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;

    /**
     * Servicio BloqueComponenteService
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Guardar.
     *
     * @return the dto
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public BloqueDTO guardar(BloqueDTO bloqueDTO, ComponenteDTO componenteDTO)
            throws ServiceException {

        // Si la NmFilas viene a vacio seteamos 1
        if (bloqueDTO.getNmFilas() == null) {
            bloqueDTO.setNmFilas(1);
        }
       
        // insertamos el Bloque
        Bloque bloque = this.insertOrUpdate(BloqueToEntity.INSTANCE.apply(bloqueDTO));
        bloqueDTO = BloqueToDTO.INSTANCE.apply(bloque);

        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoTextoFijo())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoParticipantes())) {
            this.guardarNuevoBloqueUnComponente(bloque, bloqueDTO, componenteDTO);
        }

        return bloqueDTO;

    }
    
    @Override
    @Transactional(readOnly = false)
    public BloqueDTO actualizarBloquePadre(BloqueDTO bloqueDTO, ComponenteDTO componenteDTO)
            throws ServiceException {

        // insertamos el Bloque
        Bloque bloque = this.insertOrUpdate(BloqueToEntity.INSTANCE.apply(bloqueDTO));
        bloqueDTO = BloqueToDTO.INSTANCE.apply(bloque);
        
        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoTextoFijo())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoParticipantes())) {
            this.guardarNuevoBloqueUnComponente(bloque, bloqueDTO, componenteDTO);
        }

        return bloqueDTO;
    }
    
    /**
     * Guarda un bloque que tienen un solo componente.
     *
     * @param bloque
     * @param idBloque
     */
    private void guardarNuevoBloqueUnComponente(Bloque bloque, BloqueDTO bloqueDTO,
            ComponenteDTO componenteDTO) throws ServiceException {

        try {
            
            Integer idTipComCam = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO);
            Integer idSubTipComMulTex = utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO);
            
            if (Objects.isNull(bloque.getLstBloqueComponente())
                    || bloque.getLstBloqueComponente().isEmpty()) {

                // Ponemos a null los catalogos valores del componente
                componenteDTO.setCategoriaDTO(null);
                componenteDTO.setReglaDTO(null);
                componenteDTO.setTipoDocumentoDTO(null);
                componenteDTO.setTipoDTO(null);
                componenteDTO.setSubTipoDTO(null);
                componenteDTO.setSubTipoCatalogoDTO(null);

                // En bloque tipo texto ponemos a tipo "campo" y subtipo
                // "multitexto"
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) {
                    componenteDTO.setTipoDTO(new CatalogosValoresDTO(idTipComCam));
                    componenteDTO.setSubTipoDTO(new CatalogosValoresDTO(idSubTipComMulTex));
                }

                // Guardamos el componente
                componenteDTO = this.componenteService.guardar(componenteDTO);

                // Creamos un bloque componente para el componente en el
                // bloque
                List<BloqueComponenteDTO> lstBloqueComponenteDTO = this.bloqueComponenteService.buscarIdBloqueFechBaja(bloque.getIdBloque());
                
                if (!lstBloqueComponenteDTO.isEmpty()) {
                    for (BloqueComponenteDTO bloqueComponenteDTO : lstBloqueComponenteDTO) {
                        if (!lstBloqueComponenteDTO.isEmpty() &&
                                bloqueComponenteDTO.getIdBloqueComponente() == null) {
                            bloqueComponenteDTO.setIdBloque(bloque.getIdBloque());
                            bloqueComponenteDTO.setComponenteDTO(componenteDTO);
                            bloqueComponenteDTO.setNmOrden(1);
                            this.bloqueComponenteService.guardar(bloqueComponenteDTO);
                        }
                    }
                }else {
                    BloqueComponenteDTO bloqueComponenteDTOAux = new BloqueComponenteDTO();
                    bloqueComponenteDTOAux.setIdBloque(bloque.getIdBloque());
                    bloqueComponenteDTOAux.setComponenteDTO(componenteDTO);
                    bloqueComponenteDTOAux.setNmOrden(bloqueDTO.getNmOrden());
                    this.bloqueComponenteService.guardar(bloqueComponenteDTOAux);
                }

            } else {

                Componente componente = bloque.getLstBloqueComponente().get(0).getComponente();

                componente.setCategoria(null);
                componente.setRegla(null);
                componente.setTipoDocumento(null);
                componente.setTipo(null);
                componente.setSubTipo(null);
                componente.setSubTipoCatalogo(null);

                // En bloque tipo texto ponemos a tipo "campo" y subtipo
                // "multitexto"
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) {
                    componente.setTipo(new CatalogosValores(idTipComCam));
                    componente.setSubTipo(new CatalogosValores(idSubTipComMulTex));
                }

                // Si no es de tipo participante seteamos tambien estas dos
                // variables
                if (Boolean.FALSE.equals(bloqueDTO.getEsTipoParticipantes())) {
                    // componente.setTlTextoFijo(componenteDTO.getTlTextoFijo());
                    // MD-2469 [ADM] Ampliar campo texto fijo en formularios
                    componente.setClTextoFijo(componenteDTO.getClTextoFijo());
                    componente.setNmLongitud(componenteDTO.getNmLongitud());
                }

                // Actualizamos el componente
                this.componenteService.update(componente);

            }

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

    }
   
    /**
     * Comprobar Bloque.
     *
     * @param BloqueDTO the interesado DTO
     * @return the bloque DTO
     * @throws ServiceException the service exception
     */

    @Override
    public List<BloqueDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), BloqueToDTO.INSTANCE));

    }

    /**
     * Buscar id.
     *
     * @param idBloque the id Bloque
     * @return the Bloque DTO
     * @throws ServiceException the service exception
     */

    @Override
    public BloqueDTO buscarId(Integer idBloque) throws ServiceException {
        Bloque bloque = this.find(idBloque);
        return BloqueToDTO.INSTANCE.apply(bloque);
    }

    /**
     * Buscar id.
     *
     * @param idBloque the id Bloque
     * @return the Bloque DTO
     * @throws ServiceException the service exception
     */

    @Override
    public List<BloqueDTO> buscarPorFormato(Integer idImpreso, Integer tipo)
            throws ServiceException {
        if (Objects.isNull(idImpreso) || Objects.isNull(tipo)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("impreso.idImpreso", idImpreso, Operador.EQUALS));
        filtros.add(new AtlasFilter("idFormato.idValor", tipo, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), BloqueToDTO.INSTANCE));

    }
    
    @Override
    public List<BloqueDTO> buscarByIdImpreso(Integer idImpreso)
            throws ServiceException {
        if (Objects.isNull(idImpreso)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("impreso.idImpreso", idImpreso, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), BloqueToDTO.INSTANCE));

    }
    
    
    @Override
    @Transactional(readOnly = false)
    public void modificarBloque(BloqueDTO bloqueDTO)
            throws ServiceException {
        
        // Si la NmFilas viene a vacio seteamos 1
        if (bloqueDTO.getNmFilas() == null) {
            bloqueDTO.setNmFilas(1);
        }
       
        // insertamos el Bloque
        Bloque bloque = this.insertOrUpdate(BloqueToEntity.INSTANCE.apply(bloqueDTO));
        bloqueDTO = BloqueToDTO.INSTANCE.apply(bloque);

    }
    
    
    @Override
    public List<BloqueDTO> buscarByIdBloquePadre(Integer idBloquePadre)
            throws ServiceException {
        if (Objects.isNull(idBloquePadre)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("idBloquePadre", idBloquePadre, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMSUBORDEN, true));
        
        return this.buscar(0, -1, filtros, ordenamiento);

    }
    
    /**
    *
    */
   @Override
   public BloqueDTO buscarByIdBlqPad(final Integer idBloquePadre) throws ServiceException {
       return BloqueToDTO.INSTANCE.apply(this.find(idBloquePadre));
   }
  
}
