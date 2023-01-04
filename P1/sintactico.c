
#include <stdio.h>
#include <stdlib.h>
#include "lexico.h"

//Función que consume todos los componentes hasta recibir el EOF
void consumir_componente(){
    struct componente_lexico *componente_lexico;
    for(;;) {
        componente_lexico = siguiente_componente_lexico();
        //Los carácteres que no pertenecen a ningún lexema se reciben como NULL
        if (componente_lexico != NULL) {
            //El EOF se recibe con el componente lexico -1
            if (componente_lexico->componente_lexico == -1) {
                //Liberación de memoria
                free(componente_lexico);
                //Fin del bucle
                break;
            }
            //Los componentes léxicos que no sean EOF se imprimen
            printf("Lexema: %s Componente Lexico: %d\n",componente_lexico->lexema, componente_lexico->componente_lexico);
            //Liberación de memoria
            free(componente_lexico->lexema);
            free(componente_lexico);
        } 
    }
}