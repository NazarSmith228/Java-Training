package org.java.training.web.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.util.Comparator.comparing;

@UtilityClass
public class NasaHttpClients {

    private final String MARS_PHOTOS_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=14&api_key=DEMO_KEY";
    private final String IMAGE_SOURCE_TAG = "img_src";

    @SneakyThrows
    public void getAllImageLocations() { //UrlConnection
        var url = new URL(MARS_PHOTOS_URL);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        @Cleanup InputStream inputStream = connection.getInputStream();

        var reader = new BufferedReader(new InputStreamReader(inputStream));
        var mapper = new ObjectMapper();

        JsonNode jsonContent = mapper.readTree(reader.readLine());
        jsonContent.findValues(IMAGE_SOURCE_TAG).forEach(System.out::println);
    }

    @SneakyThrows
    public void findLargestImage() { //Java HttpClient
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MARS_PHOTOS_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        var mapper = new ObjectMapper();
        JsonNode jsonContent = mapper.readTree(response.body());

        jsonContent.findValues(IMAGE_SOURCE_TAG).stream()
                .map(JsonNode::asText)
                .map(NasaHttpClients::makePair)
                .max(comparing(Pair::getRight))
                .ifPresent(pair -> {
                    System.out.println("Image url: " + pair.getLeft());
                    System.out.println("Image size in bytes: " + pair.getRight());
                });
    }

    @SneakyThrows
    private Pair<String, Integer> makePair(String url) {
        URL imageUrl = new URL(url.replace("http", "https"));
        byte[] content = IOUtils.toByteArray(imageUrl.openStream());
        return Pair.of(url, content.length);
    }

    @SneakyThrows
    public void findSmallestImg() { //Apache HttpClient
        @Cleanup var client = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy()) //???
                .build();

        var request = new HttpGet(MARS_PHOTOS_URL);

        CloseableHttpResponse response = client.execute(request);
        @Cleanup InputStream content = response.getEntity().getContent();

        var mapper = new ObjectMapper();
        JsonNode jsonContent = mapper.readTree(content);

        jsonContent.findValues(IMAGE_SOURCE_TAG).stream()
                .map(JsonNode::asText)
                .min(comparing(imgSource -> getImageSize(imgSource, client)))
                .ifPresent(url -> {
                    System.out.println("Image url: " + url);
                    System.out.println("Image size in bytes: " + getImageSize(url, client));
                });
    }

    @SneakyThrows
    private Long getImageSize(String imgUrl, CloseableHttpClient httpClient) {
        var headRequest = new HttpHead(imgUrl);
        Header contentLengthHeader = httpClient.execute(headRequest).getFirstHeader("Content-Length");
        return Long.parseLong(contentLengthHeader.getValue());
    }
}
