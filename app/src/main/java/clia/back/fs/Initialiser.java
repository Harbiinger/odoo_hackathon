package clia.back.fs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import clia.App;
import clia.back.fs.FileSystem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Initialiser {

    private String path;


    public Initialiser(String path) {
        this.path = path;
    }

    public ArrayList<FileSystem> readFolder(JSONObject json, Folder parent) throws IOException, ParseException {

        ArrayList<FileSystem> files = new ArrayList<>();

        JSONArray ja = (JSONArray) json.get("content");

        Iterator itr2 = ja.iterator();

        while (itr2.hasNext()) {
            JSONObject jo = (JSONObject) itr2.next();
            if (Objects.equals((String) jo.get("type"), "Folder")) {
                Folder tmp = new Folder((String) jo.get("name"),
                        Users.getUser((String) jo.get("perms")),
                        parent
                );
                tmp.addAll(readFolder(jo, tmp));
                files.add(tmp);
            } else {
                files.add(new TextFile((String) jo.get("name"),
                        Users.getUser((String) jo.get("perms")),
                        (String) jo.get("ext"),
                        parent,
                        (String) jo.get("content")
                ));
            }
        }


        return files;
    }

    public Folder Init() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(path));

        JSONObject json = (JSONObject) obj;

        Folder f = new Folder((String) json.get("name"),
                Users.getUser((String) json.get("perms")),
                null
        );
        f.addAll(readFolder(json, f));
        return f;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Path path = Paths.get(System.getProperty("user.dir"));
        String stringPath = path.toString();
        stringPath = stringPath + "/app/src/main/resources/gameData/data.json";
        Initialiser init = new Initialiser(stringPath);
        Folder fs = init.Init();
        System.out.println(fs.getContent());

        Folder tmp = fs.getFolder("Documents");
        System.out.println(tmp.getContent());

        System.out.println(tmp.getFile("file1.txt").getContent());
        System.out.println(tmp.getFile("file1.txt").getPath());
    }
}
