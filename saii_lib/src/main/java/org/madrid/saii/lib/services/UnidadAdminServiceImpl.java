package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.UnidadAdminDAO;
import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.dto.converters.UnidadAdminToDTO;
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
 * Implementación del servicio para la clase de dominio UnidadAdmin
 *
 * @author everis
 */
@Service("unidadAdminService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class UnidadAdminServiceImpl extends BaseServiceImpl<UnidadAdmin, Integer, UnidadAdminDAO>
        implements UnidadAdminService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param unidadAdminDao
     */
    @Autowired
    public UnidadAdminServiceImpl(@Qualifier("unidadAdminDAO") UnidadAdminDAO unidadAdminDao) {
        super();
        this.setDao(unidadAdminDao);
    }

    /**
     * Buscar todos los registros de UnidadAdmin por filtros.
     *
     * @return List<UnidadAdminDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<UnidadAdminDTO> obtenerPorFiltros(List<AtlasFilter> filtros)
            throws ServiceException {

        ArrayList<UnidadAdminDTO> lstUnidadAdminDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), UnidadAdminToDTO.INSTANCE));

        return lstUnidadAdminDTO;

    }

    /**
     * Buscar id.
     *
     * @param idUnidadAdmin the id UnidadAdmin
     * @return the UnidadAdmin DTO
     * @throws ServiceException the service exception
     */
    @Override
    public UnidadAdminDTO buscarId(final Integer idUnidadAdmin) throws ServiceException {

        return UnidadAdminToDTO.INSTANCE.apply(this.find(idUnidadAdmin));

    }

    /**
     * BuscarcdUnidad
     *
     * @param cdUnidad the cdUnidad
     * @return the UnidadAdmin DTO
     * @throws ServiceException the service exception
     */
    @Override
    public UnidadAdminDTO buscarCdUnidad(final String cdUnidad) throws ServiceException {

        List<AtlasFilter> filtroIdPadre = new ArrayList<>();
        filtroIdPadre
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD, cdUnidad, Operador.EQUALS));
        filtroIdPadre.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<UnidadAdminDTO> lstUnidadAdminDTO = new ArrayList<>(
                Lists.transform(this.find(filtroIdPadre, null), UnidadAdminToDTO.INSTANCE));

        if (!lstUnidadAdminDTO.isEmpty()) {
            return lstUnidadAdminDTO.get(0);
        } else {
            return new UnidadAdminDTO();
        }

    }

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<UnidadAdminDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {

        ArrayList<UnidadAdminDTO> lstUnidadAdminDTO = new ArrayList<>(Lists
                .transform(this.find(index, pageSize, filtros, orden), UnidadAdminToDTO.INSTANCE));

        return lstUnidadAdminDTO;
    }

    /**
    *
    */
    @Override
    public String obtenerDescripcionUnidad(Integer cdUnidad) throws ServiceException {

        String descripcion = "";
        
        UnidadAdmin unidadAdmin = this.find(cdUnidad);
        if (unidadAdmin != null && unidadAdmin.getDsUnidad() != null) {
            descripcion = unidadAdmin.getDsUnidad();
        }

        return descripcion;
    }

    /**
    *
    */
    @Override
    public String obtenerConsejeria() throws ServiceException {
        
        String descripcion = "";
        
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD, "C0900", Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<UnidadAdmin> lstUsuarios = this.find(filtros, null);

        if (!lstUsuarios.isEmpty()) {
            descripcion = lstUsuarios.get(0).getDsUnidad();
        }
        return descripcion;
    }
}
