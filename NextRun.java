import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//import javax.imageio.*;
//import java.io.*;
import java.awt.event.KeyEvent;
//import java.util.Iterator;
import java.util.*;
import javax.swing.Timer;

public class NextRun {
    public static void main(String[] args) {
        JFrame j = new JFrame();
        HyPanel m = new HyPanel();
        j.setSize(m.getSize());
        j.add(m);

        //j.addKeyListener(m);
        j.addMouseListener(m);
        j.addKeyListener(m);
        j.setVisible(true);

        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class C {
    public static final int IN_GAME = 1;
    public static final int IN_MENU = 0;
    public static final int[] but1size = {600,600,240,50};
    public static final int[] but2size = {120,360,300,50};
    
    public static final int size = 9; //Size of ship
    public static final int speed = 5; //Movement speed
    public static final double turnSpe = Math.PI/120; //Turn speed

    public static final Color bColor = Color.WHITE; //Bullet color
    public static final Color hColor = Color.GREEN; //Health bar color
    public static final Color menuColor = new Color(15,114,31); //Color of menu things

    public static final Font standardFont = new Font("Academy Engraved LET",Font.PLAIN,28);
    public static final int[] gameOverCoords = {660,580,120,30}; //Location of button
    public static final int buff = 5; //Distance from edges to text
    public static final int[] startGameCoords = {110,230,68,30};
    public static final int[] menuCoords = {670,660,100,30};
    public static final int[] contCoords = {110,290,110,30};
    public static final int[] backCoords = {50,833,70,30};
    public static final int[] butSize = {74,72};
}
class B1 {
    //Basic shot variables
    public static final int size = 2; //Size of shot type 1
    public static final double dmg = .15; //Damage of shot type 1
    public static final int spe = 18; //Speed of shot type 1
    public static final int iFr = 2; //Time before shot type 1 can hit shooter
    public static final int cd = 1; //Shot type 1 delay
    public static final double spread = 1.5*Math.PI/9; //Spread of shot type 1
    public static final int life = 1700/18+1; //Time until shot type 1 depsawns
}
class B2 {
    //Basic spread shot values
    public static final int size = 15; //b2 is simple spread shot
    public static final double dmg = 5;
    public static final int spe = 16;
    public static final int iFr = 5;
    public static final int cd = 120;
    public static final int dur = 60; //Duration that shot type 2 is active for
    public static final double spread = 4.4*Math.PI/90; //Width of attack (degrees)
    public static final int shots = 4; //Number of times fired per attack-1
    public static final int life = 1700/13+1;
}
class B3 {
    //Basic curve shot values
    public static final int size = 12; //b3 is basic curve shot
    public static final double dmg = 4; 
    public static final int spe = 11;
    public static final int iFr = 8;
    public static final int cd = 210;
    public static final int dur = 100;
    public static final double rot = Math.PI/120; //Turn speed of  bullet path
    public static final int shots = 5; 
    public static final int life = 400;
}
class B4 {
    public static final int size = 19; //b4 is lane shot
    public static final double dmg = 0.3;
    public static final int spe = 26;
    public static final int iFr = 0;
    public static final int cd = 560;
    public static final int dur = 180;
    public static final double spread = 190; //Width of attack (pixels)
    public static final int life = 1700/20+1;
}
class B5 {
    public static final int size = 16; //b5 is alternate spread shot
    public static final double dmg = 3.2;
    public static final int spe = 16;
    public static final int iFr = 7;
    public static final int cd = 210;
    public static final int dur = 90;
    public static final double spread = 1.8*Math.PI/90;
    public static final int shots = 5;
    public static final int life = 1700/11+1;
}
class B6 {
    public static final int size = 9; //b6 is alternate curve shot
    public static final double dmg = 0.5; //Close range damage
    public static final double dmgb = 2.1; //Long range damage
    public static final int spe = 12;
    public static final int iFr = 12;
    public static final int cd = 170;
    public static final int dur = 9;
    public static final double rot = Math.PI/225; //Close range curve
    public static final double rotb = 2*Math.PI/225; //Long range curve
    public static final double tilt = Math.PI/3; //Angle at which bullets are fired
    public static final int bullets = 18; //Number of bullets in one burst
    public static final double speFac = 1.5; //How much bullets speed up after burst
    public static final int burst = 40; //How long bullets remain in close range mode
    public static final int shots = 6;
    public static final int life = 300;
}
class B7 {
    public static final int size = 10; //helix laser
    public static final double dmg = 4;
    public static final int spe = 15;
    public static final int iFr = 3;
    public static final int cd = 600;
    public static final int dur = 156;
    public static final int life = 200;
    public static final double rot = Math.PI/1360; //Change of rotation speed of bullets
    public static final double initAng = Math.PI/11.2; //First angle that bullets are fired at
    public static final int width = 60; //Width of laser
    public static final int shots = 4; //Special case, frames per shot
}

class HyPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
    private Timer time;
    private Ship winner;
    private HashSet<Integer> pressedKeys; //Buttons pressed
    private ArrayList<Bullet> bullets; //Bullets on screen
    private HashSet<Button> buttons; //All buttons. Wonder why.
    private boolean flag; //If bullet should be despawned
    private Ship s1,s2;
    Button endGame; //Restarts game
    Button startGame; //Begins game
    Button menu; //Back to main menu
    Button controls;
    Button back; //Back to main menu from controls
    JButton somebutton;
    private JButton[] controlButs;
    //private JButton b1left,b1right,b1up,b1down,b2left,b2right,b2up,b2sown,b1slow,b2slow,b1turnL,b1turnR,b2turnL,b2turnR;
    //private JButton b1b2,b1b3,b1b4,b2b2,b2b3,b2b4,b1tog,b2tog;
    private int changing; //Which keybind is being changed
    private int gameState; //Menu, in game, or game over
        //List of all controls. I hate doing this.
    //Up, down, left, right, turn (left then right), toggle, slow, attacks 2-4 in that order
    private int s1control[];
    private int s2control[];

    HyPanel() {
        pressedKeys = new HashSet<Integer>();
        time = new Timer(10,this);
        s1 = new Ship(140,700,C.size,Color.BLUE,0,1);
        s2 = new Ship(1300,200,C.size,Color.RED,Math.PI,2);

        setSize(1440, 900);
        time.start();
        s1.spawn();
        s2.spawn();
        bullets = new ArrayList<Bullet>();
        buttons = new HashSet<Button>();
        gameState = 0;
        somebutton = new JButton("your mother");
        somebutton.addActionListener(this);
        somebutton.setActionCommand("action");
        somebutton.setLayout(null);
        somebutton.setBounds(0, 0, 50, 50);
        setLayout(null);  
        this.add(somebutton);
        this.remove(somebutton);
        somebutton.setEnabled(false);


        endGame = new Button(C.gameOverCoords,Color.BLUE,"Restart?",C.standardFont,2,1);
        startGame = new Button(C.startGameCoords,C.menuColor,"Start",C.standardFont,0,1);
        menu = new Button(C.menuCoords,Color.BLUE,"Menu",C.standardFont,2,0);
        controls = new Button(C.contCoords,C.menuColor,"Controls",C.standardFont,0,3);
        back = new Button(C.backCoords,C.menuColor,"Back",C.standardFont,3,0);

        s1control = new int[11];
        s2control = new int[11];
        controlButs = new JButton[22];
        s1control[0]=KeyEvent.VK_W;
        s1control[1]=KeyEvent.VK_S;
        s1control[2]=KeyEvent.VK_A;
        s1control[3]=KeyEvent.VK_D;
        s1control[4]=KeyEvent.VK_F;
        s1control[5]=KeyEvent.VK_G;
        s1control[6]=KeyEvent.VK_Q;
        s1control[7]=KeyEvent.VK_C;
        s1control[8]=KeyEvent.VK_R;
        s1control[9]=KeyEvent.VK_T;
        s1control[10]=KeyEvent.VK_V;
        s2control[0]=KeyEvent.VK_I;
        s2control[1]=KeyEvent.VK_K;
        s2control[2]=KeyEvent.VK_J;
        s2control[3]=KeyEvent.VK_L;
        s2control[4]=KeyEvent.VK_SEMICOLON;
        s2control[5]=KeyEvent.VK_QUOTE;
        s2control[6]=KeyEvent.VK_U;
        s2control[7]=KeyEvent.VK_PERIOD;
        s2control[8]=KeyEvent.VK_P;
        s2control[9]=KeyEvent.VK_OPEN_BRACKET;
        s2control[10]=KeyEvent.VK_SLASH;
        for(int x=0; x<22; x++) {
            if(x<11) {
                controlButs[x] = new JButton((char)s1control[x]+"");
            } else {
                controlButs[x] = new JButton((char)s2control[x-11]+"");
            }
        }
        controlButs[0].setBounds(667,144,C.butSize[0],C.butSize[1]);
        controlButs[1].setBounds(667,226,C.butSize[0],C.butSize[1]);
        controlButs[2].setBounds(584,226,C.butSize[0],C.butSize[1]);
        controlButs[3].setBounds(750,226,C.butSize[0],C.butSize[1]);
        controlButs[4].setBounds(623,486,C.butSize[0],C.butSize[1]);
        controlButs[5].setBounds(708,486,C.butSize[0],C.butSize[1]);
        controlButs[6].setBounds(667,771,C.butSize[0],C.butSize[1]);
        controlButs[7].setBounds(667,339,C.butSize[0],C.butSize[1]);
        controlButs[8].setBounds(584,626,C.butSize[0],C.butSize[1]);
        controlButs[9].setBounds(667,626,C.butSize[0],C.butSize[1]);
        controlButs[10].setBounds(750,626,C.butSize[0],C.butSize[1]);
        controlButs[11].setBounds(1218,144,C.butSize[0],C.butSize[1]);
        controlButs[12].setBounds(1218,226,C.butSize[0],C.butSize[1]);
        controlButs[13].setBounds(1138,226,C.butSize[0],C.butSize[1]);
        controlButs[14].setBounds(1300,226,C.butSize[0],C.butSize[1]);
        controlButs[15].setBounds(1176,486,C.butSize[0],C.butSize[1]);
        controlButs[16].setBounds(1260,486,C.butSize[0],C.butSize[1]);
        controlButs[17].setBounds(1218,771,C.butSize[0],C.butSize[1]);
        controlButs[18].setBounds(1218,339,C.butSize[0],C.butSize[1]);
        controlButs[19].setBounds(1138,626,C.butSize[0],C.butSize[1]);
        controlButs[20].setBounds(1218,626,C.butSize[0],C.butSize[1]);
        controlButs[21].setBounds(1300,626,C.butSize[0],C.butSize[1]);
        for(int x=0; x<22; x++) {
            controlButs[x].setBackground(new Color(163,94,47));
            controlButs[x].setForeground(new Color(79,173,214));
            controlButs[x].setOpaque(true);
            controlButs[x].setFont(new Font("InaiMathi",Font.PLAIN,66));
            controlButs[x].addActionListener(this);
            controlButs[x].setActionCommand("SetCont"+x);
            this.add(controlButs[x]);
            controlButs[x].setVisible(false);
            controlButs[x].setFocusable(false);
        }
        revalidate();
        buttons.add(endGame);
        buttons.add(startGame);
        buttons.add(menu);
        buttons.add(controls); 
        buttons.add(back);
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        if(gameState==0) {
            g.setFont(new Font("Bernard MT Condensed",Font.ITALIC,96));
            g.setColor(new Color(12,17,41));
            g.fillRect(0,0,1440,900);
            g.setColor(C.menuColor);
            g.drawString("NextRun",70,128);
        } else if(gameState==1) {
            g.setColor(Color.BLACK); 
            g.fillRect(0,0,1440,900);
            g.setColor(C.hColor);
            g.fillRect(40,40,220,70);
            g.fillRect(1180,40,220,70);
            g.setColor(Color.BLACK);
            g.fillRect(45,45,210,60);
            g.fillRect(1185,45,210,60);
            g.setColor(C.hColor);
            if(s1.getHP()>0) {
                g.fillRect(50,50,(int) (2*s1.getHP()),50);
            }
            if(s2.getHP()>0) {
                g.fillRect(1190,50,(int) (2*s2.getHP()),50);
            }
            g.setColor(new Color(0,210,225));
            if(s1.checkAlt()) {
                g.fillRect(280,60,30,30);
            } else {
                g.drawRect(280,60,30,30);
            }
            if(s2.checkAlt()) {
                g.fillRect(1130,60,30,30);
            } else {
                g.drawRect(1130,60,30,30);
            }
            Iterator<Bullet> f = bullets.iterator();
            while(f.hasNext()) {
                f.next().show(g);
            }
            s1.show(g);
            s2.show(g);
            return;
        } else if(gameState==2) {
            g.setColor(new Color(25,6,30)); 
            g.fillRect(0,0,1440,900);
            g.setColor(C.hColor);
            g.fillRect(40,40,220,70);
            g.fillRect(1180,40,220,70);
            g.setColor(Color.BLACK);
            g.fillRect(45,45,210,60);
            g.fillRect(1185,45,210,60);
            g.setColor(C.hColor);
            if(s1.getHP()>0) {
                g.fillRect(50,50,(int) (2*s1.getHP()),50);
            }
            if(s2.getHP()>0) {
                g.fillRect(1190,50,(int) (2*s2.getHP()),50);
            }
            if(winner.equals(s1)) {
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial",Font.BOLD,60));
                g.drawString("Player 1 wins!", 520, 480);
            } else if(winner.equals(s2)) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial",Font.BOLD,60));
                g.drawString("Player 2 wins!",520,480);
            }
        } else if(gameState==3) {
            g.setColor(new Color(56,57,135));
            g.fillRect(0,0,1440,900);
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Avenir Next Condensed",Font.ITALIC,84));
            g.drawString("Controls:",35,89);
            g.setFont(new Font("Avenir Next Condensed",Font.PLAIN,27));
            g.drawString("Movement:",34,226);
            g.drawString("Slow down:",33,371);
            g.drawString("Turn:",32,514);
            g.drawString("Attacks:",32,658);
            g.drawString("Toggle modes:",32,802);
            g.setColor(new Color(0,176,240));
            g.drawString("Player 1:",665,87);
            g.setColor(new Color(234,51,35));
            g.drawString("Player 2:",1210,87);
            g.setColor(new Color(245,194,66));
            g.drawLine(957,11,957,889);
        } else if(gameState==5) {
            g.setColor(new Color(194,214,236));
            g.fillRect(245,311,950,278);
            g.setColor(new Color(79,113,190));
            g.fillRect(247,313,946,274);
            g.setColor(new Color(208,20,61));
            g.setFont(new Font("Avenir Next Condensed",Font.PLAIN,68));
            g.drawString("Select new button:",430,446);
        }
        Iterator<Button> ggg = buttons.iterator();
        while(ggg.hasNext()) {
            ggg.next().show(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        /*if (e.getActionCommand().equals("action")) {
            System.out.println("big L");
        }*/
        try{
            if(e.getActionCommand().length()>7) {
                if(e.getActionCommand().substring(0,7).equals("SetCont")) {
                    changing=Integer.parseInt(e.getActionCommand().substring(7));
                    setGameState(5);
                }
            }
        } catch(Exception tt) {}
        if(gameState==0) {
            //WHYYYYYYYYYYY
        } else if(gameState==1) {
            boolean reset=false;
            keyChekr();
            s1.update(bullets);
            s2.update(bullets);
            Iterator<Bullet> g = bullets.iterator();
            while(g.hasNext()) {
                flag=false;
                Bullet x = g.next();
                if(x.update()) {
                    flag=true;
                }
                if(x.overlaps(s1)) {
                    flag=x.hit(s1);
                    if(s1.getHP()<=0) {
                        s1.die();
                        reset=true;
                        winner = s2;
                    }
                } 
                if(x.overlaps(s2)) {
                    flag=x.hit(s2);
                    if(s2.getHP()<=0) {
                        s2.die();
                        reset=true;
                        winner = s1;
                    }
                }
                if(flag) {
                    g.remove();
                }
            }
            if(reset) {
                setGameState(2);
            }
        }
        //System.out.println("Ship 1: "+s1.toString());
        repaint();
        return;
    }

    public void keyPressed(KeyEvent e) {
        int x = e.getKeyCode();
        pressedKeys.add(x);
        if(x==s1control[6]) {
            s1.toggle();
        }
        if(x==s2control[6]) {
            s2.toggle();
        }
        if(gameState==5) {
            if(changing<11) {
                s1control[changing]=x;
                controlButs[changing].setText(""+(char)s1control[changing]);
            } else {
                s2control[changing-11]=x;
                controlButs[changing].setText(""+(char)s2control[changing-11]);
            }
            setGameState(3);
        }
        return;
    }   
    public void keyChekr() {
        for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
            /*
            switch (it.next()) {
                case KeyEvent.VK_A:
                    s1.move(-C.speed,0);
                    break;
                case KeyEvent.VK_W:
                    s1.move(0,-C.speed);
                    break;
                case KeyEvent.VK_S:
                    s1.move(0,C.speed);
                    break;
                case KeyEvent.VK_D:
                    s1.move(C.speed,0);
                    break;
                case KeyEvent.VK_F:
                    s1.turn(7); //Updated method makes parameter pointless
                    break;
                case KeyEvent.VK_G: 
                    s1.turn(-7);
                    break;
                case KeyEvent.VK_SEMICOLON:
                    s2.turn(7);
                    break;
                case KeyEvent.VK_QUOTE:
                    s2.turn(-7);
                    break;
                case KeyEvent.VK_J:
                    s2.move(-C.speed,0);
                    break;
                case KeyEvent.VK_L:
                    s2.move(C.speed,0);
                    break;
                case KeyEvent.VK_I:
                    s2.move(0,-C.speed);
                    break;
                case KeyEvent.VK_K:
                    s2.move(0,C.speed);
                    break;
                case KeyEvent.VK_V:
                    s1.setSlow(true);
                    break;
                case KeyEvent.VK_SLASH:
                    s2.setSlow(true);
                    break;
                case KeyEvent.VK_R: 
                    s1.shot2();
                    break;
                case KeyEvent.VK_P: 
                    s2.shot2();
                    break;
                case KeyEvent.VK_T: 
                    s1.shot3();
                    break;
                case KeyEvent.VK_OPEN_BRACKET: 
                    s2.shot3();
                    break;
                case KeyEvent.VK_C: 
                    s1.shot4();
                    break;
                case KeyEvent.VK_PERIOD: 
                    s2.shot4();
                    break;
            }
            */
            int e = it.next();
            if(e==s1control[2]) {
                s1.move(-C.speed,0);
            } else if(e==s1control[0]) {
                s1.move(0,-C.speed);
            } else if(e==s1control[1]) {
                s1.move(0,C.speed);
            } else if(e==s1control[3]) {
                s1.move(C.speed,0);
            } else if(e==s2control[2]) {
                s2.move(-C.speed,0);
            } else if(e==s2control[0]) {
                s2.move(0,-C.speed);
            } else if(e==s2control[1]) {
                s2.move(0,C.speed); 
            } else if(e==s2control[3]) {
                s2.move(C.speed,0);
            } else if(e==s1control[7]) {
                s1.setSlow(true);
            } else if(e==s2control[7]) {
                s2.setSlow(true);
            } else if(e==s1control[4]) {
                s1.turn(7);
            } else if(e==s1control[5]) {
                s1.turn(-7);
            } else if(e==s2control[4]) {
                s2.turn(7);
            } else if(e==s2control[5]) {
                s2.turn(-7);
            } else if(e==s1control[8]) {
                s1.shot2();
            } else if(e==s1control[9]) {
                s1.shot3();
            } else if(e==s1control[10]) {
                s1.shot4();
            } else if(e==s2control[8]) {
                s2.shot2();
            } else if(e==s2control[9]) {
                s2.shot3();
            } else if(e==s2control[10]) {
                s2.shot4();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        return;
    }
    
    public void keyReleased(KeyEvent e) {
        int x = e.getKeyCode();
        if(x==s1control[7]) {
            s1.setSlow(false);
        }
        if(x==s2control[7]) {
            s2.setSlow(false);
        }
        pressedKeys.remove(x);
        return;
    }

    public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        Iterator<Button> g = buttons.iterator();
        while(g.hasNext()) {
            Button f = g.next();
            if(f.clicked(x,y)>-1) {
                setGameState(f.getEndState());
            }
        }
        return;
    }

    public void setGameState(int n) {
        if(gameState==3) {
            if(n!=3) {
                for(int x=0; x<22; x++) {
                    controlButs[x].setVisible(false);
                    //this.remove(controlButs[x]);
                }
            }
        }
        gameState = n;
        Iterator<Button> g = buttons.iterator();
        while(g.hasNext()) {
            Button f = g.next();
            if(f.getState()==n) {
                f.setActive();
            } else {
                f.setInactive();
            }
        }
        if(n==0) {
        } else if(n==1) {
            s1.spawn();
            s2.spawn();
        } else if(n==2) {
            Iterator<Bullet> gg = bullets.iterator();
            while(gg.hasNext()) {
                gg.next();
                gg.remove();
            }
            //Something?
        } else if(n==3) {
            for(int x=0; x<22; x++) {
                //this.add(controlButs[x]);
                controlButs[x].setVisible(true);
            }
        } else if(n==4) {
            return;
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

class Button {
    private Rectangle hh; //Area of button
    private boolean active; //Can be clicked on
    private String text; //Message on button
    private Font f; //I wonder.
    private Color c; //I wonder.
    private int activeState; //Game state during which button is active
    private int endState; //State that button changes game to

    public Button(int[] coords,Color cc,String message,Font font, int s, int e) {
        if(s==0) {
            active = true;
        } else {
            active = false;
        }
        c=cc;
        hh = new Rectangle(coords[0],coords[1],coords[2],coords[3]);
        text=message;
        f=font;
        activeState=s;
        endState = e;
    }

    public void setInactive() {
        active=false;
    }
    public void setActive() {
        active=true;
    }

    public int clicked(int x,int y) {
        //System.out.println(hh.contains(x,y)+" "+x+" "+y);
        if(active && hh.contains(x,y)) {
            return endState;
        } else {
            return -1;
        }
    }

    public void show(Graphics g) {
        if(active) {
            g.setColor(c);
            g.setFont(f);
            g.drawRect((int)hh.getX(),(int)hh.getY(),(int)hh.getWidth(),(int)hh.getHeight());
            g.drawString(text,(int) hh.getX()+C.buff,(int) (hh.getY()+hh.getHeight())-C.buff);
        }
    }
    public int getState() {
        return activeState;
    }
    public int getEndState() {
        return endState;
    }
}

class HBox {
    private int x,y,r;
    //First value is x coordinate, second value is y coordinate, and third value is radius/size
    private Color c; //Guess.

    public HBox(int x,int y,int r,Color c) {
        this.c=c;
        this.x=x;
        this.y=y;
        this.r=r;
    }

    public boolean overlaps(HBox ot) {
        return r+ot.r>(int)Math.sqrt(Math.pow(x-ot.x,2) + Math.pow(y-ot.y,2));
    }

    public void move(int x1,int y1) {
        x+=x1;
        y+=y1;
    }

    public void show(Graphics g) {
        g.setColor(c);
        g.fillOval(x-r,y-r,2*r,2*r);
    }

    public int getX() {
        return x;
    }
    public void setX(int xx) {
        x=xx;
    }
    public int getY() {
        return y;
    }
    public void setY(int yy) {
        y=yy;
    }
    public Color getColor() {
        return c;
    }
    public int getR() {
        return r;
    }
}

class Ship extends HBox {
    private double hp; //Health
    private boolean spawned,slow; //Only active during round, active when holding the slow move button
    private double angle; //Aiming
    private double startAngle; //Angle at spawn
    private int[] spawnCoords; //Spawn location
    private int b1cooldown; //Cooldown for basic shot type
    private int b2cooldown; //Cooldown for simple spread shot and alternate spread shot (shared cooldown)
    private int b3cooldown; //Cooldown for simple curve shot and alternate curve shot
    private int b4cooldown; //I hope you get it now. Lane shot.
    private int b2dur; //Timings for simple spread shot
    private int b3dur; //"         " simple curve shot
    private int b4dur; //Again, hope you get it now. Lane shot.
    private int b5dur; //Alternate spread shot. Separate timer in case shot mode is switched mid attack
    private int b6dur; //Alternate curve shot
    private int b7dur; //Helix laser
    private double laserAng;
    private int savedX1,savedX2,savedY1,savedY2; //Positions where lane shot fired from
    private double savedAng; //Direction lane shot fires in
    private boolean altMode; //Alternate shot types
    private int region; //Half of map

    public Ship(int x,int y,int r,Color c,double a,int reg) {
        super(x,y,r,c);
        spawnCoords = new int[2];
        spawnCoords[0]=x;
        spawnCoords[1]=y;
        hp=100;
        spawned = false;
        slow=false;
        angle=a;
        startAngle=a;
        b1cooldown=0;
        b2dur=-1;
        b2cooldown=0;
        b3dur=-1;
        b3cooldown=0;
        b4cooldown=0;
        b4dur=-1;
        b5dur=-1;
        b6dur=-1;
        b7dur=-1;
        laserAng = B7.initAng;
        altMode = false;
        region=reg;
    }

    public double getHp() {
        return hp;
    }

    public void turn(int x) {
        if(spawned&&b7dur==-1) {
            if(x>0) {
                angle+=C.turnSpe;
            } else {
                angle-=C.turnSpe;
            }
        }
    }

    public void update(ArrayList<Bullet> gg) {
        //System.out.println(toString());
        //System.out.println(getX()+(int)(6*Math.cos(angle+2*Math.PI/9))+" "+(getY()+(int)(6*Math.sin(angle+2*Math.PI/9))));
        if(spawned) {
            if(b1cooldown==0) { //Basic shot
                b1cooldown = B1.cd;
                Bul1 a = new Bul1(getX()+(int)(6*Math.cos(angle+B1.spread)),getY()-(int)(6*Math.sin(angle+B1.spread)),this,angle);
                Bul1 b = new Bul1(getX()+(int)(6*Math.cos(angle-B1.spread)),getY()-(int)(6*Math.sin(angle-B1.spread)),this,angle);
                gg.add(a);
                gg.add(b);
            } else {
                b1cooldown--;
            }
            if(b2dur>-1) { //Spread shot
                if(b2dur%(B2.dur/B2.shots)==0) {
                    Bul2 a = new Bul2(getX(),getY(),this,angle+B2.spread);
                    Bul2 b = new Bul2(getX(),getY(),this,angle+3*B2.spread);
                    Bul2 c = new Bul2(getX(),getY(),this,angle-B2.spread);
                    Bul2 d = new Bul2(getX(),getY(),this,angle-3*B2.spread);
                    gg.add(a);
                    gg.add(b);
                    gg.add(c);
                    gg.add(d);
                }
                b2dur++;
                if(b2dur==B2.dur+1) {
                    b2dur=-1;
                }
            }
            if(b5dur>-1) {
                if(b5dur%(B5.dur/B5.shots)==0) {
                    if(b5dur/(B5.dur/B5.shots)%2==0) {
                        Bul5 a = new Bul5(getX(),getY(),this,angle);
                        Bul5 b = new Bul5(getX(),getY(),this,angle+B5.spread*2);
                        Bul5 c = new Bul5(getX(),getY(),this,angle-B5.spread*2);
                        gg.add(a);
                        gg.add(b);
                        gg.add(c);
                    } else {
                        Bul5 a = new Bul5(getX(),getY(),this,angle+B5.spread);
                        Bul5 b = new Bul5(getX(),getY(),this,angle-B5.spread);
                        gg.add(a);
                        gg.add(b);
                    }
                }
                b5dur++;
                if(b5dur==B5.dur+1) {
                    b5dur=-1;
                }
            }
            if(b2cooldown>0) {
                b2cooldown--;
            }
            if(b3dur>-1) { //Curve shot
                if(b3dur%(B3.dur/B3.shots)==0) {
                    int shift = b3dur/(B3.dur/4);
                    Bul3 a = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20);
                    Bul3 b = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + Math.PI/4);
                    Bul3 c = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 2*Math.PI/4);
                    Bul3 d = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 3*Math.PI/4);
                    Bul3 e = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 4*Math.PI/4);
                    Bul3 f = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 5*Math.PI/4);
                    Bul3 g = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 6*Math.PI/4);
                    Bul3 h = new Bul3(getX(),getY(),this,angle+shift*Math.PI/20 + 7*Math.PI/4);
                    gg.add(a);
                    gg.add(b);
                    gg.add(c);
                    gg.add(d);
                    gg.add(e);
                    gg.add(f);
                    gg.add(g);
                    gg.add(h);
                }
                b3dur++;
                if(b3dur==B3.dur+1) {
                    b3dur=-1;
                }
            }
            if(b6dur>-1) {
                if(b6dur%(B6.dur/B6.shots)==0) {
                    for(int x=0; x<B6.bullets; x++) {
                        Bul6 a = new Bul6(getX(),getY(),this,angle+x*2*Math.PI/B6.bullets+B6.tilt);
                        gg.add(a);
                    }
                }
                b6dur++;
                if(b6dur==B6.dur+1) {
                    b6dur=-1;
                }
            }
            if(b3cooldown>0) { 
                b3cooldown--;
            }
            if(b4dur>-1) {
                b4dur++;
                int shiftx = (int)(B4.spe/2*Math.cos(savedAng));
                int shifty = -(int)(B4.spe/2*Math.sin(savedAng));
                Bul4 a = new Bul4(savedX1,savedY1,this,savedAng);
                Bul4 b = new Bul4(savedX2,savedY2,this,savedAng);
                Bul4 c = new Bul4(savedX1+shiftx,savedY1+shifty,this,savedAng);
                Bul4 d = new Bul4(savedX2+shiftx,savedY2+shifty,this,savedAng);
                gg.add(a);
                gg.add(b);
                gg.add(c);
                gg.add(d);
                if(b4dur==B4.dur+1) {
                    b4dur=-1;
                }
            }
            if(b7dur>-1) {
                setSlow(true);
                if(b7dur%B7.shots==0) {
                    Bul7 a = new Bul7(getX(),getY(),this,angle,laserAng,true);
                    Bul7 b = new Bul7(getX(),getY(),this,angle,laserAng,false);
                    gg.add(a);
                    gg.add(b);
                }
                laserAng+=B7.rot;
                b7dur++;
                if(b7dur==B7.dur+1) {
                    b7dur=-1;
                    setSlow(false);
                }
            }
            if(b4cooldown>0) {
                b4cooldown--;
            }
        }
    }

    public void shot2() { //Basic spread shot
        if(!altMode) {
            if(b2cooldown==0&&shotChekr()) {
                b2dur=0;
                b2cooldown=B2.cd;
            }
        } else {
            if(b2cooldown==0&&shotChekr()) {
                b5dur=0;
                b2cooldown=B5.cd;
            }
        }
    }
    public void shot3() { //Basic curve shot
        if(!altMode) {
            if(b3cooldown==0&&shotChekr()) {
                b3dur=0;
                b3cooldown=B3.cd;
            }
        } else {
            if(b3cooldown==0&&shotChekr()) {
                b6dur=0;
                b3cooldown=B6.cd;
            }
        }
    }
    public void shot4() { //Lane shot
        if(!altMode) {
            if(b4cooldown==0&&shotChekr()) {
                b4dur=0;
                b4cooldown=B4.cd;
                //System.out.println(savedAng);
                savedAng = angle;
                savedX1 = getX() + (int)(B4.spread*Math.cos(angle+Math.PI/2));
                savedY1 = getY() - (int)(B4.spread*Math.sin(angle+Math.PI/2));
                savedX2 = getX() + (int)(B4.spread*Math.cos(angle-Math.PI/2));
                savedY2 = getY() - (int)(B4.spread*Math.sin(angle-Math.PI/2));
            }
        } else {
            if(b4cooldown==0&&shotChekr()) {
                b7dur=0;
                b4cooldown=B7.cd;
                laserAng = B7.initAng;
            }
        }
    }
    public boolean shotChekr() { //Determines whether other shots are still firing, prevents excessive shot overlap
        if((b2dur==-1||b2dur>B2.dur*4/5) && (b3dur==-1||b3dur>B3.dur*4/5) && (b5dur==-1||b5dur>B5.dur*4/5) && (b6dur==-1||b6dur>B6.dur*4/5)) {
            return true;
        }
        return false;
    }

    public void show(Graphics g) {
        if(spawned) {
            super.show(g);
        }
    }

    public void move(int x1,int y1) {
        if(spawned) {
            if(region==1) { //Left half of map
                if(slow) {
                    if(getX()+x1*2/5>720 || getX()+x1*2/5<=0) {
                        x1=0;
                    }
                    if(getY()+y1*2/5>900 || getY()+y1*2/5<=0) {
                        y1=0;
                    }
                    super.move(x1*2/5,y1*2/5);
                } else {
                    //System.out.println(x1+" "+y1);
                    if(getX()+x1>=720 || getX()+x1<=0) {
                        x1=0;
                    }
                    if(getY()+y1>=900 || getY()+y1<=0) {
                        y1=0;
                    }
                    super.move(x1,y1);
                }
            } else {
                if(slow) {
                    if(getX()+x1*2/5>1440 || getX()+x1*2/5<=720) {
                        x1=0;
                    }
                    if(getY()+y1*2/5>900 || getY()+y1*2/5<=0) {
                        y1=0;
                    }
                    super.move(x1*2/5,y1*2/5);
                } else {
                    //System.out.println(x1+" "+y1);
                    if(getX()+x1>=1440 || getX()+x1<=720) {
                        x1=0;
                    }
                    if(getY()+y1>=900 || getY()+y1<=0) {
                        y1=0;
                    }
                    super.move(x1,y1);
                }
            }
        }
    }

    public void toggle() {
        altMode=!altMode;
    }
    public void setSlow(boolean s) {
        slow=s;
    }
    public double getHP() {
        return hp;
    }
    public double getAng() {
        return angle;
    }
    public boolean checkAlt() {
        return altMode;
    }
    public void hurt(double d) {
        hp-=d;
    }
    public void spawn() {
        spawned=true;
        setSlow(false);
        angle=startAngle;
        setX(spawnCoords[0]);
        setY(spawnCoords[1]);
        b1cooldown=b2cooldown=b3cooldown=b4cooldown=0;
        b2dur=b3dur=b4dur=b5dur=b6dur=b7dur=-1;
        altMode=false;
        hp=100;
    }
    public void die() {
        spawned=false;
    }
    public String toString() {
        return getX()+" "+getY();
    }

    public boolean equals(Ship g) {
        if(getX()==g.getX() && getY()==g.getY() & getColor().equals(g.getColor())) {
            return true;
        } else {
            return false;
        }
    }
}

