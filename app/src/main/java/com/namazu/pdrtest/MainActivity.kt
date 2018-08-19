package com.namazu.pdrtest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SensorEventListener {

    private lateinit var sensormanager : SensorManager
    private lateinit var sensor : Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensormanager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0 == null) return
        var str : String = ""
        str += "X : ${p0.values[0].toString()}\n"
        str += "Y : ${p0.values[1].toString()}\n"
        str += "Z : ${p0.values[2].toString()}\n"
        text.setText(str)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensormanager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensormanager.unregisterListener(this)
    }
}
