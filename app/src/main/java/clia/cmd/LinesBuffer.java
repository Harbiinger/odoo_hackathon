package clia.cmd;

import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * Represents the content, in terms of lines, of the terminal. Everytime the user inputs a line of text and presses enter,
 * this line is sent to the <code>LinesBuffer</code>. This data structure eases the display of a maximum number of lines.
 * It uses a buffer (<code>buffer</code>) containing the lines that are being displayed (for example, the first 20 lines)
 * and a list (<code>history</code>) that contains all past lines that are no longer displayed.
 */
public class LinesBuffer<T> {
    private final int SIZE;
    private ArrayList<T> history = new ArrayList<>();
    private Buffer<T> buffer;

    public LinesBuffer(int size) {
        SIZE = size;
        buffer = new Buffer<>(SIZE);
    }

    public void add(T s) {
        T popped = buffer.add(s);
        if (popped != null) history.add(popped);
    }

    public int size() {
        return buffer.size() + history.size();
    }

    /**
     * Reads all lines from <code>lines</code> and formats them so that they can be displayed in a {@link Label}
     * @return The formatted lines as a String with carriage returns
     */
    public String formatBufferContent() {
        StringBuilder bobTheBuilder = new StringBuilder();
        for (T s : buffer.getBufferContent()) bobTheBuilder.append(s.toString()).append("\n");
        return bobTheBuilder.substring(0, bobTheBuilder.length() - 1);
    }

    private class Buffer<contentType> {
        private final int SIZE;
        private ArrayList<contentType> buffer = new ArrayList<>();

        public Buffer(int maxSize) {
            SIZE = maxSize;
        }

        /**
         * If the buffer is full, remove the first (oldest) data it contains. Append the new data.
         * @param data The data to insert
         * @return The value of the oldest data that was removed in case the buffer was full. <code>null</code> if the
         *         buffer was not full and no data was removed.
         */
        public contentType add(contentType data) {
            contentType popped = null;
            if (buffer.size() == SIZE) popped = buffer.remove(0);
            buffer.add(data);
            return popped;
        }

        public int size() {
            return buffer.size();
        }

        public ArrayList<contentType> getBufferContent() {
            return buffer;
        }
    }
}
