package info.nightscout.implementation.queue.commands

import app.aaps.core.interfaces.logging.LTag
import app.aaps.core.interfaces.plugin.ActivePlugin
import app.aaps.core.interfaces.pump.Insight
import app.aaps.core.interfaces.pump.PumpEnactResult
import app.aaps.core.interfaces.queue.Callback
import app.aaps.core.interfaces.queue.Command
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CommandStartPump(
    injector: HasAndroidInjector,
    callback: Callback?
) : Command(injector, CommandType.START_PUMP, callback) {

    @Inject lateinit var activePlugin: ActivePlugin

    override fun execute() {
        val pump = activePlugin.activePump
        if (pump is Insight) {
            val result = pump.startPump()
            callback?.result(result)?.run()
        }
    }

    override fun status(): String = rh.gs(info.nightscout.core.ui.R.string.start_pump)

    override fun log(): String = "START PUMP"
    override fun cancel() {
        aapsLogger.debug(LTag.PUMPQUEUE, "Result cancel")
        callback?.result(PumpEnactResult(injector).success(false).comment(info.nightscout.core.ui.R.string.connectiontimedout))?.run()
    }
}