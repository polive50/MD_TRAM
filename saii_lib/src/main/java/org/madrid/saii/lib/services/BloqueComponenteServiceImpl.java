package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.BloqueComponenteDAO;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio Componente
 *
 * @author everis
 */
@Service("bloqueComponenteService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class BloqueComponenteServiceImpl
        extends BaseServiceImpl<BloqueComponente, Integer, BloqueComponenteDAO>
        implements BloqueComponenteService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param componenteDAO
     */
    @Autowired
    public BloqueComponenteServiceImpl(
            @Qualifier("bloqueComponenteDAO") BloqueComponenteDAO bloqueComponenteDAO) {
        super();
        this.setDao(bloqueComponenteDAO);
    }

    @Autowired
    @Qualifier("componenteDAO")
    private ComponenteDAO componenteDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    @Lazy
    private ComponenteService componenteService;

    /**
     *
     */
    @Override
    public BloqueComponenteDTO buscarId(Integer idBloqueComponente) throws ServiceException {
        BloqueComponente componente = this.find(idBloqueComponente);
        return BloqueComponenteToDTO.INSTANCE.apply(componente);
    }

    /**
     *
     */
    @Override
    public List<BloqueComponenteDTO> buscarIdBloque(Integer idBloque) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_BLOQUE, idBloque, Operador.EQUALS));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueComponenteToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public BloqueComponenteDTO buscarPorBloqueComponente(final Integer idBloque,
            final Integer idComponente, Integer idTramite) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_BLOQUE, idBloque, Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.ID_COMPONENTE, idComponente, Operador.EQUALS));

        if (Objects.nonNull(idTramite)) {
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        }

        List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueComponenteToDTO.INSTANCE));

        if (lstBloqueComponente.isEmpty()) {
            return new BloqueComponenteDTO();
        } else {
            return lstBloqueComponente.get(0);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public BloqueComponenteDTO guardar(final BloqueComponenteDTO bloqueComponenteDTO)
            throws ServiceException {
        BloqueComponente bloqueComponente = this
                .insertOrUpdate(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));
        return BloqueComponenteToDTO.INSTANCE.apply(bloqueComponente);
    }

    /**
     *
     */
    @Override
    public List<BloqueComponenteDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                BloqueComponenteToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public Boolean existeComponenteEnBloque(final Integer idComponente) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter(Constantes.Filtro.ID_COMPONENTE, idComponente, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueComponenteToDTO.INSTANCE));

        // true si se está utilizando
        return Boolean.FALSE.equals(lstBloqueComponente.isEmpty());
    }

    /**
     * Verificar si existe un tipo de documento en alta asociado a por lo menos
     * un bloque componente
     */
    @Override
    public Boolean existePorTipoDocumento(Integer idTipoDocumento) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUE_COMPONENTE_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueComponenteToDTO.INSTANCE));

        // true: si existe
        return Boolean.FALSE.equals(lstBloqueComponente.isEmpty());

    }

    /**
     *
     */
    @Override
    public List<BloqueComponenteDTO> obtenerPorExpediente(Integer idExpediente,
            Integer idFormatoBloque) throws ServiceException {
        return this.getDao().obtenerPorExpediente(idExpediente, idFormatoBloque);
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public void guardarFilaColumnaFinanciacion(FinanciacionDTO financiacionDTO, BloqueDTO bloqueDTO)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        List<AtlasSort> orden = new ArrayList<>();

        // grabar Filas
        List<BloqueComponente> filas = this.getDao().generarFilasBloqueComponenteFinanciacion(
                bloqueDTO.getIdBloque(), financiacionDTO.getIdFinanciacion());

        for (BloqueComponente item : filas) {
            this.insert(item);
        }

        // grabar columnas
        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATEGORIA_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.CategoriasFormulario.FINANCIACION), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.ID_COMP,
                    utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.CODIGO_ACTUACION),
                    utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA), Operador.BETWEEN));
        } else {
            filtros.add(new AtlasFilter(Constantes.Filtro.ID_COMP,
                    utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.IMPORTE), Operador.EQUALS));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        orden.add(new AtlasSort(Constantes.Filtro.ID_COMP, true));

        List<Componente> listaComponente = this.componenteDAO.find(0, -1, filtros, orden);

        int i = 1;

        for (Componente componente : listaComponente) {

            BloqueComponente bloque = new BloqueComponente();

            bloque.setBloque(new Bloque());
            bloque.getBloque().setIdBloque(bloqueDTO.getIdBloque());
            bloque.setNmOrden(i);
            bloque.setItObligatorio(Constantes.AfirmacionNegacion.S);
            bloque.setItRelacion(Constantes.AfirmacionNegacion.N);
            bloque.setTipoFinanciacion(null);
            bloque.setItFila(Constantes.AfirmacionNegacion.N);
            bloque.setItColumna(Constantes.AfirmacionNegacion.S);
            bloque.setComponente(new Componente());
            bloque.getComponente().setIdComponente(componente.getIdComponente());
            bloque.getComponente().setNmConstante(componente.getNmConstante());
            
            this.insert(bloque);

            i++;
        }

    }

    @Override
    @Transactional(readOnly = false)
    public void guardarFilaColumnaPlazosEjecucionAnualidades(
            List<ConvocatoriaAnualidadDTO> lstConvocatoriaAnualidadDTO, BloqueDTO bloqueDTO)
            throws ServiceException {

        // grabar filas
        List<BloqueComponente> filas = this
                .generarFilasPlazosEjecucionAnualidades(lstConvocatoriaAnualidadDTO, bloqueDTO);
        for (BloqueComponente item : filas) {
            this.insert(item);
        }
        
        //VMM 15/01/25 MD-1025: comentamos esto porque en saii_solicitud no necesitan estos componentes
        //para las columnas, solo necesitan las anualidades para las filas
        // grabar columnas
        /*List<AtlasFilter> filtros = new ArrayList<>();
        List<AtlasSort> orden = new ArrayList<>();
        List<Integer> lstComponentes = new ArrayList<>();
        lstComponentes.add(Constantes.ComponentesFinanciacion.FECHA_INICIO);
        lstComponentes.add(Constantes.ComponentesFinanciacion.FECHA_FIN);
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_COMP, lstComponentes, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        orden.add(new AtlasSort(Constantes.Filtro.ID_COMP, true));
        List<Componente> listaComponente = this.componenteDAO.find(0, -1, filtros, orden);
        int i = 1;
        for (Componente componente : listaComponente) {
            BloqueComponente bloqueComponente = new BloqueComponente();
            bloqueComponente.setBloque(new Bloque());
            bloqueComponente.getBloque().setIdBloque(bloqueDTO.getIdBloque());
            bloqueComponente.setNmOrden(i);
            bloqueComponente.setItObligatorio(Constantes.AfirmacionNegacion.S);
            bloqueComponente.setItRelacion(Constantes.AfirmacionNegacion.N);
            bloqueComponente.setTipoFinanciacion(null);
            bloqueComponente.setItFila(Constantes.AfirmacionNegacion.N);
            bloqueComponente.setItColumna(Constantes.AfirmacionNegacion.S);
            bloqueComponente.setComponente(new Componente());
            bloqueComponente.getComponente().setIdComponente(componente.getIdComponente());
            this.insert(bloqueComponente);
            i++;
        }*/

    }

    private List<BloqueComponente> generarFilasPlazosEjecucionAnualidades(
            List<ConvocatoriaAnualidadDTO> lstConvocatoriaAnualidadDTO, BloqueDTO bloqueDTO) {
        List<BloqueComponente> lst = new ArrayList<>();
        for (ConvocatoriaAnualidadDTO convocatoriaAnualidadDTO : lstConvocatoriaAnualidadDTO) {
            BloqueComponente temp = new BloqueComponente();
            temp.setBloque(new Bloque());
            temp.getBloque().setIdBloque(bloqueDTO.getIdBloque());
            temp.setNmOrden(convocatoriaAnualidadDTO.getNmAnualidad());
            temp.setItObligatorio(Constantes.AfirmacionNegacion.S);
            temp.setItRelacion(Constantes.AfirmacionNegacion.N);
            temp.setTipoFinanciacion(null);
            Componente com = new Componente(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.ANUALIDAD));
            com.setNmConstante(Constantes.ComponentesFinanciacion.ANUALIDAD);
            temp.setComponente(com);
            temp.setItFila(Constantes.AfirmacionNegacion.S);
            temp.setItColumna(Constantes.AfirmacionNegacion.N);
            lst.add(temp);
        }
        return lst;
    }
    
    @Override
    public List<BloqueComponenteDTO> buscarIdBloqueFechBaja(Integer idBloque) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_BLOQUE, idBloque, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueComponenteToDTO.INSTANCE));
    }
    
}
