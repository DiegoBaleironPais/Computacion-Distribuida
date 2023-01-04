#include <stdio.h>
#include <string.h>
#include "tabla_de_simbolos.h"
#include "sintactico.tab.h"

int main(int argc, char *argv[])
{
    printf("\n------------------------------------------------Calculadora en bison------------------------------------------------\n\n");
    inicializar_tabla_simbolos();
    yyparse();
    borrar_tabla_simbolos();
    return 0;
}