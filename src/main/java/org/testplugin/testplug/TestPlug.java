package org.testplugin.testplug;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TestPlug extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player playerJoin = e.getPlayer();
        createArrow(playerJoin, 12);
    }
    List<Player> inMomentShooterlist = new ArrayList<Player>();
    int countShots = 0;
    int countHits = 0;

    @EventHandler
    public void onHitArrow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player shooter = (Player) e.getEntity();
            ItemStack tempArrow = e.getConsumable();
            ItemMeta metaArrow = tempArrow.getItemMeta();

            if (metaArrow.hasCustomModelData())
                if (metaArrow.getCustomModelData() == 228337) {
                    countShots = countShots + 1;
                    shooter.sendMessage("shot: " + String.valueOf(countShots));

                    inMomentShooterlist.add(shooter);

                }
        }
    }

    @EventHandler
    public void onArrow(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player & e.getEntity() instanceof Arrow) {
            Player shooter = (Player) e.getEntity().getShooter();
            for (Iterator<Player> iterator = inMomentShooterlist.iterator(); iterator.hasNext();)
            {
                Player shooterPlayer = iterator.next();
                if (shooter == shooterPlayer) {
                    if (e.getHitEntity() instanceof Player) {
                        Player victim = (Player) e.getHitEntity();
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                        victim.getWorld().createExplosion(victim.getLocation(), 1.0f);
                    }
                    iterator.remove();
                    countHits = countHits + 1;
                    shooter.sendMessage("Hits: " + String.valueOf(countHits));

                }
            }
        }
    }

    public void createArrow(Player p, int count){
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta metaArrow = arrow.getItemMeta();

        metaArrow.setDisplayName("SuperArrow");
        metaArrow.setCustomModelData(228337);

        arrow.setItemMeta(metaArrow);
        arrow.setAmount(count);
        p.getInventory().addItem(arrow);
    }

}
