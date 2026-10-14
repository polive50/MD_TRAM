package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAnualidadDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAnualidadHistDAO;
import org.madrid.saii.lib.dao.ExpedienteAnualidadDAO;
import org.madrid.saii.lib.dao.ExpedienteAnualidadHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAnualidadToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAnualidadToEntity;
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
 * @author Quental
 */
@Service("convocatoriaAnualidadService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaAnualidadServiceImpl
        extends BaseServiceImpl<ConvocatoriaAnualidad, Integer, ConvocatoriaAnualidadDAO>
        implements ConvocatoriaAnualidadService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaAnualidadDAO
     */
    @Autowired
    public ConvocatoriaAnualidadServiceImpl(@Qualifier("convocatoriaAnualidadDAO")
    ConvocatoriaAnualidadDAO convocatoriaAnualidadDAO) {
        super();
        this.setDao(convocatoriaAnualidadDAO);
    }

    @Autowired
    @Qualifier("convocatoriaAnualidadHistDAO")
    private ConvocatoriaAnualidadHistDAO convocatoriaAnualidadHistDAO;

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteAnualidadDAO")
    private ExpedienteAnualidadDAO expedienteAnualidadDAO;

    @Autowired
    @Qualifier("expedienteAnualidadHistDAO")
    private ExpedienteAnualidadHistDAO expedienteAnualidadHistDAO;

    @Autowired
    @Qualifier("convocatoriaAnualidadDAO")
    private ConvocatoriaAnualidadDAO convocatoriaAnualidadDAO;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Metodo para realizar la busqueda de todos los registros
     * ConvocatoriaAnualidad relacionados con la convocatoria
     */
    @Override
    public List<ConvocatoriaAnualidadDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaAnualidadToDTO.INSTANCE));
    }

    /**
     * Metodo para insertar o actualizar los datos editados de los registros
     * ConvocatoriaAnualidad, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaAnualidadDTO guardar(ConvocatoriaAnualidadDTO convocatoriaAnualidadDTO)
            throws ServiceException {

        ConvocatoriaAnualidad convocatoriaAnualidad = this.insertOrUpdate(
                ConvocatoriaAnualidadToEntity.INSTANCE.apply(convocatoriaAnualidadDTO));

        this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidad);

        this.copiarAnualidadFila(convocatoriaAnualidadDTO);

        return ConvocatoriaAnualidadToDTO.INSTANCE.apply(convocatoriaAnualidad);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void cargarDatosAnualidad(Integer idConvocatoria, Integer nmAnualidad)
            throws ServiceException {

        // controlamos que el numero de anualidades sea positivo para cargar
        // datos
        if (nmAnualidad != null && nmAnualidad != 0) {

            // Insertamos los registros tipo INTERMEDIO, reservando un registro
            // para
            // el tipo FINAL
            for (int i = 1; i < nmAnualidad; i++) {

                ConvocatoriaAnualidad convocatoriaAnualidad = new ConvocatoriaAnualidad();
                convocatoriaAnualidad.setIdConvocatoria(idConvocatoria);
                convocatoriaAnualidad.setTipoConvocatoriaAnualidad(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA)));
                convocatoriaAnualidad.setNmAnualidad(i);
                convocatoriaAnualidad = this.insert(convocatoriaAnualidad);
                this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidad);

            }

            // Insertarmos el registro tipo FINAL
            ConvocatoriaAnualidad convocatoriaAnualidad = new ConvocatoriaAnualidad();
            convocatoriaAnualidad.setIdConvocatoria(idConvocatoria);
            convocatoriaAnualidad
                    .setTipoConvocatoriaAnualidad(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL)));
            convocatoriaAnualidad.setNmAnualidad(nmAnualidad);
            convocatoriaAnualidad = this.insert(convocatoriaAnualidad);
            this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidad);

            // Copiamos las justificaciones a los expedientes tipo coordinador
            this.copiarAnualidadesTotal(idConvocatoria);
        }

    }

    /**
     *
     * @param idConvocatoria
     */
    private void copiarAnualidadesTotal(Integer idConvocatoria) {

        // Replicamos el cambio a los expedientes de coordinador de la misma
        // anualidad
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

                List<ConvocatoriaAnualidad> lstConvocatoriaAnualidad = this.convocatoriaAnualidadDAO
                        .find(filtros, null);

                for (ConvocatoriaAnualidad convocatoriasAnualidad : lstConvocatoriaAnualidad) {

                    ExpedienteAnualidad expedienteAnualidad = new ExpedienteAnualidad();
                    expedienteAnualidad.setIdExpediente(expediente.getIdExpediente());
                    expedienteAnualidad.setNmAnualidad(convocatoriasAnualidad.getNmAnualidad());
                    expedienteAnualidad.setTipoExpedienteAnualidad(new CatalogosValores(
                            convocatoriasAnualidad.getTipoConvocatoriaAnualidad().getIdValor()));
                    expedienteAnualidad
                            .setFcIniEjecucion(convocatoriasAnualidad.getFcIniEjecucion());
                    expedienteAnualidad
                            .setFcFinEjecucion(convocatoriasAnualidad.getFcFinEjecucion());
                    expedienteAnualidad.setTlObservacion(convocatoriasAnualidad.getTlObservacion());

                    this.expedienteAnualidadDAO.insert(expedienteAnualidad);
                    this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaAnualidadDTO
     */
    private void copiarAnualidadFila(ConvocatoriaAnualidadDTO convocatoriaAnualidadDTO) {

        // Replicamos el cambio a los expedientes de coordinador de la misma
        // anualidad
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaAnualidadDTO.getIdConvocatoria(), Operador.EQUALS));
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
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD,
                        convocatoriaAnualidadDTO.getNmAnualidad(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteAnualidad> lstExpedienteAnualidad = this.expedienteAnualidadDAO
                        .find(filtros, null);

                if (lstExpedienteAnualidad.isEmpty()) {

                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                            solicitud.getConvocatoria().getIdConvocatoria(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    List<ConvocatoriaAnualidad> lstConvocatoriaAnualidad = this.convocatoriaAnualidadDAO
                            .find(filtros, null);

                    for (ConvocatoriaAnualidad convocatoriasAnualidad : lstConvocatoriaAnualidad) {

                        ExpedienteAnualidad expedienteAnualidad = new ExpedienteAnualidad();
                        expedienteAnualidad.setIdExpediente(expediente.getIdExpediente());
                        expedienteAnualidad.setNmAnualidad(convocatoriasAnualidad.getNmAnualidad());
                        expedienteAnualidad.setTipoExpedienteAnualidad(
                                new CatalogosValores(convocatoriasAnualidad
                                        .getTipoConvocatoriaAnualidad().getIdValor()));
                        expedienteAnualidad
                                .setFcIniEjecucion(convocatoriasAnualidad.getFcIniEjecucion());
                        expedienteAnualidad
                                .setFcFinEjecucion(convocatoriasAnualidad.getFcFinEjecucion());
                        expedienteAnualidad
                                .setTlObservacion(convocatoriasAnualidad.getTlObservacion());

                        this.expedienteAnualidadDAO.insert(expedienteAnualidad);
                        this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

                    }

                } else {

                    for (ExpedienteAnualidad expedienteAnualidad : lstExpedienteAnualidad) {

                        expedienteAnualidad.setTipoExpedienteAnualidad(
                                new CatalogosValores(convocatoriaAnualidadDTO
                                        .getTipoConvocatoriaAnualidadDTO().getIdValor()));
                        expedienteAnualidad
                                .setNmAnualidad(convocatoriaAnualidadDTO.getNmAnualidad());
                        expedienteAnualidad
                                .setFcIniEjecucion(convocatoriaAnualidadDTO.getFcIniEjecucion());
                        expedienteAnualidad
                                .setFcFinEjecucion(convocatoriaAnualidadDTO.getFcFinEjecucion());
                        expedienteAnualidad
                                .setTlObservacion(convocatoriaAnualidadDTO.getTlObservacion());

                        this.expedienteAnualidadDAO.update(expedienteAnualidad);
                        this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

                    }

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaAnualidad
     */
    private void copiarAnualidadConvExp(ConvocatoriaAnualidad convocatoriaAnualidad) {

        // Replicamos el cambio a los expedientes de coordinador de la misma
        // anualidad
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaAnualidad.getIdConvocatoria(), Operador.EQUALS));
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
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD,
                        convocatoriaAnualidad.getNmAnualidad(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteAnualidad> lstExpedienteAnualidad = this.expedienteAnualidadDAO
                        .find(filtros, null);

                for (ExpedienteAnualidad expedienteAnualidad : lstExpedienteAnualidad) {

                    expedienteAnualidad.setTipoExpedienteAnualidad(new CatalogosValores(
                            convocatoriaAnualidad.getTipoConvocatoriaAnualidad().getIdValor()));
                    expedienteAnualidad.setNmAnualidad(convocatoriaAnualidad.getNmAnualidad());
                    expedienteAnualidad
                            .setFcIniEjecucion(convocatoriaAnualidad.getFcIniEjecucion());
                    expedienteAnualidad
                            .setFcFinEjecucion(convocatoriaAnualidad.getFcFinEjecucion());
                    expedienteAnualidad.setTlObservacion(convocatoriaAnualidad.getTlObservacion());

                    this.expedienteAnualidadDAO.update(expedienteAnualidad);
                    this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

                }

            }

        }

    }

    /**
     *
     * @param convocatoriaAnualidad
     */
    private void copiarBajaAnualidadConvExp(ConvocatoriaAnualidad convocatoriaAnualidad) {

        // Replicamos el cambio a los expedientes de coordinador de la misma
        // anualidad
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaAnualidad.getIdConvocatoria(), Operador.EQUALS));
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
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD,
                        convocatoriaAnualidad.getNmAnualidad(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteAnualidad> lstExpedienteAnualidad = this.expedienteAnualidadDAO
                        .find(filtros, null);

                for (ExpedienteAnualidad expedienteAnualidad : lstExpedienteAnualidad) {

                    this.expedienteAnualidadDAO
                            .deleteLogico(expedienteAnualidad.getIdExpedienteAnualidad());
                    ExpedienteAnualidad expedienteAnualidadI = this.expedienteAnualidadDAO
                            .find(expedienteAnualidad.getIdExpedienteAnualidad());

                    this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidadI);

                }

            }

        }
    }

    /**
     *
     * @param convocatoriaAnualidad
     */
    private void insertarAnualidadConvExp(ConvocatoriaAnualidad convocatoriaAnualidad) {

        // Replicamos el cambio a los expedientes de coordinador de la misma
        // anualidad
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                convocatoriaAnualidad.getIdConvocatoria(), Operador.EQUALS));
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

                ExpedienteAnualidad expedienteAnualidad = new ExpedienteAnualidad();
                expedienteAnualidad.setIdExpediente(expediente.getIdExpediente());
                expedienteAnualidad.setNmAnualidad(convocatoriaAnualidad.getNmAnualidad());
                expedienteAnualidad.setTipoExpedienteAnualidad(new CatalogosValores(
                        convocatoriaAnualidad.getTipoConvocatoriaAnualidad().getIdValor()));
                expedienteAnualidad.setFcIniEjecucion(convocatoriaAnualidad.getFcIniEjecucion());
                expedienteAnualidad.setFcFinEjecucion(convocatoriaAnualidad.getFcFinEjecucion());
                expedienteAnualidad.setTlObservacion(convocatoriaAnualidad.getTlObservacion());

                this.expedienteAnualidadDAO.insert(expedienteAnualidad);
                this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

            }

        }

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaAnualidadDTO> buscarPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMANUALIDAD, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ConvocatoriaAnualidadToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void actualizarDatosAnualidad(Integer idConvocatoria, Integer nmAnualidad,
            int cantidadRegistrosCargados) throws ServiceException {

        List<ConvocatoriaAnualidadDTO> lista = this.buscarPorConvocatoria(idConvocatoria);
        
        Integer idTipJusInter = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA);
        Integer idTipJusFin = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL);
        
        
        if (!lista.isEmpty()) {

            if (nmAnualidad > cantidadRegistrosCargados) {

                // actualizamos el ultimo registro como Intermedio
                ConvocatoriaAnualidad convocatoriaAnualidad = this
                        .find(lista.get(lista.size() - 1).getIdConvocatoriaAnualidad());
                convocatoriaAnualidad.setTipoConvocatoriaAnualidad(new CatalogosValores(idTipJusInter));
                convocatoriaAnualidad = this.update(convocatoriaAnualidad);
                this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidad);

                this.copiarAnualidadConvExp(convocatoriaAnualidad);

                // agregamos los nuevos registros reservando uno para Final

                for (int i = cantidadRegistrosCargados + 1; i < nmAnualidad; i++) {

                    ConvocatoriaAnualidad convocatoriaAnualidadI = new ConvocatoriaAnualidad();
                    convocatoriaAnualidadI.setIdConvocatoria(idConvocatoria);
                    convocatoriaAnualidadI.setTipoConvocatoriaAnualidad(new CatalogosValores(idTipJusInter));
                    convocatoriaAnualidadI.setNmAnualidad(i);
                    convocatoriaAnualidadI = this.insert(convocatoriaAnualidadI);
                    this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidadI);

                    this.insertarAnualidadConvExp(convocatoriaAnualidadI);

                }

                // agregamos el nuevo registro Final
                ConvocatoriaAnualidad convocatoriaAnualidadF = new ConvocatoriaAnualidad();
                convocatoriaAnualidadF.setIdConvocatoria(idConvocatoria);
                convocatoriaAnualidadF.setTipoConvocatoriaAnualidad(new CatalogosValores(idTipJusFin));
                convocatoriaAnualidadF.setNmAnualidad(nmAnualidad);
                convocatoriaAnualidadF = this.insert(convocatoriaAnualidadF);
                this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidadF);

                this.insertarAnualidadConvExp(convocatoriaAnualidadF);

            } else if (nmAnualidad < cantidadRegistrosCargados) {

                // calculamos los registros sobrante
                int sobrantes = cantidadRegistrosCargados - nmAnualidad;

                // verificamos si tenemos la cantidad de registros sobrantes
                // para eliminar
                if (sobrantes >= lista.size()) {

                    // eliminamos todos los registros
                    for (int x = 0; x < cantidadRegistrosCargados; x++) {

                        this.deleteLogico(lista.get(x).getIdConvocatoriaAnualidad());
                        ConvocatoriaAnualidad convocatoriaAnualidad = this
                                .find(lista.get(x).getIdConvocatoriaAnualidad());
                        this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidad);

                        this.copiarBajaAnualidadConvExp(convocatoriaAnualidad);

                    }

                } else {

                    // eliminamos los registros sobrantes
                    for (int x = 0; x < sobrantes; x++) {

                        this.deleteLogico(
                                lista.get(lista.size() - (1 + x)).getIdConvocatoriaAnualidad());
                        ConvocatoriaAnualidad convocatoriaAnualidadI = this.find(
                                lista.get(lista.size() - (1 + x)).getIdConvocatoriaAnualidad());
                        this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidadI);

                        this.copiarBajaAnualidadConvExp(convocatoriaAnualidadI);

                    }

                    // actualizamos el ultimo registro activo como FINAL
                    ConvocatoriaAnualidad convocatoriaAnualidadF = this.find(
                            lista.get(lista.size() - (1 + sobrantes)).getIdConvocatoriaAnualidad());
                    convocatoriaAnualidadF.setTipoConvocatoriaAnualidad(new CatalogosValores(idTipJusFin));
                    convocatoriaAnualidadF = this.update(convocatoriaAnualidadF);
                    this.convocatoriaAnualidadHistDAO.crearHistorico(convocatoriaAnualidadF);

                    this.copiarAnualidadConvExp(convocatoriaAnualidadF);

                }

            }

        }

    }

    @Override
    public List<ConvocatoriaAnualidad> buscarAnualidades(ConvocatoriaDTO dto)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
                dto.getIdConvocatoria(), Operador.EQUALS));
        // filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCINIEJECUCION,
        // Operador.NOT_NULL));
        // filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCFINEJECUCION,
        // Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.find(filtros, null);
    }

}
