package me.nik.coffeeprotect.enums;

public enum Permissions {
    ADMIN("coffeeprotect.admin"),
    ALERTS("coffeeprotect.alerts");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}