package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.componentes.core.utiles.Constants;

/**
 * Bean genérico padre para todos los beans JSF que representan un catálogo.
 *
 * @param <T> entidad propia del catálogo
 * @param <PK> tipo de la clave primaria de la entidad
 *
 * @author Madrid digital
 * @version Atlas 2.1.0
 * @since 23/07/2021 10:00:53 AM
 */
@Component
public abstract class CatalogoBean<T, K> {

    /** Entidad actual. */
    private T entidad;

    /** Atributo con el total de entidades devuelto por la búsqueda. */
    private int totalEntidades;

    /** Indica si se debe realizar la búsqueda. */
    private boolean realizarBusqueda;

    /** Propiedad de filtrado para la lista paginada */
    private List<AtlasFilter> filtros;

    /** Indica si se está insertando o modificando un registro */
    private boolean nuevaEntidad;

    /** Indica si se muestran los registros dados de baja */
    private boolean mostrarBaja;

    /** Nombre de la propiedad Fecha de baja del registro. */
    private String campoFcBaja;

    /** Indica si los datos se muestran en modo consulta */
    private boolean modoConsulta;

    /** Indica si el formulario de edición se muestra en un popup */
    private boolean mostrarPopup;

    ///////////////////////////////////////////////
    //////////////////////// CONSTRUCTOR
    ///////////////////////////////////////////////

    /**
     * Crea una nueva instancia del objeto
     */
    public CatalogoBean() {
        super();
        this.entidad = this.crearEntidad();
        // Por defecto, el campo fecha de baja en Atlas se llamará así
        this.campoFcBaja = "fcBajaFila";
        // Inicialización de parámetros por defecto
        this.mostrarBaja = false;
        this.totalEntidades = 0;
        this.realizarBusqueda = true;
    }

    ///////////////////////////////////////////////
    //////////////////////// MÉTODOS PÚBLICOS
    ///////////////////////////////////////////////

    /**
     * Crea la entidad del catálogo al ser llamado desde el bean padre.
     *
     * @return nueva entidad
     */
    public abstract T crearEntidad();

    ///////////////////////////////////////////////
    //////////////////////// GETTERS Y SETTERS
    ///////////////////////////////////////////////

    /**
     * Devuelve el valor de nuevaEntidad.
     *
     * @return true si la acción=NUEVO y false si acción=MODIFICAR
     */
    public boolean isNuevaEntidad() {
        return this.nuevaEntidad;
    }

    /**
     * Establece valor a nueva entidad.
     *
     * @param nuevaEntidad true si la acción=NUEVO y false si acción=MODIFICAR
     */
    public void setNuevaEntidad(boolean nuevaEntidad) {
        this.nuevaEntidad = nuevaEntidad;
    }

    /**
     * Devuelve el valor de la entidad asociada al ManagedBean
     *
     * @return valor de entidad
     */
    public T getEntidad() {
        return this.entidad;
    }

    /**
     * Establece el valor de la variable entidad asociada al ManagedBean
     *
     * @param entidad valor de entidad a establecer
     */
    public void setEntidad(T entidad) {
        this.entidad = entidad;
    }

    /**
     * Devuelve el valor la variable 'filtros'
     *
     * @return valor de filtros
     */
    public List<AtlasFilter> getFiltros() {
        return this.filtros;
    }

    /**
     * Establece el valor de la variable filtros
     *
     * @param filtros valor de filtros a establecer
     */
    public void setFiltros(List<AtlasFilter> filtros) {
        this.filtros = filtros;
    }

    /**
     * Devuelve el valor totalEntidades
     *
     * @return valor de totalEntidades
     */
    public int getTotalEntidades() {
        return this.totalEntidades;
    }

    /**
     * Establece el valor de la variable totalEntidades
     *
     * @param totalEntidades valor de totalEntidades a establecer
     */
    public void setTotalEntidades(int totalEntidades) {
        this.totalEntidades = totalEntidades;
    }

    /**
     * Devuelve el valor realizarBusqueda
     *
     * @return valor de realizarBusqueda
     */
    public boolean isRealizarBusqueda() {
        return this.realizarBusqueda;
    }

    /**
     * Establece el valor de la variable realizarBusqueda
     *
     * @param realizarBusqueda valor de realizarBusqueda a establecer
     */
    public void setRealizarBusqueda(boolean realizarBusqueda) {
        this.realizarBusqueda = realizarBusqueda;
    }

    /**
     * Devuelve el valor mostrarBaja
     *
     * @return valor de mostrarBaja
     */
    public boolean isMostrarBaja() {
        return this.mostrarBaja;
    }

    /**
     * Establece el valor de la variable mostrarBaja
     *
     * @param mostrarBaja valor de mostrarBaja a establecer
     */
    public void setMostrarBaja(boolean mostrarBaja) {
        this.mostrarBaja = mostrarBaja;
    }

    /**
     * Devuelve el valor modoConsulta
     *
     * @return valor de modoConsulta
     */
    public boolean isModoConsulta() {
        return this.modoConsulta;
    }

    /**
     * Establece el valor de la variable modoConsulta
     *
     * @param modoConsulta valor de modoConsulta a establecer
     */
    public void setModoConsulta(boolean modoConsulta) {
        this.modoConsulta = modoConsulta;
    }

    /**
     * Devuelve el valor de la propiedad fecha de baja
     *
     * @return valor de campoFcBaja
     */
    public String getCampoFcBaja() {
        return this.campoFcBaja;
    }

    /**
     * Establece el valor de la propiedad campoFcBaja
     *
     * @param campoFcBaja valor de campoFcBaja a establecer
     */
    public void setCampoFcBaja(String campoFcBaja) {
        this.campoFcBaja = campoFcBaja;
    }

    /**
     * Devuelve el valor de mostrarPopup
     *
     * @return valor de mostrarPopup
     */
    public boolean isMostrarPopup() {
        return this.mostrarPopup;
    }

    /**
     * Establece el valor de la variable mostrarPopup
     *
     * @param mostrarPopup valor de mostrarPopup a establecer
     */
    public void setMostrarPopup(boolean mostrarPopup) {
        this.mostrarPopup = mostrarPopup;
    }

    /**
     * Lista de operaciones para el filtrado de fechas.
     *
     * @return lista de operaciones
     */
    public List<SelectItem> getOpcionesFecha() {
        return Constants.operacionesFecha;
    }

    /**
     * Lista de operaciones para el filtrado de números.
     *
     * @return lista de operaciones
     */
    public List<SelectItem> getOpcionesNumero() {
        return Constants.operacionesNumero;
    }
}