import org.apache.commons.cli.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class DocuSearchMainClass {
    public static void main(String[] args) throws IOException, ParseException, SAXException, TikaException {
        Options options = DocuSearchMainClass.getCliOptions();
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
                searcher.query(query);
            } else {
                throw new org.apache.commons.cli.ParseException("One flag should be specified (--indexFiles or --queryFiles)");
            }
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("docu-search", options);
        }
    }

    private static Options getCliOptions() {
        Options options = new Options();
        OptionGroup flags = new OptionGroup();

        Option indexFlag = new Option(null, "indexFiles", false, "flag for indexing files from a given path");
        Option queryFlag = new Option(null, "queryFiles", false, "flag for query files given an inverted index path");
        flags.addOption(indexFlag);
        flags.addOption(queryFlag);

        Option docsPath = new Option("d", "docsPath", true, "path of the docs directory to be indexed");
        Option indexPath = new Option("i", "indexPath", true, "path where the inverted index will reside");
        Option query = new Option("q", "query", true, "query string");



        options.addOptionGroup(flags);
        options.addOption(docsPath);
        options.addOption(indexPath);
        options.addOption(query);

        return options;
    }
}
