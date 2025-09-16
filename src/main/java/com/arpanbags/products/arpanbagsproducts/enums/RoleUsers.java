package com.arpanbags.products.arpanbagsproducts.enums;

public enum RoleUsers {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin"),
    ROLE_OTHERS("Others");

    private final String roleName;

    RoleUsers(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
