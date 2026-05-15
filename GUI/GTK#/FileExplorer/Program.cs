using System;
using System.IO;
using Gtk;

public class FileExplorerWindow : Window
{
    private TreeView folderTree;
    private TreeStore folderStore;

    private TreeView fileView;
    private ListStore fileStore;

    public FileExplorerWindow() : base("File Explorer")
    {
        SetDefaultSize(1000, 600);
        DeleteEvent += (o, args) => Application.Quit();
        Paned paned = new Paned(Orientation.Horizontal);
        folderStore = new TreeStore(typeof(string), typeof(string));
        folderTree = new TreeView(folderStore);
        TreeViewColumn folderColumn = new TreeViewColumn
        {
            Title = "Folders"
        };
        CellRendererText folderRenderer = new CellRendererText();
        folderColumn.PackStart(folderRenderer, true);
        folderColumn.AddAttribute(folderRenderer, "text", 0);
        folderTree.AppendColumn(folderColumn);
        PopulateRoots();
        folderTree.RowExpanded += OnRowExpanded;
        folderTree.CursorChanged += OnFolderSelected;
        ScrolledWindow leftScroll = new ScrolledWindow();
        leftScroll.Add(folderTree);
        fileStore = new ListStore(typeof(string), typeof(string));
        fileView = new TreeView(fileStore);
        TreeViewColumn nameColumn = new TreeViewColumn
        {
            Title = "File Name"
        };
        CellRendererText nameRenderer = new CellRendererText();
        nameColumn.PackStart(nameRenderer, true);
        nameColumn.AddAttribute(nameRenderer, "text", 0);
        TreeViewColumn sizeColumn = new TreeViewColumn
        {
            Title = "Size"
        };
        CellRendererText sizeRenderer = new CellRendererText();
        sizeColumn.PackStart(sizeRenderer, true);
        sizeColumn.AddAttribute(sizeRenderer, "text", 1);
        fileView.AppendColumn(nameColumn);
        fileView.AppendColumn(sizeColumn);
        ScrolledWindow rightScroll = new ScrolledWindow();
        rightScroll.Add(fileView);
        paned.Pack1(leftScroll, true, false);
        paned.Pack2(rightScroll, true, false);
        Add(paned);
        ShowAll();
    }

    private void PopulateRoots()
    {
        foreach (string drive in Directory.GetLogicalDrives())
        {
            TreeIter iter = folderStore.AppendValues(
                drive,
                drive
            );
            AddDummyNode(iter);
        }
    }

    private void AddDummyNode(TreeIter parent)
    {
        folderStore.AppendValues(parent, "Loading...", "");
    }

    private void OnRowExpanded(object? sender, RowExpandedArgs args)
    {
        TreeIter iter;
        if (!folderStore.GetIter(out iter, args.Path))
            return;
        string fullPath = (string)folderStore.GetValue(iter, 1);
        TreeIter child;
        if (folderStore.IterChildren(out child, iter))
        {
            string firstChild = (string)folderStore.GetValue(child, 0);
            if (firstChild == "Loading...")
            {
                folderStore.Remove(ref child);
                try
                {
                    foreach (string dir in Directory.GetDirectories(fullPath))
                    {
                        string name = System.IO.Path.GetFileName(dir);
                        TreeIter subIter = folderStore.AppendValues(iter, name, dir);
                        AddDummyNode(subIter);
                    }
                }
                catch
                {
                }
            }
        }
    }

    private void OnFolderSelected(object? sender, EventArgs e)
    {
        TreeSelection selection = folderTree.Selection;
        TreeIter iter;
        ITreeModel model;
        if (!selection.GetSelected(out model, out iter))
            return;
        string folderPath = (string)model.GetValue(iter, 1);
        if (string.IsNullOrEmpty(folderPath))
            return;
        LoadFiles(folderPath);
    }

    private void LoadFiles(string folderPath)
    {
        fileStore.Clear();
        try
        {
            foreach (string file in Directory.GetFiles(folderPath))
            {
                FileInfo info = new FileInfo(file);
                fileStore.AppendValues(
                    info.Name,
                    FormatSize(info.Length)
                );
            }
        }
        catch
        {
        }
    }

    private string FormatSize(long bytes)
    {
        string[] units = { "B", "KB", "MB", "GB", "TB" };
        double size = bytes;
        int unit = 0;
        while (size >= 1024 && unit < units.Length - 1)
        {
            size /= 1024;
            unit++;
        }
        return $"{size:0.##} {units[unit]}";
    }

    public static void Main()
    {
        Application.Init();
        new FileExplorerWindow();
        Application.Run();
    }
}
