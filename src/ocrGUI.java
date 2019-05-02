import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javax.swing.JSplitPane;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.util.concurrent.TimeUnit;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Core class which will create the GUI and all of its elements.
 */
public class ocrGUI extends JFrame {
    private JPanel contentPane;
    static String consoleOutput = "";
    static JPanel panel_2;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        //compile();
        //System.exit(0);
        System.out.println(System.getProperty("user.dir"));
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ocrGUI frame = new ocrGUI();
                    frame.setVisible(true);
                    consoleOutput = compile();
                    JTextArea textArea = new JTextArea(consoleOutput);
                    panel_2.add(textArea);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * @throws FileNotFoundException
     */
    public ocrGUI() throws FileNotFoundException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton("Capture");

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //Webcam webcam = Webcam.getWebcamByName("Microsoft Camera Front 1");
                Webcam webcam = Webcam.getDefault();
                webcam.open();
                try {
                    ImageIO.write(webcam.getImage(),"JPG", new File("OCR.jpg"));
                    System.out.println("Image Captured!");
                    consoleOutput = compile();
                    JTextArea textArea = new JTextArea(consoleOutput);
                    panel_2.add(textArea);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        splitPane.setLeftComponent(btnNewButton);

        JButton btnNewButton_1 = new JButton("Quit");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        splitPane.setRightComponent(btnNewButton_1);

        JSplitPane splitPane_1 = new JSplitPane();
        contentPane.add(splitPane_1, BorderLayout.NORTH);

        //Webcam webcam = Webcam.getWebcamByName("Microsoft Camera Front 1");
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);


        JPanel panel_1 = new JPanel();
        panel_1 = panel;
        splitPane_1.setLeftComponent(panel_1);

        panel_2 = new JPanel();
        splitPane_1.setRightComponent(panel_2);

        JScrollPane scrollPane = new JScrollPane();
        panel_2.add(scrollPane);

        //*******
//        Scanner inFile = new Scanner (new File("C:/Users/tlmye/345v2/345/Read.txt"));
//
//        ArrayList<String> ocrOut = new ArrayList<String>();
//
//        while (inFile.hasNextLine())
//            ocrOut.add(inFile.next());
//        for (String line : ocrOut) {
            System.out.println("WOAHHHHHHHHHHHHHHHHHHHHHHHH");
            System.out.println(consoleOutput);
            JTextArea textArea = new JTextArea("HELLO WORLD");
            panel_2.add(textArea);
//        }
//
//        inFile.close();
    }
    public static String compile() {
        System.out.println("BEGINNING COMPILING PROCESS");

        //Get the OS type
        String os = OSCheck.run();
        //Create the batch/shell files
        generateBatch(os);
        //Get the current paths of all the files we will need.
        String path = System.getProperty("user.dir");
        String outputData = "";
        if (os.equals("linux")) {
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter your linux password, if applicable.");
            String password = in.nextLine();
            //File file = new File("/home/insilvon/Desktop/collectedText.txt");
            try {
                System.out.println(path);
                String command = "echo '"+password+"' | sudo -S chmod 777 "+path+"/linuxOCR.sh && echo '"+password+"' | sudo -S chmod 777 "+path+"/linuxCompile.sh";
                System.out.println(command);
                Process p = Runtime.getRuntime().exec(command);
                p.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ProcessBuilder processBuilder = new ProcessBuilder(path+"/linuxOCR.sh");
            outputData+=runProcess(processBuilder)+"\n\n\n";
            //Do any editing on the text file here
            processBuilder = new ProcessBuilder(path+"/linuxCompile.sh");
            outputData+=runProcess(processBuilder);
            System.out.println(outputData);
            return outputData;
        }
        if (os.equals("windows")) {
            //Run the python script and generate OCR.py
            ProcessBuilder processBuilder = new ProcessBuilder(path+"\\winOCR.bat");
            outputData+=runProcess(processBuilder);

            File createdText = new File("OCR.txt");
            try {
                Scanner reader = new Scanner(createdText);
                while(reader.hasNextLine()){
                    String line = reader.nextLine();
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //compile and run result
            ProcessBuilder temp = new ProcessBuilder(path+"\\winCompile.bat");
            outputData+=runProcess(temp);

            //Do any editing on the text file here
//            processBuilder = new ProcessBuilder(path+"\\winCompile.bat");
//            runProcess(processBuilder);
            //System.out.println(outputData);
            return outputData;
        }
        return "";

    }
    /**
     * Private process which waits for an active terminal/console and will log all data
     * to this console.
     * @param processBuilder
     */
    private static String runProcess(ProcessBuilder processBuilder) {
        try {
            //Begin the process
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Print out the console lines to the IDE's console while waiting
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            //Wait for the console to end, then end this program
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return output.toString();
                //System.exit(0);
            } else {
                //uh
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Private method which will identify the user's operating system and generate two shell/batch files appropriately.
     * The first is osOCR.bat/sh, which will run the OCR.py script.
     * The second is osCompile.bat/sh, which will attempt to compile and run the target text file.
     * @param osName - what OS we've identified.
     */
    private static void generateBatch(String osName){
        String path = System.getProperty("user.dir");
        FileWriter out;
        File osOCR = null;
        File osCompiler = null;

        if(osName.equalsIgnoreCase("linux")){
            osOCR = new File(path+"/linuxOCR.sh");
            osCompiler = new File(path+"/linuxCompile.sh");
            try {
                out = new FileWriter(osOCR);
                out.write("export GOOGLE_APPLICATION_CREDENTIALS=ocr-9301078d40b9.json first on command line \n");
                out.write( "python3 ocr.py OCR.jpg");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out = new FileWriter(osCompiler);
                out.write("cd " + path + "\n");
                out.write("mv OCR.txt OCR.java\n");
                out.write("javac -target 1.8 -source 1.8 OCR.java\n");
                out.write("java OCR");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (osName.equalsIgnoreCase("windows")){
            osOCR = new File(path+"/winOCR.bat");
            osCompiler = new File(path+"/winCompile.bat");
            try {
                out = new FileWriter(osOCR);
                out.write("set GOOGLE_APPLICATION_CREDENTIALS=ocr-9301078d40b9.json" + "\n");
                out.write( "python "+path+"\\ocr.py OCR.jpg");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out = new FileWriter(osCompiler);
                out.write("cd " + path + "\n");
                out.write("move OCR.txt OCR.java\n");
                out.write("javac -target 1.8 -source 1.8 OCR.java\n");
                out.write("java OCR");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
class OSCheck {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String run() {
        System.out.println("CURRENT OS: " + OS);
        if (isWindows()) {
            System.out.println("Windows!");
            return "windows";
        } else if (isMac()) {
            return "macOS";
        } else if (isUnix()) {
            return "linux";
        } else {
            System.out.println("Unsupported!");
            return "";
        }
    }

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

    }
}