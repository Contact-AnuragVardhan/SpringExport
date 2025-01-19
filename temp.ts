// src/electron-helper.ts

import { spawn, ChildProcess, exec } from 'child_process';
import path from 'path';
import { EventEmitter } from 'events';
import fs from 'fs';
import { ExtensionMessage } from '../../shared/ExtensionMessage';
import { WebSocketServer } from 'ws';
import * as vscode from "vscode";
import * as net from 'net';
import yauzl from "yauzl";
import extract from 'extract-zip';

type PostMessageToWebview = (message: ExtensionMessage) => Promise<void>;
const ELECTRON_VERSION = '33.3.1';

function getElectronDownloadUrl(): string {
  const platform = process.platform;      // 'win32' | 'darwin' | 'linux'
  const arch = process.arch;              // 'x64' | 'arm64' | ...
  let fileName = '';

  if (platform === 'win32' && arch === 'x64') {
    fileName = `electron-v${ELECTRON_VERSION}-win32-x64.zip`;
  } else if (platform === 'darwin' && arch === 'x64') {
    fileName = `electron-v${ELECTRON_VERSION}-darwin-x64.zip`;
  } else if (platform === 'darwin' && arch === 'arm64') {
    fileName = `electron-v${ELECTRON_VERSION}-darwin-arm64.zip`;
  } else if (platform === 'linux' && arch === 'x64') {
    fileName = `electron-v${ELECTRON_VERSION}-linux-x64.zip`;
  } else {
    throw new Error(`Unsupported platform/arch: ${platform} / ${arch}`);
  }

  return `https://github.com/electron/electron/releases/download/v${ELECTRON_VERSION}/${fileName}`;
  //return 'https://github.com/electron/electron/releases/download/v33.3.1/electron-v33.3.1-darwin-arm64.zip';
}

export class ElectronHelper extends EventEmitter {
  private electronProc: ChildProcess | null = null;
  private wss: WebSocketServer | null = null;
  private websocket: WebSocket | null = null;
  private port = 12345;
  private electronAppPath: string;
  private pendingCommandToElectronMain: any[] = [];
  private userId: string;
  private postMessageToWebview: PostMessageToWebview;
  private electronInstallDir: string;
  private electronBinaryPath: string;

  constructor(userId: string, postMessageToWebview: PostMessageToWebview) {
    super();
    this.userId = userId;
    this.postMessageToWebview = postMessageToWebview;


    this.electronAppPath = path.join(__dirname, '..', 'electron-audio', 'dist', 'main-bundle.js');
    this.electronInstallDir = path.join(__dirname, '..', 'electron-binaries');

    /*this.electronBinaryPath = path.join(
      this.electronInstallDir,
      process.platform === "win32" ? "electron.exe" : "electron"
    );*/

    if (process.platform === 'darwin') {
      this.electronBinaryPath = path.join(
        this.electronInstallDir,
        'Electron.app',
        'Contents',
        'MacOS',
        'Electron'
      );
    } else if (process.platform === 'win32') {
      this.electronBinaryPath = path.join(this.electronInstallDir, 'electron.exe');
    } else {
      this.electronBinaryPath = path.join(this.electronInstallDir, 'electron');
    }

    //this.ensureElectronInstalled();
  }

  public async ensureElectronInstalled(): Promise<void> {
    try {
      if (!fs.existsSync(this.electronBinaryPath)) {
        console.log("[Extension] Electron not found locally. Downloading...");
        await this.downloadAndUnzipElectron();
        console.log("[Extension] Electron downloaded/unzipped successfully.");
      } else {
        console.log("[Extension] Electron binary already exists. No download needed.");
      }

      await this.spawnElectron();
    } catch (err) {
      console.error("[Extension] ensureElectronInstalled() failed:", err);
      vscode.window.showErrorMessage(
        `Failed to install/run Electron: ${(err as Error).message}`
      );
      this.sendMessageToWebview({ type: 'audio-pluggin-initialized-error', err });
      this.sendMessageToWebview({ type: 'audio-pluggin-error', err });
    }
  }

