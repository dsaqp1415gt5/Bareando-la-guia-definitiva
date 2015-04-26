package guia.definitiva.bareando.api;

import guia.definitiva.bareando.model.bar;
import guia.definitiva.bareando.model.barCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


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
}
