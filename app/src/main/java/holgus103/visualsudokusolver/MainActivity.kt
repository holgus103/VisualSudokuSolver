package holgus103.visualsudokusolver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import holgus103.visualsudokusolver.db.SudokuEntriesAdapter
import android.os.Environment.DIRECTORY_PICTURES
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import holgus103.visualsudokusolver.threading.RecognitionRunner
import java.net.URI
import java.util.*


class MainActivity : SudokuBaseActivity() {

    var imagePath: String? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        entries.adapter = SudokuEntriesAdapter(
                this,
                SudokuApp.instance.dao.getAllOrdered(),
                0,
                {id -> openHistoricEntry(id)})
        // Example of a call to a native method
//        sample_text.text = stringFromJNI()
    }

    fun openHistoricEntry(id : Int?) {
        val entry = SudokuApp.instance.dao.get(id);
        val i = Intent(this, SolvedActivity::class.java)
        i.putExtra(getString(R.string.sudoku), entry.sudoku)
        i.putExtra(getString(R.string.raw_sudoku), entry.original)
        i.putExtra(getString(R.string.entry_id), entry.id)
        startActivity(i);

    }

    @Throws(IOException::class)
    private fun createImageFile() : Uri? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        try {
            val p = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir      /* directory */
            )
            this.imagePath = p.absolutePath;
            val uri =  Uri.fromFile(p);
            p.delete();
            return uri;
        }
        catch(e : Exception) {
            Log.e("EXCEPTION", "Can't create file to take picture!");
            Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    fun recognize(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val uri = this.createImageFile();
            if(uri == null) {
                Log.e("ERROR", "Couldn't create file!")
                Toast.makeText(this, "Could not create file", Toast.LENGTH_LONG).show();
                return;
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun settings(v: View) {
        this.startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.contentResolver.notifyChange(Uri.parse(this.imagePath), null);
            RecognitionRunner().execute(this)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun runRecognition(path: String): IntArray

    companion object {

        const val REQUEST_IMAGE_CAPTURE = 1

        // Used to load the 'native-lib' library on application startup.
        init {
            System.load("/data/app/org.opencv.engine-1/lib/arm/libopencv_java3.so")
            System.loadLibrary("native-lib")
        }
    }
}
