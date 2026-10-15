package org.madrid.saii.tramitacion.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.madrid.saii.lib.services.UnidadAdminService;
import org.madrid.saii.lib.services.UsuarioService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.seguridad.politicas.PoliticaUsuarioInternet;
import atlas.core.seguridad.politicas.PoliticaUsuarioIntranet;
import atlas.core.seguridad.politicas.autenticacion.AtlasAuthenticationDetails;
import atlas.core.util.UserUtils;
import atlas.usuPackUsuario.dto.UsuPackUsuarioDto;
import atlas.usuPackUsuario.services.UsuPackUsuarioService;
import atlas.usuiPackUsuario.dto.UsuiPackUsuarioDto;
import atlas.usuiPackUsuario.services.UsuiPackUsuarioService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas que obtiene
 * informacion del usuario autenticado en el sistema
 *
 * @author everis
 */
@Slf4j
@Getter
@Setter
@Component("infoUsuarioBean")
@Scope("request")
public class InfoUsuarioBean {

    /**
     * Servicio UsuPackUsuarioService
     */
    @Autowired
    @Qualifier("usuPackUsuarioService")
    private UsuPackUsuarioService usuPackUsuarioService;

    /**
     * Servicio UsuiPackUsuarioService
     */
    @Autowired
    @Qualifier("usuiPackUsuarioService")
    private UsuiPackUsuarioService usuiPackUsuarioService;

    /**
     * Servicio UsuarioService
     */
    @Autowired
    @Qualifier("usuarioService")
    private UsuarioService usuarioService;

    /**
     * Servicio UnidadAdminService
     */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    /** Login del usuario */
    private String login;

    /** Roles del usuario */
    private String roles;

    /** Descripción del rol */
    private String descripcionRol;

    /** Descripción del nivel organizativo */
    private String descripcionNivelOrg;

    /** IP */
    private String ip;

    /** Fecha del último acceso */
    private String fechaUltimoAcceso;

    /** Login con certificado */
    private Boolean loginConCertificadoBo;

    /** UsuPackUsuarioDto */
    private UsuPackUsuarioDto usuPackUsuarioDto;

    /** UsuiPackUsuarioDto */
    private UsuiPackUsuarioDto usuiPackUsuarioDto;

    /** Usuaio de intranet */
    private Boolean usuarioIntranet;

    /** Usuario de internet */
    private Boolean usuarioInternet;

    /** Rol de Super Administrador */
    private Boolean rolSuperAdministrador;

    /** Rol de Administrador */
    private Boolean rolAdministrador;

    /** Rol de Supervisor */
    private Boolean rolSupervisor;

    /** Rol de Gestor */
    private Boolean rolGestor;

    /** Rol de Gestor externo */
    private Boolean rolGestorExterno;

    /** Rol de Auditor */
    private Boolean rolAuditor;

    /** Dirección */
    private UnidadAdminDTO direccion;

    /** Subdirección */
    private UnidadAdminDTO subDireccion;

    /** Unidad administrativa */
    private UnidadAdminDTO unidadAdmin;

    /** Boolean dirección */
    private Boolean nvlDireccion;

    /** Boolean subdirección */
    private Boolean nvlSubDireccion;

    /** Boolean unidad administrativa */
    private Boolean nvlUnidadAdmin;

    /**
     * Boolean que indica si tiene subdirección general o unidad para continuar
     */
    private Boolean tieneSubDirOUnidad;

    /**
     * Constructor
     */
    public InfoUsuarioBean() {
        super();
        this.direccion = new UnidadAdminDTO();
        this.subDireccion = new UnidadAdminDTO();
        this.unidadAdmin = new UnidadAdminDTO();
        this.tieneSubDirOUnidad = Boolean.TRUE;
    }

