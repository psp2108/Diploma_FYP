#include <Adafruit_Fingerprint.h>
#include <SoftwareSerial.h>

const String FINGERPRINT_NOT_FOUND = "R"; 
const String READY_FOR_COMMANDS = "S";
const String INVALID_INPUT = "T";
const String ENTER_FPID_TOENROLL= "X";
const String REMOVE_FINGER = "A";
const String PLACE_FINGER = "Z";
const String PLACE_SAME_FINGER = "B";
const String ENROLL_SUCCESSFUL = "K";
const String ENTER_FPID_TODELETE = "U";
const String FINGERPRINT_DELETED = "V";
const String COMMUNICATION_ERROR = "z";
const String NOT_DELETED = "j";
const String ERROR_WRITING_TO_FLASH = "s";
const String UNKNOWN_ERROR = "W";
const String DATABASE_CLEARED= "E";
const String DATABASE_NOT_CLEARED = "F";
const String IMAGE_ERROR = "x";
const String MESSY_IMAGE = "w";
const String NO_FINGEPRINT_FEATURE = "f";
const String INVALID_IMAGE = "r";
const String FINGERPRINTS_NOT_MATCHED = "i";
const String NO_MATCH_FOUND = "h";

const String ENROLL_NEW_FINGERPRINT = "e";
const String DELETE_FINGER_PRINT = "d";
const String GET_FINGERPRINT_ID = "g";
const String DELETE_ALL_FINGERPRINTS = "c";
const String EXIT_CURRENTMODE = "b";


String readStringOperation;
int id;
uint8_t getFingerprintEnroll();

// pin #2 is IN from sensor (GREEN wire)
// pin #3 is OUT from arduino  (WHITE wire)
SoftwareSerial mySerial(2, 3);
Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);
Adafruit_Fingerprint data = Adafruit_Fingerprint(&mySerial);

void setup()  
{
  Serial.begin(9600);

  // set the data rate for the sensor serial port
  finger.begin(57600);
  
  if (!finger.verifyPassword()) {
    //Fingerprint not found
    Serial.print(FINGERPRINT_NOT_FOUND);
    while (1);
  }
}

void loop()                     
{
  Serial.print(READY_FOR_COMMANDS);  //e: enroll, d: delete, g:getID, c: clear database
  
  while (! Serial.available());
  while (Serial.available()) {
    delay(3);  
    char c = Serial.read();
    readStringOperation += c; 
  }
  
  readStringOperation.trim();
  //Serial.print(readStringOperation);
  if (readStringOperation.length() >0) {
    if(readStringOperation == ENROLL_NEW_FINGERPRINT)
    {
      enrollloop();
    }
    else if(readStringOperation == DELETE_FINGER_PRINT)
    {
      deleteloop();
    }
    else if(readStringOperation == GET_FINGERPRINT_ID)
    {
      getFingerprintIDloop();
    }
    else if(readStringOperation == DELETE_ALL_FINGERPRINTS) {
      clearDatabase();  
    }
    else if(readStringOperation == EXIT_CURRENTMODE) {
      //Serial.print(READY_FOR_COMMANDS);
    }
    else{
      Serial.print(INVALID_INPUT);
      //Invalid input
    }
    readStringOperation="";
  }

}
/***********************************************************************************************************/
void enrollloop(){
  Serial.print(ENTER_FPID_TOENROLL);
  //Serial.print("Ready to enroll a fingerprint! Please Type in the ID # you want to save this finger as...");
  readStringOperation = "";
  while (! Serial.available());
  
  while (Serial.available()) {
    delay(3); 
    char c = Serial.read();
    readStringOperation += c;
  }      
  
  readStringOperation.trim();
  if (readStringOperation.length() >0) {
    if(readStringOperation == EXIT_CURRENTMODE){
      return; 
    }  
    else{
      id = atoi(readStringOperation.c_str());
      if(id > 0 && id < 163){
        getFingerprintEnroll();
      }
      else{
        //Invalid input
        Serial.print(INVALID_INPUT);
      }
    }
  }
}

uint8_t getFingerprintEnroll() {
  int p = -1;
  boolean breakloop = false;
  //Enter ID
  Serial.print(PLACE_FINGER);
  while (p != FINGERPRINT_OK) {
    readStringOperation = "";
    while(Serial.available()){
      delay(3); 
      char c = Serial.read();
      readStringOperation += c;
    }
    readStringOperation.trim();
    if(readStringOperation == EXIT_CURRENTMODE){
      return;
    }
    else{
      p = fingerGetImage();
    }
  }
  
  p = fingerImage2Tz(1);
  if(p != FINGERPRINT_OK){
    return;
  }
  //Remove Finger
  Serial.print(REMOVE_FINGER);
  delay(2000);
  p = 0;
  while (p != FINGERPRINT_NOFINGER) {
    p = finger.getImage();
  }
  p = -1;
  //Place same finger again
  Serial.print(PLACE_SAME_FINGER);
  
  while (p != FINGERPRINT_OK) {
    p = fingerGetImage();
  }
  
  
  // OK success!
  p = fingerImage2Tz(2);
  if(p != FINGERPRINT_OK){
    return;
  }
  //Remove Finger
  Serial.print(REMOVE_FINGER);
  
  p = fingerCreateModel();
  if(p != FINGERPRINT_OK){
    return;
  }
  
  p = fingerStoreModel(id);
  if(p != FINGERPRINT_OK){
    return;
  }
  
  //Enroll Successful
  Serial.print(ENROLL_SUCCESSFUL);
}
/***********************************************************************************************************/
void deleteloop(){
  //Input ID for deleteing
  Serial.print(ENTER_FPID_TODELETE);
  readStringOperation = "";
  while (! Serial.available());
  while (Serial.available()){
    delay(3); 
    char c = Serial.read();
    readStringOperation += c;
  }      
  
  readStringOperation.trim();
  if (readStringOperation.length() >0) {
    if(readStringOperation == EXIT_CURRENTMODE){
      return; 
    }  
    else{
      int id = 0;
      id = atoi(readStringOperation.c_str());
      if(id > 0 && id < 163){
        deleteFingerprint(id);
      }
      else{
        //Invalid Input
        Serial.print(INVALID_INPUT);
      }
    }  
  }      
}

