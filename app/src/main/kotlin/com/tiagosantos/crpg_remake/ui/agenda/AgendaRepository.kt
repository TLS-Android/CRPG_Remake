package com.tiagosantos.crpg_remake.ui.agenda

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.model.EventType
import com.tiagosantos.common.ui.utils.Constants
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

object AgendaRepository {
    private val TAG = "AgendaRepository"
    private lateinit var database: DatabaseReference
    // create 2 fixed events for lunch and dinner
    var lunchEvent = Event(
        "Almoço", "Clicar para escolher refeição", EventType.MEAL,
        "1200", "1300",
        "", "", "", false, 0
    )
    var dinnerEvent = Event(
        "Jantar", "Clicar para escolher refeição",
        EventType.MEAL, "2000", "2100",
        "", "", "", false, 0
    )

    val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", 
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", 
            |"start_time": "2000","end_time": "2100","date": "2021-03-17"},
            |{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", 
            |"start_time": "0830","end_time": "1330","date": "2021-03-17"},
            |{"title": "Actividade","info":"Sala 12","type": "ACTIVITY",
            | "start_time": "0830","end_time": "1330","date": "2021-03-17"}]""".trimMargin()

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
