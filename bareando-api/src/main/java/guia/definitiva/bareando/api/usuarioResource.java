package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;

@Path("/usuarios")
public class usuarioResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	private String GET_USUARIOS_QUERY = "insert into usuarios values (?, ?, MD5(?), ?);";
	
	@POST
	@Produces(MediaType.BAREANDO_USER)
	@Consumes(MediaType.BAREANDO_USER)
	public usuario registrarUsuario(usuario nuevo) {

		boolean existe;

		existe = comprobarNick(nuevo.getNick());

		if (!existe) {
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
				stmt = conn.prepareStatement(GET_USUARIOS_QUERY,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, nuevo.getNick());
				stmt.setString(2, nuevo.getNombre());
				stmt.setString(3, nuevo.getPass());
				stmt.setString(4, nuevo.getMail());
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					nuevo.setNick(rs.getString("nick"));
					nuevo.setNombre(rs.getString("nombre"));
					nuevo.setMail(rs.getString("mail"));
					nuevo.setLoginSuccessful(true);
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
			nuevo.setPass(null);
			insertarRoles("registrado", nuevo.getNick());
		} else {
			nuevo.setNick("null");
			nuevo.setNombre("null");
			nuevo.setPass(null);
			nuevo.setMail("null");
		}
		return nuevo;
	}

	private String GET_USER_NICK_QUERY = "select * from usuarios where nick = ?;";

	@GET
	@Path("{nick}")
	public boolean comprobarNick(@PathParam("nick") String nick) {
		boolean existe = false;

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
			stmt = conn.prepareStatement(GET_USER_NICK_QUERY);
			stmt.setString(1, nick);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				existe = true;
			} else {
				existe = false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new ServerErrorException("Error: " + e,
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return existe;
	}

	private String INSERTAR_ROL = "insert into user_roles values (?, ?);";

	private void insertarRoles(String rol, String nick) {
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
			stmt = conn.prepareStatement(INSERTAR_ROL);
			stmt.setString(1, nick);
			stmt.setString(2, rol);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			throw new ServerErrorException("Error: " + e,
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

	private String GET_USER_QUERY = "select * from usuarios where nick = ?;";

	private usuario getUsuarioNick(String nick) {
		usuario user = new usuario();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_USER_QUERY);
			stmt.setString(1, nick);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user.setNick(rs.getString("nick"));
				user.setPass(rs.getString("pass"));
				user.setMail(rs.getString("mail"));
				user.setNombre(rs.getString("nombre"));
			} else
				throw new NotFoundException(nick + " not found.");
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

		return user;
	}

	@POST
	@Path("/login")
	@Produces(MediaType.BAREANDO_USER)
	@Consumes(MediaType.BAREANDO_USER)
	public usuario login(usuario user) {
		if (user.getNick() == null || user.getPass() == null)
			throw new BadRequestException(
					"username and password cannot be null.");

		String pwdDigest = DigestUtils.md5Hex(user.getPass());
		String storedPwd = getUsuarioNick(user.getNick()).getPass();

		user.setLoginSuccessful(pwdDigest.equals(storedPwd));
		user.setPass(null);

		return user;
	}
}
