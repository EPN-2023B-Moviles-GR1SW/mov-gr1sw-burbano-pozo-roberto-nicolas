package com.example.examen

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

val firestore = FirebaseFirestore.getInstance()

class FirebaseDB {
    companion object {

        fun retrieveAllTeams() {
            val collectionRef = firestore.collection("teams")
            val teams = ArrayList<DTeam>()
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val data = document.data
                        Log.e("FIREBASE", document.toString())

                        Log.e("FIREBASE", data.toString())

                        teams.add(
                            DTeam(
                                "1",
                                document.data["name"] as String,
                                document.data["foundationDate"] as String,
                                document.data["netIncome"] as Long,
                                document.data["isActive"] as Boolean
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FIREBASE", "Error getting documents: $exception")
                }
        }

        fun retrieveTeam(teamId: String?): DTeam {
            return DTeam("1", "a", "2023", 1.3.toLong(), true, )
        }

        fun createTeam(toString: String, toString1: String, toFloat: Float, checked: Boolean) {

        }

        fun updateTeam(
            toString: String,
            toString1: String,
            toFloat: Float,
            checked: Boolean,
            id: String
        ) {

        }

        fun delete(item: String, s: String) {

        }


        fun retrievePlayer(playerId: Int): DPlayer {
            return DPlayer(" ", "a", true, "ga", 100,"2")
        }

        fun updatePlayer(
            selectedDate: String,
            checked: Boolean,
            toString: String,
            toFloat: Float,
            teamId: Int,
            playerId: Int
        ) {

        }

        fun createPlayer(
            toString: String,
            checked: Boolean,
            toString1: String,
            toFloat: Float,
            teamId: Int
        ) {

        }

        fun retrieveByTeam(currentId: Int): ArrayList<DPlayer>? {
            return ArrayList<DPlayer>()
        }

    }

}