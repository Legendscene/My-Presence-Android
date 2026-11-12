package com.kyrx.mypresence.core.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin Crashlytics facade.
 *
 * Firebase is optional: local builds can run without google-services.json, in which case
 * the default FirebaseApp never initialises and every call here throws. Each call is
 * fail-soft so a missing analytics backend can never take the app down.
 */
@Singleton
class CrashReporter @Inject constructor() {

    fun logNonFatal(throwable: Throwable) = safe {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    fun setUserId(id: String) = safe {
        FirebaseCrashlytics.getInstance().setUserId(id)
    }

    fun setCustomKey(key: String, value: String) = safe {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Int) = safe {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) = safe {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Long) = safe {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Double) = safe {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    fun log(message: String) = safe {
        FirebaseCrashlytics.getInstance().log(message)
    }

    private inline fun safe(block: () -> Unit) {
        runCatching(block)
    }
}
