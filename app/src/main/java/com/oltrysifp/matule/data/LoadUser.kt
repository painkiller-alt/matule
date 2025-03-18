package com.oltrysifp.matule.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.oltrysifp.matule.composable.toastShort
import com.oltrysifp.matule.models.User
import com.oltrysifp.matule.util.DataStoreManager
import com.oltrysifp.matule.util.log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import java.util.UUID

@Composable
fun LoadUser(
    dataStoreManager: DataStoreManager,
    supabase: SupabaseClient?,
    onSuccess: (User) -> Unit,
    onFailure: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        dataStoreManager.getUser().collect {
            if (it != null) {
                onSuccess(it)
            } else {
                supabase?.let {

                    val userInfo: UserInfo
                    try {
                        userInfo = supabase.auth.retrieveUserForCurrentSession()
                    } catch (e: AuthRestException) {
                        log(e)
                        onFailure()
                        return@collect
                    } catch (e: Exception) {
                        log(e)
                        onFailure()
                        return@collect
                    }
                    val userUUID = UUID.nameUUIDFromBytes(userInfo.id.toByteArray())

                    val userRetrieved: User?

                    try {
                        userRetrieved = supabase.from("users").select {
                            filter {
                                eq("uid", userUUID)
                            }
                        }.decodeSingle<User>()

                    } catch (e: Exception) {
                        toastShort(context, "Ошибка")
                        log(e)
                        onFailure()
                        return@let
                    }

                    onSuccess(userRetrieved)
                }
            }
        }
    }
}