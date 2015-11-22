package com.models.dataaccess;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;
import java.sql.*;

import com.models.entity.Services;
import com.models.entity.Users;
import com.models.entity.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.models.utility.Constants;

public class DataAccess {

    private Logger logger; // Logger

    Statement stmt = null;
    Connection connection = null;

    /**
     * Get connection
     * @return DB connection
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(Constants.JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(Constants.DB_URL, Constants.USER, Constants.PASS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public int saveToServices(String uid, String servicename, String servicetype,String date, String json) throws SQLException{

        int status = 0;
        connection = getConnection();
        try {
            stmt = connection.createStatement();
            String sql;
            sql = "INSERT INTO services(servicename,uid,servicetype,status,datecreated,json) VALUES('"+ servicename +"',"+ uid +",'"+servicetype +"',"+ "'created'"+",'"+ date.toString() +"','"+json +"')";
            status = stmt.executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
        } finally {
            connection.close();
        }
        return status;
    }

    public Services getServiceByName(String name) throws SQLException{
        Connection conn = getConnection();
        Services service = new Services();
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM services WHERE servicename='"+name+ "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Retrieve by column name
                service.setServiceid(rs.getString("serviceid"));
                service.setServicename(rs.getString("servicename"));
                Integer uid = rs.getInt("uid");
                service.setUid(uid.toString());
                service.setServicetype(rs.getString("servicetype"));
                service.setStatus(rs.getString("status"));
                service.setDatecreated(rs.getString("datecreated"));
                service.setJson(rs.getString("json"));
            }
            rs.close();
            conn.close();
        } catch (Exception e) {
        } finally {
            conn.close();
        }
        return service;
    }

    /**
     * Create user
     * @param user
     * @return
     * @throws SQLException
     */
    public int createUser(Users user) throws SQLException {
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql = "INSERT INTO users(uname,pwd) VALUES('"+user.getUsername()+"','"+user.getPassword()+"')";
            status = stmt.executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
        } finally {
            connection.close();
        }
        return status;
    }

    /**
     * Log user in
     * @param user
     * @return
     * @throws SQLException
     */
    public Users getUser(Users user) throws SQLException{
        try {
            connection = getConnection();
            stmt = connection.createStatement();
            String sql;
            sql = "SELECT * FROM users WHERE uname='" + user.getUsername() +"' AND pwd='"+ user.getPassword()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Retrieve by column name
                user.setUid(rs.getInt("uid"));
                user.setUsername(rs.getString("uname"));
            }
            rs.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return user;
    }


    public int saveToJob(String json) throws SQLException{
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql= "INSERT INTO jobs(jobclassname,json) VALUES('models.WebsiteHandler','"+json + "')";
            status = stmt.executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
        } finally{
            connection.close();
        }
        return status;
    }

    public int deleteJob(int jobid) throws SQLException {
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String deleteSQL = "DELETE FROM JOBS WHERE jobid ="+jobid;
            status = stmt.executeUpdate(deleteSQL);
            status = stmt.executeUpdate(deleteSQL);
            connection.close();
        } catch (Exception e) {
        } finally {
            connection.close();
        }
        return status;
    }

    public HashMap<String,ArrayList<String>> getAllServices(Integer uid) throws SQLException{
        ArrayList<String> webservice = new ArrayList<String>();
        ArrayList<String> dbservice = new ArrayList<String>();
        HashMap<String,ArrayList<String>> allservices = new HashMap<String,ArrayList<String>>();

        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql = "SELECT * FROM services WHERE uid="+uid;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Retrieve by column name

                String servicetype  = rs.getString("servicetype");
                String servicename = rs.getString("servicename");

                if(servicetype.contains("Website")){
                    webservice.add(servicename);
                }
                if(servicetype.contains("database")){
                    dbservice.add(servicename);
                }
            }
            allservices.put("webservice", webservice);
            allservices.put("dbservice",dbservice);
            rs.close();
            connection.close();
        }catch (Exception e) {
        } finally {
            connection.close();
        }
        return allservices;
    }
    public int addResource(String resourcetype , Resources resource, Services service) throws SQLException{
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql= "INSERT INTO resources VALUES("+service.getServiceid()+"'"+resourcetype+"'"+"'"+resource.getStatus()+"'"+service.getDatecreated()+"'"+resource.getJson()+"')";
            status = stmt.executeUpdate(sql);
            status = stmt.executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
        } finally{
            connection.close();
        }
        return status;
    }
    public int startResource(int userid, int serviceid, int resourceid) throws SQLException {
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql= "INSERT INTO resource_usage VALUES("+userid+", "+serviceid+", "+resourceid+ ", CURRENT_TIMESTAMP, NULL)";
            status = stmt.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return status;
    }

    public int stopResource(int resourceid) throws SQLException {
        int status =0;
        connection = getConnection();
        try{
            stmt = connection.createStatement();
            String sql;
            sql = "UPDATE resource_usage SET endtime=CURRENT_TIMESTAMP WHERE"+
                    " resourceid="+resourceid+" AND endtime IS NULL";
            status = stmt.executeUpdate(sql);
            status = stmt.executeUpdate(sql);
            connection.close();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return status;
    }

    //TODO: Update the web pages to update the total hours and total cost of the resource
    public void refreshResource(int resourceid) throws SQLException {

        Double hours = getTotalResourceHours(resourceid);
        Double cost = getTotalCost(hours);

    }

    //TODO: Update the web pages to update the total hours and total cost of the service
    public void refreshService(int serviceid) throws SQLException {

        Double hours = getTotalServiceHours(serviceid);
        Double cost = getTotalCost(hours);
    }

    public Double getTotalResourceHours(int resourceid) throws SQLException {

        String sql = "SELECT * FROM resource_usage WHERE resourceid="+resourceid;
        ResultSet rs = stmt.executeQuery(sql);
        Timestamp start;
        Timestamp end;
        long starttime;
        long endtime;
        long totaltime = 0;
        Double totalhours = 0.0;

        try {
            while(rs.next()){
                //Retrieve by column name
                start  = Timestamp.valueOf(rs.getString("starttime"));
                if ((rs.getString("endtime")) != null) {
                    end = Timestamp.valueOf(rs.getString("endtime"));
                }
                else {
                    //If service is still running, get the current timestamp
                    Date date = new Date();
                    end = new Timestamp(date.getTime());
                }
                //convert Timestamp to long
                starttime = start.getTime();
                endtime = end.getTime();
                totaltime = totaltime + (endtime - starttime);
            }
            rs.close();
            connection.close();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.close();
        }

		/*
		 * totaltime is in milliseconds format, convert to hours format
		 * First, convert to seconds by dividing by 1000
		 * Second, convert to minutes by dividing by 60
		 * Third, convert to hours by dividing by 60
		 */
        totalhours = Long.valueOf(totaltime).doubleValue();
        totalhours = ((totalhours/1000)/60)/60;

        //round up totalhours to 2 decimal places
        totalhours = Math.round(totalhours*100.0)/100.0;

        return totalhours;
    }

    public Double getTotalServiceHours(int serviceid) throws SQLException {
        String sql = "SELECT * FROM resource_usage WHERE serviceid="+serviceid;
        ResultSet rs = stmt.executeQuery(sql);
        Timestamp start;
        Timestamp end;
        long starttime;
        long endtime;
        long totaltime = 0;
        Double totalhours = 0.0;

        try {
            while(rs.next()){
                //Retrieve by column name
                start  = Timestamp.valueOf(rs.getString("starttime"));
                if ((rs.getString("endtime")) != null) {
                    end = Timestamp.valueOf(rs.getString("endtime"));
                } else {
                    //If service is still running, get the current timestamp
                    Date date = new Date();
                    end = new Timestamp(date.getTime());
                }
                //convert Timestamp to long
                starttime = start.getTime();
                endtime = end.getTime();
                totaltime = totaltime + (endtime - starttime);
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.close();
        }

		/*
		 * totaltime is in milliseconds format, convert to hours format
		 * First, convert to seconds by dividing by 1000
		 * Second, convert to minutes by dividing by 60
		 * Third, convert to hours by dividing by 60
		 */
        totalhours = Long.valueOf(totaltime).doubleValue();
        totalhours = ((totalhours/1000)/60)/60;

        //round up totalhours to 2 decimal places
        totalhours = Math.round(totalhours*100.0)/100.0;
        return totalhours;
    }

    /*
     * This method can either be used to get total cost of service or resource
     * First either call getTotalServiceHours or getTotalResourceHours to get
     * the hours then call this method to get the total cost.
     */
    public Double getTotalCost(Double hours) {
        Double total;
        total = hours * 25;

        //Round up total to 2 decimal places
        total = Math.round(total*100.0)/100.0;
        return total;
    }

    /**
     * Get logger
     *
     * @return Logger for this instance
     */
    protected Logger getLogger() {
        if (this.logger == null) {
            this.logger = LoggerFactory.getLogger(this.getClass());
        }
        return this.logger;
    }

}