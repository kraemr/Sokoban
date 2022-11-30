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
           spielfeld = new char[highestlen][linecount];
           br.close();
           

        } catch (Exception io) {
            System.err.println("IO ERROR FILE NOT FOUND!");
            System.exit(1);
        }
        return pathstr;
    }
    
    
    static void read_map(String s){
        String line;
        try (BufferedReader newbr = Files.newBufferedReader(Paths.get(s)) ){
            while((line = newbr.readLine()) != null){
                // hier die line so bearbeiten, dass sie konform zu den Spielregeln ist
                // Und die Zielpositionen und kisten zaehlen, da es gleich viele geben muss
                // Anzahl Zielpositionen,kisten in anzahl_kisten 
                // und anzahlzielfelder zählen  
                // und Spieler position bei x und y eintragen

             }
        } catch (Exception e) {

        }
    }
   

    public static void print_field(int xsize,int ysize){
         for(int j = 0; j < ysize; j++){ 
                    for(int i = 0; i < xsize;i++)System.out.print(spielfeld[i][j]); 
                    System.out.println();
                }
    }

    // guckt nach collisionen und bewegt den spieler / Kiste
    static void handle_collision(int source_x,int source_y,int target_x,int target_y){
        // bei diesem fall haben wir uns nicht bewegt also müssen wir nichts machen
        if(source_x == target_x && source_y == target_y)return;

        //wenn ein Spieler sich auf ein feld bewegt, dann muss geguckt werden ob es sich um ein leeres feld,Wand,Zielposition oder um eine Kiste handelt
        //bei der Kiste wenn z.b sourcex 1, sourcey 1 und targetx 2 und targety 1 und 
        //spielfeld[3][1] noch keine kiste und keine wand hat, dann verschiebe die Kiste eins nach rechts
        //wenn kiste auf zielposition  dann wird daraus im array '*'
        //wenn spieler auf zielposition dann wird daraus im array '+'
        
    }

    static boolean win_condition(){
        // guck ob alle kisten auf zielpositionen sind 
        // return true wenn nicht dann return false; 
        return false;
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
        
        }       
    }

    public static void main(String[] args){      
        x = 0;
        y = 0;
        exit = 0;
        String s = init_map(args[1]); // initialisiert das array
        read_map(s);
        mainloop();
    }
}