class Bullet extends HBox {
    protected double dmg; //Damage of bullet
    protected Ship owner; //Creator of bullet
    protected int iFrames; //Initial time when bullet might overlap and can't hit creator
    protected int speed; //Speed of bullet
    protected double angle; //Direction of bullet
    protected double curve; //Curving of bullet path
    protected int index; //For determining position
    protected int startX,startY; //Starting coordinates
    protected int lifetime; //Time until despawn

    public Bullet(int x, int y, int s, double d, Ship o, int iF, int spe, double ang, int life) {
        super(x,y,s,C.bColor);
        dmg=d;
        owner=o;
        iFrames=iF;
        speed=spe;
        angle = ang;
        index=0;
        startX=x;
        startY=y;
        curve=0;
        lifetime=life;
    }
    public Bullet(int x, int y, int s, double d, Ship o, int iF, int spe, double ang, double rot, int life) {
        super(x,y,s,C.bColor);
        dmg=d;
        owner=o;
        iFrames=iF;
        speed=spe;
        angle=ang;
        index=0;
        startX=x;
        startY=y;
        curve=rot;
        lifetime=life;
    }

    public boolean hit(Ship o) {
        if(iFrames==0 || !o.equals(owner)) {
            o.hurt(dmg);
            return true;
        }
        return false;
    }

