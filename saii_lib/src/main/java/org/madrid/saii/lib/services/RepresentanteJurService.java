package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.RepresentanteJurDAO;
import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.RepresentanteJurFiltrosDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio RepresentanteJur
 *
 * @author everis
 */
public interface RepresentanteJurService
        extends BaseService<RepresentanteJur, Integer, RepresentanteJurDAO> {

    /**
     * Buscar por id RepresentanteJur.
     *
     * @param idRepresentanteJur the id RepresentanteJur
     * @return RepresentanteJurDTO
     * @throws ServiceException the service exception
     */
    RepresentanteJurDTO buscarId(Integer idRepresentanteJur) throws ServiceException;

    /**
     * Buscar paginadamente RepresentanteJur.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<RepresentanteJurDTO>
     * @throws ServiceException the service exception
     */
    List<RepresentanteJurDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un representanteJur.
     *
     * @param representanteJurDTO the representanteJur DTO
     * @return representanteJurDTO
     * @throws ServiceException the service exception
     */
    RepresentanteJurDTO guardar(final RepresentanteJurDTO representanteJurDTO)
            throws ServiceException;

    /**
     * Guarda un representanteJur.
     *
     * @param representanteJurDTO the representanteJur DTO
     * @return representanteJurDTO
     * @throws ServiceException the service exception
     */
    RepresentanteJurDTO registrar(final RepresentanteJurDTO representanteJurDTO)
            throws ServiceException;

    /**
     * Borrado logico RepresentanteJur
     *
     * @param RepresentanteJurDTO
     * @return RepresentanteJurDTO
     * @throws ServiceException
     */
    RepresentanteJurDTO borradoLogico(final RepresentanteJurDTO representanteJurDTO)
            throws ServiceException;

    /**
     * Alta logica
     *
     * @param representantejurDTO
     * @return RepresentanteJurDTO
     * @throws ServiceException
     */
    RepresentanteJurDTO altaLogica(final RepresentanteJurDTO representantejurDTO)
            throws ServiceException;

    /**
     * Busca una representante Juridico según sus filtros.
     *
     * @param RepresentanteJurFiltro
     * @return List<RepresentanteJurDTO>
     * @throws ServiceException
     */
    List<RepresentanteJurDTO> filtrar(final RepresentanteJurFiltrosDTO representanteJurFiltroDTO)
            throws ServiceException;

    /**
     * Obtiene los estados de los representantes legales
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     * @throws ServiceException
     */
    List<CatalogosValoresDTO> obtenerEstadoRepresentanteLegal() throws ServiceException;

    /**
     * Devuelve el representante jurídico a partir del DNI del representante y
     * el CIF de la empresa
     *
     * @param dniRepresentante the dni Representante
     * @param cifEmpresa the cif Empresa
     * @return representanteJurDTO
     * @throws ServiceException the service exception
     */
    RepresentanteJurDTO obtenerPorInteresado(final String dniRepresentante, final String cifEmpresa)
            throws ServiceException;

    List<RepresentanteJurDTO> obtenerEntidadesPorInteresado(String dniRepresentante,
            String cifEmpresa) throws ServiceException;
}
