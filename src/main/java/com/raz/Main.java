package com.raz;

import com.raz.method.DataLoader;
import com.raz.method.processors.controller.DFSController;
import com.raz.method.processors.controller.FCFSController;
//import com.raz.method.processors.controller.GreedyController;
import com.raz.method.processors.controller.ShortestFirst;
import org.apache.commons.cli.*;

import java.io.IOException;

import static com.raz.utils.Utils.debug;

public class Main {
    public static void main(String[] args) throws IOException {
        Options options = new Options();
        options.addOption("i", "input", true, "Input file path")
                .addOption("g", "generate", false, "Flag to generate data")
                .addOption("n", "number", true, "Set number of orders to generate")
                .addOption("r", "range", true, "Set the range for generated coordinate")
                .addOption("h", "help", false, "Print help information")
                .addOption("dfs", "dfs", false, "Use brute force dfs controller")
                .addOption("fcfs", "fcfs", false, "Use first come first serve controller")
                .addOption("sjf", "sjf", false, "Use shortest job first controller")
                .addOption("all", "all", false, "try all the controller")
                .addOption("out", "output", true, "output file path")
                .addOption("dataout", "dataout", true, "output the loaded data");


        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("DroneDelivery", options);
            return;
        }

        if (cl.hasOption("h")) {
            formatter.printHelp("DroneDelivery", options);
            return;
        }

        // data loader
        DataLoader dl = null;
        if (cl.hasOption("i")) {
            String path = cl.getOptionValue("i");
            try {
                dl = new DataLoader(path);
            } catch (IOException e) {
                System.out.println("Error in loading file.");
            }
        } else {
            int defaultNum = 15;
            int defalutCoor = 30;

            if(cl.hasOption("g")) {
                if(cl.hasOption("n")) defaultNum = Integer.parseInt(cl.getOptionValue("n"));
                if(cl.hasOption("r")) defalutCoor = Integer.parseInt(cl.getOptionValue("r"));
            }

            dl = new DataLoader(defaultNum, defalutCoor);
            System.out.println(dl.toString());
        }

        if(dl == null || dl.getData().size() == 0) {
            System.out.println("data loading error, maybe empty data.");
            return;
        }

        if(cl.hasOption("dataout")) {
            String outPath = cl.getOptionValue("dataout");
            dl.exportFile(outPath);
        }

        String path = null;
        String defaultOutputPath = "D:/output";
        if(cl.hasOption("out")) {
            path = cl.getOptionValue("out");
        }

        if(cl.hasOption("all")) {
            ShortestFirst SFController = new ShortestFirst();
            SFController.dispatch(dl.getData());
            SFController.reportRes("Shortest Job First");

            FCFSController FCFScontroller = new FCFSController();
            FCFScontroller.dispatch(dl.getData());
            FCFScontroller.reportRes("First Come First Serve");

            DFSController DFScontroller = new DFSController();
            DFScontroller.dispatch(dl.getData());
            DFScontroller.reportRes("Brute Force DFS");
        } else if(cl.hasOption("dfs")) {
            DFSController DFScontroller = new DFSController();
            DFScontroller.dispatch(dl.getData());
            if(path == null) {
                DFScontroller.reportRes("Brute Force DFS");
                DFScontroller.exportToPath(defaultOutputPath);
                System.out.println("Output to path: " + defaultOutputPath);
            } else {
                DFScontroller.exportToPath(path);
            }
        } else if(cl.hasOption("fcfs")) {
            FCFSController FCFScontroller = new FCFSController();
            FCFScontroller.dispatch(dl.getData());
            if(path == null) {
                FCFScontroller.reportRes("First Come First Serve");
                FCFScontroller.exportToPath(defaultOutputPath);
                System.out.println("Output to path: " + defaultOutputPath);
            } else {
                FCFScontroller.exportToPath(path);
            }
        } else if(cl.hasOption("sjf")) {
            ShortestFirst SFController = new ShortestFirst();
            SFController.dispatch(dl.getData());
            if(path == null) {
                SFController.reportRes("Shortest Job First");
                SFController.exportToPath(defaultOutputPath);
                System.out.println("Output to path: " + defaultOutputPath);
            } else {
                SFController.exportToPath(path);
            }
        }
    }
}
