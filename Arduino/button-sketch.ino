/**
   Sketch that shows usage of a button to turn on an LED

   Using:
   1. Arduino Uno R3
   2. LED
   3. Button

   Andrew Garner
   August 3, 2018
*/

int buttonPin = 7;
int ledPin = 8;

void setup() {
  // put your setup code here, to run once:
  pinMode(ledPin, OUTPUT);
  pinMode(buttonPin, INPUT_PULLUP);

}

void loop() {
  // put your main code here, to run repeatedly:
  if (isPressed()) {
    digitalWrite(ledPin, HIGH);
  } else {
    digitalWrite(ledPin, LOW);
  }

}

boolean isPressed() {
  return digitalRead(buttonPin) == LOW;
}

