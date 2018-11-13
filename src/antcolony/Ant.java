package antcolony;

import java.util.ArrayList;

public class Ant {
    int x, y;
    String direction;
    String mode;
    int lengthWalked;
    ArrayList<Integer> walkedPath_x = new ArrayList();
    ArrayList<Integer> walkedPath_y = new ArrayList();
    ArrayList<String> walkedPath_direction = new ArrayList();
    
    Ant(int x, int y, String d, String m){
        this.x = x;
        this.y = y;
        this.direction = d;
        this.mode = m;
    }
    
    public void savePath(int x, int y, String d){
        walkedPath_x.add(x);
        walkedPath_y.add(y);
        walkedPath_direction.add(d);
        lengthWalked++;
    }
    
    public int returnPath_x(){            
        int x = walkedPath_x.get(walkedPath_x.size()-1);
        walkedPath_x.remove(walkedPath_x.size()-1);
        return x;
    }
    
    public int returnPath_y(){
        int y = walkedPath_y.get(walkedPath_y.size()-1);
        walkedPath_y.remove(walkedPath_y.size()-1);
        return y;
    }
    
    
    public String returnDirection(){
        String d = walkedPath_direction.get(walkedPath_direction.size()-1);
        walkedPath_direction.remove(walkedPath_direction.size()-1);
        switch (d) {
            case "up":
                return "down";
            case "down":
                return "up";
            case "left":
                return "right";
            case "right":
                return "left";
            case "upleft":
                return "downright";
            case "upright":
                return "downleft";
            case "downleft":
                return "upright";
            case "downright":
                return "upleft";
        }
        return null;
    }
}
