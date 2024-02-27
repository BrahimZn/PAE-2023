package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is responsible for loading and accessing configuration properties stored in a
 * properties file.
 */
public class Config {

  private static Properties props;

  /**
   * Loads the configuration properties from the specified file.
   *
   * @param file the path to the properties file to load
   * @throws WebApplicationException if there was an error reading the file
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * Returns the value of the specified property.
   *
   * @param key the key of the property to retrieve
   * @return the value of the property
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }
}
