package bombs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Bombs {
    
    
    public static void main(String[] args) throws IOException {        
        Random r = new Random();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Digite la cantidad de estaciones en la red aleatoria a crear: ");
        int n = Integer.parseInt(br.readLine());
        Graph G = new Graph(n);
        boolean sw = true;
        for (int i = 0; i < n * (n - 1); i++) {
            sw = true;
            while(sw){
                int a = r.nextInt(n);
                int b = r.nextInt(n);
                if(a != b){
                    G.AgregarArista(a, b);
                    sw = false;
                }
            }
        }

        int a = G.bomb();
        System.out.println("La estacion que debe bombardear es la estacion " + a + " : " + G.pigeonValue(a) + " : " + G.grado(a));

    }
    
}
