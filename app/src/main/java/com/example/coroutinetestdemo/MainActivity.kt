package com.example.coroutinetestdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    // add the job to cancel the extension method, when the activity run onDestroy.
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job by lazy { Job() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this launch method's receiver contains the job field.
        launch {
            mTvOutput.append(this::class.java.simpleName)
            mTvOutput.append("\n")
            Log.e("splash","scope: ${Thread.currentThread()}")
            // async() change the fun as a Deferred which seems like the suspend function
            val userOne = async(Dispatchers.IO) { fetchFirstUser() }
            val userTwo = async(Dispatchers.IO) { fetchSecondUser() }
            showUser(userOne.await(), userTwo.await())
        }
    }

    private fun fetchFirstUser(): User {
        sleep(1)
        Log.e("splash","fetchFirstUser: ${Thread.currentThread()}")
        return User(1, "first")
    }

    private fun fetchSecondUser(): User {
        sleep(2)
        Log.e("splash","fetchSecondUser: ${Thread.currentThread()}")
        return User(2, "second")
    }

    private fun showUser(user1: User, user2: User) {
        // add a divide by zero to test whether the job.cancel() is invoked.
//        val c = 1 / 0
        mTvOutput.append(user1.toString())
        mTvOutput.append("\n")
        mTvOutput.append(user2.toString())
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
