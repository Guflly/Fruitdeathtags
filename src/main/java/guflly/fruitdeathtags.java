package guflly;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public final class fruitdeathtags extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> playerSelectedMessage = new HashMap<>();
    private final Map<UUID, Integer> playerPage = new HashMap<>(); // Track player pages
    private File dataFile;
    private File configFile;
    private File dataFolder;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("fruitdeathtags enabled!");
        createConfigFiles();
        loadData();
        getCommand("refresh").setExecutor(new RefreshCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("fruitdeathtags disabled!");
        saveData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("deathtags")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                openCustomDeathMessagesGUI(player, 1);
                return true;
            } else {
                sender.sendMessage("Only players can use this command.");
                return false;
            }
        }
        return false;
    }

    private void createConfigFiles() {
        dataFolder = new File("/home/container/plugins/FruitDeathTags/FruitDeathTags/config");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                // Write default values to config.yml
                config = YamlConfiguration.loadConfiguration(configFile);
                config.set("gui-title", "&Custom Death Messages");

                // Set default messages, including names, lores, colors, and permission nodes
                config.set("player-color", "#FB0000");
                config.set("victim-prefix-color", "#FFFFFF");
                config.set("victim-name-color", "#FB0000");

                for (int i = 1; i <= 18; i++) {
                    String key = "messages.tag" + i;
                    if (i <= 9) {
                        switch (i) {
                            case 1:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} has been killed by {player}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message1");
                                break;
                            case 2:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} was slain by {player} using {weapon}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message2");
                                break;
                            case 3:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} has been put down by {player}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message3");
                                break;
                            case 4:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} was slaughtered by {player} using {weapon}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message4");
                                break;
                            case 5:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{player} pulverized {victim} using {weapon}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message5");
                                break;
                            case 6:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} was destroyed by {player}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message6");
                                break;
                            case 7:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{player} gave {victim} the L"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message7");
                                break;
                            case 8:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} guts were re-arranged by {player}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message8");
                                break;
                            case 9:
                                config.set(key + ".name", "&lPREVIEW");
                                config.set(key + ".name-color", "#FFD800");
                                config.set(key + ".lore", Arrays.asList(
                                        "{victim} was EZ clapped by {player} using {weapon}"
                                ));
                                config.set(key + ".lore-color", "#FFFFFF");
                                config.set(key + ".permission-node", "fruitdeathtags.message9");
                                break;
                        }
                    } else {
                        config.set(key + ".name", "&lPREVIEW");
                        config.set(key + ".name-color", "#FFD800");
                        config.set(key + ".lore", Arrays.asList(
                                "{victim} met their fate against {player}"
                        ));
                        config.set(key + ".lore-color", "#FFFFFF");
                        config.set(key + ".permission-node", "fruitdeathtags.message" + i);
                    }
                }
                config.save(configFile);
            } catch (IOException e) {
                getLogger().severe("Could not create config.yml: " + e.getMessage());
            }
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
            // Ensure messages section is present
            if (!config.contains("messages")) {
                getLogger().warning("Messages section missing in config.yml. Creating default messages.");
                for (int i = 1; i <= 18; i++) {
                    String key = "messages.tag" + i;
                    config.set(key + ".name", "&lPREVIEW");
                    config.set(key + ".name-color", "#FFD800");
                    config.set(key + ".lore", Arrays.asList(
                            "{victim} met their fate against {player}"
                    ));
                    config.set(key + ".lore-color", "#FFFFFF");
                    config.set(key + ".permission-node", "fruitdeathtags.message" + i);
                }
                try {
                    config.save(configFile);
                } catch (IOException e) {
                    getLogger().severe("Could not save updated config.yml: " + e.getMessage());
                }
            }
        }

        dataFile = new File(dataFolder, "data/saved.json");
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                saveData();
            } catch (IOException e) {
                getLogger().severe("Could not create saved.json: " + e.getMessage());
            }
        }
    }

    private void openCustomDeathMessagesGUI(Player player, int page) {
        // Load title from config with Minecraft formatting codes
        String guiTitle = config.getString("gui-title", "Custom Death Messages").replaceAll("&", "§");
        Inventory gui = Bukkit.createInventory(null, 27, Component.text(guiTitle).decoration(TextDecoration.ITALIC, false));
        UUID playerUUID = player.getUniqueId();
        playerPage.put(playerUUID, page); // Store player's current page
        int selectedSlot = playerSelectedMessage.getOrDefault(playerUUID, -1);

        // Fill top and bottom rows with gray glass panes
        ItemStack grayGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = grayGlassPane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.displayName(Component.text(" "));
            grayGlassPane.setItemMeta(paneMeta);
        }
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, grayGlassPane);
        }
        for (int i = 18; i < 27; i++) {
            gui.setItem(i, grayGlassPane);
        }

        // Set each slot with name tags that have the specified custom lore and custom name
        if (!config.contains("messages")) {
            getLogger().severe("Messages section is missing from config.yml");
            return;
        }
        int startIndex = (page - 1) * 9 + 1;
        int endIndex = Math.min(startIndex + 8, config.getConfigurationSection("messages").getKeys(false).size());
        for (int i = startIndex, slot = 9; i <= endIndex; i++, slot++) {
            String key = "messages.tag" + i;
            if (!config.contains(key)) {
                gui.setItem(slot, grayGlassPane); // Set missing tags to gray glass pane
                continue;
            }
            ItemStack nameTag = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = nameTag.getItemMeta();
            if (meta != null) {
                // Load name and color from config and convert color codes
                String name = config.getString(key + ".name", "PREVIEW").replaceAll("&", "§");
                String nameColor = config.getString(key + ".name-color", "#FFD800");
                Component displayName = Component.text(name)
                        .color(TextColor.fromHexString(nameColor))
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false);

                meta.displayName(displayName);

                // Add enchant glint if selected without changing color
                if (i == selectedSlot) {
                    meta.addEnchant(org.bukkit.enchantments.Enchantment.LUCK_OF_THE_SEA, 1, true);
                    meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                }

                // Load lore from config
                List<String> loreStrings = config.getStringList(key + ".lore");
                String loreColor = config.getString(key + ".lore-color", "#FFFFFF");
                String playerColor = config.getString("player-color", "#FB0000");
                String victimPrefixColor = config.getString("victim-prefix-color", "#FFFFFF");
                String victimNameColor = config.getString("victim-name-color", "#FB0000");
                List<Component> loreComponents = new ArrayList<>();

                for (String loreLine : loreStrings) {
                    // Replace placeholders with components
                    Component loreComponent = Component.text("", TextColor.fromHexString(loreColor)).decoration(TextDecoration.ITALIC, false)
                            .append(Component.text(loreLine)
                                    .replaceText(builder -> builder.matchLiteral("{player}").replacement(Component.text(player.getName()).color(TextColor.fromHexString(playerColor))))
                                    .replaceText(builder -> builder.matchLiteral("{victim}").replacement(
                                            Component.text("Victim").color(TextColor.fromHexString(victimNameColor))
                                    )))
                            .replaceText(builder -> builder.matchLiteral("{weapon}").replacement(
                                    Component.text("Netherite Sword").color(NamedTextColor.WHITE)
                            ));
                    loreComponents.add(loreComponent);
                }
                meta.lore(loreComponents);
                nameTag.setItemMeta(meta);
            }
            gui.setItem(slot, nameTag);
        }

        // Navigation arrows
        if (page > 1) {
            ItemStack backArrow = new ItemStack(Material.ARROW);
            ItemMeta backMeta = backArrow.getItemMeta();
            if (backMeta != null) {
                backMeta.displayName(Component.text("Back").color(TextColor.color(0xFF3E00)).decoration(TextDecoration.ITALIC, false));
                backArrow.setItemMeta(backMeta);
            }
            gui.setItem(18, backArrow);
        }
        if (endIndex < config.getConfigurationSection("messages").getKeys(false).size()) {
            ItemStack nextArrow = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextArrow.getItemMeta();
            if (nextMeta != null) {
                nextMeta.displayName(Component.text("Next").color(TextColor.color(0x2EFF00)).decoration(TextDecoration.ITALIC, false));
                nextArrow.setItemMeta(nextMeta);
            }
            gui.setItem(26, nextArrow);
        } else {
            // Replace next arrow slot with gray glass pane if there are no more pages
            gui.setItem(26, grayGlassPane);
        }

        // Open the GUI for the player
        player.openInventory(gui);
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text(config.getString("gui-title", "Custom Death Messages").replaceAll("&", "§")).decoration(TextDecoration.ITALIC, false))) {
            event.setCancelled(true);
            // Prevent item dragging to remove items
            if (event.isShiftClick() || event.isRightClick() || event.isLeftClick()) {
                event.setCancelled(true);
            }
            if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            UUID playerUUID = player.getUniqueId();
            int slot = event.getRawSlot();
            int page = playerPage.getOrDefault(playerUUID, 1); // Retrieve player's current page

            if (slot == 18 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ARROW) {
                // Back button clicked
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                openCustomDeathMessagesGUI(player, page - 1);
            } else if (slot == 26 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ARROW) {
                // Next button clicked
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                openCustomDeathMessagesGUI(player, page + 1);
            } else if (slot >= 9 && slot < 18 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.NAME_TAG) {
                // Only allow name tag selection within valid range
                int tagIndex = (page - 1) * 9 + slot - 8;
                String permissionNode = config.getString("messages.tag" + tagIndex + ".permission-node", "fruitdeathtags.default");
                if (player.hasPermission(permissionNode)) {
                    int previousSelected = playerSelectedMessage.getOrDefault(playerUUID, -1);
                    if (previousSelected != tagIndex) {
                        playerSelectedMessage.put(playerUUID, tagIndex);
                    }
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    openCustomDeathMessagesGUI(player, page);
                } else {
                    // Player without permission clicks the tag
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    event.getCurrentItem().setType(Material.BARRIER);
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        event.getCurrentItem().setType(Material.NAME_TAG);
                        player.updateInventory();
                    }, 10L); // Revert after 0.5 seconds (10 ticks)
                    player.sendActionBar(Component.text("You do not have permission to select this message.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().title().equals(Component.text(config.getString("gui-title", "Custom Death Messages").replaceAll("&", "§")).decoration(TextDecoration.ITALIC, false))) {
            // Removed action bar message on close without saving
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer != null && playerSelectedMessage.containsKey(killer.getUniqueId())) {
            int selectedTagIndex = playerSelectedMessage.get(killer.getUniqueId());
            String key = "messages.tag" + selectedTagIndex;
            if (config.contains(key)) {
                List<String> loreStrings = config.getStringList(key + ".lore");
                String weaponName = capitalizeWeapon(killer.getInventory().getItemInMainHand().getType().toString().replace("_", " ").toLowerCase());
                Component message = Component.text(String.join(" ", loreStrings))
                        .replaceText(builder -> builder.matchLiteral("{player}").replacement(Component.text(killer.getName()).color(TextColor.fromHexString("#FB0000"))))
                        .replaceText(builder -> builder.matchLiteral("{victim}").replacement(Component.text(victim.getName()).color(TextColor.fromHexString("#FB0000"))))
                        .replaceText(builder -> builder.matchLiteral("{weapon}").replacement(Component.text(weaponName).color(NamedTextColor.WHITE)));
                event.deathMessage(message);
            }
        }
    }

    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
            Map<UUID, Integer> data = new Gson().fromJson(reader, type);
            if (data != null) {
                playerSelectedMessage.putAll(data);
            }
        } catch (IOException e) {
            getLogger().severe("Could not load data: " + e.getMessage());
        }
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            new Gson().toJson(playerSelectedMessage, writer);
        } catch (IOException e) {
            getLogger().severe("Could not save data: " + e.getMessage());
        }
    }

    private String capitalizeWeapon(String weaponName) {
        String[] words = weaponName.split(" ");
        StringBuilder capitalizedWeaponName = new StringBuilder();
        for (String word : words) {
            capitalizedWeaponName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return capitalizedWeaponName.toString().trim();
    }

    private class RefreshCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (label.equalsIgnoreCase("refresh")) {
                if (sender.hasPermission("fruitdeathtags.refresh")) {
                    config = YamlConfiguration.loadConfiguration(configFile);
                    playerSelectedMessage.clear(); // Clear existing data
                    loadData(); // Load fresh data after config reload
                    sender.sendMessage("Config reloaded successfully!");
                    return true;
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                    return false;
                }
            }
            return false;
        }
    }
}
