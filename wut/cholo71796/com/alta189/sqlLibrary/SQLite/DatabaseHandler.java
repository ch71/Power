package wut.cholo71796.com.alta189.sqlLibrary.SQLite;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import wut.cholo71796.Power.utilities.Log;

public class DatabaseHandler {
    /*
     * @author: alta189
     *
     */
    
    private sqlCore core;
    private Connection connection;
    private File SQLFile;
    
    public DatabaseHandler(sqlCore core, File SQLFile) {
        this.core = core;
        this.SQLFile = SQLFile;
    }
    
    public Connection getConnection() {
        if (connection == null) {
            initialize();
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException ex) {
                Log.severe("Error on Connection close: " + ex);
            }
    }
    
    public Boolean initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + SQLFile.getAbsolutePath());
            return true;
        } catch (SQLException ex) {
            Log.severe("SQLite exception on initialize " + ex);
        } catch (ClassNotFoundException ex) {
            Log.severe("You need the SQLite library " + ex);
        }
        return false;
    }
    
    public Boolean createTable(String query) {
        try {
            if (query == null) {
                Log.severe("SQL Create Table query empty.");
                return false;
            }
            
            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException ex){
            Log.severe(ex.getMessage());
            return false;
        }
    }
    
    public ResultSet sqlQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            
            ResultSet result = statement.executeQuery(query);
            
            return result;
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                return retryResult(query);
            } else {
                Log.warning("Error at SQL Query: " + ex.getMessage());
            }
            
        }
        return null;
    }
    
    public void insertQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            
            statement.executeQuery(query);
            
            
        } catch (SQLException ex) {
            
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                retry(query);
            }else{
                if (!ex.toString().contains("not return ResultSet"))
                    Log.warning("Error at SQL INSERT Query: " + ex);
            }
            
        }
    }
    
    public void updateQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            
            statement.executeQuery(query);
            
            
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                retry(query);
            }else{
                if (!ex.toString().contains("not return ResultSet"))
                    Log.warning("Error at SQL UPDATE Query: " + ex);
            }
        }
    }
    
    public void deleteQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            
            statement.executeQuery(query);
            
            
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                retry(query);
            } else {
                if (!ex.toString().contains("not return ResultSet"))
                    Log.warning("Error at SQL DELETE Query: " + ex);
            }
        }
    }
    
    public Boolean wipeTable(String table) {
        try {
            if (!core.checkTable(table)) {
                Log.severe("Error at Wipe Table: table, " + table + ", does not exist");
                return false;
            }
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String query = "DELETE FROM " + table + ";";
            statement.executeQuery(query);
            
            return true;
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                //retryWipe(query);
            }else{
                if (!ex.toString().contains("not return ResultSet")) Log.warning("Error at SQL WIPE TABLE Query: " + ex);
            }
            return false;
        }
    }
    
    
    public Boolean checkTable(String table) {
        DatabaseMetaData dbm;
        try {
            dbm = this.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            if (tables.next()) {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Log.severe("Failed to check if table \"" + table + "\" exists: " + e.getMessage());
            return false;
        }
        
    }
    
    private ResultSet retryResult(String query) {
        Boolean passed = false;
        
        while (!passed) {
            try {
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                
                ResultSet result = statement.executeQuery(query);
                
                passed = true;
                
                return result;
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                    passed = false;
                }else{
                    Log.warning("Error at SQL Query: " + ex.getMessage());
                }
            }
        }
        
        return null;
    }
    
    private void retry(String query) {
        Boolean passed = false;
        
        while (!passed) {
            try {
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                
                statement.executeQuery(query);
                
                passed = true;
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked") ) {
                    passed = false;
                } else {
                    Log.severe("Error at SQL Query: " + ex.getMessage());
                }
            }
        }
        return;
    }
}
