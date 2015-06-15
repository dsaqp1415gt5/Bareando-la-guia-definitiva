package guia.definitiva.bareando.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/foto")
public class JerseyFileUpload {

	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C:/pepe/";

	@POST
	@Path("/upload-{id}")
	@Consumes("multipart/form-data")
	public Response uploadFile(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition cdh,
			@PathParam("id") String id) {
		String filePath = SERVER_UPLOAD_LOCATION_FOLDER + id + ".png";
		UUID uuid = writeAndConvertImage(fileInputStream, id);
		
		//saveFile(fileInputStream, filePath);
		
		return Response.status(200).entity("ok").build();
	}

	private UUID writeAndConvertImage(InputStream file, String id) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(file);

		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when reading the file.");
		}
		UUID uuid = UUID.randomUUID();
		String filename = id + ".png";
		try {
			ImageIO.write(
					image,
					"png",
					new File("/var/www/tgrupo5.dsa/public_html/img/bares/" + filename));
		} catch (IOException e) {//new File("C:/pepe/" + filename));/var/www/tgrupo5.dsa/public_html/img/bares/
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}

		return uuid;
	}
	// save uploaded file to a defined location on the server
	private void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(
					serverLocation));
			int read = 0;
			byte[] bytes = new byte[1];

			int ok = 1;
			outpuStream = new FileOutputStream(new File(serverLocation));
			// uploadedInputStream.skip(138);
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				for (int i = 0; i < 1; i++) {
					if (ok == 0) {
						int value = bytes[i];
						String a = Long.toString(value & 0xFFFFFFFFL);
						if (a.equals("4294967177")) {
							ok = 1;
							System.out.println("fdsf");
						}
					}
					if (ok != 0) {
						byte[] bytess = new byte[1];
						bytess[0] = bytes[i];
						outpuStream.write(bytes, 0, read);
					}
				}
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}