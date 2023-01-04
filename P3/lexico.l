%{
#include "sintactico.tab.h"

extern int yylex_destroy(void);

//Apertura del archivo con el código fuente
void abrir_archivo(char *nombre_archivo) {
    yyin = fopen(nombre_archivo,"r");
    if (yyin == NULL) {
        printf("Error al intentar abrir el archivo\n");
        yyin = stdin;
    }
}

//Eliminación de memoria
void cerrar_archivo() {
    printf("Cerrando archivo\n");
    fclose(yyin);
    yylex_destroy();
    //Si no pongo estoy se me buggea el yyin no se muy bien porque
    yyparse();
}

//Eliminación de memoria
void cerrar() {
    yy_delete_buffer(YY_CURRENT_BUFFER);
    free(yy_buffer_stack);
}

%}

%option nounput
%option noinput

NUMERO_ENTERO {DIGITO_DECIMAL}+
DIGITO_DECIMAL [0-9]
NUMERO_FLOTANTE {DIGITO_DECIMAL}+"."({DIGITO_DECIMAL}+)?{EXPONENTE_DECIMAL}?|{DIGITO_DECIMAL}+{EXPONENTE_DECIMAL}|"."{DIGITO_DECIMAL}+({EXPONENTE_DECIMAL})?
EXPONENTE_DECIMAL ("e"|"E")("+"|"-")?{DIGITO_DECIMAL}+
OPERADORESALGEBRAICOS "+"|"-"|"*"|"/"|"%"|"="
OPERADORESLOGICOS "<"|">"
MAYORIGUAL ">="
MENORIGUAL "<="
IGUAL "=="
DISTINTO "!="
SEPARADOR "\t"|" "|"\r"
COMA ","
IDENTIFICADOR {LETRA}({LETRA}|{DIGITO_DECIMAL})*
ARCHIVO ({LETRA}|{DIGITO_DECIMAL}|".")+
LETRA [a-zA-Z_]
ABRIRPARENTESIS "("
CERRARPARENTESIS ")"
FINLINEA "\n"

%x APERTURA_ARCHIVO

%%

"quit" cerrar(); yyterminate();
"workspace" return(WORKSPACE);
"echo" return(PRINT);
"clean" return(CLEAN);
"help" return(HELP);
"load "	{BEGIN(APERTURA_ARCHIVO);}

<APERTURA_ARCHIVO>{ARCHIVO} {abrir_archivo(yytext); BEGIN(0);}

{IDENTIFICADOR} {yylval.identificador = (char*)malloc(sizeof(yytext)); strcpy(yylval.identificador, yytext); return (IDENTIFICADOR);}
{NUMERO_ENTERO} |
{NUMERO_FLOTANTE} {yylval.operador=atof(yytext); return (OPERADOR);}
{OPERADORESALGEBRAICOS} return (yytext[0]);
{OPERADORESLOGICOS} return (yytext[0]);
{MAYORIGUAL} return(MAYORIGUAL);
{MENORIGUAL} return(MENORIGUAL);
{IGUAL} return(IGUAL);
{DISTINTO} return(DISTINTO);
{COMA} return ',';
{SEPARADOR}
{ABRIRPARENTESIS} return '(';
{CERRARPARENTESIS} return ')';
{FINLINEA} return '\n';
"." printf("Error lexico caracter incorrecto\n");
<<EOF>> cerrar_archivo(); yyterminate();
%%
