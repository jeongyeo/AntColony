package antcolony;

import java.util.ArrayList;

public final class Pheromone {
    int x, y;
    int intensity;
    String directionToFood;
    
    Pheromone(int x, int y, String d, int i){
        this.x = x;
        this.y = y;
        this.intensity = i;
        this.directionToFood = inverseDirection(d);
    }
    
    public String inverseDirection(String d){
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
