package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ExpedienteEvalJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalJusTecHistDAO;
import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalJusTecToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalJusTecToEntity;
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
import atlas.core.util.UserUtils;
import atlas.fica.services.FicaService;
import lombok.Setter;

/**
 * Interfaz del servicio ExpedienteEvalJusTec
 *
 * @author quental
 */

@Service("expedienteEvalJusTecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteEvalJusTecServiceImpl
        extends BaseServiceImpl<ExpedienteEvalJusTec, Integer, ExpedienteEvalJusTecDAO>
        implements ExpedienteEvalJusTecService {

    @Autowired
    public ExpedienteEvalJusTecServiceImpl(@Qualifier("expedienteEvalJusTecDAO")
    ExpedienteEvalJusTecDAO expedienteEvalJusTecDAO) {
        super();
        this.setDao(expedienteEvalJusTecDAO);
    }

    @Autowired
    @Qualifier("personaAreaDAO")
    private PersonaAreaDAO personaAreaDAO;

    /**
     * DAO ExpedienteEvalJusTecDAO
     */
    @Autowired
    @Qualifier("expedienteEvalJusTecDAO")
    private ExpedienteEvalJusTecDAO expedienteEvalJusTecDAO;

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    @Autowired
    @Qualifier("expedienteEvalJusTecHistDAO")
    private ExpedienteEvalJusTecHistDAO expedienteEvalJusTecHistDAO;

    /** servicio de expedienteJusTec **/
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * 
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec)
            throws ServiceException {

        List<AtlasFilter> filtrosEntrada = (filtros != null) ? filtros : new ArrayList<>();

        filtrosEntrada
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                        idExpedienteJusTec, Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtrosEntrada, orden),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

    /**
     * 
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarAll(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec)
            throws ServiceException {

        List<AtlasFilter> filtrosEntrada = (filtros != null) ? filtros : new ArrayList<>();

        filtrosEntrada
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                        idExpedienteJusTec, Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NOT_NULL));

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtrosEntrada, orden),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO guardar(ExpedienteEvalJusTecDTO expedienteEvalJusTecDto)
            throws ServiceException {
        expedienteEvalJusTecDto.setCdUsuModif(UserUtils.getUserName());
        expedienteEvalJusTecDto.setFcModifFila(new Date());

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(this.insertOrUpdate(
                ExpedienteEvalJusTecToEntity.INSTANCE.apply(expedienteEvalJusTecDto)));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO actualizar(ExpedienteEvalJusTecDTO expedienteEvalJusTec)
            throws ServiceException {
        expedienteEvalJusTec.setCdUsuModif(UserUtils.getUserName());
        expedienteEvalJusTec.setFcModifFila(new Date());

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(
                this.update(ExpedienteEvalJusTecToEntity.INSTANCE.apply(expedienteEvalJusTec)));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO borradoLogico(ExpedienteEvalJusTecDTO expedienteEvalJusTec)
            throws ServiceException {
        expedienteEvalJusTec.setCdUsuBaja(UserUtils.getUserName());
        expedienteEvalJusTec.setFcBajaFila(new Date());

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(
                this.update(ExpedienteEvalJusTecToEntity.INSTANCE.apply(expedienteEvalJusTec)));
    }

    @Override
    public List<ExpedienteEvalJusTecDTO> buscarPeriodosPendienteEvalJustec(Integer idExpediente,
            Boolean pendienteEval) throws ServiceException {
        return this.getDao().buscarPeriodosPendienteEvalJustec(idExpediente, pendienteEval);
    }

    /**
    *
    */
    public List<ExpedienteEvalJusTecDTO> buscarEvaluadoresJCT(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJusTecDTO) throws ServiceException {

        StringBuilder cadenaAreasCientificas = new StringBuilder();
        PersonaAreaDTO personaAreaDTO = new PersonaAreaDTO();

        if (Boolean.FALSE.equals(listaExpedienteEvalJusTecDTO.isEmpty())) {

            for (int i = 0; i < listaExpedienteEvalJusTecDTO.size(); i++) {

                if (cadenaAreasCientificas != null) {
                    cadenaAreasCientificas = new StringBuilder();
                }
                List<PersonaAreaDTO> areas = this.personaAreaDAO.listObtenerAreas(
                        listaExpedienteEvalJusTecDTO.get(i).getInteresadoDTO().getIdInteresado());
                for (int y = 0; y < areas.size(); y++) {
                    personaAreaDTO.getAreaDTO().setDsValor(areas.get(y).getAreaDTO().getDsValor());
                    if (personaAreaDTO.getAreaDTO().getIdValor() == null) {
                        if (cadenaAreasCientificas.length() > 0) {
                            cadenaAreasCientificas.append(" " + "/" + " ");
                            cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                            listaExpedienteEvalJusTecDTO.get(i)
                                    .setAreasCientificas(cadenaAreasCientificas.toString());
                        } else {
                            cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                            listaExpedienteEvalJusTecDTO.get(i)
                                    .setAreasCientificas(cadenaAreasCientificas.toString());
                        }
                    }
                }
            }

        }

        return listaExpedienteEvalJusTecDTO;
    }

    /**
    *
    */
    @Override
    public List<ExpedienteEvalJusTecDTO> retornarPaginado(
            List<ExpedienteEvalJusTecDTO> listaEvaluadoresJCTFiltrada,

            int index, int pageSize) {

        List<ExpedienteEvalJusTecDTO> listaEvaluadoresJCTPaginada = new ArrayList<>();

        if (!listaEvaluadoresJCTFiltrada.isEmpty()) {

            if (pageSize != -1) {

                int lastIndex = 0;

                if (listaEvaluadoresJCTFiltrada.size() <= 10) {
                    index = 0;
                    lastIndex = listaEvaluadoresJCTFiltrada.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaEvaluadoresJCTFiltrada.size()
                            ? listaEvaluadoresJCTFiltrada.size()
                            : lastIndex;
                }

                for (int i = index; i < lastIndex; i++) {
                    listaEvaluadoresJCTPaginada.add(listaEvaluadoresJCTFiltrada.get(i));
                }
            } else {
                listaEvaluadoresJCTPaginada.addAll(listaEvaluadoresJCTFiltrada);
            }
        }

        return listaEvaluadoresJCTPaginada;

    }

    @Override
    public List<ExpedienteEvalJusTecDTO> buscarByIdExpJustec(
            List<ExpedienteJusTecDTO> listExpedienteJusTecDTO) throws ServiceException {

        List<ExpedienteEvalJusTecDTO> listResponse = new ArrayList<>();
        List<ExpedienteEvalJusTecDTO> listResponseFinal = new ArrayList<>();

        for (int i = 0; i < listExpedienteJusTecDTO.size(); i++) {

            Integer idExpedienteJusTec = listExpedienteJusTecDTO.get(i).getIdExpedienteJusTec();
            List<ExpedienteEvalJusTec> ListExpedienteEvalJusTecResponse = this.expedienteEvalJusTecDAO
                    .buscarExpEvalJCT(idExpedienteJusTec);

            for (int y = 0; y < ListExpedienteEvalJusTecResponse.size(); y++) {
                listResponse.add(ExpedienteEvalJusTecToDTO.INSTANCE
                        .apply(ListExpedienteEvalJusTecResponse.get(y)));
                ExpedienteEvalJusTecDTO expedienteEvalJusTecResponseDTOFinal = new ExpedienteEvalJusTecDTO();

                for (int z = 0; z < listResponse.size(); z++) {
                    if (listResponse.get(z).getIdExpedienteEvalJusTec() != null) {
                        expedienteEvalJusTecResponseDTOFinal = listResponse.get(z);
                        listResponseFinal.add(expedienteEvalJusTecResponseDTOFinal);
                    }
                }

            }
        }

        return listResponseFinal;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO buscarByIdExpedienteJustec(Integer idExpedienteJusTec)
            throws ServiceException {

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(this.getDao().find(idExpedienteJusTec));

    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO guardarExpEvalJusTecCt(
            ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO) throws ServiceException {

        ExpedienteEvalJusTec expedienteEvalJusTec = this.insertOrUpdate(
                ExpedienteEvalJusTecToEntity.INSTANCE.apply(expedienteEvalJusTecDTO));

        this.expedienteEvalJusTecHistDAO.crearHistoricoExpEvalJus(expedienteEvalJusTec);

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(expedienteEvalJusTec);

    }

    @Override
    public Boolean existePersonaExpedienteEvalJusTec(Integer idInteresado) {

        Boolean resultado = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }

        return resultado;
    }

    @Override
    public Boolean existePersonaExpedienteEvalJusTecPorExpedienteJustec(Integer idInteresado,
            List<ExpedienteJusTecDTO> listPeriodosExpdienteEval) {

        Boolean resultado = Boolean.FALSE;

        for (int i = 0; i < listPeriodosExpdienteEval.size(); i++) {

            Integer idExpedienteJusTec = listPeriodosExpdienteEval.get(i).getIdExpedienteJusTec();

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, idInteresado,
                    Operador.EQUALS));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                            idExpedienteJusTec, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            int coincidencias = this.getDao().count(filtros);

            if (coincidencias > 0) {
                resultado = Boolean.TRUE;
            }
        }

        return resultado;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTec guardarCambiosRegistro(ExpedienteEvalJusTec registroEditadoGrabar)
            throws ServiceException {
        registroEditadoGrabar.setCdUsuModif(UserUtils.getUserName());
        registroEditadoGrabar.setFcModifFila(new Date());
        return this.insertOrUpdate(registroEditadoGrabar);
    }

    /**
     * 
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarEvaluadoresPorPeriodo(final int index,
            final int pageSize, final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            int idExpedienteJusTec) throws ServiceException {

        List<AtlasFilter> filtrosBuscar = new ArrayList<>();
        filtrosBuscar
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                        idExpedienteJusTec, Operador.EQUALS));
        filtrosBuscar.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtrosBuscar, orden),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO buscarId(Integer idExpedienteEvalJusTec)
            throws ServiceException {

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(this.getDao().find(idExpedienteEvalJusTec));

    }

    /**
    *
    */
    @Override
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacionJustec(Integer idExpedienteEval)
            throws ServiceException {
        return this.getDao().obtenerInformeEvaluacionJustec(idExpedienteEval);
    }

    /**
    *
    */
    @Override
    public List<InfApartadoDatoDTO> obtenerApartadosDatos(Integer idExpedienteEval,
            Integer idEvaluacion) throws ServiceException {
        return this.getDao().obtenerApartadosJustecDatos(idExpedienteEval, idEvaluacion);
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO finalizarInformeEvaluacion(
            ExpedienteEvalJusTecDTO expedienteEvalDTO, String strIdentificador, byte[] documento)
            throws ServiceException {

        if (Objects.isNull(expedienteEvalDTO) || StringUtils.isBlank(strIdentificador)
                || Objects.isNull(documento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        ExpedienteEvalJusTec expedienteEvalJustec = this
                .find(expedienteEvalDTO.getIdExpedienteEvalJusTec());
        if (Objects.nonNull(expedienteEvalJustec)) {
            expedienteEvalJustec.setCdFica(
                    this.guardarFicaFichero(expedienteEvalJustec, documento, strIdentificador));
            // expedienteEvalJustec.setDsComentario(expedienteEvalDTO.getDsComentario());
            expedienteEvalJustec.setObsEval(expedienteEvalDTO.getObsEval());
            expedienteEvalJustec.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
            this.finalizarInformeEvaluador(expedienteEvalJustec, strIdentificador);
        }

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(expedienteEvalJustec);
    }

    /**
     * Metodo para finalizar informe de evaluador con informe
     *
     * @param expedienteEval
     * @param strIdentificador
     * @throws ServiceException
     */
    private void finalizarInformeEvaluador(ExpedienteEvalJusTec expedienteEval,
            String strIdentificador) {

        expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.N);
        expedienteEval.setCdUsuModif(strIdentificador);
        expedienteEval.setFcModifFila(new Date());
        expedienteEval.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR_JUSTEC)));
        // expedienteEval.setDsComentario(expedienteEval.getDsComentario());
        // expedienteEval.setNmNotaFinal(expedienteEval.getNmNotaFinal());
        this.getDao().merge(expedienteEval);

    }

    /**
     * Metodo de obtener de ficaFichero de expedienteEval
     *
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public String guardarFicaFichero(ExpedienteEvalJusTec expedienteEval, byte[] documento,
            String strIdentificador) throws ServiceException {

        String cdFica = "";

        // Validacion
        if (documento == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        // Nombre, fecha y extension del fichero
        ficaMetadatos.setNombreFichero(Constantes.TipoCoorEval.INFORME_EVALUACION);
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
        ficaMetadatos.setUsuario(strIdentificador);
        ficaMetadatos.setEncriptar(false);
        ficaMetadatos.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        FicaFichero fichero = null;
        if (expedienteEval.getCdFica() != null) {
            fichero = this.ficaService.actualizar(Integer.parseInt(expedienteEval.getCdFica()),
                    documento, ficaMetadatos);
            cdFica = String.valueOf(fichero.getIdInterno());
        } else {
            fichero = this.ficaService.guardar(documento, ficaMetadatos);
            cdFica = String.valueOf(fichero.getIdInterno());
        }
        return cdFica;
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecDTO finalizarSinInformeEvaluacion(
            ExpedienteEvalJusTecDTO expedienteEvalDTO, String strIdentificador)
            throws ServiceException {

        if (Objects.isNull(expedienteEvalDTO)
                || Objects.isNull(expedienteEvalDTO.getIdExpedienteEvalJusTec())
                || StringUtils.isBlank(strIdentificador)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteEvalJusTec expedienteEval = this
                .find(expedienteEvalDTO.getIdExpedienteEvalJusTec());

        if (Objects.nonNull(expedienteEval)) {

            expedienteEval.setEstado(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR_JUSTEC)));
            expedienteEval.setDsComentario(expedienteEvalDTO.getDsComentario());
            expedienteEval.setNmNotaFinal(expedienteEvalDTO.getNmNotaFinal());
            expedienteEval.setItNotificado(Constantes.AfirmacionNegacion.N);
            expedienteEval.setCdFica(null);
            expedienteEval.setCdUsuModif(strIdentificador);
            expedienteEval.setFcModifFila(new Date());
            expedienteEval = this.getDao().merge(expedienteEval);
        }

        return ExpedienteEvalJusTecToDTO.INSTANCE.apply(expedienteEval);
    }

    /**
    *
    */
    @Override
    public String obtenerCausaRechazoCoordinador(Integer idExpedienteEvalJustec)
            throws ServiceException {

        if (Objects.isNull(idExpedienteEvalJustec)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVALJUSTEC,
                idExpedienteEvalJustec, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteEvalJusTec> lst = this.find(filtros, null);

        if (!lst.isEmpty() && lst.get(0).getCausa() != null) {
            ExpedienteEvalJusTec ent = lst.get(0);
            return ent.getCausa().getDsValor();
        } else {
            return null;
        }

    }

    @Override
    public List<ExpedienteEvalJusTecDTO> obtenerListaExpedienteEvalJustec(
            Integer idExpedienteEvalJustec) throws ServiceException {
        List<ExpedienteEvalJusTecDTO> result = new ArrayList<>();
        ExpedienteEvalJusTec expedienteEvalJustecDTO = this.getDao().find(idExpedienteEvalJustec);

        if (expedienteEvalJustecDTO != null && expedienteEvalJustecDTO.getExpedienteJustec() != null
                && expedienteEvalJustecDTO.getExpedienteJustec().getIdExpediente() != null) {
            result = this.expedienteEvalJusTecDAO.obtenerListaExpedienteEvalJustec(
                    expedienteEvalJustecDTO.getExpedienteJustec().getIdExpediente(),
                    expedienteEvalJustecDTO.getPersona().getIdInteresado());
        }
        return result;
    }

    @Override
    public List<ExpedienteEvalJusTec> expedienteEvalJusTecXExpedienteJusTec(
            Integer idExpedientejusTec) throws ServiceException {
        return expedienteEvalJusTecDAO.buscarExpEvalJCT(idExpedientejusTec);
    }

    /**
     * Actualiza desde el batch el estado de los expdtes eval justec segun sus
     * estados previos y fechas de JCT para iniciar plazo evaluacion
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public void iniciarPlazoEvaluacionJCT() throws ServiceException {

        // buscamos los exdtes justec en estado pdtes. de evaluacion
        List<ExpedienteJusTecDTO> lstExdteJustecDTO = this.expedienteJusTecService
                .buscarJustecPdteEvaluacion();

        if (lstExdteJustecDTO != null && !lstExdteJustecDTO.isEmpty()) {

            for (ExpedienteJusTecDTO expdteJustecDTOAux : lstExdteJustecDTO) {

                // buscamos las evaluaciones de esos expdtes justec con
                // evaluador asignado y no notificadas
                List<ExpedienteEvalJusTecDTO> lstExpdteEvalJusTecDTO = this
                        .buscarExpedientesEvalAsignadoJustec(
                                expdteJustecDTOAux.getIdExpedienteJusTec());

                if (lstExpdteEvalJusTecDTO != null && !lstExpdteEvalJusTecDTO.isEmpty()) {

                    for (ExpedienteEvalJusTecDTO expdteEvalJustecDTOAux : lstExpdteEvalJusTecDTO) {
                        // actualizamos el estado de los expdtes eval justec a
                        // pdte evaluacion y si notificados en fecha corriente
                        expdteEvalJustecDTOAux.setEstadoDTO(new CatalogosValoresDTO(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION)));
                        expdteEvalJustecDTOAux.setItNotificado(Constantes.AfirmacionNegacion.S);

                        ExpedienteEvalJusTec expedienteEvalJusTec = ExpedienteEvalJusTecToEntity.INSTANCE
                                .apply(expdteEvalJustecDTOAux);
                        expedienteEvalJusTec.setFcModifFila(new Date());
                        expedienteEvalJusTec
                                .setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        ExpedienteEvalJusTec mergedExpedienteEvalJusTec = this.getDao()
                                .merge(expedienteEvalJusTec);
                        this.expedienteEvalJusTecHistDAO
                                .crearHistoricoExpEvalJus(mergedExpedienteEvalJusTec);

                    }
                }

                // buscamos las evaluaciones de esos expdtes justec con
                // evaluador asignado y no notificadas con fechas pasadas
                List<ExpedienteEvalJusTecDTO> lstExpdteEvalPasadoJusTecDTO = this
                        .buscarExpedientesEvalAsignadoPasadoJustec(
                                expdteJustecDTOAux.getIdExpedienteJusTec());

                if (lstExpdteEvalPasadoJusTecDTO != null
                        && !lstExpdteEvalPasadoJusTecDTO.isEmpty()) {

                    for (ExpedienteEvalJusTecDTO expdteEvalJustecPasDTOAux : lstExpdteEvalPasadoJusTecDTO) {
                        // actualizamos el estado de los expdtes eval justec a
                        // pdte evaluacion y si notificados
                        expdteEvalJustecPasDTOAux.setEstadoDTO(new CatalogosValoresDTO(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO)));
                        expdteEvalJustecPasDTOAux.setItNotificado(Constantes.AfirmacionNegacion.N);

                        ExpedienteEvalJusTec expedienteEvalJusTec = ExpedienteEvalJusTecToEntity.INSTANCE
                                .apply(expdteEvalJustecPasDTOAux);
                        expedienteEvalJusTec.setFcModifFila(new Date());
                        expedienteEvalJusTec
                                .setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        ExpedienteEvalJusTec mergedExpedienteEvalJusTec = this.getDao()
                                .merge(expedienteEvalJusTec);
                        this.expedienteEvalJusTecHistDAO
                                .crearHistoricoExpEvalJus(mergedExpedienteEvalJusTec);

                    }
                }
            }
        }
    }

    /**
     * Actualiza desde el batch el estado de los expdtes eval justec segun sus
     * estados previos y fechas de JCT para finalizar plazo evaluacion
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public void finalizarPlazoEvaluacionJCT() throws ServiceException {

        // buscamos las evaluaciones JCT en estado pdte. evaluacion
        List<ExpedienteEvalJusTecDTO> lstExpdteEvalJusTecDTO = this
                .buscarExpedientesPendientesEvalJustec();

        if (lstExpdteEvalJusTecDTO != null && !lstExpdteEvalJusTecDTO.isEmpty()) {

            for (ExpedienteEvalJusTecDTO expdteEvalJustecDTOAux : lstExpdteEvalJusTecDTO) {
                // actualizamos el estado de los expdtes eval justec a
                // evaluacion No evaluado y No notificado
                expdteEvalJustecDTOAux.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO)));
                expdteEvalJustecDTOAux.setItNotificado(Constantes.AfirmacionNegacion.N);

                ExpedienteEvalJusTec expedienteEvalJusTec = ExpedienteEvalJusTecToEntity.INSTANCE
                        .apply(expdteEvalJustecDTOAux);
                expedienteEvalJusTec.setFcModifFila(new Date());
                expedienteEvalJusTec.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                ExpedienteEvalJusTec mergedExpedienteEvalJusTec = this.getDao()
                        .merge(expedienteEvalJusTec);
                this.expedienteEvalJusTecHistDAO
                        .crearHistoricoExpEvalJus(mergedExpedienteEvalJusTec);
            }
        }

    }

    /**
     * Método para buscar ExpedienteEvalJusTecDTO en estado evaluador asignado
     * en fecha
     * 
     * @param idExpedienteJustec id del expediente JCT
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarExpedientesEvalAsignadoJustec(
            Integer idExpedienteJustec) throws ServiceException {
        Date currentDate = new Date();
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                idExpedienteJustec, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_INI_PLAZO, currentDate,
                Operador.LESS_OR_EQUAL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_PLAZO, currentDate,
                Operador.GREATER_OR_EQUAL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITNOTIFICADO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

    /**
     * Método para buscar ExpedienteEvalJusTecDTO en estado pdte evaluacion
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarExpedientesPendientesEvalJustec()
            throws ServiceException {
        Date currentDate = new Date();
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_PLAZO, currentDate,
                Operador.LESS_THAN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITNOTIFICADO,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

    /**
     * Método para buscar ExpedienteEvalJusTecDTO en estado evaluador asignado
     * pasado de fecha
     * 
     * @param idExpedienteJustec id del expediente JCT
     */
    @Override
    public List<ExpedienteEvalJusTecDTO> buscarExpedientesEvalAsignadoPasadoJustec(
            Integer idExpedienteJustec) throws ServiceException {
        Date currentDate = new Date();
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC,
                idExpedienteJustec, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_PLAZO, currentDate,
                Operador.LESS_THAN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITNOTIFICADO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteEvalJusTecToDTO.INSTANCE));
    }

}
