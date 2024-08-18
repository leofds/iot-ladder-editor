### Ladder Language
Ladder is a graphical PLC programming language based on symbols similar to those found in wiring diagrams. Each symbol is a Ladder instruction.

The horizontal lines are called *Rung* and can be understood as an electrical conductor.

Each *Rung* has a state that can be *true* or *false* (logical level high or low). Some instructions may change the rung state to *false*, other instructions use the rung state on program logic. The *Rung* always starts with *true* state.

The Ladder program is executed by the CPU from left to right, top to bottom, one rung at a time until the END instruction.<br/>**In this project the END instructions was omitted!**

Variables may be associated with Ladder instructions, they may have specific functions or be general purpose:
- Variables that start with "I" are mapped to an input interface
- Variables that start with "Q" are mapped to an output interface
- Variables that start with "MI" are general purpose integer variables
- Variables that start with "MF" are general purpose float variables
- Timer instructions have control variables: AC, PRE, DN, EN
- Counter instructions have control variables: AC, PRE, DN, CC

The program execution cycle occurs as follows:
<kbd>
![image](https://user-images.githubusercontent.com/5174326/110213834-86f44080-7e80-11eb-9ae5-090b1acfaf56.png)
</kbd>

This cycle is called *Scan*. The CPU runs the program several times per second and this cycle execution time is called ***Scan Time***.

The bigger the program the bigger the runtime. Ladder instructions have different execution times between them.

Example:

<kbd>![image](https://user-images.githubusercontent.com/5174326/110214009-5f51a800-7e81-11eb-816b-4fa55850c4e3.png)</kbd>

> **I01**: variable mapped to an input interface<br/>
> **Q01**: variable mapped to an output interface<br/>
> **Contact**: read associated variable and modify rung state<br/>
> **Coil**: assigns the logical level of Rung to the associated variable<br/>
> **END**: end of Ladder Program<br/>

In this program, while the NO and NC contact is closed (I01 high and I02 low), the relay remains activated (Q01 high).

### Structural symbols of the Ladder language

| Ladder_Symbol | Description  |
| :---: | :--- |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110214456-52ce4f00-7e83-11eb-8c89-1258d472414f.png)</kbd></p> | **Left Power Rail**<br/>Begin of *rung*. Set the state of the first trailing link to *true*. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110214516-b2c4f580-7e83-11eb-9a23-cd813caef898.png)</kbd></p> | **Right Power Rail**<br/>End of *rung*. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110214604-249d3f00-7e84-11eb-84f3-88c27f214a17.png)</kbd></p> | **Horizontal Link**<br/>Available for Ladder instructions. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110214625-354db500-7e84-11eb-82be-b7e8061f6483.png)</kbd></p> | **Vertical Link**<br/>Connects *rungs* or Ladder instructions in parallel. |

#### Ladder Instruction Symbols

