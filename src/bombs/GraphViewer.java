 package bombs;

import bombs.Graph.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GraphViewer extends JFrame{
    
    Graph graph;
    
    private static final int SCREEN_WIDTH = 848, SCREEN_HEIGHT = 600, radius = 50;
    
    private int count;
    private boolean IsGraphReady = false;
    
    Image img;

    public GraphViewer(Graph graph){
        this.graph = graph;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setFocusable(true);
        requestFocusInWindow();
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        try {
            img = ImageIO.read(GraphViewer.class.getResource("/IMG/info.png"));
        } catch (IOException ex) {}
        repaint();
        OnMouseEvent();
        count = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(Color.yellow);
        
        g.drawImage(img, 0, 200, null);
        
        g.drawImage(img, 0, 0, 848, 600, null);
        
        graph.nodes.stream().filter((n) -> (n.isValid())).forEachOrdered((n) -> {
            if(graph.bomb() == n.getData()) g.setColor(Color.red);
            else g.setColor(Color.yellow);
            g.fillOval(n.getX()-radius/2, n.getY()-radius/2, radius, radius);
            if(graph.bomb() == n.getData()) g.setColor(Color.WHITE);
            else g.setColor(Color.BLUE);
            g.drawString(n.getData() + " : " + graph.pigeonValue(n.getData()), n.getX() - 10, n.getY() + 5);
        });
        if (IsGraphReady) {
            g.setColor(Color.PINK);
            for (int i = 0; i < graph.A.length; i++) {
                for (int j = 0; j < graph.A.length; j++) {
                    if (graph.A[i][j]) {
                        Node ni = graph.nodes.get(i), nf = graph.nodes.get(j);
                        g.drawLine(ni.getX(), ni.getY(), nf.getX(), nf.getY());
                    }
                }
            }
        }
    }
    
    private void OnMouseEvent(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (count < graph.nodes.size()) {
                    graph.nodes.get(count).setCoords(e.getX(), e.getY());
                    repaint();
                    count++;
                }
                if(count == graph.nodes.size() && !IsGraphReady){
                    IsGraphReady = true;
                    repaint();
                }
            }
         });
    }    
}
