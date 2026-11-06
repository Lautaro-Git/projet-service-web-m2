package com.example.projetServiceWeb.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    private DirectoryReader directoryReader;
    private IndexSearcher indexSearcher;
    private final IndexWriter indexWriter;
    private final QueryParser commentaireQueryParser;

    public IndexService() throws IOException {
        String indexDirectoryPath = "index";
        Path path = Paths.get(indexDirectoryPath);
        Directory index = FSDirectory.open(path);

        StandardAnalyzer analyzer = new StandardAnalyzer();
        this.commentaireQueryParser = new QueryParser("content", analyzer);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        this.indexWriter = new IndexWriter(index, indexWriterConfig);
        this.indexWriter.commit();

        if (DirectoryReader.indexExists(index)) {
            this.directoryReader = DirectoryReader.open(index);
            this.indexSearcher = new IndexSearcher(directoryReader);
        } else {
            // si l'index n'existe pas on le creé
            this.indexWriter.commit();
            this.directoryReader = DirectoryReader.open(index);
            this.indexSearcher = new IndexSearcher(directoryReader);
        }

    }

    public void indexEvaluation(Long restaurantId, Long evaluationId, String commentaire){
        try{
            Document document = new Document();
            document.add(new StringField("evaluationId", String.valueOf(evaluationId), Field.Store.YES));
            document.add(new StringField("restaurantId", String.valueOf(restaurantId), Field.Store.YES));
            document.add(new TextField("content", commentaire, Field.Store.YES));
            this.indexWriter.addDocument(document);
            this.indexWriter.commit();

            DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(this.directoryReader);
            if (newDirectoryReader != null) {
                this.directoryReader.close();
                this.directoryReader = newDirectoryReader;
                this.indexSearcher = new IndexSearcher(this.directoryReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //retrait d'une eval de l'index
    public void deleteEvaluation(Long evaluationId){
        try{
            this.indexWriter.deleteDocuments(new Term("evaluationId", String.valueOf(evaluationId)));
            this.indexWriter.commit();

            DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(this.directoryReader);
            if (newDirectoryReader != null) {
                this.directoryReader.close();
                this.directoryReader = newDirectoryReader;
                this.indexSearcher = new IndexSearcher(this.directoryReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //recherche les eval avec au moins un mot cle
    public List<Long> rechercheEvaluations(String motsCle){
        List<Long> listOfId = new ArrayList<>();
        if (motsCle == null || motsCle.isBlank()){
            return listOfId;
        }
        try{
            Query query = this.commentaireQueryParser.parse(QueryParser.escape(motsCle));
            TopDocs resultats = this.indexSearcher.search(query,100);

            for (ScoreDoc scoreDoc : resultats.scoreDocs) {
                Document document = this.indexSearcher.storedFields().document(scoreDoc.doc);
                listOfId.add(Long.parseLong(document.get("evaluationId")));

            }
            return listOfId;
        } catch (IOException | ParseException e){
            throw new RuntimeException(e);
        }
    }

}