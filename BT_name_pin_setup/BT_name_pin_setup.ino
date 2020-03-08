#include<SoftwareSerial.h>
SoftwareSerial BTCom(10,11);

// AT+PSWD shows us the BT module Pin
// AT+PSWD = 1234 sets the pin to '1234'

// AT+NAME shows us the name of the BT module
// AT+NAME = GG_01 sets name to 'GG_01'

// Pins used:
//VCC(BT) => 5V (on board)
//GND(BT) => GND (on board)
//RXD(BT) => D10 (on board)
//TXD(BT) => D11 (on board)
//EN(BT) => 5V (on board)

void setup() {
  // put your setup code here, to run once:
  Serial.begin(38400);
  BTCom.begin(38400);
  Serial.println("Enter AI Commands");
}

void loop() {
  // put your main code here, to run repeatedly:
  if(BTCom.available())
  {
    Serial.write((BTCom.read()));
  }
  if(Serial.available())
  {
    BTCom.write((Serial.read()));
  }
}
