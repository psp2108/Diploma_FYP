package ProjectCompletePackage;

import java.io.*;
import java.net.*;
import java.util.*;



public class MainCommunicatingClass {
    
    private URL url;
    private URLConnection urlConnection;
    private BufferedReader OutputReader;
    private HashMap<String, Integer> DepartmentAndIDMapping;
    private HashMap<String, Integer> EmployeeAndSensorIDMapping;
    private QueryURLFormation queryURL;
    private StringSplitter stringSplitter;
    private String OutputLine;
    private MessageDigest md;
    private String otp;
    
    public MainCommunicatingClass(String ipFilePath){
        DepartmentAndIDMapping = new HashMap<String, Integer>();
        EmployeeAndSensorIDMapping = new HashMap<String, Integer>();
        queryURL = new QueryURLFormation("http://192.168.1.106/BiometricAttendance/AIO.php" );
        stringSplitter = new StringSplitter(":-:");
        md = new MessageDigest();
    }
    
    private String callProcedureViaPHP(String urlString){
        
        try{
            url = new URL(urlString);
            urlConnection = url.openConnection();
            OutputReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return "Successful";
        }
        catch(Exception ex){
            
            return ex.toString();
        }
        
    }
    //////////////////////////////////////////////////////////////////
    public String checkIfAdminTableIsEmpty() throws Exception{
        /*
            1. Chek for the content in admin table and returns ture if empty else false
        
            Procedure 16
        */
        
        queryURL.setProcedureID(16);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();

        return OutputLine;
        /*
        if(OutputLine.equals("true"))
            return true;
        else
            return false;*/
    }
    
