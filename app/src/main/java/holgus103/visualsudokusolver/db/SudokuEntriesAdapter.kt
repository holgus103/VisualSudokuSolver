package holgus103.visualsudokusolver.db

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import holgus103.visualsudokusolver.R
import holgus103.visualsudokusolver.db.dao.SudokuDao

class SudokuEntriesAdapter(ctx: Context, cursor: Cursor, flags: Int) : CursorAdapter(ctx, cursor, flags){

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        view?.findViewById<TextView>(R.id.timestamp)?.text = cursor?.
                getString(cursor?.getColumnIndexOrThrow(SudokuDao.TIMESTAMP));

    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }

}