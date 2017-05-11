package bombs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Graph {

    int orden;
    int aux;
    int tamaño;
    int compQty;
    boolean[][] A;//falso = 0, verdadero = 1
    boolean[] visitado;
    int[][] components;
    ArrayList<Node> nodes;

    public Graph(int orden) {
        this.nodes = new ArrayList<Node>();
        this.orden = orden;
        this.visitado = new boolean[orden];
        this.components = new int[orden + 1][orden + 1];
        A = new boolean[orden][orden];
        for (int i = 0; i < orden; i++) {
            Arrays.fill(A[i], false);
            nodes.add(new Node(i));
        }
        compQty = 0;
        tamaño = 0;
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

    public boolean kReg() {
        for (int i = 0; i < orden - 1; i++) {
            if (grado(i) != grado(i + 1)) {
                return false;
            }
        }
        return true && esConexo();
    }

    public boolean gradeValid() {
        for (int i = 0; i < orden; i++) {
            if (grado(i) > 1) {
                return true;
            }
        }
        return false;
    }

    public boolean regularComponent(int comp) {
        int grado = grado(components[comp][1]);
        for (int i = 2; i < components[comp][0]; i++) {
            if (grado != grado(components[comp][i])) {
                return false;
            }
        }
        return true;
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
        for (int i = 0; i < compQty; i++) {
            for (int j = 1; j < components[i][0]; j++) {
                if (components[i][j] == v) {
                    return i;
                }
            }
        }
        return 0;
    }

    public int bomb() {
        int maxPV = 0, maxV = 0, s = 0;
        if (kReg()) {
            return 0;
        }
        getComponents();
        for(int i = 0; i < compQty; i++){
            if(components[i][0] != 1){
                setingUpTree(components[i][1]);
                setingLowValues(components[i][1]);
            }
        }
        
        boolean v = gradeValid();
        for (int i = 0; i < cutSet.size(); i++){ 
            int vertex = cutSet.get(i);
            if (grado(vertex) != 0) {
                if (v) {
                    if (regularComponent(getVComp(vertex))) {
                        s = compQty;
                    } else if (grado(vertex) != 1) {
                        s = pigeonValue(vertex);
                        //System.out.println(i);
                    } else {
                        s = compQty;
                    }
                } else {
                    s = pigeonValue(vertex);
                }
            } else {
                s = compQty - 1;
            }
            if (s > maxPV) {
                maxPV = s;
                maxV = i;
            }
        }
        return maxV;
    }

    public int grado(int v) {
        int g = 0;
        for (int i = 0; i < orden; i++) {
            if (A[v][i]) {
                g++;
            }
        }
        return g;
    }

    public boolean esConexo() {
        visitado = new boolean[orden + 1];
        Arrays.fill(visitado, false);
        DFS(1);
        for (int i = 1; i < orden; i++) {
            if (!visitado[i]) {
                return false;
            }
        }
        return true;
    }

    public void DFS(int v) {
        if (visitado[v]) {
            return;
        }
        visitado[v] = true;
        for (int i = 1; i < orden; i++) {
            if (A[v][i]) {
                DFS(i);
            }
        }
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

    //John's place!
    LinkedList<Integer> visited = new LinkedList<>();
    LinkedList<johnNode> vertexes = new LinkedList<>(); //No!
    LinkedList<Integer> cutSet = new LinkedList<>();

    public void loadVertexes() {
        for (int i = 0; i < orden; i++) {
            vertexes.add(new johnNode(i));
        }
    }

    public void setingUpTree(int vertex) {
        visited.add(vertex);
        nodes.get(vertex).visitedNumber = visited.size();
        int comp = getVComp(vertex);
        for (int i = 1; i < components[comp][0]; i++) {
            if (A[vertex][components[comp][i]]) {
                if (!visited.contains(components[comp][i])) {
                    nodes.get(vertex).forwardEdges.add(components[comp][i]);
                    setingUpTree(components[comp][i]);
                } else {
                    nodes.get(vertex).backEdges.add(components[comp][i]);
                }
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
            if (nodes.get(vertex).forwardEdges.size() >= 2) {
                System.out.println("Vertex " + vertex + " is an articulation point...");
                cutSet.add(vertex);
            }
            for (Integer forwardEdge : nodes.get(vertex).forwardEdges) {
                int lowFordwardEdge = setingLowValues(forwardEdge);
                if (lowFordwardEdge >= nodes.get(vertex).visitedNumber) {
                    System.out.println("Vertex " + vertex + " is an articulation point...");
                    if (!cutSet.contains(vertex)) {
                        cutSet.add(vertex);
                    }
                }
                if (low > lowFordwardEdge) {
                    low = lowFordwardEdge; //Rule 3
                    nodes.get(vertex).lowNumberReacheable = low;
                }
            }
        }
        return low;
    }

    public void detectingCutEdges() { //Just in case, but i already check in on setingLowValues(v)...
    }

    public class johnNode {

        int number;
        int visitedNumber;
        int lowNumberReacheable = -1;

        public LinkedList<Integer> backEdges = new LinkedList<>();
        public LinkedList<Integer> forwardEdges = new LinkedList<>();

        public johnNode(int number) {
            this.number = number;
        }
    }
}
