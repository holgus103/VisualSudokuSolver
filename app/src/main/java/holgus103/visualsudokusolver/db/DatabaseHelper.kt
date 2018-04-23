package holgus103.visualsudokusolver.db


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import holgus103.visualsudokusolver.db.dao.SudokuEntry

/**
 * Created by holgus103 on 25/03/18.
 */

class DatabaseHelper(ctx: Context) : OrmLiteSqliteOpenHelper(ctx, "sudokus.db", null, 1){

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<SudokuEntry, Any>(connectionSource, SudokuEntry::class.java, true)
    }

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource, SudokuEntry::class.java)
    }

}