  private async downloadAndUnzipElectron(): Promise<void> {
    fs.mkdirSync(this.electronInstallDir, { recursive: true });

    const downloadUrl = getElectronDownloadUrl();
    console.log(`[Extension] Downloading Electron from: ${downloadUrl}`);

    const localZipPath = path.join(this.electronInstallDir, 'electron-download.zip');
    await this.downloadToFile(downloadUrl, localZipPath);

    await this.unzipElectronInstaller(localZipPath, this.electronInstallDir);

    fs.unlinkSync(localZipPath);

    console.log("Electron Files extracted/unzipped ");

    let originalBinaryPath: string | null = null;
    if (process.platform === 'win32') {
      originalBinaryPath = path.join(this.electronInstallDir, 'electron.exe');
      if (!fs.existsSync(originalBinaryPath)) {
        originalBinaryPath = path.join(this.electronInstallDir, 'Electron', 'electron.exe');
      }
    } else if (process.platform === 'darwin') {
      // macOS typically has: Electron.app/Contents/MacOS/Electron
      originalBinaryPath = path.join(
        this.electronInstallDir,
        'Electron.app',
        'Contents',
        'MacOS',
        'Electron'
      );
    } else {
      // Linux: "electron" in root or in subfolder named "electron-vX.Y.Z-linux-x64"
      originalBinaryPath = path.join(this.electronInstallDir, 'electron');
      if (!fs.existsSync(originalBinaryPath)) {
        // check subfolder
        const subfolder = `electron-v${ELECTRON_VERSION}-linux-x64`;
        const possible = path.join(this.electronInstallDir, subfolder, 'electron');
        if (fs.existsSync(possible)) {
          originalBinaryPath = possible;
        }
      }
    }

    if (!originalBinaryPath || !fs.existsSync(originalBinaryPath)) {
      throw new Error(`Could not locate Electron binary after unzipping. Tried: ${originalBinaryPath}`);
    }

    /*fs.copyFileSync(originalBinaryPath, this.electronBinaryPath);

    // changing permission for files 
    if (process.platform !== 'win32') {
      fs.chmodSync(this.electronBinaryPath, 0o755);
    }
    console.log(`[Extension] Electron extracted to: ${this.electronBinaryPath}`);*/
    // On mac, we do NOT copy out the Electron binary. We keep the .app folder intact. 
    if (process.platform === 'darwin') {
      /*
      electron-binaries/
      └── Electron.app
          └── Contents
              ├── Frameworks/
              └── MacOS/
                  └── Electron
      */
      console.log('[Extension] Keeping Electron.app folder as-is on macOS.');
      // Optionally chmod the Mac’s embedded binary if needed:
      fs.chmodSync(originalBinaryPath, 0o755);
    } else {
      // On Windows / Linux, we can copy out the single "electron" or "electron.exe"
      fs.copyFileSync(originalBinaryPath, this.electronBinaryPath);
      if (process.platform !== 'win32') {
        fs.chmodSync(this.electronBinaryPath, 0o755);
      }
      console.log(`[Extension] Electron extracted to: ${this.electronBinaryPath}`);
    }
  }

