using System;
using System.IO;
using System.Management.Automation;

namespace Cmdlets
{
    [Cmdlet(VerbsCommon.Get, "Flights")]
    public class GetFlights : Cmdlet
    {
        [Parameter(Mandatory = true, Position = 0)]
        public string? Path { get; set; }

        private FileSystemWatcher? fileSystemWatcher;

        protected override void BeginProcessing()
        {
            base.BeginProcessing();
            if (!Directory.Exists(Path))
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("The specified path does not exist."),
                    "PathNotFound",
                    ErrorCategory.InvalidArgument,
                    Path));
            }
            fileSystemWatcher = new FileSystemWatcher(Path);
            fileSystemWatcher.Created += OnNewFileCreated;
            fileSystemWatcher.EnableRaisingEvents = true;
            WriteVerbose($"Started watching path: {Path}");
        }

        private void OnNewFileCreated(object sender, FileSystemEventArgs e)
        {
            WriteObject(e.FullPath);
        }

        protected override void EndProcessing()
        {
            base.EndProcessing();
            if (fileSystemWatcher != null)
            {
                fileSystemWatcher.EnableRaisingEvents = false;
                fileSystemWatcher.Created -= OnNewFileCreated;
                fileSystemWatcher.Dispose();
                WriteVerbose("Stopped watching path.");
            }
        }
    }
}
