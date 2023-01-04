#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "definiciones.h"

/*Unión para evitar desperdiciar memoria, variable double para
 * almacenar el valor de las variables y las constantes y punteros a
 * función double para poder apuntar a las funciones que utiliza la calculadora
 */
union contenido_identificador
{
    double valor_variable;
    double (*puntero_funcion)();
};

// Struct nodo
struct nodo
{
    char *nombre;                            // Nombre de la función, variable o constante
    short tipo_variable;                     // Tipo: FUNCION, VARIABLE o CONSTANTE
    union contenido_identificador contenido; // Valor númerico o puntero a función
    struct nodo *hijo_izquierdo;
    struct nodo *hijo_derecho;
};

// Definición del tipo de dato
typedef struct nodo *arbolBinario;

//--------------------------------------------------------------Funciones de gestión--------------------------------------------------------------//


// Función para crear el árbol
void crear(arbolBinario *A)
{
    *A = NULL;
}

// Función para liberar la memoria del árbol
void destruir(arbolBinario *A)
{
    if (*A != NULL)
    {
        destruir(&(*A)->hijo_izquierdo);
        destruir(&(*A)->hijo_derecho);
        free((*A)->nombre);
        free(*A);
        *A = NULL;
    }
}

/*Función privada que devuelve 0 o
 * 1 dependiendo de si el árbol esta
 * vacio o no*/
unsigned es_vacio(arbolBinario A)
{
    return A == NULL;
}

/*Función privada que devuelve una referencia
 * al hijo izquierdo de A*/
arbolBinario izq(arbolBinario A)
{
    return A->hijo_izquierdo;
}

/*Función privada que devuelve una referencia
 * al hijo derecho de A*/
arbolBinario der(arbolBinario A)
{
    return A->hijo_derecho;
}

/* Función para comprobar si hay alguna estructura guardada
 * en la tabla que coincida con el nombre que se pasa como
 * parámetro, si la hay devuelve el tipo de dato de la estructura
 */
int es_miembro(arbolBinario A, char *nombre)
{
    /*Si se llega a un nodo vacio no hay ningun tipo de dato
     * con el nombre proporcionado*/
    if (es_vacio(A))
    {
        return -1;
    }

    int comp = strcmp(A->nombre, nombre);

    if (comp == 0)
    {
        return A->tipo_variable;
    }
    if (comp > 0)
    {
        return es_miembro(der(A), nombre);
    }

    return es_miembro(izq(A), nombre);
}

//--------------------------------------------------------------Funciones para variables y constantes--------------------------------------------------------------//

// Función para insertar variables y constantes y modificar variables
void insertar_variables_y_constantes(arbolBinario *A, char *nombre, short tipo_variable, float valor_variable)
{
    // Si el nodo está vacio se crea la VARIABLE o CONSTANTE
    if (es_vacio(*A))
    {
        *A = (arbolBinario)malloc(sizeof(struct nodo));
        (*A)->nombre = (char *)malloc((sizeof(nombre)) * sizeof(char));
        strcpy((*A)->nombre, nombre);
        (*A)->tipo_variable = tipo_variable;
        (*A)->contenido.valor_variable = valor_variable;
        (*A)->hijo_izquierdo = NULL;
        (*A)->hijo_derecho = NULL;
        return;
    }

    int comp = strcmp((*A)->nombre, nombre);

    if (comp > 0)
    {
        insertar_variables_y_constantes(&(*A)->hijo_derecho, nombre, tipo_variable, valor_variable);
    }
    if (comp < 0)
    {
        insertar_variables_y_constantes(&(*A)->hijo_izquierdo, nombre, tipo_variable, valor_variable);
    }
    if (comp == 0)
    {
        (*A)->contenido.valor_variable = valor_variable;
    }
}

// Obtención del valor de variables y constantes
float obtener_valor(arbolBinario A, char *nombre)
{
    if (es_vacio(A))
    {
        return -1;
    }
    int comp = strcmp(A->nombre, nombre);

    if (comp == 0)
    {
        return A->contenido.valor_variable;
    }
    if (comp > 0)
    {
        return obtener_valor(der(A), nombre);
    }

    return obtener_valor(izq(A), nombre);
}

