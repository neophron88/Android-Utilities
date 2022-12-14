package org.rasulov.utilities.fragment

import android.graphics.drawable.GradientDrawable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline val Fragment.viewLifeCycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

suspend fun Fragment.repeatOnViewLifeCycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.repeatOnLifecycle(state, block)
}

fun Fragment.repeatWhenViewStarted(block: suspend CoroutineScope.() -> Unit) {
    viewLifeCycleScope.launch { repeatOnViewLifeCycle(Lifecycle.State.STARTED, block) }
}


typealias OnMenuItemSelected = (item: MenuItem) -> Unit
typealias OnCreateMenu = (menu: Menu) -> Unit
typealias OnPrepareMenu = (menu: Menu) -> Unit


fun Fragment.addMenuProvider(
    @MenuRes menuRes: Int,
    onMenuItemSelected: OnMenuItemSelected = {},
    onCreateMenu: OnCreateMenu = {},
    onPrepareMenu: OnPrepareMenu = {}
) {

    val menuProvider = object : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuRes, menu)
            onCreateMenu(menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            onMenuItemSelected(menuItem)
            return true
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            onPrepareMenu(menu)
        }

    }

    requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.STARTED)
}


fun Fragment.getGradientDrawable(@DrawableRes res: Int): GradientDrawable {
    return ContextCompat.getDrawable(requireContext(), res) as GradientDrawable
}

fun Fragment.disableTransitionOverlap() {
    allowEnterTransitionOverlap = false
    allowReturnTransitionOverlap = false
}


