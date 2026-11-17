package com.sebas.destinoscolombia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebas.destinoscolombia.ui.theme.DestinosColombiaTheme

private val Navy = Color(0xFF14213D)
private val Blue = Color(0xFF2563EB)
private val Pale = Color(0xFFF4F6FB)
private val Muted = Color(0xFF687386)

 data class Destino(
    val id: Int,
    val nombre: String,
    val region: String,
    val descripcion: String,
    val emoji: String,
    val color: Color,
    val actividades: List<String>,
    val dato: String,
    val favorito: Boolean = false
)

private val destinosBase = listOf(
    Destino(1, "Cartagena", "Caribe colombiano", "Ciudad amurallada con calles coloniales, plazas llenas de color y atardeceres frente al mar.", "🏰", Color(0xFFFFE4C7), listOf("Recorrer el centro histórico", "Caminar sobre las murallas", "Visitar las Islas del Rosario"), "Historia, arquitectura y playas"),
    Destino(2, "Guatapé", "Antioquia", "Pueblo de zócalos de colores junto a un embalse enorme y la imponente Piedra del Peñol.", "⛰️", Color(0xFFDCEAFE), listOf("Subir los escalones de la Piedra", "Paseo en lancha por el embalse", "Pasear por el pueblo"), "Paisajes, agua y cultura"),
    Destino(3, "Salento", "Eje Cafetero", "Entrada al Valle del Cocora, hogar de las palmas de cera más altas del mundo.", "🌴", Color(0xFFDDF4E5), listOf("Caminata por el Valle del Cocora", "Tour por una finca cafetera", "Probar trucha local"), "Naturaleza y tradición cafetera"),
    Destino(4, "Parque Tayrona", "Magdalena · Caribe", "Selva que se encuentra con el mar: playas de arena clara y senderos entre la vegetación.", "🌊", Color(0xFFD9F2F2), listOf("Caminar hasta Cabo San Juan", "Disfrutar las playas autorizadas", "Observar fauna y aves"), "Selva, mar y biodiversidad"),
    Destino(5, "San Andrés", "Región Insular", "Isla del mar de siete colores, ideal para explorar sus arrecifes y disfrutar del paisaje.", "🐠", Color(0xFFD9ECFF), listOf("Practicar careteo en zonas autorizadas", "Visitar Johnny Cay", "Recorrer la isla"), "Mar de siete colores"),
    Destino(6, "Medellín", "Antioquia", "Ciudad de la eterna primavera, rodeada de montañas, con metro, cable aéreo y mucha cultura.", "🚡", Color(0xFFF0E2FF), listOf("Subir en el Metrocable", "Conocer la Comuna 13", "Pasear por el Parque Arví"), "Cultura, innovación y montañas")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DestinosColombiaTheme { DestinosApp() } }
    }
}

