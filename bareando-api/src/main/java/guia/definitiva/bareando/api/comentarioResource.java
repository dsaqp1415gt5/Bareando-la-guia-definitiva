package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.comentario;
import guia.definitiva.bareando.model.comentarioCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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

	private String GET_COMENTARIOS = "select * from comentarios where id_bar=? order by id desc LIMIT ?, 10;";
	private String GET_COMENTARIOS_NO_PAG = "select * from comentarios where id_bar=? order by id desc";
	private String GET_COMENTARIOS_COUNT = "select count(*) from comentarios where id_bar=?;";

	public int countComentarios(int id) {
		Connection conn = null;
		int resultado = 0;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_COMENTARIOS_COUNT);
			stmt.setInt(1, id);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				resultado = rs.getInt(1);
			} else {
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
		int paginas = 0;
		try {
			paginas = resultado / 10;

			double pags = Math.ceil((double) resultado / (double) 10);
			paginas = (int) pags;
		} catch (UnsupportedOperationException e) {
			paginas = 0;
		}
		return paginas;
	}

	@GET
	@Path("/{id}")
	public comentarioCollection getComentariosNoPag(@PathParam("id") int id){
		return getComentariosByBar(id, 9999);
	}
	
	@GET
	@Path("/{id}-{pagina}")
	public comentarioCollection getComentariosByBar(@PathParam("id") int id,
			@PathParam("pagina") int pag) {
		comentarioCollection comentarios = new comentarioCollection();
		int min = pag * 10;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			if (pag == 9999) {
				stmt = conn.prepareStatement(GET_COMENTARIOS_NO_PAG);
				stmt.setInt(1, id);
			} else {
				stmt = conn.prepareStatement(GET_COMENTARIOS);
				stmt.setInt(1, id);
				stmt.setInt(2, min);
			}
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rs.previous();
				while (rs.next()) {
					comentario cmnt = new comentario();
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

		comentarios.setPaginas(countComentarios(id));
		return comentarios;
	}

	private String DELETE_COMENTARIO = "delete from comentarios where id = ?;";

	@DELETE
	@Path("/{id}")
	public void borrarComentario(@PathParam("id") int id) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(DELETE_COMENTARIO);
			stmt.setInt(1, id);

			int rows = stmt.executeUpdate();
			if (rows == 0) {
				throw new NotFoundException("001");
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

				// cmt = getComentarioById(Integer.toString(ID));
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
