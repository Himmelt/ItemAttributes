package org.soraworld.attrib.manager;

import org.soraworld.hocon.node.Serializable;
import org.soraworld.hocon.node.Setting;

@Serializable
public class LoreKeys {
    @Setting(comment = "comment.loreKeys.health")
    public String keyHealth = "Health";
    @Setting(comment = "comment.loreKeys.attack")
    public String keyAttack = "Attack";
    @Setting(comment = "comment.loreKeys.regain")
    public String keyRegain = "Regain";
    @Setting(comment = "comment.loreKeys.walkSpeed")
    public String keyWalkSpeed = "WalkSpeed";
    @Setting(comment = "comment.loreKeys.flySpeed")
    public String keyFlySpeed = "FlySpeed";
    @Setting(comment = "comment.loreKeys.knock")
    public String keyKnock = "KnockResistance";
    @Setting(comment = "comment.loreKeys.armor")
    public String keyArmor = "Armor";
    @Setting(comment = "comment.loreKeys.dodge")
    public String keyDodge = "Dodge";
    @Setting(comment = "comment.loreKeys.bind")
    public String keyBind = "BindEnable";
    @Setting(comment = "comment.loreKeys.perm")
    public String keyPerm = "Permission";
    @Setting(comment = "comment.loreKeys.immortal")
    public String keyImmortal = "Immortal";
    @Setting(comment = "comment.loreKeys.block")
    public String keyBlock = "Block";
    @Setting(comment = "comment.loreKeys.crit")
    public String keyCrit = "Crit";
    @Setting(comment = "comment.loreKeys.suck")
    public String keySuck = "Suck";
    @Setting(comment = "comment.loreKeys.onekill")
    public String keyOneKill = "OneKill";
    @Setting(comment = "comment.loreKeys.thorn")
    public String keyThorn = "Thorn";
    @Setting(comment = "comment.loreKeys.rage")
    public String keyRage = "Rage";
    @Setting(comment = "comment.loreKeys.potion")
    public String keyPotion = "Potion";
    @Setting(comment = "comment.loreKeys.spell")
    public String keySpell = "Spell";
    @Setting(comment = "comment.loreKeys.skill")
    public String keySkill = "Skill";
}
