package com.stambulo.kotlinnotes.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.errors.NoAuthException
import com.stambulo.kotlinnotes.data.model.NoteResult
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {

    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val provider: FirestoreProvider = FirestoreProvider(mockAuth, mockDb)

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    @Before
    fun setup() {
        clearAllMocks()

        every { mockUser.uid } returns ""
        every { mockAuth.currentUser } returns mockUser
        // Каждый раз запрашиваем у мокка коллекцию с любым аргументом
        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockResultCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if not auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        // Совершаем тестовое действие
        provider.saveNote(testNotes[0])

        // И прооверяем, был ли выызван set
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }  // Проверить только 1 раз
        //verify(atLeast = 1) { mockDocumentReference.set(testNotes[0]) }  // Как минимум 1 раз
        //verify(atMost = 1) { mockDocumentReference.set(testNotes[0]) }   // Максимум 1 раз
    }

    @Test
    fun `saveNote returns success note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot))
        } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }

        slot.captured.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `subscribeToAllNotes return notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)

        // Перехватываем колбэк. А его параметр ловим в - capture(slot())
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }

        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes return error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }

        slot.captured.onEvent(null, testError)
        assertEquals(testError, result)
    }

    @Test
    fun `deleteNote calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNote returns Not Error`() {
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNote("1").observeForever {
            result = (it as? NoteResult.Error)?.error
        }

        slot.captured.onSuccess(null)
        assertNotEquals("error", result.toString())
    }

    @Test
    fun `deleteNote returns Success`() {
        var result: NoteResult? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNote("1").observeForever {
            result = (it as? NoteResult.Success<*>)
        }

        slot.captured.onSuccess(null)
        assertEquals("Success(data=null)", result.toString())
    }

    @Test
    fun `deleteNote returns Note`() {
        var result: NoteResult? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNote(testNotes[0].id).observeForever {
            result = it
        }

        slot.captured.onSuccess(null)
        assertTrue(result is NoteResult.Success<*>)
    }

    @Test
    fun `getNoteById calls get`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.getNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun `getNoteById returns Note`() {
        var result: NoteResult? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.get().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.getNoteById(testNotes[0].id).observeForever {
            result = it
        }

        slot.captured.onSuccess(mockDocument1)
        assertEquals((result as NoteResult.Success<Note>).data, testNotes[0])
    }
}
