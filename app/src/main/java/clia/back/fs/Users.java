package clia.back.fs;

public enum Users {
    Employee, Manager, CEO;

    public static Users getUser(String user) {
        return switch (user) {
            case "user", "Employee" -> Employee;
            case "mngr", "Manager" -> Manager;
            case "CEO", "theo" -> CEO;
            default -> null;
        };
    }
    
    public static String getUsername(Users user) {
        return switch (user) {
            case Employee -> "user";
            case Manager -> "mngr";
            case CEO -> "theo";
        };
    }
}
