package clia.back.fs;

public abstract class File extends FileSystem {
    private String extension;

    public File(String name, String extension, Users perms, Folder parent) {
        super(name, perms, parent);
        this.extension = extension;
    }

    public String getName() {
        return super.name + "." + extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
