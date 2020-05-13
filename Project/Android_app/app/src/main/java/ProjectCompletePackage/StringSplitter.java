package ProjectCompletePackage;

import java.io.*;
import java.net.*;
import java.util.*;

public class StringSplitter {
    private Vector<String> vector;
    String deliminator;
    
    public StringSplitter(String deliminator){
        this.vector = new Vector<String>();
        this.deliminator = deliminator;
    }
    
    public void setStringToSplit(String string){
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(deliminator);
        vector.clear();
        
        while(scanner.hasNext()){
            vector.add(scanner.next());
        }
    }
    
    public int getTotalcount(){
        return vector.size();
    }
    
    public String getString(int index){
        return vector.elementAt(index);
    }

}
