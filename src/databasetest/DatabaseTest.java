package databasetest;

import java.sql.*;
import org.json.simple.*;


public class DatabaseTest{
    
    public JSONArray getJSONData(){
        
        JSONArray jsonObj = new JSONArray();
            
        Connection con = null;
        PreparedStatement sctStmt = null, stmt = null;
        ResultSet rs = null;
        ResultSetMetaData metadata = null;
        
        String query;
         
        boolean stmt_rez;
        int counter, colCount;
        
        try {
            
           
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "";
            System.out.println("Connecting to " + server + "...");
            
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            con = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (con.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                sctStmt = con.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                stmt_rez = sctStmt.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( stmt_rez || sctStmt.getUpdateCount() != -1 ) {

                    if ( stmt_rez ) {
                        
                        /* Get ResultSet Metadata */
                        
                        rs = sctStmt.getResultSet();
                        metadata = rs.getMetaData();
                        colCount = metadata.getColumnCount();
                        

                        
                        while(rs.next()) {
                            JSONObject jsObj = new JSONObject();
                            for (int i = 2; i <= colCount; i++){
                                jsObj.put(metadata.getColumnLabel(i), rs.getString(i));
                            }
                            jsonObj.add(jsObj);
                        }
                        
                    }

                    else {

                        counter = sctStmt.getUpdateCount();  

                        if ( counter == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    stmt_rez = sctStmt.getMoreResults();

                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            con.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (rs != null) { try { rs.close(); rs = null; } catch (Exception e) {} }
            
            if (sctStmt != null) { try { sctStmt.close(); sctStmt = null; } catch (Exception e) {} }
            
            if (stmt != null) { try { stmt.close(); stmt = null; } catch (Exception e) {} }
            
        }

        return jsonObj;
    }
    
    
    
}