package bombs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

public class Bombs {

//    public static void main(String[] args) throws IOException {
//
//        System.out.println("Digite inicialmente la cantidad de estaciones en su red y posteriormente la cantidad de rieles que hay usando el siguiente formato: estaciones rieles.");
//
//        System.out.println("Despues digite parejas de estaciones para indicar que el riel une a esas dos estaciones.");
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        StringTokenizer st = new StringTokenizer(br.readLine());
//
//        int n = Integer.parseInt(st.nextToken());
//
//        int m = Integer.parseInt(st.nextToken());
//
//        Graph G = new Graph(n);
//
//        for (int i = 0; i < m; i++) {
//
//            st = new StringTokenizer(br.readLine());
//
//            int a = Integer.parseInt(st.nextToken());
//
//            int b = Integer.parseInt(st.nextToken());
//
//            G.AgregarArista(a, b);
//
//        }
//
//        System.out.println("La estacion que debe bombardear es la estacion " + G.bomb() + " porque su pigeon value es " + G.pigeonValue(G.bomb()) + ".");
//
//        new GraphViewer(G);
//    }
    
    
    public static void main(String[] args) throws IOException {
        Random r = new Random();
        Graph G = new Graph(9999);
        boolean sw = true;
        for (int i = 0; i < 6000; i++) {
//            G.AgregarArista(i * 4 + 1, i * 4 + 3);
            sw = true;
            while(sw){
                int a = r.nextInt(9999);
                int b = r.nextInt(9999);
                if(a != b){
                    G.AgregarArista(a, b);
                    sw = false;
                }
            }
        }
        
//        for(int i = 0; i <  999; i++){
//            for(int j = 0; j < 999; j++){
//                if(i != j){
//                    G.AgregarArista(i, j);
//                }
//            }
//        }

        System.out.println("Empezo!");
        int a = G.bomb();
        
//        for (int i : G.cutSet) {
//            System.out.println(i + " : " + (G.compQty < G.pigeonValue(i)));
//        }
        
        System.out.println(G.esConexo() + " : " +G.kReg());
        
        
        System.out.println("QTY:    " + G.compQty + " : " + G.cutSet.size());
        System.out.println("La estacion que debe bombardear es la estacion " + a + " : " + G.pigeonValue(a));
        
        //new GraphViewer(G);
    }
    
}
