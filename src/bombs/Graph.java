package bombs;

import java.util.Arrays;

public class Graph {

    int orden, aux , tamaño;
    boolean[][] A;
    boolean[] visitado;
    
    public Graph(int orden) {
        this.orden = orden;
        this.visitado = new boolean[orden];
        this.A = new boolean[orden + 1][orden + 1];
        this.tamaño = 0;
        for (int i = 0; i < orden; i++) {
            Arrays.fill(A[i], false);
        }
    }

    public void AgregarArista(int a, int b) {
        A[a][b] = A[b][a] = true;
        tamaño++;
    }

    public void Kappa(int v, int vertex) {
        if (visitado[v]) {
            return;
        }
        visitado[v] = true;
        for (int i = 0; i < orden; i++) {
            if (A[v][i] && i != vertex) {
                Kappa(i, vertex);
            }
        }
    }

    public int pigeonValue(int vertex) {
        int pV = 0;
        Arrays.fill(visitado, false);
        for (int i = 0; i < orden; i++) {
            if (!visitado[i] && i != vertex) {
                Kappa(i, vertex);
                pV++;
            }
        }
        return pV;
    }


    public int bomb() {
        int maxPV = 0, maxV = 0, s = 0;
        for (int i = 0; i < orden; i++) {
            s = pigeonValue(i);
            if (s > maxPV) {
                maxPV = s;
                maxV = i;
            }
        }
        return maxV;
    }
}