@Composable
private fun DestinosApp() {
    val destinos = remember { mutableStateListOf<Destino>().apply { addAll(destinosBase) } }
    var tab by remember { mutableStateOf("Inicio") }
    var seleccionado by remember { mutableStateOf<Destino?>(null) }
    var busqueda by remember { mutableStateOf("") }

    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Pale, surface = Color.White)) {
        if (seleccionado != null) {
            DetalleScreen(
                destino = destinos.first { it.id == seleccionado!!.id },
                onBack = { seleccionado = null },
                onFavorite = { id ->
                    val index = destinos.indexOfFirst { it.id == id }
                    if (index >= 0) destinos[index] = destinos[index].copy(favorito = !destinos[index].favorito)
                }
            )
        } else {
            Scaffold(
                containerColor = Pale,
                bottomBar = {
                    NavigationBar(containerColor = Color.White) {
                        NavigationBarItem(selected = tab == "Inicio", onClick = { tab = "Inicio" }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") })
                        NavigationBarItem(selected = tab == "Explorar", onClick = { tab = "Explorar" }, icon = { Icon(Icons.Default.Search, null) }, label = { Text("Explorar") })
                        NavigationBarItem(selected = tab == "Favoritos", onClick = { tab = "Favoritos" }, icon = { Icon(Icons.Default.Favorite, null) }, label = { Text("Favoritos") })
                    }
                }
            ) { padding ->
                when (tab) {
                    "Inicio" -> HomeScreen(destinos, padding, onSelect = { seleccionado = it }, onFavorite = { id ->
                        val i = destinos.indexOfFirst { it.id == id }; if (i >= 0) destinos[i] = destinos[i].copy(favorito = !destinos[i].favorito)
                    }, onExplore = { tab = "Explorar" })
                    "Explorar" -> ExploreScreen(destinos, busqueda, { busqueda = it }, padding, { seleccionado = it }, { id ->
                        val i = destinos.indexOfFirst { it.id == id }; if (i >= 0) destinos[i] = destinos[i].copy(favorito = !destinos[i].favorito)
                    })
                    else -> FavoritesScreen(destinos.filter { it.favorito }, padding, { seleccionado = it }, { id ->
                        val i = destinos.indexOfFirst { it.id == id }; if (i >= 0) destinos[i] = destinos[i].copy(favorito = !destinos[i].favorito)
                    })
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(destinos: List<Destino>, padding: PaddingValues, onSelect: (Destino) -> Unit, onFavorite: (Int) -> Unit, onExplore: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 20.dp)) {
        item {
            Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 22.dp, vertical = 22.dp)) {
                Text("DESTINOS COLOMBIA", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(Modifier.height(12.dp))
                Text("Descubre lo mejor\nde nuestro país 🇨🇴", fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                Spacer(Modifier.height(8.dp))
                Text("Lugares increíbles, experiencias únicas y paisajes que enamoran.", color = Muted, fontSize = 14.sp)
                Spacer(Modifier.height(18.dp))
                Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Brush.linearGradient(listOf(Color(0xFF153E75), Color(0xFF3687C8)))).padding(20.dp)) {
                    Column {
                        Text("TU PRÓXIMA AVENTURA", color = Color.White.copy(alpha = .75f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Colombia tiene\nmucho por explorar", color = Color.White, fontSize = 23.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Navy)) { Text("Explorar destinos") }
                    }
                }
                Spacer(Modifier.height(22.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Destinos destacados", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Spacer(Modifier.weight(1f)); Text("${destinos.size} lugares", color = Muted, fontSize = 12.sp)
                }
            }
        }
        items(destinos, key = { it.id }) { destino -> DestinoCard(destino, onSelect, onFavorite) }
    }
}

@Composable
private fun ExploreScreen(destinos: List<Destino>, query: String, onQuery: (String) -> Unit, padding: PaddingValues, onSelect: (Destino) -> Unit, onFavorite: (Int) -> Unit) {
    val filtrados = destinos.filter { it.nombre.contains(query, true) || it.region.contains(query, true) || it.descripcion.contains(query, true) }
    Column(Modifier.fillMaxSize().padding(padding)) {
        Column(Modifier.background(Color.White).fillMaxWidth().padding(20.dp)) {
            Text("Explorar", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Text("Encuentra tu próximo destino", color = Muted)
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Buscar por lugar o región") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true, shape = RoundedCornerShape(16.dp))
        }
        if (filtrados.isEmpty()) EmptyState("No encontramos destinos", "Prueba con otro nombre o región.")
        else LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 14.dp)) { items(filtrados, key = { it.id }) { DestinoCard(it, onSelect, onFavorite) } }
    }
}

@Composable
private fun FavoritesScreen(favoritos: List<Destino>, padding: PaddingValues, onSelect: (Destino) -> Unit, onFavorite: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding)) {
        Column(Modifier.fillMaxWidth().background(Color.White).padding(22.dp)) {
            Text("Mis favoritos", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Text("Tus lugares guardados para después", color = Muted)
        }
        if (favoritos.isEmpty()) EmptyState("Aún no tienes favoritos", "Pulsa el corazón de un destino para guardarlo aquí.")
        else LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 14.dp)) { items(favoritos, key = { it.id }) { DestinoCard(it, onSelect, onFavorite) } }
    }
}

@Composable
private fun DestinoCard(destino: Destino, onSelect: (Destino) -> Unit, onFavorite: (Int) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 7.dp).clickable { onSelect(destino) }, shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(82.dp).clip(RoundedCornerShape(18.dp)).background(destino.color), contentAlignment = Alignment.Center) { Text(destino.emoji, fontSize = 38.sp) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(destino.region.uppercase(), color = Blue, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(destino.nombre, color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(destino.descripcion, color = Muted, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp)
            }
            IconButton(onClick = { onFavorite(destino.id) }) { Icon(if (destino.favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorito", tint = if (destino.favorito) Color(0xFFE34B67) else Muted) }
        }
    }
}

@Composable
private fun DetalleScreen(destino: Destino, onBack: () -> Unit, onFavorite: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().background(Pale)) {
        Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Navy) }
            Text("Detalle del destino", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Navy)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onFavorite(destino.id) }) { Icon(if (destino.favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorito", tint = if (destino.favorito) Color(0xFFE34B67) else Muted) }
        }
        Column(Modifier.fillMaxSize().padding(18.dp)) {
            Box(Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(26.dp)).background(Brush.linearGradient(listOf(destino.color, Color.White))), contentAlignment = Alignment.Center) { Text(destino.emoji, fontSize = 92.sp) }
            Spacer(Modifier.height(20.dp))
            Text(destino.region.uppercase(), color = Blue, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.5.sp)
            Text(destino.nombre, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Spacer(Modifier.height(10.dp))
            Text(destino.descripcion, color = Muted, fontSize = 15.sp, lineHeight = 23.sp)
            Spacer(Modifier.height(18.dp))
            Text("¿Qué puedes hacer?", fontSize = 19.sp, color = Navy, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            destino.actividades.forEach { actividad ->
                Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(30.dp).clip(CircleShape).background(Color(0xFFE5EEFF)), contentAlignment = Alignment.Center) { Text("✓", color = Blue, fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.width(10.dp)); Text(actividad, color = Navy, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Explore, null, tint = Blue)
                    Spacer(Modifier.width(10.dp)); Column { Text("Lo que lo hace especial", fontWeight = FontWeight.Bold, color = Navy); Text(destino.dato, color = Muted, fontSize = 13.sp) }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(title: String, message: String) {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🧭", fontSize = 54.sp); Spacer(Modifier.height(12.dp)); Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy); Spacer(Modifier.height(6.dp)); Text(message, color = Muted)
    }
}
