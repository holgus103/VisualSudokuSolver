package holgus103.visualsudokusolver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
//        sample_text.text = stringFromJNI()
    }



    fun recognize(v: View){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun settings(v: View){
        this.startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val i = Intent(this, SudokuEditActivity::class.java);

            this.startActivity(i)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        const val REQUEST_IMAGE_CAPTURE = 1
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
