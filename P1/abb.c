#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Struct nodo
struct nodo
{
    char *lexema;
    short componente_lexico;
    struct nodo *hijo_izquierdo;
    struct nodo *hijo_derecho;
};

// Definición del tipo de dato
typedef struct nodo *arbolBinario;

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
        free((*A)->lexema);
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

/*Función que devuelve una referencia
 * al hijo izquierdo de A*/
arbolBinario izq(arbolBinario A)
{
    return A->hijo_izquierdo;
}

/*Función que devuelve una referencia
 * al hijo derecho de A*/
arbolBinario der(arbolBinario A)
{
    return A->hijo_derecho;
}

/*Función para comprobar
 * lexema está guardado en la tabla de
 * simbolos, si no lo está devuelve -1
 * si lo está devuelve su componente léxico*/
int es_miembro(arbolBinario A, char *lexema)
{
    if (es_vacio(A))
    {
        return -1;
    }
    int comp = strcmp(A->lexema, lexema);

    if (comp == 0)
    {
        return A->componente_lexico;
    }
    if (comp > 0)
    {
        return es_miembro(der(A), lexema);
    }

    return es_miembro(izq(A), lexema);
}

/*Función para insertar un componente léxico
 * en el árbol, MUY IMPORTANTE ejecutar siempre
 * es_miembro antes para evitar tener nodos
 * duplicados*/
void insertar(arbolBinario *A, char *lexema, short componente_lexico)
{
    if (es_vacio(*A))
    {
        *A = (arbolBinario)malloc(sizeof(struct nodo));
        /*Aqui el tamaño de lexema debería ser el de la variable lexema
        * pero si no pongo el +8 valgrind se queja y tira errores muy chungos
        * no tengo ni idea de porque*/
        (*A)->lexema = (char*) malloc((sizeof(lexema)+8)*sizeof(char));
        strcpy((*A)->lexema,lexema);
        (*A)->componente_lexico = componente_lexico;
        (*A)->hijo_izquierdo = NULL;
        (*A)->hijo_derecho = NULL;
        return;
    }

    int comp = strcmp((*A)->lexema, lexema);
    if (comp > 0)
    {
        insertar(&(*A)->hijo_derecho, lexema, componente_lexico);
    }
    else
    {
        insertar(&(*A)->hijo_izquierdo, lexema, componente_lexico);
    }
}

/*Función que recorre el arbol en
 * preorden y imprime el contenido de
 * los nodos*/
void recorrido_preorder(arbolBinario raiz)
{
    if (raiz != NULL && raiz->lexema != NULL)
    {
        recorrido_preorder(raiz->hijo_izquierdo);
        printf("Componente Léxico: %d Lexema: %s\n", raiz->componente_lexico, raiz->lexema);
        recorrido_preorder(raiz->hijo_derecho);
    }
}