//--------------------------------------------------------------Funciones para variables y constantes--------------------------------------------------------------//
// Función para insertar funciones en la tabla
void crear_funcion(arbolBinario *A, char *nombre, double (*puntero_funcion)())
{
    if (es_vacio(*A))
    {
        *A = (arbolBinario)malloc(sizeof(struct nodo));
        (*A)->nombre = (char *)malloc((sizeof(nombre)) * sizeof(char));
        strcpy((*A)->nombre, nombre);
        (*A)->tipo_variable = FUNCION;
        (*A)->contenido.puntero_funcion = puntero_funcion;
        (*A)->hijo_izquierdo = NULL;
        (*A)->hijo_derecho = NULL;
        return;
    }

    int comp = strcmp((*A)->nombre, nombre);
    if (comp > 0)
    {
        crear_funcion(&(*A)->hijo_derecho, nombre, puntero_funcion);
    }
    if (comp < 0)
    {
        crear_funcion(&(*A)->hijo_izquierdo, nombre, puntero_funcion);
    }
    if (comp == 0)
    {
        if ((*A)->tipo_variable == CONSTANTE)
        {
            printf("No se puede añadir la función ya hay una constante con el mismo nombre\n");
            return;
        }
        if ((*A)->tipo_variable == VARIABLE)
        {
            printf("No se puede añadir la función ya hay una variable con el mismo nombre\n");
            return;
        }
        if ((*A)->tipo_variable == FUNCION)
        {
            printf("No se puede añadir la función ya hay una funcion con el mismo nombre\n");
            return;
        }
    }
}

// Función para usar funciones con un parámetro
double usar_funcion_1(arbolBinario A, char *nombre, double parametro)
{
    if (es_vacio(A))
    {
        printf("funcion %s no encontrada\n", nombre);
        return -1;
    }
    int comp = strcmp(A->nombre, nombre);

    if (comp == 0)
    {
        if (A->tipo_variable == FUNCION)
        {
            return A->contenido.puntero_funcion(parametro);
        }
        else
        {
            printf("%s no encontrada\n", nombre);
            return -1;
        }
    }
    if (comp > 0)
    {
        return usar_funcion_1(der(A), nombre, parametro);
    }
    return usar_funcion_1(izq(A), nombre, parametro);
}

// Función para usar funciones con dos parámetros
double usar_funcion_2(arbolBinario A, char *nombre, double parametro_1, double parametro_2)
{
    if (es_vacio(A))
    {
        printf("funcion %s no encontrada\n", nombre);
        return -1;
    }
    int comp = strcmp(A->nombre, nombre);

    if (comp == 0)
    {
        if (A->tipo_variable == FUNCION)
        {
            return A->contenido.puntero_funcion(parametro_1, parametro_2);
        }
        else
        {
            printf("%s no encontrada\n", nombre);
            return -1;
        }
    }
    if (comp > 0)
    {
        return usar_funcion_2(der(A), nombre, parametro_1, parametro_2);
    }
    return usar_funcion_2(izq(A), nombre, parametro_1, parametro_2);
}
/*Función que recorre el arbol en
 * preorden y imprime todas las
 * variables que contiene*/
void recorrido_preorder(arbolBinario raiz)
{
    if (raiz != NULL && raiz->nombre != NULL)
    {
        recorrido_preorder(raiz->hijo_izquierdo);
        if (raiz->tipo_variable == VARIABLE)
        {
            printf("Variable: %s valor: %lf\n", raiz->nombre, raiz->contenido.valor_variable);
        }
        /*
        if (raiz->tipo_variable == CONSTANTE)
        {
            printf("Constante: %s valor: %lf\n", raiz->nombre, raiz->contenido.valor_variable);
        }
        if (raiz->tipo_variable == FUNCION)
        {
            printf("Función: %s\n", raiz->nombre);
        }
        */
        recorrido_preorder(raiz->hijo_derecho);
    }
}