# IoT Ladder Editor

<p align="center"><kbd>
  <img src="https://user-images.githubusercontent.com/5174326/110049165-10e0c400-7d30-11eb-9122-5d47fe090d74.png" width="800">
</kbd></p>

This project is a open source code Ladder Editor that generates code for IoT devices. Through the MQTT protocol it is possible to interact with the program variables.
The current version generates Arduino code (.ino) for ESP32 with FreeRTOS.

It's a desktop Java application.
## How to execute
1. Install Java (8+) if it hasn't already been installed.
2. Install Arduino IDE (2.3+), read the setup guide ([arduino_ide_setup.md](https://github.com/leofds/iot-ladder-editor/blob/main/docs/arduino_ide_setup.md)).
3. Download the latest released .jar file ([Releases](https://github.com/leofds/iot-ladder-editor/releases)).
4. Run the downloaded file with doule-click if supported, or run the command:<br>`java -jar iot_ladder_editor-0.0.2.jar`

## How to execute from the source code
1. Install Java OpenJDK 11+ ([jdk.java.net/archive](https://jdk.java.net/archive/)).
2. Install VS Code ([https://code.visualstudio.com/download](https://code.visualstudio.com/download)).
3. Install VS Code Extensions: `Extension Pack for Java`, and `Maven For Java`. (Publisher: Microsoft).
4. Download this repository and extract the .ZIP or clone this repository with `git clone https://github.com/leofds/iot-ladder-editor`.
5. Open the project directory `iot-ladder-editor` on the VS Code. The project will be automatically builded by Maven, creating the directory `target`.
6. Open the file `iot-ladder-editor\src\main\java\com\github\leofds\iotladdereditor\IotLadderEditor.java`.
7. Run the project clicking on `Run/Run Without Debugging (Ctrl+F5)`. Select `Java` if requested.

**debug**

To run in debug mode add breakpoints to the source code and click on `Run/Start Debugging (F5)`

**generating .jar file**

On the `EXPLORER` tab, `JAVA PROJECTS` navegation bar, click on `Export Jar`. 

## How to use the software
To understand about the Ladder Language read [ladder_language.md](https://github.com/leofds/iot-ladder-editor/blob/main/docs/ladder_language.md).

1. Use drag and drop to place instructions on the *Rung* and associate input/output variables to instructions.
2. Go to `Project/Build` to generate the output code.
3. The output file **plc.ino** will be created in the folder `<current_folder>/out/plc`.
4. Double click to open Arduino IDE.
5. Put the ESP32 board in programming mode. (It's no longer necessary)
6. Click on *Update* icon on the Arduino IDE to load the program on the ESP32 board.
7. After success
8. Reset the ESP32 board to run the program. (It's no longer necessary)

Click on picture below to see the demo video:

[![](http://img.youtube.com/vi/Q0b-nEzqo88/0.jpg)](http://www.youtube.com/watch?v=Q0b-nEzqo88 "https://www.youtube.com/watch?v=Q0b-nEzqo88")

### Test Circuit

The table below shows recommended/equivalent Ladder Instruction for the input/output schematic.

| | Input<br/>(pull-down resistor) | Input<br/>(pull-up resistor) | Output<br/>(active high) | Output<br/>(active low) |
| :---: | :---: | :---: | :---: | :---: |
| Schematic | <p><img src="https://user-images.githubusercontent.com/5174326/110408536-7ac8d880-8064-11eb-9a7b-1365be4c1d9a.png" width="150"><p/> | <p><img src="https://user-images.githubusercontent.com/5174326/110410232-515d7c00-8067-11eb-8741-11eb1f935d87.png" width="120"><p/> | <p><img src="https://user-images.githubusercontent.com/5174326/110408431-47864980-8064-11eb-826a-6c98c532bb4b.png" width="110"><p/> | <p><img src="https://user-images.githubusercontent.com/5174326/110410853-553dce00-8068-11eb-8245-a96a0892e39f.png" width="110"><p/> |
| Ladder<br/>Instruction | <p><kbd><img src="https://user-images.githubusercontent.com/5174326/110409038-5d483e80-8065-11eb-870a-9a8611d25a34.png" width="60"></kbd><p/> | <p><kbd><img src="https://user-images.githubusercontent.com/5174326/110410526-c6c94c80-8067-11eb-9c3a-7c07ffb61390.png" width="60"></kbd><p/> | <p><kbd><img src="https://user-images.githubusercontent.com/5174326/110409070-6fc27800-8065-11eb-8a63-88cdab18d138.png" width="60"></kbd><p/> | <p><kbd><img src="https://user-images.githubusercontent.com/5174326/110411015-92a25b80-8068-11eb-8227-7c44e9c5bdc5.png" width="60"></kbd><p/> |

### Pin Mapping

You can change the pin mapping on the `Project/Properties`, on the tab "Pin Mapping".

![pinout](https://github.com/user-attachments/assets/00a8f2cd-9ea7-4d70-a59e-e56cf2c19585)

### Using Mosquitto MQTT Broker

To install and configure the Mosquitto MQTT Broker read [using_mosquitto_mqt_broker.md](https://github.com/leofds/iot-ladder-editor/blob/main/docs/using_mosquitto_mqt_broker.md).

### Connecting to the MQTT Broker

Before building, go to `Project/Properties`<br/>
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
