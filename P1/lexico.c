#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include "tabla_de_simbolos.h"
#include "sistema_de_entrada.h"
#include "definiciones.h"
#include "errores.h"

struct componente_lexico
{
    char *lexema;
    short componente_lexico;
};

// Función privada que avanza hasta terminar de leer un string
void obtener_string()
{
    char caracter_auxiliar1 = '1';
    char caracter_auxiliar2;
    do
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    } while (!(caracter_auxiliar1 == '"' && caracter_auxiliar2 != '\\'));
}

// Función privada que avanza hasta terminar de leer un identificador o una keyword
void obtener_identificador()
{
    char caracter_auxiliar;
    do
    {
        caracter_auxiliar = siguiente_caracter();
    } while (isalpha(caracter_auxiliar) || isdigit(caracter_auxiliar) || caracter_auxiliar == '_');
    retroceder_caracter();
}

/*Función privada para leer comentarios largos, se lee todo el contenido
 * hasta encontrar un */
void obtener_comentario_largo()
{
    char caracter_auxiliar1 = '1';
    char caracter_auxiliar2;
    do
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    } while (!(caracter_auxiliar2 == '*' && caracter_auxiliar1 == '/'));
}

// Función para obtener números hexadecimales
void obtener_hexadecimal(struct componente_lexico *componente_lexico)
{
    char caracter_auxiliar1;
    char caracter_auxiliar2 = 'X';

    caracter_auxiliar1 = siguiente_caracter();

    // Si después de la X no viene un carácer hexadecimal o un guión la cadena no era un número hexadecimal.
    if (!isdigit(caracter_auxiliar1) && !(caracter_auxiliar1 >= 65 && caracter_auxiliar1 <= 70) 
        && !(caracter_auxiliar1 >= 97 && caracter_auxiliar1 <= 102) && caracter_auxiliar1 != '_')
    {
        retroceder_caracter();
        retroceder_caracter();
        aceptar_lexema(&(componente_lexico->lexema));
        componente_lexico->componente_lexico = ENTEROHEXADECIMAL;
        return;
    }

    caracter_auxiliar2 = caracter_auxiliar1;
    caracter_auxiliar1 = siguiente_caracter();

    // Si después de la X viene un _ y luego no viene un número hexadecimal la cadena tampoco era un número hexadecimal
    if (caracter_auxiliar2 == '_' && (!isdigit(caracter_auxiliar1) && !(caracter_auxiliar1 >= 65 && caracter_auxiliar1 <= 70) && !(caracter_auxiliar1 >= 97 && caracter_auxiliar1 <= 102)))
    {
        retroceder_caracter();
        retroceder_caracter();
        retroceder_caracter();
        aceptar_lexema(&(componente_lexico->lexema));
        componente_lexico->componente_lexico = ENTEROHEXADECIMAL;
        return;
    }

    // Si pasamos este IF la cadena es 100% un número hexadecimal
    do
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    } while (isdigit(caracter_auxiliar1) || (caracter_auxiliar1 >= 65 && caracter_auxiliar1 <= 70) ||
             (caracter_auxiliar1 >= 97 && caracter_auxiliar1 <= 102) || (caracter_auxiliar1 == '_' && caracter_auxiliar2 != '_'));

    // Un número hexadecimal no puede acabar en _
    if (caracter_auxiliar2 == '_')
    {
        retroceder_caracter();
        retroceder_caracter();
        componente_lexico->componente_lexico = ENTEROHEXADECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }
    // Si el caracter hexadecimal termina por una i es un hexadecimal imaginario
    if (caracter_auxiliar1 == 'i')
    {
        componente_lexico->componente_lexico = ENTEROHEXADECIMALIMAGINARIO;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }
    // Si no termina con una i es un hexadecimal real
    retroceder_caracter();
    componente_lexico->componente_lexico = ENTEROHEXADECIMAL;
    aceptar_lexema(&(componente_lexico->lexema));
    return;
}

