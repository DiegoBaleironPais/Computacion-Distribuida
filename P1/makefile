all: compilacion

compilacion: main.c lexico.c lexico.h sintactico.c sintactico.h sistema_de_entrada.c sistema_de_entrada.h abb.c abb.h tabla_de_simbolos.h tabla_de_simbolos.c errores.c errores.h
	gcc -Wall main.c lexico.c lexico.h sintactico.c sintactico.h sistema_de_entrada.c sistema_de_entrada.h abb.c abb.h tabla_de_simbolos.h tabla_de_simbolos.c errores.c errores.h -o analizador_lexico
	
clean:
	rm -f analizador_lexico
	