    public String checkAdminCredentials(String Username, String Password)throws Exception{
        /*
            1. take username and get password hash stored in database with admin permissions
            2. if no username found WRONG CREDENTIALS
            3. else compare the password with the hash value returned
            4. If true then return permisson of admin
            5. Else return WRONG CREDENTIALS
        
            Procedure 4 and some logic
        */
        queryURL.setProcedureID(4);
        queryURL.append(Username);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine != null){
            stringSplitter.setStringToSplit(OutputLine);
            String adminPerms = stringSplitter.getString(1);
            String adminPasswordHash = stringSplitter.getString(0);
            
            if(md.checkIntegrity(Password, adminPasswordHash)){
                return adminPerms;
            }
            else{
                return "WRONG CREDENTIALS";
            }
            
        }
        else{
            return "WRONG CREDENTIALS";
        }
    }

    public String generateAndEmailOTP(String Username)throws Exception{
        /*
            1. Takes Username and request php page to email and return otp
            2. Stores the OTP in some String for some time;
            3. Returns false if username is wrong or if mail not send
        
            Procedure 10 and some logic in php page too
        */
        queryURL.setProcedureID(10);
        queryURL.append(Username);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();

        if(OutputLine != null){
            stringSplitter.setStringToSplit(OutputLine);
            otp = stringSplitter.getString(0);
            String emailStatus = stringSplitter.getString(1);

            if(emailStatus.equals("true"))
                //If Mail Sent
                return otp;
            else
                //If Mail not Sent
                return otp;

        }
        else {
            return "WRONG USERNAME";
        }

    }

    public String getAdminEmail(String Username)throws Exception{
        /*
            1. Takes Username and request php page to email and return otp
            2. Stores the OTP in some String for some time;
            3. Returns false if username is wrong or if mail not send

            Procedure 10 and some logic in php page too
        */
        queryURL.setProcedureID(10);
        queryURL.append(Username);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();

        return OutputLine;

    }

    public boolean verifyOTP(String otp)throws Exception{
        /*
            1. Verifies the otp received via Email
            2. Returns true if otp verified else false
        */
        
        return otp.equals(this.otp);
    }
    
    public String updatePassword(String Username, String Password)throws Exception{
        /*
            1. Update password with username
            2. return true if updated else false
        
            Procedure 11 with some extra logic
        */
        
        String hash1 = md.getMessageDigest(Password);
        String hash2 = md.getMessageDigest(hash1);
        
        queryURL.setProcedureID(11);
        queryURL.append(Username);
        queryURL.append(hash1);
        queryURL.append(hash2);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        return OutputLine;
    }
    
    public String[] getEmployeeDetails(String AdminUsername, int EmplyeeFPID)throws Exception{
        /*
            1. get employee details and return it in form of array
            2. Details are:
                -First Name
                -Last Name
                -PS Number
                
            Procedure 3
        */
        
        String EmployeeDetails[];
        
        queryURL.setProcedureID(3);
        queryURL.append(EmplyeeFPID);
        queryURL.append(AdminUsername);
        callProcedureViaPHP(queryURL.getCompleteURL());

        OutputLine = OutputReader.readLine();
        stringSplitter.setStringToSplit(OutputLine);
        EmployeeDetails = new String[stringSplitter.getTotalcount()];
        
        for(int iColCounter = 0; iColCounter < EmployeeDetails.length; iColCounter++){
            EmployeeDetails[iColCounter] = stringSplitter.getString(iColCounter);
        }

        OutputReader.close();
        
        return EmployeeDetails;
    }
    
    public boolean markAttendance(String AdminUsername, int EmployeeFPID)throws Exception{
        /*
            1. Mark the attendance as present
        
            Procedure 2
        */
        queryURL.setProcedureID(2);
        queryURL.append(new Date());
        queryURL.append(EmployeeFPID);
        queryURL.append(AdminUsername);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine.equals("Record Inserted"))
            return true;
        else
            return false;
    }
    
    public int addEmployee(
            String FirstName,
            String LastName,
            String PSNumber,
            String Department,
            String Email,
            Date DateOfBirth,
            String Gender,
            Date HireFrom,
            Date HireTill,
            String AdminUsername
    )throws Exception{
        /*
            1. Add employees
            2. Return Finger PrintID
        
            Procedure 1
        */
        getDepartmentList();
        
        queryURL.setProcedureID(1);
        queryURL.append(PSNumber);
        queryURL.append(FirstName);
        queryURL.append(LastName);
        queryURL.append(DepartmentAndIDMapping.get(Department));
        queryURL.append(Email);
        queryURL.append(DateOfBirth);
        queryURL.append(Gender);
        queryURL.append(HireFrom);
        queryURL.append(HireTill);
        queryURL.append(new Date());
        queryURL.append(AdminUsername);
        
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine != null)
            if(OutputLine.equals("Sensor limit reached. Record not inserted!"))
                return -1;
            else
                return Integer.parseInt(OutputLine);
        else
            return -1;
        
    }
    
    public String[] getDepartmentList()throws Exception{
        /*
            1. Returns the array of all department list 
            2. List format <Dept Name- Dept Desciption (Location)>
            3. Also manage the hash map (table) for Department IDs and list
        
            Procedure 8
        */
        
        DepartmentAndIDMapping.clear();
        Vector<String> vector = new Vector<>();
        String tempRow;
        
        queryURL.setProcedureID(8);
        callProcedureViaPHP(queryURL.getCompleteURL());
        
        while((OutputLine = OutputReader.readLine()) != null ){
            stringSplitter.setStringToSplit(OutputLine);
            tempRow = stringSplitter.getString(1);
            tempRow += ("- " + stringSplitter.getString(2));
            tempRow += (" (" + stringSplitter.getString(3) + ")");
            vector.add(tempRow);
            DepartmentAndIDMapping.put(tempRow, Integer.parseInt(stringSplitter.getString(0)));
        }
        
        OutputReader.close();
        String tempRows[] = new String[vector.size()];
        vector.toArray(tempRows);
        
        return tempRows;
    }
    
    public String[] getAdminProfile(String AdminUSername)throws Exception{
        /*
            1. Returns admin details in array 
            2. Details are:
                -First Name
                -Last Name
                -Email
                -DOB
                -Gender
                -Department format <Dept Name- Dept Desciption (Location)>
            3. Also manage the hash map (table) for Department ID
        
            Procedure 12
        */
        
        String[] AdminProfile;
        
        queryURL.setProcedureID(12);
        queryURL.append(AdminUSername);
        callProcedureViaPHP(queryURL.getCompleteURL());

        OutputLine = OutputReader.readLine();
        stringSplitter.setStringToSplit(OutputLine);
        AdminProfile = new String[stringSplitter.getTotalcount() + 1];
        
        for(int iColCounter = 0; iColCounter < AdminProfile.length - 1; iColCounter++){
            AdminProfile[iColCounter] = stringSplitter.getString(iColCounter);
        }
        AdminProfile[AdminProfile.length - 1] = stringSplitter.getString(5);
        AdminProfile[AdminProfile.length - 1] += ("- " + stringSplitter.getString(6));
        AdminProfile[AdminProfile.length - 1] += (" (" + stringSplitter.getString(7) + ")");
                
        OutputReader.close();
        
        getDepartmentList();
        
        return AdminProfile;
    }
    
    
    
    public String[] getEmployeesList(String AdminUsername)throws Exception{
        /*
            1. Returns the list of all employees under current Admin department
            2. List Format <PSNumber- Employee Name>
            3. Also manage the hash map (table) for Employee Fingerprint IDs and list
        
            Procedure 13
        */
        
        EmployeeAndSensorIDMapping.clear();
        Vector<String> vector = new Vector<>();
        String tempRow;
        
        queryURL.setProcedureID(13);
        queryURL.append(AdminUsername);
        callProcedureViaPHP(queryURL.getCompleteURL());
        
        while((OutputLine = OutputReader.readLine()) != null ){
            stringSplitter.setStringToSplit(OutputLine);
            tempRow = stringSplitter.getString(1);
            tempRow += ("- " + stringSplitter.getString(2));
            tempRow += (" " + stringSplitter.getString(3));
            vector.add(tempRow);
            EmployeeAndSensorIDMapping.put(tempRow, Integer.parseInt(stringSplitter.getString(4)));
        }

        OutputReader.close();
        
        String tempRows[] = new String[vector.size()];
        vector.toArray(tempRows);
        
        return tempRows;
    }
    
    public String getEmployeeSensorID(String EmployeeDetails){
        return EmployeeAndSensorIDMapping.get(EmployeeDetails) + "";
    }

    public int getDepartmentSpinnerPosition(String DepartmentName){
        return DepartmentAndIDMapping.get(DepartmentName);
    }
    
    public boolean removeEmployeeBiometrics(String EmployeeDetails, String AdminUsername)throws Exception{
        /*
            1. Remove the content of FingerPrint Table i.e only the biometrics of employee
            2. Returns Deleted Successfull if everything goes fine
        
            Procedure 15
        */ 
        getEmployeesList(AdminUsername);
        
        queryURL.setProcedureID(15);
        queryURL.append(EmployeeAndSensorIDMapping.get(EmployeeDetails));
        queryURL.append(AdminUsername);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine.contains("Deleted Successfully"))
            return true;
        else
            return false;
    }
    
    public boolean ifUsernameAvailable(String Username)throws Exception{
        /*
            1. Checks if Username is available or not
            2. Returns true if available or false
        
            Procedure 14
        */
        
        queryURL.setProcedureID(14);
        queryURL.append(Username);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine.equals("true"))
            return true;
        else
            return false;
        
        
    }
    
    public String addNewAdminCredentials(String Username, String Password)throws Exception{
        /*
            1. Inserts onky username and Password of Admin
        `   2. Returns the status if added?, username taken?, Password Interrupted?
        
            Procedure 5
        */
        String hash1 = md.getMessageDigest(Password);
        String hash2 = md.getMessageDigest(hash1);
        
        queryURL.setProcedureID(5);
        queryURL.append(Username);
        queryURL.append(hash1);
        queryURL.append(hash2);
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        return OutputLine;
    }
    
    public boolean addNewAdminDetails(
            String FirstName,
            String LastName,
            String AdminUsername,
            String Email,
            Date DateOfBirth,
            String Gender,
            String DepartmentDetails
    )throws Exception{
        /*
            1. Insert new admin with old department
            2. Returns status if inserted or not
        
            Procedure 7
        */
        return updateAdminProfile(FirstName, LastName, AdminUsername, Email, DateOfBirth, Gender, DepartmentDetails);
    }
    public boolean updateAdminProfile(
            String FirstName,
            String LastName,
            String AdminUsername,
            String Email,
            Date DateOfBirth,
            String Gender,
            String DepartmentDetails
    )throws Exception{
        /*
            1. Updates the profile of admin and old department
            2. Returns false if username is invalid
        
            Procedure 7
        */
        getDepartmentList();
        
        queryURL.setProcedureID(7);
        queryURL.append(FirstName);
        queryURL.append(LastName);
        queryURL.append(AdminUsername);
        queryURL.append(Email);
        queryURL.append(DateOfBirth);
        queryURL.append(Gender);
        queryURL.append(DepartmentAndIDMapping.get(DepartmentDetails));
        
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine.equals("Record Inserted"))
            return true;
        else
            return false;
    }
    
    public boolean addNewAdminDetails(
            String FirstName,
            String LastName,
            String AdminUsername,
            String Email,
            Date DateOfBirth,
            String Gender,
            String DepartmentName,
            String DepartmentDescription,
            String Location
    )throws Exception{
        /*
            1. Insert new admin with new department
            2. Returns status if inserted or not
        
            Procedure 6
        */
        return updateAdminProfile(FirstName, LastName, AdminUsername, Email, DateOfBirth, Gender, DepartmentName, DepartmentDescription, Location);
    }
    
    public boolean updateAdminProfile(
            String FirstName,
            String LastName,
            String AdminUsername,
            String Email,
            Date DateOfBirth,
            String Gender,
            String DepartmentName,
            String DepartmentDescription,
            String Location
    )throws Exception{
        /*
            1. Updates the profile of admin and new department
            2. Returns false if username is invalid
        
            Procedure 6
        */
        
        queryURL.setProcedureID(6);
        queryURL.append(FirstName);
        queryURL.append(LastName);
        queryURL.append(AdminUsername);
        queryURL.append(Email);
        queryURL.append(DateOfBirth);
        queryURL.append(Gender);
        queryURL.append(DepartmentName);
        queryURL.append(DepartmentDescription);
        queryURL.append(Location);
        
        callProcedureViaPHP(queryURL.getCompleteURL());
        OutputLine = OutputReader.readLine();
        OutputReader.close();
        
        if(OutputLine.equals("Record Inserted"))
            return true;
        else
            return false;
    }
}