// Obtención de exponentes en números flotantes
void obtener_exponente(int antes_del_punto, struct componente_lexico *componente_lexico)
{
    char caracter_auxiliar1 = siguiente_caracter();
    char caracter_auxiliar2 = '2';

    // lectura del signo del exponente
    if (caracter_auxiliar1 == '+' || caracter_auxiliar1 == '-')
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    }

    // Si el exponente no comienza por un digito esta mal construido
    if (!isdigit(caracter_auxiliar1))
    {
        // Retrocedemos caracteres la e no pertenecia al número era parte de otro componente lexico
        if (caracter_auxiliar2 == '+' || caracter_auxiliar2 == '-')
            retroceder_caracter();
        retroceder_caracter();
        // Si el exponente estaba antes de un punto el número es un entero decimal
        if (antes_del_punto == 1)
        {
            componente_lexico->componente_lexico = ENTERODECIMAL;
            aceptar_lexema(&(componente_lexico->lexema));
            return;
        }
        // Si el exponente estaba despues de un punto el número es un flotante decimal
        componente_lexico->componente_lexico = FLOTANTEDECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }

    // Obtención de todos los digitos del exponente
    while (isdigit(caracter_auxiliar1) || (caracter_auxiliar1 == '_' &&
                                           caracter_auxiliar2 != '_'))
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    }

    // Los digitos del exponente no pueden acabar en barra baja, se construye el número hasta antes de la barra baja
    if (caracter_auxiliar2 == '_')
    {
        retroceder_caracter();
        retroceder_caracter();
        componente_lexico->componente_lexico = FLOTANTEDECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }

    // Si el número acaba en i es un flotante imaginario
    if (caracter_auxiliar1 == 'i')
    {
        componente_lexico->componente_lexico = FLOTANTEDECIMALIMAGINARIO;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }
    // Si no es ninguna de estas opciones solo sobra el ultimo caracter
    retroceder_caracter();
    componente_lexico->componente_lexico = FLOTANTEDECIMAL;
    aceptar_lexema(&(componente_lexico->lexema));
    return;
}

// Función general para obtener números
void obtener_numero(struct componente_lexico *componente_lexico)
{
    retroceder_caracter();
    char caracter_auxiliar2 = siguiente_caracter();
    char caracter_auxiliar1 = siguiente_caracter();

    // Si el número empieza por 0X estamos ante un hexadecimal
    if ((caracter_auxiliar2 == '0' && caracter_auxiliar1 == 'x') ||
        (caracter_auxiliar2 == '0' && caracter_auxiliar1 == 'x'))
    {
        obtener_hexadecimal(componente_lexico);
        return;
    }

    /*Leemos digitos y guiones hasta encontrarnos un carácter que no sea
     * un digito o haya dos guiones bajos seguidos */
    while (isdigit(caracter_auxiliar1) || (caracter_auxiliar1 == '_' &&
                                           caracter_auxiliar2 != '_'))
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    }

    // Un número no puede acabar en guión, si lo hace tenemos que devolver el guión
    if (caracter_auxiliar2 == '_')
    {
        retroceder_caracter();
        retroceder_caracter();
        componente_lexico->componente_lexico = ENTERODECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }
    // Si el número acaba en i es un entero imaginario
    if (caracter_auxiliar1 == 'i')
    {
        componente_lexico->componente_lexico = ENTERODECIMALIMAGINARIO;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }

    // Si el número acaba en e o E es un número flotante sin parte decimal
    if (caracter_auxiliar1 == 'e' || caracter_auxiliar1 == 'E')
    {
        obtener_exponente(1, componente_lexico);
        return;
    }

    // Si el número acaba en . en un número flotante con parte decimal
    if (caracter_auxiliar1 == '.')
    {
    
        caracter_auxiliar1 = siguiente_caracter();
        // Si el siguiente caracter no es un digito el número flotante acaba en el .
        if (!isdigit(caracter_auxiliar1) && caracter_auxiliar1 != 'e' 
            && caracter_auxiliar1 != 'E')
        {
            retroceder_caracter();
            componente_lexico->componente_lexico = ENTERODECIMAL;
            aceptar_lexema(&(componente_lexico->lexema));
            return;
        }

        // Obtención de todos los digitos y guiones no consecutivos
        while (isdigit(caracter_auxiliar1) || (caracter_auxiliar1 == '_' &&
                                               caracter_auxiliar2 != '_'))
        {
            caracter_auxiliar2 = caracter_auxiliar1;
            caracter_auxiliar1 = siguiente_caracter();
        }

        // Si el flotante acaba en _ hay que devolver hasta antes del guión
        if (caracter_auxiliar2 == '_')
        {
            retroceder_caracter();
            retroceder_caracter();
            componente_lexico->componente_lexico = FLOTANTEDECIMAL;
            aceptar_lexema(&(componente_lexico->lexema));
            return;
        }
        // Si el flotante acaba en 'i' es un flotante imaginario
        if (caracter_auxiliar1 == 'i')
        {
            componente_lexico->componente_lexico = FLOTANTEDECIMALIMAGINARIO;
            aceptar_lexema(&(componente_lexico->lexema));
            return;
        }
        // Si acaba en 'e' o 'E' hay que llamar a la funcion obtener exponente
        if (caracter_auxiliar1 == 'e' || caracter_auxiliar1 == 'E')
        {
            obtener_exponente(0, componente_lexico);
            return;
        }

        // Si no es ninguo de los casos anteriores el número es un flotante decimal
        retroceder_caracter();
        componente_lexico->componente_lexico = FLOTANTEDECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }
    // Si no es ninguo de los casos anteriores el número es un entero decimal
    retroceder_caracter();
    componente_lexico->componente_lexico = ENTERODECIMAL;
    aceptar_lexema(&(componente_lexico->lexema));
    return;
}

