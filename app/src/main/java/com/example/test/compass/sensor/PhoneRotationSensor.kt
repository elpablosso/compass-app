package com.example.test.compass.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject
import kotlin.math.roundToInt

class PhoneRotationSensor @Inject constructor(
    context: Context
) : SensorEventListener {

    var onAngleUpdated: (Int) -> Unit = {}
    var onDirectionUpdated: (String) -> Unit = {}

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    init {
        (context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager).also { sensorManager ->
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
                sensorManager.registerListener(
                    this,
                    magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                lowPass(event.values, accelerometerReading)
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                lowPass(event.values, magnetometerReading)
            }
            else -> return
        }
        updateOrientationAngles()
    }

    private fun getDirection(angle: Int): String = when {
        (angle >= 350 || angle <= 10) -> "N"
        angle in 281..349 -> "NW"
        angle in 261..280 -> "W"
        angle in 191..260 -> "SW"
        angle in 171..190 -> "S"
        angle in 101..170 -> "SE"
        angle in 81..100 -> "E"
        angle in 11..80 -> "NE"
        else -> ""
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        val degrees = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
        val angle = ((degrees * 100).roundToInt() / 100)
        val direction = getDirection(angle)
        onAngleUpdated.invoke(angle)
        onDirectionUpdated.invoke(direction)
    }

    val ALPHA = 0.25f // if ALPHA = 1 OR 0, no filter applies.


    private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray? {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }
}