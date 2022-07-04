package me.nik.coffeeprotect.utils.custom;

import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.Testing;
import me.nik.coffeeprotect.checks.impl.InvalidItem;
import me.nik.coffeeprotect.checks.impl.InvalidPacket;
import me.nik.coffeeprotect.checks.impl.InvalidPosition;
import me.nik.coffeeprotect.checks.impl.LecternCrasher;
import me.nik.coffeeprotect.checks.impl.OfflinePlayer;
import me.nik.coffeeprotect.checks.impl.SelfDamage;
import me.nik.coffeeprotect.managers.profile.Profile;

import java.util.Arrays;

public class CheckHolder {

    private final Profile profile;
    private Check[] checks;
    private int checksSize;
    private boolean testing; //Used for testing new checks

    public CheckHolder(Profile profile) {
        this.profile = profile;

        registerAll();
    }

    /**
     * Runs the checks and returns the result if the player fails a check.
     *
     * @param event The packet event.
     * @return The result.
     */
    public CheckResult runChecks(PacketEvent event) {

        CheckResult result = null;

        /*
        Fastest way to loop through many objects, If you think this is stupid
        Then benchmark the long term perfomance yourself with many profilers and java articles.
         */
        for (int i = 0; i < this.checksSize; i++) {

            /*
            Set the result and break the loop if it's not null.
             */
            if ((result = this.checks[i].handle(event)) != null) break;
        }

        /*
        Return the CheckResult if there's one.
         */
        return result;
    }

    public void registerAll() {

        /*
         * Check initialization
         */
        addChecks(
                new InvalidItem(this.profile),
                new InvalidPacket(this.profile),
                new OfflinePlayer(this.profile),
                new InvalidPosition(this.profile),
                new LecternCrasher(this.profile),
                new SelfDamage(this.profile)
        );

        /*
        Remove checks if a testing check is present.
         */
        testing:
        {

            /*
            Testing check not present, break.
             */
            if (!this.testing) break testing;

            /*
            Remove the rest of the checks since a testing check is present.
             */
            this.checks = Arrays.stream(this.checks)
                    .filter(check -> check.getClass().isAnnotationPresent(Testing.class))
                    .toArray(Check[]::new);

            /*
            Update the size since we're only going to be running one check.
             */
            this.checksSize = 1;
        }
    }

    private void addChecks(Check... checks) {

        /*
        Create a new check array to account for reloads.
         */
        this.checks = new Check[0];

        /*
        Reset the check size to account for reloads
         */
        this.checksSize = 0;

        /*
        Loop through the input checks
         */
        for (Check check : checks) {

            /*
            Check if this is being used by a GUI, where we put null as the profile
            Or a check with the @Testing annotation is present or disabled.
             */
            if (this.profile != null && (!check.isEnabled() || isTesting(check))) continue;

            /*
            Copy the original array and increment the size just like an ArrayList.
             */
            this.checks = Arrays.copyOf(this.checks, this.checksSize + 1);

            /*
            Update the check.
             */
            this.checks[this.checksSize] = check;

            /*
            Update the check size variable for improved looping perfomance
             */
            this.checksSize++;
        }
    }

    /**
     * If a check with the testing annotation is present, It'll set the testing boolean to true, load it and then
     * Prevent any other checks from registering.
     */
    private boolean isTesting(Check check) {

        if (this.testing) return true;

        /*
        Update the variable and return false in order to register this check
        But not the next ones.
         */
        if (check.getClass().isAnnotationPresent(Testing.class)) this.testing = true;

        return false;
    }

    public Check[] getChecks() {
        return checks;
    }
}