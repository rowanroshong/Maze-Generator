import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Edge> edges;

    public Path(){
        ArrayList<Edge> edges = new ArrayList<>();
        setEdges(edges);
    }

    // Adds edge to path
    public void addEdge(Edge e){
        getEdges().add(e);
    }

    // Combines two paths that need to be combined
    public void combinePaths(Path p2){
        int size = p2.getEdges().size();

        for(int i = 0; i < size; i++){
            Edge e = p2.getEdges().get(i);
            Square s1 = e.getS1();
            Square s2 = e.getS2();

            s1.setPath(this);
            s2.setPath(this);
            edges.add(e);
        }
    }

    // Getter and Setter
    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
