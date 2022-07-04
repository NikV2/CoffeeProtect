package me.nik.coffeeprotect.commands.subcommands;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.commands.SubCommand;
import me.nik.coffeeprotect.enums.MsgType;
import me.nik.coffeeprotect.enums.Permissions;
import me.nik.coffeeprotect.managers.profile.Profile;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    private final CoffeeProtect plugin;

    public ReloadCommand(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected String getDescription() {
        return "Reload the plugin";
    }

    @Override
    protected String getSyntax() {
        return "/coffeeprotect reload";
    }

    @Override
    protected String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    protected boolean canConsoleExecute() {
        return true;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {

        this.plugin.getConfiguration().setup();
        this.plugin.getLang().reload();

        this.plugin.getProfileManager().getProfileMap().values().forEach(profile -> profile.getCheckHolder().registerAll());

        sender.sendMessage(MsgType.RELOADED.getMessage());
    }
}