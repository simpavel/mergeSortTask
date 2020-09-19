package sim.pavel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class ReverseInputStream extends InputStream {

    RandomAccessFile in;

    long currentLineStart = -1;
    long currentLineEnd = -1;
    long currentPos = -1;
    long lastPosInFile = -1;
    int lastChar = -1;

    public ReverseInputStream(File file) throws FileNotFoundException {
        in = new RandomAccessFile(file, "r");
        currentLineStart = file.length();
        currentLineEnd = file.length();
        lastPosInFile = file.length() - 1;
        currentPos = currentLineEnd;
    }

    public void findPrevLine() throws IOException {
        if (lastChar == -1) {
            in.seek(lastPosInFile);
            lastChar = in.readByte();
        }
        currentLineEnd = currentLineStart;

        // Достигнуто "начало" файла
        if (currentLineEnd == 0) {
            currentLineEnd = -1;
            currentLineStart = -1;
            currentPos = -1;
            return;
        }

        long filePointer = currentLineStart -1;

        while ( true ) {
            filePointer--;

            // Достигнут конец файла
            if (filePointer < 0) {
                break;
            }

            in.seek(filePointer);
            int readByte = in.readByte();

            // Пропускаем последнюю newLine в файле
            if (readByte == 0xA && filePointer != lastPosInFile ) {
                break;
            }
        }
        // we want to start at pointer +1 so we are after the LF we found or at 0 the start of the file.
        currentLineStart = filePointer + 1;
        currentPos = currentLineStart;
    }

    public int read() throws IOException {

        if (currentPos < currentLineEnd ) {
            in.seek(currentPos++);
            int readByte = in.readByte();
            return readByte;
        } else if (currentPos > lastPosInFile && currentLineStart < currentLineEnd) {
            // last line in file (first returned)
            findPrevLine();
            if (lastChar != '\n' && lastChar != '\r') {
                // last line is not terminated
                return '\n';
            } else {
                return read();
            }
        } else if (currentPos < 0) {
            return -1;
        } else {
            findPrevLine();
            return read();
        }
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
    }
}