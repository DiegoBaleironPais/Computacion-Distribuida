#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include "tabla_de_simbolos.h"
#include "sistema_de_entrada.h"
#include "sintactico.h"
#include "lexico.h"

int main(int argc, char **argv)
{   
    if (argc != 2) {
        printf("Error parámetros incorrectos");
        EXIT_FAILURE;
    }

    // Inicialización de la tabla de simbolos
    inicializar_tabla_simbolos();
    // Incialización del sistema de entrada
    abrir_documento(argv[1]);
    // Ejecución del sintactico
    consumir_componente();
    /*Impresión de la tabla de simbolos
    * una vez recibidos todos los componentes*/ 
    printf("\n\n");
    consultar_tabla();
    //Liberación de memoria en la tabla de simbolos
    borrar_tabla_simbolos();
    //Liberación de memoria en el sistema de entrada
    cerrar_documento();
}