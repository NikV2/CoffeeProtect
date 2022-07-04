package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;
import me.nik.coffeeprotect.wrappers.WrapperPlayClientUseEntity;

@CheckInfo(name = "Self Damage", description = "Checks if the player damages himself")
public class SelfDamage extends Check {
    public SelfDamage(Profile profile) {
        super(profile, Config.Setting.CHECKS_SELF_DAMAGE_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {
        return e.getPacketType() == PacketType.Play.Client.USE_ENTITY
                && new WrapperPlayClientUseEntity(e.getPacket()).getTargetID() == e.getPlayer().getEntityId()
                ? new CheckResult(this, "Unit attacked itself")
                : null;
    }
}