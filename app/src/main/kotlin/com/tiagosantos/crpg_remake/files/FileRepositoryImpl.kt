package com.tiagosantos.crpg_remake.files

import com.google.gson.Gson
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import java.io.File

class FileRepositoryImpl {
    val filename : String = EMPTY_STRING
    val fullFilename : String = EMPTY_STRING
    val file = File(fullFilename)
    val fileExists = file.exists()
    val isNewFileCreated : Boolean = file.createNewFile()
}

object GsonService {
    val gson = Gson()
}
