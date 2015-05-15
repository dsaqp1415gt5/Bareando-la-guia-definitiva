package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

@Path("/usuarios")
public class usuarioResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	private String GET_USUARIOS_QUERY = "insert into usuarios values (?, ?, MD5(?), ?);";

	@POST
	@Produces(MediaType.BAREANDO_USER)
	@Consumes(MediaType.BAREANDO_USER)
	public usuario registrarUsuario(usuario nuevo) {
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"No se ha podido conectar con la base de datos",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_USUARIOS_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nuevo.getNick());
			stmt.setString(2, nuevo.getNombre());
			stmt.setString(3, nuevo.getPass());
			stmt.setString(4, nuevo.getMail());	
			
			System.out.println(stmt);

			stmt.executeUpdate();
			
			System.out.println(stmt);
			
			ResultSet rs = stmt.getGeneratedKeys();

			System.out.println(stmt);

			if (rs.next()) {
				nuevo.setNick(rs.getString("nick"));
				nuevo.setNombre(rs.getString("nombre"));
				nuevo.setPass(rs.getString("pass"));
				nuevo.setMail(rs.getString("mail"));
				System.out.println(nuevo);

			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
			
		return nuevo;
	}
}
