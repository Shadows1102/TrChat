package me.arasple.mc.trchat.util.color

import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.module.chat.colored

/**
 * @author Arasple
 * @date 2019/8/15 20:52
 */
@PlatformSide([Platform.BUKKIT])
object MessageColors {

    const val COLOR_PERMISSION_NODE = "trchat.color."
    const val FORCE_CHAT_COLOR_PERMISSION_NODE = "trchat.color.force-defaultcolor."

    private val specialColors = arrayOf(
        "minedown",
        "rainbow",
        "gradients",
        "hex",
        "anvil",
        "sign",
        "book"
    )

    fun replaceWithPermission(player: HumanEntity, strings: List<String>, type: Type = Type.DEFAULT): List<String> {
        return strings.map { replaceWithPermission(player, it, type) }
    }

    fun replaceWithPermission(player: HumanEntity, s: String, type: Type = Type.DEFAULT): String {
        var string = s

        if (type == Type.ANVIL && player.hasPermission("trchat.color.anvil.*")) {
            return string.colorify()
        }
        if (type == Type.SIGN && player.hasPermission("trchat.color.sign.*")) {
            return string.colorify()
        }
        if (type == Type.BOOK && player.hasPermission("trchat.color.book.*")) {
            return string.colorify()
        }

        if (player.hasPermission("$COLOR_PERMISSION_NODE*")) {
            string = string.colored()
        }

        getColors(player).forEach { color ->
            string = string.replace(color, CustomColor.get(color).color)
        }

        string = if (player.hasPermission(COLOR_PERMISSION_NODE + "rainbow")) {
            string.parseRainbow()
        } else {
            string.replace(Hex.RAINBOW_PATTERN.toRegex(), "")
        }

        string = if (player.hasPermission(COLOR_PERMISSION_NODE + "gradients")) {
            string.parseGradients()
        } else {
            string.replace(Hex.GRADIENT_PATTERN.toRegex(), "")
        }

        if (player.hasPermission(COLOR_PERMISSION_NODE + "hex")) {
            string = string.parseHex()
        } else {
            Hex.HEX_PATTERNS.forEach { string = string.replace(it.toRegex(), "") }
        }

        return string
    }

    fun defaultColored(color: CustomColor, player: Player, msg: String): String {
        var message = msg

        message = replaceWithPermission(player, message)

        message = when (color.type) {
            CustomColor.ColorType.NORMAL -> color.color + message
            CustomColor.ColorType.SPECIAL -> (color.color + message).parseRainbow().parseGradients()
        }

        return message
    }

    private fun getColorsFromPermissions(player: HumanEntity, prefix: String): List<String> {
        player.recalculatePermissions()
        return player.effectivePermissions.mapNotNull {
            val permission = it.permission
            if (permission.startsWith(prefix)) {
                permission.removePrefix(prefix).let { color -> if (color.length == 1) "&$color" else color }
            } else {
                null
            }
        }.filterNot { it in specialColors }
    }

    fun getColors(player: HumanEntity): List<String> {
        return getColorsFromPermissions(player, COLOR_PERMISSION_NODE)
    }

    fun getForceColors(player: HumanEntity): List<String> {
        return getColorsFromPermissions(player, FORCE_CHAT_COLOR_PERMISSION_NODE)
    }

    enum class Type {

        DEFAULT, ANVIL, SIGN, BOOK
    }
}