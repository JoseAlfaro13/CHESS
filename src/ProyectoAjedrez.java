import java.util.*;
import javax.swing.*;

public class ProyectoAjedrez extends JFrame {//la herencia, permite definir una clase tomando como base a otra clase ya existente
    static String[][] tableroAjedrez={//Lista simulando el tablero
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"}};
    
    static int tableroPeon[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int tableroTorre[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int tableroCaballo[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int tableroAlfil[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int tableroDama[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int tableroReyMedio[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int[][] tableroReyFin={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    static String historialMovimientos="*****";
    static boolean enrroqueLargoBlancas=true, enrroqueCortoBlancas=true, enrroqueLargoNegras=true, enrroqueCortoNegras=true;
    static int humanoComoBlancas=-1, jugadorActual=1;//1=HUMANO JUEGA CON BLANCAS, 0=HUMANO JUEGA CON NEGRAS
    static int oportunidadReflexionar=-1/*1 significa que solo la computadora hace un movimiento sin reflexionar */, posicionReyC, posicionReyL, nodeCount;//CONTADOR DE NODOS ES TEMPORAL
    static String moverLista="";
    public ProyectoAjedrez() {
        dibujarVentana();
    }
    
    private void dibujarVentana() {
        this.setSize(200, 200);
        this.setTitle("Bienvenido");
        this.setUndecorated(true);
        this.setLocation(300, 300);
        JLabel linea = new JLabel("CARGANDO...");
        this.add(linea);
    }
    
    public static void main(String[] args) {
        
        
        
        //Se crea un ejecutable
        Runnable ejecutable = new Runnable() {
            @Override
            public void run() {
                //Se instancia la ventana
                ProyectoAjedrez ventanabienvenida = new ProyectoAjedrez();
                
                //Se muestra la ventana                
                ventanabienvenida.setLocationRelativeTo(null);
                ventanabienvenida.setVisible(true);
                

 
                try {//Se espera el número definido de segundos
                     Thread.sleep(10 * 1000);
 
                    } catch (Exception e) {}
 
                ventanabienvenida.dispose();
                for (int i=0;i<8;i++) {
                    System.arraycopy(tableroAjedrez[i], 0, UserInterface.tableroAjedrez[i], 0, 8);
                }
                Scanner sc=new Scanner(System.in);
        
                Object[] option={"Computadora","Humano"};//Elegir quien empieza
                humanoComoBlancas=JOptionPane.showOptionDialog(null, "Quien desea empezar?", "Nuevo Juego", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
                /*System.out.print("Quien desea empezar? (0=computadora and 1=humano): ");
                humanAsWhite=sc.nextInt();*/
                
                System.out.print("Cuántos movimientos profundos puedo pensar: ");//Tiempo de demora para la máquina
                oportunidadReflexionar=sc.nextInt();
                posicionReyC=0; posicionReyL=0;
                while (!"A".equals(tableroAjedrez[posicionReyC/8][posicionReyC%8])) {posicionReyC++;}//Posicion Rey
                while (!"a".equals(tableroAjedrez[posicionReyL/8][posicionReyL%8])) {posicionReyL++;}//Posicion Rey
        
                JFrame f=new JFrame("Chess");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                UserInterface ui=new UserInterface();
                f.add(ui);
                f.setSize(550, 570);
                f.setVisible(true);
                f.setAlwaysOnTop(true);
            }
        };
        //Se corre la tarea
        Thread tarea = new Thread(ejecutable);
        tarea.start();
    }
    public static String halfMove() {//Escoger el mejor movimiento
        long startTime = System.currentTimeMillis();
        posicionReyC=0; posicionReyL=0;
        while (!"A".equals(tableroAjedrez[posicionReyC/8][posicionReyC%8])) {posicionReyC++;}//get King's location
        while (!"a".equals(tableroAjedrez[posicionReyL/8][posicionReyL%8])) {posicionReyL++;}//get king's location
        String mejorMovimiento;
        nodeCount=0;
        mejorMovimiento=alphaBeta(oportunidadReflexionar, 1000000, -1000000, "", 0);
        System.out.println("Mejor Movimiento: "+mejorMovimiento.substring(0, 5));
        //System.out.println("K'th move: "+(posibleMoves().length()-posibleMoves().lastIndexOf(bestMove.substring(0, 5))));
        System.out.println("Puntaje Movimiento: "+mejorMovimiento.substring(5, mejorMovimiento.length()));
        
        long endTime = System.currentTimeMillis();
        System.out.println("El tiempo fue "+((endTime-startTime)*1000)+" segundos");//Timepo que tarda en responder
        System.out.println("Contador de nodos: "+nodeCount);
        System.out.println(historialMovimientos);//Historial
        
        historialMovimientos+=mejorMovimiento.substring(0, 5);
        return mejorMovimiento;
    }
    
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {//Maching Learnig
        //Regresa una cadena de la forma 1234b##################### (movimiento, luego, puntaje)
        String list=posibleMovimientos();
        if (depth==0 || list.length()==0) {return move+(rating(list.length(), depth)*(player*2-1));}//el [*(player*2-1)] se demostró correcto y necesario
        list=ordenarMovimientos2(list);
        player=1-player;
        for (int i=0;i<list.length();i+=5) {
            hacerMovimiento(list.substring(i,i+5));
            nodeCount+=1;
            flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5, returnString.length()));
            flipBoard();
            deshacerMovimiento(list.substring(i,i+5));
            if (player==0) {
                if (value<=beta) {beta=value; if (depth==oportunidadReflexionar) {move=returnString.substring(0, 5);}}
            } else {
                if (value>alpha) {alpha=value;  if (depth==oportunidadReflexionar) {move=returnString.substring(0, 5);}}
            }
            if (alpha>=beta) {
                if (player==0) {return move+beta;} else {return move+alpha;}
            }
        }
        if (player==0) {return move+beta;} else {return move+alpha;}
    }
    
    public static void hacerMovimiento(String move) {
        //Seguimiento del movimiento hecho++;
        if (move.charAt(4)!='C' && move.charAt(4)!='P') {
            tableroAjedrez[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=tableroAjedrez[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            tableroAjedrez[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if ("A".equals(tableroAjedrez[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                posicionReyC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));//Actualiza posición del Rey
            }
        } else if (move.charAt(4)=='P') {
            //Si promociona el peón
            tableroAjedrez[1][Character.getNumericValue(move.charAt(0))]=" ";
            tableroAjedrez[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        } else {
            //Si enrroca
            tableroAjedrez[7][Character.getNumericValue(move.charAt(0))]=" ";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(1))]=" ";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(2))]="A";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(3))]="R";
            posicionReyC=56+Character.getNumericValue(move.charAt(2));//Actualiza la posición del Rey
        }
    }
    public static void deshacerMovimiento(String move) {//Deshacer movimiento
        if (move.charAt(4)!='C' && move.charAt(4)!='P') {
            tableroAjedrez[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=tableroAjedrez[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            tableroAjedrez[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if ("A".equals(tableroAjedrez[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                posicionReyC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));//Actualiza la posición del Rey
            }
        } else if (move.charAt(4)=='P') {
            //Si muere peón
            tableroAjedrez[1][Character.getNumericValue(move.charAt(0))]="P";
            tableroAjedrez[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        } else {
            //Si se deshace
            tableroAjedrez[7][Character.getNumericValue(move.charAt(0))]="A";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(1))]="R";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(2))]=" ";
            tableroAjedrez[7][Character.getNumericValue(move.charAt(3))]=" ";
            posicionReyC=56+Character.getNumericValue(move.charAt(0));//Actualiza la posición del Rey
            
        }
    }
    public static void flipBoard() {//Girar el tablero
        //Seguimiento del flipboard++;
        String temp;
        for (int i=0;i<32;i++) {//Para la mitad de los cuadrados en el tablero
            int r=i/8, c=i%8;
            if (Character.isUpperCase(tableroAjedrez[r][c].charAt(0))) {
                temp=tableroAjedrez[r][c].toLowerCase();
            } else {
                temp=tableroAjedrez[r][c].toUpperCase();
            }
            if (Character.isUpperCase(tableroAjedrez[7-r][7-c].charAt(0))) {
                tableroAjedrez[r][c]=tableroAjedrez[7-r][7-c].toLowerCase();
            } else {
                tableroAjedrez[r][c]=tableroAjedrez[7-r][7-c].toUpperCase();
            }
            tableroAjedrez[7-r][7-c]=temp;
        }
        int kingTemp=posicionReyC;
        posicionReyC=63-posicionReyL;
        posicionReyL=63-kingTemp;
    }
    public static int rating(int listLength, int depth) {//La lista es para verificar el jaque mate
        //Seguimiento evaluacion++;
        int counter;
        int material=evaluacionMaterial();
        counter=material;//cuenta las piezas bajo control ligeramente contra sí mismo
        counter+=evaluacionPosicion(material);//el material es una indicación del estado del medio juego / final
        counter+=evaluacionAtaque();
        counter+=evaluacionMovilidad(listLength, depth, material);
        flipBoard();
        material=evaluacionMaterial();
        counter-=material;
        counter-=evaluacionPosicion(material);
        counter-=evaluacionAtaque();
        if (listLength==-1) {
            counter-=evaluacionMovilidad(-1, depth, material);
        } else {
            counter-=evaluacionMovilidad(posibleMovimientos().length(), depth, material);
        }
        
        flipBoard();
        return -(counter+depth*50);
    }
    public static int evaluacionMaterial() {//Tasa de evaluacion del material
        int counter=0;
        int counterAlfil=0;
        for (int i=0;i<64;i++) {//por cada cuadro en el tablero
            if (!" ".equals(tableroAjedrez[i/8][i%8])) {
                //si la casilla tiene una de mis piezas
                switch (tableroAjedrez[i/8][i%8]) {
                    case "P": counter+=100;
                        break;
                    case "R": counter+=500;
                        break;
                    case "K": counter+=300;
                        break;
                    case "B": counterAlfil+=1;
                        break;
                    case "Q": counter+=900;
                        break;
                }
            }
        }
        if (counterAlfil>=2) {counter+=300*counterAlfil;} else if (counterAlfil==1) {counter+=250;}//anima al par alfil
        return counter*7;//al menos 7
    }
    public static int evaluacionAtaque() {//Tasa de evaluacion del ataque
        int counter=0;
        int tempPositionC=posicionReyC;
        for (int i=0;i<64;i++) {//por cada cuadro en el tablero
            if (!" ".equals(tableroAjedrez[i/8][i%8])) {
                //si la casilla tiene una de mis piezas
                switch (tableroAjedrez[i/8][i%8]) {
                    case "P": {posicionReyC=i; if (!reySeguro()) {counter-=50;}}
                        break;
                    case "R": {posicionReyC=i; if (!reySeguro()) {counter-=500;}}
                        break;
                    case "K": {posicionReyC=i; if (!reySeguro()) {counter-=300;}}
                        break;
                    case "B": {posicionReyC=i; if (!reySeguro()) {counter-=300;}}
                        break;
                    case "Q": {posicionReyC=i; if (!reySeguro()) {counter-=900;}}
                        break;
                }
            }
        }
        posicionReyC=tempPositionC;
        if (!reySeguro()) {counter-=400;}//penalidad por jaque
        return counter/2;
    }
    public static int evaluacionMovilidad(int longitudLista, int depth, int material) {//Tasa de evaluacion de la mmovilidad
        int counter=0;
        counter+=longitudLista*15;//[posibleMoves().length()] devuelve 5 puntos por movimiento
        if (longitudLista==0) {//el lado actual está en jaque mate o estancado
            if (!reySeguro()) {//si esta en jaque mate
                counter+=-200000*depth;//[*(depth+1)] es una forma de favorecer una verificación más rápida
            } else {//Si está estancada
                counter+=-150000*depth;
            }
        }
        return counter;
    }
    public static int evaluacionPosicion(int material) {//Tasa de evaluacion de la mmovilidad
        int counter=0;
        for (int i=0;i<64;i++) {//para cada cuadro del tablero
            if (!" ".equals(tableroAjedrez[i/8][i%8])) {
                //si la casilla contiene mi pieza
                switch (tableroAjedrez[i/8][i%8]) {
                    case "P": counter+=tableroPeon[i/8][i%8];
                        break;
                    case "R": counter+=tableroTorre[i/8][i%8];
                        break;
                    case "K": counter+=tableroCaballo[i/8][i%8];
                        break;
                    case "B": counter+=tableroAlfil[i/8][i%8];
                        break;
                    case "Q": counter+=tableroDama[i/8][i%8];
                        break;
                    case "A": if (material>=1750) {counter+=tableroReyMedio[i/8][i%8]; counter+=movRey(posicionReyC).length()*10;} else {counter+=tableroReyFin[i/8][i%8]; counter+=movRey(posicionReyC).length()*30;}
                        break;
                }
            }
        }
        return counter;
    }
    public static String posibleMovimientos() {
        //seguimiento de los posibles movimientos++;
        String list="";
        for (int i=0;i<64;i++) {//para cada cuadro del tablero
            if (Character.isUpperCase(tableroAjedrez[i/8][i%8].charAt(0))) {
                //si el tablero contiene mi pieza
                switch (tableroAjedrez[i/8][i%8]) {//orden de probabilidad que la pieza sea seleccionada
                    case "P": list+=movPeon(i);
                        break;
                    case "R": list+=movTorre(i);
                        break;
                    case "K": list+=movCaballo(i);
                        break;
                    case "B": list+=movAlfil(i);
                        break;
                    case "Q": list+=movDama(i);
                        break;
                    case "A": list+=movRey(i);
                        break;//el enrroque se trata desde la perspectiva del rey
                }
            }
        }
        return list.replaceAll("....a", "");//elimina capturas del rey ya que eso no sucede
    }
    public static String movPeon(int i) {//movimientos del peon
        String list="", oldPiece;
        int r=i/8, c=i%8;
            for (int j=-1;j<=1;j+=2) {
                try {
                    if (Character.isLowerCase(tableroAjedrez[r-1][c+j].charAt(0)) && i>=16)
                    {
                        oldPiece=tableroAjedrez[r-1][c+j];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r-1][c+j]="P";
                        if (reySeguro()) {
                            //fila1columna1fila2columna2pieza capturada
                            list=list+r+c+(r-1)+(c+j)+oldPiece;
                        }
                        tableroAjedrez[r][c]="P";
                        tableroAjedrez[r-1][c+j]=oldPiece;
                    }
                } catch (Exception e) {}
                try {//promocion y captura
                    if (Character.isLowerCase(tableroAjedrez[r-1][c+j].charAt(0)) && i<16) {
                        String[] temp={"Q","R","B","K"};
                        for (int k=0;k<4;k++) {
                            oldPiece=tableroAjedrez[r-1][c+j];
                            tableroAjedrez[r][c]=" ";
                            tableroAjedrez[r-1][c+j]=temp[k];
                            if (reySeguro()) {
                                //columna1columna2 caputra pieza a nueva pieza
                                list=list+c+(c+j)+oldPiece+temp[k]+"P";
                            }
                            tableroAjedrez[r][c]="P";
                            tableroAjedrez[r-1][c+j]=oldPiece;
                        }
                    }
                } catch (Exception e) {}
            }
            try {
                if (" ".equals(tableroAjedrez[r-1][c]) && i>=16)
                {
                    oldPiece=tableroAjedrez[r-1][c];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r-1][c]="P";
                    if (reySeguro()) {
                        //columna1columna2 caputra pieza a nueva pieza
                        list=list+r+c+(r-1)+c+oldPiece;
                    }
                    tableroAjedrez[r][c]="P";
                    tableroAjedrez[r-1][c]=oldPiece;
                }
            } catch (Exception e) {}
            try {//promocion y no captura
                if (" ".equals(tableroAjedrez[r-1][c]) && i<16) {
                    String[] temp={"Q","R","B","K"};
                    for (int k=0;k<4;k++) {
                        oldPiece=tableroAjedrez[r-1][c];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r-1][c]=temp[k];
                        if (reySeguro()) {
                            //columna1columna2 caputra pieza a nueva pieza
                            list=list+c+c+oldPiece+temp[k]+"P";
                        }
                        tableroAjedrez[r][c]="P";
                        tableroAjedrez[r-1][c]=oldPiece;
                    }
                }
            } catch (Exception e) {}
            try {
                if (" ".equals(tableroAjedrez[r-1][c]) && " ".equals(tableroAjedrez[r-2][c]) && i>=48)
                {
                    oldPiece=tableroAjedrez[r-2][c];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r-2][c]="P";
                    if (reySeguro()) {
                        //fila1columna1fila2columna2 caputra pieza a nueva pieza
                        list=list+r+c+(r-2)+c+oldPiece;
                    }
                    tableroAjedrez[r][c]="P";
                    tableroAjedrez[r-2][c]=oldPiece;
                }
            } catch (Exception e) {}
        return list;
    }
    public static String movTorre(int i) {//movimientos de la torre
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1;j<=1;j+=2) {
            try {
                while (" ".equals(tableroAjedrez[r][c+temp*j]))
                {
                    oldPiece=tableroAjedrez[r][c+temp*j];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r][c+temp*j]="R";
                    if (reySeguro()) {
                        //fila1columna1fila2columna2 caputra pieza a nueva pieza
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    tableroAjedrez[r][c]="R";
                    tableroAjedrez[r][c+temp*j]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(tableroAjedrez[r][c+temp*j].charAt(0))) {
                    oldPiece=tableroAjedrez[r][c+temp*j];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r][c+temp*j]="R";
                    if (reySeguro()) {
                        //fila1columna1fila2columna2 caputra pieza a nueva pieza
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    tableroAjedrez[r][c]="R";
                    tableroAjedrez[r][c+temp*j]=oldPiece;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while (" ".equals(tableroAjedrez[r+temp*j][c]))
                {
                    oldPiece=tableroAjedrez[r+temp*j][c];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r+temp*j][c]="R";
                    if (reySeguro()) {
                        //fila1columna1fila2columna2 caputra pieza a nueva pieza
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    tableroAjedrez[r][c]="R";
                    tableroAjedrez[r+temp*j][c]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(tableroAjedrez[r+temp*j][c].charAt(0))) {
                    oldPiece=tableroAjedrez[r+temp*j][c];
                    tableroAjedrez[r][c]=" ";
                    tableroAjedrez[r+temp*j][c]="R";
                    if (reySeguro()) {
                        //fila1columna1fila2columna2 caputra pieza a nueva pieza
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    tableroAjedrez[r][c]="R";
                    tableroAjedrez[r+temp*j][c]=oldPiece;
                }
            } catch (Exception e) {}
            temp=1;
        }
        return list;
    }
    public static String movCaballo(int i) {//movimientos del caballo
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1;j<=1;j+=2) {
            for (int k=-1;k<=1;k+=2) {
                try {
                    if (Character.isLowerCase(tableroAjedrez[r+j][c+k*2].charAt(0)) || " ".equals(tableroAjedrez[r+j][c+k*2])) {
                        oldPiece=tableroAjedrez[r+j][c+k*2];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r+j][c+k*2]="K";
                        if (reySeguro()) {
                            //fila1columna1fila2columna2 caputra pieza 
                            list=list+r+c+(r+j)+(c+k*2)+oldPiece;
                        }
                        tableroAjedrez[r][c]="K";
                        tableroAjedrez[r+j][c+k*2]=oldPiece;
                    }
                } catch (Exception e) {}
                try {
                    if (Character.isLowerCase(tableroAjedrez[r+j*2][c+k].charAt(0)) || " ".equals(tableroAjedrez[r+j*2][c+k])) {
                        oldPiece=tableroAjedrez[r+j*2][c+k];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r+j*2][c+k]="K";
                        if (reySeguro()) {
                            //fila1columna1fila2columna2 caputra pieza 
                            list=list+r+c+(r+j*2)+(c+k)+oldPiece;
                        }
                        tableroAjedrez[r][c]="K";
                        tableroAjedrez[r+j*2][c+k]=oldPiece;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }
    public static String movAlfil(int i) {//movimientos alfil
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1;j<=1;j+=2) {
            for (int k=-1;k<=1;k+=2) {
                try {
                    while (" ".equals(tableroAjedrez[r+temp*j][c+temp*k]))
                    {
                        oldPiece=tableroAjedrez[r+temp*j][c+temp*k];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r+temp*j][c+temp*k]="B";
                        if (reySeguro()) {
                            //fila1columna1fila2columna2 caputra pieza
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        tableroAjedrez[r][c]="B";
                        tableroAjedrez[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(tableroAjedrez[r+temp*j][c+temp*k].charAt(0))) {
                        oldPiece=tableroAjedrez[r+temp*j][c+temp*k];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r+temp*j][c+temp*k]="B";
                        if (reySeguro()) {
                            //fila1columna1fila2columna2 caputra pieza 
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        tableroAjedrez[r][c]="B";
                        tableroAjedrez[r+temp*j][c+temp*k]=oldPiece;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        return list;
    }
    public static String movDama(int i) {//movimientos dama
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1;j<=1;j++) {
            for (int k=-1;k<=1;k++) {
                if (j!=0 || k!=0) {
                    try {
                        while (" ".equals(tableroAjedrez[r+temp*j][c+temp*k]))
                        {
                            oldPiece=tableroAjedrez[r+temp*j][c+temp*k];
                            tableroAjedrez[r][c]=" ";
                            tableroAjedrez[r+temp*j][c+temp*k]="Q";
                            if (reySeguro()) {
                                //fila1columna1fila2columna2 caputra pieza
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            tableroAjedrez[r][c]="Q";
                            tableroAjedrez[r+temp*j][c+temp*k]=oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(tableroAjedrez[r+temp*j][c+temp*k].charAt(0))) {
                            oldPiece=tableroAjedrez[r+temp*j][c+temp*k];
                            tableroAjedrez[r][c]=" ";
                            tableroAjedrez[r+temp*j][c+temp*k]="Q";
                            if (reySeguro()) {
                                //fila1columna1fila2columna2 caputra pieza
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            tableroAjedrez[r][c]="Q";
                            tableroAjedrez[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    temp=1;
                }
            }
        }
        return list;
    }
    public static String movRey(int i) {//movimientos rey
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=0;j<9;j++) {
            if (j!=4) {
                try {
                    if (Character.isLowerCase(tableroAjedrez[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(tableroAjedrez[r-1+j/3][c-1+j%3])) {
                        oldPiece=tableroAjedrez[r-1+j/3][c-1+j%3];
                        tableroAjedrez[r][c]=" ";
                        tableroAjedrez[r-1+j/3][c-1+j%3]="A";
                        int kingTemp=posicionReyC;
                        posicionReyC=i+(j/3)*8+j%3-9;
                        if (reySeguro()) {
                            //fila1columna1fila2columna2 caputra pieza 
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                        }
                        tableroAjedrez[r][c]="A";
                        tableroAjedrez[r-1+j/3][c-1+j%3]=oldPiece;
                        posicionReyC=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        //devuelve el movimiento como kingColumn, rookColumn, kingNewColumn, rookNewColumn, C
        if ("A".equals(tableroAjedrez[7][4]) && enrroqueLargoBlancas && "R".equals(tableroAjedrez[7][0]) && " ".equals(tableroAjedrez[7][1]) && " ".equals(tableroAjedrez[7][2]) && " ".equals(tableroAjedrez[7][3])) {
            boolean temp=false;
            for (int j=1;j<=2;j++) {
                hacerMovimiento("747"+j+" ");
                temp=(temp || !reySeguro());
                deshacerMovimiento("747"+j+" ");
            }
            if (!temp) {
                //Se cumplio todos los criterios,enrroque largo
                list+="4023C";
            }
        }
        if ("A".equals(tableroAjedrez[7][4]) && enrroqueCortoBlancas && "R".equals(tableroAjedrez[7][7]) && " ".equals(tableroAjedrez[7][5]) && " ".equals(tableroAjedrez[7][6])) {
            boolean temp=false;
            for (int j=5;j<=6;j++) {
                hacerMovimiento("747"+j+" ");
                temp=(temp || !reySeguro());
                deshacerMovimiento("747"+j+" ");
            }
            if (!temp) {
                //se cumplio todos los criterios,enrroque corto
                list+="4765C";
            }
        }
        if ("A".equals(tableroAjedrez[7][3]) && enrroqueLargoNegras && "R".equals(tableroAjedrez[7][7]) && " ".equals(tableroAjedrez[7][4]) && " ".equals(tableroAjedrez[7][5]) && " ".equals(tableroAjedrez[7][6])) {
            boolean temp=false;
            for (int j=4;j<=5;j++) {
                hacerMovimiento("747"+j+" ");
                temp=(temp || !reySeguro());
                deshacerMovimiento("747"+j+" ");
            }
            if (!temp) {
                //se cumplieron todos los criterios, enrroque largo negro
                list+="3754C";
            }
        }
        if ("A".equals(tableroAjedrez[7][3]) && enrroqueCortoNegras && "R".equals(tableroAjedrez[7][0]) && " ".equals(tableroAjedrez[7][1]) && " ".equals(tableroAjedrez[7][2])) {
            boolean temp=false;
            for (int j=1;j<=2;j++) {
                hacerMovimiento("747"+j+" ");
                temp=(temp || !reySeguro());
                deshacerMovimiento("747"+j+" ");
            }
            if (!temp) {
                //se cumplieron todos los criterios, enrroque corto negro
                list+="3012C";
            }
        }
        return list;
    }
    public static boolean reySeguro() {
        //seguimiento del rey seguro++;
        //alfil, dama
        int temp=1;
        for (int i=-1;i<=1;i+=2) {
            for (int j=-1;j<=1;j+=2) {
                try {
                    while (" ".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8+temp*j])) {temp++;}
                    if ("b".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8+temp*j]) ||
                            "q".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8+temp*j])) {return false;}
                } catch (Exception e) {}
                temp=1;
            }
        }
        //torre, dama
        for (int i=-1;i<=1;i+=2) {
            try {
                while (" ".equals(tableroAjedrez[posicionReyC/8][posicionReyC%8+temp*i])) {temp++;}
                if ("r".equals(tableroAjedrez[posicionReyC/8][posicionReyC%8+temp*i]) ||
                        "q".equals(tableroAjedrez[posicionReyC/8][posicionReyC%8+temp*i])) {return false;}
            } catch (Exception e) {}
            temp=1;
            try {
                while (" ".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8])) {temp++;}
                if ("r".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8]) ||
                        "q".equals(tableroAjedrez[posicionReyC/8+temp*i][posicionReyC%8])) {return false;}
            } catch (Exception e) {}
            temp=1;
        }
        //caballo
        for (int i=-1;i<=1;i+=2) {
            for (int j=-1;j<=1;j+=2) {
                try {if ("k".equals(tableroAjedrez[posicionReyC/8+i][posicionReyC%8+j*2])) {return false;}} catch (Exception e) {}
                try {if ("k".equals(tableroAjedrez[posicionReyC/8+i*2][posicionReyC%8+j])) {return false;}} catch (Exception e) {}
            }
        }
        //peon
        if (posicionReyC>=16) {
            try {if ("p".equals(tableroAjedrez[posicionReyC/8-1][posicionReyC%8-1])) {return false;}} catch (Exception e) {}
            try {if ("p".equals(tableroAjedrez[posicionReyC/8-1][posicionReyC%8+1])) {return false;}} catch (Exception e) {}
        }
        //rey
        for (int i=-1;i<=1;i++) {
            for (int j=-1;j<=1;j++) {
                if (i!=0 || j!=0) {
                    try {if ("a".equals(tableroAjedrez[posicionReyC/8+i][posicionReyC%8+j])) {return false;}} catch (Exception e) {}
                }
            }
        }
        return true;
    }
    public static String ordenarMovimientos(String list) {//ordenar movimientos
        String newList="";
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)=='q') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)=='r') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)=='k') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)=='b') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)=='p') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        for (int i=0;i<list.length();i+=5) {
            if (list.charAt(i+4)==' ') {newList+=list.substring(i,i+5); list=list.replaceFirst(list.substring(i,i+5), ""); i-=5;}
        }
        return newList;
    }
    public static String ordenarMovimientos2(String list) { //ordenar movimentos dos
        int[] score=new int[list.length()/5];
        for (int i=0;i<list.length();i+=5) {//veloz
            hacerMovimiento(list.substring(i,i+5));
            score[i/5]=-rating(-1, 0);
            deshacerMovimiento(list.substring(i,i+5));
        }
        String newLista="", newListb=list;
        for (int i=0;i<Math.min(50,list.length()/5);i++) {//solo los primeros movimientos
            int max=-1000000,maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;//asegura que no se vuelva a contar
            newLista+=list.substring(maxLocation*5, maxLocation*5+5);
            newListb=newListb.replace(list.substring(maxLocation*5, maxLocation*5+5), "");
        }
        return newLista+newListb;
    }
}
