package com.tiagosantos.crpg_remake.ui.agenda

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.utils.Constants
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

object AgendaRepository {
    val gson = Gson()

    fun initializeDbRef() {
        database = Firebase.database.reference
        // database = Firebase.database.getReferenceFromUrl("https://crpg-1a3d5-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    fun getEventCollectionFromJSON(): ArrayList<Event> {
        val fullFilename = "${Constants.FILES_DIR}/${AgendaViewModel.EVENT_FILENAME}"
        populateFile()

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val privateList: ArrayList<Event> = AgendaViewModel.gson.fromJson(FileReader(fullFilename), type)

        // addMealsToPrivateEvents()
        // verifyMealsNotAdded()
        concatenatePublicPrivateEvents()

        return privateList
    }

    fun getEventCollectionFromJSONWithoutPopulate(): ArrayList<Event> {
        val privateList: ArrayList<Event>
        val fullFilename = "${Constants.FILES_DIR}/${AgendaViewModel.EVENT_FILENAME}"

        val file = File(fullFilename)

        if (file.exists()) {
            print("$fullFilename does exist.")
        } else {
            print("$fullFilename does not exist.")
            populateFile()
        }

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        privateList = AgendaViewModel.gson.fromJson(FileReader(fullFilename), type)
        return privateList
    }

    private fun addMealsToPrivateEvents(): ArrayList<Event> {
        privateEventList.add(AgendaViewModel.repo.lunchEvent)
        privateEventList.add(AgendaViewModel.repo.dinnerEvent)
        return privateEventList
    }

    private fun addMealsToPublicEvents(): ArrayList<Event> {
        publicEventList.add(AgendaViewModel.repo.lunchEvent)
        publicEventList.add(AgendaViewModel.repo.dinnerEvent)
        return publicEventList
    }

    private fun concatenatePublicPrivateEvents(): ArrayList<Event> {
        addMealsToPrivateEvents()
        addMealsToPublicEvents()
        mDataList.plusAssign((privateEventList + publicEventList) as ArrayList<Event>)
        return mDataList
    }

    private fun populateFile() {
        val fullFilename = "${Constants.FILES_DIR}/${AgendaViewModel.EVENT_FILENAME}"
        val file = File(fullFilename)

        // create a new file
        val isNewFileCreated: Boolean = file.createNewFile()

        if (isNewFileCreated) {
            println("$fullFilename is created successfully.")
        } else {
            println("$fullFilename already exists.")
        }

        File(fullFilename).writeText(AgendaViewModel.repo.fileContent)
    }

}
