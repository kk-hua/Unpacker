package com.loop.unpacker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun test(view: View) {
        val render: Test =
            RenderPool.get(1)
                .render(byteArrayOf(0x0, 0x1, 0x2, 0x0, 0x0, 0x5, 0x6, 0x7, 0x8, 0x9)) as Test
        Log.e("MainActivity", "obj=$render")
    }
}