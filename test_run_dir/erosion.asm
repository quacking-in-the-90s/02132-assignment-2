LI R1, 0 ;Black value
LI R2, 1 ; constant to increase counters
LI R3, 20;Image width

LI R4, 0 ; y = 0
LI R5, 0 ; x = 0

MULT R6, R4, R3 ;Calculate input address
ADD R6, R6, R5

JEQ 32, R4, R1 ;Upper border check

JEQ 32, R5, R1 ;Left border check

ADD R7, R4, R2
JEQ 32, R7, R3 ;Lower border check

ADD R7, R5, R2
JEQ 32, R7, R3 ;Right border check

LD R7, R6
JEQ 32, R7, R1 ;Input address check

SUB R7, R6, R3
LD R7, R7
JEQ 32, R7, R1 ;Upper neighbour check

ADD R7, R6, R3
LD R7, R7
JEQ 32, R7, R1 ;Lower neighbour check

SUB R7, R6, R2
LD R7, R7
JEQ 32, R7, R1 ;Left neighbour check

ADD R7, R6, R2
LD R7, R7
JEQ 32, R7, R1 ;Right neighbour check

LI R7, 400 ;output start address
ADD R7, R7, R6 ;Calculate output address
LI R6, 255 ;White value
SD R6, R7 ;Store white value in output address
JR 35


LI R7, 400
ADD R7, R7, R6
SD R1, R7 ;Store black value in output address

ADD R5, R5, R2 ;Calculate next x-coordinate
JLT 5, R5, R3; Jump if not at the last pixel in the row

LI R5, 0 ;Reset the x-value
ADD R4, R4, R2 ;Calculate next y-coordinate
JLT 4, R4, R3 ;Jump if not the last pixel in the column

END

