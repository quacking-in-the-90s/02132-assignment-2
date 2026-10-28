LI R1, 0 ;Sort pixel / zero
LI R2, 1 ; constant to increase counters
LI R3, 20 ;Billede bredde


LI R4, 0 ; x = 0

X_LOOP:
    LI R5, 0 ; y = 0


Y_LOOP:
    ;Beregn adressen for input
    MULT R6, R4, R3
    ADD R6, R6, R5

    JEQ WRITE_ZERO, R4, R1 ;Hvis vi er ved øvre kanten

    JEQ WRITE_ZERO, R5, R1 ;Hvis vi er ved venstre kanten

    ADD R7, R4, R2
    JEQ WRITE_ZERO, R7, R3 ;Hvis vi ved nedre kant

    ADD R7, R5, R2
    JEQ WRITE_ZERO, R7, R3 ;Hvis vi er ved højre kant

    LD R7, R6
    JEQ WRITE_ZERO, R7, R1 ;Hvis vores pixel er sort hop ud

    SUB R7, R6, R3 ;Tjek over
    LD R7, R7
    JEQ WRITE_ZERO, R7, R1 ;Hvis vi trækker 20 fra er vi koordinaten over den

    ADD R7, R6, R3 ;Tjek Under
    LD R7, R7
    JEQ WRITE_ZERO, R7, R1 ;Hvis vi lægger 20 til er vi koordinaten under den

    SUB R7, R6, R2 ; Tjek til venstre
    LD R7, R7
    JEQ WRITE_ZERO, R7, R1 ;Hvis vi trækker 1 fra er vi koordinaten før

    ADD R7, R6, R2 ;Tjek Højre
    LD R7, R7
    JEQ WRITE_ZERO, R7, R1 ;Hvis vi lægger 21 til er vi koordinaten efter

    LI R7, 400 ;output start addresse
    ADD R7, R7, R6 ;beregn hvor vores input skal outputtes
    LI R6, 255 ;Gør vores output hvid
    SD R6, R7 ; Tag vores output farve og opbevar den i output adressen
    JR NEXT_PIXEL

WRITE_ZERO:
    LI R7, 400
    ADD R7, R7, R6
    SD R1, R7 ;Gør pixelen sort

NEXT_PIXEL
    ADD R5, R5, R2 ;Hop til næste y coordinat
    JLT Y_LOOP, R5, R3; Loop over hvis vi ikke skal skifte X værdi


    LI R5, 0 ;Genstart y loop
    ADD R4, R4, R2 ;gå til næste x række
    JLT X_LOOP, R4, R3; Tjek om vi har kigget hele billede igennem

    END

