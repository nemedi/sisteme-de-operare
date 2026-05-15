using System.Collections.ObjectModel;

namespace FileExplorer
{
    public class MainViewModel : ObservableObject
    {
        FolderModel? folder = null;
        public ObservableCollection<FolderModel> Folders { get; }
        public ObservableCollection<FileModel> Files { get; private set; }
        public FolderModel? Folder
        {
            get => folder;
            set
            {
                SetProperty(ref folder, value);
                Files.Clear();
                if (folder != null)
                {
                    foreach (var file in folder.GetFiles())
                    {
                        Files.Add(file);
                    }
                }
           }
        }

        public MainViewModel()
        {
            Folders = new ObservableCollection<FolderModel>();
            Files = new ObservableCollection<FileModel>();
            foreach (var folder in FolderModel.GetFolders())
            {
                Folders.Add(folder);
            }
            var enumerator = Folders.GetEnumerator();
            if (enumerator.MoveNext())
            {
                Folder = enumerator.Current;
            }
        }
    }
}
