package com.tiagosantos.access

import android.speech.tts.TextToSpeech
import com.tiagosantos.access.modal.gossip.Gossip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.Exception


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    private lateinit var textToSpeech: TextToSpeech = TextToSpeech(context, this)

    @Mock
    private lateinit var gossip: Gossip

    //@Mock
    //private lateinit var mockStringValue: String

    @Mock
    private lateinit var mockException: Exception


    @org.junit.jupiter.api.Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @BeforeEach
    fun setUp() {
        //whenever(mockStringValue).thenReturn("ola")

    }

    @org.junit.jupiter.api.Test
    fun test_mute_is_successful() {
        whenever(gossip.isMuted).then { gossip.mute() }

        assert(!gossip.isMuted)

        verify(gossip).isPlaying()

    }

    @org.junit.jupiter.api.Test
    fun test_talk_is_successful() {
        gossip.talk("ola")

        verify(gossip, atLeastOnce()).talk(eq("ola"), any(), any(), any())
    }

    @org.junit.jupiter.api.Test
    fun test_is_speaking_successful() {
        whenever(gossip.textToSpeech.isSpeaking).thenReturn(true)

        assertEquals(gossip.isPlaying(), true)
    }


    @org.junit.jupiter.api.Test
    fun test_set_language_is_successful() {
        whenever(gossip.isMuted).then { gossip.mute() }

        assert(!gossip.isMuted)

        verify(gossip).isPlaying()

    }

}