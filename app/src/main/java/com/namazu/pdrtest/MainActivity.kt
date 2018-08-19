package com.namazu.pdrtest

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SensorEventListener {

    private lateinit var sensormanager : SensorManager
    private lateinit var accelsensor : Sensor
    private lateinit var compasssensor : Sensor
    private lateinit var gyrosensor : Sensor
    private lateinit var magnetsensor : Sensor
    private val names : Array<String> = arrayOf("x-axis","y-axis","z-axis")
    private val namesForRotate : Array<String> = arrayOf("x*sin(theta/2)","y*sin(theta/2)","z*sin(theta/2)","cos(theta/2)","Accuracy")
    private val colors : Array<Int> = arrayOf(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.GRAY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensormanager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelsensor = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        compasssensor = sensormanager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
        gyrosensor = sensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetsensor = sensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        accelChart.setDescription("")
        accelChart.data = LineData()
        compassChart.setDescription("")
        compassChart.data = LineData()
        gyroChart.setDescription("")
        gyroChart.data = LineData()
        magnetChart.setDescription("")
        magnetChart.data = LineData()
        for(i in 0..2)accelChart.lineData.addDataSet(createSet(names[i],colors[i]))
        for(i in 0..4)compassChart.lineData.addDataSet(createSet(namesForRotate[i],colors[i]))
        for(i in 0..2)gyroChart.lineData.addDataSet(createSet(names[i],colors[i]))
        for(i in 0..2)magnetChart.lineData.addDataSet(createSet(names[i],colors[i]))
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0 == null) return
        when(p0.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> showAccel(p0)
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> showCompass(p0)
            Sensor.TYPE_GYROSCOPE -> showGyro(p0)
            Sensor.TYPE_MAGNETIC_FIELD -> showMagnet(p0)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensormanager.registerListener(this,accelsensor,SensorManager.SENSOR_DELAY_UI)
        sensormanager.registerListener(this,compasssensor,SensorManager.SENSOR_DELAY_UI)
        sensormanager.registerListener(this,gyrosensor,SensorManager.SENSOR_DELAY_UI)
        sensormanager.registerListener(this,magnetsensor,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensormanager.unregisterListener(this)
    }

    private fun createSet(label: String,color: Int) : LineDataSet {
        val set = LineDataSet(null,label)
        set.lineWidth = 2.5f
        set.color = color
        set.setDrawCircles(false)
        set.setDrawValues(false)

        return set
    }

    private fun showAccel(sensor: SensorEvent){
        var str : String = "Accelometer : \n"
        str += "X : ${sensor.values[0].toString()}\n"
        str += "Y : ${sensor.values[1].toString()}\n"
        str += "Z : ${sensor.values[2].toString()}\n"
        accel.setText(str)
        var data : LineData = accelChart.lineData
        if(data == null) return
        for (i in 0..2){
            var set = data.getDataSetByIndex(i)
            data.addEntry(Entry(set.entryCount.toFloat(),sensor.values[i]),i)
            data.notifyDataChanged()
        }
        accelChart.notifyDataSetChanged()
        accelChart.setVisibleXRangeMaximum(50.0f)
        accelChart.moveViewToX(data.entryCount.toFloat())
    }

    private fun showCompass(sensor: SensorEvent){
        var str : String = "Compass : \n"
        str += "x*sin(theta/2) : ${sensor.values[0].toString()}\n"
        str += "y*sin(theta/2) : ${sensor.values[1].toString()}\n"
        str += "z*sin(theta/2) : ${sensor.values[2].toString()}\n"
        str += "cos(theta/2) : ${sensor.values[3].toString()}\n"
        str += "Accuracy : ${sensor.values[4].toString()}\n"
        compass.setText(str)
        var data : LineData = compassChart.lineData
        if(data == null) return
        for (i in 0..4){
            var set = data.getDataSetByIndex(i)
            data.addEntry(Entry(set.entryCount.toFloat(),sensor.values[i]),i)
            data.notifyDataChanged()
        }
        compassChart.notifyDataSetChanged()
        compassChart.setVisibleXRangeMaximum(50.0f)
        compassChart.moveViewToX(data.entryCount.toFloat())
    }
    private fun showGyro(sensor: SensorEvent){
        var str : String = "Gyro : \n"
        str += "X : ${sensor.values[0].toString()}\n"
        str += "Y : ${sensor.values[1].toString()}\n"
        str += "Z : ${sensor.values[2].toString()}\n"
        gyro.setText(str)
        var data : LineData = gyroChart.lineData
        if(data == null) return
        for (i in 0..2){
            var set = data.getDataSetByIndex(i)
            data.addEntry(Entry(set.entryCount.toFloat(),sensor.values[i]*1000),i)
            data.notifyDataChanged()
        }
        gyroChart.notifyDataSetChanged()
        gyroChart.setVisibleXRangeMaximum(50.0f)
        gyroChart.moveViewToX(data.entryCount.toFloat())
    }
    private fun showMagnet(sensor: SensorEvent){
        var str : String = "Magnet : \n"
        str += "X : ${sensor.values[0].toString()}\n"
        str += "Y : ${sensor.values[1].toString()}\n"
        str += "Z : ${sensor.values[2].toString()}\n"
        magnet.setText(str)
        var data : LineData = magnetChart.lineData
        if(data == null) return
        for (i in 0..2){
            var set = data.getDataSetByIndex(i)
            data.addEntry(Entry(set.entryCount.toFloat(),sensor.values[i]),i)
            data.notifyDataChanged()
        }
        magnetChart.notifyDataSetChanged()
        magnetChart.setVisibleXRangeMaximum(50.0f)
        magnetChart.moveViewToX(data.entryCount.toFloat())
    }
}
