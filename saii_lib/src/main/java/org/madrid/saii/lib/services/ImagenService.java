package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ImagenDAO;
import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Imagen
 *
 * @author nttdata
 */
public interface ImagenService extends BaseService<Imagen, Integer, ImagenDAO> {

    /**
     * Buscar por id Imagen.
     *
     * @param idImagen the id Imagen
     * @return ImagenDTO
     * @throws ServiceException the service exception
     */
    ImagenDTO buscarId(Integer idImagen) throws ServiceException;

    /**
     * Buscar paginadamente Imagen.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ImagenDTO>
     * @throws ServiceException the service exception
     */
    List<ImagenDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar logos que no son de fondo europeo
     *
     * @return List<ImagenDTO>
     */
    List<ImagenDTO> buscarLogosOrganismo() throws ServiceException;

    /**
     * Buscar logos de fondos europeos de la convocatoria
     * @return List<ImagenDTO>
     */
    List<ImagenDTO> buscarLogosEuropeos(Integer idConvocatoria) throws ServiceException;

    /**
     * Guarda un/a imagen.
     *
     * @param imagenDTO the imagen DTO
     * @return imagenDTO
     * @throws ServiceException the service exception
     */
    ImagenDTO guardar(ImagenDTO imagenDTO) throws ServiceException;

    /**
     * Descargar imágen fica.
     *
     * @param cdFica the cd fica
     * @return the Imagen DTO
     * @throws ServiceException the service exception
     */
    ImagenDTO descargar(String cdFica) throws ServiceException;

    /**
     * Subir imagen fica.
     *
     * @param imagenDTO the ImagenDTO
     * @return the ImagenDTO
     * @throws ServiceException the service exception
     */
    ImagenDTO subirImagen(ImagenDTO imagenDTO) throws ServiceException;

}
