package info.nightscout.androidaps.utils.extensions

import info.nightscout.androidaps.database.entities.GlucoseValue
import info.nightscout.androidaps.utils.DateUtil
import org.json.JSONObject

fun GlucoseValue.toJson() : JSONObject =
    JSONObject()
        .put("device", sourceSensor.text)
        .put("date", timestamp)
        .put("dateString", DateUtil.toISOString(timestamp))
        .put("sgv", value)
        .put("direction", trendArrow.text)
        .put("type", "sgv")
        .put("NSCLIENT_ID", System.currentTimeMillis()) // Fake to be accepted by WS