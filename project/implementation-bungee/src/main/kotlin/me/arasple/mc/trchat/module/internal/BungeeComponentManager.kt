package me.arasple.mc.trchat.module.internal

import me.arasple.mc.trchat.ComponentManager
import me.arasple.mc.trchat.util.Internal
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.kyori.adventure.text.Component
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.PlatformSide

/**
 * @author wlys
 * @since 2022/6/8 13:05
 */
@Internal
@PlatformSide([Platform.BUNGEE])
object BungeeComponentManager : ComponentManager {

    init {
        PlatformFactory.registerAPI<ComponentManager>(this)
    }

    private var adventure: BungeeAudiences? = null

    override fun getAudienceProvider(): BungeeAudiences {
        return adventure!!
    }

    override fun init() {
        adventure = BungeeAudiences.create(TrChatBungee.plugin)
    }

    override fun release() {
        adventure?.close()
    }

    override fun sendSystemComponent(receiver: Any, component: Component, sender: Any?) {
        TODO("Not yet implemented")
    }

    override fun sendChatComponent(receiver: Any, component: Component, sender: Any?) {
        TODO("Not yet implemented")
    }

    override fun filterComponent(component: Component?, maxLength: Int): Component? {
        error("Not supported.")
    }
}