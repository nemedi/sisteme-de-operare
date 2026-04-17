package cfs.server;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fs")
public class FileSystemRestController {

    private final FileService fileService;

    public FileSystemRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/stat")
    public FileStatDTO stat(@RequestParam String path) throws IOException {
        return fileService.stat(path);
    }

    @GetMapping("/list")
    public List<String> list(@RequestParam String path) throws Exception {
        return fileService.list(path);
    }

    @GetMapping("/read")
    public byte[] read(@RequestParam String path,
                       @RequestParam long offset,
                       @RequestParam int size) throws Exception {
        return fileService.read(path, offset, size);
    }

    @PostMapping("/write")
    public int write(@RequestParam String path,
                     @RequestParam long offset,
                     @RequestBody byte[] data) throws Exception {
        return fileService.write(path, offset, data);
    }

    @PostMapping("/create")
    public void create(@RequestParam String path) throws Exception {
        fileService.create(path);
    }
    
    @PostMapping("/utimens")
    public void utimens(@RequestParam String path,
                        @RequestParam long atime,
                        @RequestParam long mtime) throws IOException {
        fileService.utimens(path, atime, mtime);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String path) throws Exception {
        fileService.delete(path);
    }

    @PostMapping("/mkdir")
    public void mkdir(@RequestParam String path) throws Exception {
        fileService.mkdir(path);
    }
    
    @DeleteMapping("/rmdir")
    public void rmdir(@RequestParam String path) throws IOException {
        fileService.rmdir(path);
    }

    @PostMapping("/rename")
    public void rename(@RequestParam String from, @RequestParam String to) throws Exception {
        fileService.rename(from, to);
    }

    @PostMapping("/truncate")
    public void truncate(@RequestParam String path, @RequestParam long size) throws Exception {
        fileService.truncate(path, size);
    }
}