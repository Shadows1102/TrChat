package me.arasple.mc.trchat.module.internal.script

import me.arasple.mc.trchat.TrChat
import me.arasple.mc.trchat.module.internal.script.js.JavaScriptAgent
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer

/**
 * @author wlys
 * @since 2022/6/15 18:05
 */
@JvmInline
value class Reaction(private val script: List<String>) {

    fun eval(player: Player, vararg additions: Pair<String, Any>): Any? {
        return if (script.isEmpty()) null
        else eval(player, script, *additions)
    }

    companion object {

        fun eval(player: Player, script: List<String>, vararg additions: Pair<String, Any>): Any? {
            val (isJavaScript, js) = JavaScriptAgent.serialize(script.first())
            return if (isJavaScript) JavaScriptAgent.eval(player, js!!, *additions).get()
            else TrChat.api().eval(adaptPlayer(player), script, *additions).get()
        }
    }
}