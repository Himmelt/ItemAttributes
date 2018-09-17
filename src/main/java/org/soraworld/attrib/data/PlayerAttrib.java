package org.soraworld.attrib.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.HashSet;

public class PlayerAttrib {
    public float armor = 0;// 防御
    public float dodgeChance = 0;// 防御
    public float blockChance = 0, blockRation = 0;// 防御
    public float thornChance = 0, thornRatio = 0;// 防御

    public float critChance = 0, critRation = 0;// 攻击
    public float rageHealth = 0, rageRation = 0;// 攻击
    public float suckChance = 0, suckRation = 0;// 攻击
    public float onekillChance = 0, onekillRation = 0;// 攻击
    public HashSet<Potion> spells = new HashSet<>();// 攻击

    public void applySpells(LivingEntity entity) {
        for (Potion spell : spells) {
            PotionEffect effect = spell.getEffect();
            if (effect != null) entity.addPotionEffect(effect, true);
        }
    }
}
