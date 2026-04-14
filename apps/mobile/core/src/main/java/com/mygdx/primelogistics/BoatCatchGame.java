package com.mygdx.primelogistics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BoatCatchGame extends ApplicationAdapter {
    private static final float WATER_HEIGHT_RATIO = 0.20f;
    private static final float BOAT_WIDTH_RATIO = 220f / 720f;
    private static final float PACKAGE_SIZE_RATIO = 88f / 720f;
    private static final float BOAT_SPEED_RATIO = 560f / 720f;
    private static final float MIN_PACKAGE_SPEED_RATIO = 140f / 1280f;
    private static final float MAX_PACKAGE_SPEED_RATIO = 200f / 1280f;
    private static final float SPAWN_MARGIN_RATIO = 30f / 720f;
    private static final float BOAT_Y_OFFSET_RATIO = 60f / 1280f;
    private static final float WATER_BOTTOM_OVERFLOW_RATIO = 80f / 1280f;
    private static final float HUD_PADDING_RATIO = 24f / 720f;
    private static final float HUD_LINE_GAP_RATIO = 48f / 1280f;
    private static final float PANEL_WIDTH_RATIO = 0.76f;
    private static final float PANEL_HEIGHT_RATIO = 0.26f;
    private static final float BUTTON_WIDTH_RATIO = 0.42f;
    private static final float BUTTON_HEIGHT_RATIO = 0.075f;
    private static final float GAME_DURATION_SECONDS = 60f;
    private static final float SPAWN_INTERVAL = 1.15f;
    private static final float WATER_U_SPAN = 1.45f;

    private final GameNavigationHandler navigationHandler;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture boatTexture;
    private Texture packageTexture;
    private Texture waterTexture;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Rectangle boatBounds;
    private Rectangle returnButtonBounds;
    private Array<FallingPackage> fallingPackages;
    private Vector3 touchPoint;
    private float worldWidth;
    private float worldHeight;
    private float waterHeight;
    private float boatWidth;
    private float boatHeight;
    private float packageSize;
    private float boatSpeed;
    private float minPackageSpeed;
    private float maxPackageSpeed;
    private float spawnMargin;
    private float boatYOffset;
    private float waterBottomOverflow;
    private float hudPadding;
    private float hudLineGap;
    private float panelWidth;
    private float panelHeight;
    private float buttonWidth;
    private float buttonHeight;
    private float spawnTimer;
    private float waterScroll;
    private float waterCurrent;
    private float timeLeft;
    private boolean gameOver;
    private int score;

    public BoatCatchGame() {
        this(null);
    }

    public BoatCatchGame(GameNavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        boatTexture = new Texture("images/boat.png");
        packageTexture = new Texture("images/package.png");
        waterTexture = new Texture("images/water1.png");
        waterTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();

        boatBounds = new Rectangle();
        returnButtonBounds = new Rectangle();
        fallingPackages = new Array<>();
        touchPoint = new Vector3();
        timeLeft = GAME_DURATION_SECONDS;

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < 3; i++) {
            spawnPackage(i * 0.18f * worldHeight);
        }
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);

        if (gameOver) {
            handleGameOverInput();
        } else {
            updateGame(delta);
        }

        drawGame();
    }

    private void updateGame(float delta) {
        timeLeft = Math.max(0f, timeLeft - delta);
        if (timeLeft <= 0f) {
            endGame();
            return;
        }

        float boatShift = moveBoat(delta);
        updateWater(boatShift, delta);

        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer -= SPAWN_INTERVAL;
            spawnPackage(0f);
        }

        for (int i = fallingPackages.size - 1; i >= 0; i--) {
            FallingPackage fallingPackage = fallingPackages.get(i);
            fallingPackage.bounds.y -= fallingPackage.speed * delta;

            if (fallingPackage.bounds.overlaps(boatBounds)) {
                score++;
                fallingPackages.removeIndex(i);
                continue;
            }

            if (fallingPackage.bounds.y <= waterHeight) {
                fallingPackages.removeIndex(i);
            }
        }
    }

    private void endGame() {
        gameOver = true;
        fallingPackages.clear();
    }

    private void handleGameOverInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(touchPoint);
        if (returnButtonBounds.contains(touchPoint.x, touchPoint.y)) {
            if (navigationHandler != null) {
                navigationHandler.returnToLogin();
            } else {
                Gdx.app.exit();
            }
        }
    }

    private float moveBoat(float delta) {
        float previousX = boatBounds.x;
        float movement = getTiltMovement() * boatSpeed * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            movement -= boatSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            movement += boatSpeed * delta;
        }

        boatBounds.x += movement;

        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            viewport.unproject(touchPoint);
            boatBounds.x = touchPoint.x - boatBounds.width / 2f;
        }

        boatBounds.x = MathUtils.clamp(boatBounds.x, 0f, worldWidth - boatBounds.width);
        return boatBounds.x - previousX;
    }

    private float getTiltMovement() {
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            return 0f;
        }

        float horizontalTilt = Gdx.input.getAccelerometerX() / 4.5f;
        return MathUtils.clamp(horizontalTilt, -1f, 1f);
    }

    private void updateWater(float boatShift, float delta) {
        if (worldWidth <= 0f) {
            return;
        }

        waterCurrent += (boatShift / worldWidth) * 12f;
        waterCurrent *= MathUtils.clamp(1f - delta * 3f, 0f, 1f);
        waterScroll += waterCurrent * delta;
    }

    private void spawnPackage(float verticalOffset) {
        float spawnX = MathUtils.random(spawnMargin, worldWidth - packageSize - spawnMargin);
        float spawnY = worldHeight + MathUtils.random(worldHeight * 0.02f, worldHeight * 0.11f) + verticalOffset;
        float speed = MathUtils.random(minPackageSpeed, maxPackageSpeed);
        fallingPackages.add(new FallingPackage(spawnX, spawnY, packageSize, speed));
    }

    private void drawGame() {
        ScreenUtils.clear(0.70f, 0.88f, 0.98f, 1f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(
            waterTexture,
            0f,
            -waterBottomOverflow,
            worldWidth,
            waterHeight + waterBottomOverflow,
            waterScroll,
            1f,
            waterScroll + WATER_U_SPAN,
            0f
        );

        for (FallingPackage fallingPackage : fallingPackages) {
            batch.draw(
                packageTexture,
                fallingPackage.bounds.x,
                fallingPackage.bounds.y,
                fallingPackage.bounds.width,
                fallingPackage.bounds.height
            );
        }

        batch.draw(boatTexture, boatBounds.x, boatBounds.y, boatBounds.width, boatBounds.height);
        font.draw(batch, "Score: " + score, hudPadding, worldHeight - hudPadding);
        font.draw(batch, "Time: " + MathUtils.ceil(timeLeft), hudPadding, worldHeight - hudPadding - hudLineGap);
        font.draw(batch, "Tilt phone left/right to move", hudPadding, worldHeight - hudPadding - (hudLineGap * 2f));
        batch.end();

        if (gameOver) {
            drawGameOverOverlay();
        }
    }

    private void drawGameOverOverlay() {
        float panelX = (worldWidth - panelWidth) / 2f;
        float panelY = (worldHeight - panelHeight) / 2f;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.42f);
        shapeRenderer.rect(0f, 0f, worldWidth, worldHeight);
        shapeRenderer.setColor(0.96f, 0.98f, 1f, 1f);
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
        shapeRenderer.setColor(0.09f, 0.45f, 0.70f, 1f);
        shapeRenderer.rect(returnButtonBounds.x, returnButtonBounds.y, returnButtonBounds.width, returnButtonBounds.height);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawCenteredText("Time's Up!", panelY + (panelHeight * 0.80f), Color.BLACK);
        drawCenteredText("Final Score: " + score, panelY + (panelHeight * 0.56f), Color.DARK_GRAY);
        drawCenteredText("Return to Login", returnButtonBounds.y + (returnButtonBounds.height * 0.66f), Color.WHITE);
        batch.end();
    }

    private void drawCenteredText(String text, float y, Color color) {
        Color previousColor = font.getColor().cpy();
        font.setColor(color);
        glyphLayout.setText(font, text);
        font.draw(batch, glyphLayout, (worldWidth - glyphLayout.width) / 2f, y);
        font.setColor(previousColor);
    }

    @Override
    public void resize(int width, int height) {
        float previousWorldWidth = worldWidth;
        float previousWorldHeight = worldHeight;
        float previousBoatCenterX = boatBounds.x + (boatBounds.width / 2f);

        viewport.update(width, height, true);
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0f);
        camera.update();

        waterHeight = worldHeight * WATER_HEIGHT_RATIO;
        boatWidth = worldWidth * BOAT_WIDTH_RATIO;
        boatHeight = boatWidth * (boatTexture.getHeight() / (float) boatTexture.getWidth());
        packageSize = worldWidth * PACKAGE_SIZE_RATIO;
        boatSpeed = worldWidth * BOAT_SPEED_RATIO;
        minPackageSpeed = worldHeight * MIN_PACKAGE_SPEED_RATIO;
        maxPackageSpeed = worldHeight * MAX_PACKAGE_SPEED_RATIO;
        spawnMargin = worldWidth * SPAWN_MARGIN_RATIO;
        boatYOffset = worldHeight * BOAT_Y_OFFSET_RATIO;
        waterBottomOverflow = worldHeight * WATER_BOTTOM_OVERFLOW_RATIO;
        hudPadding = worldWidth * HUD_PADDING_RATIO;
        hudLineGap = worldHeight * HUD_LINE_GAP_RATIO;
        panelWidth = worldWidth * PANEL_WIDTH_RATIO;
        panelHeight = worldHeight * PANEL_HEIGHT_RATIO;
        buttonWidth = worldWidth * BUTTON_WIDTH_RATIO;
        buttonHeight = worldHeight * BUTTON_HEIGHT_RATIO;
        font.getData().setScale(Math.max(1.3f, worldWidth / 480f));

        boatBounds.setSize(boatWidth, boatHeight);
        boatBounds.y = waterHeight - boatYOffset;

        if (previousWorldWidth == 0f) {
            boatBounds.x = (worldWidth - boatBounds.width) / 2f;
        } else {
            float centerRatio = previousBoatCenterX / previousWorldWidth;
            boatBounds.x = (centerRatio * worldWidth) - (boatBounds.width / 2f);
        }
        boatBounds.x = MathUtils.clamp(boatBounds.x, 0f, worldWidth - boatBounds.width);

        returnButtonBounds.set(
            (worldWidth - buttonWidth) / 2f,
            (worldHeight - panelHeight) / 2f + (panelHeight * 0.15f),
            buttonWidth,
            buttonHeight
        );

        if (previousWorldWidth > 0f && previousWorldHeight > 0f) {
            for (FallingPackage fallingPackage : fallingPackages) {
                fallingPackage.bounds.x = (fallingPackage.bounds.x / previousWorldWidth) * worldWidth;
                fallingPackage.bounds.y = (fallingPackage.bounds.y / previousWorldHeight) * worldHeight;
                fallingPackage.bounds.width = packageSize;
                fallingPackage.bounds.height = packageSize;
                fallingPackage.speed = (fallingPackage.speed / previousWorldHeight) * worldHeight;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        boatTexture.dispose();
        packageTexture.dispose();
        waterTexture.dispose();
        font.dispose();
    }

    private static class FallingPackage {
        private final Rectangle bounds;
        private float speed;

        private FallingPackage(float x, float y, float size, float speed) {
            this.bounds = new Rectangle(x, y, size, size);
            this.speed = speed;
        }
    }
}
