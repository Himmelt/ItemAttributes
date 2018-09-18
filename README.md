# ItemAttributes
ItemAttributes

攻击力相关的属性只能设置在武器上，
防御相关的属性只能设置在护甲上
范围伤害 callEvent

// dodge will not cause thorn

### 命令
```
/attrib health [health(整数)]     获取/设置 手持物品的生命提升属性(如果物品不含自定义属性，会创建新的自定义物品)
/attrib regain [value(小数)]      获取/设置 生命恢复 (每循环)
/attrib walkspeed [speed(小数)]   获取/设置 步行速度
/attrib flyspeed [speed(小数)]    获取/设置 飞行速度
/attrib attack [damage(整数)]     获取/设置 攻击力提升
/attrib knock [0-100]             获取/设置 免疫击退几率
/attrib armor [0-100]             获取/设置 防御伤害值百分比
/attrib block [chance] [ratio]    获取/设置 格挡几率，格挡比
/attrib dodge [chance(0-100)]     获取/设置 闪避几率
/attrib crit [chance] [ratio]     获取/设置 暴击几率，暴击伤害比
/attrib suck [chance] [ratio]     获取/设置 吸血几率，吸血伤害比
/attrib onekill [chance] [ratio]  获取/设置 一击必杀几率，及 对方血量下限百分比
/attrib thorn [chance] [ratio]    获取/设置 反伤几率，反伤比
/attrib immortal [chance]         获取/设置 物品不耗耐久的几率
/attrib rage [health] [ratio]     获取/设置 濒死爆发的生命下限百分比，攻击力提升百分比
/attrib bind [true|false]         获取/设置 是否启用绑定
/attrib perm [perm]               获取/设置 使用权限
/attrib update                    更新手持物品的lore
/attrib setid <id|name>           设置手持物品id或name，如果物品不含自定义信息，则创建
/attrib setname <id> <name>       设置id对应物品的name
```

### 配置
```hocon
# 显示语言
lang = zh_cn
# 调试模式
debug = false
# 玩家状态更新间隔(建议不要超过2s 即40ticks)
updateTicks = 10
# 在编辑物品时自动更新Lore
autoUpdate = false
# comment.accumulateDodge
accumulateDodge = false
# comment.accumulateBlock
accumulateBlock = false
# 默认物品描述内容
defaultLore {
  health = "穿戴时: 提升 {0} 最大生命值"
  regain = "穿戴时: 每 {0}秒 恢复 {1} 生命"
  walkspeed = "穿戴时: 提升 {0} 步行速度"
  flyspeed = "穿戴时: 提升 {0} 飞行速度"
  block = "右键时: {0}% 几率格挡 {1}% 伤害"
  attack = "手持时: 提升 {0} 攻击力"
  knock = "穿戴时: {0}% 几率免疫击退"
  armor = "穿戴时: 防御 {0}% 伤害"
  crit = "攻击时: {0}% 几率造成 {1}% 暴击伤害"
  suck = "攻击时: {0}% 几率吸取对方 {1}%伤害值的生命到自身"
  onekill = "攻击时: {0}% 几率在对方生命值小于 {1}% 时 发动 [一击必杀]"
  thorn = "穿戴时: {0}% 几率给对方造成 {1}% 攻击值的反伤"
  rage = "攻击时: 当自身生命小于 {0}% 时，提升 {1}% 攻击力"
  immortal = "使用时: {0}% 几率不消耗耐久"
  dodge = "穿戴时: {0}% 几率闪避攻击"
  perm = "使用时: 使用权限 <{0}>"
  potion = "穿戴时: 给与自身 {0}{1} 效果"
  spell = "攻击时: 给对方施加 {2}秒 {0}{1} 效果"
  skill = "右键时: 释放技能 - {0}"
}
```

### 物品
```hocon
# id 号，不能重复，会显示在物品lore的第一行
0 {
  # 提升最大生命值
  health = 5
}
1 {
  health = 3
  # 提升攻击力
  attack = 23
}
2 {
  attack = 50
}
3 {
  health = 3
}
4 {}
5 {
  health = 3
}
6 {
  health = 2
}
7 {
  health = 5
  # 每循环恢复生命值(循环时间在配置里 updateTicks 设置)
  regain = 0.3
  flyspeed = 1.0
  attack = 10
  # 免疫击退几率(小数 百分比)
  knock = 1.0
  # 反伤几率
  thornChance = 1.0
  # 反伤比
  thornRatio = 3.0
  immortalChance = 0.6
  dodgeChance = 1.0
  bindEnable = true
  # 权限
  perm = at.use
  potions = [
    water_breathing/8/60
    speed/2/60
  ]
}
8 {
  health = 30
  immortalChance = 0.5
  bindEnable = true
}
9 {
  name = sword
  attack = 1000
  suckChance = 1.0
  suckRatio = 2.0
  bindEnable = true
  perm = test.perm
  # 魔咒(攻击敌人时给与敌人效果)，只在手持有效
  spells = [
    # 药水名/等级/持续时间(tick)
    poison/2/100
  ]
}
10 {
  # 物品识别名，和id作用一样，不允许重复
  name = xxx
  bindEnable = true
  # 药水效果(穿戴时给与自身)，只在护甲有效
  potions = [
    # 药水名/等级/时长(这里应该时持续效果，所以时间最好大于50)
    speed/1/80
    jump/3/80
  ]
}
11 {
  dodgeChance = 0.4
}
```