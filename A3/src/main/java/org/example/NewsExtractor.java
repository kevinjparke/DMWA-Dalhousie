package org.example;

//Api Imports
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NewsExtractor {
    private final String NEWS_APP_API_KEY = "66975f9f0a9943939a94ca21e3bf65c9";
    private final List<String> KEY_WORDS;
    private NewsApiClient newsApiClient;


    public NewsExtractor(List<String> keyWords) throws IOException{
        this.KEY_WORDS = keyWords;
    }

    public void execute() throws IOException {
        newsApiClient = new NewsApiClient(NEWS_APP_API_KEY);
        int itr = 0;
        //Iterate over each news item containing the current word
        for(int i = 0; i < KEY_WORDS.size(); i++) {
            int finalI = i;
            //Txt files created to fulfil document requirements.
            File file = new File(KEY_WORDS.get(finalI) + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q(KEY_WORDS.get(finalI))
                            //Maximum requests for developer account per NewsApp API documentation.
                            .pageSize(100)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        //Iterate over each article
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            //Get title and description
                            for(int j = 0; j < response.getArticles().size(); j++) {
                                int finalJ = j;
                                try {
                                    fileWriter.write("[TITLE BEGINNING]");
                                    fileWriter.write(response.getArticles().get(finalJ).getTitle());
                                    fileWriter.write("[TITLE END]");
                                    fileWriter.write("[DESCRIPTION BEGINNING]");
                                    fileWriter.write(response.getArticles().get(finalJ).getDescription());
                                    fileWriter.write("[DESCRIPTION END]");
                                    fileWriter.flush();
                                } catch (IOException e) {
                                    System.out.println("Something went wrong with writing to the file");
                                    System.out.println(e);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    }
            );
        }
    }
}
