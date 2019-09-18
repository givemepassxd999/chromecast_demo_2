package com.sample.demo.chromecast_demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.cast.framework.*

class MainActivity : AppCompatActivity() {
    private var mCastContext: CastContext? = null
    private var mCastStateListener: CastStateListener? = null
    private var mediaRouteMenuItem: MenuItem? = null
    private var mIntroductoryOverlay: IntroductoryOverlay? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCastStateListener = CastStateListener { newState ->
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                mediaRouteMenuItem.run {
                    mIntroductoryOverlay?.remove()
                    mIntroductoryOverlay = IntroductoryOverlay.Builder(this@MainActivity, mediaRouteMenuItem)
                        .setTitleText(getString(R.string.introducing_cast))
                        .setOverlayColor(R.color.primary)
                        .setOnOverlayDismissedListener { mIntroductoryOverlay = null }
                        .build()
                }
                mIntroductoryOverlay?.show()
            }
        }
        mCastContext = CastContext.getSharedInstance(this)
    }

    override fun onResume() {
        super.onResume()
        mCastContext?.addCastStateListener(mCastStateListener)
    }

    override fun onPause() {
        mCastContext?.removeCastStateListener(mCastStateListener)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.player, menu)
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)

        return true
    }
}
