package holgus103.visualsudokusolver

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import holgus103.visualsudokusolver.db.SudokuEntriesAdapter
import android.os.Environment.DIRECTORY_PICTURES
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import holgus103.visualsudokusolver.threading.ModelLoader
import holgus103.visualsudokusolver.threading.RecognitionRunner
import java.net.URI
import java.util.*


class MainActivity : SudokuBaseActivity() {

    override fun onResume() {
        super.onResume()
        if(this.leftActivity){
            setContentView(R.layout.activity_main);
            this.setUpAdapter()
        }
    }
    var leftActivity: Boolean = false;

    companion object {
        var imagePath: String? = null;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestPermissions();
        this.leftActivity = false;
        this.setUpAdapter();

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        try {
            val p = File(storageDir.absolutePath + '/' + MODEL_PATH);
            if(!p.exists()){
                setContentView(R.layout.model_download)
                ModelLoader(this, findViewById(R.id.downloaded))
                        .execute(getString(R.string.model_url), p.absolutePath)
            }

        }
        catch(ex: Exception){
            Log.e("EXCEPTION", ex.message)
        }
        // Example of a call to a native method
//        sample_text.text = stringFromJNI()
    }

    fun setUpAdapter(){
        entries.adapter = SudokuEntriesAdapter(
                this,
                SudokuApp.instance.dao.getAllOrdered(),
                0,
                {id -> openHistoricEntry(id)})
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
            imagePath = p.absolutePath;
            val uri =  FileProvider.getUriForFile(this, getString(R.string.authority), p);
            p.delete();
            return uri;
        }
        catch(e : Exception) {
            Log.e("EXCEPTION", getString(R.string.no_file));
            Toast.makeText(this, getString(R.string.no_sd), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    fun recognize(v: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val uri = this.createImageFile();
            if(uri == null) {
                Log.e("ERROR", "Couldn't create file!")
                Toast.makeText(this, getString(R.string.file_not_created), Toast.LENGTH_LONG).show();
                return;
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun settings(v: View?) {
        this.startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(imagePath == null){
                Log.e("ERROR", "imagePath is null!");
            }
            this.finalizeRecognition();

        }
    }

    fun finalizeRecognition(){
        setContentView(R.layout.loading)
        this.leftActivity = false;
        val uri =  FileProvider.getUriForFile(this, getString(R.string.authority), File(imagePath));
        this.contentResolver.notifyChange(uri, null);
        RecognitionRunner().execute(this)
    }

    fun restoreInterface(){
        setContentView(R.layout.activity_main);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                this.settings(null);
                true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET);

        for (v: String in permissions) {

            if (ContextCompat.checkSelfPermission(this, v)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(v),
                        PERMISSION_GRANTED);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant

            }
        }
    }

}
