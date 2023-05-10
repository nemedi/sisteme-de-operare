using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace FileExplorer
{
    public class FileModel : ObservableObject
    {
        public string Name { get; }
        public long Size { get; }
        public FileModel(string path)
        {
            Name = Path.GetFileName(path);
            Size = new FileInfo(path).Length;
        }

        public static FileModel[] GetFiles(string path)
        {
            IList<FileModel> files = new List<FileModel>();
            try
            {
                foreach (var file in Directory.GetFiles(path, "*", SearchOption.TopDirectoryOnly))
                {
                    files.Add(new FileModel(file));
                }
            }
            catch
            {
            }
            return files.ToArray();
        }
    }
}
