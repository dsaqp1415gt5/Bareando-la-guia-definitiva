package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.bar;
import guia.definitiva.bareando.model.barCollection;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/bares")
public class barResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	private String GET_BARES_QUERY = "select * from bares";

	@GET
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getBares() {
		barCollection bares = new barCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(GET_BARES_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				bar Bar = new bar();
				Bar.setID(rs.getInt("id"));
				Bar.setNombre(rs.getString("nombre"));
				Bar.setDescripcion(rs.getString("descripcion"));
				Bar.setNota(rs.getInt("nota"));
				bares.addBar(Bar);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return bares;
	}

	private String INSERT_BAR_QUERY = "insert into bares (id, nombre, descripcion, nota) values (null, ?, ?, ?)";
	private String GET_BAR_ID_QUERY = "select * from bares where id=?";

	public bar getBarById(String id) {
		bar Bar = new bar();

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
			stmt = conn.prepareStatement(GET_BAR_ID_QUERY);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Bar.setID(rs.getInt("id"));
				Bar.setNombre(rs.getString("nombre"));
				Bar.setDescripcion(rs.getString("descripcion"));
				Bar.setNota(rs.getInt("nota"));
			} else {
				// Something has failed...
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
		return Bar;
	}

	@POST
	@Consumes(MediaType.BAREANDO_BAR)
	@Produces(MediaType.BAREANDO_BAR)
	public bar anadirBar(bar BAR) {
		// validarBar(BAR);

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
			stmt = conn.prepareStatement(INSERT_BAR_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, BAR.getNombre());
			stmt.setString(2, BAR.getDescripcion());
			stmt.setInt(3, BAR.getNota());
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int ID = rs.getInt(1);

				BAR = getBarById(Integer.toString(ID));
			} else {
				// Something has failed...
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
		return BAR;
	}

	private String DELETE_BAR_QUERY = "delete from bares where id=? ";

	@DELETE
	@Path("/{barid}")
	public void deleteBar(@PathParam("barid") String barid) {
		// validarBar(BAR);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_BAR_QUERY);
			stmt.setString(1, barid);

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no bar with id=" + barid);
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
	}

	@GET
	@Path("{id}-{nombre}-{minNota}-{maxNota}")
	// bareando-api/stings/3-chita-8-9
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getBarByAll(@PathParam("id") String id,
			@PathParam("nombre") String nombre,
			@PathParam("minNota") String minNota,
			@PathParam("maxNota") String maxNota) {
		int primero = 0;
		String QUERY = "select * from bares where ";
		try {
			int NOMBRE = Integer.parseInt(nombre);
		} catch (NumberFormatException e) {
			QUERY = QUERY.concat("nombre = '").concat(nombre).concat("' ");
			primero++;
		}
		try {
			int ID = Integer.parseInt(id);
			int MaxNota = Integer.parseInt(maxNota);
			int MinNota = Integer.parseInt(minNota);
			if (ID > 0) {
				if (primero == 1)
					QUERY = QUERY.concat("and id = '").concat(id).concat("' ");
				else
					QUERY = QUERY.concat("id = '").concat(id).concat("' ");
				primero = 1;
			}
			if (MinNota < MaxNota && MinNota >= 0 && MaxNota <= 10) {
				if (primero == 1)
					QUERY = QUERY.concat("and nota between ").concat(minNota)
							.concat(" and ").concat(maxNota);
				else
					QUERY = QUERY.concat("nota between ").concat(minNota)
							.concat(" and ").concat(maxNota);
			}
			System.out.println(QUERY);
		} catch (NumberFormatException e) {
			// no son numeros y deberian serlo
		}

		return null;
	}

	@Context
	private SecurityContext security;
}
