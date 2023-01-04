#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "abb.h"
#include "definiciones.h"

/*Tamaño de linea máximo que puede tener una linea en el fichero
 * definiciones.h*/
#define TAMANHOLINEA 50
//Número de palabras clave en el fichero de definiciones
#define NUMEROKEYWORDS 25

// Creación de la estructura de datos que contendrá la tabla de simbolos
arbolBinario tabla_simbolos;

/*Función que inicializa la tabla de simbolos y carga en ella los
 * componentes léixcos del fichero definiciones.h */
void inicializar_tabla_simbolos()
{
    char *buffer = (char *)malloc(TAMANHOLINEA * sizeof(char)); // Buffer donde se almacenan las lineas de definiciones.h
    size_t buffer_size = TAMANHOLINEA;                          // Tamanho del buffer donde se guardan las lineas de definiciones.h
    FILE *keywords;                                             // Puntero que referenciará a definiciones.h
    int componente_lexico;                                      // Variable que recogerá los componentes léxicos guardados en definiciones.h                                         // Variable que recogerá los lexemas guardados en definiciones.h
    crear(&tabla_simbolos);                                     // Inicialización de la tabla de simbolos
    char *lexema;                                               // Puntero en el que se guardará el lexema

    // Apertura del fichero
    keywords = fopen("definiciones.h", "r");
    if (keywords == NULL)
    {
        printf("Error al intentar abrir el fichero definiciones.h\n");
        exit(-1);
    }

    //Lectura de todas las keywords
    for (int i = 0; i < NUMEROKEYWORDS; i++)
    {
        // Obtención de las palabras reservadas en definiciones.h
        getline(&buffer, &buffer_size, keywords);
        // Obtención del lexema y el componente lexico usando delimitadores
        strtok(buffer, " ");
        strtok(NULL, " ");
        componente_lexico = atoi(strtok(NULL, " "));
        lexema = strtok(NULL, "// \n");
        // Chequeo de que el lexema no está en la tabla de simbolos
        if (es_miembro(tabla_simbolos, lexema) == -1)
        {
            insertar(&tabla_simbolos, lexema, componente_lexico);
        }
        else
        {
            printf("Error en el fichero de definiciones, palabra reservada repetida.\n");
            exit(-1);
        }
    }

    // Liberación de memoria
    fclose(keywords);
    free(buffer);
}

// Función que devuelve el componente léxico al que pertenece 'lexema'
int obtener_componente(char *lexema)
{
    int componente_lexico = es_miembro(tabla_simbolos, lexema);
    /*Si el lexema no es miembro la única opción que hay es que este
     * sea un identificador */
    if (componente_lexico == -1)
    {
        insertar(&tabla_simbolos, lexema, ID);
        return ID;
    }
    return componente_lexico;
}

// Función que imprime el contenido de la tabla de simbolos
void consultar_tabla()
{
    recorrido_preorder(tabla_simbolos);
}

// Funcion que libera la memoria de la tabla de simbólos
void borrar_tabla_simbolos()
{
    destruir(&tabla_simbolos);
}