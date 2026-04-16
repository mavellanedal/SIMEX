package com.mygdx.primelogistics

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport

class BoatCatchGame @JvmOverloads constructor(
    private val manejadorNavegacion: GameNavigationHandler? = null
) : ApplicationAdapter() {

    private lateinit var loteSprites: SpriteBatch
    private lateinit var renderizadorFormas: ShapeRenderer
    private lateinit var camara: OrthographicCamera
    private lateinit var vista: Viewport

    private lateinit var texturaBarco: Texture
    private lateinit var texturaPaquete: Texture
    private lateinit var texturaAgua: Texture

    private var fuenteMarcador: BitmapFont? = null
    private var fuenteInfo: BitmapFont? = null
    private var fuenteTitulo: BitmapFont? = null
    private var fuentePuntuacion: BitmapFont? = null
    private var fuenteBoton: BitmapFont? = null
    private lateinit var disposicionTexto: GlyphLayout

    private lateinit var limitesBarco: Rectangle
    private lateinit var limitesBotonVolver: Rectangle
    private lateinit var paquetesCayendo: Array<PaqueteCayendo>
    private lateinit var puntoToque: Vector3

    private var anchoMundo = 0f
    private var altoMundo = 0f

    private var altoAgua = 0f
    private var anchoBarco = 0f
    private var altoBarco = 0f
    private var tamanoPaquete = 0f
    private var velocidadBarco = 0f
    private var velocidadMinimaPaquete = 0f
    private var velocidadMaximaPaquete = 0f
    private var margenAparicion = 0f
    private var desplazamientoYBarco = 0f
    private var desbordeInferiorAgua = 0f
    private var rellenoMarcador = 0f
    private var separacionLineasMarcador = 0f
    private var anchoPanel = 0f
    private var altoPanel = 0f
    private var anchoBoton = 0f
    private var altoBoton = 0f
    private var panelMarcadorX = 0f
    private var panelMarcadorY = 0f
    private var anchoPanelMarcador = 0f
    private var altoPanelMarcador = 0f
    private var radioPanelMarcador = 0f
    private var radioPanel = 0f
    private var radioBoton = 0f

    private var temporizadorAparicion = 0f
    private var desplazamientoAgua = 0f
    private var corrienteAgua = 0f
    private var tiempoRestante = 0f

    private var partidaTerminada = false
    private var puntuacion = 0

    override fun create() {
        loteSprites = SpriteBatch()
        renderizadorFormas = ShapeRenderer()
        camara = OrthographicCamera()
        vista = ScreenViewport(camara)

        texturaBarco = Texture("images/boat.png")
        texturaPaquete = Texture("images/package.png")
        texturaAgua = Texture("images/water1.png").apply {
            setWrap(TextureWrap.Repeat, TextureWrap.Repeat)
        }

        disposicionTexto = GlyphLayout()
        limitesBarco = Rectangle()
        limitesBotonVolver = Rectangle()
        paquetesCayendo = Array()
        puntoToque = Vector3()

        tiempoRestante = SEGUNDOS_DURACION_PARTIDA
        puntuacion = 0
        partidaTerminada = false
        temporizadorAparicion = 0f
        desplazamientoAgua = 0f
        corrienteAgua = 0f

        resize(Gdx.graphics.width, Gdx.graphics.height)

        repeat(3) { indice ->
            generarPaquete(indice * 0.18f * altoMundo)
        }
    }

    override fun render() {
        val delta = minOf(Gdx.graphics.deltaTime, DELTA_MAXIMO)

        if (partidaTerminada) {
            gestionarToqueFinPartida()
        } else {
            actualizarPartida(delta)
        }

        renderizarPartida()
    }

    private fun actualizarPartida(delta: Float) {
        tiempoRestante = Math.max(0f, tiempoRestante - delta)
        if (tiempoRestante <= 0f) {
            finalizarPartida()
            return
        }

        val desplazamientoBarco = moverBarca(delta)
        actualizarMovimientoAgua(desplazamientoBarco, delta)

        temporizadorAparicion += delta
        if (temporizadorAparicion >= INTERVALO_APARICION) {
            temporizadorAparicion -= INTERVALO_APARICION
            generarPaquete(0f)
        }

        for (indice in paquetesCayendo.size - 1 downTo 0) {
            val paqueteActual = paquetesCayendo[indice]
            paqueteActual.limites.y -= paqueteActual.velocidad * delta

            if (paqueteActual.limites.overlaps(limitesBarco)) {
                puntuacion++
                paquetesCayendo.removeIndex(indice)
            } else if (paqueteActual.limites.y + paqueteActual.limites.height <= altoAgua) {
                paquetesCayendo.removeIndex(indice)
            }
        }
    }

    private fun finalizarPartida() {
        partidaTerminada = true
        paquetesCayendo.clear()
    }

    private fun gestionarToqueFinPartida() {
        if (!Gdx.input.justTouched()) {
            return
        }

        puntoToque.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        vista.unproject(puntoToque)

        if (limitesBotonVolver.contains(puntoToque.x, puntoToque.y)) {
            if (manejadorNavegacion != null) {
                manejadorNavegacion.returnToLogin()
            } else {
                Gdx.app.exit()
            }
        }
    }

    private fun moverBarca(delta: Float): Float {
        val posicionXAnterior = limitesBarco.x
        val movimiento = leerInclinacionHorizontal() * velocidadBarco * delta

        limitesBarco.x += movimiento

        if (Gdx.input.isTouched) {
            puntoToque.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            vista.unproject(puntoToque)
            limitesBarco.x = puntoToque.x - limitesBarco.width / 2f
        }

        limitesBarco.x = MathUtils.clamp(limitesBarco.x, 0f, anchoMundo - limitesBarco.width)
        return limitesBarco.x - posicionXAnterior
    }

    private fun leerInclinacionHorizontal(): Float {
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            return 0f
        }

        val inclinacionHorizontal = -Gdx.input.accelerometerX / 4.5f
        return MathUtils.clamp(inclinacionHorizontal, -1f, 1f)
    }

    private fun actualizarMovimientoAgua(desplazamientoBarco: Float, delta: Float) {
        if (anchoMundo <= 0f) {
            return
        }

        corrienteAgua += (desplazamientoBarco / anchoMundo) * 12f
        corrienteAgua *= MathUtils.clamp(1f - delta * 3f, 0f, 1f)
        desplazamientoAgua += corrienteAgua * delta
    }

    private fun generarPaquete(extraDesplazamientoY: Float) {
        val posicionXAparicion = MathUtils.random(margenAparicion, anchoMundo - tamanoPaquete - margenAparicion)
        val posicionYAparicion = altoMundo + MathUtils.random(altoMundo * 0.02f, altoMundo * 0.11f) + extraDesplazamientoY
        val velocidadPaquete = MathUtils.random(velocidadMinimaPaquete, velocidadMaximaPaquete)

        paquetesCayendo.add(PaqueteCayendo(posicionXAparicion, posicionYAparicion, tamanoPaquete, velocidadPaquete))
    }

    private fun renderizarPartida() {
        ScreenUtils.clear(COLOR_FONDO_APP)

        loteSprites.projectionMatrix = camara.combined
        loteSprites.begin()
        renderizarAgua()
        renderizarPaquetes()
        renderizarBarca()
        loteSprites.end()

        renderizarFondoMarcador()

        loteSprites.projectionMatrix = camara.combined
        loteSprites.begin()
        renderizarMarcador()
        loteSprites.end()

        if (partidaTerminada) {
            renderizarPanelFinal()
        }
    }

    private fun renderizarAgua() {
        loteSprites.draw(
            texturaAgua,
            0f,
            -desbordeInferiorAgua,
            anchoMundo,
            altoAgua + desbordeInferiorAgua,
            desplazamientoAgua,
            1f,
            desplazamientoAgua + TRAMO_HORIZONTAL_AGUA,
            0f
        )
    }

    private fun renderizarPaquetes() {
        for (paqueteActual in paquetesCayendo) {
            loteSprites.draw(
                texturaPaquete,
                paqueteActual.limites.x,
                paqueteActual.limites.y,
                paqueteActual.limites.width,
                paqueteActual.limites.height
            )
        }
    }

    private fun renderizarBarca() {
        loteSprites.draw(texturaBarco, limitesBarco.x, limitesBarco.y, limitesBarco.width, limitesBarco.height)
    }

    private fun renderizarMarcador() {
        val textoPuntuacion = "Puntuacion: $puntuacion"
        val textoTiempo = "Tiempo: ${MathUtils.ceil(tiempoRestante)}s"
        val posicionYPuntuacion = panelMarcadorY + altoPanelMarcador - (rellenoMarcador * 0.95f)
        val posicionYTiempo = posicionYPuntuacion
        val posicionYPista = panelMarcadorY + (rellenoMarcador * 1.55f)

        fuenteMarcador!!.color = COLOR_FONDO_APP
        fuenteMarcador!!.draw(loteSprites, textoPuntuacion, panelMarcadorX + (rellenoMarcador * 0.75f), posicionYPuntuacion)

        disposicionTexto.setText(fuenteMarcador, textoTiempo)
        fuenteMarcador!!.draw(
            loteSprites,
            disposicionTexto,
            panelMarcadorX + anchoPanelMarcador - disposicionTexto.width - (rellenoMarcador * 0.75f),
            posicionYTiempo
        )

        fuenteInfo!!.color = COLOR_FONDO_APP
        fuenteInfo!!.draw(loteSprites, "Inclina el movil para mover la barca", panelMarcadorX + (rellenoMarcador * 0.75f), posicionYPista)
    }

    private fun renderizarFondoMarcador() {
        renderizadorFormas.projectionMatrix = camara.combined
        renderizadorFormas.begin(ShapeRenderer.ShapeType.Filled)

        pintarRectanguloRedondeado(
            panelMarcadorX + (rellenoMarcador * 0.16f),
            panelMarcadorY - (rellenoMarcador * 0.16f),
            anchoPanelMarcador,
            altoPanelMarcador,
            radioPanelMarcador,
            Color(COLOR_SECUNDARIO_APP.r, COLOR_SECUNDARIO_APP.g, COLOR_SECUNDARIO_APP.b, 0.34f)
        )

        pintarRectanguloRedondeado(
            panelMarcadorX,
            panelMarcadorY,
            anchoPanelMarcador,
            altoPanelMarcador,
            radioPanelMarcador,
            COLOR_PRINCIPAL_APP
        )

        renderizadorFormas.end()
    }

    private fun renderizarPanelFinal() {
        val posicionXPanel = (anchoMundo - anchoPanel) / 2f
        val posicionYPanel = (altoMundo - altoPanel) / 2f
        val posicionXCabecera = posicionXPanel + (anchoPanel * 0.08f)
        val posicionYCabecera = posicionYPanel + (altoPanel * 0.73f)
        val anchuraCabecera = anchoPanel * 0.84f
        val alturaCabecera = altoPanel * 0.16f

        renderizadorFormas.projectionMatrix = camara.combined
        renderizadorFormas.begin(ShapeRenderer.ShapeType.Filled)

        renderizadorFormas.color = Color(0f, 0f, 0f, 0.42f)
        renderizadorFormas.rect(0f, 0f, anchoMundo, altoMundo)

        pintarRectanguloRedondeado(
            posicionXPanel + (rellenoMarcador * 0.18f),
            posicionYPanel - (rellenoMarcador * 0.18f),
            anchoPanel,
            altoPanel,
            radioPanel,
            Color(COLOR_PRINCIPAL_APP.r, COLOR_PRINCIPAL_APP.g, COLOR_PRINCIPAL_APP.b, 0.18f)
        )

        pintarRectanguloRedondeado(posicionXPanel, posicionYPanel, anchoPanel, altoPanel, radioPanel, COLOR_FONDO_APP)
        pintarRectanguloRedondeado(posicionXCabecera, posicionYCabecera, anchuraCabecera, alturaCabecera, radioPanel * 0.7f, COLOR_PRINCIPAL_APP)

        pintarRectanguloRedondeado(
            limitesBotonVolver.x + (rellenoMarcador * 0.12f),
            limitesBotonVolver.y - (rellenoMarcador * 0.12f),
            limitesBotonVolver.width,
            limitesBotonVolver.height,
            radioBoton,
            Color(COLOR_PRINCIPAL_APP.r, COLOR_PRINCIPAL_APP.g, COLOR_PRINCIPAL_APP.b, 0.22f)
        )

        pintarRectanguloRedondeado(
            limitesBotonVolver.x,
            limitesBotonVolver.y,
            limitesBotonVolver.width,
            limitesBotonVolver.height,
            radioBoton,
            COLOR_DESTACADO_APP
        )

        renderizadorFormas.end()

        loteSprites.projectionMatrix = camara.combined
        loteSprites.begin()
        pintarTextoCentrado(fuenteTitulo!!, "Tiempo Terminado", posicionYCabecera + (alturaCabecera * 0.73f), COLOR_FONDO_APP)
        pintarTextoCentrado(fuenteInfo!!, "Resultado final", posicionYPanel + (altoPanel * 0.58f), COLOR_PRINCIPAL_APP)
        pintarTextoCentrado(fuentePuntuacion!!, puntuacion.toString(), posicionYPanel + (altoPanel * 0.50f), COLOR_TEXTO_APP)
        pintarTextoCentrado(fuenteBoton!!, "Volver al login", limitesBotonVolver.y + (limitesBotonVolver.height * 0.64f), COLOR_FONDO_APP)
        loteSprites.end()
    }

    private fun pintarTextoCentrado(fuenteActual: BitmapFont, texto: String, posicionY: Float, color: Color) {
        val colorAnterior = Color(fuenteActual.color)
        fuenteActual.color = color
        disposicionTexto.setText(fuenteActual, texto)
        fuenteActual.draw(loteSprites, disposicionTexto, (anchoMundo - disposicionTexto.width) / 2f, posicionY)
        fuenteActual.color = colorAnterior
    }

    private fun pintarRectanguloRedondeado(x: Float, y: Float, ancho: Float, alto: Float, radio: Float, color: Color) {
        val radioAjustado = Math.min(radio, Math.min(ancho, alto) / 2f)

        renderizadorFormas.color = color
        renderizadorFormas.rect(x + radioAjustado, y, ancho - (radioAjustado * 2f), alto)
        renderizadorFormas.rect(x, y + radioAjustado, radioAjustado, alto - (radioAjustado * 2f))
        renderizadorFormas.rect(x + ancho - radioAjustado, y + radioAjustado, radioAjustado, alto - (radioAjustado * 2f))
        renderizadorFormas.circle(x + radioAjustado, y + radioAjustado, radioAjustado)
        renderizadorFormas.circle(x + ancho - radioAjustado, y + radioAjustado, radioAjustado)
        renderizadorFormas.circle(x + radioAjustado, y + alto - radioAjustado, radioAjustado)
        renderizadorFormas.circle(x + ancho - radioAjustado, y + alto - radioAjustado, radioAjustado)
    }

    override fun resize(width: Int, height: Int) {
        val anchoMundoAnterior = anchoMundo
        val altoMundoAnterior = altoMundo
        val centroXBarcoAnterior = limitesBarco.x + (limitesBarco.width / 2f)

        vista.update(width, height, true)
        anchoMundo = vista.worldWidth
        altoMundo = vista.worldHeight

        camara.position.set(anchoMundo / 2f, altoMundo / 2f, 0f)
        camara.update()

        if (!::texturaBarco.isInitialized) {
            return
        }

        altoAgua = altoMundo * RELACION_ALTURA_AGUA
        anchoBarco = anchoMundo * RELACION_ANCHURA_BARCO
        altoBarco = anchoBarco * (texturaBarco.height / texturaBarco.width.toFloat())
        tamanoPaquete = anchoMundo * RELACION_TAMANO_PAQUETE
        velocidadBarco = anchoMundo * RELACION_VELOCIDAD_BARCO
        velocidadMinimaPaquete = altoMundo * RELACION_VELOCIDAD_MINIMA_PAQUETE
        velocidadMaximaPaquete = altoMundo * RELACION_VELOCIDAD_MAXIMA_PAQUETE
        margenAparicion = anchoMundo * RELACION_MARGEN_APARICION
        desplazamientoYBarco = altoMundo * RELACION_DESPLAZAMIENTO_Y_BARCO
        desbordeInferiorAgua = altoMundo * RELACION_DESBORDE_INFERIOR_AGUA
        rellenoMarcador = anchoMundo * RELACION_RELLENO_MARCADOR
        separacionLineasMarcador = altoMundo * RELACION_SEPARACION_LINEAS_MARCADOR
        anchoPanel = anchoMundo * RELACION_ANCHURA_PANEL
        altoPanel = altoMundo * RELACION_ALTURA_PANEL
        anchoBoton = anchoMundo * RELACION_ANCHURA_BOTON
        altoBoton = altoMundo * RELACION_ALTURA_BOTON
        anchoPanelMarcador = anchoMundo - rellenoMarcador
        altoPanelMarcador = altoMundo * RELACION_ALTURA_PANEL_MARCADOR
        panelMarcadorX = rellenoMarcador * 0.5f
        panelMarcadorY = altoMundo - altoPanelMarcador - (rellenoMarcador * 0.55f)
        radioPanelMarcador = altoPanelMarcador * 0.22f
        radioPanel = Math.min(anchoPanel, altoPanel) * 0.08f
        radioBoton = altoBoton * 0.28f

        recargarFuentes()

        limitesBarco.setSize(anchoBarco, altoBarco)
        limitesBarco.y = altoAgua - desplazamientoYBarco

        limitesBarco.x = if (anchoMundoAnterior == 0f) {
            (anchoMundo - limitesBarco.width) / 2f
        } else {
            ((centroXBarcoAnterior / anchoMundoAnterior) * anchoMundo) - (limitesBarco.width / 2f)
        }

        limitesBarco.x = MathUtils.clamp(limitesBarco.x, 0f, anchoMundo - limitesBarco.width)

        val posicionXPanel = (anchoMundo - anchoPanel) / 2f
        val posicionYPanel = (altoMundo - altoPanel) / 2f
        val posicionYBoton = posicionYPanel + (altoPanel * 0.06f)

        limitesBotonVolver.set(
            posicionXPanel + (anchoPanel - anchoBoton) / 2f,
            posicionYBoton,
            anchoBoton,
            altoBoton
        )

        if (anchoMundoAnterior > 0f && altoMundoAnterior > 0f) {
            for (paqueteActual in paquetesCayendo) {
                paqueteActual.limites.x = (paqueteActual.limites.x / anchoMundoAnterior) * anchoMundo
                paqueteActual.limites.y = (paqueteActual.limites.y / altoMundoAnterior) * altoMundo
                paqueteActual.limites.width = tamanoPaquete
                paqueteActual.limites.height = tamanoPaquete
                paqueteActual.velocidad = (paqueteActual.velocidad / altoMundoAnterior) * altoMundo
            }
        }
    }

    override fun dispose() {
        loteSprites.dispose()
        renderizadorFormas.dispose()
        texturaBarco.dispose()
        texturaPaquete.dispose()
        texturaAgua.dispose()
        soltarFuentes()
    }

    private fun recargarFuentes() {
        soltarFuentes()
        fuenteMarcador = cargarFuente("fonts/montserrat_bold.ttf", maxOf(36, Math.round(anchoMundo * 0.044f)))
        fuenteInfo = cargarFuente("fonts/montserrat_medium.ttf", maxOf(22, Math.round(anchoMundo * 0.029f)))
        fuenteTitulo = cargarFuente("fonts/montserrat_bold.ttf", maxOf(40, Math.round(anchoMundo * 0.050f)))
        fuentePuntuacion = cargarFuente("fonts/montserrat_bold.ttf", maxOf(58, Math.round(anchoMundo * 0.080f)))
        fuenteBoton = cargarFuente("fonts/montserrat_bold.ttf", maxOf(26, Math.round(anchoMundo * 0.034f)))
    }

    private fun soltarFuentes() {
        fuenteMarcador?.dispose()
        fuenteMarcador = null
        fuenteInfo?.dispose()
        fuenteInfo = null
        fuenteTitulo?.dispose()
        fuenteTitulo = null
        fuentePuntuacion?.dispose()
        fuentePuntuacion = null
        fuenteBoton?.dispose()
        fuenteBoton = null
    }

    private fun cargarFuente(rutaFuente: String, tamanoFuente: Int): BitmapFont {
        val generadorFuente = FreeTypeFontGenerator(Gdx.files.internal(rutaFuente))
        val parametrosFuente = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = tamanoFuente
            minFilter = TextureFilter.Linear
            magFilter = TextureFilter.Linear
        }

        val fuenteGenerada = generadorFuente.generateFont(parametrosFuente)
        generadorFuente.dispose()
        return fuenteGenerada
    }

    private class PaqueteCayendo(
        posicionX: Float,
        posicionY: Float,
        tamano: Float,
        var velocidad: Float
    ) {
        val limites = Rectangle(posicionX, posicionY, tamano, tamano)
    }

    companion object {
        // Proporciones y parametros del juego
        private const val RELACION_ALTURA_AGUA = 0.20f
        private const val RELACION_ANCHURA_BARCO = 220 / 720f
        private const val RELACION_TAMANO_PAQUETE = 88 / 720f
        private const val RELACION_VELOCIDAD_BARCO = 560 / 720f
        private const val RELACION_VELOCIDAD_MINIMA_PAQUETE = 140 / 1280f
        private const val RELACION_VELOCIDAD_MAXIMA_PAQUETE = 200 / 1280f
        private const val RELACION_MARGEN_APARICION = 30 / 720f
        private const val RELACION_DESPLAZAMIENTO_Y_BARCO = 60 / 1280f
        private const val RELACION_DESBORDE_INFERIOR_AGUA = 80 / 1280f
        private const val RELACION_RELLENO_MARCADOR = 24 / 720f
        private const val RELACION_SEPARACION_LINEAS_MARCADOR = 48 / 1280f
        private const val RELACION_ANCHURA_PANEL = 0.76f
        private const val RELACION_ALTURA_PANEL = 0.34f
        private const val RELACION_ANCHURA_BOTON = 0.42f
        private const val RELACION_ALTURA_BOTON = 0.075f
        private const val RELACION_ALTURA_PANEL_MARCADOR = 0.14f
        private const val SEGUNDOS_DURACION_PARTIDA = 30f
        private const val INTERVALO_APARICION = 1.15f
        private const val TRAMO_HORIZONTAL_AGUA = 1.45f
        private const val DELTA_MAXIMO = 1 / 30f

        // Colores inspirados en la app
        private val COLOR_FONDO_APP = Color(0.976f, 0.980f, 0.984f, 1f)
        private val COLOR_PRINCIPAL_APP = Color(0.078f, 0.337f, 0.600f, 1f)
        private val COLOR_SECUNDARIO_APP = Color(0.125f, 0.671f, 0.941f, 1f)
        private val COLOR_DESTACADO_APP = Color(0.992f, 0.502f, 0.212f, 1f)
        private val COLOR_TEXTO_APP = Color(0.035f, 0.047f, 0.031f, 1f)
    }
}
