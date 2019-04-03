package CoreLogic;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import SwingInterface.SwingMainFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, SAXException, TikaException {
        Options options = Main.getCliOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("indexFiles")) {
                String docsPath = commandLine.getOptionValue("d");
                String invertedIndexPath = commandLine.getOptionValue("i");

                if (docsPath == null || invertedIndexPath == null) {
                    throw new org.apache.commons.cli.ParseException("Inverted Index Path and Docs Path must be both present");
                }

                Indexer indexer = new Indexer(docsPath, invertedIndexPath);
                indexer.generateIndex();
            } else if (commandLine.hasOption("queryFiles")) {
                String invertedIndexPath = commandLine.getOptionValue("i");
                String query = commandLine.getOptionValue("q");

                if (invertedIndexPath == null || query == null) {
                    throw new org.apache.commons.cli.ParseException("Inverted Index Path and Query String must be both present");
                }

                Searcher searcher = new Searcher(invertedIndexPath);
                printResults(searcher.query(query));
            } else if (commandLine.hasOption("queryLoop")) {
                String invertedIndexPath = commandLine.getOptionValue("i");

                if (invertedIndexPath == null) {
                    throw new org.apache.commons.cli.ParseException("Inverted Index Path and Query String must be both present");
                }

                Searcher searcher = new Searcher(invertedIndexPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                while (true) {

                    System.out.println(">>>>> ENTER YOUR QUERY:");
                    String input = br.readLine();

                    if ("exit".equals(input)) {
                        System.exit(0);
                    }

                    printResults(searcher.query(input));
                }

            } else if(commandLine.hasOption("swingInterface")) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        JFrame frame = new SwingMainFrame("Information Retrieval - Project I");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                        frame.setSize(1024, 550);
                    }
                });
            } else {
                throw new org.apache.commons.cli.ParseException("One flag should be specified (--indexFiles, --queryFiles, --queryLoop, --swingInterface)");
            }
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("information-retrieval-p1", options);
        }


        
    }

    private static Options getCliOptions() {
        Options options = new Options();
        OptionGroup flags = new OptionGroup();

        Option indexFlag = new Option(null, "indexFiles", false, "flag for indexing files from a given path");
        Option queryFlag = new Option(null, "queryFiles", false, "flag for query files given an inverted index path");
        Option queryLoopFlag = new Option(null, "queryLoop", false, "flag for query option to not stop after one query");
        Option swingInterfaceFlag = new Option(null, "swingInterface", false, "flag to start user interface");
        flags.addOption(indexFlag);
        flags.addOption(queryFlag);
        flags.addOption(queryLoopFlag);
        flags.addOption(swingInterfaceFlag);

        Option docsPath = new Option("d", "docsPath", true, "path of the docs directory to be indexed");
        Option indexPath = new Option("i", "indexPath", true, "path where the inverted index will reside");
        Option query = new Option("q", "query", true, "query string");

        options.addOptionGroup(flags);
        options.addOption(docsPath);
        options.addOption(indexPath);
        options.addOption(query);

        return options;
    }

    private static void printResults(ArrayList<Result> result) {
        int i = 0;
        for(Result entry : result) {
            i++; 
            System.out.println(i + ") [Score: "+entry.getScore() +"]\n    [Path: "+entry.getFilePath()+"]\n\n");
        }

        if (result.size() == 0) {
            System.out.println("NO DOCUMENTS MATCHING YOUR SEARCH");
        }
    }
}
