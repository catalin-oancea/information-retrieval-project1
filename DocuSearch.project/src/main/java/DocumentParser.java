import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class DocumentParser {

    /**
     * Extract document content (by auto-detecting content type) using Apache Tika.
     *
     * @param filePath The file location of the document to be parsed.
     * @return The string representation of the file contents.
     * @throws IOException Thrown when a certain file does not exist.
     * @throws SAXException Thrown when the file cannot be properly parsed.
     * @throws TikaException Thrown when the file cannot be properly parsed.
     */
    public static String parseFile(String filePath) throws IOException, SAXException, TikaException {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        try (InputStream stream = DocumentParser.class.getResourceAsStream("docs/"+filePath)) {
            parser.parse(stream, handler, metadata);
            return handler.toString();
        }
    }
}
