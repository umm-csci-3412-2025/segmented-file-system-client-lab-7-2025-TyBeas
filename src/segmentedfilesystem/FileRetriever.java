package segmentedfilesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class FileRetriever {
        int port;
        InetAddress address;
        DatagramSocket socket = null;
        DatagramPacket packet;
        byte[] buffer = new byte[1024];

        public FileRetriever(String server, int port) throws UnknownHostException {
                address = InetAddress.getByName(server);
                this.port = port;
        }

        public void downloadFiles(int numFiles) throws IOException {
                socket = new DatagramSocket();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                ArrayList<FileFunc> files = new ArrayList<FileFunc>();
                while (needData(files, numFiles)) {
                        buffer = new byte[1028];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        buffer = packet.getData();
                        System.out.print(".");
                        if (buffer[0] % 2 == 0) {
                                findFileOrMake(buffer, files).setHeader(buffer);
                        }else {
                                FileFunc file = findFileOrMake(buffer, files);

                                if ((buffer[0] >> 1) % 2 == 1) {
                                        file.finalData(buffer, packet.getLength());
                                } else {
                                        file.addData(buffer);
                                }
                        }
                }
                printFiles(files);
        }

        private boolean needData(ArrayList<FileFunc> files, int numFiles) {
                if (files.size() < numFiles)
                        return true;
                for (FileFunc file : files) {
                        if (file.numChunks != file.data.size() - 1) {
                                return true;
                        }
                }
                return false;
        }

        private void printFiles(ArrayList<FileFunc> files) {
                for (FileFunc file : files) {
                        File fileToWrite = new File(file.fileName.trim());
                        try {
                                fileToWrite.createNewFile();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        try (FileOutputStream writer = new FileOutputStream(fileToWrite)) {
                                int i;
                                for (i = 0; i < file.numChunks + 1; i++) {
                                        for (byte b : file.data.get(i)) {
                                                writer.write(b);
                                        }
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        private FileFunc findFileOrMake(byte[] buffer, ArrayList<FileFunc> files) {
                for (FileFunc file : files) {
                        if (file.fID == buffer[1]) {
                                return file;
                        }
                }
                FileFunc file = new FileFunc(buffer[1]);
                files.add(file);
                return file;
        }
}

