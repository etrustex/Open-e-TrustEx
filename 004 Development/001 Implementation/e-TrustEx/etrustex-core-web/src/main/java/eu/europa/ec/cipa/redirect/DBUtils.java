package eu.europa.ec.cipa.redirect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtils {

	private static DBUtils utils;
	
	private DBUtils(){
		
	}
		
	private static final String DATASOURCE_NAME = "jdbc/eTrustExDs";
	private static final String SQL_GET_PARAM = "select MD_VALUE from etr_tb_metadata where md_type=?";
	
	public static DBUtils getInstance(){
		return utils!=null ?utils: (utils= new DBUtils());
	}
	
	
	public String getParam(String key){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(DATASOURCE_NAME);
			conn = ds.getConnection();
			st = conn.prepareStatement(SQL_GET_PARAM);
			st.setString(1, key);
			rs = st.executeQuery();
			rs.next();
			return rs.getString(1);
		}catch(SQLException e){
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);			
		}finally{
			if (rs!=null){
				try{
					rs.close();
				}catch(SQLException e){}
			}
			if (st!=null){
				try{
					st.close();
				}catch(SQLException e){}
			}
			if (conn !=null){
				try{
					conn.close();
				}catch(SQLException e){}
			}
		}			
	}
}
