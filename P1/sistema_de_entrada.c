#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

// Tamanho de palabra máximo
#define MAX_LEX_LEN 40

// Struct doble centinela
struct doble_buffer_centinela
{
    char bufferA[MAX_LEX_LEN + 1];
    int inicio;
    int delantero;
    char bufferB[MAX_LEX_LEN + 1];
    /*Esta variable sirve para saber si hay que refrescar el buffer de datos
     * al llegar a el o no*/
    char ultimo_bloque_refrescado;
};

// Puntero a struct doble centinela
struct doble_buffer_centinela *doble_buffer;
// Puntero FILE al código fuente
FILE *codigo;

/* Inicialización del sistema de entrada, se abre el documento,
 * se inicializan los buffers y se cargan los datos*/
void abrir_documento(char *nombre_documento)
{
    int caracteres_leidos; // Entero con el número de carácteres leídos al primer buffer
    // Apertura del archivo
    codigo = fopen(nombre_documento, "r");
    if (codigo == NULL)
    {
        printf("Error al intentar abrir el código.\n");
        exit(-1);
    }
    // Incialización del buffer
    doble_buffer = (struct doble_buffer_centinela *)malloc(sizeof(struct doble_buffer_centinela));
    (*doble_buffer).bufferA[MAX_LEX_LEN] = EOF;
    (*doble_buffer).bufferB[MAX_LEX_LEN] = EOF;
    (*doble_buffer).inicio = 0;
    (*doble_buffer).delantero = 0;
    (*doble_buffer).ultimo_bloque_refrescado = 'a';
    caracteres_leidos = fread((*doble_buffer).bufferA, sizeof(char), MAX_LEX_LEN, codigo);
    // En caso de que no se llene el contenido del buffer se marca la primero posición vacía con un EOF
    if (caracteres_leidos < MAX_LEX_LEN)
    {
        (*doble_buffer).bufferA[caracteres_leidos] = EOF;
    }
}

//Liberación de la memoria del sistema de entrada
void cerrar_documento() {
    fclose(codigo);
    free(doble_buffer);
}

// Función privada que devuelve el siguiente caracter de los buffers.
char obtener_caracter()
{
    // Comprobamos en que buffer se encuentra delantero y devolvemos el caracter correspondiente
    if ((*doble_buffer).delantero <= MAX_LEX_LEN)
    {
        return (*doble_buffer).bufferA[(*doble_buffer).delantero];
    }
    return (*doble_buffer).bufferB[(*doble_buffer).delantero - (MAX_LEX_LEN + 1)];
}

// Función privada para refrescar el tamaño de los buffers
void refrescar_buffers()
{
    int caracteres_leidos;
    // Si delantero es 2*MAX_LEX_LEN +1 el buffer que hay que refrescar es el primero
    if ((*doble_buffer).delantero == (2 * MAX_LEX_LEN + 1))
    {
        // Reinicio de delantero y si es necesario tambien de inico
        if ((*doble_buffer).delantero == (*doble_buffer).inicio)
        {
            (*doble_buffer).inicio = 0;
        }
        (*doble_buffer).delantero = 0;
        /*Si el último bloque refrescado fue b hay que refrescar el
         * bloque a, si el último bloque refresacado fue el a aquí
         * termina el refresco*/
        if ((*doble_buffer).ultimo_bloque_refrescado == 'b')
        {
            (*doble_buffer).ultimo_bloque_refrescado = 'a';
            caracteres_leidos = fread((*doble_buffer).bufferA, sizeof(char), MAX_LEX_LEN, codigo);
            /*Si no se leen los caracteres suficientes quiere decir que el fichero acabó y por lo
             * tanto hay que añadir un EOF en el buffer*/
            if (caracteres_leidos < MAX_LEX_LEN)
            {
                (*doble_buffer).bufferA[caracteres_leidos] = EOF;
            }
            return;
        }
        return;
    }
    // Si delantero es MAX_LEX_LEN el buffer que hay que refrescar es el segundo
    if ((*doble_buffer).delantero == MAX_LEX_LEN)
    {
        // Actualización de delantero y si es necesario tambien de inico
        if ((*doble_buffer).delantero == (*doble_buffer).inicio)
        {
            (*doble_buffer).inicio++;
        }
        (*doble_buffer).delantero++;
        /*Si el último bloque refrescado fue a hay que refrescar el
         * bloque b, si el último bloque refresacado fue el b aquí
         * termina el refresco*/
        if ((*doble_buffer).ultimo_bloque_refrescado == 'a')
        {
            //printf("Refrescando b\n");
            (*doble_buffer).ultimo_bloque_refrescado = 'b';
            caracteres_leidos = fread((*doble_buffer).bufferB, sizeof(char), MAX_LEX_LEN, codigo);
            //printf("%s\n",(*doble_buffer).bufferB);
            /*Si no se leen los caracteres suficientes quiere decir que el fichero acabó y por lo
             * tanto hay que añadir un EOF en el buffer*/
            if (caracteres_leidos < MAX_LEX_LEN)
            {
                (*doble_buffer).bufferB[caracteres_leidos] = EOF;
            }
            return;
        }
        return;
    }
}