    public boolean update() {
        //int x = (int) ((index+1)*speed*Math.cos(angle)) - (int) (index*speed*Math.cos(angle));
        //int y = (int) ((index+1)*speed*Math.cos(angle)) - (int) (index*speed*Math.sin(angle));
        //move(x,y);
        int x = startX + (int) (index*speed*Math.cos(angle));
        int y = startY - (int) (index*speed*Math.sin(angle));
        index++;
        angle+=curve;
        //System.out.println(speed*Math.sin(angle));
        move(x-getX(),y-getY());
        if(iFrames>0) {
            iFrames--;
        }
        lifetime--;
        if(lifetime<0) {
            return true;
        }
        return false;
    }
    public boolean update(int n) {
        int x = startX + (int) (index*speed*Math.cos(angle));
        int y = startY - (int) (index*speed*Math.sin(angle));
        index+=n;
        angle+=curve;
        //System.out.println(speed*Math.sin(angle));
        move(x-getX(),y-getY());
        if(iFrames>0) {
            iFrames--;
        }
        lifetime--;
        if(lifetime<0) {
            return true;
        }
        return false;
    }
    public void randInd() {
        double g = Math.random();
        startX += (int) (g*index*speed*Math.cos(angle));
        startY -= (int) (g*index*speed*Math.sin(angle));
    }
}

