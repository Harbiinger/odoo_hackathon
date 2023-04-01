package clia.back.fs;

public abstract class FileSystem {
    protected String name;

    protected Users perms;

    public FileSystem(String name, Users perms) {
        this.name = name;
        this.perms = perms;
    }

    public abstract String getName();

    public abstract String getContent();

    public void setName(String name) {
        this.name = name;
    }

}
