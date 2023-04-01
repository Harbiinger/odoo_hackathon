package clia.back.fs;

public class TextFile extends File {

    private String content;

    public TextFile(String name, String extension, String content) {
        super(name, extension);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