//function to delete fingerprint
uint8_t deleteFingerprint(uint8_t id) {
  uint8_t p = -1;
  p = finger.deleteModel(id);
  
  if (p == FINGERPRINT_OK) {
    //Fingerprint deleted
    Serial.print(FINGERPRINT_DELETED);
  } 
  else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    //Communication Error
    Serial.print(COMMUNICATION_ERROR);
    return p;
  } 
  else if (p == FINGERPRINT_BADLOCATION) {
    //Could not delete
    Serial.print(NOT_DELETED);
    return p;
  } 
  else if (p == FINGERPRINT_FLASHERR) {
    //Error writing to flash
    Serial.print(ERROR_WRITING_TO_FLASH);
    return p;
  } 
  else {
    //Unknown Error
    Serial.print(UNKNOWN_ERROR);
    return p;
  }   
}
/***********************************************************************************************************/
void getFingerprintIDloop(){
  while(1){
    readStringOperation = "";
    boolean breakloop = false;
    while (Serial.available()) {
      delay(3); 
      char c = Serial.read();
      readStringOperation += c;
      if(readStringOperation == EXIT_CURRENTMODE){
        return;
      } 
    }
    getFingerprintID();
    delay(500);
  }
}

uint8_t getFingerprintID() {  
  uint8_t p = fingerGetImage();
  if(p != FINGERPRINT_OK){
    return;
  }
  
  p = fingerImage2Tz(NULL);
  if(p != FINGERPRINT_OK){
    return;
  }
  
  p = fingerFingerFastSearch();
  if(p != FINGERPRINT_OK){
    return;
  }
  String s = String(finger.fingerID);

  while(s.length() < 3){
    s = "0" + s;
  }
  Serial.print(s);
}
/***********************************************************************************************************/
void clearDatabase(){
  uint8_t check = data.emptyDatabase();
  if(check == 0){
    //Database cleared
    Serial.print(DATABASE_CLEARED);
  }
  else{
    //Database not cleared
    Serial.print(DATABASE_NOT_CLEARED);    
  }
}

/***********************************************************************************************************/
/***********************************************************************************************************/

int fingerGetImage(){
  int p;
  p = finger.getImage();
  switch (p) {
    case FINGERPRINT_OK:
      break;
    case FINGERPRINT_NOFINGER:
      break;
    case FINGERPRINT_PACKETRECIEVEERR:
      //Communication Error
      Serial.print(COMMUNICATION_ERROR);
      break;
    case FINGERPRINT_IMAGEFAIL:
      //Image error
      Serial.print(IMAGE_ERROR);
      break;
    default:
      //Unknown Error
      Serial.print(UNKNOWN_ERROR);
      break;
  }
  return p;
}

int fingerImage2Tz(int num){
  int p;
  if(num == NULL){
    p = finger.image2Tz();
  }
  else{
    p = finger.image2Tz(num);
  }
  switch (p) {
    case FINGERPRINT_OK:
      return p;
    case FINGERPRINT_IMAGEMESS:
      //Messy image
      Serial.print(MESSY_IMAGE);
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      //Communication error
      Serial.print(COMMUNICATION_ERROR);
      return p;
    case FINGERPRINT_FEATUREFAIL:
      //Could not find finger print features
      Serial.print(NO_FINGEPRINT_FEATURE);
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      //Could not find finger print features
      Serial.print(INVALID_IMAGE);
      return p;
    default:
      //Unknown error
      Serial.print(UNKNOWN_ERROR);
      return p;
  }
}

int fingerCreateModel(){
  int p;
  p = finger.createModel();
  if (p == FINGERPRINT_OK) {
    return p;
  } 
  else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    //Communication error
    Serial.print(COMMUNICATION_ERROR);
    return p;
  } 
  else if (p == FINGERPRINT_ENROLLMISMATCH) {
    //Fingerprint did not matched
    Serial.print(FINGERPRINTS_NOT_MATCHED);
    return p;
  } 
  else {
    //Unknown error
    Serial.print(UNKNOWN_ERROR);
    return p;
  }   
}

int fingerStoreModel(int id){
  int p;
  p = finger.storeModel(id);
  if (p == FINGERPRINT_OK) {
    return p;
  } 
  else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    //Communication Error
    Serial.print(COMMUNICATION_ERROR);
    return p;
  } 
  else if (p == FINGERPRINT_BADLOCATION) {
    //Could not store
    Serial.print(NOT_DELETED);
    return p;
  } 
  else if (p == FINGERPRINT_FLASHERR) {
    //Error writing to flash
    Serial.print(ERROR_WRITING_TO_FLASH);
    return p;
  } 
  else {
    //Unknown error
    Serial.print(UNKNOWN_ERROR);
    return p;
  }
}

int fingerFingerFastSearch(){
  int p;
  p = finger.fingerFastSearch();
  if (p == FINGERPRINT_OK) {
    return p;
  } 
  else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    //Communication error
    Serial.print(COMMUNICATION_ERROR);
    return p;
  } 
  else if (p == FINGERPRINT_NOTFOUND) {
    //Did not matched
    Serial.print(NO_MATCH_FOUND);
    return p;
  } 
  else {
    //unknown error
    Serial.print(UNKNOWN_ERROR);
    return p;
  }   
}
