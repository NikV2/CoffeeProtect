package me.nik.coffeeprotect.enums;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.utils.ChatUtils;

import java.util.List;

public enum MsgType {
    PREFIX(ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("prefix"))),
    NO_PERMISSION(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("no_perm"))),
    RELOADED(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("reloaded"))),
    CONSOLE_COMMANDS(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("console_commands"))),
    ALERTS_ENABLED(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("alerts_enabled"))),
    ALERTS_DISABLED(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("alerts_disabled"))),
    ALERT_MESSAGE(PREFIX.getMessage() + ChatUtils.format(CoffeeProtect.getInstance().getLang().get().getString("alert_message"))),
    ALERT_HOVER(stringFromList(CoffeeProtect.getInstance().getLang().get().getStringList("alert_hover")));

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private static String stringFromList(List<String> list) {

        StringBuilder sb = new StringBuilder();

        int size = list.size();

        for (int i = 0; i < size; i++) {

            sb.append(list.get(i));

            if (size - 1 != i) sb.append("\n");
        }

        return ChatUtils.format(sb.toString());
    }
}