// Función que devuelve el siguiente caracter del fichero
char siguiente_caracter()
{
    char caracter_leido = obtener_caracter();
    if (caracter_leido == EOF)
    {
        // Se llega al fin de uno de los buffers y por tanto hay que refrescar
        if ((*doble_buffer).delantero == MAX_LEX_LEN ||
            (*doble_buffer).delantero == (2 * MAX_LEX_LEN + 1))
        {
            refrescar_buffers();
            return siguiente_caracter();
        }
        // Se llega al fin de fichero
        else
        {
            return EOF;
        }
    }
    (*doble_buffer).delantero++;
    return caracter_leido;
}

// Función para retroceder el puntero del buffer en una unidad
void retroceder_caracter()
{
    switch ((*doble_buffer).delantero)
    {
    /*El caso 0 y el caso MAX_LEX_LEN + 1 son casos
     * especiales que hay que manejar ya que si no
     * se hiciese el puntero caería en un EOF y sería
     * como no haber retrocedido nada.*/
    case 0:
        (*doble_buffer).delantero = 2 * MAX_LEX_LEN;
        break;
    case MAX_LEX_LEN + 1:
        (*doble_buffer).delantero = MAX_LEX_LEN - 1;
        break;
    // Caso genérico
    default:
        (*doble_buffer).delantero = (*doble_buffer).delantero -1;
    }
}

/*Función privada que guarda el lexema en la variable lexema y
 * adelanta la variable inicio*/
void construir_lexema(char **lexema)
{
    int posicion = 0;
    while ((*doble_buffer).delantero != (*doble_buffer).inicio)
    {
    
        if ((*doble_buffer).inicio < MAX_LEX_LEN)
        {
            (*lexema)[posicion] = (*doble_buffer).bufferA[(*doble_buffer).inicio];
            posicion++;
        } else {
            if ((*doble_buffer).inicio != 2*MAX_LEX_LEN+1 && (*doble_buffer).inicio != MAX_LEX_LEN) {
                (*lexema)[posicion] = (*doble_buffer).bufferB[(*doble_buffer).inicio-(MAX_LEX_LEN+1)];
                posicion++;
            }
        }
        (*doble_buffer).inicio++;
        // Reinicio de la variable inicio
        if ((*doble_buffer).inicio > (2 * MAX_LEX_LEN) + 1)
        {
            (*doble_buffer).inicio = 0;
        }
    }
    (*lexema)[posicion] = '\0';
}

// Función que devuelve el léxema comprendido entre incio y delantero
void aceptar_lexema(char **lexema)
{
    int tamanho_lexema = 0;
    /*Estos ifs y elses son todas las combinaciones de posiciones que pueden tener
     * inicio y delantero, las uso para reservar el tamaño justo para el puntero lexema*/
    if ((*doble_buffer).delantero > (*doble_buffer).inicio)
    {
        if ((*doble_buffer).delantero > MAX_LEX_LEN && (*doble_buffer).inicio < MAX_LEX_LEN)
        {
            tamanho_lexema = (*doble_buffer).delantero - (*doble_buffer).inicio - 1;
        }
        else
        {
            tamanho_lexema = (*doble_buffer).delantero - (*doble_buffer).inicio;
        }
    }

    if ((*doble_buffer).delantero < (*doble_buffer).inicio)
    {
        if ((*doble_buffer).delantero < MAX_LEX_LEN && (*doble_buffer).inicio > MAX_LEX_LEN)
        {
            tamanho_lexema = (*doble_buffer).inicio - (*doble_buffer).delantero - 1;
        }
        else
        {
            tamanho_lexema = (*doble_buffer).delantero - (*doble_buffer).inicio;
        }
    }
    tamanho_lexema++;
    *lexema = (char*) malloc(tamanho_lexema*sizeof(char));
    construir_lexema(lexema);
}

//Función para actualizar el puntero inicio
void actualizar_inicio() {
    (*doble_buffer).inicio = (*doble_buffer).delantero;
}
