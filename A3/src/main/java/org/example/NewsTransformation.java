/**
 * This file contains code found on https://ihateregex.io/expr/emoji/
 * The regex pattern used was copied verbatim.
 */

package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class NewsTransformation {
    private final List<String> KEYWORDS;

    //Regex patterns for transformation
    private final String SPECIAL_CHAR = "[^\\w\\s:_\\$%@!-\\.&]";
    //Reference: https://ihateregex.io/expr/emoji/
    //Changes made: none;
    private final String EMOJI_CHAR = "(\\u00a9|\\u00ae|[\\u2000-\\u3300]|\\ud83c[\\ud000-\\udfff]|\\ud83d[\\ud000-\\udfff]|\\ud83e[\\ud000-\\udfff])";
    private final String HTML_TAGS = "^<.*?>$";
    private final String LINKS = "https?://.*/";
    private final String TITLE_DELIMITER = "(\\[TITLE BEGINNING\\] | \\[TITLE END\\])";
    private final String DESCRIPTION_DELIMITER = "(\\[DESCRIPTION BEGINNING\\] | \\[DESCRIPTION END\\])";
    private final String uri = "mongodb+srv://kv736815:Qo6QSr9edio6BXYe@bigmongonews.7fgwkyh.mongodb.net/?retryWrites=true&w=majority";
    MongoCollection collection = null;

    public NewsTransformation(List<String> keywords) {
        this.KEYWORDS = keywords;
    }

    public void execute() throws IOException {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        List<NewsArticle> documents = new ArrayList<>();

        for(int i = 0; i < KEYWORDS.size(); i++) {
            final int finalI = i;
            File file = new File(KEYWORDS.get(i) + ".txt");
            Scanner scanner = new Scanner(file);

            StringBuilder data = new StringBuilder();

            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine());
            }

            String newData = data.toString();
            String[] articles = newData.split("\\[DESCRIPTION END\\]\\[TITLE BEGINNING\\]");
            for (String item : articles) {
                NewsArticle newsArticle = new NewsArticle();
                //Split article into title and description.
                String[] content = item.split("\\[TITLE END\\]\\[DESCRIPTION BEGINNING\\]");
                String title = clean(content[0].replaceAll(TITLE_DELIMITER, ""));
                String description = clean(content[1].replaceAll(DESCRIPTION_DELIMITER, ""));
                newsArticle.title = title;
                newsArticle.description = description;
                documents.add(newsArticle);
            }
            scanner.close();
        }
        try (MongoClient mongoClient = MongoClients.create(uri);){
            MongoDatabase repository = mongoClient.getDatabase("BigMongoNews").withCodecRegistry(pojoCodecRegistry);
            collection = repository.getCollection("NewsArticles", NewsArticle.class);
            collection.insertMany(documents);
        }
    }

    private String clean(String content) {
//        //Remove delimiters
        String removedDelimiter1 = content.replaceAll(TITLE_DELIMITER, "");
        String removedDelimiter2 = removedDelimiter1.replaceAll(DESCRIPTION_DELIMITER, "");
//        Remove html tags
        String removedHTMLTags = removedDelimiter2.replaceAll(HTML_TAGS, "");
//        //Remove URL
        String removedLinks = removedHTMLTags.replaceAll(LINKS, "");
//        //Remove unicode characters
        String removedEmoji = removedLinks.replaceAll(EMOJI_CHAR, "");
//        //Remove special characters
        String cleanedStr = removedEmoji.replaceAll(SPECIAL_CHAR, "");

        return cleanedStr;
    }
}
