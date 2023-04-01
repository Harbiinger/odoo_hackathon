package clia.back.fs;

public class TextFile extends File {

    private String content;

    public TextFile(String name, Users perms, String extension, Folder parent ,String content) {
        super(name,extension, perms, parent);
        this.content = content;
    }

    public String getContent(Users user) {
        if (getPerms() == Users.CEO && user == Users.CEO) {
            return content;
        }
        if (getPerms() == Users.Manager && (user == Users.Manager || user == Users.CEO)) {
            return content;
        }
        if (getPerms() == Users.Employee) {
            return content;
        }
        return "";
    }

    public void setContent(String content) {
        this.content = content;
    }

}
