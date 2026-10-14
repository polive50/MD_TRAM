package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusTecDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusTecToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusTecToEntity;
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
@Service("convocatoriaJusTecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaJusTecServiceImpl
        extends BaseServiceImpl<ConvocatoriaJusTec, Integer, ConvocatoriaJusTecDAO>
        implements ConvocatoriaJusTecService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public ConvocatoriaJusTecServiceImpl(@Qualifier("convocatoriaJusTecDAO")
    ConvocatoriaJusTecDAO convocatoriaJusTecDAO) {
        super();
        this.setDao(convocatoriaJusTecDAO);
    }

    @Autowired
    @Qualifier("convocatoriaJusTecHistDAO")
    private ConvocatoriaJusTecHistDAO convocatoriaJusTecHistDAO;

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteJusTecDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;

    @Autowired
    @Qualifier("expedienteJusTecHistDAO")
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    @Autowired
    @Qualifier("convocatoriaJusTecDAO")
    private ConvocatoriaJusTecDAO convocatoriaJusTecDAO;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Metodo para realizar la busqueda de todos los registros
     * ConvocatoriaJusTec relacionados con la convocatoria
     */
    @Override
    public List<ConvocatoriaJusTecDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaJusTecToDTO.INSTANCE));
    }

    /**
     * Metodo para insertar o actualizar los datos editados de los registros
     * ConvocatoriaJusTec, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaJusTecDTO guardar(ConvocatoriaJusTecDTO convocatoriaJusTecDTO)
            throws ServiceException {

        ConvocatoriaJusTec convocatoriaJusTec = this
                .insertOrUpdate(ConvocatoriaJusTecToEntity.INSTANCE.apply(convocatoriaJusTecDTO));

        this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTec);

        this.copiarJustificacionFila(convocatoriaJusTecDTO);

        return ConvocatoriaJusTecToDTO.INSTANCE.apply(convocatoriaJusTec);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void cargarDatosJusTec(Integer idConvocatoria, Integer nmPeriodo)
            throws ServiceException {

        // controlamos que el numero de periodos no sea nulo ni cero para
        // insertar datos
        if (nmPeriodo != null && nmPeriodo != 0) {

            // Insertamos los registros tipo INTERMEDIO, reservando un registro
            // para
            // el tipo FINAL
            for (int i = 1; i < nmPeriodo; i++) {

                ConvocatoriaJusTec convocatoriaJusTec = new ConvocatoriaJusTec();
                convocatoriaJusTec.setIdConvocatoria(idConvocatoria);
                convocatoriaJusTec.setTipoConvocatoriaJusTec(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA)));
                convocatoriaJusTec.setNmPeriodo(i);
                convocatoriaJusTec = this.insert(convocatoriaJusTec);
                this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTec);

            }

            // Insertarmos el registro tipo FINAL
            ConvocatoriaJusTec convocatoriaJusTec = new ConvocatoriaJusTec();
            convocatoriaJusTec.setIdConvocatoria(idConvocatoria);
            convocatoriaJusTec
                    .setTipoConvocatoriaJusTec(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL)));
            convocatoriaJusTec.setNmPeriodo(nmPeriodo);
            convocatoriaJusTec = this.insert(convocatoriaJusTec);
            this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTec);

            // Copiamos las justificaciones a los expedientes tipo coordinador
            this.copiarJustificacionesTotal(idConvocatoria);
        }

    }

    /**
     *
     * @param idConvocatoria
     */
    private void copiarJustificacionesTotal(Integer idConvocatoria) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> lstExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente expediente : lstExpedientes) {

                filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                        solicitud.getConvocatoria().getIdConvocatoria(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ConvocatoriaJusTec> lstConvocatoriaJusTec = this.convocatoriaJusTecDAO
                        .find(filtros, null);

                for (ConvocatoriaJusTec convocatoriasJusTec : lstConvocatoriaJusTec) {

                    ExpedienteJusTec expedienteJusTec = new ExpedienteJusTec();
                    expedienteJusTec.setIdExpediente(expediente.getIdExpediente());
                    expedienteJusTec.setNmPeriodo(convocatoriasJusTec.getNmPeriodo());
                    expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                            convocatoriasJusTec.getTipoConvocatoriaJusTec().getIdValor()));
                    expedienteJusTec
                            .setFcIniJustiCienTec(convocatoriasJusTec.getFcIniJustiCienTec());
                    expedienteJusTec
                            .setFcFinJustiCienTec(convocatoriasJusTec.getFcFinJustiCienTec());
                    expedienteJusTec.setTlObservacion(convocatoriasJusTec.getTlObservacion());

                    this.expedienteJusTecDAO.insert(expedienteJusTec);
                    this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusTecDTO
     */
    private void copiarJustificacionFila(ConvocatoriaJusTecDTO convocatoriaJusTecDTO) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusTecDTO.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> lstExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente expediente : lstExpedientes) {

                filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        expediente.getIdExpediente(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMPERIODO,
                        convocatoriaJusTecDTO.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusTec> lstExpedienteJusTec = this.expedienteJusTecDAO.find(filtros,
                        null);

                if (lstExpedienteJusTec.isEmpty()) {

                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                            solicitud.getConvocatoria().getIdConvocatoria(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    List<ConvocatoriaJusTec> lstConvocatoriaJusTec = this.convocatoriaJusTecDAO
                            .find(filtros, null);

                    for (ConvocatoriaJusTec convocatoriasJusTec : lstConvocatoriaJusTec) {

                        ExpedienteJusTec expedienteJusTec = new ExpedienteJusTec();
                        expedienteJusTec.setIdExpediente(expediente.getIdExpediente());
                        expedienteJusTec.setNmPeriodo(convocatoriasJusTec.getNmPeriodo());
                        expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                                convocatoriasJusTec.getTipoConvocatoriaJusTec().getIdValor()));
                        expedienteJusTec
                                .setFcIniJustiCienTec(convocatoriasJusTec.getFcIniJustiCienTec());
                        expedienteJusTec
                                .setFcFinJustiCienTec(convocatoriasJusTec.getFcFinJustiCienTec());
                        expedienteJusTec.setTlObservacion(convocatoriasJusTec.getTlObservacion());

                        this.expedienteJusTecDAO.insert(expedienteJusTec);
                        this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

                    }

                } else {

                    for (ExpedienteJusTec expedienteJusTec : lstExpedienteJusTec) {

                        expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                                convocatoriaJusTecDTO.getTipoConvocatoriaJusTecDTO().getIdValor()));
                        expedienteJusTec.setNmPeriodo(convocatoriaJusTecDTO.getNmPeriodo());
                        expedienteJusTec
                                .setFcIniJustiCienTec(convocatoriaJusTecDTO.getFcIniJustiCienTec());
                        expedienteJusTec
                                .setFcFinJustiCienTec(convocatoriaJusTecDTO.getFcFinJustiCienTec());
                        expedienteJusTec.setTlObservacion(convocatoriaJusTecDTO.getTlObservacion());

                        this.expedienteJusTecDAO.update(expedienteJusTec);
                        this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

                    }

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusTec
     */
    private void copiarJustificacionConvExp(ConvocatoriaJusTec convocatoriaJusTec) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusTec.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> lstExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente expediente : lstExpedientes) {

                filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        expediente.getIdExpediente(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMPERIODO,
                        convocatoriaJusTec.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusTec> lstExpedienteJusTec = this.expedienteJusTecDAO.find(filtros,
                        null);

                for (ExpedienteJusTec expedienteJusTec : lstExpedienteJusTec) {

                    expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                            convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor()));
                    expedienteJusTec.setNmPeriodo(convocatoriaJusTec.getNmPeriodo());
                    expedienteJusTec
                            .setFcIniJustiCienTec(convocatoriaJusTec.getFcIniJustiCienTec());
                    expedienteJusTec
                            .setFcFinJustiCienTec(convocatoriaJusTec.getFcFinJustiCienTec());
                    expedienteJusTec.setTlObservacion(convocatoriaJusTec.getTlObservacion());

                    this.expedienteJusTecDAO.update(expedienteJusTec);
                    this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusTec
     */
    private void copiarBajaJustificacionConvExp(ConvocatoriaJusTec convocatoriaJusTec) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusTec.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> lstExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente expediente : lstExpedientes) {

                filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        expediente.getIdExpediente(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMPERIODO,
                        convocatoriaJusTec.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusTec> lstExpedienteJusTec = this.expedienteJusTecDAO.find(filtros,
                        null);

                for (ExpedienteJusTec expedienteJusTec : lstExpedienteJusTec) {

                    this.expedienteJusTecDAO.deleteLogico(expedienteJusTec.getIdExpedienteJusTec());
                    ExpedienteJusTec expedienteJusTecI = this.expedienteJusTecDAO
                            .find(expedienteJusTec.getIdExpedienteJusTec());

                    this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTecI);

                }

            }

        }
    }

    /**
     *
     * @param convocatoriaJusTec
     */
    private void insertarJustificacionConvExp(ConvocatoriaJusTec convocatoriaJusTec) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusTec.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> lstExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente expediente : lstExpedientes) {

                ExpedienteJusTec expedienteJusTec = new ExpedienteJusTec();
                expedienteJusTec.setIdExpediente(expediente.getIdExpediente());
                expedienteJusTec.setNmPeriodo(convocatoriaJusTec.getNmPeriodo());
                expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                        convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor()));
                expedienteJusTec.setFcIniJustiCienTec(convocatoriaJusTec.getFcIniJustiCienTec());
                expedienteJusTec.setFcFinJustiCienTec(convocatoriaJusTec.getFcFinJustiCienTec());
                expedienteJusTec.setTlObservacion(convocatoriaJusTec.getTlObservacion());

                this.expedienteJusTecDAO.insert(expedienteJusTec);
                this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

            }

        }

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaJusTecDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ConvocatoriaJusTecToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void actualizarDatosJusTec(Integer idConvocatoria, Integer nmPeriodo,
            int cantidadRegistrosCargados) throws ServiceException {

        List<ConvocatoriaJusTecDTO> lista = this.buscarPorConvocatoria(idConvocatoria);

        if (!lista.isEmpty()) {
            
            Integer idTipJusInt = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA);
            Integer idTipJusFin = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL);
            
            if (nmPeriodo > cantidadRegistrosCargados) {

                // actualizamos el ultimo registro como Intermedio
                ConvocatoriaJusTec convocatoriaJusTec = this
                        .find(lista.get(lista.size() - 1).getIdConvocatoriaJusTec());
                convocatoriaJusTec.setTipoConvocatoriaJusTec(new CatalogosValores(idTipJusInt));
                convocatoriaJusTec = this.update(convocatoriaJusTec);
                this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTec);

                this.copiarJustificacionConvExp(convocatoriaJusTec);

                // agregamos los nuevos registros reservando uno para Final

                for (int i = cantidadRegistrosCargados + 1; i < nmPeriodo; i++) {

                    ConvocatoriaJusTec convocatoriaJusTecI = new ConvocatoriaJusTec();
                    convocatoriaJusTecI.setIdConvocatoria(idConvocatoria);
                    convocatoriaJusTecI.setTipoConvocatoriaJusTec(new CatalogosValores(idTipJusInt));
                    convocatoriaJusTecI.setNmPeriodo(i);
                    convocatoriaJusTecI = this.insert(convocatoriaJusTecI);
                    this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTecI);

                    this.insertarJustificacionConvExp(convocatoriaJusTecI);

                }

                // agregamos el nuevo registro Final
                ConvocatoriaJusTec convocatoriaJusTecF = new ConvocatoriaJusTec();
                convocatoriaJusTecF.setIdConvocatoria(idConvocatoria);
                convocatoriaJusTecF.setTipoConvocatoriaJusTec(new CatalogosValores(idTipJusFin));
                convocatoriaJusTecF.setNmPeriodo(nmPeriodo);
                convocatoriaJusTecF = this.insert(convocatoriaJusTecF);
                this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTecF);

                this.insertarJustificacionConvExp(convocatoriaJusTecF);

            } else if (nmPeriodo < cantidadRegistrosCargados) {

                // calculamos los registros sobrante
                int sobrantes = cantidadRegistrosCargados - nmPeriodo;

                // verificamos si tenemos la cantidad de registros sobrantes
                // para eliminar
                if (sobrantes >= lista.size()) {

                    // eliminamos todos los registros
                    for (int x = 0; x < cantidadRegistrosCargados; x++) {

                        this.deleteLogico(lista.get(x).getIdConvocatoriaJusTec());
                        ConvocatoriaJusTec convocatoriaJusTec = this
                                .find(lista.get(x).getIdConvocatoriaJusTec());
                        this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTec);

                        this.copiarBajaJustificacionConvExp(convocatoriaJusTec);

                    }

                } else {

                    // eliminamos los registros sobrantes
                    for (int x = 0; x < sobrantes; x++) {

                        this.deleteLogico(
                                lista.get(lista.size() - (1 + x)).getIdConvocatoriaJusTec());
                        ConvocatoriaJusTec convocatoriaJusTecI = this
                                .find(lista.get(lista.size() - (1 + x)).getIdConvocatoriaJusTec());
                        this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTecI);

                        this.copiarBajaJustificacionConvExp(convocatoriaJusTecI);

                    }

                    // actualizamos el ultimo registro activo como FINAL
                    ConvocatoriaJusTec convocatoriaJusTecF = this.find(
                            lista.get(lista.size() - (1 + sobrantes)).getIdConvocatoriaJusTec());
                    convocatoriaJusTecF.setTipoConvocatoriaJusTec(new CatalogosValores(idTipJusFin));
                    convocatoriaJusTecF = this.update(convocatoriaJusTecF);
                    this.convocatoriaJusTecHistDAO.crearHistorico(convocatoriaJusTecF);

                    this.copiarJustificacionConvExp(convocatoriaJusTecF);

                }

            }

        }

    }

    @Override
    public List<ConvocatoriaJusTec> buscarPeriodos(ConvocatoriaDTO dto) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
                dto.getIdConvocatoria(), Operador.EQUALS));
        // filtros.add(new
        // AtlasFilter(Constantes.Filtro.FILTRO_FCINIJUSTICIENTEC,
        // Operador.NOT_NULL));
        // filtros.add(new
        // AtlasFilter(Constantes.Filtro.FILTRO_FCFINJUSTICIENTEC,
        // Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.find(filtros, null);
    }

    @Override
    public ConvocatoriaJusTecDTO buscarPorIdConvocatoriaJustec(Integer idConvocatoriaJustec)
            throws ServiceException {

        return ConvocatoriaJusTecToDTO.INSTANCE.apply(this.find(idConvocatoriaJustec));

    }

}
