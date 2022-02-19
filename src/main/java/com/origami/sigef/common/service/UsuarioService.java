/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
@Transactional
public class UsuarioService extends AbstractService<Usuarios> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession userSession;

    private static final Logger LOG = Logger.getLogger(UsuarioService.class.getName());

    public UsuarioService() {
        super(Usuarios.class);
    }

    @Override
    public EntityManager getEntityManager() {
//        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
//    return em;
        return em;
    }

    public Usuarios findByUsuario(String user) {
        try {
            Usuarios usuario = (Usuarios) getEntityManager().createQuery("SELECT d FROM Usuarios d WHERE d.usuario=:user AND d.estado = TRUE")
                    .setParameter("user", user).getSingleResult();
//            getEntityManager().refresh(usuario.getRoles());
            return usuario;

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuarios> findAllUser() {
        List<Usuarios> usuarios = new ArrayList<>();
        try {
            usuarios = em.createQuery("Select a from Usuarios a where a.estado=true and a.funcionario is not null").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer la lista de usuarios", ex);
        }
        return usuarios;
    }

    public Usuarios findByUsuarioId(Long id) {
        try {
            Usuarios usuario = (Usuarios) getEntityManager().createQuery("SELECT d FROM Usuarios d WHERE d.id=:id AND d.estado = TRUE")
                    .setParameter("id", id).getSingleResult();
//            getEntityManager().refresh(usuario.getRoles());
            return usuario;

        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuarios save(Usuarios usuario) {
        try {
            if (usuario.getId() == null) {
                usuario.setFechaIngreso(new Date());
                usuario.setUsuarioIngreso(userSession.getNameUser());
                if (usuario.getEstado() == null) {
                    usuario.setEstado(Boolean.TRUE);
                }
                usuario = this.create(usuario);
            } else {
                try {
                    this.edit(usuario);
                } catch (EntityNotFoundException e) {
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar usuario", e);
        }
        return usuario;
    }

    public void saveUsuarioRol(List<UsuarioRol> roles) {
        try {
            for (UsuarioRol rol : roles) {
                if (rol.getId() == null) {
                    getEntityManager().persist(rol);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar usuario roles", e);
        }
    }

    public boolean removeUsuarioRol(UsuarioRol ur) {
        try {
            ur = getEntityManager().find(UsuarioRol.class, ur.getId());
            getEntityManager().remove(ur);

            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar usuario roles", e);
        }
        return false;
    }

    public Usuarios findByIdentificacion(String identificacion) {
        return findByNamedQuery1("Usuario.findByFuncionarioIdentifivacion", new Object[]{identificacion});
    }

    public Boolean findByFuncionario(Servidor serv) {
//        return findByNamedQuery1("Usuario.findByFuncionario", new Object[]{serv});
        try {
            Usuarios user = (Usuarios) em.createQuery("SELECT ur FROM Usuarios ur WHERE ur.funcionario.id = ?1 AND ur.estado = TRUE")
                    .setParameter(1, serv.getId()).getSingleResult();
            return user != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public List<Usuarios> findAllOrderActivos() {
        try {
            return findByNamedQuery("Usuario.findByEstado", null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar usuario roles", e);
        }
        return null;
    }

    public DatosGeneralesEntidad findEmpresa(Usuarios usuario) {
        try {
            return (DatosGeneralesEntidad) findByNamedQuery1("Usuario.findEmpresaByUsuario", usuario.getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Buscar Empresa por el usuario", e);
        }
        return null;
    }

    public Usuarios findByIdentificacionEnte(String identificacion) {
        Usuarios usuario = null;
        try {
            usuario = (Usuarios) em.createQuery("Select a from Usuarios a where a.ente.identificacion=:identificacion").setParameter("identificacion", identificacion).getSingleResult();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return usuario;
    }
}
