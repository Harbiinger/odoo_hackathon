package clia.back.fs;

public abstract class FileSystem {
    protected String name;

    public FileSystem(String name) {
        this.name = name;
    }

    public abstract String getName();

    public abstract String getContent();

    public void setName(String name) {
        this.name = name;
    }

}
