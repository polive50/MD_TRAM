package org.madrid.saii.lib.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

@Service("identificacionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class IdentificacionServiceImpl implements IdentificacionService{
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Override
    public Integer calcularTipoIdentificacion(String numDoc) throws ServiceException {
        
        Integer itipoDoc = 0;

        // Validaciones de entrada
        if (StringUtils.isBlank(numDoc)) {
            return itipoDoc;
        }

        if (numDoc.length() != 9) {
            return itipoDoc;
        }

        boolean empiezaPorXoYoZ = false;

        // si es NIE, se subtituye la x,y,z inicial por 0,1,2 según corresponda,
        // para tratarlo como nif
        if (numDoc.toUpperCase().startsWith("X")) {
            numDoc = "0" + numDoc.substring(1);
            empiezaPorXoYoZ = true;
        } else if (numDoc.toUpperCase().startsWith("Y")) {
            numDoc = "1" + numDoc.substring(1);
            empiezaPorXoYoZ = true;
        } else if (numDoc.toUpperCase().startsWith("Z")) {
            numDoc = "2" + numDoc.substring(1);
            empiezaPorXoYoZ = true;
        }

        Pattern nifPattern = Pattern
                .compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher m = nifPattern.matcher(numDoc);

        if (m.matches()) {
            String letra = m.group(2);
            // Extraer letra del NIF
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int dni = Integer.parseInt(m.group(1));
            dni = dni % 23;
            String reference = letras.substring(dni, dni + 1);

            if (reference.equalsIgnoreCase(letra)) {
                itipoDoc = empiezaPorXoYoZ ? utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIE)
                        : utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIF);
            }
        }

        return itipoDoc;
        
    }

}
