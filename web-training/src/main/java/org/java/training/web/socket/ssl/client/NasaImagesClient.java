package org.java.training.web.socket.ssl.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.java.training.web.socket.ssl.response.ResponseType;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.java.training.web.socket.constant.SocketConstants.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NasaImagesClient {

    private static final String IMAGE_TAG = "img_src";
    private static final Map.Entry<String, Long> NOT_FOUND_ENTRY = Map.entry("Not found", -1L);

    SSLHelper sslHelper = new SSLHelper();
    ObjectMapper mapper = new ObjectMapper();
    URL nasaUrl;
    List<String> imageURLs;

    public NasaImagesClient(String url) {
        this.nasaUrl = sslHelper.createUrl(url);
        this.imageURLs = getAllImages();
    }

    public Map.Entry<String, Long> findImageWithSize() {
        return findImageWithSize(Comparator.naturalOrder());
    }

    public Map.Entry<String, Long> findImageWithSize(Comparator<Long> sizeComparator) {
        return constructImageSizeMap()
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue(sizeComparator))
                .orElse(NOT_FOUND_ENTRY);
    }

    private List<String> getAllImages() {
        try {
            String jsonResponse = sslHelper.getForImages(nasaUrl, ResponseType.BODY);
            return mapper.readTree(jsonResponse).findValuesAsText(IMAGE_TAG);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error during parsing response", ex);
        }
    }

    private Map<String, Long> constructImageSizeMap() {
        return imageURLs.parallelStream()
                .map(this::makeEntry)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, Long> makeEntry(String imageURL) {
        URL url = sslHelper.createUrl(imageURL);
        String headers = sslHelper.getForImages(url, ResponseType.HEADERS);

        String redirectUrl = getHeaderValue(headers, LOCATION);

        Long imageSize = isEmpty(redirectUrl)
                ? Long.valueOf(getHeaderValue(headers, CONTENT_LENGTH))
                : makeEntry(redirectUrl).getValue();

        return Map.entry(imageURL, imageSize);
    }

    private String getHeaderValue(String allHeaders, String targetHeader) {
        return Arrays.stream(allHeaders.split(NEWLINE))
                .filter(header -> header.startsWith(targetHeader))
                .findFirst()
                .map(header -> header.split(SEMICOLON_SPACE)[1].stripTrailing())
                .orElse(EMPTY);
    }
}
