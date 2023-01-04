#include<stdio.h>
#include<stdlib.h>

//Función de notificación de errores que imprime el mensaje que ese le pasa como paŕametro.
void notificar_error(char* mensaje_error) {
    printf("Error: %s\n",mensaje_error);
};