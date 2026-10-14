package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ListadoDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ListadoService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Convocatoria}</code>
 *
 * @author everis
 *
 * @since 01-oct-2020 12:22:25
 *
 * @see ListadosBean
 */
@Getter
@Setter
@Slf4j
@Component("listadosBean")
@Scope("request")
public class ListadosBean extends CatalogoBean<Listado, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * Informe DTO
     */
    private ListadoDTO listadoDTO;

    /**
     * Id convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Indicador para bloquear boton de descarga
     */
    private Boolean bloquearDescarga = Boolean.FALSE;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de Informe
     */
    @Autowired
    @Qualifier("listadoService")
    private ListadoService listadoService;

    /**
     * Servicio de Informe
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ListadosBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad Informe
     *
     * @return entidad de tipo Informe
     */
    @Override
    public Listado crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.listadoDTO = new ListadoDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// MÉTODOS
    // ////////////////////////////////////////////////////////

    /**
     * Método para obtener la lista informes
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<InteresadoDTO> Lista de informes
     */

    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ListadoDTO> obtenerListaListados(int index, int pageSize,
            Object ordenacion, Object filter) {

        AtlasListAndCount<ListadoDTO> lista = new AtlasListAndCount<ListadoDTO>();

        try {

            if (Objects.nonNull(this.idConvocatoria)) {
                List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
                List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                // obtener los registros
                lista.setResultados(this.listadoService.buscar(index, pageSize, filtros, orden));

                // obtener el total de registros (numero)
                lista.setTotal(this.listadoService.count(filtros));

                this.verificarBloqueoDescarga();
            }

        } catch (ServiceException se) {
            log.error(se.getMessage());
            AtlasMessageUtils.addErrorMessage(Constantes.Error.ERROR_DATA);
        }

        return lista;
    }

    /**
     * Descarga el informe en excel
     *
     * @param idListado
     *
     */
    public void descargarExcel(Integer idListado) {
        try {
            ListadoDTO dto = this.listadoService.buscarId(idListado);
            byte[] byInformte = this.listadoService.obtenerListadoExcel(idListado);
            // Se prepara el fichero.
            UtilesWeb.openFile(byInformte, Constantes.Extensiones.XLS, dto.getDsTitulo());

        } catch (ServiceException se) {
            log.error(se.getMessage());
            AtlasMessageUtils.addErrorMessage(Constantes.Error.ERROR_DATA);
        }
    }

    /**
     *
     */
    public void verificarBloqueoDescarga() {
        try {

            if (Objects.nonNull(this.idConvocatoria)) {
                this.bloquearDescarga = this.convocatoriaService
                        .validarDescargaListados(this.idConvocatoria);
            }

        } catch (ServiceException se) {
            log.error(se.getMessage());
            AtlasMessageUtils.addErrorMessage(Constantes.Error.ERROR_DATA);
        }
    }

}
