//Espacio para definir funciones
#include <math.h>

struct funcion{
    char* nombre_funcion;
    double (*puntero_funcion)();
};
typedef struct funcion funciones;
funciones func [] = {
   {"acos", acos}, //Función arcocoseno
   {"asin", asin}, //Función arcoseno
   {"atan", atan}, //Función arcotangente
   {"floor", floor}, //Función suelo
   {"cos", cos}, //Función coseno
   {"exp", exp}, //Función exponencial de base e
   {"fabs", fabs}, //Función valor absoluto
   {"ceil", ceil}, //Función techo
   {"fmod", fmod}, //Función modulo flotante
   {"log", log}, //Función logaritmo neperiano
   {"log10", log10}, //Función logaritmo en base 10
   {"pow", pow}, //Función exponencial de base x
   {"sin", sin}, //Función seno
   {"sinh", sinh}, //Función seno hiperbolico
   {"sqrt", sqrt}, //Función raíz cuadrada
   {"tan", tan}, //Función tangente
   {"tanh", tanh}, //Funcion tangente hiperbolica
   {"acosh", acosh}, //Funcion coseno hiperbolico
};