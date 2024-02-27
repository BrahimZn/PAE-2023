package be.vinci.pae.api;

import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.ucc.ItemUCC;
import be.vinci.pae.main.Main;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * This class handles file uploads and provides an API endpoint to upload files.
 */
@Singleton
@Path("/")
public class FileUpload {

  private final Logger logger = LogManager.getLogger(Main.class.getName());
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private ItemUCC itemUCC;

  /**
   * API endpoint to upload files via HTTP POST method.
   *
   * @param file            the InputStream of the file to be uploaded.
   * @param fileDisposition the file details extracted from the HTTP request.
   * @return a filename of the uploaded file.
   * @throws IOException if there is an error copying the file to the server.
   */

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {

    String originalFileName = fileDisposition.getFileName();
    String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    String fileName = UUID.randomUUID() + "." + extension; //fileDisposition.getFileName();
    Files.copy(file, Paths.get("uploads", fileName));
    logger.info("fileName = " + fileName);

    return Response.ok(jsonMapper.createObjectNode().put("fileName", fileName),
        MediaType.APPLICATION_JSON).build();
  }

  /**
   * Endpoint for downloading a file from the server.
   *
   * @param fileName the name of the file to download.
   * @return an HTTP response with the file to download as the response body.
   * @throws IOException             if an error occurs while retrieving the file.
   * @throws WebApplicationException if the file does not exist on the server.
   */
  @GET
  @Path("/file/{fileName}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadFile(@PathParam("fileName") String fileName) throws IOException {
    logger.info("fileName = " + fileName);
    File file = new File("uploads/" + fileName);
    if (!file.exists()) {
      throw new WebApplicationException("file not found", Status.NOT_FOUND);
    }
    logger.info("file.getPath() = " + file.getPath());
    return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
        .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
  }

  /**
   * PATCH method to update the picture of an item with a given ID.
   *
   * @param file            The input stream of the file to be uploaded.
   * @param fileDisposition The form data content disposition of the file to be uploaded.
   * @param idItem          The ID of the item to update.
   * @return A response object containing the updated item's filename in JSON format.
   * @throws IOException If there is an issue with the file or its contents.
   */
  @PATCH
  @Path("/updatePictureItem/{idItem}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response updateItemPicture(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @PathParam("idItem") int idItem) throws IOException {

    ItemDTO item = itemUCC.getItemById(idItem);
    String pictureName = item.getPicture();

    Files.delete(Paths.get("uploads", pictureName)); // delete precedent picture

    String originalFileName = fileDisposition.getFileName();
    System.out.println("originalFileName = " + originalFileName);
    String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    String fileName = UUID.randomUUID() + "." + extension;
    Files.copy(file, Paths.get("uploads", fileName));

    System.out.println("fileName = " + fileName);
    item.setPicture(fileName);
    itemUCC.updateItem(item);

    return Response.ok(jsonMapper.createObjectNode().put("fileName", fileName),
        MediaType.APPLICATION_JSON).build();
  }


}
