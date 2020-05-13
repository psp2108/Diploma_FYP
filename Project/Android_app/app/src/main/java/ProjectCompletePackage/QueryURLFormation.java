package ProjectCompletePackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.*;
import java.util.Date;
//import sun.net.www.protocol.http.HttpURLConnection;

public class QueryURLFormation{
    private String[] syntaxList = {
        "c",                //0
        "sssnsdsddds",      //1
        "dns",              //2
        "ns",               //3
        "s",                //4
        "sss",              //5
        "ssssdssss",        //6
        "ssssdsn",          //7
        "",                 //8
        "",                 //9
        "s",                //10
        "sss",              //11
        "s",                //12
        "s",                //13
        "s",                //14
        "ns",               //15
        "",                 //16
    };
    
    private int ProcedureID = 0;
    private int counter = 0;
    private StringBuilder parameterList;
    private String initialURL = "";
    
    QueryURLFormation(String url){
        initialURL = url;
    }
    
    public void clearAll(){
        parameterList = null;
        counter = 0;
    }
    
    public void setProcedureID(int id){
        parameterList = new StringBuilder();
        counter = 0;
        ProcedureID = id;
    }
    
    public void append(Object arg) throws InvalidParameterFormatException{
        if(counter != 0){
            parameterList.append(",");
        }
        if(arg != null){
            
            if(("" + arg).contains(":-:")){
                throw new InvalidParameterFormatException();
            }
            
            switch (syntaxList[ProcedureID].charAt(counter)){
                case 's':
                    parameterList.append("'");
                    parameterList.append((arg + "").replace("'", "\\'"));
                    parameterList.append("'");
                    break;
                case 'n':
                    parameterList.append(Long.parseLong(arg + ""));
                    break;
                case 'd':
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    parameterList.append("'");
                    parameterList.append(df.format(arg));
                    parameterList.append("'");
                    break;
            }
        }
        else{
            parameterList.append("null");            
        }
        
        counter++;
    }
    
    public String getCompleteURL(){
        String header = "?id="+ ProcedureID +"&param=";
        String partialURL = initialURL + header + parameterList.toString().replace(" ", "%20");;
        return partialURL;    
    }

    
    public class InvalidParameterFormatException extends Exception{
        InvalidParameterFormatException(){
            super("(:-:) Pattern is not allowed");
        }
    }
    
}
