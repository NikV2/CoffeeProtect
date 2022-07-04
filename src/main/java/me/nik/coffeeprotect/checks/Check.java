package me.nik.coffeeprotect.checks;

import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.managers.profile.Profile;
import me.nik.coffeeprotect.utils.custom.CoffeeProtectException;

public abstract class Check {

    protected final Profile profile;

    private final boolean enabled;

    private final String checkName, description;

    public Check(Profile profile, boolean enabled) {

        this.profile = profile;

        this.enabled = enabled;

        CheckInfo checkInfo = this.getClass().getAnnotation(CheckInfo.class);

        if (checkInfo == null) {
            //We forgot to add the annotation, Let us know.
            throw new CoffeeProtectException("CheckInfo annotation not found in class " + this.getClass().getSimpleName() + ".");
        }

        this.checkName = checkInfo.name();

        this.description = checkInfo.description();
    }

    public abstract CheckResult handle(PacketEvent e);

    public boolean isEnabled() {
        return enabled;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getDescription() {
        return description;
    }
}