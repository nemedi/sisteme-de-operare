package cfs.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerConfiguration {

    @Value("${fs.root}")
    private String root;

    public String getRoot() {
        return root;
    }
}