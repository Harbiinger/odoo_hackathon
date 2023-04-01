package clia.back.fs;

public enum Users {
    Employee, Manager, CEO;

    public static Users getUser(String user) {
        return switch (user) {
            case "Employee" -> Employee;
            case "Manager" -> Manager;
            case "CEO" -> CEO;
            default -> null;
        };
    }
}
