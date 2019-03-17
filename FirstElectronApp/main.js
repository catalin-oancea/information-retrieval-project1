require('electron-reload')(__dirname);

const electron = require('electron');
const shell = require('shelljs');
const app = electron.app
const BrowserWindow = electron.BrowserWindow
const ipc = electron.ipcMain
let mainWindow

function createWindow () {
    mainWindow = new BrowserWindow({
            width: 1024,
            height: 550,
            webPreferences: {
            nodeIntegration: true
        }
    })

    mainWindow.loadFile('index.html')
    // mainWindow.webContents.openDevTools()

    mainWindow.on('closed', function () {
        mainWindow = null
    })
}

app.on('ready', createWindow)

app.on('window-all-closed', function () {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})


app.on('activate', function () {
    if (mainWindow === null) {
        createWindow()
    }
})

function parseResponseFromDocuSearchEngine(result) {
    const docs = result.split('\n');
    
    return docs.filter(doc => {
        return doc && doc.length
    }).map(doc => {
        return {
            document_path: doc,
            document_name: doc.split('/')[doc.split('/').length - 1],
            file_extension: doc.split('.')[doc.split('.').length - 1].toLowerCase()
        }
    });
}
ipc.on('search', function(event, payload) {
    shell.config.execPath = shell.which('node').stdout;
    shell.cd('/Users/Catalin/Desktop/Projects/docu-search/DocuSearch.project');

    const result = shell.exec(`gradle run -q --args='--queryFiles --indexPath /Users/Catalin/Desktop/Projects/docu-search/Data/InvertedIndex -q "`+payload.query+`"'`).stdout;
    
    event.sender.send('search-results-available', parseResponseFromDocuSearchEngine(result))
})
