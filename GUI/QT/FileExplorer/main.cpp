#include <QApplication>
#include <QFileSystemModel>
#include <QHeaderView>
#include <QSplitter>
#include <QTableView>
#include <QTreeView>

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QFileSystemModel *folderModel = new QFileSystemModel();
    folderModel->setRootPath("");
    folderModel->setFilter(
        QDir::AllDirs
        | QDir::NoDotAndDotDot);
    QFileSystemModel *fileModel = new QFileSystemModel();
    fileModel->setRootPath("");
    fileModel->setFilter(QDir::Files);
    QTreeView *treeView = new QTreeView();
    treeView->setModel(folderModel);
    treeView->setRootIndex(
        folderModel->index(""));
    treeView->hideColumn(1);
    treeView->hideColumn(2);
    treeView->hideColumn(3);
    QTableView *tableView = new QTableView();
    tableView->setModel(fileModel);
    tableView->hideColumn(2);
    tableView->hideColumn(3);
    tableView->horizontalHeader()
        ->setStretchLastSection(true);
    QObject::connect(
        treeView,
        &QTreeView::clicked,
        [=](const QModelIndex &index)
        {
            QString path =
                folderModel->filePath(index);

            tableView->setRootIndex(
                fileModel->setRootPath(path));
        });
    QSplitter *splitter = new QSplitter(Qt::Horizontal);
    splitter->addWidget(treeView);
    splitter->addWidget(tableView);
    splitter->setStretchFactor(0, 1);
    splitter->setStretchFactor(1, 2);
    splitter->resize(1000, 600);
    splitter->setWindowTitle("File Explorer");
    splitter->show();
    return app.exec();
}
