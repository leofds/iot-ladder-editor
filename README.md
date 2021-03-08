# IoT Ladder Editor

<p align="center"><kbd>
  <img src="https://user-images.githubusercontent.com/5174326/110049165-10e0c400-7d30-11eb-9122-5d47fe090d74.png" width="800">
</kbd></p>

This project is a open source code Ladder Editor that generates code for IoT devices. Through the MQTT protocol it is possible to interact with the program variables.
The current version generates Arduino code (.ino) for ESP32.

It's a desktop Java application developed with Eclipse IDE + WindowBuilder plug-in.
## How to execute
1. Install Java 8 (JRE) from Oracle if doesn't already installed
2. To compile the generated code and load it on ESP32 board install the Arduino IDE and setup it, read [README_arduino_ide_setup.md](https://github.com/leofds/iot-ladder-editor/blob/main/README_arduino_ide_setup.md) to do this
3. Download the latest executable Jar file ([here](https://github.com/leofds/iot-ladder-editor/tree/main/target)) and double click to execute it.

## How to execute from source code
1. Install Java 8 (JDK) from Oracle
2. Download the Eclipse IDE, unzip and execute it
3. Import the project on Eclipse IDE, read the [README_importing_into_eclipse.md](https://github.com/leofds/iot-ladder-editor/blob/main/README_importing_into_eclipse.md) to do this
4. On the project, click with the right mouse click and select **Run As>Java Application**

## How to use the software
To understand about the Ladder Language read [README_ladder_language.md](https://github.com/leofds/iot-ladder-editor/blob/main/README_ladder_language.md).

1. Use drag and drop to place instructions on the *Rung* and associate input/output variables to instructions
2. Go to **Project>Build** to generate the output code
3. The output file **plc.ino** will be created in the folder **"current folder"/out/plc**
4. Double click to open Arduino IDE
5. Put the ESP32 board in programming mode
6. Click on *Update* icon on the Arduino IDE to load the program on the ESP32 board
7. After success, reset the ESP32 board to run the program

Click on picture below to see the demo video:

[![](http://img.youtube.com/vi/Q0b-nEzqo88/0.jpg)](http://www.youtube.com/watch?v=Q0b-nEzqo88 "https://www.youtube.com/watch?v=Q0b-nEzqo88")

### Connecting to the MQTT broker

Before building, go to **Project>Properties**<br/>
Set the Wi-fi connection and on the tab MQTT set your Broker connection.

<p>
<img src="https://user-images.githubusercontent.com/5174326/110250317-74016f00-7f59-11eb-83e4-6a014f20aa4f.png" width="600"/>
<p/>

#### Sending messages to device

Only Integer and Floating program variable can be modified by MQTT messages.<br/>
Set the topic to subscribe on the tab MQTT/Topic.

Example of message: sending value 1 to variable MI01:
```
{
  "MI01": 1
}
```
... associate the Integer variable to instruction
<p>
<img src="https://user-images.githubusercontent.com/5174326/110261559-75e62500-7f8f-11eb-85de-57cae13cc227.png" width="400"/>
<p/>

#### Sending messages from device

Set the topic to publish on the tab MQTT/Topic.<br/>
On the tab MQTT/Telemetry set the period and variables to send.

<p>
<img src="https://user-images.githubusercontent.com/5174326/110261862-9367be80-7f90-11eb-8f19-11463b8da99d.png" width="600"/>
<p/>
