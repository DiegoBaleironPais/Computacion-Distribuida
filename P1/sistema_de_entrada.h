/* Inicialización del sistema de entrada, se abre el documento,
 * se inicializan los buffers y se cargan los datos*/
void abrir_documento(char *nombre_documento);
//Función que devuelve el siguiente caracter del fichero
char siguiente_caracter();
//Función para retroceder el puntero del buffer en una unidad 
void retroceder_caracter();
// Función que devuelve el léxema comprendido entre incio y delantero
char* aceptar_lexema();
//Liberación de la memoria del sistema de entrada
void cerrar_documento();
//Función para actualizar el puntero inicio
void actualizar_inicio();