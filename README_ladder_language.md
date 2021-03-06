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
