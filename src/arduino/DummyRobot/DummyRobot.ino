#include <Servo.h>
#include <NewPing.h>
#include <LSM9DS1_Registers.h>
#include <LSM9DS1_Types.h>
#include <SparkFunLSM9DS1.h>
#include <Wire.h>
#include <SPI.h>
NewPing sensor1(7, 6, 300);
NewPing sensor2(5, 4, 300);

String xval = "";
String yval = "";
String yvaltemp = "";
int x = 0; 
int y = 0;
int l = 0;
int b = 0;
int xcoord;
int ycoord;
float distance1 = 0, distance2 = 0;
int currentHeading;
int sensorX;
int sensorY;
int servopos = 0;
double drift;
int wrongvalue = 25;
long currentMillis;
long prevMillis;
double pastTime;
int ledWrong = 12;
int ledRight = 11;
char inbyte = "";
Servo myservo;
String receivedData;
LSM9DS1 imu;
#define LSM9DS1_M 0x1E
#define LSM9DS1_AG  0x6B

#define PRINT_CALCULATED
//#define PRINT_RAW

#define DECLINATION 0

void setup() {
  Serial.begin(9600);
  myservo.attach(8);
  pinMode(ledWrong, OUTPUT);
  pinMode(ledRight, OUTPUT);
  digitalWrite(ledWrong, HIGH);
  digitalWrite(ledRight, LOW);
  imu.settings.device.commInterface = IMU_MODE_I2C;
  imu.settings.device.mAddress = LSM9DS1_M;
  imu.settings.device.agAddress = LSM9DS1_AG;
  drift = 1.5;
  currentHeading = 0;
  currentMillis = 0;
  prevMillis = 0;
  pastTime = 0;
  if (!imu.begin())
  {
    Serial.println("Failed to communicate with LSM9DS1.");
    Serial.println("Double-check wiring.");
    Serial.println("Default settings in this sketch will " \
                  "work for an out of the box LSM9DS1 " \
                  "Breakout, but may need to be modified " \
                  "if the board jumpers are.");
    //while (1)   Serial.println("Hello");
  }
  recalibrate();
}

void loop() {
  // put your main code here, to run repeatedly:
  readDistance();
  readSensors();
  delay(200);
  calcXY();
  myservo.write(servopos);                  // sets the servo position according to the scaled value
if (Serial.available() > 0)
  {
    inbyte = Serial.read();
    switch (inbyte) {
  case 'b':
    xval = getValue(receivedData, ',', 0);
    yval = getValue(receivedData, ',', 1);
    Serial.println("Y:" + yval);
    Serial.print("X:" + xval);
    receivedData = "";
    break;
  case 'x':
  //CalibrationChar, tijdelijke testchar
    recalibrate();
    break;
  case 'y':
    digitalWrite(ledWrong, HIGH);
    digitalWrite(ledRight, LOW);
    break;
  default:
    receivedData = receivedData + inbyte;
    xval = getValue(receivedData, ',', 0);
    yvaltemp = getValue(receivedData, ',', 1);
    yval = yvaltemp.substring(0,yvaltemp.length()-1);
    if(inbyte == 'z') {
    Serial.println("X:" + xval);
    Serial.println("Y:" + yval);
    x = xval.toInt();
    y = yval.toInt();
    receivedData = "";
    xval = "";
    yval = "";
    yvaltemp = "";
    inbyte = 'a';
    if(xcoord >= x - wrongvalue && ycoord >= y - wrongvalue && xcoord <= x + wrongvalue && ycoord <= y + wrongvalue) {
    digitalWrite(ledWrong, LOW);
    digitalWrite(ledRight, HIGH);
       }
    x = 9999999;
    y = 9999999;
      }
    break;
    }
  }
 }
void readDistance() {
  //Reads all sensor distances
  double tempdistance1 = sensor1.ping_cm();//(duration1*0.034/2)/distanceError;
  delay(50);
  double tempdistance2 = sensor2.ping_cm();//(duration2*0.034/2)/distanceError;
  if(tempdistance1 > 0.20){
    distance1 = tempdistance1;
    sensorX = distance1;
  }
  if(tempdistance2 > 0.20){
    distance2 = tempdistance2;
    sensorY = distance2;
  }
  Serial.print("Distance1= ");
  Serial.println(distance1);
  Serial.print("Distance2= ");
  Serial.println(distance2);
}
void readSensors() {
  //Reads out the Gyro sensor for heading calculations
  if ( imu.gyroAvailable() )
  {
    // To read from the gyroscope,  first call the
    // readGyro() function. When it exits, it'll update the
    // gx, gy, and gz variables with the most current data.
    imu.readGyro();
  }

  currentMillis = millis();

  pastTime = (currentMillis - prevMillis) * 0.001;
  if((imu.calcGyro(imu.gz)- drift) > 0.25 || (imu.calcGyro(imu.gz)- drift) < -0.25)
  {
    currentHeading += (imu.calcGyro(imu.gz) - drift) * pastTime; 
  }
  if(currentHeading < 0){
    currentHeading = currentHeading + 360;
  } 
  if(currentHeading >= 360){
    currentHeading = currentHeading - 360;
  } 
  prevMillis = currentMillis;
    Serial.println(currentHeading);
}
void recalibrate() {
  //Calibrates the sensors
  //Calibrate Gyro
  float tempDrift = 0;
  delay(600);
  for(int i = 0; i < 10; i++){
    delay(100);
    if ( imu.gyroAvailable() )
    {
      // To read from the gyroscope,  first call the
      // readGyro() function. When it exits, it'll update the
      // gx, gy, and gz variables with the most current data.
      imu.readGyro();
    }
    tempDrift += imu.calcGyro(imu.gz);
  }

  drift = tempDrift/10;
  //And normalize distance sensor readings
  currentHeading = 0;
  myservo.write(0);
  delay(2000);
  int ltemp = 0;
  int btemp = 0;
  readDistance();
  ltemp = ltemp + distance1;
  btemp = btemp + distance2;
  myservo.write(180);
  delay(2000);
  readDistance();
  ltemp = ltemp + distance1;
  btemp = btemp + distance2;
  Serial.println("Lengte: " + String(ltemp) + " Breedte: " + String(btemp));
  b = btemp;
  l = ltemp;
}

  
String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
void calcXY() {
  if(currentHeading < 180) {
    xcoord = sensorX;
    ycoord = sensorY;
    servopos = currentHeading;
    }
  else {
    xcoord = l - sensorX;
    ycoord = b - sensorY;
    servopos = currentHeading - 180;
    Serial.println("Xcoord: " + String(xcoord) + "ycoord: " + String(ycoord));
    }
}

