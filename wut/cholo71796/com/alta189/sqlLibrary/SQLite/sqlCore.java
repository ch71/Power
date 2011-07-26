package wut.cholo71796.com.alta189.sqlLibrary.SQLite;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import wut.cholo71796.Power.utilities.Log;

public class sqlCore {
    
    /*
     *  @author: alta189
     *
     */
    
    public File dbFile;
    public String dbName;
    private DatabaseHandler manageDB;
    
    public sqlCore(String dbName, File dbFile) {
        this.dbName = dbName;
        this.dbFile = dbFile;
        
    }
    
    public Boolean initialize() {
        if (dbName.contains("/") || dbName.contains("\\") || dbName.endsWith(".db")) {
            Log.severe("The database name can not contain: /, \\, or .db");
            return false;
        }
        
        if (!dbFile.exists())
            dbFile.mkdirs();
        
        manageDB = new DatabaseHandler(this, new File(dbFile.getAbsolutePath() + File.separator + dbName + ".db"));
        
        return manageDB.initialize();
    }
    
    public ResultSet sqlQuery(String query) {
        return manageDB.sqlQuery(query);
    }
    
    public Boolean createTable(String query) {
        return manageDB.createTable(query);
    }
    
    public void insertQuery(String query) {
        manageDB.insertQuery(query);
    }
    
    public void updateQuery(String query) {
        manageDB.updateQuery(query);
    }
    
    public void deleteQuery(String query) {
        manageDB.deleteQuery(query);
    }
    
    public Boolean checkTable(String table) {
        return this.manageDB.checkTable(table);
    }
    
    public Boolean wipeTable(String table) {
        return this.manageDB.wipeTable(table);
    }
    
    public Connection getConnection() {
        return this.manageDB.getConnection();
    }
    
    public void close() {
        this.manageDB.closeConnection();
    }
    
    public Boolean checkConnection() {
        Connection con = this.manageDB.getConnection();
        
        if (con != null) {
            return true;
        }
        return false;
    }
}
