package clia.back.fs;

public class TextFile extends File {

    private String content;

    public TextFile(String name, Users perms, String extension, Folder parent ,String content) {
        super(name,extension, perms, parent);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
