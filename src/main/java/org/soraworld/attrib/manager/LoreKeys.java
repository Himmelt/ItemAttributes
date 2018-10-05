package org.soraworld.attrib.manager;

import org.soraworld.hocon.node.Serializable;
import org.soraworld.hocon.node.Setting;

@Serializable
public class LoreKeys {
    @Setting(comment = "comment.loreKeys.health")
    private String keyHealth = "Health";
    @Setting(comment = "comment.loreKeys.attack")
    private String keyAttack = "Attack";
    @Setting(comment = "comment.loreKeys.regain")
    private String keyRegain = "Regain";
    @Setting(comment = "comment.loreKeys.walkSpeed")
    private String keyWalkSpeed = "WalkSpeed";
    @Setting(comment = "comment.loreKeys.flySpeed")
    private String keyFlySpeed = "FlySpeed";
    @Setting(comment = "comment.loreKeys.knock")
    private String keyKnock = "KnockResistance";
    @Setting(comment = "comment.loreKeys.armor")
    private String keyArmor = "Armor";
    @Setting(comment = "comment.loreKeys.block")
    private String keyBlock = "Block";
    @Setting(comment = "comment.loreKeys.crit")
    private String keyCrit = "Crit";
    @Setting(comment = "comment.loreKeys.suck")
    private String keySuck = "Suck";
    @Setting(comment = "comment.loreKeys.onekill")
    private String keyOneKill = "OneKill";
    @Setting(comment = "comment.loreKeys.thorn")
    private String keyThorn = "Thorn";
    @Setting(comment = "comment.loreKeys.rage")
    private String keyRage = "Rage";
    @Setting(comment = "comment.loreKeys.immortal")
    private String keyImmortal = "Immortal";
    @Setting(comment = "comment.loreKeys.dodge")
    private String keyDodge = "Dodge";
    @Setting(comment = "comment.loreKeys.bind")
    private String keyBind = "BindEnable";
    @Setting(comment = "comment.loreKeys.potion")
    private String keyPotion = "Potion";
    @Setting(comment = "comment.loreKeys.spell")
    private String keySpell = "Spell";
    @Setting(comment = "comment.loreKeys.skill")
    private String keySkill = "Skill";
}
