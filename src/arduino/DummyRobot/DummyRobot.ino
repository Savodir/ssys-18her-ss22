#include <Servo.h>
#include <NewPing.h>

NewPing sensor1(7, 6, 300);
NewPing sensor2(5, 4, 300);

String xval = "";
String yval = "";
String yvaltemp = "";
int x = 0;
int y = 0;
float distance1 = 0, distance2 = 0;
int sensorX = 230;
int sensorY = 256;
int wrongvalue = 5;
bool commandGiven = false;
int ledWrong = 12;
int ledRight = 11;
int pos = 0;
char inbyte = "";
Servo myservo;
String receivedData;
void setup() {
  Serial.begin(9600);
  myservo.attach(8);
  pinMode(ledWrong, OUTPUT);
  pinMode(ledRight, OUTPUT);
  digitalWrite(ledWrong, HIGH);
  digitalWrite(ledRight, LOW);
}

void loop() {
  // put your main code here, to run repeatedly:
  readDistance();
  delay(500);
  if(sensorX >= x - wrongvalue && sensorY >= y - wrongvalue && sensorX <= x + wrongvalue && sensorY <= y + wrongvalue) {
    digitalWrite(ledWrong, LOW);
    digitalWrite(ledRight, HIGH);
    }
if (Serial.available() > 0)
  {
    inbyte = Serial.read();
    switch (inbyte) {
  case 'a':
    commandGiven = true;
    break;
  case 'b':
    xval = getValue(receivedData, ',', 0);
    yval = getValue(receivedData, ',', 1);
    Serial.println("Y:" + yval);
    Serial.print("X:" + xval);
    receivedData = "";
    break;
  case 'x':
  //CalibrationChar, tijdelijke testchar
    pos = pos + 10;
    myservo.write(pos);
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
  }
  if(tempdistance2 > 0.20){
    distance2 = tempdistance2;
  }
  Serial.print("Distance1= ");
  Serial.println(distance1);
  Serial.print("Distance2= ");
  Serial.println(distance2);
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

