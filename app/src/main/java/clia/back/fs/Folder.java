package clia.back.fs;

import java.util.ArrayList;

public class Folder extends FileSystem {

    private ArrayList<FileSystem> files;
    public Folder(String name, Users perms, Folder parent) {
        super(name, perms, parent);
        files = new ArrayList<>();
    }

    public Folder getFolder(String name) {
        for (FileSystem file : files) {
            if (file instanceof Folder && ((Folder) file).nameEquals(name)) {
                return (Folder) file;
            }
        }
        return null;
    }

    public File getFile(String name) {
        for (FileSystem file : files) {
            if (file.getName().equals(name) && file instanceof File) {
                return (File) file;
            }
        }
        return null;
    }

    public Folder getParent() {
        return parent;
    }

    public void add(FileSystem file) {
        files.add(file);
    }

    public void addAll(ArrayList<FileSystem> files) {
        this.files.addAll(files);
    }

    public void remove(FileSystem file) {
        files.remove(file);
    }

    public String getName() {
        return "/" + super.name;
    }

    public boolean nameEquals(String name) {
        return super.name.equals(name);
    }

    public String getContent() {
        StringBuilder bobTheBuilder = new StringBuilder();
        for (FileSystem file : files) {
            bobTheBuilder.append(file.getName()).append("\n");
        }
        return bobTheBuilder.toString();
    }
}
