package antcolony;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class AntColony extends JFrame{
    //init variables
    BufferedImage up, upleft, upright, left, down , downleft, downright, right;
    static int windowWidth = 800;
    static int windowHeight = 800;
    static int animationSpeed = 0;
    int antLimit = 0;
    int foodLimit = 0;
    int foodCount = 0;
    int cellSize = 10;
    int xHome = 350;
    int yHome = 350;
    int antCount = 0;
    int rateOfPheromoneDepreciation = 1;
    ArrayList<Ant> antList = new ArrayList();
    ArrayList<Food> foodList = new ArrayList();
    ArrayList<Pheromone> pheromoneList = new ArrayList();
    static Random r = new Random();
    
    String[] upList = {"upleft", "upright", "up" };
    String[] downList = {"downleft", "downright", "down"};
    String[] leftList = {"upleft", "downleft", "left"};
    String[] rightList = {"upright", "downright", "right"};
    String[] uprightList = {"up", "right", "upright"};
    String[] upleftList = {"up", "left", "upleft"};
    String[] downrightList = {"down", "right", "downright",};
    String[] downleftList = {"down", "left", "downleft"};
        
    public void initPictures() throws IOException{
        up = ImageIO.read(new File("ant_up.png"));
        upleft = ImageIO.read(new File("ant_upleft.png"));
        upright = ImageIO.read(new File("ant_upright.png"));
        left = ImageIO.read(new File("ant_left.png"));
        down = ImageIO.read(new File("ant_down.png"));
        downleft = ImageIO.read(new File("ant_downleft.png"));
        downright = ImageIO.read(new File("ant_downright.png"));
        right = ImageIO.read(new File("ant_right.png"));
    }
    
    public int getGray(int i){
        if (i < 0)
            i = 0;
        else if (i > 100)
            i = 100;
        Double a = i/100.0*225;
        return a.intValue();
    }
        
    public void spawnFood(){
        int x = 0, y = 0;
        for (int i = 0; i <= foodLimit; i++) {
            foodCount++;
            if (cellSize == 10){
                x = (r.nextInt(cellSize*7)+5)*cellSize;
                y = (r.nextInt(cellSize*7)+5)*cellSize;
            }
            else if (cellSize == 15){
                x = (r.nextInt(cellSize*3)+2)*cellSize;
                y = (r.nextInt(cellSize*3)+2)*cellSize;
            }
            
            if (x != xHome && y != yHome)
                foodList.add(new Food(x, y));
            }
    }

    public void spawnAnt(){
        String[] list = {"up", "upleft", "upright", "down", "downleft", "downright", "left", "right"};
        antList.add(new Ant(xHome, yHome, list[r.nextInt(8)], "search"));
    }
    
    public void spawnPheromone(int x, int y, String d, int p){
        //if pheromone already exists, reset intensity to 0
        int a = 0;
        if (p > 100)
            a = 80;
        else if (p < 50)
            a = 0;
        else
            a = p;
        
        for (int i = 0; i < pheromoneList.size(); i++) {
            if (pheromoneList.get(i).x == x && pheromoneList.get(i).y == y){
                    pheromoneList.get(i).intensity = a;
                break;
            }
        }
        //else just add new pheromone
        pheromoneList.add(new Pheromone(x, y, d, a));
    }
    
    public void pheromoneDepreciation(){
        for (int i = 0; i < pheromoneList.size(); i++) {
            if (pheromoneList.get(i).intensity >= 100){
                pheromoneList.remove(i);
            }
            else
                pheromoneList.get(i).intensity += rateOfPheromoneDepreciation;
        }
    }
    public String checkForFood(String d, int x, int y){
        String[] list = null;
        switch(d){
            case "up":
                list = upList;
                break;
            case "upleft":
                list = upleftList;
                break;
            case "upright":
                list = uprightList;
                break;
            case "down":
                list = downList;
                break;
            case "downleft":
                list = downleftList;
                break;
            case "downright":
                list = downrightList;
                break;
            case "left":
                list = leftList;
                break;
            case "right":
                list = rightList;
                break;
        }
        
        for (int i = 0; i < 3; i++) {
            switch(list[i]){
                case "up":
                    y -= cellSize;
                    break;
                case "upright":
                    y -= cellSize;
                    x += cellSize;
                    break;
                case "upleft":
                    y -= cellSize;
                    x -= cellSize;
                    break;
                case "down":
                    y += cellSize;
                    break;
                case "downright":
                    y += cellSize;
                    x += cellSize;
                    break;
                case "downleft":
                    y += cellSize;
                    x -= cellSize;
                    break;
                case "left":
                    x -= cellSize;
                    break;
                case "right":
                    x += cellSize;
                    break;
            }
            for (int j = 0; j < foodList.size(); j++) {
                if (foodList.get(i).x == x && foodList.get(i).y == y)
                    return list[i];
            }    
        }
        return d;
    }
    
    public void calcNextGen(){
        //spawn new ant
        if (antCount < antLimit){
            antCount++;
            spawnAnt();
        }
        //spawn food if food is gone
        if (foodCount < 2){
            spawnFood();
        }
        //evaporate or depreciate pheromone
        if (pheromoneList.size() > 1)
            pheromoneDepreciation();
        
        for (int i = 0; i < antList.size(); i++) {
            
            if ("search".equals(antList.get(i).mode)){
                //save path
                antList.get(i).savePath(antList.get(i).x, antList.get(i).y, antList.get(i).direction);
                
                //pheromone checker.
                for (int j = 0; j < pheromoneList.size(); j++) {
                    if (antList.get(i).x == pheromoneList.get(j).x && antList.get(i).y == pheromoneList.get(j).y){
                        antList.get(i).direction = pheromoneList.get(j).directionToFood;
                        antList.get(i).mode = "pheromone";
                    }
                }
                
                if ("search".equals(antList.get(i).mode)){
                    switch (antList.get(i).direction) {
                        case "up":
                            antList.get(i).y = antList.get(i).y - cellSize;
                            antList.get(i).direction = upList[r.nextInt(3)];
                            break;
                        case "upright":
                            antList.get(i).y = antList.get(i).y - cellSize;
                            antList.get(i).x = antList.get(i).x + cellSize;
                            antList.get(i).direction = uprightList[r.nextInt(3)];
                            break;
                        case "upleft":
                            antList.get(i).y = antList.get(i).y - cellSize;
                            antList.get(i).x = antList.get(i).x - cellSize;
                            antList.get(i).direction = upleftList[r.nextInt(3)];
                            break;
                        case "down":
                            antList.get(i).y = antList.get(i).y + cellSize;
                            antList.get(i).direction = downList[r.nextInt(3)];
                            break;
                        case "downright":
                            antList.get(i).y = antList.get(i).y + cellSize;
                            antList.get(i).x = antList.get(i).x + cellSize;
                            antList.get(i).direction = downrightList[r.nextInt(3)];
                            break;
                        case "downleft":
                            antList.get(i).y = antList.get(i).y + cellSize;
                            antList.get(i).x = antList.get(i).x - cellSize;
                            antList.get(i).direction = downleftList[r.nextInt(3)];
                            break;
                        case "left":
                            antList.get(i).x = antList.get(i).x - cellSize;
                            antList.get(i).direction = leftList[r.nextInt(3)];
                            break;
                        case "right":
                            antList.get(i).x = antList.get(i).x + cellSize;
                            antList.get(i).direction = rightList[r.nextInt(3)];
                            break;
                    }
                }
                
                //will follow strongest pheromone. else has 10% chance to wonder off.
                else if ("pheromone".equals(antList.get(i).mode)){
                    antList.get(i).direction = checkForFood(antList.get(i).direction, antList.get(i).x, antList.get(i).y);
                    int a = r.nextInt(100);
                    switch (antList.get(i).direction) {
                        case "up":
                            if (a > 10){
                                antList.get(i).y = antList.get(i).y - cellSize;
                            }
                            else{
                                antList.get(i).direction = upList[r.nextInt(2)];
                            }
                            break;
                        case "upright":
                            if (a > 10){
                                antList.get(i).y = antList.get(i).y - cellSize;
                                antList.get(i).x = antList.get(i).x + cellSize;   
                            }
                            else{
                                antList.get(i).direction = uprightList[r.nextInt(2)];
                            }
                            break;
                        case "upleft":
                            if (a > 10){
                                antList.get(i).y = antList.get(i).y - cellSize;
                                antList.get(i).x = antList.get(i).x - cellSize;
                            }
                            else{
                                antList.get(i).direction = upleftList[r.nextInt(2)];
                            }
                            break;
                        case "down":
                            if (a > 10)
                                antList.get(i).y = antList.get(i).y + cellSize;
                            else{
                                antList.get(i).direction = downList[r.nextInt(2)];
                            }
                            break;
                        case "downright":
                            if (a > 10){
                                antList.get(i).y = antList.get(i).y + cellSize;
                                antList.get(i).x = antList.get(i).x + cellSize;
                            }
                            else{
                                antList.get(i).direction = downrightList[r.nextInt(2)];
                            }
                            break;
                        case "downleft":
                            if (a > 10){
                                antList.get(i).y = antList.get(i).y + cellSize;
                                antList.get(i).x = antList.get(i).x - cellSize;
                            }
                            else{
                                antList.get(i).direction = downleftList[r.nextInt(2)];
                            }
                            break;
                        case "left":
                            if (a > 10)
                                antList.get(i).x = antList.get(i).x - cellSize;
                            else{
                                antList.get(i).direction = leftList[r.nextInt(2)];
                            }
                            break;
                        case "right":
                            if (a > 10)
                                antList.get(i).x = antList.get(i).x + cellSize;
                            else{
                                antList.get(i).direction = rightList[r.nextInt(2)];
                            }
                    }
                    antList.get(i).mode = "search";
                }

                //if ant finds food, change mode to return
                for (int k = 0; k < foodList.size(); k++) {
                    if (antList.get(i).x == foodList.get(k).x && antList.get(i).y == foodList.get(k).y){
                        antList.get(i).mode = "return";
                        if (foodList.get(k).level == 1){
                            foodList.remove(k);
                            foodCount--;
                        }
                        else
                            foodList.get(k).level--;
                    }
                }
                //if ant goes out of screen, delete
                if(antList.get(i).x < 0 || antList.get(i).x > windowWidth || antList.get(i).y < 20 || antList.get(i).y > windowHeight){
                    antList.remove(i);
                    antCount--;
                }
            }
            else if ("return".equals(antList.get(i).mode)){
                if (antList.get(i).walkedPath_x.size() == 1){
                    antList.remove(i);
                    antCount--;
                }
                else{
                    antList.get(i).x = antList.get(i).returnPath_x();
                    antList.get(i).y = antList.get(i).returnPath_y();
                    antList.get(i).direction = antList.get(i).returnDirection();
                    //create pheromone
                    spawnPheromone(antList.get(i).x, antList.get(i).y, antList.get(i).direction, antList.get(i).lengthWalked);
                }
            }
        }
    }
    
    @Override
    public void paint(Graphics g){
        Image img = getImg();
        g.drawImage(img, 0, 0, rootPane);    
    }
    
    public Image getImg(){
        BufferedImage bi = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, windowWidth, windowHeight); 
        
        //draw pheromone
        for (int i = 0; i < pheromoneList.size(); i++) {
            int num = getGray(pheromoneList.get(i).intensity);
            g2.setColor(new Color(num, num, num));
            g2.fillOval(pheromoneList.get(i).x+1, pheromoneList.get(i).y+1, cellSize-3, cellSize-3);
        }
        
        //draw foods
        for (int i = 0; i < foodList.size(); i++) {
            g2.setColor(Color.red);
            g2.fillOval(foodList.get(i).x, foodList.get(i).y, cellSize, cellSize);
        }
        //draw ant
        for (int i = 0; i < antList.size(); i++) {
            switch (antList.get(i).direction) {
                case "up":
                    g2.drawImage(up, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "upright":
                    g2.drawImage(upright, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "upleft":
                    g2.drawImage(upleft, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "down":
                    g2.drawImage(down, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "downright":
                    g2.drawImage(downright, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "downleft":
                    g2.drawImage(downleft, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "left":
                    g2.drawImage(left, antList.get(i).x, antList.get(i).y, this);
                    break;
                case "right":
                    g2.drawImage(right, antList.get(i).x, antList.get(i).y, this);
                    break;
            }
            //draw ant hill
            g2.setColor( new Color(160, 82, 45) );
            g2.fillRect(xHome, yHome, cellSize, cellSize);
        }
        return bi;
    }
    
    public void sleep( int numMilliseconds ){
        try {
            Thread.sleep( numMilliseconds );
        } 
        catch (Exception e) {
        }
    }
    
    public void initWindow(){
        setTitle("Ant Colony Simulator");
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.black);
        setVisible(true);
    }
    
    public void getInput(){
        Scanner s = new Scanner(System.in);
        while (true){
            try{
                System.out.print("Enter the animation speed from 10ms to 500ms *Recommended 50ms* : ");
                animationSpeed = s.nextInt();
                if (animationSpeed >= 10 && animationSpeed <= 500)
                    break;
            }
            catch(InputMismatchException e){
                s.next();
            }
        }
        while (true){
            try{
                System.out.print("Enter the number of maximum ants from 1 to 50: ");
                antLimit = s.nextInt();
                if (antLimit >= 1 && antLimit <= 50)
                    break;
            }
            catch(InputMismatchException e){
                s.next();
            }
        }
        while (true){
            try{
                System.out.print("Enter the number of food to be spawned from 1 to 10: ");
                foodLimit = s.nextInt();
                if (foodLimit >= 1 && foodLimit <= 10)
                    break;
            }
            catch(InputMismatchException e){
                s.next();
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
        AntColony window = new AntColony();
        window.initPictures();
        window.getInput();
        window.initWindow();
        
        while(true){
            window.calcNextGen();
            window.sleep(animationSpeed);
            window.repaint();
        }
    }
}
