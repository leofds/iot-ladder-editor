### Installing ESP32 Add-on in Arduino IDE
1. In your Arduino IDE, go to File>Preferences
2. Enter https://dl.espressif.com/dl/package_esp32_index.json into the "Additional Board Manager URLs". Then, click the “OK” button
3. Open the Boards Manager. Go to Tools>Board>Boards Manager
4. Search for ESP32 and press install button for the "ESP32 by Espressif Systems"
5. Select your Board in Tools>Board "ESP32 Dev Module"
6. Select the Port. Go to Tools>Port
### Installing Additional Arduino Libraries
1. Download libraries (.zip): [PubSubClient](https://www.arduino.cc/reference/en/libraries/pubsubclient/), [ArduinoJson](https://www.arduino.cc/reference/en/libraries/arduinojson/)
2. In your Arduino IDE, go to Sketch>Include Library>Add .ZIP Library and include previously downloaded libraries
