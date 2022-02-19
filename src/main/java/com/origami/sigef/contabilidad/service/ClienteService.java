/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.models.PubPersona;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Crizz Intriago
 */
@Stateless
public class ClienteService extends AbstractService<Cliente> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private CantonService cantonService;
    @Inject
    private UserSession session;
    @Inject
    private CatalogoService catalogoService;

    public ClienteService() {
        super(Cliente.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Cliente getFindCliente(String identificacion) {
        Cliente result = new Cliente();
        try {
            if (identificacion.length() > 10) {
                result = (Cliente) getEntityManager().createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion AND c.ruc=:ruc AND c.estado = TRUE")
                        .setParameter("identificacion", identificacion.substring(0, 10))
                        .setParameter("ruc", identificacion.substring(10, 13))
                        .getSingleResult();
            } else {
                result = (Cliente) getEntityManager().createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion AND c.estado = TRUE")
                        .setParameter("identificacion", identificacion)
                        .getSingleResult();
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private String getQueryHQLExisteCliente(Cliente c) {
        String query = null;
        if (c.getIdentificacion() != null) {
            return "SELECT p FROM Cliente p WHERE p.identificacion = :identificacion AND p.estado = TRUE";
        }
        return query;
    }

    public Cliente existeCliente(Cliente c) {
        String queryString = getQueryHQLExisteCliente(c);

        Map<String, Object> params = getParameter(c);

        final Query query = em.createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<Cliente> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(Cliente c) {

        Map<String, Object> params = new HashMap<>();
        if (c.getIdentificacion() != null) {
            params.put("identificacion", c.getIdentificacion());
        }
        return params;
    }

    public List<Cliente> getExisteCliente(Cliente c) {
        Query query = em.createNativeQuery("SELECT p FROM Cliente p WHERE p.identificacion = ?1 AND p.estado = TRUE")
                .setParameter(1, c.getIdentificacion());
        List<Cliente> result = query.getResultList();
        return result;
    }

    public Boolean validarCliente(Cliente c) {
        Query query = em.createNativeQuery("SELECT p FROM Cliente p WHERE p.identificacion = ?1 AND p.estado = TRUE")
                .setParameter(1, c.getIdentificacion());
        List<Cliente> result = query.getResultList();
        return result == null;
    }

    public List<Cliente> getClienteNoProNoSer() {
        Query query = em.createNativeQuery("Select * from Cliente cc where cc.estado = true AND cc.id not in(\n"
                + "Select c.id from Cliente c \n"
                + "where  exists (select p.cliente from Proveedor p where p.estado=true and p.cliente = c.id) "
                + "UNION\n"
                + "Select c.id from Cliente c \n"
                + "where exists (select p.persona from talento_humano.Servidor p where p.estado=true and p.fecha_salida IS null and p.persona = c.id))", Cliente.class);
        List<Cliente> result = query.getResultList();
        return result;
    }

    public Distributivo getuusuarioLogeadoCargo(String name) {
        try {
            if (name != null) {
                Query query = em.createNativeQuery("select ser.id,cargo.id,cargo.nombre_cargo,uni.id  from auth.usuarios u, talento_humano.servidor ser, talento_humano.th_servidor_cargo src,\n"
                        + "talento_humano.th_cargo cargo, unidad_administrativa uni \n"
                        + "where u.funcionario=ser.id and src.id_servidor=ser.id and src.id_cargo=cargo.id\n"
                        + "and cargo.id_unidad=uni.id and u.usuario=?1");
                List<Object[]> resultado = (List<Object[]>) query.setParameter(1, name).getResultStream().findFirst().orElse(null);

                if (resultado == null || resultado.isEmpty() || resultado.size() == 0) {
                    return null;
                }
                Object[] result = resultado.get(0);
                if (result != null) {
                    BigInteger idServevidor = (BigInteger) result[0];
                    BigInteger idCargo = (BigInteger) result[1];
                    String nombreCargo = (String) result[2];
                    BigInteger idUnidad = (BigInteger) result[3];

                    Servidor ser = em.find(Servidor.class, idServevidor.longValue());
                    UnidadAdministrativa unidad = em.find(UnidadAdministrativa.class, idUnidad.longValue());

                    return new Distributivo(ser, new Cargo(nombreCargo), unidad);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Distributivo getuusuarioLogeado(String name) {
        try {
            if (name != null) {
                /*  BigInteger idl = (BigInteger) em.createNativeQuery("select DISTINCT dis.id from auth.usuarios u \n"
                        + "	INNER JOIN talento_humano.servidor s on s.id=u.funcionario \n"
                        + "	inner join talento_humano.distributivo dis ON dis.id = s.distributivo\n"
                        + "	inner join public.unidad_administrativa un on un.id=dis.unidad_administrativa\n"
                        + "	where u.usuario= ?1 ").setParameter(1, name).getResultStream().findFirst().orElse(null);

                if (idl == null) {
                    return null;
                }
                 */

                Query query = em.createNativeQuery("select ser.id as idServidor,cargo.id as id_cargo,cargo.nombre_cargo,uni.id as id_unidad  from auth.usuarios u, talento_humano.servidor ser, talento_humano.th_servidor_cargo src,\n"
                        + "talento_humano.th_cargo cargo, unidad_administrativa uni \n"
                        + "where u.funcionario=ser.id and src.id_servidor=ser.id and src.id_cargo=cargo.id\n"
                        + "and cargo.id_unidad=uni.id and u.usuario=?1 and src.activo=true");
                List<Object[]> resultado = (List<Object[]>) query.setParameter(1, name).getResultList();
                if (resultado == null || resultado.isEmpty() || resultado.size() == 0) {
                    return null;
                }
                Object[] result = resultado.get(0);
//                System.out.println("Resultado obtener usuario logueado:"+result[0]);
                if (result != null) {
                    BigInteger idServevidor = (BigInteger) result[0];
                    BigInteger idCargo = (BigInteger) result[1];
                    String nombreCargo = (String) result[2];
                    BigInteger idUnidad = (BigInteger) result[3];

                    Servidor ser = em.find(Servidor.class, idServevidor.longValue());
                    UnidadAdministrativa unidad = em.find(UnidadAdministrativa.class, idUnidad.longValue());
                    Distributivo dist = new Distributivo(ser, new Cargo(nombreCargo), unidad);
//                    System.out.println("Distributivo:"+dist.toString());
                    return dist;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public UnidadAdministrativa getUnidadKardex() {
        try {
            UnidadAdministrativa result = (UnidadAdministrativa) em.createQuery("SELECT un FROM UnidadAdministrativa un WHERE un.codigo ='JA'").getSingleResult();
            return result;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }

    }

    public String getUSerLogeo(String cargo) {
        String result = (String) em.createNativeQuery("select u.usuario from auth.usuarios u, talento_humano.servidor ser, talento_humano.th_servidor_cargo src,\n"
                + "talento_humano.th_cargo cargo, unidad_administrativa uni \n"
                + "where u.funcionario=ser.id and src.id_servidor=ser.id and src.id_cargo=cargo.id\n"
                + "and cargo.id_unidad=uni.id and cargo.nombre_cargo=?1").setParameter(1, cargo).getSingleResult();

        return result;
    }

    public String getUsuarioNameUser(UnidadAdministrativa data) {
        String user = (String) em.createNativeQuery("select u.usuario from auth.usuarios u, talento_humano.servidor ser, talento_humano.th_servidor_cargo src,\n"
                + "talento_humano.th_cargo cargo, unidad_administrativa uni \n"
                + "where u.funcionario=ser.id and src.id_servidor=ser.id and src.id_cargo=cargo.id\n"
                + "and cargo.id_unidad=uni.id and u.id=?1").setParameter(1, data.getId()).getResultStream().findFirst().orElse(null);

        return user;
    }

    public Boolean verificarIdentificacion(String identificacion, CatalogoItem tipoIdentificacion) {
        try {
            if (tipoIdentificacion != null) {
                Cliente resultado = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion AND c.estado = TRUE AND c.tipoIdentificacion=:tipoIdentificacion")
                        .setParameter("identificacion", identificacion)
                        .setParameter("tipoIdentificacion", tipoIdentificacion)
                        .getSingleResult();
                return resultado != null;
            } else {
                Cliente resultado = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion AND c.estado = TRUE")
                        .setParameter("identificacion", identificacion)
                        .getSingleResult();
                return resultado != null;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Cliente verificarIdentificacionRuc(String identificacion) {
        try {
            Cliente resultado = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion")
                    .setParameter("identificacion", identificacion)
                    //                    .setParameter("ruc", identificacion.substring(11, 13))
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;

        }
    }

    public Cliente verificarIdentificacionRucRuc(String identificacion) {
        try {
            Cliente resultado = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion=:identificacion and c.ruc = :ruc")
                    .setParameter("identificacion", identificacion.substring(0, 10))
                    .setParameter("ruc", identificacion.substring(10, 13))
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;

        }
    }

    public Boolean verificarProveedor(Cliente cliente) {
        try {
            Cliente resultado = (Cliente) em.createQuery("SELECT p.cliente FROM Proveedor p WHERE p.cliente=:cliente AND p.estado = TRUE")
                    .setParameter("cliente", cliente)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean verificarServidor(Cliente persona) {
        try {
            Cliente resultado = (Cliente) em.createQuery("SELECT s.persona FROM Servidor s WHERE s.persona=:persona AND s.estado = TRUE")
                    .setParameter("persona", persona)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
        }
        return false;
    }

    public List<Cliente> listaClienteAndProvedor() {
        Query query
                = em.createQuery("SELECT cc FROM Cliente cc WHERE cc.estado = TRUE OR cc.id IN (SELECT cc.id FROM Cliente cc WHERE cc IN (SELECT p.cliente FROM Proveedor p WHERE p.estado = TRUE ) AND cc NOT IN (SELECT s.persona FROM Servidor s WHERE s.estado = TRUE))");
        List<Cliente> result = (List<Cliente>) query.getResultList();
        return result;
    }

    public String getrolsUser(String rol) {
        try {
            String user = (String) em.createNativeQuery("select u.usuario from auth.usuarios u inner join\n"
                    + "auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join catalogo_item ci  on r.categoria=ci.id\n"
                    + "where u.estado = true AND u.estado=true and ci.codigo= ?1 AND r.nombre <> 'admin_1'")
                    .setParameter(1, rol).getResultStream().findFirst().orElse("");
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    public List<String> getrolsUsers(String rol) {
        List<String> user = new ArrayList<>();
        try {
            user = (List<String>) em.createNativeQuery("select u.usuario from auth.usuarios u inner join\n"
                    + "auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join catalogo_item ci  on r.categoria=ci.id\n"
                    + "where u.estado = true AND u.estado=true and ci.codigo= ?1 AND r.nombre <> 'admin_1'")
                    .setParameter(1, rol).getResultList();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return user;
    }

    public Usuarios getrolsUsuario(String rol) {
        try {
            Usuarios user = (Usuarios) em.createNativeQuery("select u from auth.usuarios u inner join\n"
                    + "auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join catalogo_item ci  on r.categoria=ci.id\n"
                    + "where u.estado = true AND u.estado=true and ci.codigo= ?1 AND r.nombre <> 'admin_1'", Usuarios.class)
                    .setParameter(1, rol).getResultStream().findFirst().orElse(null);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigInteger getunidadRolUser(String userString) {

        BigInteger user = (BigInteger) em.createNativeQuery("select un.id from auth.usuarios u inner join\n"
                + "                                              auth.usuario_rol ur ON ur.usuario = u.id\n"
                + "                                              inner join auth.rol r ON r.id = ur.rol\n"
                + "                                               inner join public.unidad_administrativa un on\n"
                + "                                              r.unidad_administrativa=un.id\n"
                + "                							   inner join catalogo_item ci\n"
                + "                							   on r.categoria=ci.id\n"
                + "                							   where u.estado = true AND \n"
                + "                                                u.usuario=?1").setParameter(1, userString).getResultStream().findFirst().orElse(null);

        return user;

    }

    public BigInteger getunidadRolUserSinAdmin(String userString) {

        BigInteger user = (BigInteger) em.createNativeQuery("select un.id from auth.usuarios u inner join\n"
                + "                                              auth.usuario_rol ur ON ur.usuario = u.id\n"
                + "                                              inner join auth.rol r ON r.id = ur.rol\n"
                + "                                               inner join public.unidad_administrativa un on\n"
                + "                                              r.unidad_administrativa=un.id\n"
                + "                							   inner join catalogo_item ci\n"
                + "                							   on r.categoria=ci.id\n"
                + "                							   where u.estado = true AND \n"
                + "                                                u.usuario=?1 AND r.nombre != 'admin'").setParameter(1, userString).getResultStream().findFirst().orElse(null);

        return user;

    }

    public UnidadAdministrativa getUnidadPrincipalUSer(String data) {
        BigInteger d = getunidadRolUser(data);

        UnidadAdministrativa result = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.id= :id").setParameter("id", d.longValue()).getSingleResult();
        return result;

    }

    public UnidadAdministrativa getUnidadPrincipalUSerSinAdmin(String data) {
        BigInteger d = getunidadRolUserSinAdmin(data);

        UnidadAdministrativa result = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.id= :id").setParameter("id", d.longValue()).getSingleResult();
        return result;

    }

    public List<Rol> getRolCategoriaUnidad(String user) {
        try {
            List<Rol> catRol = (List<Rol>) em.createNativeQuery("select r.* from auth.usuarios u \n"
                    + "inner join auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join public.unidad_administrativa un on r.unidad_administrativa=un.id\n"
                    + "inner join catalogo_item ci on r.categoria=ci.id\n"
                    + "where u.estado = true AND u.usuario=?1 order by ci.codigo asc", Rol.class).setParameter(1, user).getResultList();
            return catRol;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Cliente> showServidores() {
        List<Cliente> result = (List<Cliente>) em.createQuery("SELECT s.persona FROM Servidor s ").getResultList();
        return result;
    }

    public Servidor getResponsableTransferencia(String codigoRol) {
        BigInteger user = (BigInteger) em.createNativeQuery("SELECT u.funcionario FROM auth.usuarios u\n "
                + "INNER JOIN auth.usuario_rol ur ON ur.usuario = u.id\n "
                + "INNER JOIN auth.rol r ON r.id = ur.rol\n "
                + "INNER JOIN catalogo_item ci ON r.categoria=ci.id\n "
                + "WHERE u.estado = true AND  ci.codigo= ?1").setParameter(1, codigoRol).getResultStream().findFirst().orElse(null);
        if (user != null) {
            Servidor usuario = (Servidor) em.createQuery("SELECT s FROM Servidor s WHERE s.id= :id")
                    .setParameter("id", user.longValue()).getResultStream().findFirst().orElse(null);
            return usuario;
        } else {
            return null;
        }
    }

    public Servidor getResponsableXRol(String codigoRol) {
        BigInteger user = (BigInteger) em.createNativeQuery("SELECT u.funcionario FROM auth.usuarios u\n "
                + "INNER JOIN auth.usuario_rol ur ON ur.usuario = u.id\n "
                + "INNER JOIN auth.rol r ON r.id = ur.rol\n "
                + "INNER JOIN catalogo_item ci ON r.categoria=ci.id\n "
                + "WHERE u.estado = true AND  ci.codigo= ?1 AND u.funcionario is not null").setParameter(1, codigoRol).getResultStream().findFirst().orElse(null);
        if (user != null) {
            Servidor usuario = (Servidor) em.createQuery("SELECT s FROM Servidor s WHERE s.id= :id")
                    .setParameter("id", user.longValue()).getResultStream().findFirst().orElse(null);
            return usuario;
        } else {
            return null;
        }
    }

    public List<Servidor> getListServidoresRol(String codigoRol) {
        List<Servidor> result = (List<Servidor>) em.createNativeQuery("SELECT u.funcionario FROM auth.usuarios u\n "
                + "INNER JOIN auth.usuario_rol ur ON ur.usuario = u.id\n "
                + "INNER JOIN auth.rol r ON r.id = ur.rol\n "
                + "INNER JOIN catalogo_item ci ON r.categoria=ci.id\n "
                + "WHERE u.estado = true AND  ci.codigo= ?1")
                .setParameter(1, codigoRol).getResultList();
        return result;
    }

    public List<Usuarios> getListClientesByCodeRol(String codeRol) {
        try {
            return em.createQuery("SELECT u  FROM UsuarioRol ur JOIN ur.usuario u JOIN ur.rol r JOIN r.categoria ci  WHERE ci.codigo = :codeRol AND u.estado = TRUE")
                    .setParameter("codeRol", codeRol)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserByContrato(BigInteger adq) {

        Servidor serv = (Servidor) em.createNativeQuery("SELECT s.* From activos.responsable_adquisicion r \n"
                + "inner join talento_humano.servidor s ON r.responsable = s.id \n"
                + "inner join activos.adquisiciones a ON a.id = r.adquisicion \n"
                + "WHERE a.id = ?1 AND r.estado = true AND  r.fecha_finalizacion IS NULL ", Servidor.class)
                .setParameter(1, adq.longValue()).getResultStream().findFirst().orElse(null);
        if (serv != null) {
            String user = (String) em.createNativeQuery("select u.usuario from auth.usuarios u inner join\n"
                    + "talento_humano.servidor s ON s.id = u.funcionario  \n"
                    + "where u.estado = true AND s.id = ?1").setParameter(1, serv.getId()).getResultStream().findFirst().orElse(null);

            return user;
        } else {
            return null;
        }
    }

    /**
     * El Objecto {@link Cliente} debe contener la propiedad
     * {@link Cliente#identificacion} llena
     *
     * @param cliente Entity a buscar
     * @param tipoBeneficiario True si el se quiere validar el tipo de documento
     * seleccionado, caso contrario false.
     * @return {@link Cliente} si existe, caso contrario el objecto
     * {@link Cliente} vacio.
     */
    public Cliente buscarCliente(Cliente cliente, Boolean tipoBeneficiario) {
        Cliente resultado = new Cliente();
        Boolean tipo = true;
        if (cliente.getTipoIdentificacion() != null) {
            tipo = cliente.getTipoIdentificacion().getCodigo().equals("C") || cliente.getTipoIdentificacion().getCodigo().equals("RUC");
        }
        if (tipo) {
            try {
                Boolean result = false;
                System.out.println("cliente.getTipoIdentificacion()" + cliente.getTipoIdentificacion());
                System.out.println("cliente.getIdentificacion()" + cliente.getIdentificacion());
                if (cliente.getTipoIdentificacion() != null) {
                    result = verificarIdentificacion(cliente.getIdentificacion().substring(0, 10), cliente.getTipoIdentificacion());
                } else {
                    result = verificarIdentificacion(cliente.getIdentificacion().substring(0, 10), null);
                }
                if (!result) {
                    String identificacion = cliente.getIdentificacion();
                    RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
                    URI uri = new URI(String.format(CONFIG.SERVICE_URL, identificacion));
                    try {
                        ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                        if (contribuyente != null && contribuyente.getBody() != null) {
                            PubPersona p = contribuyente.getBody();
                            if (p != null) {
                                if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                                    resultado = new Cliente();
                                    resultado.setIdentificacion(p.getIdentificacion());
                                    String code = (p.getIdentificacion().length() == 10) ? "C" : (p.getIdentificacion().length() == 13 ? "RUC" : "P");
                                    if (code.equals("RUC")) {
                                        resultado.setIdentificacion(identificacion.substring(0, 10));
                                        resultado.setRuc(identificacion.substring(10, 13));
                                    }
                                    resultado.setApellido(p.getApellidos().toUpperCase());
                                    resultado.setNombre(p.getNombres().toUpperCase());
                                    if (cliente.getTipoIdentificacion() != null) {
                                        resultado.setTipoIdentificacion(cliente.getTipoIdentificacion());
                                    } else {
                                        List<CatalogoItem> tiposDeIdentificacion = catalogoService.getItemsByCatalogo("tipo_identificacion_beneficiario");
                                        for (CatalogoItem ci : tiposDeIdentificacion) {
                                            if (ci.getCodigo().equals(code)) {
                                                resultado.setTipoIdentificacion(ci);
                                            }
                                        }
                                    }
                                    resultado.setDireccion(p.getDireccion());
                                    resultado.setEmail(p.getCorreo());
                                    if (p.getTelefono() != null) {
                                        if (p.getTelefono().contains(",")) {
                                            String[] tlfs = p.getTelefono().split(",");
                                            for (String tlf : tlfs) {
                                                if (tlf.length() < 10) {
                                                    resultado.setTelefono(tlf);
                                                } else {
                                                    resultado.setCelular(tlf);
                                                }
                                            }
                                        } else {
                                            if (p.getTelefono().length() < 10) {
                                                resultado.setTelefono(p.getTelefono());
                                            } else {
                                                resultado.setCelular(p.getTelefono());
                                            }
                                        }
                                    }
                                    resultado.setFechaCreacion(new Date());
                                    resultado.setUsuarioCreacion(session.getNameUser());
                                    if (p.getFechaNacimiento() != null) {
                                        resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                                    }
                                    if (p.getFechaExpedicion() != null) {
                                        resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion()));
                                    }
                                    resultado.setEstadoCivil(p.getEstadoCivil());
                                    resultado.setActaDefuncion(p.getActaDefuncion());
                                    resultado.setCondicionCiudadano(p.getCondicionCiudadano());
                                    resultado.setFechaDefuncion(p.getFechaDefuncion());
                                    resultado.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                                    resultado.setEdad(p.getEdad());
                                    resultado.setFechaExpedicion(p.getActaDefuncion());
                                    //   resultado.setRepresentanteLegal(p.getRepresentanteLegal());
                                    resultado.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                                    resultado.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                                    resultado.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                                    resultado.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                                    resultado.setIdentificacionContador(p.getIdentificacionContador());
                                    resultado.setApellidosContador(p.getApellidosContador());
                                    resultado.setNombresContador(p.getNombresContador());
                                    resultado.setCalificacionArtesanal(p.getCalificacionArtesanal());
                                    resultado.setObligadoContabilidad(p.getObligadoContabilidad());
                                    resultado.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                                    resultado.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                                    resultado.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                                    resultado.setFechaCambioObligado(p.getFechaCambioObligado());
                                    resultado.setActividadGeneral(p.getActividadGeneral());

                                    if (p.getCanton() != null) {
                                        try {
//                                                Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", split[1].replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "_"));
                                            Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                            if (canton != null) {
                                                resultado.setCanton(canton);
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                        }
                                    }
                                    //resultado = create(resultado);
                                }
                            }
                        }
                    } catch (RestClientException rce) {
                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, rce);
                        return new Cliente();
                    } catch (ParseException ex) {
                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
                        return new Cliente();
                    }
                } else {
                    if (cliente.getTipoIdentificacion() != null) {
                        resultado = findByNamedQuery1("Cliente.findByIdCliente_2", cliente.getIdentificacion().substring(0, 10), cliente.getTipoIdentificacion());
                    } else {
                        resultado = findByNamedQuery1("Cliente.findByIdCliente_3", cliente.getIdentificacion());
                    }
                    if (resultado == null) {
                        resultado = new Cliente();
                    }
                }
                return resultado;
            } catch (URISyntaxException | RestClientException e) {
                Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Busqueda de cliente", e);
            }
            return resultado;
        }
        return resultado;
    }

    /**
     * El Objecto {@link Cliente} debe contener la propiedad
     * {@link Cliente#identificacion} llena
     *
     * @param identificacion
     * @param cliente Entity a buscar
     * @param tipoBeneficiario True si el se quiere validar el tipo de documento
     * seleccionado, caso contrario false.
     * @return {@link Cliente} si existe, caso contrario el objecto
     * {@link Cliente} vacio.
     */
    public Cliente buscarCliente(String identificacion) {
        Cliente resultado = null;
        System.out.println("identificacion>>" + identificacion);
        Boolean persona = (identificacion.length() == 10) ? true : (identificacion.length() != 13);
        try {
            Boolean result = false;
            String code = (identificacion.length() == 10) ? "C" : (identificacion.length() == 13 ? "RUC" : "P");
            CatalogoItem tipoPersona = catalogoService.getItemByCatalogoAndCodigo("tipo_identificacion_beneficiario", code);
            if (persona) {
                result = verificarIdentificacion(identificacion, tipoPersona);
            } else {
                result = verificarIdentificacion(identificacion, null);
            }

            if (!result) {
                RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
                URI uri = new URI(String.format(CONFIG.SERVICE_URL, identificacion));
                try {
                    ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                    if (contribuyente != null && contribuyente.getBody() != null) {
                        PubPersona p = contribuyente.getBody();
//                        System.out.println(p.getNombres() + "PubPersona ");
                        if (p != null) {
                            if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                                resultado = new Cliente();
                                resultado.setIdentificacion(p.getIdentificacion());
                                if (code.equals("RUC")) {
                                    resultado.setIdentificacion(identificacion);
                                    resultado.setRuc(identificacion.substring(10, 13));
                                }
                                resultado.setApellido(p.getApellidos().toUpperCase());
                                resultado.setNombre(p.getNombres().toUpperCase());
                                resultado.setTipoIdentificacion(tipoPersona);
                                resultado.setDireccion(p.getDireccion());
                                resultado.setEmail(p.getCorreo());
                                if (p.getTelefono() != null) {
                                    if (p.getTelefono().contains(",")) {
                                        String[] tlfs = p.getTelefono().split(",");
                                        for (String tlf : tlfs) {
                                            if (tlf.length() < 10) {
                                                resultado.setTelefono(tlf);
                                            } else {
                                                resultado.setCelular(tlf);
                                            }
                                        }
                                    } else {
                                        if (p.getTelefono().length() < 10) {
                                            resultado.setTelefono(p.getTelefono());
                                        } else {
                                            resultado.setCelular(p.getTelefono());
                                        }
                                    }
                                }
                                resultado.setFechaCreacion(new Date());
                                resultado.setUsuarioCreacion(session.getNameUser());
                                if (p.getFechaNacimiento() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                                }
                                if (p.getFechaExpedicion() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion()));
                                }
                                resultado.setEstadoCivil(p.getEstadoCivil());
                                resultado.setActaDefuncion(p.getActaDefuncion());
                                resultado.setCondicionCiudadano(p.getCondicionCiudadano());
                                resultado.setFechaDefuncion(p.getFechaDefuncion());
                                resultado.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                                resultado.setEdad(p.getEdad());
                                resultado.setFechaExpedicion(p.getActaDefuncion());
                                //     resultado.setRepresentanteLegal(p.getRepresentanteLegal());
                                resultado.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                                resultado.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                                resultado.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                                resultado.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                                resultado.setIdentificacionContador(p.getIdentificacionContador());
                                resultado.setApellidosContador(p.getApellidosContador());
                                resultado.setNombresContador(p.getNombresContador());
                                resultado.setCalificacionArtesanal(p.getCalificacionArtesanal());
                                resultado.setObligadoContabilidad(p.getObligadoContabilidad());
                                resultado.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                                resultado.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                                resultado.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                                resultado.setFechaCambioObligado(p.getFechaCambioObligado());
                                resultado.setActividadGeneral(p.getActividadGeneral());

                                if (p.getCanton() != null) {
                                    try {
                                        Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                        if (canton != null) {
                                            resultado.setCanton(canton);
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                    }
                                }
                            }
                        }
                    }
                } catch (RestClientException | ParseException rce) {
                    Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, rce);
                    return null;
                }
            } else {
                if (tipoPersona != null) {
                    resultado = findByNamedQuery1("Cliente.findByIdCliente_2", identificacion.substring(0, 10), tipoPersona);
                }
            }
            return resultado;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Busqueda de cliente", e);
        }
        return resultado;
    }

    public Cliente getClienteEspecificos(String rol) {
        try {
            Cliente cliente = (Cliente) em.createNativeQuery("select cl.* from auth.usuarios u inner join\n"
                    + "auth.usuario_rol ur ON ur.usuario = u.id\n"
                    + "inner join auth.rol r ON r.id = ur.rol\n"
                    + "inner join catalogo_item ci on r.categoria=ci.id\n"
                    + "inner join talento_humano.servidor ser on u.funcionario=ser.id\n"
                    + "inner join cliente cl on ser.persona=cl.id\n"
                    + "Where u.estado = true AND u.estado=true and ci.codigo=?1", Cliente.class).setParameter(1, rol).getResultStream().findFirst().orElse(null);
            return cliente;
        } catch (Exception e) {
            return null;
        }
    }

    public Usuarios getUsuarioServidor(String user) {
        try {
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.estado = TRUE AND u.usuario = ?1")
                    .setParameter(1, user);
            Usuarios usuarios = (Usuarios) query.getSingleResult();
            return usuarios;
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getUsuarioNombre(String user) {
        try {
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.estado = TRUE AND u.usuario = ?1")
                    .setParameter(1, user);
            Usuarios usuarios = (Usuarios) query.getSingleResult();

            if (usuarios != null && usuarios.getFuncionario() != null && usuarios.getFuncionario().getPersona() != null) {
                return usuarios.getFuncionario().getPersona().getNombreCompleto();
            }

            return "";
        } catch (NoResultException e) {
            return "";
        }
    }

    public UnidadAdministrativa unidadRol(String user) {
        List<UnidadAdministrativa> r = (List<UnidadAdministrativa>) em.createNativeQuery("select uni.* from auth.usuarios u inner join auth.usuario_rol  ur on ur.usuario=u.id\n"
                + "inner join auth.rol r on ur.rol=r.id \n"
                + "inner join unidad_administrativa uni on r.unidad_administrativa=uni.id\n"
                + "where u.usuario=?1", UnidadAdministrativa.class)
                .setParameter(1, user).getResultList();

        if (!r.isEmpty()) {
            return r.get(0);
        }
        return null;
    }

    public int updateCliente(String sql, BigInteger idActual, BigInteger idCondicion) {
        Query query2 = em.createNativeQuery(sql)
                .setParameter(1, idActual).setParameter(2, idCondicion);
        return query2.executeUpdate();
    }

    public List<Usuarios> listUserValidador(String tipo) {
        return (List<Usuarios>) em.createQuery("SELECT u FROM Usuarios u inner join u.funcionario f inner join f.persona p where p.tipoPersona=:tipo")
                .setParameter("tipo", tipo).getResultList();
    }

    public int registroAisgnados(Usuarios u, BigInteger idInicio, BigInteger idFin) {
        Query query = em.createQuery("UPDATE Cliente SET valiadorAsignado=:usuario where id BETWEEN :idInicio and :idFin")
                .setParameter("usuario", u).setParameter("idInicio", idInicio.longValue()).setParameter("idFin", idFin.longValue());
        return query.executeUpdate();
    }

    public Boolean isAdmin(List<UsuarioRol> usuarioRols) {

        if (!usuarioRols.isEmpty()) {
            for (UsuarioRol usuarioRol : usuarioRols) {
                if ("1".equals(usuarioRol.getRol().getCategoria().getCodigo())) {
                    return true;
                }
            }
        }

        return false;
    }

    public Cliente buscarClienteValidador(String identificacion) {
        Cliente resultado = null;
        Boolean persona = (identificacion.length() == 10) ? true : (identificacion.length() != 13);
        try {
            Boolean result = false;
            String code = (identificacion.length() == 10) ? "C" : (identificacion.length() == 13 ? "RUC" : "P");
            CatalogoItem tipoPersona = catalogoService.getItemByCatalogoAndCodigo("tipo_identificacion_beneficiario", code);
            if (persona) {
                result = verificarIdentificacion(identificacion, tipoPersona);
            } else {
                result = verificarIdentificacion(identificacion, null);
            }

            result = false;
            if (!result) {
                RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
                URI uri = new URI(String.format(CONFIG.SERVICE_URL, identificacion));
                try {
                    ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                    if (contribuyente != null && contribuyente.getBody() != null) {
                        PubPersona p = contribuyente.getBody();
                        System.out.println(p.getNombres() + "PubPersona ");
                        if (p != null) {

                            if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                                resultado = new Cliente();
                                resultado.setIdentificacion(p.getIdentificacion());
                                if (code.equals("RUC")) {
                                    resultado.setIdentificacion(identificacion);
                                    resultado.setRuc(identificacion.substring(10, 13));
                                }
                                resultado.setApellido(p.getApellidos().toUpperCase());
                                resultado.setNombre(p.getNombres().toUpperCase());
                                resultado.setTipoIdentificacion(tipoPersona);
                                resultado.setDireccion(p.getDireccion());
                                resultado.setEmail(p.getCorreo());
                                if (p.getTelefono() != null) {
                                    if (p.getTelefono().contains(",")) {
                                        String[] tlfs = p.getTelefono().split(",");
                                        for (String tlf : tlfs) {
                                            if (tlf.length() < 10) {
                                                resultado.setTelefono(tlf);
                                            } else {
                                                resultado.setCelular(tlf);
                                            }
                                        }
                                    } else {
                                        if (p.getTelefono().length() < 10) {
                                            resultado.setTelefono(p.getTelefono());
                                        } else {
                                            resultado.setCelular(p.getTelefono());
                                        }
                                    }
                                }
                                resultado.setFechaCreacion(new Date());
                                resultado.setUsuarioCreacion(session.getNameUser());
                                if (p.getFechaNacimiento() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                                }
                                if (p.getFechaExpedicion() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion()));
                                }
                                resultado.setEstadoCivil(p.getEstadoCivil());
                                resultado.setActaDefuncion(p.getActaDefuncion());
                                resultado.setCondicionCiudadano(p.getCondicionCiudadano());
                                resultado.setFechaDefuncion(p.getFechaDefuncion());
                                resultado.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                                resultado.setEdad(p.getEdad());
                                resultado.setFechaExpedicion(p.getActaDefuncion());
                                //     resultado.setRepresentanteLegal(p.getRepresentanteLegal());
                                resultado.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                                resultado.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                                resultado.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                                resultado.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                                resultado.setIdentificacionContador(p.getIdentificacionContador());
                                resultado.setApellidosContador(p.getApellidosContador());
                                resultado.setNombresContador(p.getNombresContador());
                                resultado.setCalificacionArtesanal(p.getCalificacionArtesanal());
                                resultado.setObligadoContabilidad(p.getObligadoContabilidad());
                                resultado.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                                resultado.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                                resultado.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                                resultado.setFechaCambioObligado(p.getFechaCambioObligado());
                                resultado.setActividadGeneral(p.getActividadGeneral());

                                if (p.getCanton() != null) {
                                    try {
                                        Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                        if (canton != null) {
                                            resultado.setCanton(canton);
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                    }
                                }
                            }
                        }
                    }
                } catch (RestClientException | ParseException rce) {
                    Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, rce);
                    return null;
                }
            } else {
                if (tipoPersona != null) {
                    resultado = findByNamedQuery1("Cliente.findByIdCliente_2", identificacion.substring(0, 10), tipoPersona);
                }
            }
            return resultado;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Busqueda de cliente", e);
        }
        return resultado;
    }

    public Cliente buscarClienteData(Cliente object, Boolean update) {
        Cliente resultado = object;
        Boolean persona = (object.getIdentificacion().length() == 10) ? true : (object.getIdentificacion().length() != 13);
        try {
            Boolean result = true;
            String code = (object.getIdentificacion().length() == 10) ? "C" : (object.getIdentificacion().length() == 13 ? "RUC" : "P");
            CatalogoItem tipoPersona = catalogoService.getItemByCatalogoAndCodigo("tipo_identificacion_beneficiario", code);

            if (result) {
                RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
                URI uri = new URI(String.format(CONFIG.SERVICE_URL, object.getIdentificacion()));
                try {
                    ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                    if (contribuyente != null && contribuyente.getBody() != null) {
                        PubPersona p = contribuyente.getBody();
                        System.out.println(p.getNombres() + "PubPersona ");
                        if (p != null) {

                            if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                                resultado.setApellido(p.getApellidos().toUpperCase());
                                resultado.setNombre(p.getNombres().toUpperCase());
                                resultado.setTipoIdentificacion(tipoPersona);
                                resultado.setDireccion(p.getDireccion());
                                resultado.setEmail(p.getCorreo());
                                if (p.getTelefono() != null) {
                                    if (p.getTelefono().contains(",")) {
                                        String[] tlfs = p.getTelefono().split(",");
                                        for (String tlf : tlfs) {
                                            if (tlf.length() < 10) {
                                                resultado.setTelefono(tlf);
                                            } else {
                                                resultado.setCelular(tlf);
                                            }
                                        }
                                    } else {
                                        if (p.getTelefono().length() < 10) {
                                            resultado.setTelefono(p.getTelefono());
                                        } else {
                                            resultado.setCelular(p.getTelefono());
                                        }
                                    }
                                }
                                if (p.getFechaNacimiento() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                                }
                                if (p.getFechaExpedicion() != null) {
                                    resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion()));
                                }
                                resultado.setEstadoCivil(p.getEstadoCivil());
                                resultado.setActaDefuncion(p.getActaDefuncion());
                                resultado.setCondicionCiudadano(p.getCondicionCiudadano());
                                resultado.setFechaDefuncion(p.getFechaDefuncion());
                                resultado.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                                resultado.setEdad(p.getEdad());
                                resultado.setFechaExpedicion(p.getActaDefuncion());
                                //     resultado.setRepresentanteLegal(p.getRepresentanteLegal());
                                resultado.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                                resultado.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                                resultado.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                                resultado.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                                resultado.setIdentificacionContador(p.getIdentificacionContador());
                                resultado.setApellidosContador(p.getApellidosContador());
                                resultado.setNombresContador(p.getNombresContador());
                                resultado.setCalificacionArtesanal(p.getCalificacionArtesanal());
                                resultado.setObligadoContabilidad(p.getObligadoContabilidad());
                                resultado.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                                resultado.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                                resultado.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                                resultado.setFechaCambioObligado(p.getFechaCambioObligado());
                                resultado.setActividadGeneral(p.getActividadGeneral());

                                if (p.getCanton() != null) {
                                    try {
                                        Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                        if (canton != null) {
                                            resultado.setCanton(canton);
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                    }
                                }
                            }
                        }
                    }
                } catch (RestClientException | ParseException rce) {
                    Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, rce);
                    return null;
                }
            }
            if (update) {
                this.edit(resultado);
            }
            return resultado;
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Busqueda de cliente", e);
        }
        return resultado;
    }

    public Cliente obtClienteByUsuario(String usuario) {
        Cliente result = new Cliente();
        try {
            result = (Cliente) em.createNativeQuery("SELECT cli.* FROM auth.usuarios us\n"
                    + "INNER JOIN talento_humano.servidor ser ON us.funcionario=ser.id\n"
                    + "INNER JOIN cliente cli ON ser.persona = cli.id\n"
                    + "WHERE us.usuario = :usuario")
                    .setParameter("usuario", usuario)
                    .getSingleResult();
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    public Boolean isUnidadAdministrativa(String upperPresupuesto, String username) {
        if (username == null || username.equals(null)) {
            return false;
        }
        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("SELECT u.id\n"
                + "FROM auth.usuarios u \n"
                + "INNER JOIN auth.usuario_rol ur ON ur.usuario = u.id \n"
                + "INNER JOIN auth.rol r ON r.id = ur.rol\n"
                + "INNER JOIN unidad_administrativa ua ON ua.id = r.unidad_administrativa\n"
                + "WHERE TRIM(UPPER(ua.nombre))=?1 AND TRIM(UPPER(u.usuario))=?2")
                .setParameter(1, upperPresupuesto.trim().toUpperCase()).setParameter(2, username.trim().toUpperCase()).getResultList();
        if (result == null || result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public Cliente buscarClienteDinarDap(String identificacion) {
        try {
            Cliente resultado = null;
            RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
            URI uri = new URI(String.format(CONFIG.SERVICE_URL, identificacion));
            try {
                ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                if (contribuyente != null && contribuyente.getBody() != null) {
                    PubPersona p = contribuyente.getBody();
                    if (p != null) {
                        if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                            resultado = new Cliente();
                            resultado.setIdentificacion(p.getIdentificacion());
                            if (p.getTipoDocumento().equals("RUC")) {
                                resultado.setIdentificacion(identificacion.substring(0, 10));
                                resultado.setRuc(identificacion.substring(10, 13));
                            }
                            resultado.setApellido(p.getApellidos().toUpperCase());
                            resultado.setNombre(p.getNombres().toUpperCase());
//                                resultado.setTipoIdentificacion(tipoPersona);
                            resultado.setDireccion(p.getDireccion());
                            resultado.setEmail(p.getCorreo());
                            if (p.getTelefono() != null) {
                                if (p.getTelefono().contains(",")) {
                                    String[] tlfs = p.getTelefono().split(",");
                                    for (String tlf : tlfs) {
                                        if (tlf.length() < 10) {
                                            resultado.setTelefono(tlf);
                                        } else {
                                            resultado.setCelular(tlf);
                                        }
                                    }
                                } else {
                                    if (p.getTelefono().length() < 10) {
                                        resultado.setTelefono(p.getTelefono());
                                    } else {
                                        resultado.setCelular(p.getTelefono());
                                    }
                                }
                            }
                            resultado.setFechaCreacion(new Date());
                            resultado.setUsuarioCreacion(session.getNameUser());
                            if (p.getFechaNacimiento() != null) {
                                resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                            }
                            if (p.getFechaExpedicion() != null) {
                                resultado.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion()));
                            }
                            resultado.setEstadoCivil(p.getEstadoCivil());
                            resultado.setActaDefuncion(p.getActaDefuncion());
                            resultado.setCondicionCiudadano(p.getCondicionCiudadano());
                            resultado.setFechaDefuncion(p.getFechaDefuncion());
                            resultado.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                            resultado.setEdad(p.getEdad());
                            resultado.setFechaExpedicion(p.getActaDefuncion());
                            //     resultado.setRepresentanteLegal(p.getRepresentanteLegal());
                            resultado.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                            resultado.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                            resultado.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                            resultado.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                            resultado.setIdentificacionContador(p.getIdentificacionContador());
                            resultado.setApellidosContador(p.getApellidosContador());
                            resultado.setNombresContador(p.getNombresContador());
                            resultado.setCalificacionArtesanal(p.getCalificacionArtesanal());
                            resultado.setObligadoContabilidad(p.getObligadoContabilidad());
                            resultado.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                            resultado.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                            resultado.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                            resultado.setFechaCambioObligado(p.getFechaCambioObligado());
                            resultado.setActividadGeneral(p.getActividadGeneral());
                            if (p.getCanton() != null) {
                                try {
                                    Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                    if (canton != null) {
                                        resultado.setCanton(canton);
                                    }
                                } catch (Exception e) {
                                    Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                }
                            }
                        }
                    }
                }
            } catch (RestClientException | ParseException rce) {
                System.out.println("404 dinardap " + identificacion);
                return null;
            }
            return resultado;
        } catch (URISyntaxException | RestClientException e) {
            return null;
        }
    }

    public Cliente findByCliente(Cliente findCliente) {
        String identificacion = Utils.clone(findCliente.getIdentificacion());
        if (findCliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            identificacion = identificacion.substring(0, 10);
        }
        Cliente result_1 = findByNamedQuery1("Cliente.findByClienteCedulaRucOtros", identificacion, findCliente.getTipoIdentificacion());
        if (result_1 != null) {
            return result_1;
        } else {
            Cliente result_2 = new Cliente();
            Boolean tipo = true;
            if (findCliente.getTipoIdentificacion() != null) {
                tipo = findCliente.getTipoIdentificacion().getCodigo().equals("C") || findCliente.getTipoIdentificacion().getCodigo().equals("RUC");
            }
            if (tipo) {
                try {
                    System.out.println("*****DINARDAP****");
                    System.out.println("Tipo identificación: " + findCliente.getTipoIdentificacion());
                    System.out.println("No. identificación: " + findCliente.getIdentificacion());
                    System.out.println("*****************");
                    RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(CONFIG.SERVICE_USER, CONFIG.SERVICE_PASS));
                    URI uri = new URI(String.format(CONFIG.SERVICE_URL, findCliente.getIdentificacion()));
                    try {
                        ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
                        if (contribuyente != null && contribuyente.getBody() != null) {
                            PubPersona p = contribuyente.getBody();
                            if (p != null) {
                                if (p.getIdentificacion() != null && !p.getIdentificacion().isEmpty()) {
                                    System.out.println("INT: " + p.getIdentificacion());
                                    result_2.setIdentificacion(p.getIdentificacion());
                                    result_2.setApellido(p.getApellidos().toUpperCase());
                                    result_2.setNombre(p.getNombres().toUpperCase());
                                    result_2.setTipoIdentificacion(findCliente.getTipoIdentificacion());
                                    result_2.setDireccion(p.getDireccion());
                                    result_2.setEmail(p.getCorreo());
                                    if (p.getTelefono() != null) {
                                        if (p.getTelefono().contains(",")) {
                                            String[] tlfs = p.getTelefono().split(",");
                                            for (String tlf : tlfs) {
                                                if (tlf.length() < 10) {
                                                    result_2.setTelefono(tlf);
                                                } else {
                                                    result_2.setCelular(tlf);
                                                }
                                            }
                                        } else {
                                            if (p.getTelefono().length() < 10) {
                                                result_2.setTelefono(p.getTelefono());
                                            } else {
                                                result_2.setCelular(p.getTelefono());
                                            }
                                        }
                                    }
                                    result_2.setFechaCreacion(new Date());
                                    result_2.setUsuarioCreacion(session.getNameUser());
                                    if (p.getFechaNacimiento() != null) {
                                        result_2.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento()));
                                    }
                                    if (p.getFechaExpedicion() != null) {
                                        result_2.setFechaExpedicion(Utils.convertirFechaLetra(new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaExpedicion())));
                                    }
                                    result_2.setEstadoCivil(p.getEstadoCivil());
                                    result_2.setActaDefuncion(p.getActaDefuncion());
                                    result_2.setCondicionCiudadano(p.getCondicionCiudadano());
                                    result_2.setFechaDefuncion(p.getFechaDefuncion());
                                    result_2.setFechaInscripcionDefuncion(p.getFechaInscripcionDefuncion());
                                    result_2.setEdad(p.getEdad());
                                    result_2.setFechaExpedicion(p.getActaDefuncion());
                                    result_2.setIdentificacionRepresentanteLegal(p.getIdentificacionRepresentanteLegal());
                                    result_2.setApellidosRepresentanteLegal(p.getApellidosRepresentanteLegal());
                                    result_2.setNombresRepresentanteLegal(p.getNombresRepresentanteLegal());
                                    result_2.setCargoRepresentanteLegal(p.getCargoRepresentanteLegal());
                                    result_2.setIdentificacionContador(p.getIdentificacionContador());
                                    result_2.setApellidosContador(p.getApellidosContador());
                                    result_2.setNombresContador(p.getNombresContador());
                                    result_2.setCalificacionArtesanal(p.getCalificacionArtesanal());
                                    result_2.setObligadoContabilidad(p.getObligadoContabilidad());
                                    result_2.setNombreAgenteRetencion(p.getNombreAgenteRetencion());
                                    result_2.setFechaAltaParaEspecial(p.getFechaAltaParaEspecial());
                                    result_2.setFechaCalificacionArtesanal(p.getFechaCalificacionArtesanal());
                                    result_2.setFechaCambioObligado(p.getFechaCambioObligado());
                                    result_2.setActividadGeneral(p.getActividadGeneral());
                                    if (p.getCanton() != null) {
                                        try {
                                            Canton canton = cantonService.findByNamedQuery1("Canton.findByCodigo", p.getCanton().getCodigo());
                                            if (canton != null) {
                                                result_2.setCanton(canton);
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Find Canton Cliente", e);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (RestClientException rce) {
                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, rce);
                        return new Cliente(findCliente.getIdentificacion(), findCliente.getTipoIdentificacion());
                    } catch (ParseException ex) {
                        Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
                        return new Cliente(findCliente.getIdentificacion(), findCliente.getTipoIdentificacion());
                    }
                } catch (URISyntaxException | RestClientException e) {
                    Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, "Busqueda de cliente", e);
                }
            } else {
                return new Cliente(findCliente.getIdentificacion(), findCliente.getTipoIdentificacion());
            }
            if (result_2.getNombre() != null) {
                return result_2;
            } else {
                return new Cliente(findCliente.getIdentificacion(), findCliente.getTipoIdentificacion());
            }

        }
    }

    public Cliente getClienteByIdentificacionCompleta(String identificacion, CatalogoItem tipoid) {
        try {
//            return (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.tipoIdentificacion = ?2 AND CONCAT(c.identificacion,c.ruc) = ?1 AND CASE WHEN c.validado IS NULL THEN c.estado = TRUE ELSE c.validado = TRUE AND c.estado = TRUE END, ORDER BY c.id DESC ")
            return (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.tipoIdentificacion = ?2 AND CONCAT(c.identificacion,c.ruc) = ?1 AND c.validado = TRUE AND c.estado = TRUE ORDER BY c.id DESC ")
                    .setParameter(1, identificacion).setParameter(2, tipoid).getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
