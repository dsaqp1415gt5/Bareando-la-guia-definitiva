package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.bar;
import guia.definitiva.bareando.model.comentario;
import guia.definitiva.bareando.model.comentarioCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;


@Path("/comentarios")
@Produces(MediaType.BAREANDO_COMENTARIO_COLLECTION)
public class comentarioResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_COMENTARIOS = "select * from comentarios where id_bar=?;";
	
	@GET
	@Path("/{id}")
	public comentarioCollection getComentariosByBar(@PathParam("id") int id){
		comentarioCollection comentarios = new comentarioCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_COMENTARIOS);
			stmt.setInt(1, id);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rs.previous();
				while (rs.next()) {
					comentario  cmnt = new comentario();
					cmnt.setMensaje(rs.getString("mensaje"));
					cmnt.setId(rs.getInt("id"));
					cmnt.setId_bar(rs.getInt("id_bar"));
					cmnt.setNick(rs.getString("nick"));
					comentarios.addComentario(cmnt);
				}
			} else {
				return comentarios;
			}
		} catch (SQLException e) {
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
		
		return comentarios;
	}
	
	private String SET_COMENTARIO = "insert into comentarios (nick, id_bar, mensaje) values (?, ?, ?);";
	
	@POST
	@Consumes(MediaType.BAREANDO_COMENTARIO_COLLECTION)
	@Produces(MediaType.BAREANDO_COMENTARIO_COLLECTION)
	public comentario anadirComentario(comentario cmt) {
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
			stmt = conn.prepareStatement(SET_COMENTARIO,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, cmt.getNick());
			stmt.setInt(2, cmt.getId_bar());
			stmt.setString(3, cmt.getMensaje());
			System.out.println(stmt);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int ID = rs.getInt(1);

				//cmt = getComentarioById(Integer.toString(ID));
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
		return cmt;
	}

}
/*
CREATE TABLE comentarios(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nick        VARCHAR(25) NOT NULL,
    id_bar      INTEGER,
    mensaje     VARCHAR(250) NOT NULL,
    FOREIGN KEY (id_bar)  REFERENCES bares(id) ON DELETE CASCADE,
    FOREIGN KEY (nick) REFERENCES usuarios(nick) ON DELETE CASCADE
);*/
