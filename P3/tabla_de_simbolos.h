// Función que inicializa la tabla de simbolos y carga en ella las constantes de definiciones.h
void inicializar_tabla_simbolos();
// Función que imprime las variables de la memoria de la calculadora
void consultar_tabla();
//Obtención del tipo de la estructura de datos con el nombre que se pasa como parámetro
int tipo_identificador(char *nombre);
//Obtención del valor de una constante o variable
double obtener_contenido_variable(char *nombre);
//Modificación del valor de una variable
void modificar_variable(char *nombre, float valor);
//Invocación de la función de 1 parámetro cuyo nombre se pasa como parámetro
double usar_funcion_1_parametro(char *nombre, double parametro);
//Invocación de la función de 2 parámetros cuyo nombre se pasa como parámetro
double usar_funcion_2_parametros(char *nombre, double parametro_1, double parametro_2);
//Limpieza de la memoria
void clean();
// Funcion que libera la memoria de la calculadora
void borrar_tabla_simbolos();