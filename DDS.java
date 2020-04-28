package tank;
import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
 
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
 
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
 
 
 
import java.awt.Color;
 
public class DDS extends JFrame implements Runnable, MouseListener, ActionListener {
 
    private static final long serialVersionUID = 1L;
 
    private JPanel jContentPane = null;
 
    private JLabel lback = null;
 
    private JLabel lm1 = null;
 
    private JLabel lm2 = null;
 
    private JLabel lm3 = null;
 
    private JLabel lm4 = null;
 
    private JLabel lm5 = null;
 
    private JLabel lm6 = null;
 
    private JLabel lm7 = null;
 
    private JLabel lm8 = null;
     
    private JLabel []lm;
     
    private Thread t;
 
    private JLabel lscore;
     
    private int score=0;
     
    private Cursor c1,c2;
     
    private Timer timer;
     
    private JMenuItem mon,moff;
     
    private JProgressBar progressBar;
     
    private boolean threadstarted=false,findhero=false;
     
    private Score[]sc;
     
    private File file;
     
    private JMenuItem mstart,mstop,mresume,mexit;
     
    private AudioClip clip1,clip2;  //  @jve:decl-index=0:
    private JMenuItem mhero;
 
    /**
     * This is the default constructor
     */
    static class Score implements Serializable{
        String id;
        int score;
        Score(){
            id="匿名" ;
        score=0;
        }
    }
    public DDS() {
        super();
        initialize();
    }
 
    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
         
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
         
        JMenu mcontrol = new JMenu("\u6E38\u620F\u63A7\u5236");
        menuBar.add(mcontrol);
         
