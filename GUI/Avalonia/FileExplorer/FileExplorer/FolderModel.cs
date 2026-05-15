using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;

namespace FileExplorer
{
    public class FolderModel
    {
        string path;
        bool expanded;
        ObservableCollection<FolderModel> subfolders;
        public ObservableCollection<FolderModel> Subfolders
        {
            get
            {
                if (!expanded)
                {
                    subfolders = new ObservableCollection<FolderModel> ();
                    foreach (var subfolder in GetSubfolders(path))
                    {
                        subfolders.Add(subfolder);
                    }
                    expanded = true;
                }
                return subfolders;
            }
        } 

        public string Name { get; }

        public FolderModel(string path)
        {
            this.path = path;
            expanded = false;
            subfolders = new ObservableCollection<FolderModel> ();
            Name = Path.GetFileName(path);
            if (Name == string.Empty)
            {
                Name = path;
            }
        }

        public static FolderModel[] GetSubfolders(string path)
        {
            IList<FolderModel> subfolders = new List<FolderModel> ();
            try
            {
                foreach (var subfolder in Directory.GetDirectories(path, "*", SearchOption.TopDirectoryOnly))
                {
                    subfolders.Add(new FolderModel(subfolder));
                }
            }
            catch
            {
            }
            return subfolders.ToArray();
        }

        public FileModel[] GetFiles()
        {
            return FileModel.GetFiles(path);
        }

        public static FolderModel[] GetFolders()
        {
            IList<FolderModel> folders = new List<FolderModel>();
            foreach (var drive in Directory.GetLogicalDrives())
            {
                folders.Add(new FolderModel(drive));
            }
            return folders.ToArray();
        }

    }
}
