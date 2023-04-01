package clia.back.fs;

public abstract class FileSystem {
    protected String name;

    protected Users perms;

    protected Folder parent;

    public FileSystem(String name, Users perms, Folder parent) {
        this.name = name;
        this.perms = perms;
        this.parent = parent;
    }

    public abstract String getName();

    public Users getPerms() {
        return perms;
    }

    public abstract String getContent(Users user);

    public String getPath() {
        if (parent == null) {
            return "/" + name;
        } else {
            return parent.getPath() + "/" + name;
        }
    }


    public void setName(String name) {
        this.name = name;
    }

}
