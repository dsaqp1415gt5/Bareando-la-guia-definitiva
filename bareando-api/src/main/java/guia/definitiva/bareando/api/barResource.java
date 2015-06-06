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

	private String INSERT_BAR_QUERY = "insert into bares (id, nombre, descripcion, nota, genero) values (null, ?, ?, ?, ?)";
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
				Bar.setGenero(rs.getString("genero"));
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
			stmt.setString(4, BAR.getGenero());
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

	private static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private String COUNT_ROWS = "select COUNT(*) from bares;";

	private int paginasTotales(int perPage, String query) {
		int resultado = 0;
		int paginas = 0;
		Connection conn = null;
		ResultSet rs;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"No se ha podido conectar con la base de datos",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();

			if (rs.next()) {
				resultado = rs.getInt(1);
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
		try {
			paginas = resultado / perPage;

			double pags = Math.ceil((double) resultado / (double) perPage);
			paginas = (int) pags;
		} catch (UnsupportedOperationException e) {
			paginas = 0;
		}
		return paginas;
	}

	@GET
	@Path("desc")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getTodosDesc() {
		return getBarByAll("0", "0", "0", "0", "0", "0", "D", "0", "0");
	}

	@GET
	@Path("asc")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getTodosAsc() {
		return getBarByAll("0", "0", "0", "0", "0", "0", "A", "0", "0");
	}

	@GET
	@Path("todos")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getTodos() {
		return getBarByAll("0", "0", "0", "0", "0", "0", "0", "0", "0");
	}

	@GET
	@Path("random")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getRandom() {
		return getBarByAll("0", "0", "0", "0", "R", "0", "0", "0", "0");
	}

	@GET
	@Path("nombre:{nombre}")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getByNombre(@PathParam("nombre") String nombre) {
		return getBarByAll("0", nombre, "0", "0", "0", "0", "0", "0", "0");
	}

	@GET
	@Path("genero:{genero}")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getByGenero(@PathParam("genero") String genero) {
		return getBarByAll("0", "0", "0", "0", "0", genero, "0", "0", "0");
	}

	@GET
	@Path("nota:{nota}")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getByNota(@PathParam("nota") String nota) {
		return getBarByAll("0", "0", nota, nota, "0", "0", "0", "0", "0");
	}

	@GET
	@Path("notas:{nota}&{nota2}")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getByNotas(@PathParam("nota") String nota,
			@PathParam("nota2") String nota2) {
		return getBarByAll("0", "0", nota, nota2, "0", "0", "0", "0", "0");
	}

	public static String reemplazar(String cadena, String busqueda,
			String reemplazo) {
		return cadena.replaceAll(busqueda, reemplazo);
	}

	@GET
	@Path("{id}-{nombre}-{minNota}-{maxNota}-{random}-{genero}-{orden}-{perpage}-{page}")
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getBarByAll(@PathParam("id") String id,
			@PathParam("nombre") String nombre,
			@PathParam("minNota") String minNota,
			@PathParam("maxNota") String maxNota,
			@PathParam("random") String random,
			@PathParam("genero") String genero,
			@PathParam("orden") String orden,
			@PathParam("perpage") String perpage, @PathParam("page") String page) {
		int primero = 0;
		Boolean GENERO = true;
		Boolean OnlyOne = false;
		barCollection bares = new barCollection();
		String QUERY = "select * from bares ";
		if (!isNumeric(id) || !isNumeric(minNota) || !isNumeric(maxNota)
				|| !isNumeric(perpage) || !isNumeric(page)) {
			return bares;
		}
		if (isNumeric(genero)) {
			GENERO = false;
		}
		if (isNumeric(nombre)) {
			int NOMBRE = Integer.parseInt(nombre);
			if (NOMBRE != 0) {
				QUERY = QUERY.concat("where nombre like '%").concat(nombre)
						.concat("%' ");
				primero++;
			}
		} else {
			QUERY = QUERY.concat("where nombre like '%").concat(nombre)
					.concat("%' ");
			primero++;
		}

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		int ID = Integer.parseInt(id);
		int MaxNota = Integer.parseInt(maxNota);
		int MinNota = Integer.parseInt(minNota);
		if (ID > 0) {
			if (primero == 1)
				QUERY = QUERY.concat("and id = '").concat(id).concat("' ");
			else
				QUERY = QUERY.concat("where id = '").concat(id).concat("' ");
			primero = 1;
		}
		if (MinNota < MaxNota && MinNota >= 0 && MaxNota <= 10) {
			if (primero == 1)
				QUERY = QUERY.concat("and nota between ").concat(minNota)
						.concat(" and ").concat(maxNota);
			else
				QUERY = QUERY.concat("where nota between ").concat(minNota)
						.concat(" and ").concat(maxNota);
			primero = 1;
		}
		if (MinNota == MaxNota && MinNota > 0) {
			if (primero == 1)
				QUERY = QUERY.concat("and nota=").concat(minNota);
			else
				QUERY = QUERY.concat("where nota=").concat(minNota);
			primero = 1;
		}
		System.out.println(random);

		if (GENERO) {
			if (primero == 1)
				QUERY = QUERY.concat(" and genero = '").concat(genero)
						.concat("' ");
			else
				QUERY = QUERY.concat(" where genero = '").concat(genero)
						.concat("' ");
			primero = 1;
		}
		if (random.equals("R")) {
			QUERY = QUERY.concat(" order by rand() ");
			if (primero != 1)
				OnlyOne = true;
		}
		if (orden.equals("A")) {
			QUERY = QUERY.concat(" order by nota asc ");
		} else if (orden.equals("D")) {
			QUERY = QUERY.concat(" order by nota desc ");
		} else {
			// do nothing
		}

		int porpagina = Integer.parseInt(perpage);
		int pagina = Integer.parseInt(page);
		int pagTotales;
		String QUERY_PAG = reemplazar(QUERY, "\\*", "COUNT(*)");
		QUERY_PAG.concat(";");
		if (OnlyOne) {
			QUERY = QUERY.concat(" LIMIT 1");
		} else if (porpagina != 0) {
			pagTotales = paginasTotales(porpagina, QUERY_PAG);
			int min = pagina * porpagina;
			bares.setPaginas(pagTotales);
			QUERY = QUERY.concat(" LIMIT ").concat(String.valueOf(min))
					.concat(" , ").concat(perpage);
		}

		QUERY = QUERY.concat(" ;");
		System.out.println(QUERY);

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(QUERY);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rs.previous();
				while (rs.next()) {
					bar Bar = new bar();
					Bar.setDescripcion(rs.getString("descripcion"));
					Bar.setID(rs.getInt("id"));
					Bar.setNombre(rs.getString("nombre"));
					Bar.setNota(rs.getInt("nota"));
					Bar.setGenero(rs.getString("genero"));
					bares.addBar(Bar);
				}
			} else {
				return bares;
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

		return bares;
	}

	@Context
	private SecurityContext security;
}
