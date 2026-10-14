package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.ConvocatoriaActDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaAct
 *
 * @author everis
 */
@Service("convocatoriaActService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaActServiceImpl
        extends BaseServiceImpl<ConvocatoriaAct, Integer, ConvocatoriaActDAO>
        implements ConvocatoriaActService {

    /**
     * Servicio de Impreso
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaActDAO
     */
    @Autowired
    public ConvocatoriaActServiceImpl(@Qualifier("convocatoriaActDAO")
    ConvocatoriaActDAO convocatoriaActDAO) {
        super();
        this.setDao(convocatoriaActDAO);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaActToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaActDTO guardar(final ConvocatoriaActDTO convocatoriaActDTO)
            throws ServiceException {

        // Validaciones

        // VALIDACIONES DATOS OBLIGATORIOS
        this.validarGuardarConvocatoriaActores(convocatoriaActDTO);
        
        Integer idTipActSol = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE);
        
        if (idTipActSol.equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

            // siempre genera expediente
            convocatoriaActDTO.setBolGeneraExpediente(Boolean.TRUE);

            // siempre su numero maximo de participante sera 1
            convocatoriaActDTO.setNmMax(1);
            convocatoriaActDTO.setNmMin(1);

            // no tiene asociado otro solicitante
            convocatoriaActDTO.setIdActorSolicitante(null);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {
            convocatoriaActDTO.setBolGeneraExpediente(Boolean.TRUE);
        }

        // Insertamos el actor
        ConvocatoriaAct convocatoriaAct = this
                .insertOrUpdate(ConvocatoriaActToEntity.INSTANCE.apply(convocatoriaActDTO));

        // Si de tipo Solicitante creamos el impreso
        if (idTipActSol.equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {
            if (convocatoriaActDTO.getIdActor() == null) {
                ImpresoDTO impresoDTO = new ImpresoDTO();

                impresoDTO.setIdConvocatoria(convocatoriaActDTO.getIdConvocatoria());
                impresoDTO.setItGeneraImpreso(Constantes.AfirmacionNegacion.S);
                impresoDTO.setTipoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD)));
                impresoDTO.setConvocatoriaActDTO(ConvocatoriaActToDTO.INSTANCE.apply(convocatoriaAct));
                impresoDTO = this.impresoService.guardar(impresoDTO);

            } else {

                // Obtenemos el impreso solicitud para modificarlo.
                List<AtlasFilter> filtros = new ArrayList<>();

                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, convocatoriaActDTO.getIdConvocatoria(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, convocatoriaActDTO.getIdActor(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<Impreso> lstImpresos = this.impresoService.find(filtros, null);

                if (!lstImpresos.isEmpty()) {
                    Impreso impreso = lstImpresos.get(0);
                    impreso.setConvocatoriaAct(convocatoriaAct);
                    this.impresoService.update(impreso);
                }
            }
        }

        return ConvocatoriaActToDTO.INSTANCE.apply(convocatoriaAct);

    }

    /**
     * Método para validar datos de convocatoria de actores
     *
     * @param convocatoriaActDTO Datos de convocatoria actores
     * @throws ServiceException Excepción generado en el servicio
     */
    private void validarGuardarConvocatoriaActores(ConvocatoriaActDTO convocatoriaActDTO)
            throws ServiceException {
        if (convocatoriaActDTO.getIdentificacionDTO().getIdValor() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> buscarPorTipoPersona(Integer tipoPersona,
            Integer idConvocatoria) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter("tipoActorFisJur.idValor", tipoPersona, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
    public ConvocatoriaActDTO buscarId(final Integer idActor) throws ServiceException {
        return ConvocatoriaActToDTO.INSTANCE.apply(this.find(idActor));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        List<AtlasSort> orden = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        orden.add(new AtlasSort("tlDescripcion", true));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, orden), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> buscarTipoActorParticipante(Integer idConvocatoria)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.EMPTY));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> buscarTipoActorSolicitante(Integer idConvocatoria)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.EMPTY));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaActDTO> obtenerParticipantesSolicitante(Integer idActor)
            throws ServiceException {

        // Buscamos los participantes de este actor
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDACTOR_SOLICITANTE, idActor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    @Override
    public List<ConvocatoriaActDTO> obtenerParticipantesCoordinador(Integer idActor)
            throws ServiceException {

        // Buscamos los participantes de este actor
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDACTOR_COORDINADOR, idActor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaActDTO borrarActor(Integer idActor) throws ServiceException {

        this.deleteLogico(idActor);

        ConvocatoriaAct convocatoriaAct = this.find(idActor);

        ImpresoDTO impresoDTO = this.impresoService
                .buscarPorConvActor(convocatoriaAct.getIdConvocatoria(), idActor, null);

        if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdImpreso())) {
            Impreso impreso = this.impresoService.find(impresoDTO.getIdImpreso());
            impreso.setConvocatoriaAct(null);
            this.impresoService.update(impreso);
        }

        return ConvocatoriaActToDTO.INSTANCE.apply(convocatoriaAct);
    }

    /**
    *
    */
    @Override
    public List<ConvocatoriaActDTO> buscarTipoActorCoordinador(Integer idConvocatoria)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.EMPTY));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    public Boolean validarConvocatoriaContratoActor(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCONTRATO,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaAct> lstConvocatoriaAct = this.find(filtros, null);

        return !lstConvocatoriaAct.isEmpty();

    }

    /**
     * Obtiene los coordinadores de un solicitante
     */
    @Override
    public List<ConvocatoriaActDTO> obtenerCoordinadores(Integer idActor) throws ServiceException {

        // Buscamos los coordinadores asociados al solicitante
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDACTOR_SOLICITANTE, idActor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
     * Busca actores hijos con contrato de un actor padre en una convocatoria
     */
    @Override
    public List<ConvocatoriaActDTO> buscarHijosConContrato(Integer idConvocatoria, Integer idActor,
            String tipoActorPadre) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(tipoActorPadre, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCONTRATO,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.EMPTY));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    public List<ConvocatoriaAct> retornarPaginado(List<ConvocatoriaAct> listaConvocatoriaAct,
            int index, int pageSize) {

        List<ConvocatoriaAct> listaEvaluadoresJCTPaginada = new ArrayList<>();

        if (!listaConvocatoriaAct.isEmpty()) {

            if (pageSize != -1) {

                int lastIndex = 0;

                if (listaConvocatoriaAct.size() <= 10) {
                    index = 0;
                    lastIndex = listaConvocatoriaAct.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaConvocatoriaAct.size()
                            ? listaConvocatoriaAct.size()
                            : lastIndex;
                }

                for (int i = index; i < lastIndex; i++) {
                    listaEvaluadoresJCTPaginada.add(listaConvocatoriaAct.get(i));
                }
            } else {
                listaEvaluadoresJCTPaginada.addAll(listaConvocatoriaAct);
            }
        }

        return listaConvocatoriaAct;

    }

    /**
    *
    */
    @Override
    public List<ConvocatoriaAct> buscarDatosActoresCorreo(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {

        List<ConvocatoriaAct> lstConvocatoriaAct = null;

        lstConvocatoriaAct = this.find(index, pageSize, filtros, orden);

        return lstConvocatoriaAct;
    }

    /**
    *
    */
    @Override
    public List<ConvocatoriaActDTO> obtenerActoresPorIdActor(Integer idActor)
            throws ServiceException {
        List<ConvocatoriaActDTO> listaDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("idActor", idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaAct> lista = this.find(filtros, null);

        if (lista != null && !lista.isEmpty()) {
            listaDTO = new ArrayList<>();
            for (ConvocatoriaAct convocatoriaAct : lista) {
                listaDTO.add(ConvocatoriaActToDTO.INSTANCE.apply(convocatoriaAct));
            }
        }

        return listaDTO;
    }

    @Override
    public List<ConvocatoriaActDTO> obtenerActoresAsociados(ConvocatoriaActDTO convocatoriaActDTO)
            throws ServiceException {
        return this.getDao().buscarActoresAsociados(convocatoriaActDTO);
    }
}
