using Avalonia.Controls;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;

namespace FileExplorer
{
    public partial class MainWindow : Window
    {
        

        public MainWindow()
        {
            InitializeComponent();
            DataContext = new MainViewModel();   
        }
    }
}