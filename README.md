# **Information Retrieval - Project I**
## **Student details**
* Name: Cătălin-Constantin Oancea
* Email: catalin.oancea2008@gmail.com
* Group: 407 - Artificial Inteligence

## **Project Dependencies**
* Apache Tika - version 1.20
* Apache Ant - version 1.10.5
* Apache Lucene - version 8.0.0

## **Project structure**
* `data`
    * `documents` - folder where the documents to be indexed will reside.
    * `index` - folder that will contain the computed inverted index for the documents in the `documents` folder above.
* `resources` - this folder contains a file (`stopwords.txt`) that stores a list of romanian stopwords (without diacritcs)
* `src` - this folder contains 6 java files:
    * `CoreLogic` - contains the implementation for this assignment (Searcher, Indexer, Documnet parser, Analyzer)
        * `DocumentParser.java` - Uses Apache Tika to get the contents of a document from a speciffic path.
        * `Indexer.java` - Uses `DocumentParser.java` to parse a each file from the `../data/documents` folder. After the parsing is done, it uses Lucene to generate and write the index (in the `../data/index` directory).
        * `Main.java` - Triggers the functionality from `Indexer.java` or `Searcher.java` (depending on the user input).
        * `MyRomanianAnalyzer.java` - Duplicate of `RomanianAnalyzer` with a small change (getting rid of diacritics after stemming phase).
        * `Searcher.java` - Used to search the inverted index. Accepts a path to the inverted index and a query string.
    * `SwingInterface` - Contains UI implementation
        * `SwingMainFrame` - This is the main frame, which has a border layout. On the left side (`WEST`) there is the `SwingIndexingPanel`. On the right side (`CENTER`) there is the `SwingSearchingPanel`.
        * `SwingIndexingPanel` - This panel contains two textfields (one where you can pick the path for docs folder, and one where you can pick the path to where the inverted index will be stored). You cannot change the fields manually, but by using the buttons on their right.
        * `SwingSearchingPanel` - This panel contains a textfield (used to type in your query), a button (used to trigger the search), and a textarea (used to display the results of your search).
* `lib` - In this folder the dependencies (`.jar` files) should be present.

## **How to run the project**
### **Using the User Interface (Swing)**
In the `Information_Retrieval_p1` directory, run the following command:
```bash
ant run -Dargs="--swingInterface"
```
This should open a window with two panels:
* The left panel is used for indexing. It has two fields, and one button:
    * Documents path field: the path to the directory containing the files you want to index;
    * Inverted index path field: the path to the directory where the inverted index will be stored;
    * Refresh inverted index button: used to trigger a reindexing (inverted index files will reside in the folder provided in the field above);

Initial View                     |  Search Results
:-------------------------------:|:-------------------------------:
![](./screenshot1.png?raw=true ) | ![](./screenshot2.png?raw=true)


### **Using command line**
When using command line, one of the following flags must be present:
* `--indexFiles` - This will trigger a reindexing of the files in the folder specified by the `-d` or `--docsPath`, and it will store the index in the folder specified by `-i` or `--indexPath`.
* `--queryFiles` - This will load the index from the path specified by `-i` or `--indexPath` and it will perform a search using the query specified by `-q` or `--query`.
* `--queryLoop` - This flag will trigger an interactive search (where the query is introduced from stdin) on the index specified by `-i` or `--indexPath`.

**Examples on how to run the project from command line**
* Generating an index:
```bash
ant run -Dargs="--indexFiles -d /Users/Catalin/Desktop/Projects/information_retrieval_p1/data/documents -i /Users/Catalin/Desktop/Projects/information_retrieval_p1/data/index"
```
* Performing a search on the inverted index:
```bash
ant run -Dargs="--queryFiles -i /Users/Catalin/Desktop/Projects/information_retrieval_p1/data/index -q 'căruță'"
```
* Interactive search on the inverted index (after one search, you can type another query). The execution stops when you type `exit`:

```bash
ant run -Dargs="--queryLoop -i /Users/Catalin/Desktop/Projects/information_retrieval_p1/data/index"
```