         mstart = new JMenuItem("\u5F00\u59CB");
        mstart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(threadstarted==false) {
                t.start();
                }
                else {
                    t.resume();
                }
                timer.start();
                mstart.setEnabled(false);
                mstop.setEnabled(true);
                mresume.setEnabled(true);
                progressBar.setString(null);
            }
        });
        mcontrol.add(mstart);
         
         mstop = new JMenuItem("\u6682\u505C");
         mstop.setEnabled(false);
        mstop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.suspend();
                timer.stop();
                mstop.setEnabled(false);
                mresume.setEnabled(true);
            }
        });
        mcontrol.add(mstop);
         
         mresume = new JMenuItem("\u7EE7\u7EED");
         mresume.setEnabled(false);
        mresume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.resume();
                timer.start();
                mresume.setEnabled(false);
                mstop.setEnabled(true);
            }
        });
        mcontrol.add(mresume);
         
         mexit = new JMenuItem("\u9000\u51FA");
        mexit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        mcontrol.add(mexit);
         
        JMenu menu = new JMenu("\u80CC\u666F\u97F3\u4E50");
        menuBar.add(menu);
         
         mon = new JMenuItem("ON");
        mon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip1.loop();
                mon.setEnabled(false);
                moff.setEnabled(true);
            }
        });
        menu.add(mon);
         
         moff = new JMenuItem("OFF");
         moff.setEnabled(false);
        moff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip1.stop();
                mon.setEnabled(true);
                moff.setEnabled(false);
            }
        });
        menu.add(moff);
         
        JMenu mhelp = new JMenu("\u5E2E\u52A9");
        menuBar.add(mhelp);
         
        mhero = new JMenuItem("\u82F1\u96C4\u699C");
        mhero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                 Heros heros=new Heros();
                 for(int i=0;i<DDS.this.sc.length;i++) {
                     heros.taheros.append("英雄大名:"+sc[i].id+"得分："+sc[i].score+"\n");
                 }
                 
                 heros.setModal(true);
                 heros.show();
            }
        });
        mhelp.add(mhero);
        this.setContentPane(getJContentPane());
        this.setTitle("打地鼠游戏");
        this.setBounds(new Rectangle(300, 100, 511, 546));
        this.setVisible(true);
         
        lm=new JLabel[8];
        lm[0]=lm1;
        lm[1]=lm2;
        lm[2]=lm3;
        lm[3]=lm4;
        lm[4]=lm5;
        lm[5]=lm6;
        lm[6]=lm7;
        lm[7]=lm8;
        for(int i=0;i<lm.length;i++){
            lm[i].setVisible(false);
            lm[i].addMouseListener(this);
        }
        lscore.setText("你的得分是0分");
        t=new Thread(this);
        timer=new Timer(1000,this);
        //t.start();
         
        c1=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon.png")),new Point(20,20),"c1");
        c2=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon1.png")),new Point(20,20),"c2");
        clip1=Applet.newAudioClip(this.getClass().getResource("背景音乐.wav"));
        clip2=Applet.newAudioClip(this.getClass().getResource("响声.wav"));
         
        file=new File(".//hero.data");
        if(!file.exists()) {
        try {
            file.createNewFile();
            sc=new Score[6];
            for(int i=0;i<sc.length;i++)
                sc[i]=new Score();
            OutputStream os=new FileOutputStream(file);
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(sc);
            oos.close();
            os.close();
             } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
             }
        }  
        else {
            try {
                 InputStream is=new FileInputStream(file);
                 ObjectInputStream  ois=new ObjectInputStream(is); 
                 sc=(Score[])ois.readObject();
                 //for(int i=0;i<sc.length;i++)
                    // System.out.println(sc[i].id+":"+sc[i].score);
                 ois.close();
                 is.close();
            }catch(Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
 
    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            lscore = new JLabel();
            lscore.setBounds(new Rectangle(281, 35, 222, 32));
            lscore.setText("");
            lm8 = new JLabel();
            lm8.setBounds(new Rectangle(364, 340, 62, 78));
            lm8.setText("");
            lm8.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm7 = new JLabel();
            lm7.setBounds(new Rectangle(100, 303, 67, 82));
            lm7.setText("");
            lm7.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm6 = new JLabel();
            lm6.setBounds(new Rectangle(237, 384, 74, 78));
            lm6.setText("");
            lm6.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm5 = new JLabel();
            lm5.setBounds(new Rectangle(191, 324, 68, 89));
            lm5.setText("");
            lm5.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm4 = new JLabel();
            lm4.setBounds(new Rectangle(389, 268, 60, 87));
            lm4.setText("");
            lm4.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm3 = new JLabel();
            lm3.setBounds(new Rectangle(251, 279, 64, 80));
            lm3.setText("");
            lm3.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm2 = new JLabel();
            lm2.setBounds(new Rectangle(337, 226, 65, 75));
            lm2.setText("");
            lm2.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm1 = new JLabel();
            lm1.setBounds(new Rectangle(240, 228, 60, 86));
            lm1.setIcon(new ImageIcon(getClass().getResource("/img/mouse.png")));
            lm1.setText("");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(lm1, null);
            jContentPane.add(lm2, null);
            jContentPane.add(lm3, null);
            jContentPane.add(lm4, null);
            jContentPane.add(lm5, null);
            jContentPane.add(lm6, null);
            jContentPane.add(lm7, null);
            jContentPane.add(lm8, null);
             
            jContentPane.add(lscore, null);
             
            progressBar = new JProgressBar();
            progressBar.setMaximum(10);
            progressBar.setBorder(new TitledBorder(null, "\u6E38\u620F\u8FDB\u5EA6", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.RED));
            progressBar.setStringPainted(true);
            progressBar.setBounds(28, 53, 171, 55);
            jContentPane.add(progressBar);
            lback = new JLabel();
            lback.setText("");
            lback.setSize(new Dimension(491, 500));
            lback.setLocation(new Point(0, -1));
            lback.setIcon(new ImageIcon(getClass().getResource("/img/background.jpg")));
            jContentPane.add(lback, java.awt.BorderLayout.NORTH);
        }
        return jContentPane;
    }
 
    public void run() {
        // TODO 自动生成方法存根
        while(true){
            try {
                Thread.sleep(800);
                int index=(int)(Math.random()*8);
                if(!lm[index].isShowing()){
                    lm[index].setVisible(true);
                    Thread.sleep(800);
                    lm[index].setVisible(false);
                }
            } catch (InterruptedException e) {
                // TODO 自动生成 catch 块
                e.printStackTrace();
            }
        }
    }
 
    public void mouseClicked(MouseEvent e) {
        // TODO 自动生成方法存根);
        JLabel l=(JLabel)e.getSource();
        if(l.isShowing()){
            t.interrupt();
            l.setVisible(false);
            lscore.setText("你的得分是"+(++score)+"分");
        }
        clip2.play();
    }
 
    public void mouseEntered(MouseEvent e) {
        // TODO 自动生成方法存根
         
    }
 
    public void mouseExited(MouseEvent e) {
        // TODO 自动生成方法存根
         
    }
 
    public void mousePressed(MouseEvent e) {
        // TODO 自动生成方法存根
        this.setCursor(c2);
    }
 
    public void mouseReleased(MouseEvent e) {
        // TODO 自动生成方法存根
        this.setCursor(c1);
    }
 
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        int value=progressBar.getValue();
        //System.out.println(value);
        if(value<10)
        this.progressBar.setValue(++value);
        else {
            JOptionPane.showMessageDialog(this,"时间到 !  你的分数是："+score+"分!   ","游戏提示!  ",JOptionPane.WARNING_MESSAGE);
            mstart.setEnabled(true);   
            progressBar.setValue(0);
            progressBar.setString("游戏结束了！");
            timer.stop();
            t.suspend();
            score=0;
            threadstarted=true;
             
            int heroid=0;
            for(int i=0;i<sc.length;i++) {
                if(score>sc[i].score) {
                    findhero=true;
                    heroid=1;
                    for(int j=sc.length-1;j>i;j--) {
                        sc[j].id=sc[j-1].id;
                        sc[j].score=sc[j-1].score;
                    }
                    sc[i].id="匿名";
                    sc[i].score=score;
                    break;
                    }
                }
            if(findhero) {
                for(int j=sc.length-1;j>heroid;j--) {
                    sc[j].id=sc[j-1].id;
                    sc[j].score=sc[j-1].score;
                }
                sc[heroid].id="匿名";
                sc[heroid].score=score;
                String str=JOptionPane.showInputDialog(this,"英雄请留下大名吧！" ,JOptionPane.PLAIN_MESSAGE);
                //if(heroname!=null) sc[heroid].id=heroname;
                //sc[heroid].id=str;
                OutputStream os;
                try {
                    os=new FileOutputStream(file);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    oos.writeObject(sc);
                    oos.close();
                    os.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                 
            }
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