  private async downloadToFile(url: string, filePath: string): Promise<void> {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`Download failed: ${response.status} ${response.statusText}`);
    }

    const fileStream = fs.createWriteStream(filePath);

    return new Promise<void>((resolve, reject) => {
      const reader = response.body!.getReader();

      function push() {
        reader.read().then(({ done, value }) => {
          if (done) {
            fileStream.close();
            return resolve();
          }
          fileStream.write(Buffer.from(value));
          push();
        }).catch(reject);
      }
      push();

      fileStream.on('error', reject);
    });
  }

  public async spawnElectron(): Promise<void> {
    if (this.electronProc) {
      console.warn('[Extension] Electron process is already running.');
      this.sendMessageToWebview({ type: 'audio-pluggin-initialized' });
      return;
    }
    console.log("000000000000000000000 electronBinaryPath which should be …/Electron.app/Contents/MacOS/Electron on mac is ", this.electronBinaryPath);

    try {
      this.port = await this.findFreePort();
      this.initializeSocket();

      if (!fs.existsSync(this.electronBinaryPath)) {
        throw new Error(`Electron executable not found at path: ${this.electronBinaryPath}`);
      }

      console.log(`Electron executable path: ${this.electronBinaryPath}`);
      console.log(`Electron app path:       ${this.electronAppPath}`);

      const useShell = (process.platform === 'win32');
      const env = { ...process.env };
      delete env.ELECTRON_RUN_AS_NODE;

      this.electronProc = spawn(
        this.electronBinaryPath,
        [
          this.electronAppPath,
          `--port=${this.port}`,
          `--userId=${this.userId}`
        ],
        {
          cwd: path.dirname(this.electronBinaryPath),
          stdio: ['pipe', 'pipe', 'pipe', 'ipc'],
          shell: useShell,
          env
        }
      );

      this.electronProc.stdout?.on('data', (data: Buffer) => {
        console.log(`[Electron stdout]: ${data.toString()}`);
      });

      this.electronProc.stderr?.on('data', (data: Buffer) => {
        console.error(`[Electron stderr]: ${data.toString()}`);
      });

      this.electronProc.on('exit', (code: number | null, signal: string | null) => {
        console.log(`[Extension] Electron exited with code ${code}, signal ${signal}`);
        this.emit('exit', code, signal);
        this.electronProc = null;
      });

      this.electronProc.on('close', (code, signal) => {
        console.log(`[Extension] Electron process closed (code=${code}, signal=${signal})`);
      });

      this.electronProc.on('error', (err: Error) => {
        console.error('[Extension] Failed to start Electron process:', err);
        this.emit('error', err);
        this.sendMessageToWebview({ type: 'audio-pluggin-initialized-error', err });
        this.sendMessageToWebview({ type: 'audio-pluggin-error', err });
        this.electronProc = null;
      });

      console.log('[Extension] Electron process spawned with IPC.');
    } catch (err) {
      console.error('[Extension] spawnElectron() error:', err);
      this.sendMessageToWebview({ type: 'audio-pluggin-initialized-error', err });
      this.sendMessageToWebview({ type: 'audio-pluggin-error', err });
      this.emit('error', err);
    }
  }


  private initializeSocket() {
    console.log(`[Extension] Found free port: ${this.port}`);
    this.wss = new WebSocketServer({ port: this.port });
    this.wss.on('connection', (ws: any) => {
      console.log('[Extension] Electron connected to WebSocket.');

      ws.on('message', (dataBuffer: Buffer) => {
        console.log('[Extension] Received from Electron:', dataBuffer.toString());
        try {
          const data = JSON.parse(dataBuffer.toString());
          console.log('[Electron Main] Received from Extension with type:', data.type);
          this.sendMessageToWebview(data);
        } catch (err) {
          console.error('Failed to parse WebSocket message as JSON:', err);
        }

      });

      ws.on('error', (err: any) => {
        console.error('[Electron Helper] WS Error:', err);
        this.sendMessageToWebview({ type: 'audio-pluggin-error', err });
      });

      ws.on('close', () => {
        console.log('[Electron Helper] WebSocket closed.');
      });

      this.websocket = ws;

      if (this.pendingCommandToElectronMain && this.pendingCommandToElectronMain.length > 0) {
        this.pendingCommandToElectronMain.forEach((message: any) => {
          this.sendCommandToElectronMain(message);
        });
        this.pendingCommandToElectronMain = [];
      }

      // sending a message to Electron after some delay:
      setTimeout(() => {
        ws.send(JSON.stringify({ type: 'hello-from-extension' }));
      }, 2000);
    });
  }

  public terminateElectron(): void {
    if (this.electronProc) {
      /*if (this.electronProc.connected && this.electronProc.send) {
        this.electronProc.send({ command: 'exit-app' });
      }*/
      this.sendCommandToElectronMain({ type: 'exit-app' });
      if (this.electronProc && !this.electronProc.killed) {
        const killSignal = process.platform === 'win32' ? 'SIGTERM' : 'SIGKILL';
        this.electronProc.kill(killSignal);
        console.log(`Electron process forcibly killed with ${killSignal}`);
      }
      this.electronProc = null;
      console.log('Electron process terminated.');
    }
    if (this.wss) {
      try {
        this.wss.close();
        this.wss = null;
        if (this.websocket && this.websocket.readyState === this.websocket.OPEN) {
          this.websocket.close();
        }
        console.log('[Extension] WebSocket server closed.');
      }
      catch (err) {
        console.error('Error Closing Websocket of Electron Helper ', err);
      }
    }
  }

  public sendMessageToWebview(message: any): void {
    this.postMessageToWebview({
      type: 'audioData',
      data: message
    });
  }

  public sendCommandToElectronMain(message: any): void {
    /*if (this.electronProc && this.electronProc.connected && this.electronProc.send) {
      this.electronProc.send(message);
      console.log('Sent message to Electron Main:', message);
    } else {
      console.warn('Cannot send message: Electron process not connected.');
      this.emit('error', new Error('Electron process not connected.'));
    }*/
    if (this.websocket && this.websocket.readyState === this.websocket.OPEN) {
      this.websocket.send(JSON.stringify(message));
      console.log('Sent message to Electron Main:', message);
    } else {
      this.pendingCommandToElectronMain.push(message);
      console.warn('Cannot send message: Electron process not connected.');
      console.log('[Extension] WebSocket not yet open');
      //this.emit('error', new Error('Electron process not connected.'));
    }
  }

  public findFreePort(): Promise<number> {
    return new Promise((resolve, reject) => {
      const server = net.createServer();

      // Listen on port 0 => OS picks a random available port
      server.listen(0, () => {
        const address = server.address();
        if (address && typeof address === 'object') {
          const port = address.port;
          server.close(() => resolve(port));
        } else {
          // Fallback if we didn't get a port for some reason
          server.close(() => reject(new Error('Unable to find port')));
        }
      });

      server.on('error', (err) => {
        reject(err);
      });
    });
  }

  private async unzipElectronInstaller(zipFilePath: string, outputDir: string): Promise<void> {
    try {
      await extract(zipFilePath, { dir: outputDir ,
        /*onEntry: (entry: Entry, zipfile) => {
          if (entry.fileName.includes('default_app.asar')) {
            console.log(`Skipping extraction of ${entry.fileName}`);
            entry.fileName = null as unknown as string;
          }
        },*/
      });
      console.log('Unzipped successfully using extract-zip');
    } catch (err) {
      console.error('Suppressing unzip error ', err);
    }
    /*return new Promise<void>((resolve, reject) => {
      yauzl.open(zipFilePath, { lazyEntries: true }, (err, zipfile) => {
        if (err) {
          return reject(err);
        }
        if (!zipfile) {
          return reject(new Error("Failed to open zipfile (no zipfile object)"));
        }

        // to Ensure the output directory exists
        fs.mkdirSync(outputDir, { recursive: true });

        zipfile.readEntry();

        // Fired for each folder and file
        zipfile.on("entry", (entry: yauzl.Entry) => {
          const entryPath = entry.fileName;
          const fullPath = path.join(outputDir, entryPath);

          const isDirectory = entry.fileName.endsWith("/") || (entry.externalFileAttributes & 0x10) === 0x10 || entry.fileName.endsWith(".app");
          console.log(`entryPath ${entryPath} is a directory ${isDirectory}`)

          if (isDirectory) {
            fs.mkdirSync(fullPath, { recursive: true });
            zipfile.readEntry();
          } else {
            console.log(entryPath);
            let targetPath = fullPath;
            let needsRenameAfterWrite = false;
            const normalized = entryPath.replace(/\\/g, "/");
            if (normalized && normalized.includes("default_app.asar")) {
              console.log("[Extension] handling default_app.asar");
              targetPath = fullPath + ".temp";
              needsRenameAfterWrite = true;
            }
            fs.mkdirSync(path.dirname(targetPath), { recursive: true });

            zipfile.openReadStream(entry, (err, readStream) => {
              if (err) {
                //return reject(err);
                console.error(err);
                return zipfile.readEntry(); // skip
              }
              if (!readStream) {
                console.error("No readStream for zip entry:", entry.fileName);
                return zipfile.readEntry(); // skip
              }

              const writeStream = fs.createWriteStream(targetPath);

              //readStream.on("error", reject);
              //writeStream.on("error", reject);
              readStream.on("error", (err: Error) => {
                console.error('An error occurred in readStream:', err);
              });

              writeStream.on("error", (err: Error) => {
                console.error('An error occurred in writeStream:', err);
              });

              writeStream.on("close", () => {
                if (needsRenameAfterWrite) {
                  try {
                    fs.renameSync(targetPath, fullPath);
                    console.log("[Extension] Renamed .temp => .asar:", fullPath);
                  } catch (renameErr) {
                    console.error("Rename failed:", renameErr);
                  }
                }
                zipfile.readEntry();
              });
              readStream.pipe(writeStream);
            });
          }
        });

        zipfile.on("end", () => {
          console.log("Read all entries from zip file");
        });

        zipfile.on("close", () => {
          console.log("zip file extracted successfully");
          resolve();
        });

        zipfile.on("error", (error) => {
          reject(error);
        });
      });
    });*/
  }

}
