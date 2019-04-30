//import com.github.sarxos.webcam.Webcam;
//import com.github.sarxos.webcam.WebcamPanel;
//import com.github.sarxos.webcam.WebcamResolution;
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.io.File;
//import java.io.IOException;
//import java.io.*;
//
//public class main {
//    public static void main(String[] args) throws InterruptedException, IOException {
//        while(true) {
//            Webcam webcam = Webcam.getDefault();
//            Dimension[] myCustomResolutions = new Dimension[]{
//                    WebcamResolution.HD.getSize(),
//            };
//            webcam.setCustomViewSizes(myCustomResolutions);
//            webcam.setViewSize(myCustomResolutions[0]);
//            //set up the top frame
//            WebcamPanel webcamPanel = new WebcamPanel(webcam);
//            webcamPanel.setImageSizeDisplayed(true);
//            webcamPanel.setFPSDisplayed(true);
//            webcamPanel.setMirrored(false); //mirror the input? (CHECK WORDS)
//            webcamPanel.setDisplayDebugInfo(true); //debug info
//            webcam.open();
//
//            JFrame display = new JFrame();
//            display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            display.setLayout(new GridLayout(2, 2));
//            JTextArea consolePanel = new JTextArea();
//
//            JButton button = new JButton(new AbstractAction("Take Snapshot Now") {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    try {
//                        String name = "OCR.png";
//                        ImageIO.write(webcam.getImage(), "JPG", new File(name));
//                        System.out.format("File %s has been saved\n", name);
//                        consolePanel.append("File " + name + " has been saved.");
//                        Thread t1 = new Thread(new myCompiler());
//                        t1.start();
//                    } catch (IOException t) {
//                        t.printStackTrace();
//                    }
//                }
//            });
//            display.add(webcamPanel, BorderLayout.CENTER);
//            display.add(button, BorderLayout.EAST);
//            display.add(consolePanel, BorderLayout.SOUTH);
//
//            display.pack();
//            display.setVisible(true);
//        }
//    }
//
//
//    public static class OSCheck {
//        private static String OS = System.getProperty("os.name").toLowerCase();
//        public static String run(){
//            System.out.println("CURRENT OS: "+OS);
//            if(isWindows()){
//                System.out.println("Windows!");
//                return "windows";
//            } else if(isMac()){
//                return "macOS";
//            } else if (isUnix()){
//                return "linux";
//            } else {
//                System.out.println("Unsupported!");
//                return "";
//            }
//        }
//        public static boolean isWindows() {
//
//            return (OS.indexOf("win") >= 0);
//
//        }
//
//        public static boolean isMac() {
//
//            return (OS.indexOf("mac") >= 0);
//
//        }
//
//        public static boolean isUnix() {
//
//            return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
//
//        }
//    }
//}
//class myCompiler implements Runnable {
//
//    @Override
//    public void run() {
//        //Demo code to create a Text file for a java hello world program
//
//        System.out.println("BEGINNING COMPILING PROCESS");
//        String[] values = {"public class collectedText {","\tpublic static void main(String[] args){","\t\tSystem.out.println(\"Hello world!\");","\t}","}"};
//
//        //Determine the string name of the OS we're running on
//        String os = main.OSCheck.run();
//        if (os.equals("linux")){
//            File file = new File("/home/insilvon/Desktop/collectedText.txt");
//            BufferedWriter writer = null;
//            try {
//                writer = new BufferedWriter(new FileWriter(file));
//                for(String line : values){
//                    writer.write(line);
//                    writer.write("\n");
//                }
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            ProcessBuilder processBuilder = new ProcessBuilder("/home/insilvon/Desktop/demo.sh");
//            runProcess(processBuilder);
//        }
//        if (os.equals("windows")){
//            //Load the current output.txt file (THIS WILL ALL BE REPLACED BY THE PYTHON PROGRAM LATER)
//            File file = new File("C:\\Users\\colin\\Documents\\Batch\\collectedText.txt");
//            BufferedWriter writer = null;
//            try {
//                writer = new BufferedWriter(new FileWriter(file));
//                for(String line : values){
//                    writer.write(line);
//                    writer.write("\n");
//                }
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //Create a process on this machine to run the batch file. The batch file will execute the compilation stuff.
//            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\colin\\Documents\\Batch\\compile.bat");
//            runProcess(processBuilder);
//        }
//    }
//    private static void runProcess(ProcessBuilder processBuilder) {
//        try {
//            //Begin the process
//            Process process = processBuilder.start();
//            StringBuilder output = new StringBuilder();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            //Print out the console lines to the IDE's console while waiting
//            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
//            }
//            //Wait for the console to end, then end this program
//            int exitVal = process.waitFor();
//            if (exitVal == 0) {
//                System.out.println(output);
//                System.exit(0);
//            } else {
//                //uh
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//        } catch (InterruptedException e){
//            e.printStackTrace();
//        }
//    }
//}
