#include <TinyDebugSerial.h>

int sensorPin = A1;    // select the input pin for the potentiometer
int sensorPin2 = A2;

int sensorValue = 0;  // variable to store the value coming from the sensor


TinyDebugSerial mySerial = TinyDebugSerial();

void setup() {
  Serial.begin(9600);    

}

void sendInt(int val) {
  Serial.write( (val>> 8) & 0xff );
  Serial.write( (val    ) & 0xff );
}

void loop() {  
  int sync  = 0xaabb;
  int sens1 = 255;
  int sens2 = 260;
  
  //*
  sens1 = analogRead(sensorPin);  
  sens2 = analogRead(sensorPin2); //*/
  delay(20);  
  
  sendInt(sync);
  sendInt(sens1);
  sendInt(sens2);  
}
