%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tabla_de_simbolos.h"
#include "definiciones.h"
#include "lex.yy.h"

extern int yylex();
void yyerror(char* s);

/*Variable que define si se imprimen los resultados
* de las operaciones por pantalla o no*/

short int echo = 1;

void cambiar_signo() {
    if (echo==1) {
        echo = 0;
        return;
    }
    echo = 1;
}

%}
%union {
    double operador;
    char* identificador;
}

%start input

%token <operador> OPERADOR
%token <identificador> IDENTIFICADOR;
%token PRINT;
%token WORKSPACE;
%token CLEAN;
%token HELP

%type <operador> exp
%type <operador> exp_algebraica
%type <operador> exp_logica
%type <operador> funcion_matematica

%left '='
%left '-' '+' 
%left '*' '/' '%' 
%left '>' '<' MAYORIGUAL MENORIGUAL IGUAL DISTINTO
%left NEGATIVO
%%

input: %empty | input line;

line: '\n'
    | exp '\n' { if(echo) printf ("->%.2f\n\n",$1);}
    | error {yyerror("SENTENCIA NO RECONOCIDA\n");};

exp: OPERADOR {$$ = $1;}
    | IDENTIFICADOR {switch(tipo_identificador($1)) {
        case VARIABLE:
            $$ = obtener_contenido_variable($1);
            break;
        case CONSTANTE:
            $$ = obtener_contenido_variable($1);
            break;
        case -1:
            yyerror("VARIABLE NO INICIALIZADA\n");
            break;
        default:
            yyerror("ESTAS INTENTANDO USAR UNA FUNCION COMO VARIABLE\n");
    }free($1);}
    | exp_algebraica 
    | exp_logica
    | '(' exp ')' {$$ = $2;}
    | funcion_matematica
    | WORKSPACE {consultar_tabla();}
    | PRINT {cambiar_signo();}
    | CLEAN {clean();}
    | HELP {
        FILE *file;
        char c;
        file = fopen("help.txt", "r");
        if (file == NULL) {
            yyerror("EL ARCHIVO DE AYUDA NO ESTÁ DONDE DEBERIA ESTAR\n");
        } else {
            c = fgetc(file);
            while (c != EOF)
            {
                printf("%c", c);
                c = fgetc(file);
            }
        }
        fclose(file);
    };

funcion_matematica:
    IDENTIFICADOR '(' exp ')' {switch(tipo_identificador($1)) {
        case FUNCION:
            $$ = usar_funcion_1_parametro($1,$3);
            break;
        default:
            printf("NOMBRE DE FUNCION INCORRRECTO\n"); }
            free($1);}
    | IDENTIFICADOR '(' exp ',' exp ')' {switch(tipo_identificador($1)) {
        case FUNCION:
            $$ = usar_funcion_2_parametros($1,$3,$5);
            break;
        default:
            printf("NOMBRE DE FUNCION INCORRECTO\n");
    }
    free($1);};


exp_algebraica: exp '+' exp {$$ = $1 + $3;}
    | exp '-' exp {$$ = $1 - $3;}
    | '-' exp %prec NEGATIVO {$$ = -$2;}
    | exp '*' exp {$$ = $1 * $3;}
    | exp '/' exp {if ($3==0) yyerror("ESTAS INTENTANDO DIVIDIR ENTRE 0"); else $$ = $1 / $3;}
    | exp '%' exp {$$ = (int) $1 % (int)$3;};
    | IDENTIFICADOR '=' exp {switch(tipo_identificador($1)) {
        case CONSTANTE:
            yyerror("NO PUEDES MODIFICAR EL VALOR DE UNA CONSTANTE");
            break;
        case FUNCION:
            yyerror("NO PUEDES ASIGNAR VALORES A UNA FUNCIÓN");
            break;
        case VARIABLE:
            $$ = $3;
            break;
        default:
            modificar_variable($1,$3);
            break;
    } free($1);};

exp_logica: 
    exp '>' exp {if ($1>$3) $$ = 1; else $$ = 0;}
    | exp '<' exp {if ($1<$3) $$ = 1; else $$ = 0;}
    | exp MAYORIGUAL exp {if ($1>=$3) $$ = 1; else $$ = 0;}
    | exp MENORIGUAL exp {if ($1<=$3) $$ = 1; else $$ = 0;}
    | exp IGUAL exp {if ($1==$3) $$ = 1; else $$ = 0;}
    | exp DISTINTO exp {if ($1!=$3) $$ = 1; else $$ = 0;};

%%
   
void yyerror(char *s){
    printf("ERROR: %s\n", s);
}