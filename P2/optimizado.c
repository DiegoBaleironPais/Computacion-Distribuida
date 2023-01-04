#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <sys/time.h>

#define N 10000
#define ITER 100000
#define NUMEROITERACIONES 5

int main()
{
    struct timeval inicio, final;
    int i, k, j;
    double tiempo_ejecucion[NUMEROITERACIONES], tiempo_inicializacion;
    float *x = (float *)malloc(N * sizeof(float));
    float *y = (float *)malloc(N * sizeof(float));

    gettimeofday(&inicio, NULL);
    gettimeofday(&final, NULL);

    //Calentamiento de cache
    for (i = 0; i < N; i++)
    {
            x[i] = rand() % 2;
            y[i] = rand() % 2;
    }

    tiempo_inicializacion = (final.tv_sec - inicio.tv_sec + (final.tv_usec - inicio.tv_usec) / 1.e6);

    for (j = 0; j < NUMEROITERACIONES; j++) {
    gettimeofday(&inicio, NULL);
    for (k = 0; k < ITER; k++)
    {

        for (i = 0; i < N; i++)
        {
            x[i] = sqrt((float)i);
            y[i] = (float)i + 2.0;
            x[i] += sqrt(y[i]);
        }
    }
    gettimeofday(&final, NULL);
    tiempo_ejecucion[j] = (final.tv_sec - inicio.tv_sec + (final.tv_usec - inicio.tv_usec) / 1.e6);
    }

    for (i = 0; i < N; i++)
    {
        printf("y[%d] = %f\n", i + 1, y[i]);
        printf("x[%d] = %f\n", i + 1, x[i]);
    }

    for(j = 0; j < NUMEROITERACIONES; j++) {
         printf("Tiempo total %d -> %lf\n", 1+j, tiempo_ejecucion[j] - tiempo_inicializacion);
    }
}