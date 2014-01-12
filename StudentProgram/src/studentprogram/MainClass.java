package studentprogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Sam
 */
public class MainClass {

    static JButton button;
    static Socket socket;
    static boolean state;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        state = false;
        try {
            socket = new Socket("192.168.0.22", 42421);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("USERNAME:" + System.getProperty("user.name"));
        } catch (IOException ex) {
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                BufferedReader input = null;
                try {
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                }
                while (true) {
                    try {
                        String read = input.readLine();
                        if (read.equals("DOWN")) {
                            if (state == true) {
                                try {
                                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                    out.println("DOWN");
                                    out.println("QUIT");
                                    button.setText("Hand is DOWN");
                                    state = false;
                                } catch (Exception ex) {
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        button = new JButton("Hand is DOWN");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == false) {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("UP");
                        button.setText("Hand is UP");
                        state = true;
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("DOWN");
                        button.setText("Hand is DOWN");
                        state = false;
                    } catch (Exception ex) {
                    }
                }
            }
        });
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //call terminate
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("DOWN");
                } catch (IOException ex) {
                }
            }
        });
        f.add(button);
        f.setSize(400, 100);
        f.setVisible(true);
    }
}