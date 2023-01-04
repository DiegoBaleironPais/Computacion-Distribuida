struct componente_lexico
{
    char *lexema;
    short componente_lexico;
};

void abrir_archivo(char *nombre_archivo);
struct componente_lexico* siguiente_componente_lexico();
void cerrar_archivo();