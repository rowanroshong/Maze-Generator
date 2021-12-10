public class Square {
    private boolean n;
    private boolean s;
    private boolean w;
    private boolean e;
    private Path path;
    private int index;

    public Square (int index){
        setN(true);
        setS(true);
        setW(true);
        setE(true);
        setIndex(index);
    }

    // Set's the sides of the squares based on its new edges
    public void setSides(Square s2){
        int diff = s2.getIndex() - this.getIndex();

        if(diff == 1){
            this.setE(false);
            s2.setW(false);
        }else{
            this.setS(false);
            s2.setN(false);
        }
    }

    // returns a letter that corresponds to the
    public String toString(){
        if(!n){
            if(!e){
                if(!s){
                    if(!w){
                        return "a";
                    } else {
                        return "b";
                    }
                } else {
                    if(!w){
                        return "c";
                    } else {
                        return "d";
                    }
                }
            } else {
                if(!s){
                    if(!w){
                        return "e";
                    } else {
                        return "f";
                    }
                } else {
                    if(!w){
                        return "g";
                    } else {
                        return "h";
                    }
                }
            }
        } else {
            if(!e){
                if(!s){
                    if(!w){
                        return "i";
                    } else {
                        return "j";
                    }
                } else {
                    if(!w){
                        return "k";
                    } else {
                        return "l";
                    }
                }
            } else {
                if(!s){
                    if(!w){
                        return "m";
                    } else {
                        return "n";
                    }
                } else {
                    if(!w){
                        return "o";
                    } else {
                        return "p";
                    }
                }
            }
        }
    }

    // Getters and Setters
    public boolean getN() {
        return n;
    }

    public void setN(boolean n) {
        this.n = n;
    }

    public boolean getS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public boolean getW() {
        return w;
    }

    public void setW(boolean w) {
        this.w = w;
    }

    public boolean getE() {
        return e;
    }

    public void setE(boolean e) {
        this.e = e;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