class Bul1 extends Bullet {
    //Model of basic shot type
    public Bul1(int x, int y, Ship o, double ang) {
        super(x, y, B1.size, B1.dmg, o, B1.iFr, B1.spe,ang,B1.life);
        //System.out.println(getX()+" "+getY());
    }
}
class Bul2 extends Bullet {
    //Simple spread shot type
    public Bul2(int x, int y, Ship o, double ang) {
        super(x, y, B2.size, B2.dmg, o, B2.iFr, B2.spe,ang,B2.life);
    }
}
class Bul3 extends Bullet {
    //Simple curve shot type
    public Bul3(int x, int y, Ship o, double ang) {
        super(x, y, B3.size, B3.dmg, o, B3.iFr, B3.spe,ang,B3.rot,B3.life);
    }
}
class Bul4 extends Bullet {
    //Lane shot type
    public Bul4(int x, int y, Ship o, double ang) {
        super(x, y, B4.size, B4.dmg, o, B4.iFr, B4.spe,ang,B4.life);
        randInd();
    }
}
class Bul5 extends Bullet {
    //Alternate spread shot type
    public Bul5(int x, int y, Ship o, double ang) {
        super(x, y, B5.size, B5.dmg, o, B5.iFr, B5.spe,ang,B5.life);
    }
}
class Bul6 extends Bullet {
    //Alternate curve shot type
    boolean flag;
    public Bul6(int x, int y, Ship o, double ang) {
        super(x, y, B6.size, B6.dmg, o, B6.iFr, B6.spe,ang,B6.rot,B6.life);
        flag=false;
    }
    public boolean update() {
        boolean a;
        if(!flag) {
            a = super.update();
        } else {
            a=super.update(1);
        }
        if(index>B6.burst&&!flag) {
            speed*=B6.speFac;
            index=(int) (index/B6.speFac);
            curve=B6.rotb;
            flag=true;
            dmg = B6.dmgb;
        }
        return a;
    }
}
class Bul7 extends Bullet {
    //Helix laser
    private double tilt;
    private double rot;
    boolean slid;

