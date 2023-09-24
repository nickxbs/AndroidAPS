package info.nightscout.plugins.constraints.objectives.objectives

import app.aaps.core.main.constraints.ConstraintObject
import app.aaps.core.interfaces.logging.AAPSLogger
import app.aaps.core.interfaces.utils.T
import dagger.android.HasAndroidInjector
import info.nightscout.plugins.constraints.R
import info.nightscout.plugins.constraints.safety.SafetyPlugin
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class Objective5(injector: HasAndroidInjector) : Objective(injector, "maxiobzero", R.string.objectives_maxiobzero_objective, R.string.objectives_maxiobzero_gate) {

    @Inject lateinit var safetyPlugin: SafetyPlugin
    @Inject lateinit var aapsLogger: AAPSLogger

    init {
        tasks.add(MinimumDurationTask(this, T.days(5).msecs()))
        tasks.add(
            object : Task(this, R.string.closedmodeenabled) {
                override fun isCompleted(): Boolean {
                    val closedLoopEnabled = ConstraintObject(false, aapsLogger)
                    safetyPlugin.isClosedLoopAllowed(closedLoopEnabled)
                    return closedLoopEnabled.value()
                }
            }.learned(Learned(R.string.objectives_maxiobzero_learned))
        )
    }
}