    /**
     * Método que carga los datos del usuario
     */
    public void loadUserData() {
        this.loginConCertificadoBo = UserUtils.isLoginConCertificado();
        this.login = UserUtils.getUserName().toUpperCase();
        this.fechaUltimoAcceso = DateFormatUtils.format(UserUtils.getFechaUltimoAcceso(),
                Constantes.Utiles.FORMATO_FECHA_HORA);
        this.ip = Faces.getRemoteAddr();
        this.roles = this.obtenerRoles();
        String politicaActual = Faces
                .getRequestAttribute(AtlasAuthenticationDetails.ID_POLITICA_REQUEST_KEY);
        try {
            this.consultaPoliticaUsuario(politicaActual);

            // Calculamos las variables de rol
            this.cacularRol();

            // Si es rol ADMINISTRADOR, SUPERVISOR se recupera el código de
            // unidad
            if (Boolean.TRUE.equals(this.rolAdministrador)
                    || Boolean.TRUE.equals(this.rolSupervisor)
                    || Boolean.TRUE.equals(this.rolGestor)) {

                UsuarioDTO usuarioDTO = this.usuarioService.buscarCdUsuario(this.login);

                if (Objects.isNull(usuarioDTO) || Objects.isNull(usuarioDTO.getCdUnidad())) {
                    this.tieneSubDirOUnidad = Boolean.FALSE;
                    throw new ServiceException(AtlasMessageUtils
                            .getStringFromBundle(ConstantesWeb.Err.CDUNIDAD_VACIA));
                }

                String cdUnidad = usuarioDTO.getCdUnidad();

                List<AtlasFilter> filtros = new ArrayList<>();

                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD, cdUnidad,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                List<UnidadAdminDTO> lstUnidadAdmDTO = this.unidadAdminService
                        .obtenerPorFiltros(filtros);

                if (lstUnidadAdmDTO.isEmpty()) {
                    this.tieneSubDirOUnidad = Boolean.FALSE;
                    throw new ServiceException(AtlasMessageUtils
                            .getStringFromBundle(ConstantesWeb.Err.CDUNIDAD_NO_SAII));
                } else {
                    UnidadAdminDTO unidadAdminDTO = lstUnidadAdmDTO.get(0);
                    this.calcularNivelOrganizativo(unidadAdminDTO);

                    switch (unidadAdminDTO.getNivelOrgDTO().getIdNivelOrg()) {
                    case 2:
                        this.direccion = unidadAdminDTO;
                        break;
                    case 3:
                        this.subDireccion = unidadAdminDTO;
                        this.direccion = this.unidadAdminService
                                .buscarCdUnidad(unidadAdminDTO.getCdUnidadPadre());
                        break;
                    case 4:
                        this.unidadAdmin = unidadAdminDTO;
                        this.subDireccion = this.unidadAdminService
                                .buscarCdUnidad(unidadAdminDTO.getCdUnidadPadre());

                        filtros.clear();
                        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                                this.subDireccion.getCdUnidad(), Operador.EQUALS));
                        List<UnidadAdminDTO> lstUnidadAdmDTO2 = this.unidadAdminService
                                .obtenerPorFiltros(filtros);

                        UnidadAdminDTO unidadAdminDTO2 = lstUnidadAdmDTO2.get(0);
                        this.direccion = this.unidadAdminService
                                .buscarCdUnidad(unidadAdminDTO2.getCdUnidadPadre());
                        break;
                    default:
                        break;
                    }
                }
            }

        } catch (ServiceException e) {
            String errorMessage = "Se ha producido un error al obtener los datos ampliados del usuario:"
                    + e.getMessage();
            log.error(errorMessage, e);
            Messages.addGlobalError(e.getMessage());
        }
    }

    /**
     * Este método devuelve el role asociado al usuario autenticado en el
     * sistema.
     *
     * @return <code>java.lang.String</code> El rol asociado al usuario.
     */
    private String obtenerRoles() {
        GrantedAuthority[] userRoles = UserUtils.getUserRoles();
        return !ArrayUtils.isEmpty(userRoles) ? userRoles[0].getAuthority() : "Usuario sin roles";
    }

    /**
     * Informa los booleanos de rol y su descripción
     */
    private void cacularRol() {
        this.rolSuperAdministrador = Boolean.FALSE;
        this.rolAdministrador = Boolean.FALSE;
        this.rolSupervisor = Boolean.FALSE;
        this.rolGestor = Boolean.FALSE;
        this.rolAuditor = Boolean.FALSE;
        this.rolGestorExterno = Boolean.FALSE;

        if (Constantes.Rol.SUPER_ADMINISTRADOR.equals(this.roles)) {
            this.rolSuperAdministrador = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.SUPER_ADMINISTRADOR_DS;
        }

        if (Constantes.Rol.ADMINISTRADOR.equals(this.roles)) {
            this.rolAdministrador = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.ADMINISTRADOR_DS;
        }

        if (Constantes.Rol.SUPERVISOR.equals(this.roles)) {
            this.rolSupervisor = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.SUPERVISOR_DS;
        }

        if (Constantes.Rol.GESTOR.equals(this.roles)) {
            this.rolGestor = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.GESTOR_DS;
        }

        if (Constantes.Rol.AUDITOR.equals(this.roles)) {
            this.rolAuditor = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.AUDITOR_DS;
        }

        if (Constantes.Rol.GESTOR_EXTERNO.equals(this.roles)) {
            this.rolGestorExterno = Boolean.TRUE;
            this.descripcionRol = Constantes.Rol.GESTOR_EXTERNO_DS;
        }
    }

    /**
     * Calcula el nivel organizativo
     *
     * @param unidadAdminDTO
     */
    private void calcularNivelOrganizativo(UnidadAdminDTO unidadAdminDTO) {
        this.nvlDireccion = Boolean.FALSE;
        this.nvlSubDireccion = Boolean.FALSE;
        this.nvlUnidadAdmin = Boolean.FALSE;

        if (Constantes.NivelOrganizativo.DIRECCION
                .equals(unidadAdminDTO.getNivelOrgDTO().getIdNivelOrg())) {
            this.nvlDireccion = Boolean.TRUE;
            this.descripcionNivelOrg = Constantes.NivelOrganizativo.DIRECCION_DS;
        }

        if (Constantes.NivelOrganizativo.SUBDIRECCION
                .equals(unidadAdminDTO.getNivelOrgDTO().getIdNivelOrg())) {
            this.nvlDireccion = Boolean.TRUE;
            this.nvlSubDireccion = Boolean.TRUE;
            this.descripcionNivelOrg = Constantes.NivelOrganizativo.SUBDIRECCION_DS;
        }

        if (Constantes.NivelOrganizativo.UNIDADADMIN
                .equals(unidadAdminDTO.getNivelOrgDTO().getIdNivelOrg())) {
            this.nvlDireccion = Boolean.TRUE;
            this.nvlSubDireccion = Boolean.TRUE;
            this.nvlUnidadAdmin = Boolean.TRUE;
            this.descripcionNivelOrg = Constantes.NivelOrganizativo.UNIDADADMIN_DS;
        }
    }

    /**
     * Consulta el detalla del usuario segun la politica
     *
     * @param politicaActual
     */
    private void consultaPoliticaUsuario(String politicaActual) {

        try {
            this.usuPackUsuarioDto = new UsuPackUsuarioDto();
            this.usuiPackUsuarioDto = new UsuiPackUsuarioDto();

            if (StringUtils.isNoneBlank(politicaActual)) {
                if (politicaActual.contains(PoliticaUsuarioIntranet.POLITICA_NAME)) {
                    this.usuPackUsuarioDto = this.usuPackUsuarioService
                            .consultaDetalleUsuario(this.login);
                    this.usuarioIntranet = Boolean.TRUE;
                    this.usuarioInternet = Boolean.FALSE;
                } else if (politicaActual.contains(PoliticaUsuarioInternet.POLITICA_NAME)) {
                    this.usuiPackUsuarioDto = this.usuiPackUsuarioService
                            .consultaDetalleUsuario(this.login);
                    this.usuarioIntranet = Boolean.FALSE;
                    this.usuarioInternet = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            String errorMessage = "Se ha producido un error en consultaDetalleUsuario:"
                    + e.getMessage();
            log.error(errorMessage, e);
        }
    }
}
