import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.File;

public class FileExplorerApplication extends Application {

    private TreeView<File> treeView;
    private TableView<File> tableView;
    private TreeItem<File> rootItem;

    @Override
    public void start(Stage stage) {
        rootItem = new TreeItem<File>(new File("."));
        treeView = new TreeView<File>(rootItem);
        buildTree();
        treeView.setShowRoot(false);
        treeView.setCellFactory(tv ->
            new javafx.scene.control.TreeCell<>() {
                @Override
                protected void updateItem(
                    File item,
                    boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String name = item.getName();
                        if (name.isEmpty()) {
                            setText(item.getPath());
                        } else {
                            setText(name);
                        }
                    }
                }
            });
        tableView = new TableView<>();
        TableColumn<File, String> nameColumn = new TableColumn<>("File Name");
        nameColumn.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getName()));
        nameColumn.setPrefWidth(400);
        TableColumn<File, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> {
            File file = data.getValue();
            if (file.isFile()) {
                return new SimpleStringProperty(
                    formatSize(file.length()));
            }
            return new SimpleStringProperty("");
        });
        sizeColumn.setPrefWidth(150);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(sizeColumn);
        treeView.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadFiles(newVal.getValue());
                }
            });
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().add(treeView);
        splitPane.getItems().add(tableView);
        splitPane.setDividerPositions(0.3);
        BorderPane root = new BorderPane(splitPane);
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("File Explorer");
        stage.setScene(scene);
        stage.show();
    }

    private void buildTree() {
        List<TreeItem<File>> rootItems = Arrays.asList(File.listRoots()).stream()
            .map(file -> new TreeItem<File>(file))
            .collect(Collectors.toList());
        for (TreeItem<File> rootItem : rootItems) {
            this.rootItem.getChildren().add(rootItem);
            File folder = rootItem.getValue();
            File[] subFolders = folder.listFiles(File::isDirectory);
            if (subFolders == null) {
                continue;
            }
            for (File subFolder : subFolders) {
                try {
                    TreeItem<File> item = new TreeItem<File>(subFolder);
                    rootItem.getChildren().add(item);
                    File[] secondLevel = subFolder.listFiles(File::isDirectory);
                    if (secondLevel != null) {
                        for (File innerSubFolder : secondLevel) {
                            item.getChildren().add(new TreeItem<>(innerSubFolder));
                        }
                    }
                }
                catch (Exception ex) {
                    continue;
                }
            }
        }
    }

    private void loadFiles(File folder) {
        tableView.getItems().clear();
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            tableView.getItems().add(file);
        }
    }

    private String formatSize(long bytes) {
        String[] units = { "B", "KB", "MB", "GB", "TB" };
        double size = bytes;
        int unit = 0;
        while (size >= 1024
            && unit < units.length - 1)
        {
            size /= 1024;
            unit++;
        }
        return String.format(
            "%.2f %s",
            size,
            units[unit]);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
