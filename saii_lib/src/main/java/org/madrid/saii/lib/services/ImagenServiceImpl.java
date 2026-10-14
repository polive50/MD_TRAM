package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ImagenDAO;
import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.madrid.saii.lib.dto.converters.ImagenToDTO;
import org.madrid.saii.lib.dto.converters.ImagenToEntity;
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
import atlas.fica.services.FicaService;
import lombok.Setter;

/**
 * Implementación del servicio para la clase de dominio Imagen
 *
 * @author nttdata
 */
@Service("imagenService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ImagenServiceImpl extends BaseServiceImpl<Imagen, Integer, ImagenDAO>
        implements ImagenService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param imagenDAO
     */
    @Autowired
    public ImagenServiceImpl(@Qualifier("imagenDAO") ImagenDAO imagenDAO) {
        super();
        this.setDao(imagenDAO);
    }

    /**
     * Constructor Vacio
     */
    public ImagenServiceImpl() {
        super();
    }

    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /**
     *
     */
    @Override
    public ImagenDTO buscarId(Integer idImagen) throws ServiceException {
        return ImagenToDTO.INSTANCE.apply(this.getDao().find(idImagen));
    }

    /**
     *
     */
    @Override
    public List<ImagenDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), ImagenToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<ImagenDTO> buscarLogosOrganismo() throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IMAGEN_CDUNIDAD, Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(filtros, null), ImagenToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<ImagenDTO> buscarLogosEuropeos(Integer idConvocatoria) throws ServiceException {
        return this.getDao().encontrarLogosEuropeos(idConvocatoria);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ImagenDTO guardar(ImagenDTO imagenDTO) throws ServiceException {
        return ImagenToDTO.INSTANCE
                .apply(this.insertOrUpdate(ImagenToEntity.INSTANCE.apply(imagenDTO)));
    }

    /**
     *
     */
    @Override
    public ImagenDTO descargar(String cdFica) throws ServiceException {
        if (Strings.isBlank(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ImagenDTO imagenDTO = new ImagenDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(Integer.valueOf(cdFica));
        imagenDTO.setFicaFichero(ficaFichero);
        imagenDTO.setContenidoFichero(ficaFichero.getDatosFichero());
        imagenDTO.setCdFica(cdFica);
        return imagenDTO;
    }

    /**
     *
     */
    @Override
    public ImagenDTO subirImagen(ImagenDTO imagenDTO) throws ServiceException {
        FicaFichero ficheroRecuperar = null;
        FicaFichero fichero;
        // Validacion
        if (imagenDTO.getContenidoFichero() == null || imagenDTO.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        if (StringUtils.isBlank(imagenDTO.getFicaMetadatos().getExtension())) {

            throw new ServiceException(Constantes.Error.ERROR_EXTENSION_FICHERO, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        imagenDTO.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        if (Strings.isNotBlank(imagenDTO.getCdFica())) {
            ficheroRecuperar = this.ficaService.recuperar(Integer.valueOf(imagenDTO.getCdFica()));
        }
        if (ficheroRecuperar == null) {
            fichero = this.ficaService.guardar(imagenDTO.getContenidoFichero(),
                    imagenDTO.getFicaMetadatos());
        } else {

            fichero = this.ficaService.actualizar(Integer.valueOf(imagenDTO.getCdFica()),
                    imagenDTO.getContenidoFichero(), imagenDTO.getFicaMetadatos());

            this.ficaService.actualizarMetadatos(Integer.valueOf(imagenDTO.getCdFica()),
                    imagenDTO.getFicaMetadatos());

        }
        imagenDTO.setFicaFichero(fichero);
        imagenDTO.setCdFica(String.valueOf(fichero.getIdInterno()));
        return imagenDTO;
    }

}
