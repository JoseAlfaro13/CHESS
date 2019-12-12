
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    static int mouseX; static int mouseY;
    static int newMouseX; static int newMouseY;
    static int border=10;//candidad de espacios vacios al rededor del marco
    static int tamañoTablero=64;//tamaño del tablero
    static int mouseDrag[][][]=new int[8][8][2];
    static String[][] tableroAjedrez=new String[8][8];
    static boolean antiRepeat;
    @Override
    public void paintComponent(Graphics g) {
        if (!antiRepeat && ProyectoAjedrez.humanoComoBlancas==0) {antiRepeat=true; comoNegras();}
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        this.addMouseListener(this);//capacidad de oir el mouse
        this.addMouseMotionListener(this);//agrega la capacidad de "escuchar el movimiento del mouse"
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {//componente redimensionado
                tamañoTablero=(int) ((Math.min(getHeight(), getWidth())-2*border)/8);
            }
        });
        for (int i=0;i<64;i+=2) {//dibujar tablero
            g.setColor(new Color(255, 200, 100));
            g.fillRect((i%8+(i/8)%2)*tamañoTablero+border, (i/8)*tamañoTablero+border, tamañoTablero, tamañoTablero);
            g.setColor(new Color(150, 50, 30));
            g.fillRect(((i+1)%8-((i+1)/8)%2)*tamañoTablero+border, ((i+1)/8)*tamañoTablero+border, tamañoTablero, tamañoTablero);
        }
        Image chessPieceImage;
        chessPieceImage=new ImageIcon("ChessPieces.png").getImage();
        for (int i=0;i<64;i++) {//dibujar piezas de ajedrez en forma de texto
            int j=-1,k=-1;
            switch (ProyectoAjedrez.tableroAjedrez[i/8][i%8]) {
                case "P": j=5; k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "p": j=5; k=ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "R": j=2;k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "r": j=2;k=ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "K": j=4;k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "k": j=4;k=ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "B": j=3;k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "b": j=3;k=ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "Q": j=1;k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "q": j=1;k=ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "A": j=0;k=1-ProyectoAjedrez.humanoComoBlancas;
                    break;
                case "a": j=0;k=ProyectoAjedrez.humanoComoBlancas;
                    break;
            }
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPieceImage, (i%8)*tamañoTablero+border+mouseDrag[i/8][i%8][0], (i/8)*tamañoTablero+border+mouseDrag[i/8][i%8][1], (i%8+1)*tamañoTablero+border+mouseDrag[i/8][i%8][0], (i/8+1)*tamañoTablero+border+mouseDrag[i/8][i%8][1], j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
        g.setColor(Color.BLUE);
        for (int i=0;i<8;i++) {
            for (int j=0;j<8;j++) {
                if (!tableroAjedrez[i][j].equals(ProyectoAjedrez.tableroAjedrez[i][j])) {
                    g.drawRoundRect(j*tamañoTablero+border+3, i*tamañoTablero+border+3, tamañoTablero-6, tamañoTablero-6, 10, 10);
                    g.drawRoundRect(j*tamañoTablero+border+4, i*tamañoTablero+border+4, tamañoTablero-8, tamañoTablero-8, 10, 10);
                }
            }
        }
        
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDrag[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero][0]=((e.getX()-border)/tamañoTablero-(mouseX-border)/tamañoTablero)*tamañoTablero;
        mouseDrag[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero][1]=((e.getY()-border)/tamañoTablero-(mouseY-border)/tamañoTablero)*tamañoTablero;
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        newMouseX=e.getX();
        newMouseY=e.getY();
        mouseDrag[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero][0]=0;
        mouseDrag[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero][1]=0;
        repaint();
        if (e.getButton()==MouseEvent.BUTTON1) {//si dejó el mouse
            String dragMove;
            if ((newMouseY-border)/tamañoTablero==0 && (mouseY-border)/tamañoTablero==1 &&
                    "P".equals(ProyectoAjedrez.tableroAjedrez[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero])) {
                //Si un peón intenta pasar de la fila 1 a la fila 0 (promoción de peón):
                dragMove=""+(mouseX-border)/tamañoTablero+(newMouseX-border)/tamañoTablero+
                        ProyectoAjedrez.tableroAjedrez[(newMouseY-border)/tamañoTablero][(newMouseX-border)/tamañoTablero]+"QP";//asume la promoción de reina
            } else if (Math.abs((mouseX-border)/tamañoTablero-(newMouseX-border)/tamañoTablero)==2 &&
                    "A".equals(ProyectoAjedrez.tableroAjedrez[(mouseY-border)/tamañoTablero][(mouseX-border)/tamañoTablero])) {
                //si un rey intenta mover dos casillas (enroque):
                if ((mouseX-border)/tamañoTablero>(newMouseX-border)/tamañoTablero) {//suelto
                    dragMove=""+(mouseX-border)/tamañoTablero+"0"+(newMouseX-border)/tamañoTablero+((newMouseX-border)/tamañoTablero+1)+"C";
                } else {
                    dragMove=""+(mouseX-border)/tamañoTablero+"7"+(newMouseX-border)/tamañoTablero+((newMouseX-border)/tamañoTablero-1)+"C";
                }
            } else {
            dragMove=""+(mouseY-border)/tamañoTablero+(mouseX-border)/tamañoTablero+(newMouseY-border)/tamañoTablero+
                    (newMouseX-border)/tamañoTablero+
                    ProyectoAjedrez.tableroAjedrez[(newMouseY-border)/tamañoTablero][(newMouseX-border)/tamañoTablero];
            }
            String userMovePosibilities=ProyectoAjedrez.posibleMovimientos();
            if (userMovePosibilities.replace(dragMove, "").length()<userMovePosibilities.length()) {
                ProyectoAjedrez.hacerMovimiento(dragMove);
                ProyectoAjedrez.historialMovimientos+=dragMove;
                movePieceEvent();
            }
        }
    }
    public void comoNegras() {
        ProyectoAjedrez.flipBoard();
        for (int i=0;i<8;i++) {
            System.arraycopy(ProyectoAjedrez.tableroAjedrez[i], 0, tableroAjedrez[i], 0, 8);
        }
        ProyectoAjedrez.flipBoard();
        System.out.println("Pensando...");
        System.out.println(ProyectoAjedrez.ordenarMovimientos2(ProyectoAjedrez.posibleMovimientos()));
        ProyectoAjedrez.hacerMovimiento(ProyectoAjedrez.halfMove());
        ProyectoAjedrez.flipBoard();
        
        if (ProyectoAjedrez.posibleMovimientos().length()==0) {System.out.println("La computadora te dio Jaque Mate :(");}
    }
    public void movePieceEvent() {
        //comprobar enrroque y eliminar futuros enrroques:
        if (Math.abs((mouseX-border)/tamañoTablero-(newMouseX-border)/tamañoTablero)==2 &&
                "A".equals(ProyectoAjedrez.tableroAjedrez[(newMouseY-border)/tamañoTablero][(newMouseX-border)/tamañoTablero])) {
            //si un rey intenta mover dos casillas (enroque):
            if ((mouseX-border)/tamañoTablero==3) {//black
                ProyectoAjedrez.enrroqueLargoNegras=false;
                ProyectoAjedrez.enrroqueCortoNegras=false;
            } else {//blancas
                ProyectoAjedrez.enrroqueLargoBlancas=false;
                ProyectoAjedrez.enrroqueCortoBlancas=false;
            }
        }
        for (int i=0;i<8;i++) {
            System.arraycopy(ProyectoAjedrez.tableroAjedrez[i], 0, tableroAjedrez[i], 0, 8);
        }
        ProyectoAjedrez.flipBoard();
        ProyectoAjedrez.rating(1, 0);
        System.out.println(ProyectoAjedrez.ordenarMovimientos2(ProyectoAjedrez.posibleMovimientos()));
        System.out.println("Pensando.....");
        if (ProyectoAjedrez.posibleMovimientos().length()==0) {
            System.out.println("Le diste Jaque Mate a la computadora :)");
            ProyectoAjedrez.flipBoard();
        } else {
            ProyectoAjedrez.hacerMovimiento(ProyectoAjedrez.halfMove());
            ProyectoAjedrez.flipBoard();
            if (ProyectoAjedrez.posibleMovimientos().length()==0) {
                if (ProyectoAjedrez.reySeguro()) {
                    System.out.println("La computadora te ahogó :(");
                } else {
                    System.out.println("La computadora te dio Jaque Mate :(");
                }
            }
        }
        repaint();
    }
}
