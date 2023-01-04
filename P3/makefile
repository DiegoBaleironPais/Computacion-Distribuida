all: compilacion

compilacion: main.c lex.yy.h lex.yy.c sintactico.c abb.c abb.h tabla_de_simbolos.h tabla_de_simbolos.c definiciones.h funciones.h	
	gcc -lfl lex.yy.c lex.yy.h sintactico.tab.c sintactico.tab.h definiciones.h tabla_de_simbolos.h tabla_de_simbolos.c abb.c abb.h funciones.h main.c -o calculadora_byson -lm;
	
clean:
	rm -f calculadora_byson
	
