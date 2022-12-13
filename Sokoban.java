import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.WindowConstants;

class Sokoban2{

    static char[][] spielfeld;
    
    static int x; 
    static int y;
    
    static int exit;

    static int xt; // temporärer x wert 
    static int yt; //temporärer y wert 

    static int anzahl_kisten;
    static int anzahl_zielfelder;
    static int gewinncounter;

    static int highestlen= 0;
    static int linecount = 0; 


    static String init_map(String pathstr){
        if(pathstr == null){
            pathstr = "sokoban.txt";
        }
        try(BufferedReader br =  Files.newBufferedReader(Paths.get(pathstr))){
           String line = null;
           //diese while loop findet heraus wie groß die größte Linie ist
           // und wie viele linien es gibt
           while((line = br.readLine()) != null){
            if(line.length() > highestlen){
                highestlen = line.length();
            }
            linecount++;
           }
           spielfeld = new char[linecount][highestlen];
           br.close();
           

        } catch (Exception io) {
            System.err.println("IO ERROR FILE NOT FOUND!");
            System.exit(1);
        }
        return pathstr;
    }
    
    
    static void read_map(String s){
        String line;
        int j = 0;
        int player = 0;
        int target = 0;
        int boxes = 0;
        try (BufferedReader newbr = Files.newBufferedReader(Paths.get(s)) ){
            while((line = newbr.readLine()) != null){
                // hier die line so bearbeiten, dass sie konform zu den Spielregeln ist
                // Und die Zielpositionen und kisten zaehlen, da es gleich viele geben muss
                // Anzahl Zielpositionen,kisten in anzahl_kisten 
                // und anzahlzielfelder zählen  
                // und Spieler position bei x und y eintragen

                for(int i = 0; i < line.length();i++){
                    spielfeld[j][i] = line.charAt(i);
                    if  (spielfeld[j][i] == '$') boxes++;
                    if  (spielfeld[j][i] == '.') target++;
                    if  (spielfeld[j][i] == '@') player++;
                }
                // #########
                // # @ $ . #
                // #########
                j++;
             }
            if(target != boxes && player != 1){
                System.err.println("Fehler nicht gleiche Anzahl an boxen und zielpositionen oder keine Startposition ");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Datei konnte nicht geladen/gefunden werden");
        }

    }
   

    public static void print_field(int xsize,int ysize){
         for(int j = 0; j < ysize; j++){ 
                    for(int i = 0; i < xsize;i++)System.out.print(spielfeld[j][i]);
                    System.out.println();
                }
    }

    // guckt nach collisionen und bewegt den spieler / Kiste
    static void handle_collision(int source_x,int source_y,int target_x,int target_y){
        // bei diesem fall haben wir uns nicht bewegt also müssen wir nichts machen
        int deltax=target_x + (target_x - x);
        int deltay=target_y + (target_y - y);
        // --> x=6,y=6     x=7,y=6    8,6
        char playersymbol = spielfeld[source_y][source_x];
        if(source_x == target_x && source_y == target_y) return;
        if(spielfeld[target_y][target_x] == ' '){
            spielfeld[source_y][source_x] = ' '; // Wir seind nicht mehr auf diesem Feld
            spielfeld[target_y][target_x] = '@';
            x = target_x; // Spieler Position
            y = target_y;
        }
        // x= 6 y = 6 targety = 7 targetx = 6
         else if(spielfeld[target_y][target_x] == '$'){ // Wir bewegen uns in richtung einer Kiste
                if(spielfeld[deltay][deltax] == ' ')
                {
                    // checken ob die Bewegung der Kiste durch eine Wand geblockt ist
                    spielfeld[deltay][deltax] = '$';
                    spielfeld[target_y][target_x] = '@';
                    spielfeld[source_y][source_x] = ' ';
                    x = target_x;
                    y = target_y;
                }
                else if(spielfeld[deltay][deltax] == '.')
                {
                    spielfeld[deltay][deltax] = '*';
                    spielfeld[target_y][target_x] = '@';
                    spielfeld[source_y][source_x] = ' ';
                    x = target_x;
                    y = target_y;
                }
        }else if(spielfeld[target_y][target_x] == '*' ){
            // checken ob die Bewegung der Kiste durch eine Wand geblockt ist
            spielfeld[deltay][deltax] = '$';
            spielfeld[target_y][target_x] = '+';
            spielfeld[source_y][source_x] = ' ';
            x = target_x;
            y = target_y;
        }
         else if(spielfeld[target_y][target_x] == '.'){
            spielfeld[target_y][target_x] = '+';
            spielfeld[source_y][source_x] = ' ';
            x = target_x;
            y = target_y;
        }



        //wenn ein Spieler sich auf ein feld bewegt, dann muss geguckt werden ob es sich um ein leeres feld,Wand,Zielposition oder um eine Kiste handelt
        //bei der Kiste wenn z.b sourcex 1, sourcey 1 und targetx 2 und targety 1 und 
        //spielfeld[3][1] noch keine kiste und keine wand hat, dann verschiebe die Kiste eins nach rechts
        //wenn kiste auf zielposition  dann wird daraus im array '*'
        //wenn spieler auf zielposition dann wird daraus im array '+'
        
    }

    static boolean win_condition(){
        // guck ob alle kisten auf zielpositionen sind 
        // return true wenn nicht dann return false;
        int wcount = 0;
        for(int j = 0; j < linecount;j++){
            for(int i = 0; i < highestlen;i++) {
                if(spielfeld[j][i] == '*'){
                    wcount++;
                }
            }
        }
        if(anzahl_zielfelder == wcount) return true;
        else return false;
    }

    public static void mainloop(){
        char movecmd=' ';
        Scanner scan = new Scanner(System.in);
        while(exit == 0){ //Wenn exit 1 ist beende das programm
            print_field(highestlen,linecount);
            if(win_condition() == true) exit = 1; // check ob gewonnen wurde
            System.out.println("Neuer Zug: Mit w nach oben ,s nach unten,a nach links und d nach rechts bewegen,mit enter bestätigen und mit e beenden");
            movecmd= scan.next().charAt(0);
            xt = x; // Startposition x wird gespeichert
            yt = y; // Startposiiton y wird gespeichert
            
            switch(movecmd){
                case 'd': x++;break; //Wir bewegen uns nach rechts
                case 'a': x--;break; //Wir bewegen uns nach links
                case 's': y++;break; //Wir bewegen uns nach unten
                case 'w': y--;break; //Wir bewegen uns nach oben
                case 'e': exit = 1;break;
                default: //Wenn alle anderen cases nicht erfüllt werden wird diese Anweisung ausgeführt
                System.out.println(movecmd + " " + "ist kein möglicher Befehl");
            }
            handle_collision(xt,yt,  x,y);
            if(win_condition() == true){
                exit = 1;
            }
        
        }       
    }

    public static void main(String[] args){      
        x = 0;
        y = 0;
        exit = 0;
        String test;
        if(args.length>0)
        {
            test = args[0];
        }
        else
        {
            test = "sokoban.txt";
        }
        //args[0] = "sokoban.txt"
        String s = init_map(test); // initialisiert das array
        read_map(s);
        print_field(highestlen,linecount);
        mainloop();
    }
}

