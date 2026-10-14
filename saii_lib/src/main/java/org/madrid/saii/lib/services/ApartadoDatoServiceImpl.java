package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ApartadoDatoDAO;
import org.madrid.saii.lib.dao.EvaluacionNotaDatoDAO;
import org.madrid.saii.lib.domain.ApartadoDato;
import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.ApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionApartadoDTO;
import org.madrid.saii.lib.dto.converters.ApartadoDatoToDTO;
import org.madrid.saii.lib.dto.converters.ApartadoDatoToEntity;
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
@Service("apartadoDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ApartadoDatoServiceImpl extends BaseServiceImpl<ApartadoDato, Integer, ApartadoDatoDAO>
        implements ApartadoDatoService {

    /**
     * Servicio evaluacionNotaDatoService
     */
    @Autowired
    @Qualifier("evaluacionNotaDatoService")
    private EvaluacionNotaDatoService evaluacionNotaDatoService;

    @Autowired
    @Qualifier("evaluacionNotaDatoDAO")
    private EvaluacionNotaDatoDAO evaluacionNotaDatoDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param apartadoDatoDAO
     */
    @Autowired
    public ApartadoDatoServiceImpl(@Qualifier("apartadoDatoDAO") ApartadoDatoDAO apartadoDatoDAO) {
        super();
        this.setDao(apartadoDatoDAO);
    }

    /**
     * Constructor Vacio
     */
    public ApartadoDatoServiceImpl() {
        super();
    }

    /**
    *
    */
    @Override
    public InformeEvaluacionApartadoDTO obtenerApartadosDatoPorIdExpedienteEval(
            Integer idExpedienteEval) throws ServiceException {

        InformeEvaluacionApartadoDTO informeApartadoDTO = new InformeEvaluacionApartadoDTO();

        if (Objects.isNull(idExpedienteEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL, idExpedienteEval,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_APARTADO, true));

        informeApartadoDTO.getLstApartadoDatoDTO().addAll(new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, orden), ApartadoDatoToDTO.INSTANCE)));

        informeApartadoDTO.getLstEvaluacionNotaDatoDTO().addAll(this.evaluacionNotaDatoService
                .obtenerEvaluacionNotaDatoPorIdExpedienteEval(idExpedienteEval));

        return informeApartadoDTO;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public InformeEvaluacionApartadoDTO guardarListaApartadoDato(
            InformeEvaluacionApartadoDTO apartadosDTO, String cdUsuario) throws ServiceException {

        InformeEvaluacionApartadoDTO informeApartadoDTO = new InformeEvaluacionApartadoDTO();
        List<ApartadoDato> lstApartados = new ArrayList<>();
        List<EvaluacionNotaDato> lstEvaluaciones = new ArrayList<>();

        if (Objects.isNull(apartadosDTO.getLstApartadoDatoDTO()) || StringUtils.isBlank(cdUsuario)
                || Objects.isNull(apartadosDTO.getLstEvaluacionNotaDatoDTO())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        for (ApartadoDatoDTO dto : apartadosDTO.getLstApartadoDatoDTO()) {

            ApartadoDato entidad = ApartadoDatoToEntity.INSTANCE.apply(dto);
            if (Objects.isNull(dto.getIdApartadoDato())) {
                entidad.setCdUsuAlta(cdUsuario);
                entidad.setFcAltaFila(new Date());
                lstApartados.add(this.getDao().insert(entidad));
            } else {
                entidad.setCdUsuModif(cdUsuario);
                entidad.setFcModifFila(new Date());
                lstApartados.add(this.getDao().merge(entidad));
            }

        }

        List<EvaluacionNotaDato> lstEvaluacionNotaEntidad = new ArrayList<>(Lists.transform(
                apartadosDTO.getLstEvaluacionNotaDatoDTO(), EvaluacionNotaDatoToEntity.INSTANCE));

        for (EvaluacionNotaDato evaluacionNotaDato : lstEvaluacionNotaEntidad) {

            if (Objects.isNull(evaluacionNotaDato.getIdEvaluacionNotaDato())) {

                evaluacionNotaDato.setCdUsuAlta(cdUsuario);
                evaluacionNotaDato.setFcAltaFila(new Date());

                evaluacionNotaDato = this.evaluacionNotaDatoDAO.insert(evaluacionNotaDato);

            } else {

                evaluacionNotaDato.setCdUsuModif(cdUsuario);
                evaluacionNotaDato.setFcModifFila(new Date());

                evaluacionNotaDato = this.evaluacionNotaDatoDAO.merge(evaluacionNotaDato);
            }

            lstEvaluaciones.add(evaluacionNotaDato);

        }

        informeApartadoDTO.setLstEvaluacionNotaDatoDTO(new ArrayList<>(
                Lists.transform(lstEvaluaciones, EvaluacionNotaDatoToDTO.INSTANCE)));
        informeApartadoDTO.getLstApartadoDatoDTO()
                .addAll(new ArrayList<>(Lists.transform(lstApartados, ApartadoDatoToDTO.INSTANCE)));

        return informeApartadoDTO;
    }

}
