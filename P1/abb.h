#ifndef ABB_H
#define ABB_H
// Definición del tipo de dato
typedef void *arbolBinario;
// Función para crear el árbol
void crear(arbolBinario *A);
// Función para liberar la memoria del árbol
void destruir(arbolBinario *A);
/*Función que devuelve una referencia
 * al hijo izquierdo de A*/
arbolBinario izq(arbolBinario A);
/*Función que devuelve una referencia
 * al hijo derecho de A*/
arbolBinario der(arbolBinario A);
/*Función para comprobar si un
 * lexema está guardado en la tabla de
 * simbolos*/
int es_miembro(arbolBinario A, char *lexema);
/*Función para insertar un componente léxico
 * en el árbol, MUY IMPORTANTE ejecutar siempre
 * es_miembro antes para evitar tener nodos
 * duplicados*/
void insertar(arbolBinario *A, char *lexema, short componente_lexico);
/*Función que recorre el arbol en
 * preorden y imprime el contenido de
 * los nodos*/
void recorrido_preorder(arbolBinario raiz);
#endif // ABB_H