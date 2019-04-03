package CoreLogic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Searcher {
    private Analyzer analyzer;
    private String invertedIndexPath;

    /**
     * Constructor.
     *
     * @param invertedIndexPath Path to the inverted index.
     */
    public Searcher(String invertedIndexPath) {
        this.invertedIndexPath = invertedIndexPath;
        this.analyzer = new MyRomanianAnalyzer();
    }

    /**
     * Used to search the <code>query</code> string in the inverted index.
     *
     * @param query Query string that is searched in the inverted index.
     * @return Returns an array with the results.
     * @throws IOException Thrown when a file is not found or when AccessError on a file.
     * @throws ParseException Thrown when the query string cannot be parsed.
     */
    public ArrayList<Result> query(String query) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get(this.invertedIndexPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        ArrayList<Result> results = new ArrayList<>();

        QueryParser parser = new QueryParser("contents", this.analyzer);
        Query q = parser.parse(query);
        ScoreDoc[] hits = indexSearcher.search(q, 1000).scoreDocs;

        for (ScoreDoc hit : hits) {
            Document hitDoc = indexSearcher.doc(hit.doc);
            results.add(new Result(hitDoc.get("file_path"), hit.score));
        }

        reader.close();
        dir.close();

        return results;
    }
}
