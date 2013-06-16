package com.datapublica.scraping.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MarmitonJsoupScraper implements Runnable {

    private static final String SEARCH_URL = "http://www.marmiton.org/recettes/recette_financiers-revisites-aux-noisettes-et-pepites-de-chocolat_305030.aspx";
    private static final String CHARSET = "UTF-8";

    public void run() {
        try {
            Document doc = Jsoup.connect(SEARCH_URL).get();
            String title = doc.title();

            System.out.println("Title: " + title);

            System.out.println("");

            Elements tags = doc.select("div.m_content_recette_breadcrumb");
            System.out.println("Tags: " + tags.text());

            System.out.println("");

            Elements ingredientsBlock = doc.select("p.m_content_recette_ingredients");

            Elements ingredientsInfo = ingredientsBlock.select("span");
            System.out.println("Ingredients info: " + ingredientsInfo.text());

            System.out.println("");

            ingredientsBlock.select("span").remove();

            String ingredientsAsHtml = ingredientsBlock.html();

            for(String text : ingredientsAsHtml.split("<br />")){
                System.out.println("Ing-> " + text);
            }

            System.out.println("");

            Elements stepBlock = doc.select("div.m_content_recette_todo");
            System.out.println("Steps-> " + stepBlock.html());

            for(String text : stepBlock.html().split("<br />")){
                System.out.println("Steps-> " + text);
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String... args) {
        new MarmitonJsoupScraper().run();
    }
}
