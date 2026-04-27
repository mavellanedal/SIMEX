package com.mygdx.primelogistics.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.mygdx.primelogistics.android.AgentHomeActivity
import com.mygdx.primelogistics.android.ClientHomeActivity

object HomeNavigator {
    const val CLIENT_ROLE_ID = 2
    const val LOGISTICS_OPERATOR_ROLE_ID = 3

    fun createHomeIntent(context: Context, roleId: Int?): Intent {
        val destination = when (roleId) {
            LOGISTICS_OPERATOR_ROLE_ID -> AgentHomeActivity::class.java
            else -> ClientHomeActivity::class.java
        }

        return Intent(context, destination).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }

    fun navigateToHome(activity: Activity, roleId: Int? = null, finishCurrent: Boolean = true) {
        val sessionManager = SessionManager(activity)
        val resolvedRoleId = roleId ?: sessionManager.getRoleId()
        activity.startActivity(createHomeIntent(activity, resolvedRoleId))

        if (finishCurrent) {
            activity.finish()
        }
    }
}
