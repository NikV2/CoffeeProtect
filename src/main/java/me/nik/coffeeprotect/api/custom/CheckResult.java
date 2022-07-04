package me.nik.coffeeprotect.api.custom;

import me.nik.coffeeprotect.checks.Check;

public class CheckResult {
    private final String checkName;
    private final String description;
    private final String information;

    public CheckResult(Check check, String information) {
        this.checkName = check.getCheckName();
        this.description = check.getDescription();
        this.information = information;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getDescription() {
        return description;
    }

    public String getInformation() {
        return information;
    }
}