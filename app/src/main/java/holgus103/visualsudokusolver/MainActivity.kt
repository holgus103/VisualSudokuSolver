package holgus103.visualsudokusolver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.os.Environment
import holgus103.visualsudokusolver.db.SudokuEntriesAdapter
import android.os.Environment.DIRECTORY_PICTURES
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import android.support.v4.content.FileProvider
import java.util.*


class MainActivity : SudokuBaseActivity() {

    var photoPath: String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        entries.adapter = SudokuEntriesAdapter(
                this,
                SudokuApp.instance.dao.getAllOrdered(),
                0)
        // Example of a call to a native method
//        sample_text.text = stringFromJNI()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        this.photoPath = image.getAbsolutePath();
        return image
    }

    fun recognize(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null;
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            try {
                file = this.createImageFile();
            } catch (err: IOException) {
                //TODO: error handling
            }

            if (file != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        file)
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
            val i = Intent(this, SudokuEditActivity::class.java);
            this.rawSudoku = this.runRecognition();
            i.putExtra(getString(R.string.sudoku), this.rawSudoku);
            this.startActivity(i)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun runRecognition(): IntArray

    companion object {

        const val REQUEST_IMAGE_CAPTURE = 1

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
