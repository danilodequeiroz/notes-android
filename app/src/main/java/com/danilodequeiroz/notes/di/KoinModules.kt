package com.danilodequeiroz.notes.di


import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCase
import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCaseImpl
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCaseImpl
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCaseImpl
import com.danilodequeiroz.notes.domain.notepersistence.*
import com.danilodequeiroz.notes.ui.login.LoginViewModel
import com.danilodequeiroz.notes.ui.notelist.NoteListViewModel
import com.danilodequeiroz.notes.ui.noteupdate.CreateUpdateNoteViewModel
import com.danilodequeiroz.notes_persistence.*
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SCHEDULER_SUBSCRIBE_ON_IO = "SCHEDULER_SUBSCRIBE_ON_IO"
const val SCHEDULER_OBSERVE_ON_MAIN_THREAD = "SCHEDULER_OBSERVE_ON_MAIN_THREAD"

val schedulerIO = named(SCHEDULER_SUBSCRIBE_ON_IO)
val schedulerUI = named(SCHEDULER_OBSERVE_ON_MAIN_THREAD)
val notesRepository = named(NotesRepository::class.java.simpleName)
val loginDataRepository = named(LoginDataRepository::class.java.simpleName)

val viewModelModule = module {

    viewModel {
        NoteListViewModel(
            get(),
            get(),
            get(),
            get(),
            get(schedulerIO),
            get(schedulerUI)
        )
    }

    viewModel {
        CreateUpdateNoteViewModel(
            get(),
            get(schedulerIO),
            get(schedulerUI)
        )
    }

    viewModel {
        LoginViewModel(
            get(),
            get(),
            get(),
            get(schedulerIO),
            get(schedulerUI)
        )
    }

}

val useCaseModules = module {
    factory<DeleteAllNotesUseCase> {
        DeleteAllNotesUseCaseImpl(
            get(notesRepository)
        )
    }
    factory<DeleteNoteByIdUseCase> {
        DeleteNoteByIdUseCaseImpl(
            get(notesRepository)
        )
    }
    factory<GetNoteByIdUseCase> {
        GetNoteByIdUseCaseImpl(
            get(notesRepository)
        )
    }
    factory<GetNoteListUseCase> {
        GetNoteListUseCaseImpl(
            get(notesRepository)
        )
    }
    factory<UpdateNoteUseCase> {
        UpdateNoteUseCaseImpl(
            get(notesRepository)
        )
    }
    factory<DoLoginUseCase> { DoLoginUseCaseImpl() }

    factory<GetAllLoginDataUseCase> {
        GetAllLoginDataUseCaseImpl(
            get(loginDataRepository)
        )
    }
    factory<InsertLoginDataUseCase> {
        InsertLoginDataUseCaseImpl(
            get(loginDataRepository)
        )
    }
}

val databaseModule = module {

    factory<Scheduler>(schedulerIO) { Schedulers.newThread() }

    factory<Scheduler>(schedulerUI) { AndroidSchedulers.mainThread() }

    factory<NotesRepository>(notesRepository) { NotesRepositoryImpl(get()) }
    factory<LoginDataRepository>(loginDataRepository) { LoginDataRepositoryImpl(get()) }

    factory<NoteDao> { NotesDatabase.getInstance(androidContext()).noteDao() }
    factory<LoginDataDao> { NotesDatabase.getInstance(androidContext()).loginDao() }

}