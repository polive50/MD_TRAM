package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import org.madrid.saii.lib.dao.ApartadoJustecDatoDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecNotaDatoDAO;
import org.madrid.saii.lib.domain.ApartadoDato;
import org.madrid.saii.lib.domain.ApartadoJustecDato;
import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.madrid.saii.lib.dto.ApartadoJustecDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionApartadoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionJustecApartadoDTO;
import org.madrid.saii.lib.dto.converters.ApartadoDatoToDTO;
import org.madrid.saii.lib.dto.converters.ApartadoDatoToEntity;
import org.madrid.saii.lib.dto.converters.ApartadoJustecDatoToDTO;
import org.madrid.saii.lib.dto.converters.ApartadoJustecDatoToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaDatoToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaDatoToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaDatoToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaDatoToEntity;
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
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio ApartadoDato
 *
 * @author nttdata
 */
@Service("apartadoJustecDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ApartadoJustecDatoServiceImpl extends BaseServiceImpl<ApartadoJustecDato, Integer, ApartadoJustecDatoDAO>
        implements ApartadoJustecDatoService {

    /**
     * Servicio evaluacionNotaDatoService
     */
    @Autowired
    @Qualifier("evaluacionJustecNotaDatoService")
    private EvaluacionJustecNotaDatoService evaluacionJustecNotaDatoService;

    @Autowired
    @Qualifier("evaluacionJustecNotaDatoDAO")
    private EvaluacionJustecNotaDatoDAO evaluacionJustecNotaDatoDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param apartadoDatoDAO
     */
    @Autowired
    public ApartadoJustecDatoServiceImpl(@Qualifier("apartadoJustecDatoDAO") ApartadoJustecDatoDAO apartadoJustecDatoDAO) {
        super();
        this.setDao(apartadoJustecDatoDAO);
    }

    /**
     * Constructor Vacio
     */
    public ApartadoJustecDatoServiceImpl() {
        super();
    }

    /**
    *
    */
    @Override
    public InformeEvaluacionJustecApartadoDTO obtenerApartadosDatoPorIdExpedienteEvalJustec(
            Integer idExpedienteEvalJustec) throws ServiceException {

        InformeEvaluacionJustecApartadoDTO informeApartadoDTO = new InformeEvaluacionJustecApartadoDTO();

        if (Objects.isNull(idExpedienteEvalJustec)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE_APARTADO_JTC_DATO, idExpedienteEvalJustec,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_APARTADO, true));

        informeApartadoDTO.getLstApartadoDatoDTO().addAll(new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, orden), ApartadoJustecDatoToDTO.INSTANCE)));

        informeApartadoDTO.getLstEvaluacionNotaDatoDTO().addAll(this.evaluacionJustecNotaDatoService
                .obtenerEvaluacionJustecNotaDatoPorIdExpedienteEvalJustec(idExpedienteEvalJustec));

        return informeApartadoDTO;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public InformeEvaluacionJustecApartadoDTO guardarListaApartadoJustecDato(
            InformeEvaluacionJustecApartadoDTO apartadosDTO, String cdUsuario) throws ServiceException {

        InformeEvaluacionJustecApartadoDTO informeApartadoDTO = new InformeEvaluacionJustecApartadoDTO();
        List<ApartadoJustecDato> lstApartados = new ArrayList<>();
        List<EvaluacionJustecNotaDato> lstEvaluaciones = new ArrayList<>();

        if (Objects.isNull(apartadosDTO.getLstApartadoDatoDTO()) || StringUtils.isBlank(cdUsuario)
                || Objects.isNull(apartadosDTO.getLstEvaluacionNotaDatoDTO())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        for (ApartadoJustecDatoDTO dto : apartadosDTO.getLstApartadoDatoDTO()) {

            ApartadoJustecDato entidad = ApartadoJustecDatoToEntity.INSTANCE.apply(dto);
            if (Objects.isNull(dto.getIdApartadoJustecDato())) {
                entidad.setCdUsuAlta(cdUsuario);
                entidad.setFcAltaFila(new Date());
                lstApartados.add(this.getDao().insert(entidad));
            } else {
                entidad.setCdUsuModif(cdUsuario);
                entidad.setFcModifFila(new Date());
                lstApartados.add(this.getDao().merge(entidad));
            }

        }

        List<EvaluacionJustecNotaDato> lstEvaluacionNotaEntidad = new ArrayList<>(Lists.transform(
                apartadosDTO.getLstEvaluacionNotaDatoDTO(), EvaluacionJustecNotaDatoToEntity.INSTANCE));

        for (EvaluacionJustecNotaDato evaluacionNotaDato : lstEvaluacionNotaEntidad) {

            if (Objects.isNull(evaluacionNotaDato.getIdEvaluacionJustecNotaDato())) {

                evaluacionNotaDato.setCdUsuAlta(cdUsuario);
                evaluacionNotaDato.setFcAltaFila(new Date());

                evaluacionNotaDato = this.evaluacionJustecNotaDatoDAO.insert(evaluacionNotaDato);

            } else {

                evaluacionNotaDato.setCdUsuModif(cdUsuario);
                evaluacionNotaDato.setFcModifFila(new Date());

                evaluacionNotaDato = this.evaluacionJustecNotaDatoDAO.merge(evaluacionNotaDato);
            }

            lstEvaluaciones.add(evaluacionNotaDato);

        }

        informeApartadoDTO.setLstEvaluacionNotaDatoDTO(new ArrayList<>(
                Lists.transform(lstEvaluaciones, EvaluacionJustecNotaDatoToDTO.INSTANCE)));
        informeApartadoDTO.getLstApartadoDatoDTO()
                .addAll(new ArrayList<>(Lists.transform(lstApartados, ApartadoJustecDatoToDTO.INSTANCE)));

        return informeApartadoDTO;
    }

}
