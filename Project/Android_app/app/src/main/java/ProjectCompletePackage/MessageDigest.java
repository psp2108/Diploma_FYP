package ProjectCompletePackage;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class MessageDigest {
    private String allBits = "";
    
    private String[] nBlocksOf64Bits;
    private long[] nBlocksOf64BitsLong;
    
    private String numberOfAllBitsString;
    long numberOfAllBits;
    
    private String numberOfOnesString;
    long numberOfOnes;
    
    private String KeyString;
    long Key;
    
    private String AndOperation;
    private long AndOperationLong;
    
    private String OrOperation;
    private long OrOperationLong;
    
    private String XOROperation;
    private long XOROperationLong;
    
    private String MessageDigestV;
    /**
     * Algorithm
     * 1. Read total number of bits
     * 2. Read number of 1's
     * 3. Split message into 64-bit 'n' blocks
     * 4. Generate 64-bit key
     * 5. Perform and,or,xor operation with key and first block
     * 6. then answer of each operation is a input for next block with same operations
    */
        //op = "0000000000000000000000000000000000000000000000000000000000000000".substring(op.length()) + op;
        //op = op + "0000000000000000000000000000000000000000000000000000000000000000".substring(op.length());
    
    
    private String oneGetBinary(String message){
        if (!message.equals("")){
            byte[] b = message.getBytes();
            allBits = "";
            for (byte b1 : b) {
                allBits += Integer.toBinaryString(b1);// + " ";
            }
            return allBits;
            }
        else{
            return allBits;
        }
    }
    
    private String twoCountTotalBits(){
        numberOfAllBits = allBits.length();
        numberOfAllBitsString = Long.toBinaryString(numberOfAllBits);
        return numberOfAllBitsString;
    }
    
    private String threeCountOnesInBits(){
        numberOfOnes = 0;
        char[] wholeBits = new char[allBits.length()];
        allBits.getChars(0,allBits.length()-1,wholeBits,0);
        
        for(char a : wholeBits){
            if (a == '1'){
                numberOfOnes++;
            }
        }
        numberOfOnesString = Long.toBinaryString(numberOfOnes);
        return numberOfOnesString;
    }
    
    private String[] fourDividIntoNBlocks(){
        int count = 1;
        long multiples = 64;
        while(multiples < numberOfAllBits){
            count++;
            multiples = multiples + 64;
        }
        
        int i=0;
        nBlocksOf64Bits = new String[count];
        nBlocksOf64BitsLong = new long[count];
        for(i=0; i<count-1; i++){
            nBlocksOf64Bits[i] = allBits.substring(i*64,(i+1)*64);
            nBlocksOf64BitsLong[i] = Long.valueOf(nBlocksOf64Bits[i].substring(1), 2);                
            if(nBlocksOf64Bits[i].charAt(0) == '1'){
                nBlocksOf64BitsLong[i] = nBlocksOf64BitsLong[i] * -1;
            }
        }
        nBlocksOf64Bits[i] = allBits.substring(i*64) + 
            "0000000000000000000000000000000000000000000000000000000000000000".substring(allBits.substring(i*64).length());
        nBlocksOf64BitsLong[i] = Long.valueOf(nBlocksOf64Bits[i].substring(1), 2);
        if(nBlocksOf64Bits[i].charAt(0) == '1'){
            nBlocksOf64BitsLong[i] = nBlocksOf64BitsLong[i] * -1;
        }
        
        return nBlocksOf64Bits;
    }
    
    private String fiveGenerate64BitKey(){
        Random r = new Random();
        Key = (-1);
        
        while(Key < 0){
            Key = r.nextLong();
        }
        
        KeyString = Long.toBinaryString(Key);
        KeyString = "0000000000000000000000000000000000000000000000000000000000000000".substring(KeyString.length()) + KeyString;
        
        //System.out.println(Key);
        return Long.toBinaryString(Key);
    }
    
    private String sixAndOperations(){
        AndOperationLong = Key & nBlocksOf64BitsLong[0];
        
        for(int i=1; i<nBlocksOf64BitsLong.length; i++){
            AndOperationLong = AndOperationLong & nBlocksOf64BitsLong[i];
        }
        
        AndOperation = Long.toBinaryString(AndOperationLong);
        return AndOperation;
    }
    private String sevenOrOperations(){
        OrOperationLong = Key & nBlocksOf64BitsLong[0];
        
        for(int i=1; i<nBlocksOf64BitsLong.length; i++){
            OrOperationLong = OrOperationLong | nBlocksOf64BitsLong[i];
        }
        
        OrOperation = Long.toBinaryString(OrOperationLong);
        return OrOperation;
    }
    private String eigthXOROperations(){
        XOROperationLong = Key & nBlocksOf64BitsLong[0];
        
        for(int i=1; i<nBlocksOf64BitsLong.length; i++){
            XOROperationLong = XOROperationLong ^ nBlocksOf64BitsLong[i];
        }
        
        XOROperation = Long.toBinaryString(XOROperationLong);
        return XOROperation;
    }
    
    private String B2D(String bitsBinary){
        BigInteger Decimal = new BigInteger("0");
	BigInteger BitsPos = new BigInteger("1");
	int BitsLength=bitsBinary.length()-1;
		
	while(BitsLength >= 0){
            if(bitsBinary.charAt(BitsLength) == '1'){
                Decimal = Decimal.add(BitsPos);
            }
            BitsPos = BitsPos.add(BitsPos);
            
            BitsLength--;
	}
		
	return Decimal.toString();
    }
    
    private String nineMakeMessageDigest(){
        String rawMD = "";
        
        rawMD = numberOfAllBitsString;
        rawMD += numberOfOnesString;
        rawMD += AndOperation;
        rawMD += OrOperation;
        rawMD += XOROperation;
        rawMD += KeyString;
        
        MessageDigestV = B2D(rawMD);
        //System.out.println("-> "  + MessageDigestV +  " <-");
        return MessageDigestV;
    }
    
String D2B(String Decimal){
    BigInteger DecimalNo = new BigInteger(Decimal);
    BigInteger MSB = new BigInteger("1");
    StringBuilder BitsString = new StringBuilder("");
    int i=1;
		
    while(MSB.compareTo(DecimalNo) != 1){
        MSB = MSB.add(MSB);
        i++;
    }
    MSB = MSB.divide(BigInteger.valueOf(2));
		
    if (!Decimal.equals("0")){
        BitsString.append("1");
        DecimalNo = DecimalNo.subtract(MSB);
        MSB = MSB.divide(BigInteger.valueOf(2));
			
        while(MSB.compareTo(BigInteger.valueOf(0)) != 0){
            if(MSB.compareTo(DecimalNo) <= 0){
                DecimalNo = DecimalNo.subtract(MSB);
                BitsString.append("1");
            }
            else{
                BitsString.append("0");
            }
				
            MSB = MSB.divide(BigInteger.valueOf(2));
	}
    }
    else{
        BitsString.append("0");
    }
    //System.out.println("-> "  + BitsString.toString() +  " <-");
    return BitsString.toString();
}
    
    private long extractKeyFromMD(String _MessageDigest){
        allBits = D2B(_MessageDigest);
        KeyString = allBits.substring(allBits.length() - 64);
        Key = Long.valueOf(KeyString.substring(1), 2);
        if(KeyString.charAt(0) == '1'){
            Key = Key * -1;
        }
        return Key;
    }
    public boolean checkIntegrity(String PlainMessage, String _MessageDigest){
        Key = extractKeyFromMD(_MessageDigest);
        oneGetBinary(PlainMessage);
        twoCountTotalBits();
        threeCountOnesInBits();
        fourDividIntoNBlocks();
        sixAndOperations();
        sevenOrOperations();
        eigthXOROperations();
        if(nineMakeMessageDigest().equals(_MessageDigest)){
            return true;
        }
        else{
            return false;
        }
    }
    
    public String getMessageDigest(String PlainMessage){
        oneGetBinary(PlainMessage);
        twoCountTotalBits();
        threeCountOnesInBits();
        fourDividIntoNBlocks();
        fiveGenerate64BitKey();
        sixAndOperations();
        sevenOrOperations();
        eigthXOROperations();
        return (nineMakeMessageDigest());
    }
    
}
