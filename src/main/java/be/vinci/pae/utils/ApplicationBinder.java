package be.vinci.pae.utils;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.DomainFactoryImpl;
import be.vinci.pae.business.ucc.ItemUCC;
import be.vinci.pae.business.ucc.ItemUCCImpl;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.NotificationUCCImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.dal.ItemDAO;
import be.vinci.pae.dal.ItemDAOImpl;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.NotificationDAOImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALBackendServices;
import be.vinci.pae.dal.DALBackendServicesImpl;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * This class is an implementation of AbstractBinder used to configure bindings for dependency
 * injection with HK2 framework in the context of a JAX-RS application.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * Configures the bindings for the application. Binds concrete implementations to their
   * corresponding interfaces, specifying that they should be created as singletons.
   */
  @Override
  protected void configure() {

    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);

    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(ItemDAOImpl.class).to(ItemDAO.class).in(Singleton.class);
    bind(NotificationDAOImpl.class).to(NotificationDAO.class).in(Singleton.class);

    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);

    bind(DALBackendServicesImpl.class).to(DALBackendServices.class).to(DALServices.class)
        .in(Singleton.class);


  }
}

