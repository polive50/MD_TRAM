package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.DatoDAO;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.converters.DatoToDTO;
import org.madrid.saii.lib.dto.converters.DatoToEntity;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Dato
 *
 * @author everis
 */
@Slf4j
@Service("datoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class DatoServiceImpl extends BaseServiceImpl<Dato, Integer, DatoDAO>
        implements DatoService {

    @Autowired
    @Qualifier("bloqueComponenteService")
    BloqueComponenteService bloqueComponenteService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param datoDAO
     */
    @Autowired
    public DatoServiceImpl(@Qualifier("datoDAO") DatoDAO datoDAO) {
        super();
        this.setDao(datoDAO);
    }

    @Override
    @Transactional(readOnly = false)
    public DatoDTO guardar(DatoDTO datoDTO) throws ServiceException {
        Dato dato = this.insertOrUpdate(DatoToEntity.INSTANCE.apply(datoDTO));
        return DatoToDTO.INSTANCE.apply(dato);

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
    public List<DatoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), DatoToDTO.INSTANCE));
    }

    /**
     * Buscar id.
     *
     * @param idValor the id Datos
     * @return the Datos DTO
     * @throws ServiceException the service exception
     */
    @Override
    public DatoDTO buscarIdDatos(final Integer idDatos) throws ServiceException {
        return DatoToDTO.INSTANCE.apply(this.find(idDatos));
    }

    /**
     *
     */
    @Override
    public List<DatoDTO> obtenerPorExpediente(final Integer idExpediente,
            final Integer idComponenteBloque) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        if (Objects.nonNull(idComponenteBloque)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                    idComponenteBloque, Operador.EQUALS));
        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(filtros, null), DatoToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idDato) throws ServiceException {

        this.getDao().deleteLogico(idDato);

    }

    @Override
    @Transactional(readOnly = false)
    public void bajaDatosPorBloqueComponente(Integer idExpediente, Integer idBloque, Integer nmFila,
            String cdNumIden) throws ServiceException {

        List<BloqueComponenteDTO> lstBloqueComp = this.obtenerColumnasActuacion(idBloque);

        for (BloqueComponenteDTO bloqueComp : lstBloqueComp) {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                    bloqueComp.getIdBloqueComponente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NM_FILA, nmFila, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Dato> lstDatosActivos = this.find(filtros, null);

            log.info("ID_BLOQUE_COMPONTE =" + bloqueComp.getIdBloqueComponente());

            for (Dato dato : lstDatosActivos) {
                // Seteamos datos auditoria porque es llamado de MOVA
                dato.setFcBajaFila(new Date());
                dato.setCdUsuBaja(cdNumIden);
                this.getDao().deleteLogico(dato);
                log.info("ID_DATO = " + dato.getIdDato().toString());
            }
        }

    }

    private List<BloqueComponenteDTO> obtenerColumnasActuacion(Integer idBloque)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_BLOQUE, idBloque, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_COLUMNA, "S", Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.ID_COMPONENTE,
                utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_PROVEEDOR),
                utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE), Operador.BETWEEN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.bloqueComponenteService.buscar(0, -1, filtros, null);
    }

    @Override
    public Map<String, String> obtenerValoresDatos(Integer idExpediente,
            List<Integer> lstIdComponente, List<Integer> lstIdTipoFinanciacion)
            throws ServiceException {
        return this.getDao().obtenerValoresDatos(idExpediente, lstIdComponente,
                lstIdTipoFinanciacion);
    }
    
    @Override
    public List<DatoDTO> consultarDatosImpresoModifPorIdExpModif(Integer idExpediente, Integer idExpModifPresPlazoOtra,
            Integer idBloqueComponente, boolean ordenAsc) throws ServiceException {
        
           List<AtlasFilter> filtros = new ArrayList<>();
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                   idExpediente, Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                   idBloqueComponente, Operador.EQUALS));
           if (idExpModifPresPlazoOtra != null) {
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                       idExpModifPresPlazoOtra, Operador.EQUALS));
           }
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
           
           List<AtlasSort> orderBean = new ArrayList<>();
           if (ordenAsc) {
               orderBean.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_DATO, true));
               orderBean.add(new AtlasSort(Constantes.Filtro.FILTRO_NM_COLUMNA, true));
           }else {
               orderBean.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_DATO, false));
           }
           
           return this.buscar(0, -1, filtros, orderBean);
    }
    
    @Override
    public void rellenarDatosMultiregistro(ExpedienteDTO dto, BloqueDTO bloqueDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
        // Recuperamos los datos del expediente
        List<DatoDTO> lstDatoDTO = this.obtenerPorExpediente(dto.getIdExpediente(),
                bloqueComponenteDTO.getIdBloqueComponente());

        // Obtenemos los datos relativos a este bloque
        List<DatoDTO> datosBloqueComponente = lstDatoDTO.stream()
                .filter(x -> x.getIdBloqueComponente()
                        .equals(bloqueComponenteDTO.getIdBloqueComponente())
                        && x.getExpedienteDTO() != null && x.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente())
                        && Objects.nonNull(x.getNmFila()))
                .sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList());

        // Quitamos todos los registros que estan de baja
        datosBloqueComponente = datosBloqueComponente.stream()
                .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

        bloqueComponenteDTO.getDatos().add(new DatoDTO());

        // Si no lo hemos encontrado es porque no es un tipo de
        // dato que se guarde aquí. Lo ignoramos
        if (Boolean.FALSE.equals(datosBloqueComponente.isEmpty())) {

            List<DatoDTO> lstDatos = new ArrayList<>();
           
            // Obtenemos cuantas filas tiene el bloque tabla
            Integer nmFilasTabla = this.obtenerFilasTabla(bloqueDTO);
            // Obtenemos cuantas filas hay en el bloque
            Integer nmFilas = this.obtenerNmRegistrosBloque(bloqueDTO, nmFilasTabla);

            if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                    && Boolean.FALSE.equals(bloqueComponenteDTO.getItRelacion()) || 
                    Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) 
                   ) {
                 // Si es formulario y el componente no está relacionado
                 lstDatos.addAll(datosBloqueComponente);

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())) {
                // Es un listado, creamos tantos datos como filas tenga el listado

                for (Integer i = 0; i < nmFilas; i++) {
                    if(i< datosBloqueComponente.size()) {
                        lstDatos.add(datosBloqueComponente.get(i));
                    }
                }

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                    && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
                // Es una tabla, presupuesto presentado o ayuda
                // solicitada, creamos datos sólo de las columnas
                for (Integer i = 0; i < nmFilas; i++) {
                    if(i< datosBloqueComponente.size()) {
                        lstDatos.add(datosBloqueComponente.get(i));
                    }
                }
            }
            
            // Guardamos los datos en el bloqueComponente
            bloqueComponenteDTO.setDatos(lstDatos);
        }
    }
    
    
    @Override
    public Integer obtenerFilasTabla(BloqueDTO bloqueDTO) throws ServiceException {
        Integer nmFilas = 0;

        for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO.getLstBloqueComponenteDTO()) {
            // Si es fila, NO es columna, tampoco es uno de los prohibidos y no
            // está dado de baja
            if (Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())) {
                if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                        && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())
                        && Objects.nonNull(bloqueComponenteDTO.getTipoFinanciacionDTO())
                        && Objects
                                .nonNull(bloqueComponenteDTO.getTipoFinanciacionDTO().getIdValor())
                        && Objects.isNull(bloqueComponenteDTO.getFcBajaFila())) {
                    nmFilas++;
                }
            } else {
                if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                        && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())
                        && Boolean.TRUE.equals(this.devolverComponentePorTipo(bloqueComponenteDTO))
                        && Objects.isNull(bloqueComponenteDTO.getFcBajaFila())) {
                    nmFilas++;
                }
            }
        }
        return nmFilas;
    }
    
    /**
     * Método que filtra los componentes para Datos o MOVA Si el componente no
     * está en esta lista, no se guarda en Datos o devuelve a MOVA
     *
     * @param bloqueComponenteDTO
     * @return Boolean true -> se puede devolver o guardar
     * @throws ServiceException
     */
    private Boolean devolverComponentePorTipo(BloqueComponenteDTO bloqueComponenteDTO) {

        // Si el componente no es uno de los válidos, lo ignoramos
        List<Integer> componentesValidos = new ArrayList<>();
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO));
        if (Boolean.FALSE.equals(componentesValidos
                .contains(bloqueComponenteDTO.getComponenteDTO().getTipoDTO().getIdValor()))) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
    
    /**
     * Devuelve el número de registros para el bloque
     *
     * @param bloqueDTO
     * @param nmFilas
     * @return Integer
     * @throws ServiceException 
     */
    @Override
    public Integer obtenerNmRegistrosBloque(BloqueDTO bloqueDTO, Integer nmFilas) throws ServiceException {
        // Formato de bloque formulario / listado / tabla / presupuesto
        // presentado / ayuda solicitada
        Integer nmRegistros = 1;

        Integer idValor = bloqueDTO.getIdFormato().getIdValor();

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO).equals(idValor)) {
            nmRegistros = bloqueDTO.getNmFilas();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA).equals(idValor)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES).equals(idValor)) {
            nmRegistros = nmFilas;
        }
        return nmRegistros;
    }
    
    
    
    @Override
    public List<DatoDTO> buscarDatosByIdExpAndIdBloComp(Integer idExpediente, Integer idBloqueComponente, boolean ordenar) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                idBloqueComponente, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
        List<AtlasSort> orderBean = null;
        if (ordenar) {
            orderBean = new ArrayList<>();
            orderBean.add(new AtlasSort(Constantes.Filtro.FILTRO_NM_COLUMNA, true));
        }
        return this.buscar(0, -1, filtros, orderBean);
    }

    @Override
    public DatoDTO obtenerPorIdExpedienteYIdComponente(Integer idExpediente, Integer idComponente)
            throws ServiceException {
        DatoDTO datoDTO = null;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_COMPONENTE_IDCOMPONENTE,
                idComponente, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_FCBAJAFILA, Operador.NULL));
        List<DatoDTO> lstDatoDTO = this.buscar(0, -1, filtros, null);
        if (lstDatoDTO != null && !lstDatoDTO.isEmpty()) {
            datoDTO = lstDatoDTO.get(0);
        }
        return datoDTO;
    }
    
}
