package pers.chemyoo.core.system;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JWindow;

/**
 *  右下角弹出式提示框
 *  1.自动上升
 *  2.停留一段时间
 *  3.自动下降直至消失
 *  4.线程控制窗口的出现和消失，同时添加鼠标事件控制，可以提前使提示框消失
 *  5.鼠标事件结合自己的需求实现，此处只是实现一个点击事件
 */
public class TipMessage extends JWindow implements Runnable, MouseListener {
    private static final long serialVersionUID = -3564453685861233338L;
    private Integer screenWidth;  // 屏幕宽度
    private Integer screenHeight; // 屏幕高度
    private Integer windowWidth = 380; // 设置提示窗口宽度
    private Integer windowHeight = 90; // 设置提示窗口高度
    private Integer bottmToolKitHeight; // 底部任务栏高度，如果没有任务栏则为零
    private Integer stayTime = 30 * 1000; // 提示框停留时间
    private Integer x; // 窗口起始X坐标
    private Integer y; // 窗口起始Y坐标
    private String title = "温馨提示:";
    private String message = "No Message!";
    private JLabel messageLabel; // 内容标签

    public TipMessage() {
        this.init();
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public TipMessage(String message) {
    	this.setMessage(message);
        this.init();
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public String getMessage() {
    	 return this.message;
    }
    
    private void setMessage(String message) {
    	this.message = "<html><body><p>" + message + "</p></body></html>";
    }

    private void init() {
        bottmToolKitHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration()).bottom;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = dimension.width;
        this.screenHeight = dimension.height;

        this.x = screenWidth - windowWidth;
        this.y = this.screenHeight;
        this.setLocation(x, y - bottmToolKitHeight - windowHeight);
        
        this.messageLabel = new JLabel(this.message);
        this.messageLabel.setForeground(Color.BLACK);
        this.messageLabel.setBackground(new Color(255, 228, 181));
        this.messageLabel.setSize(windowWidth, windowHeight - 20);
        Font font = new Font("TimesRoman", Font.BOLD, 14);
		this.messageLabel.setFont(font);
        
        this.getRootPane().setToolTipText(title);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.setSize(windowWidth, windowHeight);
        this.setAlwaysOnTop(false);
        this.getContentPane().setBackground(new Color(255, 228, 181));
        this.getContentPane().add(messageLabel);
        this.addMouseListener(this);
        Toolkit.getDefaultToolkit().beep(); // 播放系统声音，提示一下
        this.setVisible(true);
    }
    

    @Override
    public void run() {
        Integer delay = 10;
        Integer step = 1;
        Integer end = windowHeight + bottmToolKitHeight;
        this.setFocusableWindowState(true);
        this.requestFocus();
        while (true) {
            try {
                step++;
                y = y - 1;
                this.setLocation(x, y);
                if (step > end) {
                    Thread.sleep(stayTime);
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            	e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        step = 1;
        while (true) {
            try {
                step++;
                y = y + 1;
                this.setLocation(x, y);
                if (step > end) {
                    this.dispose();
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            	e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dispose();
    }
    
	@Override
	public void mousePressed(MouseEvent e)
	{
		// ignore
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// ignore
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// ignore
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// ignore
	}
	
	public static void main(String[] args) {
		new TipMessage("fasdfjao喝咖啡或打算fasdfjao喝咖啡或打算fasdfjao喝咖啡或打算fasdfjao喝咖啡或打算fasdfjao喝咖啡或打算");
	}
	
}