package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDocDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoDocToDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoDocToEntity;
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
 * Implementación del servicio para la clase de dominio
 * NotificacionCiudadanoDoc
 *
 * @author everis
 */
@Service("notificacionCiudadanoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class NotificacionCiudadanoDocServiceImpl extends
        BaseServiceImpl<NotificacionCiudadanoDoc, Integer, NotificacionCiudadanoDocDAO>
        implements NotificacionCiudadanoDocService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param notificacionCiudadanoDocDAO
     */
    @Autowired
    public NotificacionCiudadanoDocServiceImpl(
            @Qualifier("notificacionCiudadanoDocDAO") NotificacionCiudadanoDocDAO notificacionCiudadanoDocDAO) {
        super();
        this.setDao(notificacionCiudadanoDocDAO);
    }

    /**
     *
     */
    @Override
    public List<NotificacionCiudadanoDocDTO> buscarPorNotificacion(Integer idNotificacion)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("notificacionCiudadano.idNotificacion", idNotificacion,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), NotificacionCiudadanoDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public NotificacionCiudadanoDocDTO guardar(
            NotificacionCiudadanoDocDTO notificacionCiudadanoDocDTO) throws ServiceException {
        return NotificacionCiudadanoDocToDTO.INSTANCE.apply(this.insertOrUpdate(
                NotificacionCiudadanoDocToEntity.INSTANCE.apply(notificacionCiudadanoDocDTO)));
    }

}
