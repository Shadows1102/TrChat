package me.arasple.mc.trchat.module.display.function

import me.arasple.mc.trchat.module.display.format.JsonComponent
import me.arasple.mc.trchat.module.internal.script.Condition
import taboolib.common.util.replaceWithOrder

/**
 * @author wlys
 * @since 2021/12/12 11:41
 */
class Function(
    val id: String,
    val condition: Condition?,
    val priority: Int,
    val regex: Regex,
    val filterTextRegex: Regex?,
    val displayJson: JsonComponent,
    val action: String?
) {

    fun apply(string: String): String {
        return string.replaceRegex(regex, filterTextRegex, "{{${id}:{0}}}")
    }

    companion object {

        val functions = mutableListOf<Function>()

        fun String.replaceRegex(regex: Regex, replaceRegex: Regex?, replacement: String): String {
            var string = this
            regex.findAll(string).forEach {
                val str = it.value
                val result = replaceRegex?.find(str)?.value ?: str
                val rep = replacement.replaceWithOrder(result)
                string = string.replace(str, rep)
            }
            return string
        }
    }
}