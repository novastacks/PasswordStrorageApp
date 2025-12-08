package com.example.passwordstorageapp.feature.home

class EntryViewModel {
    var serviceName: String = ""
    var username: String = ""
    var password: String = ""
    var notes: String? = null

    fun currentEntry(service : String, user : String, pass : String, note : String?){
        serviceName = service
        username = user
        password = pass
        notes = note
    }

    fun clearEntry(){
        serviceName = ""
        username = ""
        password = ""
        notes = null
    }
}