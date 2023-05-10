using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace FileExplorer
{
    public class ObservableObject : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler? PropertyChanged;

        protected void SetProperty<T>(ref T source, T value, [CallerMemberName] string name = "")
        {
            source = value;
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}
