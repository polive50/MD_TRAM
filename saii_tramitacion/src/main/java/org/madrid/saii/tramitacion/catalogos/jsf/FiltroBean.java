package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.List;

import javax.annotation.PostConstruct;

import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase contienen metodos de Backing Bean para los filtros.
 *
 * @author everis
 */
@Getter
@Setter
@Slf4j
@Component("filtroBean")
@Scope("request")
public class FiltroBean extends CatalogoBean<Catalogos, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    // Lista de CatalogosValores para los filtros
    /**
     * listadoLineAyudaFiltro
     */
    private List<CatalogosValoresDTO> listadoLineaAyudaFiltro;

    /**
     * listadoAreaCientificaFiltro
     */
    private List<CatalogosValoresDTO> listadoAreaCientificaFiltro;

    /** Atributo con el total de entidades devuelto por la búsqueda. */
    private Integer totalEntidadesLineaAyuda;

    /** Atributo con el total de entidades devuelto por la búsqueda. */
    private Integer totalEntidadesAreaCientifica;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de CatalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public FiltroBean() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// MÉTODOS
    // ////////////////////////////////////////////////////////

    /**
     * Crear Entidad
     */
    @Override
    public Catalogos crearEntidad() {
        return new Catalogos();
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        log.info("Post Construct");
    }

    /**
     * Obtener Lista de catalogos para elegir el filtro del catalogo Obtiene la
     * lista de catalogosValores del catalogo de tipo "Lineas de ayuda"
     *
     * @param index
     * @param pageSize
     * @param orderBean
     * @param filtros
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException 
     */
    public List<CatalogosValoresDTO> obtenerListaLineasAyudaFiltro(final int index,
            final int pageSize, List<AtlasSort> orderBean, List<AtlasFilter> filtros) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = filtros;
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.LINEAS_AYUDA), Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(getCampoFcBaja(), Operador.NULL));
        if ((isRealizarBusqueda() || (getTotalEntidadesLineaAyuda() == 0))) {
            try {
                listadoLineaAyudaFiltro = catalogosValoresService.buscar(index, pageSize,
                        filtrosEntrada, orderBean);

            } catch (ServiceException exc) {
                log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
                // Se muestra un mensaje de error al usuario
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            }
        }
        return listadoLineaAyudaFiltro;
    }

    /**
     * Obtener total catalogos del tipo Lineas de ayuda del filtro.
     *
     * @param filtros
     * @return
     * @throws ServiceException 
     */
    public int obtenerTotalLineasAyudaFiltro(List<AtlasFilter> filtros) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = filtros;
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.LINEAS_AYUDA), Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(getCampoFcBaja(), Operador.NULL));
        try {
            setTotalEntidadesLineaAyuda(catalogosValoresService.count(filtrosEntrada));
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_TOTAL_CATALOGOS);
        }
        return getTotalEntidadesLineaAyuda();
    }

    /**
     * Obtener Lista de catalogos para elegir el filtro del catalogo Obtiene la
     * lista de catalogosValores del catalogo de tipo "Area Cientifica"
     *
     * @param index
     * @param pageSize
     * @param orderBean
     * @param filtros
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException 
     */
    public List<CatalogosValoresDTO> obtenerListaAreaCientificaFiltro(final int index,
            final int pageSize, List<AtlasSort> orderBean, List<AtlasFilter> filtros) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = filtros;
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.AREAS_CIENTIFICAS), Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(getCampoFcBaja(), Operador.NULL));
        if ((isRealizarBusqueda() || (getTotalEntidadesAreaCientifica() == 0))) {
            try {
                listadoAreaCientificaFiltro = catalogosValoresService.buscar(index, pageSize,
                        filtrosEntrada, orderBean);

            } catch (ServiceException exc) {
                log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
                // Se muestra un mensaje de error al usuario
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            }
        }
        return listadoAreaCientificaFiltro;
    }

    /**
     * Obtener total catalogos del tipo Area Cientifica del filtro.
     *
     * @param filtros
     * @return
     * @throws ServiceException 
     */
    public int obtenerTotalAreaCientificaFiltro(List<AtlasFilter> filtros) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = filtros;
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.AREAS_CIENTIFICAS), Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(getCampoFcBaja(), Operador.NULL));
        try {
            setTotalEntidadesAreaCientifica(catalogosValoresService.count(filtrosEntrada));
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_TOTAL_CATALOGOS);
        }
        return getTotalEntidadesAreaCientifica();
    }

}
