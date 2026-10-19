package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoToDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio NotificacionCiudadano
 *
 * @author everis
 */
@Service("notificacionCiudadanoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class NotificacionCiudadanoServiceImpl extends
        BaseServiceImpl<NotificacionCiudadano, Integer, NotificacionCiudadanoDAO>
        implements NotificacionCiudadanoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param notificacionCiudadanoDAO
     */
    @Autowired
    public NotificacionCiudadanoServiceImpl(
            @Qualifier("notificacionCiudadanoDAO") NotificacionCiudadanoDAO notificacionCiudadanoDAO) {
        super();
        this.setDao(notificacionCiudadanoDAO);
    }

    @Autowired
    @Qualifier("notificacionCiudadanoDocDAO")
    private NotificacionCiudadanoDocDAO notificacionCiudadanoDocDAO;

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO buscarId(Integer id) throws ServiceException {
        return NotificacionCiudadanoToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     *
     */
    @Override
    public List<NotificacionCiudadanoDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), NotificacionCiudadanoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public NotificacionCiudadanoDTO guardar(NotificacionCiudadanoDTO notificacionCiudadanoDTO)
            throws ServiceException {
        return NotificacionCiudadanoToDTO.INSTANCE.apply(this.insertOrUpdate(
                NotificacionCiudadanoToEntity.INSTANCE.apply(notificacionCiudadanoDTO)));
    }

    /**
     *
     */
    @Override
    public NotificacionCiudadano buscarEnCursoEntidad(Integer idExpediente, Integer idAsunto)
            throws ServiceException {

        if (Objects.isNull(idExpediente) || Objects.isNull(idAsunto)) {
            return null;
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_ID_ASUNTO,
                idAsunto, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDEREG, Operador.NULL));

        List<NotificacionCiudadano> lstNotifiEnCurso = this.find(filtros, null);

        return Objects.nonNull(lstNotifiEnCurso) && !lstNotifiEnCurso.isEmpty()
                ? lstNotifiEnCurso.get(0)
                : null;
    }

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO buscarEnCurso(Integer idExpediente, Integer idAsunto)
            throws ServiceException {

        NotificacionCiudadano notificacionCiudadano = this.buscarEnCursoEntidad(idExpediente,
                idAsunto);

        NotificacionCiudadanoDTO dto = new NotificacionCiudadanoDTO();
        if (Objects.isNull(notificacionCiudadano)) {
            return dto;
        }

        dto = NotificacionCiudadanoToDTO.INSTANCE.apply(notificacionCiudadano);

        List<AtlasFilter> filtrosDoc = new ArrayList<>();
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION,
                notificacionCiudadano.getIdNotificacion(), Operador.EQUALS));
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

        List<NotificacionCiudadanoDoc> lstDocEnlazados = this.notificacionCiudadanoDocDAO
                .find(filtrosDoc, null);

        dto.setLstExpedienteDocDTO(lstDocEnlazados.stream()
                .map(NotificacionCiudadanoDoc::getExpedienteDoc)
                .map(ExpedienteDocToDTO.INSTANCE)
                .collect(Collectors.toList()));

        return dto;
    }

}
