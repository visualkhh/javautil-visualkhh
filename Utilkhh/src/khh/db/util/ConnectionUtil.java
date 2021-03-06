package khh.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionUtil {

	public final static int MYSQL=1; 
	public final static int ORACLE=2; 
	public final static int HSQL=3; 
	public final static String MYSQL_DAFAULT_PORT="3306"; 
	
	public static Connection getConnection(String drivepath, String jdbcConnectionURL, String ip, String port, String sid, String id, String passwd) throws ClassNotFoundException, SQLException{
		return getConnection(drivepath, jdbcConnectionURL, id, passwd);
	}
	public static Connection getConnection(int dbtype, String ip, String port, String sid, String id, String passwd) throws ClassNotFoundException, SQLException{
		String driverPath = getDriverPath(dbtype);
		String jdbcConnectionURL = getJDBCConnectionURL(dbtype, ip, port, sid);
		return getConnection(driverPath, jdbcConnectionURL, id, passwd);
	}
	public static Connection getConnection(String driverPath, String jdbcConnectionURL, String id, String passwd) throws ClassNotFoundException, SQLException{
		Class.forName(driverPath);
		Connection conn=DriverManager.getConnection(jdbcConnectionURL,id,passwd);
		return conn;
	}
	
	private static String getDriverPath(int dbtype){
		String driverpath=null;
		switch (dbtype) {
		case MYSQL:
			driverpath = "com.mysql.jdbc.Driver";
			break;
		case ORACLE:
			driverpath = "oracle.jdbc.driver.OracleDriver";
			break;
		case HSQL:
			driverpath = "org.hsqldb.jdbcDriver";
			break;
		default:
			driverpath=null;
			break;
		}
		return driverpath;
	}
	
	private static String getJDBCConnectionURL(int dbtype,String ip, String port, String sid){
		String driverUrl=null;
		switch (dbtype) {
		case MYSQL:
			driverUrl = "jdbc:mysql://"+ip+":"+port+"/"+sid+"?autoReconnect=true";
			break;
		case ORACLE:
			driverUrl = "jdbc:oracle:thin:@"+ip+":"+port+":"+sid;
			break;
		case HSQL:
			driverUrl = "jdbc:hsqldb:hsql://"+ip+":"+port+"/"+sid;
			break;
		default:
			driverUrl=null;
			break;
		}
		return driverUrl;
	}
	
	
	

	
	
	
	
	
	
}