// Función que obtiene números flotantes que empiezan por .
void obtener_float_despues_de_punto(struct componente_lexico *componente_lexico)
{
    char caracter_auxiliar1 = siguiente_caracter();
    char caracter_auxiliar2 = '2';

    // Obtención de todos los digitos y guiones separados
    while (isdigit(caracter_auxiliar1) || (caracter_auxiliar1 == '_' &&
                                           caracter_auxiliar2 != '_'))
    {
        caracter_auxiliar2 = caracter_auxiliar1;
        caracter_auxiliar1 = siguiente_caracter();
    }

    // Un número flotante no puede acabar en guión
    if (caracter_auxiliar2 == '_')
    {
        retroceder_caracter();
        retroceder_caracter();
        componente_lexico->componente_lexico = FLOTANTEDECIMAL;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }

    // Si acaba en 'i' es un flotante imaginario
    if (caracter_auxiliar1 == 'i')
    {
        componente_lexico->componente_lexico = FLOTANTEDECIMALIMAGINARIO;
        aceptar_lexema(&(componente_lexico->lexema));
        return;
    }

    // Si acaba en e o E hay que leer el exponente que se viene
    if (caracter_auxiliar1 == 'e' || caracter_auxiliar1 == 'E')
    {
        obtener_exponente(1, componente_lexico);
        return;
    }

    // Si no es ninguno de los casos anteriores acaba el flotante
    retroceder_caracter();
    componente_lexico->componente_lexico = FLOTANTEDECIMAL;
    aceptar_lexema(&(componente_lexico->lexema));
    return;
}

// Función principal de detección de componentes léxicos
struct componente_lexico *siguiente_componente_lexico()
{
    char caracter_auxiliar;
    struct componente_lexico *componente_lexico;

