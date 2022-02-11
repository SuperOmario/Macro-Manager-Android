package com.macro_manager.macroapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer
import android.view.SurfaceView
import android.widget.TextView
import android.os.Bundle
import com.macro_manager.macroapp.R
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Toast
import android.view.SurfaceHolder
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.Detector.Detections
import android.util.SparseArray
import android.os.Looper
import android.view.View
import com.google.android.gms.vision.Detector
import com.macro_manager.macroapp.MenuScanner
import java.io.IOException
import java.lang.StringBuilder

// adapted from https://github.com/nandantal/TextDetectionSample/blob/master/app/src/main/java/com/example/textdetectionsample/MainActivity.java
class MenuScanner : AppCompatActivity() {
    private var mCameraSource: CameraSource? = null
    private var mTextRecognizer: TextRecognizer? = null
    private var mSurfaceView: SurfaceView? = null
    private var mTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_scanner)
        mSurfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView
        mTextView = findViewById<View>(R.id.txtScannedText) as TextView
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startTextRecognizer()
        } else {
            askCameraPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraSource!!.release()
    }

    private fun startTextRecognizer() {
        mTextRecognizer = TextRecognizer.Builder(applicationContext).build()
        if (!mTextRecognizer.isOperational()) {
            Toast.makeText(
                applicationContext,
                "Oops ! Not able to start the text recognizer ...",
                Toast.LENGTH_LONG
            ).show()
        } else {
            mCameraSource = CameraSource.Builder(applicationContext, mTextRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build()
            mSurfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        try {
                            mCameraSource.start(mSurfaceView!!.holder)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        askCameraPermission()
                    }
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    mCameraSource.stop()
                }
            })
            mTextRecognizer.setProcessor(object : Detector.Processor<TextBlock?> {
                override fun release() {}
                override fun receiveDetections(detections: Detections<TextBlock?>) {
                    val items = detections.detectedItems
                    val stringBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        if (item != null && item.value != null) {
                            stringBuilder.append(item.value + " ")
                        }
                    }
                    val fullText = stringBuilder.toString()
                    val handler = Handler(Looper.getMainLooper())
                    handler.post { mTextView!!.text = fullText }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTextRecognizer()
            return
        }
    }

    private fun askCameraPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }
    }

    companion object {
        private const val RC_HANDLE_CAMERA_PERM = 2
    }
}