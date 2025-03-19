import java.io.File
import java.util.*

class Note(var title: String, var content: String) {
    override fun toString(): String {
        return "$title: $content"
    }
}

class NoteApp {
    private val notes = mutableMapOf<String, Note>()
    private val notesDirectory = File(System.getProperty("user.dir"), "notes")

    init {
        if (!notesDirectory.exists()) {
            notesDirectory.mkdir()
        }
        loadNotes()
    }

    private fun saveNote(note: Note) {
        val file = File(notesDirectory, "${note.title}.txt")
        file.writeText(note.content)
    }

    private fun loadNote(title: String): Note? {
        val file = File(notesDirectory, "$title.txt")
        if (file.exists()) {
            val content = file.readText()
            return Note(title, content)
        }
        return null
    }

    private fun loadNotes() {
        notesDirectory.listFiles()?.forEach { file ->
            if (file.isFile && file.extension == "txt") {
                val title = file.nameWithoutExtension
                loadNote(title)?.let { notes[title] = it }
            }
        }
    }

    private fun deleteNoteFile(title: String) {
        val file = File(notesDirectory, "$title.txt")
        file.delete()
    }

    fun createNote() {
        print("Enter note title: ")
        val title = readLine() ?: return
        print("Enter note content: ")
        val content = readLine() ?: return

        val note = Note(title, content)
        notes[title] = note
        saveNote(note)
        println("Note '$title' created.")
    }

    fun editNote() {
        print("Enter note title to edit: ")
        val title = readLine() ?: return

        val existingNote = notes[title]
        if (existingNote == null) {
            println("Note '$title' not found.")
            return
        }

        print("Enter new note content: ")
        val newContent = readLine() ?: return

        existingNote.content = newContent
        saveNote(existingNote)
        println("Note '$title' edited.")
    }

    fun viewNote() {
        print("Enter note title to view: ")
        val title = readLine() ?: return

        val note = notes[title]
        if (note == null) {
            println("Note '$title' not found.")
            return
        }

        println(note)
    }

    fun listNotes() {
        if (notes.isEmpty()) {
            println("No notes found.")
            return
        }

        println("Notes:")
        notes.keys.sorted().forEach { title ->
            println("- $title")
        }
    }

    fun deleteNote() {
        print("Enter note title to delete: ")
        val title = readLine() ?: return

        if (notes.containsKey(title)) {
            notes.remove(title)
            deleteNoteFile(title)
            println("Note '$title' deleted.")
        } else {
            println("Note '$title' not found.")
        }
    }

    fun run() {
        while (true) {
            println("\n--- Note App ---")
            println("1. Create Note")
            println("2. Edit Note")
            println("3. View Note")
            println("4. List Notes")
            println("5. Delete Note")
            println("6. Exit")
            print("Enter your choice: ")

            when (readLine()?.toIntOrNull()) {
                1 -> createNote()
                2 -> editNote()
                3 -> viewNote()
                4 -> listNotes()
                5 -> deleteNote()
                6 -> return
                else -> println("Invalid choice.")
            }
        }
    }
}

fun main() {
    val app = NoteApp()
    app.run()
}