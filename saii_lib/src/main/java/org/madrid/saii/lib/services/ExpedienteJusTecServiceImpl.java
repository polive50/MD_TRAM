package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJustecDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJustecDocToEntity;
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

/**
 * @author everis
 */
@Service("expedienteJusTecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusTecServiceImpl
        extends BaseServiceImpl<ExpedienteJusTec, Integer, ExpedienteJusTecDAO>
        implements ExpedienteJusTecService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteJusTecDAO
     */
    @Autowired
    public ExpedienteJusTecServiceImpl(@Qualifier("expedienteJusTecDAO")
    ExpedienteJusTecDAO expedienteJusTecDAO) {
        super();
        this.setDao(expedienteJusTecDAO);
    }

    @Autowired
    @Qualifier("expedienteJusTecHistDAO")
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    @Autowired
    @Qualifier("expedienteJustecDocDAO")
    private ExpedienteJustecDocDAO expedienteJustecDocDAO;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     *
     */
    @Override
    public List<ExpedienteJusTecDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteJusTecToDTO.INSTANCE));
    }

    /**
     * Método para insertar o actualizar los datos editados de los registros
     * ExpedienteJusTec, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteJusTecDTO guardar(ExpedienteJusTecDTO expedienteJusTecDTO)
            throws ServiceException {

        ExpedienteJusTec expedienteJusTec = this
                .insertOrUpdate(ExpedienteJusTecToEntity.INSTANCE.apply(expedienteJusTecDTO));

        this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

        return ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTec);
    }

    /**
     * Método para buscar ExpedienteJusTecDTO por el id de expediente
     * 
     * @param idExpediente id del expediente
     */
    @Override
    public List<ExpedienteJusTecDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusTecToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    public List<ExpedienteJustecDocDTO> buscarDocs(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {

        return new ArrayList<>(
                Lists.transform(this.expedienteJustecDocDAO.find(index, pageSize, filtros, orden),
                        ExpedienteJustecDocToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public void guardarDocumentoJustec(ExpedienteJustecDocDTO expedienteJustecDocDTO) {
        this.expedienteJustecDocDAO
                .insertOrUpdate(ExpedienteJustecDocToEntity.INSTANCE.apply(expedienteJustecDocDTO));
    }

    /**
     * Método para buscar ExpedienteJusTecDTO por el id de expediente
     * 
     * @param idExpediente id del expediente
     */
    @Override
    public List<ExpedienteJusTecDTO> buscarPeriodosPendientesJustecPorExpediente(
            Integer idExpediente, Integer idEstado) throws ServiceException {
        Date currentDate = new Date();
        List<AtlasFilter> filtros = new ArrayList<>();

        if (idExpediente != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                    Operador.EQUALS));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_INICIO_JT, currentDate,
                Operador.LESS_OR_EQUAL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_JT, currentDate,
                Operador.GREATER_OR_EQUAL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, idEstado, Operador.EQUALS));

        // añadir estado no presentada
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        List<ExpedienteJusTec> result = this.find(0, -1, filtros, ordenamiento);
        return new ArrayList<>(Lists.transform(result, ExpedienteJusTecToDTO.INSTANCE));
    }

    @Override
    public ExpedienteJusTecDTO buscarPorIdExpedienteJustec(Integer idExpedienteJustec)
            throws ServiceException {

        return ExpedienteJusTecToDTO.INSTANCE.apply(this.find(idExpedienteJustec));
    }

    @Override
    public ExpedienteJusTec buscarIdExpJustec(Integer idExpedienteJustec) throws ServiceException {

        return this.find(idExpedienteJustec);
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteJusTecDTO update(ExpedienteJusTecDTO expedienteJusTecDTO)
            throws ServiceException {

        ExpedienteJusTec expedienteJusTec = this.find(expedienteJusTecDTO.getIdExpedienteJusTec());
        expedienteJusTec.setIdEstado(
                CatalogosValoresToEntity.INSTANCE.apply(expedienteJusTecDTO.getIdEstadoDTO()));
        expedienteJusTec.setIdEstadoRevAdmin(CatalogosValoresToEntity.INSTANCE
                .apply(expedienteJusTecDTO.getIdEstadoRevAdminDTO()));
        expedienteJusTec
                .setTlObservacionDelCiudadano(expedienteJusTecDTO.getTlObservacionDelCiudadano());
        expedienteJusTec = this.insertOrUpdate(expedienteJusTec);

        this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

        return ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTec);
    }

    @Override
    public List<ExpedienteJusTecDTO> buscarPeriodosPendientesSubsanarJustecPorExpediente(
            Integer idExpediente) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_REV_ADMIN,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        List<ExpedienteJusTec> result = this.find(0, -1, filtros, ordenamiento);
        return new ArrayList<>(Lists.transform(result, ExpedienteJusTecToDTO.INSTANCE));
    }

    @Override
    public List<ExpedienteJusTecDTO> buscarByIdExp(Integer idExpediente) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusTecToDTO.INSTANCE));
    }

    /**
     * Método para insertar o actualizar los datos editados de los registros
     * ExpedienteJusTec, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteJusTecDTO actualizarIdEstado(ExpedienteJusTecDTO expedienteJustecDTOAux)
            throws ServiceException {

        ExpedienteJusTec expedienteJusTec = this
                .insertOrUpdate(ExpedienteJusTecToEntity.INSTANCE.apply(expedienteJustecDTOAux));

        this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

        return ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTec);
    }

    /**
     * Método para buscar ExpedienteJusTecDTO por el id de expediente
     * 
     * @param idExpediente id del expediente
     */
    @Override
    public List<ExpedienteJusTecDTO> buscarPeriodosFinalizadoPlazoJustecPorExpediente(
            Integer idExpediente, Integer idEstado) throws ServiceException {
        Date currentDate = new Date();
        List<AtlasFilter> filtros = new ArrayList<>();

        if (idExpediente != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                    Operador.EQUALS));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_JT, currentDate,
                Operador.LESS_THAN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, idEstado, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        List<ExpedienteJusTec> result = this.find(0, -1, filtros, ordenamiento);
        return new ArrayList<>(Lists.transform(result, ExpedienteJusTecToDTO.INSTANCE));
    }

    /**
     * Actualiza desde el batch el estado de los expdtes justec segun sus
     * estados previos y fechas de JCT
     *
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public void procesarPlazosJCT() throws ServiceException {

        // iniciarPlazoJCT
        List<ExpedienteJusTecDTO> lstExpdteJusTecDTO = this
                .buscarPeriodosPendientesJustecPorExpediente(null,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_PRESENTACION));

        if (lstExpdteJusTecDTO != null && !lstExpdteJusTecDTO.isEmpty()) {

            for (ExpedienteJusTecDTO expedienteJustecDTOAux : lstExpdteJusTecDTO) {
                // actualizamos el estado de los expdtes justec pdtes de
                // presentacion a inicio plazo presentacion
                expedienteJustecDTOAux.setIdEstadoDTO(
                        new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.INICIO_PLAZO_DE_PRESENTACION)));

                ExpedienteJusTec expedienteJusTec = ExpedienteJusTecToEntity.INSTANCE
                        .apply(expedienteJustecDTOAux);
                expedienteJusTec.setFcModifFila(new Date());
                expedienteJusTec.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                ExpedienteJusTec mergedExpedienteJusTec = this.getDao().merge(expedienteJusTec);
                this.expedienteJusTecHistDAO.crearHistorico(mergedExpedienteJusTec);
            }
        }

        // finalizarPlazoJCT
        List<ExpedienteJusTecDTO> lstExpdteJusTecFinDTO = this
                .buscarPeriodosFinalizadoPlazoJustecPorExpediente(null,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.INICIO_PLAZO_DE_PRESENTACION));

        if (lstExpdteJusTecFinDTO != null && !lstExpdteJusTecFinDTO.isEmpty()) {

            for (ExpedienteJusTecDTO expedienteJustecDTOAux : lstExpdteJusTecFinDTO) {
                // actualizamos el estado de los expdtes justec en inicio plazo
                // presentacion a finalizado plazo presentacion
                expedienteJustecDTOAux.setIdEstadoDTO(
                        new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.FINALIZADO_PLAZO_PRESENTACION)));

                ExpedienteJusTec expedienteJusTec = ExpedienteJusTecToEntity.INSTANCE
                        .apply(expedienteJustecDTOAux);
                expedienteJusTec.setFcModifFila(new Date());
                expedienteJusTec.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                ExpedienteJusTec mergedExpedienteJusTec = this.getDao().merge(expedienteJusTec);
                this.expedienteJusTecHistDAO.crearHistorico(mergedExpedienteJusTec);
            }
        }

        // procesar JCTs no presentadas
        List<ExpedienteJusTecDTO> lstExpdteJusTecNPDTO = this
                .buscarPeriodosFinalizadoPlazoJustecPorExpediente(null,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.RECLAMADA_JUSTIFICACION));

        if (lstExpdteJusTecNPDTO != null && !lstExpdteJusTecNPDTO.isEmpty()) {

            for (ExpedienteJusTecDTO expedienteJustecDTOAux : lstExpdteJusTecNPDTO) {
                // actualizamos el estado de los expdtes justec en reclamada
                // justificacion con fecha vencida a no presentada
                expedienteJustecDTOAux.setIdEstadoDTO(
                        new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.NO_PRESENTADA)));

                ExpedienteJusTec expedienteJusTec = ExpedienteJusTecToEntity.INSTANCE
                        .apply(expedienteJustecDTOAux);
                expedienteJusTec.setFcModifFila(new Date());
                expedienteJusTec.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                ExpedienteJusTec mergedExpedienteJusTec = this.getDao().merge(expedienteJusTec);
                this.expedienteJusTecHistDAO.crearHistorico(mergedExpedienteJusTec);
            }
        }
    }

    /**
     * Busca los periodos de JCT en estado pdtes. de evaluacion
     *
     */
    @Override
    public List<ExpedienteJusTecDTO> buscarJustecPdteEvaluacion() throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        List<ExpedienteJusTec> result = this.find(0, -1, filtros, ordenamiento);
        return new ArrayList<>(Lists.transform(result, ExpedienteJusTecToDTO.INSTANCE));
    }

    
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idExpedienteJustec) throws ServiceException {
        this.deleteLogico(idExpedienteJustec);
    }
    
    
    @Override
    public List<ExpedienteJusTecDTO> obtenerExpedientesJustecByIdExp(Integer idExpediente) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        List<AtlasSort> orden = new ArrayList<>();

        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));
        
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));

        return this.buscar(0, -1, filtrosEntrada, orden);

    }
}
