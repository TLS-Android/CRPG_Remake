@file:Suppress("KDocUnresolvedReference")

package com.tiagosantos.access.modal.gossip

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@SuppressWarnings("LeakingThisInConstructor")
open class Gossip(private val context: Context) : TextToSpeech.OnInitListener {

    val textToSpeech: TextToSpeech = TextToSpeech(context, this)

    private var activity: Activity? = null

    private var initialized = false
    var isMuted = false
        private set
    private var playOnInit: String = EMPTY_STRING
    private var queueMode = TextToSpeech.QUEUE_FLUSH

    private val onStartJobs = HashMap<String, suspend () -> Unit>()
    private val onDoneJobs = HashMap<String, suspend () -> Unit>()
    private val onErrorJobs = HashMap<String, suspend () -> Unit>()

    val availableLanguages: Set<Locale>
        get() = textToSpeech.availableLanguages

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true
            playInternal(playOnInit, UTTERANCE_ID_NONE)
        } else {
            Log.e(TAG, "Initialization failed.")
        }
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
        enableVolumeControl(this.activity)
    }

    fun talk(text: CharSequence) {
        talk(text.toString(), null, null, null)
        println("is talking")
    }

    fun talkAndOnStart(text: String, onStart: (suspend () -> Unit)?) {
        talk(text, onStart, null, null)
    }

    fun talkAndOnDone(text: String, onDone: (suspend () -> Unit)?) {
        talk(text, null, onDone, null)
    }

    fun talkAndOnError(text: String, onError: (suspend () -> Unit)?) {
        talk(text, null, null, onError)
    }

    @Synchronized
    fun talk(
        receivedText: String,
        onStart: (suspend () -> Unit)?,
        onDone: (suspend () -> Unit)?,
        onError: (suspend () -> Unit)?
    ) {
        if (initialized) {
            val utteranceId = UUID.randomUUID().toString()
            if (onStart != null) {
                onStartJobs[utteranceId] = onStart
            }
            if (onDone != null) {
                onDoneJobs[utteranceId] = onDone
            }
            if (onError != null) {
                onErrorJobs[utteranceId] = onError
            }
            playInternal(receivedText, utteranceId)
        } else {
            playOnInit = receivedText
        }
    }

    fun stop() {
        if (::job.isInitialized) {
            job.cancel()
        }
        textToSpeech.stop()
    }

    open fun isPlaying(): Boolean = textToSpeech.isSpeaking

    private fun playInternal(text: String, utteranceId: String) {
        if (isMuted) {
            return
        }
        Log.d(TAG, "Playing: \"$text\"")
        textToSpeech.speak(text, queueMode, null, utteranceId)
    }

    fun mute() {
        isMuted = true
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
    }

    fun unmute() {
        isMuted = false
    }

    fun setSpeed(speed1: Float) {
        textToSpeech.setSpeechRate(speed1)
    }

    fun requestAudioFocus() {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
        am.requestAudioFocus(request)
    }

    fun abandonAudioFocus() {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
        am.abandonAudioFocusRequest(request)
    }

    private fun enableVolumeControl(activity: Activity?) {
        if (activity != null) {
            activity.volumeControlStream = AudioManager.STREAM_MUSIC
        }
    }

    fun disableVolumeControl(activity: Activity?) {
        if (activity != null) {
            activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
        }
    }

    fun setQueueMode(queueMode: Int) {
        this.queueMode = queueMode
    }

    fun setLanguage(locale: Locale) {
        val er = textToSpeech.setLanguage(locale)
        if (er == TextToSpeech.LANG_MISSING_DATA || er == TextToSpeech.LANG_NOT_SUPPORTED) {
            textToSpeech.language = Locale.getDefault()
        }
    }

    /**
     * Shutdown the [TextToSpeech] object and unregister activity lifecycle callbacks
     */
    fun shutdown() {
        textToSpeech.shutdown()
        if (::job.isInitialized) {
            job.cancel()
        }
    }

    /**
     * Find the runnable for a given utterance id, run it on the main thread and then remove
     * it from the map
     * @param utteranceId the id key to use
     * @param hashMap utteranceIds to runnable map to use
     * @return whether value was found
     */
    private lateinit var job: Job

    @Synchronized
    private fun detectAndRun(
        utteranceId: String,
        hashMap: HashMap<String, suspend () -> Unit>
    ): Boolean {
        return if (!hashMap.containsKey(utteranceId)) {
            false
        } else {
            job = Job()
            val call = hashMap[utteranceId]
            val scope = CoroutineScope(Dispatchers.Main + job)
            scope.launch {
                call?.invoke()
            }
            true
        }
    }

    private var audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> textToSpeech.setPitch(FOCUS_PITCH)
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> textToSpeech.setPitch(
                    DUCK_PITCH
                )
            }
        }


    private var utteranceProgressListener: UtteranceProgressListener =
        object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
                detectAndRun(utteranceId, onStartJobs)
            }

            override fun onDone(utteranceId: String) {
                if (detectAndRun(utteranceId, onDoneJobs)) {
                    if (onErrorJobs.containsKey(utteranceId)) {
                        onErrorJobs.remove(utteranceId)
                    }
                }
            }

            @Deprecated("Old stuff")
            override fun onError(utteranceId: String) {
                if (detectAndRun(utteranceId, onErrorJobs)) {
                    if (onDoneJobs.containsKey(utteranceId)) {
                        onDoneJobs.remove(utteranceId)
                    }
                }
            }
        }


    init { this.textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener) }

    companion object {

        internal val TAG = Gossip::class.java.simpleName

        /**
         * Pitch when we have focus
         */
        private const val FOCUS_PITCH = 1.0f

        /**
         * Pitch when we should duck audio for another app
         */
        private const val DUCK_PITCH = 0.5f

        /**
         * ID for when no text is spoken
         */
        const val UTTERANCE_ID_NONE = "-1"
    }

}
