#ifndef ABB_H
#define ABB_H
// Definición del tipo de dato
typedef void *arbolBinario;
//-------------------Funciones de gestión-------------------//
// Función para crear el árbol
void crear(arbolBinario *A);
// Función para liberar la memoria del árbol
void destruir(arbolBinario *A);
// Función para comprobar si hay alguna estructura guardada en la tabla que coincida con el nombre que se pasa como parámetro, si la hay devuelve el tipo de dato de la estructura
int es_miembro(arbolBinario A, char *lexema);
//-------------------Funciones para variables y constantes-------------------//
// Función para insertar variables y constantes y modificar variables
void insertar_variables_y_constantes(arbolBinario *A, char *nombre, short tipo_variable, float valor_variable);
// Obtención del valor de variables y constantes
float obtener_valor(arbolBinario A, char *nombre);
//-------------------Funciones para funciones-------------------//
// Función para insertar funciones en la tabla
void crear_funcion(arbolBinario *A, char *nombre, double (*puntero_funcion)());
// Función para usar funciones con un parámetro
double usar_funcion_1(arbolBinario A, char *nombre, double parametro);
// Función para usar funciones con dos parámetros
double usar_funcion_2(arbolBinario A, char *nombre, double parametro_1, double parametro_2);
// Función que recorre el arbol en preorden y imprime todas las variables que contiene
void recorrido_preorder(arbolBinario raiz);
#endif // ABB_H