    componente_lexico = (struct componente_lexico *)malloc(sizeof(struct componente_lexico));
    caracter_auxiliar = siguiente_caracter();
    // Implementación de los automatas que reconocen el componente léxico
    switch (caracter_auxiliar)
    {
        /*Los primeros case hacen referencia a carácteres
        * que forman lexemas por si solos, en todos se sigue
        * la misma estrategia, se construye el lexema se asigna
        * como componente léxico el valor en ASCCI del caracter 
        * y se retorna el struct*/
        case '[':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '[';
            return componente_lexico;
        case ']':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = ']';
            return componente_lexico;
        case '(':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '(';
            return componente_lexico;
        case ')':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = ')';
            return componente_lexico;
        case '}':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '}';
            return componente_lexico;
        case '{':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '{';
            return componente_lexico;
        case ';':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = ';';
            return componente_lexico;
        case ',':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = ',';
            return componente_lexico;
        case '-':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '-';
            return componente_lexico;
         case '=':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '=';
            return componente_lexico;
        case '*':
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '*';
            return componente_lexico;
        /*Si se detectan unas comillas, se reconoce la cadena
        * y se lee todo el contenido hasta que vuelvan a apa-
        * recer unas comillas con la función obtener_string
        * luego se acepta el lexema y se retorna*/
        case '"':
            obtener_string();
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = CADENA;
            return componente_lexico;
        /*A partir de aquí los case se vuelven más complejos
        * ya que hacen referencia a carácteres que pueden generar
        * lexemas de componentes lexicos distintos*/
        // El punto puede ser un punto o el comienzo de un flotante
        case '.':
            /*Si el siguiente caracter es un digito el punto
            * es el comienzo de un número flotante*/
            if (isdigit(siguiente_caracter()))
            {
                obtener_float_despues_de_punto(componente_lexico);
                return componente_lexico;
            }
            // En caso contrario es un operador
            retroceder_caracter();
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '.';
            return componente_lexico;
        //':' puede formar el operador := o el operador :
        case ':':
            if (siguiente_caracter() == '=')
            {
                aceptar_lexema(&(componente_lexico->lexema));
                componente_lexico->componente_lexico = PUNTOIGUAL;
                return componente_lexico;
            }
            retroceder_caracter();
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = ':';
            return componente_lexico;
        //'<' que solo puede formar <-
        case '<':
            if (siguiente_caracter() == '-')
            {
                aceptar_lexema(&(componente_lexico->lexema));
                componente_lexico->componente_lexico = MAYORGUION;
                return componente_lexico;
            }
            else
            {
                retroceder_caracter();
                notificar_error("'<' en posición incorrecta\n");
                free(componente_lexico);
                return NULL;
            }
            return siguiente_componente_lexico();;
        //'+' puede formar + o +=
        case '+':
            if (siguiente_caracter() == '=')
            {
                aceptar_lexema(&(componente_lexico->lexema));
                componente_lexico->componente_lexico = MASIGUAL;
                return componente_lexico;
            }
            retroceder_caracter();
            aceptar_lexema(&(componente_lexico->lexema));
            componente_lexico->componente_lexico = '+';
            return componente_lexico;
        /* Una barra lateral indica que viene un comentario, sin
        * embargo este puede ser largo o corto si es
        * corto se llama a una función que lee hasta el fin de la
        * linea, si es largo se llama a una función que lee hasta
        * encontrar un */
        case '/':
            caracter_auxiliar = siguiente_caracter();
            switch (caracter_auxiliar)
            {
            case '/':
                do
                {
                    caracter_auxiliar = siguiente_caracter();
                } while (caracter_auxiliar != '\n');
                actualizar_inicio();
                free(componente_lexico);
                return NULL;
            case '*':
                obtener_comentario_largo();
                actualizar_inicio();
                free(componente_lexico);
                return NULL;
            //Operador de división
            default:
                retroceder_caracter();
                aceptar_lexema(&(componente_lexico->lexema));
                componente_lexico->componente_lexico = '/';
                return componente_lexico;
            }
        // Si se detecta un 'EOF' se termino de leer el fichero
        case EOF:
            componente_lexico->componente_lexico = -1;
            return componente_lexico;
        /*La tabulación, el retorno de carro, el espacio y el
        * salto de linea son caracteres que por si solos no tienen
        * significado por lo que se ignoran*/
        case ' ':
            actualizar_inicio();
            free(componente_lexico);
            return NULL;
        case '\n':
            actualizar_inicio();
            free(componente_lexico);
            return NULL;
        case '\r':
            actualizar_inicio();
            free(componente_lexico);
            return NULL;
        case '\t':
            actualizar_inicio();
            free(componente_lexico);
            return NULL;
        default :
            /*Si el carácter es un guión o un caracter alfabético estamos
            * ante un identificador o una palabra reservada*/
            if (isalpha(caracter_auxiliar) || caracter_auxiliar == '_')
            {
                obtener_identificador();
                aceptar_lexema(&(componente_lexico->lexema));
                // Consultamos el componente lexico con la tabla de simbolos
                componente_lexico->componente_lexico = obtener_componente(componente_lexico->lexema);
                return componente_lexico;
            }
            /*Si el carácter es un número estamos ante un número*/
            if (isdigit(caracter_auxiliar))
            {
                obtener_numero(componente_lexico);
                return componente_lexico;
            }
            notificar_error(strncat("Ningun lexema comienza por el caracter proporcionado\n",&caracter_auxiliar,1));
            free(componente_lexico);
            return NULL;
    }
    return NULL;
}
