### Ladder Language
Ladder is a graphical PLC programming language based on symbols similar to those found in wiring diagrams. Each symbol is a Ladder instruction.

The horizontal lines are called *Rung* and can be understood as an electrical conductor.

Each *Rung* has a state that can be *true* or *false* (logical level high or low). Some instructions may change the rung state to *false*, other instructions use the rung state on program logic. The *Rung* always starts with *true* state.

The Ladder program is executed by the CPU from left to right, top to bottom, one rung at a time until the END instruction.<br/>**In this project the END instructions was omitted!**

Variables may be associated with Ladder instructions, they may have specific functions or be general purpose:
- Variables that start with "I" are mapped to an input interface
- Variables that start with "I" are mapped to an input interface
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

### Structural symbols of Ladder language


<kbd>![image](https://user-images.githubusercontent.com/5174326/110214456-52ce4f00-7e83-11eb-8c89-1258d472414f.png)</kbd>
**Left Power Rail:** Begin of *rung*. Set the state of the first trailing link to *true*.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110214516-b2c4f580-7e83-11eb-9a23-cd813caef898.png)</kbd>
**Right Power Rail:** End of *rung*.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110214604-249d3f00-7e84-11eb-84f3-88c27f214a17.png)</kbd>
**Horizontal Link:** Available for Ladder instructions.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110214625-354db500-7e84-11eb-82be-b7e8061f6483.png)</kbd>
**Vertical Link:** Connects *rungs* or Ladder instructions in parallel.
<kbd></kbd>

#### Ladder Instruction Symbols

<kbd>![image](https://user-images.githubusercontent.com/5174326/110214721-b1e09380-7e84-11eb-906d-9e1fd6409dae.png)</kbd>
**Normally Open Contact:** The left link state is copied to the right link if the state of the associated variable is *true*. Otherwise the right link state is *false*.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215144-04bb4a80-7e87-11eb-9f08-1cd3e96756aa.png)</kbd>
**Normally Closed Contact:** The left link state is copied to the right link if the state of the associated variable is *false*. Otherwise the right link state is *false*.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215183-303e3500-7e87-11eb-9f56-d3d5cf419a1f.png)</kbd>
**Positive Transition-sensing Contact:** The right link state is *true* when a transition of the associated variable from *false* to *true* is detected and the left link state is *true*. The right link state is *false* at all other times.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215273-92973580-7e87-11eb-8cc9-3605b6c7ad86.png)</kbd>
**Negative Transition-sensing Contact:** The right link state is *true* when a transition of the associated variable from *true* to *false* is detected and the left link state is *true*. The right link state is *false* at all other times.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215287-ac387d00-7e87-11eb-9ba9-3e6d66e1f714.png)</kbd>
**Coil:** The left link state is copied to the associated variable and to the right link.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215308-c7a38800-7e87-11eb-94c6-37530fb0982d.png)</kbd>
**Negated Coil:** The state of the left link is copied to the right link. The inverse of the left link state is copied to the associated variable, that is, if the left link state is *false* then the associated variable state is *true*, otherwise *false*.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215602-6da3c200-7e89-11eb-9b4c-d736161aa398.png)</kbd>
**Set Coil:** The associated variable is set to true when the left link state is true and holds until reset by a **Reset Coil**.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215677-ce32ff00-7e89-11eb-9555-e8684914384d.png)</kbd>
**Reset Coil:** The associated variable is set to *false* state when the left link state is *true* and holds until set by a **Set Coil**.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215708-00dcf780-7e8a-11eb-9875-a2e8bf793077.png) </kbd>
**Retentive Coil:** Similar to the Coil instruction, but the associated variable should preferably be a retentive memory (global variable).

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215722-10f4d700-7e8a-11eb-8768-d56e6479666d.png)</kbd>
**Set Retentive Coil:** Similar to the Set Coil instruction, but the associated variable should preferably be a retentive memory (global variable).

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215738-236f1080-7e8a-11eb-92d0-a040ff83e97c.png)</kbd>
**Reset Retentive Coil:** Similar to the Reset Coil instruction, but the associated variable should preferably be a retentive memory (global variable).

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215786-5913f980-7e8a-11eb-8477-021413970352.png)</kbd>
**Positive Transition-sensing Coil:** The state of the associated variable is *true* when a transition of the left link from *false* to *true* is detected. The left link state is always copied to the right link.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215795-692bd900-7e8a-11eb-987b-0eef83154eeb.png)</kbd>
**Negative Transition-sensing Coil:** The state of the associated variable is *true* when a transition of the left link from *true* to *false* is detected. The left link state is always copied to the right link.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215804-7779f500-7e8a-11eb-995b-5e9f967dad3c.png)</kbd>
**End:** End of the Ladder program.

<kbd>![image](https://user-images.githubusercontent.com/5174326/110215813-852f7a80-7e8a-11eb-9575-3f220b5dafa2.png)</kbd>
**Reset:** The associated variable/Timer/Counter is cleared/reset when the left link state is *true*. The left link state is always copied to the right link.

<p float="left">
  <img src="https://user-images.githubusercontent.com/5174326/110215909-f707c400-7e8a-11eb-9997-55ab8cf3c370.png" />
  teste
</p>
<kbd>![image](https://user-images.githubusercontent.com/5174326/110215909-f707c400-7e8a-11eb-9997-55ab8cf3c370.png)</kbd>
**Timer On:** The timer counter is incremented by time as set in Time Base (miliseconds) until the Preset value is reached. When Preset value is reached the right link state is true. The left link state must be true for the timer counter to be incremented otherwise the timer is reset and the right link state will be false.

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**

<kbd></kbd>
**:**





