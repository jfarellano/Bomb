package bombs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Graph {

    int orden, aux , tamaño, compQty;
    boolean[][] A;
    boolean[] visitado;
    int[][] components;
    ArrayList<Node> nodes;
    LinkedList<Integer> visited, cutSet;
    
    public Graph(int orden) {
        this.nodes = new ArrayList<Node>();
        this.orden = orden;
        this.visitado = new boolean[orden];
        this.components = new int[orden + 1][orden + 1];
        this.A = new boolean[orden + 1][orden + 1];
        this.visited = new LinkedList<>();
        this.cutSet = new LinkedList<>();
        this.compQty = 0;
        this.tamaño = 0;
        for (int i = 0; i < orden; i++) {
            Arrays.fill(A[i], false);
            nodes.add(new Node(i));
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

    public int component(int v) {
        if (visitado[v]) {
            return 0;
        }
        visitado[v] = true;
        components[compQty][aux] = v;
        aux++;
        for (int i = 0; i < orden; i++) {
            if (A[v][i]) {
                component(i);
            }
        }
        return aux;
    }

    public void getComponents() {
        compQty = 0;
        Arrays.fill(visitado, false);
        for (int i = 0; i < orden; i++) {
            if (!visitado[i]) {
                aux = 1;
                components[compQty][0] = component(i);
                compQty++;
            }
        }
    }

    public int pigeonValue(int vertex) {
        int pV = 0;
        Arrays.fill(visitado, false);
        for (int i = 1; i < components[getVComp(vertex)][0]; i++) {
            if (!visitado[components[getVComp(vertex)][i]] && components[getVComp(vertex)][i] != vertex) {
                Kappa(components[getVComp(vertex)][i], vertex);
                pV++;
            }
        }
        return pV + compQty - 1;
    }

    public int getVComp(int v) {
        for (int i = 0; i < compQty; i++) for (int j = 1; j < components[i][0]; j++) if (components[i][j] == v) return i;
        return 0;
    }

    public int bomb() {
        int maxPV = 0, maxV = 0, s = 0;
        setCutEdges();
        if(cutSet.size() == 0) for(int i = 0; i < orden; i++) if(grado(i) > 0) return i;
        sortCutSet();
        for (int i = 0; i < cutSet.size(); i++) {
            if (grado(cutSet.get(i)) > 1) {
                s = pigeonValue(cutSet.get(i));
                if(i < cutSet.size() - 1){
                    if((grado(cutSet.get(i)) == s - compQty + 1|| s > (grado(cutSet.get(i + 1)) + compQty)) && s >= maxPV) return cutSet.get(i);
                    if(maxPV >= grado(cutSet.get(i)) + compQty) return maxV;
                }
                if (s > maxPV) {
                    maxPV = s;
                    maxV = cutSet.get(i);
                }
            }
        }
        return maxV;
    }

    public int grado(int v) {
        int g = 0;
        for (int i = 0; i < orden; i++) if (A[v][i]) g++;
        return g;
    }

    public class Node {

        private final int data;
        int visitedNumber;
        int lowNumberReacheable = -1;

        public LinkedList<Integer> backEdges = new LinkedList<>();
        public LinkedList<Integer> forwardEdges = new LinkedList<>();

        private int x = 0, y = 0;

        public Node(int data) {
            this.data = data;
        }

        public void setCoords(int _x, int _y) {
            x = _x;
            y = _y;
        }

        public boolean isValid() {
            return x != 0 && y != 0;
        }

        public int getData() {
            return data;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
    
    public void sortCutSet(){
        Collections.sort(cutSet, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return grado(o2) - grado(o1);
            }
        });
    }

    //John's place!


    public void setingUpTree(int vertex, int cont) {
        visited.add(vertex);
        nodes.get(vertex).visitedNumber = cont + 1;
        int comp = getVComp(vertex);
        for (int i = 1; i < components[comp][0]; i++) {
            if (A[vertex][components[comp][i]]) {
                if (!visited.contains(components[comp][i])) {
                    nodes.get(vertex).forwardEdges.add(components[comp][i]);
                    setingUpTree(components[comp][i], cont + 1);
                } else nodes.get(vertex).backEdges.add(components[comp][i]);
            }
        }
    }

  public int setingLowValues(int vertex) {
        int low = 9999;
        if (nodes.get(vertex).lowNumberReacheable == -1) {
            low = nodes.get(vertex).visitedNumber; //Rule 1
            nodes.get(vertex).lowNumberReacheable = low;
            for (Integer backEdge : nodes.get(vertex).backEdges) {
                int lowBackEdge = nodes.get(backEdge).visitedNumber; //Rule 2
                if (low > lowBackEdge) {
                    low = lowBackEdge;
                    nodes.get(vertex).lowNumberReacheable = low;
                }
            }
            if (nodes.get(vertex).visitedNumber == 1) if (nodes.get(vertex).forwardEdges.size() >= 2) cutSet.add(vertex); //System.out.println("Vertex " + vertex + " is an articulation point...");
            for (Integer forwardEdge : nodes.get(vertex).forwardEdges) {
                int lowFordwardEdge = setingLowValues(forwardEdge);
                if (lowFordwardEdge >= nodes.get(vertex).visitedNumber) if (!cutSet.contains(vertex) && nodes.get(vertex).visitedNumber != 1) cutSet.add(vertex);
                if (low > lowFordwardEdge) {
                    low = lowFordwardEdge; //Rule 3
                    nodes.get(vertex).lowNumberReacheable = low;
                }
            }

        }
        return low;
    }

    public void setCutEdges() {
        getComponents();
        for (int i = 0; i < compQty; i++) {
            if (components[i][0] != 1) {
                setingUpTree(components[i][1], 0);
                setingLowValues(components[i][1]);
            }
        }
    }
}
