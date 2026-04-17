package cfs.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class FileSystemRestClient {

    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileSystemRestClient(String baseUrl) {
        this.baseUrl = baseUrl + "/fs";
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }
    
    public List<String> list(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(String.format("/list?path=%s", encode(path))))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("list failed: " + response.statusCode());
        }
        return mapper.readValue(
                response.body(),
                mapper.getTypeFactory().constructCollectionType(
                        List.class,
                        String.class
                )
        );
    }

    public FileStatDTO stat(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(String.format("/stat?path=%s",
                		encode(path))))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("stat failed: " + response.statusCode());
        }
        return mapper.readValue(response.body(), FileStatDTO.class);
    }

    public byte[] read(String path, long offset, int size) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(String.format("/read?path=%s&offset=%d&size=%d",
                		encode(path), offset, size)))
                .GET()
                .build();
        HttpResponse<byte[]> response =
                client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) {
            throw new RuntimeException("read failed: " + response.statusCode());
        }
        return response.body();
    }

    public int write(String path, long offset, byte[] data) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(String.format("/write?path=%s&offset=%d",
                		encode(path), offset)))
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("write failed: " + response.statusCode());
        }
        return Integer.parseInt(response.body());
    }

    public void create(String path) throws Exception {
        requestPost(String.format("/create?path=%s",
        		encode(path)));
    }

    public void delete(String path) throws Exception {
        requestDelete(String.format("/delete?path=%s",
        		encode(path)));
    }

    public void mkdir(String path) throws Exception {
        requestPost(String.format("/mkdir?path=%s",
        		encode(path)));
    }
    
    public void rmdir(String path) throws Exception {
        requestDelete(String.format("/rmdir?path=%s", encode(path)));
    }

    public void rename(String from, String to) throws Exception {
        requestPost(String.format("/rename?from=%s&to=%s",
        		encode(from), encode(to)));
    }

    public void truncate(String path, long size) throws Exception {
        requestPost(String.format("/truncate?path=%s&size=%d",
        		encode(path), size));
    }
    
    public void utimens(String path, long atime, long mtime) throws Exception {
        requestPost(String.format("/utimens?path=%s&atime=%d&mtime=%d",
        		encode(path), atime, mtime));
    }

    private URI uri(String pathAndQuery) {
        return URI.create(baseUrl + pathAndQuery);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private void requestPost(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(path))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<Void> response =
                client.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("POST failed: " + response.statusCode());
        }
    }

    private void requestDelete(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri(path))
                .DELETE()
                .build();
        HttpResponse<Void> response =
                client.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("DELETE failed: " + response.statusCode());
        }
    }
}