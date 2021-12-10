public class Edge {
    private Square s1; // Square 1 along edge
    private Square s2; // Square 2 along edge

    public Edge(Square s1, Square s2){
        setS1(s1);
        setS2(s2);
    }


    // Getters and Setters
    public Square getS1() {
        return s1;
    }

    public void setS1(Square s1) {
        this.s1 = s1;
    }

    public Square getS2() {
        return s2;
    }

    public void setS2(Square s2) {
        this.s2 = s2;
    }
}