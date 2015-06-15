package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.chatMsj;
import guia.definitiva.bareando.model.chatMsjCollection;
import guia.definitiva.bareando.model.userCollection;
import guia.definitiva.bareando.model.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

@Path("/chat")
public class chat {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	static Map<String, Integer> maps = new HashMap<String, Integer>();

	private String INSERTAR = "insert into chat values (null, ?, ?, ?);";
	private String SELECT = "select * from chat where para = ? order by id desc limit 1;";
	private String SELECT_NUEVOS = "select * from chat where (para = ? and de = ?) or (para = ? and de = ?) order by id desc limit 25;";

	
	private String GET_AMIGOS = "SELECT amigo2 FROM amigos where amigo1 =?;";
	
	@GET
	@Path("amigos-{de}")
	public userCollection getFriends(@PathParam("de") String de){
		userCollection usuarios = new userCollection();
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_AMIGOS);
			stmt.setString(1, de);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				usuario user = new usuario();
				user.setNick(rs.getString("amigo2"));
				usuarios.addUser(user);
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
		return usuarios;
	}

	private String AMIGO = "insert into amigos values (?, ?)";
	//insert into amigos values ("" ,"");
	@POST
	@Path("amigo-{nick}-{nock}")
	public int addFriend(@PathParam("nick") String para, @PathParam("nock") String de){
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"No se ha podido conectar con la base de datos",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(AMIGO,
					Statement.RETURN_GENERATED_KEYS);
			stmt2 = conn.prepareStatement(AMIGO,
							Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, de);
			stmt.setString(2, para);			
			stmt2.setString(2, de);
			stmt2.setString(1, para);

			stmt.executeUpdate();
			stmt2.executeUpdate();

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
		return 0;
	}
	
	@GET
	@Path("/nuevos/{de}-{para}")
	public chatMsjCollection getUltimosMensajes(@PathParam("para") String para, @PathParam("de") String de) {
		chatMsjCollection mensajes = new chatMsjCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SELECT_NUEVOS);
			stmt.setString(1, para);
			stmt.setString(2, de);
			stmt.setString(3, de);
			stmt.setString(4, para);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				chatMsj mensaje = new chatMsj();
				mensaje.setDe(rs.getString("de"));
				mensaje.setPara(rs.getString("para"));
				mensaje.setId(rs.getInt("id"));
				mensaje.setMensaje(rs.getString("mensaje"));
				mensajes.addMensaje(mensaje);
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
		return mensajes;
	}

	@GET
	@Path("/recibir/{para}")
	public chatMsjCollection getMensajes(@PathParam("para") String usuario) {
		int ola;
		try {
			ola = maps.get(usuario);// miramos is hay algun mensaje nuevo
		} catch (NullPointerException e) {
			return null;
		}
		if (ola == 0)
			return null;
		/*while (ola == 0) {
			try {
				ola = maps.get(usuario);// miramos is hay algun mensaje nuevo
			} catch (NullPointerException e) {
				ola = 0;// sino esperamos
			}
		}*/
		maps.put(usuario, 0);// si ya lo hemos recibido salimos del bucle
		chatMsjCollection mensajes = getMsg(usuario);
		return mensajes;
	}

	@POST
	@Consumes(MediaType.CHAT_MSJ)
	@Path("/enviar")
	public String postMensajes(chatMsj mensaje) {
		// poner mensaje db
		insertMsj(mensaje);
		maps.put(mensaje.getPara(), 1);
		return "ok";
	}

	private void insertMsj(chatMsj mensaje) {
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
			stmt = conn.prepareStatement(INSERTAR,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, mensaje.getDe());
			stmt.setString(2, mensaje.getPara());
			stmt.setString(3, mensaje.getMensaje());
			stmt.executeUpdate();
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

	private chatMsjCollection getMsg(String para) {
		chatMsjCollection mensajes = new chatMsjCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SELECT);
			stmt.setString(1, para);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				chatMsj mensaje = new chatMsj();
				mensaje.setDe(rs.getString("de"));
				mensaje.setPara(rs.getString("para"));
				mensaje.setId(rs.getInt("id"));
				mensaje.setMensaje(rs.getString("mensaje"));
				mensajes.addMensaje(mensaje);
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
		return mensajes;
	}
}
