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

public class Searcher {
    private Analyzer analyzer;

    Searcher() {
        this.analyzer = new MyRomanianAnalyzer(null);
    }

    public void query(String query) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get(Consts.INDEX_PATH));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser(Consts.FIELD_CONTENTS_NAME, this.analyzer);
        Query q = parser.parse(query);
        ScoreDoc[] hits = indexSearcher.search(q, 1000).scoreDocs;

        for (ScoreDoc hit : hits) {
            Document hitDoc = indexSearcher.doc(hit.doc);
            System.out.println(hitDoc.get(Consts.FIELD_PATH_NAME));
        }

        reader.close();
        dir.close();
    }
}
