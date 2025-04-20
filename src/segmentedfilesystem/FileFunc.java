package segmentedfilesystem;

import java.util.Arrays;
import java.util.HashMap;

public class FileFunc {
    int fID;
    String fileName;
    int numChunks = 9999;
    HashMap<Integer, byte[]> data = new HashMap<Integer, byte[]>();

    public FileFunc(byte b, byte[] buffer) {
        fID = b;
        fileName = new String(Arrays.copyOfRange(buffer, 2, buffer.length));
    }

    public FileFunc(byte b){
        fID = b;
    }

    public void addData(byte[] buffer) {
        int index = findChunkNumber(Byte.toUnsignedInt(buffer[2]), Byte.toUnsignedInt(buffer[3]));
        byte[] chunk = new byte[buffer.length - 4];
        for (int i = 4; i < buffer.length; i++) {
            chunk[i - 4] = buffer[i];
        }

        data.put(index, chunk);
    }

    public void finalData(byte[] buffer, int x) {
        int index = findChunkNumber(Byte.toUnsignedInt(buffer[2]), Byte.toUnsignedInt(buffer[3]));
        byte[] chunk = new byte[x - 4];
        for (int i = 4; i < x; i++) {
            chunk[i - 4] = buffer[i];
        }
        numChunks = index;
        data.put(index, chunk);
    }

    private int findChunkNumber(int b1, int b2) {
        return 256 * b1 + b2;
    }

    public void setHeader(byte[] buffer) {
        if (fileName == null) {
            fileName = new String(Arrays.copyOfRange(buffer, 2, buffer.length));
        }
    }

}
