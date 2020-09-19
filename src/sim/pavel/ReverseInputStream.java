package sim.pavel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class ReverseInputStream extends InputStream {

    private RandomAccessFile in;

    private long currentLineStart = -1;
    private long currentLineEnd = -1;
    private long currentPos = -1;
    private long lastPosInFile = -1;
    private int lastChar = -1;

    ReverseInputStream(File file) throws FileNotFoundException {
        in = new RandomAccessFile(file, "r");
        currentLineStart = file.length();
        currentLineEnd = file.length();
        lastPosInFile = file.length() - 1;
        currentPos = currentLineEnd;
    }

    private void findPrevLine() throws IOException {
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

        while (true) {
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
        // начинаем с позиции filePointer + 1 чтобы пропустить newLine
        currentLineStart = filePointer + 1;
        currentPos = currentLineStart;
    }

    public int read() throws IOException {

        if (currentPos < currentLineEnd ) {
            in.seek(currentPos++);
            return in.readByte();
        } else if (currentPos > lastPosInFile && currentLineStart < currentLineEnd) {
            // последняя строка в файле
            findPrevLine();
            if (lastChar != '\n' && lastChar != '\r') {
                // добавляем newLine
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
}