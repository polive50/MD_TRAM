package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusEcoDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusEcoHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusEcoToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusEcoToEntity;
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
 * @author quental
 */
@Service("convocatoriaJusEcoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaJusEcoServiceImpl
        extends BaseServiceImpl<ConvocatoriaJusEco, Integer, ConvocatoriaJusEcoDAO>
        implements ConvocatoriaJusEcoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public ConvocatoriaJusEcoServiceImpl(@Qualifier("convocatoriaJusEcoDAO")
    ConvocatoriaJusEcoDAO convocatoriaJusEcoDAO) {
        super();
        this.setDao(convocatoriaJusEcoDAO);
    }

    @Autowired
    @Qualifier("convocatoriaJusEcoHistDAO")
    private ConvocatoriaJusEcoHistDAO convocatoriaJusEcoHistDAO;

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteJusEcoDAO")
    private ExpedienteJusEcoDAO expedienteJusEcoDAO;

    @Autowired
    @Qualifier("expedienteJusEcoHistDAO")
    private ExpedienteJusEcoHistDAO expedienteJusEcoHistDAO;

    @Autowired
    @Qualifier("convocatoriaJusEcoDAO")
    private ConvocatoriaJusEcoDAO convocatoriaJusEcoDAO;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Metodo para realizar la busqueda de todos los registros
     * ConvocatoriaJusEco relacionados con la convocatoria
     */
    @Override
    public List<ConvocatoriaJusEcoDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaJusEcoToDTO.INSTANCE));
    }

    /**
     * Metodo para insertar o actualizar los datos editados de los registros
     * ConvocatoriaJusEco, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaJusEcoDTO guardar(ConvocatoriaJusEcoDTO convocatoriaJusEcoDTO)
            throws ServiceException {

        ConvocatoriaJusEco convocatoriaJusEco = this
                .insertOrUpdate(ConvocatoriaJusEcoToEntity.INSTANCE.apply(convocatoriaJusEcoDTO));

        this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEco);

        this.copiarJustificacionFila(convocatoriaJusEcoDTO);

        return ConvocatoriaJusEcoToDTO.INSTANCE.apply(convocatoriaJusEco);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void cargarDatosJusEco(Integer idConvocatoria, Integer nmPeriodo)
            throws ServiceException {

        // controlamos que el numero de periodos no sea nulo ni cero para
        // insertar datos
        if (nmPeriodo != null && nmPeriodo != 0) {

            // Insertamos los registros tipo INTERMEDIO, reservando un registro
            // para
            // el tipo FINAL
            for (int i = 1; i < nmPeriodo; i++) {

                ConvocatoriaJusEco convocatoriaJusEco = new ConvocatoriaJusEco();
                convocatoriaJusEco.setIdConvocatoria(idConvocatoria);
                convocatoriaJusEco.setTipoConvocatoriaJusEco(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.INTERMEDIA)));
                convocatoriaJusEco.setNmPeriodo(i);
                convocatoriaJusEco = this.insert(convocatoriaJusEco);
                this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEco);

            }

            // Insertarmos el registro tipo FINAL
            ConvocatoriaJusEco convocatoriaJusEco = new ConvocatoriaJusEco();
            convocatoriaJusEco.setIdConvocatoria(idConvocatoria);
            convocatoriaJusEco.setTipoConvocatoriaJusEco(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.FINAL)));
            convocatoriaJusEco.setNmPeriodo(nmPeriodo);
            convocatoriaJusEco = this.insert(convocatoriaJusEco);
            this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEco);

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

                List<ConvocatoriaJusEco> lstConvocatoriaJusEco = this.convocatoriaJusEcoDAO
                        .find(filtros, null);

                for (ConvocatoriaJusEco convocatoriasJusEco : lstConvocatoriaJusEco) {

                    ExpedienteJusEco expedienteJusEco = new ExpedienteJusEco();
                    expedienteJusEco.setIdExpediente(expediente.getIdExpediente());
                    expedienteJusEco.setNmPeriodo(convocatoriasJusEco.getNmPeriodo());
                    expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                            convocatoriasJusEco.getTipoConvocatoriaJusEco().getIdValor()));
                    expedienteJusEco
                            .setFcIniJustiEconomica(convocatoriasJusEco.getFcIniJustiEconomica());
                    expedienteJusEco
                            .setFcFinJustiEconomica(convocatoriasJusEco.getFcFinJustiEconomica());
                    expedienteJusEco.setTlObservacion(convocatoriasJusEco.getTlObservacion());

                    this.expedienteJusEcoDAO.insert(expedienteJusEco);
                    this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusEcoDTO
     */
    private void copiarJustificacionFila(ConvocatoriaJusEcoDTO convocatoriaJusEcoDTO) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusEcoDTO.getIdConvocatoria(), Operador.EQUALS));
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
                        convocatoriaJusEcoDTO.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusEco> lstExpedienteJusEco = this.expedienteJusEcoDAO.find(filtros,
                        null);

                if (lstExpedienteJusEco.isEmpty()) {

                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                            solicitud.getConvocatoria().getIdConvocatoria(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    List<ConvocatoriaJusEco> lstConvocatoriaJusEco = this.convocatoriaJusEcoDAO
                            .find(filtros, null);

                    for (ConvocatoriaJusEco convocatoriasJusEco : lstConvocatoriaJusEco) {

                        ExpedienteJusEco expedienteJusEco = new ExpedienteJusEco();
                        expedienteJusEco.setIdExpediente(expediente.getIdExpediente());
                        expedienteJusEco.setNmPeriodo(convocatoriasJusEco.getNmPeriodo());
                        expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                                convocatoriasJusEco.getTipoConvocatoriaJusEco().getIdValor()));
                        expedienteJusEco.setFcIniJustiEconomica(
                                convocatoriasJusEco.getFcIniJustiEconomica());
                        expedienteJusEco.setFcFinJustiEconomica(
                                convocatoriasJusEco.getFcFinJustiEconomica());
                        expedienteJusEco.setTlObservacion(convocatoriasJusEco.getTlObservacion());

                        this.expedienteJusEcoDAO.insert(expedienteJusEco);
                        this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

                    }

                } else {

                    for (ExpedienteJusEco expedienteJusEco : lstExpedienteJusEco) {

                        expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                                convocatoriaJusEcoDTO.getTipoConvocatoriaJusEcoDTO().getIdValor()));
                        expedienteJusEco.setNmPeriodo(convocatoriaJusEcoDTO.getNmPeriodo());
                        expedienteJusEco.setFcIniJustiEconomica(
                                convocatoriaJusEcoDTO.getFcIniJustiEconomica());
                        expedienteJusEco.setFcFinJustiEconomica(
                                convocatoriaJusEcoDTO.getFcFinJustiEconomica());
                        expedienteJusEco.setTlObservacion(convocatoriaJusEcoDTO.getTlObservacion());

                        this.expedienteJusEcoDAO.update(expedienteJusEco);
                        this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

                    }

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusEco
     */
    private void copiarJustificacionConvExp(ConvocatoriaJusEco convocatoriaJusEco) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusEco.getIdConvocatoria(), Operador.EQUALS));
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
                        convocatoriaJusEco.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusEco> lstExpedienteJusEco = this.expedienteJusEcoDAO.find(filtros,
                        null);

                for (ExpedienteJusEco expedienteJusEco : lstExpedienteJusEco) {

                    expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                            convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor()));
                    expedienteJusEco.setNmPeriodo(convocatoriaJusEco.getNmPeriodo());
                    expedienteJusEco
                            .setFcIniJustiEconomica(convocatoriaJusEco.getFcIniJustiEconomica());
                    expedienteJusEco
                            .setFcFinJustiEconomica(convocatoriaJusEco.getFcFinJustiEconomica());
                    expedienteJusEco.setTlObservacion(convocatoriaJusEco.getTlObservacion());

                    this.expedienteJusEcoDAO.update(expedienteJusEco);
                    this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaJusEco
     */
    private void copiarBajaJustificacionConvExp(ConvocatoriaJusEco convocatoriaJusEco) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusEco.getIdConvocatoria(), Operador.EQUALS));
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
                        convocatoriaJusEco.getNmPeriodo(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteJusEco> lstExpedienteJusEco = this.expedienteJusEcoDAO.find(filtros,
                        null);

                for (ExpedienteJusEco expedienteJusEco : lstExpedienteJusEco) {

                    this.expedienteJusEcoDAO.deleteLogico(expedienteJusEco.getIdExpedienteJusEco());
                    ExpedienteJusEco expedienteJusEcoI = this.expedienteJusEcoDAO
                            .find(expedienteJusEco.getIdExpedienteJusEco());

                    this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEcoI);

                }

            }

        }
    }

    /**
     *
     * @param convocatoriaJusEco
     */
    private void insertarJustificacionConvExp(ConvocatoriaJusEco convocatoriaJusEco) {

        // Replicamos el cambio a los expedientes de coordinador del mismo
        // periodo
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaJusEco.getIdConvocatoria(), Operador.EQUALS));
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

                ExpedienteJusEco expedienteJusEco = new ExpedienteJusEco();
                expedienteJusEco.setIdExpediente(expediente.getIdExpediente());
                expedienteJusEco.setNmPeriodo(convocatoriaJusEco.getNmPeriodo());
                expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                        convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor()));
                expedienteJusEco
                        .setFcIniJustiEconomica(convocatoriaJusEco.getFcIniJustiEconomica());
                expedienteJusEco
                        .setFcFinJustiEconomica(convocatoriaJusEco.getFcFinJustiEconomica());
                expedienteJusEco.setTlObservacion(convocatoriaJusEco.getTlObservacion());

                this.expedienteJusEcoDAO.insert(expedienteJusEco);
                this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

            }

        }

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaJusEcoDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ConvocatoriaJusEcoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void actualizarDatosJusEco(Integer idConvocatoria, Integer nmPeriodo,
            int cantidadRegistrosCargados) throws ServiceException {

        List<ConvocatoriaJusEcoDTO> lista = this.buscarPorConvocatoria(idConvocatoria);
        
        if (!lista.isEmpty()) {
            
            Integer idTipJusInt = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.INTERMEDIA);
            Integer idTipJusFin = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.FINAL);
            
            if (nmPeriodo > cantidadRegistrosCargados) {

                // actualizamos el ultimo registro como Intermedio
                ConvocatoriaJusEco convocatoriaJusEco = this
                        .find(lista.get(lista.size() - 1).getIdConvocatoriaJusEco());
                convocatoriaJusEco.setTipoConvocatoriaJusEco(new CatalogosValores(idTipJusInt));
                convocatoriaJusEco = this.update(convocatoriaJusEco);
                this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEco);

                this.copiarJustificacionConvExp(convocatoriaJusEco);

                // agregamos los nuevos registros reservando uno para Final

                for (int i = cantidadRegistrosCargados + 1; i < nmPeriodo; i++) {

                    ConvocatoriaJusEco convocatoriaJusEcoI = new ConvocatoriaJusEco();
                    convocatoriaJusEcoI.setIdConvocatoria(idConvocatoria);
                    convocatoriaJusEcoI.setTipoConvocatoriaJusEco(new CatalogosValores(idTipJusInt));
                    convocatoriaJusEcoI.setNmPeriodo(i);
                    convocatoriaJusEcoI = this.insert(convocatoriaJusEcoI);
                    this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEcoI);

                    this.insertarJustificacionConvExp(convocatoriaJusEcoI);

                }

                // agregamos el nuevo registro Final
                ConvocatoriaJusEco convocatoriaJusEcoF = new ConvocatoriaJusEco();
                convocatoriaJusEcoF.setIdConvocatoria(idConvocatoria);
                convocatoriaJusEcoF.setTipoConvocatoriaJusEco(new CatalogosValores(idTipJusFin));
                convocatoriaJusEcoF.setNmPeriodo(nmPeriodo);
                convocatoriaJusEcoF = this.insert(convocatoriaJusEcoF);
                this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEcoF);

                this.insertarJustificacionConvExp(convocatoriaJusEcoF);

            } else if (nmPeriodo < cantidadRegistrosCargados) {

                // calculamos los registros sobrante
                int sobrantes = cantidadRegistrosCargados - nmPeriodo;

                // verificamos si tenemos la cantidad de registros sobrantes
                // para eliminar
                if (sobrantes >= lista.size()) {

                    // eliminamos todos los registros
                    for (int x = 0; x < cantidadRegistrosCargados; x++) {

                        this.deleteLogico(lista.get(x).getIdConvocatoriaJusEco());
                        ConvocatoriaJusEco convocatoriaJusEco = this
                                .find(lista.get(x).getIdConvocatoriaJusEco());
                        this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEco);

                        this.copiarBajaJustificacionConvExp(convocatoriaJusEco);

                    }

                } else {

                    // eliminamos los registros sobrantes
                    for (int x = 0; x < sobrantes; x++) {

                        this.deleteLogico(
                                lista.get(lista.size() - (1 + x)).getIdConvocatoriaJusEco());
                        ConvocatoriaJusEco convocatoriaJusEcoI = this
                                .find(lista.get(lista.size() - (1 + x)).getIdConvocatoriaJusEco());
                        this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEcoI);

                        this.copiarBajaJustificacionConvExp(convocatoriaJusEcoI);

                    }

                    // actualizamos el ultimo registro activo como FINAL
                    ConvocatoriaJusEco convocatoriaJusEcoF = this.find(
                            lista.get(lista.size() - (1 + sobrantes)).getIdConvocatoriaJusEco());
                    convocatoriaJusEcoF.setTipoConvocatoriaJusEco(new CatalogosValores(idTipJusFin));
                    convocatoriaJusEcoF = this.update(convocatoriaJusEcoF);
                    this.convocatoriaJusEcoHistDAO.crearHistorico(convocatoriaJusEcoF);

                    this.copiarJustificacionConvExp(convocatoriaJusEcoF);

                }

            }

        }

    }

    @Override
    public List<ConvocatoriaJusEco> buscarPeriodos(ConvocatoriaDTO dto) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
                dto.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_FCINIJUSTIECONOMICA, Operador.NOT_NULL));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_FCFINJUSTIECONOMICA, Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.find(filtros, null);
    }

}