    public Bul7(int x, int y, Ship o, double ang, double tilt,boolean s) {
        super(x, y, B7.size, B7.dmg, o, B7.iFr, B7.spe,ang,B7.rot,B7.life);
        this.tilt=0;
        rot=tilt;
        slid=s;
    }

    public boolean update() {
        int x,y;
        //int x = startX + (int) (index*speed*Math.cos(angle)+C.b7width*Math.sin(angle)*Math.sin(tilt));
        //
        //int y = startY - (int) (index*speed*Math.sin(angle)+C.b7width*Math.cos(angle)*Math.sin(tilt));
        //
        if(slid) {
            //tilt+=Math.PI/10;
            x = startX + (int) (index*speed*Math.cos(angle)-B7.width*Math.sin(angle)*Math.sin(tilt));
        //
            y = startY - (int) (index*speed*Math.sin(angle)+B7.width*Math.cos(angle)*Math.sin(tilt));
        //
        } else {
            //tilt-=Math.PI/10;
            x = startX + (int) (index*speed*Math.cos(angle)+B7.width*Math.sin(angle)*Math.sin(tilt));
            //
            y = startY - (int) (index*speed*Math.sin(angle)-B7.width*Math.cos(angle)*Math.sin(tilt));
            //
        }
        index++;
        tilt+=rot;
        //System.out.println(speed*Math.sin(angle));
        move(x-getX(),y-getY());
        if(iFrames>0) {
            iFrames--;
        }
        lifetime--;
        if(lifetime<0) {
            return true;
        }
        return false;
    }
}
