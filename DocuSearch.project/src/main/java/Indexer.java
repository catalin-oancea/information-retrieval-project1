import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Paths;

public class Indexer {
    private Analyzer analyzer;

    Indexer() {
        this.analyzer = new MyRomanianAnalyzer(null);
    }

    /**
     * This method is initializing the index writer and starts the indexing process from <code>Consts.DOCS_PATH</code>.
     * @throws IOException Thrown when <code>Consts.INDEX_PATH</code> path does not exist.
     * @throws SAXException Thrown when <code>indexDocs</code> throws <code>SAXException</code>.
     * @throws TikaException Thrown when <code>indexDocs</code> throws <code>TikaException</code>.
     */
    public void generateIndex() throws IOException, SAXException, TikaException{
        File docsDir = new File(Consts.DOCS_PATH);
        Directory dir = FSDirectory.open(Paths.get(Consts.INDEX_PATH));
        IndexWriterConfig iwc = new IndexWriterConfig(this.analyzer);

        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        iwc.setRAMBufferSizeMB(4086.0);

        IndexWriter writer = new IndexWriter(dir, iwc);
        indexDocs(writer, docsDir);

        writer.close();
    }

    /**
     * This method is indexing a set of files (recursively, starting from <code>Consts.DOCS_PATH</code> directory).
     *
     * @param writer The index writer.
     * @param file The directory that contains all the files to be indexed.
     * @throws IOException Thrown when a certain file does not exist.
     * @throws SAXException Thrown when the file cannot be properly parsed.
     * @throws TikaException Thrown when the file cannot be properly parsed.
     */
    private void indexDocs(IndexWriter writer, File file) throws IOException, SAXException, TikaException {
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files != null) {
                for (String f : files) {
                    indexDocs(writer, new File(file, f));
                }
            }
        } else {
            String content = DocumentParser.parseFile(file.getName());
            Document doc = new Document();
            System.out.println(content);


            doc.add(new StringField(Consts.FIELD_PATH_NAME, file.getPath(), Field.Store.YES));
            doc.add(new TextField(Consts.FIELD_CONTENTS_NAME, content, Field.Store.YES));
            writer.addDocument(doc);
        }
    }
}
