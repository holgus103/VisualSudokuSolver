package holgus103.visualsudokusolver.db.dao

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "SolvedSudokus")
data class SudokuEntry(

    @DatabaseField(generatedId = true)
    var id: Int? = null,

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    var sudoku: IntArray = IntArray(81),

    @DatabaseField()
    var timestamp: Date = Date()

)