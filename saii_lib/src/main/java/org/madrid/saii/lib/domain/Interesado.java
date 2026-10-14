package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio Interesado
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_INTERESADO")
@AtlasAuditarFila
@ToString
public class Interesado implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idInteresado del objeto de dominio
     */
    private Integer idInteresado;

    /**
     * Representa el campo idTipoIdentificacion del objeto de dominio
     */
    private CatalogosValores tipoIdentificacion;

    /**
     * Representa el campo idTipoActor del objeto de dominio
     */
    private CatalogosValores tipoActor;

    /**
     * Representa el campo cdNumIden del objeto de dominio
     */
    private String cdNumIden;

    /**
     * Representa el campo dsApellido1 del objeto de dominio
     */
    private String dsApellido1;

    /**
     * Representa el campo dsApellido2 del objeto de dominio
     */
    private String dsApellido2;

    /**
     * Representa el campo dsNombre del objeto de dominio
     */
    private String dsNombre;

    /**
     * Representa el campo fcNacimiento del objeto de dominio
     */
    private Date fcNacimiento;

    /**
     * Representa el campo cdSexo del objeto de dominio
     */
    private String cdSexo;

    /**
     * Representa el campo dsEmail del objeto de dominio
     */
    private String dsEmail;

    /**
     * Representa el campo dsMovil del objeto de dominio
     */
    private String dsMovil;

    /**
     * Representa el campo dsTelefono del objeto de dominio
     */
    private String dsTelefono;

    /**
     * Representa el campo cdProvincia del objeto de dominio
     */
    private String cdProvincia;

    /**
     * Representa el campo cdLocalidad del objeto de dominio
     */
    private String cdLocalidad;

    /**
     * Representa el campo cdTipoVia del objeto de dominio
     */
    private String cdTipoVia;

    /**
     * Representa el campo dsDireccion del objeto de dominio
     */
    private String dsDireccion;

    /**
     * Representa el campo cdCodigoPostal del objeto de dominio
     */
    private String cdCodigoPostal;

    /**
     * Representa el campo dsNumero del objeto de dominio
     */
    private String dsNumero;

    /**
     * Representa el campo dsPiso del objeto de dominio
     */
    private String dsPiso;

    /**
     * Representa el campo dsPuerta del objeto de dominio
     */
    private String dsPuerta;

    /**
     * Representa el campo dsEscalera del objeto de dominio
     */
    private String dsEscalera;

    /**
     * Representa el campo itCoorEval del objeto de dominio
     */
    private String itCoorEval;

    /**
     * Representa el campo dsDepartamento del objeto de dominio
     */
    private String dsDepartamento;

    /**
     * Representa el campo nmSexenioInve del objeto de dominio
     */
    private Integer nmSexenioInve;

    /**
     * Representa el campo nmSexenioTran del objeto de dominio
     */
    private Integer nmSexenioTran;
    
    /**
     * Representa el campo dsNacionalidad del objeto de dominio
     */
    private String dsNacionalidad;
    
    /**
     * Representa el campo dsPaisResidencia del objeto de dominio
     */
    private String dsPaisResidencia;


    /**
     * Representa el campo cdUsuAlta del objeto de dominio
     */
    private String cdUsuAlta;

    /**
     * Representa el campo fcAltaFila del objeto de dominio
     */
    private Date fcAltaFila;

    /**
     * Representa el campo cdUsuModif del objeto de dominio
     */
    private String cdUsuModif;

    /**
     * Representa el campo fcModifFila del objeto de dominio
     */
    private Date fcModifFila;

    /**
     * Representa el campo cdUsuBaja del objeto de dominio
     */
    private String cdUsuBaja;

    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcBajaFila;

    /**
     *
     */
    private List<PersonaArea> listaPersonaArea;

    /**
     *
     */
    private List<PersonaEspec> listaPersonaEspecialidad;

    /**
     *
     */
    private List<PersonaOrg> listaPersonaOrganismo;

    /**
     *
     */
    private List<PersonaTit> listaPersonaTitulacion;

    /**
     * campo de usuario
     */
    private String cdUsuario;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Interesado() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    /**
     * Implementación de hashCode para uso de Hibernate
     *
     * @return El código hash del objeto
     *         <p>
     *         Más información en:
     *         {@link http://community.jboss.org/docs/DOC-13933}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.idInteresado)
                .append(this.cdNumIden).append(this.dsApellido1).append(this.dsApellido2)
                .append(this.dsNombre).append(this.fcNacimiento).append(this.cdSexo)
                .append(this.dsEmail).append(this.dsMovil).append(this.dsTelefono)
                .append(this.cdProvincia).append(this.cdLocalidad).append(this.cdTipoVia)
                .append(this.dsDireccion).append(this.cdCodigoPostal).append(this.dsNumero)
                .append(this.dsPiso).append(this.dsPuerta).append(this.dsEscalera)
                .append(this.itCoorEval).append(this.dsDepartamento).append(this.nmSexenioInve)
                .append(this.nmSexenioTran).append(this.cdUsuario)
                .append(this.dsNacionalidad).append(this.dsPaisResidencia).toHashCode();
    }

    /**
     * Implementación de equals para uso con Hibernate
     * <p>
     * Más información en: {@link http://community.jboss.org/docs/DOC-13933}
     *
     * @param obj objeto a comparar con esta instancia.
     * @return true si el objeto pasado es igual a este; false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Interesado)) {
            return false;
        }
        Interesado castObj = (Interesado) obj;
        return new EqualsBuilder().append(this.idInteresado, castObj.idInteresado)
                .append(this.cdNumIden, castObj.cdNumIden)
                .append(this.dsApellido1, castObj.dsApellido1)
                .append(this.dsApellido2, castObj.dsApellido2)
                .append(this.dsNombre, castObj.dsNombre)
                .append(this.fcNacimiento, castObj.fcNacimiento).append(this.cdSexo, castObj.cdSexo)
                .append(this.dsEmail, castObj.dsEmail).append(this.dsMovil, castObj.dsMovil)
                .append(this.dsTelefono, castObj.dsTelefono)
                .append(this.cdProvincia, castObj.cdProvincia)
                .append(this.cdLocalidad, castObj.cdLocalidad)
                .append(this.cdTipoVia, castObj.cdTipoVia)
                .append(this.dsDireccion, castObj.dsDireccion)
                .append(this.cdCodigoPostal, castObj.cdCodigoPostal)
                .append(this.dsNumero, castObj.dsNumero).append(this.dsPiso, castObj.dsNumero)
                .append(this.dsPuerta, castObj.dsPuerta).append(this.dsEscalera, castObj.dsEscalera)
                .append(this.itCoorEval, castObj.itCoorEval)
                .append(this.dsDepartamento, castObj.dsDepartamento)
                .append(this.nmSexenioInve, castObj.nmSexenioInve)
                .append(this.nmSexenioTran, castObj.nmSexenioTran)
                .append(this.dsNacionalidad, castObj.dsNacionalidad)
                .append(this.dsPaisResidencia, castObj.dsPaisResidencia)
                .append(this.cdUsuario, castObj.cdUsuario)
                .isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idInteresado
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_INTERESADO")
    @SequenceGenerator(name = "SAII_S_INTERESADO", sequenceName = "SAII_S_INTERESADO", allocationSize = 1)
    @Column(name = "ID_INTERESADO")
    public Integer getIdInteresado() {
        return this.idInteresado;
    }

    /**
     * Establece el valor del campo idInteresado de la entidad Interesado
     *
     * @param idInteresado El valor a establecer
     */
    public void setIdInteresado(Integer idInteresado) {
        this.idInteresado = idInteresado;
    }

    /**
     * @return Devuelve el valor del campo itTipo
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_IDENTIFICACION", nullable = true)
    public CatalogosValores getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    /**
     * Establece el valor del campo itTipo de la entidad Impreso
     *
     * @param itTipo El valor a establecer
     */
    public void setTipoIdentificacion(CatalogosValores tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    /**
     * @return Devuelve el valor del campo idTipoActor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_ACTOR", nullable = false)
    public CatalogosValores getTipoActor() {
        return this.tipoActor;
    }

    /**
     * Establece el valor del campo idTipoActor de la entidad Interesado
     *
     * @param idTipoActor El valor a establecer
     */
    public void setTipoActor(CatalogosValores tipoActor) {
        this.tipoActor = tipoActor;
    }

    /**
     * @return Devuelve el valor del campo cdNumIden
     */

    @Column(name = "CD_NUM_IDEN")
    public String getCdNumIden() {
        return this.cdNumIden;
    }

    /**
     * Establece el valor del campo cdNumIden de la entidad Interesado
     *
     * @param cdNumIden El valor a establecer
     */
    public void setCdNumIden(String cdNumIden) {
        this.cdNumIden = cdNumIden;
    }

    /**
     * @return Devuelve el valor del campo dsApellido1
     */

    @Column(name = "DS_APELLIDO1", nullable = true)
    public String getDsApellido1() {
        return this.dsApellido1;
    }

    /**
     * Establece el valor del campo dsApellido1 de la entidad Interesado
     *
     * @param dsApellido1 El valor a establecer
     */
    public void setDsApellido1(String dsApellido1) {
        this.dsApellido1 = dsApellido1;
    }

    /**
     * @return Devuelve el valor del campo dsApellido2
     */

    @Column(name = "DS_APELLIDO2", nullable = true)
    public String getDsApellido2() {
        return this.dsApellido2;
    }
    

    /**
     * Establece el valor del campo dsApellido2 de la entidad Interesado
     *
     * @param dsApellido2 El valor a establecer
     */
    public void setDsApellido2(String dsApellido2) {
        this.dsApellido2 = dsApellido2;
    }

    /**
     * @return Devuelve el valor del campo dsNombre
     */

    @Column(name = "DS_NOMBRE")
    public String getDsNombre() {
        return this.dsNombre;
    }

    /**
     * Establece el valor del campo dsNombre de la entidad Interesado
     *
     * @param dsNombre El valor a establecer
     */
    public void setDsNombre(String dsNombre) {
        this.dsNombre = dsNombre;
    }

    /**
     * @return Devuelve el valor del campo fcNacimiento
     */

    @Column(name = "FC_NACIMIENTO")
    public Date getFcNacimiento() {
        return this.fcNacimiento;
    }

    /**
     * Establece el valor del campo fcNacimiento de la entidad Interesado
     *
     * @param fcNacimiento El valor a establecer
     */
    public void setFcNacimiento(Date fcNacimiento) {
        this.fcNacimiento = fcNacimiento;
    }

    /**
     * @return Devuelve el valor del campo cdSexo
     */

    @Column(name = "CD_SEXO")
    public String getCdSexo() {
        return this.cdSexo;
    }

    /**
     * Establece el valor del campo cdSexo de la entidad Interesado
     *
     * @param cdSexo El valor a establecer
     */
    public void setCdSexo(String cdSexo) {
        this.cdSexo = cdSexo;
    }

    /**
     * @return Devuelve el valor del campo dsEmail
     */

    @Column(name = "DS_EMAIL", nullable = true)
    public String getDsEmail() {
        return this.dsEmail;
    }

    /**
     * Establece el valor del campo dsEmail de la entidad Interesado
     *
     * @param dsEmail El valor a establecer
     */
    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    /**
     * @return Devuelve el valor del campo dsMovil
     */

    @Column(name = "DS_MOVIL", nullable = true)
    public String getDsMovil() {
        return this.dsMovil;
    }

    /**
     * Establece el valor del campo dsMovil de la entidad Interesado
     *
     * @param dsMovil El valor a establecer
     */
    public void setDsMovil(String dsMovil) {
        this.dsMovil = dsMovil;
    }

    /**
     * @return Devuelve el valor del campo dsTelefono
     */

    @Column(name = "DS_TELEFONO", nullable = true)
    public String getDsTelefono() {
        return this.dsTelefono;
    }

    /**
     * Establece el valor del campo dsTelefono de la entidad Interesado
     *
     * @param dsTelefono El valor a establecer
     */
    public void setDsTelefono(String dsTelefono) {
        this.dsTelefono = dsTelefono;
    }

    /**
     * @return Devuelve el valor del campo cdProvincia
     */

    @Column(name = "CD_PROVINCIA", nullable = true)
    public String getCdProvincia() {
        return this.cdProvincia;
    }

    /**
     * Establece el valor del campo cdProvincia de la entidad Interesado
     *
     * @param cdProvincia El valor a establecer
     */
    public void setCdProvincia(String cdProvincia) {
        this.cdProvincia = cdProvincia;
    }

    /**
     * @return Devuelve el valor del campo cdLocalidad
     */

    @Column(name = "CD_LOCALIDAD", nullable = true)
    public String getCdLocalidad() {
        return this.cdLocalidad;
    }

    /**
     * Establece el valor del campo cdLocalidad de la entidad Interesado
     *
     * @param cdLocalidad El valor a establecer
     */
    public void setCdLocalidad(String cdLocalidad) {
        this.cdLocalidad = cdLocalidad;
    }

    /**
     * @return Devuelve el valor del campo cdTipoVia
     */

    @Column(name = "CD_TIPO_VIA", nullable = true)
    public String getCdTipoVia() {
        return this.cdTipoVia;
    }

    /**
     * Establece el valor del campo cdTipoVia de la entidad Interesado
     *
     * @param cdTipoVia El valor a establecer
     */
    public void setCdTipoVia(String cdTipoVia) {
        this.cdTipoVia = cdTipoVia;
    }

    /**
     * @return Devuelve el valor del campo dsDireccion
     */

    @Column(name = "DS_DIRECCION", nullable = true)
    public String getDsDireccion() {
        return this.dsDireccion;
    }

    /**
     * Establece el valor del campo dsDireccion de la entidad Interesado
     *
     * @param dsDireccion El valor a establecer
     */
    public void setDsDireccion(String dsDireccion) {
        this.dsDireccion = dsDireccion;
    }

    /**
     * @return Devuelve el valor del campo cdCodigoPostal
     */

    @Column(name = "CD_CODIGO_POSTAL", nullable = true)
    public String getCdCodigoPostal() {
        return this.cdCodigoPostal;
    }

    /**
     * Establece el valor del campo cdCodigoPostal de la entidad Interesado
     *
     * @param cdCodigoPostal El valor a establecer
     */
    public void setCdCodigoPostal(String cdCodigoPostal) {
        this.cdCodigoPostal = cdCodigoPostal;
    }

    /**
     * @return Devuelve el valor del campo dsNumero
     */

    @Column(name = "DS_NUMERO", nullable = true)
    public String getDsNumero() {
        return this.dsNumero;
    }

    /**
     * Establece el valor del campo dsNumero de la entidad Interesado
     *
     * @param dsNumero El valor a establecer
     */
    public void setDsNumero(String dsNumero) {
        this.dsNumero = dsNumero;
    }

    /**
     * @return Devuelve el valor del campo dsPiso
     */

    @Column(name = "DS_PISO", nullable = true)
    public String getDsPiso() {
        return this.dsPiso;
    }

    /**
     * Establece el valor del campo dsPiso de la entidad Interesado
     *
     * @param dsPiso El valor a establecer
     */
    public void setDsPiso(String dsPiso) {
        this.dsPiso = dsPiso;
    }

    /**
     * @return Devuelve el valor del campo dsPuerta
     */

    @Column(name = "DS_PUERTA", nullable = true)
    public String getDsPuerta() {
        return this.dsPuerta;
    }

    /**
     * Establece el valor del campo dsPuerta de la entidad Interesado
     *
     * @param dsPuerta El valor a establecer
     */
    public void setDsPuerta(String dsPuerta) {
        this.dsPuerta = dsPuerta;
    }

    /**
     * @return Devuelve el valor del campo dsEscalera
     */

    @Column(name = "DS_ESCALERA", nullable = true)
    public String getDsEscalera() {
        return this.dsEscalera;
    }

    /**
     * Establece el valor del campo dsEscalera de la entidad Interesado
     *
     * @param dsEscalera El valor a establecer
     */
    public void setDsEscalera(String dsEscalera) {
        this.dsEscalera = dsEscalera;
    }

    /**
     * @return Devuelve el valor del campo itCoorEval
     */

    @Column(name = "IT_COOR_EVAL", nullable = true)
    public String getItCoorEval() {
        return this.itCoorEval;
    }

    /**
     * Establece el valor del campo itCoorEval de la entidad Interesado
     *
     * @param it_coor_eval El valor a establecer
     */
    public void setItCoorEval(String itCoorEval) {
        this.itCoorEval = itCoorEval;
    }

    /**
     * @return Devuelve el valor del campo dsDepartamento
     */

    @Column(name = "DS_DEPARTAMENTO", nullable = true)
    public String getDsDepartamento() {
        return this.dsDepartamento;
    }

    /**
     * Establece el valor del campo dsDepartamento de la entidad Interesado
     *
     * @param dsDepartamento El valor a establecer
     */
    public void setDsDepartamento(String dsDepartamento) {
        this.dsDepartamento = dsDepartamento;
    }

    /**
     * @return Devuelve el valor del campo nmSexenioInve
     */

    @Column(name = "NM_SEXENIO_INVE", nullable = true)
    public Integer getNmSexenioInve() {
        return this.nmSexenioInve;
    }

    /**
     * Establece el valor del campo nmSexenioInve de la entidad Interesado
     *
     * @param nmSexenioInve El valor a establecer
     */
    public void setNmSexenioInve(Integer nmSexenioInve) {
        this.nmSexenioInve = nmSexenioInve;
    }

    /**
     * @return Devuelve el valor del campo nmSexenioTran
     */

    @Column(name = "NM_SEXENIO_TRAN", nullable = true)
    public Integer getNmSexenioTran() {
        return this.nmSexenioTran;
    }

    /**
     * Establece el valor del campo nmSexenioTran de la entidad Interesado
     *
     * @param nmSexenioTran El valor a establecer
     */
    public void setNmSexenioTran(Integer nmSexenioTran) {
        this.nmSexenioTran = nmSexenioTran;
    }

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Interesado
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Interesado
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad Interesado
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad Interesado
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad Interesado
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad Interesado
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

    /**
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idInteresado")
    public List<PersonaArea> getListaPersonaArea() {
        return this.listaPersonaArea;
    }

    /**
     *
     * @param listaPersonaArea
     */
    public void setListaPersonaArea(List<PersonaArea> listaPersonaArea) {
        this.listaPersonaArea = listaPersonaArea;
    }

    /**
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idInteresado")
    public List<PersonaEspec> getListaPersonaEspecialidad() {
        return this.listaPersonaEspecialidad;
    }

    /**
     *
     * @param listaPersonaEspecialidad
     */
    public void setListaPersonaEspecialidad(List<PersonaEspec> listaPersonaEspecialidad) {
        this.listaPersonaEspecialidad = listaPersonaEspecialidad;
    }

    /**
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idInteresado")
    public List<PersonaOrg> getListaPersonaOrganismo() {
        return this.listaPersonaOrganismo;
    }

    /**
     *
     * @param listaPersonaOrganismo
     */
    public void setListaPersonaOrganismo(List<PersonaOrg> listaPersonaOrganismo) {
        this.listaPersonaOrganismo = listaPersonaOrganismo;
    }

    /**
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idInteresado")
    public List<PersonaTit> getListaPersonaTitulacion() {
        return this.listaPersonaTitulacion;
    }

    /**
     *
     * @param listaPersonaTitulacion
     */
    public void setListaPersonaTitulacion(List<PersonaTit> listaPersonaTitulacion) {
        this.listaPersonaTitulacion = listaPersonaTitulacion;
    }

    /**
     * @return Devuelve el valor del campo cdUsuario
     */
    @Column(name = "CD_USUARIO", nullable = true)
    public String getCdUsuario() {
        return this.cdUsuario;
    }

    /**
     * Establece el valor del campo cdUsuario de la entidad Interesado
     *
     * @param cdUsuario El valor a establecer
     */
    public void setCdUsuario(String cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    /**
     * @return Devuelve el valor del campo dsNacionalidad
     */
    @Column(name = "DS_NACIONALIDAD", nullable = true)
    public String getDsNacionalidad() {
        return dsNacionalidad;
    }
    
    /**
     * Establece el valor del campo dsNacionalidad de la entidad Interesado
     *
     * @param dsNacionalidad El valor a establecer
     */
    public void setDsNacionalidad(String dsNacionalidad) {
        this.dsNacionalidad = dsNacionalidad;
    }

    /**
     * @return Devuelve el valor del campo dsPaisResidencia
     */
    @Column(name = "DS_PAIS_RESID", nullable = true)
    public String getDsPaisResidencia() {
        return dsPaisResidencia;
    }

    /**
     * Establece el valor del campo dsPaisResidencia de la entidad Interesado
     *
     * @param dsPaisResidencia El valor a establecer
     */
    public void setDsPaisResidencia(String dsPaisResidencia) {
        this.dsPaisResidencia = dsPaisResidencia;
    }

}
