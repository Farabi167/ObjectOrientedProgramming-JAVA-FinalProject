package adminpanel;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import java.util.Date;

class DBConnect {
    private Connection cn;
    private Statement st;
    private ResultSet rs;
    
    public DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/permitteddriving","root","");
            st=cn.createStatement();
        }
        catch(Exception e){
            System.out.println("Error!"+e);
            e.printStackTrace();
        }
    }
    
    public ResultSet getUnAuthorizedAccess(){
        try{
            String query="select * from Unauthorizeddrivers;";
            //st.executeUpdate(query);
            rs=st.executeQuery(query);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exception!");
            System.out.println(e);
        }
        return rs;
    }
    
    public boolean removeMember(String name,String did){
        try{
            String query="select * from Drivers;";
            rs=st.executeQuery(query);  
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exception!");
            System.out.println(e);
        }
        try {
            while(rs.next()){
                if(rs.getString("DID").toString().equals(did)&&rs.getString("Name").toString().equals(name)){
                    try{
                        String query="Delete from Drivers where DID='"+did+"';";
                        st.executeUpdate(query);
                        return true;
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "Exception!");
                        System.out.println(e);
                    }
                }
                else
                    return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Exception!");
            System.out.println(ex);            
        }
          return true;
    }
    
    public ResultSet getAdminShortDetails(){
        try{
            String query="select * from Drivers;";
            rs=st.executeQuery(query);  
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exception!");
            System.out.println(e);
        }
        return rs;
    }
    
    public boolean insertInformationIntoDB(String name,String password,String contactno,String email,String DID,String expiredate){
        try{
            String query = "insert into Drivers (DID,Name,Password,ContractNumber,Email,ExpireDate) values ('"
                +DID+"','"+name+"','"+password+"','"+contactno+"','"+email+"',"+expiredate+");";
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Driver Info Added Succesfully!");
            return true;
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Database insertion exception!");
            System.out.println(e);
        }
        return false;
    }
    
    public boolean checkValidityFromDB(String s){;
        try{
            String query="select * from Drivers;";
            rs=st.executeQuery(query);
            while (rs.next()){
                if(rs.getString("DID").equals(s)){
                    if(rs.getDate("ExpireDate").getTime()>=System.currentTimeMillis()){
                        return true;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Your Licence Is Expired!\nPlease Renew The License");
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exception!");
            System.out.println(e);            
        }
        return false;
    }
    
    public boolean recordUnauthorizedAccess(String did){
        try{
            String date =new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            /*Date d = new Date();
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            String date = s.format(d);
            System.out.println(date)*/
            String query = "insert into unauthorizeddrivers (DID,Time) values ('"
                +did+"',"+date+");";
            //System.out.println(query);
            st.execute(query);
            JOptionPane.showMessageDialog(null, "ID is Been Recorded.\nBe Aware!");
            return true;
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Database insertion exception!");
            System.out.println(e);
        }
        return false;
    }
  
    public void clearUnauthorizedData(){
        String query="Delete from unauthorizeddrivers;";
        try {
            st.execute(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Deletion exception!");
            System.out.println(ex);
        }
    }
}
