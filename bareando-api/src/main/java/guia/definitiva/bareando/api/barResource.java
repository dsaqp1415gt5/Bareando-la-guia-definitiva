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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;


@Path("/bares")
public class barResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_BARES_QUERY = "select * from bares";
	
	@GET
	@Produces(MediaType.BAREANDO_BAR_COLLECTION)
	public barCollection getBares(){
		barCollection bares = new barCollection();
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}	 
		PreparedStatement stmt = null;
		
		try{
			stmt = conn.prepareStatement(GET_BARES_QUERY);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				bar Bar = new bar();
				Bar.setID(rs.getInt("id"));
				Bar.setNombre(rs.getString("nombre"));
				Bar.setDescripcion(rs.getString("descripcion"));
				Bar.setNota(rs.getInt("nota"));
				bares.addBar(Bar);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
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

	public bar getBarById(String id){
		bar Bar = new bar();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("No se ha podido conectar con la base de datos",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try{
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
	public bar anadirBar(bar BAR){
		//validarBar(BAR);
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("No se ha podido conectar con la base de datos",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(INSERT_BAR_QUERY, Statement.RETURN_GENERATED_KEYS);
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
}
