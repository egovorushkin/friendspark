package com.friendspark.di

/**
 * Koin module for dependency injection in FriendSpark.
 * Provides AuthRepository and AuthViewModel as singletons.
 */
import org.koin.dsl.module
import com.friendspark.ui.AuthViewModel
import com.friendspark.data.repository.AuthRepository

val appModule = module {
    /**
     * Provides a singleton instance of FirebaseAuthManager.
     */
    single<FirebaseAuthManager> { FirebaseAuthManager() }
    /**
     * Provides a singleton instance of FriendSparkApi.
     * Replace FriendSparkApiImpl with your actual implementation if needed.
     */
    single<FriendSparkApi> { FriendSparkApiImpl() }
    /**
     * Provides a singleton instance of AuthRepository.
     * Requires FirebaseAuthManager and FriendSparkApi as dependencies.
     */
    single { AuthRepository(get(), get()) }
    /**
     * Provides a singleton instance of AuthViewModel.
     * Requires AuthRepository as a dependency.
     */
    single { AuthViewModel(get()) }
    // Add other dependencies as needed
}