| Ladder_Symbol | Description  |
| :---: | :--- |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110214721-b1e09380-7e84-11eb-906d-9e1fd6409dae.png)</kbd></p> | **Normally Open Contact**<br/>The left link state is copied to the right link if the state of the associated variable is *true*. Otherwise the right link state is *false*. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215144-04bb4a80-7e87-11eb-9f08-1cd3e96756aa.png)</kbd></p> | **Normally Closed Contact**<br/>The left link state is copied to the right link if the state of the associated variable is *false*. Otherwise the right link state is *false*. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215183-303e3500-7e87-11eb-9f56-d3d5cf419a1f.png)</kbd></p> | **Positive Transition-sensing Contact**<br/>The right link state is *true* when a transition of the associated variable from *false* to *true* is detected and the left link state is *true*. The right link state is *false* at all other times. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215273-92973580-7e87-11eb-8cc9-3605b6c7ad86.png)</kbd></p> | **Negative Transition-sensing Contact**<br/>The right link state is *true* when a transition of the associated variable from *true* to *false* is detected and the left link state is *true*. The right link state is *false* at all other times. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215287-ac387d00-7e87-11eb-9ba9-3e6d66e1f714.png)</kbd></p> | **Coil**<br/>The left link state is copied to the associated variable and to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215308-c7a38800-7e87-11eb-94c6-37530fb0982d.png)</kbd></p> | **Negated Coil**<br/>The state of the left link is copied to the right link. The inverse of the left link state is copied to the associated variable, that is, if the left link state is *false* then the associated variable state is *true*, otherwise *false*. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215602-6da3c200-7e89-11eb-9b4c-d736161aa398.png)</kbd></p> | **Set Coil**<br/>The associated variable is set to true when the left link state is true and holds until reset by a **Reset Coil**. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215677-ce32ff00-7e89-11eb-9555-e8684914384d.png)</kbd></p> | **Reset Coil**<br/>The associated variable is set to *false* state when the left link state is *true* and holds until set by a **Set Coil**. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215708-00dcf780-7e8a-11eb-9875-a2e8bf793077.png)</kbd></p> | **Retentive Coil**<br/>Similar to the Coil instruction, but the associated variable should preferably be a retentive memory (global variable). |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215722-10f4d700-7e8a-11eb-8768-d56e6479666d.png)</kbd></p> | **Set Retentive Coil**<br/>Similar to the Set Coil instruction, but the associated variable should preferably be a retentive memory (global variable). |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215738-236f1080-7e8a-11eb-92d0-a040ff83e97c.png)</kbd></p> | **Reset Retentive Coil**<br/>Similar to the Reset Coil instruction, but the associated variable should preferably be a retentive memory (global variable). |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215786-5913f980-7e8a-11eb-8477-021413970352.png)</kbd></p> | **Positive Transition-sensing Coil**<br/>The state of the associated variable is *true* when a transition of the left link from *false* to *true* is detected. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215795-692bd900-7e8a-11eb-987b-0eef83154eeb.png)</kbd></p> | **Negative Transition-sensing Coil**<br/>The state of the associated variable is *true* when a transition of the left link from *true* to *false* is detected. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110215813-852f7a80-7e8a-11eb-9575-3f220b5dafa2.png)</kbd></p> | **Reset**<br/>The associated variable/Timer/Counter is cleared/reset when the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226227-28ed4a80-7ecc-11eb-8bf2-f976f31ef656.png)</kbd></p> | **Timer On**<br/>The timer counter is incremented by time as set in Time Base (miliseconds) until the Preset value is reached. When Preset value is reached the right link state is *true*. The left link state must be *true* for the timer counter to be incremented otherwise the timer is reset and the right link state will be *false*.<br/><br/>Variables of instruction:<br/>AC – ( accumulator ) counter incremented by time;<br/>PRE – ( preset ) maximum value of accumulator;<br/>DN – ( done ) *true* if preset value has been reached;<br/>EN – ( enable ) state of left link.<br/><br/>**Reset** instruction reset the Timer. Preset variable does not change with reset. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226597-8d5dd900-7ecf-11eb-9216-1f42dcac4b61.png)</kbd></p> | **Timer Off**<br/>The timer counter is incremented by time as set in Time Base (miliseconds) until the Preset value is reached. While the left link state is *true* and the preset value is not reached the right link state is *true*. When Preset value is reached the right link state is *false*. The left link state must be *tru*e for the timer counter to be incremented otherwise the timer is reset and the right link state will be *false*.<br/><br/>Variables of instruction:<br/>AC – ( accumulator ) counter incremented by time;<br/>PRE – ( preset ) maximum value of accumulator;<br/>DN – ( done ) *true* if preset value has been reached;<br/>EN – ( enable ) state of left link.<br/><br/>**Reset** instruction reset the Timer. Preset variable does not change with reset. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226635-bb431d80-7ecf-11eb-9146-10e8a43a352b.png)</kbd></p> | **Count Up**<br/>The counter increments with each left link state transition from *false* to *true* until the Preset value is reached. The right link state is *true* when the Preset value is reached and the left link state is *true*.<br/><br/>Variables of instruction:<br/>AC – ( accumulator ) counter incremented;<br/>PRE – ( preset ) maximum value of accumulator;<br/>DN – ( done ) *true* if preset value has been reached;<br/>CC – ( counting ) *true* if the counter has been incremented.<br/><br/>**Reset** instruction reset the Counter. Preset variable does not change with reset.  |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226773-9c915680-7ed0-11eb-95b1-fa327e825888.png)</kbd></p> | **Count Down**<br/>The counter decrements with each left link state transition from *false* to *true* until the Preset value is reached. The right link state is *true* when the Preset value is reached and the left link state is *true*.<br/><br/>Variables of instruction:<br/>AC – ( accumulator ) counter decremented;<br/>PRE – ( preset ) minimum value of accumulator;<br/>DN – ( done ) *true* if preset value has been reached;<br/>CC – ( counting ) *true* if the counter has been decremented.<br/><br/>**Reset** instruction reset the Counter. Preset variable does not change with reset. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226770-98653900-7ed0-11eb-9618-afe6bf02e90e.png)</kbd></p> | **Equal**<br/>The right link state is *true* when the left link state is *true* and the associated variables Src A and Src B are equal. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226762-94391b80-7ed0-11eb-8b03-60ea5a7c17ad.png)</kbd></p> | **Greater Equal**<br/>The right link state is *true* when the left link state is *true* and the associated variable Src A is greater than or equal to the associated variable Src B. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226759-900cfe00-7ed0-11eb-8b3d-860696de6f7a.png)</kbd></p> | **Greater Than**<br/>The right link state is *true* when the left link state is *true* and the associated variable Src A is greater than the associated variable Src B. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226758-8c797700-7ed0-11eb-8153-c2d1ea78bffa.png)</kbd></p> | **Less Equal**<br/>The right link state is *true* when the left link state is *true* and the associated variable Src A is less than or equal to the associated variable Src B. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226756-871c2c80-7ed0-11eb-9d7b-dd1fb871c2c9.png)</kbd></p> | **Less**<br/>The right link state is *true* when the left link state is *true* and the associated variable Src A is less than the associated variable Src B. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226753-8388a580-7ed0-11eb-9129-52fe51ce6218.png)</kbd></p> | **Not Equal**<br/>The right link state is *true* when the left link state is *true* and the associated variable Src A is different than the associated variable Src B. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226749-7f5c8800-7ed0-11eb-9e39-7d3f02fab2d2.png)</kbd></p> | **And**<br/>The associated variable Dest receives the result of the AND logical operation between the associated variables Src A and Src B if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226745-7b306a80-7ed0-11eb-8759-22fb64cbc70d.png)</kbd></p> | **Not**<br/>The associated variable Dest is negated by the associated variable Src A if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226743-78357a00-7ed0-11eb-9cda-844a64c81c26.png)</kbd></p> | **Or**<br/>The associated variable Dest receives the result of the OR logical operation between the associated variables Src A and Src B if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226740-6bb12180-7ed0-11eb-80a7-7db778868fb8.png)</kbd></p> | **Xor**<br/>The associated variable Dest receives the result of the XOR logical operation between the associated variables Src A and Src B if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226833-14f81780-7ed1-11eb-8224-9bff77cf8361.png)</kbd></p> | **Assignment**<br/>Value is assigned to associated variable Dest if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226699-415f6400-7ed0-11eb-90f6-ef1fcfd51831.png)</kbd></p> | **Add**<br/>The associated variable Dest receives the result of the sum arithmetic operation between the associated variables Src A and Src B if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226696-3d334680-7ed0-11eb-9afe-7cd2c00db1d3.png)</kbd></p> | **Div**<br/>The associated variable Dest receives the result of the division arithmetic operation between the associated variables Src A and Src B (A / B) if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226693-399fbf80-7ed0-11eb-9f49-b0d5a6512211.png)</kbd></p> | **Mul**<br/>The associated variable Dest receives the result of the arithmetic multiplication operation between the associated variables Src A and Src B if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226690-3573a200-7ed0-11eb-8381-bbb18db03cd5.png)</kbd></p> | **Sub**<br/>The associated variable Dest receives the result of the subtraction arithmetic operation between the associated variables Src A and Src B (A - B) if the left link state is *true*. The left link state is always copied to the right link. |
| <p align="center"><kbd>![image](https://user-images.githubusercontent.com/5174326/110226684-2f7dc100-7ed0-11eb-9bbd-88f3f10ed27d.png)</kbd></p> | **System Scan Time**<br/>The **Scan Time** instruction prints the scan time every second. The left link state is always copied to the right link. |





