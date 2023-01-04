/*Función que inicializa la tabla de simbolos y carga en ella los
* componentes léixcos del fichero definiciones.h */
void inicializar_tabla_simbolos();
//Función que devuelve el componente léxico al que pertenece 'lexema'
int obtener_componente (char* lexema);
// Función que imprime el contenido de la tabla de simbolos
void consultar_tabla();
// Funcion que libera la memoria de la tabla de simbólos
void borrar_tabla_simbolos();