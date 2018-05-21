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
import java.util.*


class MainActivity : SudokuBaseActivity() {

    var image: File? = null;
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
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val p = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
//        this.photoPath = image.getAbsolutePath();
        return p
    }

    fun recognize(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null;
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            try {
                file = this.createImageFile();
                image = file
            } catch (err: IOException) {
                Log.d("EXCEPTION", err.message)
            }

            if (image != null) {
                val photoURI = Uri.fromFile(image)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun settings(v: View) {
        this.startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(image == null){
                Log.d("ERROR", "Image file empty!")
                return
            };
            val i = Intent(this, SudokuEditActivity::class.java);
            val b = image?.readBytes();
//            val file = File(this.photoPath);
            // TODO: pass Bitmap somehow
            this.rawSudoku = this.runRecognition(image!!.absolutePath);
            image?.delete();
            i.putExtra(getString(R.string.sudoku), this.rawSudoku);
            this.startActivity(i)
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
