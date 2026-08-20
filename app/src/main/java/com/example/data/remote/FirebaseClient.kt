package com.example.data.remote

import com.example.data.models.GameRoom
import com.example.data.models.Player
import com.example.data.models.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class FirebaseClient {
    private val baseUrl = "https://qcaa-chem-arena-default-rtdb.firebaseio.com"
    private val apiKey = "AIzaSyCrrbJAel1IvoUzSijaU_4Eu2nWHxWKQZw"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun buildUrl(path: String): String {
        return "$baseUrl/$path.json?auth=$apiKey"
    }

    suspend fun getRoom(roomCode: String): GameRoom? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode"))
                .get()
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null
            val body = response.body?.string() ?: return@withContext null
            if (body == "null" || body.isBlank()) return@withContext null

            parseGameRoom(roomCode, JSONObject(body))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createOrUpdateRoom(roomCode: String, room: GameRoom): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = serializeGameRoom(room)
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode"))
                .put(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateRoomField(roomCode: String, field: String, value: Any): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject()
            json.put(field, value)
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode"))
                .patch(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addPlayer(roomCode: String, player: Player): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("name", player.name)
                put("avatar", player.avatar)
                put("color", player.color)
                put("score", player.score)
                put("host", player.host)
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode/players/${player.id}"))
                .put(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updatePlayerScore(roomCode: String, playerId: String, score: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply { put("score", score) }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode/players/$playerId"))
                .patch(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun buzzIn(roomCode: String, playerId: String, playerName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val checkReq = Request.Builder().url(buildUrl("rooms/$roomCode/buzzed")).get().build()
            val checkResp = client.newCall(checkReq).execute()
            val existing = checkResp.body?.string()
            if (existing != null && existing != "null" && existing.isNotBlank()) {
                return@withContext false // someone already buzzed
            }

            val json = JSONObject().apply {
                put("playerId", playerId)
                put("playerName", playerName)
                put("timestamp", System.currentTimeMillis())
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode/buzzed"))
                .put(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun claimAnswer(roomCode: String, questionIndex: Int, playerId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val checkReq = Request.Builder().url(buildUrl("rooms/$roomCode/answeredBy/$questionIndex")).get().build()
            val checkResp = client.newCall(checkReq).execute()
            val existing = checkResp.body?.string()?.replace("\"", "")
            if (existing != null && existing != "null" && existing.isNotBlank() && existing != playerId) {
                return@withContext false
            }

            val body = "\"$playerId\"".toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode/answeredBy/$questionIndex"))
                .put(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun leaveRoom(roomCode: String, playerId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(buildUrl("rooms/$roomCode/players/$playerId"))
                .delete()
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Cloud Save Syncing
    suspend fun syncCloudProfile(userId: String, profileJson: JSONObject): Boolean = withContext(Dispatchers.IO) {
        try {
            val body = profileJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(buildUrl("cloud_profiles/$userId"))
                .put(body)
                .build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getCloudProfile(userId: String): JSONObject? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(buildUrl("cloud_profiles/$userId"))
                .get()
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null
            val body = response.body?.string() ?: return@withContext null
            if (body == "null" || body.isBlank()) return@withContext null
            JSONObject(body)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun serializeGameRoom(room: GameRoom): JSONObject {
        return JSONObject().apply {
            put("host", room.hostId)
            put("mode", room.mode)
            put("status", room.status)
            put("currentQ", room.currentQ)
            put("createdAt", room.createdAt)

            val topicsArr = JSONArray()
            room.topics.forEach { topicsArr.put(it) }
            put("topics", topicsArr)

            val qArr = JSONArray()
            room.questions.forEach { q ->
                val qObj = JSONObject().apply {
                    put("id", q.id)
                    put("topic", q.topic)
                    put("type", q.type)
                    put("q", q.q)
                    put("answer", q.answerIndex)
                    put("answerShort", q.answerShort)
                    put("explanation", q.explanation)
                    val opts = JSONArray()
                    q.options.forEach { opts.put(it) }
                    put("options", opts)
                }
                qArr.put(qObj)
            }
            put("questions", qArr)

            val playersObj = JSONObject()
            room.players.forEach { (id, p) ->
                val pObj = JSONObject().apply {
                    put("name", p.name)
                    put("avatar", p.avatar)
                    put("color", p.color)
                    put("score", p.score)
                    put("host", p.host)
                }
                playersObj.put(id, pObj)
            }
            put("players", playersObj)
        }
    }

    private fun parseGameRoom(roomCode: String, json: JSONObject): GameRoom {
        val hostId = json.optString("host", "")
        val mode = json.optString("mode", "quiz")
        val status = json.optString("status", "waiting")
        val currentQ = json.optInt("currentQ", 0)
        val createdAt = json.optLong("createdAt", System.currentTimeMillis())

        val topicsList = mutableListOf<String>()
        val topicsArr = json.optJSONArray("topics")
        if (topicsArr != null) {
            for (i in 0 until topicsArr.length()) {
                topicsList.add(topicsArr.optString(i))
            }
        }

        val questionsList = mutableListOf<Question>()
        val qArr = json.optJSONArray("questions")
        if (qArr != null) {
            for (i in 0 until qArr.length()) {
                val qObj = qArr.optJSONObject(i) ?: continue
                val optsList = mutableListOf<String>()
                val optsArr = qObj.optJSONArray("options")
                if (optsArr != null) {
                    for (j in 0 until optsArr.length()) {
                        optsList.add(optsArr.optString(j))
                    }
                }
                questionsList.add(
                    Question(
                        id = qObj.optString("id", "q_$i"),
                        topic = qObj.optString("topic", "Chemistry"),
                        type = qObj.optString("type", "mcq"),
                        q = qObj.optString("q", ""),
                        options = optsList,
                        answerIndex = qObj.optInt("answer", 0),
                        answerShort = qObj.optString("answerShort", ""),
                        explanation = qObj.optString("explanation", "")
                    )
                )
            }
        }

        val playersMap = mutableMapOf<String, Player>()
        val playersObj = json.optJSONObject("players")
        if (playersObj != null) {
            val keys = playersObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val pObj = playersObj.optJSONObject(key) ?: continue
                playersMap[key] = Player(
                    id = key,
                    name = pObj.optString("name", "Player"),
                    avatar = pObj.optString("avatar", "🧪"),
                    color = pObj.optString("color", "#00E5FF"),
                    score = pObj.optInt("score", 0),
                    host = pObj.optBoolean("host", false)
                )
            }
        }

        val buzzedObj = json.optJSONObject("buzzed")
        val buzzedId = buzzedObj?.optString("playerId")
        val buzzedName = buzzedObj?.optString("playerName")

        val answeredByMap = mutableMapOf<String, String>()
        val ansObj = json.optJSONObject("answeredBy")
        if (ansObj != null) {
            val ansKeys = ansObj.keys()
            while (ansKeys.hasNext()) {
                val k = ansKeys.next()
                answeredByMap[k] = ansObj.optString(k, "")
            }
        }

        return GameRoom(
            code = roomCode,
            hostId = hostId,
            mode = mode,
            status = status,
            questions = questionsList,
            topics = topicsList,
            currentQ = currentQ,
            players = playersMap,
            buzzedPlayerId = buzzedId,
            buzzedPlayerName = buzzedName,
            answeredBy = answeredByMap,
            createdAt = createdAt
        )
    }
}
