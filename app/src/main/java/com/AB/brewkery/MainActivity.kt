package com.AB.brewkery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.AB.brewkery.UI.cart.CartScreen
import com.AB.brewkery.UI.cart.CartViewModel
import com.AB.brewkery.UI.detail.ItemDetailScreen
import com.AB.brewkery.UI.menu.MenuScreen
import com.AB.brewkery.UI.menu.MenuUiState
import com.AB.brewkery.UI.menu.MenuViewModel
import com.AB.brewkery.UI.status.OrderStatusScreen
import com.AB.brewkery.domain.PriceCalculator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BrewkeryApp()
            }
        }
    }
}

@Composable
fun BrewkeryApp() {
    val navController = rememberNavController()
    val menuVm: MenuViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    val menuState by menuVm.uiState.collectAsStateWithLifecycle()
    val cartItems by cartVm.items.collectAsStateWithLifecycle()
    val placedOrder by cartVm.placedOrder.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "menu") {

        composable("menu") {
            MenuScreen(
                viewModel = menuVm,
                onItemClick = { id -> navController.navigate("detail/$id") },
                cartCount = cartItems.sumOf { it.quantity },
                cartTotal = PriceCalculator.round2(cartItems.sumOf { it.lineTotal }),
                onCartClick = { navController.navigate("cart") },
                activeTicketId = placedOrder?.ticketId
            )
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            val data = (menuState as? MenuUiState.Success)?.data
            val item = data?.items?.find { it.id == id }
            if (data != null && item != null) {
                ItemDetailScreen(
                    item = item,
                    symbol = data.meta.currencySymbol,
                    onBack = { navController.popBackStack() },
                    onAddToCart = {
                        cartVm.add(it)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("cart") {
            val meta = (menuState as? MenuUiState.Success)?.data?.meta
            if (meta != null) {
                CartScreen(
                    items = cartItems,
                    meta = meta,
                    onQuantityChange = cartVm::changeQuantity,
                    onRemove = cartVm::remove,
                    onPlaceOrder = {
                        cartVm.placeOrder()
                        navController.navigate("status") { popUpTo("menu") }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable("status") {
            val meta = (menuState as? MenuUiState.Success)?.data?.meta
            val order = placedOrder
            if (meta != null && order != null) {
                OrderStatusScreen(
                    order = order,
                    estimatedWait = meta.estimatedDeliveryTime,
                    onBackToMenu = { navController.popBackStack("menu", inclusive = false) }
                )
            }
        }
    }
}