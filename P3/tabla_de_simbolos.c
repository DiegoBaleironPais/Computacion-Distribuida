#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "abb.h"
#include "definiciones.h"
#include "funciones.h"

/*Tamaño de linea máximo que puede tener una linea en el fichero
 * definiciones.h*/
#define TAMANHOLINEA 24
// Número de palabras clave en el fichero de definiciones
#define NUMEROCONSTANTES 2

// Creación de la estructura de datos que contendrá la memoria de la calculadora
arbolBinario tabla_simbolos;

// Función que inicializa la tabla de simbolos y carga en ella las constantes de definiciones.h
void inicializar_tabla_simbolos()
{
    char *buffer = (char *)malloc(TAMANHOLINEA * sizeof(char)); // Buffer donde se almacenan las lineas de definiciones.h
    size_t buffer_size = TAMANHOLINEA;                          // Tamanho del buffer donde se guardan las lineas de definiciones.h
    FILE *constantes;                                           // Puntero que referenciará a definiciones.h
    float valor_constante;                                      // Variable que recoge los valores de las constantes
    crear(&tabla_simbolos);                                     // Inicialización de la memoria de la calculadora
    char *nombre_constante;                                     // Puntero en el que se guardan los nombres de las constantes

    // Apertura del fichero
    constantes = fopen("definiciones.h", "r");
    if (constantes == NULL)
    {
        printf("Error al intentar abrir el fichero definiciones.h\n");
        exit(-1);
    }

    // Lectura de las constantes
    for (int i = 0; i < NUMEROCONSTANTES; i++)
    {
        // Obtención de las palabras constantes en definiciones.h
        getline(&buffer, &buffer_size, constantes);
        // Obtención del nombre y el valor de las constantes
        strtok(buffer, " ");
        strtok(NULL, " ");
        valor_constante = atof(strtok(NULL, " "));
        nombre_constante = strtok(NULL, "// \n");
        // Chequeo de que no haya nada con el nombre de la constante en memoria
        if (es_miembro(tabla_simbolos, nombre_constante) == -1)
        {
            insertar_variables_y_constantes(&tabla_simbolos, nombre_constante, CONSTANTE, valor_constante);
        }
        else
        {
            printf("Error en el fichero de definiciones, palabra reservada repetida.\n");
            exit(-1);
        }
    }

    //Carga de las funciones en memoria
    for (int i = 0; i < sizeof(func) / sizeof(funciones); i++)
    {
        crear_funcion(&tabla_simbolos, func[i].nombre_funcion, func[i].puntero_funcion);
    }

    // Liberación de memoria
    fclose(constantes);
    free(buffer);
}

//Limpieza de la memoria
void clean()
{
    destruir(&tabla_simbolos);
    inicializar_tabla_simbolos();
}

//Obtención del tipo de la estructura de datos con el nombre que se pasa como parámetro
int tipo_identificador(char *nombre)
{
    return es_miembro(tabla_simbolos, nombre);
}

//Obtención del valor de una constante o variable
double obtener_contenido_variable(char *nombre)
{
    return obtener_valor(tabla_simbolos, nombre);
}

//Modificación del valor de una variable
void modificar_variable(char *nombre, float valor)
{
    insertar_variables_y_constantes(&tabla_simbolos, nombre, VARIABLE, valor);
}

//Invocación de la función de 1 parámetro cuyo nombre se pasa como parámetro
double usar_funcion_1_parametro(char *nombre, double parametro)
{
    return usar_funcion_1(tabla_simbolos, nombre, parametro);
}

//Invocación de la función de 2 parámetros cuyo nombre se pasa como parámetro
double usar_funcion_2_parametros(char *nombre, double parametro_1, double parametro_2)
{
    return usar_funcion_2(tabla_simbolos, nombre, parametro_1, parametro_2);
}

// Función que imprime las variables de la memoria de la calculadora
void consultar_tabla()
{
    recorrido_preorder(tabla_simbolos);
}

// Funcion que libera la memoria de la calculadora
void borrar_tabla_simbolos()
{
    destruir(&tabla_simbolos);
}
